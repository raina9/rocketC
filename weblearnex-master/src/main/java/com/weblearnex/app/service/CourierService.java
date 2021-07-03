package com.weblearnex.app.service;

import com.weblearnex.app.constant.PaymentType;
import com.weblearnex.app.entity.order.SaleOrder;
import com.weblearnex.app.entity.setup.ClientWarehouse;
import com.weblearnex.app.entity.setup.Courier;
import com.weblearnex.app.model.CourierServiceabilityBean;
import com.weblearnex.app.model.CourierServiceabilityResponseBean;
import com.weblearnex.app.model.ResponseBean;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CourierService extends BulkUploadService{
    ResponseBean<Courier> addCourier(Courier courier);
    ResponseBean<Courier> updateCourier(Courier courier);
    ResponseBean<Courier> deleteCourier(Long Id);
    ResponseBean<List<Courier>> getAllCourier();
    ResponseBean<List<Courier>> findByActive();
    ResponseBean<Courier> findByCourierId(Long id);
    DataTablesOutput<Courier> getAllCourierPaginationAndSort(DataTablesInput input);
    ResponseBean<Map<String, List<CourierServiceabilityResponseBean>>> courierServiceabilityCheck(CourierServiceabilityBean serviceabilityBean);
    ResponseBean isCourierServiceable(String courierCode, PaymentType paymentType, String souPincode, String destPincode);
    ResponseBean<Map<String, List<CourierServiceabilityResponseBean>>> clientServiceProviders(String awbNumber, String clientWarehouseCode);
    ResponseBean<Map<String, List<CourierServiceabilityResponseBean>>> getServiceProvidersForBulk();
    ResponseEntity<Resource> courierReport();
    ResponseBean<Map<String, Object>> getAllCourierByServiceWise();
}
