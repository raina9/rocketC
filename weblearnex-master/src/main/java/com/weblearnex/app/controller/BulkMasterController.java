package com.weblearnex.app.controller;


import com.weblearnex.app.entity.master.BulkHeader;
import com.weblearnex.app.entity.master.BulkMaster;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.BulkMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
public class BulkMasterController {

    @Autowired
    private BulkMasterService bulkMasterService;

    @PostMapping("/addBulkMaster")
    public ResponseEntity<ResponseBean> addBulkMaster(@RequestBody BulkMaster bulkMaster) {
        return new ResponseEntity<ResponseBean>(bulkMasterService.addBulkMaster(bulkMaster), HttpStatus.OK);
    }
    @PutMapping("/updateBulkMaster")
    public ResponseEntity<ResponseBean> updateBulkMaster(@RequestBody BulkMaster bulkMaster) {
        return new ResponseEntity<ResponseBean>(bulkMasterService.updateBulkMaster(bulkMaster), HttpStatus.OK);
    }

    @DeleteMapping("/deleteBulkMaster/{bulkMasterId}")
    public ResponseEntity<ResponseBean> deleteBulkMaster(@PathVariable Long bulkMasterId) {
        return new ResponseEntity<ResponseBean>(bulkMasterService.deleteBulkMaster(bulkMasterId), HttpStatus.OK);
    }

    @GetMapping("/getAllBulkMaster")
    public ResponseEntity<ResponseBean> getAllBulkMaster() {
        return new ResponseEntity<ResponseBean>(bulkMasterService.getAllBulkMaster(), HttpStatus.OK);
    }
    @GetMapping("/findByBulkMasterId/{id}")
    public ResponseEntity<ResponseBean> findByBulkMasterId(@PathVariable(value = "id")Long id) {
        return new ResponseEntity<ResponseBean>(bulkMasterService.findByBulkMasterId(id), HttpStatus.OK);
    }
    @PostMapping(value = "/bulkMaster/getAllPaginationAndSort")
    public DataTablesOutput<BulkMaster> getAllBulkMasterPaginationAndSort(@RequestBody DataTablesInput input) {
        return bulkMasterService.getAllBulkMasterPaginationAndSort(input);
    }

    @GetMapping("/getUploadProgress")
    public ResponseEntity<ResponseBean> getUploadProgress(@RequestParam(value = "key") String bulkUpload, @RequestParam(value = "token", required = false) String token) {
        return new ResponseEntity<ResponseBean>(bulkMasterService.getUploadProgressCount(bulkUpload, token), HttpStatus.OK);
    }
    @GetMapping("/getDeleteProgressKey")
    public ResponseEntity<ResponseBean> getDeleteProgressKey(@RequestParam(value = "key") String bulkUpload) {
        return new ResponseEntity<ResponseBean>(bulkMasterService.deleteProgressKey(bulkUpload), HttpStatus.OK);
    }
}
