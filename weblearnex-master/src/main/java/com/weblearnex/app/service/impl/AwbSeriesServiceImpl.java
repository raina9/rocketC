package com.weblearnex.app.service.impl;

import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.*;
import com.weblearnex.app.datatable.reposatory.*;
import com.weblearnex.app.entity.master.*;
import com.weblearnex.app.entity.setup.Client;
import com.weblearnex.app.entity.setup.Courier;
import com.weblearnex.app.model.BulkUploadBean;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.reposatory.*;
import com.weblearnex.app.service.AwbSeriesService;
import com.weblearnex.app.service.BulkMasterService;
import com.weblearnex.app.service.BulkUploadService;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.*;

@Service
public class AwbSeriesServiceImpl  implements AwbSeriesService {
    Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AwbSeriesRepository awbSeriesRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private SaleOrderRepository saleOrderRepository;

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private BulkMasterRepository bulkMasterRepository;

    @Autowired
    private BulkHeaderRepository bulkHeaderRepository;

    @Autowired
    private BulkMasterService bulkMasterService;

    @Override
    public ResponseBean<AwbSeries> addAwbSeries(AwbSeries awbSeries) {
        ResponseBean responseBean = new ResponseBean();
        /*if (awbSeries.getAwbNumber() == null || awbSeries.getAwbNumber().isEmpty() || awbSeriesRepository.findByAwbNumber(awbSeries.getAwbNumber()) != null) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.AWBSERIES_ALREADY_EXIST_MSG, null, null));
            return responseBean;
        }
        if (awbSeries.getEntityCode() == null) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.AWBSERIES_ID_DOES_NOT_EMPTY_OR_NO_DATA_FOUND_IN_DATABASE, null, null));
            return responseBean;
        }
        if (awbSeries.getPaymentType() == null ||awbSeries.getSeriesType()==null) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.AWBSERIES_ALREADY_EXIST_MSG, null, null));
            return responseBean;
        }

        awbSeries = awbSeriesRepository.save(awbSeries);
        if (awbSeries.getId() != null) {
            responseBean.setResponseBody(awbSeries);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.AWBSERIES_ADDED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.AWBSERIES_ADDED_ERROR_MSG, null, null));
        }*/
        return responseBean;
    }

