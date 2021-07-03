package com.weblearnex.app.controller;

import com.google.gson.Gson;
import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.constant.BulkHeaderConstant;
import com.weblearnex.app.constant.BulkUploadConstant;
import com.weblearnex.app.constant.ProjectConstant;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.datatable.reposatory.BulkHeaderRepository;
import com.weblearnex.app.datatable.reposatory.BulkMasterRepository;
import com.weblearnex.app.datatable.reposatory.CourierStatusMappingRepository;
import com.weblearnex.app.entity.master.BulkHeader;
import com.weblearnex.app.entity.master.BulkMaster;
import com.weblearnex.app.model.BulkUploadBean;
import com.weblearnex.app.model.BulkUploadResponseBean;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


@Controller
public class BulkUploadController {

    Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MessageSource messageSource;

    @Autowired
    @Qualifier("applicionConfig")
    private MessageSource applicionConfig;

    @Autowired
    private CityService cityService;

    @Autowired
    private StateService stateService;

    @Autowired
    private CountryService countryService;
    @Autowired
    private CourierService courierService;

    @Autowired
    private PincodeService pincodeService;

    @Autowired
    private ServicablePincodeService servicablePincodeService;

    @Autowired
    private AwbSeriesService awbSeriesService;

    @Autowired
    private OrderLBHService orderLBHService;

    @Autowired
    private SaleOrderService saleOrderService;

    @Autowired
    private PacketHistoryService packetHistoryService;

    @Autowired
    private BulkMasterRepository bulkMasterRepository;

    @Autowired
    private BulkHeaderRepository bulkHeaderRepository;

    @Autowired
    ClientCourierWarehouseMappingService clientCourierWarehouseMappingService;

    @Autowired
    private ClientRemittanceService clientRemittanceService;

    @Autowired
    public CourierRemittanceService courierRemittanceService;

    @Autowired
    public SupportService supportService;

    @Autowired
    private CourierStatusMappingService courierStatusMappingService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private DomasticRateCardService domasticRateCardService;

    @Autowired
    private BranchService branchService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private UserService userService;

