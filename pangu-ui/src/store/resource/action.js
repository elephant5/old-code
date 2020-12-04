import * as api from '../../api/resource';

// 酒店列表
export const GET_SELECTPAGELIST = 'GET_SELECTPAGELIST';
export const GET_SELECTPAGELIST_PENDING = 'GET_SELECTPAGELIST_PENDING';
export const GET_SELECTPAGELIST_SUCCESS = 'GET_SELECTPAGELIST_SUCCESS';
export const GET_SELECTPAGELIST_FAILURE = 'GET_SELECTPAGELIST_FAILURE';
export const getSelectPageList = params => {
    return {
        type: GET_SELECTPAGELIST,
        payload: api.getSelectPageList(params)
    }
}
// 酒店详情
export const GET_HOTELDETAIL = 'GET_HOTELDETAIL';
export const GET_HOTELDETAIL_PENDING = 'GET_HOTELDETAIL_PENDING';
export const GET_HOTELDETAIL_SUCCESS = 'GET_HOTELDETAIL_SUCCESS';
export const GET_HOTELDETAIL_FAILURE = 'GET_HOTELDETAIL_FAILURE';
export const getHotelDetail = params => {
    return {
        type: GET_HOTELDETAIL,
        payload: api.getHotelDetail(params)
    }
}
// 酒店关联商户
export const GET_HOTELSSHOP = 'GET_HOTELSSHOP';
export const GET_HOTELSSHOP_PENDING = 'GET_HOTELSSHOP_PENDING';
export const GET_HOTELSSHOP_SUCCESS = 'GET_HOTELSSHOP_SUCCESS';
export const GET_HOTELSSHOP_FAILURE = 'GET_HOTELSSHOP_FAILURE';
export const getHotelsShop = params => {
    return {
        type: GET_HOTELSSHOP,
        payload: api.getHotelsShop(params)
    }
}
// 酒店操作日志
export const GET_OPERATIONLOG = 'GET_OPERATIONLOG';
export const GET_OPERATIONLOG_PENDING = 'GET_OPERATIONLOG_PENDING';
export const GET_OPERATIONLOG_SUCCESS = 'GET_OPERATIONLOG_SUCCESS';
export const GET_OPERATIONLOG_FAILURE = 'GET_OPERATIONLOG_FAILURE';
export const getOperationLog = params => {
    return {
        type: GET_OPERATIONLOG,
        payload: api.getOperationLog(params)
    }
}
// 酒店探索章节删除接口
export const DELETE_HOTELPORTAL = 'DELETE_HOTELPORTAL';
export const DELETE_HOTELPORTAL_PENDING = 'DELETE_HOTELPORTAL_PENDING';
export const DELETE_HOTELPORTAL_SUCCESS = 'DELETE_HOTELPORTAL_SUCCESS';
export const DELETE_HOTELPORTAL_FAILURE = 'DELETE_HOTELPORTAL_FAILURE';
export const deleteHotelPortal = params => {
    return {
        type: DELETE_HOTELPORTAL,
        payload: api.deleteHotelPortal(params)
    }
}
// 酒店探索章节增加/修改接口
export const UPDATE_HOTELPORTAL = 'UPDATE_HOTELPORTAL';
export const UPDATE_HOTELPORTAL_PENDING = 'UPDATE_HOTELPORTAL_PENDING';
export const UPDATE_HOTELPORTAL_SUCCESS = 'UPDATE_HOTELPORTAL_SUCCESS';
export const UPDATE_HOTELPORTAL_FAILURE = 'UPDATE_HOTELPORTAL_FAILURE';
export const updateHotelPortal = params => {
    return {
        type: UPDATE_HOTELPORTAL,
        payload: api.updateHotelPortal(params)
    }
}
// 酒店基本信息修改接口
export const UPDATE_HOTELBASEMSG = 'UPDATE_HOTELBASEMSG';
export const UPDATE_HOTELBASEMSG_PENDING = 'UPDATE_HOTELBASEMSG_PENDING';
export const UPDATE_HOTELBASEMSG_SUCCESS = 'UPDATE_HOTELBASEMSG_SUCCESS';
export const UPDATE_HOTELBASEMSG_FAILURE = 'UPDATE_HOTELBASEMSG_FAILURE';
export const updateHotelBaseMsg = params => {
    return {
        type: UPDATE_HOTELBASEMSG,
        payload: api.updateHotelBaseMsg(params)
    }
}
// 酒店基本信息修改接口
export const UPDATE_HOTELNAME = 'UPDATE_HOTELNAME';
export const UPDATE_HOTELNAME_PENDING = 'UPDATE_HOTELNAME_PENDING';
export const UPDATE_HOTELNAME_SUCCESS = 'UPDATE_HOTELNAME_SUCCESS';
export const UPDATE_HOTELNAME_FAILURE = 'UPDATE_HOTELNAME_FAILURE';
export const updHotelName = params => {
    return {
        type: UPDATE_HOTELNAME,
        payload: api.updHotelName(params)
    }
}
// 获取商户列表
export const GET_SELECTSHOPPAGELIST = 'GET_SELECTSHOPPAGELIST';
export const GET_SELECTSHOPPAGELIST_PENDING = 'GET_SELECTSHOPPAGELIST_PENDING';
export const GET_SELECTSHOPPAGELIST_SUCCESS = 'GET_SELECTSHOPPAGELIST_SUCCESS';
export const GET_SELECTSHOPPAGELIST_FAILURE = 'GET_SELECTSHOPPAGELIST_FAILURE';
export const getSelectShopPageList = params => {
    return {
        type: GET_SELECTSHOPPAGELIST,
        payload: api.getSelectShopPageList(params)
    }
}
// 新增商户第一步
export const ADD_SHOPONE = 'ADD_SHOPONE';
export const ADD_SHOPONE_PENDING = 'ADD_SHOPONE_PENDING';
export const ADD_SHOPONE_SUCCESS = 'ADD_SHOPONE_SUCCESS';
export const ADD_SHOPONE_FAILURE = 'ADD_SHOPONE_FAILURE';
export const addShopOne = params => {
    return {
        type: ADD_SHOPONE,
        payload: api.addShopOne(params)
    }
}
// 商户详情
export const GET_SHOPDETAIL = 'GET_SHOPDETAIL';
export const GET_SHOPDETAIL_PENDING = 'GET_SHOPDETAIL_PENDING';
export const GET_SHOPDETAIL_SUCCESS = 'GET_SHOPDETAIL_SUCCESS';
export const GET_SHOPDETAIL_FAILURE = 'GET_SHOPDETAIL_FAILURE';
export const getShopDetail = params => {
    return {
        type: GET_SHOPDETAIL,
        payload: api.getShopDetail(params)
    }
}
// 商户基本信息修改
export const UPDATE_SHOPBASEMSG = 'UPDATE_SHOPBASEMSG';
export const UPDATE_SHOPBASEMSG_PENDING = 'UPDATE_SHOPBASEMSG_PENDING';
export const UPDATE_SHOPBASEMSG_SUCCESS = 'UPDATE_SHOPBASEMSG_SUCCESS';
export const UPDATE_SHOPBASEMSG_FAILURE = 'UPDATE_SHOPBASEMSG_FAILURE';
export const updateShopBaseMsg = params => {
    return {
        type: UPDATE_SHOPBASEMSG,
        payload: api.updateShopBaseMsg(params)
    }
}
// 商户协议信息修改
export const UPDATE_SHOPPROTOCOL = 'UPDATE_SHOPPROTOCOL';
export const UPDATE_SHOPPROTOCOL_PENDING = 'UPDATE_SHOPPROTOCOL_PENDING';
export const UPDATE_SHOPPROTOCOL_SUCCESS = 'UPDATE_SHOPPROTOCOL_SUCCESS';
export const UPDATE_SHOPPROTOCOL_FAILURE = 'UPDATE_SHOPPROTOCOL_FAILURE';
export const updateShopProtocol = params => {
    return {
        type: UPDATE_SHOPPROTOCOL,
        payload: api.updateShopProtocol(params)
    }
}
// 商户操作日志列表
export const GET_SHOPLOGLIST = 'GET_SHOPLOGLIST';
export const GET_SHOPLOGLIST_PENDING = 'GET_SHOPLOGLIST_PENDING';
export const GET_SHOPLOGLIST_SUCCESS = 'GET_SHOPLOGLIST_SUCCESS';
export const GET_SHOPLOGLIST_FAILURE = 'GET_SHOPLOGLIST_FAILURE';
export const getShopLogList = params => {
    return {
        type: GET_SHOPLOGLIST,
        payload: api.getShopLogList(params)
    }
}
// 商户资源添加规格
export const ADD_SIZE = 'ADD_SIZE';
export const ADD_SIZE_PENDING = 'ADD_SIZE_PENDING';
export const ADD_SIZE_SUCCESS = 'ADD_SIZE_SUCCESS';
export const ADD_SIZE_FAILURE = 'ADD_SIZE_FAILURE';
export const addSize = params => {
    return {
        type: ADD_SIZE,
        payload: api.addSize(params)
    }
}
// 商户资源更新规格
export const UPDATE_SIZE = 'UPDATE_SIZE';
export const UPDATE_SIZE_PENDING = 'UPDATE_SIZE_PENDING';
export const UPDATE_SIZE_SUCCESS = 'UPDATE_SIZE_SUCCESS';
export const UPDATE_SIZE_FAILURE = 'UPDATE_SIZE_FAILURE';
export const updateSize = params => {
    return {
        type: UPDATE_SIZE,
        payload: api.updateSize(params)
    }
}
// 商户资源删除规格
export const DELETE_SIZE = 'DELETE_SIZE';
export const DELETE_SIZE_PENDING = 'DELETE_SIZE_PENDING';
export const DELETE_SIZE_SUCCESS = 'DELETE_SIZE_SUCCESS';
export const DELETE_SIZE_FAILURE = 'DELETE_SIZE_FAILURE';
export const deleteSize = params => {
    return {
        type: DELETE_SIZE,
        payload: api.deleteSize(params)
    }
}
// 查询规格净价规则列表
export const GET_PRICERULE = 'GET_PRICERULE';
export const GET_PRICERULE_PENDING = 'GET_PRICERULE_PENDING';
export const GET_PRICERULE_SUCCESS = 'GET_PRICERULE_SUCCESS';
export const GET_PRICERULE_FAILURE = 'GET_PRICERULE_FAILURE';
export const getPriceRule = params => {
    return {
        type: GET_PRICERULE,
        payload: api.getPriceRule(params)
    }
}
// 查询规格净价规则列表
export const GET_SETTLERULE = 'GET_SETTLERULE';
export const GET_SETTLERULE_PENDING = 'GET_SETTLERULE_PENDING';
export const GET_SETTLERULE_SUCCESS = 'GET_SETTLERULE_SUCCESS';
export const GET_SETTLERULE_FAILURE = 'GET_SETTLERULE_FAILURE';
export const getSettleRule = params => {
    return {
        type: GET_SETTLERULE,
        payload: api.getSettleRule(params)
    }
}
// 产品价格保存
export const UPDATE_PRICE = 'UPDATE_PRICE';
export const UPDATE_PRICE_PENDING = 'UPDATE_PRICE_PENDING';
export const UPDATE_PRICE_SUCCESS = 'UPDATE_PRICE_SUCCESS';
export const UPDATE_PRICE_FAILURE = 'UPDATE_PRICE_FAILURE';
export const updatePrice = params => {
    return {
        type: UPDATE_PRICE,
        payload: api.updatePrice(params)
    }
}
// 资源block规格列表
export const GET_BLOCKLIST = 'GET_BLOCKLIST';
export const GET_BLOCKLIST_PENDING = 'GET_BLOCKLIST_PENDING';
export const GET_BLOCKLIST_SUCCESS = 'GET_BLOCKLIST_SUCCESS';
export const GET_BLOCKLIST_FAILURE = 'GET_BLOCKLIST_FAILURE';
export const getBlockList = params => {
    return {
        type: GET_BLOCKLIST,
        payload: api.getBlockList(params)
    }
}
// 资源block规格保存
export const UPDATE_BLOCK = 'UPDATE_BLOCK';
export const UPDATE_BLOCK_PENDING = 'UPDATE_BLOCK_PENDING';
export const UPDATE_BLOCK_SUCCESS = 'UPDATE_BLOCK_SUCCESS';
export const UPDATE_BLOCK_FAILURE = 'UPDATE_BLOCK_FAILURE';
export const updateBlock = params => {
    return {
        type: UPDATE_BLOCK,
        payload: api.updateBlock(params)
    }
}
// 结算公式中文翻译
export const GET_SETTLEEXPRESS = 'GET_SETTLEEXPRESS';
export const GET_SETTLEEXPRESS_PENDING = 'GET_SETTLEEXPRESS_PENDING';
export const GET_SETTLEEXPRESS_SUCCESS = 'GET_SETTLEEXPRESS_SUCCESS';
export const GET_SETTLEEXPRESS_FAILURE = 'GET_SETTLEEXPRESS_FAILURE';
export const getSettleExpress = params => {
    return {
        type: GET_SETTLEEXPRESS,
        payload: api.getSettleExpress(params)
    }
}
// 结算规则保存
export const UPDATE_SETTLE = 'UPDATE_SETTLE';
export const UPDATE_SETTLE_PENDING = 'UPDATE_SETTLE_PENDING';
export const UPDATE_SETTLE_SUCCESS = 'UPDATE_SETTLE_SUCCESS';
export const UPDATE_SETTLE_FAILURE = 'UPDATE_SETTLE_FAILURE';
export const updateSettle = params => {
    return {
        type: UPDATE_SETTLE,
        payload: api.updateSettle(params)
    }
}
// 商户资源删除block规则
export const DELETE_BLOCK = 'DELETE_BLOCK';
export const DELETE_BLOCK_PENDING = 'DELETE_BLOCK_PENDING';
export const DELETE_BLOCK_SUCCESS = 'DELETE_BLOCK_SUCCESS';
export const DELETE_BLOCK_FAILURE = 'DELETE_BLOCK_FAILURE';
export const deleteBlock = params => {
    return {
        type: DELETE_BLOCK,
        payload: api.deleteBlock(params)
    }
}
// 商户资源删除产品价格
export const DELETE_PRICE = 'DELETE_PRICE';
export const DELETE_PRICE_PENDING = 'DELETE_PRICE_PENDING';
export const DELETE_PRICE_SUCCESS = 'DELETE_PRICE_SUCCESS';
export const DELETE_PRICE_FAILURE = 'DELETE_PRICE_FAILURE';
export const deletePrice = params => {
    return {
        type: DELETE_PRICE,
        payload: api.deletePrice(params)
    }
}
// 商户资源删除结算规则
export const DELETE_SETTLE = 'DELETE_SETTLE';
export const DELETE_SETTLE_PENDING = 'DELETE_SETTLE_PENDING';
export const DELETE_SETTLE_SUCCESS = 'DELETE_SETTLE_SUCCESS';
export const DELETE_SETTLE_FAILURE = 'DELETE_SETTLE_FAILURE';
export const deleteSettle = params => {
    return {
        type: DELETE_SETTLE,
        payload: api.deleteSettle(params)
    }
}






