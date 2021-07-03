package com.weblearnex.app.entity.setup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "status_flow")
public class StatusFlow implements Serializable {

    private static final long serialVersionUID = 6381211981L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "status_flow_id", updatable=false, nullable=false)
    private Long id;
    private String statusFlowName;
    private String statusTransitionsList;
    private Integer active = 1;
}//"{"id":64,"statusFlowName":"gdghhd","statusTransitionsList":"33","active":1}"
