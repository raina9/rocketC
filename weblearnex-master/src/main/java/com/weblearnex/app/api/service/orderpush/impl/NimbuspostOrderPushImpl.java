package com.weblearnex.app.api.service.orderpush.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.weblearnex.app.api.ApiCallService;
import com.weblearnex.app.api.bean.orderpush.NimbusOrderPushRequestBean;
import com.weblearnex.app.api.bean.orderpush.NimbusOrderPushRequestOldBean;
import com.weblearnex.app.api.service.orderpush.PushService;
import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.constant.PaymentType;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.datatable.reposatory.ClientCourierWarehouseMappingRepository;
import com.weblearnex.app.datatable.reposatory.ClientWarehouseRepository;
import com.weblearnex.app.datatable.reposatory.ConfigrationRepository;
import com.weblearnex.app.entity.master.ApiConfig;
import com.weblearnex.app.entity.master.State;
import com.weblearnex.app.entity.order.SaleOrder;
import com.weblearnex.app.entity.setup.ClientCourierWarehouseMapping;
import com.weblearnex.app.entity.setup.ClientWarehouse;
import com.weblearnex.app.entity.setup.Configration;
import com.weblearnex.app.entity.setup.Courier;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.PincodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Qualifier("nimbuspostOrderPushImpl")
public class NimbuspostOrderPushImpl implements PushService {

    /*@Autowired
    private ClientWarehouseRepository clientWarehouseRepository;

    @Autowired
    private PincodeService pincodeService;*/

    @Autowired
    private ApiCallService apiCallService;

    @Autowired
    private ConfigrationRepository configrationRepository;

    @Autowired
    @Qualifier("applicionConfig")
    private MessageSource applicionConfig;

    @Autowired
    private ClientCourierWarehouseMappingRepository clientCourierWarehouseMappingRepository;