// 公共接口（暂未分类）

// 获取block节假日
// export const GET_FESTIVALLIST = 'GET_FESTIVALLIST';
// export const GET_FESTIVALLIST_PENDING = 'GET_FESTIVALLIST_PENDING';
// export const GET_FESTIVALLIST_SUCCESS = 'GET_FESTIVALLIST_SUCCESS';
// export const GET_FESTIVALLIST_FAILURE = 'GET_FESTIVALLIST_FAILURE';
// export const getFestivalList = params => {
//     return {
//         type: GET_FESTIVALLIST,
//         payload: api.getFestivalList(params)
//     }
// }
// 获取商户渠道
export const GET_CHANNEL = 'GET_CHANNEL';
export const GET_CHANNEL_PENDING = 'GET_CHANNEL_PENDING';
export const GET_CHANNEL_SUCCESS = 'GET_CHANNEL_SUCCESS';
export const GET_CHANNEL_FAILURE = 'GET_CHANNEL_FAILURE';
export const getChannel = params => {
    return {
        type: GET_CHANNEL,
        payload: api.getChannel(params)
    }
}

export const GET_SELECTCHANNELLISTBYSERVERTYPE = 'GET_SELECTCHANNELLISTBYSERVERTYPE';
export const GET_SELECTCHANNELLISTBYSERVERTYPE_PENDING = 'GET_SELECTCHANNELLISTBYSERVERTYPE_PENDING';
export const GET_SELECTCHANNELLISTBYSERVERTYPE_SUCCESS = 'GET_SELECTCHANNELLISTBYSERVERTYPE_SUCCESS';
export const GET_SELECTCHANNELLISTBYSERVERTYPE_FAILURE = 'GET_SELECTCHANNELLISTBYSERVERTYPE_FAILURE';
export const selectChannelListByServerType = params => {
    return {
        type: GET_SELECTCHANNELLISTBYSERVERTYPE,
        payload: api.selectChannelListByServerType(params)
    }
}


