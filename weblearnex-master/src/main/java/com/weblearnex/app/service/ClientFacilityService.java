package com.weblearnex.app.service;

import com.weblearnex.app.entity.master.ServiceType;
import com.weblearnex.app.entity.setup.Client;
import com.weblearnex.app.entity.setup.ClientFacility;
import com.weblearnex.app.model.ResponseBean;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import java.util.List;
import java.util.Map;

public interface ClientFacilityService {
    ResponseBean<ClientFacility> addClientFacility(ClientFacility clientFacility);
    ResponseBean<ClientFacility> updateClientFacility(ClientFacility clientFacility);
    ResponseBean<ClientFacility> deleteClientFacility(Long id);
    ResponseBean<List<ClientFacility>>getAllClientFacility();
    ResponseBean<ClientFacility> findByClientFacilityId(Long id);
    DataTablesOutput<ClientFacility> getAllClientFacilityPaginationAndSort(DataTablesInput input);
    ResponseBean<List<ServiceType>> getClientServiceType(Long clientId);
    ResponseBean<Map<String,String>> getClientAllowedCourier(Long clientId, Long serviceId);
}
