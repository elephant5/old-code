import {fromJS} from 'immutable';
import {createReducer} from 'redux-immutablejs';
import {getObjReducer} from '../../util/reducer';
import {
    SELECT_BANK_LOGO_LIST_SUCCESS,
    INSERT_BANK_LOGO_SUCCESS,
    UPDATE_BANK_LOGO_SUCCESS,
    } from './action';

const initailState = fromJS({
    bankLogoList:{},
    bankLogo:{}
})

const logo = createReducer(initailState, {
    [SELECT_BANK_LOGO_LIST_SUCCESS]: (state, {payload}) => {
        return state.set('bankLogoList', getObjReducer(state, payload))
    },
    [INSERT_BANK_LOGO_SUCCESS]: (state, {payload}) => {
        return state.set('bankLogo', getObjReducer(state, payload))
    },
})

export default logo;