// 结算方式列表
export const GET_SETTLEMETHOD = 'GET_SETTLEMETHOD';
export const GET_SETTLEMETHOD_PENDING = 'GET_SETTLEMETHOD_PENDING';
export const GET_SETTLEMETHOD_SUCCESS = 'GET_SETTLEMETHOD_SUCCESS';
export const GET_SETTLEMETHOD_FAILURE = 'GET_SETTLEMETHOD_FAILURE';
export const getSettleMethod = params => {
    return {
        type: GET_SETTLEMETHOD,
        payload: api.getSettleMethod(params)
    }
}
// 货币类型列表
export const GET_CURRENCY = 'GET_CURRENCY';
export const GET_CURRENCY_PENDING = 'GET_CURRENCY_PENDING';
export const GET_CURRENCY_SUCCESS = 'GET_CURRENCY_SUCCESS';
export const GET_CURRENCY_FAILURE = 'GET_CURRENCY_FAILURE';
export const getCurrency = params => {
    return {
        type: GET_CURRENCY,
        payload: api.getCurrency(params)
    }
}
// 国家城市联动列表
export const GET_COUNTRYCITY = 'GET_COUNTRYCITY';
export const GET_COUNTRYCITY_PENDING = 'GET_COUNTRYCITY_PENDING';
export const GET_COUNTRYCITY_SUCCESS = 'GET_COUNTRYCITY_SUCCESS';
export const GET_COUNTRYCITY_FAILURE = 'GET_COUNTRYCITY_FAILURE';
export const getCountryCity = params => {
    return {
        type: GET_COUNTRYCITY,
        payload: api.getCountryCity(params)
    }
}
// 获取资源类型
export const GET_RESOURCETYPE = 'GET_RESOURCETYPE';
export const GET_RESOURCETYPE_PENDING = 'GET_RESOURCETYPE_PENDING';
export const GET_RESOURCETYPE_SUCCESS = 'GET_RESOURCETYPE_SUCCESS';
export const GET_RESOURCETYPE_FAILURE = 'GET_RESOURCETYPE_FAILURE';
export const getResourceType = params => {
    return {
        type: GET_RESOURCETYPE,
        payload: api.getResourceType(params)
    }
}
// 获取权益类型列表
export const GET_GIFTTYPELIST = 'GET_GIFTTYPELIST';
export const GET_GIFTTYPELIST_PENDING = 'GET_GIFTTYPELIST_PENDING';
export const GET_GIFTTYPELIST_SUCCESS = 'GET_GIFTTYPELIST_SUCCESS';
export const GET_GIFTTYPELIST_FAILURE = 'GET_GIFTTYPELIST_FAILURE';
export const getGiftTypeList = params => {
    return {
        type: GET_GIFTTYPELIST,
        payload: api.getGiftTypeList(params)
    }
}
// 获取商户类型
export const GET_SHOPTYPE = 'GET_SHOPTYPE';
export const GET_SHOPTYPE_PENDING = 'GET_SHOPTYPE_PENDING';
export const GET_SHOPTYPE_SUCCESS = 'GET_SHOPTYPE_SUCCESS';
export const GET_SHOPTYPE_FAILURE = 'GET_SHOPTYPE_FAILURE';
export const getShopType = params => {
    return {
        type: GET_SHOPTYPE,
        payload: api.getShopType(params)
    }
}

