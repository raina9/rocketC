package com.weblearnex.app.service.impl;

import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.entity.setup.Page;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.datatable.reposatory.PageRepository;
import com.weblearnex.app.reposatory.StateRepository;
import com.weblearnex.app.service.PageService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PageServiceImpl implements PageService {

    Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private StateRepository sateRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    public ResponseBean<Page> addPage(Page page) {
        ResponseBean responseBean = new ResponseBean();
        if (page.getModule() == null || page.getModule().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.PAGE_MODULE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (page.getSubModule() == null || page.getSubModule().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.PAGE_SUBMODULE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (page.getDisplayName() == null || page.getDisplayName().isEmpty()|| pageRepository.findByDisplayName(page.getDisplayName()) != null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.PAGE_DISPLAY_NAME_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (page.getPageURL() == null || page.getPageURL().isEmpty() || pageRepository.findByPageURL(page.getPageURL()) != null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.PAGE_URL_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        page = pageRepository.save(page);
        if (page.getId() != null) {
            responseBean.setResponseBody(page);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.PAGE_ADDED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.PAGE_ADDED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<Page> updatePage(Page page) {
        ResponseBean responseBean = new ResponseBean();
        if (page.getId() == null || !pageRepository.findById(page.getId()).isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.PAGE_ID_NOT_FOUND, null, null));
            return responseBean;
        }

        if (page.getModule() == null || page.getModule().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.PAGE_MODULE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (page.getSubModule() == null || page.getSubModule().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.PAGE_SUBMODULE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (page.getDisplayName() == null || page.getDisplayName().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.PAGE_DISPLAY_NAME_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (page.getPageURL() == null || page.getPageURL().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.PAGE_URL_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        pageRepository.save(page);
        if (page != null) {
            responseBean.setResponseBody(page);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.PAGE_UPDATED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.PAGE_UPDATED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<Page> deletePage(Long id) {
        ResponseBean responseBean = new ResponseBean();
        Optional<Page>  page= pageRepository.findById(id);
        if(!page.isPresent()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.PAGE_ID_NOT_FOUND,null,null));
            return responseBean;
        }
        page.get().setActive(AppProperty.IN_ACTIVE);
        pageRepository.save( page.get());
        responseBean.setResponseBody(page);
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.PAGE_DELETE_MSG,null,null));
        return responseBean;
    }

    @Override
    public ResponseBean<Page> getAllPage() {
        ResponseBean responseBean = new ResponseBean();
        List<Page> pageList = (List<Page>) pageRepository.findAll();
        if(pageList !=null && !pageList.isEmpty()){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG, null, null));
            responseBean.setResponseBody(pageList);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG, null, null));
        }
        return responseBean;
    }
    @Override
    public ResponseBean<Page> findByPageId(Long id) {
        ResponseBean responseBean = new ResponseBean();
        Optional<Page> status = pageRepository.findById(id);
        if (!status.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.PAGE_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        responseBean.setResponseBody(status.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.PAGE_ID_FOUND, null, null));
        return responseBean;
    }

    @Override
    public DataTablesOutput<Page> getAllPagePaginationAndSort(DataTablesInput input) {
        return pageRepository.findAll(input);
    }
}
