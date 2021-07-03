package com.weblearnex.app.controller;


import com.weblearnex.app.entity.logs.DataPushErrorLog;
import com.weblearnex.app.entity.logs.PendingVendarStatusMapping;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.PendingVendarStatusMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class PendingVendarStatusMappingController {

    @Autowired
    private PendingVendarStatusMappingService pendingVendarStatusMappingService;


    @PostMapping(value = "/pendingVendarStatusMapping/getAllPendingVendarStatusMappingPaginationAndSort")
    public DataTablesOutput<PendingVendarStatusMapping> getAllPendingVendarStatusMappingPaginationAndSort(@RequestBody DataTablesInput input) {
        return pendingVendarStatusMappingService.getAllPendingVendarStatusMappingPaginationAndSort(input);
    }

    @DeleteMapping("/deletePendingVendarStatusMapping/{PendingVendarStatusMappingId}")
    public ResponseEntity<ResponseBean> deletePendingVendarStatusMapping(@PathVariable(value="PendingVendarStatusMappingId") Long pendingVendarStatusMappingId) {
        return new ResponseEntity<ResponseBean>(pendingVendarStatusMappingService.deletePendingVendarStatusMapping(pendingVendarStatusMappingId), HttpStatus.OK);
    }
}
