package com.weblearnex.app.controller;

import com.weblearnex.app.entity.master.City;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.CityService;
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
public class CityController {

    @Autowired
    private CityService cityService;

    @PostMapping("/addCity")
    public ResponseEntity<ResponseBean> addCity(@RequestBody City city) {
        return new ResponseEntity<ResponseBean>(cityService.addCity(city), HttpStatus.OK);
    }

    @PutMapping("/updateCity")
    public ResponseEntity<ResponseBean> updateCity(@RequestBody City city) {
        return new ResponseEntity<ResponseBean>(cityService.updateCity(city), HttpStatus.OK);
    }
    @DeleteMapping("/deleteCity/{cityId}")
    public ResponseEntity<ResponseBean> deleteCity(@PathVariable Long cityId) {
        return new ResponseEntity<ResponseBean>(cityService.deleteCity(cityId), HttpStatus.OK);
    }
    @GetMapping("/getAllCity")
    public ResponseEntity<ResponseBean> getAllCity() {
        return new ResponseEntity<ResponseBean>(cityService.getAllCity(), HttpStatus.OK);
    }
    @GetMapping("/findByCityCode/{code}")
    public ResponseEntity<ResponseBean> findById(@PathVariable String code) {
        return new ResponseEntity<ResponseBean>(cityService.findByCode(code), HttpStatus.OK);
    }
    /*@GetMapping("/findByCityName/{cityName}")
    public ResponseEntity<ResponseBean> findByCityName(@PathVariable String cityName) {
        return new ResponseEntity<ResponseBean>(cityService.findByCityName(cityName), HttpStatus.OK);
    }*/

    @GetMapping("/findByActiveCity")
    public ResponseEntity<ResponseBean> findByActive() {
        return new ResponseEntity<ResponseBean>(cityService.findByActive(), HttpStatus.OK);
    }
}
