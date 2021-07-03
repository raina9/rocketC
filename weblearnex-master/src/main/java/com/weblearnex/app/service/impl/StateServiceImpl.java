package com.weblearnex.app.service.impl;

import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.BulkHeaderConstant;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.entity.master.BulkMaster;
import com.weblearnex.app.entity.master.Country;
import com.weblearnex.app.entity.master.State;
import com.weblearnex.app.model.BulkUploadBean;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.reposatory.CountryRepository;
import com.weblearnex.app.reposatory.StateRepository;
import com.weblearnex.app.service.BulkMasterService;
import com.weblearnex.app.service.BulkUploadService;
import com.weblearnex.app.service.StateService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StateServiceImpl implements StateService {
    Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StateRepository stateRepository;
    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private BulkMasterService bulkMasterService;


    @Override
    public ResponseBean<State> addState(State state) {
        ResponseBean responseBean = new ResponseBean();
        if (state.getCode() == null || state.getCode().isEmpty() || stateRepository.findByCode(state.getCode())!=null) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.STATE_CODE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (state.getStateName() == null || state.getStateName().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.STATE_NAME_ALREADY_EXIST_MSG, null, null));
            return responseBean;
        }
        if (state.getCountryCode() == null || state.getCountryCode().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COUNTRY_NAME_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }

        Country country =countryRepository.findByCode(state.getCountryCode().trim());
        if(country==null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COUNTRY_CODE_NOT_FOUND, null, null));
            return responseBean;
        }

        state = stateRepository.save(state);
        if (state.getId() != null) {
            responseBean.setResponseBody(state);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.STATE_ADDED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.STATE_ADDED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<State> updateState(State state) {
        ResponseBean responseBean = new ResponseBean();
        if (state.getCode() == null || state.getCode().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.STATE_CODE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }

        if (state.getId() == null ) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.STATE_ID_DOES_NOT_EMPTY_OR_NO_DATA_FOUND_IN_DATABASE, null, null));
            return responseBean;
        }
        Optional<State> existState =stateRepository.findById(state.getId());
        if (!existState.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.STATE_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        if (state.getCountryCode() == null || state.getCountryCode().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COUNTRY_CODE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }

        Country country =countryRepository.findByCode(state.getCountryCode());
        if(country==null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COUNTRY_CODE_NOT_FOUND, null, null));
            return responseBean;
        }
        existState.get().setActive(state.getActive());
        existState.get().setCode(state.getCode());
        existState.get().setStateName(state.getStateName());
        existState.get().setCountryCode(state.getCountryCode());

        if (stateRepository.save(existState.get())!=null) {
            responseBean.setResponseBody(state);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.STATE_UPDATED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.STATE_UPDATED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<State> deleteState(Long Id) {
        ResponseBean responseBean = new ResponseBean();
        Optional<State> state = stateRepository.findById(Id);
        if (!state.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.STATE_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        //State findState = state.get();
        state.get().setActive(AppProperty.IN_ACTIVE);


        if ( stateRepository.save(state.get())!=null) {
            responseBean.setResponseBody(state.get());
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.STATE_DELETE_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.STATE_UPDATED_ERROR_MSG, null, null));
        }

        return responseBean;
    }

    @Override
    public ResponseBean<List<State>> getAllStates() {
        ResponseBean responseBean = new ResponseBean();
        List<State> stateList = stateRepository.findAll();
        if(stateList !=null && !stateList.isEmpty()){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG, null, null));
            responseBean.setResponseBody(stateList);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<State> findByCode(String code) {
        ResponseBean responseBean = new ResponseBean();
        State state=stateRepository.findByCode(code);
        if(state==null||state.getCode().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.STATE_ID_NOT_FOUND,null,null));
            return responseBean;
        }
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG,null,null));
        responseBean.setResponseBody(state);
        return responseBean;
    }

    /*@Override
    public ResponseBean<State> findByStateName(String stateName) {
        ResponseBean responseBean = new ResponseBean();
        State c_name=stateRepository.findByStateName(stateName);
        if(c_name==null||c_name.getCountryName().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.STATE_NAME_NOT_EXIST_MSG,null,null));
            return responseBean;
        }
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG,null,null));
        responseBean.setResponseBody(c_name);

        return responseBean;
    }*/

    @Override
    public ResponseBean<List<State>> findByActive() {
        ResponseBean responseBean = new ResponseBean();
        List<State> stateList=stateRepository.findByActive(1);
        if(stateList != null && !stateList.isEmpty()){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG,null,null));
            responseBean.setResponseBody(stateList);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG,null,null));
        }
        return responseBean;
    }

    @Override
    public BulkUploadBean uploadBulk(BulkUploadBean bulkUploadBean, BulkMaster bulkMaster) {
        //ResponseBean responseBean = new ResponseBean();
        //BulkUploadBean bulkUploadResponseBean = new BulkUploadBean();
        List<Map<String, String>> errorRecord = new ArrayList<Map<String, String>>();
        List<Map<String, String>> successRecord = new ArrayList<Map<String, String>>();
        int count = 0;
        int progress = 0;
        String token = BulkUploadService.generateRandomString();
        for(Map<String, String> map : bulkUploadBean.getRecords()){
            // Preogress count
            count++;
            progress = BulkUploadService.calculateUploadPercentage(count, bulkUploadBean.getRecords().size());
            bulkMasterService.setUploadProgressCount(bulkMaster.getName(), progress, token, false);

            State state = new State();
            state.setCode(map.get(BulkHeaderConstant.STATE_CODE));
            state.setStateName(map.get(BulkHeaderConstant.STATE_NAME));
            state.setCountryCode(map.get(BulkHeaderConstant.COUNTRY_CODE));

            State stateDB = stateRepository.findByCode(state.getCode());
            ResponseBean stateResponseBean = null;
            if(stateDB != null){
                state.setId(stateDB.getId());
                stateResponseBean = updateState(state);
            }else {
                stateResponseBean = addState(state);
            }


            map.put(BulkHeaderConstant.MESSAGE,stateResponseBean.getMessage());
            if(stateResponseBean.getStatus().equals(ResponseStatus.FAIL)){
                errorRecord.add(map);
                continue;
            }
            successRecord.add(map);
        }
        bulkUploadBean.setBulkMaster(bulkMaster);
        bulkUploadBean.setErrorRecords(errorRecord);
        bulkUploadBean.setSuccessRecords(successRecord);
        bulkMasterService.setUploadProgressCount(bulkMaster.getName(), 100, token, true);
        return bulkUploadBean;
    }
}
