package com.weblearnex.app.datatable.reposatory;

import com.weblearnex.app.entity.manifest.ThreeplManifest;
import com.weblearnex.app.entity.order.SaleOrder;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThreeplManifestRepository extends DataTablesRepository<ThreeplManifest,Long> {
    List<ThreeplManifest> findByawbNumbersIn(List<String> awnNoList);
    List<ThreeplManifest> findByManifestNumberIn(List<String> manifestNumber);
    List<ThreeplManifest> findAllByClientCode(String clientCode);
    ThreeplManifest findByManifestNumber(String manifestNumber);
}
