package com.weblearnex.app.setup;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.weblearnex.app.constant.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "USER")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "user_id", updatable=false, nullable=false)
	private Long id;
	private String password;//Mendatry

	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	@JsonIgnoreProperties("users")
	private List<Role> role = new ArrayList<>();

	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "users_branch", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "branch_id"))
	@JsonIgnoreProperties("users")
	private List<Branch> branch;

	private String email;
	@Column(unique = true, updatable=false, nullable=false)
	private String loginId;//Mendatry
	private String fisrtName;//Mendatry
	private String lastName;//Mendatry
	private String contact;//Mendatry
	private String alternateContact;
	private String gender;//Mendatry
	private String pincode;//Mendatry
	private String address;//Mendatry
	private String department;
	private String designation;
	@Enumerated(EnumType.STRING)
	private UserType type;//Mendatry
	private String clientCode;

	private String state;
	private String city;
	private String country;
	private String latLong;
	private String panNumber;
	private String aadharNumber;

	private Boolean admin;
	private String vehicle;
	private String employeeCode;
	private Integer active = 1;
	private boolean isLoacked = false;
	private boolean isEnabled = true;
	private boolean isExpired = false;

	@Transient
	private String token;

	/*@JsonBackReference
	public Set<Role> getRole() {
		return role;
	}*/
}
