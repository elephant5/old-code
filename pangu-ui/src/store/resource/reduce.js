import {fromJS} from 'immutable';
import {createReducer} from 'redux-immutablejs';
import {getObjReducer} from '../../util/reducer';
import { 
        GET_SELECTPAGELIST_SUCCESS,
        GET_HOTELDETAIL_SUCCESS,
        GET_HOTELSSHOP_SUCCESS,
        GET_OPERATIONLOG_SUCCESS,
        DELETE_HOTELPORTAL_SUCCESS,
        UPDATE_HOTELPORTAL_SUCCESS,
        UPDATE_HOTELBASEMSG_SUCCESS,
        GET_SELECTSHOPPAGELIST_SUCCESS,
        ADD_SHOPONE_SUCCESS,
        GET_SHOPDETAIL_SUCCESS,
        UPDATE_SHOPBASEMSG_SUCCESS,
        UPDATE_SHOPPROTOCOL_SUCCESS,
        GET_SHOPITEMSETTLEPRICE_SUCCESS,
        GET_SHOPITEMLIST_SUCCESS,
        GET_SHOPITEMDETAIL_SUCCESS,
        GET_SHOPLOGLIST_SUCCESS,
        ADD_SIZE_SUCCESS,
        UPDATE_SIZE_SUCCESS,
        GET_PRICERULE_SUCCESS,
        GET_SETTLERULE_SUCCESS,
        UPDATE_PRICE_SUCCESS,
        GET_BLOCKLIST_SUCCESS,
        UPDATE_BLOCK_SUCCESS,
        GET_SETTLEEXPRESS_SUCCESS,
        UPDATE_SETTLE_SUCCESS,
        GET_GETBLOCKREASON_SUCCESS,
        //公共接口
        // GET_FESTIVALLIST_SUCCESS,
        GET_CHANNEL_SUCCESS,
        GET_SETTLEMETHOD_SUCCESS,
        GET_CURRENCY_SUCCESS,
        GET_SHOPTYPE_SUCCESS,
        GET_RESOURCETYPE_SUCCESS,
        GET_GIFTTYPELIST_SUCCESS,
        GET_COUNTRYCITY_SUCCESS,
        GET_BLOCKRULE_SUCCESS,
        GET_BLOCKPARAMS_SUCCESS,
        UPDATE_FILE_SUCCESS,
        GET_GETALLCITY_SUCCESS,
        GET_SELECTBYHOTELNAMELIST_SUCCESS,
        GET_ADDSHOPDETAIL_SUCCESS,
        GET_SERVICEGIFTGET_SUCCESS,
        GET_QUERYBOOKBLOCK_SUCCESS,
        GET_QUERYOUTCODEBOOKBLOCK_SUCCESS,
        GET_QUERYACTCALLBOOK_SUCCESS,
        GET_QUERYOUTCALLBOOK_SUCCESS,
        GET_SELECTSHOPLIST_SUCCESS,
        GET_SHOPSETTLEMSG_SUCCESS,
        GET_GETCITY_SUCCESS,
        GET_SELECTCHANNELLISTBYSERVERTYPE_SUCCESS,
        GET_FINDPRODUCTGROUPPRODUCTBYID_SUCCESS,
        DELETE_SIZE_SUCCESS,
    } from './action';

const initailState = fromJS({
    hotelList: {},
    hotelDetail: {},
    hotelsShop: {},
    operationLog: {},
    deleteHotelPortal: {},  //酒店探索章节删除 
    updateHotelPortal: {},  //酒店探索章节增加/修改接口
    hotelBaseMsg: {},
    merchantList: {},   //获取商户列表    
    shopOne: {},       //新增商户第一步
    shopDetail: {},     //商户详情
    shopBaseMsg: {},    // 商户基本信息修改
    shopProtocol: {},    // 商户协议信息修改
    shopItemCalendar: {},
    shopItemList: {},
    shopItemDetail: {},
    shopLogList: {},      //商户操作日志列表
    sizeInfo: {},         //商户资源添加规格
    deleteSizeRes:{},    //删除商户资源
    updateSize: {},         //商户资源更新规格
    priceRule: {},
    settleRule: {},         //结算规则列表
    updatePrice: {},        //产品价格保存
    blockList: {},
    updateBlock: {},
    settleExpress: {},
    updateSettle: {},
    city:{},
    selectChannelListByServerType:{},
    // 公共数据
    authTypeList: {},
    channelAllList: {},
    festivalList: {},    // block节假日
    countryCity: {},  // 获取国家城市
    shopType: {},       //获取商户类型
    channelList: {},    //获取商户渠道
    settleMethod: {},    // 结算方式列表
    currency: {},      //货币类型列表
    resourceType: {},   //获取资源类型
    giftTypeList: {},   //获取权益类型列表
    blockRule: {},        //根据日期，week，生成block规则
    blockParams: {},      //根据block规则，获取日期，week。。。  
    queryOutCodeBookBlock: {},//已出库激活码的可预约时间查询
    file: {},             // 更新文件  
    cityList:[]    ,
    hotelSelList:{} ,
    shopDetails:{},
    serviceGiftList:{},
    blockReasonList:[],
    shopList:[],
    ShopSettleMsgRes:[],
    findProductGroupProductByIdRes:{},
    queryActCallBookRes:{},
    queryOutCallBookRes:{},
})

