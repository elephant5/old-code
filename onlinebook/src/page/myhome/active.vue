<template>
    <div class="myhomeBox_myactivity">
        <div class="mymember">
            <div class="active_tel disablestyle" ><span>&nbsp;</span><input type="tel" name="" id="" disabled placeholder="请输入正确的手机号" v-model="tel"></div>
            <div class="active_name" :class="disabled_flag ? 'disablestyle' : ''"><span>&nbsp;</span><input type="text" name="" id="" :disabled = "disabled_flag"  placeholder="请输入真实姓名" v-model="realName"></div>
            <div class="active_code"><span>&nbsp;</span><input type="text" name="" id="" placeholder="请输入权益激活码" v-model="actCode"></div>
            <div class="active_sure"><button @click="doActive">立即激活</button></div>
        </div>
         <br>
         <copyright :copyright="copyright"></copyright>
    </div>
</template>

<script>
import {mapState} from 'vuex'
import { Toast } from 'mint-ui';
import { Indicator } from 'mint-ui';
import copyright  from '@/components/copyright/copyright';
export default {
    name:'',
    data() {
        return {
            tel: null,
            realName: null,
            actCode: null,
            disabled_flag: false,
            memberId: null,
            copyright:'',
        }
    },
    
    components:{
        copyright,
    },
    created() {
        
    },
     computed:{
      ... mapState([
          "channel",
      ])
  },
  mounted(){
      this.getLoginInfo();
  },
    methods: {
        //获取用户信息
        getLoginInfo:function(){
            this.axios({
            method: 'post',
            url: '/yangjian/mem/heartBeat',
            headers: {'X-REQUESTED-SO-TOKEN': $cookies.get(localStorage.getItem('CHANNEL') + "_loginToken")}
        })
        .then(res => {
            console.log(res);
            if(res.data.code == "100"){
                //手机号
                this.tel = res.data.result.mobile;
                //姓名
                if(res.data.result.mbName!= null && res.data.result.mbName!= ''){
                    this.realName = res.data.result.mbName;
                    this.disabled_flag = true;
                }
                this.memberId = res.data.result.acid;
            }else{
                window.location.href = process.env.PASSPORT_ROOT+"/login?channel="+ localStorage.getItem('CHANNEL') + "&backurl="+ location.href  
            }
        })
        .catch(error => {
          console.log(error)
        })
      },  
      
      // 激活权益
      doActive:function(){
        if(this.realName==null){
            Toast({
                message: '请输入真实姓名'
            });
            return;
        }
        if(this.actCode==null){
            Toast({
                message: '请输入权益激活码'
            });
            return;
        }
        Indicator.open({
            text: '加载中...',
            spinnerType: 'fading-circle'
        });
        this.axios({
            method: 'post',
            url: '/mars/giftCode/activeActCode',
            data: {"actCode": this.actCode, "memberId": this.memberId, "activeRemarks": this.realName}
        })
        .then(res => {
            Indicator.close();
            if(res.data.code == "200"){
                Toast({
                    message: res.data.msg
                });
            }else{
                Toast({
                    message: '激活码激活成功'
                });
                console.log(res.data.result)
                if(this.$store.state.channel == 'JD'){
                    this.checkVipStatus(res.data.result.goodsId,res.data.result.actCode)
                }else{
                    //1 PRJCODE和ACTCODE都为空
                    if(!this.$store.state.prjCode && !this.$store.state.actCode){
                        window.location.href = '/home?channel='+this.$store.state.channel
                    } 
                    //2 PRJCODE 不为空 ACTCODE为空
                    else if(this.$store.state.prjCode && !this.$store.state.actCode) {
                        window.location.href = '/home?channel='+this.$store.state.channel+'&prjCode='+this.$store.state.prjCode
                    }
                    //3 PRJCODE 为空 ACTCODE不为空
                    else if(!this.$store.state.prjCode && this.$store.state.actCode) {
                        window.location.href = '/home?channel='+this.$store.state.channel+'&actCode='+this.$store.state.actCode
                    }
                    //4 PRJCODE 不为空 ACTCODE 不为空
                    else {
                        window.location.href = '/home?channel='+this.$store.state.channel+'&prjCode='+this.$store.state.prjCode+'&actCode='+this.$store.state.actCode
                    }
                }
            }
        })
        .catch(error => {
          console.log(error)
        })
      }, 
      
    //判断用户是否是超级会员  此处有疑问，如一个渠道即有会员项目，又有其他项目，待解决
    checkVipStatus(goodsId,actCode){
        this.axios({
            method: 'post',
            url: '/member/vip/activateVip',
            data: {"acChannel": this.$store.state.channel, "acId":this.memberId}
        })
        .then(res => { 
            if(res.data.result == 'SUCCESS'){
                window.location.href = '/vip?channel='+this.$store.state.channel +'&actCode='+ actCode
            }else{
                Toast({
                    message: res.data.msg
                });
            }
        })
        .catch(error => {
            console.log(error)
        })
    },

    },
}
</script>


