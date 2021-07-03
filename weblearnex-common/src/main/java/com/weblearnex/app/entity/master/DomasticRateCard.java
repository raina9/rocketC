package com.weblearnex.app.entity.master;

import com.weblearnex.app.constant.FreightType;
import com.weblearnex.app.entity.master.RateMatrix;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "domastic_rate_card")
public class DomasticRateCard implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "domastic_ratecard_id", updatable=false, nullable=false)
    private Long id;
    private String serviceProviderCode;
    //private String serviceTypeCode;
    // @Column(unique = true, updatable=false, nullable=false)
    private String courierCode;
    @Column(unique = true, updatable=false, nullable=false)
    private String rateCardCode;
    private String rateCardName;
    private String rateCardTypeCode;

    @Enumerated(EnumType.STRING)
    private FreightType freightType;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="domastic_ratecard_id")
    private List<RateMatrix> rateMatrixList;
    private Integer active = 1;

}
