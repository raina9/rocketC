package com.weblearnex.app.service.impl;

import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.*;
import com.weblearnex.app.datatable.reposatory.*;
import com.weblearnex.app.entity.master.BulkMaster;
import com.weblearnex.app.entity.master.ServicablePincode;
import com.weblearnex.app.entity.order.PacketHistory;
import com.weblearnex.app.entity.order.SaleOrder;
import com.weblearnex.app.entity.order.SaleOrderTransactionLog;
import com.weblearnex.app.entity.paymentwetway.ClientWallet;
import com.weblearnex.app.entity.setup.Configration;
import com.weblearnex.app.entity.setup.Courier;
import com.weblearnex.app.entity.setup.Status;
import com.weblearnex.app.model.BulkUploadBean;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.*;
import com.weblearnex.app.utils.SharedMethords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

@Service
public class SupportServiceImpl implements SupportService {

    @Autowired
    private SaleOrderRepository saleOrderRepository;

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private ServicablePincodeService servicablePincodeService;

    @Autowired
    private ConfigrationRepository configrationRepository;

    @Autowired
    private ClientWarehouseRepository clientWarehouseRepository;

    @Autowired
    private SaleOrderService saleOrderService;

    @Autowired
    @Qualifier("applicionConfig")
    private MessageSource applicionConfig;

    @Autowired
    private BulkMasterService bulkMasterService;

    @Autowired
    private ClientWalletRepository clientWalletRepository;

    @Autowired
    private SaleOrderTransactionLogService saleOrderTransactionLogService;

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
            String courierCode= map.get(BulkHeaderConstant.COURIER_CODE);
            String courierAWBNumber= map.get(BulkHeaderConstant.COURIER_AWB_NUMBER);

            String consigneeName= map.get(BulkHeaderConstant.CONSIGNEE_NAME);
            String consigneeMobileNumber= map.get(BulkHeaderConstant.CONSIGNEE_MOBILE_NO);
            String consigneeAlternateNumber= map.get(BulkHeaderConstant.CONSIGNEE_PHONE_NO);
            String consigneePinCode = map.get(BulkHeaderConstant.CONSIGNEE_PINCODE);
            String consigneeAddress = map.get(BulkHeaderConstant.CONSIGNEE_ADDRESS);
            String consigneeLandmark = map.get(BulkHeaderConstant.CONSIGNEE_LANDMARK);
            String pickupLocationId = map.get(BulkHeaderConstant.PICKUP_LOCATION_ID);
            String zoneType = map.get(BulkHeaderConstant.ZONE_TYPE);
            Double productPrice =map.get(BulkHeaderConstant.PRODUCT_PRICE) != null ? Double.parseDouble(map.get(BulkHeaderConstant.PRODUCT_PRICE).trim()) : null;

            if(awbNub == null || awbNub.trim().equals("")){
                map.put(BulkHeaderConstant.MESSAGE,"Awb Number is empty.");
                errorRecord.add(map);
                continue;
            }
            SaleOrder saleOrder = saleOrderRepository.findByReferanceNo(awbNub);
            if(saleOrder==null){
                map.put(BulkHeaderConstant.MESSAGE,"Awb number not found in database.");
                errorRecord.add(map);
                continue;
            }

