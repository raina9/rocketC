package com.weblearnex.app.datatable.reposatory;


import com.weblearnex.app.entity.setup.Configration;
import com.weblearnex.app.entity.setup.Page;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageRepository extends DataTablesRepository<Page, Long> {
    Page findByDisplayName(String displayName);
    Page findByPageURL(String pageURL);

}
