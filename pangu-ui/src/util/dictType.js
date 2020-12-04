export const BANK_TYPE = "bank_type";// "银行名称"
export const SALES_CHANNEL_TYPE = "sales_channel_type"//"销售渠道"
export const SALES_WAY_TYPE = "sales_way_type"//销售方式"
export const SETTLEMENT_METHOD_TYPE = "settlement_method_type"//结算方式"
export const INVOICE_NODE_TYPE = "invoice_node_type"//开票节点"
export const INVOICE_OBJ_TYPE = "invoice_obj_type"//开票对象"
export const CHANNEL_STATUS_TYPE = "channel_status_type"//渠道状态"
export const SERVICE_TYPE = "service_type"//资源类型"
export const GIFT_TYPE = "gift_type"//权益类型"
export const USE_LIMIT_TYPE = "use_limit_type"//使用限制"
export const AUTH_TYPE = "auth_type"//验证方式"
export const ACCOM_BLOCK = "accom_block"//住宿block
export const BUFFET_BLOCK = "buffet_block"//餐饮block
export const SPA_GYM_BLOCK = "spa_gym_block"//spa和健身block
export const ACT_CODE_TAG = "act_code_tag"//激活码的标签
export const EXPRESS_TYPE ="express_type"//快递公司
export const getLabelByType = (value, list) => {
    var label = "";
    list.map((item) => {
        if (value === item.value) {
            label = item.label;
        }
    })
    return label;
}


export const resourColor = [
    {type:'accom',color:"#108ee9"},
    { type:"buffet",color:"#87d068"},
    {type: "trip",color: "#2db7f5"},
    {type: "coupon",color:  "#f50"},
    {type: "exchange",color: "#33CC"},
    {type: "gym",color: "#f16d7a"},
    {type: "spa",color: "#e1622f"},
    {type: "other",color: "#9B30FF"}];

export   const serviceColor = [
        {name:"机场出行",type:'car',color:"blue"},
        {name:"贵宾厅", type:"lounge",color:"red"},
        {name:"水疗",type: "spa",color: "volcano"},
        {name:"健身",type: "gym",color:  "gold"},
        {name:"礼品兑换",type: "exchange",color: "lime"},
        {name:"出行券",type: "trip_cpn",color: "green"},
        {name:"电影券",type: "movie_cpn",color: "cyan"},
        {name:"甜品券",type: "sweet_cpn",color: "magenta"},
        {name:"视频VIP券",type: "video_cpn",color: "purple"},
        {name:"手机充值券",type: "charge_cpn",color: "geekblue"},
        {name:"咖啡券",type: "coffee_cpn",color: "pink"},
        {name:"面包券",type: "bakery_cpn",color: "blue"},
        {name:"外卖券",type: "takeout_cpn",color: "red"},
        {name:"音乐券",type: "music_cpn",color: "volcano"},
        {name:"银商卡券",type: "suppermarket_cpn",color: "gold"},
        {name:"口腔保健券",type: "tooth_cpn",color: "green"},
        {name:"下午茶",type: "tea",color: "salmon"},
        {name:"自助餐",type: "buffet",color: "green"},
        {name:"定制套餐",type: "setmenu",color: "geekblue"},
        {name:"住宿",type: "accom",color: "blue"},
        {name:"其他",type: "other",color: "purple"},
        {name:"单杯茶饮",type: "drink",color: "red"},
        {name:"零食券",type: "snacks_cpn",color: "magenta"},
        {name:"其他卡券",type: "others_cpn",color: "magenta"},
        {name:"实物券",type: "object_cpn",color: "pink"},
        {name:"绿通就医",type: "medical",color: "red"}

    ];  
    
export   const shopServiceColor = [
    {name:"机场出行",type:'car',color:"blue"},
    {name:"贵宾厅", type:"lounge",color:"red"},
    {name:"水疗",type: "spa",color: "volcano"},
    {name:"健身",type: "gym",color:  "gold"},
    {name:"礼品兑换",type: "exchange",color: "lime"},
    {name:"出行券",type: "trip_cpn",color: "green"},
    {name:"电影券",type: "movie_cpn",color: "cyan"},
    {name:"甜品券",type: "sweet_cpn",color: "magenta"},
    {name:"视频VIP券",type: "video_cpn",color: "purple"},
    {name:"手机充值券",type: "charge_cpn",color: "geekblue"},
    {name:"咖啡券",type: "coffee_cpn",color: "pink"},
    {name:"面包券",type: "bakery_cpn",color: "blue"},
    {name:"外卖券",type: "takeout_cpn",color: "red"},
    {name:"音乐券",type: "music_cpn",color: "volcano"},
    {name:"银商卡券",type: "suppermarket_cpn",color: "gold"},
    {name:"口腔保健券",type: "tooth_cpn",color: "green"},
    {name:"下午茶",type: "tea",color: "salmon"},
    {name:"餐饮",type: "buffet",color: "green"},
    {name:"定制套餐",type: "setmenu",color: "geekblue"},
    {name:"住宿",type: "accom",color: "blue"},
    {name:"其他",type: "other",color: "purple"},
    {name:"单杯茶饮",type: "drink",color: "red"},
    {name:"零食券",type: "snacks_cpn",color: "magenta"},
    {name:"其他卡券",type: "others_cpn",color: "magenta"},
    {name:"实物券",type: "object_cpn",color: "pink"},
    {name:"绿通就医",type: "medical",color: "red"}
];  
    export   const giftColor = [      
    {name:"二免一",type:'2F1',color:"blue"},
    {name:"三免一",type:'3F1',color:"red"},
    {name:"买一赠一",type:'B1F1',color:"volcano"},
    {name:"五折",type:'D5',color:"gold"},
    {name:"单免",type:'F1',color:"lime"},
    {name:"双免",type:'F2',color:"cyan"},
    {name:"两天一晚",type:'N1',color:"magenta"},
    {name:"三天两晚",type:'N2',color:"purple"},
    {name:"四天三晚",type:'N3',color:"geekblue"},
    {name:"五天四晚",type:'N4',color:"geekblue"},
    {name:"开放住宿",type:'NX',color:"purple"}];

export   const ReservOrderStatus = [      
{name:"尚未预订",type:'0',color:"#f50"},
{name:"预订成功",type:'1',color:"#87d068"},
{name:"预订取消",type:'2',color:"#33CC"},
{name:"预订失败",type:'3',color:"#e1622f"},
{name:"预定中",type:'4',color:"#108ee9"}];

export   const payOrderStatus = [      
    {name:"退款中",type:0,color:"#f50"},
    {name:"待支付",type:1,color:"#87d068"},
    {name:"已支付",type:2,color:"#33CC"},
    {name:"已退款",type:3,color:"#e1622f"},
    {name:"已使用",type:4,color:"#108ee9"},
    {name:"已使用",type:5,color:"#108ee9"}];
    
export   const ReservCodeStatus = [      
    {name:"未使用",type:'0',color:"#f50"},
    {name:"已过期",type:'1',color:"#87d068"},
    {name:"已使用",type:'2',color:"#33CC"},
    {name:"已作废",type:'3',color:"#e1622f"},
    ];