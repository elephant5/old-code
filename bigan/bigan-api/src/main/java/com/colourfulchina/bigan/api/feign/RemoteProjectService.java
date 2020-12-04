package com.colourfulchina.bigan.api.feign;

import com.colourfulchina.bigan.api.entity.Project;
import com.colourfulchina.bigan.api.feign.fallback.RemoteProjectServiceFallbackImpl;
import com.colourfulchina.bigan.api.vo.BigProjectVo;
import com.colourfulchina.bigan.api.vo.GoodsDetailVo;
import com.colourfulchina.bigan.api.vo.ProjectCdnVo;
import com.colourfulchina.bigan.api.vo.ProjectInfoResVo;
import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = ServiceNameConstant.BIGAN_SERVICE, fallback = RemoteProjectServiceFallbackImpl.class)
public interface RemoteProjectService {
    /**
     * 通过项目id查询项目详细信息
     * @param projectIds
     * @return
     */
    @GetMapping("/project/cdnList/{projectIds}")
    CommonResultVo<List<ProjectCdnVo>> prjList(@PathVariable("projectIds") String projectIds);

    /**
     * 通过项目id查询项目列表简略信息
     * @param projectIds
     * @return
     */
    @GetMapping("/project/prjList/{projectIds}")
    CommonResultVo<List<ProjectCdnVo>> prjBriefList(@PathVariable("projectIds") String projectIds);

    /**
     * 根据goodsID查询项目详情
     * @param goodsId
     * @return
     */
    @GetMapping("/project/getGoodsDetail/{goodsId}")
    CommonResultVo<GoodsDetailVo> getGoodsDetail(@PathVariable("goodsId") String goodsId);

    /**
     * 根据项目ID查询项目信息
     * @param id
     * @return
     */
    @GetMapping("/project/getProjectById/{id}")
    CommonResultVo<Project> getProjectById(@PathVariable("id") Integer id);

    /**
     * @ApiOperation(value = "获取项目列表")
     * @param projectIds
     * @return
     */
    @GetMapping("/project/get/id/{projectIds}")
    CommonResultVo<List<ProjectInfoResVo>> projectById(@PathVariable("projectIds") String  projectIds) ;

    /**
     * 根据项目ID查询项目信息
     * @param id
     * @return
     */
    @GetMapping("/project/getBigProjectById/{id}")
    CommonResultVo<BigProjectVo> getBigProjectById(@PathVariable("id") Integer id);
}
