package com.weblearnex.app.service.impl;

import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.BulkHeaderConstant;
import com.weblearnex.app.constant.ProjectConstant;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.constant.UserType;
import com.weblearnex.app.datatable.reposatory.*;
import com.weblearnex.app.entity.master.BulkMaster;
import com.weblearnex.app.entity.master.ServiceProvider;
import com.weblearnex.app.entity.order.PacketHistory;
import com.weblearnex.app.entity.order.SaleOrder;
import com.weblearnex.app.entity.setup.*;
import com.weblearnex.app.model.BulkUploadBean;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.model.SessionUserBean;
import com.weblearnex.app.reposatory.StateRepository;
import com.weblearnex.app.service.BulkMasterService;
import com.weblearnex.app.service.BulkUploadService;
import com.weblearnex.app.service.ClientCourierWarehouseMappingService;
import com.weblearnex.app.service.SaleOrderService;
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
import java.util.*;

@Service
public class ClientCourierWarehouseMappingServiceImpl implements ClientCourierWarehouseMappingService {

    Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ClientCourierWarehouseMappingRepository clientCourierWarehouseMappingRepository;

    @Autowired
    private StateRepository sateRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private SaleOrderRepository saleOrderRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private SaleOrderService saleOrderService;

    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientWarehouseRepository clientWarehouseRepository;
    @Autowired
    private SessionUserBean sessionUserBean;

    @Autowired
    private BulkMasterService bulkMasterService;

