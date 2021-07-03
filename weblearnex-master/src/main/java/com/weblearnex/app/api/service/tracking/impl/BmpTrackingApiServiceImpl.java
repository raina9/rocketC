package com.weblearnex.app.api.service.tracking.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weblearnex.app.api.bean.tracking.BmpTrackingApiResponseBean;
import com.weblearnex.app.api.bean.tracking.TrackingPacketHistoryBean;
import com.weblearnex.app.api.ApiUtils;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.entity.master.ApiConfig;
import com.weblearnex.app.entity.master.StatusMapping;
import com.weblearnex.app.entity.order.SaleOrder;
import com.weblearnex.app.utils.SharedMethords;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BmpTrackingApiServiceImpl implements ApiUtils {

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public List<TrackingPacketHistoryBean> getApiPacketHistory(String apiResponse, Map<String, StatusMapping> stringStatusMappingMap) {
        try {
            BmpTrackingApiResponseBean bean = objectMapper.readValue(apiResponse, BmpTrackingApiResponseBean.class);
            if(ResponseStatus.SUCCESS.name().equalsIgnoreCase(bean.getStatus())){
                List<TrackingPacketHistoryBean> packetHistoryList = new ArrayList<TrackingPacketHistoryBean>();
                for(BmpTrackingApiResponseBean.AwbDetails awbDetails : bean.getResponse()){
                    if(ResponseStatus.SUCCESS.name().equalsIgnoreCase(awbDetails.getStatus())){
                        for(BmpTrackingApiResponseBean.AwbDetails.PacketHistory packetHistory : awbDetails.getResponse()){
                            if(packetHistory != null){
                                String mapKey = packetHistory.getStatusCode();
                                mapKey = mapKey+"#"+packetHistory.getReasonCode();
                                StatusMapping statusMapping = stringStatusMappingMap.get(mapKey);

                                if(statusMapping != null){
                                    TrackingPacketHistoryBean history = new TrackingPacketHistoryBean();
                                    history.setDate(SharedMethords.getDate(packetHistory.getDate(), ApiUtils.BMP_TRACKING_DATE_FORMAT));
                                    history.setStatusCode(statusMapping.getSelfStatusCode());
                                    history.setRtoReason(statusMapping.getNdrCode());
                                    history.setRemarks(packetHistory.getRemark());
                                    history.setNdrCode(statusMapping.getNdrCode());
                                    history.setLocation(packetHistory.getLocation());
                                    packetHistoryList.add(history);
                                }
                            }
                        }
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
        Map<String,String> map = new HashMap<String,String>();
        map.put("clientKey", saleOrder.getClientCode());
        map.put("awbNumber", saleOrder.getReferanceNo());
        return map;
    }
}
