import httpAlipay from '../util/httpAlipay';

/*客服查询订单信息 */
export const querySalesOrder = data => {
    return httpAlipay.post('/salesOrder/querySalesOrder', data)
}

/*客服验证订单是否可以退款 */
export const checkSalesOrderRefund = data => {
    return httpAlipay.post('/salesOrder/checkSalesOrderRefund', data)
}

/*支付宝退款接口 */
export const refundSaleOrder = data => {
    return httpAlipay.post('/alipay/refundSaleOrder', data)
}