        // for upload file
    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity< ResponseBean > uploadData(@RequestParam(value="uploadId") String bulkMasterName,
                                                     @RequestParam(value="extra",required = false) String extra,
                                                     @RequestParam(value="isUpdate",required = false) boolean isUpdate,
                                                   @RequestParam(value="files") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {

        ResponseBean responseBean = new ResponseBean();
        if(file.isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("File is empty.");
            return new ResponseEntity<ResponseBean>(responseBean, HttpStatus.OK);
        }
        BulkMaster bulkMaster = bulkMasterRepository.findByName(bulkMasterName);
        if(bulkMaster == null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Selected bulk type not exist");
            return new ResponseEntity<ResponseBean>(responseBean, HttpStatus.OK);
        }
        List<BulkHeader> bulkHeaderList = bulkHeaderRepository.findBulkHeadersByHeaderCodeIn(Arrays.asList(bulkMaster.getBulkHeaderIds().split(",")));

        List<Map<String,String>> actualFormateData = new ArrayList<Map<String,String>>();
        if(!BulkUploadConstant.SERVICABLE_PINCODE_BULK.equals(bulkMasterName)){  // Adjustment for servicable pincode.
            List<Map<String, String>> excelData = BulkUploadService.readExcel(file);
            for(Map<String, String> excelDataMap : excelData){
                Map<String,String> map = new HashMap<String,String>();
                for(BulkHeader bulkHeader : bulkHeaderList ){
                    if(excelDataMap.containsKey(bulkHeader.getDisplayName()))
                        map.put(bulkHeader.getHeaderCode(), excelDataMap.get(bulkHeader.getDisplayName()));
                    continue;
                }
                if(!map.isEmpty()){
                    actualFormateData.add(map);
                }
            }
        }

        BulkUploadBean bulkUploadBean = new BulkUploadBean();
        bulkUploadBean.setExtra(extra);
        bulkUploadBean.setRecords(actualFormateData);
        if(BulkUploadConstant.CITY_BULK.equals(bulkMasterName)){
            bulkUploadBean = cityService.uploadBulk(bulkUploadBean,bulkMaster);
        } else if (BulkUploadConstant.STATE_BULK.equals(bulkMasterName)){
            bulkUploadBean = stateService.uploadBulk(bulkUploadBean,bulkMaster);
        }else if (BulkUploadConstant.COUNTRY_BULK.equals(bulkMasterName)){
            bulkUploadBean = countryService.uploadBulk(bulkUploadBean,bulkMaster);
        }else if (BulkUploadConstant.PIN_CODE_BULK.equals(bulkMasterName)){
            bulkUploadBean = pincodeService.uploadBulk(bulkUploadBean,bulkMaster);
        }else if (BulkUploadConstant.SERVICABLE_PINCODE_BULK.equals(bulkMasterName)){
            // bulkUploadBean = servicablePincodeService.uploadBulk(bulkUploadBean,bulkMaster);
            // New excel reading consept
            bulkUploadBean = servicablePincodeService.uploadServicablePincode(file, bulkUploadBean, bulkMaster, bulkHeaderList);
        }else if (BulkUploadConstant.AWB_SERIES_BULK.equals(bulkMasterName)){
            bulkUploadBean = awbSeriesService.uploadBulk(bulkUploadBean,bulkMaster);
        } else if (BulkUploadConstant.COURIER_LBH_BULK_UPLOAD.equals(bulkMasterName)){
            bulkUploadBean = orderLBHService.uploadBulk(bulkUploadBean,bulkMaster);
        }else if (BulkUploadConstant.SELF_LBH_BULK.equals(bulkMasterName)){
            bulkUploadBean = orderLBHService.uploadBulk(bulkUploadBean,bulkMaster);
        }else if (BulkUploadConstant.SALE_ORDER_BULK_UPLOAD.equals(bulkMasterName)){
            bulkUploadBean = saleOrderService.uploadBulk(bulkUploadBean,bulkMaster);
        }else if (BulkUploadConstant.PACKET_STATUS_UPDATE_BULK_UPLOAD.equals(bulkMasterName)){
            bulkUploadBean = packetHistoryService.uploadBulk(bulkUploadBean,bulkMaster);
        }else if (BulkUploadConstant.UPLOAD_COURIER_DETAILS.equals(bulkMasterName)){
            bulkUploadBean = courierService.uploadBulk(bulkUploadBean,bulkMaster);
        }else if (BulkUploadConstant.UPLOAD_CLIENT_COURIER_WAREHOUSE_MAPPING.equals(bulkMasterName)){
            bulkUploadBean = clientCourierWarehouseMappingService.uploadBulk(bulkUploadBean,bulkMaster);
        }else if (BulkUploadConstant.GENERATE_REMITTANCE.equals(bulkMasterName)){
            bulkUploadBean = clientRemittanceService.uploadBulk(bulkUploadBean,bulkMaster);
        }else if (BulkUploadConstant.CLOSE_COURIER_REMITTANCE.equals(bulkMasterName)){
            bulkUploadBean = courierRemittanceService.uploadBulk(bulkUploadBean,bulkMaster);
        }else if (BulkUploadConstant.UPDATE_SALEORDER_DETAILS.equals(bulkMasterName)){
            bulkUploadBean = supportService.uploadBulk(bulkUploadBean,bulkMaster);
        }else if (BulkUploadConstant.UPDATE_COURIER_DETAILS.equals(bulkMasterName)){
            bulkUploadBean = supportService.uploadBulk(bulkUploadBean,bulkMaster);
        }else if (BulkUploadConstant.COURIER_STATUS_MAPPING_BULK.equals(bulkMasterName)){
            bulkUploadBean = courierStatusMappingService.uploadBulk(bulkUploadBean,bulkMaster);
        }else if (BulkUploadConstant.DOMESTIC_RATE_CARD_BULK.equals(bulkMasterName)){
            bulkUploadBean = domasticRateCardService.uploadBulkNew(bulkUploadBean,bulkMaster,isUpdate);
        }else if (BulkUploadConstant.BRANCH_BULK_UPLOAD.equals(bulkMasterName)){
            bulkUploadBean = branchService.branchBulkUpload(bulkUploadBean,bulkMaster,isUpdate);
        }else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Please selected bulkupload type.");
            return new ResponseEntity<ResponseBean>(responseBean, HttpStatus.OK);
        }

        /*Write hear success & error record in excel file*/
        List<BulkHeader> bulkHeaders = bulkHeaderRepository.findBulkHeadersByHeaderCodeIn(new ArrayList<>(Arrays.asList(bulkMaster.getBulkHeaderIds().split(ProjectConstant.COMMA))));
        //TODO Change file name after session tracking
        String filePath = applicionConfig.getMessage(AppProperty.BULK_UPLOAD_FILE_PATH, null, null);
        String successFileName = ResponseStatus.SUCCESS.name()+"_"+bulkMaster.getName()+ String.valueOf(new Date().getTime())+ProjectConstant.EXTENSION_XLS;
        String errorFileName = ResponseStatus.FAIL.name()+"_"+bulkMaster.getName()+ String.valueOf(new Date().getTime())+ProjectConstant.EXTENSION_XLS;
        BulkHeader messageHeader = bulkHeaderRepository.findByHeaderCode(BulkHeaderConstant.MESSAGE);
        // Add message headers in success & error files
        bulkHeaders.add(messageHeader);
        if(bulkUploadBean.getSuccessRecords() != null && !bulkUploadBean.getSuccessRecords().isEmpty()){
            try {
                BulkUploadService.writeExcel(bulkHeaders, bulkUploadBean.getSuccessRecords(), successFileName, filePath);
            }catch (Exception e){e.printStackTrace();}
        }
        if(bulkUploadBean.getErrorRecords() != null && !bulkUploadBean.getErrorRecords().isEmpty()){
            try {
                BulkUploadService.writeExcel(bulkHeaders, bulkUploadBean.getErrorRecords(), errorFileName, filePath);
            }catch (Exception e){e.printStackTrace();}
        }
        BulkUploadResponseBean bulkUploadResponseBean = new BulkUploadResponseBean();
        bulkUploadResponseBean.setFilePath(filePath);
        if(BulkUploadConstant.SERVICABLE_PINCODE_BULK.equals(bulkMasterName)){
            bulkUploadResponseBean.setErrorRecord(bulkUploadBean.getErrorCount());
            bulkUploadResponseBean.setSuccessRecord(bulkUploadBean.getSuccessCount());
            bulkUploadResponseBean.setTotalRecord(bulkUploadBean.getTotalCount());
            bulkUploadResponseBean.setErrorFileName(bulkUploadBean.getErrorFilePath());
        }else{
            bulkUploadResponseBean.setErrorRecord(bulkUploadBean.getErrorRecords() != null ? bulkUploadBean.getErrorRecords().size() : 0);
            bulkUploadResponseBean.setErrorFileName(errorFileName);
            bulkUploadResponseBean.setSuccessRecord(bulkUploadBean.getSuccessRecords() != null ? bulkUploadBean.getSuccessRecords().size() : 0);
            bulkUploadResponseBean.setSuccessFileName(successFileName);
            bulkUploadResponseBean.setTotalRecord(bulkUploadBean.getRecords().size());
        }
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setResponseBody(bulkUploadResponseBean);
        redisService.save("uploadCount",bulkUploadResponseBean);
        return new ResponseEntity<ResponseBean>(responseBean, HttpStatus.OK);
    }
    @GetMapping(value = "/uploadCount")
    public ResponseEntity< ResponseBean > getUploadCount(HttpServletRequest request, HttpServletResponse response) {
        ResponseBean responseBean = new ResponseBean();
        Object obj=  redisService.get("uploadCount");
        Gson gson = new Gson();
        BulkUploadResponseBean bulkUploadResponseBean = new BulkUploadResponseBean();
        bulkUploadResponseBean =gson.fromJson(obj.toString(),BulkUploadResponseBean.class);
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setResponseBody(bulkUploadResponseBean);
        return new ResponseEntity<ResponseBean>(responseBean, HttpStatus.OK);
    }

