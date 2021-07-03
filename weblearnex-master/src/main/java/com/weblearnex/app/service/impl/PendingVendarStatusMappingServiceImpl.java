package com.weblearnex.app.service.impl;

import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.datatable.reposatory.DataPushErrorLogRepository;
import com.weblearnex.app.datatable.reposatory.PendingVendarStatusMappingRepository;
import com.weblearnex.app.entity.logs.PendingVendarStatusMapping;
import com.weblearnex.app.entity.master.CourierStatusMapping;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.PendingVendarStatusMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PendingVendarStatusMappingServiceImpl implements PendingVendarStatusMappingService {

    @Autowired
    private PendingVendarStatusMappingRepository pendingVendarStatusMappingRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    public ResponseBean<PendingVendarStatusMapping> deletePendingVendarStatusMapping(Long PendingVendarStatusMappingId) {
        ResponseBean responseBean = new ResponseBean();
        Optional<PendingVendarStatusMapping> pendingVendarStatusMapping = pendingVendarStatusMappingRepository.findById(PendingVendarStatusMappingId);
        if (!pendingVendarStatusMapping.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.PENDING_VENDAR_STATUS_MAPPING_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        //CourierStatusMapping findCourierStatusMapping = courierStatusMapping.get();
        pendingVendarStatusMappingRepository.delete(pendingVendarStatusMapping.get());
        responseBean.setResponseBody(pendingVendarStatusMapping.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.PENDING_VENDAR_STATUS_MAPPING_DELETE_MSG, null, null));
        return responseBean;
    }

    @Override
    public DataTablesOutput<PendingVendarStatusMapping> getAllPendingVendarStatusMappingPaginationAndSort(DataTablesInput input) {
        return pendingVendarStatusMappingRepository.findAll(input);
    }
}
