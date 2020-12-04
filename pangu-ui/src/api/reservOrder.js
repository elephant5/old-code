import httpMars from '../util/httpMars';

/*匹配权益列表 */
export const selectEquityList = data => {
    return httpMars.post('/reservOrder/selectEquityList', data)
}
/**来电预约新增 */
export const insertReservOrder = data => {
    return httpMars.post('/reservOrder/insertReservOrder', data)
}

/**预约单列表查询 */
export const selectReservOrderPageList = data => {
    return httpMars.post('/reservOrder/selectReservOrderPageList', data)
}

/**预约单详情 */
export const selectReservOrderById = data => {
    return httpMars.get(`/reservOrder/selectReservOrderById/${data.id}`)
}

/**开始处理 */
export const startHandle = data => {
    return httpMars.get(`/reservOrder/startHandle/${data.id}`)
}
/**预约单修正查询 */
export const reservUpdateInfo = data => {
    return httpMars.post('/reservOrder/reservUpdateInfo', data)
}
/**预约单更新查询 */
export const updateReservOrder = data => {
    return httpMars.post('/reservOrder/updateReservOrder', data)
}
/**预订成功信息 */
export const reservSuccess = data => {
    return httpMars.post('/reservOrder/reservSuccess', data)
}
/**预订失败信息 */
export const reservFail = data => {
    return httpMars.post('/reservOrder/reservFail', data)
}

/**预订失败信息 */
export const reservCancel = data => {
    return httpMars.post('/reservOrder/reservCancel', data)
}
/**预订失败信息 */
export const message = data => {
    return httpMars.post('/reservOrder/message', data)
}

/**预订失败信息 */
export const reuse = data => {
    return httpMars.get(`/reservCode/reuse/${data}`, {})
}

/**导出预约单 */
export const exportOrder = data => {
    return httpMars.post('/reservOrder/export', data)
}


/**查询日志列表 */
export const listLogs = data => {
    return httpMars.post('/reservOrderLog/list', data)
}

/**日志留言 */
export const insertLog = data => {
    return httpMars.post('/reservOrderLog/insert', data)
}

/**客服操作保存编辑实物的物流信息 */
export const saveObjEdit = data => {
    return httpMars.post('/reservOrder/saveObjEdit',data)
}

/**客服操作保存编辑实物的物流信息并发货 */
export const saveObjEditAndSend = data => {
    return httpMars.post('/reservOrder/saveObjEditAndSend',data)
}

/**查询医院信息 */
export const queryHospital = data => {
    return httpMars.post('/sysHospital/getSysHospitalList',data)
}