package com.weblearnex.app.controller;

import com.weblearnex.app.entity.master.BulkMaster;
import com.weblearnex.app.entity.setup.Client;
import com.weblearnex.app.entity.setup.ClientFacility;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.ClientFacilityService;
import com.weblearnex.app.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
public class ClientController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientFacilityService clientFacilityService;

    @PostMapping("/addClient")
    public ResponseEntity<ResponseBean> addClient(@RequestBody Client client){
        return new ResponseEntity<ResponseBean>(clientService.addClient(client), HttpStatus.OK);
    }

   /* @PostMapping(value = "/addClient", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseBean> addClient(*//*@RequestBody Client client,*//*
                                                  @RequestParam(value="add_aadharcard", required = false) MultipartFile aadharCardFiles,
                                                  @RequestParam(value="add_pancard", required = false) MultipartFile panCardFiles,
                                                  @RequestParam(value="add_gst", required = false) MultipartFile gstFile,
                                                  @RequestParam(value="add_agreement", required = false) MultipartFile agreementFiles,
                                                  *//*@RequestParam(value="data", required = false) String data ,*//*HttpServletRequest request, HttpServletResponse response){

        return new ResponseEntity<ResponseBean>(clientService.addClient(null, aadharCardFiles,panCardFiles,gstFile,agreementFiles), HttpStatus.OK);
    }*/

    @PutMapping("/updateClient")
    public ResponseEntity<ResponseBean> updateClient(@RequestBody Client client){
        return new ResponseEntity<ResponseBean>(clientService.updateClient(client), HttpStatus.OK);
    }
    @DeleteMapping("/deleteClient/{id}")
    public ResponseEntity<ResponseBean>deleteClient(@PathVariable Long id){
        return new ResponseEntity<ResponseBean>(clientService.deleteClient(id),HttpStatus.OK);
    }

    @GetMapping("/getAllClient")
    public ResponseEntity<ResponseBean> getAllClient() {
        return new ResponseEntity<ResponseBean>(clientService.getAllClient(), HttpStatus.OK);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<ResponseBean>findById(@PathVariable Long id){
        return new ResponseEntity<ResponseBean>(clientService.findById(id),HttpStatus.OK);
    }

    @GetMapping("/findBytName/{clientName}")
    public ResponseEntity<ResponseBean>findByName(@PathVariable String clientName){
        return new ResponseEntity<ResponseBean>(clientService.findByName(clientName),HttpStatus.OK);
    }
    @GetMapping("/findByActiveClient")
    public ResponseEntity<ResponseBean> findByActiveClient() {
        return new ResponseEntity<ResponseBean>(clientService.findByActive(), HttpStatus.OK);
    }

    @GetMapping(value="/clientReport")
    public ResponseEntity<Resource> clientReport(HttpServletRequest request, HttpServletResponse response){
        return clientService.clientReport();
    }

    @GetMapping(value="/getActiveWalletClient")
    public ResponseEntity<ResponseBean> getActiveWalletClient(HttpServletRequest request, HttpServletResponse response){
        return new ResponseEntity<ResponseBean>(clientService.getActiveWalletClient(),HttpStatus.OK);
    }

    @PostMapping("/addClientFacility")
    public ResponseEntity<ResponseBean> addClientFacility(@RequestBody ClientFacility clientFacility){
        return new ResponseEntity<ResponseBean>(clientFacilityService.addClientFacility(clientFacility), HttpStatus.OK);
    }

    @PutMapping("/updateClientFacility")
    public ResponseEntity<ResponseBean> updateClientFacility(@RequestBody ClientFacility clientFacility){
        return new ResponseEntity<ResponseBean>(clientFacilityService.updateClientFacility(clientFacility), HttpStatus.OK);
    }
    @DeleteMapping("/deleteClientFacility/{id}")
    public ResponseEntity<ResponseBean>deleteClientFacility(@PathVariable Long id){
        return new ResponseEntity<ResponseBean>(clientFacilityService.deleteClientFacility(id),HttpStatus.OK);
    }

    @GetMapping("/getAllClientFacility")
    public ResponseEntity<ResponseBean> getAllClientFacility() {
        return new ResponseEntity<ResponseBean>(clientFacilityService.getAllClientFacility(), HttpStatus.OK);
    }
    @GetMapping("/findByClientFacilityId/{id}")
    public ResponseEntity<ResponseBean>findByClientFacilityId(@PathVariable Long id){
        return new ResponseEntity<ResponseBean>(clientFacilityService.findByClientFacilityId(id),HttpStatus.OK);
    }
    @PostMapping(value = "/client/getAllPaginationAndSort")
    public DataTablesOutput<Client> getAllClientPaginationAndSort(@RequestBody DataTablesInput input) {
        return clientService.getAllClientPaginationAndSort(input);
    }
    @PostMapping(value = "/clientFacility/getAllPaginationAndSort")
    public DataTablesOutput<ClientFacility> getAllClientFacilityPaginationAndSort(@RequestBody DataTablesInput input) {
        return clientFacilityService.getAllClientFacilityPaginationAndSort(input);
    }
    @GetMapping("/getClientServiceType")
    public ResponseEntity<ResponseBean> getClientServiceType(@RequestParam(value = "clientId") Long clientId) {
        return new ResponseEntity<ResponseBean>(clientFacilityService.getClientServiceType(clientId), HttpStatus.OK);
    }
    @GetMapping("/getClientAllowedCourier")
    public ResponseEntity<ResponseBean> getClientAllowedCourier(@RequestParam(value = "clientId") Long clientId,
                                                                @RequestParam(value = "serviceId") Long serviceId) {
        return new ResponseEntity<ResponseBean>(clientFacilityService.getClientAllowedCourier(clientId, serviceId), HttpStatus.OK);
    }

}
