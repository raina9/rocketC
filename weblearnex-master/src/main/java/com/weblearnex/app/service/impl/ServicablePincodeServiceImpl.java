package com.weblearnex.app.service.impl;

import com.google.common.collect.Lists;
import com.monitorjbl.xlsx.StreamingReader;
import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.*;
import com.weblearnex.app.datatable.reposatory.BranchRepository;
import com.weblearnex.app.datatable.reposatory.BulkHeaderRepository;
import com.weblearnex.app.datatable.reposatory.BulkMasterRepository;
import com.weblearnex.app.datatable.reposatory.CourierRepository;
import com.weblearnex.app.entity.master.*;
import com.weblearnex.app.model.BulkUploadBean;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.reposatory.*;
import com.weblearnex.app.service.BulkMasterService;
import com.weblearnex.app.service.BulkUploadService;
import com.weblearnex.app.service.RedisService;
import com.weblearnex.app.service.ServicablePincodeService;
import com.weblearnex.app.utils.SheetHandler;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.util.XMLHelper;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.util.*;

@Service
public class ServicablePincodeServiceImpl implements ServicablePincodeService {

    Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ServicablePincodeRepository servicablePincodeRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    @Qualifier("applicionConfig")
    private MessageSource applicionConfig;

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private PincodeRepository pincodeRepository;

    @Autowired
    private BulkHeaderRepository bulkHeaderRepository;

    @Autowired
    private BulkMasterRepository bulkMasterRepository;

    @Autowired
    private RedisService redisService;
    @Autowired
    private BulkMasterService bulkMasterService;


