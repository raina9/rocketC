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
@Table(name = "courier_priority")
public class CourierPriority implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "courier_priority_id", updatable=false, nullable=false)
    private Long id;
    @Column(unique = true, updatable=false, nullable=false)
    private String courierPriorityCode;   // Mandatory
    private String courierPriorityName;   // Mandatory
    private String prioritys;             // Mandatory
    private Long clientId;            // Mandatory
    private Long serviceTypeId;       // Mandatory
    private Integer active =1;

}
