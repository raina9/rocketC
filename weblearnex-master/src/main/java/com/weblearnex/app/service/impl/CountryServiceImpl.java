package com.weblearnex.app.service.impl;

import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.BulkHeaderConstant;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.entity.master.BulkMaster;
import com.weblearnex.app.entity.master.Country;
import com.weblearnex.app.entity.master.State;
import com.weblearnex.app.model.BulkUploadBean;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.reposatory.CountryRepository;
import com.weblearnex.app.service.BulkMasterService;
import com.weblearnex.app.service.BulkUploadService;
import com.weblearnex.app.service.CountryService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CountryServiceImpl implements CountryService {
    Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private BulkMasterService bulkMasterService;

    @Override
    public ResponseBean<Country> addCountry(Country country) {
        ResponseBean responseBean = new ResponseBean();
        if (country.getCode() == null || country.getCode().isEmpty() || countryRepository.findByCode(country.getCode()) != null) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COUNTRY_CODE_ALREADY_EXIST_MSG, null, null));
            return responseBean;
        }
        if (country.getCountryName() == null || country.getCountryName().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COUNTRY_NAME_ALREADY_EXIST_MSG, null, null));
            return responseBean;
        }

        country = countryRepository.save(country);
        if (country.getId() != null) {
            responseBean.setResponseBody(country);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COUNTRY_ADDED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COUNTRY_ADDED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<Country> updateCountry(Country country) {
        ResponseBean responseBean = new ResponseBean();
        if (country.getCode() == null || country.getCode().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COUNTRY_CODE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (country.getId() == null || !countryRepository.findById(country.getId()).isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COUNTRY_ID_DOES_NOT_EMPTY_OR_NO_DATA_FOUND_IN_DATABASE, null, null));
            return responseBean;
        }
        if (country.getCountryName() == null || country.getCountryName().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COUNTRY_NAME_ALREADY_EXIST_MSG, null, null));
            return responseBean;
        }

        country = countryRepository.save(country);
        if (country != null) {
            responseBean.setResponseBody(country);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COUNTRY_UPDATED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COUNTRY_UPDATED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<Country> deleteCountry(Long countryId) {
        ResponseBean responseBean = new ResponseBean();
        Optional<Country> country = countryRepository.findById(countryId);
        if (!country.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COUNTRY_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        //Country findCountry = country.get();
        country.get().setActive(AppProperty.IN_ACTIVE);
        countryRepository.save(country.get());
        responseBean.setResponseBody(country.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.COUNTRY_DELETE_MSG, null, null));
        return responseBean;
    }

    @Override
    public ResponseBean<List<Country>> getAllCountries() {

    ResponseBean responseBean = new ResponseBean();
    List<Country> countryList = countryRepository.findAll();
    if(countryList !=null && !countryList.isEmpty()){
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG, null, null));
        responseBean.setResponseBody(countryList);
    }else{
        responseBean.setStatus(ResponseStatus.FAIL);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG, null, null));
    }
        return responseBean;
    }

    @Override
    public ResponseBean<Country> findByCode(String code) {
        ResponseBean responseBean = new ResponseBean();
        Country country=countryRepository.findByCode(code);
        if(country==null||country.getCode().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COUNTRY_CODE_NOT_FOUND,null,null));
            return responseBean;
        }
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG,null,null));
        responseBean.setResponseBody(country);

        return responseBean;
    }

   /* @Override
    public ResponseBean<Country> findByCountryName(String countryName) {
        ResponseBean responseBean = new ResponseBean();
        Country c_name=countryRepository.findByCountryName(countryName);
        if(c_name==null||c_name.getCountryName().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COUNTRY_NAME_ALREADY_EXIST_MSG,null,null));
            return responseBean;
        }
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG,null,null));
        responseBean.setResponseBody(c_name);

        return responseBean;
    }*/

    @Override
    public ResponseBean<List<Country>> findByActive() {
        ResponseBean responseBean = new ResponseBean();
        List<Country> CountryList =countryRepository.findByActive(1);
        if(CountryList != null && !CountryList.isEmpty()){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG,null,null));
            responseBean.setResponseBody(CountryList);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG,null,null));
        }
        return responseBean;

    }

    @Override
    public BulkUploadBean uploadBulk(BulkUploadBean bulkUploadBean, BulkMaster bulkMaster) {
        //ResponseBean responseBean = new ResponseBean();
        //BulkUploadBean bulkUploadResponseBean = new BulkUploadBean();
        List<Map<String, String>> errorRecord = new ArrayList<Map<String, String>>();
        List<Map<String, String>> successRecord = new ArrayList<Map<String, String>>();
        int count = 0;
        int progress = 0;
        String token = BulkUploadService.generateRandomString();
        for(Map<String, String> map : bulkUploadBean.getRecords()){
            // Progress count
            count++;
            progress = BulkUploadService.calculateUploadPercentage(count, bulkUploadBean.getRecords().size());
            bulkMasterService.setUploadProgressCount(bulkMaster.getName(), progress, token, false);

            Country country = new Country();
            country.setCode(map.get(BulkHeaderConstant.COUNTRY_CODE));
            country.setCountryName(map.get(BulkHeaderConstant.COUNTRY_NAME));

            Country countryDB = countryRepository.findByCode(country.getCode());
            ResponseBean countryResponseBean = null;
            if(countryDB != null){
                country.setId(countryDB.getId());
                countryResponseBean = updateCountry(country);
            }else {
                countryResponseBean = addCountry(country);
            }


            map.put(BulkHeaderConstant.MESSAGE,countryResponseBean.getMessage());
            if(countryResponseBean.getStatus().equals(ResponseStatus.FAIL)){
                errorRecord.add(map);
                continue;
            }
            successRecord.add(map);
        }
        bulkUploadBean.setBulkMaster(bulkMaster);
        bulkUploadBean.setErrorRecords(errorRecord);
        bulkUploadBean.setSuccessRecords(successRecord);
        // set progress count
        bulkMasterService.setUploadProgressCount(bulkMaster.getName(), 100, token, true);
        return bulkUploadBean;
    }
}
