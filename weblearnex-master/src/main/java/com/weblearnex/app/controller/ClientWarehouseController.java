package com.weblearnex.app.controller;


import com.weblearnex.app.entity.setup.ClientWarehouse;
import com.weblearnex.app.model.ResponseBean;
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
public class ClientWarehouseController {

    @Autowired
    private ClientWarehouseService clientWarehouseService;

    @PostMapping("/addClientWarehouse")
    public ResponseEntity<ResponseBean> addClientWarehouse(@RequestBody ClientWarehouse clientWarehouse) {
        return new ResponseEntity<ResponseBean>(clientWarehouseService.addClientWarehouse(clientWarehouse), HttpStatus.OK);
    }

    @PutMapping("/updateClientWarehouse")
    public ResponseEntity<ResponseBean> updateClientWarehouse(@RequestBody ClientWarehouse clientWarehouse) {
        return new ResponseEntity<ResponseBean>(clientWarehouseService.updateClientWarehouse(clientWarehouse), HttpStatus.OK);
    }
    @DeleteMapping("/deleteClientWarehouse/{clientWarehouseId}")
    public ResponseEntity<ResponseBean> deleteClientWarehouse(@PathVariable Long clientWarehouseId) {
        return new ResponseEntity<ResponseBean>(clientWarehouseService.deleteClientWarehouse(clientWarehouseId), HttpStatus.OK);
    }
    @GetMapping("/getAllClientWarehouse")
    public ResponseEntity<ResponseBean> getAllClientWarehouse() {
        return new ResponseEntity<ResponseBean>(clientWarehouseService.getAllClientWarehouse(), HttpStatus.OK);
    }
    @GetMapping("/findByClientWarehouseId/{id}")
    public ResponseEntity<ResponseBean> findByClientWarehouseId(@PathVariable(value = "id")Long id) {
        return new ResponseEntity<ResponseBean>(clientWarehouseService.findByClientWarehouse(id), HttpStatus.OK);
    }
    @PostMapping(value = "/clientWarehouse/getAllPaginationAndSort")
    public DataTablesOutput<ClientWarehouse> getAllClientWarehousePaginationAndSort(@RequestBody DataTablesInput input) {
        return clientWarehouseService.getAllClientWarehousePaginationAndSort(input);
    }
    @GetMapping("/getAllClientWarehouseByClientCode")
    public ResponseEntity<ResponseBean> getAllClientWarehouseByClientCode( @RequestParam(value = "clientCode") String clientCode) {
        return new ResponseEntity<ResponseBean>(clientWarehouseService.getAllClientWarehouseByClientCode(clientCode), HttpStatus.OK);
    }
    @GetMapping("/getLoginUserWarehouses")
    public ResponseEntity<ResponseBean> getLoginUserWarehouses() {
        return new ResponseEntity<ResponseBean>(clientWarehouseService.getLoginUserWarehouses(), HttpStatus.OK);
    }

    @GetMapping(value="/clientWarehouseReport")
    public ResponseEntity<Resource> clientWarehouseReport(HttpServletRequest request, HttpServletResponse response){
        return clientWarehouseService.clientWarehouseReport();
    }
}
