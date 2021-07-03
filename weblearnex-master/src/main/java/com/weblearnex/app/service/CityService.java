package com.weblearnex.app.service;

import com.weblearnex.app.entity.master.City;
import com.weblearnex.app.model.ResponseBean;
import java.util.List;

public interface CityService extends BulkUploadService{
    ResponseBean<City> addCity(City city);
    ResponseBean<City> updateCity(City city);
    ResponseBean<City> deleteCity(Long cityId);
    ResponseBean<List<City>> getAllCity();

    ResponseBean<City> findByCode(String code);
    //ResponseBean<City> findByCityName(String cityName);
    ResponseBean<List<City>> findByActive();
}
