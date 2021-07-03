package com.weblearnex.app.datatable.reposatory;
import com.weblearnex.app.entity.setup.Branch;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchRepository extends DataTablesRepository<Branch, Long> {
    Branch findByBranchCode(String branchCode);
    Branch findByName(String name);
    Branch findByParentBranchId(String parentBranchId);
    boolean existsByBranchCode(String branchCode);
    List<Branch> findByActive(Integer active);

}
