package com.weblearnex.app.datatable.reposatory;

import com.weblearnex.app.entity.setup.Configration;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfigrationRepository  extends DataTablesRepository<Configration, Long> {

    Configration findByConfigType(String configType);
    Configration findByConfigValue(String configValue);
    Configration findByConfigCode(String configCode);
}
