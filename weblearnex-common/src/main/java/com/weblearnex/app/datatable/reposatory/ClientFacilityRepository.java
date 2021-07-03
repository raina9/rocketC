package com.weblearnex.app.datatable.reposatory;

import com.weblearnex.app.constant.DeliveryType;
import com.weblearnex.app.entity.setup.Client;
import com.weblearnex.app.entity.setup.ClientFacility;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientFacilityRepository extends DataTablesRepository<ClientFacility, Long> {

    ClientFacility findById(String id);
    ClientFacility findByClient(Client client);
    ClientFacility findByDeliveryAttempt(Integer deliveryAttempt);
    ClientFacility findByDeliveryType(DeliveryType deliveryType);
    ClientFacility findByClientId(Long clientId);
    List<ClientFacility> findAllByWalletActive(Boolean walletActive);

}
