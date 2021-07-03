package com.weblearnex.app.entity.paymentwetway;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "client_wallet")
public class ClientWallet implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "client_wallet_id", updatable=false, nullable=false)
    private Long id;

    @Column(unique = true, updatable=false, nullable=false)
    private String clientCode;
    private Double walletAmount;
    private Double rewardAmount;
    private Double holdAmount;
    private Date lastWalletRechargeDate;
    private String remarks;

}
