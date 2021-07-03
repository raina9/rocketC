package com.weblearnex.app.datatable.reposatory;

import com.weblearnex.app.entity.master.Country;
import com.weblearnex.app.entity.setup.Client;
import com.weblearnex.app.entity.setup.Status;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends DataTablesRepository<Client, Long> {
    Client findById(String id);
    Client findByClientCode(String clientCode);
    Client findByClientName(String clientName);
    List<Client> findByActive(Integer active);
    boolean existsByClientCode(String clientCode);
    List<Client> findAllByActiveAndClientCodeIn(Integer integer, List<String> clientCodes);

}
