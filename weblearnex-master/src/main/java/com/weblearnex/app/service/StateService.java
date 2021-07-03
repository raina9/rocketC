package com.weblearnex.app.service;

import com.weblearnex.app.entity.master.State;
import com.weblearnex.app.model.ResponseBean;

import java.util.List;

public interface StateService extends BulkUploadService{
    ResponseBean<State> addState(State state);
    ResponseBean<State> updateState(State state);
    ResponseBean<State> deleteState(Long Id);
    ResponseBean<List<State>> getAllStates();

    ResponseBean<State> findByCode(String code);
    //ResponseBean<State> findByStateName(String stateName);
    ResponseBean<List<State>> findByActive();
}
