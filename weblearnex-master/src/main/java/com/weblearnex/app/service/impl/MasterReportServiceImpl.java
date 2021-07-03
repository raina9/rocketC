package com.weblearnex.app.service.impl;

import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.constant.*;
import com.weblearnex.app.datatable.reposatory.ClientRepository;
import com.weblearnex.app.datatable.reposatory.CourierRepository;
import com.weblearnex.app.datatable.reposatory.SaleOrderRepository;
import com.weblearnex.app.datatable.reposatory.StatusRepository;
import com.weblearnex.app.entity.order.PacketHistory;
import com.weblearnex.app.entity.order.SaleOrder;
import com.weblearnex.app.entity.report.MasterReport;
import com.weblearnex.app.entity.setup.Client;
import com.weblearnex.app.entity.setup.Courier;
import com.weblearnex.app.entity.setup.Status;
import com.weblearnex.app.model.SessionUserBean;
import com.weblearnex.app.service.MastertReportService;
import com.weblearnex.app.service.SaleOrderService;
import com.weblearnex.app.utils.SharedMethords;
import org.apache.commons.collections4.ListUtils;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MasterReportServiceImpl implements MastertReportService {
    @Autowired
    private SaleOrderRepository saleOrderRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    @Qualifier("applicionConfig")
    private MessageSource applicionConfig;

    @Autowired
    private SaleOrderService saleOrderService;

    @Autowired
    private SessionUserBean sessionUserBean;

    @Override
    public ResponseEntity<Resource> generateReports(List<String> awbList) {

        if (awbList == null || awbList.isEmpty()) {
            return null;
        }
        SXSSFWorkbook xlsWorkbook = new SXSSFWorkbook();
        SXSSFSheet xlsSheet = (SXSSFSheet) xlsWorkbook.createSheet();
        int rowIndex = ProjectConstant.ZERO;
        SXSSFRow titleRow = (SXSSFRow) xlsSheet.createRow(rowIndex++);
        titleRow.createCell(0).setCellValue("AWB_NUMBER");
        titleRow.createCell(1).setCellValue("ORDER_DATE");
        titleRow.createCell(2).setCellValue("SENDER_NAME");
        titleRow.createCell(3).setCellValue("SENDER_MOBILE_NO");
        titleRow.createCell(4).setCellValue("SENDER_PHONE_NO");
        titleRow.createCell(5).setCellValue("SENDER_EMAIL");
        titleRow.createCell(6).setCellValue("SENDER_PINCODE");
        titleRow.createCell(7).setCellValue("SENDER_CITY");
        titleRow.createCell(8).setCellValue("SENDER_STATE");
        titleRow.createCell(9).setCellValue("SENDER_ADDRESS");
        titleRow.createCell(10).setCellValue("CONSIGNEE_NAME");
        titleRow.createCell(11).setCellValue("CONSIGNEE_MOBILE_NO");
        titleRow.createCell(12).setCellValue("CONSIGNEE_PHONE_NO");
        titleRow.createCell(13).setCellValue("CONSIGNEE_EMAIL_ID");
        titleRow.createCell(14).setCellValue("CONSIGNEE_PINCODE");
        titleRow.createCell(15).setCellValue("CONSIGNEE_CITY");
        titleRow.createCell(16).setCellValue("CONSIGNEE_STATE");
        titleRow.createCell(17).setCellValue("CONSIGNEE_ADDRESS");
        titleRow.createCell(18).setCellValue("PRODUCT_SKU");
        titleRow.createCell(19).setCellValue("PRODUCT_NAME");
        titleRow.createCell(20).setCellValue("PRODUCT_QUANTITY");
        titleRow.createCell(21).setCellValue("PRODUCT_PRICE");
        titleRow.createCell(22).setCellValue("PAYMENT_TYPE");
        titleRow.createCell(23).setCellValue("LENGTH_OF_SHIPMENT");
        titleRow.createCell(24).setCellValue("BREADTH_OF_SHIPMENT");
        titleRow.createCell(25).setCellValue("HIGHT_OF_SHIPMENT");
        titleRow.createCell(26).setCellValue("WEIGHT_OF_SHIPMENT");
        titleRow.createCell(27).setCellValue("CLIENT_NAME");
        titleRow.createCell(28).setCellValue("CLIENT_ORDER_ID");
        titleRow.createCell(29).setCellValue("ORDER_TYPE");
        titleRow.createCell(30).setCellValue("PICKUP_LOCATION_ID");
        titleRow.createCell(31).setCellValue("SERVICE_TYPE");
        titleRow.createCell(32).setCellValue("SELECTED_COURIER");
        titleRow.createCell(33).setCellValue("COURIER_NAME");
        titleRow.createCell(34).setCellValue("COURIER_ID");
        titleRow.createCell(35).setCellValue("COURIER_AWB");
        titleRow.createCell(36).setCellValue("LAST_STATUS");
        titleRow.createCell(37).setCellValue("LAST_STATUS_DATE");
        titleRow.createCell(38).setCellValue("DELIVERY_ATTEMPTED_REASON");
        titleRow.createCell(39).setCellValue("LAST_ATTEMPT_DATE");
        titleRow.createCell(40).setCellValue("FIRST_ATTEMPT_DATE");
        titleRow.createCell(41).setCellValue("ATTEMPT_COUNT");
        titleRow.createCell(42).setCellValue("RTO_DATE");
        titleRow.createCell(43).setCellValue("LAST_UPDATE_DATE");

        List<List<String>> subList = ListUtils.partition(awbList,200);

        for(List<String> list : subList){
            List<SaleOrder> saleOrderList = saleOrderRepository.findByReferanceNoIn(list);
            if(saleOrderList != null && saleOrderList.size() > ProjectConstant.ZERO){
                for (SaleOrder saleOrder : saleOrderList) {
                    Client client = clientRepository.findByClientCode(saleOrder.getClientCode());
                    //Courier courier = courierRepository.findByCourierCode(saleOrder.getCourierCode());
                    SXSSFRow dataRow = (SXSSFRow)xlsSheet.createRow(rowIndex++);
                    dataRow.createCell(0).setCellValue(saleOrder.getReferanceNo() != null ? saleOrder.getReferanceNo() :"NA");
                    dataRow.createCell(1).setCellValue(SharedMethords.getDate(saleOrder.getOrderDate()));
                    dataRow.createCell(2).setCellValue(saleOrder.getSenderName() != null ? saleOrder.getSenderName() :"NA");

                    dataRow.createCell(3).setCellValue(saleOrder.getSenderMobileNumber() != null ? saleOrder.getSenderMobileNumber() :"NA");
                    dataRow.createCell(4).setCellValue(saleOrder.getSenderAltNumber() != null ? saleOrder.getSenderAltNumber() :"NA");
                    dataRow.createCell(5).setCellValue(saleOrder.getSenderEmail() != null ? saleOrder.getSenderEmail() :"NA");
                    dataRow.createCell(6).setCellValue(saleOrder.getSenderPinCode() != null ? saleOrder.getSenderPinCode() :"NA");
                    dataRow.createCell(7).setCellValue(saleOrder.getSenderCity() != null ? saleOrder.getSenderCity() :"NA");
                    dataRow.createCell(8).setCellValue(saleOrder.getSenderState() != null ? saleOrder.getSenderState() :"NA");
                    dataRow.createCell(9).setCellValue(saleOrder.getSenderAddress() != null ? saleOrder.getSenderAddress() :"NA");
                    dataRow.createCell(10).setCellValue(saleOrder.getConsigneeName() != null ? saleOrder.getConsigneeName() :"NA");
                    dataRow.createCell(11).setCellValue(saleOrder.getConsigneeMobileNumber() != null ? saleOrder.getConsigneeMobileNumber() :"NA");
                    dataRow.createCell(12).setCellValue(saleOrder.getConsigneeAlternateNumber() != null ? saleOrder.getConsigneeAlternateNumber()+" " :"NA");
                    dataRow.createCell(13).setCellValue(saleOrder.getConsigneeEmailId() != null ? saleOrder.getConsigneeEmailId() :"NA");
                    dataRow.createCell(14).setCellValue(saleOrder.getConsigneePinCode() != null ? saleOrder.getConsigneePinCode() :"NA");
                    dataRow.createCell(15).setCellValue(saleOrder.getConsigneeCity() != null ? saleOrder.getConsigneeCity() :"NA");
                    dataRow.createCell(16).setCellValue(saleOrder.getConsigneeState() != null ? saleOrder.getConsigneeState() :"NA");
                    dataRow.createCell(17).setCellValue(saleOrder.getConsigneeAddress() != null ? saleOrder.getConsigneeAddress() :"NA");
                    dataRow.createCell(18).setCellValue(saleOrder.getProductSKU() != null ? saleOrder.getProductSKU() :"NA");
                    dataRow.createCell(19).setCellValue(saleOrder.getProductName() != null ? saleOrder.getProductName() :"NA");
                    dataRow.createCell(20).setCellValue(saleOrder.getProductQuantity() != null ? saleOrder.getProductQuantity().toString():"NA");
                    dataRow.createCell(21).setCellValue(saleOrder.getProductPrice() != null ? saleOrder.getProductPrice().toString() :"NA");
                    dataRow.createCell(22).setCellValue(saleOrder.getPaymentType().toString() != null ? saleOrder.getPaymentType()+" " :"NA");
                    dataRow.createCell(23).setCellValue(saleOrder.getLength() != null ? saleOrder.getLength().toString() :"NA");
                    dataRow.createCell(24).setCellValue(saleOrder.getBreadth() != null ? saleOrder.getBreadth() .toString():"NA");
                    dataRow.createCell(25).setCellValue(saleOrder.getHight() != null ? saleOrder.getHight().toString() :"NA");
                    dataRow.createCell(26).setCellValue(saleOrder.getWeight() != null ? saleOrder.getWeight().toString() :"NA");
                    dataRow.createCell(27).setCellValue(client.getClientName() != null ? client.getClientName() :"NA");
                    dataRow.createCell(28).setCellValue(saleOrder.getClientOrderId() != null ? saleOrder.getClientOrderId() :"NA");
                    dataRow.createCell(29).setCellValue(saleOrder.getOrderType() != null ? saleOrder.getOrderType() :"NA");
                    dataRow.createCell(30).setCellValue(saleOrder.getPickupLocationId() != null ? saleOrder.getPickupLocationId() :"NA");
                    dataRow.createCell(31).setCellValue("NA");
                    dataRow.createCell(32).setCellValue(saleOrder.getCourierCode() != null ? saleOrder.getCourierCode() :"NA");
                    dataRow.createCell(33).setCellValue(saleOrder.getCourierCode() != null ? saleOrder.getCourierCode() :"NA");
                    dataRow.createCell(34).setCellValue(saleOrder.getCourierCode() != null ? saleOrder.getCourierCode() :"NA");
                    dataRow.createCell(35).setCellValue(saleOrder.getCourierAWBNumber() != null ? saleOrder.getCourierAWBNumber() :"NA");
                    dataRow.createCell(36).setCellValue(saleOrder.getCurrentStatus().getStatusName());

                    PacketHistory lastPacketHistory = SharedMethords.getLastPacketHistory(saleOrder);
                    if(lastPacketHistory != null && lastPacketHistory.getDate() != null){
                        dataRow.createCell(37).setCellValue(SharedMethords.getDate(lastPacketHistory.getDate()));
                        dataRow.createCell(43).setCellValue(SharedMethords.getDate(lastPacketHistory.getDate()));
                    }else{
                        dataRow.createCell(37).setCellValue("NA");
                        dataRow.createCell(43).setCellValue("NA");
                    }

                    dataRow.createCell(38).setCellValue(saleOrder.getDeliveryAttemptedReason() != null ? saleOrder.getDeliveryAttemptedReason() :"");
                    PacketHistory lastAttemptedHistory = saleOrderService.getLastAttemptedHistory(saleOrder);
                    if(lastAttemptedHistory != null){
                        dataRow.createCell(39).setCellValue(SharedMethords.getDate(lastAttemptedHistory.getDate()));
                    }else{
                        dataRow.createCell(39).setCellValue("NA");
                    }

                    PacketHistory firstAttemptedHistory = saleOrderService.getLastAttemptedHistory(saleOrder);
                    if(firstAttemptedHistory != null){
                        dataRow.createCell(40).setCellValue(SharedMethords.getDate(firstAttemptedHistory.getDate()));
                    }else{
                        dataRow.createCell(40).setCellValue("NA");
                    }
                    dataRow.createCell(41).setCellValue(saleOrder.getDrsAttemptedCount() != null ? saleOrder.getDrsAttemptedCount().toString() :"");
                    if(saleOrder.getRtoDate() != null){
                        dataRow.createCell(42).setCellValue(SharedMethords.getDate(saleOrder.getRtoDate()));
                    }else{
                        dataRow.createCell(42).setCellValue("NA");
                    }


                }
            }
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try{
            xlsWorkbook.write(bos);
        }catch (Exception e){
            e.printStackTrace();
        }
        byte [] byteArray = bos.toByteArray();
        return ResponseEntity.ok().contentLength(byteArray.length)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .cacheControl(CacheControl.noCache())
                .header("Content-Disposition", "attachment; filename="+"masterReport"+ProjectConstant.EXTENSION_XLS)
                .body(new ByteArrayResource(byteArray));
    }

    @Override
    public ResponseEntity<Resource> generateMasterReport(MasterReport masterReport) {
        List<String> awbList = new ArrayList<>();
        try{
            if(masterReport.getReportType() == null){
                return null;
            }
            if(ReportType.AWB.equals(masterReport.getReportType())){
                if(masterReport.getAwbList() == null | masterReport.getAwbList().isEmpty()){
                    return null;
                }
                return generateReports(masterReport.getAwbList());
            }else if(ReportType.CUSTOM.equals(masterReport.getReportType())){
                if(masterReport.getReportCustomType() == null){
                    return null;
                }
                Date toDate = new Date();
                Date fromDate = getReportCustomTypeDate(masterReport.getReportCustomType());
                // TODO get only awbnumber List
                List<SaleOrder>  saleOrderList = null;
                if(sessionUserBean!=null && sessionUserBean.getUser()!=null && sessionUserBean.getUser().getType()!=null &&UserType.CLIENT.equals(sessionUserBean.getUser().getType())){
                    awbList = getAwbNumbersFromObject(saleOrderRepository.getAllBetweenDatesAndclientCode(fromDate,toDate, sessionUserBean.getUser().getClientCode()));
                }else{
                    awbList  = getAwbNumbersFromObject(saleOrderRepository.getAllBetweenDates(fromDate,toDate));
                }
                return generateReports(awbList);
            }else if(ReportType.DATE_RANGE.equals(masterReport.getReportType())){
                List<SaleOrder>  saleOrderList = null;
                String tDate = SharedMethords.getOnlyDate(masterReport.getToDate())+" 23:59:59";
                masterReport.setToDate(SharedMethords.getDate(tDate, SharedMethords.DATE_FORMAT));
                if(UserType.CLIENT.equals(sessionUserBean.getUser().getType())){
                    awbList = getAwbNumbersFromObject(saleOrderRepository.getAllBetweenDatesAndclientCode(masterReport.getFromDate(),masterReport.getToDate(), sessionUserBean.getUser().getClientCode()));
                }else{
                    awbList  = getAwbNumbersFromObject(saleOrderRepository.getAllBetweenDates(masterReport.getFromDate(),masterReport.getToDate()));
                }
                return generateReports(awbList);
            }else if(ReportType.PENDING_ORDERS.equals(masterReport.getReportType())){
                List<SaleOrder> saleOrderList = (List<SaleOrder>) saleOrderRepository.findAll();
                if(saleOrderList == null){
                    return null;
                }

            }else if(ReportType.PENDING_CLIENT_REMITTANCE.equals(masterReport.getReportType())){
                String deliveredCode = applicionConfig.getMessage(AppProperty.UD_DELIVERED, null, null);

                if(sessionUserBean.getUser().getType().equals(UserType.CLIENT)){
                    String  clientCode = sessionUserBean.getUser().getClientCode();
                    awbList = getAwbNumbersFromObject(saleOrderRepository.getAllByClientCodeAndClientRemittanceAndPaymentTypeAndCurrentStatus(
                            clientCode,null,PaymentType.COD , deliveredCode));
                }else {
                    awbList = getAwbNumbersFromObject(saleOrderRepository.findAllByClientRemittanceAndPaymentTypeAndCurrentStatus(
                            null,PaymentType.COD, deliveredCode));
                }

                if(awbList == null){
                    return null;
                }
                return generateReports(awbList);


            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getAwbNumbersFromObject(List<Object[]> awbs){
        List<String> awbNumbers =new ArrayList<String>();
        for(Object[] obj:awbs){
            awbNumbers.add(obj[0].toString());
        }
        return awbNumbers;
    }

    private Date getReportCustomTypeDate(ReportCustomType reportCustomType){
        try{
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, reportCustomType.getDays());
            String result = dateFormat.format(calendar.getTime());
            return dateFormat.parse(result);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