            if( courierCode!=null && !courierCode.trim().isEmpty()){
                if(courierRepository.findByCourierCode(courierCode.trim())==null){
                    map.put(BulkHeaderConstant.MESSAGE,"Courier code not found in database.");
                    errorRecord.add(map);
                    continue;
                }
                saleOrder.setCourierCode(courierCode.trim());
            }
            if(courierAWBNumber!=null && !courierAWBNumber.trim().isEmpty()){
                if(saleOrderRepository.existsSaleOrderByCourierAWBNumber(courierAWBNumber.trim())){
                    map.put(BulkHeaderConstant.MESSAGE,"Courier awb number already assigned.");
                    errorRecord.add(map);
                    continue;
                }
                saleOrder.setCourierAWBNumber(courierAWBNumber.trim());
            }
            if(consigneeName!=null && !consigneeName.trim().isEmpty()){
                saleOrder.setConsigneeName(consigneeName.trim());
            }
            if(consigneeMobileNumber!=null && !consigneeMobileNumber.trim().isEmpty()){
                saleOrder.setConsigneeMobileNumber(consigneeMobileNumber);
            }
            if(consigneeAlternateNumber!=null && !consigneeAlternateNumber.trim().isEmpty()){
                saleOrder.setConsigneeAlternateNumber(consigneeAlternateNumber);
            }
            if(consigneePinCode!=null && !consigneePinCode.trim().isEmpty()){
                ResponseBean responseBean = servicablePincodeService.isDropActive(consigneePinCode.trim(), saleOrder.getPaymentType());
                if(ResponseStatus.FAIL.equals(responseBean.getStatus())){
                    map.put(BulkHeaderConstant.MESSAGE,"consignee pincode not servicable.");
                    errorRecord.add(map);
                    continue;
                }
                saleOrder.setConsigneePinCode(consigneePinCode.trim());
            }
            if(consigneeAddress!=null && !consigneeAddress.trim().isEmpty()){
                saleOrder.setConsigneeAddress(consigneeAddress.trim());
            }
            if(consigneeLandmark!=null && !consigneeLandmark.trim().isEmpty()){
                saleOrder.setConsigneeLandmark(consigneeLandmark.trim());
            }
            if(pickupLocationId!=null && !pickupLocationId.trim().isEmpty()){
                if(clientWarehouseRepository.findByWarehouseCode(pickupLocationId.trim())== null){
                    map.put(BulkHeaderConstant.MESSAGE,"Pickup location ID not found in system.");
                    errorRecord.add(map);
                    continue;
                }
                saleOrder.setPickupLocationId(pickupLocationId.trim());
            }
            if(zoneType!=null){
                try{
                    ZoneType zoneType1 = ZoneType.valueOf(zoneType);
                }catch (Exception e){
                    map.put(BulkHeaderConstant.MESSAGE,"Invalid zone type, zone type will be in "+ ZoneType.values());
                    errorRecord.add(map);
                    continue;
                }
            }
            if(productPrice!=null && productPrice> 0d){
                if(PaymentType.COD.equals(saleOrder.getPaymentType())){
                    String udOrderOFD = applicionConfig.getMessage(AppProperty.UD_OUT_FOR_DELIVERY, null, null);
                    String udOrderDelivered = applicionConfig.getMessage(AppProperty.UD_DELIVERED, null, null);
                    if(saleOrder.getCurrentStatus().getStatusCode().equals(udOrderDelivered)){
                        map.put(BulkHeaderConstant.MESSAGE,"Shipment is Delivered so product price can not changed");
                        errorRecord.add(map);
                        continue;
                    }
                    if(saleOrder.getCurrentStatus().getStatusCode().equals(udOrderOFD)){
                        map.put(BulkHeaderConstant.MESSAGE,"Shipment is out for delivery so product price can not changed");
                        errorRecord.add(map);
                        continue;
                    }
                    saleOrder.setCodAmount(productPrice);
                }
                saleOrder.setProductPrice(productPrice);
            }
            saleOrderRepository.save(saleOrder);

