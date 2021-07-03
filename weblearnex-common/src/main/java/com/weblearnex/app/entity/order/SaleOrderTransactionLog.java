package com.weblearnex.app.entity.order;

import com.weblearnex.app.constant.TransactionType;
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
@Table(name = "sale_order_transaction_log")
public class SaleOrderTransactionLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "sale_order_transaction_log_id", updatable=false, nullable=false)
    private Long id;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    private String clientCode;
    private String awbNumber;
    private String clientOrderId;
    private Double amount;
    private Double previousAmount;
    private Double modifiedAmount;
    private Double shipmentWeight;
    private String remarks;
    private String date;
    private Long createDate;

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
