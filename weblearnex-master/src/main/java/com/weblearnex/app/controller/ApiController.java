package com.weblearnex.app.controller;

import com.weblearnex.app.api.service.tracking.TrackingService;
import com.weblearnex.app.model.ResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    /*@Autowired
    private TrackingService trackingService;

    @GetMapping("/callAllTrackingApi")
    public ResponseEntity<ResponseBean> callAllTrackingApi() {
        return new ResponseEntity<ResponseBean>(trackingService.callAllTrackingApi(), HttpStatus.OK);
    }

    @GetMapping("/callTrackingApiByCourierCode")
    public ResponseEntity<ResponseBean> callTrackingApiByCourierCode(@RequestParam(value = "courierCode") String courierCode) {
        return new ResponseEntity<ResponseBean>(trackingService.callTrackingApiByCourierCode(courierCode), HttpStatus.OK);
    }

    @GetMapping("/callTrackingApiAwbNumber")
    public ResponseEntity<ResponseBean> callTrackingApiAwbNumber(@RequestParam(value = "awb") String awb) {
        return new ResponseEntity<ResponseBean>(trackingService.callTrackingApiAwbNumber(awb), HttpStatus.OK);
    }*/

}