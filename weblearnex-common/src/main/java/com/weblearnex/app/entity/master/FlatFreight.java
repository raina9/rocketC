package com.weblearnex.app.entity.master;

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
@Table(name = "flat_freight")
public class FlatFreight implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "flat_freight_id", updatable=false, nullable=false)
    private Long id;
/*
    @OneToOne(mappedBy="flatFreight",cascade=CascadeType.ALL)
    private RateMatrix rateMatrix;*/

    private Double baseWeight;
    private Double baseRate;
    private Double incrementalWeight;
    private Double incrementalRate;
}
