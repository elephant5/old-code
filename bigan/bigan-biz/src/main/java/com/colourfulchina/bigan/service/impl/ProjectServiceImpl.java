package com.colourfulchina.bigan.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.Project;
import com.colourfulchina.bigan.api.vo.ProjectCdnVo;
import com.colourfulchina.bigan.api.vo.ProjectGoodsResVo;
import com.colourfulchina.bigan.api.vo.ProjectGroupResVo;
import com.colourfulchina.bigan.api.vo.ProjectInfoResVo;
import com.colourfulchina.bigan.api.vo.ProjectReqVo;
import com.colourfulchina.bigan.mapper.GoodsMapper;
import com.colourfulchina.bigan.mapper.PrjGroupMapper;
import com.colourfulchina.bigan.mapper.ProjectMapper;
import com.colourfulchina.bigan.service.ProjectService;
import com.colourfulchina.bigan.utils.XMLUtils;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper,Project> implements ProjectService {
    @Autowired
    private ProjectMapper projectMapper;
    
    @Autowired
    private PrjGroupMapper prjGroupMapper;
    
    @Autowired
    private GoodsMapper goodsMapper;


	/**
	 * 查询项目详细信息
	 * @param strings
	 * @return
	 */
	@Override
    public List<ProjectCdnVo> selectProjectList(String[] strings) {
        return projectMapper.selectProjectList(strings);
    }

	/**
	 * 查询项目列表简略信息
	 * @param strings
	 * @return
	 */
	@Override
	public List<ProjectCdnVo> selectProjectBriefList(String[] strings) {
		return projectMapper.selectProjectBriefList(strings);
	}

	@Override
    public ProjectInfoResVo getProjectInfo(ProjectReqVo reqVo) throws Exception {
    	// 校验参数
		this.validateSendCodeParam(reqVo);
		// 查询项目列表
		ProjectInfoResVo proInfo = projectMapper.getProjectInfo(reqVo);
		// 如果项目不存在 直接返回 
		if(proInfo==null) {
			throw new Exception("项目不存在，请确认输入的项目ID是否正确");
		}
		//查询产品组信息
		List<ProjectGroupResVo> groupList = prjGroupMapper.getGroupList(reqVo);
		if(!groupList.isEmpty()) {
			for(ProjectGroupResVo group : groupList) {
				List<ProjectGoodsResVo> goodsList = goodsMapper.getGoodsList(group);
				group.setGoodsDetailVoList(goodsList);
			}
			proInfo.setPrjGroupVoList(groupList);
		}
		
		return proInfo;
	}


	@Override
	public List<ProjectInfoResVo> getProjectInfoByOrderSys(String  ids)  {

		// 查询项目列表
		List<ProjectInfoResVo> proInfo = projectMapper.getProjectInfoByOrderSys(ids);



		return proInfo;
	}
    
    /**
	 * @Title: validateSendCodeParam  
	 * @Description: 校验参数  
	 * @param @param vo
	 * @param @throws Exception   
	 * @return void  
	 * @author Sunny  
	 * @date 2018年9月13日  
	 * @throws
	 */
	private void validateSendCodeParam (ProjectReqVo vo) throws Exception {
		// 校验入参对象
		if (vo == null) {
			throw new Exception("参入不能为空");
		}
		// 校验必填项
		if (vo.getSignCode() == null) {
			throw new Exception("签名code不能为空");
		}
		if (vo.getValidateCode() == null) {
			throw new Exception("签名校验码不能为空");
		}
		if (vo.getToken() == null) {
			throw new Exception("签名token不能为空");
		}
		// 身份验证
		this.validateToken(vo.getSignCode(), vo.getValidateCode(), vo.getToken());
		//项目编号不能为空
		if (vo.getProjectId() == null) {
			throw new Exception("项目编号不能为空");
		}
		//权益包编号不能为空
		if (vo.getPackageId() == null) {
			throw new Exception("权益包编号不能为空");
		}
		/*//渠道编号不能为空
		if (vo.getPrjGroupId() == null) {
			throw new Exception(" 产品组ID不能为空");
		}*/
	}
	
	/**
	 * @Title: validateToken  
	 * @Description:身份验证 
	 * @param @param signCode
	 * @param @param validateCode
	 * @param @param token
	 * @param @throws Exception   
	 * @return void  
	 * @author Sunny  
	 * @date 2018年9月13日  
	 * @throws
	 */
	private void validateToken(String signCode, String validateCode, String token) throws Exception {
		String md5Data = XMLUtils.MD5(signCode + validateCode);
		if (md5Data == null || !md5Data.equals(token)) {
			throw new Exception("签名无效");
		}
	}
}
