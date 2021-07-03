package com.weblearnex.app.entity.setup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "configration")
public class Configration implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "configration_id", updatable=false, nullable=false)
    private Long id;
    private String configType;//mendatry
    private String configValue; //mendatry
    private String configCode; //mendatry, dublicates
    private String discription;
    @Column(length = 3000)
    @Type(type="text")
    private String extra1;
    @Column(length = 3000)
    @Type(type="text")
    private String extra2;
    private Integer active = 1;

    private String createBy;
    private String updateBy;
    private Date createDate;
    private Date updateDate;

}
