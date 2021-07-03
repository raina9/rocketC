package com.weblearnex.app.service.impl;

import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.datatable.reposatory.RateCardTypeRepository;
import com.weblearnex.app.entity.master.RateCardType;
import com.weblearnex.app.entity.setup.Branch;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.RateCardTypeService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RateCardTypeServiceImpl implements RateCardTypeService {
    Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RateCardTypeRepository rateCardTypeRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    public ResponseBean<RateCardType> addRateCardType(RateCardType rateCardType) {
        ResponseBean responseBean = new ResponseBean();
        if (rateCardType.getTypeName() == null || rateCardType.getTypeName().trim().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RATE_CARD_TYPE_NAME_SHOULD_NOT_NULL_OR_EMPTY, null, null));
            return responseBean;
        }
        RateCardType rateCardTypeDB = rateCardTypeRepository.findByTypeCode(rateCardType.getTypeCode());
        if (rateCardType.getTypeCode() == null || rateCardType.getTypeCode().trim().isEmpty() ||  rateCardTypeDB != null) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RATE_CARD_TYPE_CODE_ALREADY_EXIST_MSG, null, null));
            return responseBean;
        }
        rateCardType = rateCardTypeRepository.save(rateCardType);
        if (rateCardType.getId() != null) {
            responseBean.setResponseBody(rateCardType);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RATE_CARD_TYPE_ADDED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RATE_CARD_TYPE_ADDED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<RateCardType> updateRateCardType(RateCardType rateCardType) {
        ResponseBean responseBean = new ResponseBean();

        if (rateCardType.getId() == null || !rateCardTypeRepository.findById(rateCardType.getId()).isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.BULKHEADER_ID_DOES_NOT_EMPTY_OR_NO_DATA_FOUND_IN_DATABASE, null, null));
            return responseBean;
        }

        rateCardType = rateCardTypeRepository.save(rateCardType);
        if (rateCardType != null) {
            responseBean.setResponseBody(rateCardType);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RATE_CARD_TYPE_UPDATED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RATE_CARD_TYPE_UPDATED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<RateCardType> deleteRateCardType(Long rateCardTypeId) {
        ResponseBean responseBean = new ResponseBean();
        Optional<RateCardType> rateCardType = rateCardTypeRepository.findById(rateCardTypeId);
        if (!rateCardType.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RATE_CARD_TYPE_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        rateCardType.get().setActive(AppProperty.IN_ACTIVE);
        rateCardTypeRepository.save(rateCardType.get());
        responseBean.setResponseBody(rateCardType.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.RATE_CARD_TYPE_DELETE_MSG, null, null));
        return responseBean;
    }

    @Override
    public ResponseBean<RateCardType> findByRateCardTypeId(Long id) {
        ResponseBean responseBean = new ResponseBean();
        Optional<RateCardType> rateCardType = rateCardTypeRepository.findById(id);
        if (!rateCardType.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RATE_CARD_TYPE_ID_NOT_FOUND, null, null));
            return responseBean;
        }

        responseBean.setResponseBody(rateCardType.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.RATE_CARD_TYPE_ID_FOUND, null, null));
        return responseBean;
    }

    @Override
    public DataTablesOutput<RateCardType> getAllRateCardType(DataTablesInput input) {
        return rateCardTypeRepository.findAll(input);
    }

    @Override
    public ResponseBean<List<RateCardType>> getAllActiveRateCardType() {
        ResponseBean responseBean = new ResponseBean();
        List<RateCardType> rateCardTypeList =rateCardTypeRepository.findByActive(1);
        if(rateCardTypeList != null && !rateCardTypeList.isEmpty()){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG,null,null));
            responseBean.setResponseBody(rateCardTypeList);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG,null,null));
        }
        return responseBean;
    }
}
