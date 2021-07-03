package com.weblearnex.app.entity.remittance;

import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.weblearnex.app.entity.setup.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "client_remittance")
public class ClientRemittance {
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name = "client_remittance_id", updatable=false, nullable=false)
	private Long id;
	private String clientCode;
	@Column(unique = true, updatable=false, nullable=false)
	private String remittanceNo;
	private Date date;
	private Double collectedAmt;
	private Double depositedAmt;
	@OneToOne
	private Status status;
	//@Column(length = 10000)
	//private String awbs;
	@ElementCollection
	private List<String> awbList;
	private Integer totalShipment;

	private String bankName;
	private String accountNo;
	private String transactionNo;
	private String depositSlip;
	private String depositDate;
}
