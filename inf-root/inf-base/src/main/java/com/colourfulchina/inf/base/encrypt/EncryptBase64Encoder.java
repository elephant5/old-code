package com.colourfulchina.inf.base.encrypt;
 
public class EncryptBase64Encoder {
    public static String getBASE64(String s) {  
        if (s == null)  
            return null;  
        return (new sun.misc.BASE64Encoder()).encode(s.getBytes());  
    }  
    // 将 BASE64 编码的字符串 s 进行解码   解密
    public static String getFromBASE64(String s) {  
        if (s == null)  
            return null;  
        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();  
        try {  
            byte[] b = decoder.decodeBuffer(s);  
            return new String(b);  
        } catch (Exception e) {  
            return null;  
        }  
    }  
    public static String mTOa(Object ming){
        return EncryptBase64Encoder.getBASE64(EncryptBase64Encoder.getBASE64(EncryptBase64Encoder.getBASE64((String)ming)));
    }
    public static String aTOm(String an){
        return EncryptBase64Encoder.getFromBASE64(EncryptBase64Encoder.getFromBASE64(EncryptBase64Encoder.getFromBASE64(an)));
    }
//    public static void main(String[] args) {
//        String a = mTOa("1999999999900000.89".toString());
//          System.out.println(a);//加密
//          System.out.println(aTOm(a));//解密
//        String text="I Love You ! I Love You ! I Love You ! I Love You ! I Love You ! I Love You !";
//        System.out.println(EncryptBase64Encoder.getBASE64(text));
//        System.out.println(EncryptBase64Encoder.getFromBASE64(EncryptBase64Encoder.getBASE64(text)));
//        System.out.println(EncryptBase64Encoder.mTOa(text.toString()));
//        System.out.println(EncryptBase64Encoder.aTOm(EncryptBase64Encoder.mTOa(text.toString())));
//    }
}
