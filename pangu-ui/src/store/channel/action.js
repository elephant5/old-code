import * as api from '../../api/channel';


//渠道列表
export const GET_SALESCHANNELLIST = 'GET_SALESCHANNELLIST';
export const GET_SALESCHANNELLIST_SUCCESS = 'GET_SALESCHANNELLIST_SUCCESS';
export const GET_SALESCHANNELLIST_FAILURE = 'GET_SALESCHANNELLIST_FAILURE';
export const getSalesChannelList = params => {
    return {
        type: GET_SALESCHANNELLIST,
        payload: api.getSalesChannelList(params)
    }
}

//启用停用渠道
export const UPDATE_SALESCHANNELSTATUS = 'UPDATE_SALESCHANNELSTATUS';
export const UPDATE_SALESCHANNELSTATUS_SUCCESS = 'UPDATE_SALESCHANNELSTATUS_SUCCESS';
export const UPDATE_SALESCHANNELSTATUS_FAILURE = 'UPDATE_SALESCHANNELSTATUS_FAILURE';
export const updateSalesChannelStatus = params => {
    return {
        type: UPDATE_SALESCHANNELSTATUS,
        payload: api.updateSalesChannelStatus(params)
    }
}

//新增渠道
export const ADD_SALESCHANNEL = 'ADD_SALESCHANNEL';
export const ADD_SALESCHANNEL_SUCCESS = 'ADD_SALESCHANNEL_SUCCESS';
export const ADD_SALESCHANNEL_FAILURE = 'ADD_SALESCHANNEL_FAILURE';
export const addSalesChannel = params => {
    return {
        type: ADD_SALESCHANNEL,
        payload: api.add(params)
    }
}

//更新渠道
export const UPDATE_SALESCHANNEL = 'UPDATE_SALESCHANNEL';
export const UPDATE_SALESCHANNEL_SUCCESS = 'UPDATE_SALESCHANNEL_SUCCESS';
export const UPDATE_SALESCHANNEL_FAILURE = 'UPDATE_SALESCHANNEL_FAILURE';
export const updateSalesChannel = params => {
    return {
        type: UPDATE_SALESCHANNEL,
        payload: api.update(params)
    }
}

//根据id查找单个渠道
export const GET_SALESCHANNEL = 'GET_SALESCHANNEL';
export const GET_SALESCHANNEL_SUCCESS = 'GET_SALESCHANNEL_SUCCESS';
export const GET_SALESCHANNEL_FAILURE = 'GET_SALESCHANNEL_FAILURE';
export const getSalesChannel = params => {
    return {
        type: GET_SALESCHANNEL,
        payload: api.get(params)
    }
}