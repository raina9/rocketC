package com.weblearnex.app.controller;

import com.weblearnex.app.entity.master.RateCardType;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.RateCardTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RateCardTypeController {
    @Autowired
    private RateCardTypeService rateCardTypeService;

    @PostMapping("/addRateCardType")
    public ResponseEntity<ResponseBean> addRateCardType(@RequestBody RateCardType rateCardType) {
        return new ResponseEntity<ResponseBean>(rateCardTypeService.addRateCardType(rateCardType), HttpStatus.OK);
    }
    @PutMapping("/updateRateCardType")
    public ResponseEntity<ResponseBean> updateRateCardType(@RequestBody RateCardType rateCardType) {
        return new ResponseEntity<ResponseBean>(rateCardTypeService.updateRateCardType(rateCardType), HttpStatus.OK);
    }

    @DeleteMapping("/deleteRateCardType/{rateCardTypeId}")
    public ResponseEntity<ResponseBean> deleteRateCardType(@PathVariable Long rateCardTypeId) {
        return new ResponseEntity<ResponseBean>(rateCardTypeService.deleteRateCardType(rateCardTypeId), HttpStatus.OK);
    }
    @GetMapping("/findByRateCardTypeId/{id}")
    public ResponseEntity<ResponseBean> findByRateCardTypeId(@PathVariable(value = "id")Long id) {
        return new ResponseEntity<ResponseBean>(rateCardTypeService.findByRateCardTypeId(id), HttpStatus.OK);
    }

    @PostMapping(value = "/getAllRateCardType")
    public DataTablesOutput<RateCardType> getAllRateCardType(@RequestBody DataTablesInput input) {
        return rateCardTypeService.getAllRateCardType(input);
    }
    @GetMapping("/getAllActiveRateCardType")
    public ResponseEntity<ResponseBean> getAllActiveRateCardType() {
        return new ResponseEntity<ResponseBean>(rateCardTypeService.getAllActiveRateCardType(), HttpStatus.OK);
    }
}
