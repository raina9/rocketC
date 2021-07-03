package com.weblearnex.app.datatable.reposatory;

import com.weblearnex.app.entity.master.CourierPriority;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourierPriorityRepository extends DataTablesRepository<CourierPriority, Long> {

    CourierPriority findByCourierPriorityCode(String courierPriorityCode);
    CourierPriority findByCourierPriorityName(String courierPriorityName);
    CourierPriority findByPrioritys(String prioritys);
    List<CourierPriority> findAllByClientId(Long clientId);
}
