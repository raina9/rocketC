package com.weblearnex.app.entity.master;

import com.weblearnex.app.constant.ZoneType;
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
@Table(name = "rate_matrix")
public class RateMatrix implements Serializable {

    private static final long serialVersionUID = -5537796509740841356L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "rate_matrix_id", updatable=false, nullable=false)
    private Long id;

    @Column(name = "domastic_ratecard_id")
    private  Long domastic_ratecard_id;

   /* @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private FlatFreight flatFreight;*/
   @OneToOne(cascade = CascadeType.ALL)
   @JoinTable(name="RateMatrix_FlatFreight", joinColumns = @JoinColumn(name="RateMatrix_ID"),
           inverseJoinColumns = @JoinColumn(name="FlatFreight_ID"))
   private FlatFreight flatFreight;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="rate_matrix_id")
    private List<SlabFreight> slabFreights;

    @Enumerated(EnumType.STRING)
    private ZoneType zoneType;
    private Double awbCharge;				//mandate, unit value
    private Double minCodCharge;			//mandate, unit value
    private Double codChargePercent;		//mandate, unit percent
    private Double fsc;						//mandate, unit percent
    //private Double serviceTax;
    private Double gst;						//mandate, unit percent
    private Double rovCharge;				//optional, unit percent
    private Double fov;						//optional, unit percent
    private Double handlingCharges;			//optional, unit value
    //private Double extraDeliveryCharges;
    private Double oda;						//mandate, unit value
    private Double insuranceCharges;		//mandate, unit percent
    //private Double lastMileCharges;
    private Double covidCharges;
    private Double otherCharges;			//optional, unit value
}
