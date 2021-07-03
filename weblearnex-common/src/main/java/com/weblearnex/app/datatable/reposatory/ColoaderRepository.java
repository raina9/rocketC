package com.weblearnex.app.datatable.reposatory;

import com.weblearnex.app.entity.setup.Coloader;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColoaderRepository extends DataTablesRepository<Coloader, Long> {

    Coloader findByColoaderName(String coloaderName);
    Coloader findByColoaderCode(String coloaderCode);
    Coloader findByRegisteredAdd(String registeredAdd);
    Coloader findByPincode(String pinCode);
    Coloader findByContactPerson(String contactPerson);
    Coloader findByMobile(String mobile);
    Coloader findByEmail(String email);




}
