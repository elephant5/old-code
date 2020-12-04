package com.colourfulchina.mars.utils;

import com.colourfulchina.pangu.taishang.api.enums.ShopTypeEnums;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * 预约单导出工具类
 */
public class ExportReservOrderUtils {

    /**
     * 获取导出表头
     * @param type
     * @return
     */
    public static List<String> headRow(String type){
        List<String> titleList=Lists.newArrayList();
        if (!ShopTypeEnums.ACCOM.getCode().equals(type)){
            titleList.add("序号");
            titleList.add("系统编号");
            titleList.add("预订月度");
            titleList.add("商品编号");
            titleList.add("商品简称");
            titleList.add("销售渠道");
            titleList.add("销售年度");
            titleList.add("销售月度");
            titleList.add("权益类型");
            titleList.add("产品类型");
            titleList.add("订单日期");
            titleList.add("接单侠");
            titleList.add("预定状态");
            titleList.add("状态日期");
            titleList.add("激活码");
            titleList.add("验证码");
            titleList.add("会员有效期");
            titleList.add("客户姓名");
            titleList.add("联系方式");
            titleList.add("用餐月度");
            titleList.add("用餐日期");
            titleList.add("餐型");
            titleList.add("用餐人数");
            titleList.add("用餐人姓名");
            titleList.add("酒店名");
            titleList.add("餐厅名称");
            titleList.add("酒店名/餐厅名称");
            titleList.add("预订渠道");
            titleList.add("所属地区");
            titleList.add("使用状态");
            titleList.add("协议价格");
            titleList.add("净价");
            titleList.add("服务费");
            titleList.add("增值税");
            titleList.add("结算规则");
            titleList.add("结算方式");
            titleList.add("贴现金额");
            titleList.add("银行贴现金额");
            titleList.add("权益已使用次数");
            titleList.add("权益剩余次数");
        }else {
            titleList.add("产品类型");
            titleList.add("系统编号");
            titleList.add("接单侠");
            titleList.add("处理人");
            titleList.add("预定状态");
            titleList.add("状态日期");
            titleList.add("预定成功日期");
            titleList.add("预定取消日期");
            titleList.add("取消原因");
            titleList.add("预定失败日期");
            titleList.add("失败原因");
            titleList.add("是否调剂");
            titleList.add("预定年度");
            titleList.add("预定月度");
            titleList.add("大客户");
            titleList.add("销售渠道");
            titleList.add("销售方式");
            titleList.add("销售年份");
            titleList.add("销售年月");
            titleList.add("项目编号");
            titleList.add("项目简称");
            titleList.add("项目名称");
            titleList.add("系统名称");
            titleList.add("系统订单日期");
            titleList.add("产品编号/券码");
            titleList.add("原有效期");
            titleList.add("是否延期");
            titleList.add("新有效期");
            titleList.add("银行单号");
            titleList.add("客户姓名");
            titleList.add("手机");
            titleList.add("证件类型");
            titleList.add("证件号");
            titleList.add("第三方客户号");
            titleList.add("入住人姓名");
            titleList.add("酒店");
            titleList.add("城市");
            titleList.add("订房渠道");
            titleList.add("结算方式");
            titleList.add("渠道单号");
            titleList.add("入住年份");
            titleList.add("入住年月");
            titleList.add("入住日期");
            titleList.add("离店日期");
            titleList.add("间数");
            titleList.add("天数");
            titleList.add("间夜数");
            titleList.add("酒店单价(RMB)");
            titleList.add("酒店总价(RMB)");
            titleList.add("外币种类");
            titleList.add("外币金额");
            titleList.add("参考汇率");
            titleList.add("酒店预订类型");
            titleList.add("客户支付");
            titleList.add("银行补贴");
            titleList.add("权益结算总价");
            titleList.add("房型");
            titleList.add("床型");
            titleList.add("早餐");
            titleList.add("酒店确认号");
            titleList.add("备注");
            titleList.add("权益已使用次数");
            titleList.add("权益剩余次数");
            titleList.add("点数");
            titleList.add("产品ID");
            titleList.add("套餐名称");
        }
        return titleList;
    }
}
