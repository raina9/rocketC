package com.weblearnex.app.controller;

import com.weblearnex.app.constant.PaymentType;
import com.weblearnex.app.constant.SeriesType;
import com.weblearnex.app.entity.master.AwbSeries;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.AwbSeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AwbSeriesController {

    @Autowired
    private AwbSeriesService awbSeriesService;

    @PostMapping("/addAwbSeries")
    public ResponseEntity<ResponseBean> addAwbSeries(@RequestBody AwbSeries awbSeries) {
        return new ResponseEntity<ResponseBean>(awbSeriesService.addAwbSeries(awbSeries), HttpStatus.OK);
    }
    @PutMapping("/updateAwbSeries")
    public  ResponseEntity<ResponseBean> updateAwbSeries(@RequestBody AwbSeries awbSeries) {
        return new ResponseEntity<ResponseBean>(awbSeriesService.updateAwbSeries(awbSeries), HttpStatus.OK);
    }
    @DeleteMapping("/deleteAwbSeries/{awbSeriesId}")
    public ResponseEntity<ResponseBean> deleteAwbSeries(@PathVariable(value="awbSeriesId") Long awbSeriesId) {
        return new ResponseEntity<ResponseBean>(awbSeriesService.deleteAwbSeries(awbSeriesId), HttpStatus.OK);
    }
    @GetMapping("/getAllAwbSeries")
    public ResponseEntity<ResponseBean> getAllCountries() {
        return new ResponseEntity<ResponseBean>(awbSeriesService.getAllAwbSeries(), HttpStatus.OK);
    }
    @GetMapping("/findByAwbSeries")
    public ResponseEntity<ResponseBean> findByAwbSeries(@PathVariable String awbSeries) {
        return new ResponseEntity<ResponseBean>(awbSeriesService.findByAwbSeries(awbSeries), HttpStatus.OK);
    }
    @RequestMapping(value="/autoGenerateBulkSeries", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadFile(@RequestParam(value = "clientCode",required = false) String clientCode,
                                                 @RequestParam(value = "totalSeriesCount",required = false) String totalSeriesCount, @RequestParam(value = "paymentType",required = false) PaymentType paymentType , HttpServletRequest request, HttpServletResponse response) throws Exception {
        return awbSeriesService.autoGenerateBulkSeries(clientCode,Integer.valueOf(totalSeriesCount),paymentType);
    }

    @RequestMapping(value="/downloadPendingAwbSeries", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadPendingAwbSeries(@RequestParam(value = "seriesType",required = false) SeriesType seriesType,
                                                             @RequestParam(value = "typeValue",required = false) String typeValue,HttpServletRequest request, HttpServletResponse response) throws Exception {
        return awbSeriesService.downloadPendingAwbSeries(seriesType,typeValue);
    }

}
