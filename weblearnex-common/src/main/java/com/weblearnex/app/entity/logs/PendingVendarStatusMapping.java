package com.weblearnex.app.entity.logs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "pending_vendar_status_mapping")
public class PendingVendarStatusMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id", updatable=false, nullable=false)
    private Long id;

    private String statusCode;
    private String date;
    private String ndrCode;
    private String remark;
    @Column(length = 1000)
    @Type(type="text")
    private String response;
    private String courierCode;

}
