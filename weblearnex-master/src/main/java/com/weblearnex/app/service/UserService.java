package com.weblearnex.app.service;


import com.weblearnex.app.entity.setup.User;
import com.weblearnex.app.model.ResponseBean;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseBean<User> addUser(User user);
    ResponseBean<User> updateUser(User user);
    ResponseBean<User> deleteUser(Long userId);
    ResponseBean<User> getAllUser();
    ResponseBean<User> findByUserId(Long id);
    ResponseBean<User> changePassword(String oldPassword, String newPassword);
    ResponseEntity<Resource> userReport();
    DataTablesOutput<User> getAllUserPaginationAndSort(DataTablesInput input);
}
