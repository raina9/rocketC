package com.weblearnex.app.api;

import com.weblearnex.app.entity.master.ApiConfig;
import com.weblearnex.app.model.ResponseBean;

public interface ApiCallService {
    ResponseBean GetApiCall(ApiConfig apiConfig);
    ResponseBean PostApiCall(ApiConfig apiConfig, Object requestObject);
}
