package com.weblearnex.app.datatable.reposatory;

import com.weblearnex.app.entity.logs.PendingVendarStatusMapping;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PendingVendarStatusMappingRepository extends DataTablesRepository<PendingVendarStatusMapping, Long> {
}
