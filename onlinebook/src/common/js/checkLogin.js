import Vue from 'vue'
import axios from 'axios'
import router from '@/router/index.js'
Vue.use(axios)
axios.defaults.headers.post['Content-Type'] = 'application/json';
axios.defaults.withCredentials = true;
function checklogins(str) { // 验证登陆
    Vue.axios({
        method: "post",
        url: "/yangjian/mem/heartBeat/",
        headers: {
            "X-REQUESTED-SO-TOKEN": localStorage.getItem(
                "101_loginToken"
            )
        }
    }).then(res => {
        if (res.data.code == "200") {
            window.location.href = "https://login.colourfulchina.com/login?channel=101&backurl=http://" + str;
        } 
    });
    return true;
}

export { //很关键
    checklogins,
}      