package com.weblearnex.app.service.impl;

import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.BulkHeaderConstant;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.datatable.reposatory.SaleOrderRepository;
import com.weblearnex.app.entity.master.BulkMaster;
import com.weblearnex.app.entity.master.OrderLBH;
import com.weblearnex.app.entity.order.SaleOrder;
import com.weblearnex.app.model.BulkUploadBean;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.reposatory.AwbSeriesRepository;
import com.weblearnex.app.datatable.reposatory.BulkHeaderRepository;
import com.weblearnex.app.datatable.reposatory.BulkMasterRepository;
import com.weblearnex.app.reposatory.OrderLBHRepository;
import com.weblearnex.app.service.BulkMasterService;
import com.weblearnex.app.service.BulkUploadService;
import com.weblearnex.app.service.OrderLBHService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderLBHServiceImpl implements OrderLBHService {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    AwbSeriesRepository awbSeriesRepository;

    @Autowired
    OrderLBHRepository orderLBHRepository;

    @Autowired
    BulkMasterRepository bulkMasterRepository;

    @Autowired
    BulkHeaderRepository bulkHeaderRepository;

    @Autowired
    private SaleOrderRepository saleOrderRepository;

    @Autowired
    private BulkMasterService bulkMasterService;

    @Override
    public ResponseBean<OrderLBH> addLBH(OrderLBH orderLBH) {
        ResponseBean responseBean = new ResponseBean();
        if (orderLBH.getAwbNumber() == null || orderLBH.getAwbNumber().isEmpty() || saleOrderRepository.findByReferanceNo(orderLBH.getAwbNumber()) !=null ) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.AWB_NUMBER_DOES_NOT_EMPTY_OR_NO_DATA_FOUND_IN_DATABASE, null, null));
            return responseBean;
        }
        if (orderLBH.getClientHeight() <= 0.0d) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_HEIGTH_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (orderLBH.getClientLength() <= 0.0d) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_LENGTH_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (orderLBH.getClientWeight() <= 0.0d) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_WEIGHT_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (orderLBH.getClientWidth() <= 0.0d) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_WIDTH_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }

        orderLBH = orderLBHRepository.save(orderLBH);
        if (orderLBH != null) {
            responseBean.setResponseBody(orderLBH);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.LBH_ADDED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.LBH_ADDED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<OrderLBH> findByOrderLBHId(Long id) {
        ResponseBean responseBean = new ResponseBean();
        Optional<OrderLBH> orderLBH = orderLBHRepository.findById(id);
        if (!orderLBH.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.PAGE_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        responseBean.setResponseBody(orderLBH.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.PAGE_ID_FOUND, null, null));
        return responseBean;
    }

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

            OrderLBH orderLBH = new OrderLBH();
            String awbNub= (map.get(BulkHeaderConstant.AWB_NUMBER) != null && !map.get(BulkHeaderConstant.AWB_NUMBER).isEmpty())
                    ? map.get(BulkHeaderConstant.AWB_NUMBER).trim() : null;

            if(awbNub == null){
                map.put(BulkHeaderConstant.MESSAGE,"Awb number field can not be empty.");
                errorRecord.add(map);
                continue;
            }
            if(!saleOrderRepository.existsSaleOrderByReferanceNo(awbNub)){
                map.put(BulkHeaderConstant.MESSAGE,"Awb number not found in system.");
                errorRecord.add(map);
                continue;
            }
            orderLBH.setAwbNumber(awbNub);

            if(bulkMaster.getName().equals("selfLbhBulk")){
                try{
                    orderLBH.setSelfWeight(map.get(BulkHeaderConstant.SELF_WEIGHT) != null
                            ? Double.parseDouble(map.get(BulkHeaderConstant.SELF_WEIGHT).trim()) : null);
                }catch (Exception e){
                    map.put(BulkHeaderConstant.MESSAGE,"Please enter vailed numeric weight .");
                    errorRecord.add(map);
                    e.printStackTrace();
                    continue;
                }
                try{
                    orderLBH.setSelfLength(map.get(BulkHeaderConstant.SELF_LENGTH) != null
                            ? Double.parseDouble(map.get(BulkHeaderConstant.SELF_LENGTH).trim()) : null);
                }catch (Exception e){
                    map.put(BulkHeaderConstant.MESSAGE,"Please enter vailed numeric length .");
                    errorRecord.add(map);
                    e.printStackTrace();
                    continue;
                }

                try{
                    orderLBH.setSelfWidth(map.get(BulkHeaderConstant.SELF_WIDTH) != null
                            ? Double.parseDouble(map.get(BulkHeaderConstant.SELF_WIDTH).trim()) : null);
                }catch (Exception e){
                    map.put(BulkHeaderConstant.MESSAGE,"Please enter vailed numeric width .");
                    errorRecord.add(map);
                    e.printStackTrace();
                    continue;
                }
                try{
                    orderLBH.setSelfHeight(map.get(BulkHeaderConstant.SELF_HEIGHT) != null
                            ? Double.parseDouble(map.get(BulkHeaderConstant.SELF_HEIGHT).trim()) : null);
                }catch (Exception e){
                    map.put(BulkHeaderConstant.MESSAGE,"Please enter vailed numeric height .");
                    errorRecord.add(map);
                    e.printStackTrace();
                    continue;
                }

                if(orderLBH.getAwbNumber()==null || orderLBH.getAwbNumber().isEmpty()){
                    map.put(BulkHeaderConstant.MESSAGE,"Please enter Awb Number.");
                    errorRecord.add(map);
                    continue;
                }
                if(orderLBH.getSelfWeight()==null ){
                    map.put(BulkHeaderConstant.MESSAGE,"Please enter Weight.");
                    errorRecord.add(map);
                    continue;
                }
                if(orderLBH.getSelfLength()==null ){
                    map.put(BulkHeaderConstant.MESSAGE,"Please enter Length.");
                    errorRecord.add(map);
                    continue;
                }
                if(orderLBH.getSelfWidth()==null ){
                    map.put(BulkHeaderConstant.MESSAGE,"Please enter Width.");
                    errorRecord.add(map);
                    continue;
                }
                if(orderLBH.getSelfHeight()==null ){
                    map.put(BulkHeaderConstant.MESSAGE,"Please enter Height.");
                    errorRecord.add(map);
                    continue;
                }

                OrderLBH  orderLBHDb = orderLBHRepository.findByAwbNumber(orderLBH.getAwbNumber());
                if(orderLBHDb == null){
                    orderLBHRepository.save(orderLBH);
                }else {
                    orderLBHDb.setClientWeight(orderLBH.getSelfWeight());
                    orderLBHDb.setSelfLength(orderLBH.getSelfLength());
                    orderLBHDb.setClientWidth(orderLBH.getSelfWidth());
                    orderLBHDb.setSelfHeight(orderLBH.getSelfHeight());
                    orderLBHRepository.save(orderLBHDb);
                }
            }else if(bulkMaster.getName().equals("courierLBHBulkUpload")){
                try{
                    orderLBH.setCourierWeight(map.get(BulkHeaderConstant.COURIER_WEIGHT) != null
                            ? Double.parseDouble(map.get(BulkHeaderConstant.COURIER_WEIGHT).trim()) : null);
                }catch (Exception e){
                    map.put(BulkHeaderConstant.MESSAGE,"Please enter vailed numeric weight .");
                    errorRecord.add(map);
                    e.printStackTrace();
                    continue;
                }

                try{
                    orderLBH.setCourierLength(map.get(BulkHeaderConstant.COURIER_LENGTH) != null
                            ? Double.parseDouble(map.get(BulkHeaderConstant.COURIER_LENGTH).trim()) : null);
                }catch (Exception e){
                    map.put(BulkHeaderConstant.MESSAGE,"Please enter vailed numeric length .");
                    errorRecord.add(map);
                    e.printStackTrace();
                    continue;
                }
                try{
                    orderLBH.setCourierWidth(map.get(BulkHeaderConstant.COURIER_WIDTH) != null
                            ? Double.parseDouble(map.get(BulkHeaderConstant.COURIER_WIDTH).trim()) : null);
                }catch (Exception e){
                    map.put(BulkHeaderConstant.MESSAGE,"Please enter vailed numeric width .");
                    errorRecord.add(map);
                    e.printStackTrace();
                    continue;
                }

                try{
                    orderLBH.setCourierHeight(map.get(BulkHeaderConstant.COURIER_HEIGHT) != null
                            ? Double.parseDouble(map.get(BulkHeaderConstant.COURIER_HEIGHT).trim()) : null);
                }catch (Exception e){
                    map.put(BulkHeaderConstant.MESSAGE,"Please enter valid numeric height .");
                    errorRecord.add(map);
                    e.printStackTrace();
                    continue;
                }

                if(orderLBH.getAwbNumber()==null || orderLBH.getAwbNumber().isEmpty()){
                    map.put(BulkHeaderConstant.MESSAGE,"Please enter Awb Number.");
                    errorRecord.add(map);
                    continue;
                }
                if(orderLBH.getCourierWeight()==null ){
                    map.put(BulkHeaderConstant.MESSAGE,"Please enter Weight.");
                    errorRecord.add(map);
                    continue;
                }
                if(orderLBH.getCourierLength()==null ){
                    map.put(BulkHeaderConstant.MESSAGE,"Please enter Length.");
                    errorRecord.add(map);
                    continue;
                }
                if(orderLBH.getCourierWidth()==null ){
                    map.put(BulkHeaderConstant.MESSAGE,"Please enter Width.");
                    errorRecord.add(map);
                    continue;
                }
                if(orderLBH.getCourierHeight()==null ){
                    map.put(BulkHeaderConstant.MESSAGE,"Please enter Height.");
                    errorRecord.add(map);
                    continue;
                }

                OrderLBH  orderLBHDb = orderLBHRepository.findByAwbNumber(orderLBH.getAwbNumber());
                if(orderLBHDb == null){
                    orderLBHRepository.save(orderLBH);
                }else {
                    orderLBHDb.setCourierWeight(orderLBH.getCourierWeight());
                    orderLBHDb.setCourierLength(orderLBH.getCourierLength());
                    orderLBHDb.setCourierWidth(orderLBH.getCourierWidth());
                    orderLBHDb.setCourierHeight(orderLBH.getCourierHeight());
                    orderLBHRepository.save(orderLBHDb);
                }
            }

            successRecord.add(map);
        }
        bulkUploadBean.setBulkMaster(bulkMaster);
        bulkUploadBean.setErrorRecords(errorRecord);
        bulkUploadBean.setSuccessRecords(successRecord);
        bulkMasterService.setUploadProgressCount(bulkMaster.getName(), 100, token, true);
        return bulkUploadBean;
    }
}
