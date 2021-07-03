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
@Table(name = "data_push_success_log")
public class DataPushSuccessLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id", updatable=false, nullable=false)
    private Long id;
    private String awb;
    private String url;
    @Column(length = 3000)
    @Type(type="text")
    private String request;
    @Column(length = 3000)
    @Type(type="text")
    private String response;
    private String message;
    private String status;
    private String date;
    private String clientCode;
}
