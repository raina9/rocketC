package com.weblearnex.app.datatable.reposatory;



import com.weblearnex.app.entity.setup.Courier;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourierRepository extends DataTablesRepository<Courier, Long> {
    Courier findByCourierName(String courierName);
    Courier findByCourierCode(String courierCode);
    List<Courier> findAllByServiceTypeIdAndActive(Long serviceId, Integer active);
    List<Courier> findAllByServiceProviderIdAndActive(Long serviceProviderId, Integer active);
    List<Courier> findByActive(Integer active);
    boolean existsByCourierCode(String courierCode);
    List<Courier> findByCourierCodeInAndActive(List<String> courierCodes,Integer active);
    Courier findByServiceProviderCourierCode(String serviceProviderCourierCode);


}
