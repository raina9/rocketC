package com.weblearnex.app.service;

import com.weblearnex.app.constant.PaymentType;
import com.weblearnex.app.constant.ZoneType;
import com.weblearnex.app.entity.master.BulkHeader;
import com.weblearnex.app.entity.master.BulkMaster;
import com.weblearnex.app.entity.master.Pincode;
import com.weblearnex.app.entity.master.ServicablePincode;
import com.weblearnex.app.entity.order.SaleOrder;
import com.weblearnex.app.model.BulkUploadBean;
import com.weblearnex.app.model.ResponseBean;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ServicablePincodeService extends BulkUploadService{
    ResponseBean<ServicablePincode> addServicablePincode(ServicablePincode servicablePincode);
    ResponseBean<ServicablePincode> updateServicablePincode(ServicablePincode servicablePincode);
    ResponseBean<ServicablePincode> deleteServicablePincode(Long id);
    ResponseBean<List<ServicablePincode>> getAllServicablePincode();
    ResponseEntity<Resource> downloadAllServicablePincode(String bulkMasterId);
    ResponseBean<Boolean> isPickupActive(String pickupPincode);
    ResponseBean<Boolean> isDropActive(String dropPincode, PaymentType paymentType);
    ResponseEntity<Resource> getAllCourierServicablePincode(String courierCode);
    ResponseBean<List<String>>getClientServiceProviders(String sourcePincode, String dropPincode, PaymentType paymentType, List<String> alludeCourier);
    BulkUploadBean uploadServicablePincode(MultipartFile file, BulkUploadBean bulkUploadBean, BulkMaster bulkMaster,  List<BulkHeader> bulkHeaders);
}