            successRecord.add(map);
        }
        bulkUploadBean.setBulkMaster(bulkMaster);
        bulkUploadBean.setErrorRecords(errorRecord);
        bulkUploadBean.setSuccessRecords(successRecord);
        bulkMasterService.setUploadProgressCount(bulkMaster.getName(), 100, token, true);
        return bulkUploadBean;
    }


    @Override
    public ResponseBean<SaleOrder> orderCancellation(String awbNo) {
        ResponseBean responseBean = new ResponseBean();
        if(awbNo==null || awbNo.trim().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("AWB Number can not blank");
            return responseBean;

        }
        List<String> awbNumber =Arrays.asList(awbNo.trim().split(","));
        List<SaleOrder> awbList = saleOrderRepository.findByReferanceNoIn(awbNumber);
        if(awbList.size()==0){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("AWB Number  not found in Database");
            return responseBean;
        }
        List<Map<String,String>> result = new ArrayList<Map<String,String>>();
        for(SaleOrder saleOrder:awbList){
            String orderReceived = applicionConfig.getMessage(AppProperty.UD_ORDER_RECEIVED, null, null);
            String orderProcess = applicionConfig.getMessage(AppProperty.UD_ORDER_PROCESS, null, null);
            String orderAssigned = applicionConfig.getMessage(AppProperty.UD_ORDER_ASSIGNED, null, null);
            if(!(saleOrder.getCurrentStatus().getStatusCode().equals(orderReceived) ||
                    saleOrder.getCurrentStatus().getStatusCode().equals(orderProcess) ||
                    saleOrder.getCurrentStatus().getStatusCode().equals(orderAssigned)
                    /*saleOrder.getHandOver() == true*/)){
                Map<String,String> map = new LinkedHashMap<String,String>(2);
                map.put("AWB", saleOrder.getReferanceNo());
                map.put("MESSAGE", "Order cancel failed, Order not in hand.");
                result.add(map);
                continue;
            }
            Status udCancle = statusRepository.findByStatusCode(applicionConfig.getMessage(AppProperty.UD_CANCELLED, null, null));
            ResponseBean addHistoryResponse = saleOrderService.addPacketHistoryWithoutSave(saleOrder, udCancle, null, null, null);
            if(ResponseStatus.FAIL.equals(addHistoryResponse.getStatus())){
                Map<String,String> map = new LinkedHashMap<String,String>(2);
                map.put("AWB", saleOrder.getReferanceNo());
                map.put("MESSAGE",  addHistoryResponse.getMessage());
                result.add(map);
                continue;
            }
            // If amount deduct from client account then refund amount. & mentain log
            if(saleOrder.getDeliveryCharge() != null && saleOrder.getDeliveryCharge() > 0.0d){
                ClientWallet clientWallet = clientWalletRepository.findByClientCode(saleOrder.getClientCode());
                if(clientWallet == null){
                    Map<String,String> map = new LinkedHashMap<String,String>(2);
                    map.put("AWB", saleOrder.getReferanceNo());
                    map.put("MESSAGE",  "Client wallet object not found.");
                    result.add(map);
                    continue;
                }
                double amount = clientWallet.getWalletAmount() != null ? clientWallet.getWalletAmount() : 0.0d;
                clientWallet.setWalletAmount(amount + saleOrder.getDeliveryCharge());

                // Add log
                SaleOrderTransactionLog saleOrderTransactionLog = new SaleOrderTransactionLog();
                saleOrderTransactionLog.setClientCode(saleOrder.getClientCode());
                saleOrderTransactionLog.setAwbNumber(saleOrder.getReferanceNo());
                saleOrderTransactionLog.setClientOrderId(saleOrder.getClientOrderId());
                saleOrderTransactionLog.setAmount(saleOrder.getDeliveryCharge());
                saleOrderTransactionLog.setPreviousAmount(amount);
                saleOrderTransactionLog.setModifiedAmount(amount + saleOrder.getDeliveryCharge());
                saleOrderTransactionLog.setShipmentWeight(saleOrder.getChargeableWeight());
                saleOrderTransactionLog.setRemarks("Order cancel amount refund.");
                saleOrderTransactionLog.setDate(SharedMethords.getCurrentDate());
                saleOrderTransactionLog.setTransactionType(TransactionType.CREDIT);
                saleOrderTransactionLog.setCreateDate(new Date().getTime());
                ResponseBean logSaveResponse = saleOrderTransactionLogService.addSaleOrderTansaction(saleOrderTransactionLog);
                if(ResponseStatus.FAIL.equals(logSaveResponse.getStatus())){
                    Map<String,String> map = new LinkedHashMap<String,String>(2);
                    map.put("AWB", saleOrder.getReferanceNo());
                    map.put("MESSAGE",  logSaveResponse.getMessage());
                    result.add(map);
                    continue;
                }
                clientWalletRepository.save(clientWallet);
            }

            saleOrderRepository.save(saleOrder);
            Map<String,String> map = new LinkedHashMap<String,String>(2);
            map.put("AWB", saleOrder.getReferanceNo());
            map.put("MESSAGE",  ResponseStatus.SUCCESS.name());
            result.add(map);
        }
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setResponseBody(result.isEmpty() ? null : result);
        return responseBean;
    }

    @Override
    public ResponseBean<Map<String, Object>> getDeclaredClassFields() {
        ResponseBean<Map<String,Object>> responseBean = new ResponseBean();
        try{
            Class<?> className = Class.forName("com.weblearnex.app.entity.order.SaleOrder");
            Map<String, Object> resultMap = new HashMap<String, Object>();
            if (className != null) {
                Field[] allFields = className.getDeclaredFields();
                for (Field field : allFields) {
                    if (Modifier.isPrivate(field.getModifiers())) {
                        if ((field.getType() + "").equals("class java.lang.String")
                                || (field.getType() + "").equals("long") || (field.getType() + "").equals("int")
                                || (field.getType() + "").equals("boolean")
                                || (field.getType() + "").equals("class java.lang.Boolean")
                                || (field.getType() + "").equals("class java.lang.Integer")
                                || (field.getType() + "").equals("class java.lang.Long")
                                || (field.getType() + "").equals("double")
                                || (field.getType() + "").equals("class java.lang.Double")) {
                            String fieldType = field.getType() + "";
                            resultMap.put(field.getName(),
                                    fieldType.indexOf('.') > -1
                                            ? fieldType.substring(fieldType.lastIndexOf('.') + 1, fieldType.length())
                                            : fieldType);
                        }
                    }
                }
            }
            if (resultMap.size() > 0) {
                responseBean.setStatus(ResponseStatus.SUCCESS);
                responseBean.setResponseBody(resultMap);
            } else {
                responseBean.setStatus(ResponseStatus.FAIL);
            }
        }catch (Exception e){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(e.getMessage());
            e.printStackTrace();
        }
        return responseBean;
    }

    @Override
    public ResponseBean updateDeclaredClassFields(String fieldName, String value, String awbs) {
        ResponseBean responseBean = new ResponseBean();
        try{
            List<String> error = new ArrayList<String>();
            Class<?> className = Class.forName("com.weblearnex.app.entity.order.SaleOrder");
            List<String> awbList = Arrays.asList(awbs.split(","));
            awbList.removeAll(Arrays.asList(null, ""));
            if (className != null) {
                try{
                    Object object = className.newInstance();
                    if (object instanceof com.weblearnex.app.entity.order.SaleOrder) {
                        for (String awb : awbList) {
                            SaleOrder saleOrder = (SaleOrder) object;
                            saleOrder = saleOrderRepository.findByReferanceNo(awb);
                            if (saleOrder != null) {
                                Field field = com.weblearnex.app.entity.order.SaleOrder.class.getDeclaredField(fieldName);
                                field.setAccessible(true);
                                if ((field.getType() + "").equals("class java.lang.String")) {
                                    field.set(saleOrder, value);
                                } else if ((field.getType() + "").equals("class java.lang.Long")) {
                                    field.set(saleOrder, new Long(value));
                                } else if ((field.getType() + "").equals("long")) {
                                    field.set(saleOrder, value);
                                } else if ((field.getType() + "").equals("class java.lang.Double")) {
                                    field.set(saleOrder, new Double(value));
                                } else if ((field.getType() + "").equals("double")) {
                                    field.set(saleOrder, new Integer(value));
                                } else if ((field.getType() + "").equals("class java.lang.Integer")) {
                                    field.set(saleOrder, new Integer(value));
                                } else if ((field.getType() + "").equals("int")) {
                                    field.set(saleOrder, value);
                                } else if ((field.getType() + "").equals("class java.lang.Boolean")) {
                                    if ("true".equals(value)) {
                                        field.set(saleOrder, new Boolean("true"));
                                    } else if ("false".equals(value)) {
                                        field.set(saleOrder, new Boolean("false"));
                                    }
                                } else if ((field.getType() + "").equals("boolean")) {
                                    if ("true".equals(value)) {
                                        field.set(saleOrder, true);
                                    } else if ("false".equals(value)) {
                                        field.set(saleOrder, false);
                                    }
                                }
                                saleOrderRepository.save(saleOrder);
                            } else {
                                error.add(awb);
                            }
                        }
                    }
                }catch (Exception e){}
            }
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setResponseBody(error);
        }catch (Exception e){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(e.getMessage());
            e.printStackTrace();
        }
        return responseBean;
    }
}
