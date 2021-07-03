package com.weblearnex.app.service.impl;

import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.*;
import com.weblearnex.app.datatable.reposatory.*;
import com.weblearnex.app.entity.master.BulkMaster;
import com.weblearnex.app.entity.order.PacketHistory;
import com.weblearnex.app.entity.order.SaleOrder;
import com.weblearnex.app.entity.remittance.ClientRemittance;
import com.weblearnex.app.entity.remittance.CourierRemittance;
import com.weblearnex.app.entity.setup.Client;
import com.weblearnex.app.entity.setup.Courier;
import com.weblearnex.app.entity.setup.Status;
import com.weblearnex.app.model.BulkUploadBean;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.BulkMasterService;
import com.weblearnex.app.service.BulkUploadService;
import com.weblearnex.app.service.CourierRemittanceService;
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
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.*;

@Service
public class CourierRemittanceServiceImpl implements CourierRemittanceService {


    @Autowired
    private CourierRemittanceRepository courierRemittanceRepository;

    @Autowired
    private SaleOrderRepository saleOrderRepository;

    @Autowired
    private BulkHeaderRepository bulkHeaderRepository;

    @Autowired
    private BulkMasterRepository bulkMasterRepository;

    @Autowired
    private BulkMasterService bulkMasterService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private SaleOrderService saleOrderService;

    @Autowired
    @Qualifier("applicionConfig")
    private MessageSource applicionConfig;

    @Override
    public DataTablesOutput<CourierRemittance> getAllCourierClosedRemittance(DataTablesInput input) {
        return courierRemittanceRepository.findAll(input);
    }

