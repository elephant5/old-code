import {fromJS} from 'immutable';
import {createReducer} from 'redux-immutablejs';
import {getObjReducer} from '../../util/reducer';

import { 
    GET_SELECTPAGELIST_SUCCESS,
    GET_GENERATEACTCODE_SUCCESS,
    GET_CHECKCODES_SUCCESS,
    GET_CHECKCODESBYBATCH_SUCCESS,
    GET_OUTACTCODEBYCODES_SUCCESS,
    GET_OUTACTCODEBYBATCH_SUCCESS,
    GET_RETURNACTCODEBYCODES_SUCCESS,
    GET_OBSOLETEACTCODEBYCODES_SUCCESS,
    GET_EXPORTEXCEL_SUCCESS,
    GET_EXPORTOUTCODEEXCEL_SUCCESS,
    PROLONG_GIFTCODE_SUCCESS,
    GET_EXPORTLIST_SUCCESS,
} from './action';

const initailState = fromJS({
    giftCodePageList: {},
    generateActCodeRes: {},
    checkCodesRes: {},
    checkCodesByBatchRes: {},
    outActCodeByCodesRes: {},
    outActCodeByBatchRes: {},
    returnActCodeByCodesRes: {},
    obsoleteActCodeByCodesRes: {},
    exportExcelRes: {},
    exportOutCodeExcelRes: {},
    prolongGiftCodeRes:{},
    exportListRes:{},
})

const code = createReducer(initailState, {
    [GET_SELECTPAGELIST_SUCCESS]: (state, {payload}) => {
        return state.set('giftCodePageList', getObjReducer(state, payload))
    },
    [GET_GENERATEACTCODE_SUCCESS]: (state, {payload}) => {
        return state.set('generateActCodeRes', getObjReducer(state, payload))
    },
    [GET_CHECKCODES_SUCCESS]: (state, {payload}) => {
        return state.set('checkCodesRes', getObjReducer(state, payload))
    },
    [GET_CHECKCODESBYBATCH_SUCCESS]: (state, {payload}) => {
        return state.set('checkCodesByBatchRes', getObjReducer(state, payload))
    },
    [GET_OUTACTCODEBYCODES_SUCCESS]: (state, {payload}) => {
        return state.set('outActCodeByCodesRes', getObjReducer(state, payload))
    },
    [GET_OUTACTCODEBYBATCH_SUCCESS]: (state, {payload}) => {
        return state.set('outActCodeByBatchRes', getObjReducer(state, payload))
    },
    [GET_RETURNACTCODEBYCODES_SUCCESS]: (state, {payload}) => {
        return state.set('returnActCodeByCodesRes', getObjReducer(state, payload))
    },
    [GET_OBSOLETEACTCODEBYCODES_SUCCESS]: (state, {payload}) => {
        return state.set('obsoleteActCodeByCodesRes', getObjReducer(state, payload))
    },
    [GET_EXPORTEXCEL_SUCCESS]: (state, {payload}) => {
        return state.set('exportExcelRes', getObjReducer(state, payload))
    },
    [GET_EXPORTOUTCODEEXCEL_SUCCESS]: (state, {payload}) => {
        return state.set('exportOutCodeExcelRes', getObjReducer(state, payload))
    },
    [PROLONG_GIFTCODE_SUCCESS]: (state, {payload}) => {
        return state.set('prolongGiftCodeRes', getObjReducer(state, payload))
    },
    [GET_EXPORTLIST_SUCCESS]: (state, {payload}) => {
        return state.set('exportListRes', getObjReducer(state, payload))
    },
})

export default code;    