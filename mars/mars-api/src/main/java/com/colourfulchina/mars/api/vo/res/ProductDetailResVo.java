package com.colourfulchina.mars.api.vo.res;

import java.io.Serializable;

import com.colourfulchina.pangu.taishang.api.vo.res.GroupProductDetailRes;
import com.colourfulchina.pangu.taishang.api.vo.res.QueryBookBlockRes;

import lombok.Data;

@Data
public class ProductDetailResVo implements Serializable{ 
	
	private static final long serialVersionUID = 3167544938154228817L;
	
	private GroupProductDetailRes productDetail;
	
	private QueryBookBlockRes blockRules;
	
}
