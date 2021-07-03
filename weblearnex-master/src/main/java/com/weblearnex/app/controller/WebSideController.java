package com.weblearnex.app.controller;

import com.weblearnex.app.constant.TrackingType;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.SaleOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/web")
public class WebSideController {

    @Autowired
    private SaleOrderService saleOrderService;

    @GetMapping("/tracking")
    public ResponseEntity<ResponseBean> tracking(@RequestParam(value = "searchVal") String searchValue, @RequestParam(value = "trackingType") TrackingType trackingType) {
        return new ResponseEntity<ResponseBean>(saleOrderService.tracking(trackingType,searchValue), HttpStatus.OK);
    }
}
