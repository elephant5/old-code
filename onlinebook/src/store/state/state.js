export default {
    channel:String,  //渠道
    prjCode:null, //项目编码
    actCode:null,  //激活码
    userMsg:Object,  //用户信息
    backUrl:null,  //返回第三方
    mycity: '选择城市', //城市
    giftList:Object, //权益信息
    groupId:null,   //产品组ID
    goodsId:Number,  //项目ID
    unitId:null, //激活码ID
    sysService:null,  //产品类型
    surplusTimes:Number,   //权益次数
    surplusFreeTimes:Number, //出行免费次数
    minBookDays:String,   //最小可预约天数（住宿）
    maxBookDays:String,  //最大可预约天数（住宿）
    salesChannelId:Number,  //销售渠道ID
    num:Number,  //周期循环次数
    type:String,  //周期循环类型
    searchlistTime:Object, //搜索时间
    startDate:null, //预订开始时间
    endDate:null, //预订结束时间
    discountRate:null, //折扣率
    productGroupProductId:Number, //产品组产品ID  (商户的产品ID)
    hotproductList:Object, //产品推荐
    giftShopList:Object, //产品组资源列表(商户列表)
    giftShopProdList:Object, //产品组产品列表(商户资源列表)
    orderForm:String, //订单来源
    buffetKeyWord:'', //公共搜索关键字
    accomKeyWord:'', //住宿搜索关键字
    personDay:Number,//住宿预约天数
    maxNights:Number, //最大间夜数
    bookorderFrom:null, //预约单终端(区分微信、APP)
    reservOrderVomsg:Object, //预约信息
    cancelPolicy:Number,  //预约限制
    openTime:String,  //开餐时间
    closeTime:String,  //结束时间
    bookDatesList:null,  //可预约日期
    maxNight:Number,  //最大预约间夜数(住宿)
    giftNum:Number, // 权益扣减次数
    couponId:String, //优惠券编码
    vipGiftList:Object,  //超级会员权益(商户)
    vipGiftProductList:Object, //超级会员产品组信息(商户资源列表)
    viptypeCurronIndex:Number,  //超级会员当前选中
    myVipmsgList:Object, //超级会员信息相关信息(后台没有缓存，以此为准)
    hospitalMsg:null,//预约就诊医院信息
    hospitalPerson:Object, //预约人信息
}