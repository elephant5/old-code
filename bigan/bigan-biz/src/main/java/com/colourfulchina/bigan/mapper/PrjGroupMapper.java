package com.colourfulchina.bigan.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.bigan.api.entity.PrjGroup;
import com.colourfulchina.bigan.api.vo.PrjGroupVo;
import com.colourfulchina.bigan.api.vo.ProjectGroupResVo;
import com.colourfulchina.bigan.api.vo.ProjectReqVo;

import java.util.List;


public interface PrjGroupMapper extends BaseMapper<PrjGroup> {
    List<PrjGroupVo> selectPrjGroupList(PrjGroupVo prjGroupVo);

	  
	/**  
	 * @Title: getGroupList  
	 * @Description: TODO(这里用一句话描述这个方法的作用)  
	 * @param @param reqVo
	 * @param @return   
	 * @return List<ProjectGroupResVo>  
	 * @author Sunny  
	 * @date 2018年9月18日  
	 * @throws  
	*/  
	List<ProjectGroupResVo> getGroupList(ProjectReqVo reqVo);
}