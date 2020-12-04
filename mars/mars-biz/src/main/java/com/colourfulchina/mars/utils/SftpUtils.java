package com.colourfulchina.mars.utils;

import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * 解释一下SFTP的整个调用过程，这个过程就是通过Ip、Port、Username、Password获取一个Session,
 * 然后通过Session打开SFTP通道（获得SFTP Channel对象）,再在建立通道（Channel）连接，最后我们就是
 * 通过这个Channel对象来调用SFTP的各种操作方法.总是要记得，我们操作完SFTP需要手动断开Channel连接与Session连接。
 *
 * @author jiashubing
 * @since 2018/5/8
 */
@Slf4j
public class SftpUtils {

    private ChannelSftp channel;
    private Session session;
    private String sftpPath;

    public SftpUtils(String ftpHost, int ftpPort, String ftpUserName, String priKeyPath, String sftpPath) {
        this.connectServer(ftpHost, ftpPort, ftpUserName, priKeyPath, sftpPath);
    }

    public void connectServer(String ftpHost, int ftpPort, String ftpUserName, String priKeyPath, String sftpPath) {
        try {
            this.sftpPath = sftpPath;

            // 创建JSch对象
            JSch jsch = new JSch();
			jsch.addIdentity(priKeyPath);
            // 根据用户名，主机ip，端口获取一个Session对象
            session = jsch.getSession(ftpUserName, ftpHost, ftpPort);
            //if (ftpPassword != null) {
                // 设置密码
                //session.setPassword(ftpPassword);
            //}
            Properties configTemp = new Properties();
            configTemp.put("StrictHostKeyChecking", "no");
            // 为Session对象设置properties
            session.setConfig(configTemp);
            // 设置timeout时间
            session.setTimeout(60000);
            session.connect();
            // 通过Session建立链接
            // 打开SFTP通道
            channel = (ChannelSftp) session.openChannel("sftp");
            // 建立SFTP通道的连接
            channel.connect();
        } catch (JSchException e) {
            throw new RuntimeException(e);

        }
    }

    /**
     * 断开SFTP Channel、Session连接
     */
    public void closeChannel() {
        try {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        } catch (Exception e) {
            //
        }
    }

    /**
     * 上传文件
     *
     * @param localFile  本地文件
     * @param remoteFile 远程文件
     */
    public boolean upload(String localFile, String remoteFile) {
        boolean isSuccess = true;
        MyProgressMonitor myProgressMonitor = new MyProgressMonitor();
        String oldRemoteFile = sftpPath + remoteFile + ".tmp";
        try {
            log.info("-----------oldRemoteFile---------------{}", JSONObject.toJSON(oldRemoteFile));
            log.info("-----------channel---------------{}", JSONObject.toJSON(channel));
            channel.put(localFile, oldRemoteFile, myProgressMonitor, ChannelSftp.OVERWRITE);

            while(!myProgressMonitor.isFinished){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            channel.rename(oldRemoteFile,sftpPath + remoteFile);
        } catch (SftpException e) {
            //传输异常，则删除临时文件
//            try {
//                channel.rm(oldRemoteFile);
//            } catch (SftpException e1) {
//                e1.printStackTrace();
//            }
            isSuccess = false;
            e.printStackTrace();
        }
//        finally {
//			channel.quit();
//		}
        return isSuccess;
    }

    /**
     * 批量上传文件
     * @param remotePath：远程保存目录
     * @param localPath：本地上传目录(以路径符号结束)
     * @param del：上传后是否删除本地文件
     * @return
     */
    public boolean bacthUploadFile(String remotePath, String localPath,
                                   boolean del)
    {
        try
        {
            File file = new File(localPath);
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++)
            {
                if (files[i].isFile()
                        && files[i].getName().indexOf("bak") == -1)
                {
                    if (this.uploadFile(remotePath, files[i].getName(),
                            localPath)
                            && del)
                    {
                    }
                }
            }

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;

    }

    public boolean uploadFile(String remotePath, String remoteFileName,String localPath)
    {
        FileInputStream in = null;
        try
        {
            File file = new File(localPath );
            in = new FileInputStream(file);
            channel.put(in, remoteFileName);
            return true;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (SftpException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 下载文件
     *
     * @param remoteFile 远程文件
     * @param localFile  本地文件
     */
    public void download(String remoteFile, String localFile) {
        try {
            remoteFile = sftpPath + remoteFile;
            channel.get(remoteFile, localFile);
            channel.quit();
        } catch (SftpException e) {
            //e.printStackTrace();
        }
    }

    private class MyProgressMonitor implements SftpProgressMonitor {
        private boolean isFinished = false;
        private long transfered;

        @Override
        public boolean count(long count) {
            transfered = transfered + count;
            System.out.println("Currently transferred total size: " + transfered + " bytes");
            return true;
        }

        @Override
        public void end() {
            isFinished = true;
            System.out.println("Transferring done.");
        }

        @Override
        public void init(int op, String src, String dest, long max) {
            System.out.println("Transferring begin.");
        }
    }

//    public static void main(String[] args) {
//        SftpUtils sftpCustom = new SftpUtils("172.16.22.74", 22, "root", "niwodai88","/home/sftp/");
//        //上传测试
//        String localfile = "E:/temp/pgp/WARES.KLFG.2019-11-22.00.P.ZIP.DAT";
//        String remotefile = "WARES.KLFG.2019-11-22.00.P.ZIP.DAT";
//        sftpCustom.upload(localfile, remotefile);
//
//        //下载测试
////        sftpCustom.download(remotefile, "E:/lalala/tt3.xlsx");
//
//        sftpCustom.closeChannel();
//    }

}