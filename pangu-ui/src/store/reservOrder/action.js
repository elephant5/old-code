import * as api from '../../api/reservOrder';


// 产品组列表
export const GET_SELECTEQUITYLIST = 'GET_SELECTEQUITYLIST';
export const GET_SELECTEQUITYLIST_SUCCESS = 'GET_SELECTEQUITYLIST_SUCCESS';
export const selectEquityList = params => {
    return {
        type: GET_SELECTEQUITYLIST,
        payload: api.selectEquityList(params)
    }
}


export const GET_INSERTRESERVORDER = 'GET_INSERTRESERVORDER';
export const GET_INSERTRESERVORDER_SUCCESS = 'GET_INSERTRESERVORDER_SUCCESS';
export const insertReservOrder = params => {
    return { 
        type: GET_INSERTRESERVORDER,
        payload: api.insertReservOrder(params)
    }
}

export const GET_SELECTRESERVORDERPAGELIST = 'GET_SELECTRESERVORDERPAGELIST';
export const GET_SELECTRESERVORDERPAGELIST_SUCCESS = 'GET_SELECTRESERVORDERPAGELIST_SUCCESS';
export const selectReservOrderPageList = params => {
    return { 
        type: GET_SELECTRESERVORDERPAGELIST,
        payload: api.selectReservOrderPageList(params)
    }
}

export const GET_SELECTRESERVORDERBYID = 'GET_SELECTRESERVORDERBYID';
export const GET_SELECTRESERVORDERBYID_SUCCESS = 'GET_SELECTRESERVORDERBYID_SUCCESS';
export const selectReservOrderById = params => {
    return { 
        type: GET_SELECTRESERVORDERBYID,
        payload: api.selectReservOrderById(params)
    }
}

export const GET_STARTHANDLE = 'GET_STARTHANDLE';
export const GET_STARTHANDLE_SUCCESS = 'GET_STARTHANDLE_SUCCESS';
export const startHandle = params => {
    return { 
        type: GET_STARTHANDLE,
        payload: api.startHandle(params)
    }
}
export const GET_RESERVUPDATEINFO = 'GET_RESERVUPDATEINFO';
export const GET_RESERVUPDATEINFO_SUCCESS = 'GET_RESERVUPDATEINFO_SUCCESS';
export const reservUpdateInfo = params => {
    return { 
        type: GET_RESERVUPDATEINFO,
        payload: api.reservUpdateInfo(params)
    }
}

export const GET_UPDATERESERVORDER = 'GET_UPDATERESERVORDER';
export const GET_UPDATERESERVORDER_SUCCESS = 'GET_UPDATERESERVORDER_SUCCESS';
export const updateReservOrder = params => {
    return { 
        type: GET_UPDATERESERVORDER,
        payload: api.updateReservOrder(params)
    }
}

export const GET_RESERVSUCCESS = 'GET_RESERVSUCCESS';
export const GET_RESERVSUCCESS_SUCCESS = 'GET_RESERVSUCCESS_SUCCESS';
export const reservSuccess = params => {
    return { 
        type: GET_RESERVSUCCESS,
        payload: api.reservSuccess(params)
    }
}

export const GET_RESERVFAIL = 'GET_RESERVFAIL';
export const GET_RESERVFAIL_SUCCESS = 'GET_RESERVFAIL_SUCCESS';
export const reservFail = params => {
    return { 
        type: GET_RESERVFAIL,
        payload: api.reservFail(params)
    }
}

export const GET_RESERVCANCEL = 'GET_RESERVCANCEL';
export const GET_RESERVCANCEL_SUCCESS = 'GET_RESERVCANCEL_SUCCESS';
export const reservCancel = params => {
    return { 
        type: GET_RESERVCANCEL,
        payload: api.reservCancel(params)
    }
}
export const GET_MESSAGE = 'GET_MESSAGE';
export const GET_MESSAGE_SUCCESS = 'GET_MESSAGE_SUCCESS';
export const message = params => {
    return { 
        type: GET_MESSAGE,
        payload: api.message(params)
    }
}
export const GET_REUSE = 'GET_REUSE';
export const GET_REUSE_SUCCESS = 'GET_REUSE_SUCCESS';
export const reuse = params => {
    return { 
        type: GET_REUSE,
        payload: api.reuse(params)
    }
}

export const GET_EXPORTORDER = 'GET_EXPORTORDER';
export const GET_EXPORTORDER_SUCCESS = 'GET_EXPORTORDER_SUCCESS';
export const exportOrder = params => {
    return { 
        type: GET_EXPORTORDER,
        payload: api.exportOrder(params)
    }
}

export const GET_ORDERLIST = 'GET_ORDERLIST';
export const GET_ORDERLIST_SUCCESS = 'GET_ORDERLIST_SUCCESS';
export const listLogs = params => {
    return { 
        type: GET_ORDERLIST,
        payload: api.listLogs(params)
    }
}

export const ADD_ORDERLOG = 'ADD_ORDERLOG';
export const ADD_ORDERLOG_SUCCESS = 'ADD_ORDERLOG_SUCCESS';
export const insertLog = params => {
    return { 
        type: ADD_ORDERLOG,
        payload: api.insertLog(params)
    }
}

export const GET_SAVEOBJEDIT = 'GET_SAVEOBJEDIT';
export const GET_SAVEOBJEDIT_SUCCESS = 'GET_SAVEOBJEDIT_SUCCESS';
export const saveObjEdit = params => {
    return { 
        type: GET_SAVEOBJEDIT,
        payload: api.saveObjEdit(params)
    }
}

export const GET_SAVEOBJEDITANDSEND = 'GET_SAVEOBJEDITANDSEND';
export const GET_SAVEOBJEDITANDSEND_SUCCESS = 'GET_SAVEOBJEDITANDSEND_SUCCESS';
export const saveObjEditAndSend = params => {
    return { 
        type: GET_SAVEOBJEDITANDSEND,
        payload: api.saveObjEditAndSend(params)
    }
}

export const GET_SYSHOSPITALLIST = 'GET_SYSHOSPITALLIST';
export const GET_SYSHOSPITALLIST_SUCCESS = 'GET_SYSHOSPITALLIST_SUCCESS';
export const getSysHospitalList = params => {
    return { 
        type: GET_SYSHOSPITALLIST,
        payload: api.queryHospital(params)
    }
}