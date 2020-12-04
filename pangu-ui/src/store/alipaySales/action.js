import * as api from '../../api/alipaySales';


// 客服查询订单信息
export const GET_QUERYSALESORDER = 'GET_QUERYSALESORDER';
export const GET_QUERYSALESORDER_SUCCESS = 'GET_QUERYSALESORDER_SUCCESS';
export const querySalesOrder = params => {
    return {
        type: GET_QUERYSALESORDER,
        payload: api.querySalesOrder(params)
    }
}

// 客服验证订单是否可以退款
export const GET_CHECKSALESORDERREFUND = 'GET_CHECKSALESORDERREFUND';
export const GET_CHECKSALESORDERREFUND_SUCCESS = 'GET_CHECKSALESORDERREFUND_SUCCESS';
export const checkSalesOrderRefund = params => {
    return {
        type: GET_CHECKSALESORDERREFUND,
        payload: api.checkSalesOrderRefund(params)
    }
}

// 支付宝退款接口
export const GET_REFUNDSALEORDER = 'GET_REFUNDSALEORDER';
export const GET_REFUNDSALEORDER_SUCCESS = 'GET_REFUNDSALEORDER_SUCCESS';
export const refundSaleOrder = params => {
    return {
        type: GET_REFUNDSALEORDER,
        payload: api.refundSaleOrder(params)
    }
}