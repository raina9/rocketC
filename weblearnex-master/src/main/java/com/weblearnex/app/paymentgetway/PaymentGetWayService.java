package com.weblearnex.app.paymentgetway;

import com.weblearnex.app.entity.paymentwetway.ClientWallet;
import com.weblearnex.app.entity.paymentwetway.PaymentGetWayLog;

import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.paymentgetway.bean.PaymentFetchResponseBean;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface PaymentGetWayService {

    ResponseBean<Map<String,String>> getPaymentGetWayCredential();
    DataTablesOutput<PaymentGetWayLog> getAllPaymentGetWayLog(DataTablesInput input);
    DataTablesOutput<ClientWallet> getAllClientWallet(DataTablesInput input);
    ResponseBean savePaymentGetWayLog(Map<String, String> paymentDetails);
    ResponseBean createPaymentOrder(Map<String,Object> paymentOrderDetails);
    ResponseBean<PaymentFetchResponseBean> fetchPaymentDetails(Map<String,String> paymentDetails);
    ResponseEntity<Resource> downloadPaymentGetWayLogsReport();
}
