package com.weblearnex.app.controller;

import com.weblearnex.app.constant.StatusType;
import com.weblearnex.app.entity.master.BulkMaster;
import com.weblearnex.app.entity.setup.Status;
import com.weblearnex.app.entity.setup.StatusFlow;
import com.weblearnex.app.entity.setup.StatusTransition;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.StatusFlowService;
import com.weblearnex.app.service.StatusService;
import com.weblearnex.app.service.StatusTransitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class StatusController {

    @Autowired
    private StatusService statusService;
    @Autowired
    private StatusTransitionService statusTransitionService;
    @Autowired
    private StatusFlowService statusFlowService;

    @PostMapping("/addStatus")
    public ResponseEntity<ResponseBean> addStatus(@RequestBody Status status) {
        return new ResponseEntity<ResponseBean>(statusService.addStatus(status),HttpStatus.OK);
    }

    @PutMapping("/updateStatus")
    public ResponseEntity<ResponseBean> updateStatus(@RequestBody Status status) {
        return new ResponseEntity<ResponseBean>(statusService.updateStatus(status), HttpStatus.OK);
    }


    @DeleteMapping("/deleteStatus/{id}")
    public ResponseEntity<ResponseBean> deleteStatus(@PathVariable("id") Long id ) {
        return new ResponseEntity<ResponseBean>(statusService.deleteStatus(id), HttpStatus.OK);
    }
    @GetMapping("/getAllStatus")
    public ResponseEntity<ResponseBean> getAllStatus() {
        return new ResponseEntity<ResponseBean>(statusService.getAllStatus(), HttpStatus.OK);
    }

    @GetMapping("/getAllStatusByType/{statusType}")
    public ResponseEntity<ResponseBean> getAllStatusByType(@PathVariable("statusType") StatusType statusType ) {
        return new ResponseEntity<ResponseBean>(statusService.getAllStatusByType(statusType), HttpStatus.OK);
    }
    @GetMapping("/findBystatusId/{id}")
    public ResponseEntity<ResponseBean> findBystatusId(@PathVariable(value = "id")Long id) {
        return new ResponseEntity<ResponseBean>(statusService.findByStatusId(id), HttpStatus.OK);
    }
    @PostMapping("/addStatusTransition")
    public ResponseEntity<ResponseBean> addStatusTransition(@RequestBody StatusTransition statusTransition) {
        return new ResponseEntity<ResponseBean>(statusTransitionService.addStatusTransition(statusTransition), HttpStatus.OK);
    }
    @PutMapping("/updateStatusTransition")
    public ResponseEntity<ResponseBean> updateStatusTransition(@RequestBody StatusTransition statusTransition) {
        return new ResponseEntity<ResponseBean>(statusTransitionService.updateStatusTransition(statusTransition), HttpStatus.OK);
    }
    @DeleteMapping("/deleteStatusTransition/{Id}")
    public ResponseEntity<ResponseBean> deleteStatusTransition(@PathVariable Long Id) {
        return new ResponseEntity<ResponseBean>(statusTransitionService.deleteStatusTransition(Id), HttpStatus.OK);
    }
    @GetMapping("/getAllStatusTransition")
    public ResponseEntity<ResponseBean> getAllStatusTransition() {
        return new ResponseEntity<ResponseBean>(statusTransitionService.getAllStatusTransition(), HttpStatus.OK);
    }

    @GetMapping("/findByStatusTransitionId/{id}")
    public ResponseEntity<ResponseBean> findByStatusTransitionId(@PathVariable(value = "id")Long id) {
        return new ResponseEntity<ResponseBean>(statusTransitionService.findByStatusTransitionId(id), HttpStatus.OK);
    }
    @PostMapping("/addStatusFlow")
    public ResponseEntity<ResponseBean> addStatusFlow(@RequestBody StatusFlow statusFlow){
        return new ResponseEntity<ResponseBean>(statusFlowService.addStatusFlow(statusFlow), HttpStatus.OK);
    }

    @PutMapping("/updateStatusFlow")
    public ResponseEntity<ResponseBean> updateStatusFlow(@RequestBody StatusFlow statusFlow){
        return new ResponseEntity<ResponseBean>(statusFlowService.updateStatusFlow(statusFlow), HttpStatus.OK);
    }
    @DeleteMapping("/deleteStatusFlow/{id}")
    public ResponseEntity<ResponseBean>deleteStatusFlow(@PathVariable Long id){
        return new ResponseEntity<ResponseBean>(statusFlowService.deleteStatusFlow(id),HttpStatus.OK);
    }


    @GetMapping("/getAllStatusFlow")
    public ResponseEntity<ResponseBean> getAllStatusFlow() {
        return new ResponseEntity<ResponseBean>(statusFlowService.getAllStatusFlow(), HttpStatus.OK);
    }
    @GetMapping("/findByStatusFlowId/{id}")
    public ResponseEntity<ResponseBean> findByStatusFlowId(@PathVariable(value = "id")Long id) {
        return new ResponseEntity<ResponseBean>(statusFlowService.findByStatusFlowId(id), HttpStatus.OK);
    }
    @PostMapping(value = "/status/getAllPaginationAndSort")
    public DataTablesOutput<Status> getAllStatusPaginationAndSort(@RequestBody DataTablesInput input) {
        return statusService.getAllStatusPaginationAndSort(input);
    }
    @PostMapping(value = "/statusTransition/getAllPaginationAndSort")
    public DataTablesOutput<StatusTransition> getAllStatusTransitionPaginationAndSort(@RequestBody DataTablesInput input) {
        return statusTransitionService.getAllStatusTransitionPaginationAndSort(input);
    }
    @PostMapping(value = "/statusFlow/getAllPaginationAndSort")
    public DataTablesOutput<StatusFlow> getAllStatusFlowPaginationAndSort(@RequestBody DataTablesInput input) {
        return statusFlowService.getAllStatusFlowPaginationAndSort(input);
    }

}
