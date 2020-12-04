import {fromJS} from 'immutable';
import {createReducer} from 'redux-immutablejs';
import {getObjReducer} from '../../util/reducer';

import { 
    GET_QUERYSALESORDER_SUCCESS,
    GET_CHECKSALESORDERREFUND_SUCCESS,
    GET_REFUNDSALEORDER_SUCCESS,
} from './action';

const initailState = fromJS({
    querySalesOrderRes: {},
    checkSalesOrderRefundRes: {},
    refundSaleOrderRes: {},
})

const alipaySales = createReducer(initailState, {
    [GET_QUERYSALESORDER_SUCCESS]: (state, {payload}) => {
        return state.set('querySalesOrderRes', getObjReducer(state, payload))
    },
    [GET_CHECKSALESORDERREFUND_SUCCESS]: (state, {payload}) => {
        return state.set('checkSalesOrderRefundRes', getObjReducer(state, payload))
    },
    [GET_REFUNDSALEORDER_SUCCESS]: (state, {payload}) => {
        return state.set('refundSaleOrderRes', getObjReducer(state, payload))
    },
})

export default alipaySales;    