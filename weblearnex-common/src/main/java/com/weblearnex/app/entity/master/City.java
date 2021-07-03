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
@Table(name = "city")
public class City implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "city_id", updatable=false, nullable=false)
    private Long id;
    @Column(unique = true)
    private String code;
    private String stateCode;
    private String cityName;
    private Integer active = 1;


}
