import * as api from '../../api/goods';
//商品搜索
export const GET_SELECTGOODSPAGELIST = 'GET_SELECTGOODSPAGELIST';
export const GET_SELECTGOODSPAGELIST_SUCCESS = 'GET_SELECTGOODSPAGELIST_SUCCESS';
export const selectGoodsPageList = params => {
    return {
        type: GET_SELECTGOODSPAGELIST,
        payload: api.selectGoodsPageList(params)
    }
}


export const GET_AUTHTYPELIST = 'GET_AUTHTYPELIST';
export const GET_AUTHTYPELIST_SUCCESS = 'GET_AUTHTYPELIST_SUCCESS';
export const authTypeList = params => {
    return {
        type: GET_AUTHTYPELIST,
        payload: api.getAuthType(params)
    }
}
export const GET_CHANNELSELECTALL = 'GET_CHANNELSELECTALL';
export const GET_CHANNELSELECTALL_SUCCESS = 'GET_CHANNELSELECTALL_SUCCESS';
export const channelSelectAll = params => {
    return {
        type: GET_CHANNELSELECTALL,
        payload: api.selectAllChannel(params)
    }
}

export const GET_SELECTGOODSCHANNEL = 'GET_SELECTGOODSCHANNEL';
export const GET_SELECTGOODSCHANNEL_SUCCESS = 'GET_SELECTGOODSCHANNEL_SUCCESS';
export const selectGoodsChannel = params => {
    return{
        type: GET_SELECTGOODSCHANNEL,
        payload: api.selectGoodsChannel(params)
    }
}


export const GET_GOODSINSERT = 'GET_GOODSINSERT';
export const GET_GOODSINSERT_SUCCESS = 'GET_GOODSINSERT_SUCCESS';
export const GET_GOODSINSERT_PENDING = 'GET_GOODSINSERT_PENDING';
export const GET_GOODSINSERT_FAILURE = 'GET_GOODSINSERT_FAILURE';
export const goodsInsert = params => {
    return {
        type: GET_GOODSINSERT,
        payload: api.goodsInsert(params)
    }
}


export const GET_GOODSUPDATESTATUS = 'GET_GOODSUPDATESTATUS';
export const GET_GOODSUPDATESTATUS_SUCCESS = 'GET_GOODSUPDATESTATUS_SUCCESS';
export const GET_GOODSUPDATESTATUS_PENDING = 'GET_GOODSUPDATESTATUS_PENDING';
export const GET_GOODSUPDATESTATUS_FAILURE = 'GET_GOODSUPDATESTATUS_FAILURE';
export const goodsUpdateStatus = params => {
    return {
        type: GET_GOODSUPDATESTATUS,
        payload: api.goodsUpdateStatus(params)
    }
}
export const GET_UPDATEGOODSSTATUS = 'GET_UPDATEGOODSSTATUS';
export const GET_UPDATEGOODSSTATUS_SUCCESS = 'GET_UPDATEGOODSSTATUS_SUCCESS';
export const GET_UPDATEGOODSSTATUS_PENDING = 'GET_UPDATEGOODSSTATUS_PENDING';
export const GET_UPDATEGOODSSTATUS_FAILURE = 'GET_UPDATEGOODSSTATUS_FAILURE';
export const updateGoodsStatus = params => {
    return {
        type: GET_UPDATEGOODSSTATUS,
        payload: api.updateGoodsStatus(params)
    }
}


export const GET_COPYGOODSBYID = 'GET_COPYGOODSBYID';
export const GET_COPYGOODSBYID_SUCCESS = 'GET_COPYGOODSBYID_SUCCESS';
export const GET_COPYGOODSBYID_PENDING = 'GET_COPYGOODSBYID_PENDING';
export const GET_COPYGOODSBYID_FAILURE = 'GET_COPYGOODSBYID_FAILURE';
export const copyGoodsById = params => {
    return {
        type: GET_COPYGOODSBYID,
        payload: api.copyGoodsById(params)
    }
}
export const GET_SELECTBYID = 'GET_SELECTBYID';
export const GET_SELECTBYID_SUCCESS = 'GET_SELECTBYID_SUCCESS';
export const selectById = params => {
    return {
        type: GET_SELECTBYID,
        payload: api.selectById(params)
    }
}

