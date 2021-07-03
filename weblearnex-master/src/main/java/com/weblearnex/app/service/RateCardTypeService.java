package com.weblearnex.app.service;

import com.weblearnex.app.entity.master.RateCardType;
import com.weblearnex.app.model.ResponseBean;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import java.util.List;

public interface RateCardTypeService {
    ResponseBean<RateCardType> addRateCardType(RateCardType rateCardType);
    ResponseBean<RateCardType> updateRateCardType(RateCardType rateCardType);
    ResponseBean<RateCardType> deleteRateCardType(Long rateCardTypeId);
    ResponseBean<RateCardType> findByRateCardTypeId(Long id);
    DataTablesOutput<RateCardType> getAllRateCardType(DataTablesInput input);
    ResponseBean<List<RateCardType>> getAllActiveRateCardType();
}
