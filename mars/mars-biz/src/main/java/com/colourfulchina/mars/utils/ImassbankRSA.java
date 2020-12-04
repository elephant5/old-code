package com.colourfulchina.mars.utils;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

/**
 * 1公钥加密
 * 2私钥解密
 * *****************
 * 3私钥加签
 * 4公钥验签
 */
public class ImassbankRSA {

    /** 指定key的大小  注意KEYSIZE跟MAX_ENCRYPT_BLOCK=KEYSIZE/8-11,MAX_DECRYPT_BLOCK有关关系,莫随便修改*/
    private static int KEYSIZE = 2048;
    public static final String CHAR_ENCODING = "UTF-8";
    public static final String AES_ALGORITHM = "AES/ECB/PKCS5Padding";
    public static final String RSA_ALGORITHM = "RSA/ECB/PKCS1Padding";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 245;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 256;

//    public static String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC9ZYZHynl2jLDVqSgZfSMEGdGBeDCYvKcCwQtDFCWoO6d+zuwwy55jKkLrfYZtm9QNgxWxOLEC8kX72LZ3Lw4m4ExcGDulMjvOSDCxmyEmu+arOIUmHDk7aPTqs4BeyJDgD7eARh+WqZP8qRL0I/4OQGLhiyp+gE8SUkrFADqC6HVrwPd2A4N/Hi0AmwGNKvG9XhPzxbvr+OMHx2yhKjgbuyf0KRuLSyK2HxszSusClKQaNxd9QgrVnphHh5/hOKVCpLswdErzmKC79LE+yFI7/jO49u1smoBNGT3mrXEX4wJDQIprT+UaaHgiTChpbBVuGNBySHWRxcy8XB+HJyPrAgMBAAECggEAIX8+Q54840m40OEtFWz7b3M9iZ9/QNRE2YjY/BEpI9sBIb1pKqwopDNKSKoXx3UDCSzA/mofbHz7AbLvep7Y6ulKuBMVIX+D8lT0P8VHFLmw72F8syPUv3wtrQD4WEsyzF4bP1WnqpbtdIdWsdVXzp92nZtmdZ8t3Piu/q6uqo4x/jpgbt92YXCb+Rd3dWXNxj0bd/zOdPag4fHG0F/SHQgeIRInQ2grydx0KcRaPWThbcD+wFQEgwO34g+QRXCdGNTET3JRKUP46C+kWxJuSkz2ZPu/7TakqBezQg58oPmAaz6cMn/PbVqTKmtQJDDkUzQdY3olehwr7D82mh06uQKBgQD7HytWxofvpvNB1Ml6CkbjRrYC2ieqRfgURYdZ+wWKwx7xVk/xcEirg2xWnWjq4GqPPxnuUnljgo+tAj6KXeKmnqwjwGiG+vdHr95E8jaODepsSenWvt3UzT02a5vzgpEPB3dhJexeoVPBLy0z7lZJeaxKSMCCffD/UoBE/nsnlwKBgQDBE2U48QldXDLWo0AaMCaIKC/sxC6tzWF7XwC8OQYfDHPFJ/fx80RjaH6k6L0M4frBPvpXinGdpmf48mLwhMvP6yhWrTBCqpM1fC88YOpYfny3gsr8o9Rv+7Xg98tO0Rq0P+SmDhtqcSyOYK3zO5vkYUEn7gqW/q43nce8jkkQzQKBgQCEV1bVsmbtS4SjSuqrIENff2JoemOw/pLQzBuatgmjqVTjRuN2kAAcM3Si6O3j6kT+HPkjed9zK9lgCfC/pt7NhI2fgcUUbapn4y02FJffoUHLs+BCRfBON6NszKUxXbvoubyWPBAnhsnG43/lAFQFANRliZW6r3iUdfwXYC2zHQKBgBqoq+xN08YxSc+7IMm85kUcj2l4uR1EY3ZeasKSeRbmaEHLv/+HYGT9c6cff6L8vNvb70MRGZjz9h04kpK1M1MVHC/DoAtEdCkJaZhDpXfslxnblF2H3XhM4BMdA9RxKOfEfrSy8/Qhhk5uaPZRv7er+gwiYRHyPmntfju5xbrtAoGAOvuedl9NDlLLjiufqHfflZQmlpOGw4HzhHNMK9oEieRGmHF1ld6KxVDV4JPG+AC+0FqITZPgi0rqL86loUumRcJ/sfIRi+ygjR4R9f0/Fkej4yBflL8hfXkX3vBGAz28QMgPG6120BLjbt4Kxxsx/RWsscblc7EGQJDkpYJ5PPw=";
    public static String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCBSuihDcwzBTXKw2vEXkayJG2rmLadsVdBbPiPd5qtp8DbfI/LsTkiq7bu2HRZRyXVdJYhV+Pae9/qj/+/fmRHpgEsnRjhPv0vBO1i940G9KtyODQHeCuaXMKQJgS/8d1IzngwomORRielTnAyHvz9s1iKXS/y6Hmy90oKAhy8QN+TWVJajtTad95eR/nJCs/isjh7rRNCk3aN2qyB0ByRh2xlcSe+7wkodEHpy4UsiFg86+1eqrlfEQYib2G4DjmuTD//4gnO2tWdceCFmU/+SoW9UbEuOD1GXnqe4mYI+P5Ei6I8Cfwq9LBVtESDiM8thHDPKXBGV0ZZa7tr4Ze3AgMBAAECggEAKGzOfYS94vnvDr25VUWQ3yQqsitmdbm8KXWz5c+kqkU4NKgmoa6AyclcwiMlhcCMIMh6JGTBlk+C/GkXsr0HB6vs1BEudUbnjKsCuh3il1kqveS6x9cX8wSrxLxOYeFyqc/8nmA3t3Vy8emrhNBRk9VeiqyGUHHFk2JkYOq2J1nBQf2TEyA6ZiC0Eu+FDW/KGeEaDULz6CYGsCjIolQ80UNs6aBz3EzzjqIrwwFGjsisZY+UEf4wiQz6b8mWlw6QQL6BjSsnCbz4yfePc37mY/Nue1DR+CTgJiRABEDVp+UfkGPUt3thSyNI8fSbPcDs+jvSHVdWMpb4pNR+bpbi8QKBgQDeKw3troSxDxNjT94SYFm1boi1hFbKiXXYhp0pEcDPp+B1cMhSGldAj6vmFNhovz+hkQsislODnD6eGOSCjm74IeUneHYv6ZI8wsOB+9rXYpTlPnjtaalez8LPQC8lZ+IgSveNHaQPYhCa05rdwT4j5pdGXWcBi2Z+ycIg/Jc9WQKBgQCU+ze6XgPmLFOf7bLnAnx2XvvGRoT7XQdLOG7W6p7b55Z4qQ7cdHI0nnU/mETvYBaWlfCwEV7jA9SfxdQaSQc/DvXqWGmpiRU+Oomr2Ijp783VYfvEcWEzzgTGAwa712m0UN03hXdxfpYMxbsjFz50N5ZSEjFUKG37/3z8h++LjwKBgDplQrH4rzQC7GusMIjP/oRr208qy81MuEcDiweifPWYAK2dyWmbNtfna518WS2fJ1CGWVDzYnQze1n+3QFcAtsBvcPUK+yIDkIeR1lBI9J9PPnMo+hzX2tmXKQxELNaTNuEGPFkmxzX/9lSSWCETJsPHzAFmO+U/onQ6k/VVMNxAoGAZ53V45Px6EuaGdhLnmnXQMjNGdkAModARGvKg0WJGW8X83G5eVCwAbLX31Q7VbWh4DgGv55FPWTzo/AGlgWGua/hD41QZ+6EzNQP3mpqxzHXGfrdDYLB1TMLlOCT9V9cEGJRiYUVTuV43QdFIaHP+SqULR6kGmFIJYVLW6P70jcCgYEAuKEk80I91Jo+W6w2IP/VRN6cTO2yMSPP0JobasdrQ4yoa6SSRJy4golN10D6J5bJi0WgKDshP8ltQzEncxNMYfNsTzieGlIRZzClo7HcLE3x/8AGLNFU3ageXi3r08M4mO7gUSEmkzO+5yRBDR/a3MGpJ06d8H+wJkg3O+PsLC8=";

//    public static String pulicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhhJrY14mTvZkTjkwF6HX9dz5pbKnJDVl/9PhSAQSZZEPCX8CKDBvF9uJIpFcrkHXMUVmPkLILdDa12JtK6yoonhPJjBxlCl1x+vISQk6kFlRJbecMhxccCxbBpsKiZzpUNnmFyg6fDItDOqks76qK4BG5GoF7sXgrBjUwFbIZIH/8J6XO7ZhWQjmCa8bvyBUoSz2Jors2XmcsW4QUefQ/YyUqrFOtC1KmFgQ5VKYuMv7rFT6j/IM7l4G/a8fYpAXcSdJODvgxRTOgjUdKT26S5IJ4LvE5w4Z8I6rn74wIEMVoCsUjAIiPayd9x17tAT7Zi1cGaBZpkOhaiiBUoMVjQIDAQAB";
    public static String pulicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvts7u/pryQQIJhuzHSR79nYlOPaujOqURaveRbGIa0S94EnrA6jHX6QBOyZqWA5aL6PSpoO38Aa3VhsYWh3njMhDqkNYQC1Suy+UZ3clhxlDUH3mL8dti5fFz9xi3coB7PQe0EGY/K1SkFu4r0Dw9SBvwJ2jtimLBz/7eaCr58TRagYKlJS9L9ZazVqszFo6Zq1HM4AepyU9jXmQdy573YjHGLMRYRuQM533fNS89AkJ4JAl0MD+hCeCPwX5Kbz3uaxddgpDzw060npucLHUzSLixePPQCTxRXBxvbu+dmHC8LHXsHKWf2RIyYwFbFqyEF2iDJsD3MJomYNmOiOQJQIDAQAB";
    /**
     * 生成密钥对
     */
    public static Map<String, String> generateKeyPair() throws Exception {
        /** RSA算法要求有一个可信任的随机数源 */
        SecureRandom sr = new SecureRandom();
        /** 为RSA算法创建一个KeyPairGenerator对象 */
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        /** 利用上面的随机数据源初始化这个KeyPairGenerator对象 */
        kpg.initialize(KEYSIZE, sr);
        /** 生成密匙对 */
        KeyPair kp = kpg.generateKeyPair();
        /** 得到公钥 */
        Key publicKey = kp.getPublic();
        byte[] publicKeyBytes = publicKey.getEncoded();
        String pub = new String(Base64.getEncoder().encode(publicKeyBytes), CHAR_ENCODING);
        /** 得到私钥 */
        Key privateKey = kp.getPrivate();
        byte[] privateKeyBytes = privateKey.getEncoded();
        String pri = new String(Base64.getEncoder().encode(privateKeyBytes), CHAR_ENCODING);

        Map<String, String> map = new HashMap<String, String>();
        map.put("publicKey", pub);
        map.put("privateKey", pri);
        RSAPublicKey rsp = (RSAPublicKey) kp.getPublic();
        BigInteger bint = rsp.getModulus();
        byte[] b = bint.toByteArray();
        byte[] deBase64Value = Base64.getEncoder().encode(b);
        String retValue = new String(deBase64Value);
        map.put("modulus", retValue);
        return map;
    }

