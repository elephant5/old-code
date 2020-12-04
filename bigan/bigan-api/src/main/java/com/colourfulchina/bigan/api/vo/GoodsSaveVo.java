package com.colourfulchina.bigan.api.vo;

import lombok.Data;
import org.springframework.jdbc.support.xml.SqlXmlValue;

import java.io.Serializable;
import java.sql.SQLXML;

/**
 * User: Ryan
 * Date: 2018/7/30
 */
@Data
public class GoodsSaveVo implements Serializable {
    private static final long serialVersionUID = -1;

    private Long id;
    private String type;
    private String gift ;
    private Long shop_id ;
    private String items ;
    private String title ;
    private Long channel_id ;
    private Double price ;
    private String block ;
    private String clause ;
    private String tag ;
    private String  alias ;
    private String  projects ;
    private String  more;
    private String  uid ;
    private Long goods_id ;//返回：
    private String warning ;//
    private String result ;
    private String error ;//返回不成功原因
    private SqlXmlValue sqlxml;


}
