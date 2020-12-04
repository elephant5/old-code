// 公共数据接口,暂未处理，方便后期分类

import http from '../util/http';

//渠道列表
export const getSalesChannelList = data => {
    return http.post('/salesChannel/page', data)
}

//启用停用渠道
export const updateSalesChannelStatus = data => {
    return http.post('/salesChannel/enable', data)
}

//新增渠道
export const add = data => {
    return http.post('/salesChannel/add', data)
}

//更新渠道
export const update = data => {
    return http.post('/salesChannel/update', data)
}

//根据id查找单个渠道
export const get = id => {
    return http.get(`/salesChannel/get/${id}`, id)
}






