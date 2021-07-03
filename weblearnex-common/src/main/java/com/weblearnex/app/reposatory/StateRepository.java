package com.weblearnex.app.reposatory;

import com.weblearnex.app.entity.master.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {
    State findByCode(String code);
    List<State> findByActive(Integer active);
}
