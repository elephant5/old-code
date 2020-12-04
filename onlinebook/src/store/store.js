import Vue from 'vue'
import Vuex from 'vuex'
import defaultState from './state/state'
import mutations from './mutations/mutations'
import getters from './getters/getters'

Vue.use(Vuex)

const store = new Vuex.Store({
    // strict:true,
    state: defaultState,
    mutations,  //赋值
    getters  //处理
})


export default store