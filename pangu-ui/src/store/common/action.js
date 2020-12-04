import * as api from '../../api/common';

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

// 获取服务类型操作接口
export const GET_SERVICELIST = 'GET_SERVICELIST';
export const GET_SERVICELIST_PENDING = 'GET_SERVICELIST_PENDING';
export const GET_SERVICELIST_SUCCESS = 'GET_SERVICELIST_SUCCESS';
export const GET_SERVICELIST_FAILURE = 'GET_SERVICELIST_FAILURE';
export const getServiceList = params => {
    return {
        type: GET_SERVICELIST,
        payload: api.getServiceList(params)
    }
}

//字典列表
export const GET_DICTLIST = 'GET_DICTLIST';
export const GET_DICTLIST_SUCCESS = 'GET_DICTLIST_SUCCESS';
export const GET_DICTLIST_FAILURE = 'GET_DICTLIST_FAILURE';
export const getDictList = params => {
    return {
        type: GET_DICTLIST,
        payload: api.getDictListByType(params)
    }
}

//根据label和type返回字典对象
export const GET_DICTBYTYPE = 'GET_DICTBYTYPE';
export const GET_DICTBYTYPE_SUCCESS = 'GET_DICTBYTYPE_SUCCESS';
export const GET_DICTBYTYPE_FAILURE = 'GET_DICTBYTYPE_FAILURE';
export const selectDictByType = params => {
    return {
        type: GET_DICTBYTYPE,
        payload: api.selectDictByType(params)
    }
}

//添加字典
export const ADD_DICT = 'ADD_DICT';
export const ADD_DICT_SUCCESS = 'ADD_DICT_SUCCESS';
export const ADD_DICT_FAILURE = 'ADD_DICT_FAILURE';
export const addDict = params => {
    return {
        type: ADD_DICT,
        payload: api.addDict(params)
    }
}
//查询已经上传的文件列表
export const GET_UPLOADEDFILE = 'GET_UPLOADEDFILE';
export const GET_UPLOADEDFILE_PENDING = 'GET_UPLOADEDFILE_PENDING';
export const GET_UPLOADEDFILE_SUCCESS = 'GET_UPLOADEDFILE_SUCCESS';
export const GET_UPLOADEDFILE_FAILURE = 'GET_UPLOADEDFILE_FAILURE';
export const getUploadedFile = params => {
    return {
        type: GET_UPLOADEDFILE,
        payload: api.getUploadedFile(params)
    }
}
//查询已经上传的产品价格文件列表
export const GET_UPLOADEDPRICE = 'GET_UPLOADEDPRICE';
export const GET_UPLOADEDPRICE_PENDING = 'GET_UPLOADEDPRICE_PENDING';
export const GET_UPLOADEDPRICE_SUCCESS = 'GET_UPLOADEDPRICE_SUCCESS';
export const GET_UPLOADEDPRICE_FAILURE = 'GET_UPLOADEDPRICE_FAILURE';
export const getUploadedPrice = params => {
    return {
        type: GET_UPLOADEDPRICE,
        payload: api.getUploadedFile(params)
    }
}
//查询已经上传的结算规则文件列表
export const GET_UPLOADEDSETTLE = 'GET_UPLOADEDSETTLE';
export const GET_UPLOADEDSETTLE_PENDING = 'GET_UPLOADEDSETTLE_PENDING';
export const GET_UPLOADEDSETTLE_SUCCESS = 'GET_UPLOADEDSETTLE_SUCCESS';
export const GET_UPLOADEDSETTLE_FAILURE = 'GET_UPLOADEDSETTLE_FAILURE';
export const getUploadedSettle = params => {
    return {
        type: GET_UPLOADEDSETTLE,
        payload: api.getUploadedFile(params)
    }
}
// 刪除文件
export const DELETE_FILE = 'DELETE_FILE';
export const DELETE_FILE_PENDING = 'DELETE_FILE_PENDING';
export const DELETE_FILE_SUCCESS = 'DELETE_FILE_SUCCESS';
export const DELETE_FILE_FAILURE = 'DELETE_FILE_FAILURE';
export const deleteFile = params => {
    return {
        type: DELETE_FILE,
        payload: api.deleteFile(params)
    }
}

// 刪除文件
export const GET_GETSHOPTYPESERVICE = 'GET_GETSHOPTYPESERVICE';
export const GET_GETSHOPTYPESERVICE_SUCCESS = 'GET_GETSHOPTYPESERVICE_SUCCESS';
export const getShopTypeService = params => {
    return {
        type: GET_GETSHOPTYPESERVICE,
        payload: api.getShopTypeService(params)
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