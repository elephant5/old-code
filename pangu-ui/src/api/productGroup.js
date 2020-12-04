// 公共数据接口,暂未处理，方便后期分类

import http from '../util/http';

//产品组列表查询
export const getSelectGroupPageList = data => {
    return http.post('/productGroup/selectPage', data)
}

//产品组详情查询
export const getProductGroupDetail = data => {
    return http.post('/productGroup/groupDetail', data)
}
//产品组的所属产品条件查询
export const queryGroupProduct = data => {
    return http.post('/productGroupProduct/queryGroupProduct', data)
}

//产品组对应的产品停售
export const updateStatus = data => {
    return http.post('/productGroupProduct/updateStatus', data)
}

//产品组对应的产品停售
export const updateRecommend = data => {
    return http.post('/productGroupProduct/updateRecommend', data)
}
//查找所有渠道
export const selectAllChannel = data => {
    return http.get(`/salesChannel/selectAll`, {})
}
//产品组的所属产品条件查询
export const groupAddProduct = data => {
    return http.post('/productGroup/groupAddProduct', data)
}
export const groupDelProduct = data => {
    return http.post('/productGroup/groupDelProduct', data)
}

export const copyGroup = data => {
    return http.post('/productGroup/copyGroup', data)
}

//产品组编辑产品接口
export const groupEditProduct = data => {
    return http.post('/productGroup/groupEditProduct', data)
}

//产品组编辑产品接口
export const saveSort = data => {
    return http.post('/productGroup/saveSort', data)
}
//产品组产品导出
export const groupProductExport = data => {
    return http.post('/productGroupProduct/export', data)
}

//根据产品组id查询产品组信息
export const findByGroupId = data => {
    return http.post('/productGroup/findByGroupId', data)
}
