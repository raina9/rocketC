package com.weblearnex.app.service.impl;

import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.config.MessageProperty;
import com.weblearnex.app.constant.*;
import com.weblearnex.app.datatable.reposatory.*;
import com.weblearnex.app.entity.master.*;
import com.weblearnex.app.entity.order.PacketHistory;
import com.weblearnex.app.entity.order.SaleOrder;
import com.weblearnex.app.entity.setup.*;
import com.weblearnex.app.model.*;

import com.weblearnex.app.reposatory.ServicablePincodeRepository;
import com.weblearnex.app.service.*;
import com.weblearnex.app.utils.SharedMethords;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import java.util.*;

@Service
public class CourierServiceImpl implements CourierService {
    Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private ClientWarehouseRepository clientWarehouseRepository;

    @Autowired
    private DomasticRateCardService domasticRateCardService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientFacilityRepository clientFacilityRepository;

    @Autowired
    private ServicablePincodeService servicablePincodeService;

    @Autowired
    private DomasticRateCardRepository domasticRateCardRepository;

    @Autowired
    private PincodeService pincodeService;

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;
    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    @Autowired
    private SaleOrderRepository saleOrderRepository;

    @Autowired
    private SaleOrderService saleOrderService;

    @Autowired
    private ServicablePincodeRepository servicablePincodeRepository;

    @Autowired
    @Qualifier("applicionConfig")
    private MessageSource applicionConfig;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private SessionUserBean sessionUserBean;

    @Autowired
    private BulkMasterService bulkMasterService;

    @Autowired
    private RateCardTypeRepository rateCardTypeRepository;

