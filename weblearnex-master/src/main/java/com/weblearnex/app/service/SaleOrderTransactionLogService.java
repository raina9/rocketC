package com.weblearnex.app.service;

import com.weblearnex.app.entity.order.SaleOrderTransactionLog;
import com.weblearnex.app.entity.setup.Role;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.paymentgetway.bean.PaymentFetchResponseBean;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface SaleOrderTransactionLogService {
    ResponseBean<SaleOrderTransactionLog>addSaleOrderTansaction(SaleOrderTransactionLog saleOrderTransactionLog);
    DataTablesOutput<SaleOrderTransactionLog> getAllSaleOrderTransactions(DataTablesInput input);
    ResponseBean clientWalletManualRecharge(SaleOrderTransactionLog saleOrderTransactionLog);
    ResponseBean deletesSaleOrderTransactionLog(String id);
    ResponseEntity<Resource> downloadSaleOrderTransactionsReport();
}
