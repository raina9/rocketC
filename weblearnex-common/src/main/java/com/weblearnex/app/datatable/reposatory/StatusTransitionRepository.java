package com.weblearnex.app.datatable.reposatory;

import com.weblearnex.app.entity.setup.StatusTransition;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusTransitionRepository extends DataTablesRepository<StatusTransition,Long> {

    StatusTransition findById(String id);
    List<StatusTransition> findByStatusTransitionName(String statusTransitionName);
    StatusTransition findByStatusTransitionCode(String statusTransitionCode);
}
