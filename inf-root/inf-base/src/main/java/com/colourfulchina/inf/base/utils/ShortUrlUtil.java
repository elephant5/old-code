package com.colourfulchina.inf.base.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.util.Assert;

import java.net.URLEncoder;
import java.util.Map;

@Slf4j
@UtilityClass
public class ShortUrlUtil {
	
	//API 短链http接口
	private static String WEIBO_API2_URL_PREFIX = null;
	private static String API_APPKEY = null;
	private static String API_SHORT_URL_PARAMS = null;
	private static String API_EXPAND_URL_PARAMS = null;
	private static String WEIBO_API2_URL_SHORTEN = null;
	private static String WEIBO_API2_URL_EXPAND = null;
	private static String KLF_API_URL_PREFIX = null;
	private static String KLF_API_URL_SHORT = null;
	private static String KLF_API_URL_LONG = null;
	static {
		//后续优化成从配置中读取值
		WEIBO_API2_URL_PREFIX="http://api.weibo.com/2/short_url/";
		WEIBO_API2_URL_SHORTEN=WEIBO_API2_URL_PREFIX+"shorten.json";
		WEIBO_API2_URL_EXPAND=WEIBO_API2_URL_PREFIX+"expand.json";
		API_APPKEY="2849184197";
		API_SHORT_URL_PARAMS ="source="+API_APPKEY+"&url_long=";
		API_EXPAND_URL_PARAMS ="source="+API_APPKEY+"&url_short=";

		KLF_API_URL_PREFIX="https://icolourful.com/url/api/";
		KLF_API_URL_SHORT="short";
		KLF_API_URL_LONG="long";
	}

	/**
	 * 通过目标url获取新浪微博短链
	 * @param longUrl
	 * @return
	 * @throws Exception
	 */
	public static String getWeiboShortUrl2(String longUrl) throws Exception{
		return getWeiboShortExpandUrl2(WEIBO_API2_URL_SHORTEN, API_SHORT_URL_PARAMS, URLEncoder.encode(longUrl,"utf-8"),"url_short");
	}

	/**
	 * 通过新浪微博短链获取目标url
	 * @param shortUrl
	 * @return
	 * @throws Exception
	 */
	public static String getWeiboExpandUrl2(String shortUrl) throws Exception{
		return getWeiboShortExpandUrl2(WEIBO_API2_URL_EXPAND, API_EXPAND_URL_PARAMS,shortUrl,"url_long");
	}

	/**
	 * 根据不同的参数类型获取新浪微博长短链接口
	 * @param url
	 * @param params
	 * @param longUrl
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static String getWeiboShortExpandUrl2(String url, String params, String longUrl, String key) throws Exception{
		String resultUrl=null;
		try {
			//{"urls":[{"result":true,"url_short":"http://t.cn/Rp7YRQG","url_long":"https://www.baidu.com","object_type":"","type":0,"object_id":""}]}
			log.info("ShortUrlUtil.getWeiboShortExpandUrl2 url {} params {} longUrl {} key {}",url,params,longUrl,key);
			final String result = HttpUtils.sendGet(url, params + longUrl);
			Assert.hasText(result,"接口返回为空");
			Map<String,Object> resultMap= JSON.parseObject(result,Map.class);
			Assert.notEmpty(resultMap,"转换成的map为空");
			if (resultMap.containsKey("error") || resultMap.containsKey("error_code") || !resultMap.containsKey("urls")){
				throw new Exception(resultMap.get("error").toString());
			}
			final Object urls = resultMap.get("urls");
			if (urls instanceof JSONArray){
				final JSONArray urlArr = (JSONArray) urls;
				final Object urlResult = urlArr.get(0);
				if (urlResult instanceof Map){
					final Map urlResultMap = (Map) urlResult;
					if (urlResultMap.containsKey("result")){
						if ("true".equals(urlResultMap.get("result").toString())){
							resultUrl=urlResultMap.get(key).toString();
						}
					}
				}
			}
		}catch (Exception e){
			log.error("getWeiboShortUrl error",e);
			throw new Exception(e);
		}
		return resultUrl;
	}

	public static String getKlfShortUrl(String params) throws Exception{
		return getKlfShortExpandUrl(KLF_API_URL_PREFIX+KLF_API_URL_SHORT,params,true);
	}
	public static String getKlfLongUrl(String params) throws Exception{
		return getKlfShortExpandUrl(KLF_API_URL_PREFIX+KLF_API_URL_LONG,params,false);
	}

	/**
	 * 获取客乐芙长短链
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private static String getKlfShortExpandUrl(String url, String params,boolean shortUrl) throws Exception{
		String resultUrl=null;
		try {
			log.info("ShortUrlUtil.getKlfShortExpandUrl url {} params {} ",url,params);
			final String result = HttpUtils.sendPost(url, params);
			Assert.hasText(result,"接口返回为空");
			Map<String,Object> map=JSON.parseObject(result,Map.class);
			Assert.notEmpty(map,"返回结果为空");
			Assert.isTrue(map.containsKey("code"),"接口状态有误");
			Assert.isTrue(map.containsKey("msg"),"接口状态有误");
			final Object code = map.get("code");
			final Object msg = map.get("msg");
			int codeNum= NumberUtils.toInt(code+"");
			Assert.isTrue(codeNum==100,msg+"");
			Assert.isTrue(map.containsKey("result"),"接口返回result为空");
			final Object result1 = map.get("result");
			Map<String,Object> resultMap=JSON.parseObject(result1+"",Map.class);
			Assert.notEmpty(resultMap,"返回结果为空");
			resultUrl = shortUrl ? resultMap.get("shortUrl")+"" : resultMap.get("longUrl")+"";
		}catch (Exception e){
			log.error("getKlfShortExpandUrl error",e);
			throw new Exception(e);
		}
		return resultUrl;
	}
}
