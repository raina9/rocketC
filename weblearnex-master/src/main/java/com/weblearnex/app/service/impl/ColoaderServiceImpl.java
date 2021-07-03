package com.weblearnex.app.service.impl;

import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.ProjectConstant;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.entity.setup.Coloader;
import com.weblearnex.app.entity.setup.Courier;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.datatable.reposatory.ColoaderRepository;
import com.weblearnex.app.reposatory.StateRepository;
import com.weblearnex.app.service.ColoaderService;
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
import java.util.List;
import java.util.Optional;

@Service
public class ColoaderServiceImpl implements ColoaderService {

    Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ColoaderRepository coloaderRepository;

    @Autowired
    private StateRepository sateRepository;

    @Autowired
    private MessageSource messageSource;


    @Override
    public ResponseBean<Coloader> addColoader(Coloader coloader) {
        ResponseBean responseBean = new ResponseBean();
        if (coloader.getColoaderName() == null || coloader.getColoaderName().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COLOADER_NAME_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (coloader.getColoaderCode() == null || coloader.getColoaderCode().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COLOADER_CODE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (coloader.getRegisteredAdd() == null || coloader.getRegisteredAdd().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COLOADER_REGISTER_ADD_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (coloader.getPincode() == null || coloader.getPincode().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COLOADER_PIN_CODE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (coloader.getContactPerson() == null || coloader.getContactPerson().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COLOADER_CONTACT_PERSON_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (coloader.getMobile() == null || coloader.getMobile().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COLOADER_MOBILE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if(coloader.getCity()==null ||coloader.getCity().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("City can not empty.");
            return responseBean;
        }
        if(coloader.getState()==null ||coloader.getState().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("State can not empty.");
            return responseBean;
        }
        if(coloader.getCountry()==null ||coloader.getCountry().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Country can not empty.");
            return responseBean;
        }
        if (coloader.getEmail() == null || coloader.getEmail().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COLOADER_EMAIL_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        coloader = coloaderRepository.save(coloader);
        if (coloader.getId() != null) {
            responseBean.setResponseBody(coloader);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COLOADER_ADDED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COLOADER_ADDED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<Coloader> updateColoader(Coloader coloader) {
        ResponseBean responseBean = new ResponseBean();

        if (coloader.getId() == null || !coloaderRepository.findById(coloader.getId()).isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COLOADER_ID_NOT_FOUND, null, null));
            return responseBean;
        }

        if (coloader.getColoaderName() == null || coloader.getColoaderName().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COLOADER_NAME_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (coloader.getColoaderCode() == null || coloader.getColoaderCode().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COLOADER_CODE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (coloader.getRegisteredAdd() == null || coloader.getRegisteredAdd().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COLOADER_REGISTER_ADD_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if(coloader.getCity()==null ||coloader.getCity().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("City can not empty.");
            return responseBean;
        }
        if(coloader.getState()==null ||coloader.getState().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("State can not empty.");
            return responseBean;
        }
        if(coloader.getCountry()==null ||coloader.getCountry().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Country can not empty.");
            return responseBean;
        }
        if (coloader.getPincode() == null || coloader.getPincode().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COLOADER_PIN_CODE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (coloader.getContactPerson() == null || coloader.getContactPerson().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COLOADER_CONTACT_PERSON_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (coloader.getMobile() == null || coloader.getMobile().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COLOADER_MOBILE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (coloader.getEmail() == null || coloader.getEmail().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COLOADER_EMAIL_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        coloaderRepository.save(coloader);
        if (coloader != null) {
            responseBean.setResponseBody(coloader);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COLOADER_UPDATED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COLOADER_UPDATED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<Coloader> deleteColoader(Long coloaderId) {
        ResponseBean responseBean = new ResponseBean();
        Optional<Coloader> coloader= coloaderRepository.findById(coloaderId);
        if(!coloader.isPresent()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COLOADER_ID_NOT_FOUND,null,null));
            return responseBean;
        }
        coloader.get().setActive(AppProperty.IN_ACTIVE);
        coloaderRepository.save( coloader.get());
        responseBean.setResponseBody(coloader);
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.COLOADER_DELETE_MSG,null,null));
        return responseBean;
    }


    @Override
    public ResponseBean<Coloader> getAllColoader() {
        ResponseBean responseBean = new ResponseBean();
        List<Coloader> coloaderList = (List<Coloader>) coloaderRepository.findAll();
        if(coloaderList !=null && !coloaderList.isEmpty()){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG, null, null));
            responseBean.setResponseBody(coloaderList);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<Coloader> findByColoaderId(Long id) {
        ResponseBean responseBean = new ResponseBean();
        Optional<Coloader> status = coloaderRepository.findById(id);
        if (!status.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COLOADER_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        responseBean.setResponseBody(status.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.COLOADER_ID_FOUND, null, null));
        return responseBean;

    }

    @Override
    public DataTablesOutput<Coloader> getAllColoaderPaginationAndSort(DataTablesInput input) {
        return coloaderRepository.findAll(input);
    }

    @Override
    public ResponseEntity<Resource> coloaderReport() {
        List<Coloader> coloaderList = (List<Coloader>) coloaderRepository.findAll();
        if(coloaderList == null){
            return null;
        }
        SXSSFWorkbook xlsWorkbook = new SXSSFWorkbook();
        SXSSFSheet xlsSheet = (SXSSFSheet) xlsWorkbook.createSheet();
        int rowIndex = ProjectConstant.ZERO;
        SXSSFRow titleRow = (SXSSFRow) xlsSheet.createRow(rowIndex++);
        titleRow.createCell(0).setCellValue("COLOADER_NAME");
        titleRow.createCell(1).setCellValue("COLOADER_CODE");
        titleRow.createCell(2).setCellValue("ADDRESS");
        titleRow.createCell(3).setCellValue("MOBILE_NUMBER");
        titleRow.createCell(4).setCellValue("EMAIL_ID");
        titleRow.createCell(5).setCellValue("PINCODE");
        titleRow.createCell(6).setCellValue("CITY");
        titleRow.createCell(7).setCellValue("STATE");
        titleRow.createCell(8).setCellValue("COUNTRY");
        titleRow.createCell(9).setCellValue("CONTACT_PERSON");
        titleRow.createCell(10).setCellValue("BENEFICIRY");
        titleRow.createCell(11).setCellValue("ACCOUNT_NUMBER");
        titleRow.createCell(12).setCellValue("BANK_NAME");
        titleRow.createCell(13).setCellValue("IFSC_CODE");
        titleRow.createCell(14).setCellValue("GST_NUMBER");
        titleRow.createCell(15).setCellValue("ACTIVE");
        titleRow.createCell(16).setCellValue("CREATE_BY");
        titleRow.createCell(17).setCellValue("UPDATE_BY");
        titleRow.createCell(18).setCellValue("CREATE_DATE");
        titleRow.createCell(29).setCellValue("UPDATE_DATE");

        if(coloaderList != null && coloaderList.size() > ProjectConstant.ZERO){
            for (Coloader coloader : coloaderList) {
                SXSSFRow dataRow = (SXSSFRow)xlsSheet.createRow(rowIndex++);
                dataRow.createCell(0).setCellValue(coloader.getColoaderName() != null ? coloader.getColoaderName():"NA");
                dataRow.createCell(1).setCellValue(coloader.getColoaderCode() != null ? coloader.getColoaderCode():"NA");
                dataRow.createCell(2).setCellValue(coloader.getRegisteredAdd() != null ? coloader.getRegisteredAdd():"NA");
                dataRow.createCell(3).setCellValue(coloader.getMobile() != null ? coloader.getMobile():"NA");
                dataRow.createCell(4).setCellValue(coloader.getEmail() != null ? coloader.getEmail():"NA");
                dataRow.createCell(5).setCellValue(coloader.getPincode() != null ? coloader.getPincode():"NA");
                dataRow.createCell(6).setCellValue(coloader.getCity() != null ? coloader.getCity():"NA");
                dataRow.createCell(7).setCellValue(coloader.getState()!= null ? coloader.getState():"NA");
                dataRow.createCell(8).setCellValue(coloader.getCountry() != null ? coloader.getCountry():"NA");
                dataRow.createCell(9).setCellValue(coloader.getContactPerson() != null ? coloader.getContactPerson():"NA");
                dataRow.createCell(10).setCellValue(coloader.getBeneficiry() != null ? coloader.getBeneficiry():"NA");
                dataRow.createCell(11).setCellValue(coloader.getAccountNo() != null ? coloader.getAccountNo():"NA");
                dataRow.createCell(12).setCellValue(coloader.getBankName() != null ? coloader.getBankName():"NA");
                dataRow.createCell(13).setCellValue(coloader.getIfscCode() != null ? coloader.getIfscCode():"NA");

                dataRow.createCell(14).setCellValue(coloader.getGstNumber() != null ? coloader.getGstNumber():"NA");
                dataRow.createCell(25).setCellValue(coloader.getActive() != null ? coloader.getActive().toString():"NA");
                dataRow.createCell(26).setCellValue(coloader.getCreateBy() != null ? coloader.getCreateBy():"NA");
                dataRow.createCell(27).setCellValue(coloader.getUpdateBy() != null ? coloader.getUpdateBy():"NA");
                dataRow.createCell(28).setCellValue(coloader.getCreateDate() != null ? coloader.getCreateDate().toString():"NA");
                dataRow.createCell(29).setCellValue(coloader.getUpdateDate() != null ? coloader.getUpdateDate().toString():"NA");


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
                .header("Content-Disposition", "attachment; filename="+"coloaderReport"+ProjectConstant.EXTENSION_XLS)
                .body(new ByteArrayResource(byteArray));

    }
}
