package com.weblearnex.app.service;



import com.weblearnex.app.entity.order.SaleOrder;
import com.weblearnex.app.model.ResponseBean;

import java.util.Map;

public interface SupportService extends BulkUploadService{
    ResponseBean<SaleOrder> orderCancellation(String awbNo);
    ResponseBean<Map<String,Object>> getDeclaredClassFields();
    ResponseBean updateDeclaredClassFields(String fieldName, String value, String awbs);
}
