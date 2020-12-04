
/**
 * @Title: UnionPayRunConfig.java
 * @Description: TODO(用一句话描述该文件做什么)
 * @author Sunny
 * @date 2018年11月1日
 * @company Colourful@copyright(c) 2018
 * @version V1.0
*/

package com.colourfulchina.mars.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName: SynchroPushConfig
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Sunny
 * @date 2018年11月1日
 * @company Colourful@copyright(c) 2018
 * @version V1.0
*/
@Data
@Component
public class SynchroPushConfig {

	@Value("${sync.unionpay.getUrl}")
	private String getUrl;  //获取银联token的url  http://tdctest.95516.com/upapi/uprs/token

	@Value("${sync.unionpay.postUrl}")
	private String postUrl;  //推送到银联订单的url  http://tdctest.95516.com/upapi/uprs/notifyOrderState

	@Value("${sync.unionpay.partnerId}")
	private String partnerId;  //合作商ID， 银联分配 59

	@Value("${sync.unionpay.appId}")
	private String appId; // 请求银联接口的 身份验证id  up_jvcvohy0np_1mlr1i

	@Value("${sync.unionpay.appSecret}")
	private String appSecret; // 请求银联接口的 身份验证id 9d29fba3f5df89479c39bea20d068111

	@Value("${sync.unionpay.signSecret}")
	private String signSecret; // 签名密钥 银联分配 测试 test

}
