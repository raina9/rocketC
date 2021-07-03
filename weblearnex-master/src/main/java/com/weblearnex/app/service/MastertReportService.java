package com.weblearnex.app.service;

import com.weblearnex.app.entity.report.MasterReport;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MastertReportService {
    ResponseEntity<Resource> generateReports(List<String> awbList);
    ResponseEntity<Resource> generateMasterReport(MasterReport masterReport);

}
