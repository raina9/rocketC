package com.weblearnex.app.controller;

import com.weblearnex.app.entity.master.BulkMaster;
import com.weblearnex.app.entity.setup.Page;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PageController {

    @Autowired
    private PageService pageService;


    @PostMapping("/addPage")
    public ResponseEntity<ResponseBean> addPage(@RequestBody Page page) {
        return new ResponseEntity<ResponseBean>(pageService.addPage(page), HttpStatus.OK);
    }

    @PutMapping("/updatePage")
    public ResponseEntity<ResponseBean> updatePage(@RequestBody Page page) {
        return new ResponseEntity<ResponseBean>(pageService.updatePage(page), HttpStatus.OK);
    }
    @DeleteMapping("/deletePage/{Id}")
    public ResponseEntity<ResponseBean> deletePage(@PathVariable Long Id) {
        return new ResponseEntity<ResponseBean>(pageService.deletePage(Id), HttpStatus.OK);
    }
    @GetMapping("/getAllPage")
    public ResponseEntity<ResponseBean> getAllPage() {
        return new ResponseEntity<ResponseBean>(pageService.getAllPage(), HttpStatus.OK);
    }
    @GetMapping("/findByPageId/{id}")
    public ResponseEntity<ResponseBean> findByPageId(@PathVariable(value = "id")Long id) {
        return new ResponseEntity<ResponseBean>(pageService.findByPageId(id), HttpStatus.OK);
    }
    @PostMapping(value = "/page/getAllPaginationAndSort")
    public DataTablesOutput<Page> getAllPagePaginationAndSort(@RequestBody DataTablesInput input) {
        return pageService.getAllPagePaginationAndSort(input);
    }
}

