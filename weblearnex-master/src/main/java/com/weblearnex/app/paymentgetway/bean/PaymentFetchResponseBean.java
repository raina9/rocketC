package com.weblearnex.app.paymentgetway.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentFetchResponseBean {
    private String id;
    private String entity;
    private Double amount;
    private String currency;
    private String status;
    private String order_id;
    private String invoice_id;
    private Boolean international;
    private String method;
    private String description;
    private String card_id;
    private String bank;
    private String email;
    private String contact;
    private String error_code;
    private String error_description;
    private String error_reason;
    private Long created_at;
}
