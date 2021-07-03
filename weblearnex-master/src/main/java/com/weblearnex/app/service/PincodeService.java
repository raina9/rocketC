package com.weblearnex.app.service;


import com.weblearnex.app.constant.ZoneType;
import com.weblearnex.app.entity.master.Pincode;
import com.weblearnex.app.model.ResponseBean;
import java.util.List;
import java.util.Map;

public interface PincodeService extends BulkUploadService{

    ResponseBean<Pincode> addPincode(Pincode pincode);
    ResponseBean<Pincode> updatePincode(Pincode pincode);
    ResponseBean<Pincode> deletePincode(Long id);
    ResponseBean<List<Pincode>> getAllPincode();
    ResponseBean<Pincode> findById(Long id);
    ResponseBean<Pincode> findByPinCode(String pinCode);
    ResponseBean<Map<String,String>> getCityStateCountryByPincode(String pinCode);
    ZoneType calculateZoneType(Map<String,String> sourcePincodeMap, Map<String,String> dropPincodeMap);
    ResponseBean<ZoneType> getZone(String sourcePincode, String dropPincode);

}
