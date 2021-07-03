package com.weblearnex.app.controller;

import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.PacketHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/packetHistory")
public class PacketHistoryController {
    @Autowired
    private PacketHistoryService packetHistoryService;

    @GetMapping("/updatePacketStatus/{awbNumbers}/{courierCode}")
    public ResponseEntity<ResponseBean> updatePacketStatus(@PathVariable(value = "awbNumbers")String awbNumbers, @PathVariable(value = "courierCode")String courierCode) {
        return new ResponseEntity<ResponseBean>(packetHistoryService.updatePacketStatus(awbNumbers,courierCode), HttpStatus.OK);
    }
}
