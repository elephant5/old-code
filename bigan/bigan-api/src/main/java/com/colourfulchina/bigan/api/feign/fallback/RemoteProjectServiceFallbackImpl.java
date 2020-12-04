package com.colourfulchina.bigan.api.feign.fallback;

import com.colourfulchina.bigan.api.entity.Project;
import com.colourfulchina.bigan.api.feign.RemoteProjectService;
import com.colourfulchina.bigan.api.vo.BigProjectVo;
import com.colourfulchina.bigan.api.vo.GoodsDetailVo;
import com.colourfulchina.bigan.api.vo.ProjectCdnVo;
import com.colourfulchina.bigan.api.vo.ProjectInfoResVo;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RemoteProjectServiceFallbackImpl implements RemoteProjectService {
    /**
     * 通过项目id查询项目详细信息
     * @param projectIds
     * @return
     */
    @Override
    public CommonResultVo<List<ProjectCdnVo>> prjList(String projectIds) {
        log.error("feign 查询项目详细信息失败:{}",projectIds);
        return null;
    }

    /**
     * 通过项目id查询项目列表简略信息
     * @param projectIds
     * @return
     */
    @Override
    public CommonResultVo<List<ProjectCdnVo>> prjBriefList(String projectIds) {
        log.error("feign 查询项目列表简略信息失败:{}",projectIds);
        return null;
    }

    /**
     * 通过goodsID查询商品详情
     * @param goodsId
     * @return
     */
    @Override
    public CommonResultVo<GoodsDetailVo> getGoodsDetail(String goodsId) {
        log.error("feign 查询商品详情失败{}",goodsId);
        return null;
    }

    @Override
    public CommonResultVo<Project> getProjectById(Integer id) {
        log.error("feign 查询项目详情失败{}", id);
        return null;
    }
    @Override
    public CommonResultVo<List<ProjectInfoResVo>> projectById(String projectIds) {
        log.error("feign 查询项目失败{}",projectIds);
        return null;
    }

    @Override
    public CommonResultVo<BigProjectVo> getBigProjectById(Integer id) {
        log.error("feign 查询大项目详情失败{}", id);
        return null;
    }
}
