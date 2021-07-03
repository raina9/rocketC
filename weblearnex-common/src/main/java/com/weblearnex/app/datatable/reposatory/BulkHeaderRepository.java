package com.weblearnex.app.datatable.reposatory;

import com.weblearnex.app.entity.master.BulkHeader;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;
//import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Collection;
import java.util.List;

@Repository
public interface BulkHeaderRepository extends DataTablesRepository<BulkHeader, Long> {

    BulkHeader findByDisplayName(String displayName);
    BulkHeader findByHeaderCode(String headerCode);
    List<BulkHeader> findBulkHeadersByHeaderCodeIn(Collection<String> headerCodes );


}
