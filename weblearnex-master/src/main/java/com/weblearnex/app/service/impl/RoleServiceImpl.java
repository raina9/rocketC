package com.weblearnex.app.service.impl;

import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.entity.setup.Page;
import com.weblearnex.app.entity.setup.Role;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.datatable.reposatory.PageRepository;
import com.weblearnex.app.datatable.reposatory.RoleRepository;
import com.weblearnex.app.reposatory.StateRepository;
import com.weblearnex.app.service.RoleService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private StateRepository sateRepository;

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private MessageSource messageSource;


        @Override
        public ResponseBean<Role> addRole(Role role) {
            ResponseBean responseBean = new ResponseBean();
            if (role.getName() == null || role.getName().isEmpty() || roleRepository.findRoleByName(role.getName()) != null) {
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage(messageSource.getMessage(MessageProperty.ROLE_NAME_ALREADY_EXIST_MSG, null, null));
                return responseBean;
            }
            if (role.getPages() == null || role.getPages().isEmpty()) {
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage(messageSource.getMessage(MessageProperty.ROLE_PAGEIDS_DOES_NOT_NULL_OR_BLANK, null, null));
                return responseBean;
            }
            List<Page> pagesDB = new ArrayList<Page>();
            for(Page page : role.getPages()){
                if(page != null && page.getId() != null){
                    Optional<Page> dbPage = pageRepository.findById(page.getId());
                    if(dbPage.isPresent()){
                        pagesDB.add(dbPage.get());
                    }
                }
            }
            role.setPages(pagesDB);
            role = roleRepository.save(role);
            if (role.getId() != null) {
                responseBean.setResponseBody(role);
                responseBean.setStatus(ResponseStatus.SUCCESS);
                responseBean.setMessage(messageSource.getMessage(MessageProperty.ROLE_ADDED_MSG, null, null));
            } else {
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage(messageSource.getMessage(MessageProperty.ROLE_ADDED_ERROR_MSG, null, null));
            }
            return responseBean;
        }


        @Override
        public ResponseBean<Role> updateRole(Role role) {
            ResponseBean responseBean = new ResponseBean();
            if (role.getId() == null || !roleRepository.findById(role.getId()).isPresent()) {
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage(messageSource.getMessage(MessageProperty.ROLE_ID_NOT_FOUND, null, null));
                return responseBean;
            }
            if (role.getName() == null || role.getName().isEmpty()) {
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage(messageSource.getMessage(MessageProperty.ROLE_NAME_DOES_NOT_NULL_OR_BLANK, null, null));
                return responseBean;
            }
            if (role.getPages() == null || role.getPages().isEmpty())  {
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage(messageSource.getMessage(MessageProperty.ROLE_PAGEIDS_DOES_NOT_NULL_OR_BLANK, null, null));
                return responseBean;
            }

            List<Page> pagesDB = new ArrayList<Page>();
            for(Page page : role.getPages()){
                if(page != null && page.getId() != null){
                    Optional<Page> dbPage = pageRepository.findById(page.getId());
                    if(dbPage.isPresent()){
                        pagesDB.add(dbPage.get());
                    }
                }
            }
            role.setPages(pagesDB);
            role = roleRepository.save(role);
            if (role != null) {
                responseBean.setResponseBody(role);
                responseBean.setStatus(ResponseStatus.SUCCESS);
                responseBean.setMessage(messageSource.getMessage(MessageProperty.ROLE_UPDATED_MSG, null, null));
            } else {
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage(messageSource.getMessage(MessageProperty.ROLE_UPDATED_ERROR_MSG, null, null));
            }
            return responseBean;
        }

    @Override
    public ResponseBean<Role> deleteRole(Long roleId) {
        ResponseBean responseBean = new ResponseBean();
        Optional<Role> role = roleRepository.findById(roleId);
        if (!role.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.ROLE_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        //Role findRole = role.get();
        role.get().setActive(AppProperty.IN_ACTIVE);
        roleRepository.save(role.get());
        responseBean.setResponseBody(role.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.ROLE_DELETE_MSG, null, null));
        return responseBean;
    }
    @Override
    public ResponseBean<Role> findByRoleId(Long id) {
        ResponseBean responseBean = new ResponseBean();
        Optional<Role> status = roleRepository.findById(id);
        if (!status.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.ROLE_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        responseBean.setResponseBody(status.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.ROLE_ID_FOUND, null, null));
        return responseBean;
    }

    @Override
    public DataTablesOutput<Role> getAllRolePaginationAndSort(DataTablesInput input) {
        return roleRepository.findAll(input);
    }

    @Override
    public ResponseBean<Role> getAllRole() {
        ResponseBean responseBean = new ResponseBean();
        List<Role> roleList = (List<Role>) roleRepository.findAll();
        if(roleList !=null && !roleList.isEmpty()){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG, null, null));
            responseBean.setResponseBody(roleList);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG, null, null));
        }
        return responseBean;
    }


}
