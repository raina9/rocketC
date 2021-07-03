package com.weblearnex.app.service;

import com.weblearnex.app.entity.setup.Page;
import com.weblearnex.app.model.ResponseBean;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

public interface PageService {
    ResponseBean<Page> addPage(Page page);
    ResponseBean<Page> updatePage(Page page);
    ResponseBean<Page> deletePage(Long pageId);
    ResponseBean<Page> getAllPage();
    ResponseBean<Page> findByPageId(Long id);
    DataTablesOutput<Page> getAllPagePaginationAndSort(DataTablesInput input);
}
