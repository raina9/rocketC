package com.weblearnex.app.entity.master;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "courier_status_mapping")
public class CourierStatusMapping implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "COURIER_STATUS_MAPPING_ID", updatable=false, nullable=false)
    private Long id;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="COURIER_STATUS_MAPPING_ID")
    private List<StatusMapping> statusMappings;
    private Long courierId;
    private Long serviceProviderId;

    @Column(length = 1000)
    @Type(type="text")
    private String token;


}
