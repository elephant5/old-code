package com.colourfulchina.bigan.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.bigan.api.entity.Project;
import com.colourfulchina.bigan.api.vo.ProjectCdnVo;
import com.colourfulchina.bigan.api.vo.ProjectInfoResVo;
import com.colourfulchina.bigan.api.vo.ProjectReqVo;

import java.util.List;

public interface ProjectMapper extends BaseMapper<Project> {
	/**
	 * 查询项目详细信息
	 * @param strings
	 * @return
	 */
    List<ProjectCdnVo> selectProjectList(String[] strings);

	/**
	 * 查询项目列表简略信息
	 * @param strings
	 * @return
	 */
	List<ProjectCdnVo> selectProjectBriefList(String[] strings);

	/**
	 * @Title: getProjectList
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param reqVo
	 * @param @return
	 * @return List<ProjectInfoResVo>
	 * @author Sunny
	 * @date 2018年9月18日
	 * @throws
	*/
	ProjectInfoResVo getProjectInfo(ProjectReqVo reqVo);

	/**
	 * 订单管理系统的查询项目信息
	 * @param ids
	 * @return
	 */
	List<ProjectInfoResVo> getProjectInfoByOrderSys(String ids);
}
