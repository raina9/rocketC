package com.weblearnex.app.entity.order;

import com.weblearnex.app.constant.*;
import com.weblearnex.app.entity.setup.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "sale_order", indexes = {@Index(columnList = "referanceNo"), @Index(columnList = "courierCode"),
        @Index(columnList = "clientCode")})
public class SaleOrder implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "sale_order_id", updatable=false, nullable=false)
    private Long id;

    @Column(unique = true, updatable=false, nullable=false)
    private String referanceNo;
    @OneToOne
    private Status currentStatus;

    @Enumerated(EnumType.STRING)
    private OrderSourceType orderSourceType;

    @Enumerated(EnumType.STRING)
    private OrderFlowType orderFlowType;

    private String sourceBranchCode;
    private String currentBranchCode;
    private String nextBranchCode;
    private String destinationBranchCode;

    private String courierCode;
    private String courierAWBNumber;

    private Boolean handOver = false;
    private String handOverBranchCode;
    private Boolean threePlManifestGenerated = false;

    private String senderName;
    private String senderMobileNumber;
    private String senderAltNumber;
    private String senderEmail;
    private String senderPinCode;
    private String senderCity;
    private String senderState;
    private String senderCountry;
    @Column(length = 1500)
    @Type(type="text")
    private String senderAddress;
    private String senderLandmark;

    private String consigneeName;
    private String consigneeMobileNumber;
    private String consigneeAlternateNumber;
    private String consigneeEmailId;
    private String consigneePinCode;
    private String consigneeCity;
    private String consigneeState;
    private String consigneeCountry;
    @Column(length = 2500)
    @Type(type="text")
    private String consigneeAddress;
    private String consigneeLandmark;

    private String productSKU;
    @Column(length = 1500)
    @Type(type="text")
    private String productName;
    private Integer productQuantity = 1;
    private Double productPrice;
    private Double codAmount;
    private Double length;
    private Double breadth;
    private Double hight;
    private Double weight;
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType; //COD or PREPAID
    private String clientCode;
    private String clientOrderId;
    private String orderType;
    private String pickupLocationId;  // Client warehouse code
    private String pickupLocation;     // Client warehouse name
    private String ewaybill;
    private String latitude;
    private String longitude;
    private String productImageUrl;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="sale_order_id")

    /*@OneToMany(targetEntity=PacketHistory.class,cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, orphanRemoval = true)*/
    private List<PacketHistory> packetHistory = new ArrayList<PacketHistory>(3);

    private Boolean softDataReceived = false;
    private Integer drsAttemptedCount = 0;
    private String deliveryAttemptedReason;
    private String rtoReason;
    private Date rtoDate;
    private Date deliveredDate;
    private Date orderDate;
    private Date scanDate;
    @Enumerated(EnumType.STRING)
    private ZoneType zoneType;
    private Double deliveryCharge;
    private Double chargeableWeight;
    private String threeplManifest;
    @Enumerated(EnumType.STRING)
    private RemittanceStatus clientRemittance;
    private Long remittanceId;
    // courier remittance
    @Enumerated(EnumType.STRING)
    private RemittanceStatus courierRemittance;
    private Long courierRemittanceId;
    @Transient
    private String msg;
    @Transient
    private boolean found= true;


    public String getSenderPinCode() {
        return senderPinCode;
    }
}
