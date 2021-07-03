package com.weblearnex.app.controller;

import com.weblearnex.app.entity.setup.Branch;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class BranchController {

    @Autowired
    private BranchService branchService;

    @PostMapping("/addBranch")
    public ResponseEntity<ResponseBean> addBranch(@RequestBody Branch branch) {
        return new ResponseEntity<ResponseBean>(branchService.addBranch(branch), HttpStatus.OK);
    }
    @PutMapping("/updateBranch")
    public ResponseEntity<ResponseBean> updateBranch(@RequestBody Branch branch) {
        return new ResponseEntity<ResponseBean>(branchService.updateBranch(branch), HttpStatus.OK);
    }

    @DeleteMapping("/deleteBranch/{id}")
    public ResponseEntity<ResponseBean> deleteBranch(@PathVariable Long id) {
        return new ResponseEntity<ResponseBean>(branchService.deleteBranch(id), HttpStatus.OK);
    }

    @GetMapping("/getAllBranch")
    public ResponseEntity<ResponseBean> getAllBranch() {
        return new ResponseEntity<ResponseBean>(branchService.getAllBranch(), HttpStatus.OK);
    }

    @GetMapping("/findByBranchId/{id}")
    public ResponseEntity<ResponseBean> findByBranchId(@PathVariable(value = "id")Long id) {
        return new ResponseEntity<ResponseBean>(branchService.findByBranchId(id), HttpStatus.OK);
    }
    @PostMapping(value = "/branch/getAllPaginationAndSort")
    public DataTablesOutput<Branch> getAllBranchPaginationAndSort(@RequestBody DataTablesInput input) {
        return branchService.getAllBranchPaginationAndSort(input);
    }

    @GetMapping("/getAllActiveBranch")
    public ResponseEntity<ResponseBean> getAllActiveBranch() {
        return new ResponseEntity<ResponseBean>(branchService.getAllActiveBranch(), HttpStatus.OK);
    }

    @GetMapping(value="/branchReport")
    public ResponseEntity<Resource> branchReport(HttpServletRequest request, HttpServletResponse response){
        return branchService.branchReport();
    }

}
