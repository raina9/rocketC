package com.weblearnex.app.api.service.orderpush;

import com.weblearnex.app.entity.order.SaleOrder;
import com.weblearnex.app.entity.setup.Courier;
import com.weblearnex.app.model.ResponseBean;

public interface PushService {
    ResponseBean getObjectForPush(SaleOrder saleOrder, Courier courier);
}
