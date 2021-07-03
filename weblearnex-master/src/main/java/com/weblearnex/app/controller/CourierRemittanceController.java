package com.weblearnex.app.controller;

import com.weblearnex.app.entity.remittance.CourierRemittance;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.CourierRemittanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class CourierRemittanceController {

    @Autowired
    private CourierRemittanceService courierRemittanceService;

    @PostMapping(value = "/getAllCourierClosedRemittance")
    public DataTablesOutput<CourierRemittance> getAllCourierClosedRemittance(@RequestBody DataTablesInput input) {
        return courierRemittanceService.getAllCourierClosedRemittance(input);
    }
    @RequestMapping(value="/awbNOReadyForCourierRemittance", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadFile(@RequestParam(value = "courierCode",required = false) String courierCode,
                                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        return courierRemittanceService.awbNOReadyForCourierRemittance(courierCode);
    }

    @RequestMapping(value="/courierRemittanceReport", method = RequestMethod.GET)
    public ResponseEntity<Resource> courierRemittanceReport(@RequestParam(value = "remittanceNo",required = false) String remittanceNo,
                                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        return courierRemittanceService.courierRemittanceReport(remittanceNo);
    }
}
