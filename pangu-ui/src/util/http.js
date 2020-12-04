import axios from 'axios';
import { BASE_PANGU_URL,BASE_PANGU_UI_URL,BASE_MARS_URL } from './url.js';

// import qs from 'qs';
// import { message } from 'antd';

const instance = axios.create({
    baseURL: BASE_PANGU_URL,
    withCredentials: true,   //跨域请求时是否需要使用凭证,是否允许带cookie这些
    responseType: JSON,   
    timeout: 30000,  //请求超时
    headers: {
        'Content-Type': 'application/json'   
    },
})


instance.interceptors.request.use(config => {
    // token处理    

    //POST传参序列化
    // if (config.method === 'post') {
    //     config.data = qs.stringify(config.data);
    // }
    return config;   
}, err => {
    return Promise.reject(err)
})

instance.interceptors.response.use(response => {
    if (response.status === 200) {            
        return Promise.resolve(response);        
    } else {            
        return Promise.reject(response);        
    }  
}, error => {
    if (error.response.status) {
        switch (error.response.status) {                
            // 401: 未登录                
            // 未登录则跳转登录页面，并携带当前页面的路径                
            // 在登录成功后返回当前页面，这一步需要在登录页操作。                
            case 401:                    
                break;
            case 500:
                // window.location.href=BASE_PANGU_UI_URL;
                break;
            // 403 token过期
            // 登录过期对用户进行提示                
            // 清除本地token和清空vuex中token对象                
            // 跳转登录页面                
            case 403:                     
                // 清除token                    
                localStorage.removeItem('token');                    
                // 跳转登录页面，并将要浏览的页面fullPath传过去，登录成功后跳转需要访问的页面
                break; 
            // 404请求不存在                
            case 404:                    
            break;                
            default:                    
        }            
        return Promise.reject(error.response);        
    }       
})

const http = {
    get(url, params) {
        return instance.get(url, params)
    },
    post(url, params) {
        return instance.post(url, params)
    }
}

const instanceMars = axios.create({
    baseURL: BASE_MARS_URL,
    withCredentials: true,   //跨域请求时是否需要使用凭证,是否允许带cookie这些
    responseType: JSON,   
    timeout: 5000,  //请求超时
    headers: {
        'Content-Type': 'application/json'   
    },
}) ;

instanceMars.interceptors.request.use(config => {
    // token处理    

    //POST传参序列化
    // if (config.method === 'post') {
    //     config.data = qs.stringify(config.data);
    // }
    return config;   
}, err => {
    return Promise.reject(err)
})

instanceMars.interceptors.response.use(response => {
    if (response.status === 200) {            
        return Promise.resolve(response);        
    } else {            
        return Promise.reject(response);        
    }  
}, error => {
    if (error.response.status) {
        switch (error.response.status) {                
            // 401: 未登录                
            // 未登录则跳转登录页面，并携带当前页面的路径                
            // 在登录成功后返回当前页面，这一步需要在登录页操作。                
            case 401:                    
                break;
            // case 500:
            //     window.location.href=BASE_PANGU_UI_URL;
            //     break;
            // 403 token过期
            // 登录过期对用户进行提示                
            // 清除本地token和清空vuex中token对象                
            // 跳转登录页面                
            case 403:                     
                // 清除token                    
                localStorage.removeItem('token');                    
                // 跳转登录页面，并将要浏览的页面fullPath传过去，登录成功后跳转需要访问的页面
                break; 
            // 404请求不存在                
            case 404:                    
            break;                
            default:                    
        }            
        return Promise.reject(error.response);        
    }       
})
const httpMars = {
    get(url, params) {
        return instanceMars.get(url, params)
    },
    post(url, params) {
        return instanceMars.post(url, params)
    }
}
export default http;