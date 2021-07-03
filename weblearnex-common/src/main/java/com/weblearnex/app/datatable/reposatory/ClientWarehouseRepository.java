package com.weblearnex.app.datatable.reposatory;



import com.weblearnex.app.entity.setup.ClientWarehouse;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientWarehouseRepository extends DataTablesRepository<ClientWarehouse, Long> {

    // ClientWarehouse findByClientCode(String clientCode);
    List<ClientWarehouse>  findAllByClientCode(String clientCode);
    ClientWarehouse findByWarehouseCode(String warehouseCode);

}
