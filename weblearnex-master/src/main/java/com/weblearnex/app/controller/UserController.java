package com.weblearnex.app.controller;


import com.weblearnex.app.constant.TrackingType;
import com.weblearnex.app.entity.master.BulkMaster;
import com.weblearnex.app.entity.setup.User;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/addUser")
    public ResponseEntity<ResponseBean> addUser(@RequestBody User user) {
        return new ResponseEntity<ResponseBean>(userService.addUser(user), HttpStatus.OK);
    }

    @PutMapping("/updateUser")
    public ResponseEntity<ResponseBean> updateUser(@RequestBody User user) {
        return new ResponseEntity<ResponseBean>(userService.updateUser(user), HttpStatus.OK);
    }
    @DeleteMapping("/deleteUser/{Id}")
    public ResponseEntity<ResponseBean> deleteUser(@PathVariable Long Id) {
        return new ResponseEntity<ResponseBean>(userService.deleteUser(Id), HttpStatus.OK);
    }
    @GetMapping("/getAllUser")
    public ResponseEntity<ResponseBean> getAllUser() {
        return new ResponseEntity<ResponseBean>(userService.getAllUser(), HttpStatus.OK);
    }
    @GetMapping("/findByUserId/{id}")
    public ResponseEntity<ResponseBean> findByUserId(@PathVariable(value = "id")Long id) {
        return new ResponseEntity<ResponseBean>(userService.findByUserId(id), HttpStatus.OK);
    }
    @PostMapping(value = "/user/getAllPaginationAndSort")
    public DataTablesOutput<User> getAllUserPaginationAndSort(@RequestBody DataTablesInput input) {
        return userService.getAllUserPaginationAndSort(input);
    }
    @GetMapping(value="/userReport")
    public ResponseEntity<Resource> userReport(HttpServletRequest request, HttpServletResponse response){
        return userService.userReport();
    }

    @GetMapping("/changePassword")
    public ResponseEntity<ResponseBean> orderCancellation (@RequestParam(value = "oldPassword") String oldPassword, @RequestParam(value = "newPassword") String newPassword) {
        return new ResponseEntity<ResponseBean>(userService.changePassword(oldPassword,newPassword), HttpStatus.OK);
    }

}
