// 公共数据接口,暂未处理，方便后期分类

import http from '../util/http';
//商品列表
export const selectGoodsPageList = data => {
    return http.post('/goods/selectGoodsPageList', data)
}

//商品新增
export const goodsInsert = data => {
    return http.post('/goods/insert', data)
}
//商品修改
export const goodsUpdate = data => {
    return http.post('/goods/update', data)
}

//商品上架、下架修改
export const goodsUpdateStatus = data => {
    return http.post('/goods/status', data)
}
//商品上架、下架修改
export const updateGoodsStatus = data => {
    return http.post('/goods/updateGoodsStatus', data)
}
export const getBlockRule = data => {
    return http.post('/blockRule/generateBlockRule', data)
}
// 根据block规则，获取日期，week。。。
export const getBlockParams = data => {
    return http.post('/blockRule/editBlockRule', data)
}
//产品组列表查询
export const getSelectGroupPageList = data => {
    return http.post('/productGroup/selectPage', data)
}

//商品使用规则
export const getGoodsClauseList = data => {
    return http.post('/productGroup/selectPage', data)
}
//根据Id复制商品
export const copyGoodsById = data => {
    return http.get(`/goods/copy/${data.goodsId}`, {})
}
//根据Id查找商品
export const selectById = data => {
    return http.get(`/goods/selectById/${data.id}`, {})
}
//根据goodsId查找商品销售价
export const selectGoodsPriceListByGoodsId = data => {
    return http.post(`/goodsPrice/list`, data)
}
//根据goodsId更新商品销售价
export const saveGoodsPriceByGoodsId = data => {
    return http.post(`/goodsPrice/save`, data)
}
//根据goodsId查找商品使用限制
export const selectGoodsClauseListByGoodsId = data => {
    return http.post(`/goodsClause/list`, data)
}
//根据goodsId批量更新商品使用限制
export const saveGoodsClauseListByGoodsId = data => {
    return http.post(`/goodsClause/saveBatch`, data)
}
//根据goodsId查找商品详情
export const selectGoodsDetailByGoodsId = data => {
    return http.post(`/goodsDetail/get/${data}`, {})
}
//保存商品详情
export const saveGoodsDetail = data => {
    return http.post(`/goodsDetail/save`, data)
}

//------------------渠道-----------------------
//查找验证方式
export const getAuthType = data => {
    return http.get(`/salesChannel/authType`, {})
}
//查找所有渠道
export const selectAllChannel = data => {
    return http.get(`/salesChannel/selectAll`, {})
}
//查询商品渠道详情
export const selectGoodsChannel = data => {
    return http.post(`/goodsChannel/selectGoodsChannel`,data)
}
// // 获取节假日
// export const getFestivalList = data => {
//     return http.post('/SysHolidayConfig/list', data)
// }
export const getFestivalList = data => {
    return http.post('/SysHolidayConfig/holiday2BlockRule', data)
}


// 获取资源类型
export const getServiceTypeAll = data => {
    return http.post('/sysService/selectSysServiceList', data)
}
// 获取权益类型列表
export const getGiftTypeAll = data => {
    return http.post('/gift/selectGiftList', data)
}
//新增产品组接口
export const productGroupSave = data => {
    return http.post('/productGroup/save', data)
}

//查找所有字典表
export const getAllSysDict = data => {
    return http.post('/goods/getAllSysDict', data)
}

//查找产品组
export const productGroupSelectPage = data => {
    return http.post('/productGroup/selectPage', data)
}

//删除产品组
export const productGroupDelete = data => {
    return http.post('/productGroup/delete', data)
}

//编辑产品组接口
export const productGroupUpdate = data => {
    return http.post('/productGroup/update', data)
}

//产品组详情查询接口
export const productGroupDetail = data => {
    return http.post('/productGroup/groupDetail', data)
}

//复制产品组接口
export const productGroupCopy = data => {
    return http.post('/productGroup/copyGroup', data)
}

//产品组添加产品接口
export const productGroupAddProduct = data => {
    return http.post('/productGroup/groupAddProduct', data)
}

//产品组移除产品接口
export const groupDelProduct = data => {
    return http.post('/productGroup/groupDelProduct', data)
}

//产品组编辑产品接口
export const groupEditProduct = data => {
    return http.post('/productGroup/groupEditProduct', data)
}

//根据goodsId查找商品详情
export const groupfindOneById = data => {
    return http.get(`/productGroup/findOneById/${data}`, {})
}
//查询商品下面的产品组信息
export const selectGoodsGroup = data => {
    return http.post('/productGroup/selectGoodsGroup', data)
}

//查询产品组的资源类型
export const selectGroupService = data => {
    return http.post('/productGroup/selectGroupService', data)
}


//查询产品组资源类型的商户
export const selectShopByGroupService = data => {
    return http.post('/productGroup/selectShopByGroupService', data)
}
//添加产品组block
export const addGroupBlock = data => {
    return http.post('/productGroup/addGroupBlock', data)
}
//添加产品组block
export const editGroupBlock = data => {
    return http.post('/productGroup/editGroupBlock', data)
}

//查询产品组资源类型的商户
export const getAllCity = data => {
    return http.post('/city/selectCityList', data)
}

//查询渠道佣金
export const getSalesChannel = data => {
    return http.post('/goods/getSalesChannel', data)
}

//查询渠道佣金
export const selectGoodsList = data => {
    return http.post('/goods/selectGoodsList', {})
}


//查询渠道佣金
export const selectGoodsGroupById = data => {
    return http.post('/productGroup/selectGoodsGroupById', data)
}

// 更新文件接口
export const updateFile = data => {
    return http.post('/sysFile/merge', data)
}
// 查询商品网站设置
export const selectGoodsPortalSetting = data => {
    return http.get(`/GoodsPortalSetting/get/${data.id}`)
}
// 新增商品网站设置
export const insertGoodsPortalSetting = data => {
    return http.post('/GoodsPortalSetting/add', data)
}
// 更新商品网站设置
export const updateGoodsPortalSetting = data => {
    return http.post('/GoodsPortalSetting/update', data)
}

//查询预约支付金额列表
export const selectBookBasePaymentList = data => {
    return http.post('/bookBasePayment/selectList',data)
}
//预约支付金额弹窗转换
export const convertPayment = data => {
    return http.post('/bookBasePayment/convertPayment',data)
}
//根据商户类型查询资源类型
export const selectListByShopType = data => {
    return http.post('/sysService/selectListByShopType',data)
}
//根据商户类型查询权益类型
export const selectGiftByShopType = data => {
    return http.post('/gift/selectGiftByShopType',data)
}
//检测该产品组是否存在预约支付的产品(true存在，false不存在)
export const checkGroupNeedPay = data => {
    return http.post('/bookBasePayment/checkGroupNeedPay',data)
}