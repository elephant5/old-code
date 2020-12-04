package com.colourfulchina.bigan.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class ExcelUtils {
    private ExcelUtils(){

    }

    /**
     * 判断是否为2007及以上版本Excel
     * @param filePath
     * @return
     */
    public static boolean isExcel2007(String filePath){
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }
    public static Workbook getWorkbook(MultipartFile multipartFile,String filePath){
        Workbook wb=null;
        InputStream inputStream = null;
        try {
            final File file = FileUtils.multiTransferToFile(multipartFile, filePath);
            inputStream=new FileInputStream(file);
            if (isExcel2007(file.getName())){
                wb=new XSSFWorkbook(inputStream);
            }else {
                wb=new HSSFWorkbook(inputStream);
            }
        }catch (Exception e){
            log.error("获取工作薄失败",e);
        }finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                }
            }
        }

        return wb;
    }
}