const resource = createReducer(initailState, {
    [GET_SELECTPAGELIST_SUCCESS]: (state, {payload}) => {
        return state.set('hotelList', getObjReducer(state, payload))
    },
    [GET_HOTELDETAIL_SUCCESS]: (state, {payload}) => {
        return state.set('hotelDetail', getObjReducer(state, payload))
    },
    [GET_HOTELSSHOP_SUCCESS]: (state, {payload}) => {
        return state.set('hotelsShop', getObjReducer(state, payload))
    },
    [GET_OPERATIONLOG_SUCCESS]: (state, {payload}) => {
        return state.set('operationLog', getObjReducer(state, payload))
    },
    [DELETE_HOTELPORTAL_SUCCESS]: (state, {payload}) => {
        return state.set('deleteHotelPortal', getObjReducer(state, payload))
    },
    [UPDATE_HOTELPORTAL_SUCCESS]: (state, {payload}) => {
        return state.set('updateHotelPortal', getObjReducer(state, payload))
    },
    [UPDATE_HOTELBASEMSG_SUCCESS]: (state, {payload}) => {
        return state.set('hotelBaseMsg', getObjReducer(state, payload))
    },
    [GET_SELECTSHOPPAGELIST_SUCCESS]: (state, {payload}) => {
        return state.set('merchantList', getObjReducer(state, payload))
    },
    [ADD_SHOPONE_SUCCESS]: (state, {payload}) => {
        return state.set('shopOne', getObjReducer(state, payload))
    },
    [GET_SHOPDETAIL_SUCCESS]: (state, {payload}) => {
        return state.set('shopDetail', getObjReducer(state, payload))
    },
    [UPDATE_SHOPBASEMSG_SUCCESS]: (state, {payload}) => {
        return state.set('shopBaseMsg', getObjReducer(state, payload))
    },
    [UPDATE_SHOPPROTOCOL_SUCCESS]: (state, {payload}) => {
        return state.set('shopProtocol', getObjReducer(state, payload))
    },
    [GET_SHOPITEMSETTLEPRICE_SUCCESS]: (state, {payload}) => {
        return state.set('shopItemCalendar', getObjReducer(state, payload))
    },
    [GET_SHOPITEMLIST_SUCCESS]: (state, {payload}) => {
      return state.set('shopItemList', getObjReducer(state, payload))
    },
    [GET_SHOPITEMDETAIL_SUCCESS]: (state, {payload}) => {
      return state.set('shopItemDetail', getObjReducer(state, payload))
    },
    [GET_SHOPLOGLIST_SUCCESS]: (state, {payload}) => {
        return state.set('shopLogList', getObjReducer(state, payload))
    },
    [ADD_SIZE_SUCCESS]: (state, {payload}) => {
        return state.set('sizeInfo', getObjReducer(state, payload))
    },
    [UPDATE_SIZE_SUCCESS]: (state, {payload}) => {
        return state.set('updateSize', getObjReducer(state, payload))
    },
    [GET_PRICERULE_SUCCESS]: (state, {payload}) => {
        return state.set('priceRule', getObjReducer(state, payload))
    },
    [GET_SETTLERULE_SUCCESS]: (state, {payload}) => {
        return state.set('settleRule', getObjReducer(state, payload))
    },
    [UPDATE_PRICE_SUCCESS]: (state, {payload}) => {
        return state.set('updatePrice', getObjReducer(state, payload))
    },
    [GET_BLOCKLIST_SUCCESS]: (state, {payload}) => {
        return state.set('blockList', getObjReducer(state, payload))
    },
    [UPDATE_BLOCK_SUCCESS]: (state, {payload}) => {
        return state.set('updateBlock', getObjReducer(state, payload))
    },
    [GET_SETTLEEXPRESS_SUCCESS]: (state, {payload}) => {
        return state.set('settleExpress', getObjReducer(state, payload))
    },
    [UPDATE_SETTLE_SUCCESS]: (state, {payload}) => {
        return state.set('updateSettle', getObjReducer(state, payload))
    },



    //公共数据
    // [GET_FESTIVALLIST_SUCCESS]: (state, {payload}) => {
    //     return state.set('festivalList', getObjReducer(state, payload))
    // },
    [GET_CHANNEL_SUCCESS]: (state, {payload}) => {
        return state.set('channelList', getObjReducer(state, payload))
    },
    [GET_SELECTCHANNELLISTBYSERVERTYPE_SUCCESS]: (state, {payload}) => {
        return state.set('selectChannelListByServerType', getObjReducer(state, payload))
    },
    [GET_SETTLEMETHOD_SUCCESS]: (state, {payload}) => {
        return state.set('settleMethod', getObjReducer(state, payload))
    },
    [GET_CURRENCY_SUCCESS]: (state, {payload}) => {
        return state.set('currency', getObjReducer(state, payload))
    },
    [GET_SHOPTYPE_SUCCESS]: (state, {payload}) => {
        return state.set('shopType', getObjReducer(state, payload))
    },
    [GET_RESOURCETYPE_SUCCESS]: (state, {payload}) => {
        return state.set('resourceType', getObjReducer(state, payload))
    },
    [GET_GIFTTYPELIST_SUCCESS]: (state, {payload}) => {
        return state.set('giftTypeList', getObjReducer(state, payload))
    },
    [GET_COUNTRYCITY_SUCCESS]: (state, {payload}) => {
        return state.set('countryCity', getObjReducer(state, payload))
    },
    [GET_BLOCKRULE_SUCCESS]: (state, {payload}) => {
        return state.set('blockRule', getObjReducer(state, payload))
    },
    [GET_BLOCKPARAMS_SUCCESS]: (state, {payload}) => {
        return state.set('blockParams', getObjReducer(state, payload))
    },
    [UPDATE_FILE_SUCCESS]: (state, {payload}) => {
        return state.set('file', getObjReducer(state, payload))
    },
    [GET_GETALLCITY_SUCCESS]: (state, {payload}) => {
        return state.set('cityList', getObjReducer(state, payload))
    },
    [GET_SELECTBYHOTELNAMELIST_SUCCESS]: (state, {payload}) => {
        return state.set('hotelSelList', getObjReducer(state, payload))
    },
    [GET_ADDSHOPDETAIL_SUCCESS]: (state, {payload}) => {
        return state.set('shopDetails', getObjReducer(state, payload))
    },
    [GET_SERVICEGIFTGET_SUCCESS]: (state, {payload}) => {
        return state.set('serviceGiftList', getObjReducer(state, payload))
    },
    [GET_QUERYBOOKBLOCK_SUCCESS]: (state, {payload}) => {
        return state.set('queryBookBlock', getObjReducer(state, payload))
    },
    [GET_QUERYOUTCODEBOOKBLOCK_SUCCESS]: (state, {payload}) => {
        return state.set('queryOutCodeBookBlock', getObjReducer(state, payload))
    },
    [GET_QUERYACTCALLBOOK_SUCCESS]: (state, {payload}) => {
        return state.set('queryActCallBookRes', getObjReducer(state, payload))
    },
    [GET_QUERYOUTCALLBOOK_SUCCESS]: (state, {payload}) => {
        return state.set('queryOutCallBookRes', getObjReducer(state, payload))
    },
    [GET_GETBLOCKREASON_SUCCESS]: (state, {payload}) => {
        return state.set('blockReasonList', getObjReducer(state, payload))
    },
    [GET_SELECTSHOPLIST_SUCCESS]: (state, {payload}) => {
        return state.set('shopList', getObjReducer(state, payload))
    },
    [GET_SHOPSETTLEMSG_SUCCESS]: (state, {payload}) => {
        return state.set('ShopSettleMsgRes', getObjReducer(state, payload))
    },
    [GET_GETCITY_SUCCESS]: (state, {payload}) => {
        return state.set('city', getObjReducer(state, payload))
    },
    [GET_FINDPRODUCTGROUPPRODUCTBYID_SUCCESS]: (state, {payload}) => {
        return state.set('findProductGroupProductByIdRes', getObjReducer(state, payload))
    },
    [DELETE_SIZE_SUCCESS]: (state, {payload}) => {
        return state.set('deleteSizeRes', getObjReducer(state, payload))
    },
    
})

export default resource;