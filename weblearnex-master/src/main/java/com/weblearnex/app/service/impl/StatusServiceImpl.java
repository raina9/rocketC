package com.weblearnex.app.service.impl;

import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.constant.StatusType;
import com.weblearnex.app.entity.setup.Status;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.datatable.reposatory.StatusRepository;
import com.weblearnex.app.service.StatusService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatusServiceImpl implements StatusService {

    Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    public ResponseBean<Status> addStatus(Status status) {
        ResponseBean responseBean = new ResponseBean();
        if(status.getStatusCode() == null || status.getStatusCode().isEmpty() || statusRepository.findByStatusCode(status.getStatusCode()) !=null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.STATUS_ALREADY_EXIST_MSG,null,null));
            return responseBean;
        }
        Status dbstatus = statusRepository.save(status);
        if(dbstatus.getId() != null){
            responseBean.setResponseBody(dbstatus);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.STATUS_ADDED_MSG,null,null));
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.STATUS_ADDED_ERROR_MSG,null,null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<Status> updateStatus(Status status) {
        ResponseBean responseBean = new ResponseBean();
        Status dbstatus = statusRepository.save(status);
        if (dbstatus != null) {
            responseBean.setResponseBody(dbstatus);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.STATUS_UPDATED_MSG,null,null));
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.STATUS_UPDATED_ERROR_MSG,null,null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<Status> deleteStatus(Long statusId) {
        ResponseBean responseBean = new ResponseBean();
        Optional<Status>  status= statusRepository.findById(statusId);
        if(!status.isPresent()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.STATUS_ID_NOT_FOUND,null,null));
            return responseBean;
        }
        status.get().setActive(AppProperty.IN_ACTIVE);
        statusRepository.save( status.get());
        responseBean.setResponseBody(status);
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.STATUS_DELETE_MSG,null,null));
        return responseBean;
    }

    @Override
    public ResponseBean<List<Status>> getAllStatusByType(StatusType statusType) {
        ResponseBean responseBean = new ResponseBean();
        List<Status> statusList = statusRepository.findAllByStatusType(statusType);
        if(statusList != null && !statusList.isEmpty()){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG,null,null));
            responseBean.setResponseBody(statusList);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG,null,null));
        }
        return responseBean;
    }
    @Override
    public ResponseBean<Status> findByStatusId(Long id) {
        ResponseBean responseBean = new ResponseBean();
        Optional<Status> status = statusRepository.findById(id);
        if (!status.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.STATUS_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        responseBean.setResponseBody(status.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.STATUS_ID_FOUND, null, null));
        return responseBean;
    }
    @Override
    public ResponseBean<List<Status>> getAllStatus() {
        ResponseBean responseBean = new ResponseBean();
        List<Status> statusList = statusRepository.findAllByStatusType(StatusType.PACKET_STATUS);
        if(statusList != null && !statusList.isEmpty()){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG,null,null));
            responseBean.setResponseBody(statusList);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG,null,null));
        }
        return responseBean;
    }

    @Override
    public DataTablesOutput<Status> getAllStatusPaginationAndSort(DataTablesInput input) {
        return statusRepository.findAll(input);
    }
}
