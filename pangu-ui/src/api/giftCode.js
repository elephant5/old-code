import httpMars from '../util/httpMars';

/*激活码分页查询 */
export const selectPageList = data => {
    return httpMars.post('/giftCode/selectPageList', data)
}

/*激活码生成*/
export const generateActCode = data => {
    return httpMars.post('/giftCode/generateActCode', data)
}

/* 激活码检测 */
export const checkActCodes = data => {
    return httpMars.post('/giftCode/checkCodes', data)
}

/**检测激活码（根据批次号） */
export const checkActCodesByBatch = data => {
    return httpMars.post('/giftCode/checkCodesByBatch', data)
}

/**激活码出库,根据激活码 */
export const outActCodeByCodes = data => {
    return httpMars.post('/giftCode/outActCodeByCodes', data)
}

/**激活码出库，根据批次号 */
export const outActCodeByBatch = data => {
    return httpMars.post('/giftCode/outActCodeByBatch', data)
}

/**激活码退货，根据激活码 */
export const returnActCodeByCodes = data => {
    return httpMars.post('/giftCode/returnActCodes', data)
}

/**激活码作废，根据激活码 */
export const obsoleteActCodeByCodes = data => {
    return httpMars.post('/giftCode/obsoleteActCodes', data)
}

/**生成激活码导出 */
export const exportExcel = data => {
    return httpMars.get(`/giftCode/exportExcel/${data}`)
}

/**出库激活码导出 */
export const exportOutCodeExcel = data => {
    return httpMars.post('/giftCode/exportOutCodeExcel', data)
}

/**延长有效期 */
export const prolongGiftCode = data => {
    return httpMars.post('/giftCode/prolongGiftCode', data)
}

/**激活码列表导出 */
export const exportList = data => {
    return httpMars.post('/giftCode/exportList', data)
}