export const GET_GOODS_PRICE_LIST_SELECTBYGOODSID = 'GET_GOODS_PRICE_LIST_SELECTBYGOODSID';
export const GET_GOODS_PRICE_LIST_SELECTBYGOODSID_SUCCESS = 'GET_GOODS_PRICE_LIST_SELECTBYGOODSID_SUCCESS';
export const selectGoodsPriceListByGoodsId = params => {
    return {
        type: GET_GOODS_PRICE_LIST_SELECTBYGOODSID,
        payload: api.selectGoodsPriceListByGoodsId(params)
    }
}

export const SAVE_GOODS_PRICE = 'SAVE_GOODS_PRICE';
export const SAVE_GOODS_PRICE_SUCCESS = 'SAVE_GOODS_PRICE_SUCCESS';
export const saveGoodsPriceByGoodsId = params => {
    return {
        type: SAVE_GOODS_PRICE,
        payload: api.saveGoodsPriceByGoodsId(params)
    }
}

export const GET_GOODS_CLAUSE_LIST_SELECTBYGOODSID = 'GET_GOODS_CLAUSE_LIST_SELECTBYGOODSID';
export const GET_GOODS_CLAUSE_LIST_SELECTBYGOODSID_SUCCESS = 'GET_GOODS_CLAUSE_LIST_SELECTBYGOODSID_SUCCESS';
export const selectGoodsClauseListByGoodsId = params => {
    return {
        type: GET_GOODS_CLAUSE_LIST_SELECTBYGOODSID,
        payload: api.selectGoodsClauseListByGoodsId(params)
    }
}

export const SAVE_GOODS_CLAUSE_LIST = 'SAVE_GOODS_CLAUSE_LIST';
export const SAVE_GOODS_CLAUSE_LIST_SUCCESS = 'SAVE_GOODS_CLAUSE_LIST_SUCCESS';
export const saveGoodsClauseListByGoodsId = params => {
    return {
        type: SAVE_GOODS_CLAUSE_LIST,
        payload: api.saveGoodsClauseListByGoodsId(params)
    }
}

export const GET_GOODS_DETAIL_SELECTBYID = 'GET_GOODS_DETAIL_SELECTBYID';
export const GET_GOODS_DETAIL_SELECTBYID_SUCCESS = 'GET_GOODS_DETAIL_SELECTBYID_SUCCESS';
export const selectGoodsDetailByGoodsId = params => {
    return {
        type: GET_GOODS_DETAIL_SELECTBYID,
        payload: api.selectGoodsDetailByGoodsId(params)
    }
}
export const SAVE_GOODS_DETAIL = 'SAVE_GOODS_DETAIL';
export const SAVE_GOODS_DETAIL_SUCCESS = 'SAVE_GOODS_DETAIL_SUCCESS';
export const saveGoodsDetail = params => {
    return {
        type: SAVE_GOODS_DETAIL,
        payload: api.saveGoodsDetail(params)
    }
}
export const GET_GOODSUPDATE_PENDING = 'GET_GOODSUPDATE_PENDING';
export const GET_GOODSUPDATE_FAILURE = 'GET_GOODSUPDATE_FAILURE';
export const GET_GOODSUPDATE = 'GET_GOODSUPDATE';
export const GET_GOODSUPDATE_SUCCESS = 'GET_GOODSUPDATE_SUCCESS';
export const goodsUpdate = params => {
    return {
        type: GET_GOODSUPDATE,
        payload: api.goodsUpdate(params)
    }
}
// 获取block节假日
export const GET_FESTIVALLIST = 'GET_FESTIVALLIST';
export const GET_FESTIVALLIST_PENDING = 'GET_FESTIVALLIST_PENDING';
export const GET_FESTIVALLIST_SUCCESS = 'GET_FESTIVALLIST_SUCCESS';
export const GET_FESTIVALLIST_FAILURE = 'GET_FESTIVALLIST_FAILURE';
export const getFestivalList = params => {
    return {
        type: GET_FESTIVALLIST,
        payload: api.getFestivalList(params)
    }
}

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

