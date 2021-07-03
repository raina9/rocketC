package com.weblearnex.app.service;

import com.weblearnex.app.constant.TrackingType;
import com.weblearnex.app.entity.master.CourierPriority;
import com.weblearnex.app.entity.order.PacketHistory;
import com.weblearnex.app.entity.order.SaleOrder;
import com.weblearnex.app.entity.remittance.ClientRemittance;
import com.weblearnex.app.entity.setup.Client;
import com.weblearnex.app.entity.setup.Courier;
import com.weblearnex.app.entity.setup.Status;
import com.weblearnex.app.model.ResponseBean;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SaleOrderService extends BulkUploadService{
    ResponseBean<SaleOrder> addSaleOrder(SaleOrder saleOrder, boolean isValidatioCheck);
    ResponseBean<SaleOrder> saleOrderValidator(SaleOrder saleOrder);
    ResponseBean<SaleOrder> addPacketHistory(SaleOrder saleOrder, Status toStatus, String ndrCode, Date date, String location);
    ResponseBean<SaleOrder> addPacketHistoryWithoutSave(SaleOrder saleOrder, Status toStatus, String ndrCode, Date date, String location);
    ResponseBean<List<SaleOrder>> getAllSaleOrder();
    ResponseBean<SaleOrder> findByAwbNumbers(String referanceNo);
    ResponseBean<SaleOrder> findByCourierAwbNumber(String courierAWBNumber);
    DataTablesOutput<SaleOrder> getAllOrderReceived(DataTablesInput input);
    ResponseBean assigneeCourier(String awbNumber, String courierCode, String warehouseCode, SaleOrder saleOrder);
    ResponseBean assigneeCourierBulk(Map<String, Object> map);
    ResponseBean assigneeCourierByPriority(Map<String, Object> map);
    DataTablesOutput<SaleOrder> getAllOrderProcess(DataTablesInput input);
    DataTablesOutput<SaleOrder> getAllOrderAssigned(DataTablesInput input);
    DataTablesOutput<SaleOrder> getAllPendingFor3PlManifest(DataTablesInput input);
    DataTablesOutput<SaleOrder> getAllPendingForClientRemittance(DataTablesInput input);
    DataTablesOutput<SaleOrder> getAllPendingForCourierRemittance(DataTablesInput input);
    PacketHistory getFirstAttemptedHistory(SaleOrder saleOrder);
    PacketHistory getLastAttemptedHistory(SaleOrder saleOrder);
    ResponseEntity<Resource> downloadOrderReceivedReport();
    ResponseEntity<Resource> downloadOrderProcessReport();
    ResponseEntity<Resource> downloadOrderAssignedReport();
    ResponseBean<Map<String,SaleOrder>> tracking(TrackingType trackingType, String referanceNo);

    DataTablesOutput<SaleOrder> getInTransitOrder(DataTablesInput input);
    DataTablesOutput<SaleOrder> getDeliverdOrder(DataTablesInput input);
    DataTablesOutput<SaleOrder> getOutForDeliveryOrder(DataTablesInput input);
    DataTablesOutput<SaleOrder> getRTOOrder(DataTablesInput input);
    DataTablesOutput<SaleOrder> getCancelledOrder(DataTablesInput input);
    DataTablesOutput<SaleOrder> getDeliveryAttemptedOrder(DataTablesInput input);

    ResponseEntity<Resource> downloadInTransitOrderReport();
    ResponseEntity<Resource> downloadDeliverdOrderReport();
    ResponseEntity<Resource> downloadOutForDeliveryOrderReport();
    ResponseEntity<Resource> downloadRTOOrderReport();
    ResponseEntity<Resource> downloadCancelledOrderReport();
    ResponseEntity<Resource> downloadDeliveryAttemptedOrderReport();
}
