package com.colourfulchina.mars.api.vo.res;

import java.io.Serializable;

import lombok.Data;

@Data
public class CityResVo implements Serializable{ 
	
	private static final long serialVersionUID = -3090080297014761859L;
	
	private String label;
	
	private String name;
	
	private String pinyin;
	
	private String zip;

}
