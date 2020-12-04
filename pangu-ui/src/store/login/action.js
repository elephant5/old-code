import * as api from '../../api/login';

// 登录验证
export const GET_CHECKLOGIN = 'GET_CHECKLOGIN';
export const GET_CHECKLOGIN_PENDING = 'GET_CHECKLOGIN_PENDING';
export const GET_CHECKLOGIN_SUCCESS = 'GET_CHECKLOGIN_SUCCESS';
export const GET_CHECKLOGIN_FAILURE = 'GET_CHECKLOGIN_FAILURE';
export const checkLogin = params => {
    return {
        type: GET_CHECKLOGIN,
        payload: api.checkLogin(params)
    }
}
// 登录退出
export const GET_LOGINOUT = 'GET_LOGINOUT';
export const GET_LOGINOUT_PENDING = 'GET_LOGINOUT_PENDING';
export const GET_LOGINOUT_SUCCESS = 'GET_LOGINOUT_SUCCESS';
export const GET_LOGINOUT_FAILURE = 'GET_LOGINOUT_FAILURE';
export const loginOut = params => {
    return {
        type: GET_LOGINOUT,
        payload: api.loginOut(params)
    }
}
// 获取菜单
export const GET_USERMENUS = 'GET_USERMENUS';
export const GET_USERMENUS_PENDING = 'GET_USERMENUS_PENDING';
export const GET_USERMENUS_SUCCESS = 'GET_USERMENUS_SUCCESS';
export const GET_USERMENUS_FAILURE = 'GET_USERMENUS_FAILURE';
export const getUserMenus = params => {
    return {
        type: GET_USERMENUS,
        payload: api.getUserMenus(params)
    }
}
// 获取按钮
export const GET_USERBUTTONS = 'GET_USERBUTTONS';
export const GET_USERBUTTONS_PENDING = 'GET_USERBUTTONS_PENDING';
export const GET_USERBUTTONS_SUCCESS = 'GET_USERBUTTONS_SUCCESS';
export const GET_USERBUTTONS_FAILURE = 'GET_USERBUTTONS_FAILURE';
export const getUserButtons = params => {
    return {
        type: GET_USERBUTTONS,
        payload: api.getUserButton(params)
    }
}