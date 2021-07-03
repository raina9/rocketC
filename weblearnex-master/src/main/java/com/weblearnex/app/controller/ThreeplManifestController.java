package com.weblearnex.app.controller;

import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.entity.manifest.ThreeplManifest;
import com.weblearnex.app.entity.order.SaleOrder;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.PrintLabelService;
import com.weblearnex.app.service.ThreeplManifestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
public class ThreeplManifestController {

    @Autowired
    @Qualifier("applicionConfig")
    private MessageSource applicionConfig;

    @Autowired
    private ThreeplManifestService threeplManifestService;

    @Autowired
    private PrintLabelService printLabelService;

    @PostMapping("/addThreeplManifest")
    public ResponseEntity<ResponseBean> addThreeplManifest(@RequestBody ThreeplManifest threeplManifest) {
        return new ResponseEntity<ResponseBean>(threeplManifestService.addThreeplManifest(threeplManifest), HttpStatus.OK);
    }

    @PostMapping(value = "/threeplManifest/getAllThreeplManifest")
    public DataTablesOutput<ThreeplManifest> getAllThreeplManifest(@RequestBody DataTablesInput input) {
        return threeplManifestService.getAllThreeplManifest(input);
    }

    @GetMapping("/3PlManifestFindByAwbNumber/{awbNumbers}/{courierCode}")
    public ResponseEntity<ResponseBean> findByAwbNumber(@PathVariable(value = "awbNumbers")String awbNumbers,@PathVariable(value = "courierCode")String courierCode) {
        return new ResponseEntity<ResponseBean>(threeplManifestService.findByAwbNumber(awbNumbers,courierCode), HttpStatus.OK);
    }

    @RequestMapping(value="/generateManifestPrintLabel", method = RequestMethod.POST)
    public ResponseEntity<Resource> generateManifestPrintLabel(@RequestBody String manifestNumber){
        return printLabelService.generateManifestPrintLabel(manifestNumber);
    }
    @GetMapping("/getAllThreeplManifest")
    public ResponseEntity<ResponseBean> getAllThreeplManifest() {
        return new ResponseEntity<ResponseBean>(threeplManifestService.getAllThreeplManifest(), HttpStatus.OK);
    }

    @PostMapping(value = "/upload3PlManifestPod", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity< ResponseBean > uploadData(@RequestParam(value="files", required = false) MultipartFile file,
                                                     @RequestParam(value="manifestId", required = false) String manifestId, HttpServletRequest request, HttpServletResponse response) {
        return new ResponseEntity<ResponseBean>(threeplManifestService.uploadPodFile(file,manifestId), HttpStatus.OK);
    }


    @RequestMapping(value="/download3PlManifestPod", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadFile(@RequestParam("podFileName") String podFileName,
                                                 HttpServletRequest request, HttpServletResponse response) {

        String filePath = applicionConfig.getMessage(AppProperty.THREE_PL_POD_UPLOAD_FILE_PATH, null, null);
        byte[] bytes = ThreeplManifestService.downloadPodFile(podFileName);
        return ResponseEntity.ok().contentLength(bytes.length)
                .body(new ByteArrayResource(bytes));
    }

    @GetMapping("/findByManifestId/{id}")
    public ResponseEntity<ResponseBean> findByManifestId(@PathVariable Long id){
        return new ResponseEntity<ResponseBean>(threeplManifestService.findByManifestId(id),HttpStatus.OK);
    }
    @GetMapping("/getManifestAndSaleOrders/{id}")
    public ResponseEntity<ResponseBean> getManifestAndSaleOrders(@PathVariable Long id){
        return new ResponseEntity<ResponseBean>(threeplManifestService.getManifestAndSaleOrders(id),HttpStatus.OK);
    }

    @PutMapping("/updateManifest")
    public ResponseEntity<ResponseBean> updateManifest(@RequestBody ThreeplManifest threeplManifest){
        return new ResponseEntity<ResponseBean>(threeplManifestService.updateManifest(threeplManifest), HttpStatus.OK);
    }

    @PostMapping(value = "/getAllPodUploadedManifest")
    public DataTablesOutput<ThreeplManifest> getAllPodUploadedManifest(@RequestBody DataTablesInput input) {
        return threeplManifestService.getAllPodUploadedManifest(input);
    }

}
