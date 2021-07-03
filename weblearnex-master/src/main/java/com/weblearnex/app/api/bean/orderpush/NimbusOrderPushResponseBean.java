package com.weblearnex.app.api.bean.orderpush;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NimbusOrderPushResponseBean {
    private Boolean status;
    private Data data;
    private String message;

    @lombok.Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data{
        private String order_id;
        private String shipment_id;
        private String awb_number;
        private String courier_id;
        private String courier_name;
        private String status;
        private String additional_info;
        private String payment_type;
        private String label;
        private String manifest;
    }
}
