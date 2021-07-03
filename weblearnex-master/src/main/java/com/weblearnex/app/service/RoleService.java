package com.weblearnex.app.service;


import com.weblearnex.app.entity.setup.Role;
import com.weblearnex.app.model.ResponseBean;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

public interface RoleService {
    ResponseBean<Role> addRole(Role role);
    ResponseBean<Role> updateRole(Role role);
    ResponseBean<Role> deleteRole(Long roleId);
    ResponseBean<Role> getAllRole();
    ResponseBean<Role> findByRoleId(Long id);
    DataTablesOutput<Role> getAllRolePaginationAndSort(DataTablesInput input);
}
