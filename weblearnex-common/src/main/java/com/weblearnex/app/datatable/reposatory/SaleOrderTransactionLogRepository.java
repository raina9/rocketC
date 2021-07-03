package com.weblearnex.app.datatable.reposatory;


import com.weblearnex.app.entity.order.SaleOrderTransactionLog;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleOrderTransactionLogRepository extends DataTablesRepository<SaleOrderTransactionLog, Long> {

}
