package com.weblearnex.app.datatable.reposatory;


import com.weblearnex.app.entity.setup.Role;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends DataTablesRepository<Role, Long> {
    Role findRoleByName(String name);
    List<Role> findByActive(Integer active);
}

