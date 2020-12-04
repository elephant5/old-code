<template>
    <div class="myhomeBox_bottombar">
      <div @click="gotohome"><span class="index">&nbsp;</span>{{bartitle}}</div>
      <div class="curron"><span class="myhome"></span>个人中心</div>
    </div>
</template>

<script>
import {mapState} from 'vuex' 
export default {
    name:'bottombar',
    data() {
        return {
            bartitle:'我的权益'
        }
    },
    props:[],
    created(){
        if(localStorage.getItem('CHANNEL') == 'JD' && this.$store.state.userMsg.vipExpirationDate){
            this.bartitle = '超级会员'
        }
     },
     computed:{
         ... mapState([
             "channel",
             "actCode",
             "prjCode",
         ])
     },
    methods: {
        // gotopercenter:function(){
        //     this.$router.push({name:'percenter'})
        // },
        gotohome:function(){
            console.log(this.channel,this.actCode,this.prjCode)
            if(this.channel == 'JD' && this.bartitle == '超级会员'){
                 window.location.href = '/vip?channel='+localStorage.getItem('CHANNEL') + '&actCode=' + this.actCode
            }else{
                //1 PRJCODE和ACTCODE都为空
                if(this.prjCode){
                    window.location.href = '/home?channel='+localStorage.getItem('CHANNEL')+'&prjcode='+this.prjCode
                }else if(this.actCode){
                    window.location.href = '/home?channel='+localStorage.getItem('CHANNEL')+'&actCode='+this.actCode
                }else if(this.prjCode && this.actCode){
                    window.location.href = '/home?channel='+localStorage.getItem('CHANNEL')+'&actCode='+this.actCode +'&prjcode='+this.prjCode
                }else{
                    window.location.href = '/home?channel='+localStorage.getItem('CHANNEL')
                }
            }
            
        }
    },
}
</script>
        
