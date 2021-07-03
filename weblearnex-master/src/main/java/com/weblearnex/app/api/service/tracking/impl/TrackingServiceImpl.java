package com.weblearnex.app.api.service.tracking.impl;

import com.weblearnex.app.api.ApiCallService;
import com.weblearnex.app.api.bean.tracking.TrackingPacketHistoryBean;
import com.weblearnex.app.api.ApiUtils;
import com.weblearnex.app.api.service.tracking.TrackingService;
import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.constant.ApiConfigType;
import com.weblearnex.app.constant.EntityType;
import com.weblearnex.app.constant.RequestMethod;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.datatable.reposatory.*;
import com.weblearnex.app.entity.master.ApiConfig;
import com.weblearnex.app.entity.master.CourierStatusMapping;
import com.weblearnex.app.entity.order.PacketHistory;
import com.weblearnex.app.entity.order.SaleOrder;
import com.weblearnex.app.entity.setup.Courier;
import com.weblearnex.app.entity.setup.Status;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.utils.SharedMethords;
import org.apache.commons.lang.SerializationUtils;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TrackingServiceImpl implements TrackingService {
    private Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ApiConfigRepository apiConfigRepository;

    @Autowired
    private SaleOrderRepository saleOrderRepository;

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    @Qualifier("applicionConfig")
    private MessageSource applicionConfig;

    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    private CourierStatusMappingRepository courierStatusMappingRepository;

    @Autowired
    private ApiCallService apiCallService;



    @Override
    public ResponseBean callTrackingApi(SaleOrder saleOrder, Courier courier, ApiConfig apiConfig) {
        ResponseBean responseBean = new ResponseBean();
        try{
           /* if(!beanFactory.containsBean(apiConfig.getImplClass())){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Bean factory npt found bean : "+apiConfig.getImplClass());
                return responseBean;
            }*/
            CourierStatusMapping courierStatusMapping = courierStatusMappingRepository.findByServiceProviderId(courier.getServiceProviderId());
            if(courierStatusMapping == null){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Courier status mapping bean not found.");
                return responseBean;
            }
            Object object = beanFactory.getBean(apiConfig.getImplClass());
            ApiUtils trackingApiService = null;
            if(object instanceof BmpTrackingApiServiceImpl){
                trackingApiService = (BmpTrackingApiServiceImpl) object;
            } else if(object instanceof NimbuspostTrackingApiServiceImpl){
                trackingApiService = (NimbuspostTrackingApiServiceImpl) object;
                apiConfig.setApiUrl(apiConfig.getApiUrl()+saleOrder.getCourierAWBNumber());
            }
            String responseString = null;
            if(RequestMethod.GET.equals(apiConfig.getRequestMethod())){
               ResponseBean apiResponse = apiCallService.GetApiCall(apiConfig);
               if(ResponseStatus.SUCCESS.equals(apiResponse.getStatus())){
                   responseString = (String) apiResponse.getResponseBody();
               }
            }else if(RequestMethod.POST.equals(apiConfig.getRequestMethod())){
                ResponseBean apiResponse = apiCallService.PostApiCall(apiConfig, trackingApiService.getPostRequestBean(saleOrder,apiConfig));
                if(ResponseStatus.SUCCESS.equals(apiResponse.getStatus())){
                    responseString = (String) apiResponse.getResponseBody();
                }
            }

            if(responseString != null){
                List<TrackingPacketHistoryBean> historyBeans = trackingApiService.getApiPacketHistory(responseString, ApiUtils.getMappingInMap(courierStatusMapping));
                // Set actual packet history in SaleOrder
                if(historyBeans != null){
                    List<TrackingPacketHistoryBean> actualHistoryBeans = ApiUtils.getValidPacketHistory(historyBeans,saleOrder);
                    if(actualHistoryBeans != null && !actualHistoryBeans.isEmpty()){
                        String currentSatatusCode = saleOrder.getCurrentStatus().getStatusCode();
                        List<PacketHistory> packetHistoryList = new ArrayList<PacketHistory>();
                        for(TrackingPacketHistoryBean trackingPacketHistoryBean : actualHistoryBeans){
                            PacketHistory packetHistory = new PacketHistory();
                            packetHistory.setCreatedDate(new Date());
                            packetHistory.setCreatedByCode("Api");
                            packetHistory.setCreatedByName("Api");
                            packetHistory.setNdrReason(trackingPacketHistoryBean.getNdrCode());
                            packetHistory.setRtoReason(trackingPacketHistoryBean.getRtoReason());
                            packetHistory.setRemarks(trackingPacketHistoryBean.getRemarks());
                            packetHistory.setLocation(trackingPacketHistoryBean.getLocation());
                            packetHistory.setFromStatusCode(currentSatatusCode);
                            packetHistory.setToStatusCode(trackingPacketHistoryBean.getStatusCode());
                            packetHistory.setDate(trackingPacketHistoryBean.getDate());
                            packetHistoryList.add(packetHistory);
                            currentSatatusCode = trackingPacketHistoryBean.getStatusCode();
                        }

                        if(!packetHistoryList.isEmpty()){
                            Status status = statusRepository.findByStatusCode(currentSatatusCode);
                            saleOrder.setCurrentStatus(status);
                            saleOrder.getPacketHistory().addAll(packetHistoryList);
                            saleOrderRepository.save(saleOrder);

                            responseBean.setStatus(ResponseStatus.SUCCESS);
                            responseBean.setResponseBody(saleOrder);
                            return responseBean;
                        }
                    }
                }
            }else {
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Tracing api response is null or Tracking impl class not load.");
                return responseBean;
            }
        }catch (Exception e){
            e.printStackTrace();
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(e.getMessage());
            return responseBean;
        }
        responseBean.setStatus(ResponseStatus.FAIL);
        responseBean.setMessage("Status not updated.");
        return responseBean;
    }

    @Override
    public ResponseBean callAllTrackingApi() {
        ResponseBean responseBean = new ResponseBean();
        try {
            List<ApiConfig> apiConfigList= apiConfigRepository.findAllByActiveAndApiConfigTypeAndEntityType(1, ApiConfigType.STATUS_UPDATE, EntityType.COURIER);
            if(apiConfigList == null && apiConfigList.isEmpty()){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Not any ApiConfig object found.");
                log.info("Not any ApiConfig object found.");
                return  responseBean;
            }
            for(ApiConfig apiConfig : apiConfigList){
                List<Courier> courierList = courierRepository.findAllByServiceProviderIdAndActive(apiConfig.getServiceProviderId(), 1);
                if(courierList != null && !courierList.isEmpty()){
                    for(Courier courier : courierList){
                        // set token if any dynamic token generate before call api.
                        // setApiToken(apiConfig);
                        String currentStatusNotIn = applicionConfig.getMessage(AppProperty.TRACK_ORDER_CURRENT_STATUS_NOT_IN, null, null);
                        List<Status> statusList = statusRepository.findAllByActiveAndStatusCodeIn(1, new ArrayList<String>(Arrays.asList(currentStatusNotIn.split(","))));
                        List<SaleOrder> saleOrderList = saleOrderRepository.findAllByCourierCodeAndCurrentStatusNotIn(courier.getCourierCode(),statusList);
                        int counter = 1;
                        for(SaleOrder saleOrder : saleOrderList){
                            //ApiConfig cloneApiConfig =  SerializationUtils.clone(apiConfig);
                            ApiConfig cloneApiConfig = (ApiConfig) apiConfig.clone();
                            ResponseBean statusUpdateResponse = callTrackingApi(saleOrder, courier,cloneApiConfig);
                            log.info(statusUpdateResponse.getStatus().name()+" : "+saleOrder.getCourierCode()+" total count --> "+counter+"/"+saleOrderList.size());
                            counter++;
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(e.getMessage());
        }
        return responseBean;
    }

    @Override
    public ResponseBean callTrackingApiByCourierCode(String courierCode) {
        ResponseBean responseBean = new ResponseBean();
        try {
            Courier courier = courierRepository.findByCourierCode(courierCode);
            if(courier == null){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Courier code not found in system.");
                return responseBean;
            }
            String currentStatusNotIn = applicionConfig.getMessage(AppProperty.TRACK_ORDER_CURRENT_STATUS_NOT_IN, null, null);
            List<Status> statusList = statusRepository.findAllByActiveAndStatusCodeIn(1, new ArrayList<String>(Arrays.asList(currentStatusNotIn.split(","))));
            List<SaleOrder> saleOrderList = saleOrderRepository.findAllByCourierCodeAndCurrentStatusNotIn(courier.getCourierCode(),statusList);
            ApiConfig apiConfig = apiConfigRepository.findByServiceProviderId(courier.getServiceProviderId());
            if(apiConfig == null){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("ApiConfig or Courier ID not found.");
                return responseBean;
            }
            // set token if any dynamic token generate before call api.
            // setApiToken(apiConfig);

            int counter = 1;
            for(SaleOrder saleOrder : saleOrderList){
                ApiConfig cloneApiConfig = (ApiConfig) apiConfig.clone();
                ResponseBean statusUpdateResponse = callTrackingApi(saleOrder, courier,cloneApiConfig);
                log.info(statusUpdateResponse.getStatus().name()+" : "+saleOrder.getCourierCode()+" total count --> "+counter+"/"+saleOrderList.size());
                counter++;
            }
        }catch (Exception e){
            e.printStackTrace();
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(e.getMessage());
        }
        return responseBean;
    }

    @Override
    public ResponseBean callTrackingApiAwbNumber(String awbNumber) {
        ResponseBean responseBean = new ResponseBean();
        try {
            List<SaleOrder> saleOrderList = saleOrderRepository.findByReferanceNoIn(Arrays.asList(awbNumber.split(",")));
            if(saleOrderList == null || saleOrderList.isEmpty()){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Awb number not found in system.");
                return responseBean;
            }
            int counter = 1;
            String currentStatusNotIn = applicionConfig.getMessage(AppProperty.TRACK_ORDER_CURRENT_STATUS_NOT_IN, null, null);
            List<String> currentList = new ArrayList<>(Arrays.asList(currentStatusNotIn.split(",")));
            for(SaleOrder saleOrder : saleOrderList) {
                // TODO check saleOrder present in terminal status or not if yes then continue.
                if(currentList.contains(saleOrder.getCurrentStatus().getStatusCode())){
                    continue;
                }
                Courier courier = courierRepository.findByCourierCode(saleOrder.getCourierCode());
                ApiConfig apiConfig = apiConfigRepository.findByServiceProviderId(courier.getServiceProviderId());
                if(courier == null || apiConfig == null){
                    responseBean.setStatus(ResponseStatus.FAIL);
                    responseBean.setMessage("ApiConfig or Courier not found in system");
                    return responseBean;
                }
                // set token if any dynamic token generate before call api.
                //setApiToken(apiConfig);

                // ApiConfig cloneApiConfig = (ApiConfig) apiConfig.clone();
                ResponseBean statusUpdateResponse = callTrackingApi(saleOrder, courier, apiConfig);
                log.info(statusUpdateResponse.getStatus().name()+" : "+saleOrder.getCourierCode()+" total count --> "+counter+"/"+saleOrderList.size());
                counter++;
            }
        }catch (Exception e){
            e.printStackTrace();
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(e.getMessage());
            return responseBean;
        }
        return responseBean;
    }

    private void setApiToken( ApiConfig apiConfig){
        // For Nimbus case get token first then call api.
        try{
            Object object = beanFactory.getBean(apiConfig.getImplClass());
            ApiUtils trackingApiService = null;
            if(object instanceof NimbuspostTrackingApiServiceImpl){
                NimbuspostTrackingApiServiceImpl nimbuspostTrackingApiService = (NimbuspostTrackingApiServiceImpl) object;
                String token = nimbuspostTrackingApiService.getNimbusToken(apiConfig);
                if(token != null){
                    String header = "Authorization=Bearer "+token;
                    apiConfig.setApiToken(token);
                    apiConfig.setHeaderParems(header);
                }
            }
        }catch(Exception e){e.printStackTrace();}
    }

}
