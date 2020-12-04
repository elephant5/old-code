<template>
    <div class="bottombar">
      <div class="curron" @click="gotohome"><span class="index">&nbsp;</span>{{bartitle}}</div>
      <div @click="gotopercenter"><span class="myhome"></span>个人中心</div>
    </div>
</template>

<script>

export default {
    name:'bottombar',
    data() {
        return {
            bartitle:'我的权益',
            jdflage:false
        }
    },
    created(){
        if(localStorage.getItem('CHANNEL') == 'JD' && this.$store.state.userMsg.vipExpirationDate && this.$store.state.goodsId == '10891'){
            this.bartitle = '超级会员'
        }
     },
    props:[],
    methods: {
        gotopercenter:function(){
            this.$router.push({name:'percenter'})
        },
        gotohome:function(){
            if(localStorage.getItem('CHANNEL') == 'JD' && this.bartitle == '超级会员'){
               window.location.href = '/vip?channel=' + localStorage.getItem('CHANNEL') + '&actCode=' + this.$store.state.actCode
            }
        }
    },
}
</script>
        
