package com.weblearnex.app.entity.paymentwetway;

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
@Table(name = "payment_get_way_log")
public class PaymentGetWayLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id", updatable=false, nullable=false)
    private Long id;

    private String clientCode;
    private Double amount;
    private String currency;
    private String status;
    private String paymentId;
    private String orderId;
    private String signature;
    private String method;
    private String bank;
    private String email;
    private String contact;
    private String date;
    private String remarks;
    private Long created_at;
}
