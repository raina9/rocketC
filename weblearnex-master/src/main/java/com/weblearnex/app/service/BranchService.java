package com.weblearnex.app.service;

import com.weblearnex.app.entity.master.BulkMaster;
import com.weblearnex.app.entity.setup.Branch;
import com.weblearnex.app.model.BulkUploadBean;
import com.weblearnex.app.model.ResponseBean;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BranchService extends BulkUploadService{
    ResponseBean<Branch> addBranch(Branch branch);
    ResponseBean<Branch> updateBranch(Branch branch);
    ResponseBean<Branch> deleteBranch(Long id);
    ResponseBean<Branch> getAllBranch();
    ResponseBean<List<Branch>> getAllActiveBranch();
    ResponseBean<Branch> findByBranchId(Long id);
    DataTablesOutput<Branch> getAllBranchPaginationAndSort(DataTablesInput input);
    ResponseEntity<Resource> branchReport();
    public BulkUploadBean branchBulkUpload(BulkUploadBean bulkUploadBean, BulkMaster bulkMaster, boolean isUpdate);
}
