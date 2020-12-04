package com.colourfulchina.mars.controller;

import java.util.List;

import com.colourfulchina.mars.api.vo.req.QueryGoodsDetailReqVo;
import com.colourfulchina.mars.api.vo.res.GoodsInfoResVo;
import com.colourfulchina.pangu.taishang.api.entity.Goods;
import com.colourfulchina.pangu.taishang.api.vo.req.QueryProductGroupInfoReqVo;
import com.colourfulchina.colourfulCoupon.api.enums.ThirdSourceEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.entity.GiftCode;
import com.colourfulchina.mars.api.vo.GoodsListReqVo;
import com.colourfulchina.mars.api.vo.GoodsReqVo;
import com.colourfulchina.mars.api.vo.MyGiftCodeVo;
import com.colourfulchina.mars.service.GoodsDetailService;
import com.colourfulchina.mars.service.PanguInterfaceService;
import com.colourfulchina.pangu.taishang.api.entity.SysService;
import com.colourfulchina.pangu.taishang.api.feign.RemoteProductGroupService;
import com.colourfulchina.pangu.taishang.api.vo.res.ProductGroupResVO;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/goods")
public class GoodsDetailController extends BaseController{

    private final static int SUCCESS_CODE = 100;

    private final static int ERROR_CODE = 200;
    @Autowired
    private GoodsDetailService goodsdetailService;

    @Autowired
    private PanguInterfaceService panguInterfaceService;

    @Autowired
    private RemoteProductGroupService remoteProductGroupService;

    /**
     * 根据电话号查询是否存在权益
     *
     * @param id
     * @return flag(true : 存在, false : 不存在)
     */
    @SysGodDoorLog("根据电话号查询是否存在权益")
    @GetMapping(value = "/check")
    public CommonResultVo checkGiftByid(GiftCode id) {
        CommonResultVo result = new CommonResultVo<>();
        try {
            boolean flag = goodsdetailService.checkGiftByid(id);
            result.setResult(flag);
        } catch (Exception e) {
            result.setCode(ERROR_CODE);
            result.setMsg(e.getMessage());
            log.error("日志插入异常：" + e.getMessage());
        }
        return result;
    }

