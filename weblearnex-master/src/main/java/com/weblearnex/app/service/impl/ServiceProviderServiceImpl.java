package com.weblearnex.app.service.impl;

import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.entity.master.ServiceProvider;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.reposatory.StateRepository;
import com.weblearnex.app.datatable.reposatory.ServiceProviderRepository;
import com.weblearnex.app.service.ServiceProviderService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceProviderServiceImpl implements ServiceProviderService {

    Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    @Autowired
    private StateRepository sateRepository;

    @Autowired
    private MessageSource messageSource;


    @Override
    public ResponseBean<ServiceProvider> addServiceProvider(ServiceProvider serviceProvider) {
        ResponseBean responseBean = new ResponseBean();
        if (serviceProvider.getServiceProviderName() == null || serviceProvider.getServiceProviderName().isEmpty()|| serviceProviderRepository.findByServiceProviderName(serviceProvider.getServiceProviderName())!=null ) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.SERVICE_PROVIDER_NAME_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (serviceProvider.getServiceProviderCode() == null || serviceProvider.getServiceProviderCode().isEmpty() || serviceProviderRepository.findByServiceProviderCode(serviceProvider.getServiceProviderCode()) !=null) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.SERVICE_PROVIDER_CODE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        serviceProvider = serviceProviderRepository.save(serviceProvider);
        if(serviceProvider.getId() != null) {
            responseBean.setResponseBody(serviceProvider);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.SERVICE_PROVIDER_ADDED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.SERVICE_PROVIDER_ADDED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<ServiceProvider> updateServiceProvider(ServiceProvider serviceProvider) {
        ResponseBean responseBean = new ResponseBean();
        if (serviceProvider.getId() == null ) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.SERVICE_PROVIDER_ID_DOSE_NOT_NULL, null, null));
            return responseBean;
        }
        serviceProvider = serviceProviderRepository.save(serviceProvider);
        if (serviceProvider != null) {
            responseBean.setResponseBody(serviceProvider);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.SERVICE_PROVIDER_UPDATED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.SERVICE_PROVIDER_UPDATED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<ServiceProvider> deleteServiceProvider(Long serviceProviderId) {
        ResponseBean responseBean = new ResponseBean();
        Optional<ServiceProvider> serviceProvider = serviceProviderRepository.findById(serviceProviderId);
        if (!serviceProvider.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.SERVICE_PROVIDER_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        //Configration findConfigration = configration.get();
        serviceProvider.get().setActive(AppProperty.IN_ACTIVE);
        serviceProviderRepository.save(serviceProvider.get());
        responseBean.setResponseBody(serviceProvider.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.SERVICE_PROVIDER_DELETE_MSG, null, null));
        return responseBean;
    }

    @Override
    public ResponseBean<ServiceProvider> getAllServiceProvider(){
        ResponseBean responseBean = new ResponseBean();
        List<ServiceProvider> serviceProviderList = (List<ServiceProvider>) serviceProviderRepository.findAll();
        if(serviceProviderList !=null && !serviceProviderList.isEmpty()){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG, null, null));
            responseBean.setResponseBody(serviceProviderList);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<ServiceProvider> findByServiceProviderID(Long id) {
        ResponseBean responseBean = new ResponseBean();
        Optional<ServiceProvider> serviceProvider = serviceProviderRepository.findById(id);
        if (!serviceProvider.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.SERVICE_PROVIDER_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        responseBean.setResponseBody(serviceProvider.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.SERVICE_PROVIDER_ID_FOUND, null, null));
        return responseBean;
    }

    @Override
    public DataTablesOutput<ServiceProvider> getAllServiceProviderPaginationAndSort(DataTablesInput input) {
        return serviceProviderRepository.findAll(input);
    }
}
