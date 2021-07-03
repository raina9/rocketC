package com.weblearnex.app.service.impl;

import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.*;
import com.weblearnex.app.entity.master.BulkMaster;
import com.weblearnex.app.entity.master.DomasticRateCard;
import com.weblearnex.app.entity.master.FlatFreight;
import com.weblearnex.app.entity.master.RateMatrix;
import com.weblearnex.app.entity.order.PacketHistory;
import com.weblearnex.app.entity.order.SaleOrder;
import com.weblearnex.app.entity.setup.Branch;
import com.weblearnex.app.entity.setup.Client;
import com.weblearnex.app.entity.setup.Courier;
import com.weblearnex.app.model.BulkUploadBean;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.datatable.reposatory.BranchRepository;
import com.weblearnex.app.reposatory.PincodeRepository;
import com.weblearnex.app.service.BranchService;
import com.weblearnex.app.service.BulkMasterService;
import com.weblearnex.app.service.BulkUploadService;
import com.weblearnex.app.service.PincodeService;
import com.weblearnex.app.utils.SharedMethords;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BranchServiceImpl implements BranchService {
    private static String regex_mobile = "^[0-9][0-9]{9}$";
    private static String regex_pincode = "^[0-9]{6}$";
    private static String regex_email = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private BulkMasterService bulkMasterService;

    @Autowired
    private PincodeService pincodeService;

    @Autowired
    private PincodeRepository pincodeRepository;

    @Override
    public ResponseBean<Branch> addBranch(Branch branch) {
        ResponseBean responseBean = new ResponseBean();
        Branch b=branchRepository.findByName(branch.getName());
        if (branch.getName() == null || branch.getName().isEmpty()|| b!=null ){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.BRANCH_ALREADY_EXIST_MSG, null, null));
            return responseBean;
        }
        if (branch.getBranchCode() == null || branch.getBranchCode().isEmpty() || branchRepository.findByBranchCode(branch.getBranchCode()) != null) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.BRANCH_CODE_EXIST_MSG, null, null));
            return responseBean;
        }
        if (branch.getAddress() == null || branch.getAddress().isEmpty() ){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Address name is empty.");
            return responseBean;
        }
        if (branch.getMobileNo() == null || branch.getMobileNo().isEmpty() ) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Mobile number  is empty");
            return responseBean;
        }
        if (branch.getPincode() == null || branch.getPincode().isEmpty() ){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Pincode name is empty.");
            return responseBean;
        }
        if (branch.getCity() == null || branch.getCity().isEmpty() ) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("City number  is empty");
            return responseBean;
        }
        if (branch.getState() == null || branch.getState().isEmpty() ){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("State name is empty.");
            return responseBean;
        }


        branch = branchRepository.save(branch);
        if (branch.getId() != null) {
            responseBean.setResponseBody(branch);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.BRANCH_ADDED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.BRANCH_ADDED_ERROR_MSG, null, null));
        }
        return responseBean;

    }

    @Override
    public ResponseBean<Branch> updateBranch(Branch branch) {
        ResponseBean responseBean = new ResponseBean();

        if (branch.getId() == null || !branchRepository.findById(branch.getId()).isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.BRANCH_ID_DOES_NOT_EMPTY_OR_NO_DATA_FOUND_IN_DATABASE, null, null));
            return responseBean;
        }
        if (branch.getName() == null || branch.getName().isEmpty() ){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Branch name is empty.");
            return responseBean;
        }
        if (branch.getBranchCode() == null || branch.getBranchCode().isEmpty() ) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Branch code is empty");
            return responseBean;
        }
        if (branch.getAddress() == null || branch.getAddress().isEmpty() ){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Address name is empty.");
            return responseBean;
        }
        if (branch.getMobileNo() == null || branch.getMobileNo().isEmpty() ) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Mobile number  is empty");
            return responseBean;
        }
        if (branch.getPincode() == null || branch.getPincode().isEmpty() ){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Pincode name is empty.");
            return responseBean;
        }
        if (branch.getCity() == null || branch.getCity().isEmpty() ) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("City number  is empty");
            return responseBean;
        }
        if (branch.getState() == null || branch.getState().isEmpty() ){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("State name is empty.");
            return responseBean;
        }
        branch = branchRepository.save(branch);
        if (branch != null) {
            responseBean.setResponseBody(branch);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.BRANCH_UPDATED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.BRANCH_UPDATED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<Branch> deleteBranch(Long id) {
        ResponseBean responseBean = new ResponseBean();
        Optional<Branch> branch = branchRepository.findById(id);
        if (!branch.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.BRANCH_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        branch.get().setActive(AppProperty.IN_ACTIVE);
        branchRepository.save(branch.get());
        responseBean.setResponseBody(branch.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.BRANCH_DELETE_MSG, null, null));
        return responseBean;
    }

    @Override
    public ResponseBean<Branch> getAllBranch() {
        ResponseBean responseBean = new ResponseBean();
        List<Branch> BranchList = (List<Branch>) branchRepository.findAll();
        if(BranchList !=null && !BranchList.isEmpty()){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG, null, null));
            responseBean.setResponseBody(BranchList);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<Branch> findByBranchId(Long id) {
        ResponseBean responseBean = new ResponseBean();
        Optional<Branch> branch = branchRepository.findById(id);
        if (!branch.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.BRANCH_ID_NOT_FOUND, null, null));
            return responseBean;
        }

        responseBean.setResponseBody(branch.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.BRANCH_ID_FOUND, null, null));
        return responseBean;
    }

    @Override
    public DataTablesOutput<Branch> getAllBranchPaginationAndSort(DataTablesInput input) {
        return branchRepository.findAll(input);
    }

    @Override
    public ResponseBean<List<Branch>> getAllActiveBranch() {
        ResponseBean responseBean = new ResponseBean();
        List<Branch> branchList =branchRepository.findByActive(1);
        if(branchList != null && !branchList.isEmpty()){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG,null,null));
            responseBean.setResponseBody(branchList);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG,null,null));
        }
        return responseBean;
    }

    @Override
    public ResponseEntity<Resource> branchReport() {
        List<Branch> BranchList = (List<Branch>) branchRepository.findAll();
        if(BranchList == null){
            return null;
        }
        SXSSFWorkbook xlsWorkbook = new SXSSFWorkbook();
        SXSSFSheet xlsSheet = (SXSSFSheet) xlsWorkbook.createSheet();
        int rowIndex = ProjectConstant.ZERO;
        SXSSFRow titleRow = (SXSSFRow) xlsSheet.createRow(rowIndex++);
        titleRow.createCell(0).setCellValue("BRANCH_NAME");
        titleRow.createCell(1).setCellValue("BRANCH_CODE");
        titleRow.createCell(2).setCellValue("ADDRESS");
        titleRow.createCell(3).setCellValue("MOBILE_NUMBER");
        titleRow.createCell(4).setCellValue("EMAIL_ID");
        titleRow.createCell(5).setCellValue("PINCODE");
        titleRow.createCell(6).setCellValue("CITY");
        titleRow.createCell(7).setCellValue("STATE");
        titleRow.createCell(8).setCellValue("COUNTRY");
        titleRow.createCell(9).setCellValue("PARENT_BRANCH_ID");
        titleRow.createCell(10).setCellValue("LAT_LONG");
        titleRow.createCell(11).setCellValue("DESTINATION_BAGGING_ALLOW");
        titleRow.createCell(12).setCellValue("MANUL_MANIFEST_ALLOW");
        titleRow.createCell(13).setCellValue("ACTIVE");
        titleRow.createCell(14).setCellValue("CREATE_BY");
        titleRow.createCell(15).setCellValue("UPDATE_BY");
        titleRow.createCell(16).setCellValue("CREATE_DATE");
        titleRow.createCell(17).setCellValue("UPDATE_DATE");

        if(BranchList != null && BranchList.size() > ProjectConstant.ZERO){
            for (Branch branch : BranchList) {
                SXSSFRow dataRow = (SXSSFRow)xlsSheet.createRow(rowIndex++);
                dataRow.createCell(0).setCellValue(branch.getName() != null ? branch.getName():"NA");
                dataRow.createCell(1).setCellValue(branch.getBranchCode() != null ? branch.getBranchCode():"NA");
                dataRow.createCell(2).setCellValue(branch.getAddress() != null ? branch.getAddress():"NA");
                dataRow.createCell(3).setCellValue(branch.getMobileNo() != null ? branch.getMobileNo():"NA");
                dataRow.createCell(4).setCellValue(branch.getEmailId() != null ? branch.getEmailId():"NA");
                dataRow.createCell(5).setCellValue(branch.getPincode() != null ? branch.getPincode():"NA");
                dataRow.createCell(6).setCellValue(branch.getCity() != null ? branch.getCity():"NA");
                dataRow.createCell(7).setCellValue(branch.getState()!= null ? branch.getState():"NA");
                dataRow.createCell(8).setCellValue(branch.getCountry() != null ? branch.getCountry():"NA");
                dataRow.createCell(9).setCellValue(branch.getParentBranchId() != null ? branch.getParentBranchId():"NA");
                dataRow.createCell(10).setCellValue(branch.getLatLong() != null ? branch.getLatLong():"NA");
                dataRow.createCell(11).setCellValue(branch.getDestinationBaggingAllow() != null ? branch.getDestinationBaggingAllow().toString():"NA");
                dataRow.createCell(12).setCellValue(branch.getManulManifestAllow() != null ? branch.getManulManifestAllow().toString():"NA");
                dataRow.createCell(13).setCellValue(branch.getActive() != null ? branch.getActive().toString():"NA");
                dataRow.createCell(14).setCellValue(branch.getCreateBy() != null ? branch.getCreateBy():"NA");
                dataRow.createCell(15).setCellValue(branch.getUpdateBy() != null ? branch.getUpdateBy():"NA");
                dataRow.createCell(16).setCellValue(branch.getCreateDate() != null ? branch.getCreateDate().toString():"NA");
                dataRow.createCell(17).setCellValue(branch.getUpdateDate() != null ? branch.getUpdateDate().toString():"NA");

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
                .header("Content-Disposition", "attachment; filename="+"branchReport"+ProjectConstant.EXTENSION_XLS)
                .body(new ByteArrayResource(byteArray));

    }
    @Override
    public BulkUploadBean branchBulkUpload(BulkUploadBean bulkUploadBean, BulkMaster bulkMaster, boolean isUpdate) {
        List<Map<String, String>> errorRecord = new ArrayList<Map<String, String>>();
        List<Map<String, String>> successRecord = new ArrayList<Map<String, String>>();
        String token = BulkUploadService.generateRandomString();
        int uploadPersentage = 0;
        int count = 0;
        for(Map<String, String> map : bulkUploadBean.getRecords()){
            count++;
            uploadPersentage =BulkUploadService.calculateUploadPercentage(count, bulkUploadBean.getRecords().size());
            bulkMasterService.setUploadProgressCount(bulkMaster.getName() ,uploadPersentage, token, false);
            Branch branch = new Branch();

            branch.setName((map.get(BulkHeaderConstant.BRANCH_NAME) != null && !map.get(BulkHeaderConstant.BRANCH_NAME).isEmpty()) ? map.get(BulkHeaderConstant.RATE_CARD_CODE).trim() : null);
            branch.setBranchCode((map.get(BulkHeaderConstant.BRANCH_CODE) != null && !map.get(BulkHeaderConstant.BRANCH_CODE).isEmpty()) ? map.get(BulkHeaderConstant.RATE_CARD_NAME).trim() : null);
            branch.setAddress(map.get(BulkHeaderConstant.ADDRESS) != null ? (map.get(BulkHeaderConstant.ADDRESS).trim()) : null);
            branch.setMobileNo(map.get(BulkHeaderConstant.MOBILE_NO) != null ? (map.get((BulkHeaderConstant.MOBILE_NO).trim())) : null);
            branch.setParentBranchId((map.get(BulkHeaderConstant.PARENT_BRANCH_ID) != null && !map.get(BulkHeaderConstant.PARENT_BRANCH_ID).isEmpty()) ? map.get(BulkHeaderConstant.RATE_CARD_CODE).trim() : null);
            branch.setEmailId((map.get(BulkHeaderConstant.EMAIL_ID) != null && !map.get(BulkHeaderConstant.EMAIL_ID).isEmpty()) ? map.get(BulkHeaderConstant.EMAIL_ID).trim() : null);
            branch.setLatLong((map.get(BulkHeaderConstant.LAT_LONG) != null && !map.get(BulkHeaderConstant.LAT_LONG).isEmpty()) ? map.get(BulkHeaderConstant.LAT_LONG).trim() : null);
            if(!branch.getEmailId().matches(regex_email)){
                map.put(BulkHeaderConstant.MESSAGE,"Enter valid email id.");
                errorRecord.add(map);
                continue;
            }
            if(!branch.getMobileNo().matches(regex_mobile)){
                map.put(BulkHeaderConstant.MESSAGE,"Enter valid mobile number.");
                errorRecord.add(map);
                continue;
            }

            branch.setPincode((map.get(BulkHeaderConstant.PIN_CODE) != null && !map.get(BulkHeaderConstant.PIN_CODE).isEmpty()) ? map.get(BulkHeaderConstant.RATE_CARD_TYPE).trim() : null);
            if(branch.getPincode() == null || branch.getPincode().isEmpty()){
                map.put(BulkHeaderConstant.MESSAGE,"pin-code can not be blank.");
                errorRecord.add(map);
                continue;

            }
            if(!branch.getPincode().matches(regex_pincode)){
                map.put(BulkHeaderConstant.MESSAGE,"Enter valid pin-code.");
                errorRecord.add(map);
                continue;
            }
            if(!pincodeRepository.existsByPinCode(branch.getPincode())){
                map.put(BulkHeaderConstant.MESSAGE,"Pincode not found in system.");
                errorRecord.add(map);
                continue;
            }
            ResponseBean<Map<String,String>> pincodeResponse = pincodeService.getCityStateCountryByPincode(branch.getPincode());
            if(pincodeResponse.getStatus().equals(ResponseStatus.SUCCESS)){
                branch.setCity(pincodeResponse.getResponseBody().get("COUNTRY_NAME"));
                branch.setState(pincodeResponse.getResponseBody().get("STATE_NAME"));
                branch.setCountry(pincodeResponse.getResponseBody().get("CITY_NAME"));
            }

            Boolean destinationBaggingAllow = Boolean.valueOf(map.get(BulkHeaderConstant.DESTINATION_BAGGING_ALLOW).trim());
            Boolean manulManifestAllow = Boolean.valueOf(map.get(BulkHeaderConstant.MANUL_MANIFEST_ALLOW).trim());
            if(destinationBaggingAllow == null ){
                branch.setDestinationBaggingAllow(false);
            } else {
                branch.setDestinationBaggingAllow(true);
            }
            if( manulManifestAllow == null){
                branch.setManulManifestAllow(false);
            } else {
                branch.setManulManifestAllow(true);
            }

            if(isUpdate){
                String id =  (map.get(BulkHeaderConstant.ID) != null && !map.get(BulkHeaderConstant.ID).isEmpty()) ? map.get(BulkHeaderConstant.ID).trim() : null;
                if(id==null|| id.trim().isEmpty()){
                    map.put(BulkHeaderConstant.MESSAGE,"Invalid ID, Please provide valid branch id.");
                    errorRecord.add(map);
                    continue;
                }
                try{
                    Long objectId = Long.valueOf(id);
                    Optional<Branch> branchDb = branchRepository.findById(objectId);
                    if(!branchDb.isPresent()){
                        map.put(BulkHeaderConstant.MESSAGE,"Invalid ID, Please provide valid branch id.");
                        errorRecord.add(map);
                        continue;
                    }
                    ResponseBean responseBean = updateBranch(branch);
                    if(ResponseStatus.FAIL.equals(responseBean.getStatus())){
                        map.put(BulkHeaderConstant.MESSAGE,responseBean.getMessage());
                        errorRecord.add(map);
                        continue;
                    }
                }catch (Exception e){
                    map.put(BulkHeaderConstant.MESSAGE,"server internal error.");
                    errorRecord.add(map);
                    continue;
                }
            }
            else{
                ResponseBean responseBean = addBranch(branch);
                if(ResponseStatus.FAIL.equals(responseBean.getStatus())){
                    map.put(BulkHeaderConstant.MESSAGE,responseBean.getMessage());
                    errorRecord.add(map);
                    continue;
                }
            }

            successRecord.add(map);
        }
        bulkMasterService.setUploadProgressCount(bulkMaster.getName() ,100, token, true);

        bulkUploadBean.setBulkMaster(bulkMaster);
        bulkUploadBean.setErrorRecords(errorRecord);
        bulkUploadBean.setSuccessRecords(successRecord);
        return bulkUploadBean;
    }

    @Override
    public BulkUploadBean uploadBulk(BulkUploadBean bulkUploadBean, BulkMaster bulkMaster) {
        return null;
    }
}
