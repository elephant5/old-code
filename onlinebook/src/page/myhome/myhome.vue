<template>
    <div class="myhomeBox_index">
        <Topheader :headtitle="headtitle" v-if="headerBarflage"></Topheader>
       <div class="perindex">
           <div class="perMsgbox">
               <div class="mybaseMsg" @click="gotomybasemsg">
                   <div class="myphoto">
                       <span><img :src="headPhoto"></span>
                   </div>
                   <div class="myname">
                       <p class="name">{{username}}</p>
                       <p class="tel">{{tel}}</p>
                   </div>
               </div>
               <div class="memberMsgBox" v-if="vipExpirationDate">
                   <div class="memberDate">
                       <p><span class="memberDateshow">&nbsp;</span>{{vipExpirationDate}}到期</p>
                       <p><span class="memberMoenyshow">&nbsp;</span>会员一年预计可省1200元</p>
                   </div>
                   <div class="memberMoney">
                       <!-- <span @click="buyMemberBtn">续费</span> -->
                   </div>
               </div>
           </div>
           <div v-if="channel == 'KWX'" style="padding:.3rem .3rem .1rem .3rem; background:#fff; margin-bottom:.2rem;" @click="kwxdailog"><img src="../../common/images/banner.png" alt="" srcset="" width="100%" style="border-radius:.1rem;"></div>
           <div class="perMsgbox">
               <div class="titleDiv">
                   <div class="titleName">我的订单</div>
                   <div class="more" @click="myorderlist('all')">查看全部订单</div>
               </div>
               <div class="orderBox">
                   <p  @click="myorderlist('ordering')">
                       <span class="appointment"></span>
                       待处理
                   </p>
                   <p @click="myorderlist('usering')">
                       <span class="use"></span>
                       处理中
                   </p>
                   <p @click="myorderlist('over')">
                       <span class="used"></span>
                       可使用
                   </p>
                   <p @click="myorderlist('cancelled')">
                       <span class="cancelled"></span>
                       已使用
                   </p>
               </div>
                <!-- <ul class="waitingBox" style="display:none;">
                    <li>
                        <div class="orderstatus">可使用</div>
                        <div class="orderMsg">
                            <div class="orderPic"><span></span></div>
                            <div class="orderName">
                                <p class="name">波特曼餐厅</p>
                                <p class="date">预约日期：2018-08-01 12:00</p>
                            </div>
                        </div>
                    </li>
                </ul> -->
           </div>

           <div class="perMsgbox" v-if="vipExpirationDate">
               <div class="mygfit" @click="mygift">
                   <div class="modeName">我的权益</div>
               </div>
           </div>
           <div class="perMsgbox" >
               <div class="couponBox" @click="mycouponmsg">
                   <div class="modeName">优惠券</div>
               </div>
           </div>
           <div class="perMsgbox">
               <div class="quanyiBox" @click="myactivitymsg">
                   <div class="modeName">激活权益</div>
               </div>
           </div>
           <!-- <div class="perMsgbox" style="display:none;">
               <div class="exchangeBox">
                   <div class="modeName">权益转赠</div>
               </div>
           </div> -->
           <div class="perMsgbox" v-if="vipExpirationDate">
               <div class="memberrulue" @click="aboutvip">
                   <div class="modeName">会员章程</div>
               </div>
           </div>
           <div class="perMsgbox" v-if="vipExpirationDate">
               <div class="membertel" >
                   <div class="modeName"><a href="Tel:400-636-1881" style="color:#666;text-decoration: none;">客服热线</a></div>
               </div>
           </div>
           <div class="perMsgbox" v-if="vipExpirationDate">
               <div class="memberquestion" >
                   <div class="modeName"><a href="https://www.wjx.cn/jq/67338552.aspx"  style="color:#666;text-decoration: none;">问题反馈</a></div>
               </div>
           </div>
           <div class="perMsgbox">
               <div class="memberloginout" @click="loginout">
                   <div class="modeName">退出登录</div>
               </div>
           </div>
           <br>
         <!-- <copyright :copyright="copyright"></copyright> -->
           <div style="height:.8rem;"></div>

           <mybottombar v-if="bottomTabflag"></mybottombar>
       </div>
       <transition name="fade">
        <router-view></router-view>
       </transition>

        <div class="kwxdailogshow" v-if="kwxflag">
            <div class="kwddailogmsgBox">
                <div class="closeDailog"><span @click="closedailog">&nbsp;</span></div>
                <div class="kwdtitleDiv">即将离开客乐芙登录众网小贷<br>请确认授权以下信息</div>
                <div class="kwdtips">使用您的客乐芙手机号登录该应用</div>
                <div class="kwdtips" style="font-size:.24rem; text-align:left;"><input type="checkbox" v-model="serverbookFlag" style="vertical-align: middle;"> 您已阅读并同意<a href="http://zwdebitdev.imassbank.com/zwRegister">《众网小贷注册协议》</a><a href="http://zwdebitdev.imassbank.com/zwPrivacy">《众网小贷用户隐私政策》</a>，首次登录自动注册</div>
                <div class="kwdbtnBox">
                    <div :class="serverbookFlag?'activeshow':'disableshow'" @click="goImassbank">授权登录</div>
                </div>
                
            </div>
        </div>

    </div>
</template>

<script>
import {mapState} from 'vuex'
import mybottombar  from '@/components/bottombar/mybottombar';
import { Picker } from 'mint-ui';
import { Toast } from 'mint-ui';
import qs from 'qs'
import { setInterval } from 'timers';
import Topheader  from '@/components/head/head';
import copyright  from '@/components/copyright/copyright';
import{WXbodyBottomshow,getChannge} from '@/common/js/common.js'

