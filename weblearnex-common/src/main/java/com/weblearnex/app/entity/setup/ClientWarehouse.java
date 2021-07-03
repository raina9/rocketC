package com.weblearnex.app.entity.setup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "client_warehouse")
public class ClientWarehouse implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "client_warehouse_id", updatable=false, nullable=false)
    private Long id;

    private String clientCode;  // Client dropdown
    @Column(unique = true, updatable=false, nullable=false)
    private String warehouseCode;
    private String warehouseName;
    private String contactPersonName;
    private String contactNumber;
    private String alternateContact;  // optional
    private String email;             // optional
    private String address;
    private String pinCode;
    private String state;
    private String city;
    private String country;
    private Integer active = 1;
}
