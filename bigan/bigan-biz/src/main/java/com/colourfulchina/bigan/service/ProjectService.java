package com.colourfulchina.bigan.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.bigan.api.entity.Project;
import com.colourfulchina.bigan.api.vo.ProjectCdnVo;
import com.colourfulchina.bigan.api.vo.ProjectInfoResVo;
import com.colourfulchina.bigan.api.vo.ProjectReqVo;

import java.util.List;

public interface ProjectService extends IService<Project> {

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
	 * @Description: 获取项目列表
	 * @param @param reqVo
	 * @param @return
	 * @return List<ProjectInfoResVo>
	 * @author Sunny
	 * @date 2018年9月18日
	 * @throws
	*/
	public ProjectInfoResVo getProjectInfo(ProjectReqVo reqVo) throws Exception;


	/**
	 *
	 * @param ids
	 * @return
	 */
	public List<ProjectInfoResVo> getProjectInfoByOrderSys(String  ids);
}
