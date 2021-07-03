package com.weblearnex.app.service;

import com.weblearnex.app.entity.setup.StatusFlow;
import com.weblearnex.app.entity.setup.StatusTransition;
import com.weblearnex.app.model.ResponseBean;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import java.util.List;

public interface StatusFlowService {
    ResponseBean<StatusFlow> addStatusFlow(StatusFlow statusFlow);
    ResponseBean<StatusFlow> updateStatusFlow(StatusFlow statusFlow);
    ResponseBean<StatusFlow> deleteStatusFlow(Long id);
    ResponseBean<List<StatusFlow>>getAllStatusFlow();
    ResponseBean<StatusFlow> findByStatusFlowId(Long id);
    ResponseBean<List<StatusFlow>> findByactive();

    DataTablesOutput<StatusFlow> getAllStatusFlowPaginationAndSort(DataTablesInput input);
}
