import * as api from '../../api/product';

// 产品列表
export const GET_SELECTPRODUCTPAGELIST = 'GET_SELECTPRODUCTPAGELIST';
export const GET_SELECTPRODUCTPAGELIST_PENDING = 'GET_SELECTPRODUCTPAGELIST_PENDING';
export const GET_SELECTPRODUCTPAGELIST_SUCCESS = 'GET_SELECTPRODUCTPAGELIST_SUCCESS';
export const GET_SELECTPRODUCTPAGELIST_FAILURE = 'GET_SELECTPRODUCTPAGELIST_FAILURE';
export const getSelectProductPageList = params => {
    return {
        type: GET_SELECTPRODUCTPAGELIST,
        payload: api.getSelectProductPageList(params)
    }
}
// 产品列表
export const GET_SELECTPAGE = 'GET_SELECTPAGE';
export const GET_SELECTPAGE_SUCCESS = 'GET_SELECTPAGE_SUCCESS';
export const selectPage = params => {
    return {
        type: GET_SELECTPAGE,
        payload: api.selectPage(params)
    }
}

//导出产品
export const GET_SELECTEXPORT = 'GET_SELECTEXPORT';
export const GET_SELECTEXPORT_SUCCESS = 'GET_SELECTEXPORT_SUCCESS';
export const selectExport = params => {
    return {
        type: GET_SELECTEXPORT,
        payload: api.selectExport(params)
    }
}