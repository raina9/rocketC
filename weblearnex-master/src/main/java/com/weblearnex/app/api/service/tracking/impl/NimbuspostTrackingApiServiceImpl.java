package com.weblearnex.app.api.service.tracking.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.weblearnex.app.api.ApiCallService;
import com.weblearnex.app.api.ApiUtils;
import com.weblearnex.app.api.bean.tracking.BmpTrackingApiResponseBean;
import com.weblearnex.app.api.bean.tracking.NimbuspostTrackingApiResponse;
import com.weblearnex.app.api.bean.tracking.TrackingPacketHistoryBean;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.entity.master.ApiConfig;
import com.weblearnex.app.entity.master.StatusMapping;
import com.weblearnex.app.entity.order.SaleOrder;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.utils.SharedMethords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Qualifier("nimbuspostTrackingApiServiceImpl")
public class NimbuspostTrackingApiServiceImpl implements ApiUtils {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApiCallService apiCallService;

    @Override
    public List<TrackingPacketHistoryBean> getApiPacketHistory(String apiResponse, Map<String, StatusMapping> stringStatusMappingMap) {
        try {
            NimbuspostTrackingApiResponse bean = objectMapper.readValue(apiResponse, NimbuspostTrackingApiResponse.class);
            if(bean != null && bean.getStatus() != null && bean.getStatus() && bean.getData() != null
                    && bean.getData().getHistory() != null && !bean.getData().getHistory().isEmpty()){
                List<TrackingPacketHistoryBean> packetHistoryList = new ArrayList<TrackingPacketHistoryBean>();
                Collections.reverse(bean.getData().getHistory());
                for(NimbuspostTrackingApiResponse.Data.History history : bean.getData().getHistory()){
                    if(history != null){
                        try{
                            String mapKey = history.getStatus_code().trim();
                            mapKey = mapKey+"##"+history.getMessage().replaceAll("\\s", "");
                            StatusMapping statusMapping = null;
                            if(stringStatusMappingMap.containsKey(mapKey)){
                                statusMapping = stringStatusMappingMap.get(mapKey);
                            }else{
                                mapKey = history.getMessage().replaceAll("\\s", "");
                                if(stringStatusMappingMap.containsKey(mapKey)){
                                    statusMapping = stringStatusMappingMap.get(mapKey);
                                }else{
                                    mapKey = history.getStatus_code();
                                    if(stringStatusMappingMap.containsKey(mapKey)){
                                        statusMapping = stringStatusMappingMap.get(mapKey);
                                    }
                                }
                            }
                            if(statusMapping != null) {
                                TrackingPacketHistoryBean trackingPacketHistoryBean = new TrackingPacketHistoryBean();
                                trackingPacketHistoryBean.setDate(new Date(Long.valueOf(history.getEvent_time())));
                                trackingPacketHistoryBean.setStatusCode(statusMapping.getSelfStatusCode());
                                trackingPacketHistoryBean.setRtoReason(statusMapping.getNdrCode());
                                trackingPacketHistoryBean.setRemarks(history.getMessage());
                                trackingPacketHistoryBean.setNdrCode(statusMapping.getNdrCode());
                                trackingPacketHistoryBean.setLocation(history.getLocation());
                                packetHistoryList.add(trackingPacketHistoryBean);
                            }
                        }catch (Exception e){e.printStackTrace();}
                    }
                }
                if(!packetHistoryList.isEmpty()){
                    return packetHistoryList;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object getPostRequestBean(SaleOrder saleOrder, ApiConfig apiConfig) {
        return null;
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
