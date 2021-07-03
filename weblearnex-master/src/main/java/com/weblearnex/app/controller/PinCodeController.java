package com.weblearnex.app.controller;

import com.weblearnex.app.entity.master.Pincode;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.PincodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PinCodeController {
    @Autowired
    private PincodeService pincodeService;

    @PostMapping("/addPincode")
    public ResponseEntity<ResponseBean> addPincode(@RequestBody Pincode pincode) {
        return new ResponseEntity<ResponseBean>(pincodeService.addPincode(pincode), HttpStatus.OK);
    }
    @PutMapping("/updatePincode")
    public ResponseEntity<ResponseBean> updatePincode(@RequestBody Pincode pincode) {
        return new ResponseEntity<ResponseBean>(pincodeService.updatePincode(pincode), HttpStatus.OK);
    }
    @DeleteMapping("/deletePincode/{id}")
    public ResponseEntity<ResponseBean> deletePincode(@PathVariable Long id) {
        return new ResponseEntity<ResponseBean>(pincodeService.deletePincode(id), HttpStatus.OK);
    }
    @GetMapping("/getAllPincode")
    public ResponseEntity<ResponseBean> getAllPincode() {
        return new ResponseEntity<ResponseBean>(pincodeService.getAllPincode(), HttpStatus.OK);
    }

    @GetMapping("/findByPincodeId/{id}")
    public ResponseEntity<ResponseBean> findByPincodeServiceId(@PathVariable(value = "id")Long id) {
        return new ResponseEntity<ResponseBean>(pincodeService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/findByPinCode/{pinCode}")
    public ResponseEntity<ResponseBean> findByPincodeServiceId(@PathVariable(value = "pinCode")String pinCode) {
        return new ResponseEntity<ResponseBean>(pincodeService.findByPinCode(pinCode), HttpStatus.OK);
    }

    @GetMapping("/getCityStateCountryByPincode/{pinCode}")
    public ResponseEntity<ResponseBean> getCityStateCountryByPincode(@PathVariable(value = "pinCode")String pinCode) {
        return new ResponseEntity<ResponseBean>(pincodeService.getCityStateCountryByPincode(pinCode), HttpStatus.OK);
    }
}
