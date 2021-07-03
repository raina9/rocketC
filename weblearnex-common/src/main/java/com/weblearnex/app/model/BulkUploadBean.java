package com.weblearnex.app.model;

import com.weblearnex.app.entity.master.BulkMaster;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class BulkUploadBean {

    private List<Map<String,String>> records;
    private List<Map<String,String>> successRecords;
    private List<Map<String,String>> errorRecords;
    private BulkMaster bulkMaster;
    private String currentUser;
    private String extra;
    private int totalCount;
    private int errorCount;
    private int successCount;
    private String errorFilePath;

}