// 获取商户日历班期
export const GET_SHOPITEMSETTLEPRICE = 'GET_SHOPITEMSETTLEPRICE';
export const GET_SHOPITEMSETTLEPRICE_PENDING = 'GET_SHOPITEMSETTLEPRICE_PENDING';
export const GET_SHOPITEMSETTLEPRICE_SUCCESS = 'GET_SHOPITEMSETTLEPRICE_SUCCESS';
export const GET_SHOPITEMSETTLEPRICE_FAILURE = 'GET_SHOPITEMSETTLEPRICE_FAILURE';
export const getShopItemSettlePrice = params => {
  return {
    type: GET_SHOPITEMSETTLEPRICE,
    payload: api.getShopItemCalendar(params)
  }
}

// 根据日期，week，生成block规则
export const GET_BLOCKRULE = 'GET_BLOCKRULE';
export const GET_BLOCKRULE_PENDING = 'GET_BLOCKRULE_PENDING';
export const GET_BLOCKRULE_SUCCESS = 'GET_BLOCKRULE_SUCCESS';
export const GET_BLOCKRULE_FAILURE = 'GET_BLOCKRULE_FAILURE';
export const getBlockRule = params => {
    return {
        type: GET_BLOCKRULE,
        payload: api.getBlockRule(params)
    }
}
// 根据block规则，获取日期，week。。。
export const GET_BLOCKPARAMS = 'GET_BLOCKPARAMS';
export const GET_BLOCKPARAMS_PENDING = 'GET_BLOCKPARAMS_PENDING';
export const GET_BLOCKPARAMS_SUCCESS = 'GET_BLOCKPARAMS_SUCCESS';
export const GET_BLOCKPARAMS_FAILURE = 'GET_BLOCKPARAMS_FAILURE';
export const getBlockParams = params => {
    return {
        type: GET_BLOCKPARAMS,
        payload: api.getBlockParams(params)
    }
}

