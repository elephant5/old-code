import http from '../util/http';
//Logo列表查询
export const selectBankLogoList = data => {
    return http.post('/sysBankLogo/listWithPic', {})
}

//Logo新增
export const insertBankLogo = data => {
    return http.post('/sysBankLogo/add', data)
}
//Logo修改
export const updateBankLogo = data => {
    return http.post('/sysBankLogo/update', data)
}
