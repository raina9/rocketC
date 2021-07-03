package com.weblearnex.app.service.impl;

import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.ProjectConstant;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.constant.UserType;
import com.weblearnex.app.datatable.reposatory.ClientRepository;
import com.weblearnex.app.datatable.reposatory.ClientWarehouseRepository;
import com.weblearnex.app.entity.setup.Branch;
import com.weblearnex.app.entity.setup.ClientWarehouse;
import com.weblearnex.app.entity.setup.Configration;
import com.weblearnex.app.entity.setup.User;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.model.SessionUserBean;
import com.weblearnex.app.reposatory.StateRepository;
import com.weblearnex.app.service.ClientWarehouseService;
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

import javax.persistence.Column;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class ClientWarehouseServiceImpl implements ClientWarehouseService {

    Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ClientWarehouseRepository clientWarehouseRepository;

    @Autowired
    private StateRepository sateRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private SessionUserBean sessionUserBean;



    @Override
    public ResponseBean<ClientWarehouse> addClientWarehouse(ClientWarehouse clientWarehouse) {
        ResponseBean responseBean = new ResponseBean();
        if (clientWarehouse.getWarehouseName() == null || clientWarehouse.getWarehouseName().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_WAREHOUSE_NAME_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (clientWarehouse.getWarehouseCode() == null || clientWarehouse.getWarehouseCode().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Client warehouse code is empty.");
            return responseBean;
        }
        if (clientWarehouseRepository.findByWarehouseCode(clientWarehouse.getWarehouseCode()) != null) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_WAREHOUSE_CODE_ALREADY_EXIST_MSG, null, null));
            return responseBean;
        }
        if (clientWarehouse.getContactNumber() == null || clientWarehouse.getContactNumber().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CONTACT_NUMBER_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (clientWarehouse.getState() == null || clientWarehouse.getState().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_WAREHOUSE_STATE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (clientWarehouse.getCity() == null || clientWarehouse.getCity().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_WAREHOUSE_CITY_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (clientWarehouse.getCountry() == null || clientWarehouse.getCountry().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_WAREHOUSE_COUNTRY_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (clientWarehouse.getContactPersonName() == null || clientWarehouse.getContactPersonName().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CONTACT_PERSON_NAME_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (clientWarehouse.getClientCode() == null || clientWarehouse.getClientCode().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Client code is empty.");
            return responseBean;
        }
        if(clientRepository.findByClientCode(clientWarehouse.getClientCode()) == null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Client code is incorrect, not found in system.");
            return responseBean;
        }
        clientWarehouse = clientWarehouseRepository.save(clientWarehouse);
        if (clientWarehouse.getId() != null) {
            responseBean.setResponseBody(clientWarehouse);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_WAREHOUSE_ADDED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_WAREHOUSE_ADDED_ERROR_MSG, null, null));
        }
        return responseBean;
    }


    @Override
    public ResponseBean<ClientWarehouse> updateClientWarehouse(ClientWarehouse clientWarehouse) {
        ResponseBean responseBean = new ResponseBean();
        if (clientWarehouse.getWarehouseName() == null || clientWarehouse.getWarehouseName().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_WAREHOUSE_NAME_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (clientWarehouse.getWarehouseCode() == null || clientWarehouse.getWarehouseCode().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Client warehouse code is empty.");
            return responseBean;
        }
        Optional<ClientWarehouse> warehouseDB = clientWarehouseRepository.findById(clientWarehouse.getId());
        if (!warehouseDB.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Warehouse code not present in system.");
            return responseBean;
        }
        // Upade actual code if chaged from ui side.
        clientWarehouse.setWarehouseCode(warehouseDB.get().getWarehouseCode());

        if (clientWarehouse.getContactNumber() == null || clientWarehouse.getContactNumber().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CONTACT_NUMBER_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (clientWarehouse.getContactPersonName() == null || clientWarehouse.getContactPersonName().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CONTACT_PERSON_NAME_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (clientWarehouse.getState() == null || clientWarehouse.getState().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_WAREHOUSE_STATE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (clientWarehouse.getCity() == null || clientWarehouse.getCity().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_WAREHOUSE_CITY_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (clientWarehouse.getCountry() == null || clientWarehouse.getCountry().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_WAREHOUSE_COUNTRY_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (clientWarehouse.getClientCode() == null || clientWarehouse.getClientCode().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Client code is empty.");
            return responseBean;
        }
        if(clientRepository.findByClientCode(clientWarehouse.getClientCode()) == null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Client code is incorrect, not found in system.");
            return responseBean;
        }

        clientWarehouse = clientWarehouseRepository.save(clientWarehouse);
        if (clientWarehouse != null) {
            responseBean.setResponseBody(clientWarehouse);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_WAREHOUSE_UPDATED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_WAREHOUSE_UPDATED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<ClientWarehouse> deleteClientWarehouse(Long clientWarehouseId) {

        ResponseBean responseBean = new ResponseBean();
        Optional<ClientWarehouse> clientWarehouse = clientWarehouseRepository.findById(clientWarehouseId);
        if (!clientWarehouse.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_WAREHOUSE_ID_NOT_FOUND, null, null));
            return responseBean;
        }

        clientWarehouse.get().setActive(AppProperty.IN_ACTIVE);
        clientWarehouseRepository.save(clientWarehouse.get());
        responseBean.setResponseBody(clientWarehouse.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_WAREHOUSE_DELETE_MSG, null, null));
        return responseBean;
    }

    @Override
    public ResponseBean<ClientWarehouse> getAllClientWarehouse() {
        ResponseBean responseBean = new ResponseBean();
        List<ClientWarehouse> clientWarehouseList = (List<ClientWarehouse>) clientWarehouseRepository.findAll();
        if(clientWarehouseList !=null && !clientWarehouseList.isEmpty()){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG, null, null));
            responseBean.setResponseBody(clientWarehouseList);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<ClientWarehouse> findByClientWarehouse(Long id) {
        ResponseBean responseBean = new ResponseBean();
        Optional<ClientWarehouse> status = clientWarehouseRepository.findById(id);
        if (!status.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_WAREHOUSE_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        responseBean.setResponseBody(status.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_WAREHOUSE_ID_FOUND, null, null));
        return responseBean;
    }

    @Override
    public DataTablesOutput<ClientWarehouse> getAllClientWarehousePaginationAndSort(DataTablesInput input) {
        User currentLoginUser = sessionUserBean.getUser();
        if(UserType.CLIENT.equals(currentLoginUser.getType())){
            input.addColumn("clientCode", true, false, currentLoginUser.getClientCode());
        }
        return clientWarehouseRepository.findAll(input);
    }

    @Override
    public ResponseBean<List<ClientWarehouse>> getAllClientWarehouseByClientCode(String clientCode) {
        ResponseBean responseBean = new ResponseBean();
        try {
            List<ClientWarehouse> warehouseList = clientWarehouseRepository.findAllByClientCode(clientCode);
            if(warehouseList == null || warehouseList.isEmpty()){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("No Warehouse found.");
                return responseBean;
            }
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setResponseBody(warehouseList);
        }catch (Exception e){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Servier internal error.");
            e.printStackTrace();
        }
        return responseBean;
    }

    @Override
    public ResponseBean<List<ClientWarehouse>> getLoginUserWarehouses() {
        ResponseBean responseBean = new ResponseBean();
        try{
            User user = sessionUserBean.getUser();
            if(UserType.CLIENT.equals(user.getType())){
                responseBean =  getAllClientWarehouseByClientCode(user.getClientCode());
                return responseBean;
            }else {
                Iterable<ClientWarehouse> clientWarehouseList = clientWarehouseRepository.findAll();
                if(clientWarehouseList != null){
                    Iterator<ClientWarehouse> iterator = clientWarehouseList.iterator();
                    List<ClientWarehouse> warehouseList = new ArrayList<ClientWarehouse>();
                    while (iterator.hasNext()){
                        ClientWarehouse clientWarehouse = iterator.next();
                        clientWarehouse.setWarehouseName(clientWarehouse.getWarehouseName()+" ("+clientWarehouse.getClientCode()+")");
                        warehouseList.add(clientWarehouse);
                    }
                    responseBean.setResponseBody(warehouseList);
                }
                responseBean.setStatus(ResponseStatus.SUCCESS);
            }
        }catch(Exception e){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Server internal error.");
            e.printStackTrace();
        }
        return responseBean;
    }

    @Override
    public ResponseEntity<Resource> clientWarehouseReport() {
        List<ClientWarehouse> clientWarehouseList = (List<ClientWarehouse>) clientWarehouseRepository.findAll();
        if(clientWarehouseList == null){
            return null;
        }
        SXSSFWorkbook xlsWorkbook = new SXSSFWorkbook();
        SXSSFSheet xlsSheet = (SXSSFSheet) xlsWorkbook.createSheet();
        int rowIndex = ProjectConstant.ZERO;
        SXSSFRow titleRow = (SXSSFRow) xlsSheet.createRow(rowIndex++);
        titleRow.createCell(0).setCellValue("CLIENT_CODE");
        titleRow.createCell(1).setCellValue("WAREHOUSE_CODE");
        titleRow.createCell(2).setCellValue("WAREHOUSE_NAME");
        titleRow.createCell(3).setCellValue("CONTACT_PERSON_NAME");
        titleRow.createCell(4).setCellValue("CONTACT_NUMBER");
        titleRow.createCell(5).setCellValue("ALTERNATE_CONTACT");
        titleRow.createCell(6).setCellValue("EMAIL");
        titleRow.createCell(7).setCellValue("ADDRESS");
        titleRow.createCell(8).setCellValue("PINCODE");
        titleRow.createCell(9).setCellValue("STATE");
        titleRow.createCell(10).setCellValue("CITY");
        titleRow.createCell(11).setCellValue("COUNTRY");
        titleRow.createCell(12).setCellValue("ACTIVE");

        if(clientWarehouseList != null && clientWarehouseList.size() > ProjectConstant.ZERO){
            for (ClientWarehouse clientWarehouse : clientWarehouseList) {
                SXSSFRow dataRow = (SXSSFRow)xlsSheet.createRow(rowIndex++);
                dataRow.createCell(0).setCellValue(clientWarehouse.getClientCode() != null ? clientWarehouse.getClientCode():"NA");
                dataRow.createCell(1).setCellValue(clientWarehouse.getWarehouseCode() != null ? clientWarehouse.getWarehouseCode():"NA");
                dataRow.createCell(2).setCellValue(clientWarehouse.getWarehouseName() != null ? clientWarehouse.getWarehouseName():"NA");
                dataRow.createCell(3).setCellValue(clientWarehouse.getContactPersonName() != null ? clientWarehouse.getContactPersonName():"NA");
                dataRow.createCell(4).setCellValue(clientWarehouse.getContactNumber() != null ? clientWarehouse.getContactNumber():"NA");
                dataRow.createCell(5).setCellValue(clientWarehouse.getAlternateContact() != null ? clientWarehouse.getAlternateContact():"NA");
                dataRow.createCell(6).setCellValue(clientWarehouse.getEmail() != null ? clientWarehouse.getEmail():"NA");
                dataRow.createCell(7).setCellValue(clientWarehouse.getAddress()!= null ? clientWarehouse.getAddress():"NA");
                dataRow.createCell(8).setCellValue(clientWarehouse.getPinCode() != null ? clientWarehouse.getPinCode():"NA");
                dataRow.createCell(9).setCellValue(clientWarehouse.getState() != null ? clientWarehouse.getState():"NA");
                dataRow.createCell(10).setCellValue(clientWarehouse.getCity() != null ? clientWarehouse.getCity():"NA");
                dataRow.createCell(11).setCellValue(clientWarehouse.getCountry() != null ? clientWarehouse.getCountry():"NA");
                dataRow.createCell(12).setCellValue(clientWarehouse.getActive() != null ? clientWarehouse.getActive().toString():"NA");
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
                .header("Content-Disposition", "attachment; filename="+"clientWarehouseReport"+ProjectConstant.EXTENSION_XLS)
                .body(new ByteArrayResource(byteArray));
    }
}
