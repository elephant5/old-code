package com.colourfulchina.inf.base.encrypt;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;

public class EncryptBase64 {
	// 加密
	public static String getBase64(String str) {
		byte[] b = null;
		String s = null;
		try {
			b = str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (b != null) {
			s = new BASE64Encoder().encode(b);
		}
		return s;
	}

	// 解密
	public static String getFromBase64(String s) {
		byte[] b = null;
		String result = null;
		if (s != null) {
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				b = decoder.decodeBuffer(s);
				result = new String(b, "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
//	public static void main(String args[]){
//		String text="I Love You ! I Love You ! I Love You ! I Love You ! I Love You ! I Love You !";
//		System.out.println(EncryptBase64.getBase64(text));
//		System.out.println(EncryptBase64.getFromBase64("TZvqROPUQfpDaq1H/BDWZA4bLYYMw1dibOSB1k08H6IH5aelheGzMhe/0pQ1mjy2ezuldGzLtnz7P3OgGPNFHx4L64zE7oetiAzRnxeXvoeAeR7lzEZiaTjNAdwwFhupgpqXIWQgFkvBCXnfYneUHPSN+Xlgr9+jpdtxo7dp2UC9FX1KGLt3Kq/qXTs9oZyikBBb/NEMdhjPJjMca83WhigtnIWTKdgUBgK3hI6NxfKXrrvpNg8yltCMJ65EK5PS5Zr/gtx7uCARiwg+yC/v4S/refe01IGDflw6cZWhPX//6JVM2Mspo76qlbqBpRm+JS7xsDI87xv3rFBbBTgGyA=="));
//	}
}
