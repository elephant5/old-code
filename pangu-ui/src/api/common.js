// 公共数据接口,暂未处理，方便后期分类

import http from '../util/http';
import httpPublic from "../util/httpPublic";

// 获取节假日
export const getFestivalList = data => {
    return http.post('/SysHolidayConfig/holiday2BlockRule', data)
}

//服务类型list
export const getServiceList = data => {
    return http.post('/sysService/selectSysServiceList', data)
}
//服务类型list
export const getShopTypeService = data => {
    return http.post(`/shopTypeService/getShopTypeService`,data)
}

//字典列表
export const getDictListByType = data => {
    return httpPublic.get(`/dict/type/${data}`, data)
}

//根据label和type返回字典对象
export const selectDictByType = data => {
    return httpPublic.post("/dict/selectByType", data)
}

//添加字典
export const addDict = data => {
    return httpPublic.post("/dict/addDict", data)
}

// 查询已经上传的文件列表
export const getUploadedFile = data => {
    return http.post('/sysFile/list', data)
}

// 刪除文件
export const deleteFile = id => {
    return http.get(`/sysFile/deleteFileByFileId/${id}`)
}

//查找所有字典表
export const getAllSysDict = data => {
    return http.post('/goods/getAllSysDict', data)
}