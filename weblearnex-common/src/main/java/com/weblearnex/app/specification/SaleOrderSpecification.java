package com.weblearnex.app.specification;

import com.weblearnex.app.entity.order.SaleOrder;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class SaleOrderSpecification {

    public static Specification<SaleOrder> courierCodeNotNull(){
        Specification<SaleOrder> specification = (root, query, criteriaBuilder) -> {
            return criteriaBuilder.not(root.get("courierCode").isNull());
        };
        return specification;
    }
    public static Specification<SaleOrder> courierAwbNotNull(){
        return (root, query, criteriaBuilder) -> { return criteriaBuilder.not(root.get("courierAWBNumber").isNull());};
    }
    public static Specification<SaleOrder> currentStatusIn(List<String> statusList){
        Specification<SaleOrder> specification = (root, query, criteriaBuilder) -> {
            return root.get("currentStatus.statusCode").in(statusList.toArray(new String[0]));
        };
        return specification;
    }
}
