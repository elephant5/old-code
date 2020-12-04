import * as api from '../../api/logo';

// 获取logo列表
export const SELECT_BANK_LOGO_LIST = 'SELECT_BANK_LOGO_LIST';
export const SELECT_BANK_LOGO_LIST_PENDING = 'SELECT_BANK_LOGO_LIST_PENDING';
export const SELECT_BANK_LOGO_LIST_SUCCESS = 'SELECT_BANK_LOGO_LIST_SUCCESS';
export const SELECT_BANK_LOGO_LIST_FAILURE = 'SELECT_BANK_LOGO_LIST_FAILURE';
export const selectBankLogoList = params => {
    return {
        type: SELECT_BANK_LOGO_LIST,
        payload: api.selectBankLogoList(params)
    }
}

// 新增Logo
export const INSERT_BANK_LOGO = 'INSERT_BANK_LOGO';
export const INSERT_BANK_LOGO_PENDING = 'INSERT_BANK_LOGO_PENDING';
export const INSERT_BANK_LOGO_SUCCESS = 'INSERT_BANK_LOGO_SUCCESS';
export const INSERT_BANK_LOGO_FAILURE = 'INSERT_BANK_LOGO_FAILURE';
export const insertBankLogo = params => {
    return {
        type: INSERT_BANK_LOGO,
        payload: api.insertBankLogo(params)
    }
}

// 更新Logo
export const UPDATE_BANK_LOGO = 'UPDATE_BANK_LOGO';
export const UPDATE_BANK_LOGO_PENDING = 'UPDATE_BANK_LOGO_PENDING';
export const UPDATE_BANK_LOGO_SUCCESS = 'UPDATE_BANK_LOGO_SUCCESS';
export const UPDATE_BANK_LOGO_FAILURE = 'UPDATE_BANK_LOGO_FAILURE';
export const updateBankLogo = params => {
    return {
        type: UPDATE_BANK_LOGO,
        payload: api.updateBankLogo(params)
    }
}