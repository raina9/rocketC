package com.weblearnex.app.datatable.reposatory;

import com.weblearnex.app.entity.master.DomasticRateCard;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

public interface DomasticRateCardRepository extends DataTablesRepository<DomasticRateCard, Long> {

    DomasticRateCard findByRateCardCode(String rateCardCode);
    DomasticRateCard findByCourierCode(String courierCode);
    DataTablesOutput<DomasticRateCard> findAll(DataTablesInput input);
    boolean existsByCourierCodeAndRateCardTypeCodeAndServiceProviderCode(String courierCode, String ratecardTypeCode, String serviceProviderCode);
    DomasticRateCard findByCourierCodeAndRateCardTypeCodeAndServiceProviderCode(String courierCode, String ratecardTypeCode, String serviceProviderCode);
}
