<template>
    <div class="myhomeBox_myorder">
        <Topheader :headtitle="headtitle" v-if="headerBarflage"></Topheader>
        <tabs :tabList="tabList" :tabIndex="tabIndex" :couponsize="couponsize" @changeTab="changeTab">
            <div class="myorderlist" slot="tabcontent" v-if="orderlist.length != 0">
                <ul class="myorderlistDiv"
                    v-infinite-scroll="loadMore"
                    infinite-scroll-disabled="isMoreLoading"
                    :infinite-scroll-immediate-check="true"
                    infinite-scroll-distance="10"
                >
                    <li v-for="order in orderlist" :key="order.id">
                        <div class="orderMsg" @click="gotoshoworder(order.id)">
                            <div class="productmsg">
                                <div class="productImg"><span class="defaultBg"><span :style="{backgroundImage:'url('+ order.goodsImg + ')',backgroundSize:'cover', backgroundPosition:'center',backgroundRepeat:'no-repeat'}"></span></span></div>
                                <div class="productName">
                                    <p>{{order.hotelName}}</p>
                                    <p v-if="order.serviceType  == 'accom'">{{order.shopItemName}}|{{order.addon}}|{{order.needs}}</p>
                                    <p v-else-if="order.serviceType.indexOf('_cpn') != -1">{{order.shopItemName}}</p>
                                    <p v-else>{{order.shopName}}</p>
                                    <!-- <p><span >{{order.giftType | getGiftTxt}}</span></p> -->
                                    <p v-if="order.serviceType.indexOf('_cpn') != -1"><span >{{order.serviceName}}</span></p>
                                     <p v-else><span v-if="order.giftType">{{order.giftType | getGiftTxt}}</span><span v-else-if="order.travelType ==2">送机</span><span v-else-if="order.travelType ==1">接机</span><span v-else-if="order.serviceType == 'medical'">{{order.shopItemName}}</span></p>
                                </div>
                            </div>
                            <div class="prostatus" style="">
                                {{order.proseStatus | transformatStatus(order.useStatus,order.payStatus,order.serviceType,order.expressStatus)|transformat}}
                            </div>
                        </div>
                        <div class="orderDate"  @click="gotoshoworder(order.id)">
                            <div><span>{{order.serviceName}}</span></div>
                            <div v-if="order.serviceType  == 'accom'">入住日期：{{order.giftDate}} {{order.giftTime}}</div>
                            <div v-else-if="order.serviceType.indexOf('_cpn') != -1"><i v-if="order.experTime">有效期: {{order.experTime}}</i></div>
                            <div v-else>预约日期：{{order.giftDate}} {{order.giftTime}}</div>
                        </div>
                        <div class="paystatus" v-if="order.payAmoney">
                            <div class="paymoneyNum">订单总额：<span>¥{{order.payAmoney}}</span></div>
                            <div class="payoneyBtn">
                                <span v-if="order.proseStatus =='2' || order.proseStatus =='3'"><i v-if="order.payStatus=='3'"  class="payOverBtn">已退款</i></span>
                                <span v-else-if="order.proseStatus=='0' & order.payStatus=='1'"><i @click="gotopay(order)">立即支付</i></span>
                                <span v-else></span>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
            <div slot="tabcontent" v-else class="nodata">暂无数据</div>
            </tabs>
        <backthird v-if='backthirdFlag'></backthird>
        <cmbPay v-if="cmbpayshow" :cmbpayValue="cmbpayValue" ref="cmbPaychild"></cmbPay>
        <icbcpay v-if="icbcpayshow" :icbcpayValue="icbcpayValue" ref="icbcPaychild"></icbcpay>
    </div>
</template>

<script>
import {mapState} from 'vuex'
import Vue from 'vue'
import tabs from '@/components/tab/tab'
import { debug } from 'util';
import{getChannge} from '@/common/js/common.js'
import backthird from '@/components/bottombar/backthird'
import cmbPay from "@/components/button_pay/cmbpay"
import icbcpay from "@/components/button_pay/icbcpay"
import Topheader  from '@/components/head/head'
import{WXbodyBottomshow} from '@/common/js/common.js'
import { Toast, Indicator,InfiniteScroll } from 'mint-ui';
Vue.use(InfiniteScroll)
let Base64 = require('js-base64').Base64;

