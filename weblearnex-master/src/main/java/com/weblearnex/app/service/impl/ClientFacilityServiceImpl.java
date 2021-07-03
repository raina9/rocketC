package com.weblearnex.app.service.impl;

import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.constant.UserType;
import com.weblearnex.app.datatable.reposatory.CourierRepository;
import com.weblearnex.app.datatable.reposatory.ServiceTypeRepository;
import com.weblearnex.app.entity.master.ServiceType;
import com.weblearnex.app.entity.setup.Client;
import com.weblearnex.app.entity.setup.ClientFacility;
import com.weblearnex.app.entity.setup.Courier;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.datatable.reposatory.ClientFacilityRepository;
import com.weblearnex.app.datatable.reposatory.ClientRepository;
import com.weblearnex.app.model.SessionUserBean;
import com.weblearnex.app.service.ClientFacilityService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ClientFacilityServiceImpl implements ClientFacilityService {

    Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ClientFacilityRepository clientFacilityRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private SessionUserBean sessionUserBean;


    @Override
    public ResponseBean<ClientFacility> addClientFacility(ClientFacility clientFacility) {
        ResponseBean responseBean = new ResponseBean();
        if(clientFacility.getDeliveryType()==null ||clientFacility.getDeliveryAttempt()==0){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_DELIVERY_ATTEMPT_EXIST_MSG,null,null));
            return responseBean;
        }
        if(clientFacility.getRateCardTypeCode()==null || clientFacility.getRateCardTypeCode().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Rate Card Tyep is empty.");
            return responseBean;

        }
        ClientFacility clientFacilityDB = clientFacilityRepository.findByClientId(clientFacility.getClientId());
        if(clientFacilityDB != null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Client facility already present. Please use different client.");
            return responseBean;
        }
        Optional<Client> client=clientRepository.findById(clientFacility.getClientId());
        clientFacility.setClient(client.get());
        if(clientFacilityRepository.findByClientId(clientFacility.getClientId()) != null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Client already exist.");
            return responseBean;
        }
        clientFacility=clientFacilityRepository.save(clientFacility);
        if(clientFacility.getId()!=null){
            responseBean.setResponseBody(clientFacility);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_FACILITY_ADDED_MSG,null,null));
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_FACILITY_ADDED_ERROR_MSG,null,null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<ClientFacility> updateClientFacility(ClientFacility clientFacility) {
        ResponseBean responseBean = new ResponseBean();

        Optional<ClientFacility> clientFacilityDB = clientFacilityRepository.findById(clientFacility.getId());
        if(clientFacility.getId()==null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Client id is empty.");
            return responseBean;
        }
        if(clientFacility.getRateCardTypeCode()==null || clientFacility.getRateCardTypeCode().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Rate Card Tyep is empty.");
            return responseBean;

        }
        if(clientFacilityDB.get().getClientId() != null && !clientFacilityDB.get().getClientId().equals(clientFacility.getClientId())){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Client id changed, Client ID can not be updated.");
            return responseBean;
        }
        Optional<Client> client=clientRepository.findById(clientFacility.getClientId());
        clientFacility.setClient(client.get());
        clientFacilityRepository.save(clientFacility);
        responseBean.setResponseBody(clientFacility);
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_FACILITY_UPDATED_MSG,null,null));
        return responseBean;
    }

    @Override
    public ResponseBean<ClientFacility> deleteClientFacility(Long id) {
        ResponseBean responseBean = new ResponseBean();
        Optional<ClientFacility> clientFacility = clientFacilityRepository.findById(id);
        if (!clientFacility.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_FACILITY_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        clientFacility.get().setActive(AppProperty.IN_ACTIVE);
        clientFacilityRepository.save(clientFacility.get());
        responseBean.setResponseBody(clientFacility.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_FACILITY_DELETE_MSG, null, null));
        return responseBean;
    }

    @Override
    public ResponseBean<List<ClientFacility>> getAllClientFacility() {
        ResponseBean responseBean = new ResponseBean();
        List<ClientFacility> clientFacilityList = (List<ClientFacility>) clientFacilityRepository.findAll();

        if(clientFacilityList != null && !clientFacilityList.isEmpty()){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG,null,null));
            responseBean.setResponseBody(clientFacilityList);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG,null,null));
        }
        return responseBean;

    }

    @Override
    public ResponseBean<ClientFacility> findByClientFacilityId(Long id) {
        ResponseBean responseBean = new ResponseBean();
        Optional<ClientFacility> clientFacility=clientFacilityRepository.findById(id);
        if(!clientFacility.isPresent()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_FACILITY_ID_NOT_FOUND,null,null));
            return responseBean;
        }
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG,null,null));
        responseBean.setResponseBody(clientFacility.get());
        return responseBean;
    }

    @Override
    public DataTablesOutput<ClientFacility> getAllClientFacilityPaginationAndSort(DataTablesInput input) {
        if(sessionUserBean.getUser().getType().equals(UserType.CLIENT)){
            input.addColumn("clientId", true, false,String.valueOf(sessionUserBean.getClientId()) );
        }
        return clientFacilityRepository.findAll(input);
    }

    @Override
    public ResponseBean<List<ServiceType>> getClientServiceType(Long clientId) {
        ResponseBean<List<ServiceType>> responseBean = new ResponseBean<List<ServiceType>>();
        try {
            ClientFacility clientFacility = clientFacilityRepository.findByClientId(clientId);
            if(clientFacility == null){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Client facility details not found.");
                return responseBean;
            }
            List<ServiceType> result = new ArrayList<ServiceType>();
            if(clientFacility.getServiceCourierMap() != null){
                for(String serviceTypeCode : clientFacility.getServiceCourierMap().keySet()){
                    ServiceType serviceType = serviceTypeRepository.findByServiceCode(serviceTypeCode);
                    if(serviceType != null){
                        result.add(serviceType);
                    }
                }
            }
            if(result.isEmpty()){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("No service selected in client account.");
                return responseBean;
            }
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setResponseBody(result);
        }catch(Exception e){
            e.printStackTrace();
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Server internal error.");
        }
        return responseBean;
    }

    @Override
    public ResponseBean<Map<String, String>> getClientAllowedCourier(Long clientId, Long serviceId) {
        ResponseBean<Map<String, String>> responseBean = new ResponseBean<Map<String, String>>();
        ClientFacility clientFacility = clientFacilityRepository.findByClientId(clientId);
        if(clientFacility == null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Client facility details not found.");
            return responseBean;
        }
        Optional<ServiceType> serviceType = serviceTypeRepository.findById(serviceId);
        Map<String,String> resultMap = new HashMap<String, String>();
        if(clientFacility.getServiceCourierMap() != null && clientFacility.getServiceCourierMap().get(serviceType.get().getServiceCode()) != null){
            for(String courierCode : clientFacility.getServiceCourierMap().get(serviceType.get().getServiceCode()).split(",")){
                Courier courier = courierRepository.findByCourierCode(courierCode);
                if(courier != null){
                    resultMap.put(courier.getCourierCode(), courier.getCourierName());
                }
            }
        }
        if(resultMap.isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("No service selected in client account.");
            return responseBean;
        }
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setResponseBody(resultMap);
        return responseBean;
    }
}
