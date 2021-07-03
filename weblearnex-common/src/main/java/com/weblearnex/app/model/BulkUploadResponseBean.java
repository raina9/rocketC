package com.weblearnex.app.model;

import lombok.Data;

@Data
public class BulkUploadResponseBean {

    private String fileName;
    private int totalRecord;
    private int successRecord;
    private int errorRecord;
    private String successFileName;
    private String errorFileName;
    private String filePath;

}
