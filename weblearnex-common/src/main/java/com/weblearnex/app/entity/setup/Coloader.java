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
@Table(name = "coloader")
public class Coloader implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "coloader_id", updatable=false, nullable=false)
    private Long id;
    private String coloaderName;  //Mendatry
    @Column(unique = true, updatable=false, nullable=false)
    private String coloaderCode;  //Mendatry & Unique
    private String registeredAdd; //Mendatry
    private String pincode;        //Mendatry
    private String city;
    private String state;
    private String country;

    private String contactPerson;  //Mendatry
    private String mobile;         //Mendatry
    private String email;           //Mendatry
    private String gstNumber;

    private String beneficiry;
    private String accountNo;
    private String bankName;
    private String ifscCode;

    private Integer active = 1;

    private String createBy;
    private String updateBy;
    private Date createDate;
    private Date updateDate;
}
