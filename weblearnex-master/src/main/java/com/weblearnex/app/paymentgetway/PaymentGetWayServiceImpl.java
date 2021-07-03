package com.weblearnex.app.paymentgetway;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.ProjectConstant;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.constant.UserType;
import com.weblearnex.app.datatable.reposatory.ClientWalletRepository;
import com.weblearnex.app.datatable.reposatory.PaymentGetWayLogRepository;
import com.weblearnex.app.entity.order.SaleOrderTransactionLog;
import com.weblearnex.app.entity.paymentwetway.ClientWallet;
import com.weblearnex.app.entity.paymentwetway.PaymentGetWayLog;
import com.weblearnex.app.entity.setup.Page;
import com.weblearnex.app.entity.setup.User;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.model.SessionUserBean;
import com.weblearnex.app.paymentgetway.bean.PaymentFetchResponseBean;
import com.weblearnex.app.paymentgetway.bean.PaymentOrderResponseBean;
// import com.weblearnex.app.reposatory.ClientWalletRepository;
import com.weblearnex.app.utils.SharedMethords;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.codehaus.jettison.json.JSONObject;
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
public class PaymentGetWayServiceImpl implements PaymentGetWayService{

    @Autowired
    private MessageSource messageSource;

    @Autowired
    @Qualifier("applicionConfig")
    private MessageSource applicionConfig;

    @Autowired
    private PaymentGetWayLogRepository paymentGetWayLogRepository;

    @Autowired
    private ClientWalletRepository clientWalletRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SessionUserBean sessionUserBean;

    @Override
    public ResponseBean<Map<String, String>> getPaymentGetWayCredential() {
        ResponseBean<Map<String, String>> responseBean = new ResponseBean<Map<String, String>>();
       try{
           Map<String,String> resultMap = new HashMap<String, String>();
           resultMap.put("apiKey", applicionConfig.getMessage(AppProperty.RAZORPAY_API_KEY, null, null));
           //resultMap.put("apiToken", applicionConfig.getMessage(AppProperty.RAZORPAY_API_TOKEN, null, null));
           resultMap.put("paymentGetWalLogo", applicionConfig.getMessage(AppProperty.RAZORPAY_GET_WAY_LOGO_URL, null, null));
           resultMap.put("orderApiUrl", applicionConfig.getMessage(AppProperty.RAZORPAY_ORDER_API_URL, null, null));
           resultMap.put("fetchPaymentUrl", applicionConfig.getMessage(AppProperty.RAZORPAY_FETCH_PAYMENT_URL, null, null));
           // TODO get login user, client and send wallet amount details.
           if(sessionUserBean.getUser().getType().equals(UserType.CLIENT)){
               ClientWallet clientWallet = clientWalletRepository.findByClientCode(sessionUserBean.getUser().getClientCode());
               resultMap.put("walletAmount", (clientWallet == null || clientWallet.getWalletAmount() == null ) ? "0.0" : String.valueOf(clientWallet.getWalletAmount()));
               resultMap.put("rewardAmount", (clientWallet == null || clientWallet.getRewardAmount() == null ) ? "0.0" : String.valueOf(clientWallet.getRewardAmount()));
               resultMap.put("authorizedForRecharge","Yes");
               resultMap.put("authorizedMsg","Success");
           }else {
               resultMap.put("authorizedForRecharge","Not");
               resultMap.put("authorizedMsg","Client type user can recharge wallet.");
           }
           responseBean.setStatus(ResponseStatus.SUCCESS);
           responseBean.setResponseBody(resultMap);
           return responseBean;
       }catch (Exception e){
           e.printStackTrace();
           responseBean.setStatus(ResponseStatus.FAIL);
           responseBean.setMessage(e.getMessage());
       }
       return responseBean;
    }

    @Override
    public DataTablesOutput<PaymentGetWayLog> getAllPaymentGetWayLog(DataTablesInput input) {
        User user = sessionUserBean.getUser();
        if(input.getColumn("id") == null){
            input.addColumn("id", true, true,null);
        }
        input.addOrder("id",false);
        if(UserType.CLIENT.equals(user.getType())){
            input.addColumn("clientCode", true, false, sessionUserBean.getUser().getClientCode());
        }
        return paymentGetWayLogRepository.findAll(input);
    }

