package com.weblearnex.app.api.bean.orderpush;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShiprocketOrderPushResponseBeen {
    private String message;
    private Integer status_code;
    private Object errors;
    private Integer status;
    private Payload payload;

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Payload{
        private Integer pickup_location_added;
        private Integer order_created;
        private Integer awb_generated;
        private Integer label_generated;
        private Integer pickup_generated;
        private Integer manifest_generated;
        private String pickup_scheduled_date;
        private Long order_id;
        private Long shipment_id;
        private String awb_code;
        private Integer courier_company_id;
        private String courier_name;
        private Integer applied_weight;
        private Integer cod;
        private String label_url;
        private String manifest_url;
        private String routing_code;
        private String rto_routing_code;
        private String pickup_token_number;
        private String awb_assign_error;
        private String action;
        private String error_message;
    }

}
