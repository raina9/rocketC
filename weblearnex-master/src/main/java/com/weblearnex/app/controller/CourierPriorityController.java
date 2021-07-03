package com.weblearnex.app.controller;

import com.weblearnex.app.entity.master.BulkMaster;
import com.weblearnex.app.entity.master.CourierPriority;
import com.weblearnex.app.entity.master.ServiceType;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.CourierPriorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CourierPriorityController {

    @Autowired
    private CourierPriorityService courierPriorityService;

    @PostMapping("/addCourierPriority")
    public ResponseEntity<ResponseBean> addCourierPriority(@RequestBody CourierPriority courierPriority) {
        return new ResponseEntity<ResponseBean>(courierPriorityService.addCourierPriority(courierPriority), HttpStatus.OK);
    }
    @PutMapping("/updateCourierPriority")
    public ResponseEntity<ResponseBean> updateCourierPriority(@RequestBody CourierPriority courierPriority) {
        return new ResponseEntity<ResponseBean>(courierPriorityService.updateCourierPriority(courierPriority), HttpStatus.OK);
    }
    @DeleteMapping("/deleteCourierPriority/{CourierPriorityId}")
    public ResponseEntity<ResponseBean> deleteCourierPriority(@PathVariable Long courierPriorityId) {
        return new ResponseEntity<ResponseBean>(courierPriorityService.deleteCourierPriority(courierPriorityId), HttpStatus.OK);
    }
    @GetMapping("/getAllCourierPriority")
    public ResponseEntity<ResponseBean> getAllCourierPriority() {
        return new ResponseEntity<ResponseBean>(courierPriorityService.getAllCourierPriority(), HttpStatus.OK);
    }
    @GetMapping("/findByCourierPriorityId/{id}")
    public ResponseEntity<ResponseBean> findByCourierPriorityId(@PathVariable(value = "id")Long id) {
        return new ResponseEntity<ResponseBean>(courierPriorityService.findByCourierPriorityId(id), HttpStatus.OK);
    }
    @PostMapping(value = "/courierPriority/getAllPaginationAndSort")
    public DataTablesOutput<CourierPriority> getAllCourierPriorityPaginationAndSort(@RequestBody DataTablesInput input) {
        return courierPriorityService.getAllCourierPriorityPaginationAndSort(input);
    }

}
