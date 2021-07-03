package com.weblearnex.app.api.service.orderpush;

import com.weblearnex.app.entity.master.ApiConfig;
import com.weblearnex.app.entity.order.SaleOrder;
import com.weblearnex.app.entity.setup.Courier;
import com.weblearnex.app.model.ResponseBean;

public interface OrderPushService {
    ResponseBean callOrderPushOrderApi(SaleOrder saleOrder, Courier courier, ApiConfig apiConfig);
    ResponseBean callOrderPushOrderApiByAwb(String awbNumber);
}
