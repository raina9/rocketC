package com.weblearnex.app.service;

import com.weblearnex.app.entity.master.ServiceProvider;
import com.weblearnex.app.entity.master.ServiceType;
import com.weblearnex.app.model.ResponseBean;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

public interface ServiceProviderService {
    ResponseBean<ServiceProvider> addServiceProvider(ServiceProvider serviceProvider);
    ResponseBean<ServiceProvider> updateServiceProvider(ServiceProvider serviceProvider);
    ResponseBean<ServiceProvider> deleteServiceProvider(Long serviceProviderId);
    ResponseBean<ServiceProvider> getAllServiceProvider();
    ResponseBean<ServiceProvider> findByServiceProviderID(Long id);

    DataTablesOutput<ServiceProvider> getAllServiceProviderPaginationAndSort(DataTablesInput input);
}
