import {fromJS} from 'immutable';
import {createReducer} from 'redux-immutablejs';
import {getObjReducer, getArrayReducer} from '../../util/reducer';
import { 
    GET_FESTIVALLIST_SUCCESS,
    GET_SERVICELIST_SUCCESS,
    GET_DICTLIST_SUCCESS ,
    GET_DICTBYTYPE_SUCCESS,
    ADD_DICT_SUCCESS,
    GET_UPLOADEDFILE_SUCCESS,
    GET_UPLOADEDPRICE_SUCCESS,
    GET_UPLOADEDSETTLE_SUCCESS,
    GET_GETSHOPTYPESERVICE_SUCCESS,
    GET_GETALLSYSDICT_SUCCESS,
} from './action';

const initailState = fromJS({
    festivalList: [],
    serviceList: [],
    dictList:{},
    dict:{},
    addDictResult:{},
    uploadedFile: [],
    uploadedPrice: [],
    uploadedSettle: [],
    shopTypeService:[],
    allSysDictList :[]
})

const common = createReducer(initailState, {
    [GET_FESTIVALLIST_SUCCESS]: (state, {payload}) => {
        return state.set('festivalList', getArrayReducer(state, payload))
    },
    [GET_SERVICELIST_SUCCESS]: (state, {payload}) => {
        return state.set('serviceList', getArrayReducer(state, payload))
    },
    [GET_DICTLIST_SUCCESS]:(state, {payload}) => {
        return state.set('dictList', getObjReducer(state, payload))
    },
    [GET_DICTBYTYPE_SUCCESS]:(state, {payload}) => {
        return state.set('dict', getObjReducer(state, payload))
    },
    [ADD_DICT_SUCCESS]:(state, {payload}) => {
        return state.set('addDictResult', getObjReducer(state, payload))
    },
    [GET_UPLOADEDFILE_SUCCESS]:(state, {payload}) => {
        return state.set('uploadedFile', getArrayReducer(state, payload))
    },
    [GET_UPLOADEDPRICE_SUCCESS]:(state, {payload}) => {
        return state.set('uploadedPrice', getArrayReducer(state, payload))
    },
    [GET_UPLOADEDSETTLE_SUCCESS]:(state, {payload}) => {
        return state.set('uploadedSettle', getArrayReducer(state, payload))
    },
    [GET_GETSHOPTYPESERVICE_SUCCESS]:(state, {payload}) => {
        return state.set('shopTypeService', getArrayReducer(state, payload))
    },
    [GET_GETALLSYSDICT_SUCCESS]:(state, {payload}) => {
        return state.set('allSysDictList', getArrayReducer(state, payload))
    },
    
})

export default common;