package com.weblearnex.app.setup;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "role")
public class Role implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "role_id", updatable=false, nullable=false)
	private Long id;
	private String name; //mendatry

	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "role_page", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "page_id"))
	@JsonIgnoreProperties("roles")
	private List<Page> pages;

	private String discription;
	private Integer active = 1;


	@ManyToMany(mappedBy = "role")
	@JsonIgnoreProperties("role")
	private List <User> users;

	private String createBy;
	private String updateBy;
	private Date createDate;
	private Date updateDate;
}

