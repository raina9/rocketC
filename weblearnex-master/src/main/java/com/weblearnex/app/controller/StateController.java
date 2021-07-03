package com.weblearnex.app.controller;


import com.weblearnex.app.entity.master.State;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class  StateController {

    @Autowired
    private StateService stateService;

    @PostMapping("/addState")
    public ResponseEntity<ResponseBean> addStatus(@RequestBody State state) {
        return new ResponseEntity<ResponseBean>(stateService.addState(state), HttpStatus.OK);
    }

    @PutMapping("/updateState")
    public ResponseEntity<ResponseBean> updateStatus(@RequestBody State state) {
        return new ResponseEntity<ResponseBean>(stateService.updateState(state), HttpStatus.OK);
    }

    @DeleteMapping("/deleteState/{stateId}")
    public ResponseEntity<ResponseBean> deleteState(@PathVariable Long stateId) {
        return new ResponseEntity<ResponseBean>(stateService.deleteState(stateId), HttpStatus.OK);
    }

    @GetMapping("/getAllStates")
    public ResponseEntity<ResponseBean> getAllStates() {
        return new ResponseEntity<ResponseBean>(stateService.getAllStates(), HttpStatus.OK);
    }

    @GetMapping("/findByStateCode/{code}")
    public ResponseEntity<ResponseBean> findById(@PathVariable String code) {
        return new ResponseEntity<ResponseBean>(stateService.findByCode(code), HttpStatus.OK);
    }

    /*@GetMapping("/findByStateName/{stateName}")
    public ResponseEntity<ResponseBean> findByStateName(@PathVariable String stateName) {
        return new ResponseEntity<ResponseBean>(stateService.findByStateName(stateName), HttpStatus.OK);
    }*/

    @GetMapping("/findByActiveState")
    public ResponseEntity<ResponseBean> findByActive() {
        return new ResponseEntity<ResponseBean>(stateService.findByActive(), HttpStatus.OK);
    }
}
