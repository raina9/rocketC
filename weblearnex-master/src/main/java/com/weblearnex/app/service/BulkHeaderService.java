package com.weblearnex.app.service;

import com.weblearnex.app.entity.master.BulkHeader;
import com.weblearnex.app.model.DataTableResult;
import com.weblearnex.app.model.ResponseBean;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;


public interface BulkHeaderService {
    ResponseBean<BulkHeader> addBulkHeader(BulkHeader bulkHeader);
    ResponseBean<BulkHeader> updateBulkHeader(BulkHeader bulkHeader);
    ResponseBean<BulkHeader> deleteBulkHeader(Long bulkHeaderId);
    ResponseBean<BulkHeader> getAllBulkHeader();
    DataTablesOutput<BulkHeader> getAllBulkHeader(DataTablesInput input);
    ResponseBean<BulkHeader> findByIdBulkHeader(Long id);
    DataTableResult getAll(Integer pageNo, Integer pageSize, Integer draw, String sortBy);

}
