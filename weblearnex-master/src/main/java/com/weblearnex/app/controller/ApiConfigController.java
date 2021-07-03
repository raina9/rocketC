package com.weblearnex.app.controller;

import com.weblearnex.app.entity.master.ApiConfig;
import com.weblearnex.app.entity.master.BulkMaster;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.ApiConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ApiConfigController {

    @Autowired
    private ApiConfigService apiConfigService;

    @PostMapping("/addApiConfig")
    public ResponseEntity<ResponseBean> addApiConfig(@RequestBody ApiConfig apiConfig) {
        return new ResponseEntity<ResponseBean>(apiConfigService.addApiConfig(apiConfig), HttpStatus.OK);
    }

    @PutMapping("/updateApiConfig")
    public ResponseEntity<ResponseBean> updateApiConfig(@RequestBody ApiConfig apiConfig) {
        return new ResponseEntity<ResponseBean>(apiConfigService.updateApiConfig(apiConfig), HttpStatus.OK);
    }
    @DeleteMapping("/deleteApiConfig/{apiConfigId}")
    public ResponseEntity<ResponseBean> deleteApiConfig(@PathVariable Long apiConfigId) {
        return new ResponseEntity<ResponseBean>(apiConfigService.deleteApiConfig(apiConfigId), HttpStatus.OK);
    }
    @GetMapping("/getAllApiConfig")
    public ResponseEntity<ResponseBean> getAllApiConfig() {
        return new ResponseEntity<ResponseBean>(apiConfigService.getAllApiConfig(), HttpStatus.OK);
    }
    @GetMapping("/findByApiConfigId/{id}")
    public ResponseEntity<ResponseBean> findByApiConfigId(@PathVariable(value = "id")Long id) {
        return new ResponseEntity<ResponseBean>(apiConfigService.findByApiConfigId(id), HttpStatus.OK);
    }
    @PostMapping(value = "/apiConfig/getAllPaginationAndSort")
    public DataTablesOutput<ApiConfig> getAllApiConfigPaginationAndSort(@RequestBody DataTablesInput input) {
        return apiConfigService.getAllApiConfigPaginationAndSort(input);
    }
}


