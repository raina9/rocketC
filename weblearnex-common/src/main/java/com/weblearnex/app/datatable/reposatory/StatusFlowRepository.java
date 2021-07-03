package com.weblearnex.app.datatable.reposatory;

import com.weblearnex.app.entity.setup.StatusFlow;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface StatusFlowRepository extends DataTablesRepository<StatusFlow,Long> {
    StatusFlow findById(String id);
    StatusFlow findByStatusFlowName(String statusFlowName);
    StatusFlow findByStatusTransitionsList(String statusTransitionsList);
    List<StatusFlow> findByActive(Integer active);


}