    @Override
    public DataTablesOutput<ClientWallet> getAllClientWallet(DataTablesInput input) {
        User user = sessionUserBean.getUser();
        if(UserType.CLIENT.equals(user.getType())){
            input.addColumn("clientCode", true, false, sessionUserBean.getUser().getClientCode());
        }
        return clientWalletRepository.findAll(input);
    }

    @Override
    public ResponseBean savePaymentGetWayLog(Map<String, String> paymentDetails) {
        ResponseBean responseBean = new ResponseBean();
        try{
            ResponseBean responseBean1 = fetchPaymentDetails(paymentDetails);
            if(ResponseStatus.FAIL.equals(responseBean1.getStatus())){
                return responseBean1;
            }
            PaymentFetchResponseBean paymentFetchResponseBean = (PaymentFetchResponseBean) responseBean1.getResponseBody();
            PaymentGetWayLog paymentGetWayLogBD = paymentGetWayLogRepository.findByPaymentId(paymentFetchResponseBean.getId());
            if(paymentGetWayLogBD != null){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Payment id already exist.");
                return responseBean;
            }
            PaymentGetWayLog paymentGetWayLog = new PaymentGetWayLog();
            paymentGetWayLog.setAmount(paymentFetchResponseBean.getAmount() != null ? paymentFetchResponseBean.getAmount()/100 : paymentFetchResponseBean.getAmount());
            paymentGetWayLog.setStatus(paymentFetchResponseBean.getStatus());
            paymentGetWayLog.setPaymentId(paymentFetchResponseBean.getId());
            paymentGetWayLog.setOrderId(paymentFetchResponseBean.getOrder_id());
            paymentGetWayLog.setSignature(paymentDetails.get("razorpay_signature"));
            paymentGetWayLog.setBank(paymentFetchResponseBean.getBank());
            paymentGetWayLog.setMethod(paymentFetchResponseBean.getMethod());
            paymentGetWayLog.setEmail(paymentFetchResponseBean.getEmail());
            paymentGetWayLog.setContact(paymentFetchResponseBean.getContact());
            paymentGetWayLog.setDate(SharedMethords.getCurrentDate());
            paymentGetWayLog.setCreated_at(paymentFetchResponseBean.getCreated_at());

            // TODO get login user client and save it.
            paymentGetWayLog.setClientCode(sessionUserBean.getUser().getClientCode());

            ClientWallet clientWallet = clientWalletRepository.findByClientCode(sessionUserBean.getUser().getClientCode());
            if(clientWallet == null){
                clientWallet = new ClientWallet();
                clientWallet.setClientCode(sessionUserBean.getUser().getClientCode());
                clientWallet.setLastWalletRechargeDate(new Date());
                clientWallet.setWalletAmount(paymentGetWayLog.getAmount());
                clientWallet.setRewardAmount(0.0);
            }else{
                clientWallet.setWalletAmount(clientWallet.getWalletAmount() + paymentGetWayLog.getAmount());
                clientWallet.setLastWalletRechargeDate(new Date());
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
    public ResponseBean createPaymentOrder(Map<String, Object> paymentOrderDetails) {
        ResponseBean responseBean = new ResponseBean();
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.set("Authorization", applicionConfig.getMessage(AppProperty.RAZORPAY_API_TOKEN, null, null));
            HttpEntity<Map<String,Object>> entity = new HttpEntity<Map<String,Object>>(paymentOrderDetails,headers);
            String url = applicionConfig.getMessage(AppProperty.RAZORPAY_ORDER_API_URL, null, null);
            String response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class).getBody();
            PaymentOrderResponseBean paymentOrderResponseBean = objectMapper.readValue(response, PaymentOrderResponseBean.class);
            if(paymentOrderResponseBean != null && paymentOrderResponseBean.getId() != null){
                responseBean.setStatus(ResponseStatus.SUCCESS);
                responseBean.setResponseBody(paymentOrderResponseBean.getId());
                return responseBean;
            }else {
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Payment Order creation api fail.");
                return responseBean;
            }
        }catch (Exception e){
            e.printStackTrace();
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(e.getMessage());
        }
        return responseBean;
    }

    @Override
    public ResponseBean<PaymentFetchResponseBean> fetchPaymentDetails(Map<String, String> paymentDetails) {
        ResponseBean<PaymentFetchResponseBean> responseBean = new ResponseBean<PaymentFetchResponseBean>();
        //razorpay_payment_id, razorpay_order_id, razorpay_signature
        if(paymentDetails == null || paymentDetails.isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Payment details is empty.");
            return responseBean;
        }
        if(paymentDetails.get("razorpay_payment_id") == null || paymentDetails.get("razorpay_payment_id").trim().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Payment id is empty.");
            return responseBean;
        }
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.set("Authorization", applicionConfig.getMessage(AppProperty.RAZORPAY_API_TOKEN, null, null));
            String url = applicionConfig.getMessage(AppProperty.RAZORPAY_FETCH_PAYMENT_URL, null, null) + paymentDetails.get("razorpay_payment_id");
            HttpEntity <String> entity = new HttpEntity<String>(headers);
            String response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
            PaymentFetchResponseBean paymentFetchResponseBean = objectMapper.readValue(response, PaymentFetchResponseBean.class);
            if(paymentFetchResponseBean != null && "authorized".equalsIgnoreCase(paymentFetchResponseBean.getStatus())){
                responseBean.setStatus(ResponseStatus.SUCCESS);
                responseBean.setResponseBody(paymentFetchResponseBean);
                return responseBean;
            }else {
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Payment details not found");
                return responseBean;
            }
        }catch (Exception e){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(e.getMessage());
        }
        return responseBean;
    }

