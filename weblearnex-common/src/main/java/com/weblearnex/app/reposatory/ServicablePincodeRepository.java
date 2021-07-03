package com.weblearnex.app.reposatory;

import com.weblearnex.app.entity.master.ServicablePincode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public interface ServicablePincodeRepository extends JpaRepository<ServicablePincode, Long> {

    default List<ServicablePincode> saveAllAndFlush(List<ServicablePincode> servicablePincodes) {
        List<ServicablePincode> list = saveAll(servicablePincodes);
        flush();
        return list;
    }
    ServicablePincode findByPinCode(String pincode);
    List<ServicablePincode> findAllByBranchCode(String branchCode);
    ServicablePincode findByPinCodeAndCourierCode(String pincode, String courierCode);
    @Query(value = "select sp.id from ServicablePincode sp where sp.pinCode= :pincode AND sp.courierCode= :courierCode")
    Long findOnlyIdByPinCodeAndCourierCode(@Param("pincode")String pincode, @Param("courierCode")String courierCode);

    boolean existsByPinCodeAndPickupActiveAndActive(String pincode, Integer pickupActive, Integer active);
    boolean existsByPinCodeAndDropActiveAndCodActiveAndActive(String pincode, Integer dropActive, Integer codActive, Integer active);
    boolean existsByPinCodeAndDropActiveAndPrepaidActiveAndActive(String pincode, Integer dropActive, Integer prepaidActive, Integer active);
    List<ServicablePincode> findByCourierCodeIn(List<String> courierCodeList);
}
