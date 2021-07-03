package com.weblearnex.app.service.impl;

import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.constant.UserType;
import com.weblearnex.app.datatable.reposatory.ClientRepository;
import com.weblearnex.app.datatable.reposatory.ServiceTypeRepository;
import com.weblearnex.app.entity.master.CourierPriority;
import com.weblearnex.app.entity.master.ServiceType;
import com.weblearnex.app.entity.setup.Client;
import com.weblearnex.app.entity.setup.User;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.datatable.reposatory.CourierPriorityRepository;
import com.weblearnex.app.datatable.reposatory.CourierRepository;
import com.weblearnex.app.model.SessionUserBean;
import com.weblearnex.app.service.CourierPriorityService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourierPriorityServiceImpl implements CourierPriorityService {

    Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CourierPriorityRepository courierPriorityRepository;

    @Autowired
    private CourierRepository vendorRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private SessionUserBean sessionUserBean;


    @Override
    public ResponseBean<CourierPriority> addCourierPriority(CourierPriority courierPriority) {
        ResponseBean responseBean = new ResponseBean();

        if (courierPriority.getClientId() == null ) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Client ID field is empty");
            return responseBean;
        }
        if(courierPriority.getServiceTypeId()== null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Service type field is empty.");
            return responseBean;
        }
        Optional<Client> client = clientRepository.findById(courierPriority.getClientId());
        Optional<ServiceType> serviceType = serviceTypeRepository.findById(courierPriority.getServiceTypeId());
        courierPriority.setCourierPriorityCode(client.get().getClientCode()+"_"+serviceType.get().getServiceCode());
        if(courierPriorityRepository.findByCourierPriorityCode(courierPriority.getCourierPriorityCode()) != null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_PRIORITY_ALREADY_EXIST, null, null));
            return responseBean;

        }
        if (courierPriority.getCourierPriorityName() == null || courierPriority.getCourierPriorityName().isEmpty() || courierPriorityRepository.findByCourierPriorityName(courierPriority.getCourierPriorityName()) != null) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_PRIORITY_NAME_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (courierPriority.getPrioritys() == null || courierPriority.getPrioritys().isEmpty() || courierPriorityRepository.findByPrioritys(courierPriority.getPrioritys()) != null) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_PRIORITYS_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        courierPriority = courierPriorityRepository.save(courierPriority);
        if (courierPriority.getId() != null) {
            responseBean.setResponseBody(courierPriority);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_PRIORITY_ADDED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.PRIORITY_DOES_NOT_FOUND, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<CourierPriority> updateCourierPriority(CourierPriority courierPriority) {
        ResponseBean responseBean = new ResponseBean();
        if (courierPriority.getClientId() == null ) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Client ID field is empty");
            return responseBean;
        }
        if(courierPriority.getServiceTypeId()== null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Service type field is empty.");
            return responseBean;
        }

        Optional<Client> client = clientRepository.findById(courierPriority.getClientId());
        Optional<ServiceType> serviceType = serviceTypeRepository.findById(courierPriority.getServiceTypeId());
        courierPriority.setCourierPriorityCode(client.get().getClientCode()+"_"+serviceType.get().getServiceCode());

        if (courierPriority.getCourierPriorityName() == null || courierPriority.getCourierPriorityName().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_PRIORITY_NAME_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (courierPriority.getPrioritys() == null || courierPriority.getPrioritys().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_PRIORITYS_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }

        courierPriority = courierPriorityRepository.save(courierPriority);
        if (courierPriority != null) {
            responseBean.setResponseBody(courierPriority);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_PRIORITY_UPDATED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_PRIORITY_UPDATED_ERROR_MSG, null, null));
        }
        return responseBean;
    }


    @Override
    public ResponseBean<CourierPriority> deleteCourierPriority(Long courierPriorityId) {
        ResponseBean responseBean = new ResponseBean();
        Optional<CourierPriority> courierPriority = courierPriorityRepository.findById(courierPriorityId);
        if (!courierPriority.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_PRIORITY_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        //CerviceType findServiceType = serviceType.get();
        courierPriorityRepository.delete(courierPriority.get());
        responseBean.setResponseBody(courierPriority.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_PRIORITY_DELETE_MSG, null, null));
        return responseBean;
    }

    @Override
    public ResponseBean<CourierPriority> getAllCourierPriority() {
        ResponseBean responseBean = new ResponseBean();
        List<CourierPriority> courierPriorityList = null;
        User user = sessionUserBean.getUser();
        if(UserType.CLIENT.equals(user.getType())){
            courierPriorityList = courierPriorityRepository.findAllByClientId(sessionUserBean.getClientId());
        }else {
            courierPriorityList = (List<CourierPriority>) courierPriorityRepository.findAll();
        }
        if( courierPriorityList !=null && !courierPriorityList.isEmpty()){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG, null, null));
            responseBean.setResponseBody(courierPriorityList);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<CourierPriority> findByCourierPriorityId(Long id) {
        ResponseBean responseBean = new ResponseBean();
        Optional<CourierPriority> courierPriority = courierPriorityRepository.findById(id);
        if (!courierPriority.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_PRIORITY_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        responseBean.setResponseBody(courierPriority.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_PRIORITY_ID_FOUND, null, null));
        return responseBean;
    }

    @Override
    public DataTablesOutput<CourierPriority> getAllCourierPriorityPaginationAndSort(DataTablesInput input) {
        User user = sessionUserBean.getUser();
        if(UserType.CLIENT.equals(user.getType())){
            input.addColumn("clientId", true, false, sessionUserBean.getClientId().toString());
        }
        return courierPriorityRepository.findAll(input);
    }
}
