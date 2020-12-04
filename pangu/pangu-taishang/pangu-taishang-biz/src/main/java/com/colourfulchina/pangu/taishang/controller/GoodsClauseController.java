package com.colourfulchina.pangu.taishang.controller;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.inf.base.enums.SysDictTypeEnums;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.Goods;
import com.colourfulchina.pangu.taishang.api.entity.GoodsClause;
import com.colourfulchina.pangu.taishang.api.entity.ProductGroupResource;
import com.colourfulchina.pangu.taishang.api.utils.GoodsUtils;
import com.colourfulchina.pangu.taishang.api.vo.req.GoodsBaseVo;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsClauseRes;
import com.colourfulchina.pangu.taishang.service.GoodsClauseService;
import com.colourfulchina.pangu.taishang.service.GoodsService;
import com.colourfulchina.pangu.taishang.service.ProductGroupResourceService;
import com.colourfulchina.tianyan.admin.api.entity.SysDict;
import com.colourfulchina.tianyan.admin.api.feign.RemoteDictService;
import com.colourfulchina.tianyan.common.core.constant.enums.EnumsReqCode;
import com.colourfulchina.tianyan.common.core.util.R;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/goodsClause")
@Slf4j
@AllArgsConstructor
@Api(tags = {"商品使用限制"})
public class GoodsClauseController {
    @Autowired
    private GoodsClauseService goodsClauseService;
    @Autowired
    private ProductGroupResourceService productGroupResourceService;

    @Autowired
    private GoodsService goodsService;

    private final RemoteDictService remoteDictService;


    /**
     * 根据商品id查询使用规则列表
     * @param goodsClause
     * @return
     */
    @SysGodDoorLog("根据商品id查询使用规则列表")
    @ApiOperation("根据商品id查询使用规则列表")
    @PostMapping("/list")
    public CommonResultVo<List<GoodsClauseRes>> list(@RequestBody GoodsClause goodsClause){
        CommonResultVo<List<GoodsClauseRes>> result = new CommonResultVo<>();
        try {
            Assert.notNull(goodsClause,"参数不能为空");
            Assert.notNull(goodsClause.getGoodsId(),"商品id不能为空");

            final R<List<SysDict>> dictByType = remoteDictService.findDictByType(SysDictTypeEnums.CLAUSE_TYPE.getType());
            Assert.notNull(dictByType,"远程服务调用异常:remoteDictService.findDictByType");
            Assert.isTrue(dictByType.getCode()== EnumsReqCode.SUCCESS.getCode(),"远程服务调用失败:remoteDictService.findDictByType");
            final List<SysDict> dictList = dictByType.getData();
            Assert.notEmpty(dictList,"远程服务调用结果为空:remoteDictService.findDictByType");

            final List<ProductGroupResource> resourceList = productGroupResourceService.selectByGoodsId(goodsClause.getGoodsId());
            Set<String> serviceTypeSet= Sets.newHashSet();
            resourceList.forEach(resource->{
                serviceTypeSet.add(resource.getService());
            });
            Wrapper<GoodsClause> listWrapper=new Wrapper<GoodsClause>() {
                @Override
                public String getSqlSegment() {
                    return "where del_flag = 0  and  goods_id = "+goodsClause.getGoodsId();
                }
            };
            final List<GoodsClause> goodsClauseList = goodsClauseService.selectList(listWrapper);
            List<GoodsClauseRes> resultList= Lists.newArrayList();
            Map<String,GoodsClause> goodsClauseMap= Maps.newHashMap();
            goodsClauseList.forEach(clause->{
                goodsClauseMap.put(clause.getClauseType(),clause);
            });
            for (SysDict dict:dictList){
                if (dict.getValue().equals("total") || serviceTypeSet.contains(dict.getValue())){
                    GoodsClauseRes clauseRes=new GoodsClauseRes();
                    final GoodsClause clause = goodsClauseMap.get(dict.getValue());
                    if (clause == null){
                        clauseRes.setGoodsId(goodsClause.getGoodsId());
                        clauseRes.setClauseType(dict.getValue());
                    }else {
                        BeanUtils.copyProperties(clause,clauseRes);
                    }
                    clauseRes.setClauseTypeName(dict.getLabel());
                    if (clauseRes.getClauseType().equals("total")){
                        resultList.add(0,clauseRes);
                    }else {
                        resultList.add(clauseRes);
                    }

                }
            }
            result.setResult(resultList);
        }catch (Exception e){
            log.error("查询商品使用规则出错",e);
        }
        return result;
    }

