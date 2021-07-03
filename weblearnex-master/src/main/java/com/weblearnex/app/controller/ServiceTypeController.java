package com.weblearnex.app.controller;
import com.weblearnex.app.entity.master.BulkMaster;
import com.weblearnex.app.entity.master.ServiceType;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.ServiceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ServiceTypeController {

    @Autowired
    private ServiceTypeService serviceTypeService;


    @PostMapping("/addServiceType")
    public ResponseEntity<ResponseBean> addServiceType(@RequestBody ServiceType serviceType) {
        return new ResponseEntity<ResponseBean>(serviceTypeService.addServiceType(serviceType), HttpStatus.OK);
    }
    @PutMapping("/updateServiceType")
    public ResponseEntity<ResponseBean> updateServiceType(@RequestBody ServiceType serviceType) {
        return new ResponseEntity<ResponseBean>(serviceTypeService.updateServiceType(serviceType), HttpStatus.OK);
    }
    @DeleteMapping("/deleteServiceType/{serviceTypeId}")
    public ResponseEntity<ResponseBean> deleteServiceType(@PathVariable Long serviceTypeId) {
        return new ResponseEntity<ResponseBean>(serviceTypeService.deleteServiceType(serviceTypeId), HttpStatus.OK);
    }
    @GetMapping("/getAllServiceType")
    public ResponseEntity<ResponseBean> getAllServiceType() {
        return new ResponseEntity<ResponseBean>(serviceTypeService.getAllServiceType(), HttpStatus.OK);
    }
    @GetMapping("/findByServiceTypeId/{id}")
    public ResponseEntity<ResponseBean> findByServiceTypeId(@PathVariable(value = "id")Long id) {
        return new ResponseEntity<ResponseBean>(serviceTypeService.findByServiceTypeId(id), HttpStatus.OK);
    }
    @PostMapping(value = "/serviceType/getAllPaginationAndSort")
    public DataTablesOutput<ServiceType> getAllServiceTypePaginationAndSort(@RequestBody DataTablesInput input) {
        return serviceTypeService.getAllServiceTypePaginationAndSort(input);
    }
    @GetMapping("/getClientServiceCourier")
    public ResponseEntity<ResponseBean> getClientServiceCourier() {
        return new ResponseEntity<ResponseBean>(serviceTypeService.getClientServiceCourier(), HttpStatus.OK);
    }

}