// 获取商户的资源列表
export const GET_SHOPITEMLIST = 'GET_SHOPITEMLIST';
export const GET_SHOPITEMLIST_PENDING = 'GET_SHOPITEMLIST_PENDING';
export const GET_SHOPITEMLIST_SUCCESS = 'GET_SHOPITEMLIST_SUCCESS';
export const GET_SHOPITEMLIST_FAILURE = 'GET_SHOPITEMLIST_FAILURE';
export const getShopItemList = params => {
  return {
    type: GET_SHOPITEMLIST,
    payload: api.getShopItemList(params)
  }
}

// 获取商户的某个资源。
export const GET_SHOPITEMDETAIL = 'GET_SHOPITEMDETAIL';
export const GET_SHOPITEMDETAIL_PENDING = 'GET_SHOPITEMDETAIL_PENDING';
export const GET_SHOPITEMDETAIL_SUCCESS = 'GET_SHOPITEMDETAIL_SUCCESS';
export const GET_SHOPITEMDETAIL_FAILURE = 'GET_SHOPITEMDETAIL_FAILURE';
export const getShopItemDetail = params => {
  return {
    type: GET_SHOPITEMDETAIL,
    payload: api.getShopItemDetail(params)
  }
}
// 更新文件
export const UPDATE_FILE = 'UPDATE_FILE';
export const UPDATE_FILE_PENDING = 'UPDATE_FILE_PENDING';
export const UPDATE_FILE_SUCCESS = 'UPDATE_FILE_SUCCESS';
export const UPDATE_FILE_FAILURE = 'UPDATE_FILE_FAILURE';
export const updateFile = params => {
    return {
        type: UPDATE_FILE,
        payload: api.updateFile(params)
    }
}
//获取所有城市
export const GET_GETALLCITY = 'GET_GETALLCITY';
export const GET_GETALLCITY_SUCCESS = 'GET_GETALLCITY_SUCCESS';
export const getAllCity = params => {
    return { 
        type: GET_GETALLCITY,
        payload: api.getAllCity(params)
    }
}

