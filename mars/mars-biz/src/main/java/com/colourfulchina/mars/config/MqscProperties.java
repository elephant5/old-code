package com.colourfulchina.mars.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 导出文件配置文件
 */
@Data
@ConfigurationProperties(prefix = "mqsc")
public class MqscProperties {

    //uid
    private Integer uid;

    //商品id
    private Integer goodsId;
    //激活码数量
    private Integer actCodeNum;

    //本地生成文件基目录
    private String localSavePath;

    //远程上传地址，主机、端口、用户名、密码
    private String remotePath;
    private String remoteHost;
    private Integer remotePort;
    private String remoteUser;
    private String remotePriKeyPath;
    //第一次只会将
    private Boolean isFirst;
    //是否上传到服务器
    private Boolean isUpload;
    //是否重置商品id序列
    private Boolean isReset;
    //上传日期
    private String dateStr;
    //实时优惠券已使用更新相关属性
    //rsa加密公钥
    private String pubKey;
    private String couponUpdateUrl;
}
