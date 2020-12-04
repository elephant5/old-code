package com.colourfulchina.pangu.taishang.controller;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.enums.SysDictTypeEnums;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.dto.SalesChannelVo;
import com.colourfulchina.pangu.taishang.api.entity.SalesChannel;
import com.colourfulchina.pangu.taishang.api.vo.req.SalesChannelReqVo;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsChannelRes;
import com.colourfulchina.pangu.taishang.service.SalesChannelService;
import com.colourfulchina.tianyan.admin.api.entity.SysDict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 渠道controller
 *
 * @Author: luck.luo
 * @Description:
 * @Date: 2019/2/27 10:03
 */
@RestController
@RequestMapping("/salesChannel")
@Slf4j
@Api(value = "渠道controller",tags = {"渠道操作接口"})
public class SalesChannelController {
    @Autowired
    private SalesChannelService salesChannelService;

    /**
     * 渠道分页查询
     * @param pageVoReq
     * @return
     */
    @SysGodDoorLog("渠道列表")
    @ApiOperation("渠道列表")
    @PostMapping("/page")
    public CommonResultVo<PageVo> page(@RequestBody PageVo<SalesChannelReqVo> pageVoReq){
        CommonResultVo<PageVo> result = new CommonResultVo<>();
        try {
            final PageVo<SalesChannel> pageVoRes = salesChannelService.findPageList(pageVoReq);
            result.setResult(pageVoRes);
        }catch (Exception e){
            log.error("查询渠道列表失败",e);
            result.setMsg(e.getMessage());
            result.setCode(200);
        }
        return result;
    }

    /**
     * 新增渠道
     * @param salesChannel
     * @return
     */
    @SysGodDoorLog("新增渠道")
    @ApiOperation("新增渠道")
    @PostMapping("/add")
    public CommonResultVo add(@RequestBody SalesChannelReqVo salesChannel){
        CommonResultVo result = new CommonResultVo<>();
        try {
            Assert.notNull(salesChannel.getBankName(),"银行名称不能为空！");
            Assert.notNull(salesChannel.getSalesWayName(),"销售方式不能为空！");
            Assert.notNull(salesChannel.getSettleMethodId(),"结算方式不能为空！");
            Assert.notNull(salesChannel.getStatus(),"状态不能为空！");
            Boolean flag = salesChannelService.insertSalesChannel(salesChannel);
            if(!flag){
                throw new Exception("没有插入记录");
            }
        }catch (Exception e){
            log.error("新增渠道失败",e);
            result.setMsg(e.getMessage());
            result.setCode(200);
        }
        return result;
    }

    /**
     * 更新渠道
     * @param salesChannel
     * @return
     */
    @SysGodDoorLog("更新渠道")
    @ApiOperation("更新渠道")
    @PostMapping("/update")
    public CommonResultVo update(@RequestBody SalesChannel salesChannel){
        CommonResultVo result = new CommonResultVo<>();
        try {
            Assert.notNull(salesChannel.getId(),"id不能为空！");
            Assert.notNull(salesChannel.getStatus(),"状态不能为空！");

            Boolean flag = salesChannelService.updateChannelById(salesChannel);
            if(!flag){
                throw new Exception("没有更新记录");
            }
        }catch (Exception e){
            log.error("更新渠道失败",e);
            result.setMsg(e.getMessage());
            result.setCode(200);
        }
        return result;
    }

    /**
     * 更新渠道
     * @param salesChannel
     * @return
     */
    @SysGodDoorLog("启用停用渠道")
    @ApiOperation("启用停用渠道")
    @PostMapping("/enable")
    public CommonResultVo enable(@RequestBody SalesChannel salesChannel){
        CommonResultVo result = new CommonResultVo<>();
        try {
            Assert.notNull(salesChannel.getId(),"id不能为空！");
            Assert.notNull(salesChannel.getStatus(),"状态不能为空！");

            Boolean flag = salesChannelService.updateById(salesChannel);
            if(!flag){
                throw new Exception("没有更新记录");
            }
        }catch (Exception e){
            log.error("启用停用渠道失败",e);
            result.setMsg(e.getMessage());
            result.setCode(200);
        }
        return result;
    }

    /**
     * 根据id查找渠道
     * @param id
     * @return
     */
    @SysGodDoorLog("根据id查询渠道")
    @ApiOperation("根据id查询渠道")
    @GetMapping("/get/{id}")
    public CommonResultVo<SalesChannel> get(@PathVariable Integer id){
        CommonResultVo<SalesChannel> result = new CommonResultVo<>();
        try {
            SalesChannel salesChannel = salesChannelService.selectById(id);
            result.setResult(salesChannel);
        }catch (Exception e){
            log.error("根据id查询渠道失败",e);
            result.setMsg(e.getMessage());
            result.setCode(200);
        }
        return result;
    }

