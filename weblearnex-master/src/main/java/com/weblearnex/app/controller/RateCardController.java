package com.weblearnex.app.controller;

import com.weblearnex.app.entity.master.DomasticRateCard;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.DomasticRateCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class RateCardController {

    @Autowired
    private DomasticRateCardService domasticRateCardService;

    @PostMapping("/addDomasticRateCard")
    public ResponseEntity<ResponseBean> addDomasticRateCard(@RequestBody DomasticRateCard domasticRateCard){
        return new ResponseEntity<ResponseBean>(domasticRateCardService.addDomasticRateCard(domasticRateCard), HttpStatus.OK);
    }

    @PutMapping("/updateDomasticRateCard")
    public ResponseEntity<ResponseBean> updateDomasticRateCard(@RequestBody DomasticRateCard domasticRateCard){
        return new ResponseEntity<ResponseBean>(domasticRateCardService.updateDomasticRateCard(domasticRateCard), HttpStatus.OK);
    }
    @DeleteMapping("/deleteDomasticRateCard/{id}")
    public ResponseEntity<ResponseBean>deleteDomasticRateCard(@PathVariable Long id){
        return new ResponseEntity<ResponseBean>(domasticRateCardService.deleteDomasticRateCard(id),HttpStatus.OK);
    }

    @GetMapping("/getAllDomasticRateCard")
    public ResponseEntity<ResponseBean> getAllDomasticRateCard() {
        return new ResponseEntity<ResponseBean>(domasticRateCardService.getAllDomasticRateCards(), HttpStatus.OK);
    }

    @GetMapping("/findDomasticRateCardById/{id}")
    public ResponseEntity<ResponseBean>findDomasticRateCardById(@PathVariable Long id){
        return new ResponseEntity<ResponseBean>(domasticRateCardService.findById(id),HttpStatus.OK);
    }
    @PostMapping(value = "/domasticRateCard/getAllPaginationAndSort")
    public DataTablesOutput<DomasticRateCard> getAllDomasticRateCardPaginationAndSort(@RequestBody DataTablesInput input) {
        return domasticRateCardService.getAllDomasticRateCardPaginationAndSort(input);
    }

    @GetMapping(value="/viewClientRateReport")
    public ResponseEntity<Resource> viewClientRateReport(HttpServletRequest request, HttpServletResponse response){
        return domasticRateCardService.viewClientRateReport();
    }

    @GetMapping(value="/domesticRateCardReport")
    public ResponseEntity<Resource> domesticRateCardReport(HttpServletRequest request, HttpServletResponse response){
        return domasticRateCardService.domesticRateCardReport();
    }
}
