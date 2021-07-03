package com.weblearnex.app.datatable.reposatory;

import com.weblearnex.app.entity.paymentwetway.ClientWallet;
import com.weblearnex.app.entity.paymentwetway.PaymentGetWayLog;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientWalletRepository extends DataTablesRepository<ClientWallet, Long> {
    ClientWallet findByClientCode(String clientCode);
}
