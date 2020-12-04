// 盘古ui地址
export let BASE_PANGU_UI_URL = `http://bigan.icolourful.cn`;  
// // 接口基础地址
export let BASE_URL = 'http://bigan.icolourful.cn';

//线下退款地址
export let REFUND_URL = 'http://127.0.0.1:8080/#/refund';

// //盘古ui地址  
// export let BASE_PANGU_UI_URL = `http://localhost:3000`;  
// 接口基础地址
// export let BASE_URL = 'http://127.0.0.1:9999'; 
if(process.env.NODE_ENV === 'sit'){
    BASE_URL = 'http://bigan.icolourful.cn';
    BASE_PANGU_UI_URL = 'http://pangu.icolourful.cn';
    REFUND_URL = BASE_PANGU_UI_URL+'/cmallui/#/refund';
}else if(process.env.NODE_ENV === 'production'){
    BASE_URL = 'https://bigan.icolourful.com';
    BASE_PANGU_UI_URL = 'https://pangu.icolourful.com';
    REFUND_URL = BASE_PANGU_UI_URL+'/cmallui/#/refund';
}

//访问天眼的base url
export const BASE_ADMIN_URL = `${BASE_URL}/admin`;
//盘古地址
export const BASE_PANGU_URL = `${BASE_URL}/ts`;

//登录地址
export const BASE_LOGIN_URL = `${BASE_URL}/gd`;

//水星
export const BASE_MARS_URL = `${BASE_URL}/mars`;

//支付宝
export const BASE_ALIPAY_URL = `${BASE_URL}/alipay`;

//女娲
export const BASE_NUWA_URL = `${BASE_URL}/nuwa`;

// 全局上传接口地址
export const UPLOAD_URL = `${BASE_PANGU_URL}/sysFile/upload`;