export default {
    name:'myhome',
    data() {
        return {
            headPhoto: null,
            username: null,
            tel: null,
            bottomTabflag:true,
            headtitle:'个人中心',
            headerBarflage:true,
            vipExpirationDate:'',
            copyright:'',
            domainname: location.host.split(".")[1]+"."+location.host.split(".")[2], //一级域名
            serverbookFlag:true,
            kwxflag:false,
        }
    },
    components:{
        mybottombar,
        Topheader,
        copyright
    },
    created(){
        WXbodyBottomshow()
        if(WXbodyBottomshow()){
            this.headerBarflage =false
        }else{
            this.headerBarflage = true
        }
        if(localStorage.getItem('disablebottom') && localStorage.getItem('disablebottom') == 'show' ){
            this.bottomTabflag = false
        }else{
            this.bottomTabflag = true
        }
        this.getLoginInfo()
        
    },
   computed:{
      ... mapState([
          "channel",
      ])
  },
   mounted() {
       
    },
   watch:{//对自定义组件的监控

   },
    props:[],
    methods:{
        buyMemberBtn:function(channel){
            if(this.channel == 'JD'){
                window.location.href='https://benefits.icolourful.cn/jd/member.html'
            }else{
                Toast({
                    message: '亲，您还在购买会员的路上呢，请耐心等待'
                });
            }
        },
        gotomybasemsg:function(){
            this.$router.push({name:'personmsg'})
        },
        myorderlist:function(type){
            this.$router.push({name:'myorder',params:{id:type}})
        },
        mycouponmsg:function(){
            this.$router.push({name:'mycoupon'})
        },
        myactivitymsg:function(){
            this.$router.push({name:'mymember'})
        },
        mygift:function(){
            this.$router.push({name:'mygift'})
        },
        aboutvip:function(){
            this.$router.push({name:'aboutvip'})
        },
        loginout:function(){
             $cookies.set(this.channel + '_loginToken', '','','/', this.domainname);
             $cookies.remove(this.channel + '_loginToken');
             window.location.href='/percenter'
        },
        //获取用户信息
        getLoginInfo:function(){
            this.axios({
            method: 'post',
            url: '/yangjian/mem/heartBeat',
            headers: {'X-REQUESTED-SO-TOKEN': $cookies.get(localStorage.getItem('CHANNEL')+'_loginToken')}
        })
        .then(res => {//100表示已登录
            if(res.data.code == "100"){
                this.headPhoto = res.data.result.headImgUrl
                //手机号
                this.tel = res.data.result.mobile;
                //昵称
                this.username = res.data.result.acName;
                this.$store.commit("getUserInfo", res.data.result);
                if(localStorage.getItem('CHANNEL') == 'JD'){
                    this.getVipStateinfo(res.data.result.acid)
                }
            }else{
                window.location.href = process.env.PASSPORT_ROOT+"/login?channel="+ localStorage.getItem('CHANNEL') + "&backurl="+ location.href  
            }
        })
        .catch(error => {
            console.log(error)
        })
      }, 
      
      //获取用户是否是超级会员
        getVipStateinfo:function(acid){
            this.axios({
            method: 'post',
            url: '/member/account/getMemAccount',
            data:{acId:acid}
        })
        .then(res => {
            if(res.data.code == "100"){
                this.$store.commit('getMyVipmsgList',res.data.result)
                this.vipExpirationDate = res.data.result.vipExpirationDate
            }
        })
        .catch(error => {
            console.log(error)
        })
      },
      goImassbank:function(){
         this.axios({
            method: "post",
            url: "/yangjian/mem/heartBeat-helife"
        })
            .then(res => {
                this.axios({
                method: 'post',
                url: '/mars/imassbank/unionlogin',
                data:qs.stringify({mobile:res.data.result.mobile,name:res.data.result.acName?res.data.result.acName:res.data.result.mobile}),
            })
            .then(suc => {
                if(suc.data.code == "100"){
                    window.location.href=suc.data.result
                }else{
                    
                }
            })
            .catch(error => {
                console.log(error)
            })
        })
        .catch(error => {
        console.log(error);
        });   
      },
      kwxdailog:function(){
          this.kwxflag = true
      },
      closedailog:function(){
          this.kwxflag = false
      },


    },
    
}
</script>

<style scoped>
    .kwxdailogshow{position: fixed; background: rgba(0, 0, 0, .5); left: 0; top: 0; width: 100%; height: 100%; z-index: 99;}
    .kwddailogmsgBox{background: #fff; width: 80%; text-align: center; position: fixed; left: 10%; top: 50%; transform: translateY(-50%); border-radius: .1rem; padding: .3rem; box-sizing: border-box;}
    .kwddailogmsgBox .kwdtitleDiv{font-weight: bold; padding:.2rem 0; font-size: .29rem; line-height: .52rem;}
    .kwddailogmsgBox .kwdtips{color: #999; padding: .1rem 0;}
    .kwddailogmsgBox .kwdtips a{color: #f60; margin: 0 .1rem; line-height: .4rem;}
    .kwddailogmsgBox .kwdbtnBox{padding-bottom:.3rem; padding-top:.2rem;}
    .kwddailogmsgBox .activeshow{margin: 0 .2rem; background:#ee7d32; color: #fff; padding: .2rem 0; border-radius: .6rem; font-size: .3rem;}
    .kwddailogmsgBox .disableshow{margin: 0 .2rem; background:#f3f3f3; color: #999; padding: .2rem 0; border-radius: .6rem; font-size: .3rem;}
    .kwddailogmsgBox .closeDailog{text-align: right; padding: .2rem .2rem; position: absolute; top: .1rem; right: .1rem;}
    .kwddailogmsgBox .closeDailog span { display: inline-block; width: .32rem; height: .32rem; background: url(../../common/images/close.png) center no-repeat; background-size: 80%; }
</style>
