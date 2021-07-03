package com.weblearnex.app.reposatory;

import com.weblearnex.app.entity.master.OrderLBH;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLBHRepository extends JpaRepository<OrderLBH, Long> {

    OrderLBH findByAwbNumber(String awbNumber);
}
