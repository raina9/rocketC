package com.weblearnex.app.datatable.reposatory;



import com.weblearnex.app.entity.master.ServiceType;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceTypeRepository extends DataTablesRepository<ServiceType, Long> {


    ServiceType findByServiceCode(String serviceCode);
    ServiceType findByServiceName(String serviceName);
    List<ServiceType> findAllByActive(Integer active);
}
