package com.colourfulchina.pangu.taishang.config;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * @author zhaojun2
 * @date 2019/3/16
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
    	TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

    	SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
    	.loadTrustMaterial(null, acceptingTrustStrategy)
    	.build();

    	SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

    	CloseableHttpClient httpClient = HttpClients.custom()
    	.setSSLSocketFactory(csf)
    	.build();
    	HttpComponentsClientHttpRequestFactory requestFactory =
    	new HttpComponentsClientHttpRequestFactory();
    	
    	requestFactory.setConnectTimeout(5000);
    	requestFactory.setReadTimeout(5000);

    	requestFactory.setHttpClient(httpClient);
    	RestTemplate restTemplate = new RestTemplate(requestFactory);
    	return restTemplate;
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(5000);
        factory.setConnectTimeout(5000);
        return factory;
    }
}