    /**
     * 根据id查询使用规则
     * @param id
     * @return
     */
    @SysGodDoorLog("根据id查询使用规则")
    @ApiOperation("根据id查询使用规则")
    @PostMapping("/get/{id}")
    public CommonResultVo<GoodsClause> get(@PathVariable Integer id){
        CommonResultVo<GoodsClause> result = new CommonResultVo<>();
        try {
            Assert.notNull(id,"商品id不能为空");
            final GoodsClause goodsClause = goodsClauseService.selectById(id);
            result.setResult(goodsClause);
        }catch (Exception e){
            log.error("查询商品使用规则出错",e);
        }
        return result;
    }

    @SysGodDoorLog("新增商品使用规则")
    @ApiOperation("新增商品使用规则")
    @PostMapping("/insert")
    public CommonResultVo<GoodsClause> insert(@RequestBody GoodsClause goodsClause){
        CommonResultVo<GoodsClause>  result = new CommonResultVo<>();
        try {
            Assert.notNull(goodsClause,"参数不能为空");
            Assert.notNull(goodsClause.getGoodsId(),"参数goodsId不能为空");
            Assert.hasText(goodsClause.getClauseType(),"参数clauseType不能为空");
            checkClauseType(goodsClause.getClauseType());
            goodsClause.setCreateTime(new Date());
            goodsClause.setCreateUser(SecurityUtils.getLoginName());
            goodsClause.setUpdateTime(new Date());
            goodsClause.setUpdateUser(SecurityUtils.getLoginName());
            goodsClauseService.insert(goodsClause);
            result.setResult(goodsClause);
        }catch (Exception e){
            log.error("新增商品使用规则失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("更新商品使用规则")
    @ApiOperation("更新商品使用规则")
    @PostMapping("/update")
    public CommonResultVo<GoodsClause> update(@RequestBody GoodsClause goodsClause){
        CommonResultVo<GoodsClause>  result = new CommonResultVo<>();
        try {
            Assert.notNull(goodsClause,"参数不能为空");
            Assert.notNull(goodsClause.getGoodsId(),"参数goodsId不能为空");
            Assert.hasText(goodsClause.getClauseType(),"参数clauseType不能为空");
            checkClauseType(goodsClause.getClauseType());
            updateGoodsClause(goodsClause);
            goodsClause.setUpdateTime(new Date());
            goodsClause.setUpdateUser(SecurityUtils.getLoginName());
            goodsClauseService.updateById(goodsClause);
            result.setResult(goodsClause);
        }catch (Exception e){
            log.error("更新商品使用规则失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("批量保存商品使用规则")
    @ApiOperation("批量保存商品使用规则")
    @PostMapping("/saveBatch")
    public CommonResultVo<List<GoodsClauseRes>> saveBatch(@RequestBody List<GoodsClause> clauseList){
        CommonResultVo<List<GoodsClauseRes>>  result = new CommonResultVo<>();
        try {
            Assert.notEmpty(clauseList,"参数不能为空");
            for (GoodsClause goodsClause:clauseList){
                Assert.notNull(goodsClause.getGoodsId(),"参数goodsId不能为空");
                Assert.hasText(goodsClause.getClauseType(),"参数clauseType不能为空");
                checkClauseType(goodsClause.getClauseType());
                if (goodsClause.getId() == null){
                    goodsClause.setCreateTime(new Date());
                    goodsClause.setCreateUser(SecurityUtils.getLoginName());
                }else {
                    final GoodsClause clause = goodsClauseService.selectById(goodsClause.getId());
                    updateGoodsClause(goodsClause);
                }
                goodsClause.setUpdateTime(new Date());
                goodsClause.setUpdateUser(SecurityUtils.getLoginName());
            }
            final boolean updateBatch = goodsClauseService.insertOrUpdateBatch(clauseList);
            Assert.isTrue(updateBatch,"批量保存商品使用规则失败");
            final Map<String, SysDict> clauseTypeMap = getClauseTypeMap();
            List<GoodsClauseRes> clauseResList=Lists.newArrayList();
            clauseList.forEach(clause->{
                GoodsClauseRes clauseRes=new GoodsClauseRes();
                BeanUtils.copyProperties(clause,clauseRes);
                final SysDict dict = clauseTypeMap.get(clauseRes.getClauseType());
                if (dict != null){
                    clauseRes.setClauseTypeName(dict.getLabel());
                }
                clauseResList.add(clauseRes);
            });
            result.setResult(clauseResList);
        }catch (Exception e){
            log.error("批量保存商品使用规则失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("根据goodsId获取商品扩展信息")
    @ApiOperation("根据goodsId获取商品扩展信息")
    @PostMapping("/selectGoodsClauseById")
    public CommonResultVo<GoodsBaseVo> selectGoodsClauseById(@RequestBody Integer goodsId){
        CommonResultVo<GoodsBaseVo> resultVo = new CommonResultVo<GoodsBaseVo>();
        GoodsBaseVo goodsBaseVo = new GoodsBaseVo();
        try {
            Goods goods = goodsService.selectById(goodsId);
            List<GoodsClause> goodsClauseList = goodsClauseService.selectGoodsClauseById(goodsId);
            goodsBaseVo.setGoodsClauseList(goodsClauseList);
            goodsBaseVo.setShortName(goods.getShortName());
            goodsBaseVo.setName(goods.getName());
            goodsBaseVo.setId(goods.getId());
            goodsBaseVo.setExpiryDate(goods.getExpiryDate());
            goodsBaseVo.setExpiryValue(goods.getExpiryValue());
            resultVo.setResult(goodsBaseVo);
        }catch (Exception e){
            log.error("根据goodsId 获取商品扩展信息异常:{}",e);
            resultVo.setCode(200);
            resultVo.setMsg("根据goodsId 获取商品扩展信息异常:");
        }
        return resultVo;
    }

    /**
     * 填充不进行更新的字段
     * @param goodsClause
     * @throws Exception
     */
    private void updateGoodsClause(GoodsClause goodsClause) throws Exception {
        final GoodsClause clause = goodsClauseService.selectById(goodsClause.getId());
        if (clause == null) {
            throw new Exception("id:" + goodsClause.getId() + "对应的使用规则不存在");
        } else if (!clause.getClauseType().equalsIgnoreCase(goodsClause.getClauseType())) {
            throw new Exception("旧的clauseType和新的clauseType不一致");
        } else {
            goodsClause.setCreateTime(clause.getCreateTime());
            goodsClause.setCreateUser(clause.getCreateUser());
        }
    }

    /**
     * 校验clauseType是否有效
     * @param clauseType
     */
    private void checkClauseType(String clauseType) {
        final Map<String, SysDict> clauseTypeMap = getClauseTypeMap();
        Assert.notNull(clauseTypeMap.get(clauseType),"资源类型:"+clauseType+"不存在");
    }

    private Map<String,SysDict> getClauseTypeMap(){
        Map<String,SysDict> clauseTypeMap=Maps.newHashMap();
        final R<List<SysDict>> dictByType = remoteDictService.findDictByType(SysDictTypeEnums.CLAUSE_TYPE.getType());
        Assert.notNull(dictByType,"远程服务调用异常:remoteDictService.findDictByType");
        Assert.isTrue(dictByType.getCode()== EnumsReqCode.SUCCESS.getCode(),"远程服务调用失败:remoteDictService.findDictByType");
        final List<SysDict> dictList = dictByType.getData();
//        Assert.notEmpty(dictList,"远程服务调用结果为空:remoteDictService.findDictByType");
        dictList.forEach(dict->{
            clauseTypeMap.put(dict.getValue(),dict);
        });
        return clauseTypeMap;
    }
}