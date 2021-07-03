package com.weblearnex.app.entity.master;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "bulk_master")
public class BulkMaster implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "bulk_master_id", updatable=false, nullable=false)
    private Long id;
    private String name;
    @Column(length = 1500)
    @Type(type="text")
    private String bulkHeaderIds; // Comma seperated bulkheader id
    @Column(length = 1500)
    @Type(type="text")
    private String bulkHeaderSequenceIds; // Comma seperated bulkheader id
    private Integer active = 1;

}
