package com.colourfulchina.mars.utils;

import com.colourfulchina.inf.base.encrypt.EncryptAES;
import com.colourfulchina.mars.api.config.MarsApiConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.security.SecureRandom;

@Slf4j
@Component
public class CheckSignUtils {

    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";//默认的加密算法


    private static String ENCRYPTKEY;
    @Value("${interface.encrypt.key}")
    public void setENCRYPTKEY(String ENCRYPTKEY){
        CheckSignUtils.ENCRYPTKEY = ENCRYPTKEY;
    }


    public CheckSignUtils() {
    }

    /**
     * 加密
     * @param content 待加密字段
     * @param password 加密密码
     * @return
     */
    public static String encrypt(String content, String password) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password));// 初始化为加密模式的密码器
            byte[] result = cipher.doFinal(byteContent);// 加密
            return Base64.encodeBase64String(result);//通过Base64转码返回
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static SecretKeySpec getSecretKey(final String password) {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = null;
        try{
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(password.getBytes());
            //AES 要求密钥长度为 128
            kg.init(128, secureRandom);
            //生成一个密钥
            SecretKey secretKey = kg.generateKey();
            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);// 转换为AES专用密钥

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Title: validateKey
     * @Description: 验证入参，解密
     * @author: sunny.wang
     * @date:  2019年5月15日 上午9:34:36
     * @param: @param param
     * @param: @return
     * @param: @throws Exception
     * @return: UnionLoginVo
     * @throws
     */
    public static Boolean validateKey(String param) throws Exception {
        if (param == null) {
            throw new Exception("参数为空");
        }
        if(param.indexOf("%")>-1) {
            param = URLDecoder.decode(param);
        }
        // 解密
        String encrypt = EncryptAES.decrypt(param, ENCRYPTKEY);
        if (encrypt == null) {
            throw new Exception("验签失败");
        }
        return true;
    }

}
