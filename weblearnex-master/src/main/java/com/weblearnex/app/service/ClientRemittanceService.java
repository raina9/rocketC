package com.weblearnex.app.service;

import com.weblearnex.app.entity.master.Country;
import com.weblearnex.app.entity.master.CourierStatusMapping;
import com.weblearnex.app.entity.remittance.ClientRemittance;
import com.weblearnex.app.model.ResponseBean;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public interface ClientRemittanceService extends BulkUploadService{
    DataTablesOutput<ClientRemittance> getAllClientGeneratedRemittance(DataTablesInput input);
    DataTablesOutput<ClientRemittance> getAllClientClosedRemittance(DataTablesInput input);
    ResponseBean<ClientRemittance> findById(Long id);
    ResponseEntity<Resource> getAllClientAwbNumber(String clientCode);
    ResponseBean closedClientRemittance(MultipartFile file,String bankName,String accountNo,String transactionNo,String depositeDate,String remittanceNo,Double depositedAmt);
    ResponseBean<ClientRemittance> deleteClientRemittance(Long id);

    ResponseEntity<Resource> clientRemittanceReport(String remittanceNo);
    //ResponseEntity<Resource> downloadDepositeSlipFile(String remittanceNo);
    public static byte[] downloadDepositeSlipFile(String remittancefullFilePath) {
        try {
            File file = new File(remittancefullFilePath);
            //File file=folder.listFiles()[0];
            FileInputStream fileInputStream = new FileInputStream(file);
            long byteLength = file.length();
            byte[] byteArr = new byte[(int) byteLength];
            fileInputStream.read(byteArr, 0, (int) byteLength);
            return byteArr;
        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
