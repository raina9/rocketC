package com.weblearnex.app.service.impl;

import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.ApiConfigType;
import com.weblearnex.app.constant.EntityType;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.entity.master.ApiConfig;
import com.weblearnex.app.entity.setup.Client;
import com.weblearnex.app.entity.setup.Courier;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.datatable.reposatory.ApiConfigRepository;
import com.weblearnex.app.datatable.reposatory.ClientRepository;
import com.weblearnex.app.reposatory.StateRepository;
import com.weblearnex.app.datatable.reposatory.CourierRepository;
import com.weblearnex.app.service.ApiConfigService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApiConfigServiceImpl implements ApiConfigService {

    Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ApiConfigRepository apiConfigRepository;

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private StateRepository sateRepository;

    @Autowired
    private MessageSource messageSource;


    @Override
    public ResponseBean<ApiConfig> addApiConfig(ApiConfig apiConfig) {
        ResponseBean responseBean = new ResponseBean();
        if (apiConfig.getApiConfigType() == null ) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.APICONFIG_TYPE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (apiConfig.getApiUrl() == null || apiConfig.getApiUrl().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.APIURL_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (apiConfig.getEntityType() == null  ) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.ENTITY_TYPE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }


        if (apiConfig.getRequestMethod() == null ) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.REQUEST_METHOD_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }

        if(ApiConfigType.ORDER_PUSH.equals(apiConfig.getApiConfigType()) || ApiConfigType.STATUS_UPDATE.equals(apiConfig.getApiConfigType())){
            // apiConfig.setClientId(null);
            if(apiConfig.getCourierId() == null){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Courier must be selected for data push.");
                return responseBean;
            }

            if(apiConfig.getServiceProviderId() == null){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Service provide must be recurred for data push.");
                return responseBean;
            }

            ApiConfig apiConfigDB = apiConfigRepository.findByCourierIdAndApiConfigTypeAndEntityType(apiConfig.getCourierId(),apiConfig.getApiConfigType(), apiConfig.getEntityType());
            if(apiConfigDB != null){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Api config entry already present. (Courier)");
                return responseBean;
            }

            /*apiConfigDB = apiConfigRepository.findByServiceProviderId(apiConfig.getServiceProviderId());
            if(apiConfigDB != null){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Api config entry already present. (Service provider)");
                return responseBean;
            }*/

        } else if(ApiConfigType.STATUS_PUSH.equals(apiConfig.getApiConfigType())){
            // apiConfig.setCourierId(null);
            if(apiConfig.getClientId() == null || apiConfigRepository.findByClientId(apiConfig.getClientId()) == null){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Client must be selected for status push.");
                return responseBean;
            }
            ApiConfig apiConfigDB = apiConfigRepository.findByClientIdAndApiConfigTypeAndEntityType(apiConfig.getClientId(),apiConfig.getApiConfigType(), apiConfig.getEntityType());
            if(apiConfigDB != null){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Api config entry already present.");
                return responseBean;
            }
        }

        apiConfig = apiConfigRepository.save(apiConfig);
        if (apiConfig.getId() != null) {
            responseBean.setResponseBody(apiConfig);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.APICONFIG_ADDED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.APICONFIG_ADDED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<ApiConfig> updateApiConfig(ApiConfig apiConfig) {
        ResponseBean responseBean = new ResponseBean();
        if (apiConfig.getApiConfigType() == null ) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.APICONFIG_TYPE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (apiConfig.getApiUrl() == null || apiConfig.getApiUrl().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.APIURL_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (apiConfig.getEntityType() == null ) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.ENTITY_TYPE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (apiConfig.getRequestMethod() == null ) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.REQUEST_METHOD_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }

        Optional<ApiConfig> apiConfigDB = apiConfigRepository.findById(apiConfig.getId());
        if(apiConfigDB == null || !apiConfigDB.isPresent()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Api config obj not present in DB, So update not allowed.");
            return responseBean;
        }

        apiConfig = apiConfigRepository.save(apiConfig);
        if (apiConfig != null) {
            responseBean.setResponseBody(apiConfig);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.APICONFIG_UPDATED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.APICONFIG_UPDATED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<ApiConfig> deleteApiConfig(Long apiConfigId) {
        ResponseBean responseBean = new ResponseBean();
        Optional<ApiConfig> apiConfig = apiConfigRepository.findById(apiConfigId);
        if (!apiConfig.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.APICONFIG_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        //Configration findConfigration = configration.get();
        apiConfig.get().setActive(AppProperty.IN_ACTIVE);
        apiConfigRepository.save(apiConfig.get());
        responseBean.setResponseBody(apiConfig.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.APICONFIG_DELETE_MSG, null, null));
        return responseBean;
    }


    @Override
    public ResponseBean<ApiConfig> getAllApiConfig() {
        ResponseBean responseBean = new ResponseBean();
        List<ApiConfig> apiConfigList = (List<ApiConfig>) apiConfigRepository.findAll();
        if(apiConfigList !=null && !apiConfigList.isEmpty()){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG, null, null));
            responseBean.setResponseBody(apiConfigList);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<ApiConfig> findByApiConfigId(Long apiConfigId) {
        ResponseBean responseBean = new ResponseBean();
        Optional<ApiConfig> status = apiConfigRepository.findById(apiConfigId);
        if (!status.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.APICONFIG_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        responseBean.setResponseBody(status.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.APICONFIG_ID_FOUND, null, null));
        return responseBean;
    }

    @Override
    public DataTablesOutput<ApiConfig> getAllApiConfigPaginationAndSort(DataTablesInput input) {
        DataTablesOutput<ApiConfig> dataTablesOutput =  apiConfigRepository.findAll(input);
        if(dataTablesOutput != null && dataTablesOutput.getData() != null){
            for (ApiConfig apiConfig : dataTablesOutput.getData()){
                if(apiConfig.getClientId() != null){
                    Optional<Client> client = clientRepository.findById(apiConfig.getClientId());
                    apiConfig.setClientCode(client.isPresent() ? client.get().getClientCode() : null);
                }
                if(apiConfig.getCourierId() != null){
                    Optional<Courier> courier = courierRepository.findById(apiConfig.getCourierId());
                    apiConfig.setCourierCode(courier.isPresent() ? courier.get().getCourierCode() : null);
                }
            }
        }
        return dataTablesOutput;
    }
}
