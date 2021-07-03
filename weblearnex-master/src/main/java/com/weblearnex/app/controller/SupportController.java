package com.weblearnex.app.controller;


import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.SupportService;
import com.weblearnex.app.status.engine.StatusEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class SupportController {

    @Autowired
    private SupportService supportService;

    @Autowired
    private StatusEngine statusEngine;

    @GetMapping("/reloadStatusFlow")
    public ResponseEntity<ResponseBean> reloadStatusFlow () {
        return new ResponseEntity<ResponseBean>(statusEngine.reloadStatusFlow(), HttpStatus.OK);
    }

    @GetMapping("/orderCancellation/{awbNo}")
    public ResponseEntity<ResponseBean> orderCancellation (@PathVariable String awbNo) {
        return new ResponseEntity<ResponseBean>(supportService.orderCancellation(awbNo), HttpStatus.OK);
    }

    @GetMapping("/getDeclaredClassFields")
    public ResponseEntity<ResponseBean> getDeclaredClassFields () {
        return new ResponseEntity<ResponseBean>(supportService.getDeclaredClassFields(), HttpStatus.OK);
    }

    @PostMapping("/updateDeclaredClassFields")
    public ResponseEntity<ResponseBean> getDeclaredClassFields (@RequestBody Map<String,String> requestMap) {
        if(requestMap.get("FIELD") == null || requestMap.get("FIELD").isEmpty()){
            return new ResponseEntity<>(new ResponseBean("Field is not selected.", ResponseStatus.FAIL,null), HttpStatus.OK);
        }
        if(requestMap.get("AWB") == null || requestMap.get("AWB").isEmpty()){
            return new ResponseEntity<>(new ResponseBean("Awb field in empty.", ResponseStatus.FAIL,null), HttpStatus.OK);
        }
        if(requestMap.get("UPDATE_VALUE") == null || requestMap.get("UPDATE_VALUE").isEmpty()){
            return new ResponseEntity<>(new ResponseBean("Update field in empty.", ResponseStatus.FAIL,null), HttpStatus.OK);
        }
        return new ResponseEntity<ResponseBean>(supportService.updateDeclaredClassFields(requestMap.get("FIELD"), requestMap.get("UPDATE_VALUE"), requestMap.get("AWB")), HttpStatus.OK);
    }
}


