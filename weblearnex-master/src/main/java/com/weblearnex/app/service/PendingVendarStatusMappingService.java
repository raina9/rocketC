package com.weblearnex.app.service;


import com.weblearnex.app.entity.logs.PendingVendarStatusMapping;
import com.weblearnex.app.entity.setup.Page;
import com.weblearnex.app.model.ResponseBean;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

public interface PendingVendarStatusMappingService {
    ResponseBean<PendingVendarStatusMapping> deletePendingVendarStatusMapping(Long PendingVendarStatusMappingId);
    DataTablesOutput<PendingVendarStatusMapping> getAllPendingVendarStatusMappingPaginationAndSort(DataTablesInput input);
}
