package com.weblearnex.app.entity.master;

import com.weblearnex.app.constant.PaymentType;
import com.weblearnex.app.constant.SeriesType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "awbseries")
public class AwbSeries implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "awbseries_id", updatable=false, nullable=false)
    private Long id;
    private String awbNumber;

    @Enumerated(EnumType.STRING)
    private SeriesType seriesType;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    private String entityCode;
}
