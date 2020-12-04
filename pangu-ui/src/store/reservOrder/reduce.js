import { fromJS } from 'immutable';
import { createReducer } from 'redux-immutablejs';
import { getObjReducer } from '../../util/reducer';

import {
    GET_SELECTEQUITYLIST_SUCCESS,
    GET_INSERTRESERVORDER_SUCCESS,
    GET_SELECTRESERVORDERPAGELIST_SUCCESS,
    GET_SELECTRESERVORDERBYID_SUCCESS,
    GET_STARTHANDLE_SUCCESS,
    GET_RESERVUPDATEINFO_SUCCESS,
    GET_RESERVCANCEL_SUCCESS,
    GET_RESERVFAIL_SUCCESS,
    GET_RESERVSUCCESS_SUCCESS,
    GET_UPDATERESERVORDER_SUCCESS,
    GET_MESSAGE_SUCCESS,
    GET_REUSE_SUCCESS,
    GET_EXPORTORDER_SUCCESS,
    GET_ORDERLIST_SUCCESS,
    ADD_ORDERLOG_SUCCESS,
    GET_SAVEOBJEDIT_SUCCESS,
    GET_SAVEOBJEDITANDSEND_SUCCESS,
    GET_SYSHOSPITALLIST_SUCCESS,
} from './action';

const initailState = fromJS({
    EquityList: {},
    reservOrderInfo: {},
    reservOrderList: [],
    insertReservOrder: {},
    startHandle: {},
    updateReservOrderVo: {},
    messageInfo: {},
    reuse: {},
    reservUpdateInfo: {},
    updateReservOrder: {},
    exportOrderRes: {},
    orderLogListRes:{},
    logLogRes:{},
    saveObjEditRes:{},
    saveObjEditAndSendRes:{},
})

const reserv = createReducer(initailState, {
    [GET_SELECTEQUITYLIST_SUCCESS]: (state, { payload }) => {
        return state.set('EquityList', getObjReducer(state, payload))
    },
    [GET_INSERTRESERVORDER_SUCCESS]: (state, { payload }) => {
        return state.set('insertReservOrder', getObjReducer(state, payload))
    },
    [GET_SELECTRESERVORDERPAGELIST_SUCCESS]: (state, { payload }) => {
        return state.set('reservOrderList', getObjReducer(state, payload))
    },
    [GET_SELECTRESERVORDERBYID_SUCCESS]: (state, { payload }) => {
        return state.set('reservOrderInfo', getObjReducer(state, payload))
    },
    [GET_STARTHANDLE_SUCCESS]: (state, { payload }) => {
        return state.set('startHandle', getObjReducer(state, payload))
    },
    [GET_RESERVUPDATEINFO_SUCCESS]: (state, { payload }) => {
        return state.set('reservUpdateInfo', getObjReducer(state, payload))
    },
    [GET_RESERVCANCEL_SUCCESS]: (state, { payload }) => {
        return state.set('reservCancel', getObjReducer(state, payload))
    },
    [GET_RESERVFAIL_SUCCESS]: (state, { payload }) => {
        return state.set('reservFail', getObjReducer(state, payload))
    },
    [GET_RESERVSUCCESS_SUCCESS]: (state, { payload }) => {
        return state.set('reservSuccess', getObjReducer(state, payload))
    },
    [GET_UPDATERESERVORDER_SUCCESS]: (state, { payload }) => {
        return state.set('updateReservOrder', getObjReducer(state, payload))
    },
    [GET_MESSAGE_SUCCESS]: (state, { payload }) => {
        return state.set('messageInfo', getObjReducer(state, payload))
    },
    [GET_REUSE_SUCCESS]: (state, { payload }) => {
        return state.set('reuse', getObjReducer(state, payload))
    },
    [GET_EXPORTORDER_SUCCESS]: (state, { payload }) => {
        return state.set('exportOrderRes', getObjReducer(state, payload))
    },
    [GET_ORDERLIST_SUCCESS]: (state, { payload }) => {
        return state.set('orderLogListRes', getObjReducer(state, payload))
    },
    [ADD_ORDERLOG_SUCCESS]: (state, { payload }) => {
        return state.set('logLogRes', getObjReducer(state, payload))
    },
    [GET_SAVEOBJEDIT_SUCCESS]: (state, { payload }) => {
        return state.set('saveObjEditRes', getObjReducer(state, payload))
    },
    [GET_SAVEOBJEDITANDSEND_SUCCESS]: (state, { payload }) => {
        return state.set('saveObjEditAndSendRes', getObjReducer(state, payload))
    },
    [GET_SYSHOSPITALLIST_SUCCESS]: (state, { payload }) => {
        return state.set('getSysHospitalListRes', getObjReducer(state, payload))
    },
})

export default reserv;    