    @Override
    public ResponseEntity<Resource> downloadPaymentGetWayLogsReport() {
        List<PaymentGetWayLog> paymentGetWayLogList = new ArrayList<PaymentGetWayLog>();
        if(sessionUserBean.getUser().getType().equals(UserType.CLIENT)){
            paymentGetWayLogList = paymentGetWayLogRepository.findByClientCode(sessionUserBean.getUser().getClientCode());

        }else {
            paymentGetWayLogList = (List<PaymentGetWayLog>) paymentGetWayLogRepository.findAll();
        }
       if(paymentGetWayLogList == null){
            return null;
        }
        SXSSFWorkbook xlsWorkbook = new SXSSFWorkbook();
        SXSSFSheet xlsSheet = (SXSSFSheet) xlsWorkbook.createSheet();
        int rowIndex = ProjectConstant.ZERO;
        SXSSFRow titleRow = (SXSSFRow) xlsSheet.createRow(rowIndex++);
        titleRow.createCell(0).setCellValue("CLIENT_CODE");
        titleRow.createCell(1).setCellValue("PAYMENT_ID");
        titleRow.createCell(2).setCellValue("AMOUNT");
        titleRow.createCell(3).setCellValue("STATUS");
        titleRow.createCell(4).setCellValue("METHOD");
        titleRow.createCell(5).setCellValue("EMAIL");
        titleRow.createCell(6).setCellValue("CONTACT");
        titleRow.createCell(7).setCellValue("DATE");
        titleRow.createCell(8).setCellValue("REMARKS");

        if(paymentGetWayLogList != null && paymentGetWayLogList.size() > ProjectConstant.ZERO){
            for (PaymentGetWayLog paymentGetWayLog : paymentGetWayLogList) {
                SXSSFRow dataRow = (SXSSFRow)xlsSheet.createRow(rowIndex++);
                dataRow.createCell(0).setCellValue(paymentGetWayLog.getClientCode() != null ? paymentGetWayLog.getClientCode():"NA");
                dataRow.createCell(1).setCellValue(paymentGetWayLog.getPaymentId() != null ? paymentGetWayLog.getPaymentId():"NA");
                dataRow.createCell(2).setCellValue(paymentGetWayLog.getAmount() != null ? paymentGetWayLog.getAmount().toString():"NA");
                dataRow.createCell(3).setCellValue(paymentGetWayLog.getStatus() != null ? paymentGetWayLog.getStatus():"NA");
                dataRow.createCell(4).setCellValue(paymentGetWayLog.getMethod() != null ? paymentGetWayLog.getMethod():"NA");
                dataRow.createCell(5).setCellValue(paymentGetWayLog.getEmail() != null ? paymentGetWayLog.getEmail():"NA");
                dataRow.createCell(6).setCellValue(paymentGetWayLog.getContact() != null ? paymentGetWayLog.getContact():"NA");
                dataRow.createCell(7).setCellValue(paymentGetWayLog.getDate()!= null ? paymentGetWayLog.getDate():"NA");
                dataRow.createCell(8).setCellValue(paymentGetWayLog.getRemarks() != null ? paymentGetWayLog.getRemarks():"NA");

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
                .header("Content-Disposition", "attachment; filename="+"paymentGatewayTransactionLogReport"+ProjectConstant.EXTENSION_XLS)
                .body(new ByteArrayResource(byteArray));
    }
}
