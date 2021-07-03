package com.weblearnex.app.entity.setup;

import com.weblearnex.app.entity.master.Pincode;
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
@Table(name = "client")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "client_id", updatable=false, nullable=false)
    private Long id;

    @Column(unique = true, updatable=false, nullable=false)
    private String clientCode;
    private String clientName;
    //private String displayName;
    private String registeredAddress;
    private String pincode;
    private String city;
    private String state;
    private String country;
    private String contactPerson;
    //private String phone;
    private String mobile;
    private String email;
    private String awbSeriesPrefix;
    private Long awbSeriesSequence;
    private String token;

    private String beneficiry;
    private String accountNo;
    private String bankName;
    private String ifscCode;
    private String panNumber;
    private String aadhaarNumber;
    private String gstNumber;
    private String accountManager;
    private String saleManager;
    private String latLong;

    private String createBy;
    private String updateBy;
    private Date createDate;
    private Date updateDate;
    private Integer active = 1;

    private String uploadAadharCard;
    private String uploadPanCard;
    private String uploadgst;
    private String uploadAgreement;
}
