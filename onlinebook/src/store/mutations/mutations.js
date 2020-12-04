export default {
    //获取渠道
    getChannel(state,data){
        state.channel = data
    },
    //获取项目
    getPrjCode(state,data){
        state.prjCode = data
    },
    //获取激活码
    getActCode(state,data){
        state.actCode = data
    },
    //获取返回第三方地址
    getBackUrl(state,data){
        state.backUrl = data   
    },
    //用户信息
    getUserInfo(state, data){
        state.userMsg = data
    },
    //权益列表
    getGiftList(state,data){
        state.giftList = data
    },
    //获取列表信息所需入参
    getSearchList(state,data){
        state.searchList = data
    },
    //获取产品组ID
    getGroupId(state,data){
        state.groupId = data
    },
    //获取项目ID
    getGoodsId(state,data){
        state.goodsId = data
    },
    //获取激活码ID
    getUnitId(state,data){
        state.unitId = data
    },
    //获取预约开始时间
    getStartDate(state,data){
        state.startDate = data
    },
    //获取预约结束时间
    getEndDate(state,data){
        state.endDate = data
    },
    //获取产品组类型
    getSysService(state,data){
        state.sysService = data
    },
    //获取权益次数
    getSurplusTimes(state,data){
        state.surplusTimes = data
    },
    //获取出行免费次数
    getSurplusFreeTimes(state,data){
        state.surplusFreeTimes = data
    },
    //获取最小可预约天数
    getMinBookDays(state,data){
        state.minBookDays = data
    },
    //获取最大可预约天数
    getMaxBookDays(state,data){
        state.maxBookDays = data
    },
    //获取销售渠道
    getSalesChannelId(state,data){
        state.salesChannelId = data
    },
    //获取周期循环次次
    getNum(state,data){
        state.num = data
    },
    //获取周期循环类型
    getType(state,data){
        state.type = data
    },
    //获取产品组产品折扣率
    getDiscountRate(state,data){
        state.discountRate = data
    },
    //获取热门推荐产品
    getHotproductList(state,data){
        state.hotproductlist = data
    },
    //获取城市
    cityChoosed(state, data) {
        state.mycity = data
    },
    //获取公共搜索关键字
    getBuffetKeyWord(state,data){
        state.buffetKeyWord = data
    },
    //获取其他搜索关键字
    getAccomKeyWord(state,data){
        state.accomKeyWord = data
    },
    //住宿预订天数
    getBookingDay(state,data){
        state.personDay = data
    },
    //产品组资源列表(商户)
    getGiftShopList(state,data){
        state.giftShopList = data
    },
    //产品组产品列表(商户资源)
    getGiftShopProdList(state,data){
        state.giftShopProdList = data
    },
    //产品组产品ID(商户的产品ID)
    getProductGroupProductId(state,data){
        state.productGroupProductId = data
    },
    //订单来源
    getOrderForm(state, data){
        state.orderForm = data
    },
    //预约信息
    getReservOrderVo(state, data){
        state.reservOrderVomsg = data
    },
    //预约限制
    getCancelPolicy(state, data){
        state.cancelPolicy = data
    },
    //开餐时间
    getOpenTime(state, data){
        state.openTime = data
        console.log(data)
    },
    //结束时间
    getCloseTime(state, data){
        state.closeTime = data
        console.log(data)
    },
    //可预约日期
    getBookDatesList(state, data){
        state.bookDatesList = data
    }, 
    //最大预约间夜数(住宿)
    getMaxNight(state, data){
        state.maxNight = data
    }, 
    // 权益扣减次数
    getGiftNum(state, data){
        state.giftNum = data
    },
    // 预约单终端(区分微信、APP)
    getBookorderFrom(state, data){
        state.bookorderFrom = data
    },
    // 优惠券编码
    getCouponId(state, data){
        state.couponId = data
    },   
    //超级会员权益列表
    getVipGiftList(state,data){
        state.vipGiftList = data
    },
    //超级会员产品组资源列表
    getVipGiftProductList(state,data){
        state.vipGiftProductList = data
    },
    //超级会员选中当前项
    getViptypeCurronIndex(state,data){
        state.viptypeCurronIndex = data
    },
    //超级会员信息相关信息
    getMyVipmsgList(state,data){
        state.myVipmsgList = data
    },
    //预约就诊医院信息
    getHospitalMsg(state,data){
        state.hospitalMsg = data
    },
    //预约人信息
    getHospitalPerson(state,data){
        state.hospitalPerson = data
    },
}