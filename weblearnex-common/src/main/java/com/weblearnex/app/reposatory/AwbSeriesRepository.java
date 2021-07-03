package com.weblearnex.app.reposatory;

import com.weblearnex.app.constant.PaymentType;
import com.weblearnex.app.constant.SeriesType;
import com.weblearnex.app.entity.master.AwbSeries;
import com.weblearnex.app.entity.order.SaleOrder;
import com.weblearnex.app.entity.setup.Status;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AwbSeriesRepository extends JpaRepository<AwbSeries, Long> {

    AwbSeries findByAwbNumber(String awbNumber);
    AwbSeries findBySeriesType(SeriesType seriesType);
    AwbSeries findByPaymentType(PaymentType paymentType);
    AwbSeries findByEntityCode(String entityCode);
    boolean existsAwbSeriesByAwbNumber(String awbNumber);
    void delete(AwbSeries awbSeries);
    List<AwbSeries> findAllBySeriesTypeAndEntityCode(SeriesType seriesType, String entityCode);
}
