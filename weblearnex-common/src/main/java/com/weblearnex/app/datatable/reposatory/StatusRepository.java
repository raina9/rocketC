package com.weblearnex.app.datatable.reposatory;

import com.weblearnex.app.constant.StatusType;
import com.weblearnex.app.entity.setup.Status;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusRepository extends DataTablesRepository<Status, Long> {

    Status findByStatusCode(String statusCode);
    Status findById(String id);
    List<Status> findAllByStatusType(StatusType statusType);
    List<Status> findByStatusNameContains(String statusName);
    List<Status> findAllByActiveAndStatusCodeIn(Integer integer, List<String> statusCode);

}
