package com.weblearnex.app.service.impl;


import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.BulkHeaderConstant;
import com.weblearnex.app.constant.ProjectConstant;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.datatable.reposatory.ConfigrationRepository;
import com.weblearnex.app.datatable.reposatory.StatusRepository;
import com.weblearnex.app.entity.master.*;
import com.weblearnex.app.entity.order.SaleOrder;
import com.weblearnex.app.entity.setup.Configration;
import com.weblearnex.app.entity.setup.Courier;
import com.weblearnex.app.entity.setup.Status;
import com.weblearnex.app.model.BulkUploadBean;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.datatable.reposatory.CourierStatusMappingRepository;
import com.weblearnex.app.datatable.reposatory.CourierRepository;
import com.weblearnex.app.service.BulkMasterService;
import com.weblearnex.app.service.BulkUploadService;
import com.weblearnex.app.service.CourierStatusMappingService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CourierStatusMappingServiceImpl implements CourierStatusMappingService {


    Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CourierStatusMappingRepository courierStatusMappingRepository;

    @Autowired
    private CourierRepository vendorRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private BulkMasterService bulkMasterService;

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private ConfigrationRepository configrationRepository;

    @Override
    public ResponseBean<CourierStatusMapping> addCourierStatusMapping(CourierStatusMapping courierStatusMapping) {
        ResponseBean responseBean = new ResponseBean();
        if (courierStatusMapping.getStatusMappings() == null || courierStatusMapping.getStatusMappings().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_STATUS_MAPPING_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (courierStatusMapping.getCourierId() == null || courierStatusMappingRepository.findByCourierId(courierStatusMapping.getCourierId())!=null) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_ID_ALREADY_EXIST, null, null));
            return responseBean;
       }
        /*if (courierStatusMapping.getToken() == null || courierStatusMapping.getToken().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.TOKEN_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }*/


        courierStatusMapping = courierStatusMappingRepository.save(courierStatusMapping);
        if (courierStatusMapping.getId() != null) {
            responseBean.setResponseBody(courierStatusMapping);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_STATUS_MAPPING_ADDED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_STATUS_MAPPING_DOES_NOT_FOUND, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<CourierStatusMapping> updateCourierStatusMapping(CourierStatusMapping courierStatusMapping) {
        ResponseBean responseBean = new ResponseBean();
        if (courierStatusMapping.getStatusMappings() == null || courierStatusMapping.getStatusMappings().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_STATUS_MAPPING_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (courierStatusMapping.getCourierId() == null ) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_ID_ALREADY_EXIST, null, null));
            return responseBean;
       }
        /*if (courierStatusMapping.getToken() == null || courierStatusMapping.getToken().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.TOKEN_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }*/
        courierStatusMapping = courierStatusMappingRepository.save(courierStatusMapping);
        if (courierStatusMapping != null) {
            responseBean.setResponseBody(courierStatusMapping);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_STATUS_MAPPING_UPDATED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_STATUS_MAPPING_UPDATED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<CourierStatusMapping> deleteCourierStatusMapping(Long CourierStatusMappingId) {
        ResponseBean responseBean = new ResponseBean();
        Optional<CourierStatusMapping> courierStatusMapping = courierStatusMappingRepository.findById(CourierStatusMappingId);
        if (!courierStatusMapping.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_STATUS_MAPPING_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        //CourierStatusMapping findCourierStatusMapping = courierStatusMapping.get();
        courierStatusMappingRepository.delete(courierStatusMapping.get());
        responseBean.setResponseBody(courierStatusMapping.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_STATUS_MAPPING_DELETE_MSG, null, null));
        return responseBean;
    }

    @Override
    public ResponseBean<CourierStatusMapping> getAllCourierStatusMapping() {
        ResponseBean responseBean = new ResponseBean();
        List<CourierStatusMapping> courierStatusMappingList = (List<CourierStatusMapping>) courierStatusMappingRepository.findAll();
        if(courierStatusMappingList !=null && !courierStatusMappingList.isEmpty()){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG, null, null));
            responseBean.setResponseBody(courierStatusMappingList);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<CourierStatusMapping> findByCourierStatusMappingId(Long id) {
        ResponseBean responseBean = new ResponseBean();
        Optional<CourierStatusMapping> courierStatusMapping = courierStatusMappingRepository.findById(id);
        if (!courierStatusMapping.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_STATUS_MAPPING_ID_NOT_FOUND, null, null));
        return responseBean;
    }
        responseBean.setResponseBody(courierStatusMapping.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_STATUS_MAPPING_ID_FOUND, null, null));
        return responseBean;
    }

    @Override
    public DataTablesOutput<CourierStatusMapping> getAllCourierStatusMappingPaginationAndSort(DataTablesInput input) {
        return courierStatusMappingRepository.findAll(input);
    }


    @Override
    public BulkUploadBean uploadBulk(BulkUploadBean bulkUploadBean, BulkMaster bulkMaster) {
        //BulkUploadBean bulkUploadResponseBean = new BulkUploadBean();
        List<Map<String, String>> errorRecord = new ArrayList<Map<String, String>>();
        List<Map<String, String>> successRecord = new ArrayList<Map<String, String>>();
        List<StatusMapping> statusMappingList = new ArrayList<>();
        int count = 0;
        int percentage = 0;
        String token = BulkUploadService.generateRandomString();
        for (Map<String, String> map : bulkUploadBean.getRecords()) {
            count ++;
            percentage = BulkUploadService.calculateUploadPercentage(count, bulkUploadBean.getRecords().size());
            bulkMasterService.setUploadProgressCount(bulkMaster.getName(), percentage, token, false);

            CourierStatusMapping courierStatusMapping = new CourierStatusMapping();
            String courierCode= map.get(BulkHeaderConstant.COURIER_CODE);
            String selfStatusCode= map.get(BulkHeaderConstant.SELF_STATUS_CODE);
            String courierStatusCode= map.get(BulkHeaderConstant.COURIER_STATUS_CODE);
            String ndrCode= map.get(BulkHeaderConstant.NDR_CODE);
            String extra= map.get(BulkHeaderConstant.EXTRA);

            if(courierCode == null || courierCode.trim().equals("")){
                map.put(BulkHeaderConstant.MESSAGE,"Courier code is empty.");
                errorRecord.add(map);
                continue;
            }
            if(!courierRepository.existsByCourierCode(courierCode)){
                map.put(BulkHeaderConstant.MESSAGE,"Courier code not found in database.");
                errorRecord.add(map);
                continue;
            }
            if(selfStatusCode == null || selfStatusCode.trim().equals("")){
                map.put(BulkHeaderConstant.MESSAGE,"Self status code is empty.");
                errorRecord.add(map);
                continue;
            }
            if(courierStatusCode == null || courierStatusCode.trim().equals("")){
                map.put(BulkHeaderConstant.MESSAGE,"Courier status code is empty.");
                errorRecord.add(map);
                continue;
            }

            if(selfStatusCode != null && !selfStatusCode.trim().equals("")){
                Status selfStatus = statusRepository.findByStatusCode(selfStatusCode);
                if(selfStatus == null){
                    map.put(BulkHeaderConstant.MESSAGE,"Self status code not found in database.");
                    errorRecord.add(map);
                    continue;
                }
            }

            if(courierStatusCode != null && !courierStatusCode.trim().equals("")){
                Status courierStatus = statusRepository.findByStatusCode(courierStatusCode);
                if(courierStatus == null){
                    map.put(BulkHeaderConstant.MESSAGE,"Courier status code not found in database.");
                    errorRecord.add(map);
                    continue;
                }
            }

            if(ndrCode == null || ndrCode.trim().equals("") || extra == null || extra.trim().equals("")){
                map.put(BulkHeaderConstant.MESSAGE,"NDR code is empty.");
                errorRecord.add(map);
                continue;
            }

            if(ndrCode != null && !ndrCode.trim().equals("")){
                Configration configration = configrationRepository.findByConfigCode(ndrCode);
                if(configration == null){
                    map.put(BulkHeaderConstant.MESSAGE,"Ndr code not found in database.");
                    errorRecord.add(map);
                    continue;
                }
            }
            if(extra != null && !extra.trim().equals("")){
                Configration configration = configrationRepository.findByConfigCode(extra);
                if(configration == null){
                    map.put(BulkHeaderConstant.MESSAGE,"Ndr code not found in database.");
                    errorRecord.add(map);
                    continue;
                }
            }

            StatusMapping statusMapping = new  StatusMapping();
            statusMapping.setSelfStatusCode(selfStatusCode);
            statusMapping.setCourierStatusCode(courierStatusCode);
            statusMapping.setNdrCode(ndrCode);
            statusMapping.setExtra(extra);
            statusMappingList.add(statusMapping);


            Courier courier = courierRepository.findByCourierCode(courierCode);
            if(courierStatusMappingRepository.findByCourierId(courier.getId())!=null){
              CourierStatusMapping courierStatusMappingUpdate = courierStatusMappingRepository.findByCourierId(courier.getId());
                courierStatusMappingUpdate.setStatusMappings(statusMappingList);
                courierStatusMappingRepository.save(courierStatusMappingUpdate);
            }else {

                courierStatusMapping.setCourierId(courier.getId());
                courierStatusMapping.setStatusMappings(statusMappingList);
                courierStatusMappingRepository.save(courierStatusMapping);
            }


            successRecord.add(map);
        }
        bulkUploadBean.setBulkMaster(bulkMaster);
        bulkUploadBean.setErrorRecords(errorRecord);
        bulkUploadBean.setSuccessRecords(successRecord);
        // Set upload count
        bulkMasterService.setUploadProgressCount(bulkMaster.getName(), 100, token, true);
        return bulkUploadBean;
    }
}