export const GET_GIFTTYPE = 'GET_GIFTTYPE';
export const GET_GIFTTYPE_PENDING = 'GET_GIFTTYPE_PENDING';
export const GET_GIFTTYPE_SUCCESS = 'GET_GIFTTYPE_SUCCESS';
export const GET_GIFTTYPE_FAILURE = 'GET_GIFTTYPE_FAILURE';
export const getGiftTypeAll = params => {
    return {
        type: GET_GIFTTYPE,
        payload: api.getGiftTypeAll(params)
    }
}


export const GET_SERVICETYPE = 'GET_SERVICETYPE';
export const GET_SERVICETYPE_PENDING = 'GET_SERVICETYPE_PENDING';
export const GET_SERVICETYPE_SUCCESS = 'GET_SERVICETYPE_SUCCESS';
export const GET_SERVICETYPE_FAILURE = 'GET_SERVICETYPE_FAILURE';
export const getServiceTypeAll = params => {
    return {
        type: GET_SERVICETYPE,
        payload: api.getServiceTypeAll(params)
    }
}

export const GET_PRODUCTGROUP = 'GET_PRODUCTGROUP';
export const GET_PRODUCTGROUP_PENDING = 'GET_PRODUCTGROUP_PENDING';
export const GET_PRODUCTGROUP_SUCCESS = 'GET_PRODUCTGROUP_SUCCESS';
export const GET_PRODUCTGROUP_FAILURE = 'GET_PRODUCTGROUP_FAILURE';
export const productGroupSave = params => {
    return {
        type: GET_PRODUCTGROUP,
        payload: api.productGroupSave(params)
    }
}

export const GET_GETALLSYSDICT = 'GET_GETALLSYSDICT';
export const GET_GETALLSYSDICT_SUCCESS = 'GET_GETALLSYSDICT_SUCCESS';
export const getAllSysDict = params => {
    return {
        type: GET_GETALLSYSDICT,
        payload: api.getAllSysDict(params)
    }
}


export const GET_PRODUCTGROUPSELECTPAGE = 'GET_PRODUCTGROUPSELECTPAGE';
export const GET_PRODUCTGROUPSELECTPAGEPENDING = 'GET_PRODUCTGROUPSELECTPAGEPENDING';
export const GET_PRODUCTGROUPSELECTPAGE_SUCCESS = 'GET_PRODUCTGROUPSELECTPAGE_SUCCESS';
export const GET_PRODUCTGROUPSELECTPAGE_FAILURE = 'GET_PRODUCTGROUPSELECTPAGE_FAILURE';
export const selectGoodsGroup = params => {
    return {
        type: GET_PRODUCTGROUPSELECTPAGE,
        payload: api.selectGoodsGroup(params)
    }
}

export const GET_PRODUCTGROUPGROUPDETAIL = 'GET_PRODUCTGROUPGROUPDETAIL';
export const GET_PRODUCTGROUPSGROUPDETAIL_PENDING = 'GET_PRODUCTGROUPSGROUPDETAIL_PENDING';
export const GET_PRODUCTGROUPGROUPDETAIL_SUCCESS = 'GET_PRODUCTGROUPGROUPDETAIL_SUCCESS';
export const GET_PRODUCTGROUPGROUPDETAIL_FAILURE = 'GET_PRODUCTGROUPGROUPDETAIL_FAILURE';
export const groupfindOneById = params => {
    return { 
        type: GET_PRODUCTGROUPGROUPDETAIL,
        payload: api.groupfindOneById(params)
    }
}

export const GET_GROUPDELPRODUCT = 'GET_GROUPDELPRODUCT';
export const GET_GROUPDELPRODUCT_PENDING = 'GET_GROUPDELPRODUCT_PENDING';
export const GET_GROUPDELPRODUCT_SUCCESS = 'GET_GROUPDELPRODUCT_SUCCESS';
export const GET_GROUPDELPRODUCT_FAILURE = 'GET_GROUPDELPRODUCT_FAILURE';
export const groupDelProduct = params => {
    return { 
        type: GET_GROUPDELPRODUCT,
        payload: api.groupDelProduct(params)
    }
}


