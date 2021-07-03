package com.weblearnex.app.controller;

import com.weblearnex.app.entity.logs.DataPushErrorLog;
import com.weblearnex.app.entity.setup.Client;
import com.weblearnex.app.service.DataPushErrorLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class DataPushErrorLogController {

    @Autowired
    private DataPushErrorLogService dataPushErrorLogService;

    @PostMapping(value = "/dataPushErrorLog/getAllDataPushErrorLogPaginationAndSort")
    public DataTablesOutput<DataPushErrorLog> getAllDataPushErrorLogPaginationAndSort(@RequestBody DataTablesInput input) {
        return dataPushErrorLogService.getAllDataPushErrorLogPaginationAndSort(input);
    }
}
