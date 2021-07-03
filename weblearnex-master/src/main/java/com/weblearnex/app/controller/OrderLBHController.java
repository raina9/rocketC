package com.weblearnex.app.controller;

import com.weblearnex.app.entity.master.OrderLBH;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.OrderLBHService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class OrderLBHController {

    @Autowired
    private OrderLBHService orderLBHService;

    @PostMapping("/addLBH")
    public ResponseEntity<ResponseBean> addBranch(@RequestBody OrderLBH orderLBH) {
        return new ResponseEntity<ResponseBean>(orderLBHService.addLBH(orderLBH), HttpStatus.OK);
    }

    @GetMapping("/findByOrderLBHId/{id}")
    public ResponseEntity<ResponseBean> findByOrderLBHId(@PathVariable(value = "id")Long id) {
        return new ResponseEntity<ResponseBean>(orderLBHService.findByOrderLBHId(id), HttpStatus.OK);
    }
}
