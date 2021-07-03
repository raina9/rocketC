package com.weblearnex.app.service.impl;

import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.constant.UserType;
import com.weblearnex.app.datatable.reposatory.ClientFacilityRepository;
import com.weblearnex.app.datatable.reposatory.CourierRepository;
import com.weblearnex.app.entity.master.ServiceType;
import com.weblearnex.app.entity.setup.ClientFacility;
import com.weblearnex.app.entity.setup.Courier;
import com.weblearnex.app.entity.setup.User;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.datatable.reposatory.ServiceTypeRepository;
import com.weblearnex.app.model.SessionUserBean;
import com.weblearnex.app.service.ServiceTypeService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ServiceTypeServiceImpl implements ServiceTypeService {


    Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ClientFacilityRepository clientFacilityRepository;

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private SessionUserBean sessionUserBean;

    @Override
    public ResponseBean<ServiceType> addServiceType(ServiceType serviceType) {
        ResponseBean responseBean = new ResponseBean();
        if (serviceType.getServiceCode() == null || serviceType.getServiceCode().isEmpty() || serviceTypeRepository.findByServiceCode(serviceType.getServiceCode()) != null) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.SERVICE_CODE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (serviceType.getServiceName() == null || serviceType.getServiceName().isEmpty()|| serviceTypeRepository.findByServiceName(serviceType.getServiceName()) != null) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.SERVICE_NAME_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
            serviceType = serviceTypeRepository.save(serviceType);
        if (serviceType.getId() != null) {
            responseBean.setResponseBody(serviceType);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.SERVICE_ADDED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.SERVICE_ADDED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<ServiceType> updateServiceType(ServiceType serviceType) {
        ResponseBean responseBean = new ResponseBean();
        if (serviceType.getId() == null ) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.SERVICE_ID_DOSE_NOT_NULL, null, null));
            return responseBean;
        }
        serviceType = serviceTypeRepository.save(serviceType);
        if (serviceType != null) {
            responseBean.setResponseBody(serviceType);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.SERVICE_UPDATED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.SERVICE_UPDATED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<ServiceType> deleteServiceType(Long serviceTypeId) {
        ResponseBean responseBean = new ResponseBean();
        Optional<ServiceType> serviceType = serviceTypeRepository.findById(serviceTypeId);
        if (!serviceType.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.SERVICE_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        //CerviceType findServiceType = serviceType.get();
        serviceType.get().setActive(AppProperty.IN_ACTIVE);
        serviceTypeRepository.save(serviceType.get());
        responseBean.setResponseBody(serviceType.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.SERVICE_DELETE_MSG, null, null));
        return responseBean;
    }

    @Override
    public ResponseBean<ServiceType> getAllServiceType() {
        ResponseBean responseBean = new ResponseBean();
        List<ServiceType> serviceTypeList = (List<ServiceType>) serviceTypeRepository.findAll();
        if(serviceTypeList !=null && !serviceTypeList.isEmpty()){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG, null, null));
            responseBean.setResponseBody(serviceTypeList);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<ServiceType> findByServiceTypeId(Long id) {
        ResponseBean responseBean = new ResponseBean();
        Optional<ServiceType> serviceType = serviceTypeRepository.findById(id);
        if (!serviceType.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.SERVICE_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        responseBean.setResponseBody(serviceType.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.SERVICE_ID_FOUND, null, null));
        return responseBean;
    }

    @Override
    public DataTablesOutput<ServiceType> getAllServiceTypePaginationAndSort(DataTablesInput input) {
        return serviceTypeRepository.findAll(input);
    }

    @Override
    public ResponseBean<ServiceType> getClientServiceCourier() {
        ResponseBean responseBean = new ResponseBean();
        try{
            Map<String,String> serviceCourierMap = new HashMap<String,String>();
            User user = sessionUserBean.getUser();
            if(UserType.CLIENT.equals(user.getType())){
                ClientFacility clientFacility = clientFacilityRepository.findByClientId(sessionUserBean.getClientId());
                serviceCourierMap = clientFacility.getServiceCourierMap();
            }else {
                Iterable<ServiceType> allCouriers = serviceTypeRepository.findAll();
                Iterator<ServiceType> iterator = allCouriers.iterator();
                while (iterator.hasNext()){
                    ServiceType serviceType = iterator.next();
                    List<Courier> courierList = courierRepository.findAllByServiceTypeIdAndActive(serviceType.getId(), AppProperty.ACTIVE);
                    if(courierList != null && !courierList.isEmpty()){
                        String s = courierList.stream().map(courier -> courier.getCourierCode()).collect(Collectors.joining(","));
                        serviceCourierMap.put(serviceType.getServiceCode(), s);
                    }
                }
            }

            if(serviceCourierMap == null || serviceCourierMap.isEmpty()){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Service, Courier mapping not found in client account.");
                return responseBean;
            }
            Map<ServiceType, List<Courier>> resultMap = new HashMap<ServiceType, List<Courier>>(5);
            for(Map.Entry<String,String> entry : serviceCourierMap.entrySet()){
                resultMap.put(serviceTypeRepository.findByServiceCode(entry.getKey()),
                        courierRepository.findByCourierCodeInAndActive(Arrays.asList(entry.getValue().split(",")),1));
            }
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setResponseBody(resultMap);
        }catch (Exception e){
            e.printStackTrace();
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(e.getMessage());
        }
        return responseBean;
    }

}
