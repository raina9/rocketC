package com.weblearnex.app.entity.setup;

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
@Table(name = "status_transition")
public class StatusTransition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "status_transition_id", updatable=false, nullable=false)
    private Long id;
    private String statusTransitionName;
    private String fromStatusCode;
    private String toStatusCode;
    @Column(unique = true, updatable=true, nullable=false)
    private String statusTransitionCode;

    private String createBy;
    private String updateBy;
    private Date createDate;
    private Date updateDate;
    private Integer active = 1;
}
