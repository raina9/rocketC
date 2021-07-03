package com.weblearnex.app.entity.setup;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "branch")
public class Branch implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "branch_id", updatable=false, nullable=false)
    private Long id;
    @ManyToMany(mappedBy = "branch")
    @JsonIgnoreProperties("branch")
    private List<User> users;

    private String name;
    @Column(unique = true, updatable=false, nullable=false)
    private String branchCode;
    private String address;
    private String mobileNo;
    private String emailId;
    private String pincode;
    private String city;
    private String state;
    private String country;
    private String parentBranchId;
    //private Boolean autoAWB;
    private Boolean destinationBaggingAllow;
    private Boolean manulManifestAllow;
    private Integer active = 1;
    private String latLong;
    private String createBy;
    private String updateBy;
    private Date createDate;
    private Date updateDate;
}
