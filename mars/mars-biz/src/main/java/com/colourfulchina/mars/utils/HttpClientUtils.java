
/**    
 * @Title: UnionPayUtils.java  
 * @Description: TODO(用一句话描述该文件做什么)  
 * @author Sunny  
 * @date 2018年11月1日  
 * @company Colourful@copyright(c) 2018
 * @version V1.0    
*/

package com.colourfulchina.mars.utils;

import okhttp3.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * @ClassName: HttpClientUtils
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Sunny
 * @date 2018年11月1日
 * @company Colourful@copyright(c) 2018
 * @version V1.0
 */

public class HttpClientUtils {

	public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");

	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
			'e', 'f' };

	/**
	 * @Title: httpGet 
	 * @Description: get请求方式
	 * @throws IOException 
	 * @return String 
	 * @author Sunny 
	 * @date 2018年10月12日 
	 * @throws
	 */
	public static String httpGet(String url) throws IOException {
		OkHttpClient httpClient = new OkHttpClient();
		Request request = new Request.Builder()
				.url(url)
				.build();
		Response response = httpClient.newCall(request).execute();
		
		return response.body().string(); // 返回的是string 类型，json的mapper可以直接处理
	}

	/**
	 * @Title: postOkHttp 
	 * @Description: post请求接口 
	 * @throws IOException 
	 * @return String 
	 * @author Sunny 
	 * @date 2018年10月31日 @throws
	 */
	public static String httpPostForm(String url, Map<String, String> map) throws IOException {
		// 上传文字格式 数据的传输，区别于多媒体输出
		FormBody.Builder formbody = new FormBody.Builder();
		// 上传参数
		for (String key : map.keySet()) {
			formbody.add(key, map.get(key));
		}
		// 创建请求体
		FormBody body = formbody.build();
		Request request = new Request.Builder()
				.url(url)
				.post(body)// 请求体
				.build();
		OkHttpClient httpClient = new OkHttpClient();

		Response response = httpClient.newCall(request).execute();
		
		return response.body().string();
	}
	
	/**
	 * @Title: httpPostJson  
	 * @Description: json传参方式post请求
	 * @param @param url
	 * @param @param json
	 * @param @return
	 * @param @throws IOException   
	 * @return String  
	 * @author Sunny  
	 * @date 2018年11月2日  
	 * @throws
	 */
	public static String httpPostJson(String url, String json) throws IOException {
		//创建json请求
		RequestBody requestBody = RequestBody.create(JSON, json);
		Request request = new Request.Builder()
				.url(url)
				.post(requestBody)// 请求体
				.build();
		OkHttpClient httpClient = new OkHttpClient();
		Response response = httpClient.newCall(request).execute();
		
		return response.body().string();
	}

	public static String httpsPostForm(String url, Map<String, String> map) throws IOException{
		// 上传文字格式 数据的传输，区别于多媒体输出
		FormBody.Builder formbody = new FormBody.Builder();
		// 上传参数
		for (String key : map.keySet()) {
			formbody.add(key, map.get(key));
		}
		// 创建请求体
		FormBody body = formbody.build();
		Request request = new Request.Builder()
				.url(url)
				.post(body)// 请求体
				.build();

		OkHttpClient httpClient = buildOKHttpClient().build();

		Response response = httpClient.newCall(request).execute();

		return response.body().string();
	}
	
	/**
	 * 签名算法
	 * @param body 报文体
	 * @param ts 时间戳
	 * @param signSecret 密钥
	 * @return
	 */
	public static String sign(String body, String ts, String signSecret) {
		String sign = encodeBySHA256(signSecret + body + ts);
		return sign;
	}

	/**
	 * encode By SHA-256
	 * @param str
	 * @return
	 */
	private static String encodeBySHA256(String str) {
		if (str == null) {
			return null;
		}
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
			return getFormattedText(messageDigest.digest());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Takes the raw bytes from the digest and formats them correct.
	 * @param bytes the raw bytes from the digest.
	 * @return the formatted bytes.
	 */
	private static String getFormattedText(byte[] bytes) {
		int len = bytes.length;
		StringBuilder buf = new StringBuilder(len * 2);
		// 把密文转换成十六进制的字符串形式
		for (int j = 0; j < len; j++) {
			buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
			buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
		}
		return buf.toString();
	}

	private static OkHttpClient.Builder buildOKHttpClient() {
		try {
			TrustManager[] trustAllCerts = buildTrustManagers();
			final SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

			final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
			OkHttpClient.Builder builder = new OkHttpClient.Builder();
			builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
			builder.hostnameVerifier((hostname, session) -> true);
			return builder;
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			e.printStackTrace();
			return new OkHttpClient.Builder();
		}
	}

	private static TrustManager[] buildTrustManagers() {
		return new TrustManager[]{
				new X509TrustManager() {
					@Override
					public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
					}

					@Override
					public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
					}

					@Override
					public java.security.cert.X509Certificate[] getAcceptedIssuers() {
						//return new java.security.cert.X509Certificate[]{};
						return null;
					}
				}
		};
	}
}
