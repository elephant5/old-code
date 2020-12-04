package com.colourfulchina.pangu.taishang.utils;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 产品组产品导出工具类
 */
public class ExportGroupProductUtils {

    /**
     * 获取导出表头
     * @param noAccom
     * @return
     */
    public static List<String> headRow(boolean noAccom){
        List<String> titleList=Lists.newArrayList();
        if (noAccom){
            titleList.add("城市");
            titleList.add("酒店中文名");
            titleList.add("酒店英文名");
            titleList.add("餐厅英文名");
            titleList.add("餐厅中文名");
            titleList.add("权益项目");
            titleList.add("权益类型");
            titleList.add("权益内容");
            titleList.add("市场参考价");
            titleList.add("适用日期");
            titleList.add("适用星期");
            titleList.add("餐段");
            titleList.add("净价");
            titleList.add("服务费");
            titleList.add("税费");
            titleList.add("单人总价");
            titleList.add("双人总价");
            titleList.add("成本");
            titleList.add("折扣率");
            titleList.add("点评/微商城售价（元/人）");
            titleList.add("开餐时间");
            titleList.add("不可用日期");
            titleList.add("地址");
            titleList.add("电话");
            titleList.add("泊车信息");
            titleList.add("儿童政策 ");
            titleList.add("备注");
        }else {
            titleList.add("国家");
            titleList.add("城市");
            titleList.add("酒店中文名称");
            titleList.add("酒店英文名称");
            titleList.add("房型");
            titleList.add("床型");
            titleList.add("餐型");
            titleList.add("适用日期");
            titleList.add("适用星期");
            titleList.add("结算价（RMB）");
            titleList.add("OTA参考价");
            titleList.add("地址");
            titleList.add("电话");
            titleList.add("不可用日期");
            titleList.add("备注");
        }
        return titleList;
    }
}
