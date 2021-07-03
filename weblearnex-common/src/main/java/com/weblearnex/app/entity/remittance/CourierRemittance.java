package com.weblearnex.app.entity.remittance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "courier_remittance")
public class CourierRemittance implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "courier_remittance_id", updatable=false, nullable=false)
    private Long id;

    @Column(unique = true, updatable=false, nullable=false)
    private String remittanceNo;
    private String courierCode;
    private Double totalAmountReceived;
    private Date date;
    @ElementCollection
    private List<String> awbList;
    private Integer totalShipment;
    private String createBy;
}
