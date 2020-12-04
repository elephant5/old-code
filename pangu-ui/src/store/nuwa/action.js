import * as api from '../../api/nuwa';

// 登录验证
export const GET_SENDMSG = 'GET_SENDMSG';
export const GET_SENDMSG_SUCCESS = 'GET_SENDMSG_SUCCESS';
export const sendMsg = params => {
    return {
        type: GET_SENDMSG,
        payload: api.sendMsg(params)
    }
}
// 登录退出
export const GET_SELECTQUEUEBYPAGE = 'GET_SELECTQUEUEBYPAGE';
export const GET_SELECTQUEUEBYPAGE_SUCCESS = 'GET_SELECTQUEUEBYPAGE_SUCCESS';
export const selectQueueByPage = params => {
    return {
        type: GET_SELECTQUEUEBYPAGE,
        payload: api.selectQueueByPage(params)
    }
}
