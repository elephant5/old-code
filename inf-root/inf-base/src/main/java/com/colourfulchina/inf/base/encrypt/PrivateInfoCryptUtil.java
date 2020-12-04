package com.colourfulchina.inf.base.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;

/**
 * @author zhaojun2
 * @ClassName PrivateInfoCryptUtil
 * @Description: 个人信息加解密方法类
 * @date 2019/12/23 10:33
 * @Version 1.0
 **/
public class PrivateInfoCryptUtil {

    private static String MOBILE_ENCRYPT_KEY;

    private static String BANKCARD_ENCRYPT_KEY;

    private static String IDENTITY_ENCRYPT_KEY;

    private static String ENCRYP_PREFIX = "Encrypt";

    private static byte[] mobileKeyBytes;

    private static byte[] identityKeyBytes;

    private static byte[] bankCardKeyBytes;

    private static final String ALGORITHM_KEY = "AES";

    private static final String ALGORITHM_ENCODING = "UTF-8";

    // 从配置文件中获取银行卡和手机号加密的key
    static {
        MOBILE_ENCRYPT_KEY = "colourful_mobile";
        BANKCARD_ENCRYPT_KEY = "colourful_bankCard";
        IDENTITY_ENCRYPT_KEY = "colourful_identity";

        try {
            mobileKeyBytes = Arrays.copyOf(
                    MOBILE_ENCRYPT_KEY.getBytes("ASCII"), 16);
            bankCardKeyBytes = Arrays.copyOf(
                    BANKCARD_ENCRYPT_KEY.getBytes("ASCII"), 16);
            identityKeyBytes = Arrays.copyOf(
                    IDENTITY_ENCRYPT_KEY.getBytes("ASCII"), 16);
        } catch (Exception e) {
            throw new IllegalArgumentException("获取手机号和银行卡加密key失败", e);
        }
    }

    /**
     * 加密手机叿
     * @param mobile 手机号原斿
     * @return 返回带Encrypt前缀的密斿
     */
    public static String encryptMobile(String mobile) {
        if (StringUtils.isEmpty(mobile)) {
            return mobile;
        }

        if (!mobile.startsWith(ENCRYP_PREFIX)) {
            byte[] encryptBytes = encrypt(mobile, mobileKeyBytes);

            if (encryptBytes == null) {
                // 加密报错亿
                return mobile;
            }
            // 返回带特定标识的密文
            // 进行Base64编码
            return new StringBuilder(ENCRYP_PREFIX).append(Base64.encodeBase64String(encryptBytes)).toString();
        } else {
            return mobile;
        }
    }

    /**
     * 解密手机叿
     * @param encryptMobile 手机号密斿
     * @return 返回解密后的原文
     */
    public static String decryptMobile(String encryptMobile) {
        if (StringUtils.isEmpty(encryptMobile)) {
            return encryptMobile;
        }

        if (encryptMobile.startsWith(ENCRYP_PREFIX)) {
            String mobile = decrypt(Base64.decodeBase64(encryptMobile.substring(ENCRYP_PREFIX.length())), mobileKeyBytes);

            if (mobile == null) {
                // 解密报错，直接返回原始忍
                return encryptMobile;
            }
            // 解密成功返回解密后的原文
            return mobile;
        } else {
            // 不是特定标识的密文，无法解密，直接返囿
            return encryptMobile;
        }
    }

    /**
     * 加密银行卿
     * @param bankCard 银行卡原斿
     * @return 返回带Encrypt前缀的密斿
     */
    public static String encryptBankCard(String bankCard) {
        if (StringUtils.isEmpty(bankCard)) {
            return bankCard;
        }

        if (!bankCard.startsWith(ENCRYP_PREFIX)) {
            byte[] encryptBytes = encrypt(bankCard, bankCardKeyBytes);

            if (encryptBytes == null) {
                // 加密报错亿
                return bankCard;
            }
            // 返回带特定标识的密文
            // 进行Base64编码
            return new StringBuilder(ENCRYP_PREFIX).append(Base64.encodeBase64String(encryptBytes)).toString();
        } else {
            return bankCard;
        }
    }

    /**
     * 解密银行卿
     * @param encryptBankCard 银行卡密斿
     * @return 返回解密后的原文
     */
    public static String decryptBankCard(String encryptBankCard) {
        if (StringUtils.isEmpty(encryptBankCard)) {
            return encryptBankCard;
        }

        if (encryptBankCard.length() <= ENCRYP_PREFIX.length()) {
            return encryptBankCard;
        }

        if (encryptBankCard.startsWith(ENCRYP_PREFIX)) {
            String bankCard = decrypt(Base64.decodeBase64(encryptBankCard.substring(ENCRYP_PREFIX.length())), bankCardKeyBytes);

            if (bankCard == null) {
                // 解密报错，直接返回原始忍
                return encryptBankCard;
            }
            // 解密成功返回解密后的原文
            return bankCard;
        } else {
            // 不是特定标识的密文，无法解密，直接返囿
            return encryptBankCard;
        }
    }

    /**
     * 加密身份证号
     * @param identity 身份证号原文
     * @return 返回带Encrypt前缀的密斿
     */
    public static String encryptIdentity(String identity) {
        if (StringUtils.isEmpty(identity)) {
            return identity;
        }

        if (!identity.startsWith(ENCRYP_PREFIX)) {
            // 身份证大写进行加寿
            byte[] encryptBytes = encrypt(identity.toUpperCase(), identityKeyBytes);

            if (encryptBytes == null) {
                // 加密报错亿
                return identity;
            }
            // 返回带特定标识的密文
            // 进行Base64编码
            return new StringBuilder(ENCRYP_PREFIX).append(Base64.encodeBase64String(encryptBytes)).toString();
        } else {
            return identity;
        }
    }

