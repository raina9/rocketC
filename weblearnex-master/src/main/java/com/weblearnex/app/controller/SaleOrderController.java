package com.weblearnex.app.controller;

import com.weblearnex.app.constant.TrackingType;
import com.weblearnex.app.entity.order.SaleOrder;
import com.weblearnex.app.entity.order.SaleOrderTransactionLog;
import com.weblearnex.app.entity.paymentwetway.PaymentGetWayLog;
import com.weblearnex.app.entity.remittance.ClientRemittance;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.SaleOrderService;
import com.weblearnex.app.service.SaleOrderTransactionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class SaleOrderController {

    @Autowired
    private SaleOrderService saleOrderService;

    @Autowired
    private SaleOrderTransactionLogService saleOrderTransactionLogService;

    @PostMapping("/addSaleOrder")
    public ResponseEntity<ResponseBean> addSaleOrder(@RequestBody SaleOrder saleOrder) {
        return new ResponseEntity<ResponseBean>(saleOrderService.addSaleOrder(saleOrder, true), HttpStatus.OK);
    }
    @GetMapping("/getAllSaleOrders")
    public ResponseEntity<ResponseBean> getAllCourier() {
        return new ResponseEntity<ResponseBean>(saleOrderService.getAllSaleOrder(), HttpStatus.OK);
    }
    @GetMapping("/findByAwbNumbers/{referanceNos}")
    public ResponseEntity<ResponseBean> findByAwbNumber(@PathVariable(value = "referanceNos")String referanceNo) {
        return new ResponseEntity<ResponseBean>(saleOrderService.findByAwbNumbers(referanceNo), HttpStatus.OK);
    }
    @GetMapping("/findByCourierAwbNumber/{courierAWBNumber}")
    public ResponseEntity<ResponseBean> findByCourierAwbNumber(@PathVariable(value = "courierAWBNumber")String courierAWBNumber) {
        return new ResponseEntity<ResponseBean>(saleOrderService.findByCourierAwbNumber(courierAWBNumber), HttpStatus.OK);
    }

    @PostMapping(value = "/getAllOrderReceived")
    public DataTablesOutput<SaleOrder> getAllOrderReceived(@RequestBody DataTablesInput input) {
        return saleOrderService.getAllOrderReceived(input);
    }

    @GetMapping(value = "/downloadOrderReceivedReport")
    public ResponseEntity<Resource> downloadOrderReceivedReport(HttpServletRequest request, HttpServletResponse response) {
        return saleOrderService.downloadOrderReceivedReport();
    }

    @GetMapping(value = "/downloadOrderProcessReport")
    public ResponseEntity<Resource> downloadOrderProcessReport(HttpServletRequest request, HttpServletResponse response) {
        return saleOrderService.downloadOrderProcessReport();
    }

    @GetMapping(value = "/downloadOrderAssignedReport")
    public ResponseEntity<Resource> downloadOrderAssignedReport(HttpServletRequest request, HttpServletResponse response) {
        return saleOrderService.downloadOrderAssignedReport();
    }

    @PostMapping("/addSaleOrderTansaction")
    public ResponseEntity<ResponseBean> addSaleOrderTansaction(@RequestBody SaleOrderTransactionLog saleOrderTransactionLog) {
        return new ResponseEntity<ResponseBean>(saleOrderTransactionLogService.addSaleOrderTansaction(saleOrderTransactionLog), HttpStatus.OK);
    }

    @PostMapping(value = "/getAllSaleOrderTransactions")
    public DataTablesOutput<SaleOrderTransactionLog> getAllSaleOrderTransactions(@RequestBody DataTablesInput input) {
        return saleOrderTransactionLogService.getAllSaleOrderTransactions(input);
    }

    @GetMapping("/assigneeCourier")
    public ResponseEntity<ResponseBean> assigneeCourier(@RequestParam(value = "awb")String awb,
                                                        @RequestParam(value = "courierCode")String courierCode,
                                                        @RequestParam(value = "warehouseCode")String warehouseCode) {
        return new ResponseEntity<ResponseBean>(saleOrderService.assigneeCourier(awb, courierCode, warehouseCode, null), HttpStatus.OK);
    }

    @PostMapping("/assigneeCourierBulk")
    public ResponseEntity<ResponseBean> assigneeCourierBulk(@RequestBody Map<String, Object> map) {
        return new ResponseEntity<ResponseBean>(saleOrderService.assigneeCourierBulk(map), HttpStatus.OK);
    }
    @PostMapping("/assigneeCourierByPriority")
    public ResponseEntity<ResponseBean> assigneeCourierByPriority(@RequestBody Map<String, Object> map) {
        return new ResponseEntity<ResponseBean>(saleOrderService.assigneeCourierByPriority(map), HttpStatus.OK);
    }

    @PostMapping(value = "/getAllOrderProcess")
    public DataTablesOutput<SaleOrder> getAllOrderProcess(@RequestBody DataTablesInput input) {
        return saleOrderService.getAllOrderProcess(input);
    }

    @PostMapping(value = "/getAllOrderAssigned")
    public DataTablesOutput<SaleOrder> getAllOrderAssigned(@RequestBody DataTablesInput input) {
        return saleOrderService.getAllOrderAssigned(input);
    }
    @PostMapping("/getAllPendingFor3PlManifest")
    public  DataTablesOutput<SaleOrder> getAllPendingFor3PlManifest(@RequestBody DataTablesInput input) {
        return saleOrderService.getAllPendingFor3PlManifest(input);
    }

    @PostMapping("/clientWalletManualRecharge")
    public ResponseEntity<ResponseBean> clientWalletManualRecharge(@RequestBody SaleOrderTransactionLog saleOrderTransactionLog) {
        return new ResponseEntity<ResponseBean>(saleOrderTransactionLogService.clientWalletManualRecharge(saleOrderTransactionLog), HttpStatus.OK);
    }

    @PostMapping(value = "/getAllPendingForClientRemittance")
    public DataTablesOutput<SaleOrder> getAllPendingForClientRemittance(@RequestBody DataTablesInput input) {
        return saleOrderService.getAllPendingForClientRemittance(input);
    }

    @PostMapping(value = "/getAllPendingForCourierRemittance")
    public DataTablesOutput<SaleOrder> getAllPendingForCourierRemittance(@RequestBody DataTablesInput input) {
        return saleOrderService.getAllPendingForCourierRemittance(input);
    }

    @PostMapping(value = "/getInTransitOrder")
    public DataTablesOutput<SaleOrder> getInTransitOrder(@RequestBody DataTablesInput input) {
        return saleOrderService.getInTransitOrder(input);
    }
    @PostMapping(value = "/getDeliverdOrder")
    public DataTablesOutput<SaleOrder> getDeliverdOrder(@RequestBody DataTablesInput input) {
        return saleOrderService.getDeliverdOrder(input);
    }
    @PostMapping(value = "/getOutForDeliveryOrder")
    public DataTablesOutput<SaleOrder> getOutForDeliveryOrder(@RequestBody DataTablesInput input) {
        return saleOrderService.getOutForDeliveryOrder(input);
    }
    @PostMapping(value = "/getRTOOrder")
    public DataTablesOutput<SaleOrder> getRTOOrder(@RequestBody DataTablesInput input) {
        return saleOrderService.getRTOOrder(input);
    }
    @PostMapping(value = "/getCancelledOrder")
    public DataTablesOutput<SaleOrder> getCancelledOrder(@RequestBody DataTablesInput input) {
        return saleOrderService.getCancelledOrder(input);
    }
    @PostMapping("/tracking")
    public ResponseEntity<ResponseBean> tracking(@RequestBody String searchValue,@RequestParam (value = "trackingType")TrackingType trackingType) {
        return new ResponseEntity<ResponseBean>(saleOrderService.tracking(trackingType,searchValue), HttpStatus.OK);
    }
    @PostMapping(value = "/getDeliveryAttemptedOrder")
    public DataTablesOutput<SaleOrder> getDeliveryAttemptedOrder(@RequestBody DataTablesInput input) {
        return saleOrderService.getDeliveryAttemptedOrder(input);
    }


    @GetMapping(value = "/downloadInTransitOrderReport")
    public ResponseEntity<Resource> downloadInTransitOrderReport(HttpServletRequest request, HttpServletResponse response) {
        return saleOrderService.downloadInTransitOrderReport();
    }
    @GetMapping(value = "/downloadDeliverdOrderReport")
    public ResponseEntity<Resource> downloadDeliverdOrderReport(HttpServletRequest request, HttpServletResponse response) {
        return saleOrderService.downloadDeliverdOrderReport();
    }
    @GetMapping(value = "/downloadOutForDeliveryOrderReport")
    public ResponseEntity<Resource> downloadOutForDeliveryOrderReport(HttpServletRequest request, HttpServletResponse response) {
        return saleOrderService.downloadOutForDeliveryOrderReport();
    }
    @GetMapping(value = "/downloadRTOOrderReport")
    public ResponseEntity<Resource> downloadRTOOrderReport(HttpServletRequest request, HttpServletResponse response) {
        return saleOrderService.downloadRTOOrderReport();
    }
    @GetMapping(value = "/downloadCancelledOrderReport")
    public ResponseEntity<Resource> downloadCancelledOrderReport(HttpServletRequest request, HttpServletResponse response) {
        return saleOrderService.downloadCancelledOrderReport();
    }
    @GetMapping(value = "/downloadDeliveryAttemptedOrderReport")
    public ResponseEntity<Resource> downloadDeliveryAttemptedOrderReport(HttpServletRequest request, HttpServletResponse response) {
        return saleOrderService.downloadDeliveryAttemptedOrderReport();
    }

    @GetMapping("/deletesSaleOrderTransactionLog")
    public ResponseEntity<ResponseBean> deletesSaleOrderTransactionLog(@RequestParam(value = "ids")String ids) {
        return new ResponseEntity<ResponseBean>(saleOrderTransactionLogService.deletesSaleOrderTransactionLog(ids), HttpStatus.OK);
    }

    @GetMapping(value="/saleOrderTransactionLogReport")
    public ResponseEntity<Resource> saleOrderTransactionLogReport(HttpServletRequest request, HttpServletResponse response){
        return saleOrderTransactionLogService.downloadSaleOrderTransactionsReport();
    }

}
