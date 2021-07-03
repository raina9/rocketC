package com.weblearnex.app.service;

import com.weblearnex.app.constant.FreightType;
import com.weblearnex.app.constant.PaymentType;
import com.weblearnex.app.entity.master.BulkMaster;
import com.weblearnex.app.entity.master.DomasticRateCard;
import com.weblearnex.app.entity.master.RateMatrix;
import com.weblearnex.app.entity.order.SaleOrder;
import com.weblearnex.app.entity.setup.ClientFacility;
import com.weblearnex.app.entity.setup.Courier;
import com.weblearnex.app.model.BulkUploadBean;
import com.weblearnex.app.model.ResponseBean;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface DomasticRateCardService extends BulkUploadService{

    ResponseBean<DomasticRateCard> addDomasticRateCard(DomasticRateCard domasticRateCard);
    ResponseBean<DomasticRateCard> updateDomasticRateCard(DomasticRateCard domasticRateCard);
    ResponseBean<DomasticRateCard> deleteDomasticRateCard(Long domasticRateCardId);
    ResponseBean<List<DomasticRateCard>> getAllDomasticRateCards();
    ResponseBean<DomasticRateCard> findById(Long id);
    double calculateVolumetricWeight(Double length, Double breadth, Double height, Double weightFactor);
    Map<String,Double> calculateAllCharges(Double weight, Double volWeight, PaymentType paymentType, Double codAmount, FreightType freightType, RateMatrix rateMatrix);
    ResponseBean<Double> getCharge(SaleOrder saleOrder, Courier courier,  ClientFacility clientFacility);
    DataTablesOutput<DomasticRateCard> getAllDomasticRateCardPaginationAndSort(DataTablesInput input);
    ResponseEntity<Resource> viewClientRateReport();
    ResponseEntity<Resource> domesticRateCardReport();
    public BulkUploadBean uploadBulkNew(BulkUploadBean bulkUploadBean, BulkMaster bulkMaster, boolean isUpdate);
}