    @Override
    public BulkUploadBean uploadServicablePincode(MultipartFile file, BulkUploadBean bulkUploadBean, BulkMaster bulkMaster, List<BulkHeader> bulkHeaders) {
        try{
            Map<String,String> headerCodeAndName = new HashMap<String,String>();
            bulkHeaders.forEach(bulkHeader -> headerCodeAndName.put(bulkHeader.getHeaderCode().trim(), bulkHeader.getDisplayName().trim()));
            Map<String,Integer> headers = new LinkedHashMap<String, Integer>();

            Workbook workbook = StreamingReader.builder().sstCacheSize(100).open(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(ProjectConstant.ZERO);
            Iterator <Row> rowIterator = sheet.iterator();

            List<Map<String, String>> listResult = new ArrayList<>();
            Map<Row,String> errorRow = new LinkedHashMap<Row,String>();
            int count = 1;
            int totalRowCount = sheet.getLastRowNum();
            int percentage = 0;
            String token = BulkUploadService.generateRandomString();
            ServicablePincode servicablePincode;
            List<ServicablePincode> servicablePincodeList = new ArrayList<ServicablePincode>(500);
            while (rowIterator.hasNext()) {
                Row row = (Row) rowIterator.next();
                try{
                    log.info("Total upload progress -----------> "+ count +"/"+totalRowCount);
                    percentage = BulkUploadService.calculateUploadPercentage(count, totalRowCount);
                    bulkMasterService.setUploadProgressCount(bulkMaster.getName(), percentage, token, false);
                    Iterator<Cell> cellIterator = row.cellIterator();
                    if(count == ProjectConstant.ONE){
                        int headerCount = 0;
                        while (cellIterator.hasNext())
                        {
                            Cell cell = cellIterator.next();
                            headers.put(cell.getStringCellValue().trim(), headerCount);
                            headerCount++;
                        }
                    }else {
                        servicablePincode = new ServicablePincode();
                        servicablePincode.setPinCode(getCellData(row.getCell(headers.get(headerCodeAndName.get(BulkHeaderConstant.PIN_CODE)))));
                        servicablePincode.setBranchCode(getCellData(row.getCell(headers.get(headerCodeAndName.get(BulkHeaderConstant.BRANCH_CODE)))));
                        servicablePincode.setCourierCode(getCellData(row.getCell(headers.get(headerCodeAndName.get(BulkHeaderConstant.COURIER_CODE)))));
                        servicablePincode.setRouteCode(getCellData(row.getCell(headers.get(headerCodeAndName.get(BulkHeaderConstant.ROUTE_CODE)))));
                        try{
                            servicablePincode.setActive(Integer.valueOf(getCellData(row.getCell(headers.get(headerCodeAndName.get(BulkHeaderConstant.ACTIVE))))));
                        }catch (Exception e){
                            errorRow.put(row, "Invalid input, please enter 0 or 1 value in active");
                            continue;
                        };
                        try{
                            servicablePincode.setPickupActive(Integer.valueOf(getCellData(row.getCell(headers.get(headerCodeAndName.get(BulkHeaderConstant.PICKUP_ACTIVE))))));
                        }catch (Exception e){
                            errorRow.put(row,"Invalid pickup active value, please enter 0 or 1 value");
                            continue;
                        };
                        try{
                            servicablePincode.setDropActive(Integer.valueOf(getCellData(row.getCell(headers.get(headerCodeAndName.get(BulkHeaderConstant.DROP_ACTIVE))))));
                        }catch (Exception e){
                            errorRow.put(row, "Invalid drop active value, please enter 0 or 1 value");
                            continue;
                        };
                        try{
                            servicablePincode.setPrepaidActive(Integer.valueOf(getCellData(row.getCell(headers.get(headerCodeAndName.get(BulkHeaderConstant.PREPAID_ACTIVE))))));
                        }catch (Exception e){
                            errorRow.put(row, "Invalid prepaid active value, please enter 0 or 1 value");
                            continue;
                        };
                        try{
                            servicablePincode.setCodActive(Integer.valueOf(getCellData(row.getCell(headers.get(headerCodeAndName.get(BulkHeaderConstant.COD_ACTIVE))))));
                        }catch (Exception e){
                            errorRow.put(row, "Invalid cod active value, please enter 0 or 1 value");
                            continue;
                        };

                        // Field validations
                        if (!(servicablePincode.getActive() == ProjectConstant.ZERO || servicablePincode.getActive() == ProjectConstant.ONE)) {
                            errorRow.put(row, "Please enter 0 or 1 for activate or deactivate pincode.");
                            continue;
                        }
                        if (!(servicablePincode.getCodActive() == ProjectConstant.ZERO || servicablePincode.getCodActive() == ProjectConstant.ONE)) {
                            errorRow.put(row, "Please enter 0 or 1 for enable  or disable COD service.");
                            continue;
                        }
                        if (!(servicablePincode.getPrepaidActive() == ProjectConstant.ZERO || servicablePincode.getPrepaidActive() == ProjectConstant.ONE)) {
                            errorRow.put(row, "Please enter 0 or 1 for enable or disable prepaid service.");
                            continue;
                        }
                        if (!(servicablePincode.getPickupActive() == ProjectConstant.ZERO || servicablePincode.getPickupActive() == ProjectConstant.ONE)) {
                            errorRow.put(row, "Please enter 0 or 1 for enable or disable pickup service.");
                            continue;
                        }
                        if (!(servicablePincode.getDropActive() == ProjectConstant.ZERO || servicablePincode.getDropActive() == ProjectConstant.ONE)) {
                            errorRow.put(row, "Please enter 0 or 1 for enable or disable drop service.");
                            continue;
                        }

                        if (servicablePincode.getCourierCode() == null || !courierRepository.existsByCourierCode(servicablePincode.getCourierCode())) {
                            errorRow.put(row, "Please enter valid courier code.");
                            continue;
                        }

                        String selfCourierCode = applicionConfig.getMessage(AppProperty.SELF_COURIER_CODE, null, null);
                        if(servicablePincode.getCourierCode().equals(selfCourierCode)){
                            if(servicablePincode.getBranchCode() == null || servicablePincode.getBranchCode().isEmpty()){
                                errorRow.put(row, "Please enter branch code in case of self network.");
                                continue;
                            }
                        }
                        if(servicablePincode.getBranchCode() != null && !servicablePincode.getBranchCode().isEmpty()){
                            if (!branchRepository.existsByBranchCode(servicablePincode.getBranchCode())) {
                                errorRow.put(row, "Please enter valid branch code.");
                                continue;
                            }
                        }
                        Long pincodeId = pincodeRepository.findOnlyIdByPinCode(servicablePincode.getPinCode());
                        if (pincodeId == null) {
                            errorRow.put(row, "Please enter valid pincode code. Pincode not exist in pincode master.");
                            continue;
                        }
                        servicablePincode.setPincode_id(pincodeId);
                        Long servicablePincodeId = servicablePincodeRepository.findOnlyIdByPinCodeAndCourierCode(servicablePincode.getPinCode(), servicablePincode.getCourierCode());
                        if (servicablePincodeId != null) {
                            servicablePincode.setId(servicablePincodeId);
                        }
                        if(!servicablePincodeList.contains(servicablePincode)){
                            servicablePincodeList.add(servicablePincode);
                        }
                        if(servicablePincodeList.size() >= 500){
                            Runnable task = () -> {
                                List<ServicablePincode> list = new ArrayList<ServicablePincode>(servicablePincodeList);
                                servicablePincodeRepository.saveAllAndFlush(list);
                            };
                            Thread thread = new Thread(task);
                            thread.start();
                            servicablePincodeList.clear();
                        }
                    }
                    count++;
                }catch (Exception e){
                    errorRow.put(row, "Server internal error.");
                }
            }
            if(servicablePincodeList.size() > 0){
                servicablePincodeRepository.saveAllAndFlush(servicablePincodeList);
            }
            // Write error file
            String filePath = applicionConfig.getMessage(AppProperty.BULK_UPLOAD_FILE_PATH, null, null);
            String errorFileName = ResponseStatus.FAIL.name()+"_"+bulkMaster.getName()+ String.valueOf(new Date().getTime())+ProjectConstant.EXTENSION_XLS;
            if(!errorRow.isEmpty()){
                try{
                    FileOutputStream excelWriteFile = new FileOutputStream(new File(filePath+"/"+errorFileName));

                    SXSSFWorkbook xlsWorkbook = new SXSSFWorkbook();
                    SXSSFSheet xlsSheet = (SXSSFSheet) xlsWorkbook.createSheet(errorFileName);
                    int rowIndex = ProjectConstant.ZERO;
                    SXSSFRow titleRow = (SXSSFRow) xlsSheet.createRow(rowIndex++);
                    List<String> displayHeader = new ArrayList<>(headers.keySet());
                    displayHeader.add("MESSAGE");
                    for (int i = ProjectConstant.ZERO; i < displayHeader.size(); i++) {
                        titleRow.createCell(i).setCellValue(displayHeader.get(i));
                    }
                    if(errorRow != null && errorRow.size() > ProjectConstant.ZERO){
                        for (Map.Entry<Row, String> entry : errorRow.entrySet()) {
                            SXSSFRow dataRow = (SXSSFRow)xlsSheet.createRow(rowIndex++);
                            Iterator<Cell> cellIterator = entry.getKey().cellIterator();
                            while (cellIterator.hasNext()){
                                Cell cell = cellIterator.next();
                                dataRow.createCell(cell.getColumnIndex()).setCellValue(getCellData(cell));
                            }
                            dataRow.createCell(dataRow.getLastCellNum()).setCellValue(entry.getValue());
                        }
                    }
                    xlsWorkbook.write(excelWriteFile);
                }catch (Exception e){
                   e.printStackTrace();
                }
            }
            bulkMasterService.setUploadProgressCount(bulkMaster.getName(), 100, token, true);
            bulkUploadBean.setTotalCount(count+errorRow.size());
            bulkUploadBean.setSuccessCount(count-errorRow.size());
            bulkUploadBean.setErrorCount(errorRow.size());
            bulkUploadBean.setErrorFilePath(errorFileName);
        }catch(Exception e){
            e.printStackTrace();
        }
        return bulkUploadBean;
    }

    public static  String getCellData(Cell cell){
        try {
            if(cell == null){
                return null;
            }
            if(cell.getCellType() == CellType.STRING){
                return String.valueOf(cell.getStringCellValue()).trim();
            }else if(cell.getCellType() == CellType.NUMERIC){
                return NumberToTextConverter.toText(cell.getNumericCellValue()).trim();
            }else if(cell.getCellType() == CellType.BOOLEAN){
                return String.valueOf(cell.getBooleanCellValue()).trim();
            }else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public BulkUploadBean uploadBulk(BulkUploadBean bulkUploadBean, BulkMaster bulkMaster) {
        //BulkUploadBean bulkUploadResponseBean = new BulkUploadBean();
        List<Map<String, String>> errorRecord = new ArrayList<Map<String, String>>();
        List<Map<String, String>> successRecord = new ArrayList<Map<String, String>>();
        //AtomicInteger count = new AtomicInteger();
        //AtomicInteger percentage = new AtomicInteger();
        String token = BulkUploadService.generateRandomString();

       /* // Executor framework for improve performance.
        List<List<Map<String, String>>> listParts = Lists.partition(bulkUploadBean.getRecords(), 10);
        //Queue<Map<String, String>> queue = new ArrayBlockingQueue<Map<String, String>>(bulkUploadBean.getRecords().size(), true, bulkUploadBean.getRecords());
        Queue<List<Map<String, String>>> queue = new ArrayBlockingQueue<List<Map<String, String>>>(listParts.size(), true, listParts);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        while (!queue.isEmpty()){
            *//*count ++;
            log.info("Total upload progress -----------> "+ count+"/"+bulkUploadBean.getRecords().size());
            percentage = BulkUploadService.calculateUploadPercentage(count, bulkUploadBean.getRecords().size());
            bulkMasterService.setUploadProgressCount(bulkMaster.getName(), percentage, token, false);*//*

            List<Map<String, String>> liatMap = queue.poll();
            Future<List<ResponseBean<Map<String,String>>>>  future = executorService.submit(() -> {
                List<ResponseBean<Map<String,String>>> responseBeanList = new ArrayList<>();
                for(Map<String,String> map : liatMap){
                    count.getAndIncrement();
                    log.info("Total upload progress -----------> "+ count.get()+"/"+bulkUploadBean.getRecords().size());
                    percentage.set(BulkUploadService.calculateUploadPercentage(count.get(), bulkUploadBean.getRecords().size()));
                    bulkMasterService.setUploadProgressCount(bulkMaster.getName(), percentage.get(), token, false);

                    ResponseBean<Map<String,String>> responseBean = new ResponseBean<Map<String,String>>();
                    responseBeanList.add(responseBean);
                    ServicablePincode servicablePincode = new ServicablePincode();
                    servicablePincode.setPinCode((map.get(BulkHeaderConstant.PIN_CODE) != null && !map.get(BulkHeaderConstant.PIN_CODE).isEmpty())
                            ? map.get(BulkHeaderConstant.PIN_CODE).trim() : null);
                    servicablePincode.setBranchCode((map.get(BulkHeaderConstant.BRANCH_CODE) != null && !map.get(BulkHeaderConstant.BRANCH_CODE).isEmpty())
                            ? map.get(BulkHeaderConstant.BRANCH_CODE).trim() : null);
                    servicablePincode.setCourierCode((map.get(BulkHeaderConstant.COURIER_CODE) != null && !map.get(BulkHeaderConstant.COURIER_CODE).isEmpty())
                            ? map.get(BulkHeaderConstant.COURIER_CODE).trim() : null);
                    servicablePincode.setRouteCode((map.get(BulkHeaderConstant.ROUTE_CODE) != null && !map.get(BulkHeaderConstant.ROUTE_CODE).isEmpty())
                            ? map.get(BulkHeaderConstant.ROUTE_CODE).trim() : null);
                    try {
                        servicablePincode.setActive((map.get(BulkHeaderConstant.ACTIVE) != null && !map.get(BulkHeaderConstant.ACTIVE).isEmpty())
                                ? Integer.valueOf(map.get(BulkHeaderConstant.ACTIVE).trim()) : AppProperty.IN_ACTIVE);
                    } catch (Exception e) {
                        map.put(BulkHeaderConstant.MESSAGE, "Invalid input, please enter 0 or 1 value in active");
                        responseBean.setStatus(ResponseStatus.FAIL);
                        responseBean.setResponseBody(map);
                        responseBean.setMessage(map.get(BulkHeaderConstant.MESSAGE));
                        e.printStackTrace();
                        continue;
                        //return responseBean;
                    }
                    try {
                        servicablePincode.setCodActive((map.get(BulkHeaderConstant.COD_ACTIVE) != null && !map.get(BulkHeaderConstant.COD_ACTIVE).isEmpty())
                                ? Integer.valueOf(map.get(BulkHeaderConstant.COD_ACTIVE).trim()) : AppProperty.IN_ACTIVE);
                    } catch (Exception e) {
                        map.put(BulkHeaderConstant.MESSAGE, "Invalid input, please enter  0 or 1 value in COD active");
                        responseBean.setStatus(ResponseStatus.FAIL);
                        responseBean.setResponseBody(map);
                        responseBean.setMessage(map.get(BulkHeaderConstant.MESSAGE));
                        e.printStackTrace();
                        continue;
                        //return responseBean;
                    }
                    try {
                        servicablePincode.setDropActive((map.get(BulkHeaderConstant.DROP_ACTIVE) != null && !map.get(BulkHeaderConstant.DROP_ACTIVE).isEmpty())
                                ? Integer.valueOf(map.get(BulkHeaderConstant.DROP_ACTIVE).trim()) : AppProperty.IN_ACTIVE);
                    }catch (Exception e){
                        map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter 0 or 1 value");
                        responseBean.setStatus(ResponseStatus.FAIL);
                        responseBean.setResponseBody(map);
                        responseBean.setMessage(map.get(BulkHeaderConstant.MESSAGE));
                        e.printStackTrace();
                        continue;
                        //return responseBean;
                    }
                    try {
                        servicablePincode.setPickupActive((map.get(BulkHeaderConstant.PICKUP_ACTIVE) != null && !map.get(BulkHeaderConstant.PICKUP_ACTIVE).isEmpty())
                                ? Integer.valueOf(map.get(BulkHeaderConstant.PICKUP_ACTIVE).trim()) : AppProperty.IN_ACTIVE);
                    } catch (Exception e) {
                        map.put(BulkHeaderConstant.MESSAGE, "Invalid input, please enter 0 or 1 value in pickup active");
                        responseBean.setStatus(ResponseStatus.FAIL);
                        responseBean.setResponseBody(map);
                        responseBean.setMessage(map.get(BulkHeaderConstant.MESSAGE));
                        e.printStackTrace();
                        continue;
                       // return responseBean;
                    }
                    try {
                        servicablePincode.setPrepaidActive((map.get(BulkHeaderConstant.PREPAID_ACTIVE) != null && !map.get(BulkHeaderConstant.PREPAID_ACTIVE).isEmpty())
                                ? Integer.valueOf(map.get(BulkHeaderConstant.PREPAID_ACTIVE).trim()) : AppProperty.IN_ACTIVE);
                    } catch (Exception e) {
                        map.put(BulkHeaderConstant.MESSAGE, "Invalid input, please enter 0 or 1 value in prepaid active");
                        responseBean.setStatus(ResponseStatus.FAIL);
                        responseBean.setResponseBody(map);
                        responseBean.setMessage(map.get(BulkHeaderConstant.MESSAGE));
                        e.printStackTrace();
                        continue;
                        //return responseBean;
                    }

                    // Field validations
                    if (!(servicablePincode.getActive() == ProjectConstant.ZERO || servicablePincode.getActive() == ProjectConstant.ONE)) {
                        map.put(BulkHeaderConstant.MESSAGE, "Please enter 0 or 1 for activate or deactivate pincode.");
                        responseBean.setStatus(ResponseStatus.FAIL);
                        responseBean.setResponseBody(map);
                        responseBean.setMessage(map.get(BulkHeaderConstant.MESSAGE));
                        continue;
                        //return responseBean;
                    }
                    if (!(servicablePincode.getCodActive() == ProjectConstant.ZERO || servicablePincode.getCodActive() == ProjectConstant.ONE)) {
                        map.put(BulkHeaderConstant.MESSAGE, "Please enter 0 or 1 for enable  or disable COD service.");
                        responseBean.setStatus(ResponseStatus.FAIL);
                        responseBean.setResponseBody(map);
                        responseBean.setMessage(map.get(BulkHeaderConstant.MESSAGE));
                        continue;
                        //return responseBean;
                    }
                    if (!(servicablePincode.getPrepaidActive() == ProjectConstant.ZERO || servicablePincode.getPrepaidActive() == ProjectConstant.ONE)) {
                        map.put(BulkHeaderConstant.MESSAGE, "Please enter 0 or 1 for enable or disable prepaid service.");
                        responseBean.setStatus(ResponseStatus.FAIL);
                        responseBean.setResponseBody(map);
                        responseBean.setMessage(map.get(BulkHeaderConstant.MESSAGE));
                        continue;
                        //return responseBean;
                    }
                    if (!(servicablePincode.getPickupActive() == ProjectConstant.ZERO || servicablePincode.getPickupActive() == ProjectConstant.ONE)) {
                        map.put(BulkHeaderConstant.MESSAGE, "Please enter 0 or 1 for enable or disable pickup service.");
                        responseBean.setStatus(ResponseStatus.FAIL);
                        responseBean.setResponseBody(map);
                        responseBean.setMessage(map.get(BulkHeaderConstant.MESSAGE));
                        continue;
                        //return responseBean;
                    }
                    if (!(servicablePincode.getDropActive() == ProjectConstant.ZERO || servicablePincode.getDropActive() == ProjectConstant.ONE)) {
                        map.put(BulkHeaderConstant.MESSAGE, "Please enter 0 or 1 for enable or disable drop service.");
                        responseBean.setStatus(ResponseStatus.FAIL);
                        responseBean.setResponseBody(map);
                        responseBean.setMessage(map.get(BulkHeaderConstant.MESSAGE));
                        continue;
                       // return responseBean;
                    }

                    if (servicablePincode.getCourierCode() == null || !courierRepository.existsByCourierCode(servicablePincode.getCourierCode())) {
                        map.put(BulkHeaderConstant.MESSAGE, "Please enter valid courier code.");
                        responseBean.setStatus(ResponseStatus.FAIL);
                        responseBean.setResponseBody(map);
                        responseBean.setMessage(map.get(BulkHeaderConstant.MESSAGE));
                        continue;
                        //return responseBean;
                    }

                    String selfCourierCode = applicionConfig.getMessage(AppProperty.SELF_COURIER_CODE, null, null);
                    if(servicablePincode.getCourierCode().equals(selfCourierCode)){
                        if(servicablePincode.getBranchCode() == null || servicablePincode.getBranchCode().isEmpty()){
                            map.put(BulkHeaderConstant.MESSAGE, "Please enter branch code in case of self network.");
                            responseBean.setStatus(ResponseStatus.FAIL);
                            responseBean.setResponseBody(map);
                            responseBean.setMessage(map.get(BulkHeaderConstant.MESSAGE));
                            continue;
                            //return responseBean;
                        }
                    }

                    if(servicablePincode.getBranchCode() != null && !servicablePincode.getBranchCode().isEmpty()){
                        if (!branchRepository.existsByBranchCode(servicablePincode.getBranchCode())) {
                            map.put(BulkHeaderConstant.MESSAGE, "Please enter valid branch code.");
                            responseBean.setStatus(ResponseStatus.FAIL);
                            responseBean.setResponseBody(map);
                            responseBean.setMessage(map.get(BulkHeaderConstant.MESSAGE));
                            continue;
                            //return responseBean;
                        }
                    }

                    // TODO change code to improve performance of system.
                    Pincode pincode = pincodeRepository.findByPinCode(servicablePincode.getPinCode());
                    if (pincode == null) {
                        map.put(BulkHeaderConstant.MESSAGE, "Please enter valid pincode code. Pincode not exist in pincode master.");
                        responseBean.setStatus(ResponseStatus.FAIL);
                        responseBean.setResponseBody(map);
                        responseBean.setMessage(map.get(BulkHeaderConstant.MESSAGE));
                        continue;
                        //return responseBean;
                    }
                    servicablePincode.setPincode_id(pincode.getId());

                    ServicablePincode servicablePincodeDB = servicablePincodeRepository.findByPinCodeAndCourierCode(servicablePincode.getPinCode(), servicablePincode.getCourierCode());
                    if (servicablePincodeDB == null) {
                        servicablePincodeRepository.save(servicablePincode);
                    } else {
                        servicablePincode.setId(servicablePincodeDB.getId());
                        servicablePincodeRepository.save(servicablePincode);
                    }
                    responseBean.setStatus(ResponseStatus.SUCCESS);
                    responseBean.setResponseBody(map);
                    // return responseBean;
                }
                return null;
            });
            // Get future object result & validate it.
            try{
                List<ResponseBean<Map<String,String>>> futureResponseBean = future.get();
                for(ResponseBean<Map<String,String>> responseBean : futureResponseBean){
                    if(ResponseStatus.SUCCESS.equals(responseBean.getStatus())){
                        successRecord.add((Map<String, String>) responseBean.getResponseBody());
                    }else {
                        errorRecord.add((Map<String, String>) responseBean.getResponseBody());
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        executorService.shutdown();*/

        // This code is for single thread
        int totalProcessed =0;
        int count =0;
        int  percentage =0;
        List<ServicablePincode> servicablePincodeList =new ArrayList<ServicablePincode>();
        List<HashMap<String,String>> mainMap = new ArrayList<HashMap<String,String>>();
        for (Map<String, String> map : bulkUploadBean.getRecords()) {
            totalProcessed++;
            log.info("Total upload progress -----------> "+ totalProcessed+"/"+bulkUploadBean.getRecords().size());
            percentage = BulkUploadService.calculateUploadPercentage(totalProcessed, bulkUploadBean.getRecords().size());
            bulkMasterService.setUploadProgressCount(bulkMaster.getName(), percentage, token, false);

            ServicablePincode servicablePincode = new ServicablePincode();
            servicablePincode.setPinCode((map.get(BulkHeaderConstant.PIN_CODE) != null && !map.get(BulkHeaderConstant.PIN_CODE).isEmpty())
                    ? map.get(BulkHeaderConstant.PIN_CODE).trim() : null);
            servicablePincode.setBranchCode((map.get(BulkHeaderConstant.BRANCH_CODE) != null && !map.get(BulkHeaderConstant.BRANCH_CODE).isEmpty())
                    ? map.get(BulkHeaderConstant.BRANCH_CODE).trim() : null);
            servicablePincode.setCourierCode((map.get(BulkHeaderConstant.COURIER_CODE) != null && !map.get(BulkHeaderConstant.COURIER_CODE).isEmpty())
                    ? map.get(BulkHeaderConstant.COURIER_CODE).trim() : null);
            servicablePincode.setRouteCode((map.get(BulkHeaderConstant.ROUTE_CODE) != null && !map.get(BulkHeaderConstant.ROUTE_CODE).isEmpty())
                    ? map.get(BulkHeaderConstant.ROUTE_CODE).trim() : null);
            try {
                servicablePincode.setActive((map.get(BulkHeaderConstant.ACTIVE) != null && !map.get(BulkHeaderConstant.ACTIVE).isEmpty())
                        ? Integer.valueOf(map.get(BulkHeaderConstant.ACTIVE).trim()) : AppProperty.IN_ACTIVE);
            } catch (Exception e) {
                map.put(BulkHeaderConstant.MESSAGE, "Invalid input, please enter 0 or 1 value in active");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                servicablePincode.setCodActive((map.get(BulkHeaderConstant.COD_ACTIVE) != null && !map.get(BulkHeaderConstant.COD_ACTIVE).isEmpty())
                        ? Integer.valueOf(map.get(BulkHeaderConstant.COD_ACTIVE).trim()) : AppProperty.IN_ACTIVE);
            } catch (Exception e) {
                map.put(BulkHeaderConstant.MESSAGE, "Invalid input, please enter  0 or 1 value in COD active");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                servicablePincode.setDropActive((map.get(BulkHeaderConstant.DROP_ACTIVE) != null && !map.get(BulkHeaderConstant.DROP_ACTIVE).isEmpty())
                        ? Integer.valueOf(map.get(BulkHeaderConstant.DROP_ACTIVE).trim()) : AppProperty.IN_ACTIVE);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter 0 or 1 value");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                servicablePincode.setPickupActive((map.get(BulkHeaderConstant.PICKUP_ACTIVE) != null && !map.get(BulkHeaderConstant.PICKUP_ACTIVE).isEmpty())
                        ? Integer.valueOf(map.get(BulkHeaderConstant.PICKUP_ACTIVE).trim()) : AppProperty.IN_ACTIVE);
            } catch (Exception e) {
                map.put(BulkHeaderConstant.MESSAGE, "Invalid input, please enter 0 or 1 value in pickup active");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                servicablePincode.setPrepaidActive((map.get(BulkHeaderConstant.PREPAID_ACTIVE) != null && !map.get(BulkHeaderConstant.PREPAID_ACTIVE).isEmpty())
                        ? Integer.valueOf(map.get(BulkHeaderConstant.PREPAID_ACTIVE).trim()) : AppProperty.IN_ACTIVE);
            } catch (Exception e) {
                map.put(BulkHeaderConstant.MESSAGE, "Invalid input, please enter 0 or 1 value in prepaid active");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }

            // Field validations
            if (!(servicablePincode.getActive() == ProjectConstant.ZERO || servicablePincode.getActive() == ProjectConstant.ONE)) {
                map.put(BulkHeaderConstant.MESSAGE, "Please enter 0 or 1 for activate or deactivate pincode.");
                errorRecord.add(map);
                continue;
            }
            if (!(servicablePincode.getCodActive() == ProjectConstant.ZERO || servicablePincode.getCodActive() == ProjectConstant.ONE)) {
                map.put(BulkHeaderConstant.MESSAGE, "Please enter 0 or 1 for enable  or disable COD service.");
                errorRecord.add(map);
                continue;
            }
            if (!(servicablePincode.getPrepaidActive() == ProjectConstant.ZERO || servicablePincode.getPrepaidActive() == ProjectConstant.ONE)) {
                map.put(BulkHeaderConstant.MESSAGE, "Please enter 0 or 1 for enable or disable prepaid service.");
                errorRecord.add(map);
                continue;
            }
            if (!(servicablePincode.getPickupActive() == ProjectConstant.ZERO || servicablePincode.getPickupActive() == ProjectConstant.ONE)) {
                map.put(BulkHeaderConstant.MESSAGE, "Please enter 0 or 1 for enable or disable pickup service.");
                errorRecord.add(map);
                continue;
            }
            if (!(servicablePincode.getDropActive() == ProjectConstant.ZERO || servicablePincode.getDropActive() == ProjectConstant.ONE)) {
                map.put(BulkHeaderConstant.MESSAGE, "Please enter 0 or 1 for enable or disable drop service.");
                errorRecord.add(map);
                continue;
            }

            if (servicablePincode.getCourierCode() == null || !courierRepository.existsByCourierCode(servicablePincode.getCourierCode())) {
                map.put(BulkHeaderConstant.MESSAGE, "Please enter valid courier code.");
                errorRecord.add(map);
                continue;
            }

            String selfCourierCode = applicionConfig.getMessage(AppProperty.SELF_COURIER_CODE, null, null);
            if(servicablePincode.getCourierCode().equals(selfCourierCode)){
                if(servicablePincode.getBranchCode() == null || servicablePincode.getBranchCode().isEmpty()){
                    map.put(BulkHeaderConstant.MESSAGE, "Please enter branch code in case of self network.");
                    errorRecord.add(map);
                    continue;
                }
            }

            if(servicablePincode.getBranchCode() != null && !servicablePincode.getBranchCode().isEmpty()){
                if (!branchRepository.existsByBranchCode(servicablePincode.getBranchCode())) {
                    map.put(BulkHeaderConstant.MESSAGE, "Please enter valid branch code.");
                    errorRecord.add(map);
                    continue;
                }
            }

            // TODO change code to improve performance of system.
            //Pincode pincode = pincodeRepository.findByPinCode(servicablePincode.getPinCode());
            Long pincodeId = pincodeRepository.findOnlyIdByPinCode(servicablePincode.getPinCode());
            if (pincodeId == null) {
                map.put(BulkHeaderConstant.MESSAGE, "Please enter valid pincode code. Pincode not exist in pincode master.");
                errorRecord.add(map);
                continue;
            }
            servicablePincode.setPincode_id(pincodeId);

            //ServicablePincode servicablePincodeDB = servicablePincodeRepository.findByPinCodeAndCourierCode(servicablePincode.getPinCode(), servicablePincode.getCourierCode());
            Long servicablePincodeId = servicablePincodeRepository.findOnlyIdByPinCodeAndCourierCode(servicablePincode.getPinCode(), servicablePincode.getCourierCode());
            if (servicablePincodeId != null) {
                servicablePincode.setId(servicablePincodeId);
            }
            servicablePincodeList.add(servicablePincode);
            count++;
            if(count>=30){
                servicablePincodeRepository.saveAllAndFlush(servicablePincodeList);
                count =0;
                servicablePincodeList.clear();
            }
            successRecord.add(map);
            log.info("Total processed progress =============================================> "+totalProcessed+"/"+bulkUploadBean.getRecords().size());
        }

        if(servicablePincodeList.size()>0){
            servicablePincodeRepository.saveAll(servicablePincodeList);
        }
        bulkUploadBean.setBulkMaster(bulkMaster);
        bulkUploadBean.setErrorRecords(errorRecord);
        bulkUploadBean.setSuccessRecords(successRecord);
        // Set upload count
        bulkMasterService.setUploadProgressCount(bulkMaster.getName(), 100, token, true);
        return bulkUploadBean;
    }

    @Override
    public ResponseBean<ServicablePincode> addServicablePincode(ServicablePincode servicablePincode) {
        ResponseBean responseBean = new ResponseBean();
        servicablePincode = servicablePincodeRepository.save(servicablePincode);
        if (servicablePincode.getId() != null) {
            responseBean.setResponseBody(servicablePincode);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.SERVISABLE_PINCODE_ADDED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.PINCODE_ADDED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<ServicablePincode> updateServicablePincode(ServicablePincode servicablePincode) {

        ResponseBean responseBean = new ResponseBean();
        if (servicablePincode.getPinCode() == null || servicablePincode.getPinCode().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.PINCODE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }

        if (servicablePincode.getId() == null) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.SERVISABLE_PINCODE_ID_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        Optional<ServicablePincode> existServicablePincode = servicablePincodeRepository.findById(servicablePincode.getId());
        if (!existServicablePincode.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.SERVISABLE_PINCODE_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        servicablePincode = servicablePincodeRepository.save(servicablePincode);
        if (servicablePincode.getId() != null) {
            responseBean.setResponseBody(servicablePincode);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.SERVISABLE_PINCODE_UPDATED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.SERVISABLE_PINCODE_ADDED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<ServicablePincode> deleteServicablePincode(Long id) {
        ResponseBean responseBean = new ResponseBean();
        Optional<ServicablePincode> existServicablePincode = servicablePincodeRepository.findById(id);
        if (!existServicablePincode.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.SERVISABLE_PINCODE_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        existServicablePincode.get().setActive(AppProperty.IN_ACTIVE);

        if (servicablePincodeRepository.save(existServicablePincode.get()) != null) {
            responseBean.setResponseBody(existServicablePincode.get());
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.SERVISABLE_PINCODE_DELETE_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.SERVISABLE_PINCODE_DELETE_ERROR_MSG, null, null));
        }

        return responseBean;

    }

    @Override
    public ResponseBean<List<ServicablePincode>> getAllServicablePincode() {

        ResponseBean responseBean = new ResponseBean();
        List<ServicablePincode> servicablePincodeList = servicablePincodeRepository.findAll();
        if (servicablePincodeList != null && !servicablePincodeList.isEmpty()) {
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG, null, null));
            responseBean.setResponseBody(servicablePincodeList);
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseEntity<Resource> downloadAllServicablePincode(String bulkMasterId) {
        BulkMaster bulkMaster = bulkMasterRepository.findByName(bulkMasterId.trim());
        if (bulkMaster == null) {
            return null;
        }
        List<BulkHeader> bulkHeaderList = bulkHeaderRepository.findBulkHeadersByHeaderCodeIn(Arrays.asList(bulkMaster.getBulkHeaderIds().split(",")));
        if (bulkHeaderList == null) {
            return null;
        }
        List<Pincode> pincodeList = pincodeRepository.findAll();
        Iterator<Pincode> pincodeIterator = pincodeList.iterator();

        List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
        while (pincodeIterator.hasNext()) {
            Pincode pincode = pincodeIterator.next();
            if (pincode != null) {

            }
            Map<String,String> map = new HashMap<String,String>();
            map.put(BulkHeaderConstant.PIN_CODE,pincode.getPinCode());
            if(pincode.getServicablePincode() != null && !pincode.getServicablePincode().isEmpty()){
                for(ServicablePincode servicablePincode : pincode.getServicablePincode()){
                    map.put(BulkHeaderConstant.PREPAID_ACTIVE,servicablePincode.getPrepaidActive()+"");
                    map.put(BulkHeaderConstant.COD_ACTIVE, servicablePincode.getCodActive()+"");
                    map.put(BulkHeaderConstant.PICKUP_ACTIVE, servicablePincode.getPickupActive()+"");
                    map.put(BulkHeaderConstant.DROP_ACTIVE, servicablePincode.getDropActive()+"");
                    map.put(BulkHeaderConstant.COURIER_CODE, servicablePincode.getCourierCode());
                    map.put(BulkHeaderConstant.ACTIVE, servicablePincode.getActive() + "");
                    map.put(BulkHeaderConstant.BRANCH_CODE, servicablePincode.getBranchCode());
                    mapList.add(map);
                }
            }
        }
        if (!mapList.isEmpty()) {
            try {
                byte bytesArr[] = BulkUploadService.writeExcel(bulkHeaderList, mapList);
                return ResponseEntity.ok().contentLength(bytesArr.length)
                        .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                        .cacheControl(CacheControl.noCache())
                        .header("Content-Disposition", "attachment; filename=ServicablePincodes" + ProjectConstant.EXTENSION_XLS)
                        .body(new ByteArrayResource(bytesArr));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public ResponseBean<Boolean> isPickupActive(String pickupPincode) {
        ResponseBean<Boolean> responseBean = new ResponseBean<Boolean>();
        if (pickupPincode == null || pickupPincode.isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Pickup pincode can not be blank.");
            responseBean.setResponseBody(false);
            return responseBean;
        }
        int pickupActive = 1;
        int pincodeActive = 1;
        //String pincode, Integer pickupActive, Integer active, Collection<String> alloudeCourier
        boolean isPickupActive = servicablePincodeRepository.existsByPinCodeAndPickupActiveAndActive(pickupPincode, pickupActive, pincodeActive);
        if (!isPickupActive) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Pickup pin-code not servicable.");
            responseBean.setResponseBody(false);
            return responseBean;
        }
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setResponseBody(true);
        return responseBean;
    }

    @Override
    public ResponseBean<Boolean> isDropActive(String dropPincode, PaymentType paymentType) {
        ResponseBean<Boolean> responseBean = new ResponseBean<Boolean>();

        if (dropPincode == null || dropPincode.isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Drop pin-code can not be blank.");
            responseBean.setResponseBody(false);
            return responseBean;
        }
        int dropActive = 1;
        int codActive = 1;
        int ppActive = 1;
        int pincodeActive = 1;
        boolean isDropActive = false;
        if (PaymentType.PREPAID.equals(paymentType)) {
            // String pincode, Integer dropActive, Integer prepaidActive, Integer active, Collection<String> alloudeCourier
            isDropActive = servicablePincodeRepository.existsByPinCodeAndDropActiveAndPrepaidActiveAndActive(dropPincode, dropActive, ppActive, pincodeActive);
        } else if (PaymentType.COD.equals(paymentType)) {
            //String pincode, Integer dropActive, Integer codActive, Integer active, Collection<String> alloudeCourier
            isDropActive = servicablePincodeRepository.existsByPinCodeAndDropActiveAndCodActiveAndActive(dropPincode, dropActive, codActive, pincodeActive);
        }
        if (!isDropActive) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Drop pin-code not servicable.");
            responseBean.setResponseBody(false);
            return responseBean;
        }
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setResponseBody(true);
        return responseBean;
    }

    @Override
    public ResponseBean<List<String>> getClientServiceProviders(String sourcePincode, String dropPincode, PaymentType paymentType, List<String> alludeCourier) {
        ResponseBean<List<String>> responseBean =  new ResponseBean<List<String>>();

        Pincode sourcePincodeObj = pincodeRepository.findByPinCode(sourcePincode);
        Pincode dropPincodeObj = pincodeRepository.findByPinCode(dropPincode);

        List<ServicablePincode> pickupProviders = sourcePincodeObj.getServicablePincode();
        List<ServicablePincode> dropProviders = dropPincodeObj.getServicablePincode();
        if(pickupProviders == null || pickupProviders.isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Pickup pincode not serviceable.");
            return responseBean;
        }
        if(dropProviders == null || dropProviders.isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Drop pincode not serviceable.");
            return responseBean;
        }
        Map<String, ServicablePincode> sourceProviderMap = new HashMap<String, ServicablePincode>();
        for(ServicablePincode servicablePincode : pickupProviders){
            sourceProviderMap.put(servicablePincode.getCourierCode(), servicablePincode);
        }
        Map<String, ServicablePincode> dropProviderMap = new HashMap<String, ServicablePincode>();
        for(ServicablePincode servicablePincode : dropProviders){
            dropProviderMap.put(servicablePincode.getCourierCode(), servicablePincode);
        }
        List<String> actualProvidersList = new ArrayList<String>();
        for(String courierCode : alludeCourier){
            // Source serviceable check logic.
            if(!sourceProviderMap.containsKey(courierCode)){
                continue;
            }
            ServicablePincode sourceServicablePincode = sourceProviderMap.get(courierCode);
            if(sourceServicablePincode.getActive() == 0 || sourceServicablePincode.getPickupActive() == 0){
                continue;
            }

            // Destination serviceable check logic.
            if(!dropProviderMap.containsKey(courierCode)){
                continue;
            }
            ServicablePincode dropServicablePincode = dropProviderMap.get(courierCode);
            if(dropServicablePincode.getActive() == 0 || dropServicablePincode.getDropActive() == 0){
                continue;
            }
            if(PaymentType.COD.equals(paymentType) && dropServicablePincode.getCodActive() == 0){
                continue;
            }else if(PaymentType.PREPAID.equals(paymentType) && dropServicablePincode.getPickupActive() == 0){
                continue;
            }
            actualProvidersList.add(courierCode);
        }

        if(actualProvidersList.isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Service provider not found.");
            return responseBean;
        }

        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setResponseBody(actualProvidersList);
        return responseBean;
    }


    @Override
    public ResponseEntity<Resource> getAllCourierServicablePincode(String courierCode) {
        List<String> courierCodes = Arrays.asList(courierCode.split(","));
        List<ServicablePincode> courierList = servicablePincodeRepository.findByCourierCodeIn(courierCodes);
        if (courierList == null) {
            return null;
        }

        BulkMaster bulkMaster = bulkMasterRepository.findByName(BulkUploadConstant.SERVICABLE_PINCODE_BULK);
        SXSSFWorkbook xlsWorkbook = new SXSSFWorkbook();
        SXSSFSheet xlsSheet = (SXSSFSheet) xlsWorkbook.createSheet();
        int rowIndex = ProjectConstant.ZERO;
        SXSSFRow titleRow = (SXSSFRow) xlsSheet.createRow(rowIndex++);
        titleRow.createCell(0).setCellValue("COURIER_CODE");
        titleRow.createCell(1).setCellValue("PIN_CODE");
        titleRow.createCell(2).setCellValue("PREPAID_ACTIVE");
        titleRow.createCell(3).setCellValue("COD_ACTIVE");
        titleRow.createCell(4).setCellValue("PICKUP_ACTIVE");
        titleRow.createCell(5).setCellValue("DROP_ACTIVE");
        titleRow.createCell(6).setCellValue("ACTIVE");
        titleRow.createCell(7).setCellValue("BRANCH_CODE");

        if(courierList != null && courierList.size() > ProjectConstant.ZERO){
            for (ServicablePincode courier : courierList) {
                SXSSFRow dataRow = (SXSSFRow)xlsSheet.createRow(rowIndex++);
                dataRow.createCell(0).setCellValue(courier.getCourierCode());
                dataRow.createCell(1).setCellValue(courier.getPinCode());
                dataRow.createCell(2).setCellValue(courier.getPrepaidActive());
                dataRow.createCell(3).setCellValue(courier.getCodActive());
                dataRow.createCell(4).setCellValue(courier.getPickupActive());
                dataRow.createCell(5).setCellValue(courier.getDropActive());
                dataRow.createCell(6).setCellValue(courier.getActive());
                dataRow.createCell(7).setCellValue(courier.getBranchCode());
            }
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try{
            xlsWorkbook.write(bos);
        }catch (Exception e){
            e.printStackTrace();
        }
    byte [] byteArray = bos.toByteArray();
        return ResponseEntity.ok().contentLength(byteArray.length)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
            .cacheControl(CacheControl.noCache())
            .header("Content-Disposition", "attachment; filename="+"servicablePincode"+ProjectConstant.EXTENSION_XLS)
                .body(new ByteArrayResource(byteArray));
    }


}
