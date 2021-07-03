package com.weblearnex.app.controller;

import com.weblearnex.app.entity.master.Country;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
@RestController
public class CountryController {

    @Autowired
    private CountryService countryService;

    @PostMapping("/addCountry")
    public ResponseEntity<ResponseBean> addCountry(@RequestBody Country country) {
        return new ResponseEntity<ResponseBean>(countryService.addCountry(country), HttpStatus.OK);
    }

    @PutMapping("/updateCountry")
    public ResponseEntity<ResponseBean> updateCountry(@RequestBody Country country) {
        return new ResponseEntity<ResponseBean>(countryService.updateCountry(country), HttpStatus.OK);
    }

    @DeleteMapping("/deleteCountry/{countryId}")
    public ResponseEntity<ResponseBean> deleteCountry(@PathVariable Long countryId) {
        return new ResponseEntity<ResponseBean>(countryService.deleteCountry(countryId), HttpStatus.OK);
    }

    @GetMapping("/getAllCountries")
    public ResponseEntity<ResponseBean> getAllCountries() {
        return new ResponseEntity<ResponseBean>(countryService.getAllCountries(), HttpStatus.OK);
    }

    @GetMapping("/findByCountryCode/{code}")
    public ResponseEntity<ResponseBean> findById(@PathVariable String code) {
        return new ResponseEntity<ResponseBean>(countryService.findByCode(code), HttpStatus.OK);
    }
    /*@GetMapping("/findByCountryName/{countryName}")
    public ResponseEntity<ResponseBean> findByCountryName(@PathVariable String countryName) {
        return new ResponseEntity<ResponseBean>(countryService.findByCountryName(countryName), HttpStatus.OK);
    }*/
    @GetMapping("/findByActiveCountry")
    public ResponseEntity<ResponseBean> findByActive() {
        return new ResponseEntity<ResponseBean>(countryService.findByActive(), HttpStatus.OK);
    }
}
