package com.weblearnex.app.service;

import com.weblearnex.app.entity.logs.DataPushSuccessLog;
import com.weblearnex.app.entity.setup.Client;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

public interface DataPushSuccessLogService {
    DataTablesOutput<DataPushSuccessLog> getAllDataPushSuccessLogPaginationAndSort(DataTablesInput input);
}
