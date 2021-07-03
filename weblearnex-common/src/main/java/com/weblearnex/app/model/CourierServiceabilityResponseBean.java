package com.weblearnex.app.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class CourierServiceabilityResponseBean {
    private String courierCode;
    private String courierName;
    private Double serviceCharge;
    private String serviceCode;
    private String serviceName;

}
