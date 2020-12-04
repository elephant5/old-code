import * as api from '../../api/giftCode';


// 激活码分页
export const GET_SELECTPAGELIST = 'GET_SELECTPAGELIST';
export const GET_SELECTPAGELIST_SUCCESS = 'GET_SELECTPAGELIST_SUCCESS';
export const selectPageList = params => {
    return {
        type: GET_SELECTPAGELIST,
        payload: api.selectPageList(params)
    }
}

// 激活码生成
export const GET_GENERATEACTCODE = 'GET_GENERATEACTCODE';
export const GET_GENERATEACTCODE_SUCCESS = 'GET_GENERATEACTCODE_SUCCESS';
export const generateActCode = params => {
    return {
        type: GET_GENERATEACTCODE,
        payload: api.generateActCode(params)
    }
}

// 激活码检测
export const GET_CHECKCODES = 'GET_CHECKCODES';
export const GET_CHECKCODES_SUCCESS = 'GET_CHECKCODES_SUCCESS';
export const checkActCodes = params => {
    return {
        type: GET_CHECKCODES,
        payload: api.checkActCodes(params)
    }
}

// 激活码检测 根据批次号
export const GET_CHECKCODESBYBATCH = 'GET_CHECKCODESBYBATCH';
export const GET_CHECKCODESBYBATCH_SUCCESS = 'GET_CHECKCODESBYBATCH_SUCCESS';
export const checkActCodesByBatch = params => {
    return {
        type: GET_CHECKCODESBYBATCH,
        payload: api.checkActCodesByBatch(params)
    }
}

// 激活码出库，根据激活码列表
export const GET_OUTACTCODEBYCODES = 'GET_OUTACTCODEBYCODES';
export const GET_OUTACTCODEBYCODES_SUCCESS = 'GET_OUTACTCODEBYCODES_SUCCESS';
export const outActCodeByCodes = params => {
    return {
        type: GET_OUTACTCODEBYCODES,
        payload: api.outActCodeByCodes(params)
    }
}

// 激活码出库，根据批次号
export const GET_OUTACTCODEBYBATCH = 'GET_OUTACTCODEBYBATCH';
export const GET_OUTACTCODEBYBATCH_SUCCESS = 'GET_OUTACTCODEBYBATCH_SUCCESS';
export const outActCodeByBatch = params => {
    return {
        type: GET_OUTACTCODEBYBATCH,
        payload: api.outActCodeByBatch(params)
    }
}

// 激活码退货
export const GET_RETURNACTCODEBYCODES = 'GET_RETURNACTCODEBYCODES';
export const GET_RETURNACTCODEBYCODES_SUCCESS = 'GET_RETURNACTCODEBYCODES_SUCCESS';
export const returnActCodeByCodes = params => {
    return {
        type: GET_RETURNACTCODEBYCODES,
        payload: api.returnActCodeByCodes(params)
    }
}

// 激活码作废
export const GET_OBSOLETEACTCODEBYCODES = 'GET_OBSOLETEACTCODEBYCODES';
export const GET_OBSOLETEACTCODEBYCODES_SUCCESS = 'GET_OBSOLETEACTCODEBYCODES_SUCCESS';
export const obsoleteActCodeByCodes = params => {
    return {
        type: GET_OBSOLETEACTCODEBYCODES,
        payload: api.obsoleteActCodeByCodes(params)
    }
}

// 激活码生成导出
export const GET_EXPORTEXCEL = 'GET_EXPORTEXCEL';
export const GET_EXPORTEXCEL_SUCCESS = 'GET_EXPORTEXCEL_SUCCESS';
export const exportExcel = params => {
    return {
        type: GET_EXPORTEXCEL,
        payload: api.exportExcel(params)
    }
}

// 激活码出库导出
export const GET_EXPORTOUTCODEEXCEL = 'GET_EXPORTOUTCODEEXCEL';
export const GET_EXPORTOUTCODEEXCEL_SUCCESS = 'GET_EXPORTOUTCODEEXCEL_SUCCESS';
export const exportOutCodeExcel = params => {
    return {
        type: GET_EXPORTOUTCODEEXCEL,
        payload: api.exportOutCodeExcel(params)
    }
}

// 延长激活码有效期
export const PROLONG_GIFTCODE = 'PROLONG_GIFTCODE';
export const PROLONG_GIFTCODE_SUCCESS = 'PROLONG_GIFTCODE_SUCCESS';
export const prolongGiftCode = params => {
    return {
        type: PROLONG_GIFTCODE,
        payload: api.prolongGiftCode(params)
    }
}

// 激活码列表导出
export const GET_EXPORTLIST = 'GET_EXPORTLIST';
export const GET_EXPORTLIST_SUCCESS = 'GET_EXPORTLIST_SUCCESS';
export const exportList = params => {
    return {
        type: GET_EXPORTLIST,
        payload: api.exportList(params)
    }
}