package com.weblearnex.app.service;

import com.weblearnex.app.entity.logs.DataPushErrorLog;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

public interface DataPushErrorLogService {

    DataTablesOutput<DataPushErrorLog> getAllDataPushErrorLogPaginationAndSort(DataTablesInput input);
}
