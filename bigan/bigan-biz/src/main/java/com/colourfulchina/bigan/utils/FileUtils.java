package com.colourfulchina.bigan.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Slf4j
public class FileUtils {
    private FileUtils(){}

    /**
     * MultipartFile转File
     * @param multipartFile
     * @param filePath
     * @return
     * @throws Exception
     */
    public static File multiTransferToFile(MultipartFile multipartFile,String filePath)throws Exception{
        File file = null;
        try {
            File uploadDir = new File(filePath);
            if(!uploadDir.exists()){//判断目录是否存在
                uploadDir.mkdirs();
            }
            file = new File(uploadDir + File.separator + System.currentTimeMillis() + multipartFile.getOriginalFilename());//将mfile转化成file，以供后续处理
            //将上传的文件写入到新建的目录中
            multipartFile.transferTo(file);
        }catch (Exception e){
            log.error("获取输入流错误",e);
            throw new Exception(e);
        }
        return file;

    }
}