    @RequestMapping(value="/downloadBulkTemplate", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadBulkTemplate(@RequestParam("templateName") String templateName, HttpServletRequest request, HttpServletResponse response){
        BulkMaster bulkMaster = bulkMasterRepository.findByName(templateName);
        List<BulkHeader> bulkHeaders = bulkHeaderRepository.findBulkHeadersByHeaderCodeIn(new ArrayList<>(Arrays.asList(bulkMaster.getBulkHeaderIds().split(ProjectConstant.COMMA))));
        String filePath = applicionConfig.getMessage(AppProperty.BULK_UPLOAD_FILE_PATH, null, null);
        String fileName = "";
        if(BulkUploadConstant.CITY_BULK.equals(templateName)){
            fileName = BulkUploadConstant.CITY_BULK;
        }else if(BulkUploadConstant.STATE_BULK.equals(templateName)){
            fileName = BulkUploadConstant.STATE_BULK;
        }else if(BulkUploadConstant.COUNTRY_BULK.equals(templateName)){
            fileName = BulkUploadConstant.COUNTRY_BULK;
        }else if(BulkUploadConstant.PIN_CODE_BULK.equals(templateName)){
            fileName = BulkUploadConstant.PIN_CODE_BULK;
        }else if(BulkUploadConstant.SERVICABLE_PINCODE_BULK.equals(templateName)){
            fileName = BulkUploadConstant.SERVICABLE_PINCODE_BULK;
        }else if(BulkUploadConstant.AWB_SERIES_BULK.equals(templateName)){
            fileName = BulkUploadConstant.AWB_SERIES_BULK;
        }else if(BulkUploadConstant.COURIER_LBH_BULK_UPLOAD.equals(templateName)){
            fileName = BulkUploadConstant.COURIER_LBH_BULK_UPLOAD;
        }else if(BulkUploadConstant.SELF_LBH_BULK.equals(templateName)){
            fileName = BulkUploadConstant.SELF_LBH_BULK;
        }else if(BulkUploadConstant.SALE_ORDER_BULK_UPLOAD.equals(templateName)){
            fileName = BulkUploadConstant.SALE_ORDER_BULK_UPLOAD;
        }else if(BulkUploadConstant.PACKET_STATUS_UPDATE_BULK_UPLOAD.equals(templateName)){
            fileName = BulkUploadConstant.PACKET_STATUS_UPDATE_BULK_UPLOAD;
        }else if(BulkUploadConstant.UPLOAD_COURIER_DETAILS.equals(templateName)){
            fileName = BulkUploadConstant.UPLOAD_COURIER_DETAILS;
        }else if(BulkUploadConstant.UPLOAD_CLIENT_COURIER_WAREHOUSE_MAPPING.equals(templateName)){
            fileName = BulkUploadConstant.UPLOAD_CLIENT_COURIER_WAREHOUSE_MAPPING;
        }else if(BulkUploadConstant.UPDATE_SALEORDER_DETAILS.equals(templateName)){
            fileName = BulkUploadConstant.UPDATE_SALEORDER_DETAILS;
        }else if(BulkUploadConstant.UPDATE_COURIER_DETAILS.equals(templateName)){
            fileName = BulkUploadConstant.UPDATE_COURIER_DETAILS;
        }else if(BulkUploadConstant.COURIER_STATUS_MAPPING_BULK.equals(templateName)){
            fileName = BulkUploadConstant.COURIER_STATUS_MAPPING_BULK;
        }else if(BulkUploadConstant.DOMESTIC_RATE_CARD_BULK.equals(templateName)){
            fileName = BulkUploadConstant.DOMESTIC_RATE_CARD_BULK;
        }else if(BulkUploadConstant.BRANCH_BULK_UPLOAD.equals(templateName)){
            fileName = BulkUploadConstant.BRANCH_BULK_UPLOAD;
        }

        BulkUploadService.createBulkTemplate(bulkMaster, bulkHeaders, fileName, filePath);

        byte[] bytes = BulkUploadService.downloadExcelFile(filePath, fileName, request, response);
        return ResponseEntity.ok().contentLength(bytes.length)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .cacheControl(CacheControl.noCache())
                .header("Content-Disposition", "attachment; filename=" + fileName+ProjectConstant.EXTENSION_XLS)
                .body(new ByteArrayResource(bytes));
       // return new ResponseEntity<ResponseBean>(BulkUploadService.downloadExcelFile(filePath, fileName, request, response),HttpStatus.OK);
        //return BulkUploadService.downloadExcelFile(filePath, fileName, request, response);
    }

    @RequestMapping(value="/downloadExcelFile", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadFile(@RequestParam("filePath") String filePath, @RequestParam("fileName") String fileName,
                                                            HttpServletRequest request, HttpServletResponse response){

        byte[] bytes = BulkUploadService.downloadExcelFile(filePath+"/"+fileName);
        return  ResponseEntity.ok().contentLength(bytes.length)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .cacheControl(CacheControl.noCache())
                .header("Content-Disposition", "attachment; filename=" + fileName+ProjectConstant.EXTENSION_XLS)
                .body(new ByteArrayResource(bytes));
        //return new ResponseEntity<ResponseBean>(BulkUploadService.downloadExcelFile(filePath, fileName, request, response),HttpStatus.OK);
        //return BulkUploadService.downloadExcelFile(filePath, fileName, request, response);
    }

    @RequestMapping(value="/downloadAllServicablePincode", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadFile(@RequestParam("bulkMaster") String bulkMaster, HttpServletRequest request, HttpServletResponse response){
        return servicablePincodeService.downloadAllServicablePincode(bulkMaster);
    }

}
