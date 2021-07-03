package com.weblearnex.app.controller;


import com.weblearnex.app.constant.PaymentType;
import com.weblearnex.app.entity.master.BulkMaster;
import com.weblearnex.app.entity.setup.Courier;
import com.weblearnex.app.model.CourierServiceabilityBean;
import com.weblearnex.app.model.CourierServiceabilityResponseBean;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.CourierService;
import com.weblearnex.app.service.PrintLabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
public class CourierController {

    @Autowired
    private CourierService courierService;

    @Autowired
    private PrintLabelService printLabelService;

    @PostMapping("/addCourier")
    public ResponseEntity<ResponseBean> addCourier(@RequestBody Courier courier) {
        return new ResponseEntity<ResponseBean>(courierService.addCourier(courier), HttpStatus.OK);
    }
    @PutMapping("/updateCourier")
    public ResponseEntity<ResponseBean> updateCourier(@RequestBody Courier courier) {
        return new ResponseEntity<ResponseBean>(courierService.updateCourier(courier), HttpStatus.OK);
    }
    @DeleteMapping("/deleteCourier/{id}")
    public ResponseEntity<ResponseBean> deleteCourier(@PathVariable Long id) {
        return new ResponseEntity<ResponseBean>(courierService.deleteCourier(id), HttpStatus.OK);
    }
    @GetMapping("/getAllCourier")
    public ResponseEntity<ResponseBean> getAllCourier() {
        return new ResponseEntity<ResponseBean>(courierService.getAllCourier(), HttpStatus.OK);
    }

    @GetMapping("/findByCourierId/{id}")
    public ResponseEntity<ResponseBean> findByCourierId(@PathVariable(value = "id")Long id) {
        return new ResponseEntity<ResponseBean>(courierService.findByCourierId(id), HttpStatus.OK);
    }
    @GetMapping("/findByActiveCourier")
    public ResponseEntity<ResponseBean> findByActiveCourier() {
        return new ResponseEntity<ResponseBean>(courierService.findByActive(), HttpStatus.OK);
    }
    @PostMapping(value = "/courier/getAllPaginationAndSort")
    public DataTablesOutput<Courier> getAllCourierPaginationAndSort(@RequestBody DataTablesInput input) {
        return courierService.getAllCourierPaginationAndSort(input);
    }
    @PostMapping("/getServiceableCouriers")
    public ResponseEntity<ResponseBean> getServiceableCouriers(@RequestBody CourierServiceabilityBean serviceabilityBean) {
        return new ResponseEntity<ResponseBean>(courierService.courierServiceabilityCheck(serviceabilityBean), HttpStatus.OK);
    }
    @GetMapping("/clientServiceProviders")
    public ResponseEntity<ResponseBean> clientServiceProviders(@RequestParam(value="awb") String awb, @RequestParam(value="warehouseCode") String clientWarehouseCode) {
        return new ResponseEntity<ResponseBean>(courierService.clientServiceProviders(awb, clientWarehouseCode), HttpStatus.OK);
    }

    @GetMapping("/getServiceProvidersForBulk")
    public ResponseEntity<ResponseBean> getServiceProvidersForBulk() {
        return new ResponseEntity<ResponseBean>(courierService.getServiceProvidersForBulk(), HttpStatus.OK);
    }

    @RequestMapping(value="/generateOrderPrintLabel", method = RequestMethod.POST)
    public ResponseEntity<Resource> generateOrderPrintLabel(@RequestBody String awbNumber){
        return printLabelService.generateOrderPrintLabel(awbNumber);
    }

    @RequestMapping(value="/generateInvoicePrintLabel", method = RequestMethod.POST)
    public ResponseEntity<Resource> generateInvoicePrintLabel(@RequestBody String awbNumber){
        return printLabelService.generateInvoicePrintLabel(awbNumber);
    }

    @GetMapping(value="/courierReport")
    public ResponseEntity<Resource> courierReport(HttpServletRequest request, HttpServletResponse response){
        return courierService.courierReport();
    }

    @GetMapping("/getAllCourierByServiceWise")
    public ResponseEntity<ResponseBean> getAllCourierByServiceWise() {
        return new ResponseEntity<ResponseBean>(courierService.getAllCourierByServiceWise(), HttpStatus.OK);
    }
}
