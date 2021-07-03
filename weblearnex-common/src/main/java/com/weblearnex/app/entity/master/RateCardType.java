package com.weblearnex.app.entity.master;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "rate_card_type")
public class RateCardType {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "rate_card_type_id", updatable=false, nullable=false)
    private Long id;
    @Column(unique = true, updatable=false, nullable=false)
    private String typeCode;
    private String typeName;
    private Integer active = 1;

}