export const GET_PRODUCTGROUPDELETE = 'GET_PRODUCTGROUPDELETE';
export const GET_PRODUCTGROUPDELETE_PENDING = 'GET_PRODUCTGROUPDELETE_PENDING';
export const GET_PRODUCTGROUPDELETE_SUCCESS = 'GET_PRODUCTGROUPDELETE_SUCCESS';
export const GET_PRODUCTGROUPDELETE_FAILURE = 'GET_PRODUCTGROUPDELETE_FAILURE';
export const productGroupDelete = params => {
    return { 
        type: GET_PRODUCTGROUPDELETE,
        payload: api.productGroupDelete(params)
    }
}

export const GET_PRODUCTGROUPUPDATE = 'GET_PRODUCTGROUPUPDATE';
export const GET_PRODUCTGROUPUPDATE_PENDING = 'GET_PRODUCTGROUPUPDATE_PENDING';
export const GET_PRODUCTGROUPUPDATE_SUCCESS = 'GET_PRODUCTGROUPUPDATE_SUCCESS';
export const GET_PRODUCTGROUPUPDATE_FAILURE = 'GET_PRODUCTGROUPUPDATE_FAILURE';
export const productGroupUpdate = params => {
    return { 
        type: GET_PRODUCTGROUPUPDATE,
        payload: api.productGroupUpdate(params)
    }
}


export const GET_GETALLCITY = 'GET_GETALLCITY';
export const GET_GETALLCITY_SUCCESS = 'GET_GETALLCITY_SUCCESS';
export const getAllCity = params => {
    return { 
        type: GET_GETALLCITY,
        payload: api.getAllCity(params)
    }
}
export const GET_GETSALESCHANNEL = 'GET_GETSALESCHANNEL';
export const GET_GETSALESCHANNEL_SUCCESS = 'GET_GETSALESCHANNEL_SUCCESS';
export const getSalesChannel = params => {
    return { 
        type: GET_GETSALESCHANNEL,
        payload: api.getSalesChannel(params)
    }
}


export const GET_ADDGROUPBLOCK = 'GET_ADDGROUPBLOCK';
export const GET_ADDGROUPBLOCK_SUCCESS = 'GET_ADDGROUPBLOCK_SUCCESS';
export const addGroupBlock = params => {
    return { 
        type: GET_ADDGROUPBLOCK,
        payload: api.addGroupBlock(params)
    }
}
export const GET_EDITGROUPBLOCK = 'GET_EDITGROUPBLOCK';
export const GET_EDITGROUPBLOCK_SUCCESS = 'GET_EDITGROUPBLOCK_SUCCESS';
export const editGroupBlock = params => {
    return { 
        type: GET_EDITGROUPBLOCK,
        payload: api.editGroupBlock(params)
    }
}

export const GET_SELECTGOODSLIST = 'GET_SELECTGOODSLIST';
export const GET_SELECTGOODSLIST_SUCCESS = 'GET_SELECTGOODSLIST_SUCCESS';
export const selectGoodsList = params => {
    return { 
        type: GET_SELECTGOODSLIST,
        payload: api.selectGoodsList(params)
    }
}



