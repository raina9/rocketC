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
@Table(name = "client_courier_warehouse_mapping")
public class ClientCourierWarehouseMapping implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "client_courier_warehouse_mapping_id", updatable=false, nullable=false)
    private Long id;

    private String clientCode; // DRopDown
    private String clientWarehouseCode;
    private String serviceProviderCode;
    private Long serviceProviderID;
    private String serviceProviderWarehouseCode;
    private Integer active = 1;
}
