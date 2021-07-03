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
@Table(name = "order_lbh")
public class OrderLBH implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "order_lbh_id", updatable=false, nullable=false)
    private Long id;

    @Column(unique = true, updatable=false, nullable=false)
    private String awbNumber;

    private Double clientWeight;
    private Double clientLength;
    private Double clientWidth;
    private Double clientHeight;

    private Double courierWeight;
    private Double courierLength;
    private Double courierWidth;
    private Double courierHeight;

    private Double selfWeight;
    private Double selfLength;
    private Double selfWidth;
    private Double selfHeight;

}
