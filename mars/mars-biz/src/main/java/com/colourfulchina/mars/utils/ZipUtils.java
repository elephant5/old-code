package com.colourfulchina.mars.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

@Slf4j
public class ZipUtils {

    /***
     * 压缩文件变成zip输出流
     */
    public static byte[] createZip(String srcSource) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        //将目标文件打包成zip导出
        File file = new File(srcSource);
        createAllFile(zip, file, "");
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    /***
     * 对文件下的文件处理
     */
    public static void createAllFile(ZipOutputStream zip, File file, String dir) throws Exception {
        //如果当前的是文件夹，则进行进一步处理
        if (file.isDirectory()) {
            //得到文件列表信息
            File[] files = file.listFiles();
            //将文件夹添加到下一级打包目录
//            zip.putNextEntry(new ZipEntry(dir + "/"));
            dir = dir.length() == 0 ? "" : dir + "/";
            //循环将文件夹中的文件打包
            for (int i = 0; i < files.length; i++) {
                createAllFile(zip, files[i], dir + files[i].getName());         //递归处理
            }
        } else {   //当前的是文件，打包处理
            //文件输入流
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(file.getName());
            zip.putNextEntry(entry);
            zip.write(FileUtils.readFileToByteArray(file));
            IOUtils.closeQuietly(bis);
            zip.flush();
            zip.closeEntry();
        }
    }

    /**
     * 将存放在sourceFilePath目录下的源文件，打包成fileName名称的zip文件，并存放到zipFilePath路径下
     *
     * @param sourceFilePath :待压缩的文件路径
     * @param zipFilePath    :压缩后存放路径
     * @return
     */
    public static boolean fileToZip(String sourceFilePath, String zipFilePath) {
        boolean flag = false;
        File sourceFile = new File(sourceFilePath);
        FileOutputStream fos = null;
        ZipOutputStream zos = null;

        try {
            File zipFile = new File(zipFilePath);
            if (zipFile.exists()) {
                log.info(zipFilePath + "存在");
                if(!zipFile.delete()){
                    throw new RuntimeException("压缩文件删除不了");
                }
            }

            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(new BufferedOutputStream(fos), Charset.forName("GBK"));
            createAllFile(zos, sourceFile, sourceFilePath);

//            byte[] bufs = new byte[1024 * 10];
//
//            // 创建ZIP实体，并添加进压缩包
//            ZipEntry zipEntry = new ZipEntry(sourceFile.getName());
//            zos.putNextEntry(zipEntry);
//            // 读取待压缩的文件并写进压缩包里
//            fis = new FileInputStream(sourceFile);
//            bis = new BufferedInputStream(fis, 1024 * 10);
//            int read = 0;
//            while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
//                zos.write(bufs, 0, read);
//            }
            flag = true;

        }  catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            try {
                if (null != zos)
                    zos.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return flag;
    }

    /**
     * 解压缩zip包
     *
     * @param zipFilePath        需要解压的zip文件的全路径
     * @param unzipFilePath      解压后的文件保存的路径
     * @param includeZipFileName 解压后的文件保存的路径是否包含压缩文件的文件名。true-包含；false-不包含
     */
    @SuppressWarnings("unchecked")
    public static void unzip(String zipFilePath, String unzipFilePath, boolean includeZipFileName) throws Exception {
        if (StringUtils.isNotBlank(zipFilePath) || StringUtils.isNotBlank(unzipFilePath)) {
            File zipFile = new File(zipFilePath);
            // 如果解压后的文件保存路径包含压缩文件的文件名，则追加该文件名到解压路径
            if (includeZipFileName) {
                String fileName = zipFile.getName();
                if (StringUtils.isNotEmpty(fileName)) {
                    fileName = fileName.substring(0, fileName.lastIndexOf("."));
                }
                unzipFilePath = unzipFilePath + File.separator + fileName;
            }
            // 创建解压缩文件保存的路径
            File unzipFileDir = new File(unzipFilePath);
            if (!unzipFileDir.exists() || !unzipFileDir.isDirectory()) {
                unzipFileDir.mkdirs();
            }

            // 开始解压
            ZipEntry entry = null;
            String entryFilePath = null, entryDirPath = null;
            File entryFile = null, entryDir = null;
            int index = 0, count = 0, bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            ZipFile zip = new ZipFile(zipFile);
            Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();
            // 循环对压缩包里的每一个文件进行解压
            while (entries.hasMoreElements()) {
                entry = entries.nextElement();
                // 构建压缩包中一个文件解压后保存的文件全路径
                entryFilePath = unzipFilePath + File.separator + entry.getName();
                // 构建解压后保存的文件夹路径
                index = entryFilePath.lastIndexOf(File.separator);
                if (index != -1) {
                    entryDirPath = entryFilePath.substring(0, index);
                } else {
                    entryDirPath = "";
                }
                entryDir = new File(entryDirPath);
                // 如果文件夹路径不存在，则创建文件夹
                if (!entryDir.exists() || !entryDir.isDirectory()) {
                    entryDir.mkdirs();
                }

                // 创建解压文件
                entryFile = new File(entryFilePath);
                if (entryFile.exists()) {
                    // 检测文件是否允许删除，如果不允许删除，将会抛出SecurityException
                    SecurityManager securityManager = new SecurityManager();
                    securityManager.checkDelete(entryFilePath);
                    // 删除已存在的目标文件
                    entryFile.delete();
                }

                // 写入文件
                bos = new BufferedOutputStream(new FileOutputStream(entryFile));
                bis = new BufferedInputStream(zip.getInputStream(entry));
                while ((count = bis.read(buffer, 0, bufferSize)) != -1) {
                    bos.write(buffer, 0, count);
                }
                bos.flush();
                bos.close();
            }
            zip.close();// 切记一定要关闭掉，不然在同一个线程，你想解压到临时路径之后，再去删除掉这些临时数据，那么就删除不了
        }

    }

    public static void fixFileName(File file, String prefix, String targetName){
        if(file.isDirectory()){
            File[] files = file.listFiles();

            for(File f: files){
                fixFileName(f, prefix, targetName);
            }
        }else{
            String name = file.getName();
            if(name.startsWith(prefix)){
                String parent = file.getParent();
                file.renameTo(new File(parent+File.separator+targetName));
            }

        }
    }

}
