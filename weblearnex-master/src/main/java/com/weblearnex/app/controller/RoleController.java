package com.weblearnex.app.controller;

import com.weblearnex.app.entity.master.BulkMaster;
import com.weblearnex.app.entity.setup.Role;
import com.weblearnex.app.model.ResponseBean;
import com.weblearnex.app.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/addRole")
    public ResponseEntity<ResponseBean> addRole(@RequestBody Role role) {
        return new ResponseEntity<ResponseBean>(roleService.addRole(role), HttpStatus.OK);
    }

    @PutMapping("/updateRole")
    public ResponseEntity<ResponseBean> updateRole(@RequestBody Role role) {
        return new ResponseEntity<ResponseBean>(roleService.updateRole(role), HttpStatus.OK);
    }
    @DeleteMapping("/deleteRole/{roleId}")
    public ResponseEntity<ResponseBean> deleteRole(@PathVariable Long roleId) {
        return new ResponseEntity<ResponseBean>(roleService.deleteRole(roleId), HttpStatus.OK);
    }
    @GetMapping("/getAllRole")
    public ResponseEntity<ResponseBean> getAllRole() {
        return new ResponseEntity<ResponseBean>(roleService.getAllRole(), HttpStatus.OK);
    }
    @GetMapping("/findByRoleId/{id}")
    public ResponseEntity<ResponseBean> findByRoleId(@PathVariable(value = "id")Long id) {
        return new ResponseEntity<ResponseBean>(roleService.findByRoleId(id), HttpStatus.OK);
    }
    @PostMapping(value = "/role/getAllPaginationAndSort")
    public DataTablesOutput<Role> getAllRolePaginationAndSort(@RequestBody DataTablesInput input) {
        return roleService.getAllRolePaginationAndSort(input);
    }

}
