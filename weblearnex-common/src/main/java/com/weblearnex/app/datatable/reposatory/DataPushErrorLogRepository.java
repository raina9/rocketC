package com.weblearnex.app.datatable.reposatory;

import com.weblearnex.app.entity.logs.DataPushErrorLog;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataPushErrorLogRepository extends DataTablesRepository<DataPushErrorLog, Long> {
}
