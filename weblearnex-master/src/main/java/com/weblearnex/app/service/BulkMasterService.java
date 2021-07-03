package com.weblearnex.app.service;

import com.weblearnex.app.entity.master.BulkHeader;
import com.weblearnex.app.entity.master.BulkMaster;
import com.weblearnex.app.model.ResponseBean;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.web.multipart.MultipartFile;
//import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface BulkMasterService {

    ResponseBean<BulkMaster> addBulkMaster(BulkMaster bulkMaster);
    ResponseBean<BulkMaster> updateBulkMaster(BulkMaster bulkMaster);
    ResponseBean<BulkMaster> deleteBulkMaster(Long bulkMasterId);
    ResponseBean<BulkMaster> getAllBulkMaster();
    ResponseBean<BulkMaster> findByBulkMasterId(Long id);
    DataTablesOutput<BulkMaster> getAllBulkMasterPaginationAndSort(DataTablesInput input);
    ResponseBean setUploadProgressCount(String bulkUpload, Integer progressCount, String token, boolean uploadCompleted);
    ResponseBean getUploadProgressCount(String bulkUpload, String token);
    ResponseBean deleteProgressKey(String bulkUpload);
}