//获取所有城市
export const GET_SELECTBYHOTELNAMELIST = 'GET_SELECTBYHOTELNAMELIST';
export const GET_SELECTBYHOTELNAMELIST_SUCCESS = 'GET_SELECTBYHOTELNAMELIST_SUCCESS';
export const selectByHotelNameList = params => {
    return { 
        type: GET_SELECTBYHOTELNAMELIST,
        payload: api.selectByHotelNameList(params)
    }
}
export const GET_ADDSHOPDETAIL = 'GET_ADDSHOPDETAIL';
export const GET_ADDSHOPDETAIL_SUCCESS = 'GET_ADDSHOPDETAIL_SUCCESS';
export const addShopDetail = params => {
    return { 
        type: GET_SELECTBYHOTELNAMELIST,
        payload: api.addShopDetail(params)
    }
}
export const GET_SERVICEGIFTGET = 'GET_SERVICEGIFTGET';
export const GET_SERVICEGIFTGET_SUCCESS = 'GET_SERVICEGIFTGET_SUCCESS';
export const serviceGiftGet = params => {
    return { 
        type: GET_SERVICEGIFTGET,
        payload: api.serviceGiftGet(params)
    }
}
export const GET_QUERYBOOKBLOCK = 'GET_QUERYBOOKBLOCK';
export const GET_QUERYBOOKBLOCK_SUCCESS = 'GET_QUERYBOOKBLOCK_SUCCESS';
export const queryBookBlock = params => {
    return { 
        type: GET_QUERYBOOKBLOCK,
        payload: api.queryBookBlock(params)
    }
}

