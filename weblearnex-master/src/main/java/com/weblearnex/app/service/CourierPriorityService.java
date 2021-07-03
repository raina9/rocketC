package com.weblearnex.app.service;

import com.weblearnex.app.entity.master.CourierPriority;
import com.weblearnex.app.model.ResponseBean;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

public interface CourierPriorityService {
    ResponseBean<CourierPriority> addCourierPriority(CourierPriority courierPriority);
    ResponseBean<CourierPriority> updateCourierPriority(CourierPriority courierPriority);
    ResponseBean<CourierPriority> deleteCourierPriority(Long courierPriorityId);
    ResponseBean<CourierPriority> getAllCourierPriority();
    ResponseBean<CourierPriority> findByCourierPriorityId(Long id);

    DataTablesOutput<CourierPriority> getAllCourierPriorityPaginationAndSort(DataTablesInput input);
}
