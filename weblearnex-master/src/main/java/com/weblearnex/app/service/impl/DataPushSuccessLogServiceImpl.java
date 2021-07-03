package com.weblearnex.app.service.impl;

import com.weblearnex.app.constant.UserType;
import com.weblearnex.app.datatable.reposatory.DataPushSuccessLogRepository;
import com.weblearnex.app.entity.logs.DataPushSuccessLog;
import com.weblearnex.app.model.SessionUserBean;
import com.weblearnex.app.service.DataPushSuccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

@Service
public class DataPushSuccessLogServiceImpl implements DataPushSuccessLogService {


    @Autowired
    private DataPushSuccessLogRepository dataPushSuccessLogRepository;

    @Autowired
    private SessionUserBean sessionUserBean;

    @Override
    public DataTablesOutput<DataPushSuccessLog> getAllDataPushSuccessLogPaginationAndSort(DataTablesInput input) {
        if(input.getColumn("id") == null){
            input.addColumn("id", true, true,null);
        }
        input.addOrder("id",false);
        if(sessionUserBean.getUser().getType().equals(UserType.CLIENT)){
            input.addColumn("clientCode", true, false, sessionUserBean.getUser().getClientCode());
        }
        return dataPushSuccessLogRepository.findAll(input);
    }
}