export const GET_QUERYOUTCODEBOOKBLOCK = 'GET_QUERYOUTCODEBOOKBLOCK';
export const GET_QUERYOUTCODEBOOKBLOCK_SUCCESS = 'GET_QUERYOUTCODEBOOKBLOCK_SUCCESS';
export const queryOutCodeBookBlock = params => {
    return { 
        type: GET_QUERYOUTCODEBOOKBLOCK,
        payload: api.queryOutCodeBookBlock(params)
    }
}

export const GET_QUERYACTCALLBOOK = 'GET_QUERYACTCALLBOOK';
export const GET_QUERYACTCALLBOOK_SUCCESS = 'GET_QUERYACTCALLBOOK_SUCCESS';
export const queryActCallBook = params => {
    return { 
        type: GET_QUERYACTCALLBOOK,
        payload: api.queryActCallBook(params)
    }
}

export const GET_QUERYOUTCALLBOOK = 'GET_QUERYOUTCALLBOOK';
export const GET_QUERYOUTCALLBOOK_SUCCESS = 'GET_QUERYOUTCALLBOOK_SUCCESS';
export const queryOutCallBook = params => {
    return { 
        type: GET_QUERYOUTCALLBOOK,
        payload: api.queryOutCallBook(params)
    }
}

export const GET_GETBLOCKREASON = 'GET_GETBLOCKREASON';
export const GET_GETBLOCKREASON_SUCCESS = 'GET_GETBLOCKREASON_SUCCESS';
export const getBlockReason = params => {
    return { 
        type: GET_GETBLOCKREASON,
        payload: api.getBlockReason(params)
    }
}
export const GET_SELECTSHOPLIST = 'GET_SELECTSHOPLIST';
export const GET_SELECTSHOPLIST_SUCCESS = 'GET_SELECTSHOPLIST_SUCCESS';
export const selectShopList = params => {
    return { 
        type: GET_SELECTSHOPLIST,
        payload: api.selectShopList(params)
    }
}

export const GET_SHOPSETTLEMSG = 'GET_SHOPSETTLEMSG';
export const GET_SHOPSETTLEMSG_SUCCESS = 'GET_SHOPSETTLEMSG_SUCCESS';
export const shopSettleMsg = params => {
    return { 
        type: GET_SHOPSETTLEMSG,
        payload: api.shopSettleMsg(params)
    }
}
export const GET_GETCITY = 'GET_GETCITY';
export const GET_GETCITY_SUCCESS = 'GET_GETCITY_SUCCESS';
export const getCity = params => {
    return { 
        type: GET_GETCITY,
        payload: api.getCity(params)
    }
}
/**根据id查询产品关系表 */
export const GET_FINDPRODUCTGROUPPRODUCTBYID = 'GET_FINDPRODUCTGROUPPRODUCTBYID';
export const GET_FINDPRODUCTGROUPPRODUCTBYID_SUCCESS = 'GET_FINDPRODUCTGROUPPRODUCTBYID_SUCCESS';
export const findProductGroupProductById = params => {
    return { 
        type: GET_FINDPRODUCTGROUPPRODUCTBYID,
        payload: api.findProductGroupProductById(params)
    }
}


