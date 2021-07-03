package com.weblearnex.app.controller;

import com.weblearnex.app.entity.paymentwetway.ClientWallet;
import com.weblearnex.app.entity.paymentwetway.PaymentGetWayLog;
import com.weblearnex.app.entity.setup.Page;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.paymentgetway.PaymentGetWayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class PaymentGetWayController {

    @Autowired
    private PaymentGetWayService paymentGetWayService;

    @GetMapping("/getPaymentGetWayCredential")
    public ResponseEntity<ResponseBean> getPaymentGetWayCredential() {
        return new ResponseEntity<ResponseBean>(paymentGetWayService.getPaymentGetWayCredential(), HttpStatus.OK);
    }

    @PostMapping("/savePaymentGetWayLog")
    public ResponseEntity<ResponseBean> savePaymentGetWayLog(@RequestBody Map<String, String> paymentDetails) {
        return new ResponseEntity<ResponseBean>(paymentGetWayService.savePaymentGetWayLog(paymentDetails), HttpStatus.OK);
    }

    @PostMapping("/fetchPaymentDetails")
    public ResponseEntity<ResponseBean> fetchPaymentDetails(@RequestBody Map<String, String> paymentDetails) {
        return new ResponseEntity<ResponseBean>(paymentGetWayService.fetchPaymentDetails(paymentDetails), HttpStatus.OK);
    }

    @PostMapping(value = "/getAllPaymentGetWayLog")
    public DataTablesOutput<PaymentGetWayLog> getAllPaymentGetWayLog(@RequestBody DataTablesInput input) {
        return paymentGetWayService.getAllPaymentGetWayLog(input);
    }

    @PostMapping(value = "/getAllClientWallet")
    public DataTablesOutput<ClientWallet> getAllClientWallet(@RequestBody DataTablesInput input) {
        return paymentGetWayService.getAllClientWallet(input);
    }

    @PostMapping("/createPaymentOrder")
    public ResponseEntity<ResponseBean> createPaymentOrder(@RequestBody Map<String,Object> mapRequest) {
        return new ResponseEntity<ResponseBean>(paymentGetWayService.createPaymentOrder(mapRequest), HttpStatus.OK);
    }

    @GetMapping(value="/downloadPaymentGetWayLogsReport")
    public ResponseEntity<Resource> downloadPaymentGetWayLogsReport(HttpServletRequest request, HttpServletResponse response){
        return paymentGetWayService.downloadPaymentGetWayLogsReport();
    }
}
