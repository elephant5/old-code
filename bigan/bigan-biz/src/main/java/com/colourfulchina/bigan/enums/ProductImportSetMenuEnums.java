package com.colourfulchina.bigan.enums;

public class ProductImportSetMenuEnums {
    public enum accom {
        ;

        private String cellName;
        private String fieldName;

        accom(String cellName, String fieldName) {
            this.cellName = cellName;
            this.fieldName = fieldName;
        }

        public String getCellName() {
            return cellName;
        }

        public String getFieldName() {
            return fieldName;
        }
    }

    /**
     * 自助餐：城市、酒店中文名、酒店英文名、餐厅名、餐厅英文名、权益项目、权益类型、餐段、周Block、午餐开餐时间、晚餐开餐时间、地址、电话、泊车信息、儿童政策、用户名、密码
     * 下午茶：城市、酒店中文名、酒店英文名、餐厅名、餐厅英文名、权益项目、权益类型、周Block、套餐名称、下午茶开餐时间
     * 定制套餐：城市、酒店中文名、酒店英文名、餐厅名、餐厅英文名、权益项目、权益类型、周Block、套餐名称
     * 住宿：城市、酒店中文名、酒店英文名、餐厅名、权益项目、权益类型、房型、床型、餐型、适用条款、地址、电话、泊车信息、儿童政策
     */
    public enum setMenu{
        CITY("城市","hotel.city"),
        HOTEL_NAME_CN("酒店中文名","hotel.name"),
        HOTEL_NAME_EN("酒店英文名","hotel.name_en"),
        SHOP_NAME("餐厅名","shop.name"),
        SHOP_NAME_EN("餐厅英文名","shop.nameEn"),
        SERVICE("权益项目","hotel.city"),
        SALERULE("优惠政策","hotel.saleRule"),
        DISCOUNT("权益类型","hotel.city"),
        PACKAGENAME("套餐名称","hotel.packageName"),
        OPENWEEKDAY("开放周天","hotel.openWeekDay"),
        SHOPPRICE("门市价","hotel.shopPrice"),
        PRICE("价格","hotel.city"),
        CAN_RANGE("餐段","hotel.city"),
        CAN_RANGE_TYPE("餐型","hotel.city"),
        WEEKBLOCK("周block","hotel.city"),
        LUNCH_OPEN_RANGE("午餐开餐时间","hotel.lunchTime"),
        TEA_OPEN_RANGE("下午茶开餐时间","hotel.lunchTime"),
        SUPPER_OPEN_RANGE("晚餐开餐时间","hotel.dinnerTime"),
        ROOM_TYPE("房型","hotel.dinnerTime"),
        BED_TYPE("床型","hotel.dinnerTime"),
        BREAKFAST_TYPE("餐型","hotel.dinnerTime"),
        CLAUSE("适用条款","hotel.city"),
        ADDRESS("地址","hotel.city"),
        PHONE("电话","hotel.city"),
        PARKING_POLICY("泊车信息","hotel.city"),
        CHILD_POLICY("儿童政策","hotel.city"),
        ACCOUNT("用户名","hotel.city"),
        PWD("密码","hotel.city"),
        ;
        private String cellName;
        private String fieldName;

        setMenu(String cellName, String fieldName) {
            this.cellName = cellName;
            this.fieldName = fieldName;
        }

        public String getCellName() {
            return cellName;
        }

        public String getFieldName() {
            return fieldName;
        }
    }
    public enum exchange{
        ;
        private String cellName;
        private String fieldName;

        exchange(String cellName, String fieldName) {
            this.cellName = cellName;
            this.fieldName = fieldName;
        }

        public String getCellName() {
            return cellName;
        }

        public String getFieldName() {
            return fieldName;
        }
    }


}