    @Override
    public ResponseBean getObjectForPush(SaleOrder saleOrder, Courier courier) {
        ResponseBean responseBean = new ResponseBean();
        try{
            // This is new api request
            /*NimbusOrderPushRequestBean bean = new NimbusOrderPushRequestBean();
            bean.setOrder_number(saleOrder.getReferanceNo());
            bean.setShipping_charges(0.0d);
            bean.setDiscount(0.0d);
            bean.setCod_charges(0.0d);
            if(PaymentType.COD.equals(saleOrder.getPaymentType())){
                bean.setPayment_type("cod");
                bean.setOrder_amount(saleOrder.getCodAmount());
            }else if(PaymentType.PREPAID.equals(saleOrder.getPaymentType())){
                bean.setPayment_type("prepaid");
                bean.setOrder_amount(saleOrder.getProductPrice());
            }
            bean.setCourier_id(courier.getServiceProviderCourierCode());
            bean.setPackage_weight(saleOrder.getWeight()*1000);
            bean.setPackage_length(saleOrder.getLength());
            bean.setPackage_breadth(saleOrder.getBreadth());
            bean.setPackage_height(saleOrder.getHight());
            bean.setRequest_auto_pickup("yes");

            NimbusOrderPushRequestBean.Consignee consignee = new NimbusOrderPushRequestBean.Consignee();
            consignee.setName(saleOrder.getConsigneeName());
            consignee.setAddress(saleOrder.getConsigneeAddress());
            consignee.setAddress_2(saleOrder.getConsigneeLandmark());
            consignee.setCity(saleOrder.getConsigneeCity());
            consignee.setState(saleOrder.getConsigneeState());
            consignee.setPincode(saleOrder.getConsigneePinCode());
            consignee.setPhone(saleOrder.getConsigneeMobileNumber());
            bean.setConsignee(consignee);

            NimbusOrderPushRequestBean.Pickup pickup = new NimbusOrderPushRequestBean.Pickup();
            ClientWarehouse clientWarehouse = clientWarehouseRepository.findByWarehouseCode(saleOrder.getPickupLocationId());
            if(clientWarehouse  == null){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Client warehouse details not found.");
                return responseBean;
            }
            pickup.setWarehouse_name(clientWarehouse.getWarehouseName());
            pickup.setName(clientWarehouse.getContactPersonName());
            pickup.setAddress(clientWarehouse.getAddress());
            ResponseBean cityDetails = pincodeService.getCityStateCountryByPincode(clientWarehouse.getPinCode());
            if(ResponseStatus.FAIL.equals(cityDetails.getStatus())){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Client warehouse pin-code not found.");
                return responseBean;
            }
            Map<String, String> map = (Map<String, String>) cityDetails.getResponseBody();
            pickup.setCity(map.get("CITY_NAME"));
            pickup.setState(map.get("STATE_NAME"));
            pickup.setPincode(clientWarehouse.getPinCode());
            pickup.setPhone(clientWarehouse.getContactNumber());
            bean.setPickup(pickup);

            NimbusOrderPushRequestBean.OrderItems orderItems = new NimbusOrderPushRequestBean.OrderItems();
            Configration configration = configrationRepository.findByConfigCode(applicionConfig.getMessage(AppProperty.REPLACE_PRODUCT_NAME, null, null));
            if(configration != null && configration.getExtra2().contains(saleOrder.getCourierCode())){
                String productName = saleOrder.getProductName();
                List<String> list = new ArrayList<>(Arrays.asList(configration.getExtra1().split(",")));
                list.forEach(s -> {
                    if(productName.contains(s)){
                        productName.replaceAll(s, configration.getConfigValue());
                    }
                });
                orderItems.setName(productName);
            }else{
                orderItems.setName(saleOrder.getProductName());
            }
            //orderItems.setName(saleOrder.getProductName());
            orderItems.setQty(saleOrder.getProductQuantity().toString());
            orderItems.setPrice(String.valueOf(saleOrder.getProductPrice()));
            orderItems.setSku(saleOrder.getProductSKU());
            List<NimbusOrderPushRequestBean.OrderItems> orderItemsList = new ArrayList<NimbusOrderPushRequestBean.OrderItems>();
            orderItemsList.add(orderItems);
            bean.setOrder_items(orderItemsList);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setResponseBody(bean);*/


            // This is Old implements
            NimbusOrderPushRequestOldBean bean = new NimbusOrderPushRequestOldBean();
            ClientCourierWarehouseMapping clientCourierWarehouseMapping = clientCourierWarehouseMappingRepository.findByClientCodeAndClientWarehouseCodeAndServiceProviderID(
                    saleOrder.getClientCode(), saleOrder.getPickupLocationId(), courier.getServiceProviderId());
            if(clientCourierWarehouseMapping == null){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Client warehouse mapping not found.");
                return responseBean;
            }
            bean.setPickup_warehouse_id(clientCourierWarehouseMapping.getServiceProviderWarehouseCode());
            bean.setRto_warehouse_id(clientCourierWarehouseMapping.getServiceProviderWarehouseCode());
            bean.setCourier_id(String.valueOf(courier.getServiceProviderCourierCode()));

            NimbusOrderPushRequestOldBean.Order order = new NimbusOrderPushRequestOldBean.Order();
            order.setOrder_number(saleOrder.getReferanceNo());
            order.setShipping_charges(0.0d);
            order.setDiscount(0.0d);
            order.setCod_charges(0.0d);
            if(PaymentType.COD.equals(saleOrder.getPaymentType())){
                order.setPayment_type("cod");
                order.setTotal(saleOrder.getCodAmount());
            }else if(PaymentType.PREPAID.equals(saleOrder.getPaymentType())){
                order.setPayment_type("prepaid");
                order.setTotal(saleOrder.getProductPrice());
            }
            order.setPackage_weight(saleOrder.getWeight()*1000);
            order.setPackage_length(saleOrder.getLength());
            order.setPackage_height(saleOrder.getHight());
            order.setPackage_breadth(saleOrder.getBreadth());
            bean.setOrder(order);

            NimbusOrderPushRequestOldBean.Consignee consignee = new NimbusOrderPushRequestOldBean.Consignee();
            consignee.setName(saleOrder.getConsigneeName());
            consignee.setAddress(saleOrder.getConsigneeAddress());
            consignee.setAddress_2(saleOrder.getConsigneeLandmark());
            consignee.setCity(saleOrder.getConsigneeCity());
            consignee.setState(saleOrder.getConsigneeState());
            consignee.setPincode(saleOrder.getConsigneePinCode());
            consignee.setPhone(saleOrder.getConsigneeMobileNumber());
            bean.setConsignee(consignee);

            NimbusOrderPushRequestOldBean.OrderItems orderItems = new NimbusOrderPushRequestOldBean.OrderItems();
            Configration configration = configrationRepository.findByConfigCode(applicionConfig.getMessage(AppProperty.REPLACE_PRODUCT_NAME, null, null));
            if(configration != null && configration.getExtra2() != null && configration.getExtra2().contains(courier.getCourierCode())){
                String productName = saleOrder.getProductName();
                List<String> list = new ArrayList<>(Arrays.asList(configration.getExtra1().split(",")));
                list.forEach(s -> {
                    if(productName.contains(s)){
                        productName.replaceAll(s, configration.getConfigValue());
                    }
                });
                orderItems.setName(productName);
            }else{
                orderItems.setName(saleOrder.getProductName());
            }
            // orderItems.setName(saleOrder.getProductName());
            orderItems.setQty(saleOrder.getProductQuantity().toString());
            orderItems.setPrice(String.valueOf(saleOrder.getProductPrice()));
            orderItems.setSku(saleOrder.getProductSKU());
            List<NimbusOrderPushRequestOldBean.OrderItems> list = new ArrayList<>(1);
            list.add(orderItems);
            bean.setOrder_items(list);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setResponseBody(bean);
        }catch (Exception e){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(e.getMessage());
            e.printStackTrace();
        }
        return responseBean;
    }

    public String getNimbusToken(ApiConfig apiConfig) {
        try{
            if(apiConfig.getExtra1() == null || apiConfig.getExtra1().trim().isEmpty()
                    || apiConfig.getExtra2() == null || apiConfig.getExtra2().trim().isEmpty()
                    || apiConfig.getExtra3() == null || apiConfig.getExtra3().trim().isEmpty()){
                return null;
            }
            ApiConfig config = new ApiConfig();
            config.setApiUrl(apiConfig.getExtra1());
            Map<String, String> map = new HashMap<String,String>();
            map.put("email",apiConfig.getExtra2());
            map.put("password", apiConfig.getExtra3());
            ResponseBean responseBean = apiCallService.PostApiCall(config, map);
            if(ResponseStatus.SUCCESS.equals(responseBean.getStatus())){
                JsonParser parser = new JsonParser();
                JsonObject jsonObject = (JsonObject) parser.parse(responseBean.getResponseBody().toString());
                if(jsonObject.has("data")){
                    return jsonObject.get("data").getAsString();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
