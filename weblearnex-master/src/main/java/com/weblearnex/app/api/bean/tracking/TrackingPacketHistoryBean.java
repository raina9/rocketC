package com.weblearnex.app.api.bean.tracking;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class TrackingPacketHistoryBean {
    private String statusCode;
    private String ndrCode;
    private String location;
    private Date date;
    private String rtoReason;
    private String remarks;
    private String extra1;
    private String extra2;
}
