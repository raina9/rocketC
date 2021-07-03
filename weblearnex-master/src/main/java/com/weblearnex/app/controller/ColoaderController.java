package com.weblearnex.app.controller;

import com.weblearnex.app.entity.master.BulkMaster;
import com.weblearnex.app.entity.setup.Coloader;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.ColoaderService;
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
public class ColoaderController {

    @Autowired
    private ColoaderService coloaderService;

    @PostMapping("/addColoader")
    public ResponseEntity<ResponseBean> addColoader(@RequestBody Coloader coloader) {
        return new ResponseEntity<ResponseBean>(coloaderService.addColoader(coloader), HttpStatus.OK);
    }

    @PutMapping("/updateColoader")
    public ResponseEntity<ResponseBean> updateColoader(@RequestBody Coloader coloader) {
        return new ResponseEntity<ResponseBean>(coloaderService.updateColoader(coloader), HttpStatus.OK);
    }
    @DeleteMapping("/deleteColoader/{Id}")
    public ResponseEntity<ResponseBean> deleteColoader(@PathVariable Long Id) {
        return new ResponseEntity<ResponseBean>(coloaderService.deleteColoader(Id), HttpStatus.OK);
    }
    @GetMapping("/getAllColoader")
    public ResponseEntity<ResponseBean> getAllColoader() {
        return new ResponseEntity<ResponseBean>(coloaderService.getAllColoader(), HttpStatus.OK);
    }
    @GetMapping("/findByColoaderId/{id}")
    public ResponseEntity<ResponseBean> findByColoaderId(@PathVariable(value = "id")Long id) {
        return new ResponseEntity<ResponseBean>(coloaderService.findByColoaderId(id), HttpStatus.OK);
    }
    @PostMapping(value = "/coloader/getAllPaginationAndSort")
    public DataTablesOutput<Coloader> getAllColoaderPaginationAndSort(@RequestBody DataTablesInput input) {
        return coloaderService.getAllColoaderPaginationAndSort(input);
    }
    @GetMapping(value="/coloaderReport")
    public ResponseEntity<Resource> coloaderReport(HttpServletRequest request, HttpServletResponse response){
        return coloaderService.coloaderReport();
    }
}
