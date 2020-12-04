
/**    
 * @Title: HttpClient.java  
 * @Description: 利用OkHttpClient进行简单的http请求，利用Jackson框架把json转化为java对象的实现
 * @author Sunny  
 * @date 2018年10月12日  
 * @company Colourful@copyright(c) 2018
 * @version V1.0    
*/

package com.colourfulchina.mars.utils;

import okhttp3.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: HttpClient
 * @Description: 利用OkHttpClient进行简单的http请求，利用Jackson框架把json转化为java对象的实现
 * @author Sunny
 * @date 2018年10月12日
 * @company Colourful@copyright(c) 2018
 * @version V1.0
 */

public class HttpClient {

	public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");

	/**
	 * @Title: httpGet  
	 * @Description: get请求方式
	 * @param @param url
	 * @param @return
	 * @param @throws IOException   
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
	 * @Title: httpPost  
	 * @Description: post请求方式
	 * @param @param url
	 * @param @param json
	 * @param @return
	 * @param @throws IOException   
	 * @return String  
	 * @author Sunny  
	 * @date 2018年10月12日  
	 * @throws
	 */
	public static String httpPost(String url, String json) throws IOException {
        OkHttpClient httpClient = new OkHttpClient().newBuilder()
				.connectTimeout(30, TimeUnit.SECONDS)
				.readTimeout(30, TimeUnit.SECONDS)
				.writeTimeout(30, TimeUnit.SECONDS)
				//测试环境绕过证书
				.sslSocketFactory(SSLSocketClient.getSSLSocketFactory())//配置
				.hostnameVerifier(SSLSocketClient.getHostnameVerifier())//配置
				.build();
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }
	/**
	 * @Title: httpPostUnion  
	 * @Description: post请求方式
	 * @param @param url
	 * @param @param json
	 * @param @return
	 * @param @throws IOException   
	 * @return String  
	 * @author Sunny  
	 * @date 2018年10月12日  
	 * @throws
	 */
	public static String httpPostUnion(String url, String json) throws IOException {
        OkHttpClient httpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }
	
	/**
	 * @Title: httpFormPost  
	 * @Description: TODO(这里用一句话描述这个方法的作用)  
	 * @param @param url
	 * @param @param map
	 * @param @return
	 * @param @throws IOException   
	 * @return String  
	 * @author Sunny  
	 * @date 2018年12月11日  
	 * @throws
	 */
	public static String httpFormPost(String url, Map<String, String> map) throws IOException {
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

	public static HttpResponse formPost(String url,String params) throws IOException {
		HttpPost httpPost = new HttpPost(url);
		CloseableHttpClient client = HttpClients.createDefault();
		StringEntity entity = new StringEntity(params,"utf-8");
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/x-www-form-urlencoded");
		httpPost.setEntity(entity);
		return 	 client.execute(httpPost);

	}

}
