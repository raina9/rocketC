package com.weblearnex.app.service.impl;

import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.entity.setup.Configration;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.datatable.reposatory.ConfigrationRepository;
import com.weblearnex.app.reposatory.StateRepository;
import com.weblearnex.app.service.ConfigrationService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConfigrationServiceImpl implements ConfigrationService {

    Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ConfigrationRepository configrationRepository;

    @Autowired
    private StateRepository sateRepository;

    @Autowired
    private MessageSource messageSource;


    @Override
    public ResponseBean<Configration> addConfigration(Configration configration) {
        ResponseBean responseBean = new ResponseBean();
        if (configration.getConfigType() == null || configration.getConfigType().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CONFIGRATION_TYPE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (configration.getConfigCode() == null || configration.getConfigCode().isEmpty() || configrationRepository.findByConfigCode(configration.getConfigCode()) != null) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CONFIGRATION_CODE_ALREADY_EXIST_MSG, null, null));
            return responseBean;
        }
        if (configration.getConfigValue() == null || configration.getConfigValue().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CONFIGRATION_VALUE_ALREADY_EXIST_MSG, null, null));
            return responseBean;
        }
        configration = configrationRepository.save(configration);
        if (configration.getId() != null) {
            responseBean.setResponseBody(configration);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CONFIGRATION_ADDED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CONFIGRATION_ADDED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<Configration> updateConfigration(Configration configration) {
        ResponseBean responseBean = new ResponseBean();
        if (configration.getConfigType() == null || configration.getConfigType().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Configration type field is empty.");
            return responseBean;
        }
        if (configration.getConfigValue() == null || configration.getConfigValue().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CONFIGRATION_VALUE_ALREADY_EXIST_MSG, null, null));
            return responseBean;
        }
        if (configration.getConfigCode() == null || configration.getConfigCode().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CONFIGRATION_CODE_DOES_NOT_EMPTY_OR_NO_DATA_FOUND_IN_DATABASE, null, null));
            return responseBean;
        }
        // Configration code updation not alloude.
        Optional<Configration> configrationDB = configrationRepository.findById(configration.getId());
        if(!configrationDB.isPresent()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("ID is requried at update time.");
            return responseBean;
        }
        configration.setConfigCode(configrationDB.get().getConfigCode());

        configration = configrationRepository.save(configration);
        if (configration != null) {
            responseBean.setResponseBody(configration);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CONFIGRATION_UPDATED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CONFIGRATION_UPDATED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<Configration> deleteConfigration(Long configrationId) {
        ResponseBean responseBean = new ResponseBean();
        Optional<Configration> configration = configrationRepository.findById(configrationId);
        if (!configration.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CONFIGRATION_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        //Configration findConfigration = configration.get();
        configration.get().setActive(AppProperty.IN_ACTIVE);
        configrationRepository.save(configration.get());
        responseBean.setResponseBody(configration.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.CONFIGRATION_DELETE_MSG, null, null));
        return responseBean;
    }

    @Override
    public ResponseBean<Configration> findByConfigId(Long configrationId) {
        ResponseBean responseBean = new ResponseBean();
        Optional<Configration> status = configrationRepository.findById(configrationId);
        if (!status.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CONFIGRATION_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        responseBean.setResponseBody(status.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.CONFIGRATION_ID_FOUND, null, null));
        return responseBean;
    }

    @Override
    public DataTablesOutput<Configration> getAllConfigrationPaginationAndSort(DataTablesInput input) {
        return configrationRepository.findAll(input);
    }

    @Override
    public ResponseBean<Configration> getAllConfigration() {
        ResponseBean responseBean = new ResponseBean();
        List<Configration> configrationList = (List<Configration>) configrationRepository.findAll();
        if(configrationList !=null && !configrationList.isEmpty()){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG, null, null));
            responseBean.setResponseBody(configrationList);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG, null, null));
        }
        return responseBean;
    }
}
