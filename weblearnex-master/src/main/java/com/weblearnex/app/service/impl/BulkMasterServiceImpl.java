package com.weblearnex.app.service.impl;

import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.config.ProgressBean;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.entity.master.BulkMaster;
import com.weblearnex.app.model.ResponseBean;

import com.weblearnex.app.datatable.reposatory.BulkMasterRepository;
import com.weblearnex.app.model.SessionUserBean;
import com.weblearnex.app.service.BulkMasterService;
import com.weblearnex.app.service.BulkUploadService;
import com.weblearnex.app.service.RedisService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BulkMasterServiceImpl implements BulkMasterService {

    Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BulkMasterRepository bulkMasterRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private RedisService redisService;
    @Autowired
    private SessionUserBean sessionUserBean;

    @Override
    public ResponseBean<BulkMaster> addBulkMaster(BulkMaster bulkMaster) {
        ResponseBean responseBean = new ResponseBean();
        if (bulkMaster.getName() == null || bulkMaster.getName().isEmpty() || bulkMasterRepository.findByName(bulkMaster.getName()) != null) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.BULKMASTER_ALREADY_EXIST_MSG, null, null));
            return responseBean;
        }
        BulkMaster bulkMaster1= bulkMasterRepository.save(bulkMaster);
        if (bulkMaster.getId() != null) {
            responseBean.setResponseBody(bulkMaster);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.BULKMASTER_ADDED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.BULKMASTER_ADDED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<BulkMaster> updateBulkMaster(BulkMaster bulkMaster) {

        ResponseBean responseBean = new ResponseBean();

        if (bulkMaster.getId() == null || !bulkMasterRepository.findById(bulkMaster.getId()).isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.BULKMASTER_ID_DOES_NOT_EMPTY_OR_NO_DATA_FOUND_IN_DATABASE, null, null));
            return responseBean;
        }

        bulkMaster = bulkMasterRepository.save(bulkMaster);
        if (bulkMaster != null) {
            responseBean.setResponseBody(bulkMaster);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.BULKMASTER_UPDATED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.BULKMASTER_UPDATED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<BulkMaster> deleteBulkMaster(Long bulkMasterId) {

        ResponseBean responseBean = new ResponseBean();
        Optional<BulkMaster> bulkMaster = bulkMasterRepository.findById(bulkMasterId);
        if (!bulkMaster.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.BULKMASTER_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        //BulkMaster findBulkMaster = bulkMaster.get();
        bulkMaster.get().setActive(AppProperty.IN_ACTIVE);
        bulkMasterRepository.save(bulkMaster.get());
        responseBean.setResponseBody(bulkMaster.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.BULKMASTER_DELETE_MSG, null, null));
        return responseBean;
    }

    @Override
    public ResponseBean<BulkMaster> getAllBulkMaster() {
        ResponseBean responseBean = new ResponseBean();
        List<BulkMaster> bulkMasterList = (List<BulkMaster>)bulkMasterRepository.findAll();
        if(bulkMasterList !=null && !bulkMasterList.isEmpty()){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG, null, null));
            responseBean.setResponseBody(bulkMasterList);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<BulkMaster> findByBulkMasterId(Long id) {
        ResponseBean responseBean = new ResponseBean();
        Optional<BulkMaster> bulkMaster = bulkMasterRepository.findById(id);
        if (!bulkMaster.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.BULKMASTER_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        responseBean.setResponseBody(bulkMaster.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.BULKMASTER_ID_FOUND, null, null));
        return responseBean;
    }

    @Override
    public DataTablesOutput<BulkMaster> getAllBulkMasterPaginationAndSort(DataTablesInput input) {
        return bulkMasterRepository.findAll(input);
    }

    @Override
    public ResponseBean setUploadProgressCount(String bulkUpload, Integer progressCount, String token, boolean uploadCompleted) {
        ResponseBean responseBean = new ResponseBean();
        try {
            String key = sessionUserBean.getUser().getLoginId()+"_"+bulkUpload;
            ProgressBean progressBean = new ProgressBean();
            progressBean.setProgressPercentage(progressCount);
            progressBean.setUploadKey(key);
            progressBean.setToken(token);
            progressBean.setUploadCompleted(uploadCompleted);
            BulkUploadService.setUploadProgressCount(redisService,key,progressBean);

            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setResponseBody(progressBean);
        }catch (Exception e){
            e.printStackTrace();
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(e.getMessage());
        }
        return responseBean;
    }

    @Override
    public ResponseBean getUploadProgressCount(String bulkUpload, String token) {
        String key = sessionUserBean.getUser().getLoginId()+"_"+bulkUpload;
        ResponseBean responseBean = BulkUploadService.getUploadProgressCount(redisService,key,bulkUpload);
        try{
            if(ResponseStatus.SUCCESS.equals(responseBean.getStatus())){
                ProgressBean progressBean = (ProgressBean) responseBean.getResponseBody();
                if((token == null || "".equals(token.trim())) && progressBean.getUploadCompleted() != null && progressBean.getUploadCompleted()){
                    progressBean.setProgressPercentage(0);
                    progressBean.setUploadCompleted(false);
                    return responseBean;
                }
                if(token != null && !"".equals(token.trim()) && !token.equals(progressBean.getToken())){
                    progressBean.setProgressPercentage(0);
                    progressBean.setUploadCompleted(false);
                    return responseBean;
                }
                if(progressBean.getUploadCompleted()){
                    deleteProgressKey(key);
                }
            }
        }catch (Exception e){}
        return responseBean;
    }

    @Override
    public ResponseBean deleteProgressKey(String bulkUpload) {
        ResponseBean responseBean = new ResponseBean();
        try {
            String key = bulkUpload+"_"+sessionUserBean.getUser().getLoginId();
            redisService.delete(key);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setResponseBody(true);
        }catch (Exception e){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Server internal error.");
            e.printStackTrace();
        }
        return responseBean;
    }
}
