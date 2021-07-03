package com.weblearnex.app.paymentgetway.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentOrderResponseBean {
    private String id;
    private String entity;
    private Double amount;
    private Double amount_paid;
    private Double amount_due;
    private String currency;
    private String receipt;
    private String offer_id;
    private String status;
    private Integer attempts;
    private Long created_at;
}
