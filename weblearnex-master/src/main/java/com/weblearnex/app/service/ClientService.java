package com.weblearnex.app.service;

import com.weblearnex.app.entity.setup.Client;
import com.weblearnex.app.model.ResponseBean;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ClientService {
    /*ResponseBean<Client> addClient(Client client, MultipartFile aadharCard,MultipartFile panCard,MultipartFile gst,MultipartFile agreement);*/
    ResponseBean<Client> addClient(Client client);

    ResponseBean<Client> updateClient(Client client);
    ResponseBean<Client> deleteClient(Long id);
    ResponseBean<List<Client>> getAllClient();
    ResponseBean<List<Client>> findByActive();
    ResponseBean<Client> findById(Long id);
    ResponseBean<Client> findByName(String clientName);
    DataTablesOutput<Client> getAllClientPaginationAndSort(DataTablesInput input);
    ResponseEntity<Resource> clientReport();
    ResponseBean<List<Client>> getActiveWalletClient();





}
