package com.weblearnex.app.service.impl;

import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.BulkHeaderConstant;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.datatable.reposatory.ConfigrationRepository;
import com.weblearnex.app.datatable.reposatory.SaleOrderRepository;
import com.weblearnex.app.datatable.reposatory.StatusRepository;
import com.weblearnex.app.entity.master.BulkMaster;
import com.weblearnex.app.entity.order.PacketHistory;
import com.weblearnex.app.entity.order.SaleOrder;
import com.weblearnex.app.entity.setup.Configration;
import com.weblearnex.app.entity.setup.Status;
import com.weblearnex.app.model.BulkUploadBean;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.BulkMasterService;
import com.weblearnex.app.service.BulkUploadService;
import com.weblearnex.app.service.PacketHistoryService;
import com.weblearnex.app.service.SaleOrderService;
import com.weblearnex.app.utils.SharedMethords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PacketHistoryServiceImpl implements PacketHistoryService {

    @Autowired
    private SaleOrderRepository saleOrderRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private ConfigrationRepository configrationRepository;

    @Autowired
    private SaleOrderService saleOrderService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    @Qualifier("applicionConfig")
    private MessageSource applicionConfig;

    @Autowired
    private BulkMasterService bulkMasterService;

    @Override
    public BulkUploadBean uploadBulk(BulkUploadBean bulkUploadBean, BulkMaster bulkMaster) {
        List<Map<String, String>> errorRecord = new ArrayList<Map<String, String>>();
        List<Map<String, String>> successRecord = new ArrayList<Map<String, String>>();
        int count = 0;
        int percentage = 0;
        String token = BulkUploadService.generateRandomString();
        for(Map<String, String> map : bulkUploadBean.getRecords()){
            count ++;
            percentage = BulkUploadService.calculateUploadPercentage(count, bulkUploadBean.getRecords().size());
            bulkMasterService.setUploadProgressCount(bulkMaster.getName(), percentage, token, false);

            String awbNub= map.get(BulkHeaderConstant.AWB_NUMBER);
            String statusCode= map.get(BulkHeaderConstant.STATUS_CODE);
            String location= map.get(BulkHeaderConstant.LOCATION);
            String date= map.get(BulkHeaderConstant.DATE);
            String time= map.get(BulkHeaderConstant.TIME);
            String ndrReason = map.get(BulkHeaderConstant.NDR_REASON);

            if(awbNub == null || awbNub.trim().equals("")){
                map.put(BulkHeaderConstant.MESSAGE,"Awb Number is empty.");
                errorRecord.add(map);
                continue;
            }

            if(statusCode == null || statusCode.trim().equals("")){
                map.put(BulkHeaderConstant.MESSAGE,"Status code is empty.");
                errorRecord.add(map);
                continue;
            }
            if(location == null || location.trim().equals("")){
                map.put(BulkHeaderConstant.MESSAGE,"Location name is empty.");
                errorRecord.add(map);
                continue;
            }
            if(date == null || date.trim().equals("")){
                map.put(BulkHeaderConstant.MESSAGE,"Date is empty.");
                errorRecord.add(map);
                continue;
            }
            if(time == null || time.trim().equals("")){
                map.put(BulkHeaderConstant.MESSAGE,"Time is empty.");
                errorRecord.add(map);
                continue;
            }
            SaleOrder  saleOrder = saleOrderRepository.findByReferanceNo(awbNub);
            if(saleOrder==null){
                map.put(BulkHeaderConstant.MESSAGE,"Awb number not found in database.");
                errorRecord.add(map);
                continue;
            }
            Status status = statusRepository.findByStatusCode(statusCode);
            if(status == null){
                map.put(BulkHeaderConstant.MESSAGE,"Status not found in database.");
                errorRecord.add(map);
                continue;

            }

            // TODO if packet status Delivery attempted --> NDR code can not be empty.
            if(ndrReason != null && !ndrReason.trim().equals("")){
                Configration configration = configrationRepository.findByConfigCode(ndrReason);
                if(configration == null){
                    map.put(BulkHeaderConstant.MESSAGE,"Ndr reason not found in database.");
                    errorRecord.add(map);
                    continue;
                }
            }

            String dateTime = date+" "+time;
            Date updateDate = SharedMethords.getDate(dateTime, SharedMethords.DATE_FORMAT);
            if(updateDate == null){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid Date and ime formate. Date (yyyy-mm-dd) Time(hh:mm:ss)");
                errorRecord.add(map);
                continue;
            }
            String deliveredCode = applicionConfig.getMessage(AppProperty.UD_DELIVERED, null, null);
            if(status.getStatusCode().equals(deliveredCode)){
                saleOrder.setDeliveredDate(updateDate);

            }
            PacketHistory currentPacketHistory = saleOrder.getPacketHistory().get(saleOrder.getPacketHistory().size()-1);
            boolean isValideDate = SharedMethords.isValidPacketHistoryDate(currentPacketHistory.getDate(), updateDate);
            if(!isValideDate){
                map.put(BulkHeaderConstant.MESSAGE,"Date must be grater then current packet history date");
                errorRecord.add(map);
                continue;
            }

            ResponseBean responseBean = saleOrderService.addPacketHistory(saleOrder,status,ndrReason,updateDate, location);
            saleOrder.setCurrentStatus(status);
            if(responseBean.getStatus().equals(ResponseStatus.FAIL)){
                map.put(BulkHeaderConstant.MESSAGE, responseBean.getMessage());
                errorRecord.add(map);
                continue;
            }

            successRecord.add(map);
        }
        bulkUploadBean.setBulkMaster(bulkMaster);
        bulkUploadBean.setErrorRecords(errorRecord);
        bulkUploadBean.setSuccessRecords(successRecord);
        bulkMasterService.setUploadProgressCount(bulkMaster.getName(), 100, token, true);
        return bulkUploadBean;
    }

    @Override
    public ResponseBean<PacketHistory> updatePacketStatus(String awbs, String status) {
        ResponseBean responseBean = new ResponseBean();
        try{
            if(awbs==null||awbs.trim().isEmpty()){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Please enter awb number.");
                return responseBean;
            }
            if(status==null||status.trim().isEmpty()){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Please salect status.");
                return responseBean;
            }
            awbs = awbs.replaceAll("\\s", "");
            List<String> awbNumbers = Arrays.asList(awbs.split(","));
            List<SaleOrder>  saleOrderList= saleOrderRepository.findByReferanceNoIn(awbNumbers);

            if (saleOrderList.size()==0) {
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage(messageSource.getMessage(MessageProperty.AWB_NUMBER_NOT_FOUND, null, null));
                return responseBean;
            }
            List<SaleOrder> errorRecord = new ArrayList<SaleOrder>();
            saleOrderList.forEach(saleOrder -> {
                List<PacketHistory> packetHistories = saleOrder.getPacketHistory();
                PacketHistory packetHistory = SharedMethords.getLastPacketHistory(saleOrder);
                if(packetHistory != null && packetHistory.getToStatusCode().equals(status)){
                    Status currentStatus = statusRepository.findByStatusCode(packetHistory.getFromStatusCode());
                    saleOrder.setCurrentStatus(currentStatus);
                    packetHistories.remove(packetHistory);
                    saleOrderRepository.save(saleOrder);
                }else {
                    errorRecord.add(saleOrder);
                    responseBean.setStatus(ResponseStatus.FAIL);
                    responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG,null,null));
                }
            });
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setResponseBody(errorRecord);

        }catch (Exception e){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Server internal error.");
            e.printStackTrace();
        }
        return responseBean;
    }
}
