import * as api from '../../api/productGroup';

// 产品组列表
export const GET_SELECTGROUPPAGELIST = 'GET_SELECTGROUPPAGELIST';
export const GET_SELECTGROUPPAGELIST_PENDING = 'GET_SELECTGROUPPAGELIST_PENDING';
export const GET_SELECTGROUPPAGELIST_SUCCESS = 'GET_SELECTGROUPPAGELIST_SUCCESS';
export const GET_SELECTGROUPPAGELIST_FAILURE = 'GET_SELECTGROUPPAGELIST_FAILURE';
export const getSelectGroupPageList = params => {
    return {
        type: GET_SELECTGROUPPAGELIST,
        payload: api.getSelectGroupPageList(params)
    }
}
// 产品组详情
export const GET_PRODUCTGROUPDETAIL = 'GET_PRODUCTGROUPDETAIL';
export const GET_PRODUCTGROUPDETAIL_PENDING = 'GET_PRODUCTGROUPDETAIL_PENDING';
export const GET_PRODUCTGROUPDETAIL_SUCCESS = 'GET_PRODUCTGROUPDETAIL_SUCCESS';
export const GET_PRODUCTGROUPDETAIL_FAILURE = 'GET_PRODUCTGROUPDETAIL_FAILURE';
export const getProductGroupDetail = params => {
    return {
        type: GET_PRODUCTGROUPDETAIL,
        payload: api.getProductGroupDetail(params)
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

// 产品组的所属产品条件查询
export const GET_QUERYGROUPPRODUCT = 'GET_QUERYGROUPPRODUCT';
export const GET_QUERYGROUPPRODUCT_PENDING = 'GET_QUERYGROUPPRODUCT_PENDING';
export const GET_QUERYGROUPPRODUCT_SUCCESS = 'GET_QUERYGROUPPRODUCT_SUCCESS';
export const GET_QUERYGROUPPRODUCT_FAILURE = 'GET_QUERYGROUPPRODUCT_FAILURE';
export const queryGroupProduct = params => {
    return {
        type: GET_QUERYGROUPPRODUCT,
        payload: api.queryGroupProduct(params)
    }
}
 // 产品组的所属产品条件查询
export const GET_UPDATESTATUS = 'GET_UPDATESTATUS';

export const GET_UPDATESTATUS_SUCCESS = 'GET_UPDATESTATUS_SUCCESS';

export const updateStatus = params => {
    return {
        type: GET_UPDATESTATUS,
        payload: api.updateStatus(params)
    }
}
 // 产品组的所属产品条件查询
 export const GET_UPDATERECOMMEND = 'GET_UPDATERECOMMEND';

 export const GET_UPDATERECOMMEND_SUCCESS = 'GET_UPDATERECOMMEND_SUCCESS';
 
 export const updateRecommend = params => {
     return {
         type: GET_UPDATERECOMMEND,
         payload: api.updateRecommend(params)
     }
 }

// 产品组的所属产品条件查询
export const GET_GROUPADDPRODUCT = 'GET_GROUPADDPRODUCT';
export const GET_GROUPADDPRODUCT_PENDING = 'GET_GROUPADDPRODUCT_PENDING';
export const GET_GROUPADDPRODUCT_SUCCESS = 'GET_GROUPADDPRODUCT_SUCCESS';
export const GET_GROUPADDPRODUCT_FAILURE = 'GET_GROUPADDPRODUCT_FAILURE';
export const groupAddProduct = params => {
    return {
        type: GET_GROUPADDPRODUCT,
        payload: api.groupAddProduct(params)
    }
}
// 产品组移除产品接口
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

// 产品组移除产品接口
export const GET_COPYGROUP = 'GET_COPYGROUP';
export const GET_COPYGROUP_PENDING = 'GET_COPYGROUP_PENDING';
export const GET_COPYGROUP_SUCCESS = 'GET_COPYGROUP_SUCCESS';
export const GET_COPYGROUP_FAILURE = 'GET_COPYGROUP_FAILURE';
export const copyGroup = params => {
    return {
        type: GET_COPYGROUP,
        payload: api.copyGroup(params)
    } 
}

export const GET_GROUPEDITPRODUCT = 'GET_GROUPEDITPRODUCT';
export const GET_GROUPEDITPRODUCT_SUCCESS = 'GET_GROUPEDITPRODUCT_SUCCESS';
export const groupEditProduct = params => {
    return { 
        type: GET_GROUPEDITPRODUCT,
        payload: api.groupEditProduct(params)
    }
}

export const GET_SAVESORT = 'GET_SAVESORT';
export const GET_SAVESORT_SUCCESS = 'GET_SAVESORT_SUCCESS';
export const saveSort = params => {
    return { 
        type: GET_SAVESORT,
        payload: api.saveSort(params)
    }
}

export const GET_GROUPPRODUCTEXPORT = 'GET_GROUPPRODUCTEXPORT';
export const GET_GROUPPRODUCTEXPORT_SUCCESS = 'GET_GROUPPRODUCTEXPORT_SUCCESS';
export const groupProductExport = params => {
    return { 
        type: GET_GROUPPRODUCTEXPORT,
        payload: api.groupProductExport(params)
    }
}

export const GET_FINDBYGROUPID = 'GET_FINDBYGROUPID';
export const GET_FINDBYGROUPID_SUCCESS = 'GET_FINDBYGROUPID_SUCCESS';
export const findByGroupId = params => {
    return { 
        type: GET_FINDBYGROUPID,
        payload: api.findByGroupId(params)
    }
}