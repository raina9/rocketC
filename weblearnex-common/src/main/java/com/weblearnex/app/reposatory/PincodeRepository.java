package com.weblearnex.app.reposatory;

import com.weblearnex.app.entity.master.City;
import com.weblearnex.app.entity.master.Pincode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PincodeRepository extends JpaRepository<Pincode, Long> {

    Pincode findByCityCode(String cityCode);
    Pincode findByPinCode(String pincode);
    Pincode findByActive(Integer active);
    boolean existsByPinCode(String pincode);

    @Query(value = "select p.id from Pincode p where p.pinCode= :pincode")
    Long findOnlyIdByPinCode(@Param("pincode")String pincode);

    @Query(value = "select p.cityCode from Pincode p where p.pinCode= :pincode")
    String findOnlyCityCodeByPinCode(@Param("pincode")String pincode);

}
