package com.weblearnex.app.datatable.reposatory;

import com.weblearnex.app.entity.master.CourierStatusMapping;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourierStatusMappingRepository extends DataTablesRepository<CourierStatusMapping, Long> {

    boolean existsByCourierId(Long courierId);
    CourierStatusMapping findByCourierId(Long courierId);
    CourierStatusMapping findByServiceProviderId(Long serviceProviderId);
}
