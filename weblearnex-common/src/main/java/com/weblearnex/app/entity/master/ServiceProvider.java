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
@Table(name = "serviceProvider")
public class ServiceProvider implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "serviceProvider_id", updatable=false, nullable=false)
    private Long id;
    private String serviceProviderCode; //Mandatory
    private String serviceProviderName; //Mandatory
    private String extra;
    private Integer active = 1;

}
