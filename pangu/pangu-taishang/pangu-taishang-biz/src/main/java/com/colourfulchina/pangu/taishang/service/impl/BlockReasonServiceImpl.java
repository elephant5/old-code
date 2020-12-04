package com.colourfulchina.pangu.taishang.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.pangu.taishang.api.dto.BlockRule;
import com.colourfulchina.pangu.taishang.api.entity.BlockReason;
import com.colourfulchina.pangu.taishang.api.entity.ShopItem;
import com.colourfulchina.pangu.taishang.api.enums.BlockReasonEnums;
import com.colourfulchina.pangu.taishang.mapper.BlockReasonMapper;
import com.colourfulchina.pangu.taishang.mapper.ShopItemMapper;
import com.colourfulchina.pangu.taishang.service.BlockReasonService;
import com.colourfulchina.pangu.taishang.service.BlockRuleService;
import com.colourfulchina.pangu.taishang.service.ShopItemService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@Slf4j
public class BlockReasonServiceImpl  extends ServiceImpl<BlockReasonMapper, BlockReason> implements BlockReasonService {
    @Autowired
    private BlockReasonMapper blockReasonMapper;
    @Autowired
    private ShopItemMapper shopItemMapper;

    @Autowired
    BlockRuleService blockRuleService;

    /**
     * block原因插入
     * @param type
     * @param value
     * @param list
     * @return
     * @throws Exception
     */
    @Override
    public List<BlockReason> addReason(String type, Integer value, List<BlockRule> list) throws Exception {
        List<BlockReason> result = Lists.newLinkedList();
//        Wrapper wrapper = new Wrapper() {
//            @Override
//            public String getSqlSegment() {
//                return "where type ='"+type+"' and value ="+value;
//            }
//        };
//        //删除该条记录旧的原因
//        blockReasonMapper.delete(wrapper);
        //添加新原因
        if (!CollectionUtils.isEmpty(list)){
            for (BlockRule blockRule : list) {
                if (StringUtils.isNotBlank(blockRule.getReason())){
                    BlockReason blockReason = new BlockReason();
                    blockReason.setType(type);
                    blockReason.setValue(value);
                    blockReason.setBlock(blockRule.getRule());
                    blockReason.setReason(blockRule.getReason());
                    blockReasonMapper.insert(blockReason);
                    result.add(blockReason);
                }
            }
        }
        return result;
    }

    /**
     * 获取商户关房关餐
     *
     * @param id
     * @return
     */
    @Override
    public List<BlockReason> getBlockReason(Integer id) {
        ShopItem shopItem = shopItemMapper.selectById(id);
        Wrapper wrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = 0 and ((value ="+id+" and type ='"+BlockReasonEnums.ReasonType.TYPE_SHOP_ITEM.getCode()+"') or (value ="+shopItem.getShopId()+" and type ='"+BlockReasonEnums.ReasonType.TYPE_SHOP_PROTOCOL.getCode()+"'))";
            }
        };
//        EntityWrapper<BlockReason> entityLocal = new EntityWrapper();
//        entityLocal.eq("value",id).eq("type","shop").or().eq("type","shop_item").eq("value",id);
        List<BlockReason> result  = blockReasonMapper.selectList(wrapper);
        if(result.size() > 0 ){
            for(BlockReason reason : result){
                List<BlockRule>  list  = blockRuleService.blockRuleStr2list(reason.getBlock());
                if(!list.isEmpty()){
                    reason.setBlock(list.get(0).getNatural());
                }
            }
        }
        return result;
    }
}