    @Override
    public ResponseBean<ClientCourierWarehouseMapping> addClientCourierWarehouseMapping(ClientCourierWarehouseMapping clientCourierWarehouseMapping) {
        ResponseBean responseBean = new ResponseBean();
        if (clientCourierWarehouseMapping.getClientWarehouseCode() == null || clientCourierWarehouseMapping.getClientWarehouseCode().trim().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Client Warehouse code field is blank.");
            return responseBean;
        }
        ClientWarehouse  clientWarehouse = clientWarehouseRepository.findByWarehouseCode(clientCourierWarehouseMapping.getClientWarehouseCode());
        if(clientWarehouse==null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Please enter valid client warehouse code, Code not found in system.");
            return responseBean;
        }


        if (clientCourierWarehouseMapping.getServiceProviderCode() == null || clientCourierWarehouseMapping.getServiceProviderCode().trim().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Service provider code field is blank.");
            return responseBean;
        }
        ServiceProvider serviceProvider = serviceProviderRepository.findByServiceProviderCode(clientCourierWarehouseMapping.getServiceProviderCode());
        if(serviceProvider == null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Please enter valid service provider code, Code not found in system.");
            return responseBean;
        }
        if (clientCourierWarehouseMapping.getServiceProviderWarehouseCode() == null || clientCourierWarehouseMapping.getServiceProviderWarehouseCode().trim().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Service provider warehouse code is blank.");
            return responseBean;
        }
        if (clientCourierWarehouseMapping.getClientCode() == null || clientCourierWarehouseMapping.getClientCode().trim().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Client code field is blank.");
            return responseBean;
        }
        if(!clientWarehouse.getClientCode().equals(clientCourierWarehouseMapping.getClientCode())){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Warehouse code is not belong to client.");
            return responseBean;
        }
        if(clientRepository.findByClientCode(clientCourierWarehouseMapping.getClientCode()) == null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Please enter valid client code, Code not found in system.");
            return responseBean;
        }
        ClientCourierWarehouseMapping mapping = clientCourierWarehouseMappingRepository.findByClientCodeAndClientWarehouseCodeAndServiceProviderID(
                clientCourierWarehouseMapping.getClientCode(), clientCourierWarehouseMapping.getClientWarehouseCode(),
                serviceProvider.getId());
        if (mapping != null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Same mapping already exist. (client,clientWarehouseCode,serviceProvider,serviceProviderWarehouseCode)");
            return responseBean;
        }

        clientCourierWarehouseMapping.setServiceProviderID(serviceProvider.getId());
        clientCourierWarehouseMapping = clientCourierWarehouseMappingRepository.save(clientCourierWarehouseMapping);
        if (clientCourierWarehouseMapping.getId() != null) {
            responseBean.setResponseBody(clientCourierWarehouseMapping);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_COURIER_WAREHOUSE_MAPPING_ADDED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_COURIER_WAREHOUSE_MAPPING_ADDED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<ClientCourierWarehouseMapping> updateClientCourierWarehouseMapping(ClientCourierWarehouseMapping clientCourierWarehouseMapping) {
        ResponseBean responseBean = new ResponseBean();
        if (clientCourierWarehouseMapping.getClientWarehouseCode() == null || clientCourierWarehouseMapping.getClientWarehouseCode().trim().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Client Warehouse code field is blank.");
            return responseBean;
        }
        if(clientWarehouseRepository.findByWarehouseCode(clientCourierWarehouseMapping.getClientWarehouseCode()) == null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Please enter valid client warehouse code, Code not found in system.");
            return responseBean;
        }
        if (clientCourierWarehouseMapping.getServiceProviderCode() == null || clientCourierWarehouseMapping.getServiceProviderCode().trim().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Service provider code field is blank.");
            return responseBean;
        }
        ServiceProvider serviceProvider = serviceProviderRepository.findByServiceProviderCode(clientCourierWarehouseMapping.getServiceProviderCode());
        if(serviceProvider == null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Please enter valid service provider code, Code not found in system.");
            return responseBean;
        }
        /*if(serviceProviderRepository.findByServiceProviderCode(clientCourierWarehouseMapping.getServiceProviderCode()) == null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Please enter valid service provider code, Code not found in system.");
            return responseBean;
        }*/
        if (clientCourierWarehouseMapping.getServiceProviderWarehouseCode() == null || clientCourierWarehouseMapping.getServiceProviderWarehouseCode().trim().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Service provider warehouse code is blank.");
            return responseBean;
        }
        if (clientCourierWarehouseMapping.getClientCode() == null || clientCourierWarehouseMapping.getClientCode().trim().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Client code field is blank.");
            return responseBean;
        }
        if(clientRepository.findByClientCode(clientCourierWarehouseMapping.getClientCode()) == null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Please enter valid client code, Code not found in system.");
            return responseBean;
        }

        clientCourierWarehouseMapping.setServiceProviderID(serviceProvider.getId());
        clientCourierWarehouseMapping = clientCourierWarehouseMappingRepository.save(clientCourierWarehouseMapping);
        if (clientCourierWarehouseMapping != null) {
            responseBean.setResponseBody(clientCourierWarehouseMapping);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_COURIER_WAREHOUSE_MAPPING_UPDATED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_COURIER_WAREHOUSE_MAPPING_UPDATED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<ClientCourierWarehouseMapping> deleteClientCourierWarehouseMapping(Long clientCourierWarehouseMappingId) {
        ResponseBean responseBean = new ResponseBean();
        Optional<ClientCourierWarehouseMapping> clientCourierWarehouseMapping = clientCourierWarehouseMappingRepository.findById(clientCourierWarehouseMappingId);
        if (!clientCourierWarehouseMapping.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_COURIER_WAREHOUSE_MAPPING_ID_NOT_FOUND, null, null));
            return responseBean;
        }

        /*clientCourierWarehouseMapping.get().setActive(AppProperty.IN_ACTIVE);
        clientCourierWarehouseMappingRepository.save(clientCourierWarehouseMapping.get());
        responseBean.setResponseBody(clientCourierWarehouseMapping.get());*/
        clientCourierWarehouseMappingRepository.delete(clientCourierWarehouseMapping.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_COURIER_WAREHOUSE_MAPPING_DELETE_MSG, null, null));
        return responseBean;
    }

    @Override
    public ResponseBean<ClientCourierWarehouseMapping> getAllClientCourierWarehouseMapping() {
        ResponseBean responseBean = new ResponseBean();
        List<ClientCourierWarehouseMapping> clientCourierWarehouseMappingList = (List<ClientCourierWarehouseMapping>) clientCourierWarehouseMappingRepository.findAll();
        if (clientCourierWarehouseMappingList != null && !clientCourierWarehouseMappingList.isEmpty()) {
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG, null, null));
            responseBean.setResponseBody(clientCourierWarehouseMappingList);
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<ClientCourierWarehouseMapping> findByClientCourierWarehouseMapping(Long id) {
        ResponseBean responseBean = new ResponseBean();
        Optional<ClientCourierWarehouseMapping> status = clientCourierWarehouseMappingRepository.findById(id);
        if (!status.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_COURIER_WAREHOUSE_MAPPING_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        responseBean.setResponseBody(status.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_COURIER_WAREHOUSE_MAPPING_ID_FOUND, null, null));
        return responseBean;
    }

    @Override
    public DataTablesOutput<ClientCourierWarehouseMapping> getAllClientCourierWarehouseMappingPaginationAndSort(DataTablesInput input) {
        User currentLoginUser = sessionUserBean.getUser();
        if(UserType.CLIENT.equals(currentLoginUser.getType())){
            input.addColumn("clientCode", true, false, currentLoginUser.getClientCode());
        }
        return clientCourierWarehouseMappingRepository.findAll(input);
    }


    @Override
    public BulkUploadBean uploadBulk(BulkUploadBean bulkUploadBean, BulkMaster bulkMaster) {
        List<Map<String, String>> errorRecord = new ArrayList<Map<String, String>>();
        List<Map<String, String>> successRecord = new ArrayList<Map<String, String>>();
        int count = 0;
        int progress= 0;
        String token = BulkUploadService.generateRandomString();
        for (Map<String, String> map : bulkUploadBean.getRecords()) {
            // Progress Count
            count++;
            progress = BulkUploadService.calculateUploadPercentage(count, bulkUploadBean.getRecords().size());
            bulkMasterService.setUploadProgressCount(bulkMaster.getName(), progress, token, false);

            String clientCode = map.get(BulkHeaderConstant.CLIENT_CODE);
            String clientWarehouseCode = map.get(BulkHeaderConstant.CLIENT_WAREHOUSE_CODE);
            String serviceProviderCode = map.get(BulkHeaderConstant.SERVICE_PROVIDER_CODE);
            String serviceProviderWarehouseCode = map.get(BulkHeaderConstant.SERVICE_PROVIDER_WAREHOUSE_CODE);  // optional

            if(clientCode==null || clientCode.trim().equals("")){
                map.put(BulkHeaderConstant.MESSAGE,"Client code is empty.");
                errorRecord.add(map);
                continue;
            }
            if(clientWarehouseCode==null || clientWarehouseCode.trim().equals("")){
                map.put(BulkHeaderConstant.MESSAGE,"Client warehouse code is empty.");
                errorRecord.add(map);
                continue;
            }
            if(serviceProviderCode==null || serviceProviderCode.trim().equals("")){
                map.put(BulkHeaderConstant.MESSAGE,"Service provider code is empty.");
                errorRecord.add(map);
                continue;
            }
            if(serviceProviderWarehouseCode==null || serviceProviderWarehouseCode.trim().equals("")){
                map.put(BulkHeaderConstant.MESSAGE,"Service provider warehouse code is empty.");
                errorRecord.add(map);
                continue;
            }

            Client client = clientRepository.findByClientCode(clientCode);
            if(client==null){
                map.put(BulkHeaderConstant.MESSAGE,"Client Code not find in database.");
                errorRecord.add(map);
                continue;

            }

            ClientCourierWarehouseMapping bean = new ClientCourierWarehouseMapping();
            bean.setClientWarehouseCode(clientWarehouseCode);
            bean.setClientCode(clientCode);
            bean.setServiceProviderWarehouseCode(serviceProviderWarehouseCode);
            bean.setServiceProviderCode(serviceProviderCode);

            ResponseBean responseBean = addClientCourierWarehouseMapping(bean);
            if(responseBean.getStatus().equals(ResponseStatus.FAIL)){
                map.put(BulkHeaderConstant.MESSAGE, responseBean.getMessage());
                errorRecord.add(map);
                continue;
            }
            successRecord.add(map);
        }
        bulkUploadBean.setBulkMaster(bulkMaster);
        bulkUploadBean.setErrorRecords(errorRecord);
        bulkUploadBean.setSuccessRecords(successRecord);
        // set progress count
        bulkMasterService.setUploadProgressCount(bulkMaster.getName(), 100, token, true);
        return bulkUploadBean;
    }

    @Override
    public ResponseEntity<Resource> clientCourierWarehouseMappingReport() {
        List<ClientCourierWarehouseMapping> clientCourierWarehouseMappingList = (List<ClientCourierWarehouseMapping>) clientCourierWarehouseMappingRepository.findAll();
        if(clientCourierWarehouseMappingList == null){
            return null;
        }
        SXSSFWorkbook xlsWorkbook = new SXSSFWorkbook();
        SXSSFSheet xlsSheet = (SXSSFSheet) xlsWorkbook.createSheet();
        int rowIndex = ProjectConstant.ZERO;
        SXSSFRow titleRow = (SXSSFRow) xlsSheet.createRow(rowIndex++);
        titleRow.createCell(0).setCellValue("CLIENT_CODE");
        titleRow.createCell(1).setCellValue("CLIENT_WAREHOUSE_CODE");
        titleRow.createCell(2).setCellValue("SERVICE_PROVIDER_CODE");
        titleRow.createCell(3).setCellValue("SEVICE_PROVIDER_WAREHOUSE_CODE");
        titleRow.createCell(4).setCellValue("SEVICE_PROVIDER_ID");
        titleRow.createCell(5).setCellValue("ACTIVE");

        if(clientCourierWarehouseMappingList != null && clientCourierWarehouseMappingList.size() > ProjectConstant.ZERO){
            for (ClientCourierWarehouseMapping clientCourierWarehouseMapping : clientCourierWarehouseMappingList) {
                SXSSFRow dataRow = (SXSSFRow)xlsSheet.createRow(rowIndex++);
                dataRow.createCell(0).setCellValue(clientCourierWarehouseMapping.getClientCode() != null ? clientCourierWarehouseMapping.getClientCode():"NA");
                dataRow.createCell(1).setCellValue(clientCourierWarehouseMapping.getClientWarehouseCode() != null ? clientCourierWarehouseMapping.getClientWarehouseCode():"NA");
                dataRow.createCell(2).setCellValue(clientCourierWarehouseMapping.getServiceProviderCode() != null ? clientCourierWarehouseMapping.getServiceProviderCode():"NA");
                dataRow.createCell(3).setCellValue(clientCourierWarehouseMapping.getServiceProviderWarehouseCode() != null ? clientCourierWarehouseMapping.getServiceProviderWarehouseCode():"NA");
                dataRow.createCell(4).setCellValue(clientCourierWarehouseMapping.getServiceProviderID() != null ? clientCourierWarehouseMapping.getServiceProviderID().toString():"NA");
                dataRow.createCell(5).setCellValue(clientCourierWarehouseMapping.getActive() != null ? clientCourierWarehouseMapping.getActive().toString():"NA");

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
                .header("Content-Disposition", "attachment; filename="+"clientCourierWarehouseMappingReport"+ProjectConstant.EXTENSION_XLS)
                .body(new ByteArrayResource(byteArray));
    }
}



