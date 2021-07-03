package com.weblearnex.app.service;

import com.weblearnex.app.entity.setup.ClientWarehouse;
import com.weblearnex.app.model.ResponseBean;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ClientWarehouseService {
    ResponseBean<ClientWarehouse> addClientWarehouse(ClientWarehouse clientWarehouse);
    ResponseBean<ClientWarehouse> updateClientWarehouse(ClientWarehouse clientWarehouse);
    ResponseBean<ClientWarehouse> deleteClientWarehouse(Long clientWarehouseId);
    ResponseBean<ClientWarehouse> getAllClientWarehouse();
    ResponseBean<ClientWarehouse> findByClientWarehouse(Long id);
    DataTablesOutput<ClientWarehouse> getAllClientWarehousePaginationAndSort(DataTablesInput input);
    ResponseBean<List<ClientWarehouse>> getAllClientWarehouseByClientCode(String clientCode);
    ResponseBean<List<ClientWarehouse>> getLoginUserWarehouses();
    ResponseEntity<Resource> clientWarehouseReport();
}
