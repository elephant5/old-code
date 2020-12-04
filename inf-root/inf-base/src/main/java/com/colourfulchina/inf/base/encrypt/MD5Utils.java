package com.colourfulchina.inf.base.encrypt;

import org.apache.commons.lang.StringUtils;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * @Description:  MD5Utils
 */
public class MD5Utils {
	
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	/**
	 * 转换字节数组为16进制字串
	 * 
	 * @param b
	 *            字节数组
	 * @return 16进制字串
	 */
	public static String byteArrayToHexString(byte[] b) {
		StringBuilder resultSb = new StringBuilder();
		for (byte aB : b) {
			resultSb.append(byteToHexString(aB));
		}
		return resultSb.toString();
	}

	/**
	 * 转换byte到16进制
	 * 
	 * @param b
	 *            要转换的byte
	 * @return 16进制格式
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * MD5编码
	 * 
	 * @param origin
	 *            原始字符串
	 * @return 经过MD5加密之后的结果
	 */
	public static String MD5Encode(String origin) {
		String resultString = null;
		try {
			resultString = origin;
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString.getBytes("UTF-8")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultString;
	}
   public static String getSign(String string, String posKey) {

        String result = string;
        // key是双方约定的加密秘钥
        result += "&key=" + posKey;
        result = MD5Utils.MD5Encode(result).toUpperCase();
        return result;
    }
	public static String getSign(Map<String, String> map, String posKey) {
		ArrayList<String> list = new ArrayList<String>();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (!entry.getValue().equals("") && !entry.getKey().equals("Signature")) {
				list.add(entry.getKey() + "=" + entry.getValue() + "&");
			}
		}
		int size = list.size();
		String[] arrayToSort = list.toArray(new String[size]);
		Arrays.sort(arrayToSort);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			sb.append(arrayToSort[i]);
		}
		String result = sb.toString();
		result += "key=" + posKey; // key是双方约定的加密秘钥
		result = MD5Utils.MD5Encode(result).toUpperCase();
		
		return result;
	}

	/**
	 * 支付签名
	 */
	public static String getFinalSign(Map<String, String> params, String payKey) {
		String sign = "";
		Collection<String> keyset = params.keySet();
		List<String> list = new ArrayList<String>(keyset);
		Collections.sort(list);
		for (int i = 0; i < list.size(); i++) {
			sign = sign + list.get(i) + "=" + params.get(list.get(i)) + "&";
		}
		String mdKey = payKey;
		String tmpSign1 = MD5Encode(sign + mdKey);
		String tmpSign2 = tmpSign1.substring(0, tmpSign1.length() - 1) + "w";
		String finalSign = MD5Encode(tmpSign2);
		return finalSign;
	}
	
	/**
	 * 支付签名 
	 */
	public static String getNotifySign(Map<String, String> params, String payKey) {
		String sign = "";
		Collection<String> keyset = params.keySet();
		List<String> list = new ArrayList<String>(keyset);
		Collections.sort(list);
		for (int i = 0; i < list.size(); i++) {
			if (StringUtils.isNotBlank(list.get(i)) && !StringUtils.equals("sign", list.get(i).toString())) {
				sign = sign + list.get(i) + "=" + params.get(list.get(i)) + "&";
			}
		}
		String mdKey = payKey;
		String tmpSign1 = MD5Encode(sign + mdKey);
		String tmpSign2 = tmpSign1.substring(0, tmpSign1.length() - 1) + "b";
		String finalSign = MD5Encode(tmpSign2);
		return finalSign;
	}
}
