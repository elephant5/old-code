package com.colourfulchina.bigan.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.dto.GoodsInfo;
import com.colourfulchina.bigan.api.entity.Goods;
import com.colourfulchina.bigan.api.entity.PrjGroupGoods;
import com.colourfulchina.bigan.api.entity.Shop;
import com.colourfulchina.bigan.api.entity.ShopItem;
import com.colourfulchina.bigan.api.vo.GoodsDetailVo;
import com.colourfulchina.bigan.api.vo.GoodsSaveVo;
import com.colourfulchina.bigan.enums.GoodsEnums;
import com.colourfulchina.bigan.enums.ProductImportSetMenuEnums;
import com.colourfulchina.bigan.mapper.GoodsMapper;
import com.colourfulchina.bigan.service.GoodsService;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper,Goods> implements GoodsService {


    @Autowired
    GoodsMapper goodsMapper;

    @Override
    public Map saveGoodsInfoVo(Shop shop, List<ShopItem> items, Map map, GoodsSaveVo goodsVo  ,Integer projectId,String serviceCode,GoodsInfo vo){
        if(null == goodsVo){
            goodsVo  = new GoodsSaveVo();
            goodsVo.setType(serviceCode);
            if(!serviceCode.equals(GoodsEnums.service.DRINK.getCode())){
                goodsVo.setGift(vo.getGiftCode());
            }

            goodsVo.setProjects("{\"id\":"+vo.getGoodsImportVo().getProjectId()+"}");
        }
        if(null != items && items.size() > 0    ){
            goodsVo.setItems(JSONArray.toJSONString(items));
            if(serviceCode.equals(GoodsEnums.service.BUFFET.getCode())){
                for(ShopItem item :items){
                    if(StringUtils.isNotEmpty(goodsVo.getTitle())){
                        goodsVo.setTitle(goodsVo.getTitle()+"、"+item.getName());
                    }else{
                        goodsVo.setTitle(item.getName());

                    }
                }
            }
            if(serviceCode.equals(GoodsEnums.service.ACCOM.getCode())){
                goodsVo.setTitle(vo.getRoomType());
            }
        }
        goodsVo.setShop_id(shop.getId());
        goodsVo.setChannel_id(shop.getChannelId());
        if(serviceCode.equals(GoodsEnums.service.SETMENU.getCode())||serviceCode.equals(GoodsEnums.service.TEA.getCode())){
            for (ShopItem it:items) {
                if(StringUtils.isNotEmpty(goodsVo.getTitle())){
                    goodsVo.setTitle(goodsVo.getTitle()+"、" + it.getName());
                }else{
                    goodsVo.setTitle( it.getName());
                }
            }
        }
        //----------------------------------------------------
//        if(!serviceCode.equals(GoodsEnums.service.ACCOM.getCode())){
//
//            if(StringUtils.isNotEmpty(vo.getClauseBlock())){
//                goodsVo.setBlock(vo.getClauseBlock());
//            }
//        }
        if(StringUtils.isNotEmpty(vo.getClauseBlock())){
            goodsVo.setBlock(vo.getClauseBlock());
        }
        //------------------------健身没有对应的存储过程，只能手动添加----------------------------
        goodsVo.setClause(getValue(map,ProductImportSetMenuEnums.setMenu.CLAUSE.getCellName()));
        if(null != projectId ){
            goodsVo.setProjects("{\"id\":"+projectId+"}");
        }
        Map params = obj2Map(goodsVo);
        goodsMapper.saveGoodsInfo(params);
        if(params.containsKey("error")){
//            if(null  != params.get("error") && StringUtils.isNotEmpty(params.get("error")+"")){
//                log.info("新增产品错误："+params.get("error"));
//            }
//            if(null  != params.get("result") && StringUtils.isNotEmpty(params.get("result")+"")){
//                log.info("产品属性验证错误："+params.get("result"));
//            }

        }
        return params;
    }

    /**
     * 根据PrjGroupGoods中的goodsID列表查询商品详情
     * @param prjGroupGoodsList
     * @return
     */
    @Override
    public List<GoodsDetailVo> queryGoodsDetail(List<PrjGroupGoods> prjGroupGoodsList) {
        return goodsMapper.queryGoodsDetail(prjGroupGoodsList);
    }

    /**
     * 根据goodsID查询商品详情
     * @param goodsId
     * @return
     */
    @Override
    public GoodsDetailVo getGoodsDetail(String goodsId) {
        return goodsMapper.getGoodsDetail(goodsId);
    }

    /**
     * 导入查询goods
     *
     * @param goodsVo
     * @param serviceCode
     * @return
     */
    @Override
    public List<Goods> selectForList(GoodsInfo goodsVo, String serviceCode,Shop shop) {
        // TODO 增加Block查询条件
        //根据shop_id、serviceCode、giftCode查询已存在的所有goods
        Wrapper<Goods> goodsWrapper=new Wrapper<Goods>() {
            @Override
            public String getSqlSegment() {
                String str  =  "where type='" + serviceCode


                        + "' and shop_id=" + shop.getId();
                if(serviceCode.equals(GoodsEnums.service.ACCOM.getCode())){
                    str = str  + " and title='" + goodsVo.getRoomType()+"'" + " and gift='" + goodsVo.getGiftCode()+"'";
                }
                if(serviceCode.equals(GoodsEnums.service.BUFFET.getCode())){
                    String titles = Joiner.on(",").join(goodsVo.getMeatName());
                    str = str  + " and title='" + titles+"'" + " and gift='" + goodsVo.getGiftCode()+"'";
                }
                if(serviceCode.equals(GoodsEnums.service.SETMENU.getCode())||serviceCode.equals(GoodsEnums.service.TEA.getCode())){
                    String titles = Joiner.on(",").join(goodsVo.getMeatName());
                    str = str  + " and title='" + titles+"'" + "and gift='" + goodsVo.getGiftCode()+"'";
                }
                if(serviceCode.equals(GoodsEnums.service.DRINK.getCode())){
                    str = str  + " and title='" + goodsVo.getServiceName()+"'";
                }
                return str;
            }
        };

         List<Goods> goodsList = goodsMapper.selectList(goodsWrapper);
         return goodsList;
    }


    private static Map<String, String> obj2Map(Object obj) {

        Map<String, String> map = new HashMap<String, String>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0, len = fields.length; i < len; i++) {
            String varName = fields[i].getName();
            varName = varName.toLowerCase();//将key置为小写，默认为对象的属性
            try {
                boolean accessFlag = fields[i].isAccessible();
                fields[i].setAccessible(true);
                Object o = fields[i].get(obj);
                if (o != null) {
                    map.put(varName, o.toString());
                }else{
                    map.put(varName, null);
                }

                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
        return map;
    }

    public String getValue(Map map,String name){
//        map.get(ProductImportSetMenuEnums.setMenu.PACKAGENAME.getCellName())==null?null:map.get(ProductImportSetMenuEnums.setMenu.PACKAGENAME.getCellName())+"");
        String value  = map.get(name) == null?null:map.get(name)+"";
        if(StringUtils.isNotEmpty(value)&&!value.equals("null")){
            return value.trim().replaceAll("\n","").replaceAll("\r","");
        }
        return null;
    }
}