export const GET_SELECTGOODSGROUPBYID = 'GET_SELECTGOODSGROUPBYID';
export const GET_SELECTGOODSGROUPBYID_SUCCESS = 'GET_SELECTGOODSGROUPBYID_SUCCESS';
export const selectGoodsGroupById = params => {
    return { 
        type: GET_SELECTGOODSGROUPBYID,
        payload: api.selectGoodsGroupById(params)
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
// 查询商品网站设置
export const SELECT_GOODS_PORTAL_SETTING = 'SELECT_GOODS_PORTAL_SETTING';
export const SELECT_GOODS_PORTAL_SETTING_PENDING = 'SELECT_GOODS_PORTAL_SETTING_PENDING';
export const SELECT_GOODS_PORTAL_SETTING_SUCCESS = 'SELECT_GOODS_PORTAL_SETTING_SUCCESS';
export const SELECT_GOODS_PORTAL_SETTING_FAILURE = 'SELECT_GOODS_PORTAL_SETTING_FAILURE';
export const selectGoodsPortalSetting = params => {
    return {
        type: SELECT_GOODS_PORTAL_SETTING,
        payload: api.selectGoodsPortalSetting(params)
    }
}
// 新增商品网站设置
export const INSERT_GOODS_PORTAL_SETTING = 'INSERT_GOODS_PORTAL_SETTING';
export const INSERT_GOODS_PORTAL_SETTING_PENDING = 'INSERT_GOODS_PORTAL_SETTING_PENDING';
export const INSERT_GOODS_PORTAL_SETTING_SUCCESS = 'INSERT_GOODS_PORTAL_SETTING_SUCCESS';
export const INSERT_GOODS_PORTAL_SETTING_FAILURE = 'INSERT_GOODS_PORTAL_SETTING_FAILURE';
export const insertGoodsPortalSetting = params => {
    return {
        type: INSERT_GOODS_PORTAL_SETTING,
        payload: api.insertGoodsPortalSetting(params)
    }
}
// 更新商品网站设置
export const UPDATE_GOODS_PORTAL_SETTING = 'UPDATE_GOODS_PORTAL_SETTING';
export const UPDATE_GOODS_PORTAL_SETTING_PENDING = 'UPDATE_GOODS_PORTAL_SETTING_PENDING';
export const UPDATE_GOODS_PORTAL_SETTING_SUCCESS = 'UPDATE_GOODS_PORTAL_SETTING_SUCCESS';
export const UPDATE_GOODS_PORTAL_SETTING_FAILURE = 'UPDATE_GOODS_PORTAL_SETTING_FAILURE';
export const updateGoodsPortalSetting = params => {
    return {
        type: UPDATE_GOODS_PORTAL_SETTING,
        payload: api.updateGoodsPortalSetting(params)
    }
}
// 查询预约支付金额列表
export const GET_BOOKBASEPAYMENTLIST = 'GET_BOOKBASEPAYMENTLIST';
export const GET_BOOKBASEPAYMENTLIST_SUCCESS = 'GET_BOOKBASEPAYMENTLIST_SUCCESS';
export const selectBookBasePaymentList = params => {
    return {
        type: GET_BOOKBASEPAYMENTLIST,
        payload: api.selectBookBasePaymentList(params)
    }
}
// 预约支付金额弹窗转换
export const GET_CONVERTPAYMENT = 'GET_CONVERTPAYMENT';
export const GET_CONVERTPAYMENT_SUCCESS = 'GET_CONVERTPAYMENT_SUCCESS';
export const convertPayment = params => {
    return {
        type: GET_CONVERTPAYMENT,
        payload: api.convertPayment(params)
    }
}
// 根据商户类型查询资源类型
export const GET_SELECTLISTBYSHOPTYPE = 'GET_SELECTLISTBYSHOPTYPE';
export const GET_SELECTLISTBYSHOPTYPE_SUCCESS = 'GET_SELECTLISTBYSHOPTYPE_SUCCESS';
export const selectListByShopType = params => {
    return {
        type: GET_SELECTLISTBYSHOPTYPE,
        payload: api.selectListByShopType(params)
    }
}
// 根据商户类型查询权益类型
export const GET_SELECTGIFTBYSHOPTYPE = 'GET_SELECTGIFTBYSHOPTYPE';
export const GET_SELECTGIFTBYSHOPTYPE_SUCCESS = 'GET_SELECTGIFTBYSHOPTYPE_SUCCESS';
export const selectGiftByShopType = params => {
    return {
        type: GET_SELECTGIFTBYSHOPTYPE,
        payload: api.selectGiftByShopType(params)
    }
}
// 检测该产品组是否存在预约支付的产品(true存在，false不存在)
export const GET_CHECKGROUPNEEDPAY = 'GET_CHECKGROUPNEEDPAY';
export const GET_CHECKGROUPNEEDPAY_SUCCESS = 'GET_CHECKGROUPNEEDPAY_SUCCESS';
export const checkGroupNeedPay = params => {
    return {
        type: GET_CHECKGROUPNEEDPAY,
        payload: api.checkGroupNeedPay(params)
    }
}