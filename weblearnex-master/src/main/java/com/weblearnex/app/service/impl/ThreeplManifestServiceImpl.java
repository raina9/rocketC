package com.weblearnex.app.service.impl;

import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.RemittanceStatus;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.constant.UserType;
import com.weblearnex.app.datatable.reposatory.SaleOrderRepository;
import com.weblearnex.app.datatable.reposatory.StatusRepository;
import com.weblearnex.app.datatable.reposatory.ThreeplManifestRepository;
import com.weblearnex.app.entity.manifest.ThreeplManifest;
import com.weblearnex.app.entity.order.SaleOrder;
import com.weblearnex.app.entity.setup.Status;
import com.weblearnex.app.entity.setup.User;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.model.SessionUserBean;
import com.weblearnex.app.service.ThreeplManifestService;
import com.weblearnex.app.utils.SharedMethords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ThreeplManifestServiceImpl implements ThreeplManifestService {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private SaleOrderRepository saleOrderRepository;

    @Autowired
    @Qualifier("applicionConfig")
    private MessageSource applicionConfig;

    @Autowired
    private ThreeplManifestRepository threeplManifestRepository;

    @Autowired
    private SessionUserBean sessionUserBean;

    @Autowired
    private StatusRepository statusRepository;

    @Override
    public ResponseBean<ThreeplManifest> addThreeplManifest(ThreeplManifest threeplManifest) {
        ResponseBean responseBean = new ResponseBean();
        if(threeplManifest.getAwbNumbers() == null || threeplManifest.getAwbNumbers().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.AWB_NUMBER_NOT_NULL, null, null));
            return responseBean;
        }

        List<SaleOrder> saleOrderList = saleOrderRepository.findByReferanceNoIn(Arrays.asList(threeplManifest.getAwbNumbers().split(",")));
        if(saleOrderList == null || saleOrderList.isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Awb number not found in system.");
            return responseBean;
        }
        saleOrderList.stream().forEach(saleOrder -> saleOrder.setThreePlManifestGenerated(true));
        saleOrderRepository.saveAll(saleOrderList);
        if(threeplManifest.getCourierCode() == null || threeplManifest.getCourierCode().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_CODE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        threeplManifest.setManifestNumber("M_"+String.valueOf(new Date().getTime()));
        User sessionUser = sessionUserBean.getUser();
        if(UserType.CLIENT.equals(sessionUser.getType())){
            threeplManifest.setClientCode(sessionUser.getClientCode());
            threeplManifest.setCreateBy(sessionUser.getClientCode());
        }
        threeplManifest.setCourierCode(threeplManifest.getCourierCode());
        threeplManifest.setCreateDate(new Date());

        threeplManifest = threeplManifestRepository.save(threeplManifest);
        if(threeplManifest.getId() != null) {
            responseBean.setResponseBody(threeplManifest);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_ADDED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.ERROR_AT_SAVE_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public DataTablesOutput<ThreeplManifest> getAllThreeplManifest(DataTablesInput input) {
        User user = sessionUserBean.getUser();
        if(user.getType().equals(UserType.CLIENT)){
            input.addColumn("clientCode", true, false, sessionUserBean.getUser().getClientCode());
        }
        input.addColumn("podUpload", true, false, "false");
        //input.addColumn("podUpload", true, false, "1");
        return threeplManifestRepository.findAll(input);
    }

    @Override
    public ResponseBean<ThreeplManifest> findByAwbNumber(String awbNumbers,String courierCode) {
        ResponseBean responseBean = new ResponseBean();
        //List<String> referanceNos = Arrays.asList(awbNumbers.split(","));
        SaleOrder  saleOrder = saleOrderRepository.findByReferanceNo(awbNumbers);
        if (saleOrder == null) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.AWB_NUMBER_NOT_FOUND, null, null));
            return responseBean;
        }
        String deliveredCode = applicionConfig.getMessage(AppProperty.UD_DELIVERED, null, null);
        Status deliveredStatus = statusRepository.findByStatusCode(deliveredCode);

        if(saleOrder.getCurrentStatus().getStatusName().equals(deliveredStatus.getStatusName())){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Deliverd AWB");
            return responseBean;
        }


        if(courierCode==null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Courier code does not null or empty.");
            return responseBean;
        }
        if(saleOrder.getCourierAWBNumber()==null|| saleOrder.getCourierAWBNumber().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_AWB_NUM_NOT_NOT_NULL, null, null));
            return responseBean;
        }
        String saleOrderCourierCode=saleOrder.getCourierCode();
        if(!courierCode.equals(saleOrderCourierCode)){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("please select correct courier code.");
            return responseBean;
        }
        if(saleOrder.getThreePlManifestGenerated()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Manifest already generated.");
            return responseBean;
        }
        responseBean.setResponseBody(saleOrder);
        responseBean.setStatus(ResponseStatus.SUCCESS);
        // responseBean.setMessage(messageSource.getMessage(MessageProperty.AWB_NUMBER_FOUND, null, null));
        return responseBean;
    }

    @Override
    public ResponseBean<List<ThreeplManifest>> getAllThreeplManifest() {
        ResponseBean responseBean = new ResponseBean();
        List<ThreeplManifest> manifestList = null;
        User sessionUser = sessionUserBean.getUser();
        if(UserType.CLIENT.equals(sessionUser.getType())){
            manifestList = threeplManifestRepository.findAllByClientCode(sessionUser.getClientCode());
        }else {
            manifestList = (List<ThreeplManifest>) threeplManifestRepository.findAll();
        }
        if(manifestList !=null && !manifestList.isEmpty()){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG, null, null));
            responseBean.setResponseBody(manifestList);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean uploadPodFile(MultipartFile file, String manifestId) {
        ResponseBean responseBean = new ResponseBean();
        if(file.isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("File is empty.");
            return responseBean;
        }
        if(manifestId.isEmpty()||manifestId==null) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("manifestId is empty.");
            return responseBean;
        }

        String filePath = applicionConfig.getMessage(AppProperty.THREE_PL_POD_UPLOAD_FILE_PATH, null, null);
        String ext = Arrays.stream(file.getOriginalFilename().split("\\.")).reduce((a,b) -> b).orElse(null);
        File files =  new File(filePath);
        if(!files.isDirectory()){
            files.mkdir();
        }
        filePath =  filePath + "/"+ manifestId +"."+ ext;

        ThreeplManifest threeplManifest = threeplManifestRepository.findByManifestNumber(manifestId);
        threeplManifest.setPodFileName(filePath);
        threeplManifest.setPodUpload(true);
        threeplManifestRepository.save(threeplManifest);
        
        try {
            file.transferTo(new File(filePath));
        } catch (Exception e){e.printStackTrace();}
        responseBean.setStatus(ResponseStatus.SUCCESS);
        return responseBean;
    }


    @Override
    public ResponseBean<ThreeplManifest> findByManifestId(Long id) {
        ResponseBean responseBean = new ResponseBean();
        Optional<ThreeplManifest> threeplManifest=threeplManifestRepository.findById(id);
        if(!threeplManifest.isPresent()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.CLIENT_ID_NOT_FOUND,null,null));
            return responseBean;
        }
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG,null,null));
        responseBean.setResponseBody(threeplManifest.get());

        return responseBean;
    }

    @Override
    public ResponseBean<Map<String, Object>> getManifestAndSaleOrders(Long id) {
        ResponseBean responseBean = new ResponseBean();
        try{
            Optional<ThreeplManifest> threeplManifest=threeplManifestRepository.findById(id);
            if(!threeplManifest.isPresent()){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Object not found.");
                return responseBean;
            }
            List<SaleOrder> saleOrderList = saleOrderRepository.findByReferanceNoIn(Arrays.asList(threeplManifest.get().getAwbNumbers().split(",")));
            Map<String,Object> resultMap = new HashMap<String,Object>();
            resultMap.put("manifest", threeplManifest.get());
            resultMap.put("saleOrderList", saleOrderList);

            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setResponseBody(resultMap);
        }catch (Exception e){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Server internal error.");
            e.printStackTrace();
        }
        return responseBean;
    }

    @Override
    public ResponseBean<ThreeplManifest> updateManifest(ThreeplManifest threeplManifest) {
        ResponseBean responseBean = new ResponseBean();
        try{
            if(threeplManifest.getAwbNumbers() == null || threeplManifest.getAwbNumbers().isEmpty()) {
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage(messageSource.getMessage(MessageProperty.AWB_NUMBER_NOT_NULL, null, null));
                return responseBean;
            }
            List<SaleOrder> saleOrderList = saleOrderRepository.findByReferanceNoIn(Arrays.asList(threeplManifest.getAwbNumbers().split(",")));
            if(saleOrderList == null || saleOrderList.isEmpty()){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Awb number not found in system.");
                return responseBean;
            }
            Optional<ThreeplManifest> threeplManifestDB = threeplManifestRepository.findById(threeplManifest.getId());
            threeplManifest.setCourierCode(threeplManifestDB.get().getCourierCode());
            threeplManifest.setClientCode(threeplManifestDB.get().getClientCode());
            threeplManifest.setManifestNumber(threeplManifestDB.get().getManifestNumber());

            saleOrderList.stream().forEach(saleOrder -> saleOrder.setThreePlManifestGenerated(true));
            saleOrderRepository.saveAll(saleOrderList);
            threeplManifest.setCourierCode(threeplManifest.getCourierCode());
            threeplManifest.setCreateDate(new Date());
            threeplManifestRepository.save(threeplManifest);
            if (threeplManifest != null) {
                responseBean.setResponseBody(threeplManifest);
                responseBean.setStatus(ResponseStatus.SUCCESS);
                responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_UPDATED_MSG, null, null));
            } else {
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage(messageSource.getMessage(MessageProperty.ERROR_AT_UPDATE_MSG, null, null));
            }
        }catch (Exception e){
            e.printStackTrace();
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Server internal error.");
        }
        return responseBean;
    }


    @Override
    public DataTablesOutput<ThreeplManifest> getAllPodUploadedManifest(DataTablesInput input) {
        input.addColumn("podUpload", true, false, "true");
        if(sessionUserBean.getUser().getType().equals(UserType.CLIENT)){
            input.addColumn("clientCode", true, false, sessionUserBean.getUser().getClientCode());
        }
        //input.addColumn("podUpload", true, false, "1");
        return threeplManifestRepository.findAll(input);
    }
}
