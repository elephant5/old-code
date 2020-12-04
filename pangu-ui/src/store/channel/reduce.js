import {fromJS} from 'immutable';
import {createReducer} from 'redux-immutablejs';
import {getObjReducer} from '../../util/reducer';
import {
    GET_SALESCHANNELLIST_SUCCESS,
    UPDATE_SALESCHANNELSTATUS_SUCCESS,
    ADD_SALESCHANNEL_SUCCESS,
    UPDATE_SALESCHANNEL_SUCCESS,
    GET_SALESCHANNEL_SUCCESS
} from './action';


const initailState = fromJS({
    salesChannelList: {},
    updateSalesChannelStatus: {},
    addResult:{},
    updateResult:{},
    salesChannel:{}
})

const channel = createReducer(initailState, {
    [GET_SALESCHANNELLIST_SUCCESS]: (state, {payload}) => {
        return state.set('salesChannelList', getObjReducer(state, payload))
    },
    [UPDATE_SALESCHANNELSTATUS_SUCCESS]: (state, {payload}) => {
        return state.set('updateSalesChannelStatus', getObjReducer(state, payload))
    },
    [ADD_SALESCHANNEL_SUCCESS]: (state, {payload}) => {
        return state.set('addResult', getObjReducer(state, payload))
    },
    [UPDATE_SALESCHANNEL_SUCCESS]: (state, {payload}) => {
        return state.set('updateResult', getObjReducer(state, payload))
    },
    [GET_SALESCHANNEL_SUCCESS]: (state, {payload}) => {
        return state.set('salesChannel', getObjReducer(state, payload))
    },
})

export default channel;