package com.weblearnex.app.datatable.reposatory;


import com.weblearnex.app.entity.remittance.CourierRemittance;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourierRemittanceRepository extends DataTablesRepository<CourierRemittance, Long> {
    CourierRemittance findByRemittanceNo(String remittanceNo);
}
