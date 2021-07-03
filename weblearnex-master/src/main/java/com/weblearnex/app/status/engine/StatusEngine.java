package com.weblearnex.app.status.engine;

import com.weblearnex.app.entity.setup.Status;
import com.weblearnex.app.model.ResponseBean;

public interface StatusEngine {

    public ResponseBean reloadStatusFlow();
    public boolean isValidStatus(String statusFlowName, Status fromStatus, Status toStatus);
}
