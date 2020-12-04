import httpNuwa from '../util/httpNuwa';


//单发短信列表
export const sendMsg = data => {
    return httpNuwa.post('/kltsms/sendMsg', data)
}

//单发短信列表
export const selectQueueByPage = data => {
    return httpNuwa.post('/kltsms/selectQueueByPage', data)
}