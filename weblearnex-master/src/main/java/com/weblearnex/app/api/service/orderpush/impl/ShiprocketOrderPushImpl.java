package com.weblearnex.app.api.service.orderpush.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.weblearnex.app.api.ApiCallService;
import com.weblearnex.app.api.ApiUtils;
import com.weblearnex.app.api.bean.orderpush.ShiprocketOrderPushRequestBeen;
import com.weblearnex.app.api.service.orderpush.PushService;
import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.constant.PaymentType;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.datatable.reposatory.ClientCourierWarehouseMappingRepository;
import com.weblearnex.app.datatable.reposatory.ClientWarehouseRepository;
import com.weblearnex.app.datatable.reposatory.ConfigrationRepository;
import com.weblearnex.app.entity.master.ApiConfig;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Qualifier("shiprocketOrderPushImpl")
public class ShiprocketOrderPushImpl implements PushService {

    /*@Autowired
    private ClientWarehouseRepository clientWarehouseRepository;*/

    @Autowired
    private ClientCourierWarehouseMappingRepository clientCourierWarehouseMappingRepository;

    /*@Autowired
    private PincodeService pincodeService;*/

    @Autowired
    private ApiCallService apiCallService;

    @Autowired
    private ConfigrationRepository configrationRepository;

    @Autowired
    @Qualifier("applicionConfig")
    private MessageSource applicionConfig;

    @Override
    public ResponseBean getObjectForPush(SaleOrder saleOrder, Courier courier) {
        ResponseBean responseBean = new ResponseBean();
        ShiprocketOrderPushRequestBeen requestBeen = new ShiprocketOrderPushRequestBeen();
        try{
            requestBeen.setRequest_pickup("true");
            requestBeen.setGenerate_manifest("true");
            requestBeen.setCourier_id(courier.getServiceProviderCourierCode());
            requestBeen.setOrder_id(saleOrder.getReferanceNo());
            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat(ApiUtils.SHIP_ROCKET_ORDER_PUSH_DATE_FORMAT);
            requestBeen.setOrder_date(dateFormat.format(date));
            requestBeen.setChannel_id("");
            requestBeen.setBilling_customer_name("Praylax Creation");
            requestBeen.setBilling_last_name("Praylax Creation");
            requestBeen.setBilling_address("B-889/890 Sector-7, Dwarka, Near Ramphal Chowk, New Delhi-110075");
            requestBeen.setBilling_city("New Delhi");
            requestBeen.setBilling_state("Delhi");
            requestBeen.setBilling_country("India");
            requestBeen.setBilling_pincode("110075");
            requestBeen.setBilling_email("praylaxprint@gmail.com");
            requestBeen.setBilling_phone("8700904917");
            requestBeen.setBilling_alternate_phone("8700904917");
            requestBeen.setShipping_is_billing("0");
            requestBeen.setShipping_customer_name(saleOrder.getConsigneeName());
            requestBeen.setShipping_address(saleOrder.getConsigneeAddress());
            requestBeen.setShipping_address_2(saleOrder.getConsigneeLandmark());
            requestBeen.setShipping_city(saleOrder.getConsigneeCity());
            requestBeen.setShipping_state(saleOrder.getConsigneeState());
            requestBeen.setShipping_country(saleOrder.getConsigneeCountry());
            requestBeen.setShipping_pincode(saleOrder.getConsigneePinCode());
            requestBeen.setShipping_email((saleOrder.getConsigneeEmailId() == null || saleOrder.getConsigneeEmailId().isEmpty()) ? "abc@a.com" : saleOrder.getConsigneeEmailId());
            requestBeen.setShipping_phone(saleOrder.getConsigneeMobileNumber());
            if(PaymentType.COD.equals(saleOrder.getPaymentType())){
                requestBeen.setPayment_method("COD");
                requestBeen.setSub_total(String.valueOf(saleOrder.getCodAmount()));
            }else{
                requestBeen.setPayment_method("Prepaid");
                requestBeen.setSub_total(String.valueOf(saleOrder.getProductPrice()));
            }
            requestBeen.setWeight(String.valueOf(saleOrder.getWeight()));
            requestBeen.setLength(String.valueOf(saleOrder.getLength()));
            requestBeen.setBreadth(String.valueOf(saleOrder.getBreadth()));
            requestBeen.setHeight(String.valueOf(saleOrder.getHight()));

            ClientCourierWarehouseMapping clientCourierWarehouseMapping = clientCourierWarehouseMappingRepository.findByClientCodeAndClientWarehouseCodeAndServiceProviderID(
                    saleOrder.getClientCode(), saleOrder.getPickupLocationId(), courier.getServiceProviderId());
            if(clientCourierWarehouseMapping == null){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Client warehouse mapping not found.");
                return responseBean;
            }
            /*ClientWarehouse clientWarehouse = clientWarehouseRepository.findByWarehouseCode(saleOrder.getPickupLocationId());
            if(clientWarehouse == null){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Client warehouse details not found.");
                return responseBean;
            }*/
            requestBeen.setPickup_location(clientCourierWarehouseMapping.getServiceProviderWarehouseCode());

            // Order Items
            ShiprocketOrderPushRequestBeen.OrderItems orderItems = new ShiprocketOrderPushRequestBeen.OrderItems();
            // messageSource.getMessage(MessageProperty.SALE_ORDER_ADDED_MSG, null, null)
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
            orderItems.setSku(saleOrder.getProductSKU());
            orderItems.setSelling_price(String.valueOf(saleOrder.getProductPrice()));
            orderItems.setUnits("1");
            List<ShiprocketOrderPushRequestBeen.OrderItems> orderItemsList = new ArrayList<>();
            orderItemsList.add(orderItems);
            requestBeen.setOrder_items(orderItemsList);

            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setResponseBody(requestBeen);
        }catch (Exception e){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(e.getMessage());
            e.printStackTrace();
        }
        return responseBean;
    }

    public String getShiprocketToken(ApiConfig apiConfig) {
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
                if(jsonObject.has("token")){
                    return jsonObject.get("token").getAsString();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
