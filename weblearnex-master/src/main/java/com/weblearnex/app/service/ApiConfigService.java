package com.weblearnex.app.service;

import com.weblearnex.app.entity.master.ApiConfig;
import com.weblearnex.app.model.ResponseBean;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

public interface ApiConfigService {
    ResponseBean<ApiConfig> addApiConfig(ApiConfig apiConfig);
    ResponseBean<ApiConfig> updateApiConfig(ApiConfig apiConfig);
    ResponseBean<ApiConfig> deleteApiConfig(Long apiConfigId);
    ResponseBean<ApiConfig> getAllApiConfig();
    ResponseBean<ApiConfig> findByApiConfigId(Long id);
    DataTablesOutput<ApiConfig> getAllApiConfigPaginationAndSort(DataTablesInput input);
}
