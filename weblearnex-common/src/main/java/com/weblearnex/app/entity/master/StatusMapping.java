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
@Table(name = "status_mapping")
public class StatusMapping implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "STATUS_MAPPING", updatable=false, nullable=false)
    private Long id;
    @Column(name="COURIER_STATUS_MAPPING_ID")
    private Long courierStatusMappingId;
    private String selfStatusCode;
    private String courierStatusCode;
    private String ndrCode;
    private String extra;
}
