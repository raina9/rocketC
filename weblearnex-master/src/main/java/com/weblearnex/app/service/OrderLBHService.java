package com.weblearnex.app.service;

import com.weblearnex.app.entity.master.OrderLBH;
import com.weblearnex.app.model.ResponseBean;

public interface OrderLBHService extends BulkUploadService{
    ResponseBean<OrderLBH> addLBH(OrderLBH orderLBH);
    ResponseBean<OrderLBH> findByOrderLBHId(Long id);
}
