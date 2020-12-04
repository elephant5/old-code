<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App',
  created(){
    //在页面加载时读取sessionStorage里的状态信息
    if (sessionStorage.getItem("STORE") ) {
    this.$store.replaceState(Object.assign({}, this.$store.state,JSON.parse(sessionStorage.getItem("STORE"))))
    }
    //在页面刷新时将vuex里的信息保存到sessionStorage里
    window.addEventListener("beforeunload",()=>{
    sessionStorage.setItem("STORE",JSON.stringify(this.$store.state))
    })

    /*区分关闭和刷新，关闭退出登录 start*/
    // window.onload = function(){
    //   if(!window.sessionStorage["tempFlag"]){
    //     debugger
    //     $cookies.remove(this.$store.state.channel+'_loginToken')
    //     location.reload();   //不能省，强制跳到登陆页
    //   }else{
    //     window.sessionStorage.removeItem("tempFlag");
    //   }
    // }
    // window.onunload = function (){
    //   window.sessionStorage["tempFlag"] = true;
    // }
    // window.onbeforeunload = function (){
    //   window.sessionStorage["tempFlag"] = true;
    // }
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
</style>
