import {fromJS} from 'immutable';
import {createReducer} from 'redux-immutablejs';
import {getObjReducer,getArrayReducer} from '../../util/reducer';
import {
    //商品
    GET_SELECTGOODSPAGELIST_SUCCESS,
    GET_AUTHTYPELIST_SUCCESS,
    GET_CHANNELSELECTALL_SUCCESS,
    GET_SELECTGOODSCHANNEL_SUCCESS,
    GET_SELECTBYID_SUCCESS,
    GET_GOODS_PRICE_LIST_SELECTBYGOODSID_SUCCESS,
    SAVE_GOODS_PRICE_SUCCESS,
    GET_GOODS_CLAUSE_LIST_SELECTBYGOODSID_SUCCESS,
    SAVE_GOODS_CLAUSE_LIST_SUCCESS,
    GET_GOODS_DETAIL_SELECTBYID_SUCCESS,
    SAVE_GOODS_DETAIL_SUCCESS,
    GET_GOODSINSERT_SUCCESS,
    GET_GOODSUPDATESTATUS_SUCCESS,
    GET_FESTIVALLIST_SUCCESS,
    GET_BLOCKRULE_SUCCESS,
    GET_BLOCKPARAMS_SUCCESS,
    GET_SERVICETYPE_SUCCESS,
    GET_GIFTTYPE_SUCCESS,
    GET_PRODUCTGROUP_SUCCESS,
    GET_GETALLSYSDICT_SUCCESS,
    GET_PRODUCTGROUPSELECTPAGE_SUCCESS,
    GET_PRODUCTGROUPGROUPDETAIL_SUCCESS,
    GET_PRODUCTGROUPDELETE_SUCCESS,
    GET_PRODUCTGROUPUPDATE_SUCCESS,
    GET_GOODSUPDATE_SUCCESS,
    GET_GETALLCITY_SUCCESS,
    GET_UPDATEGOODSSTATUS_SUCCESS,
    GET_GETSALESCHANNEL_SUCCESS,
    GET_ADDGROUPBLOCK_SUCCESS,
    GET_EDITGROUPBLOCK_SUCCESS,
    GET_SELECTGOODSLIST_SUCCESS,
    GET_SELECTGOODSGROUPBYID_SUCCESS,
    UPDATE_FILE_SUCCESS,
    SELECT_GOODS_PORTAL_SETTING_SUCCESS,
    INSERT_GOODS_PORTAL_SETTING_SUCCESS,
    UPDATE_GOODS_PORTAL_SETTING_SUCCESS,
    GET_BOOKBASEPAYMENTLIST_SUCCESS,
    GET_CONVERTPAYMENT_SUCCESS,
    GET_SELECTLISTBYSHOPTYPE_SUCCESS,
    GET_SELECTGIFTBYSHOPTYPE_SUCCESS,
    GET_CHECKGROUPNEEDPAY_SUCCESS,
} from './action';