    @Override
    public ResponseBean<Courier> addCourier(Courier courier ) {
        ResponseBean responseBean = new ResponseBean();
        if (courier.getCourierName() == null || courier.getCourierName().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_NMAE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if(courierRepository.findByCourierName(courier.getCourierName())!=null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_NAME_ALREADY_EXIST, null, null));
            return responseBean;
        }
        if(courierRepository.findByCourierCode(courier.getCourierCode())!=null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_CODE_ALREADY_EXIST, null, null));
            return responseBean;
        }

        if (courier.getCourierCode() == null || courier.getCourierCode().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_CODE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }

        if (courier.getServiceProviderId() == null ) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.SERVICE_PROVIDER_ID_DOSE_NOT_NULL, null, null));
            return responseBean;
        }
        if (courier.getServiceTypeId() == null ) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.SERVICE_ID_DOSE_NOT_NULL, null, null));
            return responseBean;
        }

        if (courier.getMobile() == null || courier.getMobile().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_MOBILE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (courier.getPincode() == null || courier.getPincode().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_PIN_CODE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        courier = courierRepository.save(courier);
        if(courier.getId() != null) {
            responseBean.setResponseBody(courier);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_ADDED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_ADDED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<Courier> updateCourier(Courier courier) {
        ResponseBean responseBean = new ResponseBean();
        if (courier.getCourierName() == null || courier.getCourierName().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_NMAE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (courier.getCourierCode() == null || courier.getCourierCode().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_CODE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (courier.getMobile() == null || courier.getMobile().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_CODE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (courier.getPincode() == null || courier.getPincode().isEmpty()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_CODE_DOES_NOT_NULL_OR_BLANK, null, null));
            return responseBean;
        }
        if (courier.getId() == null || !courierRepository.findById(courier.getId()).isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_ID_DOES_NOT_EMPTY_OR_NO_DATA_FOUND_IN_DATABASE, null, null));
            return responseBean;
        }

        courier = courierRepository.save(courier);
        if (courier != null) {
            responseBean.setResponseBody(courier);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_UPDATED_MSG, null, null));
        } else {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_UPDATED_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<Courier> deleteCourier(Long id) {
        ResponseBean responseBean = new ResponseBean();
        Optional<Courier> vendor = courierRepository.findById(id);
        if (!vendor.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        //Vendor findVendor = vendor.get();
        vendor.get().setActive(AppProperty.IN_ACTIVE);
        courierRepository.save(vendor.get());
        responseBean.setResponseBody(vendor.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_DELETE_MSG, null, null));
        return responseBean;
    }

    @Override
    public ResponseBean<List<Courier>> getAllCourier() {
        ResponseBean responseBean = new ResponseBean();
        List<Courier> courierList = (List<Courier>) courierRepository.findAll();
        if(courierList !=null && !courierList.isEmpty()){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG, null, null));
            responseBean.setResponseBody(courierList);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG, null, null));
        }
        return responseBean;
    }

    @Override
    public ResponseBean<Courier> findByCourierId(Long id) {
        ResponseBean responseBean = new ResponseBean();
        Optional<Courier> vendor = courierRepository.findById(id);
        if (!vendor.isPresent()) {
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_ID_NOT_FOUND, null, null));
            return responseBean;
        }
        responseBean.setResponseBody(vendor.get());
        responseBean.setStatus(ResponseStatus.SUCCESS);
        responseBean.setMessage(messageSource.getMessage(MessageProperty.COURIER_ID_FOUND, null, null));
        return responseBean;
    }


    @Override
    public DataTablesOutput<Courier> getAllCourierPaginationAndSort(DataTablesInput input) {
        DataTablesOutput<Courier> result = courierRepository.findAll(input);
        if(result !=null && result.getData() !=null){
            result.getData().forEach(courier -> {
                Optional<ServiceProvider> serviceProvider = serviceProviderRepository.findById(courier.getServiceProviderId());
                Optional<ServiceType> serviceType = serviceTypeRepository.findById(courier.getServiceTypeId());
                courier.setServiceProviderName(serviceProvider.isPresent() ? serviceProvider.get().getServiceProviderName() : null);
                courier.setServiceTypeName(serviceType.isPresent() ? serviceType.get().getServiceName() : null);
            });
        }
        return courierRepository.findAll(input);
    }

    @Override
    public ResponseBean<Map<String, List<CourierServiceabilityResponseBean>>> courierServiceabilityCheck(CourierServiceabilityBean serviceabilityBean) {
        ResponseBean<Map<String, List<CourierServiceabilityResponseBean>>> responseBean = new ResponseBean<Map<String, List<CourierServiceabilityResponseBean>>>();
        if(serviceabilityBean.getClientCode() == null || serviceabilityBean.getClientCode().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Client code field is empty.");
            return responseBean;
        }
        Client client = clientRepository.findByClientCode(serviceabilityBean.getClientCode());
        if(client == null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Invalid client code, Please check.");
            return responseBean;
        }
        if(serviceabilityBean.getSourcePincode() == null || serviceabilityBean.getSourcePincode().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Source pincode field is empty.");
            return responseBean;
        }
        if(serviceabilityBean.getDestinationPincode() == null || serviceabilityBean.getDestinationPincode().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Destination pincode field is empty.");
            return responseBean;
        }
        if(serviceabilityBean.getPaymentType() == null || serviceabilityBean.getPaymentType().equals(PaymentType.BOTH)){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Payment type can be only PREPAID or COD");
            return responseBean;
        }
        if(serviceabilityBean.getWeight() == null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Weight field is empty.");
            return responseBean;
        }
        if(serviceabilityBean.getWeight() <= 0.0d){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Weight must be grater than zero.");
            return responseBean;
        }
        double actualWeight = serviceabilityBean.getWeight();
        if(serviceabilityBean.getLength() != null && serviceabilityBean.getBreadth() != null && serviceabilityBean.getHeight() != null){
            double volumetricWeight = domasticRateCardService.calculateVolumetricWeight(serviceabilityBean.getLength(), serviceabilityBean.getBreadth(), serviceabilityBean.getHeight(), null);
            if(volumetricWeight > actualWeight){
                actualWeight = volumetricWeight;
            }
        }

        ClientFacility clientFacility = clientFacilityRepository.findByClientId(client.getId());
        if(clientFacility == null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Client facility details not exist.");
            return responseBean;
        }
        if(clientFacility.getServiceCourierMap() == null || clientFacility.getServiceCourierMap().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Courier not allude in client account.");
            return responseBean;
        }
        if(clientFacility.getRateCardTypeCode() == null || clientFacility.getRateCardTypeCode().trim().isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Rate type not defined in client facility.");
            return responseBean;
        }
        if(rateCardTypeRepository.findByTypeCode(clientFacility.getRateCardTypeCode()) == null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Rate type not found in system.");
            return responseBean;
        }
        Map<String, String> serviceCourierMap = clientFacility.getServiceCourierMap();

        Map<String,List<String>>  actualServiceCourierMap = new HashMap<String, List<String>>();
        for(Map.Entry<String,String> entry : serviceCourierMap.entrySet()){
            if(entry.getValue() != null || !entry.getValue().isEmpty()){
                //String sourcePincode, String dropPincode, PaymentType paymentType, List<String> alludeCourier
                ResponseBean<List<String>> serviceProviderResponse = servicablePincodeService.getClientServiceProviders(
                        serviceabilityBean.getSourcePincode(), serviceabilityBean.getDestinationPincode(),
                        serviceabilityBean.getPaymentType(), new ArrayList<>(Arrays.asList(entry.getValue().split(","))));
                if(ResponseStatus.SUCCESS.equals(serviceProviderResponse.getStatus())){
                    actualServiceCourierMap.put(entry.getKey(), serviceProviderResponse.getResponseBody());
                }
            }
        }
        if(actualServiceCourierMap.isEmpty()){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("No service provider available.");
            return responseBean;
        }
        CourierServiceabilityResponseBean serviceabilityResponseBean = new CourierServiceabilityResponseBean();
        //Double volWeight = domasticRateCardService.calculateVolumetricWeight(serviceabilityBean.getLength(), serviceabilityBean.getBreadth(), serviceabilityBean.getHeight());
        ResponseBean<ZoneType> zoneTypeResponseBean = pincodeService.getZone(serviceabilityBean.getSourcePincode(), serviceabilityBean.getDestinationPincode());
        if(ResponseStatus.SUCCESS.equals(zoneTypeResponseBean.getStatus())){
            Map<String, List<CourierServiceabilityResponseBean>> resultMap = new HashMap<String, List<CourierServiceabilityResponseBean>>();
            for(Map.Entry<String,List<String>> entry : actualServiceCourierMap.entrySet()){
                List<CourierServiceabilityResponseBean> providers = new ArrayList<CourierServiceabilityResponseBean>();
                for(String courierCode : entry.getValue()){
                    Courier courier = courierRepository.findByCourierCode(courierCode);
                    if(courier == null || courier.getActive() == null || courier.getActive() == 0){
                        continue;
                    }
                    Optional<ServiceProvider> serviceProvider = serviceProviderRepository.findById(courier.getServiceProviderId());
                    Double volWeight = domasticRateCardService.calculateVolumetricWeight(serviceabilityBean.getLength(), serviceabilityBean.getBreadth(), serviceabilityBean.getHeight(), courier.getWeightDimentionFactor());
                    // TODO to check rate card
                    // Domastic rate card fetch from client rate type.
                    DomasticRateCard domasticRateCard = domasticRateCardRepository.findByCourierCodeAndRateCardTypeCodeAndServiceProviderCode(
                            courier.getCourierCode(), clientFacility.getRateCardTypeCode(), serviceProvider.get().getServiceProviderCode());

                    // DomasticRateCard domasticRateCard = domasticRateCardRepository.findByRateCardCode(courierCode);
                    if(domasticRateCard != null && domasticRateCard.getRateMatrixList() != null && !domasticRateCard.getRateMatrixList().isEmpty()){
                        for(RateMatrix rateMatrix : domasticRateCard.getRateMatrixList()){
                            if(zoneTypeResponseBean.getResponseBody().equals(rateMatrix.getZoneType())){
                                Map<String, Double> chargesMap = domasticRateCardService.calculateAllCharges(serviceabilityBean.getWeight(), volWeight, serviceabilityBean.getPaymentType(),
                                        serviceabilityBean.getCodAmount(),domasticRateCard.getFreightType(),rateMatrix);
                                if(chargesMap != null && !chargesMap.isEmpty() && chargesMap.get("totalAmount") != null){
                                    CourierServiceabilityResponseBean bean = new CourierServiceabilityResponseBean();
                                    bean.setCourierCode(courierCode);
                                    bean.setCourierName(courier != null ? courier.getCourierName() : null);
                                    bean.setServiceCharge(chargesMap.get("totalAmount"));
                                    bean.setServiceCode(entry.getKey());
                                    ServiceType serviceType = serviceTypeRepository.findByServiceCode(entry.getKey());
                                    bean.setServiceName(serviceType != null ? serviceType.getServiceName() : null);
                                    providers.add(bean);
                                }
                            }
                        }
                    }
                }
                if(!providers.isEmpty()){
                    resultMap.put(entry.getKey(), providers);
                }
            }
            if(resultMap.isEmpty()){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("No service provider available.");
                return responseBean;
            }else{
                responseBean.setStatus(ResponseStatus.SUCCESS);
                responseBean.setResponseBody(resultMap);
            }
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(zoneTypeResponseBean.getMessage());
            return responseBean;
        }
        return responseBean;
    }

    @Override
    public ResponseBean isCourierServiceable(String courierCode, PaymentType paymentType, String souPincode, String destPincode) {
        ResponseBean responseBean = new ResponseBean();
        try{
            ServicablePincode souServicablePincode = servicablePincodeRepository.findByPinCodeAndCourierCode(souPincode, courierCode);
            if(souServicablePincode == null){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Pickup pin-code not found.");
                return responseBean;
            }
            ServicablePincode destServicablePincode = servicablePincodeRepository.findByPinCodeAndCourierCode(destPincode, courierCode);
            if(destServicablePincode == null){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Drop pin-code not found.");
                return responseBean;
            }
            // Check for source pin-code.
            if(souServicablePincode.getActive() == 0 || souServicablePincode.getPickupActive() == 0){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Pickup pin-code not serviceable.");
                return responseBean;
            }
            // Check for drop pin-code
            if(destServicablePincode.getActive() == 0 || destServicablePincode.getDropActive() == 0){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Drop pin-code not serviceable.");
                return responseBean;
            }
            if(PaymentType.COD.equals(paymentType)){
                if(destServicablePincode.getCodActive() == 0){
                    responseBean.setStatus(ResponseStatus.FAIL);
                    responseBean.setMessage("COD service not available at drop pin-code.");
                    return responseBean;
                }
            }else if(PaymentType.PREPAID.equals(paymentType)){
                if(destServicablePincode.getPickupActive() == 0){
                    responseBean.setStatus(ResponseStatus.FAIL);
                    responseBean.setMessage("Prepaid service not available at drop pin code.");
                    return responseBean;
                }
            }else {
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("Payment type can not be null.");
                return responseBean;
            }

            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(ResponseStatus.SUCCESS.name());
            responseBean.setResponseBody(true);
        }catch(Exception e){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Server internal error.");
            e.printStackTrace();
        }
        return responseBean;
    }

    @Override
    public ResponseBean<Map<String, List<CourierServiceabilityResponseBean>>> clientServiceProviders(String awbNumber, String clientWarehouseCode) {
        ResponseBean<Map<String, List<CourierServiceabilityResponseBean>>>  responseBean = new ResponseBean<Map<String, List<CourierServiceabilityResponseBean>>>();
        SaleOrder saleOrder = saleOrderRepository.findByReferanceNo(awbNumber);
        if(saleOrder == null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Awb number not found in system.");
            return responseBean;
        }
        ClientWarehouse clientWarehouse = clientWarehouseRepository.findByWarehouseCode(clientWarehouseCode);
        if(clientWarehouse == null){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Client warehouse details not found.");
            return responseBean;
        }

        CourierServiceabilityBean bean = new CourierServiceabilityBean();
        bean.setLength(saleOrder.getLength());
        bean.setBreadth(saleOrder.getBreadth());
        bean.setHeight(saleOrder.getHight());
        bean.setWeight(saleOrder.getWeight());
        bean.setClientCode(saleOrder.getClientCode());
        bean.setCodAmount(0.0);
        if(PaymentType.COD.equals(saleOrder.getPaymentType())){
            bean.setCodAmount(saleOrder.getCodAmount());
        }
        bean.setDestinationPincode(saleOrder.getConsigneePinCode());
        bean.setPaymentType(saleOrder.getPaymentType());
        // rate calculate from pickup wearehouse pincode.
        // ClientWarehouse clientWarehouse = clientWarehouseRepository.findByWarehouseCode(saleOrder.getPickupLocationId());
        if(clientWarehouse != null){
            bean.setSourcePincode(clientWarehouse.getPinCode());
        }else {
            bean.setSourcePincode(saleOrder.getSenderPinCode());
        }
        ResponseBean servicableReasonseBean = courierServiceabilityCheck(bean);
        return servicableReasonseBean;
    }

    @Override
    public ResponseBean<Map<String, List<CourierServiceabilityResponseBean>>> getServiceProvidersForBulk() {
        ResponseBean responseBean = new ResponseBean();
        try{
            Map<String, List<CourierServiceabilityResponseBean>> map = new HashMap<String, List<CourierServiceabilityResponseBean>>();
            if(UserType.CLIENT.equals(sessionUserBean.getUser().getType())){
                ClientFacility clientFacility = clientFacilityRepository.findByClientId(sessionUserBean.getClientId());
                if(clientFacility == null){
                    responseBean.setStatus(ResponseStatus.FAIL);
                    responseBean.setMessage("Client Facility object not found.");
                    return responseBean;
                }
                if(clientFacility.getServiceCourierMap() == null || clientFacility.getServiceCourierMap().isEmpty()){
                    responseBean.setStatus(ResponseStatus.FAIL);
                    responseBean.setMessage("Service courier mapping not found.");
                    return responseBean;
                }
                for (Map.Entry<String,String> entry : clientFacility.getServiceCourierMap().entrySet()){
                    ServiceType serviceType = serviceTypeRepository.findByServiceCode(entry.getKey());
                    if(serviceType != null){
                        List<Courier> courierList = courierRepository.findByCourierCodeInAndActive(Arrays.asList(entry.getValue().split(",")),1);
                        List<CourierServiceabilityResponseBean> serviceabilityResponseBeansList = new ArrayList<CourierServiceabilityResponseBean>();
                        courierList.forEach(courier -> {
                            CourierServiceabilityResponseBean bean = new CourierServiceabilityResponseBean();
                            bean.setCourierCode(courier.getCourierCode());
                            bean.setCourierName(courier.getCourierName());
                            bean.setServiceCode(serviceType.getServiceCode());
                            bean.setServiceName(serviceType.getServiceName());
                            serviceabilityResponseBeansList.add(bean);
                        });
                        map.put(serviceType.getServiceCode(), serviceabilityResponseBeansList);
                    }
                }
                responseBean.setStatus(ResponseStatus.SUCCESS);
                responseBean.setResponseBody(map);
                return responseBean;
            }else{
                List<ServiceType> serviceTypeList = serviceTypeRepository.findAllByActive(1);
                serviceTypeList.forEach(serviceType -> {
                    List<Courier> courierList = courierRepository.findAllByServiceTypeIdAndActive(serviceType.getId(),1);
                    List<CourierServiceabilityResponseBean> serviceabilityResponseBeansList = new ArrayList<CourierServiceabilityResponseBean>();
                    courierList.forEach(courier -> {
                        CourierServiceabilityResponseBean bean = new CourierServiceabilityResponseBean();
                        bean.setCourierCode(courier.getCourierCode());
                        bean.setCourierName(courier.getCourierName());
                        bean.setServiceCode(serviceType.getServiceCode());
                        bean.setServiceName(serviceType.getServiceName());
                        serviceabilityResponseBeansList.add(bean);
                    });
                    map.put(serviceType.getServiceCode(), serviceabilityResponseBeansList);
                });
                responseBean.setStatus(ResponseStatus.SUCCESS);
                responseBean.setResponseBody(map);
                return responseBean;
            }
        }catch (Exception e){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Server internal error.");
            e.printStackTrace();
        }
        return responseBean;
    }


    @Override
    public ResponseBean<List<Courier>> findByActive() {
        ResponseBean responseBean = new ResponseBean();
        List<Courier> vendorList =courierRepository.findByActive(1);
        if(vendorList != null && !vendorList.isEmpty()){
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_SUCCESS_MSG,null,null));
            responseBean.setResponseBody(vendorList);
        }else{
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage(messageSource.getMessage(MessageProperty.RECORD_GET_ERROR_MSG,null,null));
        }
        return responseBean;
    }

    @Override
    public BulkUploadBean uploadBulk(BulkUploadBean bulkUploadBean, BulkMaster bulkMaster) {
        List<Map<String, String>> errorRecord = new ArrayList<Map<String, String>>();
        List<Map<String, String>> successRecord = new ArrayList<Map<String, String>>();
        int count = 0;
        int percentage = 0;
        String token = BulkUploadService.generateRandomString();
        for(Map<String, String> map : bulkUploadBean.getRecords()){
            count ++;
            percentage = BulkUploadService.calculateUploadPercentage(count, bulkUploadBean.getRecords().size());
            bulkMasterService.setUploadProgressCount(bulkMaster.getName(), percentage, token, false);

            String awbNub= map.get(BulkHeaderConstant.AWB_NUMBER) ;
            String courierCode= map.get(BulkHeaderConstant.COURIER_CODE);
            String courierAwb= map.get(BulkHeaderConstant.COURIER_AWB_NUMBER);

            if(awbNub == null || awbNub.trim().equals("")){
                map.put(BulkHeaderConstant.MESSAGE,"Awb Number is empty.");
                errorRecord.add(map);
                continue;
            }

            if(courierCode == null || courierCode.trim().equals("")){
                map.put(BulkHeaderConstant.MESSAGE,"Courier code is empty.");
                errorRecord.add(map);
                continue;
            }
            if(courierAwb == null || courierAwb.trim().equals("")){
                map.put(BulkHeaderConstant.MESSAGE,"Courier Awb Number is empty.");
                errorRecord.add(map);
                continue;
            }

            if(saleOrderRepository.existsSaleOrderByReferanceNoAndCourierCodeAndCourierAWBNumber(awbNub.trim(), courierCode.trim(), courierAwb.trim())){
                map.put(BulkHeaderConstant.MESSAGE,"Duplicate courier awb. already assigned to sale-order.");
                errorRecord.add(map);
                continue;
            }
            SaleOrder  saleOrder = saleOrderRepository.findByReferanceNo(awbNub.trim());
            if(saleOrder==null){
                map.put(BulkHeaderConstant.MESSAGE,"Awb number not found in database.");
                errorRecord.add(map);
                continue;
            }
            // Block re-assigne courier
            String selfCourierCode = applicionConfig.getMessage(AppProperty.SELF_COURIER_CODE, null, null);
            if(!selfCourierCode.equals(saleOrder.getCourierCode()) && saleOrder.getCourierAWBNumber() != null){
                map.put(BulkHeaderConstant.MESSAGE,"Courier details already updated & further update not allowed. ");
                errorRecord.add(map);
                continue;
            }
            Courier courier =courierRepository.findByCourierCode(courierCode.trim());
            if(courier==null || courier.getActive()==0){
                map.put(BulkHeaderConstant.MESSAGE,"Courier code not found in database.");
                errorRecord.add(map);
                continue;
            }
            saleOrder.setCourierCode(courierCode.trim());
            saleOrder.setCourierAWBNumber(courierAwb.toString());

            // If packet current status is equals with == UD Order Process then update status to UD Order Assigned.
            String orderProcessedCode = applicionConfig.getMessage(AppProperty.UD_ORDER_PROCESS, null, null);
            if(saleOrder.getCurrentStatus().getStatusCode().equals(orderProcessedCode)){
                Status orderAssigned = statusRepository.findByStatusCode(applicionConfig.getMessage(AppProperty.UD_ORDER_ASSIGNED, null, null));
                ResponseBean responseBean = saleOrderService.addPacketHistory(saleOrder, orderAssigned, null, null, null);
                if(ResponseStatus.FAIL.equals(responseBean.getStatus())){
                    map.put(BulkHeaderConstant.MESSAGE,responseBean.getMessage());
                    errorRecord.add(map);
                    continue;
                }
            }else {
                saleOrderRepository.save(saleOrder);
            }
            successRecord.add(map);
        }
        bulkUploadBean.setBulkMaster(bulkMaster);
        bulkUploadBean.setErrorRecords(errorRecord);
        bulkUploadBean.setSuccessRecords(successRecord);
        bulkMasterService.setUploadProgressCount(bulkMaster.getName(), 100, token, true);
        return bulkUploadBean;

    }

    @Override
    public ResponseEntity<Resource> courierReport() {
        List<Courier> courierList = (List<Courier>) courierRepository.findAll();
        if(courierList == null){
            return null;
        }
        SXSSFWorkbook xlsWorkbook = new SXSSFWorkbook();
        SXSSFSheet xlsSheet = (SXSSFSheet) xlsWorkbook.createSheet();
        int rowIndex = ProjectConstant.ZERO;
        SXSSFRow titleRow = (SXSSFRow) xlsSheet.createRow(rowIndex++);
        titleRow.createCell(0).setCellValue("COURIER_NAME");
        titleRow.createCell(1).setCellValue("COURIER_CODE");
        titleRow.createCell(2).setCellValue("ADDRESS");
        titleRow.createCell(3).setCellValue("MOBILE_NUMBER");
        titleRow.createCell(4).setCellValue("EMAIL_ID");
        titleRow.createCell(5).setCellValue("PINCODE");
        titleRow.createCell(6).setCellValue("CITY");
        titleRow.createCell(7).setCellValue("STATE");
        titleRow.createCell(8).setCellValue("COUNTRY");
        titleRow.createCell(9).setCellValue("CONTACT_PERSON");
        titleRow.createCell(10).setCellValue("LAT_LONG");
        titleRow.createCell(11).setCellValue("SERVICE_TYPE_ID");
        titleRow.createCell(12).setCellValue("SERVICE_PROVIDER_ID");
        titleRow.createCell(13).setCellValue("TOKEN");
        titleRow.createCell(14).setCellValue("BENEFICIRY");
        titleRow.createCell(15).setCellValue("ACCOUNT_NUMBER");
        titleRow.createCell(16).setCellValue("BANK_NAME");
        titleRow.createCell(17).setCellValue("IFSC_CODE");
        titleRow.createCell(18).setCellValue("GST_NUMBER");
        titleRow.createCell(19).setCellValue("SERVICE_PROVIDER_COURIER_CODE");
        titleRow.createCell(20).setCellValue("WEIGHT_DIMENTION_FACTOR");
        titleRow.createCell(21).setCellValue("VIEW_LOGO_AT_LABEL");
        titleRow.createCell(22).setCellValue("API_CODE");
        titleRow.createCell(23).setCellValue("ACTIVE");
        titleRow.createCell(24).setCellValue("CREATE_BY");
        titleRow.createCell(25).setCellValue("UPDATE_BY");
        titleRow.createCell(26).setCellValue("CREATE_DATE");
        titleRow.createCell(27).setCellValue("UPDATE_DATE");

        if(courierList != null && courierList.size() > ProjectConstant.ZERO){
            for (Courier courier : courierList) {
                SXSSFRow dataRow = (SXSSFRow)xlsSheet.createRow(rowIndex++);
                dataRow.createCell(0).setCellValue(courier.getCourierName() != null ? courier.getCourierName():"NA");
                dataRow.createCell(1).setCellValue(courier.getCourierCode() != null ? courier.getCourierCode():"NA");
                dataRow.createCell(2).setCellValue(courier.getRegisteredAdd() != null ? courier.getRegisteredAdd():"NA");
                dataRow.createCell(3).setCellValue(courier.getMobile() != null ? courier.getMobile():"NA");
                dataRow.createCell(4).setCellValue(courier.getEmail() != null ? courier.getEmail():"NA");
                dataRow.createCell(5).setCellValue(courier.getPincode() != null ? courier.getPincode():"NA");
                dataRow.createCell(6).setCellValue(courier.getCity() != null ? courier.getCity():"NA");
                dataRow.createCell(7).setCellValue(courier.getState()!= null ? courier.getState():"NA");
                dataRow.createCell(8).setCellValue(courier.getCountry() != null ? courier.getCountry():"NA");
                dataRow.createCell(9).setCellValue(courier.getContactPerson() != null ? courier.getContactPerson():"NA");
                dataRow.createCell(10).setCellValue(courier.getLatLong() != null ? courier.getLatLong():"NA");
                dataRow.createCell(11).setCellValue(courier.getServiceTypeId() != null ? courier.getServiceTypeId().toString():"NA");
                dataRow.createCell(12).setCellValue(courier.getServiceProviderId() != null ? courier.getServiceProviderId().toString():"NA");
                dataRow.createCell(13).setCellValue(courier.getToken() != null ? courier.getToken():"NA");
                dataRow.createCell(14).setCellValue(courier.getBeneficiry() != null ? courier.getBeneficiry():"NA");
                dataRow.createCell(15).setCellValue(courier.getAccountNo() != null ? courier.getAccountNo():"NA");
                dataRow.createCell(16).setCellValue(courier.getBankName() != null ? courier.getBankName():"NA");
                dataRow.createCell(17).setCellValue(courier.getIfscCode() != null ? courier.getIfscCode():"NA");

                dataRow.createCell(18).setCellValue(courier.getGstNumber() != null ? courier.getGstNumber():"NA");
                dataRow.createCell(19).setCellValue(courier.getServiceProviderCourierCode() != null ? courier.getServiceProviderCourierCode():"NA");
                dataRow.createCell(20).setCellValue(courier.getWeightDimentionFactor() != null ? courier.getWeightDimentionFactor().toString():"NA");
                dataRow.createCell(21).setCellValue(courier.getViewLogoAtLabel() != null ? courier.getViewLogoAtLabel().toString():"NA");
                dataRow.createCell(22).setCellValue(courier.getApiCode() != null ? courier.getApiCode():"NA");
                dataRow.createCell(23).setCellValue(courier.getActive() != null ? courier.getActive().toString():"NA");
                dataRow.createCell(24).setCellValue(courier.getCreateBy() != null ? courier.getCreateBy():"NA");
                dataRow.createCell(25).setCellValue(courier.getUpdateBy() != null ? courier.getUpdateBy():"NA");
                dataRow.createCell(26).setCellValue(courier.getCreateDate() != null ? courier.getCreateDate().toString():"NA");
                dataRow.createCell(27).setCellValue(courier.getUpdateDate() != null ? courier.getUpdateDate().toString():"NA");


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
                .header("Content-Disposition", "attachment; filename="+"courierReport"+ProjectConstant.EXTENSION_XLS)
                .body(new ByteArrayResource(byteArray));

    }

    @Override
    public ResponseBean<Map<String, Object>> getAllCourierByServiceWise() {
        ResponseBean responseBean = new ResponseBean();
        try{
            Iterable<ServiceType> serviceTypeIterable =  serviceTypeRepository.findAll();
            Iterator<ServiceType> serviceTypeIterator = serviceTypeIterable.iterator();
            Map<String, List<Courier>> resultMap = new HashMap<>();
            while (serviceTypeIterator.hasNext()){
                ServiceType serviceType = serviceTypeIterator.next();
                List<Courier> courierList = courierRepository.findAllByServiceTypeIdAndActive(serviceType.getId(), 1);
                if(courierList != null && !courierList.isEmpty()){
                    resultMap.put(serviceType.getServiceCode(), courierList);
                }
            }
            if(resultMap.isEmpty()){
                responseBean.setStatus(ResponseStatus.FAIL);
                responseBean.setMessage("No courier found.");
                return responseBean;
            }
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setResponseBody(resultMap);
        }catch (Exception e){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Server internal error.");
            e.printStackTrace();
        }
        return responseBean;
    }
}
