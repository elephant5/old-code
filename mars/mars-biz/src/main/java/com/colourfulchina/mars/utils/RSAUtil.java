package com.colourfulchina.mars.utils;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * com.jiayincredit.dataplatform.utils
 * Created by zhangxing on 2018/9/4.
 */
public class RSAUtil {

    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final String RSA_ALGORITHM = "RSA";

    public static Map<String, String> createKeys(int keySize) {
        //为RSA算法创建一个KeyPairGenerator对象
        KeyPairGenerator kpg;
        try {
            kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No such algorithm-->[" + RSA_ALGORITHM + "]");
        }

        //初始化KeyPairGenerator对象,密钥长度
        kpg.initialize(keySize);
        //生成密匙对
        KeyPair keyPair = kpg.generateKeyPair();
        //得到公钥
        Key publicKey = keyPair.getPublic();
        String publicKeyStr = Base64.encodeBase64URLSafeString(publicKey.getEncoded());
        //得到私钥
        Key privateKey = keyPair.getPrivate();
        String privateKeyStr = Base64.encodeBase64URLSafeString(privateKey.getEncoded());
        Map<String, String> keyPairMap = new HashMap<String, String>();
        keyPairMap.put("publicKey", publicKeyStr);
        keyPairMap.put("privateKey", privateKeyStr);

        return keyPairMap;
    }

    //读取公私钥⽂文件
    public static String readKeyFile(String keyFile) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(keyFile)));
        String readLine = null;
        StringBuilder sb = new StringBuilder();
        while ((readLine = br.readLine()) != null) {
            if (readLine.charAt(0) != '-') {
                sb.append(readLine);
                sb.append(System.getProperty("line.separator"));
            }
        }
        return sb.toString();
    }

    public static String readKeyStr(String keyStr) throws Exception {
        String[] lines = keyStr.trim().split("\n");
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            String readLine = line.trim();
            if (readLine.charAt(0) != '-') {
                sb.append(readLine);
                sb.append(System.getProperty("line.separator"));
            }
        }
        return sb.toString();
    }

    //构造私钥对象
    public static RSAPrivateKey loadPrivateKey(String privateKey) throws Exception {
        // 解密由base64编码的私钥
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        // 构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        // 取私钥匙对象
        return (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
    }

    //构造公钥对象
    public static RSAPublicKey loadPublicKey(String publicKey) throws Exception {
        byte[] buffer = Base64.decodeBase64(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    /**
     * 公钥加密
     *
     * @param publicKeyFilePath 私钥地址
     * @param content
     * @return
     * @throws Exception
     */
    public static String publicEncrypt(String publicKeyFilePath, String content) throws Exception {
        return publicEncrypt(loadPublicKey(readKeyFile(publicKeyFilePath)), content);
    }

    /**
     * 私钥解密
     *
     * @param privateKeyFilePath 私钥地址
     * @param content
     * @return
     * @throws Exception
     */
    public static String privateDecrypt(String privateKeyFilePath, String content) throws Exception {
        return privateDecrypt(loadPrivateKey(readKeyFile(privateKeyFilePath)), content);
    }

    //公钥加密
    public static String publicEncrypt(RSAPublicKey publicKey, String content) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.encodeBase64URLSafeString(cipher.doFinal(content.getBytes(DEFAULT_CHARSET)));
    }

    //私钥解密
    public static String privateDecrypt(RSAPrivateKey privateKey, String content) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(Base64.decodeBase64(content)), DEFAULT_CHARSET);
    }

    public static byte[] hexStringToByteArray(String hexString) {
        hexString = hexString.replaceAll(" ", "");
        int len = hexString.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个字节
            bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character
                    .digit(hexString.charAt(i + 1), 16));
        }
        return bytes;
    }

    public static RSAPublicKey getPublicKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus, 16); //此处为进制数
            BigInteger b2 = new BigInteger(exponent, 16);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encodeForString(String data, String key) throws Exception {
        String outStr = null;
        byte[] cer;
        try {
            String pub = key;
            cer = Hex.decode(pub);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(cer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pk = keyFactory.generatePublic(keySpec);
            System.out.println("pk" + pk);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pk);
            byte[] enc = cipher.doFinal(data.getBytes("UTF-8"));
            outStr = new String(Hex.encode(enc), "ISO-8859-1").toUpperCase();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return outStr;
    }
    private static final int MAX_ENCRYPT_BLOCK = 117;
    /**
     * RSA加密
     *
     * @param data 待加密数据
     * @param publicKey 公钥
     * @return
     */
    public static String encrypt(String data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int inputLen = data.getBytes().length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        // 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
        // 加密后的字符串
        return new String(Base64.encodeBase64String(encryptedData));
    }
}



