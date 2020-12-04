package com.colourfulchina.inf.base.enums;

/**
 * 字典项类型枚举
 *
 * @Author: luck.luo
 * @Description:
 * @Date: 2019/2/28 9:25
 */
public enum SysDictTypeEnums {
    BANK_TYPE("bank_type", "银行名称"),
    SALES_CHANNEL_TYPE("sales_channel_type", "销售渠道"),
    SALES_WAY_TYPE("sales_way_type", "销售方式"),
    SETTLEMENT_METHOD_TYPE("settlement_method_type", "结算方式"),
    INVOICE_NODE_TYPE("invoice_node_type", "开票节点"),
    INVOICE_OBJ_TYPE("invoice_obj_type", "开票对象"),
    CHANNEL_STATUS_TYPE("channel_status_type", "渠道状态"),
    SERVICE_TYPE("service_type", "资源类型"),
    CLAUSE_TYPE("clause_type", "使用限制类型"),
    GIFT_TYPE("gift_type", "权益类型"),
    USE_LIMIT_TYPE("use_limit_type", "使用限制"),
    AUTH_TYPE("auth_type", "验证方式"),
    RESERV_ORDER_TYPE("reserv_order_type", "预约单来源");

    private String type;
    private String remark;

    SysDictTypeEnums(String type, String remark) {
        this.type = type;
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public String getRemark() {
        return remark;
    }

    public static SysDictTypeEnums getSysDictByType(String type) {
        for (SysDictTypeEnums dict : SysDictTypeEnums.values()) {
            if (type.equals(dict.type)) {
                return dict;
            }
        }
        return null;
    }
}