    /**
     * 查找所有渠道
     * @return
     */
    @SysGodDoorLog("查找所有渠道")
    @ApiOperation("查找所有渠道")
    @GetMapping("/selectAll")
    public CommonResultVo<List<SalesChannelVo>> selectAll(){
        CommonResultVo<List<SalesChannelVo>> result = new CommonResultVo<>();
        try {
            List<SalesChannelVo> salesChannel = salesChannelService.selectAll();
            result.setResult(salesChannel);
        }catch (Exception e){
            log.error("查找所有渠道失败",e);
            result.setMsg(e.getMessage());
            result.setCode(200);
        }
        return result;
    }
    /**
     * 查找验证方式渠道
     * @return
     */
    @SysGodDoorLog("查找验证方式渠道")
    @ApiOperation("查找验证方式渠道")
    @GetMapping("/authType")
    public CommonResultVo<List<SysDict>> authType(){
        CommonResultVo<List<SysDict>> result = new CommonResultVo<>();
        try {
            List<SysDict> sysDictList = salesChannelService.selectSysDict(SysDictTypeEnums.AUTH_TYPE.getType());
            result.setResult(sysDictList);
        }catch (Exception e){
            log.error("查找验证方式渠道失败",e);
            result.setMsg(e.getMessage());
            result.setCode(200);
        }
        return result;
    }

    /**
     * 根据大客户、销售渠道销售方式查询salesChannel
     * @param salesChannel
     * @return
     */
    @SysGodDoorLog("根据大客户、销售渠道销售方式查询salesChannel")
    @ApiOperation("根据大客户、销售渠道销售方式查询salesChannel")
    @PostMapping("/selectByBCW")
    public CommonResultVo<List<SalesChannel>> selectByBCW(@RequestBody SalesChannel salesChannel){
        CommonResultVo<List<SalesChannel>> result = new CommonResultVo<>();
        try {
            List<SalesChannel> list = salesChannelService.selectByBCW(salesChannel);
            result.setResult(list);
        }catch (Exception e){
            log.error("根据大客户、销售渠道销售方式查询失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 根据大客户、销售渠道销售方式查询salesChannel
     * @param salesChannel
     * @return
     */
    @SysGodDoorLog("根据大客户查询salesChannel")
    @ApiOperation("根据大客户查询salesChannel")
    @PostMapping("/selectByBankIds")
    public CommonResultVo<List<SalesChannel>> selectByBankIds(@RequestBody SalesChannel salesChannel){
        CommonResultVo<List<SalesChannel>> result = new CommonResultVo<>();
        try {
            Wrapper wrapper = new Wrapper() {
                @Override
                public String getSqlSegment() {
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("where del_flag = 0");
                    if (StringUtils.isNotBlank(salesChannel.getBankId())){
                        stringBuffer.append(" and bank_id ='"+salesChannel.getBankId()+"'");
                    }

//                    if (StringUtils.isNotBlank(salesChannel.getSalesChannelId())){
//                        stringBuffer.append(" and sales_channel_id ='"+salesChannel.getSalesChannelId()+"'");
//                    }else {
//                        stringBuffer.append(" and (sales_channel_id IS NULL or sales_channel_id = '')");
//                    }
//                    if (StringUtils.isNotBlank(salesChannel.getSalesWayId())){
//                        stringBuffer.append(" and sales_way_id ='"+salesChannel.getSalesWayId()+"'");
//                    }
                    return stringBuffer.toString();
                }
            };
            List<SalesChannel> list = salesChannelService.selectList(wrapper);
            result.setResult(list);
        }catch (Exception e){
            log.error("根据大客户查询失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("根据渠道id获取渠道信息")
    @ApiOperation("根据渠道id获取渠道信息")
    @PostMapping("/selectSalesChannel")
    public CommonResultVo<List<Integer>> selectSalesChannel(@RequestBody String bankId){
        CommonResultVo<List<Integer>> result = new CommonResultVo<>();
        try {
             List<Integer> salesChannelRes = salesChannelService.selectSalesChannel(bankId);
             result.setResult(salesChannelRes);
        }catch (Exception e){
            log.error("根据渠道id获取渠道信息异常:",e);
            result.setCode(200);
            result.setMsg("根据渠道id获取渠道信息异常");
		}
		return result ;
	}

    /**
     * 根据id查找渠道详情
     * @param id
     * @return
     */
    @SysGodDoorLog("根据id查询渠道详情")
    @ApiOperation("根据id查询渠道详情")
    @PostMapping("/findById")
    public CommonResultVo<GoodsChannelRes> findById(@RequestBody Integer id){
        CommonResultVo<GoodsChannelRes> result = new CommonResultVo<>();
        try {
            GoodsChannelRes goodsChannelRes = salesChannelService.findById(id);
            result.setResult(goodsChannelRes);
        }catch (Exception e){
            log.error("根据id查询渠道详情失败",e);
            result.setMsg(e.getMessage());
            result.setCode(200);
        }
        return result;
    }

    @SysGodDoorLog("查询销售渠道列表详情")
    @ApiOperation("查询销售渠道列表详情")
    @PostMapping("/selectSalesChannelList")
    public CommonResultVo<List<GoodsChannelRes>> selectSalesChannelList(){
        CommonResultVo<List<GoodsChannelRes>> result = new CommonResultVo<>();
        try {
            List<GoodsChannelRes> list = salesChannelService.selectSalesChannelList();
            result.setResult(list);
        }catch (Exception e){
            log.error("查询销售渠道列表详情失败",e);
            result.setCode(200);
            result.setMsg("查询销售渠道列表详情失败");
        }
        return result;
    }
}
