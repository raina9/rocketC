package com.weblearnex.app.service;


import com.weblearnex.app.entity.remittance.CourierRemittance;
import com.weblearnex.app.model.ResponseBean;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface CourierRemittanceService extends BulkUploadService{

    DataTablesOutput<CourierRemittance> getAllCourierClosedRemittance(DataTablesInput input);
    ResponseEntity<Resource> awbNOReadyForCourierRemittance(String courierCode);
    ResponseEntity<Resource> courierRemittanceReport(String remittanceNo);

}
