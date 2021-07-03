package com.weblearnex.app.service;

import com.weblearnex.app.entity.manifest.ThreeplManifest;
import com.weblearnex.app.entity.order.SaleOrder;
import com.weblearnex.app.model.ResponseBean;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

public interface ThreeplManifestService {
    ResponseBean<ThreeplManifest> addThreeplManifest(ThreeplManifest threeplManifest);
    DataTablesOutput<ThreeplManifest> getAllThreeplManifest(DataTablesInput input);
    ResponseBean<ThreeplManifest> findByAwbNumber(String awbNumbers,String courierCode);
    ResponseBean<List<ThreeplManifest>> getAllThreeplManifest();
    ResponseBean<ThreeplManifest> findByManifestId(Long id);
    ResponseBean<Map<String, Object>> getManifestAndSaleOrders(Long id);
    ResponseBean<ThreeplManifest> updateManifest(ThreeplManifest threeplManifest);

    ResponseBean uploadPodFile(MultipartFile file,String manifestId);
    DataTablesOutput<ThreeplManifest> getAllPodUploadedManifest(DataTablesInput input);


    public static byte[] downloadPodFile(String fullFilePath) {
        try {
            File file = new File(fullFilePath);
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
