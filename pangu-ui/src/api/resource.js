import http from '../util/http';

export const getSelectPageList = data => {
    return http.post('/hotel/selectPageList', data)
}
export const getHotelDetail = data => {
    return http.post('/hotel/hotelDetail', data)
}
export const getHotelsShop = data => {
    return http.post('/hotel/hotelsShop', data)
}
export const getOperationLog = data => {
    return http.post('/hotel/selectNameLogPageList', data)
}
// 酒店探索章节删除接口
export const deleteHotelPortal = data => {
    return http.post('/hotel/delHotelPortal', data)
}
// 酒店探索章节增加/修改接口
export const updateHotelPortal = data => {
    return http.post('/hotel/optHotelPortal', data)
}
// 酒店基本信息修改接口
export const updateHotelBaseMsg = data => {
    return http.post('/hotel/updateHotelBaseMsg', data)
}
// 酒店名称修改接口
export const updHotelName = data => {
    return http.post('/hotel/updHotelName', data)
}
// 获取商户列表
export const getSelectShopPageList = data => {
    return http.post('/shop/selectShopPageList', data)
}
// 新增商户第一步
export const addShopOne = data => {
    return http.post('/shop/addShopOne', data)
}
// 商户详情
export const getShopDetail = data => {
    return http.post('/shop/shopDetail', data)
}
// 商户基本信息修改
export const updateShopBaseMsg = data => {
    return http.post('/shop/addShopBaseMsg', data)
}
// 商户协议信息修改
export const updateShopProtocol = data => {
    return http.post('/shop/addShopProtocol', data)
}
// 商户操作日志列表
export const getShopLogList = data => {
    return http.post('/shop/selectShopLogPageList', data)
}
// 商户资源添加规格
export const addSize = data => {
    return http.post('/shopItem/add', data)
}
// 商户资源更新规格
export const updateSize = data => {
    return http.post('/shopItem/update', data)
}
// 商户资源删除规格
export const deleteSize = data => {
    return http.post('/shopItem/delete', data)
}
// 查询规格净价规则列表
export const getPriceRule = data => {
    return http.post('/shopItemNetPriceRule/list', data)
}
// // 查询规格净价详情
// export const getSettleRule = data => {
//     return http.post('/shopItemSettlePriceRule/list', data)
// }
// 产品价格保存
export const updatePrice = data => {
    return http.post('/shopItemNetPriceRule/add', data)
}
// 资源block规格列表
export const getBlockList = data => {
    return http.post('/shopItem/shopItemBlockList', data)
}
// 资源block规格保存
export const updateBlock = data => {
    return http.post('/shopItem/shopItemBlockAdd', data)
}
// 资源结算规则列表
export const getSettleRule = data => {
    return http.post('/shopItemSettlePriceRule/list', data)
}
// 结算公式中文翻译
export const getSettleExpress = data => {
    return http.post('/shopItemSettlePriceRule/translateSettleExpress', data)
}
// 结算规则保存
export const updateSettle = data => {
    return http.post('/shopItemSettlePriceRule/add', data)
}
// 商户资源删除block规则
export const deleteBlock = data => {
    return http.post('/shopItem/shopItemBlockDel', data)
}
// 商户资源删除产品价格
export const deletePrice = data => {
    return http.post('/shopItemNetPriceRule/delete', data)
}
// 商户资源删除结算规则
export const deleteSettle = data => {
    return http.post('/shopItemSettlePriceRule/delete', data)
}
// // 商户资源编辑价格
// export const editPrice = data => {
//     return http.post('/shopItemNetPriceRule/update', data)
// }

//查询产品组资源类型的商户
export const getAllCity = data => {
    return http.post('/city/selectCityList', data)
}
//查询产品组资源类型的商户
export const getCity = data => {
    return http.get(`/city/get/${data}`, {})
}

// 公共接口（暂未分类）

// 国家城市联动列表
export const getCountryCity = data => {
    return http.post('/country/selectCountryCity', data)
}
// 获取商户渠道列表
export const getChannel = data => {
    return http.post('/shopChannel/selectChannelList', data)
}
// 根据资源类型查询商户渠道列表
export const selectChannelListByServerType = data => {
    return http.post('/shopChannel/selectChannelListByServerType', data)
}
// 结算方式列表
export const getSettleMethod = data => {
    return http.post('/settleMethod/selectSettleMethodList', data)
}
// 货币类型列表
export const getCurrency = data => {
    return http.post('/sysCurrency/selectCurrencyList', data)
}
// 获取资源类型
export const getResourceType = data => {
    return http.post('/sysService/selectSysServiceList', data)
}
// 获取权益类型列表
export const getGiftTypeList = data => {
    return http.post('/gift/selectGiftList', data)
}
// 获取节假日
// export const getFestivalList = data => {
//     return http.post('/SysHolidayConfig/holiday2BlockRule', data)
// }
// 获取商户类型列表
export const getShopType = data => {
    return http.post('/shopType/list', data)
}
export const addShopDetail = data => {
    return http.post('/shop/addShopDetail', data)
}

// 获取资源日历班期
export const getShopItemCalendar = data => {
  return http.post('/ShopItemSettlePrice/listShopItemSettlePrice', data)
}
// 根据日期，week，生成block规则
export const getBlockRule = data => {
    return http.post('/blockRule/generateBlockRule', data)
}
// 根据block规则，获取日期，week。。。
export const getBlockParams = data => {
    return http.post('/blockRule/editBlockRule', data)
}

// 根据商户资源ID，获取资源规格
export const getShopItemList = data => {
    return http.post('/shopItem/list', data)
}
// 获取商户某个资源
export const getShopItemDetail = data => {
  return http.get(`/shopItem/get/${data.shopItemId}`, {})
}

// 根据酒店名称模糊搜索列表
export const selectByHotelNameList = data => {
    return http.post('/hotel/selectByHotelNameList', data)
  }
// 更新文件接口
export const updateFile = data => {
    return http.post('/sysFile/merge', data)
}
// 根据code资源类型对应的权益类型
export const serviceGiftGet = data => {
    return http.post('/serviceGift/get', data)
}

// 查询整个block(已激活的码)
export const queryBookBlock = data => {
    return http.post('/blockRule/queryBookBlock', data)
}

// 查询整个block(已出库的码)
export const queryOutCodeBookBlock = data => {
    return http.post('/blockRule/queryOutCodeBookBlock', data)
}

//来电录单初步查询指定时间能否预约（商户）(已激活的码)
export const queryActCallBook = data => {
    return http.post('/blockRule/queryActCallBook',data)
}

//来电录单初步查询指定时间能否预约（商户）(已出库的码)
export const queryOutCallBook = data => {
    return http.post('/blockRule/queryOutCallBook',data)
}

// 查询关房关餐
export const getBlockReason = data => {
    return http.post('/shop/getBlockReason', data)
}

// 查询所有商户
export const selectShopList = data => {
    return http.post('/shop/selectShopList', data)
}
// 查询所有商户
export const shopSettleMsg = data => {
    return http.post('/productGroup/shopSettleMsg', data)
}
//根据id查询产品关系表
export const findProductGroupProductById = data => {
    return http.post('/productGroupProduct/findById',data)
}
