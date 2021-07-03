package com.weblearnex.app.model;

import com.weblearnex.app.constant.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CourierServiceabilityBean {

    private String clientCode;
    private PaymentType  paymentType;
    private String sourcePincode;
    private String destinationPincode;
    private Double length;
    private Double breadth;
    private Double height;
    private Double weight;
    private Double codAmount;

}
