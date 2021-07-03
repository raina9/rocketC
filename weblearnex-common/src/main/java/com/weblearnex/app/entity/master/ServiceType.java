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
@Table(name = "serviceType")
public class ServiceType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "serviceType_id", updatable=false, nullable=false)
    private Long id;
    private String serviceCode; //Mandatory
    private String serviceName; //Mandatory
    private String extra;
    private Integer active = 1;

}
