package com.weblearnex.app.service.impl;

import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.ProjectConstant;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.constant.UserType;
import com.weblearnex.app.datatable.reposatory.ClientFacilityRepository;
import com.weblearnex.app.entity.manifest.ThreeplManifest;
import com.weblearnex.app.entity.setup.Branch;
import com.weblearnex.app.entity.setup.Client;
import com.weblearnex.app.entity.setup.ClientFacility;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.datatable.reposatory.ClientRepository;
import com.weblearnex.app.model.SessionUserBean;
import com.weblearnex.app.service.ClientService;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

    Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    @Qualifier("applicionConfig")
    private MessageSource applicionConfig;

    @Autowired
    private ClientFacilityRepository clientFacilityRepository;

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private SessionUserBean sessionUserBean;


   /* @Override
    public ResponseBean<Client> addClient(Client client,MultipartFile aadharCard,MultipartFile panCard,MultipartFile gst,MultipartFile agreement) {
        ResponseBean responseBean = new ResponseBean();
        if(client.getClientCode()==null ||client.getClientCode().isEmpty()||
            clientRepository.findByClientCode(client.getClientCode())!=null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_ALREADY_EXIST_MSG,null,null));
            return responseBean;
        }
        if(client.getClientName()==null ||client.getClientName().isEmpty()||
                clientRepository.findByClientName(client.getClientName())!=null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_Name_EXIST_MSG,null,null));
            return responseBean;
        }
        if(client.getCity()==null ||client.getCity().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("City can not empty.");
            return responseBean;
        }
        if(client.getState()==null ||client.getState().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("State can not empty.");
            return responseBean;
        }
        if(client.getCountry()==null ||client.getCountry().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Country can not empty.");
            return responseBean;
        }

        if(client.getCountry()==null ||client.getCountry().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Country can not empty.");
            return responseBean;
        }
        if(client.getPanNumber()==null ||client.getPanNumber().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Pan Number can not empty.");
            return responseBean;
        }
        if(client.getAadhaarNumber()==null ||client.getAadhaarNumber().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Aadhaar number can not empty.");
            return responseBean;
        }

        if(client.getAwbSeriesSequence()==null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Client sequence number can not empty.");
            return responseBean;
        }
        if(client.getAwbSeriesSequence() <= 0){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Sequence number grater than zero.");
            return responseBean;
        }
        if(client.getAwbSeriesPrefix() == null || client.getAwbSeriesPrefix().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Series prefix can not empty.");
            return responseBean;
        }
        if(aadharCard == null || aadharCard.isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Please Select Aadhar Card.");
            return responseBean;
        }
        if(panCard == null || panCard.isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Please Select pan Card.");
            return responseBean;
        }
        if(gst == null || gst.isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Please Select gst.");
            return responseBean;
        }
        if(agreement == null || agreement.isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Please Select  agreement.");
            return responseBean;
        }
        String filePath = applicionConfig.getMessage(AppProperty.DOC_POD_UPLOAD_FILE_PATH, null, null);
        String aadharCardExt = Arrays.stream(aadharCard.getOriginalFilename().split("\\.")).reduce((a, b) -> b).orElse(null);
        String panCardExt = Arrays.stream(panCard.getOriginalFilename().split("\\.")).reduce((a, b) -> b).orElse(null);
        String gstExt = Arrays.stream(gst.getOriginalFilename().split("\\.")).reduce((a, b) -> b).orElse(null);
        String agreementExt = Arrays.stream(gst.getOriginalFilename().split("\\.")).reduce((a, b) -> b).orElse(null);
        File files =  new File(filePath);
        if(!files.isDirectory()){
            files.mkdir();
        }
        //filePath =  filePath + "/"+ client.getClientCode() +"."+ aadharCardExt;


        client.setUploadAadharCard(filePath + "/"+ client.getClientCode() +"/aadharCard."+ aadharCardExt);
        client.setUploadPanCard(filePath + "/"+ client.getClientCode() +"/panCard."+ panCardExt);
        client.setUploadgst(filePath + "/"+ client.getClientCode() +"/gst."+ gstExt);
        client.setUploadgst(filePath + "/"+ client.getClientCode() +"/agreement."+ agreementExt);
        client=clientRepository.save(client);
        if(client.getId()!=null){
            responseBean.setResponseBody(client);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_ADDED_MSG,null,null));
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_ADDED_ERROR_MSG,null,null));
        }
        return responseBean;

    }*/

    @Override
    public ResponseBean<Client> addClient(Client client) {
        ResponseBean responseBean = new ResponseBean();
        if(client.getClientCode()==null ||client.getClientCode().isEmpty()||
                clientRepository.findByClientCode(client.getClientCode())!=null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_ALREADY_EXIST_MSG,null,null));
            return responseBean;
        }
        if(client.getClientName()==null ||client.getClientName().isEmpty()||
                clientRepository.findByClientName(client.getClientName())!=null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_Name_EXIST_MSG,null,null));
            return responseBean;
        }
        if(client.getCity()==null ||client.getCity().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("City can not empty.");
            return responseBean;
        }
        if(client.getState()==null ||client.getState().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("State can not empty.");
            return responseBean;
        }
        if(client.getCountry()==null ||client.getCountry().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Country can not empty.");
            return responseBean;
        }

        if(client.getCountry()==null ||client.getCountry().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Country can not empty.");
            return responseBean;
        }
        if(client.getPanNumber()==null ||client.getPanNumber().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Pan Number can not empty.");
            return responseBean;
        }
        if(client.getAadhaarNumber()==null ||client.getAadhaarNumber().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Aadhaar number can not empty.");
            return responseBean;
        }

        if(client.getAwbSeriesSequence()==null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Client sequence number can not empty.");
            return responseBean;
        }
        if(client.getAwbSeriesSequence() <= 0){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Sequence number grater than zero.");
            return responseBean;
        }
        if(client.getAwbSeriesPrefix() == null || client.getAwbSeriesPrefix().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Series prefix can not empty.");
            return responseBean;
        }
        client=clientRepository.save(client);
        if(client.getId()!=null){
            responseBean.setResponseBody(client);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_ADDED_MSG,null,null));
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_ADDED_ERROR_MSG,null,null));
        }
        return responseBean;

    }

    @Override
    public ResponseBean<Client> updateClient(Client client) {
        ResponseBean responseBean = new ResponseBean();

        if(client.getId()==null||clientRepository.findById(client.getId())==null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG,null,null));
            return responseBean;
        }
        if(client.getCity()==null ||client.getCity().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("City can not empty.");
            return responseBean;
        }
        if(client.getState()==null ||client.getState().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("State can not empty.");
            return responseBean;
        }
        if(client.getCountry()==null ||client.getCountry().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Country can not empty.");
            return responseBean;
        }

        if(client.getCountry()==null ||client.getCountry().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Country can not empty.");
            return responseBean;
        }
        if(client.getPanNumber()==null ||client.getPanNumber().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Pan Number can not empty.");
            return responseBean;
        }
        if(client.getAadhaarNumber()==null ||client.getAadhaarNumber().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Aadhaar number can not empty.");
            return responseBean;
        }

        if(client.getClientName()==null ||client.getClientName().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Client name can not be empty.");
            return responseBean;
        }
        if(client.getAwbSeriesSequence()==null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Client sequence number can not empty.");
            return responseBean;
        }
        if(client.getAwbSeriesSequence() <= 0){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Sequence number grater than zero.");
            return responseBean;
        }
        if(client.getAwbSeriesPrefix() == null || client.getAwbSeriesPrefix().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Series prefix can not empty.");
            return responseBean;
        }
        clientRepository.save(client);
        responseBean.setResponseBody(client);
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_UPDATED_MSG,null,null));
        return responseBean;

    }

    @Override
    public ResponseBean<Client> deleteClient(Long id) {
        ResponseBean responseBean = new ResponseBean();
        Optional<Client> client=clientRepository.findById(id);

        if(!client.isPresent()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_ID_NOT_FOUND,null,null));
            return responseBean;
        }
        client.get().setActive(AppProperty.IN_ACTIVE);
        clientRepository.save(client.get());
        responseBean.setResponseBody(client.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_DELETE_MSG,null,null));
        return responseBean;

    }

    @Override
    public ResponseBean<List<Client>> getAllClient() {
        ResponseBean responseBean = new ResponseBean();
        List<Client> clientList = (List<Client>) clientRepository.findAll();

        if(clientList != null && !clientList.isEmpty()){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG,null,null));
            responseBean.setResponseBody(clientList);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG,null,null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<Client> findById(Long id) {
        ResponseBean responseBean = new ResponseBean();
        Optional<Client> client=clientRepository.findById(id);
        if(!client.isPresent()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_ID_NOT_FOUND,null,null));
            return responseBean;
        }
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG,null,null));
        responseBean.setResponseBody(client.get());

        return responseBean;
    }

    @Override
    public ResponseBean<Client> findByName(String clientName) {
        ResponseBean responseBean = new ResponseBean();
        Client client=clientRepository.findByClientName(clientName);
        if(client != null ){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG,null,null));
            responseBean.setResponseBody(client);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG,null,null));
        }
        return responseBean;
    }

    @Override
    public DataTablesOutput<Client> getAllClientPaginationAndSort(DataTablesInput input) {
        if(sessionUserBean.getUser().getType().equals(UserType.CLIENT)){
            input.addColumn("clientCode", true, false,sessionUserBean.getUser().getClientCode());
        }
        return clientRepository.findAll(input);
    }

    @Override
    public ResponseBean<List<Client>> findByActive() {
        ResponseBean responseBean = new ResponseBean();
        List<Client> clientList = new ArrayList<Client>();
        if(UserType.CLIENT.equals(sessionUserBean.getUser().getType())){
            Client client = clientRepository.findByClientCode(sessionUserBean.getUser().getClientCode());
            if(ProjectConstant.ONE == client.getActive()){
                clientList.add(client);
            }
        }else {
            clientList.addAll(clientRepository.findByActive(1));
        }
        if(clientList != null && !clientList.isEmpty()){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG,null,null));
            responseBean.setResponseBody(clientList);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG,null,null));
        }
        return responseBean;
    }

    @Override
    public ResponseEntity<Resource> clientReport() {
        List<Client> clientList = (List<Client>) clientRepository.findAll();
        if(clientList == null){
            return null;
        }
        SXSSFWorkbook xlsWorkbook = new SXSSFWorkbook();
        SXSSFSheet xlsSheet = (SXSSFSheet) xlsWorkbook.createSheet();
        int rowIndex = ProjectConstant.ZERO;
        SXSSFRow titleRow = (SXSSFRow) xlsSheet.createRow(rowIndex++);
        titleRow.createCell(0).setCellValue("CLIENT_NAME");
        titleRow.createCell(1).setCellValue("CLIENT_CODE");
        titleRow.createCell(2).setCellValue("ADDRESS");
        titleRow.createCell(3).setCellValue("MOBILE_NUMBER");
        titleRow.createCell(4).setCellValue("EMAIL_ID");
        titleRow.createCell(5).setCellValue("PINCODE");
        titleRow.createCell(6).setCellValue("CITY");
        titleRow.createCell(7).setCellValue("STATE");
        titleRow.createCell(8).setCellValue("COUNTRY");
        titleRow.createCell(9).setCellValue("CONTACT_PERSON");
        titleRow.createCell(10).setCellValue("LAT_LONG");
        titleRow.createCell(11).setCellValue("AWB_SERIES_PREFIX");
        titleRow.createCell(12).setCellValue("AWB_SERIES_SEQUENCE");
        titleRow.createCell(13).setCellValue("TOKEN");
        titleRow.createCell(14).setCellValue("BENEFICIRY");
        titleRow.createCell(15).setCellValue("ACCOUNT_NUMBER");
        titleRow.createCell(16).setCellValue("BANK_NAME");
        titleRow.createCell(17).setCellValue("IFSC_CODE");
        titleRow.createCell(18).setCellValue("PAN_NUMDER");
        titleRow.createCell(19).setCellValue("AADHAAR_NUMBER");
        titleRow.createCell(20).setCellValue("GST_NUMBER");
        titleRow.createCell(21).setCellValue("ACCOUNT_MANAGER");
        titleRow.createCell(22).setCellValue("SALE_MANAGER");
        titleRow.createCell(23).setCellValue("ACTIVE");
        titleRow.createCell(24).setCellValue("CREATE_BY");
        titleRow.createCell(25).setCellValue("UPDATE_BY");
        titleRow.createCell(26).setCellValue("CREATE_DATE");
        titleRow.createCell(27).setCellValue("UPDATE_DATE");

        if(clientList != null && clientList.size() > ProjectConstant.ZERO){
            for (Client client : clientList) {
                SXSSFRow dataRow = (SXSSFRow)xlsSheet.createRow(rowIndex++);
                dataRow.createCell(0).setCellValue(client.getClientName() != null ? client.getClientName():"NA");
                dataRow.createCell(1).setCellValue(client.getClientCode() != null ? client.getClientCode():"NA");
                dataRow.createCell(2).setCellValue(client.getRegisteredAddress() != null ? client.getRegisteredAddress():"NA");
                dataRow.createCell(3).setCellValue(client.getMobile() != null ? client.getMobile():"NA");
                dataRow.createCell(4).setCellValue(client.getEmail() != null ? client.getEmail():"NA");
                dataRow.createCell(5).setCellValue(client.getPincode() != null ? client.getPincode():"NA");
                dataRow.createCell(6).setCellValue(client.getCity() != null ? client.getCity():"NA");
                dataRow.createCell(7).setCellValue(client.getState()!= null ? client.getState():"NA");
                dataRow.createCell(8).setCellValue(client.getCountry() != null ? client.getCountry():"NA");
                dataRow.createCell(9).setCellValue(client.getContactPerson() != null ? client.getContactPerson():"NA");
                dataRow.createCell(10).setCellValue(client.getLatLong() != null ? client.getLatLong():"NA");
                dataRow.createCell(11).setCellValue(client.getAwbSeriesPrefix() != null ? client.getAwbSeriesPrefix():"NA");
                dataRow.createCell(12).setCellValue(client.getAwbSeriesSequence() != null ? client.getAwbSeriesSequence().toString():"NA");
                dataRow.createCell(13).setCellValue(client.getToken() != null ? client.getToken():"NA");
                dataRow.createCell(14).setCellValue(client.getBeneficiry() != null ? client.getBeneficiry():"NA");
                dataRow.createCell(15).setCellValue(client.getAccountNo() != null ? client.getAccountNo():"NA");
                dataRow.createCell(16).setCellValue(client.getBankName() != null ? client.getBankName():"NA");
                dataRow.createCell(17).setCellValue(client.getIfscCode() != null ? client.getIfscCode():"NA");

                dataRow.createCell(18).setCellValue(client.getPanNumber() != null ? client.getPanNumber():"NA");
                dataRow.createCell(19).setCellValue(client.getAadhaarNumber() != null ? client.getAadhaarNumber():"NA");
                dataRow.createCell(20).setCellValue(client.getGstNumber() != null ? client.getGstNumber():"NA");
                dataRow.createCell(21).setCellValue(client.getAccountManager() != null ? client.getAccountManager():"NA");
                dataRow.createCell(22).setCellValue(client.getSaleManager() != null ? client.getSaleManager():"NA");
                dataRow.createCell(23).setCellValue(client.getActive() != null ? client.getActive().toString():"NA");
                dataRow.createCell(24).setCellValue(client.getCreateBy() != null ? client.getCreateBy():"NA");
                dataRow.createCell(25).setCellValue(client.getUpdateBy() != null ? client.getUpdateBy():"NA");
                dataRow.createCell(26).setCellValue(client.getCreateDate() != null ? client.getCreateDate().toString():"NA");
                dataRow.createCell(27).setCellValue(client.getUpdateDate() != null ? client.getUpdateDate().toString():"NA");


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
                .header("Content-Disposition", "attachment; filename="+"clientReport"+ProjectConstant.EXTENSION_XLS)
                .body(new ByteArrayResource(byteArray));

    }

    @Override
    public ResponseBean<List<Client>> getActiveWalletClient() {
        ResponseBean responseBean = new ResponseBean();
        try{
        List<ClientFacility> clientFacilities = clientFacilityRepository.findAllByWalletActive(true);
        List<String> list = new ArrayList<String>();
        for(ClientFacility clientFacility : clientFacilities){
            list.add(clientFacility.getClient().getClientCode());
        }
        List<Client> clients = clientRepository.findAllByActiveAndClientCodeIn(1, list);
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setResponseBody(clients);
        }catch (Exception e){
            e.printStackTrace();
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Server internal error.");
        }
        return responseBean;
    }


}
