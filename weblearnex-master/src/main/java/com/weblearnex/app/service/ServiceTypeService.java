package com.weblearnex.app.service;

import com.weblearnex.app.entity.master.ServiceType;
import com.weblearnex.app.model.ResponseBean;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

public interface ServiceTypeService {
    ResponseBean<ServiceType> addServiceType(ServiceType serviceType);
    ResponseBean<ServiceType> updateServiceType(ServiceType serviceType);
    ResponseBean<ServiceType> deleteServiceType(Long serviceTypeId);
    ResponseBean<ServiceType> getAllServiceType();
    ResponseBean<ServiceType> findByServiceTypeId(Long id);

    DataTablesOutput<ServiceType> getAllServiceTypePaginationAndSort(DataTablesInput input);
    ResponseBean<ServiceType> getClientServiceCourier();
}
