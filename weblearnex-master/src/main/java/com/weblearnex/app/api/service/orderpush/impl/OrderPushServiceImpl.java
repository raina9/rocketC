package com.weblearnex.app.api.service.orderpush.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weblearnex.app.api.ApiCallService;
import com.weblearnex.app.api.ApiUtils;
import com.weblearnex.app.api.bean.orderpush.NimbusOrderPushResponseBean;
import com.weblearnex.app.api.bean.orderpush.ShiprocketOrderPushResponseBeen;
import com.weblearnex.app.api.service.orderpush.OrderPushService;
import com.weblearnex.app.api.service.orderpush.PushService;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.datatable.reposatory.*;
import com.weblearnex.app.entity.logs.DataPushErrorLog;
import com.weblearnex.app.entity.logs.DataPushSuccessLog;
import com.weblearnex.app.entity.master.ApiConfig;
import com.weblearnex.app.entity.order.SaleOrder;
import com.weblearnex.app.entity.setup.Courier;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.utils.SharedMethords;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class OrderPushServiceImpl implements OrderPushService {

    @Autowired
    @Qualifier("applicionConfig")
    private MessageSource applicionConfig;

    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    private ApiCallService apiCallService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DataPushErrorLogRepository dataPushErrorLogRepository;

    @Autowired
    private DataPushSuccessLogRepository dataPushSuccessLogRepository;

    @Autowired
    private SaleOrderRepository saleOrderRepository;

    @Autowired
    private ApiConfigRepository apiConfigRepository;

    @Autowired
    private CourierRepository courierRepository;


    @Override
    public ResponseBean callOrderPushOrderApi(SaleOrder saleOrder, Courier courier, ApiConfig apiConfig) {
        ResponseBean responseBean = new ResponseBean();
        try{
            PushService pushService = null;
            Object object = beanFactory.getBean(apiConfig.getImplClass());
            if(object instanceof NimbuspostOrderPushImpl){
                pushService = (NimbuspostOrderPushImpl) object;
                // It is used for new API
                //setApiToken(apiConfig);
            }else if(object instanceof ShiprocketOrderPushImpl){
                pushService = (ShiprocketOrderPushImpl) object;
                Date expiredDate = SharedMethords.getDate(apiConfig.getTokenExpiredDate(), SharedMethords.ONLY_DATE_FORMAT);
                Date currentDate = new Date();
                if(expiredDate == null || expiredDate.compareTo(currentDate) < 0 || apiConfig.getApiToken() == null || apiConfig.getApiToken().trim().isEmpty()){
                    setApiToken(apiConfig);
                }else{
                    String header = "Authorization=Bearer "+apiConfig.getApiToken();
                    apiConfig.setHeaderParems(header);
                }
            }
            if(pushService == null){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("PushService not initialized.");
                return responseBean;
            }
            ResponseBean objectForPush = pushService.getObjectForPush(saleOrder,courier);
            if(ResponseStatus.FAIL.equals(objectForPush.getStatus())){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Push object creation failed of : Impl-->"+apiConfig.getImplClass()+" MSG : "+objectForPush.getMessage());
                return responseBean;
            }
            Object pushRequest = objectForPush.getResponseBody();
            ResponseBean pushApiResponse = apiCallService.PostApiCall(apiConfig, pushRequest);
            if(ResponseStatus.SUCCESS.equals(pushApiResponse.getStatus())){
                if(object instanceof NimbuspostOrderPushImpl){
                    String s = (String) pushApiResponse.getResponseBody();
                    NimbusOrderPushResponseBean response = objectMapper.readValue(s, NimbusOrderPushResponseBean.class);
                    if(response != null && !response.getStatus()){
                        saveErrorLogObj(saleOrder, apiConfig, pushRequest, pushApiResponse);
                        responseBean.setStatus(ResponseStatus.FAIL);
                        responseBean.setMessage(response.getMessage());
                        return responseBean;
                    }
                    if(response != null && response.getStatus()){
                        saleOrder.setCourierAWBNumber(response.getData().getAwb_number());
                        // If courier id not match with vendor then update courier assigned by vendor.
                        if(response.getData().getCourier_id() != null && !courier.getServiceProviderCourierCode().equals(response.getData().getCourier_id())){
                            Courier courierVendor = courierRepository.findByServiceProviderCourierCode(response.getData().getCourier_id());
                            saleOrder.setCourierCode(courierVendor != null ? courierVendor.getCourierCode() : saleOrder.getCourierCode());
                        }
                        // saleOrderRepository.save(saleOrder);
                    }
                }else if(object instanceof ShiprocketOrderPushImpl){
                    String s = (String) pushApiResponse.getResponseBody();
                    ShiprocketOrderPushResponseBeen response = objectMapper.readValue(s, ShiprocketOrderPushResponseBeen.class);
                    if(response != null && (response.getStatus() == null || response.getStatus() == 0 || response.getPayload() == null)){
                        saveErrorLogObj(saleOrder, apiConfig, pushRequest, pushApiResponse);
                        responseBean.setStatus(ResponseStatus.FAIL);
                        String msg = response.getPayload() != null ? response.getPayload().getError_message() : "Api data push error.";
                        responseBean.setMessage(response.getMessage() != null ? response.getMessage() : msg);
                        return responseBean;
                    }
                    // If wallet balance is low in then send error
                    if(response.getPayload().getAwb_code() == null || response.getPayload().getAwb_code().trim().isEmpty()){
                        pushApiResponse.setMessage(response.getPayload().getAwb_assign_error());
                        saveErrorLogObj(saleOrder, apiConfig, pushRequest, pushApiResponse);
                        responseBean.setStatus(ResponseStatus.FAIL);
                        responseBean.setMessage(response.getPayload().getAwb_assign_error());
                        return responseBean;
                    }
                    saleOrder.setCourierAWBNumber(response.getPayload().getAwb_code());
                    // If courier id not match with vendor then update courier assigned by vendor.
                    if(response.getPayload().getCourier_company_id() != null
                            && !courier.getServiceProviderCourierCode().equals(String.valueOf(response.getPayload().getCourier_company_id()))){
                        Courier courierVendor = courierRepository.findByServiceProviderCourierCode(String.valueOf(response.getPayload().getCourier_company_id()));
                        saleOrder.setCourierCode(courierVendor != null ? courierVendor.getCourierCode() : saleOrder.getCourierCode());
                    }

                }
               saveSuccessLogObj(saleOrder, apiConfig, pushRequest, pushApiResponse);
            }else {
               saveErrorLogObj(saleOrder, apiConfig, pushRequest, pushApiResponse);
            }
            return pushApiResponse;
        }catch (Exception e){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(e.getMessage());
            e.printStackTrace();
        }
        return responseBean;
    }

    @Override
    public ResponseBean callOrderPushOrderApiByAwb(String awbNumber) {
        ResponseBean responseBean = new ResponseBean();
        try{
            if(awbNumber == null || awbNumber.trim().isEmpty()){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Awb is empty.");
                return responseBean;
            }
            List<SaleOrder> saleOrderList = saleOrderRepository.findByReferanceNoIn(Arrays.asList(awbNumber.split(",")));
            if(saleOrderList == null || saleOrderList.isEmpty()){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Provided awb not found in system.");
                return responseBean;
            }
            Map<String, ResponseBean> map = new HashMap<String, ResponseBean>();
            Map<String, Courier> courierMap = new HashMap<String, Courier>();
            Map<Long, ApiConfig> apiConfigMap = new HashMap<Long, ApiConfig>();
            for(SaleOrder saleOrder : saleOrderList){
                try{
                    if(!courierMap.containsKey(saleOrder.getCourierCode())){
                        courierMap.put(saleOrder.getCourierCode(),courierRepository.findByCourierCode(saleOrder.getCourierCode()));
                    }
                    Courier courier = courierMap.get(saleOrder.getCourierCode());
                    if(!apiConfigMap.containsKey(courier.getServiceProviderId())){
                        apiConfigMap.put(courier.getServiceProviderId(), apiConfigRepository.findByServiceProviderId(courier.getServiceProviderId()));
                    }
                    ApiConfig apiConfig = apiConfigMap.get(courier.getServiceProviderId());
                    ResponseBean pushResponse = callOrderPushOrderApi(saleOrder, courier, apiConfig);
                    if(ResponseStatus.SUCCESS.equals(pushResponse.getStatus())){
                        saleOrderRepository.save(saleOrder);
                    }
                    map.put(saleOrder.getReferanceNo(), pushResponse);
                }catch (Exception e){
                    ResponseBean bean = new ResponseBean();
                    bean.setStatus(ResponseStatus.FAIL);
                    bean.setMessage(e.getMessage());
                    map.put(saleOrder.getReferanceNo(), bean);
                    e.printStackTrace();
                }
            }
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setResponseBody(map);
        }catch (Exception e){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(e.getMessage());
            e.printStackTrace();
        }
        return responseBean;
    }

    private void saveSuccessLogObj(SaleOrder saleOrder, ApiConfig apiConfig, Object request, ResponseBean responseBean){
        DataPushSuccessLog successLog = new DataPushSuccessLog();
        try{
            successLog.setAwb(saleOrder.getReferanceNo());
            successLog.setRequest(objectMapper.writeValueAsString(request));
            successLog.setResponse(responseBean.getResponseBody().toString());
            successLog.setUrl(apiConfig.getApiUrl());
            successLog.setClientCode(saleOrder.getClientCode());
            successLog.setStatus(responseBean.getStatus().name());
            successLog.setMessage(responseBean.getMessage());
            successLog.setDate(SharedMethords.getDate(new Date()));
            dataPushSuccessLogRepository.save(successLog);
            // Remove all error log if pushed success.
            // TODO check with Sweta.
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void saveErrorLogObj(SaleOrder saleOrder, ApiConfig apiConfig, Object request, ResponseBean responseBean){
        DataPushErrorLog errorLog = new DataPushErrorLog();
        try{
            errorLog.setAwb(saleOrder.getReferanceNo());
            errorLog.setRequest(objectMapper.writeValueAsString(request));
            errorLog.setResponse(responseBean.getResponseBody() != null ? responseBean.getResponseBody().toString() : null);
            errorLog.setUrl(apiConfig.getApiUrl());
            errorLog.setClientCode(saleOrder.getClientCode());
            errorLog.setStatus(ResponseStatus.FAIL.name());
            errorLog.setMessage(responseBean.getMessage());
            errorLog.setDate(SharedMethords.getDate(new Date()));
            dataPushErrorLogRepository.save(errorLog);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setApiToken( ApiConfig apiConfig){
        // For Nimbus case get token first then call api.
        try{
            Object object = beanFactory.getBean(apiConfig.getImplClass());
            ApiUtils trackingApiService = null;
            if(object instanceof NimbuspostOrderPushImpl){
                NimbuspostOrderPushImpl nimbuspostOrderPush = (NimbuspostOrderPushImpl) object;
                String token = nimbuspostOrderPush.getNimbusToken(apiConfig);
                if(token != null){
                    String header = "Authorization=Bearer "+token;
                    apiConfig.setApiToken(token);
                    apiConfig.setHeaderParems(header);
                }
            }else if(object instanceof ShiprocketOrderPushImpl){
                ShiprocketOrderPushImpl shiprocketOrderPush = (ShiprocketOrderPushImpl) object;
                String token = shiprocketOrderPush.getShiprocketToken(apiConfig);
                if(token != null){
                    String header = "Authorization=Bearer "+token;
                    apiConfig.setApiToken(token);
                    apiConfig.setHeaderParems(header);
                    // Set token expired date
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DAY_OF_MONTH, 8);
                    apiConfig.setTokenExpiredDate(SharedMethords.getOnlyDate(cal.getTime()));
                    apiConfigRepository.save(apiConfig);
                }
            }
        }catch(Exception e){e.printStackTrace();}
    }

}
