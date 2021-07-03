package com.weblearnex.app.datatable.reposatory;

import com.weblearnex.app.entity.logs.DataPushSuccessLog;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataPushSuccessLogRepository  extends DataTablesRepository<DataPushSuccessLog, Long> {
}
