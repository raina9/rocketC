package com.weblearnex.app.controller;


import com.weblearnex.app.entity.master.BulkMaster;
import com.weblearnex.app.entity.master.ServiceProvider;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.ServiceProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ServiceProviderController {

    @Autowired
    private ServiceProviderService serviceProviderService;

    @PostMapping("/addServiceProvider")
    public ResponseEntity<ResponseBean> addServiceType(@RequestBody ServiceProvider serviceProvider) {
        return new ResponseEntity<ResponseBean>(serviceProviderService.addServiceProvider(serviceProvider), HttpStatus.OK);
    }
    @PutMapping("/updateServiceProvider")
    public ResponseEntity<ResponseBean> updateServiceProvider(@RequestBody ServiceProvider serviceProvider) {
        return new ResponseEntity<ResponseBean>(serviceProviderService.updateServiceProvider(serviceProvider), HttpStatus.OK);
    }
    @DeleteMapping("/deleteServiceProvider/{serviceProviderId}")
    public ResponseEntity<ResponseBean> deleteServiceProvider(@PathVariable Long serviceProviderId) {
        return new ResponseEntity<ResponseBean>(serviceProviderService.deleteServiceProvider(serviceProviderId), HttpStatus.OK);
    }
    @GetMapping("/getAllServiceProvider")
    public ResponseEntity<ResponseBean> getAllServiceProvider() {
        return new ResponseEntity<ResponseBean>(serviceProviderService.getAllServiceProvider(), HttpStatus.OK);
    }
    @GetMapping("/findByServiceProviderId/{id}")
    public ResponseEntity<ResponseBean> findByServiceProviderId(@PathVariable(value = "id")Long id) {
        return new ResponseEntity<ResponseBean>(serviceProviderService.findByServiceProviderID(id), HttpStatus.OK);
    }
    @PostMapping(value = "/serviceProvider/getAllPaginationAndSort")
    public DataTablesOutput<ServiceProvider> getAllServiceProviderPaginationAndSort(@RequestBody DataTablesInput input) {
        return serviceProviderService.getAllServiceProviderPaginationAndSort(input);
    }

}
