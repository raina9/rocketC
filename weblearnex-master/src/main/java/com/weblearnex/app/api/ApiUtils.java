package com.weblearnex.app.api;
import com.weblearnex.app.api.bean.tracking.TrackingPacketHistoryBean;
import com.weblearnex.app.entity.master.ApiConfig;
import com.weblearnex.app.entity.master.CourierStatusMapping;
import com.weblearnex.app.entity.master.StatusMapping;
import com.weblearnex.app.entity.order.SaleOrder;

import java.util.*;

public interface ApiUtils {
    public static final String BMP_TRACKING_DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
    public static final String NIMBUS_TRACKING_DATE_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String SHIP_ROCKET_ORDER_PUSH_DATE_FORMAT = "yyyy-MM-dd HH:mm";

    List<TrackingPacketHistoryBean> getApiPacketHistory(String apiResponse, Map<String, StatusMapping> stringStatusMappingMap);
    Object getPostRequestBean(SaleOrder saleOrder, ApiConfig apiConfig);

    public static List<TrackingPacketHistoryBean> getValidPacketHistory(List<TrackingPacketHistoryBean> apiPacketHistory, SaleOrder saleOrder){
        if(apiPacketHistory == null || apiPacketHistory.isEmpty()){
            return null;
        }
        Date fromDate = saleOrder.getPacketHistory().get(saleOrder.getPacketHistory().size()-1).getDate();
        Date toDate = null;
        List<TrackingPacketHistoryBean> actualPacketHistory = new ArrayList<TrackingPacketHistoryBean>();
        for(TrackingPacketHistoryBean packetHistory : apiPacketHistory){
            toDate = packetHistory.getDate();
            if(isValidPacketHistory(fromDate,toDate)){
                actualPacketHistory.add(packetHistory);
                fromDate = packetHistory.getDate();
            }
        }
        if(!actualPacketHistory.isEmpty()){
            return actualPacketHistory;
        }
        return null;
    }
    public static Map<String, StatusMapping> getMappingInMap(CourierStatusMapping courierStatusMapping){
        if(courierStatusMapping == null){
            return null;
        }
        Map<String, StatusMapping> map = new HashMap<String, StatusMapping>();
        for(StatusMapping statusMapping : courierStatusMapping.getStatusMappings()){
            map.put(statusMapping.getCourierStatusCode(), statusMapping);
        }
        return map;
    }
    public static boolean isValidPacketHistory(Date fromDate, Date toDate){
        if(fromDate == null || toDate == null){
            return false;
        }
        if (fromDate.compareTo(toDate) > 0) {
            // When  fromDate > Date toDate
            return false;
        } else if (fromDate.compareTo(toDate) < 0) {
            // When Date fromDate < Date toDate
            return true;
        } else if (fromDate.compareTo(toDate) == 0) {
            return true;
        }
        return false;
    }
}
