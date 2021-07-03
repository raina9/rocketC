package com.weblearnex.app.setup;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "page")
public class Page implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "page_id", updatable=false, nullable=false)
    private Long id;
    @ManyToMany(mappedBy = "pages")
    @JsonIgnoreProperties("pages")
    private List<Role> roles;

    private String module; //mendatry
    private String subModule; //mendatry
    private String page; //mendatry, dublicate
    private String displayName; //mendatry
    private String pageURL; //mendatry
    private String description;
    private Integer active = 1;

    private String createBy;
    private String updateBy;
    private Date createDate;
    private Date updateDate;


}