export default {
    name:'mybasemsg',
    data() {
        return {
            tabIndex: 0,
            tabList:[
                {
                    index : 0,
                    name: "全部",
                    orderstatus: "-1",
                    payStatus:"",
                },
                {
                    index : 1,
                    name: "待处理",
                    orderstatus: 0,
                    payStatus:2,
                },
                {
                    index : 2,
                    name: "处理中",
                    orderstatus: 4,
                    payStatus:2,
                },
                {
                    index : 3,
                    name: "可使用",
                    orderstatus: 1,
                    payStatus:2,
                },
                {
                    index : 4,
                    name: "已使用",
                    orderstatus: 7,
                    payStatus:2,
                },
            ],
            orderlist: [],
            orderType: null,
            memberId : null,
            currentTab: 0,
            backthirdflag:true,
            couponsize:[],
            pageInfo:{
                page:1,
                size:5
            },
            isLoading: false, // 加载中转
            isMoreLoading: true, // 加载更多中
            noMore: false, // 是否还有更多
            headtitle:'我的订单',
            headerBarflage:true,
            cmbpayshow:false,
            cmbpayValue:'',
            icbcpayshow:false,
            icbcpayValue:'',
            backthirdFlag:false,//返回第三方按钮
        }
    },
    components:{
        tabs,
        backthird,
        Topheader,
        cmbPay,
        icbcpay
    },
    created(){
        WXbodyBottomshow()
        if(WXbodyBottomshow()){
            this.headerBarflage =false
        }else{
            this.headerBarflage = true
        }
        if(getChannge('channel')){
            localStorage.setItem('CHANNEL',getChannge('channel').toUpperCase())
            this.$store.commit('getChannel',getChannge('channel').toUpperCase())
        }
        
        if(getChannge('bottomflag')){
            localStorage.setItem('disablebottom','show')
        }else{
            localStorage.removeItem('disablebottom')
        }
        if(getChannge('backurl')){
            this.$store.commit('getBackUrl',window.location.href.split("backurl=")[1])
            $cookies.set('THIRDBACKURL',window.location.href.split('backurl=')[1],'','/', this.domainname)
        }else{
            $cookies.set('THIRDBACKURL','','','/', this.domainname)
        }
        if(localStorage.getItem('disablebottom') && localStorage.getItem('disablebottom') == 'show' ){
            this.bottomTabflag = false
        }else{
            this.bottomTabflag = true
        }
        if(this.$store.state.backUrl){
          this.backthirdFlag = true
      }else{
          this.backthirdFlag = false
      }
        
        this.orderType = this.$router.currentRoute.params.id
        if(this.orderType == 'all'){
            this.currentTab = 0;
        } else if(this.orderType == 'ordering'){
            this.currentTab = 1;
        } else if(this.orderType == 'usering'){
            this.currentTab = 2;
        } else if(this.orderType == 'over'){
            this.currentTab = 3;
        } else if(this.orderType == 'cancelled'){
            this.currentTab = 4;
        }
        this.tabIndex = this.currentTab
        this.getLoginInfo()
    },
    props:[],
     computed:{
      ... mapState([
          "channel"
      ]),
    },
    mounted(){
        if(this.channel == 'CMB'){
            this.cmbpayshow = true
        }else if(this.channel == 'ICBC'){
            this.icbcpayshow = true
        }
    },
    methods:{
        changeTab:function(tab) {
            this.tabIndex = tab.index
            this.myorderlist(tab.orderstatus,tab.payStatus)
        },
        myorderlist:function(orderstatus,paystatus,type) { // 获取产品列表
             var param ={
                proseStatus:orderstatus,
                memberId:this.memberId,
                payStatus:paystatus
            };
            var datas = {};
            datas.condition = param;
            datas.size = this.pageInfo.size;
            if (type != 'loadMore') {
                this.pageInfo.page = 1;
            }
            datas.current=this.pageInfo.page;
            this.axios.post("/mars/reservOrder/page", datas)
                .then((res)=>{
                     if(res.data.code==100){
                         let pageInfo = res.data.result;
                        if (type === 'loadMore') {
                            this.orderlist = this.orderlist.concat(pageInfo.records);
                        } else {
                            this.orderlist = pageInfo.records;
                        }
                        this.pageInfo.total = pageInfo.total
                        this.pageInfo.totalPage = Math.ceil(this.pageInfo.total / this.pageInfo.size);
                    }else{
                        // console.log(res.data.msg);
                    }
                   
                })
                .catch((error)=>{
                    // console.log(error)
                })
            this.isLoading = false
            this.isMoreLoading = false
        },
        loadMore () { // 加载更多
            this.pageInfo.page += 1 // 增加分页
            this.isMoreLoading = true // 设置加载更多中
           this.isLoading = true // 加载中
            // console.log(this.pageInfo.page, this.pageInfo.totalPage)
            if (this.pageInfo.page > this.pageInfo.totalPage) { // 超过了分页
                this.noMore = true // 显示没有更多了
                this.isLoading = false // 关闭加载中
                return false
            }
            // 做个缓冲
            setTimeout(() => {
                let index = this.tabIndex;
                let list = this.tabList;
                let tab;
                for(let i in list){
                     if(list[i].index === index){
                        tab = list[i];
                        this.myorderlist(tab.orderstatus,tab.payStatus ,'loadMore');
                        break;
                    }
                }
            }, 100)
        },
        gotoshoworder:function(orderId){
            if(this.backthirdflag){
                window.location.href='/order?reservOrderId='+orderId +'&bottomflag=disablebottom&backurl=' + this.$store.state.backUrl
            }else{
                this.$router.push({name:'order',query :{reservOrderId:orderId}})
            }

            // this.$router.push({name:'order',query :{reservOrderId:orderId}})
        },
        //获取用户信息
        getLoginInfo:function(channel){
            this.axios({
            method: 'post',
            url: '/yangjian/mem/heartBeat',
            headers: {
                'X-REQUESTED-SO-TOKEN': $cookies.get(this.channel + '_loginToken')
            }
        })
        .then(res => {//100表示已登录
            if(res.data.code == "100"){
                this.memberId = res.data.result.acid;
                this.myorderlist(this.tabList[this.tabIndex].orderstatus,this.tabList[this.tabIndex].payStatus);
                this.$store.commit("getUserInfo", res.data.result);
            }else{
                if(localStorage.getItem('disablebottom') && localStorage.getItem('disablebottom') == 'show'){
                    window.location.href = process.env.PASSPORT_ROOT+"/login?channel="+ this.channel.toUpperCase() + "&backurl="+ location.href + '?&bottomflag=disablebottom'
                } else {
                    window.location.href = process.env.PASSPORT_ROOT+"/login?channel="+ this.channel.toUpperCase() + "&backurl="+ location.href
                }
            }
        })
        .catch(error => {
          console.log(error)
        })
        },
        gotopay:function(order){
            console.log(order.id)
            //聚合支付
            this.axios({ method: "get", url: "/mars/pay/getPayParams/"+ order.id}).then(res => {
                    if(res.data.code == 100){
                        var jsondata = JSON.parse(res.data.result.params);
                        let params = {
                            "acId": jsondata.acId,
                            "amount": jsondata.amount,
                            "body": order.hotelName?order.hotelName:'',
                            "curcode": jsondata.curcode,
                            "goodsName": order.shopItemName?order.shopItemName:'',
                            "mbId": jsondata.mbId,
                            "merchantId": jsondata.merchantId,
                            "orderId": jsondata.orderId,
                            "payMethod": jsondata.payMethod,
                            "paymentType": jsondata.paymentType,
                            "source": jsondata.source
                        }
                        console.log(params)
                        //判断是否是光大-高迪安渠道 用开联通支付
                        if(this.channel == 'GDA'){
                            this.axios.post('/member/bank/getBankcardList',{"acid": jsondata.acId})
                            .then(val => {
                                if(val.data.code == 100){
                                    if(val.data.result){
                                        jsondata.id = jsondata.orderId;
                                        this.$router.push({name: "card",query:{cardlist:val.data.result,ordermsg:jsondata}});
                                    }else{
                                        this.$router.push({name: "card"});
                                    }
                                }else{
                                Toast({
                                    message: res.data.msg
                                }); 
                                }
                            })
                            .catch(error => {
                                console.log(error)
                            })
                        }else if(this.channel == 'ICBC'){
                            this.axios.post('/member/account/getMemAccount',{'acChannel':'ICBC','acId':jsondata.acId})
                                .then(res => {
                                if(res.data.code == 100){
                                    this.icbcthirdId = res.data.result.thirdId
                                    params.custId = this.icbcthirdId
                                    this.axios.post("/mars/pay/getAggregatePayParams",params,{headers: { 'Content-Type': 'application/json', 'User-Agent': 'okhttp'}}).then(res => {
                                    if(res.data.code == 100){
                                    // let paramsData = JSON.parse(res.data.result.params)  JSON.stringify(paramsData)
                                    //     paramsData.custId = this.icbcthirdId
                                    this.axios.post('/aggregatePay/pay/order',{"params": res.data.result.params,"payChannelId": 11})
                                    .then(res => {
                                        if(res.data.code == 100){
                                            this.icbcpayValue = res.data.result.icbcPayRes.form
                                            console.log(this.icbcpayValue)
                                            this.$nextTick(function(){
                                                this.$refs.icbcPaychild.icbcPaypram()
                                            });
                                        }
                                    })
                                    .catch(error => {
                                        console.log(error)
                                    })
                                    }else{
                                    Toast({
                                        message: res.data.msg
                                    });
                                    }
                                }).catch(error => {
                                    console.log(error);
                                });

                                }
                                })
                        }else if(this.channel == 'CMB'){
                            this.axios.post('/aggregatePay/pay/order',{
                                  "params": res.data.result.params,"payChannelId": 13
                              })
                              .then(res => {
                                  if(res.data.code == 100){
                                      this.cmbpayValue = res.data.result.cmbPayResVO.jsonRequestData
                                      console.log(this.cmbpayValue)
                                      this.$nextTick(function(){
                                        this.$refs.cmbPaychild.cmbPaypram()
                                      });
                                  }
                              })
                              .catch(error => {
                                console.log(error)
                              })
                        }else{
                            window.location.href = process.env.PAY_ROOT+'/home?channel='+Base64.encode(this.channel)+'&params='+ Base64.encode(encodeURIComponent(res.data.result.params))
                        }
                    }else{
                    Toast({
                        message: res.data.msg
                    });
                    }
                    
                }).catch(error => {
                    console.log(error);
                })

        }, 
    }
}
</script>
        
<style scoped>
    .nodata{text-align: center; padding:2rem 0; background: #fff; color: #999; margin-top: .3rem;}
</style>