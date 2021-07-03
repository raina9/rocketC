package com.weblearnex.app.datatable.reposatory;

import com.weblearnex.app.entity.remittance.ClientRemittance;
import com.weblearnex.app.entity.setup.Client;
import com.weblearnex.app.entity.setup.Status;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRemittanceRepository extends DataTablesRepository<ClientRemittance, Long> {
    List<ClientRemittance> findByStatus(String status);
    ClientRemittance findByRemittanceNo(String remittanceNo);
}
