package com.weblearnex.app.service.impl;



import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.BulkHeaderConstant;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.constant.ZoneType;
import com.weblearnex.app.entity.master.*;
import com.weblearnex.app.entity.setup.Page;
import com.weblearnex.app.model.BulkUploadBean;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.reposatory.CityRepository;
import com.weblearnex.app.reposatory.CountryRepository;
import com.weblearnex.app.reposatory.PincodeRepository;
import com.weblearnex.app.reposatory.StateRepository;
import com.weblearnex.app.service.BulkMasterService;
import com.weblearnex.app.service.BulkUploadService;
import com.weblearnex.app.service.PincodeService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PincodeServiceImpl implements PincodeService {
    Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PincodeRepository pincodeRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    @Qualifier("applicionConfig")
    private MessageSource applicionConfig;

    @Autowired
    private BulkMasterService bulkMasterService;

    @Override
    public ResponseBean<Pincode> addPincode(Pincode pincode) {
        ResponseBean responseBean = new ResponseBean();
        if (pincode.getPinCode() == null || pincode.getPinCode().isEmpty() || pincodeRepository.findByPinCode(pincode.getPinCode()) != null) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.PINCODE_ALREADY_EXIST_MSG, null, null));
            return responseBean;
        }
        if (pincode.getCityCode() == null || pincode.getCityCode().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CITY_CODE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }

        City city = cityRepository.findByCode(pincode.getCityCode());
        if (city == null) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CITY_NAME_NOT_EXIST_MSG, null, null));
            return responseBean;
        }

        pincode = pincodeRepository.save(pincode);
        if (pincode.getId() != null) {
            responseBean.setResponseBody(pincode);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.PINCODE_ADDED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.PINCODE_ADDED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<Pincode> updatePincode(Pincode pincode) {
        ResponseBean responseBean = new ResponseBean();

        if (pincode.getPinCode() == null || pincode.getPinCode().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.PINCODE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }

        if (pincode.getId() == null ) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.PINCODE_ID_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        Optional<Pincode> existPincode =pincodeRepository.findById(pincode.getId());
        if (!existPincode.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.PINCODE_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        if (pincode.getCityCode() == null || pincode.getCityCode().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CITY_CODE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }

        City city =cityRepository.findByCode(pincode.getCityCode());
        if(city==null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CITY_CODE_NOT_EXIST_MSG, null, null));
            return responseBean;
        }
        existPincode.get().setActive(pincode.getActive());
        existPincode.get().setPinCode(pincode.getPinCode());
        existPincode.get().setCityCode(pincode.getCityCode());


        if (pincodeRepository.save(existPincode.get())!=null) {
            responseBean.setResponseBody(pincode);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.PINCODE_UPDATED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.PINCODE_UPDATED_ERROR_MSG, null, null));
        }
        return responseBean;

    }

    @Override
    public ResponseBean<Pincode> deletePincode(Long id) {
        ResponseBean responseBean = new ResponseBean();
        Optional<Pincode> pincode = pincodeRepository.findById(id);
        if (!pincode.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.PINCODE_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        pincode.get().setActive(AppProperty.IN_ACTIVE);

        if ( pincodeRepository.save(pincode.get())!=null) {
            responseBean.setResponseBody(pincode.get());
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.PINCODE_DELETE_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.PINCODE_DELETE_ERROR_MSG, null, null));
        }

        return responseBean;
    }

    @Override
    public ResponseBean<List<Pincode>> getAllPincode() {
        ResponseBean responseBean = new ResponseBean();
        List<Pincode> pincodeList = pincodeRepository.findAll();
        if(pincodeList !=null && !pincodeList.isEmpty()){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG, null, null));
            responseBean.setResponseBody(pincodeList);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG, null, null));
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

            // Progress Count
            count++;
            progress = BulkUploadService.calculateUploadPercentage(count, bulkUploadBean.getRecords().size());
            bulkMasterService.setUploadProgressCount(bulkMaster.getName(), progress, token, false);

            Pincode pincode = new Pincode();
            pincode.setPinCode(map.get(BulkHeaderConstant.PIN_CODE));
            pincode.setCityCode(map.get(BulkHeaderConstant.CITY_CODE));

            Pincode pincodeDB = pincodeRepository.findByPinCode(pincode.getPinCode());
            ResponseBean countryResponseBean = null;
            if(pincodeDB != null){
                pincode.setId(pincodeDB.getId());
                countryResponseBean = updatePincode(pincode);
            }else {
                countryResponseBean = addPincode(pincode);
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
        bulkMasterService.setUploadProgressCount(bulkMaster.getName(), 100, token, true);
        return bulkUploadBean;
    }

    @Override
    public ResponseBean<Pincode> findById(Long id){
        ResponseBean responseBean = new ResponseBean();
        Optional<Pincode> pincode = pincodeRepository.findById(id);
        if (!pincode.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.PINCODE_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        responseBean.setResponseBody(pincode.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.PINCODE_ID_FOUND, null, null));
        return responseBean;
    }
    @Override
    public ResponseBean<Pincode> findByPinCode(String pinCode){
        ResponseBean responseBean = new ResponseBean();
        Pincode pincode = pincodeRepository.findByPinCode(pinCode);
        if (pincode==null) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.PINCODE_VALUE_NOT_FOUND, null, null));
            return responseBean;
        }
        responseBean.setResponseBody(pincode);
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.PINCODE_VALUE_FOUND, null, null));
        return responseBean;
    }

    @Override
    public ResponseBean<Map<String, String>> getCityStateCountryByPincode(String pinCode) {
        // TODO change method to improve system performance, Use join query of JPA
        ResponseBean<Map<String,String>> responseBean = new ResponseBean<>();
        String cityCode = pincodeRepository.findOnlyCityCodeByPinCode(pinCode);
        if(cityCode == null || cityCode.trim().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Pincode not found in syatem.");
            return responseBean;
        }
        City city = cityRepository.findByCode(cityCode);
        Map<String,String> resultMap = new HashMap<String,String>();
        if(city != null){
            resultMap.put("CITY_NAME",city.getCityName());
            resultMap.put("CITY_CODE",city.getCode());
            State state = stateRepository.findByCode(city.getStateCode());
            if(state != null){
                resultMap.put("STATE_NAME", state.getStateName());
                resultMap.put("STATE_CODE", state.getCode());
                Country country = countryRepository.findByCode(state.getCountryCode());
                resultMap.put("COUNTRY_NAME", country != null ? country.getCountryName() : null);
                resultMap.put("COUNTRY_CODE", country != null ? country.getCode() : null);
            }
        }
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setResponseBody(resultMap);
        return responseBean;
    }

    @Override
    public ZoneType calculateZoneType(Map<String,String> sourcePincodeMap, Map<String,String> dropPincodeMap) {
        List<String> metroCityCodeList = new ArrayList<>(Arrays.asList(applicionConfig.getMessage(AppProperty.METRO_ZOME_CITY_CODE, null, null).split(",")));
        List<String> nejkStateCodeList = new ArrayList<>(Arrays.asList(applicionConfig.getMessage(AppProperty.NEJK_ZOME_STATES_CODE, null, null).split(",")));
        if( sourcePincodeMap == null || dropPincodeMap == null || sourcePincodeMap.isEmpty() || dropPincodeMap.isEmpty()){
            return null;
        }
        //1- If source city & destination city same the local
        if(sourcePincodeMap.get("CITY_CODE") != null && sourcePincodeMap.get("CITY_CODE").equals(dropPincodeMap.get("CITY_CODE"))){
            return ZoneType.LOCAL;
        }
        // 2 If shipment move from one metro city to another metro city then zone metro
        else if(metroCityCodeList.contains(sourcePincodeMap.get("CITY_CODE")) && metroCityCodeList.contains(dropPincodeMap.get("CITY_CODE"))){
            return ZoneType.METRO;
        }
        // 3- If shipment move from one state to another state then zone withenState
        else if(sourcePincodeMap.get("STATE_CODE") != null && sourcePincodeMap.get("STATE_CODE").equals(dropPincodeMap.get("STATE_CODE"))){
            return ZoneType.WITHINSTATE;
        }
        // 4- If shipment move one nejk state to another nejk state then zone nejk
        else if(nejkStateCodeList.contains(sourcePincodeMap.get("STATE_CODE")) || nejkStateCodeList.contains(dropPincodeMap.get("STATE_CODE"))){
            return ZoneType.NEJK;
        }else
            return ZoneType.ROI;
    }

    @Override
    public ResponseBean<ZoneType> getZone(String sourcePincode, String dropPincode) {
        ResponseBean<ZoneType> responseBean = new ResponseBean<ZoneType>();
        ResponseBean<Map<String,String>> sourcePincodeResponse = getCityStateCountryByPincode(sourcePincode);
        if(ResponseStatus.FAIL.equals(sourcePincodeResponse.getStatus())){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(sourcePincodeResponse.getMessage());
            return responseBean;
        }
        ResponseBean<Map<String,String>> dropPincodeResponse = getCityStateCountryByPincode(dropPincode);
        if(ResponseStatus.FAIL.equals(dropPincodeResponse.getStatus())){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(dropPincodeResponse.getMessage());
            return responseBean;
        }

        ZoneType zoneType = calculateZoneType(sourcePincodeResponse.getResponseBody(), dropPincodeResponse.getResponseBody());
        if(zoneType == null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(sourcePincodeResponse.getMessage());
            return responseBean;
        }

        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setResponseBody(zoneType);
        return responseBean;
    }

}
