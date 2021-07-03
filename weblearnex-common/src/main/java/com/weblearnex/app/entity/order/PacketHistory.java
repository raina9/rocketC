package com.weblearnex.app.entity.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "packet_history")
public class PacketHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "packet_history_id", updatable=false, nullable=false)
    private Long id;

    @Column(name = "sale_order_id")
    private  Long saleOrderId;

    private String fromStatusCode;
    private String toStatusCode;
    private String location;
    private Date date;
    private String rtoReason;
    private String ndrReason;
    private String remarks;
    private Date createdDate;
    private String createdByCode;
    private String createdByName;
    private String latLong;
    private boolean appRequest;

    // this variable used only fro view purpose of status name.
    @Transient
    private String statusName;
}
