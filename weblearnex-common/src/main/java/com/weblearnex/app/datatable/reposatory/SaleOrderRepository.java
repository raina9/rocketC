package com.weblearnex.app.datatable.reposatory;

import com.weblearnex.app.constant.PaymentType;
import com.weblearnex.app.constant.RemittanceStatus;
import com.weblearnex.app.entity.order.SaleOrder;
import com.weblearnex.app.entity.setup.Client;
import com.weblearnex.app.entity.setup.Status;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SaleOrderRepository extends DataTablesRepository<SaleOrder, Long> {
    SaleOrder findByReferanceNo(String referanceNo);
    List<SaleOrder> findByReferanceNoIn(List<String> referanceNoList);
    List<SaleOrder> findByCourierAWBNumberIn(List<String> courierAwbList);
    List<SaleOrder> findByClientOrderIdIn(List<String> clientOrderId);
    boolean existsSaleOrderByReferanceNo(String awbNumber);
    boolean existsSaleOrderByCourierAWBNumber(String courierAwb);
    boolean existsSaleOrderByReferanceNoAndCourierCodeAndCourierAWBNumber(String awbNumber, String courierCode, String courierAwbNo);
    List<SaleOrder> findAllByCourierCodeAndCurrentStatusNotIn(String courierCode, List<Status> statusList);
    List<SaleOrder> findByHandOverAndThreePlManifestGenerated(Boolean handOver,Boolean threePlManifestGenerated);
    List<SaleOrder> findByHandOverAndThreePlManifestGeneratedAndClientCode(Boolean handOver,Boolean threePlManifestGenerated, String clientCode);
    List<SaleOrder> findAllByClientCodeAndClientRemittanceAndPaymentTypeAndCurrentStatus(
            String clientCode, RemittanceStatus remittanceStatus, PaymentType paymentType, Status status);


    List<SaleOrder> findAllByCourierCodeAndCourierRemittanceAndPaymentTypeAndCurrentStatus(
            String courierCode, RemittanceStatus remittanceStatus, PaymentType paymentType, Status status);
    @Query(value = "select t.referanceNo from SaleOrder t where t.orderDate BETWEEN :startDate AND :endDate")
    public List<Object[]> getAllBetweenDates(@Param("startDate")Date startDate,@Param("endDate")Date endDate);

    @Query(value = "select t.referanceNo from SaleOrder t where t.clientCode= :clientC AND t.orderDate BETWEEN :startDate AND :endDate ")
    public List<Object[]> getAllBetweenDatesAndclientCode(@Param("startDate")Date startDate,@Param("endDate")Date endDate, @Param("clientC")String clientCode);

    @Query(value = "select t.referanceNo from SaleOrder t where t.currentStatus.statusCode= :statusCode AND t.clientCode= :clientCode")
    public List<Object[]> getAllAwbByCurrentStatusAndClientCode(@Param("statusCode")String statusCode,@Param("clientCode")String clientCode);

    @Query(value = "select t.referanceNo from SaleOrder t where t.currentStatus.statusCode= :statusCode")
    public List<Object[]> getAllAwbByCurrentStatus(@Param("statusCode")String statusCode);


    @Query(value = "select t.referanceNo from SaleOrder t where t.clientRemittance= :clientRemittance AND t.paymentType= :paymentType AND t.currentStatus.statusCode= :statusCode")
    public List<Object[]> findAllByClientRemittanceAndPaymentTypeAndCurrentStatus(@Param("clientRemittance")String clientRemittance,@Param("paymentType")PaymentType paymentType,@Param("statusCode")String statusCode);

    @Query(value = "select t.referanceNo from SaleOrder t where t.clientCode= :clientCode AND t.clientRemittance= :clientRemittance AND t.paymentType= :paymentType AND t.currentStatus.statusCode= :statusCode")
    public List<Object[]> getAllByClientCodeAndClientRemittanceAndPaymentTypeAndCurrentStatus(@Param("clientCode")String clientCode,@Param("clientRemittance")String clientRemittance,@Param("paymentType")PaymentType paymentType,@Param("statusCode")String statusCode);
}