    @Override
    public ResponseBean<AwbSeries> updateAwbSeries(AwbSeries awbSeries) {
        ResponseBean responseBean = new ResponseBean();
        if (awbSeries.getAwbNumber() == null || awbSeries.getAwbNumber().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.AWBSERIES_CODE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (awbSeries.getId() == null || !awbSeriesRepository.findById(awbSeries.getId()).isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.AWBSERIES_ID_DOES_NOT_EMPTY_OR_NO_DATA_FOUND_IN_DATABASE, null, null));
            return responseBean;
        }
        awbSeries = awbSeriesRepository.save(awbSeries);
        if (awbSeries != null) {
            responseBean.setResponseBody(awbSeries);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.AWBSERIES_UPDATED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.AWBSERIES_UPDATED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<AwbSeries> deleteAwbSeries(Long awbSeriesId) {
            ResponseBean responseBean = new ResponseBean();
                Optional<AwbSeries> awbSeries = awbSeriesRepository.findById(awbSeriesId);
                if (!awbSeries.isPresent()) {
                    responseBean.setStatus(ResponseStatus.FAIL);
                    responseBean.setMessage(messageSource.getMessage(MessageProperty.AWBSERIES_ID_NOT_FOUND, null, null));
                    return responseBean;
                }

            awbSeriesRepository.delete(awbSeries.get());
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.AWBSERIES_DELETE_MSG, null, null));
            return responseBean;
    }

    @Override
    public ResponseBean<List<AwbSeries>> getAllAwbSeries() {

        ResponseBean responseBean = new ResponseBean();
        List<AwbSeries> awbSeriesList = awbSeriesRepository.findAll();
        if(awbSeriesList !=null && !awbSeriesList.isEmpty()){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG, null, null));
            responseBean.setResponseBody(awbSeriesList);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG, null, null));
        }
        return responseBean;
    }


    @Override
    public ResponseBean<AwbSeries> findByAwbSeries(String awbSeries) {
        ResponseBean responseBean = new ResponseBean();
        List<AwbSeries> AwbSeriesList = (List<AwbSeries>) awbSeriesRepository.findByAwbNumber(awbSeries);
        if(AwbSeriesList != null && !AwbSeriesList.isEmpty()){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG,null,null));
            responseBean.setResponseBody(AwbSeriesList);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG,null,null));
        }
        return responseBean;
    }


    @Override
    public BulkUploadBean uploadBulk(BulkUploadBean bulkUploadBean, BulkMaster bulkMaster) {
        List<Map<String, String>> errorRecord = new ArrayList<Map<String, String>>();
        List<Map<String, String>> successRecord = new ArrayList<Map<String, String>>();
        int count = 0;
        int progress = 0;
        String token = BulkUploadService.generateRandomString();
        for(Map<String, String> map : bulkUploadBean.getRecords()){
            count++;
            progress = BulkUploadService.calculateUploadPercentage(count, bulkUploadBean.getRecords().size());
            bulkMasterService.setUploadProgressCount(bulkMaster.getName(), progress, token, false);


            AwbSeries awbSeries = new AwbSeries();
            awbSeries.setAwbNumber((map.get(BulkHeaderConstant.AWB_NUMBER) != null && !map.get(BulkHeaderConstant.AWB_NUMBER).isEmpty())
                    ? map.get(BulkHeaderConstant.AWB_NUMBER).trim() : null);
            awbSeries.setEntityCode((map.get(BulkHeaderConstant.ENTITY_CODE) != null && !map.get(BulkHeaderConstant.ENTITY_CODE).isEmpty() )
                    ? map.get(BulkHeaderConstant.ENTITY_CODE).trim() : null);
            String paymentType = map.get(BulkHeaderConstant.PAYMENT_TYPE);

            if(PaymentType.PREPAID.name().equals(paymentType)) {
                awbSeries.setPaymentType(PaymentType.PREPAID);
            }else if(PaymentType.COD.name().equals(paymentType)) {
                awbSeries.setPaymentType(PaymentType.COD);
            }else if(PaymentType.BOTH.name().equals(paymentType)) {
                awbSeries.setPaymentType(PaymentType.BOTH);
            }
            String seriestype = map.get(BulkHeaderConstant.SERIES_TYPE);
            if(SeriesType.CLIENT.name().equals(seriestype)){
                awbSeries.setSeriesType(SeriesType.CLIENT);
            }else if(SeriesType.COURIER.name().equals(seriestype)){
                awbSeries.setSeriesType(SeriesType.COURIER);
            }

            if(awbSeries.getSeriesType().equals(SeriesType.CLIENT)){
                Client client = clientRepository.findByClientCode(awbSeries.getEntityCode());
                if(client==null){
                    map.put(BulkHeaderConstant.MESSAGE,"Entity code does not match in client database.");
                    errorRecord.add(map);
                    continue;
                }
            }
            if(awbSeries.getSeriesType().equals(SeriesType.COURIER)){
              Courier vendor= courierRepository.findByCourierCode(awbSeries.getEntityCode());
                if(vendor==null){
                    map.put(BulkHeaderConstant.MESSAGE,"Entity code does not match in vendor database.");
                    errorRecord.add(map);
                    continue;
                }
            }

            if(awbSeries.getAwbNumber()==null){
                map.put(BulkHeaderConstant.MESSAGE,"Please enter Awb Number.");
                errorRecord.add(map);
                continue;
            }
            //TODO We will check series type with Entity type
            AwbSeries  awbSeriesDb = awbSeriesRepository.findByAwbNumber(awbSeries.getAwbNumber());
            if(awbSeriesDb == null){
                awbSeriesRepository.save(awbSeries);
            }else {
                map.put(BulkHeaderConstant.MESSAGE,"Series already exist.");
                errorRecord.add(map);
                continue;
            }
            successRecord.add(map);
        }
        bulkUploadBean.setBulkMaster(bulkMaster);
        bulkUploadBean.setErrorRecords(errorRecord);
        bulkUploadBean.setSuccessRecords(successRecord);
        // set Upload progress count
        bulkMasterService.setUploadProgressCount(bulkMaster.getName(), 100, token, true);
        return bulkUploadBean;
    }


    @Override
    public ResponseEntity<Resource> downloadAllAwbSeries(String bulkMasterId) {
        BulkMaster bulkMaster = bulkMasterRepository.findByName(bulkMasterId.trim());
        if(bulkMaster == null){
            return null;
        }
        List<BulkHeader> bulkHeaderList = bulkHeaderRepository.findBulkHeadersByHeaderCodeIn(Arrays.asList(bulkMaster.getBulkHeaderIds().split(",")));
        if(bulkHeaderList == null){
            return null;
        }
        List<AwbSeries> awbSeriesList = awbSeriesRepository.findAll();
        Iterator<AwbSeries> awbIterator = awbSeriesList.iterator();

        List<Map<String,String>> mapList = new ArrayList<Map<String,String>>();
        while (awbIterator.hasNext()){
            AwbSeries awbSeries = awbIterator.next();
            if(awbSeries != null){

            }
            Map<String,String> map = new HashMap<String,String>();
            map.put(BulkHeaderConstant.AWB_NUMBER,awbSeries.getAwbNumber());
            map.put(BulkHeaderConstant.SERIES_TYPE,awbSeries.getSeriesType()+"");
            map.put(BulkHeaderConstant.PAYMENT_TYPE, awbSeries.getPaymentType()+"");
            map.put(BulkHeaderConstant.ENTITY_CODE, awbSeries.getEntityCode()+"");
            mapList.add(map);
        }
        if(!mapList.isEmpty()){
            try {
                byte bytesArr[] =   BulkUploadService.writeExcel(bulkHeaderList, mapList);
                return ResponseEntity.ok().contentLength(bytesArr.length)
                        .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                        .cacheControl(CacheControl.noCache())
                        .header("Content-Disposition", "attachment; filename=ServicablePincodes"+ProjectConstant.EXTENSION_XLS)
                        .body(new ByteArrayResource(bytesArr));
            }catch (Exception e){e.printStackTrace();}
        }
        return null;
    }

    @Override
    public ResponseBean<String> getAutoAwbNumber(Client client, PaymentType paymentType) {
        ResponseBean responseBean  = new ResponseBean();
        try {
            if(client == null){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Client ID invalid.");
                return responseBean;
            }
            Long sequenceCount = client.getAwbSeriesSequence() != null ? client.getAwbSeriesSequence() : 1L;
            String awbNumber = null;
            while (true){
                awbNumber = client.getAwbSeriesPrefix()+""+String.valueOf(sequenceCount);
                if(awbSeriesRepository.existsAwbSeriesByAwbNumber(awbNumber)){
                    sequenceCount++;
                    continue;
                }
                if (saleOrderRepository.existsSaleOrderByReferanceNo(awbNumber)){
                    sequenceCount++;
                    continue;
                }
                break;
            }
            AwbSeries awbSeries = new AwbSeries();
            awbSeries.setAwbNumber(awbNumber);
            awbSeries.setEntityCode(client.getClientCode());
            awbSeries.setSeriesType(SeriesType.CLIENT);
            awbSeries.setPaymentType(paymentType);
            awbSeriesRepository.save(awbSeries);
            // Update series sequence in client account.
            client.setAwbSeriesSequence(sequenceCount);
            clientRepository.save(client);

            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setResponseBody(awbNumber);

        }catch (Exception e){
            e.printStackTrace();
        }
        return responseBean;
    }

    @Override
    public ResponseEntity<Resource> autoGenerateBulkSeries(String clientCode, int totalSeriesCount, PaymentType paymentType) throws Exception {
        Client client = clientRepository.findByClientCode(clientCode);
        if(client == null){
            return null;
        }
        List<AwbSeries> actualAwbSeriesList = new ArrayList<AwbSeries>();
        long sequenceCount = client.getAwbSeriesSequence() == null ? 0 : client.getAwbSeriesSequence();
        for (int i=0; i <totalSeriesCount; i++){
            String awbNumber = null;
            while (true){
                awbNumber = client.getAwbSeriesPrefix()+""+String.valueOf(sequenceCount);
                if(awbSeriesRepository.existsAwbSeriesByAwbNumber(awbNumber)){
                    sequenceCount++;
                    continue;
                }
                if (saleOrderRepository.existsSaleOrderByReferanceNo(awbNumber)){
                    sequenceCount++;
                    continue;
                }
                break;
            }

            AwbSeries awbSeries = new AwbSeries();
            awbSeries.setAwbNumber(awbNumber);
            awbSeries.setEntityCode(clientCode);
            awbSeries.setSeriesType(SeriesType.CLIENT);
            awbSeries.setPaymentType(paymentType);
            awbSeriesRepository.save(awbSeries);

            actualAwbSeriesList.add(awbSeries);
        }
        BulkMaster bulkMaster = bulkMasterRepository.findByName(BulkUploadConstant.AWB_SERIES_BULK);
        List<BulkHeader> bulkHeaders = new ArrayList<BulkHeader>(bulkHeaderRepository.findBulkHeadersByHeaderCodeIn(Arrays.asList(bulkMaster.getBulkHeaderIds().split(","))));

        SXSSFWorkbook xlsWorkbook = new SXSSFWorkbook();
        SXSSFSheet xlsSheet = (SXSSFSheet) xlsWorkbook.createSheet();
        int rowIndex = ProjectConstant.ZERO;
        SXSSFRow titleRow = (SXSSFRow) xlsSheet.createRow(rowIndex++);
        titleRow.createCell(0).setCellValue("ENTITY_TYPE");
        titleRow.createCell(1).setCellValue("ENTITY_CODE");
        titleRow.createCell(2).setCellValue("PAYMENT_TYPE");
        titleRow.createCell(3).setCellValue("AWB_NUMBER");

        if(actualAwbSeriesList != null && actualAwbSeriesList.size() > ProjectConstant.ZERO){
            for (AwbSeries awbSeries : actualAwbSeriesList) {
                SXSSFRow dataRow = (SXSSFRow)xlsSheet.createRow(rowIndex++);
                dataRow.createCell(0).setCellValue(SeriesType.CLIENT.name());
                dataRow.createCell(1).setCellValue(clientCode);
                dataRow.createCell(2).setCellValue(paymentType.name());
                dataRow.createCell(3).setCellValue(ProjectConstant.NULL_STRING);
                dataRow.createCell(3).setCellValue(awbSeries.getAwbNumber());
            }
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            xlsWorkbook.write(bos);
            client.setAwbSeriesSequence(sequenceCount);
            clientRepository.save(client);
        } catch (Exception e) {
            e.printStackTrace();
            for(AwbSeries series : actualAwbSeriesList){awbSeriesRepository.delete(series);}
            bos.close();
        }
        byte [] byteArray = bos.toByteArray();
        return ResponseEntity.ok().contentLength(byteArray.length)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .cacheControl(CacheControl.noCache())
                .header("Content-Disposition", "attachment; filename="+clientCode+"-awbseries"+ProjectConstant.EXTENSION_XLS)
                .body(new ByteArrayResource(byteArray));
    }

    @Override
    public ResponseEntity<Resource> downloadPendingAwbSeries(SeriesType seriesType, String typeValue) {
        if(seriesType == null){
            return null;
        }
        if(typeValue == null){
            return null;
        }
        List<AwbSeries> awbSeriesList = awbSeriesRepository.findAllBySeriesTypeAndEntityCode(seriesType,typeValue);
        if(awbSeriesList==null){
            return null;
        }
        SXSSFWorkbook xlsWorkbook = new SXSSFWorkbook();
        SXSSFSheet xlsSheet = (SXSSFSheet) xlsWorkbook.createSheet();
        int rowIndex = ProjectConstant.ZERO;
        SXSSFRow titleRow = (SXSSFRow) xlsSheet.createRow(rowIndex++);
        titleRow.createCell(0).setCellValue("AWB_NUMBER");
        titleRow.createCell(1).setCellValue("ENTITY_CODE");
        titleRow.createCell(2).setCellValue("PAYMENT_TYPE");
        titleRow.createCell(3).setCellValue("SERIES_TYPE");


            if(awbSeriesList != null && awbSeriesList.size() > ProjectConstant.ZERO) {
                for (AwbSeries clientAwbSeries : awbSeriesList) {
                    SXSSFRow dataRow = (SXSSFRow) xlsSheet.createRow(rowIndex++);
                    dataRow.createCell(0).setCellValue(clientAwbSeries.getAwbNumber() != null ? clientAwbSeries.getAwbNumber() + " " : "NA");
                    dataRow.createCell(1).setCellValue(clientAwbSeries.getEntityCode() != null ? clientAwbSeries.getEntityCode() + " " : "NA");
                    dataRow.createCell(2).setCellValue(clientAwbSeries.getPaymentType() != null ? clientAwbSeries.getPaymentType() + " " : "NA");
                    dataRow.createCell(3).setCellValue(clientAwbSeries.getSeriesType() != null ? clientAwbSeries.getSeriesType() + " " : "NA");
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
                .header("Content-Disposition", "attachment; filename="+"pendingAwbSeries"+ProjectConstant.EXTENSION_XLS)
                .body(new ByteArrayResource(byteArray));
    }
}
