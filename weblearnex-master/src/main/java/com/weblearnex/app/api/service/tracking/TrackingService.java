package com.weblearnex.app.api.service.tracking;

import com.weblearnex.app.entity.master.ApiConfig;
import com.weblearnex.app.entity.order.SaleOrder;
import com.weblearnex.app.entity.setup.Courier;
import com.weblearnex.app.model.ResponseBean;

public interface TrackingService {

    ResponseBean callTrackingApi(SaleOrder saleOrder, Courier courier, ApiConfig apiConfig);
    ResponseBean callAllTrackingApi();
    ResponseBean callTrackingApiByCourierCode(String courierCode);
    ResponseBean callTrackingApiAwbNumber(String awbNumber);

}
