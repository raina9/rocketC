package com.weblearnex.app.service;

import com.weblearnex.app.entity.master.Country;
import com.weblearnex.app.model.ResponseBean;
import java.util.List;

public interface CountryService extends BulkUploadService{
    ResponseBean<Country> addCountry(Country country);
    ResponseBean<Country> updateCountry(Country country);
    ResponseBean<Country> deleteCountry(Long countryId);
    ResponseBean<List<Country>> getAllCountries();

    ResponseBean<Country> findByCode(String code);
    //ResponseBean<Country> findByCountryName(String countryName);
    ResponseBean<List<Country>> findByActive();
}