const initailState = fromJS({
    goodsInfo:{},
    goodsList: {},
    authTypeList: {},
    channelAllList: {},
    goodsChannelList: {},
    goodsPriceList:[],
    goodsPrice:{},
    goodsClauseList:[],
    goodsDetail:{},
    festivalList: {},
    giftTypeAll:[],
    serviceTypeAll: [],
    blockRule: {},        //根据日期，week，生成block规则
    blockParams: {},      //根据block规则，获取日期，week。。。  
    productGroup:{},
    sysDict:[],//字典表的集合，所有的都可以查询
    productGroupList:[], //产品组列表
    groupDetail:{},
    groupDelete:{},
    productGroupUpdate:{},
    goodsInfoUpdate:{},
    cityList:[],
    updateGoodsStatus:{},
    salesChannel:{},
    editBlockRuleList:[],
    allGoodsList:[],
    selectGoodsGroupById:[],
    goodsPortalSetting:{},
    bankLogoList:[],
    file: {},             // 更新文件
    selectBookBasePaymentListRes: {},
    convertPaymentRes: {},
    selectListByShopTypeRes: {},
    selectGiftByShopTypeRes: {},
    checkGroupNeedPayRes: {},
})
const goods = createReducer(initailState, {

    [GET_SELECTGOODSPAGELIST_SUCCESS]: (state, {payload}) => {
        return state.set('goodsList', getObjReducer(state, payload))
    },

    [GET_AUTHTYPELIST_SUCCESS]: (state, {payload}) => {
        return state.set('authTypeList', getObjReducer(state, payload))
    },
    [GET_CHANNELSELECTALL_SUCCESS]: (state, {payload}) => {
        return state.set('channelList', getObjReducer(state, payload))
    },
    [GET_SELECTGOODSCHANNEL_SUCCESS]: (state, {payload}) => {
        return state.set('goodsChannelList', getObjReducer(state, payload))
    },  
    [GET_SELECTBYID_SUCCESS]: (state, {payload}) => {
        return state.set('goodsInfo', getObjReducer(state, payload))
    }, 
    [GET_GOODS_PRICE_LIST_SELECTBYGOODSID_SUCCESS]: (state, {payload}) => {
        return state.set('goodsPriceList', getObjReducer(state, payload))
    }, 
    [SAVE_GOODS_PRICE_SUCCESS]: (state, {payload}) => {
        return state.set('goodsPrice', getObjReducer(state, payload))
    }, 
    [GET_GOODS_CLAUSE_LIST_SELECTBYGOODSID_SUCCESS]: (state, {payload}) => {
        return state.set('goodsClauseList', getObjReducer(state, payload))
    }, 
    [SAVE_GOODS_CLAUSE_LIST_SUCCESS]: (state, {payload}) => {
        return state.set('goodsClauseList', getObjReducer(state, payload))
    }, 
    [GET_GOODS_DETAIL_SELECTBYID_SUCCESS]: (state, {payload}) => {
        return state.set('goodsDetail', getObjReducer(state, payload))
    }, 
    [SAVE_GOODS_DETAIL_SUCCESS]: (state, {payload}) => {
        return state.set('goodsDetail', getObjReducer(state, payload))
    }, 
    [GET_GOODSINSERT_SUCCESS]: (state, {payload}) => {
        return state.set('goodsInfo', getObjReducer(state, payload))
    },
    [GET_GOODSUPDATE_SUCCESS]: (state, {payload}) => {
        return state.set('goodsInfoUpdate', getObjReducer(state, payload))
    },
    [GET_FESTIVALLIST_SUCCESS]: (state, {payload}) => {
        return state.set('festivalList', getObjReducer(state, payload))
    },
    [GET_BLOCKRULE_SUCCESS]: (state, {payload}) => {
        return state.set('blockRule', getObjReducer(state, payload))
    },
    [GET_BLOCKPARAMS_SUCCESS]: (state, {payload}) => {
        return state.set('blockParams', getObjReducer(state, payload))
    },
    [GET_SERVICETYPE_SUCCESS]: (state, {payload}) => {
        return state.set('serviceTypeAll', getObjReducer(state, payload))
    },
    [GET_GIFTTYPE_SUCCESS]: (state, {payload}) => {
        return state.set('giftTypeAll', getObjReducer(state, payload))
    },
    [GET_PRODUCTGROUP_SUCCESS]: (state, {payload}) => {
        return state.set('productGroup', getObjReducer(state, payload))
    },
    [GET_GETALLSYSDICT_SUCCESS]: (state, {payload}) => {
        return state.set('sysDict', getObjReducer(state, payload))
    },
    [GET_PRODUCTGROUPSELECTPAGE_SUCCESS]: (state, {payload}) => {
        return state.set('productGroupList', getObjReducer(state, payload))
    },
    [GET_PRODUCTGROUPGROUPDETAIL_SUCCESS]: (state, {payload}) => {
        return state.set('groupDetail', getObjReducer(state, payload))
    },
    // [GET_PRODUCTGROUPGROUPDETAIL_SUCCESS]: (state, {payload}) => {
    //     return state.set('groupDetail', getObjReducer(state, payload))
    // },
    [GET_PRODUCTGROUPDELETE_SUCCESS]: (state, {payload}) => {
        return state.set('groupDelete', getObjReducer(state, payload))
    },
    [GET_PRODUCTGROUPUPDATE_SUCCESS]: (state, {payload}) => {
        return state.set('productGroupUpdate', getObjReducer(state, payload))
    },
    [GET_GETALLCITY_SUCCESS]: (state, {payload}) => {
        return state.set('cityList', getObjReducer(state, payload))
    },
    [GET_UPDATEGOODSSTATUS_SUCCESS]: (state, {payload}) => {
        return state.set('updateGoodsStatus', getObjReducer(state, payload))
    },
    [GET_GETSALESCHANNEL_SUCCESS]: (state, {payload}) => {
        return state.set('salesChannel', getObjReducer(state, payload))
    },
    [GET_ADDGROUPBLOCK_SUCCESS]: (state, {payload}) => {
        return state.set('blockRuleList', getObjReducer(state, payload))
    },
    [GET_EDITGROUPBLOCK_SUCCESS]: (state, {payload}) => {
        return state.set('editBlockRuleList', getObjReducer(state, payload))
    },
    [GET_SELECTGOODSLIST_SUCCESS]: (state, {payload}) => {
        return state.set('allGoodsList', getObjReducer(state, payload))
    },
    [GET_SELECTGOODSGROUPBYID_SUCCESS]: (state, {payload}) => {
        return state.set('selectGoodsGroupById', getObjReducer(state, payload))
    },
    [UPDATE_FILE_SUCCESS]: (state, {payload}) => {
        return state.set('file', getObjReducer(state, payload))
    },
    [SELECT_GOODS_PORTAL_SETTING_SUCCESS]: (state, {payload}) => {
        return state.set('goodsPortalSetting', getObjReducer(state, payload))
    },
    [INSERT_GOODS_PORTAL_SETTING_SUCCESS]: (state, {payload}) => {
        return state.set('goodsPortalSetting', getObjReducer(state, payload))
    },
    [UPDATE_GOODS_PORTAL_SETTING_SUCCESS]: (state, {payload}) => {
        return state.set('goodsPortalSetting', getObjReducer(state, payload))
    },
    [GET_BOOKBASEPAYMENTLIST_SUCCESS]: (state, {payload}) => {
        return state.set('selectBookBasePaymentListRes', getObjReducer(state, payload))
    },
    [GET_CONVERTPAYMENT_SUCCESS]: (state, {payload}) => {
        return state.set('convertPaymentRes', getObjReducer(state, payload))
    },
    [GET_SELECTLISTBYSHOPTYPE_SUCCESS]: (state, {payload}) => {
        return state.set('selectListByShopTypeRes', getObjReducer(state, payload))
    },
    [GET_SELECTGIFTBYSHOPTYPE_SUCCESS]: (state, {payload}) => {
        return state.set('selectGiftByShopTypeRes', getObjReducer(state, payload))
    },
    [GET_CHECKGROUPNEEDPAY_SUCCESS]: (state, {payload}) => {
        return state.set('checkGroupNeedPayRes', getObjReducer(state, payload))
    },
})
export default goods;