package com.weblearnex.app.datatable.reposatory;

import com.weblearnex.app.entity.setup.ClientCourierWarehouseMapping;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientCourierWarehouseMappingRepository extends DataTablesRepository<ClientCourierWarehouseMapping, Long> {
    ClientCourierWarehouseMapping findByClientCode(String clientCode);
    ClientCourierWarehouseMapping findByClientWarehouseCode(String clientWarehouseCode);
    ClientCourierWarehouseMapping findByServiceProviderCode(String serviceProviderCode);
    ClientCourierWarehouseMapping findByServiceProviderWarehouseCode(String serviceProviderWarehouseCode);
    /* ClientCourierWarehouseMapping findByClientCodeAndClientWarehouseCodeAndServiceProviderCodeAndServiceProviderWarehouseCode
            (String clientCode, String clientWarehouseCode, String serviceProviderCode, String serviceProviderWarehouseCode); */
    ClientCourierWarehouseMapping findByClientCodeAndClientWarehouseCodeAndServiceProviderID
            (String clientCode, String clientWarehouseCode, Long serviceProviderID);

}
