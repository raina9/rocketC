package com.weblearnex.app.controller;

import com.weblearnex.app.entity.master.BulkHeader;
import com.weblearnex.app.model.DataTableResult;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.BulkHeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class BulkHeaderController {

    @Autowired
    private BulkHeaderService bulkHeaderService;

    @PostMapping("/addBulkHeader")
    public ResponseEntity<ResponseBean> addBulkHeader(@RequestBody BulkHeader bulkHeader) {
        return new ResponseEntity<ResponseBean>(bulkHeaderService.addBulkHeader(bulkHeader), HttpStatus.OK);
    }
    @PutMapping("/updateBulkHeader")
    public ResponseEntity<ResponseBean> updateBulkHeader(@RequestBody BulkHeader bulkHeader) {
        return new ResponseEntity<ResponseBean>(bulkHeaderService.updateBulkHeader(bulkHeader), HttpStatus.OK);
    }

    @DeleteMapping("/deleteBulkHeader/{bulkHeaderId}")
    public ResponseEntity<ResponseBean> deleteBulkHeader(@PathVariable Long bulkHeaderId) {
        return new ResponseEntity<ResponseBean>(bulkHeaderService.deleteBulkHeader(bulkHeaderId), HttpStatus.OK);
    }

    @GetMapping("/getAllBulkHeader")
    public ResponseEntity<ResponseBean> getAllBulkHeader() {
        return new ResponseEntity<ResponseBean>(bulkHeaderService.getAllBulkHeader(), HttpStatus.OK);
    }

    @GetMapping("/findByIdBulkHeader/{id}")
    public ResponseEntity<ResponseBean> findByIdBulkHeader(@PathVariable(value = "id")Long id) {
        return new ResponseEntity<ResponseBean>(bulkHeaderService.findByIdBulkHeader(id), HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public DataTableResult getAllBulkHeaderWithPagination(HttpServletRequest request,
                                                          @RequestParam(defaultValue = "0") Integer pageNo,
                                                          @RequestParam(defaultValue = "10") Integer pageSize,
                                                          @RequestParam(value = "draw",required = false,defaultValue = "0") Integer draw,
                                                          @RequestParam(defaultValue = "id") String sortBy){
        return bulkHeaderService.getAll(pageNo,pageSize,draw,sortBy);
        //return new ResponseEntity<ResponseBean>(bulkHeaderService.getAll(pageNo,pageSize,sortBy), HttpStatus.OK);
    }
    @PostMapping(value = "/getAllPaginationAndSort")
    public DataTablesOutput<BulkHeader> getUsers(@RequestBody  DataTablesInput input) {
        return bulkHeaderService.getAllBulkHeader(input);
    }

}


