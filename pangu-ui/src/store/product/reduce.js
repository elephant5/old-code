import { fromJS } from 'immutable';
import { createReducer } from 'redux-immutablejs';
import { getObjReducer } from '../../util/reducer';
import {
    GET_SELECTPRODUCTPAGELIST_SUCCESS,
    GET_SELECTPAGE_SUCCESS,
    GET_SELECTEXPORT_SUCCESS
} from './action';

const initailState = fromJS({
    productList: {},
    selectExportRes: {},
})

const resource = createReducer(initailState, {
    [GET_SELECTPRODUCTPAGELIST_SUCCESS]: (state, { payload }) => {
        return state.set('productList', getObjReducer(state, payload))
    },
    [GET_SELECTPAGE_SUCCESS]: (state, { payload }) => {
        return state.set('productPageList', getObjReducer(state, payload))
    },
    [GET_SELECTEXPORT_SUCCESS]: (state, { payload }) => {
        return state.set('selectExportRes', getObjReducer(state, payload))
    },
})

export default resource;