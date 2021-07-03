package com.weblearnex.app.datatable.reposatory;

import com.weblearnex.app.constant.ApiConfigType;
import com.weblearnex.app.constant.EntityType;
import com.weblearnex.app.constant.RequestMethod;
import com.weblearnex.app.entity.master.ApiConfig;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApiConfigRepository extends DataTablesRepository<ApiConfig, Long> {
    ApiConfig findByClientId(Long clientId);
    ApiConfig findByCourierId(Long courierId);
    ApiConfig findByCourierIdAndActiveAndApiConfigTypeAndEntityType(Long courierId, Integer active, ApiConfigType apiConfigType, EntityType entityType);
    ApiConfig findByServiceProviderIdAndActiveAndApiConfigTypeAndEntityType(Long serviceProviderId, Integer active, ApiConfigType apiConfigType, EntityType entityType);
    ApiConfig findByCourierIdAndApiConfigTypeAndEntityType(Long courierId, ApiConfigType apiConfigType, EntityType entityType);
    ApiConfig findByClientIdAndApiConfigTypeAndEntityType(Long courierId, ApiConfigType apiConfigType, EntityType entityType);
    ApiConfig findByServiceProviderId(Long serviceProviderId);
    List<ApiConfig> findAllByActiveAndApiConfigTypeAndEntityType(Integer active, ApiConfigType apiConfigType, EntityType entityType);
}
