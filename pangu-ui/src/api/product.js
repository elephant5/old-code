// 公共数据接口,暂未处理，方便后期分类

import http from '../util/http';

//产品列表查询
export const getSelectProductPageList = data => {
    return http.post('/product/selectPage', data)
}

//产品列表查询新的
export const selectPage = data => {
    return http.post('/product/selectPackGoodsPage', data)
}

//导出产品
export const selectExport = data => {
    return http.post('/product/export', data)
}