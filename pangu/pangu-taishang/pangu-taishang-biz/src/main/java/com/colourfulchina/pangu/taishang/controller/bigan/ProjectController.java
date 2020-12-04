package com.colourfulchina.pangu.taishang.controller.bigan;

import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.vo.res.bigan.*;
import com.colourfulchina.pangu.taishang.service.ProjectService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bigan-replace/project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @ApiOperation(value = "生成项目列表简略信息")
    @GetMapping("/prjList/{projectIds}")
    public CommonResultVo<List<ProjectCdnVo>> prjList(@PathVariable(value = "projectIds") String projectIds) {
        CommonResultVo<List<ProjectCdnVo>> result = new CommonResultVo<>();
        if (projectIds == null || "".equals(projectIds)) {
            result.setCode(300);
            result.setMsg("项目id为空！");
            return result;
        }
        try {
            String[] ids = projectIds.split(",");
            List<ProjectCdnVo> projectCdnVoList = projectService.prjBriefList(ids);
            result.setResult(projectCdnVoList);
        } catch (Exception e) {
            log.error("生成项目列表简略信息失败", e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "生成项目详细信息")
    @GetMapping("/cdnList/{projectIds}")
    public CommonResultVo<List<ProjectCdnVo>> cdnList(@PathVariable(required = false) String projectIds) {
        CommonResultVo<List<ProjectCdnVo>> result = new CommonResultVo<>();
        if (projectIds == null || "".equals(projectIds)) {
            result.setCode(300);
            result.setMsg("项目id为空！");
            return result;
        }
        String[] ids = projectIds.split(",");
        List<ProjectCdnVo> projectCdnVoList = projectService.prjList(ids);
        result.setResult(projectCdnVoList);
        return result;
    }

    @ApiOperation(value="根据项目ID查询项目信息")
    @GetMapping("/getProjectById/{id}")
    public CommonResultVo<Project> getProjectByid(@PathVariable Integer id){
        CommonResultVo<Project> resultVo = new CommonResultVo<Project>();
        Project project = projectService.getProjectById(id);
        resultVo.setResult(project);
        return resultVo;
    }

    @ApiOperation("根据goodsID查询商品详情")
    @GetMapping("/getGoodsDetail/{pgpId}")
    public CommonResultVo<GoodsDetailVo> getGoodsDetail(@PathVariable(value = "pgpId") Long pgpId) {
        CommonResultVo<GoodsDetailVo> result = new CommonResultVo<>();
        try {
            GoodsDetailVo vo = projectService.getGoodsDetail(pgpId);
            result.setResult(vo);
        }catch (Exception e){
            log.error("根据goodsID查询商品详情失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

}
