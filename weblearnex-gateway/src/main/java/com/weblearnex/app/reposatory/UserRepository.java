package com.weblearnex.app.reposatory;

import com.weblearnex.app.constant.UserType;
import com.weblearnex.app.setup.User;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByFisrtName(String fisrtName);
    User findByLastName(String lastName);
    User findByContact(String contact);
    User findByGender(String gender);
    List<User> findByPincode(String pincode);
    User findByAddress(String address);
    User findByType(UserType type);
    User findByLoginId(String loginId);
}
