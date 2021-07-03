package com.weblearnex.app.service;

import com.weblearnex.app.entity.order.PacketHistory;
import com.weblearnex.app.model.ResponseBean;

public interface PacketHistoryService extends BulkUploadService{
    ResponseBean<PacketHistory> updatePacketStatus (String awbs, String status);
}
