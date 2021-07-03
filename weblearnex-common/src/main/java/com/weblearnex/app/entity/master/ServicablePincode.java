package com.weblearnex.app.entity.master;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "servicablePincode", indexes = {@Index(columnList = "pinCode"), @Index(columnList = "courierCode"),
        @Index(columnList = "active")})
public class ServicablePincode implements Serializable {

   /* @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "servicablePincode_id", updatable=false, nullable=false)
    private Long id;*/
   @Id
   @SequenceGenerator(name = "mySeqGen", sequenceName = "mySeq", initialValue = 1, allocationSize = 100)
   @GeneratedValue(generator = "mySeqGen")
   @Column(name = "servicablePincode_id")
   private Long id;

    @Column(name="pincode_id")
    private Long pincode_id;
    private String pinCode;
    private String branchCode;
    private Integer prepaidActive = 0;
    private Integer codActive = 0;
    private Integer pickupActive = 0;
    private Integer dropActive = 0;
    private String courierCode;
    private String routeCode; // Optional
    private Integer active = 0;

    private String createBy;
    private String updateBy;
    private Date createDate;
    private Date updateDate;

}
