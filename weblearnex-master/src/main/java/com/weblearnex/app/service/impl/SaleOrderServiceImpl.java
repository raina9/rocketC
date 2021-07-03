package com.weblearnex.app.service.impl;

import com.weblearnex.app.api.service.orderpush.OrderPushService;
import com.weblearnex.app.api.service.orderpush.impl.OrderPushServiceImpl;
import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.*;
import com.weblearnex.app.datatable.reposatory.*;
import com.weblearnex.app.entity.master.*;
import com.weblearnex.app.entity.order.PacketHistory;
import com.weblearnex.app.entity.order.SaleOrder;
import com.weblearnex.app.entity.order.SaleOrderTransactionLog;
import com.weblearnex.app.entity.paymentwetway.ClientWallet;
import com.weblearnex.app.entity.setup.*;
import com.weblearnex.app.model.BulkUploadBean;
import com.weblearnex.app.model.ResponseBean;
//import com.weblearnex.app.reposatory.ClientWalletRepository;
import com.weblearnex.app.model.SessionUserBean;
import com.weblearnex.app.reposatory.PincodeRepository;
import com.weblearnex.app.service.*;
import com.weblearnex.app.specification.SaleOrderSpecification;
import com.weblearnex.app.status.engine.StatusEngine;
import com.weblearnex.app.utils.SharedMethords;
import lombok.Data;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.datatables.mapping.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SaleOrderServiceImpl implements SaleOrderService {

    private static String regex_mobile = "^[0-9][0-9]{9}$";
    private static String regex_pincode = "^[0-9]{6}$";
    private static String regex_email = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private static String regex_decimal = "^[0-9]+(\\.[0-9]{1,3})?$";

    Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SaleOrderRepository saleOrderRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private StatusEngine statusEngine;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    @Qualifier("applicionConfig")
    private MessageSource applicionConfig;

    @Autowired
    private AwbSeriesService awbSeriesService;

    @Autowired
    private ServicablePincodeService servicablePincodeService;

    @Autowired
    private PincodeRepository pincodeRepository;

    @Autowired
    private PincodeService pincodeService;

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private ClientWalletRepository clientWalletRepository;

    @Autowired
    private ClientFacilityRepository clientFacilityRepository;

    @Autowired
    private DomasticRateCardService domasticRateCardService;

    @Autowired
    private SaleOrderTransactionLogService saleOrderTransactionLogService;

    @Autowired
    private SaleOrderTransactionLogRepository saleOrderTransactionLogRepository;

    @Autowired
    private RedisService redisService;

    @Autowired
    private SessionUserBean sessionUserBean;

    @Autowired
    private BulkMasterService bulkMasterService;

    @Autowired
    private CourierPriorityRepository courierPriorityRepository;

    @Autowired
    private CourierService courierService;

    @Autowired
    private MastertReportService mastertReportService;

    @Autowired
    private ClientWarehouseRepository clientWarehouseRepository;

    @Autowired
    private OrderPushService orderPushService;

    @Autowired
    private ApiConfigRepository apiConfigRepository;


    @Override
    public BulkUploadBean uploadBulk(BulkUploadBean bulkUploadBean, BulkMaster bulkMaster) {

        List<Map<String, String>> errorRecord = new ArrayList<Map<String, String>>();
        List<Map<String, String>> successRecord = new ArrayList<Map<String, String>>();
        List<SaleOrder> ordersValidateSuccessList = new ArrayList<SaleOrder>();
        String token = BulkUploadService.generateRandomString();
        int uploadPersentage = 0;
        int count = 0;
        for(Map<String, String> map : bulkUploadBean.getRecords()){
            SaleOrder saleOrder = new SaleOrder();

            saleOrder.setReferanceNo((map.get(BulkHeaderConstant.AWB_NUMBER) != null && !map.get(BulkHeaderConstant.AWB_NUMBER).isEmpty()) ? map.get(BulkHeaderConstant.AWB_NUMBER).trim() : null);
            saleOrder.setSenderName((map.get(BulkHeaderConstant.SENDER_NAME) != null && !map.get(BulkHeaderConstant.SENDER_NAME).isEmpty()) ? map.get(BulkHeaderConstant.SENDER_NAME).trim() : null);

            saleOrder.setSenderMobileNumber(map.get(BulkHeaderConstant.SENDER_MOBILE_NO) != null ? (map.get(BulkHeaderConstant.SENDER_MOBILE_NO).trim()) : null);
            saleOrder.setSenderAltNumber(map.get(BulkHeaderConstant.SENDER_PHONE_NO) != null ? (map.get((BulkHeaderConstant.SENDER_PHONE_NO).trim())) : null);
            saleOrder.setSenderEmail((map.get(BulkHeaderConstant.SENDER_EMAIL) != null && !map.get(BulkHeaderConstant.SENDER_EMAIL).isEmpty()) ? map.get(BulkHeaderConstant.SENDER_EMAIL).trim() : null);
            saleOrder.setSenderPinCode(map.get(BulkHeaderConstant.SENDER_PINCODE) != null ? (map.get(BulkHeaderConstant.SENDER_PINCODE).trim()) : null);
            saleOrder.setSenderAddress((map.get(BulkHeaderConstant.SENDER_ADDRESS) != null && !map.get(BulkHeaderConstant.SENDER_ADDRESS).isEmpty()) ? map.get(BulkHeaderConstant.SENDER_ADDRESS).trim() : null);
            saleOrder.setConsigneeName((map.get(BulkHeaderConstant.CONSIGNEE_NAME) != null && !map.get(BulkHeaderConstant.CONSIGNEE_NAME).isEmpty()) ? map.get(BulkHeaderConstant.CONSIGNEE_NAME).trim() : null);

            saleOrder.setConsigneeMobileNumber(map.get(BulkHeaderConstant.CONSIGNEE_MOBILE_NO) != null ? (map.get(BulkHeaderConstant.CONSIGNEE_MOBILE_NO).trim()) : null);
            saleOrder.setConsigneeAlternateNumber(map.get(BulkHeaderConstant.CONSIGNEE_PHONE_NO) != null ? (map.get(BulkHeaderConstant.CONSIGNEE_PHONE_NO).trim()) : null);
            saleOrder.setConsigneeEmailId((map.get(BulkHeaderConstant.CONSIGNEE_EMAIL_ID) != null && !map.get(BulkHeaderConstant.CONSIGNEE_EMAIL_ID).isEmpty()) ? map.get(BulkHeaderConstant.CONSIGNEE_EMAIL_ID).trim() : null);
            saleOrder.setConsigneePinCode(map.get(BulkHeaderConstant.CONSIGNEE_PINCODE) != null ? (map.get(BulkHeaderConstant.CONSIGNEE_PINCODE).trim()) : null);
            saleOrder.setConsigneeAddress((map.get(BulkHeaderConstant.CONSIGNEE_ADDRESS) != null && !map.get(BulkHeaderConstant.CONSIGNEE_ADDRESS).isEmpty()) ? map.get(BulkHeaderConstant.CONSIGNEE_ADDRESS).trim() : null);

            saleOrder.setProductSKU((map.get(BulkHeaderConstant.PRODUCT_SKU) != null && !map.get(BulkHeaderConstant.PRODUCT_SKU).isEmpty()) ? map.get(BulkHeaderConstant.PRODUCT_SKU).trim() : null);
            saleOrder.setProductName((map.get(BulkHeaderConstant.PRODUCT_NAME) != null && !map.get(BulkHeaderConstant.PRODUCT_NAME).isEmpty()) ? map.get(BulkHeaderConstant.PRODUCT_NAME).trim() : null);

            try {
                Integer productquntity= (map.get(BulkHeaderConstant.PRODUCT_QUANTITY) != null && !map.get(BulkHeaderConstant.PRODUCT_QUANTITY).isEmpty())
                        ? Integer.valueOf(map.get(BulkHeaderConstant.PRODUCT_QUANTITY).trim().split("\\.", 2)[0]) : null;
                saleOrder.setProductQuantity(productquntity);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter 0 or 1 value");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try{
                saleOrder.setProductPrice(map.get(BulkHeaderConstant.PRODUCT_PRICE) != null ? Double.parseDouble(map.get(BulkHeaderConstant.PRODUCT_PRICE).trim()) : null);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Please enter valid numeric product price .");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                saleOrder.setLength((map.get(BulkHeaderConstant.LENGTH_OF_SHIPMENT) != null && !map.get(BulkHeaderConstant.LENGTH_OF_SHIPMENT).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.LENGTH_OF_SHIPMENT).trim().split("\\.", 2)[0])  : null);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric length");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                saleOrder.setBreadth((map.get(BulkHeaderConstant.BREADTH_OF_SHIPMENT) != null && !map.get(BulkHeaderConstant.BREADTH_OF_SHIPMENT).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.BREADTH_OF_SHIPMENT).trim().split("\\.", 2)[0]) : null);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric breadth");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                saleOrder.setHight((map.get(BulkHeaderConstant.HIGHT_OF_SHIPMENT) != null && !map.get(BulkHeaderConstant.HIGHT_OF_SHIPMENT).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.HIGHT_OF_SHIPMENT).trim().split("\\.", 2)[0]) : null);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric height");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                saleOrder.setWeight((map.get(BulkHeaderConstant.WEIGHT_OF_SHIPMENT) != null && !map.get(BulkHeaderConstant.WEIGHT_OF_SHIPMENT).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.WEIGHT_OF_SHIPMENT).trim()) : null);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric weight");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }

            String paymentType = map.get(BulkHeaderConstant.PAYMENT_TYPE);
            if(paymentType != null && !paymentType.isEmpty()){
                if("pp".equalsIgnoreCase(paymentType.trim()) || "prepaid".equalsIgnoreCase(paymentType.trim())){
                    saleOrder.setPaymentType(PaymentType.PREPAID);
                } else if("cod".equalsIgnoreCase(paymentType.trim())){
                    saleOrder.setPaymentType(PaymentType.COD);
                }
            }

            // COD amount will auto fill from product price when shipment is COD.
            /*try{
                saleOrder.setCodAmount(map.get(BulkHeaderConstant.COD_AMOUNT) != null ? Double.parseDouble(map.get(BulkHeaderConstant.COD_AMOUNT).trim()) : null);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Please enter vailed numeric COD Amount .");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }*/
            if(sessionUserBean.getUser().getType().equals(UserType.CLIENT)){
                if(!sessionUserBean.getUser().getClientCode().equals(map.get(BulkHeaderConstant.CLIENT_NAME).trim())){
                    map.put(BulkHeaderConstant.MESSAGE,"You are not authorized to upload other client data.");
                    errorRecord.add(map);
                    continue;
                }
                saleOrder.setClientCode((map.get(BulkHeaderConstant.CLIENT_NAME) != null && !map.get(BulkHeaderConstant.CLIENT_NAME).isEmpty()) ? map.get(BulkHeaderConstant.CLIENT_NAME).trim() : null);
            }else {
                saleOrder.setClientCode((map.get(BulkHeaderConstant.CLIENT_NAME) != null && !map.get(BulkHeaderConstant.CLIENT_NAME).isEmpty()) ? map.get(BulkHeaderConstant.CLIENT_NAME).trim() : null);
            }
            saleOrder.setClientOrderId((map.get(BulkHeaderConstant.CLIENT_ORDER_ID) != null && !map.get(BulkHeaderConstant.CLIENT_ORDER_ID).isEmpty()) ? map.get(BulkHeaderConstant.CLIENT_ORDER_ID).trim() : null);
            saleOrder.setOrderType((map.get(BulkHeaderConstant.ORDER_TYPE) != null && !map.get(BulkHeaderConstant.ORDER_TYPE).isEmpty()) ? map.get(BulkHeaderConstant.ORDER_TYPE).trim() : null);
            saleOrder.setPickupLocationId((map.get(BulkHeaderConstant.PICKUP_LOCATION_ID) != null && !map.get(BulkHeaderConstant.PICKUP_LOCATION_ID).isEmpty()) ? map.get(BulkHeaderConstant.PICKUP_LOCATION_ID).trim() : null);
            saleOrder.setEwaybill((map.get(BulkHeaderConstant.EWAYBILL) != null && !map.get(BulkHeaderConstant.EWAYBILL).isEmpty()) ? map.get(BulkHeaderConstant.EWAYBILL).trim() : null);
            saleOrder.setLatitude((map.get(BulkHeaderConstant.LACTITUDE) != null && !map.get(BulkHeaderConstant.LACTITUDE).isEmpty()) ? map.get(BulkHeaderConstant.LACTITUDE).trim() : null);
            saleOrder.setLongitude((map.get(BulkHeaderConstant.LONGITUDE) != null && !map.get(BulkHeaderConstant.LONGITUDE).isEmpty()) ? map.get(BulkHeaderConstant.LONGITUDE).trim() : null);
            saleOrder.setProductImageUrl((map.get(BulkHeaderConstant.PRODUCT_IMAGE_URL) != null && !map.get(BulkHeaderConstant.PRODUCT_IMAGE_URL).isEmpty()) ? map.get(BulkHeaderConstant.PRODUCT_IMAGE_URL).trim() : null);

            saleOrder.setOrderSourceType(OrderSourceType.BULK);

            // First validate all order then start upload.
            ResponseBean saleOrderValidatorResponse = saleOrderValidator(saleOrder);
            if(ResponseStatus.SUCCESS.equals(saleOrderValidatorResponse.getStatus())){
                SaleOrder saleOrderValidateSuccess = (SaleOrder) saleOrderValidatorResponse.getResponseBody();
                ordersValidateSuccessList.add(saleOrderValidateSuccess);
                map.put(BulkHeaderConstant.AWB_NUMBER,saleOrderValidateSuccess.getReferanceNo());
                successRecord.add(map);
            }else{
                map.put(BulkHeaderConstant.MESSAGE,saleOrderValidatorResponse.getMessage());
                errorRecord.add(map);
                continue;
            }
        }
        // If no error found in upload file then start to upload

        if(errorRecord.isEmpty() && bulkUploadBean.getRecords().size() == ordersValidateSuccessList.size()){
            for(SaleOrder saleOrder : ordersValidateSuccessList){
                count++;
                uploadPersentage =BulkUploadService.calculateUploadPercentage(count, ordersValidateSuccessList.size());
                bulkMasterService.setUploadProgressCount(bulkMaster.getName() ,uploadPersentage, token, false);
                ResponseBean<SaleOrder> saleOrderAddResponse = null;
                try{
                    saleOrderAddResponse = addSaleOrder(saleOrder, false);
                }catch(Exception e){
                    saleOrderAddResponse = new ResponseBean<>();
                    saleOrderAddResponse.setStatus(ResponseStatus.FAIL);
                    saleOrderAddResponse.setMessage(e.getMessage());
                    e.printStackTrace();
                }
                if(ResponseStatus.FAIL.equals(saleOrderAddResponse.getStatus())) {
                    Map<String,String> errorMap = null;
                    for(Map map : successRecord){
                        if(saleOrder.getReferanceNo().equals(map.get(BulkHeaderConstant.AWB_NUMBER))){
                            map.put(BulkHeaderConstant.MESSAGE, saleOrderAddResponse.getMessage());
                            errorMap = map;
                            break;
                        }
                    }
                    successRecord.remove(errorMap);
                    errorRecord.add(errorMap);
                }
            }
        }
        bulkMasterService.setUploadProgressCount(bulkMaster.getName() ,100, token, true);

        bulkUploadBean.setBulkMaster(bulkMaster);
        bulkUploadBean.setErrorRecords(errorRecord);
        bulkUploadBean.setSuccessRecords(successRecord);
        return bulkUploadBean;
    }

    @Override
    public ResponseBean<SaleOrder> addSaleOrder(SaleOrder saleOrder, boolean isValidatioCheck) {
        ResponseBean responseBean = new ResponseBean();
        try{
            if(isValidatioCheck){
                ResponseBean validationResponse = saleOrderValidator(saleOrder);
                if(ResponseStatus.FAIL.equals(validationResponse.getStatus())){
                    responseBean.setStatus(ResponseStatus.FAIL);
                    responseBean.setMessage(validationResponse.getMessage());
                    return responseBean;
                }
            }
            if(saleOrder.getZoneType() == null){
                ResponseBean<ZoneType> zoneTypeResponseBean = pincodeService.getZone(saleOrder.getSenderPinCode(), saleOrder.getDestinationBranchCode());
                if(ResponseStatus.SUCCESS.equals(zoneTypeResponseBean.getStatus())){
                    saleOrder.setZoneType(zoneTypeResponseBean.getResponseBody());
                }
            }
            if(saleOrder.getReferanceNo() == null || "".equals(saleOrder.getReferanceNo().trim())){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Awb number field is blank.");
                return responseBean;
            }
            Status softDataReceivedStatus = statusRepository.findByStatusCode(applicionConfig.getMessage(AppProperty.UD_ORDER_RECEIVED, null, null));
            saleOrder.setCurrentStatus(softDataReceivedStatus);
            // TODO set current login user branch details in loaction
            ResponseBean<SaleOrder> packetHistoryResponse = addPacketHistory(saleOrder, softDataReceivedStatus, null, null, null);
            // saleOrder = saleOrderRepository.save(saleOrder);
            if (saleOrder.getId() != null) {
                responseBean.setResponseBody(saleOrder);
                responseBean.setStatus(ResponseStatus.SUCCESS);
                responseBean.setMessage(messageSource.getMessage(MessageProperty.SALE_ORDER_ADDED_MSG, null, null));
            } else {
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage(messageSource.getMessage(MessageProperty.SALE_ORDER_ADDED_ERROR_MSG, null, null));
            }
        }catch (Exception e){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(e.getMessage());
            e.printStackTrace();
        }

        return responseBean;
    }

    @Override
    public ResponseBean<SaleOrder> saleOrderValidator(SaleOrder saleOrder) {
        ResponseBean<SaleOrder> responseBean = new ResponseBean<SaleOrder>();
        if(saleOrder.getReferanceNo() !=null && !saleOrder.getReferanceNo().trim().isEmpty() && saleOrderRepository.findByReferanceNo(saleOrder.getReferanceNo()) != null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Please check AWB number already used.");
            responseBean.setResponseBody(saleOrder);
            return responseBean;
        }
        if(saleOrder.getClientCode() == null || saleOrder.getClientCode().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Client Id field can be blank.");
            responseBean.setResponseBody(saleOrder);
            return responseBean;
        }
        Client client = clientRepository.findByClientCode(saleOrder.getClientCode());
        if(client == null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Invalid client id, please provide valid client id.");
            responseBean.setResponseBody(saleOrder);
            return responseBean;
        }
        if(client.getActive() == ProjectConstant.ZERO){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Client is de-activated.");
            responseBean.setResponseBody(saleOrder);
            return responseBean;
        }
        if(saleOrder.getClientOrderId() == null || saleOrder.getClientOrderId().trim().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Client order id can not blank.");
            responseBean.setResponseBody(saleOrder);
            return responseBean;
        }
        if(saleOrder.getSenderName() == null || saleOrder.getSenderName().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Sender name can not be blank.");
            responseBean.setResponseBody(saleOrder);
            return responseBean;
        }
        if(saleOrder.getSenderMobileNumber() == null || saleOrder.getSenderMobileNumber().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Sender mobile number can not be blank.");
            responseBean.setResponseBody(saleOrder);
            return responseBean;
        }
        if(!saleOrder.getSenderMobileNumber().matches(regex_mobile)){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Enter valid sender mobile number.");
            responseBean.setResponseBody(saleOrder);
            return responseBean;
        }
        if(saleOrder.getSenderAddress() == null || saleOrder.getSenderAddress().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Sender address can not be blank.");
            responseBean.setResponseBody(saleOrder);
            return responseBean;
        }
        if(saleOrder.getSenderPinCode() == null || saleOrder.getSenderPinCode().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Sender pin-code can not be blank.");
            responseBean.setResponseBody(saleOrder);
            return responseBean;
        }
        if(!saleOrder.getSenderPinCode().matches(regex_pincode)){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Enter valid sender pin-code.");
            responseBean.setResponseBody(saleOrder);
            return responseBean;
        }
        if(!pincodeRepository.existsByPinCode(saleOrder.getSenderPinCode())){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Sender pincode not found in system.");
            responseBean.setResponseBody(saleOrder);
            return responseBean;
        }
        if (saleOrder.getSenderEmail() == null || saleOrder.getSenderEmail().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Sender email can not be blank.");
            responseBean.setResponseBody(saleOrder);
            return responseBean;
        }
        if(!saleOrder.getSenderEmail().matches(regex_email)){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Enter valid sender email.");
            responseBean.setResponseBody(saleOrder);
            return responseBean;
        }

        if(saleOrder.getConsigneeName() == null || saleOrder.getConsigneeName().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Consignee name can not be blank. ");
            responseBean.setResponseBody(saleOrder);
            return responseBean;
        }
        if(saleOrder.getConsigneeMobileNumber() == null || saleOrder.getConsigneeMobileNumber().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Consignee mobile number can not be blank. ");
            responseBean.setResponseBody(saleOrder);
            return responseBean;
        }
        if(!saleOrder.getConsigneeMobileNumber().matches(regex_mobile)){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Enter valid consignee mobile number. ");
            responseBean.setResponseBody(saleOrder);
            return responseBean;
        }
        if(saleOrder.getConsigneeAddress() == null || saleOrder.getConsigneeAddress().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Consignee address can not be blank. ");
            responseBean.setResponseBody(saleOrder);
            return responseBean;
        }
        if(saleOrder.getConsigneePinCode() == null || saleOrder.getConsigneePinCode().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Consignee pin-code can not be blank. ");
            responseBean.setResponseBody(saleOrder);
            return responseBean;
        }
        if(!pincodeRepository.existsByPinCode(saleOrder.getConsigneePinCode())){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Consignee pin-code not exist in system.");
            responseBean.setResponseBody(saleOrder);
            return responseBean;
        }
        if(!saleOrder.getConsigneePinCode().matches(regex_pincode)){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Enter consignee valid pin-code. ");
            responseBean.setResponseBody(saleOrder);
            return responseBean;
        }

        if (saleOrder.getPaymentType() == null) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Payment type invalid, Enter payment type (Prepaid or COD)");
            responseBean.setResponseBody(saleOrder);
            return responseBean;
        }
        if(saleOrder.getProductName() == null || saleOrder.getProductName().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Product name can not be blank.");
            responseBean.setResponseBody(saleOrder);
            return responseBean;
        }
        if(saleOrder.getProductPrice() == null || saleOrder.getProductPrice() == 0.0){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Product price can not be blank.");
            responseBean.setResponseBody(saleOrder);
            return responseBean;
        }
        if(PaymentType.COD.equals(saleOrder.getPaymentType())){
            saleOrder.setCodAmount(saleOrder.getProductPrice());
        }else if(PaymentType.PREPAID.equals(saleOrder.getPaymentType())){
            saleOrder.setCodAmount(0.0);
        }
        if(saleOrder.getProductSKU() == null || saleOrder.getProductSKU().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Product SKU can not be blank.");
            responseBean.setResponseBody(saleOrder);
            return responseBean;
        }
        if(saleOrder.getProductQuantity() <= ProjectConstant.ZERO){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Product quantity can be one or grater than one");
            responseBean.setResponseBody(saleOrder);
            return responseBean;
        }
        if(saleOrder.getLength() == null || saleOrder.getLength() <= ProjectConstant.ZERO){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Product length can be one or grater than one");
            responseBean.setResponseBody(saleOrder);
            return responseBean;
        }
        ResponseBean<Map<String,String>> sourcePincodeResponse = pincodeService.getCityStateCountryByPincode(saleOrder.getSenderPinCode());
        if(sourcePincodeResponse.getStatus().equals(ResponseStatus.SUCCESS)){
            saleOrder.setSenderCountry(sourcePincodeResponse.getResponseBody().get("COUNTRY_NAME"));
            saleOrder.setSenderState(sourcePincodeResponse.getResponseBody().get("STATE_NAME"));
            saleOrder.setSenderCity(sourcePincodeResponse.getResponseBody().get("CITY_NAME"));
        }
        ResponseBean<Map<String,String>> dropPincodeResponse = pincodeService.getCityStateCountryByPincode(saleOrder.getConsigneePinCode());
        if(dropPincodeResponse.getStatus().equals(ResponseStatus.SUCCESS)){
            saleOrder.setConsigneeCity(dropPincodeResponse.getResponseBody().get("CITY_NAME"));
            saleOrder.setConsigneeState(dropPincodeResponse.getResponseBody().get("STATE_NAME"));
            saleOrder.setConsigneeCountry(dropPincodeResponse.getResponseBody().get("COUNTRY_NAME"));
        }
        ZoneType zoneType = pincodeService.calculateZoneType(sourcePincodeResponse.getResponseBody(), dropPincodeResponse.getResponseBody());
        saleOrder.setZoneType(zoneType);

        saleOrder.setOrderDate(new Date());
        if(saleOrder.getReferanceNo() == null||saleOrder.getReferanceNo().trim().isEmpty()){
            ResponseBean<String> awbSerirsResponse = awbSeriesService.getAutoAwbNumber(client,saleOrder.getPaymentType());
            if(ResponseStatus.SUCCESS.equals(awbSerirsResponse.getStatus())){
                saleOrder.setReferanceNo(awbSerirsResponse.getResponseBody());
            }else {
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Auto Awb series generat faild");
                responseBean.setResponseBody(saleOrder);
                return responseBean;
            }

        }

        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setResponseBody(saleOrder);
        return responseBean;
    }

    @Override
    public ResponseBean<SaleOrder> addPacketHistory(SaleOrder saleOrder, Status toStatus, String ndrCode, Date date, String  location) {
        String statusTransitionCode = saleOrder.getCurrentStatus().getStatusCode()+"_"+toStatus.getStatusCode();
        String statusFlowName = applicionConfig.getMessage(AppProperty.PACKET_FLOW_NAME, null, null);
        ResponseBean responseBean = new ResponseBean();
        if(!statusEngine.isValidStatus(statusFlowName, saleOrder.getCurrentStatus(), toStatus)){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Invalid status transition flow.");
            responseBean.setResponseBody(saleOrder);
            return responseBean;
        }

        PacketHistory packetHistory = new PacketHistory();
        packetHistory.setFromStatusCode(saleOrder.getCurrentStatus().getStatusCode());
        packetHistory.setToStatusCode(toStatus.getStatusCode());
        //packetHistory.setAppRequest();
        if(date == null){
            packetHistory.setDate(new Date());
        }else{
            packetHistory.setDate(date);
        }
        packetHistory.setCreatedDate(new Date());
        if(location != null && !location.trim().isEmpty()) {
            packetHistory.setLocation(location);
        }else{
            if(sessionUserBean != null && sessionUserBean.getUser() != null && sessionUserBean.getUser().getBranch() != null){
                List<Branch> branchList = sessionUserBean.getUser().getBranch();
                if(!branchList.isEmpty() && branchList.get(0) != null){
                    packetHistory.setLocation(branchList.get(0).getName());
                }
            }
        }
        //packetHistory.setRemarks();
        packetHistory.setRtoReason(ndrCode);
        packetHistory.setCreatedByName(sessionUserBean.getUser().getFisrtName()+" "+ sessionUserBean.getUser().getLastName());
        packetHistory.setCreatedByCode(sessionUserBean.getUser().getLoginId());
        List<PacketHistory> packetHistoryList = saleOrder.getPacketHistory();
        packetHistoryList.add(packetHistory);
        saleOrder.setPacketHistory(packetHistoryList);
        saleOrder.setCurrentStatus(toStatus);
        saleOrderRepository.save(saleOrder);
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setResponseBody(saleOrder);
        return responseBean;
    }

    @Override
    public ResponseBean<SaleOrder> addPacketHistoryWithoutSave(SaleOrder saleOrder, Status toStatus, String ndrCode, Date date, String location) {
        String statusTransitionCode = saleOrder.getCurrentStatus().getStatusCode()+"_"+toStatus.getStatusCode();
        String statusFlowName = applicionConfig.getMessage(AppProperty.PACKET_FLOW_NAME, null, null);
        ResponseBean responseBean = new ResponseBean();
        if(!statusEngine.isValidStatus(statusFlowName, saleOrder.getCurrentStatus(), toStatus)){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Invalid status transition flow.");
            responseBean.setResponseBody(saleOrder);
            return responseBean;
        }

        PacketHistory packetHistory = new PacketHistory();
        packetHistory.setFromStatusCode(saleOrder.getCurrentStatus().getStatusCode());
        packetHistory.setToStatusCode(toStatus.getStatusCode());
        //packetHistory.setAppRequest();
        if(date == null){
            packetHistory.setDate(new Date());
        }else{
            packetHistory.setDate(date);
        }
        packetHistory.setCreatedDate(new Date());
        if(location != null && !location.trim().isEmpty()) {
            packetHistory.setLocation(location);
        }else{
            if(sessionUserBean != null && sessionUserBean.getUser() != null && sessionUserBean.getUser().getBranch() != null){
                List<Branch> branchList = sessionUserBean.getUser().getBranch();
                if(!branchList.isEmpty() && branchList.get(0) != null){
                    packetHistory.setLocation(branchList.get(0).getName());
                }
            }
        }
        //packetHistory.setRemarks();
        packetHistory.setRtoReason(ndrCode);
        packetHistory.setCreatedByName(sessionUserBean.getUser().getFisrtName()+" "+ sessionUserBean.getUser().getLastName());
        packetHistory.setCreatedByCode(sessionUserBean.getUser().getLoginId());
        List<PacketHistory> packetHistoryList = saleOrder.getPacketHistory();
        packetHistoryList.add(packetHistory);
        saleOrder.setPacketHistory(packetHistoryList);
        saleOrder.setCurrentStatus(toStatus);
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setResponseBody(saleOrder);
        return responseBean;
    }

    @Override
    public ResponseBean<List<SaleOrder>> getAllSaleOrder() {
        ResponseBean responseBean = new ResponseBean();
        List<SaleOrder> saleOrderList = (List<SaleOrder>) saleOrderRepository.findAll();
        if(saleOrderList !=null && !saleOrderList.isEmpty()){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG, null, null));
            responseBean.setResponseBody(saleOrderList);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<SaleOrder> findByAwbNumbers(String referanceNo) {

        ResponseBean responseBean = new ResponseBean();
        List<String> referanceNos = Arrays.asList(referanceNo.split(","));
        if(referanceNos.size()>50){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.AWB_NUMBER_NOT_MORE_THAN_50, null, null));
            return responseBean;
        }

        List<SaleOrder>  awbNumbers= saleOrderRepository.findByReferanceNoIn(referanceNos);

        if (awbNumbers.size()==0) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.AWB_NUMBER_NOT_FOUND, null, null));
            return responseBean;
        }

        //used only for showing status name
        awbNumbers.forEach(saleOrder -> {
            List<PacketHistory> packetHistories = saleOrder.getPacketHistory();
            if(packetHistories != null && !packetHistories.isEmpty()){
                packetHistories.forEach(packetHistory -> {
                    if(packetHistory != null && packetHistory.getToStatusCode() != null){
                        Status status = statusRepository.findByStatusCode(packetHistory.getToStatusCode());
                        if(status != null){
                            packetHistory.setStatusName(status.getStatusName());
                        }
                    }
                });
            }
        });

        List<SaleOrder>  notFoundList= new ArrayList<SaleOrder>();
        for(String str :referanceNos){
            boolean isFound =false;
            for(SaleOrder sl :awbNumbers){
                if(str.equals(sl.getReferanceNo())){
                    isFound=true;
                    sl.setFound(true);
                    sl.setMsg("Found");
                    break;
                }
            }
            if(!isFound){
                SaleOrder s =new SaleOrder();
                s.setReferanceNo(str);
                s.setFound(false);
                s.setMsg("Not Found");
                notFoundList.add(s);
            }
        }
        awbNumbers.addAll(notFoundList);
        responseBean.setResponseBody(awbNumbers);
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.AWB_NUMBER_FOUND, null, null));
        return responseBean;
    }

    @Override
    public ResponseBean<SaleOrder> findByCourierAwbNumber(String courierAWBNumber) {
        ResponseBean responseBean = new ResponseBean();
        List<String> courierAWBNumbers = Arrays.asList(courierAWBNumber.split(","));
        if(courierAWBNumbers.size()>50){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.AWB_NUMBER_NOT_MORE_THAN_50, null, null));
            return responseBean;
        }

        List<SaleOrder>  awbNumbers= saleOrderRepository.findByCourierAWBNumberIn(courierAWBNumbers);

        if (awbNumbers.size()==0) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.AWB_NUMBER_NOT_FOUND, null, null));
            return responseBean;
        }

        //used only for showing status name
        awbNumbers.forEach(saleOrder -> {
            List<PacketHistory> packetHistories = saleOrder.getPacketHistory();
            if(packetHistories != null && !packetHistories.isEmpty()){
                packetHistories.forEach(packetHistory -> {
                    if(packetHistory != null && packetHistory.getToStatusCode() != null){
                        Status status = statusRepository.findByStatusCode(packetHistory.getToStatusCode());
                        if(status != null){
                            packetHistory.setStatusName(status.getStatusName());
                        }
                    }
                });
            }
        });

        List<SaleOrder>  notFoundList= new ArrayList<SaleOrder>();
        for(String str :courierAWBNumbers){
            boolean isFound =false;
            for(SaleOrder sl :awbNumbers){
                if(str.equals(sl.getCourierAWBNumber())){
                    isFound=true;
                    sl.setFound(true);
                    sl.setMsg("Found");
                    break;
                }
            }
            if(!isFound){
                SaleOrder s =new SaleOrder();
                s.setCourierAWBNumber(str);
                s.setFound(false);
                s.setMsg("Not Found");
                notFoundList.add(s);
            }
        }
        awbNumbers.addAll(notFoundList);
        responseBean.setResponseBody(awbNumbers);
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.AWB_NUMBER_FOUND, null, null));
        return responseBean;
    }

    @Override
    public DataTablesOutput<SaleOrder> getAllOrderReceived(DataTablesInput input) {
        String udOrderRecived = applicionConfig.getMessage(AppProperty.UD_ORDER_RECEIVED, null, null);
        input.addColumn("currentStatus.statusCode", true, false, udOrderRecived);
        if(input.getColumn("id") == null){
          input.addColumn("id", true, true,null);
        }
        input.addOrder("id",false);
        if(sessionUserBean.getUser().getType().equals(UserType.CLIENT)){
            input.addColumn("clientCode", true, false, sessionUserBean.getUser().getClientCode());
        }
        return saleOrderRepository.findAll(input);
    }

    @Override
    public ResponseBean assigneeCourier(String awbNumber, String courierCode, String warehouseCode, SaleOrder saleOrder) {
        ResponseBean responseBean = new  ResponseBean();
        try {
            if(warehouseCode == null || "".equals(warehouseCode.trim())){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Warehouse code can not empty.");
                return responseBean;
            }
            ClientWarehouse clientWarehouse = clientWarehouseRepository.findByWarehouseCode(warehouseCode);
            if(clientWarehouse == null ){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Client Warehouse not found.");
                return responseBean;
            }
            // For saving one db call.
            if(saleOrder == null){
                saleOrder = saleOrderRepository.findByReferanceNo(awbNumber);
            }
            if(saleOrder == null){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Awb number not found");
                return responseBean;
            }
            saleOrder.setPickupLocation(clientWarehouse.getWarehouseName());

            Courier courier = courierRepository.findByCourierCode(courierCode);
            if (courier == null){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Courier not exist.");
                return responseBean;
            }

            Client client = clientRepository.findByClientCode(saleOrder.getClientCode());
            ClientFacility clientFacility = clientFacilityRepository.findByClientId(client.getId());
            // Client client = clientFacility.getClient();
            if(clientFacility == null){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Client facility configuration details not found. Please contact support team.");
                return responseBean;
            }

            ClientWallet clientWallet = null;
            Double deliveryCharge = 0.0d;
            boolean deductDeliveryCharge = false;
            if(clientFacility.getWalletActive() != null && clientFacility.getWalletActive()){
                clientWallet = clientWalletRepository.findByClientCode(saleOrder.getClientCode());
                if(clientWallet == null || clientWallet.getWalletAmount() == null || clientWallet.getWalletAmount() <= 0d){
                    responseBean.setStatus(ResponseStatus.FAIL);
                    responseBean.setMessage("Wallet balance is low : Please recharge wallet balance.");
                    return responseBean;
                }
                saleOrder.setSenderPinCode(clientWarehouse.getPinCode());
                ResponseBean<ZoneType> zoneTypeResponseBean = pincodeService.getZone(saleOrder.getSenderPinCode(), saleOrder.getConsigneePinCode());
                if(ResponseStatus.SUCCESS.equals(zoneTypeResponseBean.getStatus())){
                    saleOrder.setZoneType(zoneTypeResponseBean.getResponseBody());
                }
                // Calculate delivery charge hear
                ResponseBean<Double> rateCalculationResponse = domasticRateCardService.getCharge(saleOrder,courier, clientFacility);
                if(ResponseStatus.FAIL.equals(rateCalculationResponse.getStatus())){
                    return  rateCalculationResponse;
                }
                deliveryCharge = rateCalculationResponse.getResponseBody();
                if(clientWallet.getWalletAmount() < deliveryCharge){
                    responseBean.setStatus(ResponseStatus.FAIL);
                    responseBean.setMessage("Wallet balance is low : Please recharge wallet balance.");
                    return responseBean;
                }
            }
            saleOrder.setPickupLocationId(warehouseCode);
            // TODO if courier api exist then data auto pushed.
            // If data push api exist then push other wise not pushed.
            // If data pushed by api then status will,  UD Order Assigned
            ApiConfig apiConfig = apiConfigRepository.findByServiceProviderIdAndActiveAndApiConfigTypeAndEntityType(
                    courier.getServiceProviderId(), 1, ApiConfigType.ORDER_PUSH, EntityType.COURIER);
            ResponseBean apiPushResponse = null;
            if(apiConfig != null){
                apiPushResponse = orderPushService.callOrderPushOrderApi(saleOrder, courier, apiConfig);
                if(ResponseStatus.SUCCESS.equals(apiPushResponse.getStatus())){
                    String udOrderAssigned = applicionConfig.getMessage(AppProperty.UD_ORDER_ASSIGNED, null, null);
                    Status status = statusRepository.findByStatusCode(udOrderAssigned);
                    if(saleOrder.getCourierCode() == null || saleOrder.getCourierCode().trim().isEmpty()){
                        saleOrder.setCourierCode(courier.getCourierCode());
                    }
                    ResponseBean packetHistoryResponse = addPacketHistoryWithoutSave(saleOrder,status, null, null, null);
                    if(ResponseStatus.FAIL.equals(packetHistoryResponse.getStatus())){
                        return packetHistoryResponse;
                    }
                    // Data pushed proper then amount deduct
                    deductDeliveryCharge = true;
                }else {
                    return apiPushResponse;
                }
            }else{
                String udOrderProcess = applicionConfig.getMessage(AppProperty.UD_ORDER_PROCESS, null, null);
                Status status = statusRepository.findByStatusCode(udOrderProcess);
                saleOrder.setCourierCode(courier.getCourierCode());

                ResponseBean packetHistoryResponse = addPacketHistoryWithoutSave(saleOrder,status, null, null, null);
                if(ResponseStatus.FAIL.equals(packetHistoryResponse.getStatus())){
                    return packetHistoryResponse;
                }
                deductDeliveryCharge = true;
            }

            // Finally deduct amount from client wallet & add logs.
            if(deductDeliveryCharge && clientFacility.getWalletActive() != null && clientFacility.getWalletActive()){
                SaleOrderTransactionLog saleOrderTransactionLog = new SaleOrderTransactionLog();
                saleOrderTransactionLog.setAmount(deliveryCharge);
                saleOrderTransactionLog.setAwbNumber(saleOrder.getReferanceNo());
                saleOrderTransactionLog.setClientCode(saleOrder.getClientCode());
                saleOrderTransactionLog.setClientOrderId(saleOrder.getClientOrderId());
                saleOrderTransactionLog.setModifiedAmount(clientWallet.getWalletAmount()-deliveryCharge);
                saleOrderTransactionLog.setPreviousAmount(clientWallet.getWalletAmount());
                //saleOrderTransactionLog.setRemarks();
                Double volWeight = domasticRateCardService.calculateVolumetricWeight(saleOrder.getLength(), saleOrder.getBreadth(),saleOrder.getHight(),courier.getWeightDimentionFactor());
                if(volWeight > saleOrder.getWeight()){
                    saleOrderTransactionLog.setShipmentWeight(volWeight);
                }else {
                    saleOrderTransactionLog.setShipmentWeight(saleOrder.getWeight());
                }
                saleOrderTransactionLog.setShipmentWeight(saleOrder.getWeight());
                saleOrderTransactionLog.setTransactionType(TransactionType.DEBIT);
                saleOrderTransactionLog.setDate(SharedMethords.getCurrentDate());
                saleOrderTransactionLog.setCreateDate(new Date().getTime());

                saleOrder.setDeliveryCharge(deliveryCharge);
                saleOrderRepository.save(saleOrder);
                // Deduct amount from client wallet
                clientWallet.setWalletAmount(clientWallet.getWalletAmount()-deliveryCharge);
                clientWalletRepository.save(clientWallet);
                saleOrderTransactionLogRepository.save(saleOrderTransactionLog);
            }
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setResponseBody(ResponseStatus.SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(e.getMessage());
        }
        return responseBean;
    }

    @Override
    public ResponseBean assigneeCourierBulk(Map<String, Object> map) {
        ResponseBean responseBean = new ResponseBean();
        try{
            if(map == null || map.isEmpty()){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Request body is empty.");
                return responseBean;
            }
            if(map.get("awbList") == null){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("awbList field is empty or null.");
                return responseBean;
            }
            if(map.get("courierCode") == null || map.get("courierCode").toString().isEmpty()){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("courierCode field is empty or null.");
                return responseBean;
            }
            if(map.get("warehouseCode") == null || map.get("warehouseCode").toString().isEmpty()){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Warehouse code field is empty or null.");
                return responseBean;
            }
            List<Map<String, String>> errorMapList = new ArrayList<Map<String, String>>();
            List<String> awbList = (List<String>) map.get("awbList");
            for(String awb : awbList){
                ResponseBean responseBean1 = assigneeCourier(awb, map.get("courierCode").toString(), map.get("warehouseCode").toString(), null);
                if(ResponseStatus.FAIL.equals(responseBean1.getStatus())){
                    Map<String,String> errorMap = new LinkedHashMap<String,String>();
                    errorMap.put("AWB",awb);
                    errorMap.put("MESSAGE", responseBean1.getMessage());
                    errorMapList.add(errorMap);
                    continue;
                }else if(ResponseStatus.SUCCESS.equals(responseBean1.getStatus())){
                    Map<String,String> errorMap = new LinkedHashMap<String,String>();
                    errorMap.put("AWB",awb);
                    errorMap.put("MESSAGE", ResponseStatus.SUCCESS.name());
                    errorMapList.add(errorMap);
                }
            }
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setResponseBody(errorMapList.isEmpty() ? null : errorMapList);
        }catch (Exception e){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Server internal error.");
            e.printStackTrace();
        }
        return responseBean;
    }

    @Override
    public ResponseBean assigneeCourierByPriority(Map<String, Object> map) {
        ResponseBean responseBean = new ResponseBean();
        try{
            if(map == null || map.isEmpty()){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Request body is empty.");
                return responseBean;
            }
            if(map.get("awbList") == null){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("awbList field is empty or null.");
                return responseBean;
            }
            if(map.get("serviceType") == null || map.get("serviceType").toString().isEmpty()){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Service Type field is empty.");
                return responseBean;
            }
            if(map.get("warehouseCode") == null || map.get("warehouseCode").toString().isEmpty()){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Warehouse code field is empty or null.");
                return responseBean;
            }
            List<Map<String, String>> errorMapList = new ArrayList<Map<String, String>>();
            String serviceTypeCode = map.get("serviceType").toString();
            List<String> awbList = (List<String>) map.get("awbList");
            if(!awbList.isEmpty()){
                for(String awbNumber : awbList){
                    SaleOrder saleOrder = saleOrderRepository.findByReferanceNo(awbNumber);
                    if(saleOrder != null){
                        CourierPriority courierPriority = courierPriorityRepository.findByCourierPriorityCode(saleOrder.getClientCode()+"_"+serviceTypeCode);
                        if(courierPriority == null){
                            Map<String,String> errorMap = new LinkedHashMap<String,String>();
                            errorMap.put("AWB",awbNumber);
                            errorMap.put("MESSAGE", "Client ( " + saleOrder.getClientCode() + ") courier priority not found");
                            errorMapList.add(errorMap);
                            continue;
                        }
                        for(String courierCode : courierPriority.getPrioritys().split(",")){
                            ResponseBean responseBean1 = courierService.isCourierServiceable(saleOrder.getCourierCode(), saleOrder.getPaymentType(), saleOrder.getSenderPinCode(), saleOrder.getDestinationBranchCode());
                            if(ResponseStatus.FAIL.equals(responseBean1.getStatus())){
                                Map<String,String> errorMap = new LinkedHashMap<String,String>();
                                errorMap.put("AWB",awbNumber);
                                errorMap.put("MESSAGE", responseBean1.getMessage());
                                errorMapList.add(errorMap);
                                continue;
                            }
                            ResponseBean responseBean2 = assigneeCourier(awbNumber, courierCode, map.get("warehouseCode").toString(), saleOrder);
                            if(ResponseStatus.FAIL.equals(responseBean2.getStatus())){
                                Map<String,String> errorMap = new LinkedHashMap<String,String>();
                                errorMap.put("AWB", awbNumber);
                                errorMap.put("MESSAGE", responseBean2.getMessage());
                                errorMapList.add(errorMap);
                                continue;
                            }else if(ResponseStatus.SUCCESS.equals(responseBean2.getStatus())){
                                Map<String,String> errorMap = new LinkedHashMap<String,String>();
                                errorMap.put("AWB", awbNumber);
                                errorMap.put("MESSAGE", ResponseStatus.SUCCESS.name());
                                errorMapList.add(errorMap);
                                continue;
                            }
                        }
                    }
                }
            }
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setResponseBody(errorMapList.isEmpty() ? null : errorMapList);
        }catch (Exception e){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Server internal error.");
            e.printStackTrace();
        }
        return responseBean;
    }

    @Override
    public DataTablesOutput<SaleOrder> getAllOrderProcess(DataTablesInput input) {
        String udOrderProcess = applicionConfig.getMessage(AppProperty.UD_ORDER_PROCESS, null, null);
        input.addColumn("currentStatus.statusCode", true, false, udOrderProcess);
        if(input.getColumn("id") == null){
            input.addColumn("id", true, true,null);
        }
        input.addOrder("id",false);
        if(sessionUserBean.getUser().getType().equals(UserType.CLIENT)){
            input.addColumn("clientCode", true, false, sessionUserBean.getUser().getClientCode());
        }
        return saleOrderRepository.findAll(input);
    }

    @Override
    public DataTablesOutput<SaleOrder> getAllOrderAssigned(DataTablesInput input) {
        String udOrderAssigned = applicionConfig.getMessage(AppProperty.UD_ORDER_ASSIGNED, null, null);
        input.addColumn("currentStatus.statusCode", true, false, udOrderAssigned);
        if(input.getColumn("id") == null){
            input.addColumn("id", true, true,null);
        }
        input.addOrder("id",false);
        if(sessionUserBean.getUser().getType().equals(UserType.CLIENT)){
            input.addColumn("clientCode", true, false, sessionUserBean.getUser().getClientCode());
        }
        return saleOrderRepository.findAll(input);
    }

    @Override
    public DataTablesOutput<SaleOrder> getAllPendingFor3PlManifest(DataTablesInput input) {
        ResponseBean responseBean = new ResponseBean();
        List<SaleOrder> saleOrderList = null;
        Specification<SaleOrder> specification = Specification
                .where(SaleOrderSpecification.courierAwbNotNull())
                .and(SaleOrderSpecification.courierCodeNotNull());
        if(UserType.CLIENT.equals(sessionUserBean.getUser().getType())){
            input.addColumn("handOver", true, false, "false");
            input.addColumn("threePlManifestGenerated", true, false, "false");
            input.addColumn("clientCode", true, false, sessionUserBean.getUser().getClientCode());
        }else{
            input.addColumn("handOver", true, false, "false");
            input.addColumn("threePlManifestGenerated", true, false, "false");
        }
        return saleOrderRepository.findAll(input, specification);
    }

    @Override
    public PacketHistory getFirstAttemptedHistory(SaleOrder saleOrder) {
        if(saleOrder == null || saleOrder.getPacketHistory() == null || saleOrder.getPacketHistory().isEmpty()){
            return  null;
        }
        String deliveryAttemptedCode = applicionConfig.getMessage(AppProperty.UD_DELIVERY_ATTEMPTED, null, null);
        for(PacketHistory packetHistory : saleOrder.getPacketHistory()){
            if(deliveryAttemptedCode.equals(packetHistory.getToStatusCode())){
                return packetHistory;
            }
        }
        return null;
    }

    @Override
    public PacketHistory getLastAttemptedHistory(SaleOrder saleOrder) {
        if(saleOrder == null || saleOrder.getPacketHistory() == null || saleOrder.getPacketHistory().isEmpty()){
            return  null;
        }
        String deliveryAttemptedCode = applicionConfig.getMessage(AppProperty.UD_DELIVERY_ATTEMPTED, null, null);
        List<PacketHistory> packetHistoryList = saleOrder.getPacketHistory();
        Collections.reverse(packetHistoryList);
        for(PacketHistory packetHistory : packetHistoryList){
            if(deliveryAttemptedCode.equals(packetHistory.getToStatusCode())){
                return packetHistory;
            }
        }
        return null;
    }

    @Override
    public ResponseEntity<Resource> downloadOrderReceivedReport() {
        try {
            String udOrderRecived = applicionConfig.getMessage(AppProperty.UD_ORDER_RECEIVED, null, null);
            List<String> awbList = null;
            if(sessionUserBean.getUser().getType().equals(UserType.CLIENT)){
                awbList = MasterReportServiceImpl.getAwbNumbersFromObject(saleOrderRepository.getAllAwbByCurrentStatusAndClientCode(udOrderRecived,sessionUserBean.getUser().getClientCode()));
            }else {
                awbList = MasterReportServiceImpl.getAwbNumbersFromObject(saleOrderRepository.getAllAwbByCurrentStatus(udOrderRecived));
            }
           return mastertReportService.generateReports(awbList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    @Override
    public ResponseEntity<Resource> downloadOrderProcessReport() {
        try {
            String udOrderRecived = applicionConfig.getMessage(AppProperty.UD_ORDER_PROCESS, null, null);
            List<String> awbList = null;
            if(sessionUserBean.getUser().getType().equals(UserType.CLIENT)){
                awbList = MasterReportServiceImpl.getAwbNumbersFromObject(saleOrderRepository.getAllAwbByCurrentStatusAndClientCode(udOrderRecived,sessionUserBean.getUser().getClientCode()));
            }else {
                awbList = MasterReportServiceImpl.getAwbNumbersFromObject(saleOrderRepository.getAllAwbByCurrentStatus(udOrderRecived));
            }
            return mastertReportService.generateReports(awbList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    @Override
    public ResponseEntity<Resource> downloadOrderAssignedReport() {
        try {
            String udOrderRecived = applicionConfig.getMessage(AppProperty.UD_ORDER_ASSIGNED, null, null);
            List<String> awbList = null;
            if(sessionUserBean.getUser().getType().equals(UserType.CLIENT)){
                awbList = MasterReportServiceImpl.getAwbNumbersFromObject(saleOrderRepository.getAllAwbByCurrentStatusAndClientCode(udOrderRecived,sessionUserBean.getUser().getClientCode()));
            }else {
                awbList = MasterReportServiceImpl.getAwbNumbersFromObject(saleOrderRepository.getAllAwbByCurrentStatus(udOrderRecived));
            }
            return mastertReportService.generateReports(awbList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    @Override
    public DataTablesOutput<SaleOrder> getAllPendingForClientRemittance(DataTablesInput input) {
        String deliveredCode = applicionConfig.getMessage(AppProperty.UD_DELIVERED, null, null);
        input.addColumn("clientRemittance", true, false, "NULL");
        input.addColumn("paymentType", true, false, PaymentType.COD.name());
        input.addColumn("currentStatus.statusCode", true, false, deliveredCode);
        if(sessionUserBean.getUser().getType().equals(UserType.CLIENT)){
            input.addColumn("clientCode", true, false, sessionUserBean.getUser().getClientCode());
        }
        return saleOrderRepository.findAll(input);
    }

    @Override
    public DataTablesOutput<SaleOrder> getAllPendingForCourierRemittance(DataTablesInput input) {
        String deliveredCode = applicionConfig.getMessage(AppProperty.UD_DELIVERED, null, null);
        //input.addColumn("courierCode", true, false, ">=0");
        input.addColumn("courierRemittance", true, false, null);
        input.addColumn("paymentType", true, false, PaymentType.COD.name());
        input.addColumn("currentStatus.statusCode", true, false, deliveredCode);
        if(sessionUserBean.getUser().getType().equals(UserType.CLIENT)){
            input.addColumn("clientCode", true, false, sessionUserBean.getUser().getClientCode());
        }
        return saleOrderRepository.findAll(input);
    }


    @Override
    public ResponseBean<Map<String, SaleOrder>> tracking(TrackingType trackingType, String searchValue) {
        ResponseBean responseBean = new ResponseBean();
        try{
            Map<String,Object> result = new HashMap<String,Object>();
            if(trackingType == null ){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Tracking type con not null.");
                return responseBean;
            }
            if(searchValue == null || searchValue.trim().isEmpty()){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Please fill track value.");
                return responseBean;
            }
            List<String> searchValues = new ArrayList<>(Arrays.asList(searchValue.replaceAll("\\s", "").split(",")));
            /*if(searchValues.size()>50){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Tracking value should not more than 50");
                return responseBean;
            }*/
            List<String> notFound = new ArrayList<>();
            if(TrackingType.AWB_NUMBER.equals(trackingType)){
                List<SaleOrder>  awbNumbers = saleOrderRepository.findByReferanceNoIn(searchValues);
                if (awbNumbers.size()==0) {
                    responseBean.setStatus(ResponseStatus.FAIL);
                    responseBean.setMessage(messageSource.getMessage(MessageProperty.AWB_NUMBER_NOT_FOUND, null, null));
                    return responseBean;
                }
                List<String> foundAwb = new ArrayList<>();
                awbNumbers.forEach(saleOrder -> {
                    foundAwb.add(saleOrder.getReferanceNo());
                    List<PacketHistory> packetHistories = saleOrder.getPacketHistory();
                    if(packetHistories != null && !packetHistories.isEmpty()){
                        packetHistories.forEach(packetHistory -> {
                            if(packetHistory != null && packetHistory.getToStatusCode() != null){
                                Status status = statusRepository.findByStatusCode(packetHistory.getToStatusCode());
                                if(status != null){
                                    packetHistory.setStatusName(status.getStatusName());
                                }
                            }
                        });
                    }
                });
                searchValues.removeAll(foundAwb);
                result.put("FOUND", awbNumbers);
                result.put("NOT_FOUND", searchValues);
                responseBean.setStatus(ResponseStatus.SUCCESS);
                responseBean.setResponseBody(result);
                return responseBean;
            } else if (TrackingType.COURIER_AWB.equals(trackingType)){
                List<SaleOrder>  saleOrderList = saleOrderRepository.findByCourierAWBNumberIn(searchValues);
                if (saleOrderList.size()==0) {
                    responseBean.setStatus(ResponseStatus.FAIL);
                    responseBean.setMessage("Courier AWB number not found in system");
                    return responseBean;
                }
                List<String> foundAwb = new ArrayList<>();
                saleOrderList.forEach(saleOrder -> {
                    foundAwb.add(saleOrder.getCourierAWBNumber());
                    List<PacketHistory> packetHistories = saleOrder.getPacketHistory();
                    if(packetHistories != null && !packetHistories.isEmpty()){
                        packetHistories.forEach(packetHistory -> {
                            if(packetHistory != null && packetHistory.getToStatusCode() != null){
                                Status status = statusRepository.findByStatusCode(packetHistory.getToStatusCode());
                                if(status != null){
                                    packetHistory.setStatusName(status.getStatusName());
                                }
                            }
                        });
                    }
                });
                searchValues.removeAll(foundAwb);
                result.put("FOUND", saleOrderList);
                result.put("NOT_FOUND", searchValues);
                responseBean.setStatus(ResponseStatus.SUCCESS);
                responseBean.setResponseBody(result);
                return responseBean;
            }else if(TrackingType.CLIENT_ORDER_ID.equals(trackingType)){
                List<SaleOrder>  saleOrderList= saleOrderRepository.findByClientOrderIdIn(searchValues);
                if (saleOrderList.size()==0) {
                    responseBean.setStatus(ResponseStatus.FAIL);
                    responseBean.setMessage("Client Order Id not found in system");
                    return responseBean;
                }
                List<String> foundAwb = new ArrayList<>();
                saleOrderList.forEach(saleOrder -> {
                    foundAwb.add(saleOrder.getClientOrderId());
                    List<PacketHistory> packetHistories = saleOrder.getPacketHistory();
                    if(packetHistories != null && !packetHistories.isEmpty()){
                        packetHistories.forEach(packetHistory -> {
                            if(packetHistory != null && packetHistory.getToStatusCode() != null){
                                Status status = statusRepository.findByStatusCode(packetHistory.getToStatusCode());
                                if(status != null){
                                    packetHistory.setStatusName(status.getStatusName());
                                }
                            }
                        });
                    }
                });
                searchValues.removeAll(foundAwb);
                result.put("FOUND", saleOrderList);
                result.put("NOT_FOUND", searchValues);
                responseBean.setStatus(ResponseStatus.SUCCESS);
                responseBean.setResponseBody(result);
                return responseBean;
            }
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Search type not found.");
        }catch (Exception e){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(e.getMessage());
            e.printStackTrace();
        }
        return responseBean;
    }

    @Override
    public DataTablesOutput<SaleOrder> getInTransitOrder(DataTablesInput input) {
        String udInTransit = applicionConfig.getMessage(AppProperty.UD_IN_TRANSIT, null, null);
        input.addColumn("currentStatus.statusCode", true, false, udInTransit);
        if(sessionUserBean.getUser().getType().equals(UserType.CLIENT)){
            input.addColumn("clientCode", true, false, sessionUserBean.getUser().getClientCode());
        }
        return saleOrderRepository.findAll(input);
    }

    @Override
    public DataTablesOutput<SaleOrder> getDeliverdOrder(DataTablesInput input) {
        String udDeliverd = applicionConfig.getMessage(AppProperty.UD_DELIVERED, null, null);
        input.addColumn("currentStatus.statusCode", true, false, udDeliverd);
        if(sessionUserBean.getUser().getType().equals(UserType.CLIENT)){
            input.addColumn("clientCode", true, false, sessionUserBean.getUser().getClientCode());
        }
        return saleOrderRepository.findAll(input);
    }

    @Override
    public DataTablesOutput<SaleOrder> getOutForDeliveryOrder(DataTablesInput input) {
        String udOutForDelivery = applicionConfig.getMessage(AppProperty.UD_OUT_FOR_DELIVERY, null, null);
        input.addColumn("currentStatus.statusCode", true, false, udOutForDelivery);
        if(sessionUserBean.getUser().getType().equals(UserType.CLIENT)){
            input.addColumn("clientCode", true, false, sessionUserBean.getUser().getClientCode());
        }
        return saleOrderRepository.findAll(input);
    }

    @Override
    public DataTablesOutput<SaleOrder> getRTOOrder(DataTablesInput input) {
        List<String> statusList = new ArrayList<String>();
        statusList.add(applicionConfig.getMessage(AppProperty.RT_RETURNED, null, null));
        statusList.add(applicionConfig.getMessage(AppProperty.RT_IN_TRANSIT, null, null));
        statusList.add(applicionConfig.getMessage(AppProperty.RT_OUT_FOR_DELIVERY, null, null));
        statusList.add(applicionConfig.getMessage(AppProperty.RT_DELIVERED, null, null));
        statusList.add(applicionConfig.getMessage(AppProperty.RT_DELIVERY_ATTEMPTED, null, null));
        // TODO pending for query
        Specification<SaleOrder> specification = Specification.where(SaleOrderSpecification.currentStatusIn(statusList));
        /*input.addColumn("currentStatus.statusCode", true, false, rtRetured);
        input.addColumn("currentStatus.statusCode", true, false, rtInTransit);
        input.addColumn("currentStatus.statusCode", true, false, rtOutForDelivery);
        input.addColumn("currentStatus.statusCode", true, false, rtDelivered);
        input.addColumn("currentStatus.statusCode", true, false, rtDeliveryAttempted);*/
        if(sessionUserBean.getUser().getType().equals(UserType.CLIENT)){
            input.addColumn("clientCode", true, false, sessionUserBean.getUser().getClientCode());
        }
        return saleOrderRepository.findAll(input,specification);
    }
    @Override
    public DataTablesOutput<SaleOrder> getCancelledOrder(DataTablesInput input) {
        String udCancelled = applicionConfig.getMessage(AppProperty.UD_CANCELLED, null, null);
        input.addColumn("currentStatus.statusCode", true, false, udCancelled);
        if(sessionUserBean.getUser().getType().equals(UserType.CLIENT)){
            input.addColumn("clientCode", true, false, sessionUserBean.getUser().getClientCode());
        }
        return saleOrderRepository.findAll(input);
    }

    @Override
    public DataTablesOutput<SaleOrder> getDeliveryAttemptedOrder(DataTablesInput input) {
        String udDeliveryAttempted = applicionConfig.getMessage(AppProperty.UD_DELIVERY_ATTEMPTED, null, null);
        input.addColumn("currentStatus.statusCode", true, false, udDeliveryAttempted);
        if(sessionUserBean.getUser().getType().equals(UserType.CLIENT)){
            input.addColumn("clientCode", true, false, sessionUserBean.getUser().getClientCode());
        }
        return saleOrderRepository.findAll(input);
    }

    @Override
    public ResponseEntity<Resource> downloadInTransitOrderReport() {
        try {
            String udInTransit = applicionConfig.getMessage(AppProperty.UD_IN_TRANSIT, null, null);
            List<String> awbList = null;
            if(sessionUserBean.getUser().getType().equals(UserType.CLIENT)){
                awbList = MasterReportServiceImpl.getAwbNumbersFromObject(saleOrderRepository.getAllAwbByCurrentStatusAndClientCode(udInTransit,sessionUserBean.getUser().getClientCode()));
            }else {
                awbList = MasterReportServiceImpl.getAwbNumbersFromObject(saleOrderRepository.getAllAwbByCurrentStatus(udInTransit));
            }
            return mastertReportService.generateReports(awbList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    @Override
    public ResponseEntity<Resource> downloadDeliverdOrderReport() {
        try {
            String udDeliverd = applicionConfig.getMessage(AppProperty.UD_DELIVERED, null, null);
            List<String> awbList = null;
            if(sessionUserBean.getUser().getType().equals(UserType.CLIENT)){
                awbList = MasterReportServiceImpl.getAwbNumbersFromObject(saleOrderRepository.getAllAwbByCurrentStatusAndClientCode(udDeliverd,sessionUserBean.getUser().getClientCode()));
            }else {
                awbList = MasterReportServiceImpl.getAwbNumbersFromObject(saleOrderRepository.getAllAwbByCurrentStatus(udDeliverd));
            }
            return mastertReportService.generateReports(awbList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    @Override
    public ResponseEntity<Resource> downloadOutForDeliveryOrderReport() {
        try {
            String udOutForDelivery = applicionConfig.getMessage(AppProperty.UD_OUT_FOR_DELIVERY, null, null);
            List<String> awbList = null;
            if(sessionUserBean.getUser().getType().equals(UserType.CLIENT)){
                awbList = MasterReportServiceImpl.getAwbNumbersFromObject(saleOrderRepository.getAllAwbByCurrentStatusAndClientCode(udOutForDelivery,sessionUserBean.getUser().getClientCode()));
            }else {
                awbList = MasterReportServiceImpl.getAwbNumbersFromObject(saleOrderRepository.getAllAwbByCurrentStatus(udOutForDelivery));
            }
            return mastertReportService.generateReports(awbList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    @Override
    public ResponseEntity<Resource> downloadRTOOrderReport() {
        try {
            String udOutForDelivery = applicionConfig.getMessage(AppProperty.UD_OUT_FOR_DELIVERY, null, null);
            List<String> awbList = null;
            if(sessionUserBean.getUser().getType().equals(UserType.CLIENT)){
                awbList = MasterReportServiceImpl.getAwbNumbersFromObject(saleOrderRepository.getAllAwbByCurrentStatusAndClientCode(udOutForDelivery,sessionUserBean.getUser().getClientCode()));
            }else {
                awbList = MasterReportServiceImpl.getAwbNumbersFromObject(saleOrderRepository.getAllAwbByCurrentStatus(udOutForDelivery));
            }
            return mastertReportService.generateReports(awbList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    @Override
    public ResponseEntity<Resource> downloadCancelledOrderReport() {
        try {
            String udCancelled = applicionConfig.getMessage(AppProperty.UD_CANCELLED, null, null);
            List<String> awbList = null;
            if(sessionUserBean.getUser().getType().equals(UserType.CLIENT)){
                awbList = MasterReportServiceImpl.getAwbNumbersFromObject(saleOrderRepository.getAllAwbByCurrentStatusAndClientCode(udCancelled,sessionUserBean.getUser().getClientCode()));
            }else {
                awbList = MasterReportServiceImpl.getAwbNumbersFromObject(saleOrderRepository.getAllAwbByCurrentStatus(udCancelled));
            }
            return mastertReportService.generateReports(awbList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    @Override
    public ResponseEntity<Resource> downloadDeliveryAttemptedOrderReport() {
        try {
            String udDeliveryAttempted = applicionConfig.getMessage(AppProperty.UD_DELIVERY_ATTEMPTED, null, null);
            List<String> awbList = null;
            if(sessionUserBean.getUser().getType().equals(UserType.CLIENT)){
                awbList = MasterReportServiceImpl.getAwbNumbersFromObject(saleOrderRepository.getAllAwbByCurrentStatusAndClientCode(udDeliveryAttempted,sessionUserBean.getUser().getClientCode()));
            }else {
                awbList = MasterReportServiceImpl.getAwbNumbersFromObject(saleOrderRepository.getAllAwbByCurrentStatus(udDeliveryAttempted));
            }
            return mastertReportService.generateReports(awbList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }
}
