package com.colourfulchina.pangu.taishang.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
public class ToInterfaceUtils {
    /**
     * 接口调用GET
     * @param path
     */
    public static String httpURLConectionGET(String path) {
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            connection.disconnect();
            return sb.toString();
        } catch (Exception e) {
            log.info("{}接口调用失败(GET)！",path);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 接口调用POST
     * @param path
     * @param param:json字符串格式参数
     */
    public static String httpURLConnectionPOST (String path,String param) {
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            connection.connect();
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(param);
            out.flush();
            out.close();
            BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = bf.readLine()) != null) {
                sb.append(line).append(System.getProperty("line.separator"));
            }
            bf.close();
            connection.disconnect();
            return sb.toString();
        } catch (Exception e) {
            log.info("{}接口调用失败(POST)！",path);
            e.printStackTrace();
        }
        return null;
    }


}
