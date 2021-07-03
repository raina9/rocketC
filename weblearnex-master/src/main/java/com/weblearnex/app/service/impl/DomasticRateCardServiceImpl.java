package com.weblearnex.app.service.impl;

import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.*;
import com.weblearnex.app.datatable.reposatory.*;
import com.weblearnex.app.entity.master.*;
import com.weblearnex.app.entity.order.SaleOrder;
import com.weblearnex.app.entity.setup.*;
import com.weblearnex.app.model.BulkUploadBean;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.model.SessionUserBean;
import com.weblearnex.app.service.BulkMasterService;
import com.weblearnex.app.service.BulkUploadService;
import com.weblearnex.app.service.DomasticRateCardService;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.*;

@Service
public class DomasticRateCardServiceImpl implements DomasticRateCardService {

    Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private SessionUserBean sessionUserBean;

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    @Autowired
    private RateCardTypeRepository rateCardTypeRepository;

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;



    @Autowired
    private ClientFacilityRepository clientFacilityRepository;

    @Autowired
    private DomasticRateCardRepository domasticRateCardRepository;

    @Autowired
    private BulkMasterService bulkMasterService;



    @Override
    public ResponseBean<DomasticRateCard> addDomasticRateCard(DomasticRateCard domasticRateCard) {
        ResponseBean responseBean = new ResponseBean();
        if (domasticRateCard == null) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.DOMASTIC_RATE_CARD_NULL_MSG, null, null));
            return responseBean;
        }
        if(domasticRateCard.getFreightType() == null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.DOMASTIC_RATE_CARD_FREIGHT_TYPE_NULL_MSG, null, null));
            return responseBean;
        }
        if (domasticRateCard.getRateMatrixList() == null || domasticRateCard.getRateMatrixList().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.DOMASTIC_RATE_CARD_MATRIX_EMPTY_MSG, null, null));
            return responseBean;
        }
        if(domasticRateCard.getCourierCode() == null || domasticRateCard.getCourierCode().trim().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Rate card courier cod is empty.");
            return responseBean;
        }
        if(!courierRepository.existsByCourierCode(domasticRateCard.getCourierCode())){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Incorrect courier code, Please enter correct courier code.");
            return responseBean;
        }
        if(domasticRateCard.getServiceProviderCode() == null || domasticRateCard.getServiceProviderCode().trim().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Service provider code is empty.");
            return responseBean;
        }
        if(serviceProviderRepository.findByServiceProviderCode(domasticRateCard.getServiceProviderCode())==null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Incorrect service provider code, Please enter correct service provider code.");
            return responseBean;
        }


        if(domasticRateCard.getRateCardCode() == null || domasticRateCard.getRateCardCode().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.DOMASTIC_RATE_CARD_CODE_EMPTY_MSG, null, null));
            return responseBean;
        }
        if(domasticRateCard.getRateCardTypeCode() == null || domasticRateCard.getRateCardTypeCode().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Rate card type field is empty.");
            return responseBean;
        }
        if(rateCardTypeRepository.findByTypeCode(domasticRateCard.getRateCardTypeCode())==null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Incorrect rate card type code, Please enter correct code.");
            return responseBean;
        }

        if(domasticRateCardRepository.existsByCourierCodeAndRateCardTypeCodeAndServiceProviderCode(domasticRateCard.getCourierCode(),
                domasticRateCard.getRateCardTypeCode(), domasticRateCard.getServiceProviderCode())){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Same courier, same rate type and same provider ---> Domastic Rate Card already exist.");
            return responseBean;
        }

        DomasticRateCard domasticRateCardDB = domasticRateCardRepository.findByRateCardCode(domasticRateCard.getRateCardCode());
        if(domasticRateCardDB != null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.DOMASTIC_RATE_CARD_CODE_ALREADY_EXIST, null, null));
            return responseBean;
        }

        domasticRateCard = domasticRateCardRepository.save(domasticRateCard);
        if(domasticRateCard.getId() != null){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setResponseBody(domasticRateCard);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.DOMASTIC_RATE_CARD_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<DomasticRateCard> updateDomasticRateCard(DomasticRateCard domasticRateCard) {
        ResponseBean responseBean = new ResponseBean();
        if (domasticRateCard == null) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.DOMASTIC_RATE_CARD_CODE_ALREADY_EXIST, null, null));
            return responseBean;
        }
        if(domasticRateCard.getFreightType() == null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.DOMASTIC_RATE_CARD_FREIGHT_TYPE_NULL_MSG, null, null));
            return responseBean;
        }
        if (domasticRateCard.getRateMatrixList() == null || domasticRateCard.getRateMatrixList().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.DOMASTIC_RATE_CARD_MATRIX_EMPTY_MSG, null, null));
            return responseBean;
        }

        if(domasticRateCard.getRateCardCode() == null || domasticRateCard.getRateCardCode().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.DOMASTIC_RATE_CARD_CODE_EMPTY_MSG, null, null));
            return responseBean;
        }
        if(domasticRateCard.getRateCardTypeCode() == null || domasticRateCard.getRateCardTypeCode().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Rate card type field is empty.");
            return responseBean;
        }

        Optional<DomasticRateCard> domasticRateCardDB = domasticRateCardRepository.findById(domasticRateCard.getId());
        if(domasticRateCardDB == null || !domasticRateCardDB.isPresent()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.DOMASTIC_RATE_CARD_ID_INVALIDE, null, null));
            return responseBean;
        }
        // Update issue to resolved first deleted object then save;
        domasticRateCardRepository.delete(domasticRateCardDB.get());

        domasticRateCardRepository.save(domasticRateCard);
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setResponseBody(domasticRateCard);
        return responseBean;
    }

    @Override
    public ResponseBean<DomasticRateCard> deleteDomasticRateCard(Long domasticRateCardId) {
        ResponseBean responseBean = new ResponseBean();
        domasticRateCardRepository.deleteById(domasticRateCardId);
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setResponseBody(domasticRateCardId);
        return responseBean;
    }

    @Override
    public ResponseBean<List<DomasticRateCard>> getAllDomasticRateCards() {
        ResponseBean responseBean = new ResponseBean();
        List<DomasticRateCard> domasticRateCardList = (List<DomasticRateCard>)domasticRateCardRepository.findAll();
        if(domasticRateCardList == null || domasticRateCardList.isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_NOT_FOUND, null, null));
            return responseBean;
        }
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setResponseBody(domasticRateCardList);
        return responseBean;
    }

    @Override
    public ResponseBean<DomasticRateCard> findById(Long id) {
        ResponseBean responseBean = new ResponseBean();
        Optional<DomasticRateCard> domasticRateCard = domasticRateCardRepository.findById(id);
        if(domasticRateCard.get() != null){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setResponseBody(domasticRateCard.get());
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_NOT_FOUND, null, null));
        }
        return responseBean;
    }

    @Override
    public double calculateVolumetricWeight(Double length, Double breadth, Double height, Double weightFactor) {
        try {
            if(length==null|| breadth==null|| height==null){
                return 0.0d;
            }
            if(weightFactor != null && weightFactor > 0){
                return (length*breadth*height)/weightFactor;
            }
            return (length*breadth*height)/5000;
        }catch (Exception e){e.printStackTrace();}
        return 0;
    }

    @Override
    public Map<String, Double> calculateAllCharges(Double weight, Double volWeight, PaymentType paymentType, Double codAmount, FreightType freightType, RateMatrix rateMatrix) {

        Double pktVolWeight = calculateWeightFromCourierRate(volWeight, freightType, rateMatrix);
        Double pktActWeight = calculateWeightFromCourierRate(weight, freightType, rateMatrix);
        Double chargeableWeight = pktVolWeight > pktActWeight ? pktVolWeight : pktActWeight;

        Double baseCharge = calculateRateFromCourierRate(chargeableWeight, freightType, rateMatrix);
        Double freightRate = getFreightRate(chargeableWeight, freightType, rateMatrix);
        Double addFreightRate = getIncrementalRate(freightType, rateMatrix);

        Double awbCharge = rateMatrix.getAwbCharge() == null ? 0.0 : rateMatrix.getAwbCharge();
        Double minCodCharge = rateMatrix.getMinCodCharge() == null ? 0.0 : rateMatrix.getMinCodCharge();
        Double handlingCharge = rateMatrix.getHandlingCharges() == null ? 0.0 :rateMatrix.getHandlingCharges();
        // Double handlingCharge = getPercentageCharge(rateMatrix.getHandlingCharges(), baseCharge);
        Double odaCharge = rateMatrix.getOda() == null ? 0.0 : rateMatrix.getOda();
        // Double odaCharge = getPercentageCharge(rateMatrix.getOda(), baseCharge);
        //Double covidCharge = rateMatrix.getCovidCharges() == null ? 0.0 : rateMatrix.getCovidCharges();
        Double covidCharge = getPercentageCharge(rateMatrix.getCovidCharges(), baseCharge);
        Double otherCharge = rateMatrix.getOtherCharges() == null ? 0.0 : rateMatrix.getOtherCharges();

        Double codCharge = 0.0;
        if(PaymentType.COD.equals(paymentType)){
            codCharge = getPercentageCharge(rateMatrix.getCodChargePercent(), codAmount);
            if(codCharge > minCodCharge){
                minCodCharge = codCharge;
            }
        }else {
            minCodCharge = 0.0;
        }
        Double fscCharge = getPercentageCharge(rateMatrix.getFsc(), baseCharge);
        Double fovCharge = getPercentageCharge(rateMatrix.getFov(), baseCharge);
        Double rovCharge = getPercentageCharge(rateMatrix.getRovCharge(), baseCharge);
        Double insuranceCharge = getPercentageCharge(rateMatrix.getInsuranceCharges(), baseCharge);

        Double grossTotalAmount = baseCharge + awbCharge + minCodCharge + handlingCharge + fscCharge + fovCharge + rovCharge +
                insuranceCharge + odaCharge + covidCharge + otherCharge;

        Double gst = getPercentageCharge(rateMatrix.getGst(), grossTotalAmount);
        Double totalAmount = grossTotalAmount + gst;

        Map<String,Double> chargesDetailsMap = new HashMap<String,Double>();
        chargesDetailsMap.put("volumetricWeight", pktVolWeight);
        chargesDetailsMap.put("actualWeight", pktActWeight);
        chargesDetailsMap.put("chargeableWeight", chargeableWeight);
        chargesDetailsMap.put("baseCharge", baseCharge);
        chargesDetailsMap.put("freightRate", freightRate);
        chargesDetailsMap.put("addFreightRate",addFreightRate);
        chargesDetailsMap.put("awbCharge",awbCharge);
        chargesDetailsMap.put("minCodCharge",minCodCharge);
        chargesDetailsMap.put("handlingCharge",handlingCharge);
        chargesDetailsMap.put("odaCharge",odaCharge);
        chargesDetailsMap.put("covidCharge", covidCharge);
        chargesDetailsMap.put("otherCharge",otherCharge);
        chargesDetailsMap.put("fscCharge", fscCharge);
        chargesDetailsMap.put("fovCharge", fovCharge);
        chargesDetailsMap.put("rovCharge", rovCharge);
        chargesDetailsMap.put("insuranceCharge",insuranceCharge);
        chargesDetailsMap.put("gst",gst);
        chargesDetailsMap.put("grossTotalAmount",grossTotalAmount);
        chargesDetailsMap.put("totalAmount", grossTotalAmount);
        return chargesDetailsMap;
    }

    @Override
    public ResponseBean<Double> getCharge(SaleOrder saleOrder, Courier courier, ClientFacility clientFacility) {
        ResponseBean<Double> responseBean = new ResponseBean<Double>();
        try{
            //DomasticRateCard domasticRateCard = domasticRateCardRepository.findByCourierCode(courier.getCourierCode());
            Optional<ServiceProvider> serviceProvider = serviceProviderRepository.findById(courier.getServiceProviderId());
            DomasticRateCard domasticRateCard = domasticRateCardRepository.findByCourierCodeAndRateCardTypeCodeAndServiceProviderCode(
                    courier.getCourierCode(), clientFacility.getRateCardTypeCode(), serviceProvider.get().getServiceProviderCode());
            if(domasticRateCard == null){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Courier rate card not found.");
                return responseBean;
            }
            if(saleOrder.getZoneType() == null){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Zone type not found in sale order.");
                return responseBean;
            }
            // Calculate delivery charge hear
            Double volWeight = calculateVolumetricWeight(saleOrder.getLength(),saleOrder.getBreadth(),saleOrder.getHight(),courier.getWeightDimentionFactor());
            for(RateMatrix rateMatrix : domasticRateCard.getRateMatrixList()){
                if(saleOrder.getZoneType().equals(rateMatrix.getZoneType())){
                    Map<String,Double> rateMap = calculateAllCharges(saleOrder.getWeight(), volWeight, saleOrder.getPaymentType(),
                            saleOrder.getCodAmount(), domasticRateCard.getFreightType(), rateMatrix);
                    if (rateMap != null && !rateMap.isEmpty() && rateMap.get("totalAmount") != null && rateMap.get("totalAmount") > 0.0d){
                        responseBean.setStatus(ResponseStatus.SUCCESS);
                        responseBean.setResponseBody(rateMap.get("totalAmount"));
                        return responseBean;
                    } else {
                        responseBean.setStatus(ResponseStatus.FAIL);
                        responseBean.setMessage("Rate calculation failed.");
                        return responseBean;
                    }
                }
            }
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("RateMatrix not found.");
        }catch (Exception e){
            e.printStackTrace();
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(e.getMessage());
        }
        return responseBean;
    }

    @Override
    public DataTablesOutput<DomasticRateCard> getAllDomasticRateCardPaginationAndSort(DataTablesInput input) {
        if(sessionUserBean.getUser().getType().equals(UserType.CLIENT)){
            ClientFacility clientFacility = clientFacilityRepository.findByClientId(sessionUserBean.getClientId());
            input.addColumn("rateCardTypeCode", true, false, clientFacility.getRateCardTypeCode());
        }

        return domasticRateCardRepository.findAll(input);
    }


    private Double getPercentageCharge(Double percentCharge, Double amount){
        Double percentageCharge = 0.0d;
        if(percentCharge == null || percentCharge == 0.0 || amount == null || amount == 0.0){
            return percentageCharge;
        }
        return (percentCharge * amount)/100;
    }

    public Double getIncrementalRate(FreightType type, RateMatrix rateMatrix) {
        if(type.equals(FreightType.FLAT)) {
            return rateMatrix.getFlatFreight().getIncrementalRate();
        }
        return 0.0d;
    }
    private Double getFreightRate(Double weight, FreightType type, RateMatrix rateMatrix) {
        if(type.equals(FreightType.FLAT)) {
            return rateMatrix.getFlatFreight().getBaseRate();
        } else if(type.equals(FreightType.SLAB)) {
            for(SlabFreight slab : rateMatrix.getSlabFreights()) {
                if(slab.getFromWeight() <=  weight && slab.getToWeight() >= weight) {
                    return slab.getRate();
                }
            }
        }
        return 0.0d;
    }


    private Double calculateWeightFromCourierRate(Double weight, FreightType type, RateMatrix rateMatrix) {
        Double calculateWeight = 0.0;
        if(weight == null || rateMatrix == null){
            return calculateWeight;
        }
        try {
            if(type.equals(FreightType.FLAT)){
                FlatFreight flatFreight = rateMatrix.getFlatFreight();
                if(flatFreight.getBaseWeight() >= weight){
                    return flatFreight.getBaseWeight();
                }
                Double incWeight = weight - flatFreight.getBaseWeight();
                Double incFactor = Math.ceil(incWeight / flatFreight.getIncrementalWeight());
                return ((incFactor * flatFreight.getIncrementalWeight()) + flatFreight.getBaseWeight());

            } else if(type.equals(FreightType.SLAB)){
                for(SlabFreight slab : rateMatrix.getSlabFreights()) {
                    if(slab.getFromWeight() <=  weight && slab.getToWeight() >= weight) {
                        return slab.getToWeight();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calculateWeight;
    }
    private Double calculateRateFromCourierRate(Double weight, FreightType type, RateMatrix rateMatrix) {
        Double calculateRate = 0.0;

        if(weight == null || rateMatrix == null) {
            return calculateRate;
        }
        try {
            if(type.equals(FreightType.FLAT)) {
                FlatFreight flatFreight = rateMatrix.getFlatFreight();
                if(flatFreight.getBaseWeight() >= weight) {
                    return flatFreight.getBaseRate();
                }
                Double incWeight = weight - flatFreight.getBaseWeight();
                Double incFactor = Math.ceil(incWeight / flatFreight.getIncrementalWeight());
                return ((incFactor * flatFreight.getIncrementalRate()) + flatFreight.getBaseRate());

            } else if(type.equals(FreightType.SLAB)){
                for(SlabFreight slab : rateMatrix.getSlabFreights()) {
                    if(slab.getFromWeight() <=  weight && slab.getToWeight() >= weight) {
                        return slab.getRate();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calculateRate;
    }


    @Override
    public ResponseEntity<Resource> viewClientRateReport() {
        List<DomasticRateCard> domasticRateCardList = (List<DomasticRateCard>) domasticRateCardRepository.findAll();
        if(domasticRateCardList == null){
            return null;
        }
        SXSSFWorkbook xlsWorkbook = new SXSSFWorkbook();
        SXSSFSheet xlsSheet = (SXSSFSheet) xlsWorkbook.createSheet();
        int rowIndex = ProjectConstant.ZERO;
        SXSSFRow titleRow = (SXSSFRow) xlsSheet.createRow(rowIndex++);
        titleRow.createCell(0).setCellValue("CODE");
        titleRow.createCell(1).setCellValue("RATE_TYPE");
        titleRow.createCell(2).setCellValue("FREIGHT_TYPE");
        titleRow.createCell(3).setCellValue("COURIER");

        titleRow.createCell(4).setCellValue("LOCAL_BASE_WEIGHT");
        titleRow.createCell(5).setCellValue("LOCAL_BASE_RATE");
        titleRow.createCell(6).setCellValue("LOCAL_INCREMENTAL_WEIGHT");
        titleRow.createCell(7).setCellValue("LOCAL_INCREMENTAL_RATE");

        titleRow.createCell(8).setCellValue("METRO_BASE_WEIGHT");
        titleRow.createCell(9).setCellValue("METRO_BASE_RATE");
        titleRow.createCell(10).setCellValue("METRO_INCREMENTAL_WEIGHT");
        titleRow.createCell(11).setCellValue("METRO_INCREMENTAL_RATE");

        titleRow.createCell(12).setCellValue("ROI_BASE_WEIGHT");
        titleRow.createCell(13).setCellValue("ROI_BASE_RATE");
        titleRow.createCell(14).setCellValue("ROI_INCREMENTAL_WEIGHT");
        titleRow.createCell(15).setCellValue("ROI_INCREMENTAL_RATE");

        titleRow.createCell(16).setCellValue("NEJK_BASE_WEIGHT");
        titleRow.createCell(17).setCellValue("NEJKL_BASE_RATE");
        titleRow.createCell(18).setCellValue("NEJK_INCREMENTAL_WEIGHT");
        titleRow.createCell(19).setCellValue("NEJK_INCREMENTAL_RATE");

        titleRow.createCell(20).setCellValue("WITHIN_STATE_BASE_WEIGHT");
        titleRow.createCell(21).setCellValue("WITHIN_STATE_BASE_RATE");
        titleRow.createCell(22).setCellValue("WITHIN_STATEL_INCREMENTAL_WEIGHT");
        titleRow.createCell(23).setCellValue("WITHIN_STATEL_INCREMENTAL_RATE");

        if(domasticRateCardList != null && domasticRateCardList.size() > ProjectConstant.ZERO){
            for (DomasticRateCard domasticRateCard : domasticRateCardList) {
                SXSSFRow dataRow = (SXSSFRow)xlsSheet.createRow(rowIndex++);
                List<RateMatrix> rateMatrixList = domasticRateCard.getRateMatrixList();
                for (RateMatrix rateMatrix :rateMatrixList ) {
                    FlatFreight flatFreight = rateMatrix.getFlatFreight();


                    dataRow.createCell(0).setCellValue(domasticRateCard.getRateCardCode() != null ? domasticRateCard.getRateCardCode(): "NA");
                    dataRow.createCell(1).setCellValue(domasticRateCard.getRateCardTypeCode() != null ? domasticRateCard.getRateCardTypeCode(): "NA");
                    dataRow.createCell(2).setCellValue(domasticRateCard.getFreightType() != null ? domasticRateCard.getFreightType().toString(): "NA");
                    dataRow.createCell(3).setCellValue(domasticRateCard.getCourierCode() != null ? domasticRateCard.getCourierCode(): "NA");
                    if (rateMatrix.getZoneType().equals(ZoneType.LOCAL)){
                        dataRow.createCell(4).setCellValue(flatFreight.getBaseWeight() != null ? flatFreight.getBaseWeight().toString(): "NA");
                        dataRow.createCell(5).setCellValue(flatFreight.getBaseRate() != null ? flatFreight.getBaseRate().toString(): "NA");
                        dataRow.createCell(6).setCellValue(flatFreight.getIncrementalWeight() != null ? flatFreight.getIncrementalWeight().toString(): "NA");
                        dataRow.createCell(7).setCellValue(flatFreight.getIncrementalRate() != null ? flatFreight.getIncrementalRate().toString(): "NA");

                    }
                    if (rateMatrix.getZoneType().equals(ZoneType.METRO)){
                        dataRow.createCell(8).setCellValue(flatFreight.getBaseWeight() != null ? flatFreight.getBaseWeight().toString(): "NA");
                        dataRow.createCell(9).setCellValue(flatFreight.getBaseRate() != null ? flatFreight.getBaseRate().toString(): "NA");
                        dataRow.createCell(10).setCellValue(flatFreight.getIncrementalWeight() != null ? flatFreight.getIncrementalWeight().toString(): "NA");
                        dataRow.createCell(11).setCellValue(flatFreight.getIncrementalRate() != null ? flatFreight.getIncrementalRate().toString(): "NA");

                    }
                    if (rateMatrix.getZoneType().equals(ZoneType.ROI)){
                        dataRow.createCell(12).setCellValue(flatFreight.getBaseWeight() != null ? flatFreight.getBaseWeight().toString(): "NA");
                        dataRow.createCell(13).setCellValue(flatFreight.getBaseRate() != null ? flatFreight.getBaseRate().toString(): "NA");
                        dataRow.createCell(14).setCellValue(flatFreight.getIncrementalWeight() != null ? flatFreight.getIncrementalWeight().toString(): "NA");
                        dataRow.createCell(15).setCellValue(flatFreight.getIncrementalRate() != null ? flatFreight.getIncrementalRate().toString(): "NA");

                    }
                    if (rateMatrix.getZoneType().equals(ZoneType.NEJK)){
                        dataRow.createCell(16).setCellValue(flatFreight.getBaseWeight() != null ? flatFreight.getBaseWeight().toString(): "NA");
                        dataRow.createCell(17).setCellValue(flatFreight.getBaseRate() != null ? flatFreight.getBaseRate().toString(): "NA");
                        dataRow.createCell(18).setCellValue(flatFreight.getIncrementalWeight() != null ? flatFreight.getIncrementalWeight().toString(): "NA");
                        dataRow.createCell(19).setCellValue(flatFreight.getIncrementalRate() != null ? flatFreight.getIncrementalRate().toString(): "NA");
                    }
                    if (rateMatrix.getZoneType().equals(ZoneType.WITHINSTATE)){
                        dataRow.createCell(20).setCellValue(flatFreight.getBaseWeight() != null ? flatFreight.getBaseWeight().toString(): "NA");
                        dataRow.createCell(21).setCellValue(flatFreight.getBaseRate() != null ? flatFreight.getBaseRate().toString(): "NA");
                        dataRow.createCell(22).setCellValue(flatFreight.getIncrementalWeight() != null ? flatFreight.getIncrementalWeight().toString(): "NA");
                        dataRow.createCell(23).setCellValue(flatFreight.getIncrementalRate() != null ? flatFreight.getIncrementalRate().toString(): "NA");

                    }
                }
            }
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try{
            xlsWorkbook.write(bos);
        }catch (Exception e){
            e.printStackTrace();
        }
        byte [] byteArray = bos.toByteArray();
        return ResponseEntity.ok().contentLength(byteArray.length)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .cacheControl(CacheControl.noCache())
                .header("Content-Disposition", "attachment; filename="+"viewClientRateReport"+ProjectConstant.EXTENSION_XLS)
                .body(new ByteArrayResource(byteArray));

    }

    @Override
    public ResponseEntity<Resource> domesticRateCardReport() {
        List<DomasticRateCard> domasticRateCardList = (List<DomasticRateCard>) domasticRateCardRepository.findAll();
        if(domasticRateCardList == null){
            return null;
        }
        SXSSFWorkbook xlsWorkbook = new SXSSFWorkbook();
        SXSSFSheet xlsSheet = (SXSSFSheet) xlsWorkbook.createSheet();
        int rowIndex = ProjectConstant.ZERO;
        SXSSFRow titleRow = (SXSSFRow) xlsSheet.createRow(rowIndex++);
        titleRow.createCell(0).setCellValue("ID");
        titleRow.createCell(1).setCellValue("RATE_CARD_CODE");
        titleRow.createCell(2).setCellValue("RATE_CARD_NAME");
        titleRow.createCell(3).setCellValue("COURIER");
        titleRow.createCell(4).setCellValue("SERVICE_PROVIDER_ID");
        titleRow.createCell(5).setCellValue("RATE_CARD_TYPE");
        titleRow.createCell(6).setCellValue("FREIGHT_TYPE");

        titleRow.createCell(7).setCellValue("LOCAL_BASE_WEIGHT");
        titleRow.createCell(8).setCellValue("LOCAL_BASE_RATE");
        titleRow.createCell(9).setCellValue("LOCAL_INCREMENTAL_WEIGHT");
        titleRow.createCell(10).setCellValue("LOCAL_INCREMENTAL_RATE");
        titleRow.createCell(11).setCellValue("LOCAl_AWB_CHARGE");
        titleRow.createCell(12).setCellValue("LOCAl_MIN_COD_CHARGE");
        titleRow.createCell(13).setCellValue("LOCAl_COD_CHARGE_PERCENT");
        titleRow.createCell(14).setCellValue("LOCAl_FSC");
        titleRow.createCell(15).setCellValue("LOCAl_GST");
        titleRow.createCell(16).setCellValue("LOCAl_ROV_CHARGE");
        titleRow.createCell(17).setCellValue("LOCAl_FOV_CHARGE");
        titleRow.createCell(18).setCellValue("LOCAl_HANDLING_CHARGES");
        titleRow.createCell(19).setCellValue("LOCAl_ODA");
        titleRow.createCell(20).setCellValue("LOCAl_INSURANCE_CHARGES");
        titleRow.createCell(21).setCellValue("LOCAl_COVID_CHARGES");
        titleRow.createCell(22).setCellValue("LOCAl_OTHER_CHARGES");

        titleRow.createCell(23).setCellValue("METRO_BASE_WEIGHT");
        titleRow.createCell(24).setCellValue("METRO_BASE_RATE");
        titleRow.createCell(25).setCellValue("METRO_INCREMENTAL_WEIGHT");
        titleRow.createCell(26).setCellValue("METRO_INCREMENTAL_RATE");
        titleRow.createCell(27).setCellValue("METRO_AWB_CHARGE");
        titleRow.createCell(28).setCellValue("METRO_MIN_COD_CHARGE");
        titleRow.createCell(29).setCellValue("METRO_COD_CHARGE_PERCENT");
        titleRow.createCell(30).setCellValue("METRO_FSC");
        titleRow.createCell(31).setCellValue("METRO_GST");
        titleRow.createCell(32).setCellValue("METRO_ROV_CHARGE");
        titleRow.createCell(33).setCellValue("METRO_FOV_CHARGE");
        titleRow.createCell(34).setCellValue("METRO_HANDLING_CHARGES");
        titleRow.createCell(35).setCellValue("METRO_ODA");
        titleRow.createCell(36).setCellValue("METRO_INSURANCE_CHARGES");
        titleRow.createCell(37).setCellValue("METRO_COVID_CHARGES");
        titleRow.createCell(38).setCellValue("METRO_OTHER_CHARGES");

        titleRow.createCell(39).setCellValue("ROI_BASE_WEIGHT");
        titleRow.createCell(40).setCellValue("ROI_BASE_RATE");
        titleRow.createCell(41).setCellValue("ROI_INCREMENTAL_WEIGHT");
        titleRow.createCell(42).setCellValue("ROI_INCREMENTAL_RATE");
        titleRow.createCell(43).setCellValue("ROI_AWB_CHARGE");
        titleRow.createCell(44).setCellValue("ROI_MIN_COD_CHARGE");
        titleRow.createCell(45).setCellValue("ROI_COD_CHARGE_PERCENT");
        titleRow.createCell(46).setCellValue("ROI_FSC");
        titleRow.createCell(47).setCellValue("ROI_GST");
        titleRow.createCell(48).setCellValue("ROI_ROV_CHARGE");
        titleRow.createCell(49).setCellValue("ROI_FOV_CHARGE");
        titleRow.createCell(50).setCellValue("ROI_HANDLING_CHARGES");
        titleRow.createCell(51).setCellValue("ROI_ODA");
        titleRow.createCell(52).setCellValue("ROI_INSURANCE_CHARGES");
        titleRow.createCell(53).setCellValue("ROI_COVID_CHARGES");
        titleRow.createCell(54).setCellValue("ROI_OTHER_CHARGES");

        titleRow.createCell(55).setCellValue("NEJK_BASE_WEIGHT");
        titleRow.createCell(56).setCellValue("NEJKL_BASE_RATE");
        titleRow.createCell(57).setCellValue("NEJK_INCREMENTAL_WEIGHT");
        titleRow.createCell(58).setCellValue("NEJK_INCREMENTAL_RATE");

        titleRow.createCell(59).setCellValue("NEJK_AWB_CHARGE");
        titleRow.createCell(60).setCellValue("NEJK_MIN_COD_CHARGE");
        titleRow.createCell(61).setCellValue("NEJK_COD_CHARGE_PERCENT");
        titleRow.createCell(62).setCellValue("NEJK_FSC");
        titleRow.createCell(63).setCellValue("NEJK_GST");
        titleRow.createCell(64).setCellValue("NEJK_ROV_CHARGE");
        titleRow.createCell(65).setCellValue("NEJK_FOV_CHARGE");
        titleRow.createCell(66).setCellValue("NEJK_HANDLING_CHARGES");
        titleRow.createCell(67).setCellValue("NEJK_ODA");
        titleRow.createCell(68).setCellValue("NEJK_INSURANCE_CHARGES");
        titleRow.createCell(69).setCellValue("NEJK_COVID_CHARGES");
        titleRow.createCell(70).setCellValue("NEJK_OTHER_CHARGES");

        titleRow.createCell(71).setCellValue("WITHIN_STATE_BASE_WEIGHT");
        titleRow.createCell(72).setCellValue("WITHIN_STATE_BASE_RATE");
        titleRow.createCell(73).setCellValue("WITHIN_STATEL_INCREMENTAL_WEIGHT");
        titleRow.createCell(74).setCellValue("WITHIN_STATEL_INCREMENTAL_RATE");
        titleRow.createCell(75).setCellValue("WITHIN_STATE_AWB_CHARGE");
        titleRow.createCell(76).setCellValue("WITHIN_STATE_MIN_COD_CHARGE");
        titleRow.createCell(77).setCellValue("WITHIN_STATE_COD_CHARGE_PERCENT");
        titleRow.createCell(78).setCellValue("WITHIN_STATE_FSC");
        titleRow.createCell(79).setCellValue("WITHIN_STATE_GST");
        titleRow.createCell(80).setCellValue("WITHIN_STATE_ROV_CHARGE");
        titleRow.createCell(81).setCellValue("WITHIN_STATE_FOV_CHARGE");
        titleRow.createCell(82).setCellValue("WITHIN_STATE_HANDLING_CHARGES");
        titleRow.createCell(83).setCellValue("WITHIN_STATE_ODA");
        titleRow.createCell(84).setCellValue("WITHIN_STATE_INSURANCE_CHARGES");
        titleRow.createCell(85).setCellValue("WITHIN_STATE_COVID_CHARGES");
        titleRow.createCell(86).setCellValue("WITHIN_STATE_OTHER_CHARGES");


        if(domasticRateCardList != null && domasticRateCardList.size() > ProjectConstant.ZERO){
            for (DomasticRateCard domasticRateCard : domasticRateCardList) {
                SXSSFRow dataRow = (SXSSFRow)xlsSheet.createRow(rowIndex++);
                List<RateMatrix> rateMatrixList = domasticRateCard.getRateMatrixList();
                for (RateMatrix rateMatrix :rateMatrixList ) {
                    FlatFreight flatFreight = rateMatrix.getFlatFreight();

                    dataRow.createCell(0).setCellValue(domasticRateCard.getId() != null ? domasticRateCard.getId().toString(): "NA");
                    dataRow.createCell(1).setCellValue(domasticRateCard.getRateCardCode() != null ? domasticRateCard.getRateCardCode(): "NA");
                    dataRow.createCell(2).setCellValue(domasticRateCard.getRateCardName() != null ? domasticRateCard.getRateCardName(): "NA");
                    dataRow.createCell(3).setCellValue(domasticRateCard.getCourierCode() != null ? domasticRateCard.getCourierCode(): "NA");
                    dataRow.createCell(4).setCellValue(domasticRateCard.getServiceProviderCode() != null ? domasticRateCard.getServiceProviderCode(): "NA");
                    dataRow.createCell(5).setCellValue(domasticRateCard.getRateCardTypeCode() != null ? domasticRateCard.getRateCardTypeCode(): "NA");
                    dataRow.createCell(6).setCellValue(domasticRateCard.getFreightType() != null ? domasticRateCard.getFreightType().toString(): "NA");

                    if (rateMatrix.getZoneType().equals(ZoneType.LOCAL)){
                        dataRow.createCell(7).setCellValue(flatFreight.getBaseWeight() != null ? flatFreight.getBaseWeight().toString(): "NA");
                        dataRow.createCell(8).setCellValue(flatFreight.getBaseRate() != null ? flatFreight.getBaseRate().toString(): "NA");
                        dataRow.createCell(9).setCellValue(flatFreight.getIncrementalWeight() != null ? flatFreight.getIncrementalWeight().toString(): "NA");
                        dataRow.createCell(10).setCellValue(flatFreight.getIncrementalRate() != null ? flatFreight.getIncrementalRate().toString(): "NA");

                        dataRow.createCell(11).setCellValue(rateMatrix.getAwbCharge() != null ? rateMatrix.getAwbCharge().toString(): "NA");
                        dataRow.createCell(12).setCellValue(rateMatrix.getMinCodCharge() != null ? rateMatrix.getMinCodCharge().toString(): "NA");
                        dataRow.createCell(13).setCellValue(rateMatrix.getCodChargePercent() != null ? rateMatrix.getCodChargePercent().toString(): "NA");
                        dataRow.createCell(14).setCellValue(rateMatrix.getFsc() != null ? rateMatrix.getFsc().toString(): "NA");
                        dataRow.createCell(15).setCellValue(rateMatrix.getGst() != null ? rateMatrix.getGst().toString(): "NA");
                        dataRow.createCell(16).setCellValue(rateMatrix.getRovCharge() != null ? rateMatrix.getRovCharge().toString(): "NA");
                        dataRow.createCell(17).setCellValue(rateMatrix.getFov() != null ? rateMatrix.getFov().toString(): "NA");
                        dataRow.createCell(18).setCellValue(rateMatrix.getHandlingCharges() != null ? rateMatrix.getHandlingCharges().toString(): "NA");
                        dataRow.createCell(19).setCellValue(rateMatrix.getOda() != null ? rateMatrix.getOda().toString(): "NA");
                        dataRow.createCell(20).setCellValue(rateMatrix.getInsuranceCharges() != null ? rateMatrix.getInsuranceCharges().toString(): "NA");
                        dataRow.createCell(21).setCellValue(rateMatrix.getCovidCharges() != null ? rateMatrix.getCovidCharges().toString(): "NA");
                        dataRow.createCell(22).setCellValue(rateMatrix.getOtherCharges() != null ? rateMatrix.getOtherCharges().toString(): "NA");



                    }
                    if (rateMatrix.getZoneType().equals(ZoneType.METRO)){
                        dataRow.createCell(23).setCellValue(flatFreight.getBaseWeight() != null ? flatFreight.getBaseWeight().toString(): "NA");
                        dataRow.createCell(24).setCellValue(flatFreight.getBaseRate() != null ? flatFreight.getBaseRate().toString(): "NA");
                        dataRow.createCell(25).setCellValue(flatFreight.getIncrementalWeight() != null ? flatFreight.getIncrementalWeight().toString(): "NA");
                        dataRow.createCell(26).setCellValue(flatFreight.getIncrementalRate() != null ? flatFreight.getIncrementalRate().toString(): "NA");

                        dataRow.createCell(27).setCellValue(rateMatrix.getAwbCharge() != null ? rateMatrix.getAwbCharge().toString(): "NA");
                        dataRow.createCell(28).setCellValue(rateMatrix.getMinCodCharge() != null ? rateMatrix.getMinCodCharge().toString(): "NA");
                        dataRow.createCell(29).setCellValue(rateMatrix.getCodChargePercent() != null ? rateMatrix.getCodChargePercent().toString(): "NA");
                        dataRow.createCell(30).setCellValue(rateMatrix.getFsc() != null ? rateMatrix.getFsc().toString(): "NA");
                        dataRow.createCell(31).setCellValue(rateMatrix.getGst() != null ? rateMatrix.getGst().toString(): "NA");
                        dataRow.createCell(32).setCellValue(rateMatrix.getRovCharge() != null ? rateMatrix.getRovCharge().toString(): "NA");
                        dataRow.createCell(33).setCellValue(rateMatrix.getFov() != null ? rateMatrix.getFov().toString(): "NA");
                        dataRow.createCell(34).setCellValue(rateMatrix.getHandlingCharges() != null ? rateMatrix.getHandlingCharges().toString(): "NA");
                        dataRow.createCell(35).setCellValue(rateMatrix.getOda() != null ? rateMatrix.getOda().toString(): "NA");
                        dataRow.createCell(36).setCellValue(rateMatrix.getInsuranceCharges() != null ? rateMatrix.getInsuranceCharges().toString(): "NA");
                        dataRow.createCell(37).setCellValue(rateMatrix.getCovidCharges() != null ? rateMatrix.getCovidCharges().toString(): "NA");
                        dataRow.createCell(38).setCellValue(rateMatrix.getOtherCharges() != null ? rateMatrix.getOtherCharges().toString(): "NA");
                    }
                    if (rateMatrix.getZoneType().equals(ZoneType.ROI)){
                        dataRow.createCell(39).setCellValue(flatFreight.getBaseWeight() != null ? flatFreight.getBaseWeight().toString(): "NA");
                        dataRow.createCell(40).setCellValue(flatFreight.getBaseRate() != null ? flatFreight.getBaseRate().toString(): "NA");
                        dataRow.createCell(41).setCellValue(flatFreight.getIncrementalWeight() != null ? flatFreight.getIncrementalWeight().toString(): "NA");
                        dataRow.createCell(42).setCellValue(flatFreight.getIncrementalRate() != null ? flatFreight.getIncrementalRate().toString(): "NA");

                        dataRow.createCell(43).setCellValue(rateMatrix.getAwbCharge() != null ? rateMatrix.getAwbCharge().toString(): "NA");
                        dataRow.createCell(44).setCellValue(rateMatrix.getMinCodCharge() != null ? rateMatrix.getMinCodCharge().toString(): "NA");
                        dataRow.createCell(45).setCellValue(rateMatrix.getCodChargePercent() != null ? rateMatrix.getCodChargePercent().toString(): "NA");
                        dataRow.createCell(46).setCellValue(rateMatrix.getFsc() != null ? rateMatrix.getFsc().toString(): "NA");
                        dataRow.createCell(47).setCellValue(rateMatrix.getGst() != null ? rateMatrix.getGst().toString(): "NA");
                        dataRow.createCell(48).setCellValue(rateMatrix.getRovCharge() != null ? rateMatrix.getRovCharge().toString(): "NA");
                        dataRow.createCell(49).setCellValue(rateMatrix.getFov() != null ? rateMatrix.getFov().toString(): "NA");
                        dataRow.createCell(50).setCellValue(rateMatrix.getHandlingCharges() != null ? rateMatrix.getHandlingCharges().toString(): "NA");
                        dataRow.createCell(51).setCellValue(rateMatrix.getOda() != null ? rateMatrix.getOda().toString(): "NA");
                        dataRow.createCell(52).setCellValue(rateMatrix.getInsuranceCharges() != null ? rateMatrix.getInsuranceCharges().toString(): "NA");
                        dataRow.createCell(53).setCellValue(rateMatrix.getCovidCharges() != null ? rateMatrix.getCovidCharges().toString(): "NA");
                        dataRow.createCell(54).setCellValue(rateMatrix.getOtherCharges() != null ? rateMatrix.getOtherCharges().toString(): "NA");
                    }
                    if (rateMatrix.getZoneType().equals(ZoneType.NEJK)){
                        dataRow.createCell(55).setCellValue(flatFreight.getBaseWeight() != null ? flatFreight.getBaseWeight().toString(): "NA");
                        dataRow.createCell(56).setCellValue(flatFreight.getBaseRate() != null ? flatFreight.getBaseRate().toString(): "NA");
                        dataRow.createCell(57).setCellValue(flatFreight.getIncrementalWeight() != null ? flatFreight.getIncrementalWeight().toString(): "NA");
                        dataRow.createCell(58).setCellValue(flatFreight.getIncrementalRate() != null ? flatFreight.getIncrementalRate().toString(): "NA");

                        dataRow.createCell(59).setCellValue(rateMatrix.getAwbCharge() != null ? rateMatrix.getAwbCharge().toString(): "NA");
                        dataRow.createCell(60).setCellValue(rateMatrix.getMinCodCharge() != null ? rateMatrix.getMinCodCharge().toString(): "NA");
                        dataRow.createCell(61).setCellValue(rateMatrix.getCodChargePercent() != null ? rateMatrix.getCodChargePercent().toString(): "NA");
                        dataRow.createCell(62).setCellValue(rateMatrix.getFsc() != null ? rateMatrix.getFsc().toString(): "NA");
                        dataRow.createCell(63).setCellValue(rateMatrix.getGst() != null ? rateMatrix.getGst().toString(): "NA");
                        dataRow.createCell(64).setCellValue(rateMatrix.getRovCharge() != null ? rateMatrix.getRovCharge().toString(): "NA");
                        dataRow.createCell(65).setCellValue(rateMatrix.getFov() != null ? rateMatrix.getFov().toString(): "NA");
                        dataRow.createCell(66).setCellValue(rateMatrix.getHandlingCharges() != null ? rateMatrix.getHandlingCharges().toString(): "NA");
                        dataRow.createCell(67).setCellValue(rateMatrix.getOda() != null ? rateMatrix.getOda().toString(): "NA");
                        dataRow.createCell(68).setCellValue(rateMatrix.getInsuranceCharges() != null ? rateMatrix.getInsuranceCharges().toString(): "NA");
                        dataRow.createCell(69).setCellValue(rateMatrix.getCovidCharges() != null ? rateMatrix.getCovidCharges().toString(): "NA");
                        dataRow.createCell(70).setCellValue(rateMatrix.getOtherCharges() != null ? rateMatrix.getOtherCharges().toString(): "NA");
                    }
                    if (rateMatrix.getZoneType().equals(ZoneType.WITHINSTATE)){
                        dataRow.createCell(71).setCellValue(flatFreight.getBaseWeight() != null ? flatFreight.getBaseWeight().toString(): "NA");
                        dataRow.createCell(72).setCellValue(flatFreight.getBaseRate() != null ? flatFreight.getBaseRate().toString(): "NA");
                        dataRow.createCell(73).setCellValue(flatFreight.getIncrementalWeight() != null ? flatFreight.getIncrementalWeight().toString(): "NA");
                        dataRow.createCell(74).setCellValue(flatFreight.getIncrementalRate() != null ? flatFreight.getIncrementalRate().toString(): "NA");

                        dataRow.createCell(75).setCellValue(rateMatrix.getAwbCharge() != null ? rateMatrix.getAwbCharge().toString(): "NA");
                        dataRow.createCell(76).setCellValue(rateMatrix.getMinCodCharge() != null ? rateMatrix.getMinCodCharge().toString(): "NA");
                        dataRow.createCell(77).setCellValue(rateMatrix.getCodChargePercent() != null ? rateMatrix.getCodChargePercent().toString(): "NA");
                        dataRow.createCell(78).setCellValue(rateMatrix.getFsc() != null ? rateMatrix.getFsc().toString(): "NA");
                        dataRow.createCell(79).setCellValue(rateMatrix.getGst() != null ? rateMatrix.getGst().toString(): "NA");
                        dataRow.createCell(80).setCellValue(rateMatrix.getRovCharge() != null ? rateMatrix.getRovCharge().toString(): "NA");
                        dataRow.createCell(81).setCellValue(rateMatrix.getFov() != null ? rateMatrix.getFov().toString(): "NA");
                        dataRow.createCell(82).setCellValue(rateMatrix.getHandlingCharges() != null ? rateMatrix.getHandlingCharges().toString(): "NA");
                        dataRow.createCell(83).setCellValue(rateMatrix.getOda() != null ? rateMatrix.getOda().toString(): "NA");
                        dataRow.createCell(84).setCellValue(rateMatrix.getInsuranceCharges() != null ? rateMatrix.getInsuranceCharges().toString(): "NA");
                        dataRow.createCell(85).setCellValue(rateMatrix.getCovidCharges() != null ? rateMatrix.getCovidCharges().toString(): "NA");
                        dataRow.createCell(86).setCellValue(rateMatrix.getOtherCharges() != null ? rateMatrix.getOtherCharges().toString(): "NA");
                    }
                }
            }
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try{
            xlsWorkbook.write(bos);
        }catch (Exception e){
            e.printStackTrace();
        }
        byte [] byteArray = bos.toByteArray();
        return ResponseEntity.ok().contentLength(byteArray.length)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .cacheControl(CacheControl.noCache())
                .header("Content-Disposition", "attachment; filename="+"DomesticRateCardReport"+ProjectConstant.EXTENSION_XLS)
                .body(new ByteArrayResource(byteArray));
    }

    @Override
    public BulkUploadBean uploadBulkNew(BulkUploadBean bulkUploadBean, BulkMaster bulkMaster,boolean isUpdate) {
        List<Map<String, String>> errorRecord = new ArrayList<Map<String, String>>();
        List<Map<String, String>> successRecord = new ArrayList<Map<String, String>>();
        String token = BulkUploadService.generateRandomString();
        int uploadPersentage = 0;
        int count = 0;
        for(Map<String, String> map : bulkUploadBean.getRecords()){
            count++;
            uploadPersentage =BulkUploadService.calculateUploadPercentage(count, bulkUploadBean.getRecords().size());
            bulkMasterService.setUploadProgressCount(bulkMaster.getName() ,uploadPersentage, token, false);

            DomasticRateCard domasticRateCard = new DomasticRateCard();
            String freightType = map.get(BulkHeaderConstant.FREIGHT_TYPE);
            if(freightType == null || freightType.trim().isEmpty()){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter correct freight type SLAB or FLAT.");
                errorRecord.add(map);
                continue;
            }
            if("flat".equalsIgnoreCase(freightType.trim())){
                domasticRateCard.setFreightType(FreightType.FLAT);
            } else if("slab".equalsIgnoreCase(freightType.trim())){
                map.put(BulkHeaderConstant.MESSAGE,"For slab type entry, Please used UI interface.");
                errorRecord.add(map);
                continue;
            }else {
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter correct freight type SLAB or FLAT.");
                errorRecord.add(map);
                continue;
            }
            // Local rate matrix start hear.
            RateMatrix rateMatrixLocal = new RateMatrix();
            rateMatrixLocal.setZoneType(ZoneType.LOCAL);
            FlatFreight flatFreightLocal = new FlatFreight();
            try {
                flatFreightLocal.setBaseRate((map.get(BulkHeaderConstant.LOCAL_BASE_RATE) != null && !map.get(BulkHeaderConstant.LOCAL_BASE_RATE).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.LOCAL_BASE_RATE).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local base rate.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                flatFreightLocal.setBaseWeight((map.get(BulkHeaderConstant.LOCAL_BASE_WEIGHT) != null && !map.get(BulkHeaderConstant.LOCAL_BASE_WEIGHT).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.LOCAL_BASE_WEIGHT).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local base weight.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                flatFreightLocal.setIncrementalRate((map.get(BulkHeaderConstant.LOCAL_INCREMENTAL_RATE) != null && !map.get(BulkHeaderConstant.LOCAL_INCREMENTAL_RATE).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.LOCAL_INCREMENTAL_RATE).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local incremental rate.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                flatFreightLocal.setIncrementalWeight((map.get(BulkHeaderConstant.LOCAL_INCREMENTAL_WEIGHT) != null && !map.get(BulkHeaderConstant.LOCAL_INCREMENTAL_WEIGHT).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.LOCAL_INCREMENTAL_WEIGHT).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local incremental weight.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            rateMatrixLocal.setFlatFreight(flatFreightLocal);

            try {
                rateMatrixLocal.setAwbCharge((map.get(BulkHeaderConstant.LOCAl_AWB_CHARGE) != null && !map.get(BulkHeaderConstant.LOCAl_AWB_CHARGE).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.LOCAl_AWB_CHARGE).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local wab charge.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixLocal.setMinCodCharge((map.get(BulkHeaderConstant.LOCAl_MIN_COD_CHARGE) != null && !map.get(BulkHeaderConstant.LOCAl_MIN_COD_CHARGE).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.LOCAl_MIN_COD_CHARGE).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local min cod charge.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixLocal.setCodChargePercent((map.get(BulkHeaderConstant.LOCAl_COD_CHARGE_PERCENT) != null && !map.get(BulkHeaderConstant.LOCAl_COD_CHARGE_PERCENT).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.LOCAl_COD_CHARGE_PERCENT).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local cod charge percent.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixLocal.setFsc((map.get(BulkHeaderConstant.LOCAl_FSC) != null && !map.get(BulkHeaderConstant.LOCAl_FSC).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.LOCAl_FSC).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local FSC.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixLocal.setGst((map.get(BulkHeaderConstant.LOCAl_GST) != null && !map.get(BulkHeaderConstant.LOCAl_GST).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.LOCAl_GST).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local GST.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixLocal.setRovCharge((map.get(BulkHeaderConstant.LOCAl_ROV_CHARGE) != null && !map.get(BulkHeaderConstant.LOCAl_ROV_CHARGE).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.LOCAl_ROV_CHARGE).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local ROV charge.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixLocal.setFov((map.get(BulkHeaderConstant.LOCAl_FOV_CHARGE) != null && !map.get(BulkHeaderConstant.LOCAl_FOV_CHARGE).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.LOCAl_FOV_CHARGE).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local FOV charge.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixLocal.setHandlingCharges((map.get(BulkHeaderConstant.LOCAl_HANDLING_CHARGES) != null && !map.get(BulkHeaderConstant.LOCAl_HANDLING_CHARGES).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.LOCAl_HANDLING_CHARGES).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local handling chargrs.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixLocal.setOda((map.get(BulkHeaderConstant.LOCAl_ODA) != null && !map.get(BulkHeaderConstant.LOCAl_ODA).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.LOCAl_ODA).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local ODA.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixLocal.setInsuranceCharges((map.get(BulkHeaderConstant.LOCAl_INSURANCE_CHARGES) != null && !map.get(BulkHeaderConstant.LOCAl_INSURANCE_CHARGES).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.LOCAl_INSURANCE_CHARGES).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local insurance charges.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixLocal.setCovidCharges((map.get(BulkHeaderConstant.LOCAl_COVID_CHARGES) != null && !map.get(BulkHeaderConstant.LOCAl_COVID_CHARGES).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.LOCAl_COVID_CHARGES).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local covid charges.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixLocal.setOtherCharges((map.get(BulkHeaderConstant.LOCAl_OTHER_CHARGES) != null && !map.get(BulkHeaderConstant.LOCAl_OTHER_CHARGES).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.LOCAl_OTHER_CHARGES).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local other charges.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }

            // Metro rate matrix start hear.
            RateMatrix rateMatrixMetro = new RateMatrix();
            rateMatrixMetro.setZoneType(ZoneType.METRO);
            FlatFreight flatFreightMetro = new FlatFreight();
            try {
                flatFreightMetro.setBaseRate((map.get(BulkHeaderConstant.METRO_BASE_RATE) != null && !map.get(BulkHeaderConstant.METRO_BASE_RATE).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.METRO_BASE_RATE).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local base rate.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                flatFreightMetro.setBaseWeight((map.get(BulkHeaderConstant.METRO_BASE_WEIGHT) != null && !map.get(BulkHeaderConstant.METRO_BASE_WEIGHT).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.METRO_BASE_WEIGHT).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local base weight.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                flatFreightMetro.setIncrementalRate((map.get(BulkHeaderConstant.METRO_INCREMENTAL_RATE) != null && !map.get(BulkHeaderConstant.METRO_INCREMENTAL_RATE).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.METRO_INCREMENTAL_RATE).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local incremental rate.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                flatFreightMetro.setIncrementalWeight((map.get(BulkHeaderConstant.METRO_INCREMENTAL_WEIGHT) != null && !map.get(BulkHeaderConstant.METRO_INCREMENTAL_WEIGHT).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.METRO_INCREMENTAL_WEIGHT).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local incremental weight.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            rateMatrixMetro.setFlatFreight(flatFreightMetro);

            try {
                rateMatrixMetro.setAwbCharge((map.get(BulkHeaderConstant.METRO_AWB_CHARGE) != null && !map.get(BulkHeaderConstant.METRO_AWB_CHARGE).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.METRO_AWB_CHARGE).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local wab charge.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixMetro.setMinCodCharge((map.get(BulkHeaderConstant.METRO_MIN_COD_CHARGE) != null && !map.get(BulkHeaderConstant.METRO_MIN_COD_CHARGE).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.METRO_MIN_COD_CHARGE).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local min cod charge.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixMetro.setCodChargePercent((map.get(BulkHeaderConstant.METRO_COD_CHARGE_PERCENT) != null && !map.get(BulkHeaderConstant.METRO_COD_CHARGE_PERCENT).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.METRO_COD_CHARGE_PERCENT).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local cod charge percent.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixMetro.setFsc((map.get(BulkHeaderConstant.METRO_FSC) != null && !map.get(BulkHeaderConstant.METRO_FSC).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.METRO_FSC).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local FSC.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixMetro.setGst((map.get(BulkHeaderConstant.METRO_GST) != null && !map.get(BulkHeaderConstant.METRO_GST).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.METRO_GST).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local GST.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixMetro.setRovCharge((map.get(BulkHeaderConstant.METRO_ROV_CHARGE) != null && !map.get(BulkHeaderConstant.METRO_ROV_CHARGE).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.METRO_ROV_CHARGE).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local ROV charge.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixMetro.setFov((map.get(BulkHeaderConstant.METRO_FOV_CHARGE) != null && !map.get(BulkHeaderConstant.METRO_FOV_CHARGE).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.METRO_FOV_CHARGE).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local FOV charge.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixMetro.setHandlingCharges((map.get(BulkHeaderConstant.METRO_HANDLING_CHARGES) != null && !map.get(BulkHeaderConstant.METRO_HANDLING_CHARGES).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.METRO_HANDLING_CHARGES).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local handling chargrs.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixMetro.setOda((map.get(BulkHeaderConstant.METRO_ODA) != null && !map.get(BulkHeaderConstant.METRO_ODA).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.METRO_ODA).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local ODA.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixMetro.setInsuranceCharges((map.get(BulkHeaderConstant.METRO_INSURANCE_CHARGES) != null && !map.get(BulkHeaderConstant.METRO_INSURANCE_CHARGES).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.METRO_INSURANCE_CHARGES).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local insurance charges.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixMetro.setCovidCharges((map.get(BulkHeaderConstant.METRO_COVID_CHARGES) != null && !map.get(BulkHeaderConstant.METRO_COVID_CHARGES).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.METRO_COVID_CHARGES).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local covid charges.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixMetro.setOtherCharges((map.get(BulkHeaderConstant.METRO_OTHER_CHARGES) != null && !map.get(BulkHeaderConstant.METRO_OTHER_CHARGES).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.METRO_OTHER_CHARGES).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local other charges.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }

            // Local rate matrix start hear.
            RateMatrix rateMatrixRoi = new RateMatrix();
            rateMatrixRoi.setZoneType(ZoneType.ROI);
            FlatFreight flatFreightRoi = new FlatFreight();
            try {
                flatFreightRoi.setBaseRate((map.get(BulkHeaderConstant.ROI_BASE_RATE) != null && !map.get(BulkHeaderConstant.ROI_BASE_RATE).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.ROI_BASE_RATE).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local base rate.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                flatFreightRoi.setBaseWeight((map.get(BulkHeaderConstant.ROI_BASE_WEIGHT) != null && !map.get(BulkHeaderConstant.ROI_BASE_WEIGHT).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.ROI_BASE_WEIGHT).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local base weight.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                flatFreightRoi.setIncrementalRate((map.get(BulkHeaderConstant.ROI_INCREMENTAL_RATE) != null && !map.get(BulkHeaderConstant.ROI_INCREMENTAL_RATE).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.ROI_INCREMENTAL_RATE).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local incremental rate.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                flatFreightRoi.setIncrementalWeight((map.get(BulkHeaderConstant.ROI_INCREMENTAL_WEIGHT) != null && !map.get(BulkHeaderConstant.ROI_INCREMENTAL_WEIGHT).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.ROI_INCREMENTAL_WEIGHT).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local incremental weight.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            rateMatrixRoi.setFlatFreight(flatFreightRoi);

            try {
                rateMatrixRoi.setAwbCharge((map.get(BulkHeaderConstant.ROI_AWB_CHARGE) != null && !map.get(BulkHeaderConstant.ROI_AWB_CHARGE).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.ROI_AWB_CHARGE).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local wab charge.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixRoi.setMinCodCharge((map.get(BulkHeaderConstant.ROI_MIN_COD_CHARGE) != null && !map.get(BulkHeaderConstant.ROI_MIN_COD_CHARGE).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.ROI_MIN_COD_CHARGE).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local min cod charge.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixRoi.setCodChargePercent((map.get(BulkHeaderConstant.ROI_COD_CHARGE_PERCENT) != null && !map.get(BulkHeaderConstant.ROI_COD_CHARGE_PERCENT).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.ROI_COD_CHARGE_PERCENT).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local cod charge percent.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixRoi.setFsc((map.get(BulkHeaderConstant.ROI_FSC) != null && !map.get(BulkHeaderConstant.ROI_FSC).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.ROI_FSC).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local FSC.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixRoi.setGst((map.get(BulkHeaderConstant.ROI_GST) != null && !map.get(BulkHeaderConstant.ROI_GST).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.ROI_GST).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local GST.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixRoi.setRovCharge((map.get(BulkHeaderConstant.ROI_ROV_CHARGE) != null && !map.get(BulkHeaderConstant.ROI_ROV_CHARGE).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.ROI_ROV_CHARGE).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local ROV charge.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixRoi.setFov((map.get(BulkHeaderConstant.ROI_FOV_CHARGE) != null && !map.get(BulkHeaderConstant.ROI_FOV_CHARGE).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.ROI_FOV_CHARGE).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local FOV charge.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixRoi.setHandlingCharges((map.get(BulkHeaderConstant.ROI_HANDLING_CHARGES) != null && !map.get(BulkHeaderConstant.ROI_HANDLING_CHARGES).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.ROI_HANDLING_CHARGES).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local handling chargrs.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixRoi.setOda((map.get(BulkHeaderConstant.ROI_ODA) != null && !map.get(BulkHeaderConstant.ROI_ODA).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.ROI_ODA).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local ODA.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixRoi.setInsuranceCharges((map.get(BulkHeaderConstant.ROI_INSURANCE_CHARGES) != null && !map.get(BulkHeaderConstant.ROI_INSURANCE_CHARGES).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.ROI_INSURANCE_CHARGES).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local insurance charges.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixRoi.setCovidCharges((map.get(BulkHeaderConstant.ROI_COVID_CHARGES) != null && !map.get(BulkHeaderConstant.ROI_COVID_CHARGES).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.ROI_COVID_CHARGES).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local covid charges.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixRoi.setOtherCharges((map.get(BulkHeaderConstant.ROI_OTHER_CHARGES) != null && !map.get(BulkHeaderConstant.ROI_OTHER_CHARGES).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.ROI_OTHER_CHARGES).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local other charges.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }

            // Nejk rate matrix start hear.
            RateMatrix rateMatrixNejk = new RateMatrix();
            rateMatrixNejk.setZoneType(ZoneType.NEJK);
            FlatFreight flatFreightNejk = new FlatFreight();
            try {
                flatFreightNejk.setBaseRate((map.get(BulkHeaderConstant.NEJK_BASE_RATE) != null && !map.get(BulkHeaderConstant.NEJK_BASE_RATE).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.NEJK_BASE_RATE).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local base rate.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                flatFreightNejk.setBaseWeight((map.get(BulkHeaderConstant.NEJK_BASE_WEIGHT) != null && !map.get(BulkHeaderConstant.NEJK_BASE_WEIGHT).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.NEJK_BASE_WEIGHT).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local base weight.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                flatFreightNejk.setIncrementalRate((map.get(BulkHeaderConstant.NEJK_INCREMENTAL_RATE) != null && !map.get(BulkHeaderConstant.NEJK_INCREMENTAL_RATE).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.NEJK_INCREMENTAL_RATE).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local incremental rate.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                flatFreightNejk.setIncrementalWeight((map.get(BulkHeaderConstant.NEJK_INCREMENTAL_WEIGHT) != null && !map.get(BulkHeaderConstant.NEJK_INCREMENTAL_WEIGHT).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.NEJK_INCREMENTAL_WEIGHT).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local incremental weight.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            rateMatrixNejk.setFlatFreight(flatFreightNejk);

            try {
                rateMatrixNejk.setAwbCharge((map.get(BulkHeaderConstant.NEJK_AWB_CHARGE) != null && !map.get(BulkHeaderConstant.NEJK_AWB_CHARGE).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.NEJK_AWB_CHARGE).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local wab charge.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixNejk.setMinCodCharge((map.get(BulkHeaderConstant.NEJK_MIN_COD_CHARGE) != null && !map.get(BulkHeaderConstant.NEJK_MIN_COD_CHARGE).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.NEJK_MIN_COD_CHARGE).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local min cod charge.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixNejk.setCodChargePercent((map.get(BulkHeaderConstant.NEJK_COD_CHARGE_PERCENT) != null && !map.get(BulkHeaderConstant.NEJK_COD_CHARGE_PERCENT).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.NEJK_COD_CHARGE_PERCENT).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local cod charge percent.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixNejk.setFsc((map.get(BulkHeaderConstant.NEJK_FSC) != null && !map.get(BulkHeaderConstant.NEJK_FSC).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.NEJK_FSC).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local FSC.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixNejk.setGst((map.get(BulkHeaderConstant.NEJK_GST) != null && !map.get(BulkHeaderConstant.NEJK_GST).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.NEJK_GST).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local GST.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixNejk.setRovCharge((map.get(BulkHeaderConstant.NEJK_ROV_CHARGE) != null && !map.get(BulkHeaderConstant.NEJK_ROV_CHARGE).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.NEJK_ROV_CHARGE).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local ROV charge.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixNejk.setFov((map.get(BulkHeaderConstant.NEJK_FOV_CHARGE) != null && !map.get(BulkHeaderConstant.NEJK_FOV_CHARGE).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.NEJK_FOV_CHARGE).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local FOV charge.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixNejk.setHandlingCharges((map.get(BulkHeaderConstant.NEJK_HANDLING_CHARGES) != null && !map.get(BulkHeaderConstant.NEJK_HANDLING_CHARGES).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.NEJK_HANDLING_CHARGES).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local handling chargrs.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixNejk.setOda((map.get(BulkHeaderConstant.NEJK_ODA) != null && !map.get(BulkHeaderConstant.NEJK_ODA).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.NEJK_ODA).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local ODA.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixNejk.setInsuranceCharges((map.get(BulkHeaderConstant.NEJK_INSURANCE_CHARGES) != null && !map.get(BulkHeaderConstant.NEJK_INSURANCE_CHARGES).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.NEJK_INSURANCE_CHARGES).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local insurance charges.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixNejk.setCovidCharges((map.get(BulkHeaderConstant.NEJK_COVID_CHARGES) != null && !map.get(BulkHeaderConstant.NEJK_COVID_CHARGES).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.NEJK_COVID_CHARGES).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local covid charges.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixNejk.setOtherCharges((map.get(BulkHeaderConstant.NEJK_OTHER_CHARGES) != null && !map.get(BulkHeaderConstant.NEJK_OTHER_CHARGES).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.NEJK_OTHER_CHARGES).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local other charges.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }

            // Within State rate matrix start hear.
            RateMatrix rateMatrixWithinState = new RateMatrix();
            rateMatrixWithinState.setZoneType(ZoneType.WITHINSTATE);
            FlatFreight flatFreightWithinState = new FlatFreight();
            try {
                flatFreightWithinState.setBaseRate((map.get(BulkHeaderConstant.WITHIN_STATE_BASE_RATE) != null && !map.get(BulkHeaderConstant.WITHIN_STATE_BASE_RATE).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.WITHIN_STATE_BASE_RATE).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local base rate.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                flatFreightWithinState.setBaseWeight((map.get(BulkHeaderConstant.WITHIN_STATE_BASE_WEIGHT) != null && !map.get(BulkHeaderConstant.WITHIN_STATE_BASE_WEIGHT).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.WITHIN_STATE_BASE_WEIGHT).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local base weight.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                flatFreightWithinState.setIncrementalRate((map.get(BulkHeaderConstant.WITHIN_STATE_INCREMENTAL_RATE) != null && !map.get(BulkHeaderConstant.WITHIN_STATE_INCREMENTAL_RATE).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.WITHIN_STATE_INCREMENTAL_RATE).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local incremental rate.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                flatFreightWithinState.setIncrementalWeight((map.get(BulkHeaderConstant.WITHIN_STATE_INCREMENTAL_WEIGHT) != null && !map.get(BulkHeaderConstant.WITHIN_STATE_INCREMENTAL_WEIGHT).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.WITHIN_STATE_INCREMENTAL_WEIGHT).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local incremental weight.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            rateMatrixWithinState.setFlatFreight(flatFreightWithinState);

            try {
                rateMatrixWithinState.setAwbCharge((map.get(BulkHeaderConstant.WITHIN_STATE_AWB_CHARGE) != null && !map.get(BulkHeaderConstant.WITHIN_STATE_AWB_CHARGE).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.WITHIN_STATE_AWB_CHARGE).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local wab charge.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixWithinState.setMinCodCharge((map.get(BulkHeaderConstant.WITHIN_STATE_MIN_COD_CHARGE) != null && !map.get(BulkHeaderConstant.WITHIN_STATE_MIN_COD_CHARGE).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.WITHIN_STATE_MIN_COD_CHARGE).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local min cod charge.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixWithinState.setCodChargePercent((map.get(BulkHeaderConstant.WITHIN_STATE_COD_CHARGE_PERCENT) != null && !map.get(BulkHeaderConstant.WITHIN_STATE_COD_CHARGE_PERCENT).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.WITHIN_STATE_COD_CHARGE_PERCENT).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local cod charge percent.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixWithinState.setFsc((map.get(BulkHeaderConstant.WITHIN_STATE_FSC) != null && !map.get(BulkHeaderConstant.WITHIN_STATE_FSC).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.WITHIN_STATE_FSC).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local FSC.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixWithinState.setGst((map.get(BulkHeaderConstant.WITHIN_STATE_GST) != null && !map.get(BulkHeaderConstant.WITHIN_STATE_GST).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.WITHIN_STATE_GST).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local GST.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixWithinState.setRovCharge((map.get(BulkHeaderConstant.WITHIN_STATE_ROV_CHARGE) != null && !map.get(BulkHeaderConstant.WITHIN_STATE_ROV_CHARGE).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.WITHIN_STATE_ROV_CHARGE).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local ROV charge.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixWithinState.setFov((map.get(BulkHeaderConstant.WITHIN_STATE_FOV_CHARGE) != null && !map.get(BulkHeaderConstant.WITHIN_STATE_FOV_CHARGE).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.WITHIN_STATE_FOV_CHARGE).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local FOV charge.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixWithinState.setHandlingCharges((map.get(BulkHeaderConstant.WITHIN_STATE_HANDLING_CHARGES) != null && !map.get(BulkHeaderConstant.WITHIN_STATE_HANDLING_CHARGES).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.WITHIN_STATE_HANDLING_CHARGES).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local handling chargrs.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixWithinState.setOda((map.get(BulkHeaderConstant.WITHIN_STATE_ODA) != null && !map.get(BulkHeaderConstant.WITHIN_STATE_ODA).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.WITHIN_STATE_ODA).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local ODA.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixWithinState.setInsuranceCharges((map.get(BulkHeaderConstant.WITHIN_STATE_INSURANCE_CHARGES) != null && !map.get(BulkHeaderConstant.WITHIN_STATE_INSURANCE_CHARGES).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.WITHIN_STATE_INSURANCE_CHARGES).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local insurance charges.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixWithinState.setCovidCharges((map.get(BulkHeaderConstant.WITHIN_STATE_COVID_CHARGES) != null && !map.get(BulkHeaderConstant.WITHIN_STATE_COVID_CHARGES).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.WITHIN_STATE_COVID_CHARGES).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local covid charges.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            try {
                rateMatrixWithinState.setOtherCharges((map.get(BulkHeaderConstant.WITHIN_STATE_OTHER_CHARGES) != null && !map.get(BulkHeaderConstant.WITHIN_STATE_OTHER_CHARGES).isEmpty()) ? Double.valueOf(map.get(BulkHeaderConstant.WITHIN_STATE_OTHER_CHARGES).trim())  : 0.0d);
            }catch (Exception e){
                map.put(BulkHeaderConstant.MESSAGE,"Invalid input, please enter numeric local other charges.");
                errorRecord.add(map);
                e.printStackTrace();
                continue;
            }
            if(isUpdate){
               String id =  (map.get(BulkHeaderConstant.DOMESTIC_RATE_CARD_ID) != null && !map.get(BulkHeaderConstant.DOMESTIC_RATE_CARD_ID).isEmpty()) ? map.get(BulkHeaderConstant.DOMESTIC_RATE_CARD_ID).trim() : null;
                if(id==null|| id.trim().isEmpty()){
                    map.put(BulkHeaderConstant.MESSAGE,"Invalid ID, Please provide valid Domastic rate card id.");
                    errorRecord.add(map);
                    continue;
                }
               try{
                   Long objectId = Long.valueOf(id);
                   Optional<DomasticRateCard> domasticRateCardDB = domasticRateCardRepository.findById(objectId);
                   if(!domasticRateCardDB.isPresent()){
                       map.put(BulkHeaderConstant.MESSAGE,"Invalid ID, Please provide valid Domastic rate card id.");
                       errorRecord.add(map);
                       continue;
                   }
                   domasticRateCard.setId(domasticRateCardDB.get().getId());
                   domasticRateCard.setRateCardCode(domasticRateCardDB.get().getRateCardCode());
                   domasticRateCard.setRateCardName(domasticRateCardDB.get().getRateCardName());
                   domasticRateCard.setCourierCode(domasticRateCardDB.get().getCourierCode());
                   domasticRateCard.setServiceProviderCode(domasticRateCardDB.get().getServiceProviderCode());
                   domasticRateCard.setRateCardTypeCode(domasticRateCardDB.get().getRateCardTypeCode());
               }catch (Exception e){
                   map.put(BulkHeaderConstant.MESSAGE,"server internal error.");
                   errorRecord.add(map);
                   continue;
               }
            }
            else{
                domasticRateCard.setRateCardCode((map.get(BulkHeaderConstant.RATE_CARD_CODE) != null && !map.get(BulkHeaderConstant.RATE_CARD_CODE).isEmpty()) ? map.get(BulkHeaderConstant.RATE_CARD_CODE).trim() : null);
                domasticRateCard.setRateCardName((map.get(BulkHeaderConstant.RATE_CARD_NAME) != null && !map.get(BulkHeaderConstant.RATE_CARD_NAME).isEmpty()) ? map.get(BulkHeaderConstant.RATE_CARD_NAME).trim() : null);
                domasticRateCard.setCourierCode(map.get(BulkHeaderConstant.COURIER_CODE) != null ? (map.get(BulkHeaderConstant.COURIER_CODE).trim()) : null);
                domasticRateCard.setServiceProviderCode(map.get(BulkHeaderConstant.SERVICE_PROVIDER_CODE) != null ? (map.get((BulkHeaderConstant.SERVICE_PROVIDER_CODE).trim())) : null);
                domasticRateCard.setRateCardTypeCode((map.get(BulkHeaderConstant.RATE_CARD_TYPE) != null && !map.get(BulkHeaderConstant.RATE_CARD_TYPE).isEmpty()) ? map.get(BulkHeaderConstant.RATE_CARD_TYPE).trim() : null);
            }

            List<RateMatrix> rateMatrixList = new ArrayList<RateMatrix>(5);
            rateMatrixList.add(rateMatrixLocal);
            rateMatrixList.add(rateMatrixMetro);
            rateMatrixList.add(rateMatrixRoi);
            rateMatrixList.add(rateMatrixNejk);
            rateMatrixList.add(rateMatrixWithinState);
            domasticRateCard.setRateMatrixList(rateMatrixList);
            if(isUpdate){
                domasticRateCardRepository.save(domasticRateCard);
            }else {

                ResponseBean responseBean = addDomasticRateCard(domasticRateCard);
                if(ResponseStatus.FAIL.equals(responseBean.getStatus())){
                    map.put(BulkHeaderConstant.MESSAGE,responseBean.getMessage());
                    errorRecord.add(map);
                    continue;
                }
            }

            successRecord.add(map);
        }
        bulkMasterService.setUploadProgressCount(bulkMaster.getName() ,100, token, true);

        bulkUploadBean.setBulkMaster(bulkMaster);
        bulkUploadBean.setErrorRecords(errorRecord);
        bulkUploadBean.setSuccessRecords(successRecord);
        return bulkUploadBean;
    }

    @Override
    public BulkUploadBean uploadBulk(BulkUploadBean bulkUploadBean, BulkMaster bulkMaster) {
        return null;
    }
}
