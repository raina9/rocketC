package com.weblearnex.app.service;

import com.weblearnex.app.entity.setup.Coloader;
import com.weblearnex.app.model.ResponseBean;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.ResponseEntity;

public interface ColoaderService {

    ResponseBean<Coloader> addColoader(Coloader coloader);
    ResponseBean<Coloader> updateColoader(Coloader coloader);
    ResponseBean<Coloader> deleteColoader(Long coloaderId);
    ResponseBean<Coloader> getAllColoader();
    ResponseBean<Coloader> findByColoaderId(Long id);
    DataTablesOutput<Coloader> getAllColoaderPaginationAndSort(DataTablesInput input);
    ResponseEntity<Resource> coloaderReport();
}
