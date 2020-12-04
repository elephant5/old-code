package com.colourfulchina.inf.base.encrypt;

import org.apache.commons.lang.StringUtils;

import java.security.MessageDigest;

/**
 * MD5加密
 */
public class EncryptMD5 {

    /**
     * 获得字符串的 32位大写md5加密串
     * @param key
     * @return
     */
    public static String MD5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 获取字符串的 16位大写md5加密串
     * @param key
     * @return
     */
    public static String MD5_16(String key) {
        final String md5 = MD5(key);
        if (StringUtils.isBlank(md5)){
            return md5;
        }else {
            return md5.substring(8,24);
        }
    }

//    /**
//     * 获取字符串的 16位大写md5加密串
//     * @param key
//     * @return
//     */
//    public static String MD5_16(String key) {
//        final String md5 = MD5(key);
//        if (StringUtils.isBlank(md5)){
//            return md5;
//        }else {
//            return md5.substring(8,24);
//        }
//    }

//    public static void main(String [] args){
//        System.out.println(MD5("this is md test"));
//        System.out.println(MD5_16("this is md test"));
//    }
}
