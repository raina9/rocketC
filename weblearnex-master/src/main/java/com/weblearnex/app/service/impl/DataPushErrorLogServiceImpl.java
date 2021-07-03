package com.weblearnex.app.service.impl;

import com.weblearnex.app.constant.UserType;
import com.weblearnex.app.datatable.reposatory.DataPushErrorLogRepository;
import com.weblearnex.app.entity.logs.DataPushErrorLog;
import com.weblearnex.app.model.SessionUserBean;
import com.weblearnex.app.service.DataPushErrorLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

@Service
public class DataPushErrorLogServiceImpl implements DataPushErrorLogService {

    @Autowired
    private DataPushErrorLogRepository dataPushErrorLogRepository;

    @Autowired
    private SessionUserBean sessionUserBean;

    @Override
    public DataTablesOutput<DataPushErrorLog> getAllDataPushErrorLogPaginationAndSort(DataTablesInput input) {
        if(input.getColumn("id") == null){
            input.addColumn("id", true, true,null);
        }
        input.addOrder("id",false);
        if(sessionUserBean.getUser().getType().equals(UserType.CLIENT)){
            input.addColumn("clientCode", true, false, sessionUserBean.getUser().getClientCode());
        }
        return dataPushErrorLogRepository.findAll(input);
    }
}
