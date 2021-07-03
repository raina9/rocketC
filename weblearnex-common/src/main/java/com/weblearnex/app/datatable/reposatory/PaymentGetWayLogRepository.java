package com.weblearnex.app.datatable.reposatory;

import com.weblearnex.app.entity.paymentwetway.PaymentGetWayLog;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentGetWayLogRepository extends DataTablesRepository<PaymentGetWayLog, Long> {
    Boolean existsByPaymentId(String paymentId);
    PaymentGetWayLog findByPaymentId(String paymentId);
    List<PaymentGetWayLog> findByClientCode(String clientCode);
}
