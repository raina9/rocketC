package com.weblearnex.app.service;

import com.weblearnex.app.config.ProgressBean;
import com.weblearnex.app.constant.ProjectConstant;
import com.weblearnex.app.constant.ResponseStatus;
import com.weblearnex.app.entity.master.BulkHeader;
import com.weblearnex.app.entity.master.BulkMaster;
import com.weblearnex.app.entity.setup.User;
import com.weblearnex.app.model.BulkUploadBean;
import com.weblearnex.app.model.ResponseBean;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public interface BulkUploadService {


    BulkUploadBean uploadBulk(BulkUploadBean bulkUploadBean, BulkMaster bulkMaster);

    public static List<Map<String, String>> readExcel(MultipartFile file){
        try
        {
            /*Workbook workbook = StreamingReader.builder().rowCacheSize(100) // number of rows to keep in memory
                    .bufferSize(4096) // index of sheet to use (defaults to 0)
                    .open(file);*/
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(ProjectConstant.ZERO);
            Iterator < Row > rowIterator = sheet.iterator();

            /*Iterator<Row> rowIterator = WorkbookFactory.create(file.getInputStream()).getSheetAt(ProjectConstant.ZERO).rowIterator();
            // SXSSFRow*/

            List<Map<String, String>> listResult = new ArrayList<>();
            List<String> headers = new ArrayList<String>();
            int count = 1;
            int totalRowCount = 1;
            while (rowIterator.hasNext()) {
                Row row = (Row) rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();

                if(count == ProjectConstant.ONE){
                    while (cellIterator.hasNext())
                    {
                        Cell cell = cellIterator.next();
                        headers.add(cell.getStringCellValue().trim());
                    }
                    count++;
                }else {
                    System.out.println("Reads excel row number --> "+totalRowCount++);
                    Map<String, String> map = new HashMap<String, String>();
                    int cellCount = ProjectConstant.ZERO;
                    for(String header : headers){
                        Cell cell = row.getCell(cellCount);
                        map.put(headers.get(cellCount).trim(), getCellData(cell));
                        cellCount++;
                    }
                    /*while (cellIterator.hasNext())
                    {
                        Cell cell = cellIterator.next();
                        map.put(headers.get(cellCount).trim(), getCellData(cell).trim());
                        cellCount++;
                    }*/
                    listResult.add(map);
                }
            }

            /*Workbook workbook = WorkbookFactory.create(file.getInputStream());
            workbook.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK);
            for(int i = ProjectConstant.ZERO; i<workbook.getNumberOfSheets(); i++) {
                Iterator<Row> rowIterator = workbook.getSheetAt(i).rowIterator();
                List<String> headers = new ArrayList<String>();
                long count = ProjectConstant.ONE;
                while (rowIterator.hasNext()) {
                    Row row = (Row) rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();
                    if(count == ProjectConstant.ONE){
                        while (cellIterator.hasNext())
                        {
                            Cell cell = cellIterator.next();
                            headers.add(cell.getStringCellValue().trim());
                        }
                        count++;
                    }else {
                        Map<String, String> map = new HashMap<String, String>();
                        int cellCount = ProjectConstant.ZERO;
                        while (cellIterator.hasNext())
                        {
                            Cell cell = cellIterator.next();
                            map.put(headers.get(cellCount).trim(), getCellData(cell).trim());
                            cellCount++;
                        }
                        listResult.add(map);
                    }
                }
            }*/
            return listResult;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static  String getCellData(Cell cell){
        try {
            if(cell == null){
                return null;
            }
            /*switch (cell.getCellType())
            {
                case Cell.CELL_TYPE_NUMERIC:
                    return NumberToTextConverter.toText(cell.getNumericCellValue()).trim();
                    //return String.valueOf(cell.getNumericCellValue());
                case Cell.CELL_TYPE_STRING:
                    return String.valueOf(cell.getStringCellValue()).trim();
                case Cell.CELL_TYPE_BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue()).trim();
                default:
                    return null;
            }*/
            if(cell.getCellType() == CellType.STRING){
                return String.valueOf(cell.getStringCellValue()).trim();
            }else if(cell.getCellType() == CellType.NUMERIC){
                return NumberToTextConverter.toText(cell.getNumericCellValue()).trim();
            }else if(cell.getCellType() == CellType.BOOLEAN){
                return String.valueOf(cell.getBooleanCellValue()).trim();
            }else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeExcel(List<BulkHeader> bulkHeaders, List<Map<String, String>> lstRecords, String fileName, String filePath) throws IOException {
        FileOutputStream file = new FileOutputStream(new File(filePath+"/"+fileName));
        SXSSFWorkbook xlsWorkbook = new SXSSFWorkbook();
        SXSSFSheet xlsSheet = (SXSSFSheet) xlsWorkbook.createSheet(fileName);
        int rowIndex = ProjectConstant.ZERO;
        SXSSFRow titleRow = (SXSSFRow) xlsSheet.createRow(rowIndex++);
        for (int i = ProjectConstant.ZERO; i < bulkHeaders.size(); i++) {
            titleRow.createCell(i).setCellValue(bulkHeaders.get(i).getDisplayName());
        }
        if(lstRecords != null && lstRecords.size() > ProjectConstant.ZERO){
            for (Map<String, String> map : lstRecords) {
                SXSSFRow dataRow = (SXSSFRow)xlsSheet.createRow(rowIndex++);
                for (int i = ProjectConstant.ZERO; i < bulkHeaders.size(); i++) {
                    if(map.get(bulkHeaders.get(i).getHeaderCode())==null){
                        dataRow.createCell(i).setCellValue(ProjectConstant.NULL_STRING);
                        continue;
                    }
                    dataRow.createCell(i).setCellValue(map.get(bulkHeaders.get(i).getHeaderCode()).toString());
                }
            }
        }
        xlsWorkbook.write(file);
    }

    public static byte[]  writeExcel(List<BulkHeader> bulkHeaders, List<Map<String, String>> lstRecords) throws IOException {
        SXSSFWorkbook xlsWorkbook = new SXSSFWorkbook();
        SXSSFSheet xlsSheet = (SXSSFSheet) xlsWorkbook.createSheet();
        int rowIndex = ProjectConstant.ZERO;
        SXSSFRow titleRow = (SXSSFRow) xlsSheet.createRow(rowIndex++);
        for (int i = ProjectConstant.ZERO; i < bulkHeaders.size(); i++) {
            titleRow.createCell(i).setCellValue(bulkHeaders.get(i).getDisplayName());
        }
        if(lstRecords != null && lstRecords.size() > ProjectConstant.ZERO){
            for (Map<String, String> map : lstRecords) {
                SXSSFRow dataRow = (SXSSFRow)xlsSheet.createRow(rowIndex++);
                for (int i = ProjectConstant.ZERO; i < bulkHeaders.size(); i++) {
                    if(map.get(bulkHeaders.get(i).getHeaderCode())==null){
                        dataRow.createCell(i).setCellValue(ProjectConstant.NULL_STRING);
                        continue;
                    }
                    dataRow.createCell(i).setCellValue(map.get(bulkHeaders.get(i).getHeaderCode()).toString());
                }
            }
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            xlsWorkbook.write(bos);
        } finally {
            bos.close();
        }
        return bos.toByteArray();
    }

    public static void createBulkTemplate(BulkMaster bulkMaster, List<BulkHeader> bulkHeaders, String fileName, String filePath) {
        try{
            Map<String, BulkHeader> bulkHeaderMap = new HashMap<String, BulkHeader>();
            bulkHeaders.forEach(bulkHeader -> bulkHeaderMap.put(bulkHeader.getHeaderCode(), bulkHeader));
            SXSSFWorkbook xlsWorkbook = new SXSSFWorkbook();
            SXSSFSheet xlsSheet = (SXSSFSheet) xlsWorkbook.createSheet(fileName);
            int rowIndex = ProjectConstant.ZERO;
            SXSSFRow titleRow = (SXSSFRow) xlsSheet.createRow(rowIndex++);
            List<String> stringList = new ArrayList<String>(Arrays.asList(bulkMaster.getBulkHeaderSequenceIds().split(",")));
            int i = 0;
            for (String s : stringList) {
                titleRow.createCell(i).setCellValue(bulkHeaderMap.get(s).getDisplayName());
                i++;
            }
            String fullPath = filePath+"/"+fileName+ProjectConstant.EXTENSION_XLS;
            FileOutputStream outputStream =new FileOutputStream(fullPath);
            xlsWorkbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        }catch(Exception e){e.printStackTrace();}
    }

    public static byte[] downloadExcelFile( String filePath, String fileName, HttpServletRequest request,HttpServletResponse response) {
        try {
            String path = filePath+"/"+fileName+ProjectConstant.EXTENSION_XLS;
            File file = new File(path);
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

    public static byte[] downloadExcelFile(String fullFilePath) {
        try {
            File file = new File(fullFilePath);
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
    public static void setUploadProgressCount(RedisService redisService, String key, ProgressBean progressBean){
        try {
            redisService.delete(key);
            if(progressBean.getUploadCompleted() != null && progressBean.getUploadCompleted()){
                redisService.save(key, progressBean, 6l); // key valide only for 5 second
            }else {
                redisService.save(key, progressBean);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static ResponseBean getUploadProgressCount(RedisService redisService, String key,String bulkUploadMaster){
        ResponseBean responseBean = new ResponseBean();
        try {
            ProgressBean progressBean =  redisService.getProgressBean(key);
            responseBean.setStatus(ResponseStatus.SUCCESS);
            responseBean.setResponseBody(progressBean);
        }catch (Exception e){
            responseBean.setStatus(ResponseStatus.FAIL);
            responseBean.setMessage("Server Internal Error.");
            e.printStackTrace();
        }
        return responseBean;
    }
    public static int calculateUploadPercentage(int progressCount, int totalCount){
        if(progressCount == 0){
            return 0;
        }
        return (progressCount*100) / totalCount;
    }
    public static String generateRandomString() {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }
}
