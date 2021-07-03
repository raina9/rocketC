package com.weblearnex.app.service;

import com.weblearnex.app.entity.master.BulkMaster;
import com.weblearnex.app.entity.setup.StatusTransition;
import com.weblearnex.app.model.ResponseBean;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import java.util.List;

public interface StatusTransitionService {
    ResponseBean<StatusTransition> addStatusTransition(StatusTransition statusTransition);
    ResponseBean<StatusTransition> updateStatusTransition(StatusTransition statusTransition);
    ResponseBean<StatusTransition> deleteStatusTransition(Long statusTransition);
    ResponseBean<List<StatusTransition>> getAllStatusTransition();
    ResponseBean<StatusTransition> findByStatusTransitionId(Long id);

    DataTablesOutput<StatusTransition> getAllStatusTransitionPaginationAndSort(DataTablesInput input);
}
