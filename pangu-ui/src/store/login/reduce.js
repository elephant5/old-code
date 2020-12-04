import {fromJS} from 'immutable';
import {createReducer} from 'redux-immutablejs';
import {getObjReducer} from '../../util/reducer';
import {
    GET_CHECKLOGIN_SUCCESS,
    GET_USERMENUS_SUCCESS,
    GET_USERBUTTONS_SUCCESS,
    GET_LOGINOUT_SUCCESS
    } from './action';

const initailState = fromJS({
    user: {},
    userMenus: {},
    userButtons: {},
    loginOut: {},
})

const login = createReducer(initailState, {
    [GET_CHECKLOGIN_SUCCESS]: (state, {payload}) => {
        return state.set('user', getObjReducer(state, payload))
    },
    [GET_USERMENUS_SUCCESS]: (state, {payload}) => {
        return state.set('userMenus', getObjReducer(state, payload))
    },
    [GET_USERBUTTONS_SUCCESS]: (state, {payload}) => {
        return state.set('userButtons', getObjReducer(state, payload))
    },
    [GET_LOGINOUT_SUCCESS]: (state, {payload}) => {
        return state.set('loginOut', getObjReducer(state, payload))
    },
})

export default login;