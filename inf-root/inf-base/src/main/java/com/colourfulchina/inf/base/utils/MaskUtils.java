package com.colourfulchina.inf.base.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@UtilityClass
public class MaskUtils {

    /**
     * 生成掩码
     * @param source 原字符串
     * @param prefixLen 前缀长度
     * @param suffixLen 后缀长度
     * @param maskCode 掩码字符串
     * @param maskLen 掩码长度
     * @return
     */
    public static String mask(String source,int prefixLen,int suffixLen,String maskCode,int maskLen){
        if (StringUtils.isEmpty(source)|| prefixLen < 0 || suffixLen < 0 || StringUtils.isEmpty(maskCode) || maskLen < 0 || maskLen > 10){
            throw new RuntimeException("非法输入");
        }
        if (source.length() < (prefixLen+suffixLen) || maskCode.length() < maskLen){
            throw new RuntimeException("非法输入");
        }

        String preStr=source.substring(0,prefixLen);
        String sufStr=source.substring(source.length()-suffixLen);
        String mask=maskCode.substring(0,maskLen);
        return preStr+mask+sufStr;
    }

    /**
     * 手机号掩码(如：13912345678->139****5678)
     * @param phone
     * @return
     */
    public static String maskPhone(String phone){
        if (StringUtils.isEmpty(phone) || phone.length() != 11){
            throw new RuntimeException("非法输入");
        }
        return mask(phone,3,4,"****",4);
    }

    /**
     * 不限定长度的手机掩码
     * @param phone
     * @return
     */
    public static String allMaskPhone(String phone){
        if (StringUtils.isEmpty(phone)){
            return "";
        }else {
            int length = phone.length();
            int chu = length/3;
            int yu = length%3;
            int prefixLen = 0;
            int suffixLen = 0;
            int maskLen = 0;
            if (yu == 0){
                prefixLen = chu;
                suffixLen = chu;
                maskLen = chu;
            }else if (yu == 1){
                prefixLen = chu;
                suffixLen = chu;
                maskLen = chu +1;
            }else if (yu == 2){
                prefixLen = chu;
                suffixLen = chu+1;
                maskLen = chu+1;
            }
            return MaskUtils.mask(phone,prefixLen,suffixLen,"*********",maskLen);
        }
    }

    /**
     * 银行卡掩码(如：6222123456789027->6222****9027)
     * @param bankCard
     * @return
     */
    public static String maskBankCard(String bankCard){
        if (StringUtils.isEmpty(bankCard) || bankCard.length() < 10){
            throw new RuntimeException("非法输入");
        }
        return mask(bankCard,4,4,"****",4);
    }
    /**
     * 证件号码掩码(如：400200199910105233->400200****5233)
     * @param idCard
     * @return
     */
    public static String maskIdCard(String idCard){
        if (StringUtils.isEmpty(idCard) || idCard.length() < 15){
            throw new RuntimeException("非法输入");
        }
        return mask(idCard,6,4,"****",4);
    }
    /**
     * @Title: mobileMask
     * @Description: 正则表达式掩码手机号
     * @param mobile
     * @return String
     * @author Sunny
     * @date 2018年6月29日
     * @throws
     */
    public static String mobileMask(String mobile) {
        //如果空的话
        if(mobile==null || "".equals(mobile)) {
            return "";
        }
        try {
            return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
    /**
     * @Title: mobileCheck
     * @Description: 正则表达式匹配手机号
     * @param mobile
     * @return String
     * @throws
     */
    public static Boolean mobileCheck(String mobile) {
        String regex ="^1[3-9][0-9]\\d{4,8}$";
        Boolean isMatch = false;
        //如果空的话
        if(mobile==null || "".equals(mobile)) {
            return false;
        }
        if(mobile.length() != 11){
            return false;
        }

        try {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(mobile);
            isMatch= m.matches();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return isMatch;
    }
    /**
     * @Title: idCardMask
     * @Description: 身份证掩码处理
     * @param idCard
     * @return String
     * @author Sunny
     * @date 2018年6月29日
     * @throws
     */
    public static String idCardMask(String idCard) {
        //如果空的话
        if(idCard==null || "".equals(idCard)) {
            return "";
        }
        try {
            return idCard.replaceAll("(\\d{4})\\d{10}(\\w{4})","$1*****$2");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * @Title: bankCardMask
     * @Description: 银行卡掩码处理
     * @param bankCard
     * @return String
     * @author Sunny
     * @date 2018年6月29日
     * @throws
     */
    public static String bankCardMask(String bankCard) {
        //如果空的话
        if(bankCard==null || "".equals(bankCard)) {
            return "";
        }
        try {
            //16位的银行卡   **** **** **** 00180
            if(bankCard.length()==16) {
                return bankCard.replaceAll("\\d{12}(\\w{4})","**** **** **** $1");
            }
            //19位的银行卡 **** **** **** **** 0001
            if(bankCard.length()==19) {
                return bankCard.replaceAll("\\d{16}(\\w{3})","**** **** **** **** $1");
            }

            return bankCard;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * @Title: nameMask
     * @Description: 姓氏隐藏
     * @param @param str
     * @param @return
     * @return String
     * @author Sunny
     * @date 2019年1月7日
     * @throws
     */
    public static String nameMask(String name) {
        //如果空的话
        if(name==null || "".equals(name)) {
            return "";
        }
        String reg = ".{1}";
        StringBuffer sb = new StringBuffer();
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(name);
        int i = 0;
        while(m.find()){
            i++;
            if(i==1) {
                continue;
            }
            m.appendReplacement(sb, "*");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * @Title: surnameMask
     * @Description: 名字隐藏
     * @param @param str
     * @param @return
     * @return String
     * @author Sunny
     * @date 2019年1月7日
     * @throws
     */
    public static String surnameMask(String name) {
        //如果空的话
        if(name==null || "".equals(name)) {
            return "";
        }
        String reg = ".{1}";
        StringBuffer sb = new StringBuffer();
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(name);
        int i = 0;
        while(m.find()){
            i++;
            if(i==name.length()) {
                continue;
            }
            m.appendReplacement(sb, "*");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public static String idNumMask(String idNum){
        if(StringUtils.isNotEmpty(idNum)){
            int cardNoL = idNum.length();
            return idNum.substring(0, 5)+makeReplaceChar(cardNoL-10)+idNum.substring(cardNoL-5);
        }
        return null;
    }

    public static String bankCardNoMask(String bankCard){
        if(StringUtils.isNotEmpty(bankCard)){
            int cardNoL = bankCard.length();
            return bankCard.substring(0, 6)+makeReplaceChar(cardNoL-10)+bankCard.substring(cardNoL-4);
        }
        return null;
    }

//    public static String mobileMask(String mobile){
//        if(StringUtils.isNotEmpty(mobile)){
//            int cardNoL = mobile.length();
//            return mobile.substring(0, 3)+makeReplaceChar(cardNoL-7)+mobile.substring(cardNoL-4);
//        }
//        return null;
//    }

    private static String makeReplaceChar(int len){
        StringBuffer spaceStr = new StringBuffer();
        for(int i = 0;i<len; i++){
            spaceStr.append("*");
        }
        return spaceStr.toString();
    }
}