    @Override
    public ResponseEntity<Resource> awbNOReadyForCourierRemittance(String courierCode) {
        String deliveredCode = applicionConfig.getMessage(AppProperty.UD_DELIVERED, null, null);
        Status deliveredStatus = statusRepository.findByStatusCode(deliveredCode);
        List<SaleOrder> saleOrderList = saleOrderRepository.findAllByCourierCodeAndCourierRemittanceAndPaymentTypeAndCurrentStatus(
                courierCode, null, PaymentType.COD, deliveredStatus);
        if (saleOrderList == null) {
            return null;
        }
        SXSSFWorkbook xlsWorkbook = new SXSSFWorkbook();
        SXSSFSheet xlsSheet = (SXSSFSheet) xlsWorkbook.createSheet();
        int rowIndex = ProjectConstant.ZERO;
        SXSSFRow titleRow = (SXSSFRow) xlsSheet.createRow(rowIndex++);
        titleRow.createCell(0).setCellValue("AWB_NUMBER");
        titleRow.createCell(1).setCellValue("COURIER_NAME");
        titleRow.createCell(2).setCellValue("COURIER_CODE");
        titleRow.createCell(3).setCellValue("SALEORDER_NO");
        titleRow.createCell(4).setCellValue("PAYMENT_TYPE");
        titleRow.createCell(5).setCellValue("COD_AMOUNT");
        titleRow.createCell(6).setCellValue("ORDER_DATE");
        titleRow.createCell(7).setCellValue("DELIVERED_DATE");

        Courier courier = courierRepository.findByCourierCode(courierCode);
        if(saleOrderList != null && saleOrderList.size() > ProjectConstant.ZERO){
            for (SaleOrder saleOrder : saleOrderList) {
                SXSSFRow dataRow = (SXSSFRow)xlsSheet.createRow(rowIndex++);
                dataRow.createCell(0).setCellValue(saleOrder.getReferanceNo());
                dataRow.createCell(1).setCellValue(courier.getCourierName());
                dataRow.createCell(2).setCellValue(courier.getCourierCode());
                dataRow.createCell(3).setCellValue(saleOrder.getId());
                dataRow.createCell(4).setCellValue(saleOrder.getPaymentType().toString());
                dataRow.createCell(5).setCellValue(saleOrder.getCodAmount());
                dataRow.createCell(6).setCellValue(SharedMethords.getDate(saleOrder.getOrderDate()));
                dataRow.createCell(7).setCellValue(SharedMethords.getDate(saleOrder.getDeliveredDate()));
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
                .header("Content-Disposition", "attachment; filename="+"courierRemittance"+ProjectConstant.EXTENSION_XLS)
                .body(new ByteArrayResource(byteArray));
    }

    @Override
    public BulkUploadBean uploadBulk(BulkUploadBean bulkUploadBean, BulkMaster bulkMaster) {


        int count = 0;
        int percentage = 0;
        List<Map<String, String>> errorRecord = new ArrayList<Map<String, String>>();
        List<Map<String, String>> successRecord = new ArrayList<Map<String, String>>();
        CourierRemittance courierRemittance = new CourierRemittance();
        List<SaleOrder> saleOrderList = new ArrayList<SaleOrder>();
        List<String> awbList = new ArrayList<>();
        Integer shipmentCount  = 0;
        String token = BulkUploadService.generateRandomString();
        double totalAmount = 0.0d;
        for (Map<String, String> map : bulkUploadBean.getRecords()) {
            count ++;
            percentage = BulkUploadService.calculateUploadPercentage(count, bulkUploadBean.getRecords().size());
            bulkMasterService.setUploadProgressCount(bulkMaster.getName(), percentage, token, false);

            try{
                String awb = (map.get(BulkHeaderConstant.AWB_NUMBER) != null && !map.get(BulkHeaderConstant.AWB_NUMBER).isEmpty()) ? map.get(BulkHeaderConstant.AWB_NUMBER).trim() : null;
                SaleOrder saleOrder = saleOrderRepository.findByReferanceNo(awb);
                if(saleOrder == null){
                    map.put(BulkHeaderConstant.MESSAGE,"Awb no. not fount in dataBase");
                    errorRecord.add(map);
                    continue;
                }

                if(!saleOrder.getCourierCode().equals(bulkUploadBean.getExtra())){
                    map.put(BulkHeaderConstant.MESSAGE,"Invailid selected courier.");
                    errorRecord.add(map);
                    continue;
                }
                String deliveredStatusCode = applicionConfig.getMessage(AppProperty.UD_DELIVERED, null, null);
                if(!deliveredStatusCode.equals(saleOrder.getCurrentStatus().getStatusCode())){
                    map.put(BulkHeaderConstant.MESSAGE,"Shipment current status is not deliverd");
                    errorRecord.add(map);
                    continue;
                }
                if(!saleOrder.getPaymentType().equals(PaymentType.COD)){
                    map.put(BulkHeaderConstant.MESSAGE,"Shipment payment type must be cod");
                    errorRecord.add(map);
                    continue;

                }
                if(saleOrder.getCourierRemittance()!=null){
                    map.put(BulkHeaderConstant.MESSAGE,"Remittance already generated");
                    errorRecord.add(map);
                    continue;
                }

                successRecord.add(map);
                saleOrderList.add(saleOrder);
                awbList.add(awb);
                totalAmount = totalAmount+saleOrder.getCodAmount();

            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,e.getMessage());
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
        }
        bulkUploadBean.setBulkMaster(bulkMaster);
        bulkUploadBean.setErrorRecords(errorRecord);
        bulkUploadBean.setSuccessRecords(successRecord);

        // File is empty
        if(bulkUploadBean.getRecords() == null || bulkUploadBean.getRecords().isEmpty()){
            bulkMasterService.setUploadProgressCount(bulkMaster.getName(), 100, token, true);
            return bulkUploadBean;
        }

        if(!errorRecord.isEmpty()){
            // set progress completed
            bulkMasterService.setUploadProgressCount(bulkMaster.getName(), 100, token, true);
            return bulkUploadBean;
        }

        String remittanceNo = bulkUploadBean.getExtra()+"_"+new Date().getTime();
        courierRemittance.setRemittanceNo(remittanceNo);
        courierRemittance.setTotalAmountReceived(totalAmount);
        courierRemittance.setCourierCode(bulkUploadBean.getExtra());
        courierRemittance.setDate(new Date());
        courierRemittance.setAwbList(awbList);
        courierRemittance.setTotalShipment(awbList.size());



        courierRemittanceRepository.save(courierRemittance);
        saleOrderList.forEach(saleOrder -> {
            saleOrder.setCourierRemittance(RemittanceStatus.CLOSED);
            //saleOrder.setReferanceNo(remittanceNo);
            saleOrder.setCourierRemittanceId(courierRemittance.getId());
        });
        saleOrderRepository.saveAll(saleOrderList);
        bulkUploadBean.setBulkMaster(bulkMaster);
        // set progress completed
        bulkMasterService.setUploadProgressCount(bulkMaster.getName(), 100, token, true);
        return bulkUploadBean;
    }

    @Override
    public ResponseEntity<Resource> courierRemittanceReport(String remittanceNo) {
        CourierRemittance courierRemittance = courierRemittanceRepository.findByRemittanceNo(remittanceNo);
        if(courierRemittance == null){
            return null;
        }
       List<SaleOrder> saleOrderList = saleOrderRepository.findByReferanceNoIn(courierRemittance.getAwbList());
        if(saleOrderList == null){
            return null;
        }

        SXSSFWorkbook xlsWorkbook = new SXSSFWorkbook();
        SXSSFSheet xlsSheet = (SXSSFSheet) xlsWorkbook.createSheet();
        int rowIndex = ProjectConstant.ZERO;
        SXSSFRow titleRow = (SXSSFRow) xlsSheet.createRow(rowIndex++);
        titleRow.createCell(0).setCellValue("AWB_NUMBER");
        titleRow.createCell(1).setCellValue("ORDER_DATE");
        titleRow.createCell(2).setCellValue("CONSIGNEE_NAME");
        titleRow.createCell(3).setCellValue("PRODUCT_SKU");
        titleRow.createCell(4).setCellValue("PRODUCT_NAME");
        titleRow.createCell(5).setCellValue("PRODUCT_QUANTITY");
        titleRow.createCell(6).setCellValue("PRODUCT_PRICE");
        titleRow.createCell(7).setCellValue("PAYMENT_TYPE");
        titleRow.createCell(8).setCellValue("CLIENT_NAME");
        titleRow.createCell(9).setCellValue("CLIENT_ORDER_ID");
        titleRow.createCell(10).setCellValue("COURIER_NAME");
        titleRow.createCell(11).setCellValue("COURIER_ID");
        titleRow.createCell(12).setCellValue("COURIER_AWB");
        titleRow.createCell(13).setCellValue("LAST_STATUS");
        titleRow.createCell(14).setCellValue("LAST_STATUS_DATE");

        if(saleOrderList != null && saleOrderList.size() > ProjectConstant.ZERO){
            for (SaleOrder saleOrder : saleOrderList) {
                Client client = clientRepository.findByClientCode(saleOrder.getClientCode());
                Courier courier = courierRepository.findByCourierCode(saleOrder.getCourierCode());
                SXSSFRow dataRow = (SXSSFRow)xlsSheet.createRow(rowIndex++);
                dataRow.createCell(0).setCellValue(saleOrder.getReferanceNo() != null ? saleOrder.getReferanceNo()+" " :"NA");
                dataRow.createCell(1).setCellValue(SharedMethords.getDate(saleOrder.getOrderDate()));
                dataRow.createCell(2).setCellValue(saleOrder.getConsigneeName() != null ? saleOrder.getConsigneeName()+" " :"NA");

                dataRow.createCell(3).setCellValue(saleOrder.getProductSKU() != null ? saleOrder.getProductSKU()+" " :"NA");
                dataRow.createCell(4).setCellValue(saleOrder.getProductName() != null ? saleOrder.getProductName()+" " :"NA");
                dataRow.createCell(5).setCellValue(saleOrder.getProductQuantity() != null ? saleOrder.getProductQuantity()+" " :"NA");
                dataRow.createCell(6).setCellValue(saleOrder.getProductPrice() != null ? saleOrder.getProductPrice()+" " :"NA");
                dataRow.createCell(7).setCellValue(saleOrder.getPaymentType().toString() != null ? saleOrder.getPaymentType()+" " :"NA");
                dataRow.createCell(8).setCellValue(client.getClientName() != null ? client.getClientName()+" " :"NA");
                dataRow.createCell(9).setCellValue(saleOrder.getClientOrderId() != null ? saleOrder.getClientOrderId()+" " :"NA");
                dataRow.createCell(10).setCellValue(courier.getCourierName() != null ? courier.getCourierName()+" " :"NA");
                dataRow.createCell(11).setCellValue(courier.getCourierCode() != null ? courier.getCourierCode()+" " :"NA");
                dataRow.createCell(12).setCellValue(saleOrder.getCourierAWBNumber() != null ? saleOrder.getCourierAWBNumber()+" " :"NA");
                dataRow.createCell(13).setCellValue(saleOrder.getCurrentStatus().getStatusName());
                PacketHistory lastPacketHistory = SharedMethords.getLastPacketHistory(saleOrder);
                if(lastPacketHistory != null && lastPacketHistory.getDate() != null){
                    dataRow.createCell(14).setCellValue(SharedMethords.getDate(lastPacketHistory.getDate()));
                }else{
                    dataRow.createCell(14).setCellValue("NA");
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
                .header("Content-Disposition", "attachment; filename="+"courierRemittanceReport"+ProjectConstant.EXTENSION_XLS)
                .body(new ByteArrayResource(byteArray));
    }

}
