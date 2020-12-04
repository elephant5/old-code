// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import 'babel-polyfill'
import axios from 'axios'
import qs from 'qs'
import VueAxios from 'vue-axios'
import Vuex from 'vuex'
import store from './store/store'
import moment from 'moment'

import VueCookies from 'vue-cookies'

// import vueCity from 'vue-city-select'

// import MintUI from 'mint-ui'

Vue.use(VueAxios,axios,Vuex,VueCookies,moment)
Vue.filter('dateformat', function(dataStr, pattern = 'YYYY-MM-DD HH:mm:ss') {
  return moment(dataStr).format(pattern)
})

require ('./common/css/reset.css')
var theme_name = "default";
if(localStorage.getItem('CHANNEL')=='BOSC'){
  theme_name = "bosc";
}else if(localStorage.getItem('CHANNEL')=='CCB'){
  theme_name = "ccb";
}else if(localStorage.getItem('CHANNEL')=='ICBC'){
  theme_name = "icbc";
}
require ('./common/theme/'+theme_name+'/css/index.css')

require('./common/js/rem.js')

import{getChannge} from './common/js/common.js'
import * as custom from '@/common/js/util.js'
Object.keys(custom).forEach(item => Vue.filter(item, custom[item])) 

Vue.config.productionTip = false

Vue.prototype.$qs = qs
// axios.defaults.headers.post['Content-Type'] = 'application/json';
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
axios.defaults.headers.common['X-REQUESTED-SO-TOKEN'] = $cookies.get(localStorage.getItem('CHANNEL') + '_loginToken');
axios.defaults.withCredentials = true;
axios.defaults.baseURL = process.env.API_ROOT


router.beforeEach((to, from, next) => {//每个路由执行守卫
  if(getChannge('channel')){
    localStorage.setItem('CHANNEL',getChannge('channel').toUpperCase())
  }
  window.scrollTo(0,0)
//   next()
  if (to.meta.requireAuth) {
    document.title = to.meta.title  //title
    var token =  $cookies.get(localStorage.getItem('CHANNEL') + '_loginToken')
    if (token) { 
      next();
    }else{
      window.location.href = process.env.PASSPORT_ROOT+"/login?channel="+ localStorage.getItem('CHANNEL') + "&backurl="+ location.href //+ to.fullPath
    }
  }else{
    next();
  }
})

// http request 拦截器  统一处理axios请求和响应
axios.interceptors.request.use(
  config => {
      // 对响应数据做点什么
      return config;
  },
  err => {
      return Promise.reject(err);
  });

// http response inteceptor，当后端接口让用户重新登录
axios.interceptors.response.use(
    response => {
      if(response.data.msg=='请重新登录!'){
        if(!getChannge('channel')){
          // router.replace({
          //   path: '/other',
          //   query: {backurl: location.href}
          // })
          window.location.href = process.env.PASSPORT_ROOT+"/login?channel="+ localStorage.getItem('CHANNEL') + "&backurl="+ location.href //+ to.fullPath
        }
      }else{
        return response;
      }
      return response;
    },
    error => {
        return Promise.reject(error.response.data)   // 返回接口返回的错误信息
    });

/* eslint-disable no-new */
new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
