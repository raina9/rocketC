package com.weblearnex.app.status.engine.impl;

import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.entity.setup.Status;
import com.weblearnex.app.entity.setup.StatusFlow;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.datatable.reposatory.StatusFlowRepository;
import com.weblearnex.app.status.engine.StatusEngine;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Scope("singleton")
public class StatusEngineImpl implements StatusEngine {
    Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private StatusFlowRepository statusFlowRepository;

    @Autowired
    @Qualifier("applicionConfig")
    private MessageSource applicionConfig;

    private Map<String,List<String>> statusFlowMap;

    @Override
    public ResponseBean reloadStatusFlow() {
        ResponseBean responseBean = new ResponseBean();
        List<StatusFlow> statusFlowList = (List<StatusFlow>) statusFlowRepository.findAll();
        if (!statusFlowList.isEmpty()){
            statusFlowMap = new HashMap<String,List<String>>();
        }
        for (StatusFlow statusFlow : statusFlowList){
            statusFlowMap.put(statusFlow.getStatusFlowName(),new ArrayList<>(Arrays.asList(statusFlow.getStatusTransitionsList().split(","))));
        }
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setResponseBody(statusFlowMap);
        return responseBean;
    }

    @Override
    public boolean isValidStatus(String statusFlowName, Status fromStatus, Status toStatus) {
        if(statusFlowMap == null || statusFlowMap.isEmpty()){
            reloadStatusFlow();
        }
        List<String> statusTransitionList = statusFlowMap.get(statusFlowName);
        String statusTransitionKey = fromStatus.getStatusCode()+"_"+toStatus.getStatusCode();
        if(statusTransitionList.contains(statusTransitionKey)){
            return true;
        }
        return false;
    }
}
