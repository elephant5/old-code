package com.colourfulchina.pangu.taishang.api.enums;

/**
 * 图片类型
 */
public enum FileTypeEnums {
    HOTEL_FILE("hotel.file","酒店探索章节图片"),
    HOTEL_PIC("hotel.pic","酒店图片"),
    PROJECT_FILE("project.file","项目文件"),
    SHOP_CONTRACT("shop_contract","商户合同"),
    SHOP_ITEM_PIC("shop.item.pic","商户规格图片"),
    SHOP_ITEM_PRICE_CONTRACT("shop.item.price.contract","商户规格价格合同"),
    SHOP_ITEM_SETTLE_CONTRACT("shop.item.settle.contract","商户规格结算规则合同"),
    SHOP_PIC("shop.pic","商户图片"),
    TICKET_FILE("ticket.file","工单文件"),
    TICKET_REPLY_FILE("ticket.reply.file","星期日"),
    ;
    private String code;
    private String name;

    FileTypeEnums(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getNameByCode(String code){
        for (FileTypeEnums weekEnums : FileTypeEnums.values()) {
            if (weekEnums.code.equals(code)){
                return weekEnums.name;
            }
        }
        return null;
    }
}