    /**
     * 解密身份证号
     * @param encryptIdentity 身份证号密文
     * @return 返回解密后的原文
     */
    public static String decryptIdentity(String encryptIdentity) {
        if (StringUtils.isEmpty(encryptIdentity)) {
            return encryptIdentity;
        }

        if (encryptIdentity.startsWith(ENCRYP_PREFIX)) {
            String identity = decrypt(Base64.decodeBase64(encryptIdentity.substring(ENCRYP_PREFIX.length())), identityKeyBytes);

            if (identity == null) {
                // 解密报错，直接返回原始忍
                return encryptIdentity;
            }
            // 解密成功返回解密后的原文
            return identity;
        } else {
            // 不是特定标识的密文，无法解密，直接返囿
            return encryptIdentity;
        }
    }

    /**
     * AES加密
     * @param clearText 待加密字符串明文
     * @param keyBytes 密钥字节数组
     * @return
     */
    private static byte[] encrypt(String clearText, byte[] keyBytes) {
        try {
            SecretKey key = new SecretKeySpec(keyBytes, ALGORITHM_KEY);
            Cipher cipher = Cipher.getInstance(ALGORITHM_KEY);
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] cleartext = clearText.getBytes(ALGORITHM_ENCODING);
            byte[] ciphertextBytes = cipher.doFinal(cleartext);

            return ciphertextBytes;
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES解密
     * @param encryptBytes 带解密的字节数据
     * @param keyBytes 密钥字节数组
     * @return
     */
    private static String decrypt(byte[] encryptBytes, byte[] keyBytes) {
        try {
            SecretKey key = new SecretKeySpec(keyBytes, ALGORITHM_KEY);
            Cipher cipher = Cipher.getInstance(ALGORITHM_KEY);
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] ciphertextBytes = cipher.doFinal(encryptBytes);

            return new String(ciphertextBytes, ALGORITHM_ENCODING);

        } catch (Exception e) {
//            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密身份证并作掩码处理
     * @param encryptIdentity
     * @return
     */
    public static String decryptAndMaskIdentity(String encryptIdentity) {
        if (StringUtils.isEmpty(encryptIdentity)) {
            return encryptIdentity;
        }

        if (encryptIdentity.startsWith(ENCRYP_PREFIX)) {
            String identity = decrypt(Base64.decodeBase64(encryptIdentity.substring(ENCRYP_PREFIX.length())), identityKeyBytes);

            if (identity == null) {
                // 解密报错，直接返回原始忍
                return encryptIdentity;
            }
            // 解密成功返回解密并处理过的字符串
            return maskString(4, 4, identity);
        } else {
            // 不是特定标识的密文，无法解密，直接返囿
            return encryptIdentity;
        }

    }

    /**
     * 解密银行卡号并作掩码处理
     * @param encryptBankCard
     * @return
     */
    public static String decryptAndMaskBankCard(String encryptBankCard) {
        if (StringUtils.isEmpty(encryptBankCard)) {
            return encryptBankCard;
        }

        if (encryptBankCard.length() <= ENCRYP_PREFIX.length()) {
            return encryptBankCard;
        }

        if (encryptBankCard.startsWith(ENCRYP_PREFIX)) {
            String bankCard = decrypt(Base64.decodeBase64(encryptBankCard.substring(ENCRYP_PREFIX.length())), bankCardKeyBytes);

            if (bankCard == null) {
                // 解密报错，直接返回原始忍
                return encryptBankCard;
            }
            // 解密成功返回解密并处理过的字符串
            return maskString(4, 4, bankCard);
        } else {
            // 不是特定标识的密文，无法解密，直接返回
            return encryptBankCard;
        }
    }

    /**
     * 解密手机号并作掩码处理
     * @param encryptMobile
     * @return
     */
    public static String decryptAndMaskMobile(String encryptMobile) {
        if (StringUtils.isEmpty(encryptMobile)) {
            return encryptMobile;
        }

        if (encryptMobile.startsWith(ENCRYP_PREFIX)) {
            String mobile = decrypt(Base64.decodeBase64(encryptMobile.substring(ENCRYP_PREFIX.length())), mobileKeyBytes);

            if (mobile == null) {
                // 解密报错，直接返回原始忍
                return encryptMobile;
            }
            // 解密成功返回解密并处理过的字符串
            return maskString(3, 4, mobile);
        } else {
            // 不是特定标识的密文，无法解密，直接返囿
            return encryptMobile;
        }
    }

    /**
     * 将字符串加星号处理
     * @param prefixLen
     * @param suffixLen
     * @param str
     * @return
     */
    private static String maskString(int prefixLen, int suffixLen, String str){
        StringBuilder sb = new StringBuilder();
        sb.append(str.substring(0, prefixLen));
        for(int i=0; i<str.length()-prefixLen-suffixLen; i++){
            sb.append("*");
        }
        sb.append(str.substring(str.length()-suffixLen, str.length()));
        return sb.toString();
    }

    /**
     * 强制将银行卡加密，如果已经加密则不处理，否则进行加密
     */
    public static String forceEncryptBankCard(String cardno){
        if (StringUtils.isBlank(cardno)) return "";
        String newno=decryptBankCard(cardno);
        if (newno.equals(cardno)){
            return encryptBankCard(cardno);
        }else{
            return cardno;
        }
    }

    /**
     * 强制将手机号加密，如果已经加密则不处理，否则进行加密
     */
    public static String forceDecryptMobile(String mobile){
        if (StringUtils.isBlank(mobile)) return "";
        String newno=decryptMobile(mobile);
        if (newno.equals(mobile)){
            return encryptMobile(mobile);
        }else{
            return mobile;
        }
    }
}
