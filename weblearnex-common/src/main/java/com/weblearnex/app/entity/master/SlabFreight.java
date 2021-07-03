package com.weblearnex.app.entity.master;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "slab_freight")
public class SlabFreight {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "slab_freight_id", updatable=false, nullable=false)
    private Long id;

    @Column(name="rate_matrix_id")
    private Long rate_matrix_id;

    private Double fromWeight;
    private Double toWeight;
    private Double rate;

}
