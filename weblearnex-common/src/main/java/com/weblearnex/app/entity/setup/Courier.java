package com.weblearnex.app.entity.setup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "courier")
public class Courier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "courier_id", updatable=false, nullable=false)
    private Long id;
    private String courierName;
    @Column(unique = true, updatable=false, nullable=false)
    private String courierCode;
    private String registeredAdd;

    @Transient
    private String serviceTypeName;
    private Long serviceTypeId;    // Add service type dropdown
    @Transient
    private String serviceProviderName;
    private Long serviceProviderId;

    private String pincode;
    private String city;
    private String state;
    private String country;
    private String contactPerson;
    private String mobile;
    private String email;
    private String gstNumber;
    private String token;
    private String serviceProviderCourierCode;
    private Double weightDimentionFactor;
    private String latLong;
    private String apiCode;
   /* private String panCardDoc;
    private String aadhaarCardDoc;
    private String cancelChequeDoc;
    private String additionalDoc1;*/
    private String beneficiry;
    private String accountNo;
    private String bankName;
    private String ifscCode;

    private String createBy;
    private String updateBy;
    private Date createDate;
    private Date updateDate;
    private Boolean viewLogoAtLabel;
    private Integer active = 1;
    @Transient
    private String msg;
    @Transient
    private boolean found= true;


    private String uploadgst;
    private String uploadAgreement;
}
