package com.weblearnex.app.service;

import com.weblearnex.app.entity.master.CourierStatusMapping;
import com.weblearnex.app.model.ResponseBean;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

public interface CourierStatusMappingService extends BulkUploadService{

    ResponseBean<CourierStatusMapping> addCourierStatusMapping(CourierStatusMapping courierStatusMapping);
    ResponseBean<CourierStatusMapping> updateCourierStatusMapping(CourierStatusMapping courierStatusMapping);
    ResponseBean<CourierStatusMapping> deleteCourierStatusMapping(Long CourierStatusMappingId);
    ResponseBean<CourierStatusMapping> getAllCourierStatusMapping();
    ResponseBean<CourierStatusMapping> findByCourierStatusMappingId(Long id);
    DataTablesOutput<CourierStatusMapping> getAllCourierStatusMappingPaginationAndSort(DataTablesInput input);
}

