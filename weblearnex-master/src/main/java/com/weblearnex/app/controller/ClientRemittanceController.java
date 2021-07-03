package com.weblearnex.app.controller;

import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.constant.ProjectConstant;
import com.weblearnex.app.datatable.reposatory.ClientRemittanceRepository;
import com.weblearnex.app.entity.remittance.ClientRemittance;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.BulkUploadService;
import com.weblearnex.app.service.ClientRemittanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class ClientRemittanceController {

    @Autowired
    private ClientRemittanceService clientRemittanceService;

    @Autowired
    private ClientRemittanceRepository clientRemittanceRepository;

    @Autowired
    @Qualifier("applicionConfig")
    private MessageSource applicionConfig;

    @PostMapping(value = "/getAllClientGeneratedRemittance")
    public DataTablesOutput<ClientRemittance> getAllClientGeneratedRemittance(@RequestBody DataTablesInput input) {
        return clientRemittanceService.getAllClientGeneratedRemittance(input);
    }
    @PostMapping(value = "/getAllClientClosedRemittance")
    public DataTablesOutput<ClientRemittance> getAllClientClosedRemittance(@RequestBody DataTablesInput input) {
        return clientRemittanceService.getAllClientClosedRemittance(input);
    }
    @GetMapping("/findByClientRemittanceId/{id}")
    public ResponseEntity<ResponseBean> findById(@PathVariable Long id){
        return new ResponseEntity<ResponseBean>(clientRemittanceService.findById(id), HttpStatus.OK);
    }

    @RequestMapping(value="/getAllClientAwbNumber", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadFile(@RequestParam(value = "clientCode",required = false) String courierCode,
                                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        return clientRemittanceService.getAllClientAwbNumber(courierCode);
    }
    @PostMapping("/closedClientRemittance")
    public ResponseEntity<ResponseBean> closedClientRemittance(@RequestParam(value="file", required = false) MultipartFile file,
                  @RequestParam(value="bankName", required = false) String bankName,
                  @RequestParam(value="accountNo", required = false) String accountNo,
                  @RequestParam(value="transactionNo", required = false) String transactionNo,
                  @RequestParam(value="depositeDate", required = false) String depositeDate,
                  @RequestParam(value="remittanceNo", required = false) String remittanceNo,
                  @RequestParam(value="depositedAmt", required = false) Double depositedAmt) {

        //Double depAmount = Double.valueOf(depositedAmt);
        return new ResponseEntity<ResponseBean>(clientRemittanceService.closedClientRemittance(file,bankName,accountNo,transactionNo,depositeDate,remittanceNo,depositedAmt), HttpStatus.OK);
    }

   /* @RequestMapping(value="/downloadDepositeSlipFile", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadDepositeSlipFile(@RequestParam(value = "remittanceNo",required = true) String remittanceNo,
                                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        return clientRemittanceService.downloadDepositeSlipFile(remittanceNo);
    }*/

    @RequestMapping(value="/downloadDepositeSlipFile", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadDepositeSlipFile(@RequestParam("depositSlip") String depositSlip,
                                                 HttpServletRequest request, HttpServletResponse response) {

        String filePath = applicionConfig.getMessage(AppProperty.CLIENT_REMITTANCE_DEPOSITED_SLIP_PATH, null, null);
        byte[] bytes = ClientRemittanceService.downloadDepositeSlipFile(filePath+"/"+depositSlip);
        return ResponseEntity.ok().contentLength(bytes.length)
                .body(new ByteArrayResource(bytes));

    }
    @RequestMapping(value="/clientRemittanceReport", method = RequestMethod.GET)
    public ResponseEntity<Resource> clientRemittanceReport(@RequestParam(value = "remittanceNo",required = false) String remittanceNo,
                                                            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return clientRemittanceService.clientRemittanceReport(remittanceNo);
    }
    @GetMapping("/deleteClientRemittance/{id}")
    public ResponseEntity<ResponseBean> deleteClientRemittance(@PathVariable(value="id") Long id) {
        return new ResponseEntity<ResponseBean>(clientRemittanceService.deleteClientRemittance(id), HttpStatus.OK);
    }
}
