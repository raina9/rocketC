package com.weblearnex.app.controller;


import com.weblearnex.app.entity.master.BulkMaster;
import com.weblearnex.app.entity.master.CourierStatusMapping;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.CourierStatusMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CourierStatusMappingController {

    @Autowired
    private CourierStatusMappingService courierStatusMappingService;
    @PostMapping("/addCourierStatusMapping")
    public ResponseEntity<ResponseBean> addCourierStatusMapping(@RequestBody CourierStatusMapping courierStatusMapping) {
        return new ResponseEntity<ResponseBean>(courierStatusMappingService.addCourierStatusMapping(courierStatusMapping), HttpStatus.OK);
    }
    @PutMapping("/updateCourierStatusMapping")
    public ResponseEntity<ResponseBean> updateCourierStatusMapping(@RequestBody CourierStatusMapping courierStatusMapping) {
        return new ResponseEntity<ResponseBean>(courierStatusMappingService.updateCourierStatusMapping(courierStatusMapping), HttpStatus.OK);
    }
    @DeleteMapping("/deleteCourierStatusMapping/{CourierStatusMappingId}")
    public ResponseEntity<ResponseBean> deleteCourierStatusMapping(@PathVariable(value="CourierStatusMappingId") Long courierStatusMappingId) {
        return new ResponseEntity<ResponseBean>(courierStatusMappingService.deleteCourierStatusMapping(courierStatusMappingId), HttpStatus.OK);
    }
    @GetMapping("/getAllCourierStatusMapping")
    public ResponseEntity<ResponseBean> getAllCourierStatusMapping() {
        return new ResponseEntity<ResponseBean>(courierStatusMappingService.getAllCourierStatusMapping(), HttpStatus.OK);
    }
    @GetMapping("/findByCourierStatusMappingId/{id}")
    public ResponseEntity<ResponseBean> findByCourierStatusMappingId(@PathVariable(value = "id")Long id) {
        return new ResponseEntity<ResponseBean>(courierStatusMappingService.findByCourierStatusMappingId(id), HttpStatus.OK);
    }
    @PostMapping(value = "/courierStatusMapping/getAllPaginationAndSort")
    public DataTablesOutput<CourierStatusMapping> getAllCourierStatusMappingPaginationAndSort(@RequestBody DataTablesInput input) {
        return courierStatusMappingService.getAllCourierStatusMappingPaginationAndSort(input);
    }
}
