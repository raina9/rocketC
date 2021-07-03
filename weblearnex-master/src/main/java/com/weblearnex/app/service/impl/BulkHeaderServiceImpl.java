package com.weblearnex.app.service.impl;

import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.entity.master.BulkHeader;
import com.weblearnex.app.model.DataTableResult;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.datatable.reposatory.BulkHeaderRepository;
import com.weblearnex.app.service.BulkHeaderService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@Service
public class BulkHeaderServiceImpl implements BulkHeaderService {

    Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BulkHeaderRepository bulkHeaderRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    public ResponseBean<BulkHeader> addBulkHeader(BulkHeader bulkHeader) {
        ResponseBean responseBean = new ResponseBean();
        BulkHeader bulkHeaderName= bulkHeaderRepository.findByDisplayName(bulkHeader.getDisplayName());
        if (bulkHeader.getDisplayName() == null || bulkHeader.getDisplayName().isEmpty() || bulkHeaderName !=null) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.DISPLAY_NAME_ALREADY_EXIST_MSG, null, null));
            return responseBean;
        }
        BulkHeader bulkHeaderDB = bulkHeaderRepository.findByHeaderCode(bulkHeader.getHeaderCode());
        if (bulkHeader.getHeaderCode() == null || bulkHeader.getHeaderCode().isEmpty() ||  bulkHeaderDB != null) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.BULKHEADER_ALREADY_EXIST_MSG, null, null));
            return responseBean;
        }
        bulkHeader = bulkHeaderRepository.save(bulkHeader);
        if (bulkHeader.getId() != null) {
            responseBean.setResponseBody(bulkHeader);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.BULKHEADER_ADDED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.BULKHEADER_ADDED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<BulkHeader> updateBulkHeader(BulkHeader bulkHeader) {
        ResponseBean responseBean = new ResponseBean();

        if (bulkHeader.getId() == null || !bulkHeaderRepository.findById(bulkHeader.getId()).isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.BULKHEADER_ID_DOES_NOT_EMPTY_OR_NO_DATA_FOUND_IN_DATABASE, null, null));
            return responseBean;
        }

        bulkHeader = bulkHeaderRepository.save(bulkHeader);
        if (bulkHeader != null) {
            responseBean.setResponseBody(bulkHeader);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.BULKHEADER_UPDATED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.BULKHEADER_UPDATED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<BulkHeader> deleteBulkHeader(Long bulkHeaderId) {
        ResponseBean responseBean = new ResponseBean();
        Optional<BulkHeader> bulkHeader = bulkHeaderRepository.findById(bulkHeaderId);
        if (!bulkHeader.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.BULKHEADER_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        //BulkHeader findBulkHeader = bulkHeader.get();
        bulkHeader.get().setActive(AppProperty.IN_ACTIVE);
        bulkHeaderRepository.save(bulkHeader.get());
        responseBean.setResponseBody(bulkHeader.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.BULKHEADER_DELETE_MSG, null, null));
        return responseBean;
    }

    @Override
    public ResponseBean<BulkHeader> getAllBulkHeader() {
        ResponseBean responseBean = new ResponseBean();
        List<BulkHeader> bulkHeaderList = (List<BulkHeader>)bulkHeaderRepository.findAll();
        if(bulkHeaderList !=null && !bulkHeaderList.isEmpty()){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG, null, null));
            responseBean.setResponseBody(bulkHeaderList);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public DataTablesOutput<BulkHeader> getAllBulkHeader(DataTablesInput input) {
        return bulkHeaderRepository.findAll(input);
    }


    @Override
    public ResponseBean<BulkHeader> findByIdBulkHeader(Long id) {
        ResponseBean responseBean = new ResponseBean();
        Optional<BulkHeader> bulkHeader = bulkHeaderRepository.findById(id);
        if (!bulkHeader.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.BULKHEADER_ID_NOT_FOUND, null, null));
            return responseBean;
        }

        responseBean.setResponseBody(bulkHeader.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.BULKHEADER_ID_FOUND, null, null));
        return responseBean;
    }
    @Override
    public DataTableResult  getAll(Integer pageNo, Integer pageSize,Integer draw, String sortBy)
    {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        ResponseBean responseBean = new ResponseBean();
        Page<BulkHeader> pagedResult = bulkHeaderRepository.findAll(paging);
        DataTableResult dataTableResult = new DataTableResult();
        dataTableResult.setData(pagedResult);
        dataTableResult.setDraw(draw);
        dataTableResult.setLength(pageSize);
        dataTableResult.setRecordsTotal(pagedResult.getContent().size());
        dataTableResult.setRecordsFiltered(pagedResult.getTotalPages());
        return dataTableResult;
    }
}
