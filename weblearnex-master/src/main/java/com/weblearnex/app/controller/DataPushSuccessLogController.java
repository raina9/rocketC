package com.weblearnex.app.controller;

import com.weblearnex.app.entity.logs.DataPushErrorLog;
import com.weblearnex.app.entity.logs.DataPushSuccessLog;
import com.weblearnex.app.service.DataPushErrorLogService;
import com.weblearnex.app.service.DataPushSuccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataPushSuccessLogController {

    @Autowired
    private DataPushSuccessLogService dataPushSuccessLogService;

    @PostMapping(value = "/dataPushSuccessLog/getAllDataPushSuccessLogPaginationAndSort")
    public DataTablesOutput<DataPushSuccessLog> getAllDataPushSuccessLogPaginationAndSort(@RequestBody DataTablesInput input) {
        return dataPushSuccessLogService.getAllDataPushSuccessLogPaginationAndSort(input);
    }
}
