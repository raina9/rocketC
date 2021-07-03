package com.weblearnex.app.entity.setup;

import com.weblearnex.app.constant.DeliveryType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "client_facility")
public class ClientFacility implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "client_facility_id", updatable=false, nullable=false)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, targetEntity = Client.class)
    @JoinColumn(name = "client_fk", updatable = false)
    //@OneToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "client_fk")
    private Client client;
    private Boolean awbAutoGenerate = false;
    private Boolean smsServiceActive = false;
    private Boolean mailServiceActive = false;
    private Boolean walletActive = false;
    private Integer deliveryAttempt;
    @Enumerated(EnumType.STRING)
    private DeliveryType deliveryType;
    @ElementCollection
    @Type(type="text")
    private Map<String, String> serviceCourierMap; // MapKey-->ServiceTypeCode  Value--> List of Couriers
    private String createBy;
    private String updateBy;
    private Date createDate;
    private Date updateDate;
    private Integer active = 1;
    @Column(unique = true, updatable=false, nullable=false)
    private Long clientId;
    private String rateCardTypeCode;

}
