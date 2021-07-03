package com.weblearnex.app.entity.master;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weblearnex.app.entity.setup.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "pincode", indexes = {@Index(columnList = "pinCode"),@Index(columnList = "active")})
public class Pincode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "pincode_id", updatable=false, nullable=false)
    private Long id;
    private String cityCode;
    @Column(unique = true)
    private String pinCode;
    private Integer active = 1;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="pincode_id")
    private List<ServicablePincode> servicablePincode;

}
