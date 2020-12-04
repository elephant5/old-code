package com.colourfulchina.pangu.taishang.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.util.Map;

@Slf4j
public class FtpClientUtils {
    private FtpClientUtils(){}

    /**
     * ftp上传文件(目录不存在时 会自动创建目录)
     * @param host
     * @param port
     * @param username
     * @param password
     * @param path
     * @param fileName
     * @param file
     */
    public static void upload(String host, int port, String username, String password, String path,String fileName, File file) throws Exception{
        FtpClient ftpClient = null;
        try {
            if (file == null){
                log.error("文件参数为空");
                throw new InvalidParameterException("文件参数为空");
            }
            ftpClient = FtpClient.createFtpCli(host, port, username, password);
            if (!ftpClient.isConnected()){
                ftpClient.connect();
            }
            if (!ftpClient.isConnected()){
                log.error("ftp连接失败");
                throw new Exception("ftp连接失败");
            }
            ftpClient.makeDirs(path);
            ftpClient.upload(path+"/"+fileName,file);
        }catch (Exception e){
            log.error("ftp上传失败",e);
            throw new Exception(e);
        }finally {
            if (ftpClient != null){
                ftpClient.disconnect();
            }
        }
    }

    /**
     * ftp上传文件流(目录不存在时 会自动创建目录)
     * @param host
     * @param port
     * @param username
     * @param password
     * @param path
     * @param fileName
     * @param inputStream
     * @throws Exception
     */
    public static void upload(String host, int port, String username, String password, String path,String fileName, InputStream inputStream) throws Exception{
        FtpClient ftpClient = null;
        try {
            if (inputStream == null){
                log.error("文件流为空");
                throw new InvalidParameterException("文件流为空");
            }
            ftpClient = FtpClient.createFtpCli(host, port, username, password);
            if (!ftpClient.isConnected()){
                ftpClient.connect();
            }
            if (!ftpClient.isConnected()){
                log.error("ftp连接失败");
                throw new Exception("ftp连接失败");
            }
            ftpClient.makeDirs(path);
            ftpClient.upload(path+"/"+fileName,inputStream);
        }catch (Exception e){
            log.error("ftp上传失败",e);
            throw new Exception(e);
        }finally {
            if (ftpClient != null){
                ftpClient.disconnect();
            }
        }
    }
    public static void batchUpload(String host, int port, String username, String password, String path,Map<String, MultipartFile> fileInfo) throws Exception{
        FtpClient ftpClient = null;
        try {
            if (CollectionUtils.isEmpty(fileInfo)){
                log.error("文件流为空");
                throw new InvalidParameterException("文件流为空");
            }
            ftpClient = FtpClient.createFtpCli(host, port, username, password);
            if (!ftpClient.isConnected()){
                ftpClient.connect();
            }
            if (!ftpClient.isConnected()){
                log.error("ftp连接失败");
                throw new Exception("ftp连接失败");
            }
            ftpClient.makeDirs(path);
            for (Map.Entry<String,MultipartFile> entry:fileInfo.entrySet()){
                String fileName = entry.getValue().getOriginalFilename();
                ftpClient.upload(entry.getKey()+"."+fileName.substring(fileName.lastIndexOf(".")+1),entry.getValue().getInputStream());
            }
        }catch (Exception e){
            log.error("ftp上传失败",e);
            throw new Exception(e);
        }finally {
            if (ftpClient != null){
                ftpClient.disconnect();
            }
        }
    }
}
