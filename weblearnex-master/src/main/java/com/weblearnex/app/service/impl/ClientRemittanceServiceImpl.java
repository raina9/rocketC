package com.weblearnex.app.service.impl;

import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.*;
import com.weblearnex.app.datatable.reposatory.*;
import com.weblearnex.app.entity.master.BulkMaster;
import com.weblearnex.app.entity.order.PacketHistory;
import com.weblearnex.app.entity.order.SaleOrder;
import com.weblearnex.app.entity.remittance.ClientRemittance;
import com.weblearnex.app.entity.setup.Client;
import com.weblearnex.app.entity.setup.Courier;
import com.weblearnex.app.entity.setup.Status;
import com.weblearnex.app.model.BulkUploadBean;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.model.SessionUserBean;
import com.weblearnex.app.service.BulkMasterService;
import com.weblearnex.app.service.BulkUploadService;
import com.weblearnex.app.service.ClientRemittanceService;
import com.weblearnex.app.service.SaleOrderService;
import com.weblearnex.app.utils.SharedMethords;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class ClientRemittanceServiceImpl implements ClientRemittanceService {

    @Autowired
    private ClientRemittanceRepository clientRemittanceRepository;

    @Autowired
    private SaleOrderRepository saleOrderRepository;

    @Autowired
    private BulkMasterService bulkMasterService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private SaleOrderService saleOrderService;

    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    @Qualifier("applicionConfig")
    private MessageSource applicionConfig;

    @Autowired
    private SessionUserBean sessionUserBean;

    @Override
    public DataTablesOutput<ClientRemittance> getAllClientGeneratedRemittance(DataTablesInput input) {
        String remittanceGenerated = applicionConfig.getMessage(AppProperty.REMITTANCE_GENERATED, null, null);
        input.addColumn("status.statusCode", true, false, remittanceGenerated);
        if(sessionUserBean.getUser().getType().equals(UserType.CLIENT)){
            input.addColumn("clientCode", true, false, sessionUserBean.getUser().getClientCode());
        }
        return clientRemittanceRepository.findAll(input);
    }
    @Override
    public DataTablesOutput<ClientRemittance> getAllClientClosedRemittance(DataTablesInput input) {
        String remittanceClosed = applicionConfig.getMessage(AppProperty.REMITTANCE_COLSED, null, null);
        input.addColumn("status.statusCode", true, false, remittanceClosed);
        if(sessionUserBean.getUser().getType().equals(UserType.CLIENT)){
            input.addColumn("clientCode", true, false, sessionUserBean.getUser().getClientCode());
        }
        return clientRemittanceRepository.findAll(input);
    }

    @Override
    public ResponseBean<ClientRemittance> findById(Long id) {
        ResponseBean responseBean = new ResponseBean();
        Optional<ClientRemittance> clientRemittance=clientRemittanceRepository.findById(id);
        if(!clientRemittance.isPresent()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_REMITTANCE_ID_NOT_FOUND,null,null));
            return responseBean;
        }
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG,null,null));
        responseBean.setResponseBody(clientRemittance.get());

        return responseBean;
    }

    @Override
    public ResponseEntity<Resource> getAllClientAwbNumber(String clientCode) {

        String deliveredCode = applicionConfig.getMessage(AppProperty.UD_DELIVERED, null, null);
        Status deliveredStatus = statusRepository.findByStatusCode(deliveredCode);
        List<SaleOrder> saleOrderList = saleOrderRepository.findAllByClientCodeAndClientRemittanceAndPaymentTypeAndCurrentStatus(
                clientCode, null, PaymentType.COD, deliveredStatus);
        if (saleOrderList == null) {
            return null;
        }
        SXSSFWorkbook xlsWorkbook = new SXSSFWorkbook();
        SXSSFSheet xlsSheet = (SXSSFSheet) xlsWorkbook.createSheet();
        int rowIndex = ProjectConstant.ZERO;
        SXSSFRow titleRow = (SXSSFRow) xlsSheet.createRow(rowIndex++);
        titleRow.createCell(0).setCellValue("AWB_NUMBER");
        titleRow.createCell(1).setCellValue("CLIENT_NAME");
        titleRow.createCell(2).setCellValue("SALEORDER_NO");
        titleRow.createCell(3).setCellValue("PAYMENT_TYPE");
        titleRow.createCell(4).setCellValue("COD_AMOUNT");
        titleRow.createCell(5).setCellValue("ORDER_DATE");
        titleRow.createCell(6).setCellValue("DELIVERED_DATE");


       Client client = clientRepository.findByClientCode(clientCode);
        if(saleOrderList != null && saleOrderList.size() > ProjectConstant.ZERO){
            for (SaleOrder saleOrder : saleOrderList) {
                SXSSFRow dataRow = (SXSSFRow)xlsSheet.createRow(rowIndex++);
                dataRow.createCell(0).setCellValue(saleOrder.getReferanceNo());
                dataRow.createCell(1).setCellValue(client.getClientName());
                dataRow.createCell(2).setCellValue(saleOrder.getId());
                dataRow.createCell(3).setCellValue(saleOrder.getPaymentType().toString());
                dataRow.createCell(4).setCellValue(saleOrder.getCodAmount());
                dataRow.createCell(5).setCellValue(SharedMethords.getDate(saleOrder.getOrderDate()));
                dataRow.createCell(6).setCellValue(SharedMethords.getDate(saleOrder.getDeliveredDate()));
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
                .header("Content-Disposition", "attachment; filename="+"clientRemittance"+ProjectConstant.EXTENSION_XLS)
                .body(new ByteArrayResource(byteArray));
    }

    @Override
    public BulkUploadBean uploadBulk(BulkUploadBean bulkUploadBean, BulkMaster bulkMaster) {
        int count = 0;
        int percentage = 0;
        String token = BulkUploadService.generateRandomString();
        List<Map<String, String>> errorRecord = new ArrayList<Map<String, String>>();
        List<Map<String, String>> successRecord = new ArrayList<Map<String, String>>();
        ClientRemittance clientRemittance = new ClientRemittance();
        List<SaleOrder> saleOrderList = new ArrayList<SaleOrder>();
        List<String> awbList = new ArrayList<>();
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

                if(!saleOrder.getClientCode().equals(bulkUploadBean.getExtra())){
                    map.put(BulkHeaderConstant.MESSAGE,"Invailid selected client.");
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
                if(saleOrder.getClientRemittance()!=null){
                    map.put(BulkHeaderConstant.MESSAGE,"Remittance already generated");
                    errorRecord.add(map);
                    continue;
                }
                if(saleOrder.getCourierCode()!=null || saleOrder.getCourierCode().isEmpty()){
                    map.put(BulkHeaderConstant.MESSAGE,"Courier not assigned this saleOrder");
                    errorRecord.add(map);
                    continue;
                }

                if(saleOrder.getCourierAWBNumber()!=null || saleOrder.getCourierAWBNumber().isEmpty()){
                    map.put(BulkHeaderConstant.MESSAGE,"Courier Awb number is empty");
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
            // set upload count
            bulkMasterService.setUploadProgressCount(bulkMaster.getName(), 100, token, true);
            return bulkUploadBean;
        }

        String remittanceNo = bulkUploadBean.getExtra()+"_"+new Date().getTime();
        clientRemittance.setRemittanceNo(remittanceNo);
        clientRemittance.setCollectedAmt(totalAmount);
        clientRemittance.setAwbList(awbList);
        clientRemittance.setTotalShipment(awbList.size());
        //clientRemittance.setAwbs(stringBuffer.substring(0,stringBuffer.length()-1));
        clientRemittance.setClientCode(bulkUploadBean.getExtra());
        clientRemittance.setDate(new Date());
        Status remittanceGenerated = statusRepository.findByStatusCode(applicionConfig.getMessage(AppProperty.REMITTANCE_GENERATED, null, null));
        clientRemittance.setStatus(remittanceGenerated);

        clientRemittanceRepository.save(clientRemittance);
        saleOrderList.forEach(saleOrders -> {
            saleOrders.setClientRemittance(RemittanceStatus.GENERATED);
            //saleOrders.setReferanceNo(remittanceNo);
            saleOrders.setRemittanceId(clientRemittance.getId());
        });
        saleOrderRepository.saveAll(saleOrderList);
        bulkUploadBean.setBulkMaster(bulkMaster);
        bulkMasterService.setUploadProgressCount(bulkMaster.getName(), 100, token, true);
        return bulkUploadBean;
    }

    @Override
    public ResponseBean closedClientRemittance(MultipartFile file,String bankName,String accountNo,String transactionNo,String depositeDate,String remittanceNo,Double depositedAmt) {
        ResponseBean responseBean = new ResponseBean();
        ClientRemittance clientRemittance = clientRemittanceRepository.findByRemittanceNo(remittanceNo);
        if(clientRemittance==null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Remittance Number not found in system");
            return responseBean;
        }

        if (accountNo == null || accountNo.trim().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_REMITTANCE_ACCOUNT_NUMBER_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        // "      "
        if (bankName == null || bankName.trim().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_REMITTANCE_BANK_NAME_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (transactionNo == null || transactionNo.trim().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_REMITTANCE_TRANSACTIONNo_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (depositeDate == null || depositeDate.trim().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_REMITTANCE_DEPOSITED_ATM_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
       if (depositedAmt == null || depositedAmt<=0.0d) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_REMITTANCE_DEPOSITED_ATM_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
       }
        if (file == null || file.isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Deposited slip is empty");
            return responseBean;
        }
        if(!clientRemittance.getCollectedAmt().equals(depositedAmt)){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("DepositedAmt missmatch");
            return responseBean;
        }

       clientRemittance.setBankName(bankName);
       clientRemittance.setAccountNo(accountNo);
       clientRemittance.setDepositedAmt(depositedAmt);
       clientRemittance.setDepositDate(depositeDate);
       clientRemittance.setTransactionNo(transactionNo);

        String ext = Arrays.stream(file.getOriginalFilename().split("\\.")).reduce((a,b) -> b).orElse(null);
        String fileName = remittanceNo+"."+ext;

        clientRemittance.setDepositSlip(fileName);
        String filePath = applicionConfig.getMessage(AppProperty.CLIENT_REMITTANCE_DEPOSITED_SLIP_PATH, null, null);
        File files =  new File(filePath);
        if(!files.isDirectory()){
            files.mkdir();
        }
        filePath =  filePath + "/"+ fileName;
        try {
            file.transferTo(new File(filePath));
        } catch (Exception e){e.printStackTrace();}


        //List<String> awbList = Arrays.asList(clientRemittance.getAwbs().split(","));
        List<SaleOrder> saleOrderList = saleOrderRepository.findByReferanceNoIn(clientRemittance.getAwbList());
        saleOrderList.forEach(saleOrder -> {
            saleOrder.setClientRemittance(RemittanceStatus.CLOSED);
        });

        saleOrderRepository.saveAll(saleOrderList);
        Status remittanceClosed = statusRepository.findByStatusCode(applicionConfig.getMessage(AppProperty.REMITTANCE_COLSED, null, null));
        clientRemittance.setStatus(remittanceClosed);
        clientRemittance = clientRemittanceRepository.save(clientRemittance);

        if (clientRemittance != null) {
            responseBean.setResponseBody(clientRemittance);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_REMITTANCE_UPDATED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_REMITTANCE_UPDATED_ERROR_MSG, null, null));
        }
        return responseBean;
    }


    @Override
    public ResponseEntity<Resource> clientRemittanceReport(String remittanceNo) {
        ClientRemittance clientRemittance = clientRemittanceRepository.findByRemittanceNo(remittanceNo);
        if(clientRemittance == null){
            return null;
        }
        List<SaleOrder> saleOrderList = saleOrderRepository.findByReferanceNoIn(clientRemittance.getAwbList());
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
                .header("Content-Disposition", "attachment; filename="+"clientRemittanceReport"+ProjectConstant.EXTENSION_XLS)
                .body(new ByteArrayResource(byteArray));
    }

    @Override
    public ResponseBean<ClientRemittance> deleteClientRemittance(Long id) {
        ResponseBean responseBean = new ResponseBean();
        Optional<ClientRemittance> clientRemittance = clientRemittanceRepository.findById(id);
        if (!clientRemittance.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_REMITTANCE_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        List<SaleOrder> saleOrders = saleOrderRepository.findByReferanceNoIn(clientRemittance.get().getAwbList());
        clientRemittanceRepository.delete(clientRemittance.get());
        saleOrders.forEach(saleOrder -> {
            saleOrder.setClientRemittance(null);
            saleOrder.setRemittanceId(null);
        });
        saleOrderRepository.saveAll(saleOrders);
        responseBean.setResponseBody(clientRemittance.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_DELETE_MSG, null, null));
        return responseBean;
    }
}
