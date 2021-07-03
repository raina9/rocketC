package com.weblearnex.app.reposatory;


import com.weblearnex.app.entity.master.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    Country findByCode(String code);
    List<Country> findByActive(Integer active);
}
