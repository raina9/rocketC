package com.weblearnex.app.entity.setup;

import com.weblearnex.app.constant.StatusType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "status")
public class Status implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "status_id", updatable=false, nullable=false)
    private Long id;
    private String statusName;
    @Column(unique = true, updatable=false, nullable=false)
    private String statusCode;

    @Enumerated(EnumType.STRING)
    private StatusType statusType;
    private String displayName;
    private Boolean external;

    private String createBy;
    private String updateBy;
    private Date createDate;
    private Date updateDate;
    private Integer active = 1;
}
