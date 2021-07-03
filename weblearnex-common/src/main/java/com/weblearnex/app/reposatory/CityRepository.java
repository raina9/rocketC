package com.weblearnex.app.reposatory;

import com.weblearnex.app.entity.master.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    City findByCode(String code);
    List<City> findByActive(Integer active);
}
