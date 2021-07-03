package com.weblearnex.app.api.bean.tracking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NimbuspostTrackingApiResponse {
    private Boolean status;
    private Data data;
    private String message;

    @lombok.Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data{
        private String id;
        private String order_id;
        private String order_number;
        private String created;
        private String awb_number;
        private String courier_id;
        private String warehouse_id;
        private String rto_warehouse_id;
        private String status;
        private String rto_awb;
        private String rto_status;
        private String shipment_info;
        private List<History> history;

        @lombok.Data
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class History{
            private String status_code;
            private String location;
            private String event_time;
            private String message;
        }
    }
}
