package com.weblearnex.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.ProjectConstant;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.constant.UserType;
import com.weblearnex.app.datatable.reposatory.ClientWalletRepository;
import com.weblearnex.app.datatable.reposatory.PaymentGetWayLogRepository;
import com.weblearnex.app.datatable.reposatory.SaleOrderTransactionLogRepository;
import com.weblearnex.app.entity.order.SaleOrderTransactionLog;
import com.weblearnex.app.entity.paymentwetway.ClientWallet;
import com.weblearnex.app.entity.paymentwetway.PaymentGetWayLog;
import com.weblearnex.app.entity.setup.Client;
import com.weblearnex.app.entity.setup.User;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.model.SessionUserBean;
import com.weblearnex.app.paymentgetway.bean.PaymentFetchResponseBean;
import com.weblearnex.app.service.SaleOrderTransactionLogService;
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
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.util.*;


@Service
public class SaleOrderTransactionLogServiceImpl implements SaleOrderTransactionLogService {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private SaleOrderTransactionLogRepository saleOrderTransactionLogRepository;

    @Autowired
    private SessionUserBean sessionUserBean;

    @Autowired
    private PaymentGetWayLogRepository paymentGetWayLogRepository;

    @Autowired
    private ClientWalletRepository clientWalletRepository;

    @Autowired
    @Qualifier("applicionConfig")
    private MessageSource applicionConfig;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ResponseBean<SaleOrderTransactionLog> addSaleOrderTansaction(SaleOrderTransactionLog saleOrderTransactionLog) {
        ResponseBean responseBean = new ResponseBean();
        if(saleOrderTransactionLog.getClientCode()==null||saleOrderTransactionLog.getClientCode().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_CODE_NOT_NULL, null, null));
            return responseBean;
        }
        if(saleOrderTransactionLog.getAwbNumber()==null||saleOrderTransactionLog.getAwbNumber().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.AWB_NUMBER_NOT_NULL, null, null));
            return responseBean;
        }
        if(saleOrderTransactionLog.getAmount()==null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.AMOUNT_NOT_NULL, null, null));
            return responseBean;
        }


        if(saleOrderTransactionLog.getClientOrderId()==null||saleOrderTransactionLog.getClientOrderId().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_ORDER_ID_NOT_NULL, null, null));
            return responseBean;
        }
        saleOrderTransactionLog.setDate(SharedMethords.getCurrentDate());
        saleOrderTransactionLog.setCreateDate(new Date().getTime());

        saleOrderTransactionLog = saleOrderTransactionLogRepository.save(saleOrderTransactionLog);
        if(saleOrderTransactionLog.getId() != null) {
            responseBean.setResponseBody(saleOrderTransactionLog);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_ADDED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.ERROR_AT_SAVE_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public DataTablesOutput<SaleOrderTransactionLog> getAllSaleOrderTransactions(DataTablesInput input) {
        User user = sessionUserBean.getUser();
        if(input.getColumn("id") == null){
            input.addColumn("id", true, true,null);
        }
        input.addOrder("id",false);
        if(UserType.CLIENT.equals(user.getType())){
            input.addColumn("clientCode", true, false, sessionUserBean.getUser().getClientCode());
        }
        return saleOrderTransactionLogRepository.findAll(input);
    }

    @Override
    public ResponseBean clientWalletManualRecharge(SaleOrderTransactionLog saleOrderTransactionLog) {
        ResponseBean responseBean = new ResponseBean();
        try{
            if(saleOrderTransactionLog.getAmount() == null || saleOrderTransactionLog.getAmount() <= 0.0d){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Wallet recharge amount grater than zero.");
                return responseBean;
            }
            if(saleOrderTransactionLog.getClientCode() == null || saleOrderTransactionLog.getClientCode().trim().isEmpty()){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Client code fields is empty.");
                return responseBean;
            }
            if(saleOrderTransactionLog.getRemarks() == null || saleOrderTransactionLog.getRemarks().trim().isEmpty()){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Wallet recharge remarks can not empty.");
                return responseBean;
            }
            PaymentGetWayLog paymentGetWayLog = new PaymentGetWayLog();
            paymentGetWayLog.setAmount(saleOrderTransactionLog.getAmount() != null ? saleOrderTransactionLog.getAmount() : saleOrderTransactionLog.getAmount());
            paymentGetWayLog.setDate(SharedMethords.getCurrentDate());
            paymentGetWayLog.setStatus("Success");
            paymentGetWayLog.setMethod("Topup");
            paymentGetWayLog.setCurrency("IN");
            paymentGetWayLog.setRemarks(saleOrderTransactionLog.getRemarks());
            Date date = new Date();
            paymentGetWayLog.setPaymentId(String.valueOf(date.getTime()));
            paymentGetWayLog.setOrderId(String.valueOf(date.getTime()));
            paymentGetWayLog.setClientCode(saleOrderTransactionLog.getClientCode());
            paymentGetWayLog.setCreated_at(date.getTime());

            ClientWallet clientWallet = clientWalletRepository.findByClientCode(saleOrderTransactionLog.getClientCode());
            if(clientWallet == null){
                clientWallet = new ClientWallet();
                clientWallet.setClientCode(saleOrderTransactionLog.getClientCode());
                clientWallet.setLastWalletRechargeDate(new Date());
                clientWallet.setWalletAmount(paymentGetWayLog.getAmount());
                clientWallet.setRewardAmount(0.0);
                clientWallet.setRemarks(saleOrderTransactionLog.getRemarks());
            }else{
                double amount = clientWallet.getWalletAmount() == null ? 0.0d : clientWallet.getWalletAmount();
                clientWallet.setWalletAmount(amount + paymentGetWayLog.getAmount());
                clientWallet.setLastWalletRechargeDate(new Date());
                clientWallet.setRemarks(saleOrderTransactionLog.getRemarks());
            }
            clientWalletRepository.save(clientWallet);
            paymentGetWayLog = paymentGetWayLogRepository.save(paymentGetWayLog);
            if (paymentGetWayLog.getId() != null) {
                responseBean.setResponseBody(paymentGetWayLog);
                responseBean.setStatus(ResponseStatus.SUCCESS);
                responseBean.setResponseBody(clientWallet);
            } else {
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Error at saving payment get-way log.");
            }
        }catch (Exception e){
            e.printStackTrace();
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(e.getMessage());
        }
        return responseBean;
    }

    @Override
    public ResponseBean deletesSaleOrderTransactionLog(String ids) {
        ResponseBean responseBean = new ResponseBean();
        try{
            if(ids == null || ids.trim().isEmpty()){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("SaleOrderTransactionLog id can not empty.");
                return responseBean;
            }
            List<String> list = new ArrayList<>(Arrays.asList(ids.split(",")));
            for (String id : list){
                saleOrderTransactionLogRepository.deleteById(Long.valueOf(id));
            }
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(ResponseStatus.SUCCESS.name());
        }catch (Exception e){
            e.printStackTrace();
        }
        return responseBean;
    }

    @Override
    public ResponseEntity<Resource> downloadSaleOrderTransactionsReport() {
        List<SaleOrderTransactionLog> saleOrderTransactionLogList = (List<SaleOrderTransactionLog>) saleOrderTransactionLogRepository.findAll();
        if(saleOrderTransactionLogList == null){
            return null;
        }
        SXSSFWorkbook xlsWorkbook = new SXSSFWorkbook();
        SXSSFSheet xlsSheet = (SXSSFSheet) xlsWorkbook.createSheet();
        int rowIndex = ProjectConstant.ZERO;
        SXSSFRow titleRow = (SXSSFRow) xlsSheet.createRow(rowIndex++);
        titleRow.createCell(0).setCellValue("CLIENT_CODE");
        titleRow.createCell(1).setCellValue("AWB_NUMBER");
        titleRow.createCell(2).setCellValue("AMOUNT");
        titleRow.createCell(3).setCellValue("PREVIOUS_AMOUNT");
        titleRow.createCell(4).setCellValue("MODIFIED_AMOUNT");
        titleRow.createCell(5).setCellValue("TRANSACTION_TYPE");
        titleRow.createCell(6).setCellValue("CLIENT_ORDER_ID");
        titleRow.createCell(7).setCellValue("DATE");
        titleRow.createCell(8).setCellValue("REMARKS");
        titleRow.createCell(9).setCellValue("SHIPMENT_WEIGHT");

        if(saleOrderTransactionLogList != null && saleOrderTransactionLogList.size() > ProjectConstant.ZERO){
            for (SaleOrderTransactionLog saleOrderTransactionLog : saleOrderTransactionLogList) {
                SXSSFRow dataRow = (SXSSFRow)xlsSheet.createRow(rowIndex++);
                dataRow.createCell(0).setCellValue(saleOrderTransactionLog.getClientCode() != null ? saleOrderTransactionLog.getClientCode():"NA");
                dataRow.createCell(1).setCellValue(saleOrderTransactionLog.getAwbNumber() != null ? saleOrderTransactionLog.getAwbNumber():"NA");
                dataRow.createCell(2).setCellValue(saleOrderTransactionLog.getAmount() != null ? saleOrderTransactionLog.getAmount().toString():"NA");
                dataRow.createCell(3).setCellValue(saleOrderTransactionLog.getPreviousAmount() != null ? saleOrderTransactionLog.getPreviousAmount().toString():"NA");
                dataRow.createCell(4).setCellValue(saleOrderTransactionLog.getModifiedAmount() != null ? saleOrderTransactionLog.getModifiedAmount().toString():"NA");
                dataRow.createCell(5).setCellValue(saleOrderTransactionLog.getTransactionType() != null ? saleOrderTransactionLog.getTransactionType().toString():"NA");
                dataRow.createCell(6).setCellValue(saleOrderTransactionLog.getClientOrderId() != null ? saleOrderTransactionLog.getClientOrderId():"NA");
                dataRow.createCell(7).setCellValue(saleOrderTransactionLog.getDate()!= null ? saleOrderTransactionLog.getDate():"NA");
                dataRow.createCell(8).setCellValue(saleOrderTransactionLog.getRemarks() != null ? saleOrderTransactionLog.getRemarks():"NA");
                dataRow.createCell(9).setCellValue(saleOrderTransactionLog.getShipmentWeight() != null ? saleOrderTransactionLog.getShipmentWeight().toString():"NA");

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
                .header("Content-Disposition", "attachment; filename="+"OrderTransactionLogReport"+ProjectConstant.EXTENSION_XLS)
                .body(new ByteArrayResource(byteArray));
    }

}
