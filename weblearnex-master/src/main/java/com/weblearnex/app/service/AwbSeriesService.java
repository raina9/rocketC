package com.weblearnex.app.service;

import com.weblearnex.app.constant.PaymentType;
import com.weblearnex.app.constant.SeriesType;
import com.weblearnex.app.entity.master.AwbSeries;
import com.weblearnex.app.entity.setup.Client;
import com.weblearnex.app.model.ResponseBean;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface AwbSeriesService extends BulkUploadService{
    ResponseBean<AwbSeries> addAwbSeries(AwbSeries awbSeries);
    ResponseBean<AwbSeries> updateAwbSeries(AwbSeries awbSeries);
    ResponseBean<AwbSeries> deleteAwbSeries(Long awbSeriesId);
    ResponseBean<List<AwbSeries>> getAllAwbSeries();
    ResponseBean<AwbSeries> findByAwbSeries(String awbSeries);
    ResponseEntity<Resource> downloadAllAwbSeries(String bulkMasterId);
    ResponseBean<String> getAutoAwbNumber(Client client, PaymentType paymentType);
    ResponseEntity<Resource> autoGenerateBulkSeries(String clientCode, int totalSeriesCount, PaymentType paymentType) throws Exception;
    ResponseEntity<Resource> downloadPendingAwbSeries(SeriesType seriesType, String typeValue);
}
