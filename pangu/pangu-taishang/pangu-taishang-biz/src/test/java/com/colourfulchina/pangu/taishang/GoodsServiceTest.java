package com.colourfulchina.pangu.taishang;

import com.colourfulchina.pangu.taishang.api.vo.res.bigan.*;
import com.colourfulchina.pangu.taishang.mapper.ProductMapper;
import com.colourfulchina.pangu.taishang.service.BlockRuleService;
import com.colourfulchina.pangu.taishang.service.ProjectService;
import com.colourfulchina.pangu.taishang.service.ShopService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

public class GoodsServiceTest extends BaseApplicationTest{

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private BlockRuleService blockRuleService;

    @Test
    public void testQueryGoods(){
        List<ProjectCdnVo> ls = projectService.prjBriefList(new String[]{"142"});
        System.out.println(ls);
    }

    @Test
    public void testQueryGoodsDetail(){
        List<ProjectCdnVo> ls = projectService.prjList(new String[]{"142"});
        System.out.println(ls==null?"--":ls.size());
    }


    @Test
    public void testGetProject(){
        ShopDetailVo vo = shopService.getShopDetail(166L);
        System.out.println(vo);
    }


    @Test
    public void testShop(){
        GoodsDetailVo vo = null;
        try {
            vo = projectService.getGoodsDetail(12750L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(vo);
    }


    @Test
    public void testProduct(){
        BookOrderReqVo vo = new BookOrderReqVo();
        vo.setGoodsId(1);
        vo.setGroupId(1);
        vo.setProjectId(1);
        vo.setShopItemId(1);
        vo.setShopId(5);
        List<GoodsBlockVo> ls = projectService.queryGiftBlockList(vo);
        System.out.println(ls);
    }

    @Test
    public void testBlockRule(){
        String blockRule = blockRuleService.getBlockRule(12750);
        System.out.println(blockRule);
    }
}
