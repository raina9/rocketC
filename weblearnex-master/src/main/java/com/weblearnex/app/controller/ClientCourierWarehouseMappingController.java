package com.weblearnex.app.controller;

import com.weblearnex.app.entity.setup.ClientCourierWarehouseMapping;
import com.weblearnex.app.entity.setup.ClientWarehouse;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.ClientCourierWarehouseMappingService;
import com.weblearnex.app.service.ClientWarehouseService;
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
public class ClientCourierWarehouseMappingController {

    @Autowired
    private ClientCourierWarehouseMappingService clientCourierWarehouseMappingService;

    @PostMapping("/addClientCourierWarehouseMapping")
    public ResponseEntity<ResponseBean> addClientCourierWarehouseMapping(@RequestBody ClientCourierWarehouseMapping clientCourierWarehouseMapping) {
        return new ResponseEntity<ResponseBean>(clientCourierWarehouseMappingService.addClientCourierWarehouseMapping(clientCourierWarehouseMapping), HttpStatus.OK);
    }

    @PutMapping("/updateClientCourierWarehouseMapping")
    public ResponseEntity<ResponseBean> updateClientCourierWarehouseMapping(@RequestBody ClientCourierWarehouseMapping clientCourierWarehouseMapping) {
        return new ResponseEntity<ResponseBean>(clientCourierWarehouseMappingService.updateClientCourierWarehouseMapping(clientCourierWarehouseMapping), HttpStatus.OK);
    }
    @DeleteMapping("/deleteClientCourierWarehouseMapping/{clientCourierWarehouseMappingId}")
    public ResponseEntity<ResponseBean> deleteClientCourierWarehouseMapping(@PathVariable Long clientCourierWarehouseMappingId) {
        return new ResponseEntity<ResponseBean>(clientCourierWarehouseMappingService.deleteClientCourierWarehouseMapping(clientCourierWarehouseMappingId), HttpStatus.OK);
    }
    @GetMapping("/getAllClientCourierWarehouseMapping")
    public ResponseEntity<ResponseBean> getAllClientCourierWarehouseMapping() {
        return new ResponseEntity<ResponseBean>(clientCourierWarehouseMappingService.getAllClientCourierWarehouseMapping(), HttpStatus.OK);
    }
    @GetMapping("/findByClientCourierWarehouseMappingId/{id}")
    public ResponseEntity<ResponseBean> findByClientCourierWarehouseMappingId(@PathVariable(value = "id")Long id) {
        return new ResponseEntity<ResponseBean>(clientCourierWarehouseMappingService.findByClientCourierWarehouseMapping(id), HttpStatus.OK);
    }
    @PostMapping(value = "/clientCourierWarehouseMapping/getAllPaginationAndSort")
    public DataTablesOutput<ClientCourierWarehouseMapping> getAllClientCourierWarehouseMappingPaginationAndSort(@RequestBody DataTablesInput input) {
        return clientCourierWarehouseMappingService.getAllClientCourierWarehouseMappingPaginationAndSort(input);
    }

    @GetMapping(value="/clientCourierWarehouseMappingReport")
    public ResponseEntity<Resource> clientCourierWarehouseMappingReport(HttpServletRequest request, HttpServletResponse response){
        return clientCourierWarehouseMappingService.clientCourierWarehouseMappingReport();
    }
}
