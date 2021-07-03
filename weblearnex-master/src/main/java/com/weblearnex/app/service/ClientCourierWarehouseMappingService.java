package com.weblearnex.app.service;

import com.weblearnex.app.entity.setup.ClientCourierWarehouseMapping;
import com.weblearnex.app.model.ResponseBean;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.ResponseEntity;

public interface ClientCourierWarehouseMappingService extends BulkUploadService {

    ResponseBean<ClientCourierWarehouseMapping> addClientCourierWarehouseMapping(ClientCourierWarehouseMapping clientCourierWarehouseMapping);
    ResponseBean<ClientCourierWarehouseMapping> updateClientCourierWarehouseMapping(ClientCourierWarehouseMapping clientCourierWarehouseMapping);
    ResponseBean<ClientCourierWarehouseMapping> deleteClientCourierWarehouseMapping(Long clientCourierWarehouseMappingId);
    ResponseBean<ClientCourierWarehouseMapping> getAllClientCourierWarehouseMapping();
    ResponseBean<ClientCourierWarehouseMapping> findByClientCourierWarehouseMapping(Long id);
    DataTablesOutput<ClientCourierWarehouseMapping> getAllClientCourierWarehouseMappingPaginationAndSort(DataTablesInput input);
    ResponseEntity<Resource> clientCourierWarehouseMappingReport();
}
