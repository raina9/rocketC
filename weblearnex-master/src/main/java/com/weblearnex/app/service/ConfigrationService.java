package com.weblearnex.app.service;

import com.weblearnex.app.entity.setup.Configration;
import com.weblearnex.app.model.ResponseBean;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

public interface ConfigrationService {

    ResponseBean<Configration> addConfigration(Configration configration);
    ResponseBean<Configration> updateConfigration(Configration configration);
    ResponseBean<Configration> deleteConfigration(Long configrationId);
    ResponseBean<Configration> getAllConfigration();
    ResponseBean<Configration> findByConfigId(Long configrationId);

    DataTablesOutput<Configration> getAllConfigrationPaginationAndSort(DataTablesInput input);
}
