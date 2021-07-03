package com.weblearnex.app.datatable.reposatory;

import com.weblearnex.app.entity.master.RateCardType;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RateCardTypeRepository extends DataTablesRepository<RateCardType, Long> {
    RateCardType findByTypeCode(String typeCode);
    List<RateCardType> findByActive(Integer active);
}
