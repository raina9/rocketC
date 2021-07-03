package com.weblearnex.app.controller;

import com.weblearnex.app.entity.order.SaleOrder;
import com.weblearnex.app.entity.report.MasterReport;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.MastertReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class MasterReportController {
    @Autowired
    private MastertReportService mastertReportService;

    @PostMapping("/generateMasterReport")
    public ResponseEntity<Resource> generateMasterReport(@RequestBody MasterReport masterReport,
                                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mastertReportService.generateMasterReport(masterReport);
    }

 /*   @PostMapping("/generateMasterRepot")
    public ResponseEntity<Resource> generateMasterRepot(@RequestBody MasterReport masterReport,) {
        return mastertReportService.generateMasterRepot(masterReport);
    }*/
}
