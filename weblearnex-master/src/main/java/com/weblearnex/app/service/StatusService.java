package com.weblearnex.app.service;

import com.weblearnex.app.constant.StatusType;
import com.weblearnex.app.entity.setup.Status;
import com.weblearnex.app.entity.setup.StatusFlow;
import com.weblearnex.app.entity.setup.StatusTransition;
import com.weblearnex.app.model.ResponseBean;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import java.util.List;

public interface StatusService {
    ResponseBean<Status> addStatus(Status status);
    ResponseBean<Status> updateStatus(Status status);
    ResponseBean<Status> deleteStatus(Long statusId);
    ResponseBean<List<Status>> getAllStatusByType(StatusType statusType);
    ResponseBean<Status> findByStatusId(Long id);
    ResponseBean<List<Status>> getAllStatus();

    DataTablesOutput<Status> getAllStatusPaginationAndSort(DataTablesInput input);
}
