package com.weblearnex.app.service.impl;

import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.BulkHeaderConstant;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.entity.master.BulkMaster;
import com.weblearnex.app.entity.master.City;
import com.weblearnex.app.entity.master.State;
import com.weblearnex.app.model.BulkUploadBean;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.reposatory.CityRepository;
import com.weblearnex.app.reposatory.StateRepository;
import com.weblearnex.app.service.BulkMasterService;
import com.weblearnex.app.service.BulkUploadService;
import com.weblearnex.app.service.CityService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CityServiceImpl implements CityService {
    Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private StateRepository sateRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private BulkMasterService bulkMasterService;


    @Override
    public ResponseBean<City> addCity(City city) {
        ResponseBean responseBean = new ResponseBean();
        if (city.getCode() == null || city.getCode().isEmpty() || cityRepository.findByCode(city.getCode()) != null) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CITY_ALREADY_EXIST_MSG, null, null));
            return responseBean;
        }
        if (city.getStateCode() == null || city.getStateCode().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.STATE_CODE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }

        State state =sateRepository.findByCode(city.getStateCode().trim());
        if(state==null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.STATE_CODE_NOT_EXIST_MSG, null, null));
            return responseBean;
        }

        city = cityRepository.save(city);
        if (city.getId() != null) {
            responseBean.setResponseBody(city);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CITY_ADDED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CITY_ADDED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<City> updateCity(City city) {
        ResponseBean responseBean = new ResponseBean();
        if (city.getCode() == null || city.getCode().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CITY_CODE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (city.getStateCode() == null || city.getStateCode().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.STATE_CODE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        State state =sateRepository.findByCode(city.getStateCode().trim());
        if(state==null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.STATE_CODE_NOT_EXIST_MSG, null, null));
            return responseBean;
        }
        Optional<City> existedCity = cityRepository.findById(city.getId());
        if (!existedCity.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CITY_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        existedCity.get().setActive(city.getActive());
        existedCity.get().setCityName(city.getCityName());
        existedCity.get().setCode(city.getCode());
        existedCity.get().setStateCode(city.getStateCode());
        if (cityRepository.save(existedCity.get()) != null) {
            responseBean.setResponseBody(city);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CITY_UPDATED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CITY_UPDATED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<City> deleteCity(Long cityId) {
        ResponseBean responseBean = new ResponseBean();
        Optional<City> city = cityRepository.findById(cityId);
        if (!city.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CITY_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        city.get().setActive(AppProperty.IN_ACTIVE);
        cityRepository.save(city.get());
        responseBean.setResponseBody(city.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.CITY_DELETE_MSG, null, null));
        return responseBean;
    }

    @Override
    public ResponseBean<List<City>> getAllCity() {
        ResponseBean responseBean = new ResponseBean();
        List<City> cityList = cityRepository.findAll();
        if(cityList !=null && !cityList.isEmpty()){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG, null, null));
            responseBean.setResponseBody(cityList);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<City> findByCode(String code) {
        ResponseBean responseBean = new ResponseBean();
        City city=cityRepository.findByCode(code);
        if(city==null||city.getCode().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CITY_ID_NOT_FOUND,null,null));
            return responseBean;
        }
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG,null,null));
        responseBean.setResponseBody(city);
        return responseBean;
    }

    /*@Override
    public ResponseBean<City> findByCityName(String cityName) {
        ResponseBean responseBean = new ResponseBean();
        City c_name=cityRepository.findByCityName(cityName);
        if(c_name==null||c_name.getCityName().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CITY_NAME_ALREADY_EXIST_MSG,null,null));
            return responseBean;
        }
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG,null,null));
        responseBean.setResponseBody(c_name);
        return responseBean;
    }*/

    @Override
    public ResponseBean<List<City>> findByActive() {
        ResponseBean responseBean = new ResponseBean();
        List<City> CityList =cityRepository.findByActive(1);
        if(CityList != null && !CityList.isEmpty()){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG,null,null));
            responseBean.setResponseBody(CityList);
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
        int progress= 0;
        String token = BulkUploadService.generateRandomString();
        for(Map<String, String> map : bulkUploadBean.getRecords()){
            // Progress Count
            count++;
            progress = BulkUploadService.calculateUploadPercentage(count, bulkUploadBean.getRecords().size());
            bulkMasterService.setUploadProgressCount(bulkMaster.getName(), progress, token, false);

            City city = new City();
            city.setCode(map.get(BulkHeaderConstant.CITY_CODE));
            city.setCityName(map.get(BulkHeaderConstant.CITY_NAME));
            city.setStateCode(map.get(BulkHeaderConstant.STATE_CODE));

            City cityDB = cityRepository.findByCode(city.getCode());
            ResponseBean cityResponseBean = null;
            if(cityDB != null){
                city.setId(cityDB.getId());
                cityResponseBean = updateCity(city);
            }else {
                cityResponseBean = addCity(city);
            }


            map.put(BulkHeaderConstant.MESSAGE, cityResponseBean.getMessage());
            if(cityResponseBean.getStatus().equals(ResponseStatus.FAIL)){
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
