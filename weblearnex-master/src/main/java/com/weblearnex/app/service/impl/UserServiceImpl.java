package com.weblearnex.app.service.impl;

import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.ProjectConstant;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.constant.UserType;
import com.weblearnex.app.entity.setup.Branch;
import com.weblearnex.app.entity.setup.Role;
import com.weblearnex.app.entity.setup.User;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.datatable.reposatory.RoleRepository;
import com.weblearnex.app.model.SessionUserBean;
import com.weblearnex.app.reposatory.StateRepository;
import com.weblearnex.app.datatable.reposatory.UserRepository;
import com.weblearnex.app.service.UserService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private StateRepository sateRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private SessionUserBean sessionUserBean;

    /*@Autowired
    private PasswordEncoder passwordEncoder;*/

    @Override
    @Transactional
    public ResponseBean<User> addUser(User user) {
        ResponseBean responseBean = new ResponseBean();
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.USER_PASWORD_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (user.getLoginId() == null || user.getLoginId().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.USER_LOGIN_ID_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (user.getFisrtName() == null || user.getFisrtName().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.USER_FIRST_NAME_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        /*if (user.getLastName() == null || user.getLastName().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.USER_LAST_NAME_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }*/
        if (user.getContact() == null || user.getContact().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.USER_CONTACT_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (user.getGender() == null || user.getGender().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.USER_GENDER_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (user.getPincode() == null || user.getPincode().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.USER_PIN_CODE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (user.getAddress() == null || user.getAddress().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.USER_ADDRESS_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (user.getPanNumber() == null || user.getPanNumber().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.USER_PAN_NUMBER_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (user.getAadharNumber() == null || user.getAadharNumber().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.USER_AADHAR_NUMBER_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (user.getState() == null || user.getState().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.USER_STATE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (user.getCity() == null || user.getCity().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.USER_CITY_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (user.getCountry() == null || user.getCountry().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.USER_COUNTRY_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (user.getType() == null) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.USER_TYPE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (UserType.CLIENT.equals(user.getType()) && (user.getClientCode() == null || "".equals(user.getClientCode().trim()))){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Client code is mandatory for client type user.");
            return responseBean;
        }
        if(user.getAdmin().equals(false)){
            if (user.getRole() == null || user.getRole().isEmpty()) {
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Please select role.");
                return responseBean;
            }
        }
        if (user.getBranch() == null || user.getBranch().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Please select branch.");
            return responseBean;
        }
        /*List<Role> roles =new ArrayList<>();
        String roleIds = user.getUserRoleIds();
        String[] idArray = roleIds.split(",");
        for(int i =0;i<idArray.length;i++){
            Long id =Long.parseLong(idArray[i]);
            Optional<Role> r =roleRepository.findById(id);
            if(r.isPresent()){
                roles.add(r.get()) ;
            }else{
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage(messageSource.getMessage(MessageProperty.ROLE_ID_NOT_FOUND, null, null));
                return responseBean;
            }
        }*/
        // user.setPassword(passwordEncoder.encode(user.getPassword()));
        //user.setRole(roles);
        user = userRepository.save(user);
        if (user.getId() != null) {
            responseBean.setResponseBody(user);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.USER_ADDED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.USER_ADDED_ERROR_MSG, null, null));
        }
        return responseBean;
    }
    @Override
    @Transactional
    public ResponseBean<User> updateUser(User user) {
        ResponseBean responseBean = new ResponseBean();
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.USER_PASWORD_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (user.getLoginId() == null || user.getLoginId().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.USER_LOGIN_ID_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (user.getFisrtName() == null || user.getFisrtName().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.USER_FIRST_NAME_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        /*if (user.getLastName() == null || user.getLastName().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.USER_LAST_NAME_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }*/
        if (user.getContact() == null || user.getContact().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.USER_CONTACT_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (user.getGender() == null || user.getGender().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.USER_GENDER_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }

        if (user.getPincode() == null || user.getPincode().trim().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.USER_PIN_CODE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
       // if (user.getAddress() == null || userRepository.findByAddress(user.getAddress())==null){
        if (user.getAddress() == null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.USER_ADDRESS_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (user.getType() == null ) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.USER_TYPE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (UserType.CLIENT.equals(user.getType()) && (user.getClientCode() == null || "".equals(user.getClientCode().trim()))){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Client code is mandatory for client type user.");
            return responseBean;
        }
        if(user.getAdmin().equals(false)){
            if (user.getRole() == null || user.getRole().isEmpty()) {
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Please select role.");
                return responseBean;
            }
        }
        if (user.getBranch() == null || user.getBranch().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Please select branch.");
            return responseBean;
        }
       /* List<Role> roles =new ArrayList<>();
        String roleIds = user.getUserRoleIds();
        String[] idArray = roleIds.split(",");
        for(int i =0;i<idArray.length;i++){
            Long id =Long.parseLong(idArray[i]);
            Optional<Role> r =roleRepository.findById(id);
            if(r.isPresent()){
                roles.add(r.get()) ;
            }
        }
        // user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(roles);*/
        user = userRepository.save(user);
        if (user != null) {
            responseBean.setResponseBody(user);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.USER_UPDATED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.USER_ADDED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<User> deleteUser(Long userId) {
        ResponseBean responseBean = new ResponseBean();
        Optional<User> user= userRepository.findById(userId);
        if(!user.isPresent()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.USER_ID_NOT_FOUND,null,null));
            return responseBean;
        }
        user.get().setActive(AppProperty.IN_ACTIVE);
        userRepository.save( user.get());
        responseBean.setResponseBody(user);
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.USER_DELETE_MSG,null,null));
        return responseBean;
    }
    @Override
    public ResponseBean<User> getAllUser() {
        ResponseBean responseBean = new ResponseBean();
        List<User> userList = (List<User>) userRepository.findAll();
        if(userList !=null && !userList.isEmpty()){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG, null, null));
            responseBean.setResponseBody(userList);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG, null, null));
        }
        return responseBean;
    }
    @Override
    public ResponseBean<User> findByUserId(Long id) {
        ResponseBean responseBean = new ResponseBean();
        Optional<User> optional = userRepository.findById(id);
        if (!optional.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.USER_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        responseBean.setResponseBody(optional.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.USER_ID_FOUND, null, null));
        return responseBean;
    }

    @Override
    public ResponseEntity<Resource> userReport() {
        List<User> UserList = (List<User>) userRepository.findAll();
        if(UserList == null){
            return null;
        }
        SXSSFWorkbook xlsWorkbook = new SXSSFWorkbook();
        SXSSFSheet xlsSheet = (SXSSFSheet) xlsWorkbook.createSheet();
        int rowIndex = ProjectConstant.ZERO;
        SXSSFRow titleRow = (SXSSFRow) xlsSheet.createRow(rowIndex++);
        titleRow.createCell(0).setCellValue("ADMIN");
        titleRow.createCell(1).setCellValue("BRANCH");
        titleRow.createCell(2).setCellValue("EMAIL");
        titleRow.createCell(3).setCellValue("LOGIN_ID");
        titleRow.createCell(4).setCellValue("FIRST_NAME");
        titleRow.createCell(5).setCellValue("LAST_NAME");
        titleRow.createCell(6).setCellValue("CONTACT");
        titleRow.createCell(7).setCellValue("ALTERNATE_CONTACT");
        titleRow.createCell(8).setCellValue("GENDER");
        titleRow.createCell(9).setCellValue("PIN_CODE");
        titleRow.createCell(10).setCellValue("ADDRESS");
        titleRow.createCell(11).setCellValue("DEPARTMENT");
        titleRow.createCell(12).setCellValue("DESIGNATION");
        titleRow.createCell(13).setCellValue("TYPE");
        titleRow.createCell(14).setCellValue("CLIENT_CODE");
        titleRow.createCell(15).setCellValue("STATE");
        titleRow.createCell(16).setCellValue("CITY");
        titleRow.createCell(17).setCellValue("PAN_NUMBER");
        titleRow.createCell(18).setCellValue("AADHAR_NUMBER");
        titleRow.createCell(19).setCellValue("EMPLOYEE CODE");
        titleRow.createCell(20).setCellValue("ROLE");
        titleRow.createCell(21).setCellValue("ACTIVE");


        if(UserList != null && UserList.size() > ProjectConstant.ZERO){
            for (User user : UserList) {
                SXSSFRow dataRow = (SXSSFRow)xlsSheet.createRow(rowIndex++);
                dataRow.createCell(0).setCellValue(user.getAdmin() != null ? user.getAdmin().toString() :"NA");

                List<Branch> branchList = user.getBranch();
                for (Branch branch : branchList){
                  dataRow.createCell(1).setCellValue(branch.getName()!= null ? branch.getName() :"NA");
                }
                dataRow.createCell(2).setCellValue(user.getEmail() != null ? user.getEmail() :"NA");
                dataRow.createCell(3).setCellValue(user.getLoginId() != null ? user.getLoginId() :"NA");
                dataRow.createCell(4).setCellValue(user.getFisrtName() != null ? user.getFisrtName() :"NA");
                dataRow.createCell(5).setCellValue(user.getLastName() != null ? user.getLastName() :"NA");
                dataRow.createCell(6).setCellValue(user.getContact() != null ? user.getContact():"NA");
                dataRow.createCell(7).setCellValue(user.getAlternateContact()!= null ? user.getAlternateContact() :"NA");
                dataRow.createCell(8).setCellValue(user.getGender() != null ? user.getGender():"NA");
                dataRow.createCell(9).setCellValue(user.getPincode() != null ? user.getPincode() :"NA");
                dataRow.createCell(10).setCellValue(user.getAddress() != null ? user.getAddress() :"NA");
                dataRow.createCell(11).setCellValue(user.getDepartment() != null ? user.getDepartment() :"NA");
                dataRow.createCell(12).setCellValue(user.getDesignation() != null ? user.getDesignation() :"NA");
                dataRow.createCell(13).setCellValue(user.getType() != null ? user.getType().toString() :"NA");
                dataRow.createCell(14).setCellValue(user.getClientCode() != null ? user.getClientCode() :"NA");
                dataRow.createCell(15).setCellValue(user.getState() != null ? user.getState() :"NA");
                dataRow.createCell(16).setCellValue(user.getCity() != null ? user.getCity() :"NA");
                dataRow.createCell(17).setCellValue(user.getPanNumber() != null ? user.getPanNumber() :"NA");
                dataRow.createCell(18).setCellValue(user.getAadharNumber() != null ? user.getAadharNumber():"NA");
                dataRow.createCell(19).setCellValue(user.getEmployeeCode() != null ? user.getEmployeeCode() :"NA");
                List<Role>roleList = user.getRole();
                for (Role role : roleList){
                    dataRow.createCell(20).setCellValue(role.getName() != null ? role.getName() :"NA");
                }
                dataRow.createCell(21).setCellValue(user.getActive() != null ? user.getActive().toString() :"NA");

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
                .header("Content-Disposition", "attachment; filename="+"userReport"+ProjectConstant.EXTENSION_XLS)
                .body(new ByteArrayResource(byteArray));

    }

    @Override
    public DataTablesOutput<User> getAllUserPaginationAndSort(DataTablesInput input) {
        return userRepository.findAll(input);
    }

    @Override
    public ResponseBean<User> changePassword(String oldPassword, String newPassword) {
        ResponseBean responseBean = new ResponseBean();
        if(oldPassword == null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Please enter old password.");
            return responseBean;
        }
        if(newPassword == null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Please enter new password.");
            return responseBean;
        }

        User user = userRepository.findByLoginId(sessionUserBean.getUser().getLoginId());
        if(!oldPassword.equals(user.getPassword())){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Old password mismatch, Please enter currect password.");
            return responseBean;
        }
        user.setPassword(newPassword);
        user = userRepository.save(user);
        if (user != null) {
            responseBean.setResponseBody(user);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage("User password changed successfully.");
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Fail to change user password.");
        }
        return responseBean;

    }
}
