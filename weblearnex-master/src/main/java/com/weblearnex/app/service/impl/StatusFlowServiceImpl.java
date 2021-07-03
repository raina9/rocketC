package com.weblearnex.app.service.impl;

import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.datatable.reposatory.StatusFlowRepository;
import com.weblearnex.app.entity.setup.StatusFlow;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.StatusFlowService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatusFlowServiceImpl implements StatusFlowService {

    Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StatusFlowRepository statusFlowRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    public ResponseBean<StatusFlow> addStatusFlow(StatusFlow statusFlow) {
        log.info("Entering inside of StatusFlowServiceImpl class and addStatusFlow method");
        ResponseBean responseBean = new ResponseBean();
        if(statusFlowRepository.findByStatusFlowName(statusFlow.getStatusFlowName())!=null){
            log.info("Entering inside of StatusFlowServiceImpl class and addStatusFlow method and checking status name is existed or not");
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.STATUSFLOW_NAME_ALREADY_EXIST_MSG,null,null));
            return responseBean;
        }
        log.info("Entering inside of StatusFlowServiceImpl class and addStatusFlow method and inserting statusFlow data in database");
        statusFlow =statusFlowRepository.save(statusFlow);
        if(statusFlow.getId()!=null){
            responseBean.setResponseBody(statusFlow);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            log.info("Entering inside of StatusFlowServiceImpl class and addStatusFlow method and successfully save records");
            responseBean.setMessage(messageSource.getMessage(MessageProperty.STATUSFLOW_ADDED_MSG,null,null));
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            log.info("Entering inside of StatusFlowServiceImpl class and addStatusFlow method and fail to save records");
            responseBean.setMessage(messageSource.getMessage(MessageProperty.STATUSFLOW_ADDED_ERROR_MSG,null,null));
        }
        return responseBean;

    }

    @Override
    public ResponseBean<StatusFlow> updateStatusFlow(StatusFlow statusFlow) {
        ResponseBean responseBean = new ResponseBean();
        if(statusFlow.getId()==null||statusFlowRepository.findById(statusFlow.getId())==null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG,null,null));
            return responseBean;
        }
        statusFlowRepository.save(statusFlow);
        responseBean.setResponseBody(statusFlow);
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.STATUSFLOW_UPDATED_MSG,null,null));
        return responseBean;

    }

    @Override
    public ResponseBean<StatusFlow> deleteStatusFlow(Long id) {
        ResponseBean responseBean = new ResponseBean();
        Optional<StatusFlow> statusFlow=statusFlowRepository.findById(id);
        if(!statusFlow.isPresent()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.STATUSFLOW_ID_NOT_FOUND,null,null));
            return responseBean;
        }
        statusFlow.get().setActive(AppProperty.IN_ACTIVE);
        statusFlowRepository.save(statusFlow.get());
        responseBean.setResponseBody(statusFlow.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.STATUSFLOW_DELETE_MSG,null,null));
        return responseBean;

    }

    @Override
    public ResponseBean<List<StatusFlow>> getAllStatusFlow() {
        ResponseBean responseBean = new ResponseBean();

        List<StatusFlow> statusFlowList = (List<StatusFlow>) statusFlowRepository.findAll();

        if(statusFlowList != null && !statusFlowList.isEmpty()){
          responseBean.setStatus(ResponseStatus.SUCCESS);
          responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG,null,null));
          responseBean.setResponseBody(statusFlowList);
       }else{
         responseBean.setStatus(ResponseStatus.FAIL);
         responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG,null,null));
    }
        return responseBean;

    }
    @Override
    public ResponseBean<StatusFlow> findByStatusFlowId(Long id) {
        ResponseBean responseBean = new ResponseBean();
        Optional<StatusFlow> statusFlow = statusFlowRepository.findById(id);
        if (!statusFlow.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.STATUSFLOW_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        responseBean.setResponseBody(statusFlow.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.STATUSFLOW_ID_FOUND, null, null));
        return responseBean;
    }

    @Override
    public ResponseBean<List<StatusFlow>>  findByactive(){
        ResponseBean responseBean = new ResponseBean();
        List<StatusFlow> statusFlowList =statusFlowRepository.findByActive(1);
        if(statusFlowList != null && !statusFlowList.isEmpty()){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG,null,null));
            responseBean.setResponseBody(statusFlowList);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG,null,null));
        }
        return responseBean;

    }

    @Override
    public DataTablesOutput<StatusFlow> getAllStatusFlowPaginationAndSort(DataTablesInput input) {
        return statusFlowRepository.findAll(input);
    }


}
