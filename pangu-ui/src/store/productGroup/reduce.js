import { fromJS } from 'immutable';
import { createReducer } from 'redux-immutablejs';
import { getObjReducer } from '../../util/reducer';
import {
    GET_SELECTGROUPPAGELIST_SUCCESS,
    GET_PRODUCTGROUPDETAIL_SUCCESS,
    GET_QUERYGROUPPRODUCT_SUCCESS,
    GET_GROUPADDPRODUCT_SUCCESS,
    GET_UPDATESTATUS_SUCCESS,
    GET_GROUPDELPRODUCT_SUCCESS,
    GET_COPYGROUP_SUCCESS,
    GET_GROUPEDITPRODUCT_SUCCESS,
    GET_CHANNELSELECTALL_SUCCESS,
    GET_SAVESORT_SUCCESS,
    GET_UPDATERECOMMEND_SUCCESS,
    GET_GROUPPRODUCTEXPORT_SUCCESS,
    GET_FINDBYGROUPID_SUCCESS,
} from './action';

const initailState = fromJS({
    productGroupList: {},
    productGroupDetail: {},
    queryGroupProduct: [],
    groupAddProduct: {},
    copyGroup: {},
    groupEditProduct: {},
    channelList: [],
    saveSort: [],
    updateStatus: {},
    groupProductExportRes: {},
    findByGroupIdRes: {},
})

const resource = createReducer(initailState, {
    [GET_SELECTGROUPPAGELIST_SUCCESS]: (state, { payload }) => {
        return state.set('productGroupList', getObjReducer(state, payload))
    },
    [GET_PRODUCTGROUPDETAIL_SUCCESS]: (state, { payload }) => {
        return state.set('productGroupDetail', getObjReducer(state, payload))
    },
    [GET_QUERYGROUPPRODUCT_SUCCESS]: (state, { payload }) => {
        return state.set('queryGroupProduct', getObjReducer(state, payload))
    },
    [GET_GROUPADDPRODUCT_SUCCESS]: (state, { payload }) => {
        return state.set('groupAddProduct', getObjReducer(state, payload))
    },
    [GET_GROUPDELPRODUCT_SUCCESS]: (state, { payload }) => {
        return state.set('groupDelProduct', getObjReducer(state, payload))
    },
    [GET_COPYGROUP_SUCCESS]: (state, { payload }) => {
        return state.set('copyGroup', getObjReducer(state, payload))
    },
    [GET_GROUPEDITPRODUCT_SUCCESS]: (state, { payload }) => {
        return state.set('groupEditProduct', getObjReducer(state, payload))
    },
    [GET_CHANNELSELECTALL_SUCCESS]: (state, { payload }) => {
        return state.set('channelList', getObjReducer(state, payload))
    },
    [GET_SAVESORT_SUCCESS]: (state, { payload }) => {
        return state.set('saveSort', getObjReducer(state, payload))
    },
    [GET_UPDATESTATUS_SUCCESS]: (state, { payload }) => {
        return state.set('updateStatus', getObjReducer(state, payload))
    },
    [GET_UPDATERECOMMEND_SUCCESS]: (state, { payload }) => {
        return state.set('updateRecommend', getObjReducer(state, payload))
    },
    [GET_GROUPPRODUCTEXPORT_SUCCESS]: (state, { payload }) => {
        return state.set('groupProductExportRes', getObjReducer(state, payload))
    },
    [GET_FINDBYGROUPID_SUCCESS]: (state, { payload }) => {
        return state.set('findByGroupIdRes', getObjReducer(state, payload))
    },
})

export default resource;