    /**
     * 加密方法 source： 源数据
     * @param source  源信息明文
     * @param publicKey 公钥
     * @return
     * @throws Exception
     */
    public static String encrypt(String source, String publicKey) throws Exception {
        Key key = getPublicKey(publicKey);
        /** 得到Cipher对象来实现对源数据的RSA加密 */
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
//		byte[] b = source.getBytes();
//		/** 执行加密操作 */
//		byte[] b1 = cipher.doFinal(b);
//		return new String(Base64.getEncoder().encode(b1), CHAR_ENCODING);
        int inputLen = source.getBytes(CHAR_ENCODING).length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(source.getBytes(CHAR_ENCODING), offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(source.getBytes(CHAR_ENCODING), offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return Base64.getEncoder().encodeToString(encryptedData);

    }

    /**
     * 解密算法 cryptograph:密文
     * @param cryptograph 密文
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static String decrypt(String cryptograph, String privateKey) throws Exception {
        Key key = getPrivateKey(privateKey);
        /** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
//		byte[] b1 = Base64.getDecoder().decode(cryptograph.getBytes());
//		/** 执行解密操作 */
//		byte[] b = cipher.doFinal(b1);
//		return new String(b, CHAR_ENCODING);
        byte[] encryptedData = Base64.getDecoder().decode(cryptograph);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return new String(decryptedData, CHAR_ENCODING);
    }

    /**
     * 得到公钥
     *
     * @param key
     *            密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static PublicKey getPublicKey(String key) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(key.getBytes()));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 得到私钥
     *
     * @param key
     *            密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(key.getBytes()));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    public static String sign(String content, String privateKey) {
        String charset = CHAR_ENCODING;
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey.getBytes()));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);

            Signature signature = Signature.getInstance("SHA1WithRSA");

            signature.initSign(priKey);
            signature.update(content.getBytes(charset));

            byte[] signed = signature.sign();

            return new String(Base64.getEncoder().encode(signed));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 验证签名
     * @param content  签名内容
     * @param sign  签名
     * @param publicKey 公钥
     * @return
     */
    public static boolean checkSign(String content, String sign, String publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.getDecoder().decode(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

            java.security.Signature signature = java.security.Signature.getInstance("SHA1WithRSA");

            signature.initVerify(pubKey);
            signature.update(content.getBytes(CHAR_ENCODING));

            boolean bverify = signature.verify(Base64.getDecoder().decode(sign));
            return bverify;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     *
     * @param args
     */
//    public static void main(String[] args) {
//        try {
//            System.out.println(generateKeyPair());
//            System.out.println("***************************");
//            String source="第二届软件绿色联盟开发者大会将于11月19日北京国家会议中心举行，以“构生态 建未来”为主题，汇聚40+议题、300+合作伙伴、2000+开发者，集结阿里巴巴、百度、华为、腾讯、网易等国内知名企业，携手广大开发者共同提升泛终端系统软件体验。大会为期一天，设有1个主论坛、4个分论坛，联盟将以更全面的议题设置、更有料的新风向释放，邀你共赴2019年度之约。大会将覆盖开源、AI、安全技术等主题方向，分享前沿技术及实践经验。在本次绿色联盟开发者大会上，开源中国的红薯将为你解读《中国开源生态研究》";
//
//            // 加密 解密
//            String decryptPrivateKey="MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCGEmtjXiZO9mROOTAXodf13PmlsqckNWX/0+FIBBJlkQ8JfwIoMG8X24kikVyuQdcxRWY+Qsgt0NrXYm0rrKiieE8mMHGUKXXH68hJCTqQWVElt5wyHFxwLFsGmwqJnOlQ2eYXKDp8Mi0M6qSzvqorgEbkagXuxeCsGNTAVshkgf/wnpc7tmFZCOYJrxu/IFShLPYmiuzZeZyxbhBR59D9jJSqsU60LUqYWBDlUpi4y/usVPqP8gzuXgb9rx9ikBdxJ0k4O+DFFM6CNR0pPbpLkgngu8TnDhnwjqufvjAgQxWgKxSMAiI9rJ33HXu0BPtmLVwZoFmmQ6FqKIFSgxWNAgMBAAECggEABlTiW2v71SjPrd4HhmYvQ74u0ANy30PiUEWaUwNWNxO1qRYlbO50oKTOgiJpRRV46yvC21feCkPdweQ1EaXyxNF3s28APiYDGPDqoYBdEqoVKfnU14z9VJLTrakuwzb00cuokRlmQa1A8WY3ZwGvGaTSNEFJjHAt3xLBIWA30H88zth9lVgs1Xz1+bx3SUvRGQPQryscb3XzXStiOZ3UEvAFY4CHGJ5OP9MOBmmrq82JLiUog1hmC7yPLvER9adnQs3gYiyu7s+N1715gbb9uNEpPiEKmF50T9/yJ4vse5H7QMOhWa10bzAZqh7fKtD/kA2cOCrgP0Vug7JfbldZAQKBgQD9xQkomvWljR7Y+SCAFcTuKKQMWpCtvaL28uiezUBW17xpPO7dri9uHGwEVqbtxcTxo9pp/m5TwP4PZfz1+0V+N+WlYlS6nGQxJN98eC5Seimr+b1cTrXqAlZIZJpcofwmcX3MmY+dPqq6vpEObDDCLY+rQC+OUTr4gWXgwQzRTQKBgQCHQBJ11nRKgZJAyTiVZRwCPD5Uf9CXZmokHxHGy93YizZ1vtODig2BUoo19VgJc8vRPCWlSbneYyvyVxNGtQM5LOuvPvG6Qkp904GxoeHgblNvaegZPQYpSURg9P3w0N66rhDxJ5DdxyGUSx1mm0YW4/w13zkCvQguZioCXCE1QQKBgC6MFqr5CXfNU8wunhSAabfZgoW1kvYbY6nyUlLWiSekuK2A/LtyuMZ0tidxkXrpnL9J88UfYzM/ohpG9chUx6FoNVpJmAsGpqYRAmE9VhnQyJX/4AjvcxSySwtKULoPCEYw6Qc/X4VTOlO4orjHhYYIf2LFCjQLmEp+yaYZIvW9AoGAF2+Gw1xxsidZjFhRsbFfdROzSTkMj+9zHOc1UVkqC8eznBuA0x4s0OfYgxkMNDcYbh0Z+NYZmutcRhXIhUwATvv1qtwhTsEF31+y7XC7jwQO+RNE4HqugqDrdFF/XycSMQ7QaLkOZR+INgX+BjWJo5XFsiAH5O2UaVXSma4VIwECgYA3IL92wtSkol3IDpIBhX+NfMSLhXZuqUUcpslKb9fH8/xFn7+rbJtO/9GDvfoafyAx7NT5udYAFA4uHzFMZcDN5WD/BQ9XI1OjTSQAuyGV6IIyLLKAXUmWLRvvdKDYFrUWfNFI93VtHNoVDV5LDCRU7dMYO78p9mtkQT3e/qNgow==";
//            String encryptpublicKey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhhJrY14mTvZkTjkwF6HX9dz5pbKnJDVl/9PhSAQSZZEPCX8CKDBvF9uJIpFcrkHXMUVmPkLILdDa12JtK6yoonhPJjBxlCl1x+vISQk6kFlRJbecMhxccCxbBpsKiZzpUNnmFyg6fDItDOqks76qK4BG5GoF7sXgrBjUwFbIZIH/8J6XO7ZhWQjmCa8bvyBUoSz2Jors2XmcsW4QUefQ/YyUqrFOtC1KmFgQ5VKYuMv7rFT6j/IM7l4G/a8fYpAXcSdJODvgxRTOgjUdKT26S5IJ4LvE5w4Z8I6rn74wIEMVoCsUjAIiPayd9x17tAT7Zi1cGaBZpkOhaiiBUoMVjQIDAQAB";
//
//            // 加签 验签
//            String signPrivateKey="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCGC0Hwyfkw9MrZ4Axew34QSnbfLlO3GoSC3Oy8c6O24njl3maAizPW/g+/d7+mizCFoWkV8QkIizlJPOShtVrziV3fLxsNZcw0CGu3K6aJ9qw87P/IrXTCZXUL2nCYF1zkvxq3HwiVXqZKgw/yzvg8ok5gXtfU6ClrQ555WgFGIzImASLnyY0kOKYHU87e2po/Z/PVT5Jmcifaiu8Y7FAgNYH/eyooi6gOZ24TQeIFcy58iXWhOI6Ut8rdrAyhbJtTv0vh5KmlgAOsaBy4uBJiZ4uSg3i3QohhCMZ+6cD7EA0aSUE+VVtm299ZU89NxO1i8zwUDWcCIGuaEWY00srXAgMBAAECggEAAZMsSSn2VY7quziYrJxbZNGbJ/AhZ6tWFzuFRImPSNzMUtcUGTqJd2MwyC0eAVTtUwSceXeFhaYtHCEtBoFc+jOI+NBS3wttJMdcjKBkqoZNa8HVF4SCNK6Ae40reX38x2s/YxtoPGkKRyHXFCE+ZWf6x8MVghjgM7RtrjtSfIJ0/vi2zz3ooU3EObfZ8wt9hslgMyKGaqwylKB7BX+RHtelddSxMdDXZh5ONJeXFoYYVEi99UEcIR/LaHpeY8IMKPkdw3zdrl1jO8K1SPDs84LRlXoS9iTUOb7zBV6Mtp8XPV6JgX9DCzB2oMxxnxsKC9PZyK23YWGOZbKxAHSZwQKBgQDBd72xgYgGKRKHAjpouPzA/Jywf4KvHNBm+Ukr8r2Ut9hoI5oYVObsjXinU2aeSJ+wdNzYX7vDIzvJIk5t8LIZz/kZed/5TK5wW5//t7yyGeX9cweTdpfK+J0yHVDEDsrhjCcIG0IyuZU9QdDA6sHPokFdgZmOd9ctfrZEA4o10QKBgQCxXpFQKtaD6w8BROhN6Ys07gwZRkQPcKimrF/NIVUXspTa3SiKrRC2h64rhWkAipAM3W1dbFpsR55oCpEyQ4ipvIjHJqU262n6qecEhJGv7ibCjfoWYqX2ZrMjPIEF17jqPRX2odJ6+pJ/GHxEZXPFsW+BTiFf0nPfROKMM2kYJwKBgBkA9r04NnJFYKWePhpGvjPa8g1FjYhAOxbz5OcPKj1MPWXIJCQ85NNxvUQy3TJbxHezws8cIFIp/ZITIvvGfd4kKNeIDtBTfr/6t4T/JB4IhtboxPPkAd4zn21YqJWOK95WlLzReQ7BOW5/URF9xaqmF7iUBVqKVn8Ev12u72/hAoGBAKE+ooYp24hj2elCdpnV9eqxeihD+725omibjiBS7CO2AqMRqTig6u+1Wz5Pc0y9qwU3C6SBy5ZiaXyWFreGM6hzMDk0RcAutdLeQJmX10YEEBKQtclB9zSQ2svuxjDEk2PHEr97D3kjcKjAXsBjJqOTkhYQLEELdrGD0xy35BPDAoGBAIwYJaEY7E6PnARBwgov84tuKQXdhIiYZ0DkTGH2UdN4QjAiGSYu6CO70oszQnPdrIyXUB2pEzLMVDS0O8OGYI77S3wk89mB4/6nrP+ulrdiP4GY5W7Ery7sbuPGS31XiKuJdsb0ZIu4MhJVE25cbXqqKfZk5ezSCK1nX5Xzi87X";
//            String signpublicKey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhgtB8Mn5MPTK2eAMXsN+EEp23y5TtxqEgtzsvHOjtuJ45d5mgIsz1v4Pv3e/poswhaFpFfEJCIs5STzkobVa84ld3y8bDWXMNAhrtyumifasPOz/yK10wmV1C9pwmBdc5L8atx8IlV6mSoMP8s74PKJOYF7X1Ogpa0OeeVoBRiMyJgEi58mNJDimB1PO3tqaP2fz1U+SZnIn2orvGOxQIDWB/3sqKIuoDmduE0HiBXMufIl1oTiOlLfK3awMoWybU79L4eSppYADrGgcuLgSYmeLkoN4t0KIYQjGfunA+xANGklBPlVbZtvfWVPPTcTtYvM8FA1nAiBrmhFmNNLK1wIDAQAB";
//
//            System.out.println("source:"+source);
//            //1加密
//            String encryptStr=encrypt(source,encryptpublicKey);
//            System.out.println("加密encryptStr:"+encryptStr);
//            System.out.println("***************************");
//            //2加签
//            String signStr=sign(encryptStr,signPrivateKey);
//            System.out.println("加签signStr:"+signStr);
//            System.out.println("***************************");
//            //3验签
//            boolean b=checkSign(encryptStr,signStr,signpublicKey);
//            System.out.println("验签checkSign:"+b);
//            System.out.println("***************************");
//            //4解密
//            String decryptStr=decrypt(encryptStr,decryptPrivateKey);
//            System.out.println("解密decryptStr:"+decryptStr);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
}
