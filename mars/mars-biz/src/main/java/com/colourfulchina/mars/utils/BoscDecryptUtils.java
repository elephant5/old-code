package com.colourfulchina.mars.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

@Component
@Data
public class BoscDecryptUtils {

    private static String KEY_ALGORITHM;
    @Value("${bosc.key_algorithm}")
    public void setKeyAlgorithm(String KEY_ALGORITHM){
        BoscDecryptUtils.KEY_ALGORITHM = KEY_ALGORITHM;
    }
    private static String DEFAULT_CIPHER_ALGORITHM;
    @Value("${bosc.default_cipher_algorithm}")
    public void setDefaultCipherAlgorithm(String DEFAULT_CIPHER_ALGORITHM){
        BoscDecryptUtils.DEFAULT_CIPHER_ALGORITHM = DEFAULT_CIPHER_ALGORITHM;
    }

    private static String IV;
    @Value("${bosc.iv}")
    public void setIV(String IV){
        BoscDecryptUtils.IV = IV;
    }

    private static String AES_KEY;
    @Value("${bosc.aes_key}")
    public void setAesKey(String AES_KEY){
        BoscDecryptUtils.AES_KEY = AES_KEY;
    }
    //验卡接口地址
    @Value("${bosc.checkCardUrl}")
    private String checkCardUrl;
    @Value("${bosc.validcard}")
    private boolean validcard=false;
    /**
     * BASE64解密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] strToBase64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    /**
     * 解密
     *
     * @param data
     *            待解密数据
     * @param key
     *            二进制密钥
     * @return byte[] 解密数据
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        return decrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
    }

    /**
     * 解密
     *
     * @param data
     *            待解密数据
     * @param key
     *            二进制密钥
     * @param cipherAlgorithm
     *            加密算法/工作模式/填充方式
     * @return byte[] 解密数据
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, byte[] key, String cipherAlgorithm)
            throws Exception {
        // 还原密钥
        Key k = toKey(key);
        return decrypt(data, k, cipherAlgorithm);
    }

    /**
     * 转换密钥
     *
     * @param key
     *            二进制密钥
     * @return 密钥
     */
    public static Key toKey(byte[] key) {
        // 生成密钥
        return new SecretKeySpec(key, KEY_ALGORITHM);
    }

    /**
     * 解密
     *
     * @param data
     *            待解密数据
     * @param key
     *            密钥
     * @param cipherAlgorithm
     *            加密算法/工作模式/填充方式
     * @return byte[] 解密数据
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, Key key, String cipherAlgorithm)
            throws Exception {
        // 实例化
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        // 使用密钥初始化，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, key,
                new IvParameterSpec(IV.getBytes("UTF-8")));
        // 执行操作
        return cipher.doFinal(data);
    }

    /**
     * 对客编号第一次解密
     * @param ecifNo 客编号
     * @return
     * @throws Exception
     */
    public static String decryEcifNo(String ecifNo) throws Exception {
        String ecifNoStr = new String(BoscDecryptUtils.decrypt(BoscDecryptUtils.strToBase64(ecifNo), AES_KEY.getBytes()));
        return JSONObject.parseObject(ecifNoStr).getString("ecifNo");
    }

}
