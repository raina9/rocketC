package com.weblearnex.app.controller;

import com.weblearnex.app.constant.PaymentType;
import com.weblearnex.app.entity.master.Pincode;
import com.weblearnex.app.entity.master.ServicablePincode;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.PincodeService;
import com.weblearnex.app.service.ServicablePincodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class ServicablePincodeController {

    @Autowired
    private ServicablePincodeService servicablePincodeService;

    @PostMapping("/addServicablePincode")
    public ResponseEntity<ResponseBean> addPincode(@RequestBody ServicablePincode servicablePincode) {
        return new ResponseEntity<ResponseBean>(servicablePincodeService.addServicablePincode(servicablePincode), HttpStatus.OK);
    }
    @PutMapping("/updateServicablePincode")
    public ResponseEntity<ResponseBean> updatePincode(@RequestBody ServicablePincode servicablePincode) {
        return new ResponseEntity<ResponseBean>(servicablePincodeService.updateServicablePincode(servicablePincode), HttpStatus.OK);
    }
    @DeleteMapping("/deleteServicablePincode/{id}")
    public ResponseEntity<ResponseBean> deletePincode(@PathVariable Long id) {
        return new ResponseEntity<ResponseBean>(servicablePincodeService.deleteServicablePincode(id), HttpStatus.OK);
    }
    @GetMapping("/getAllServicablePincode")
    public ResponseEntity<ResponseBean> getAllPincode() {
        return new ResponseEntity<ResponseBean>(servicablePincodeService.getAllServicablePincode(), HttpStatus.OK);
    }

    @RequestMapping(value="/getAllCourierServicablePincode", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadFile(@RequestParam(value = "courierCode",required = false) String courierCode,
                                                  HttpServletRequest request, HttpServletResponse response) throws Exception {
        return servicablePincodeService.getAllCourierServicablePincode(courierCode);
    }

}
