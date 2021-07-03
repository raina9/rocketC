package com.weblearnex.app.controller;

import com.weblearnex.app.entity.master.BulkMaster;
import com.weblearnex.app.entity.setup.Configration;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.ConfigrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ConfigrationController {

    @Autowired
    private ConfigrationService configrationService;

    @PostMapping("/addConfigration")
    public ResponseEntity<ResponseBean> addConfigration(@RequestBody Configration configration) {
        return new ResponseEntity<ResponseBean>(configrationService.addConfigration(configration), HttpStatus.OK);
    }

    @PutMapping("/updateConfigration")
    public ResponseEntity<ResponseBean> updateConfigration(@RequestBody Configration configration) {
        return new ResponseEntity<ResponseBean>(configrationService.updateConfigration(configration), HttpStatus.OK);
    }
    @DeleteMapping("/deleteConfigration/{configrationId}")
    public ResponseEntity<ResponseBean> deleteConfigration(@PathVariable Long configrationId) {
        return new ResponseEntity<ResponseBean>(configrationService.deleteConfigration(configrationId), HttpStatus.OK);
    }
    @GetMapping("/getAllConfigration")
    public ResponseEntity<ResponseBean> getAllConfigration() {
        return new ResponseEntity<ResponseBean>(configrationService.getAllConfigration(), HttpStatus.OK);
    }
    @GetMapping("/findByConfigId/{id}")
    public ResponseEntity<ResponseBean> findByConfigId(@PathVariable(value = "id")Long id) {
        return new ResponseEntity<ResponseBean>(configrationService.findByConfigId(id), HttpStatus.OK);
    }
    @PostMapping(value = "/configration/getAllPaginationAndSort")
    public DataTablesOutput<Configration> getAllConfigrationPaginationAndSort(@RequestBody DataTablesInput input) {
        return configrationService.getAllConfigrationPaginationAndSort(input);
    }
}