    /**
     * 查询权益列表(稍后根据条件判断是否charlie或者新系统权益)
     *
     * @return
     */
    @SysGodDoorLog("查询权益列表")
    @PostMapping(value = "/selectGiftTable")
    public CommonResultVo<List<GiftCode>> selectGiftTable(GiftCode gift) {
        CommonResultVo<List<GiftCode>> result = new CommonResultVo();
        try {
            List<GiftCode> giftTableList = goodsdetailService.selectGiftCodeList(gift);
            result.setResult(giftTableList);
        } catch (Exception e) {
            result.setCode(ERROR_CODE);
            result.setMsg(e.getMessage());
            log.error("日志插入异常：" + e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("商品详情")
    @ApiOperation("商品详情")
    @PostMapping(value = "/detail")
    public CommonResultVo<List<MyGiftCodeVo>> getGoodsDetail(String actCode) throws Exception {
        CommonResultVo<List<MyGiftCodeVo>> commResultVo = new CommonResultVo<List<MyGiftCodeVo>>();
        try {
            List<MyGiftCodeVo> myGiftCodeVoList = goodsdetailService.getGoodsDetail(actCode);
            commResultVo.setResult(myGiftCodeVoList);
            commResultVo.setCode(SUCCESS_CODE);
            commResultVo.setMsg("成功");
        } catch (Exception e) {
            log.error("商品详情查询失败",e);
            commResultVo.setCode(ERROR_CODE);
            commResultVo.setMsg(e.getMessage());
        }
        return commResultVo;

    }

    @SysGodDoorLog("查询产品组资源信息")
    @ApiOperation("查询产品组资源信息")
    @PostMapping(value = "/getGroupInfo")
    public CommonResultVo<List<SysService>> getGroupInfo( Integer groupId) throws Exception {
        if(null == groupId){
            throw new Exception("参数不能为空");
        }
        CommonResultVo<List<SysService>> resultVo = new CommonResultVo<List<SysService>>();
        try {
            List<SysService> sysServices = goodsdetailService.selectGroupInfo(groupId);
            resultVo.setResult(sysServices);
            resultVo.setCode(SUCCESS_CODE);
            resultVo.setMsg("成功");
        } catch (Exception e) {
            log.error("查询产品组资源信息失败");
            resultVo.setCode(ERROR_CODE);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("在线预约首页")
    @ApiOperation("在线预约首页")
    @PostMapping(value = "/index")
    public CommonResultVo<List<MyGiftCodeVo>> getIndex(@RequestBody GoodsReqVo vo) throws Exception {
        CommonResultVo<List<MyGiftCodeVo>> commResultVo = new CommonResultVo<List<MyGiftCodeVo>>();
        try {
            List<MyGiftCodeVo> myGiftCodeVoList = goodsdetailService.getGoodsList(vo.getMemberId(), vo.getPrjCode(), vo.getActCode());
            commResultVo.setResult(myGiftCodeVoList);
            commResultVo.setCode(SUCCESS_CODE);
            commResultVo.setMsg("成功");
        } catch (Exception e) {
            log.error("商品详情查询失败",e);
            commResultVo.setCode(ERROR_CODE);
            commResultVo.setMsg(e.getMessage());
        }
        return commResultVo;

    }

    @SysGodDoorLog("根据产品组id(产品id)查询产品组下商户和规格")
    @ApiOperation("根据产品组id(产品id)查询产品组下商户和规格")
    @PostMapping(value = "/getProductGrouopInfo")
    public CommonResultVo<ProductGroupResVO> getGoodsInfoById(@RequestBody QueryProductGroupInfoReqVo reqVo) throws Exception {
        CommonResultVo<ProductGroupResVO> commResultVo = new CommonResultVo<ProductGroupResVO>();
        try {
            CommonResultVo<List<ProductGroupResVO>> resultVo = remoteProductGroupService.selectProductGroupById(reqVo);
            if(null != resultVo && !CollectionUtils.isEmpty(resultVo.getResult())){
                ProductGroupResVO productGroupResVO = resultVo.getResult().get(0);
                if(null != productGroupResVO && null != productGroupResVO.getShopChannelId()){
                    productGroupResVO.setSource(ThirdSourceEnum.getCode(productGroupResVO.getShopChannelId()));
                }
                commResultVo.setResult(productGroupResVO);
            }
            commResultVo.setCode(SUCCESS_CODE);
            commResultVo.setMsg("成功");
        } catch (Exception e) {
            log.error("商品详情查询失败",e);
            commResultVo.setCode(ERROR_CODE);
            commResultVo.setMsg(e.getMessage());
        }
        return commResultVo;

    }
    
    @SysGodDoorLog("在线预约搜索页")
    @ApiOperation("在线预约搜索页")
    @PostMapping(value = "/giftdetail")
    public CommonResultVo<MyGiftCodeVo> getGiftDetail(@RequestBody GoodsListReqVo vo) throws Exception {
        CommonResultVo<MyGiftCodeVo> commResultVo = new CommonResultVo<MyGiftCodeVo>();
        try {
            MyGiftCodeVo rseCodeVo = goodsdetailService.getGiftDetail(vo.getGiftcodeId(), vo.getGroupId());
            commResultVo.setResult(rseCodeVo);
            commResultVo.setCode(SUCCESS_CODE);
            commResultVo.setMsg("成功");
        } catch (Exception e) {
            log.error("商品详情查询失败",e);
            commResultVo.setCode(ERROR_CODE);
            commResultVo.setMsg(e.getMessage());
        }
        return commResultVo;

    }

    @SysGodDoorLog("根据激活码查询商品详情")
    @ApiOperation("根据激活码查询商品详情")
    @PostMapping(value = "/getGoodsDetailNew")
    public CommonResultVo<GoodsInfoResVo> getGoodsDetailNew(@RequestBody QueryGoodsDetailReqVo reqVo)  {
        CommonResultVo<GoodsInfoResVo> commResultVo = new CommonResultVo<GoodsInfoResVo>();
        try {
            Assert.notNull(reqVo,"激活码不能为空");
            GoodsInfoResVo resVo = goodsdetailService.getGoodsDetailNew(reqVo);
            commResultVo.setResult(resVo);
            commResultVo.setCode(SUCCESS_CODE);
            commResultVo.setMsg("成功");
        } catch (Exception e) {
            log.error("根据激活码查询商品详情失败",e);
            commResultVo.setCode(ERROR_CODE);
            commResultVo.setMsg(e.getMessage());
        }
        return commResultVo;

    }
}
