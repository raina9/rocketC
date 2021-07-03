package com.weblearnex.app.datatable.reposatory;

import com.weblearnex.app.entity.master.ServiceProvider;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceProviderRepository extends DataTablesRepository<ServiceProvider, Long> {

    ServiceProvider findByServiceProviderCode(String serviceProviderCode);
    ServiceProvider findByServiceProviderName(String serviceProviderCode);

}
