package com.weblearnex.app.service.impl;

import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.entity.setup.StatusTransition;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.datatable.reposatory.StatusTransitionRepository;
import com.weblearnex.app.service.StatusTransitionService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatusTransitionServiceImpl implements StatusTransitionService {

    Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StatusTransitionRepository statusTransitionRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    public ResponseBean<StatusTransition> addStatusTransition(StatusTransition statusTransition) {
        ResponseBean responseBean = new ResponseBean();

        if(statusTransition.getStatusTransitionName() == null || statusTransition.getStatusTransitionName().isEmpty() ||statusTransitionRepository.findByStatusTransitionName(statusTransition.getStatusTransitionName()).size()!=0||
           statusTransition.getFromStatusCode() == null || statusTransition.getFromStatusCode().isEmpty() || statusTransition.getToStatusCode() == null || statusTransition.getToStatusCode().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.ST_ALREADY_EXIST_MSG,null,null));
            return responseBean;
        }
        statusTransition.setStatusTransitionCode(statusTransition.getFromStatusCode()+"_"+statusTransition.getToStatusCode());
        if(statusTransition.getStatusTransitionCode() == null || statusTransition.getStatusTransitionCode().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.ST_CODE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if(statusTransitionRepository.findByStatusTransitionCode(statusTransition.getStatusTransitionCode()) !=null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.ST_ALREADY_EXIST_MSG, null, null));
            return responseBean;
        }

        statusTransition = statusTransitionRepository.save(statusTransition);
        if(statusTransition.getId() != null){
            responseBean.setResponseBody(statusTransition);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.ST_ADDED_MSG,null,null));
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.ST_ADDED_ERROR_MSG,null,null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<StatusTransition> updateStatusTransition(StatusTransition statusTransition) {
        ResponseBean responseBean = new ResponseBean();
        if(statusTransition.getId() == null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG,null,null));
            return responseBean;
        }
        statusTransition.setStatusTransitionCode(statusTransition.getFromStatusCode()+"_"+statusTransition.getToStatusCode());
        if(statusTransition.getStatusTransitionCode() == null || statusTransition.getStatusTransitionCode().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.ST_CODE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        statusTransitionRepository.save(statusTransition);
        responseBean.setResponseBody(statusTransition);
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.ST_UPDATED_MSG,null,null));
        return responseBean;
    }

    @Override
    public ResponseBean<StatusTransition> deleteStatusTransition(Long statusTransitionId) {
        ResponseBean responseBean = new ResponseBean();
        Optional<StatusTransition>  statusTransition = statusTransitionRepository.findById(statusTransitionId);
        if(!statusTransition.isPresent()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.STATUS_ID_NOT_FOUND,null,null));
            return responseBean;
        }
        statusTransition.get().setActive(AppProperty.IN_ACTIVE);
        statusTransitionRepository.save( statusTransition.get());
        responseBean.setResponseBody(statusTransition);
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.ST_DELETE_MSG,null,null));
        return responseBean;
    }

    @Override
    public ResponseBean<List<StatusTransition>> getAllStatusTransition() {
        ResponseBean responseBean = new ResponseBean();
        List<StatusTransition> statusTransitionList = (List<StatusTransition>) statusTransitionRepository.findAll();

        if(statusTransitionList != null && !statusTransitionList.isEmpty()){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG,null,null));
            responseBean.setResponseBody(statusTransitionList);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG,null,null));
        }
        return responseBean;
    }
    @Override
    public ResponseBean<StatusTransition> findByStatusTransitionId(Long id) {
        ResponseBean responseBean = new ResponseBean();
        Optional<StatusTransition> statusTransition = statusTransitionRepository.findById(id);
        if (!statusTransition.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.STATUS_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        responseBean.setResponseBody(statusTransition.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.STATUS_ID_FOUND, null, null));
        return responseBean;
    }

    @Override
    public DataTablesOutput<StatusTransition> getAllStatusTransitionPaginationAndSort(DataTablesInput input) {
        return statusTransitionRepository.findAll(input);
    }
}
