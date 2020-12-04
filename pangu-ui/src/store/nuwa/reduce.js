import {fromJS} from 'immutable';
import {createReducer} from 'redux-immutablejs';
import {getObjReducer} from '../../util/reducer';
import {
    GET_SENDMSG_SUCCESS,
    GET_SELECTQUEUEBYPAGE_SUCCESS,
    } from './action';

const initailState = fromJS({
    user: {},
    userMenus: {},
    userButtons: {},
    loginOut: {},
})

const login = createReducer(initailState, {
    [GET_SENDMSG_SUCCESS]: (state, {payload}) => {
        return state.set('sendMsg', getObjReducer(state, payload))
    },
    [GET_SELECTQUEUEBYPAGE_SUCCESS]: (state, {payload}) => {
        return state.set('queueList', getObjReducer(state, payload))
    },
})

export default login;