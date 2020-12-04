package com.colourfulchina.pangu.taishang.utils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * http帮助类
 *
 */
public class HttpUtil {
 
	/**
	 * get通用请求方法
	 * @param url
	 * @return
	 */
	public static String executeGet(String url){
		
		String result = "";
		
		if(url.startsWith("https")){
			result = httpsGet(url);
		}else{
			result = httpGet(url);
		}
		
		return result;
	}
	
	/**
	 * post通用请求方法
	 * @param url
	 * @param param
	 * @return
	 */
	public static String executePost(String url,Map<String,String> param){
		String result = "";
		
		if(url.startsWith("https")){
			result = httpsPost(url,param);
		}else{
			result = httpPost(url,param);
		}
		
		return result;
	}
	
	

	
	/**
	 * get请求
	 * @param url 请求地址
	 * @return
	 */
	public static String httpGet(String url){
		HttpClient client = new DefaultHttpClient();
		
		HttpGet post = new HttpGet(url);
		String result = "";
		try {
			HttpResponse res = client.execute(post);
			result = EntityUtils.toString(res.getEntity(),"utf-8");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(client!=null){
				client.getConnectionManager().shutdown();  
			}
		}
		return result;
	}

	/**
	 * https get请求
	 * @param url
	 * @return
	 */
	public static String httpsGet(String url){
		HttpClient client = new DefaultHttpClient();
		
        String result = "";
        X509TrustManager xtm = new X509TrustManager(){   //创建TrustManager
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
            public X509Certificate[] getAcceptedIssuers() { return null; }
        };

        SSLContext ctx;
        try {
            ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[]{xtm}, null);

            //创建SSLSocketFactory
            SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);
            client.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));
            HttpGet get = new HttpGet(url);
            HttpResponse httpResponse = client.execute(get);
            result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        	if(client!=null){
				client.getConnectionManager().shutdown();  
			}
        }

        return result;
	}
	
	/**
	 * https post请求
	 * @param url 请求地址
	 * @param param 参数
	 * @return
	 */
	public static String httpsPost(String url,Map<String,String> param){
		
		HttpClient client = new DefaultHttpClient();
		
		String result = "";
		X509TrustManager xtm = new X509TrustManager(){   //创建TrustManager 
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {} 
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {} 
            public X509Certificate[] getAcceptedIssuers() { return null; } 
        };
		
		SSLContext ctx;
		try {
			ctx = SSLContext.getInstance("TLS");
			ctx.init(null, new TrustManager[]{xtm}, null); 
	         
	        //创建SSLSocketFactory 
	        SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);
	        client.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));
	        HttpPost post = new HttpPost(url);

	        //创建参数队列
	        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
	        if(param!=null&&param.size()!=0){
	        	for (Entry<String, String> set : param.entrySet()) {  
	        		String key = set.getKey();
	        		String value = set.getValue()==null?"":set.getValue();
	        		formparams.add(new BasicNameValuePair(key, value));
	        	}
	        }

            post.setEntity(new UrlEncodedFormEntity(formparams, HTTP.UTF_8));  

	        //result = EntityUtils.toString(post.getEntity());
            HttpResponse httpResponse = client.execute(post);
            result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
        	if(client!=null){
				client.getConnectionManager().shutdown();  
			}
        }
       
		return result;
	}
	
	/**
	 * https post请求
	 * @param url 请求地址
	 * @param param 参数
	 * @return
	 */
	public static String httpPost(String url,Map<String,String> param){
		
		HttpClient client = new DefaultHttpClient();
		
		String result = "";
		try {
		
	        HttpPost post = new HttpPost(url);

	        // 创建参数队列
	        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
	        if(param!=null&&param.size()!=0){
	        	for (Entry<String, String> set : param.entrySet()) {  
	        		String key = set.getKey();
	        		String value = set.getValue()==null?"":set.getValue();
	        		formparams.add(new BasicNameValuePair(key, value));
	        	}
	        }
	        
            post.setEntity(new UrlEncodedFormEntity(formparams, HTTP.UTF_8));  
            
	        //result = EntityUtils.toString(post.getEntity());
	        
	        HttpResponse httpResponse = client.execute(post);
            result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
        	if(client!=null){
				client.getConnectionManager().shutdown();  
			}
        }
        
		return result;
	}
	

	/**
	 * https post请求
	 * @param url 请求地址
	 * @return
	 */
	public static String httpsPost(String url){
		
		HttpClient client = new DefaultHttpClient();
		
		String result = "";
		X509TrustManager xtm = new X509TrustManager(){   //创建TrustManager 
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {} 
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {} 
            public X509Certificate[] getAcceptedIssuers() { return null; } 
        };
		
		SSLContext ctx;
		try {
			ctx = SSLContext.getInstance("TLS");
			ctx.init(null, new TrustManager[]{xtm}, null); 
	         
	        //创建SSLSocketFactory 
	        SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);
	        client.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));
	        HttpPost post = new HttpPost(url);

	        //result = EntityUtils.toString(post.getEntity());
	        HttpResponse httpResponse = client.execute(post);
            result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
        	if(client!=null){
				client.getConnectionManager().shutdown();  
			}
        }
        
		return result;
	}
	


	/**
	 * http post请求
	 * @param url 请求地址
	 * @return
	 */
	public static String httpPost(String url){
		
		HttpClient client = new DefaultHttpClient();
		
		String result = "";
		
		try {
	        HttpPost post = new HttpPost(url);
	        //result = EntityUtils.toString(post.getEntity());
	        HttpResponse httpResponse = client.execute(post);
            result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
        	if(client!=null){
				client.getConnectionManager().shutdown();  
			}
        }
          
		return result;
	}
	
 

    /**
     * json方式的https请求
     * @param url
     * @param content
     * @return
     */
    public static String httpsJsonPost(String url,String content){

    	HttpClient client = new DefaultHttpClient();
    	
        String result = "";
        X509TrustManager xtm = new X509TrustManager(){   //创建TrustManager
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
            public X509Certificate[] getAcceptedIssuers() { return null; }
        };

        SSLContext ctx;
        try {
            ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[]{xtm}, null);

            //创建SSLSocketFactory
            SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);
            client.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));
            HttpPost post = new HttpPost(url);
            StringEntity s = new StringEntity(content, "UTF-8");   // 中文乱码在此解决
            s.setContentType("application/json");
            post.setEntity(s);  //设置内容

            HttpResponse httpResponse = client.execute(post);

            result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
        } finally{
        	if(client!=null){
				client.getConnectionManager().shutdown();  
			}
        }

        return result;
    }

    /**
     * json方式的https请求
     * @param url
     * @param content 请求内容
     * @param head 请求头信息
     * @return
     */
    public static String httpsJsonPost(String url,String content,Map<String,String> head){

    	HttpClient client = new DefaultHttpClient();
    	
        String result = "";
        X509TrustManager xtm = new X509TrustManager(){   //创建TrustManager
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
            public X509Certificate[] getAcceptedIssuers() { return null; }
        };

        SSLContext ctx;
        try {
            ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[]{xtm}, null);

            //创建SSLSocketFactory
            SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);
            client.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));
            HttpPost post = new HttpPost(url);
            StringEntity s = new StringEntity(content, "UTF-8");   // 中文乱码在此解决
            s.setContentType("application/json");
            post.setEntity(s);  //设置内容

            //设置head
            if(head!=null&&head.size()!=0){
                Object[] attr = head.keySet().toArray();
                for(Object obj:attr){
                    String key = String.valueOf(obj);
                    String value = head.get(String.valueOf(obj));
                    post.addHeader(key,value);
                }
            }

            HttpResponse httpResponse = client.execute(post);

            result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
        } finally{
        	if(client!=null){
				client.getConnectionManager().shutdown();  
			}
        }

        return result;
    }
    
    /**
     * json提交
     * @return
     */
    public static String executePostJSON(String url,String content){
    	String result = "";
		
		if(url.startsWith("https")){
			result = httpsJsonPost(url,content);
		}else{
			//result = httpPost(url,param);
			result = httpsJsonPost(url,content);
		}
		
		return result;
    }
}
