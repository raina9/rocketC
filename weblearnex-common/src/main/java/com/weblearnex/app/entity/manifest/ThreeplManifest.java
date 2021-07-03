package com.weblearnex.app.entity.manifest;

import com.weblearnex.app.entity.order.PacketHistory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "threepl_manifest")
public class ThreeplManifest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "threepl_manifest_id", updatable=false, nullable=false)
    private Long id;
    @Column(unique = true, updatable=false, nullable=false)
    private String manifestNumber;  // date in milisecond
    private String courierCode;
    private String handoverBranch;
    private String clientCode;
    private String createBy;
    private Date createDate;
    private Boolean podUpload=false;
    private Boolean heandOver=false;
    @Column(length = 4000)
    @Type(type="text")
    private String awbNumbers; // comma seperated list
    private String podFileName;

}
