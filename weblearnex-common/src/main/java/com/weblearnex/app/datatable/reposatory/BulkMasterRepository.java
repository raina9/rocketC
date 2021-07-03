package com.weblearnex.app.datatable.reposatory;


import com.weblearnex.app.entity.master.BulkHeader;
import com.weblearnex.app.entity.master.BulkMaster;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BulkMasterRepository extends DataTablesRepository<BulkMaster, Long> {

    BulkMaster findByName(String Name);
    BulkMaster findBybulkHeaderIds(String bulkHeaderIds);


}
