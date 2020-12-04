import httpLogin from '../util/httpLogin';

//登录验证接口
export const checkLogin = data => {
    return httpLogin.post('/login/login', data)
}

//退出登录
export const loginOut = data => {
    return httpLogin.post('/login/logout',data)
}

//获取菜单
export const getUserMenus = data => {
    return httpLogin.get('/login/userMenus/pangu', data)
}

//获取按钮
export const getUserButton = data => {
    return httpLogin.get('/login/userButtons/pangu',data)
}



