<template>
  <div class="shoppingBox">
    <Topheader :headtitle="headtitle" v-if="headerBarflage"></Topheader>
    <shoppingcoupon :mealSection="mealSection"
      :valueDate="valueDate"
      v-if="sysServicetype.indexOf('_cpn') != -1"
      :reservOrderVo="reservOrderVo"
      :memberInfo="memberInfo"
      :tipText="tipText"
      :linkedMemeList="linkedMemeList"
      :linkedNamelist="linkedNamelist"
      :sysServicetype="sysServicetype"
      ref="myChild"></shoppingcoupon>
    <shoppingaccom
      v-if="sysServicetype=='accom'"
      :reservOrderVo="reservOrderVo"
      :memberInfo="memberInfo"
      :memberCertificate="memberCertificate"
      :tipText="tipText"
      :myInfo="myInfo"
      :certificateNumber="certificateNumber"
      :linkedMemeList="linkedMemeList"
      :linkedNamelist="linkedNamelist"
      :onlinePayFlag="onlinePayFlag"
      ref="myChild"
      :setting="setting"
      :bookPrice="bookPrice"
      :channel="channel"
    ></shoppingaccom>
    <shoppingbuffet
      :mealSection="mealSection"
      :valueDate="valueDate"
      v-if="sysServicetype=='buffet' || sysServicetype=='tea' || sysServicetype=='setmenu'"
      :reservOrderVo="reservOrderVo"
      :memberInfo="memberInfo"
      :tipText="tipText"
      :linkedMemeList="linkedMemeList"
      :linkedNamelist="linkedNamelist"
      :onlinePayFlag="onlinePayFlag"
      :onlySelf="onlySelfshow"
      :channel="channel"
      ref="myChild"
    ></shoppingbuffet>
    <shoppinggym
      :mealSection="mealSection"
      :valueDate="valueDate"
      v-if="sysServicetype=='gym' || sysServicetype=='spa' || sysServicetype=='drink'"
      :reservOrderVo="reservOrderVo"
      :memberInfo="memberInfo"
      :tipText="tipText"
      :onlinePayFlag="onlinePayFlag"
      :onlySelf="onlySelfshow"
      ref="myChild"
    ></shoppinggym>
    <shoppingtrip
      :mealSection="mealSection"
      :valueDate="valueDate"
      v-if="sysServicetype=='car'"
      :reservOrderVo="reservOrderVo"
      :memberInfo="memberInfo"
      :tipText="tipText"
      :onlinePayFlag="onlinePayFlag"
      :onlySelf="onlySelfshow"
      ref="myChild"
    ></shoppingtrip>
    <shoppingmedical
      :mealSection="mealSection"
      :valueDate="valueDate"
      v-if="sysServicetype=='medical'"
      :reservOrderVo="reservOrderVo"
      :memberInfo="memberInfo"
      :tipText="tipText"
      :onlinePayFlag="onlinePayFlag"
      :onlySelf="onlySelfshow"
      ref="myChild"
    ></shoppingmedical>

    <div class="shoppingnode"   v-if="sysServicetype == 'car'">
      <div class="shoppingnodeBox" style="margin-bottom:0; padding-bottom:0">留言备注</div>
      <div class="textareaBox"  style="padding-bottom:.1rem;">
        <textarea
          class="textarea"
          placeholder="您可以在这里填写特殊要求"
          v-model="remark"
          name="remark"
          style="width:86.5%; border:solid .01rem #d7d7d7; margin:.3rem;"
        ></textarea>
      </div>
    </div>

    <div class="shoppingnode"   v-if="sysServicetype == 'medical'">
      <div class="shoppingnodeBox" style="margin-bottom:0; padding-bottom:0">病情描述</div>
      <div class="textareaBox"  style="padding-bottom:.1rem;">
        <textarea
          class="textarea"
          placeholder="您可以在这里填写病情描述"
          v-model="remark"
          name="remark"
          style="width:86.5%; border:solid .01rem #d7d7d7; margin:.3rem;"
        ></textarea>
      </div>
      <div class="shoppingnodeBox" style="margin-bottom:0; padding-bottom:0; border-top:solid .2rem #f4f4f4; border-bottom:solid .2rem #f4f4f4; display:block;">
        服务流程
        <div style="padding-top:.2rem; padding-bottom:.3rem; color:#999; line-height:.42rem;">
          1：就诊人登记成功后方可预约就诊服务；<br>
          2：就诊人至少提前一天在线提交专家号预约申请； <br>                 
          3：（电话初诊）客服回访预订人，根据其健康情况，综合乙方专家意见，为客户提供合作医院网络中知名医院及对症专家门诊预约服务，并告知预约信息和注意事项；<br> 
          4：确认就诊信息后2个工作日内短信回复预约情况，5个工作日内安排专家门诊 ； <br>                 
          5：就诊前一天客服和客户电话提醒并确认就诊信息； <br>                   
          6：就诊当天陪诊员提前到达医院等待客户； <br>                   
          7：陪诊员帮助客户挂号取号、排队缴费； <br>                   
          8：就诊过程中所产生的医疗费用由客户自理，如挂号费、检查费、手术费、治疗费、药品费等第三方费用。
        </div>
      </div>
    </div>

    
  
  <div class="shoppingnode"  v-if="sysServicetype == 'buffet'">
      <div class="shoppingnodeBox">
        <div class="titleDiv">特殊需求</div>
        <div class="textareaBox">
          <div>
          <span v-for="(item,index) in buffet_best" @click="selectradiovalue(item.name,index)" :class="curronbest == index ? 'curron' : '' " :key='item.name'>{{item.name}}</span>
          </div>
        </div>
      </div>
      <p>酒店会尽量满足您的需求，但不能保证</p>
    </div>

    <div  v-on:click="gotocoupon" style="margin-top:.6rem;" :class="productPrice ? 'shoppingBox' :'shoppingBox disable'">
            <!-- <div class="couponTitle">优惠</div> -->
            <div class="couponlist">
                <div>优惠券</div>
                <div>
                  <div v-if="cpnInfoCnt>0">
                    <i v-if="cpnInfoCnt==1" >
                        <span>{{couponName}}</span>
                    </i>
                    <i v-else>
                      <span>{{cpnInfoCnt}}张可用优惠券</span>
                    </i>
                  </div>
                  <div v-else>
                    无可用优惠券
                  </div>
                </div>
            </div>
            <!-- <div class="couponlist">
                <div>免费赠送</div>
                <div>5折咖啡券</div>
            </div> -->
    </div>

  <div class="shoppingnode" style="padding:.2rem .3rem;" v-if="channel=='BOSC' || channel=='ICBC'">
    <div class="inputlist">
      <div>我已知晓<router-link to="">《服务规则》</router-link></div>
      <div><span :class="paybtnflag?'rulurserverbox':'disableshow'" @click="payBtnstart"><span></span></span></div>
    </div>
  </div> 

  <div class="couponDiv" v-if="couponlistFlag">
    <div class="couponMsgBox">
        <div class="selectcouponbox">
            <div>选择可用优惠券</div>
            <div><span class="closecouponbtn" @click="closeCoupon"></span></div>
        </div>
        <div class="couponTip">每个订单只可用一张优惠券</div> 
        <ul class="couponlistMsg">
            <li v-for="(item, index) in cpnDiscountsInfoList" @click="selsect2(index,item.couponsType,$event)" :class="{curron:index == curronindex2}" :key='item' >
                <div class="couponlistType">
                  <div class="couponprice"><em>{{item.discountRatio}}</em>折<p>{{item.upperAmount==0?"无限制":"最高可减"+item.upperAmount+"元"}}</p></div>
                    <div>
                        <p class="noteDiv">{{item.name}}</p>
                        <p class="noteDate">有效期: {{item.validStartTime.split(' ')[0]}} - {{item.experTime.split(' ')[0]}}</p>
                    </div>
                  </div>
                <div class="couponlistSelect"><span></span></div>
            </li>

            <li v-for="(item, index) in cpnVoucherInfoList"   @click="selsect(index,item.couponsType,$event)" :class="{curron:index == curronindex}"  :key='item' >
              <div class="couponlistType">
                <div class="couponprice">￥<em>{{item.worth}}</em>元<p>{{item.floorAmount==0?"无门槛":"满"+item.floorAmount+"元可用"}}</p></div>
                  <div>
                      <p class="noteDiv">{{item.name}}</p>
                      <p class="noteDate">有效期: {{item.validStartTime.split(' ')[0]}} - {{item.experTime.split(' ')[0]}}</p>
                  </div>
                </div>
              <div class="couponlistSelect"><span></span></div>

            </li>
            
           
            <li @click="usecoupon" :class="nocouponflag?'curron':''">
               <div class="couponlistType">
                    <div>不使用优惠券</div>
                </div>
                <div class="couponlistSelect"><span></span></div>
            </li>
        </ul>
        <!-- <div class="couponbtnDiv">使用</div> -->
    </div>
  </div>
    <cmbPay v-if="cmbpayshow" :cmbpayValue="cmbpayValue" ref="cmbPaychild"></cmbPay>
    <icbcpay v-if="icbcpayshow" :icbcpayValue="icbcpayValue" ref="icbcPaychild"></icbcpay>
    <div style="height:1.4rem"></div>
    <button_pay v-if="paybtnflag"
      :gopay="gopay"
      :totalAmount="reservOrderVo.totalAmount"
      :discountAmount="reservOrderVo.discountAmount"
      :productPrice="productPrice"
      :buyBtnTitle="buyBtnTitle"
      :payMoneyBtnTitle="payMoneyBtnTitle"
      :paybtnflag="paybtnflag"
      :storePrice="storePrice"
      :onlinePrice="onlinePrice"
      :giftType="reservOrderVo.giftType"
      :num="peopleNum"
      :bookPrice="bookPrice"
      :btnDisable="btnDisable"
      :totalNum="totalNum"
      :sysServicetype="sysServicetype"
      :oldPrice="realPrice"></button_pay>
   <backthird v-if='backthirdFlag'></backthird>
  </div>
</template>

<script>
import {mapState} from 'vuex'
import { Toast } from "mint-ui";
import button_pay from "@/components/button_pay/button_pay";
import shoppingcoupon from "@/components/bookfrom/coupon";
import shoppingaccom from "@/components/bookfrom/accom";
import shoppingbuffet from "@/components/bookfrom/buffet";
import shoppinggym from "@/components/bookfrom/gym";
import shoppingtrip from "@/components/bookfrom/trip";
import shoppingmedical from "@/components/bookfrom/medical";
import cmbPay from "@/components/button_pay/cmbpay";
import icbcpay from "@/components/button_pay/icbcpay";
// import shoppingspa from '@/components/bookfrom/spa'
import Topheader  from '@/components/head/head';
import backthird from '@/components/bottombar/backthird';
import { WXbodyBottomshow,getChannge,formateDate } from "@/common/js/common.js";
import { keepAlive } from "@/common/js/bosLogin.js";
import { debug, debuglog } from "util";
let Base64 = require('js-base64').Base64;
export default {
  name: "shopping",
  data() {
    return {
      reservOrderVoNew: {},
      sysServicetype: this.$store.state.sysService,
      accomName: "-",
      channel:localStorage.getItem('CHANNEL'),//渠道
      memberInfo: {},
      juageMember: {},
      cancelPolicy: "", //取消政策
      tipText: "", //对应取消政策的文案
      // :idNumber="memberInfo.idNumber"  :passportNum="memberInfo.passportNum" :mobile="memberInfo.mobile" :passportName="memberInfo.passportName"
      myInfo: {},
      valueDate: [],
      mealSection: "",
      btnDisable: false,
      setting: {},
      productPrice:'',
      bookPrice:"",
      buyBtnTitle:'',
      payMoneyBtnTitle:'',
      paybtnflag:false,
      memberCertificate:{},
      certificateNumber:"",
      linkedMemeList:[],
      linkedNamelist:[],
      storePrice:{},
      onlinePrice:{},
      peopleNum:0,
      totalNum:0,
      buffet_best:[{name:'需要儿童座椅', type:0}, {name:'无需求', type:1}],
      curronbest:-1,
      coupounNum:'',//券批次号
      discountNum:'',//优惠券编码
      upperAmount:'',//最大优惠折扣
      discountRatio:'',//折扣
      couponName:'',
      terminal:'',//终端
      cpnDiscountsInfoList:[], //折扣券
      cpnVoucherInfoList:[], //抵用券
      cpnId:'',// 最优券编号
      couponlistFlag:false,
      curronindex:-1,
      curronindex2:-1,
      cpnInfoCnt:0,
      domainname: location.host.split(".")[1]+"."+location.host.split(".")[2], //一级域名,
      realPrice:'',
      goodsPrice:'',
      nocouponflag:false,
      selecouponflag:true,
      onlinePayFlag: false,
      onlySelf:null,
      onlySelfshow:'',
      headtitle:'订单填写',
      headerBarflage:true,
      bottomTabflag:true,
      discountrate: this.$store.state.discountRate?this.$store.state.discountRate:'',
      discountPrice:'',
      cmbpayshow:false,
      cmbpayValue:'',
      icbcpayshow:false,
      icbcpayValue:'',
      remark:'',//备注信息 
      backthirdFlag:false,//返回第三方按钮
    };
  },
  created() {
    WXbodyBottomshow();
    if(WXbodyBottomshow()){
        this.headerBarflage =false
    }else{
        this.headerBarflage = true
    }
    if(this.$store.state.channel == 'CMB'){
        this.cmbpayshow = true
    }else if(this.$store.state.channel == 'ICBC'){
        this.icbcpayshow = true
    }
    if(getChannge('backurl')){
        this.$store.commit('getBackUrl',window.location.href.split("&backurl=")[1])
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
    this.getLoginInfo();
     //订单来源
    let ua = window.navigator.userAgent.toLowerCase()
      if (ua.match(/MicroMessenger/i) == 'micromessenger') {
          this.$store.commit('getBookorderFrom',this.channel+'_WECHAT')
          // return true
      }else{
          // 2.2微信外
          this.$store.commit('getBookorderFrom',this.$store.state.channel+'_APP')
          // return false
      }
    

    this.init();
    var array = this.$store.state.startDate.split(",");
    var length = array.length;
    var value = new Array(length);
    for (var i = 0; i < length; i++) {
      value[i] = parseInt(array[i]);
    }
    this.valueDate = value;
    var serviceType = this.reservOrderVo.serviceType;
    if(serviceType=='buffet'||serviceType=='setmenu'||serviceType=='tea'||serviceType=='gym'||serviceType=='spa'){
      var mealSection = this.setTimes(this.$store.state.openTime, this.$store.state.closeTime)
      this.mealSection = !!mealSection ? mealSection : [];
    }
    this.getSetting();
    this.reservOrderVo.exchangeNum = 1;//初始化
    if(this.$store.state.channel == 'BOSC'){
      setInterval(()=>{keepAlive()},60000);
    }
    this.reservOrderVo.reservRemark = ''
    
    // this.getMemCpns()
    
    

  },
  watch:{
    realPrice(val,oldVal){
      this.getMemCpns()
    }
  },
  computed:{
    ... mapState([
        "sysService",
        "bookorderFrom",
        "discountRate",
        "reservOrderVomsg",
        "surplusFreeTimes", //出行免费次数
    ]),
  },

 mounted(){ 
    if(this.reservOrderVo.serviceType == 'object_cpn'){
          this.$refs['myChild'].expressMsg()
      }
    this.sysServicetype = this.sysService
    this.reservOrderVo.orderSource = this.bookorderFrom
    this.discountrate = this.discountrate
 },
  components: {
    button_pay,
    shoppingcoupon,
    shoppingaccom,
    shoppingbuffet,
    shoppinggym,
    shoppingtrip,
    shoppingmedical,
    Topheader,
    backthird,
    cmbPay,
    icbcpay
  },
  methods: {
    init: function() {
      //读取缓存
      this.reservOrderVo = this.reservOrderVomsg;
      this.reservOrderVo.productGroupProductId = this.$store.state.productGroupProductId;
      this.cancelPolicy = this.$store.state.cancelPolicy;
      if (!!this.reservOrderVo.discountAmount) {
        this.reservOrderVo.discountAmount = 0;
      }
      this.reservOrderVo.giftCodeId = this.$store.state.unitId
      this.reservOrderVo.serviceType = this.$store.state.sysService
      this.reservOrderVo.salesChannelId = this.$store.state.salesChannelId
      this.reservOrderVo.productGroupId = this.$store.state.groupId
      this.setTipText(this.cancelPolicy)
      this.reservOrderVo.equitySubNum = 1 //权益扣减次数  默认值为1
    //todo 0
      this.reservOrderVo.totalAmount=0;
      this.reservOrderVo.discountAmount=0;
      //免费权益次数特殊处理
      if(Number(this.$store.state.surplusFreeTimes)-1 >= 0 ){
          this.reservOrderVo.useFreeCount = 1
      }else{
          this.reservOrderVo.useFreeCount = 0
      }
      console.log(this.reservOrderVo)

    //type
      
    },
    
    getMemCpns:function(){
      //获取用户可使用优惠券列表
      this.axios.post('/coupons/common/getMemCpnListByCondition',{
        "amount":this.realPrice == 0 ?null:this.realPrice, //预约需支付金额
        "acId":this.memberInfo.acid,
        "statusList":[0], //未使用
        "goodsId":this.reservOrderVo.goodsId,
        "productGroupId":this.reservOrderVo.productGroupId,
        "productId":this.reservOrderVo.productId,
        "productGroupProductId":this.reservOrderVo.productGroupProductId,
        "useType":1, //可使用场景:0:购买时可用;1:预约时可用
        "limitType":1,
      })
      .then(res => {
          if(res.data.code == 100 && res.data.msg == 'ok'){
            var cvList = []  //筛选出可使用抵用券列表
            var cdList = []  //筛选出可使用折扣券列表
            if(res.data.result.cpnVoucherInfoList.length > 0){
              cvList = res.data.result.cpnVoucherInfoList.filter(function(item){
                  if(Date.parse(formateDate(new Date(item.validStartDate))) <= Date.parse(formateDate(new Date())) && Date.parse(formateDate(new Date())) <= Date.parse(formateDate(new Date(item.experDate)))){
                      return item;
                  }
              });
            }
            if(res.data.result.cpnDiscountsInfoList.length > 0){
              cdList = res.data.result.cpnDiscountsInfoList.filter(function(item){                    
                  if(Date.parse(formateDate(new Date(item.validStartDate))) <= Date.parse(formateDate(new Date())) && Date.parse(formateDate(new Date())) <= Date.parse(formateDate(new Date(item.experDate)))){
                      return item;
                  }
              });
            }
            this.cpnVoucherInfoList = cvList, //抵用券
            this.cpnDiscountsInfoList = cdList //折扣券
            this.cpnId = res.data.result.cpnId
            this.cpnInfoCnt = Number(cdList.length) + Number(cvList.length)
            if(this.cpnInfoCnt == 0){
              this.selecouponflag = false
            }else{
              this.selecouponflag = true
              if(this.cpnInfoCnt == 1){
                if(cdList.length == 1 && cvList.length == 0){
                  this.selsect2(0);
                }else if(cdList.length == 0 && cvList.length == 1){
                  this.selsect(0);
                }else{
                  this.selsect2(0);
                }
              }else{
                if(res.data.result.cpnId.indexOf('VO') != -1){
                  for(var i=0;i<res.data.result.cpnVoucherInfoList.length; i++){
                     if(res.data.result.cpnVoucherInfoList[i].voucherNum==res.data.result.cpnId){
                       this.selsect(i);
                     }
                  }
                }else{
                  for(var i=0;i<res.data.result.cpnDiscountsInfoList.length; i++){
                     if(res.data.result.cpnDiscountsInfoList[i].discountNum==res.data.result.cpnId){
                       this.selsect2(i);
                     }
                  }
                }
              }
            }

          }
      })
      .catch(error => {
        console.log(error)
      })
    },

    setTipText: function(tip) {
      var tipText = "";
      switch (tip) {
        case "1":
          tipText = "订单确认后不可取消或变更";
          break;
        case "2":
          tipText = "订单确认后可提前24小时取消或变更";
          break;
        case "3":
          tipText = "订单确认后可提前48小时取消或变更";
          break;
      }
      this.tipText = tipText;
    },
    formatStrDate(date) {
      let mymonth = date[1];
      let myweekday = date[2];
      return (
        mymonth.replace(/\b(0+)/gi, "") +
        "月" +
        myweekday.replace(/\b(0+)/gi, "") +
        "日"
      );
    },
    //切割餐段
    setTimes: function(start, end) {
      var times = [];
      if (!start || start == "null") {
        start = "00:00";
      }
      if (!end || end == "null") {
        end = "23:59";
      }
      var starts = start.split(":");
      var tmpH = parseInt(starts[0]);
      var tmpS = parseInt(starts[1]);
      var endArrays=[];
      if(start>end){
        //处理跨天
        endArrays[0] ='24:00';
        endArrays[1]=end;
      }else{
        endArrays[0]=end;
      }
      var index = 0;
      var length = endArrays.length;
      for(var i=0;i<length;i++ ){
        var ends = endArrays[i].split(":");
        var endsH = parseInt(ends[0]);
        var endsS = parseInt(ends[1]);
        var tmp ;
        do  {
          times[index] ="00".substr((tmpH + "").length) +tmpH +":" +"00".substr((tmpS + "").length) +tmpS;
          tmp = parseInt((tmpS + 30) / 60);
          tmpS = (tmpS + 30) % 60;
          tmpH = (tmpH + tmp) % 24;
          index++;
          if(tmpH==0&&tmpS==0){
            break;
          }
        }while(tmpH < endsH || (tmpH == endsH && tmpS < endsS));
      }
      console.log(times);
      return times;
    },

    gopay: function(data) {
      if(this.btnDisable){
        this.btnDisable = false;
         var flag = this.$refs.myChild.sendMsgToParent();
          if (!flag || !this.check(this.reservOrderVo)) {
            this.btnDisable = true
            return false;
          }
          this.reservOrderVo.memberId = this.memberInfo.acid;
          this.reservOrderVo.remark = this.remark
          
          if(!!this.productPrice&&Object.keys(this.onlinePrice).length!=0){
            // this.reservOrderVo.payAmoney = this.productPrice;
            if(this.discountrate != null){
              this.reservOrderVo.payAmoney = this.discountPrice; //把没有使用优惠卷的价格返给后端
            }else {
              this.reservOrderVo.payAmoney = this.productPrice;//改为优惠前实际金额，具体支付金额后端计算
            }
          }
        if(this.paybtnflag){
          if(this.sysServicetype.indexOf('_cpn') != -1){
            if(this.sysServicetype == 'object_cpn'){
              if(this.reservOrderVo.expressAddress && this.reservOrderVo.consignee && this.reservOrderVo.expressPhone){
                this.axios({
                    method: 'post',
                    url: '/mars/goods/getProductGrouopInfo',
                    headers: { 'Content-type': 'application/json' },
                    data: {productGroupId:this.reservOrderVo.productGroupId,productId:this.reservOrderVo.productId}
                })
                .then(res => {
                    if (res.data.code == 100) {
                        this.reservOrderVo.equitySubNum = 1;//权益扣减次数  默认值为1
                        this.reservOrderVo.exchangeNum = 1;
                        this.reservOrderVo.source = res.data.result.source.toLowerCase();
                        this.reservOrderVo.productNo = res.data.result.thirdCpnNo;
                        this.reservOrderVo.productGroupProductId = res.data.result.productGroupProductId;
                        this.reservOrderVo.productId = res.data.result.productId;
                        this.reservOrderVo.shopId = res.data.result.shopId ;//商户ID 必传
                        this.reservOrderVo.shopItemId = res.data.result.shopItemId;//商品规格  必传
                        this.reservOrderVo.shopChannelId = res.data.result.shopChannelId;
                        this.shoppingorder()
                    }
                })
                 .catch(error => {
                  console.log(error);
                });
              }else{
              Toast({
                message: '请正确输入收货信息'
              });
              this.btnDisable = true;
            }
            }else{
              this.axios({
                    method: 'post',
                    url: '/mars/goods/getProductGrouopInfo',
                    headers: { 'Content-type': 'application/json' },
                    data: {productGroupId:this.reservOrderVo.productGroupId,productId:this.reservOrderVo.productId}
                })
                .then(res => {
                    if (res.data.code == 100) {
                        this.reservOrderVo.equitySubNum = 1;//权益扣减次数  默认值为1
                        this.reservOrderVo.exchangeNum = 1;
                        this.reservOrderVo.source = res.data.result.source.toLowerCase();
                        this.reservOrderVo.productNo = res.data.result.thirdCpnNo;
                        this.reservOrderVo.productGroupProductId = res.data.result.productGroupProductId;
                        this.reservOrderVo.productId = res.data.result.productId;
                        this.reservOrderVo.shopId = res.data.result.shopId ;//商户ID 必传
                        this.reservOrderVo.shopItemId = res.data.result.shopItemId;//商品规格  必传
                        this.reservOrderVo.shopChannelId = res.data.result.shopChannelId;
                        this.shoppingorder()
                    }
                })
                 .catch(error => {
                  console.log(error);
                });
            }
            }else{
              this.shoppingorder()
            }
          
        }
      }
    },

  //生成预约单
  shoppingorder:function(){
    this.axios({
       method: 'post',
      url:"/mars/reservOrderAttach/placeOrder",
      data:this.reservOrderVo,
      headers:{'X-REQUESTED-SO-TOKEN':$cookies.get(localStorage.getItem('CHANNEL') + '_loginToken')}
      })
          .then(res => {
            if (res.data.code == 100) {
              var ordermsg = res.data.result;
                //跳聚合支付（收银台）
                if(!!this.reservOrderVo.payAmoney){
                  //判断是否是光大-高迪安渠道 用开联通支付
                  if(localStorage.getItem('CHANNEL') == 'GDA'){
                     this.axios.post('/member/bank/getBankcardList',{
                      "acid": this.memberInfo.acid
                    }).then(res => {
                      if(res.data.code == 100){
                        this.$router.push({name: "card",query:{cardlist:res.data.result,ordermsg:ordermsg}});
                      }
                    })
                  } else if(localStorage.getItem('CHANNEL') == 'ICBC'){
                    this.axios.post('/member/account/getMemAccount',{'acChannel':'ICBC','acId':this.memberInfo.acid})
                      .then(res => {
                        if(res.data.code == 100){
                          this.icbcthirdId = res.data.result.thirdId
                           let params = {
                            "acId": this.memberInfo.acid,
                            "amount": ordermsg.payAmount,
                            "body": this.reservOrderVo.hotelName?this.reservOrderVo.hotelName:'',
                            "curcode": "CNY",
                            "goodsName": this.reservOrderVo.shopItemName?this.reservOrderVo.shopItemName:'',
                            "mbId": ordermsg.memberId,
                            "merchantId": 10005,
                            "orderId": ordermsg.id,
                            "payMethod": 3,
                            "paymentType": "工行预约单",
                            "source": ordermsg.orderSource,
                            "custId":this.icbcthirdId
                          }
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
                    }else if(localStorage.getItem('CHANNEL') == 'CMB'){
                      let params = {
                        "acId": this.memberInfo.acid,
                        "amount": ordermsg.payAmount,
                        "body": this.reservOrderVo.hotelName?this.reservOrderVo.hotelName:'',
                        "curcode": "CNY",
                        "goodsName": this.reservOrderVo.shopItemName?this.reservOrderVo.shopItemName:'',
                        "mbId": ordermsg.memberId,
                        "merchantId": 1574326245806,
                        "orderId": ordermsg.id,
                        "payMethod": 3,
                        "paymentType": "招商预约单",
                        "source": ordermsg.orderSource
                      }
                      this.axios.post("/mars/pay/getAggregatePayParams",params,{headers: { 'Content-Type': 'application/json', 'User-Agent': 'okhttp' }}).then(res => {
                        if(res.data.code == 100){
                          this.axios.post('/aggregatePay/pay/order',{ "params": res.data.result.params,"payChannelId": 13})
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
                          Toast({
                            message: res.data.msg
                          });
                        }
                      }).catch(error => {
                          console.log(error);
                      });
                    }else{
                      this.axios({method: "get", url: "/mars/pay/getPayParams/"+ ordermsg.id}).then(res => {
                        if(res.data.code == 100){
                          window.location.href = process.env.PAY_ROOT+'/home?channel='+Base64.encode(this.$store.state.channel)+'&params='+ Base64.encode(encodeURIComponent(res.data.result.params))
                        }else{
                          Toast({
                            message: res.data.msg
                          });
                        }
                      }).catch(error => {
                          console.log(error);
                      });

                      // window.location.href = process.env.PAY_ROOT+'/home?channel='+Base64.encode(this.$store.state.channel)+'&params='+ Base64.encode(encodeURIComponent(res.data.result.params))
                    }
                }else{
                  this.$router.push({name: "ressuc", params: { reservOrderId: ordermsg.id }});
                }
              
            } else {
              Toast({
                message: res.data.msg
              });
              this.btnDisable = true;
            }
          })
          .catch(error => {
            this.btnDisable = true;
            console.log(error);
          });

    },



    check: function(params) {
      //身份证校验正则
      var idcardReg = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$|^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/;
      //护照正则
      var portReg = /^1[45][0-9]{7}$|([P|p|S|s]\d{7}$)|([S|s|G|g|E|e]\d{8}$)|([Gg|Tt|Ss|Ll|Qq|Dd|Aa|Ff]\d{8}$)|([H|h|M|m]\d{8,10})$/;

      // 港澳通行证正则
      var HKMacaoPassReg =  /^[HMhm]{1}([0-9]{10}|[0-9]{8})$/;

      //手机号校验规则
      var phoneReg = /^1(3|4|5|6|7|8|9)\d{9}$/;
      if(this.setting.goodsSetting.disableWechat=='1'){
        Toast({
          message: '禁止在线预约'
        });
        return false;
      }
      if (params.serviceType == "accom") {
        //住宿校验
        if (!params.checkDate) {
          Toast({
            message: "入住时间不能为空"
          });
          return false;
        }
        if (!params.deparDate) {
          Toast({
            message: "离开时间不能为空"
          });
          return false;
        }
        if (!params.checkNight) {
          Toast({
            message: "房间数不能为空"
          });
          return false;
        }
        if (!params.nightNumbers) {
          Toast({
            message: "入住夜不能为空"
          });
          return false;
        }
        if (!params.bookName) {
          Toast({
            message: "入住人不能为空"
          });
          return false;
        }
        if (params.countryId != "CN" && !params.bookNameEn) {
          Toast({
            message: "入住人拼音不能为空"
          });
          return false;
        }
        if (params.bookIdType == "idCard") {
          if (!this.checkTip(params.bookIdNum, idcardReg, "身份证格式有误")) {
            return false;
          }
        } else if (params.bookIdType == "passport") {
          if (!this.checkTip(params.bookIdNum, portReg, "护照格式有误")) {
            return false;
          }
        }else if(params.bookIdType === 'HK_Macao_Pass'){
          if(!this.checkTip(params.bookIdNum,HKMacaoPassReg,"港澳通行证格式有误")){
            return false;
          }
        }
      } else if(params.serviceType.indexOf('_cpn') != -1){

      }else if(params.serviceType == 'car'){
        if(!params.giftDate || !params.giftTime){
            Toast({
              message: "接送机时间不能为空"
            });
            return false;
          }
          if(!params.giftPeopleNum || !params.giftName){
            Toast({
              message: "乘车人不能为空"
            });
            return false;
          }
          if(!params.airport){
            Toast({
              message: "机场不能为空"
            });
            return false;
          }
        if(params.travelType == 1){ //接机
          if(!params.flightNumber){
            Toast({
              message: "航班号不能为空"
            });
            return false;
          }

          if(!params.endPoint){
            Toast({
              message: "目的地不能为空"
            });
            return false;
          }
        }
        if(params.travelType == 2){ //送机
          if(!params.startPoint){
            Toast({
              message: "出发地不能为空"
            });
            return false;
          }
        }
      }else if(params.serviceType == 'medical'){
        if(!params.giftDate || !params.giftTime){
            Toast({
              message: "预约时间不能为空"
            });
            return false;
          }
          if(!params.department){
            Toast({
              message: "预约科室不能为空"
            });
            return false;
          }
      }else {
        //自助餐
        if (!params.giftPeopleNum) {
          Toast({
            message: "预约人数不能为空"
          });
          return false;
        }
        if (!params.giftDate) {
          Toast({
            message: (params.serviceType=='buffet'||params.serviceType=='tea')? "用餐日期不能为空":"预约日期不能为空"
          });
          return false;
        }
        if (!params.giftTime) {
          Toast({
            message:(params.serviceType=='buffet'||params.serviceType=='tea')?"用餐时间不能为空":"预约时间不能为空"
          });
          return false;
        }
        if (!params.giftName) {
          Toast({
            message: "用餐人不能为空"
          });
          return false;
        }
      }


      if (!this.checkTip(params.giftPhone, phoneReg, "手机号格式有误")) {
        return false;
      }
      return true;
    },
    checkTip: function(param, reg, tip) {
      if (!reg.test(param)) {
        Toast({
          message: tip
        });
        return false;
      }
      return true;
    },
    selectradiovalue:function(valueword,index){
      this.curronbest = index
      this.reservOrderVo.reservRemark = valueword
      // console.log(this.reservOrderVo.reservRemark)
    },
    //获取用户信息
    getLoginInfo: function() {
      this.axios({
        method: "post",
        url: "/yangjian/mem/heartBeat-helife",
        headers: {'X-REQUESTED-SO-TOKEN': $cookies.get(localStorage.getItem('CHANNEL')+'_loginToken')}
      })
        .then(res => {
          this.memberInfo = res.data.result;
          // this.getMemCpns()
          //我的信息
          this.myInfo.idNumber = res.data.result.idNumber;
          this.myInfo.passportNum = res.data.result.passportNum;
          this.myInfo.mobile = res.data.result.mobile;
          this.myInfo.passportName = res.data.result.passportName;
          this.myInfo.giftName = res.data.result.mbName;
          if('accom' === this.$store.state.sysService.toLowerCase()){
            this.getMemberCertificate(res.data.result.mbid)
          }
          this.getLinkedMemList(res.data.result.acid)
        })
        .catch(error => {
          console.log(error);
        });
    },
    //获取用户证件信息
    getMemberCertificate:function(mbid){
          this.axios.post('/mars/reservOrderAttach/getMemberCertificateInfo',{
            "mbid":mbid
        })
        .then(res => {
            if(res.data.code == 100){
                this.memberCertificate=res.data.result;
                this.certificateNumber = res.data.result['HK_Macao_Pass']
            }
        })
        .catch(error => {
          console.log(error)
        })
    },
    getLinkedMemList: function(acid){
      this.axios({
        method: 'post',
        url: '/member/contact/getContactList',
        headers:{'Content-Type':'application/json'},
        data: acid
      })
      .then(res => {
        if(res.data.code == "200"){
            Toast({
                message: res.data.msg
            });
        }else{
          if(res.data.result.length>0){
            for(var i =0;res.data.result.length>i;i++){
              this.linkedMemeList.push(res.data.result[i].ctName+'-'+res.data.result[i].ctMoblie)
              this.linkedNamelist.push(res.data.result[i].ctName)
            }
          }
        }
      })
    },
    getSetting: function() {
        this.axios.post('/mars/reservOrderAttach/getGoodsSetting',{
            "goodsId":this.reservOrderVo.goodsId,
            "productGroupId":this.reservOrderVo.productGroupId,
            "giftCodeId":this.reservOrderVo.giftCodeId
        })
        .then(res => {
            if(res.data.code == 100){
                this.setting=res.data.result;
                this.onlySelfshow = res.data.result.goodsSetting.onlySelf
                this.reservOrderVo.superposition = res.data.result.goodsSetting.superposition;//同一时段权益不叠加 
                this.reservOrderVo.singleThread = res.data.result.goodsSetting.singleThread;//行权完毕次日才可以进行下一次预约
                this.productPriceshow(this.$store.state.productGroupProductId,this.$store.state.startDate.replace(/,/g,'-'),this.$store.state.endDate.replace(/,/g,'-'))
            }
        })
        .catch(error => {
          console.log(error)
        })
    },
    //住宿只查在线付  其他先查在线付 没有再查到店付
    productPriceshow:function(productGroupProductId,startDate,endDate){ 
          if(this.sysServicetype!='accom'){
            endDate=startDate;
          }
          this.axios.post('/mars/boscbank/selectBookPay',{productGroupProductId:productGroupProductId,startDate:startDate,endDate:endDate})
            .then((res)=>{
              if(res.data.code = 100){
                let onlinePrice = res.data.result;
                  console.log("onlinePrice",onlinePrice)
                  if(!!onlinePrice&&onlinePrice.length!=0){
                    this.onlinePrice = onlinePrice;
                    let price=0;
                    for(let i=0;i<  onlinePrice.length;i++){
                      price+=onlinePrice[i].bookPrice;
                    }
                    this.bookPrice = price;
                    this.onlinePayFlag = true;
                    this.setNum(1,0,this.onlinePayFlag);
                    // this.getMemCpns();
                  }else if(this.sysServicetype!='accom' && this.sysServicetype.indexOf('_cpn')===-1){
                    this.axios.post('/mars/boscbank/getStorePrice',{productGroupProductId:productGroupProductId,startDate:startDate,endDate:endDate})
                    .then((res)=>{
                      this.storePrice = res.data.result;
                      this.bookPrice = res.data.result.payPrice;
                      // this.productPrice =this.bookPrice*this.totalNum-this.storePrice.netPrice*this.peopleNum;
                      this.realPrice = this.bookPrice*this.totalNum-this.storePrice.netPrice*this.peopleNum;
                      this.goodsPrice = this.bookPrice*this.totalNum-this.storePrice.netPrice*this.peopleNum;
                      if(this.totalNum - this.peopleNum > 0 ){
                        this.productPrice =this.bookPrice*this.totalNum-this.storePrice.netPrice*this.peopleNum;
                      }
                    })
                    .catch((error)=>{
                        console.log(error)
                    })
                  }
                  this.btnDisable = true
                  this.paybtnflag = true
              }else{
                  this.btnDisable = false
                  this.paybtnflag = false
              }
            })
            .catch((error)=>{
                console.log(error)
            })

      if(this.sysServicetype.indexOf('_cpn') === -1){
          this.buyBtnTitle = '立即预约'
          this.payMoneyBtnTitle = '提交'
      }else{
            this.payMoneyBtnTitle = '支付'
            this.buyBtnTitle = '立即兑换'
      }
    },
    // 优惠人数   总人数  付费人 = 总人数-优惠人数
     setNum:function(totalNum,num,isOnline){
      this.peopleNum =num;
      this.totalNum = totalNum;
      if(isOnline){
        if(this.sysServicetype == 'accom'){
           if(this.discountrate != null){
              this.discountPrice = this.bookPrice*(totalNum-num) * this.discountrate;
              this.productPrice = this.bookPrice*(totalNum-num) * this.discountrate;
            }else {
              this.productPrice = this.bookPrice*(totalNum-num);
            }
            this.realPrice = this.bookPrice*(totalNum-num);
            this.goodsPrice = this.bookPrice*(totalNum-num);
        } else{
          if(this.discountrate != null){
              this.discountPrice = this.bookPrice*(1) * this.discountrate;
              this.productPrice = this.bookPrice*(1) * this.discountrate;
            }else {
              this.productPrice = this.bookPrice*(1);
            }
            this.realPrice = this.bookPrice*(1);
            this.goodsPrice = this.bookPrice*(1);
            if(this.sysServicetype == 'car'){
              if(this.surplusFreeTimes != 0){ //
                  this.productPrice = 0
              }
            }
        }
        
      }else{
        //到店付 =  算出来的实际支付金额*总人数 - 净价*优惠人数
        // this.productPrice =this.bookPrice*totalNum-this.storePrice.netPrice*num;
        this.realPrice = this.bookPrice*totalNum-this.storePrice.netPrice*num;
        this.goodsPrice = this.bookPrice*totalNum-this.storePrice.netPrice*num;
        if(totalNum - num > 0 ){
            this.productPrice =this.bookPrice*this.totalNum-this.storePrice.netPrice*this.peopleNum;
        }else{
          this.productPrice = null;
        }
      }
    },
    payBtnstart:function(){
      if(this.paybtnflag){
        this.paybtnflag = false
      }else{
        this.paybtnflag = true
      }
    },
    gotocoupon:function(){
      if(this.selecouponflag){
        this.couponlistFlag = true
      }
    },


  //使用优惠券
  selsect: function (index,coupontype,$event) {
          this.nocouponflag = false
          // 1是优惠券，2是折扣券
          var couponMoney;
          if(this.discountrate != null){
            //var realMoney = this.realPrice;// 如果有优惠价 默认选择的情况 需要把当前的折后价赋上
            var realMoney = this.discountPrice;
          }else {
            var realMoney = this.realPrice;
          }
          //抵用券
          this.curronindex = index
          this.curronindex2 = -1
          this.couponName = this.cpnVoucherInfoList[this.curronindex].name  //名称
          realMoney =  (realMoney - this.cpnVoucherInfoList[this.curronindex].worth) < 0 ? 0 :(realMoney - this.cpnVoucherInfoList[this.curronindex].worth)
          this.reservOrderVo.cpnType = 1
          this.reservOrderVo.cpnId = this.cpnVoucherInfoList[this.curronindex].voucherNum
          if(this.discountrate !=null){
            this.productPrice = realMoney
          }
          this.discountNum = this.cpnVoucherInfoList[this.curronindex].voucherNum //券编号     
          // localStorage.setItem("couponId",this.discountNum)
          this.$store.commit('getCouponId',this.discountNum)
          // this.couponflag = true
          this.cpnInfoCnt = 1
          this.couponlistFlag = false
          this.coupontips = "最高可省" + realMoney + "元"
          // this.priceshow = (this.goodsPrice - couponMoney > 0 ? this.goodsPrice - couponMoney : 0).toFixed(2)
          return realMoney;
         
      },

      selsect2: function (index,coupontype,$event) {
        this.nocouponflag = false
        // 1是优惠券，2是折扣券
        this.curronindex2 = index
        this.curronindex = -1
        var couponMoney;
        var realMoney = this.realPrice;
        this.discountRatio = this.cpnDiscountsInfoList[this.curronindex2].discountRatio  //折扣
        this.upperAmount = this.cpnDiscountsInfoList[this.curronindex2].upperAmount //最高可优惠金额:0表示无限制
        this.couponName = this.cpnDiscountsInfoList[this.curronindex2].name  //名称
        this.discountNum = this.cpnDiscountsInfoList[this.curronindex2].discountNum //券编号              
          if(0 === this.upperAmount){
          //无限制
             realMoney = (Number( realMoney) / 10) * (this.discountRatio)
             
          } else if((realMoney-(Number( realMoney) / 10) * (this.discountRatio)) > this.upperAmount){
             realMoney = (realMoney - this.upperAmount)
          }else if((realMoney-(Number( realMoney) / 10) * (this.discountRatio)) < this.upperAmount){
             realMoney = (Number( realMoney) / 10) * (this.discountRatio)
          }      
        this.reservOrderVo.cpnType = 2
        this.reservOrderVo.cpnId = this.cpnDiscountsInfoList[this.curronindex2].discountNum   
        this.productPrice = realMoney
        this.$store.commit('getCouponId',this.discountNum)
        // this.couponflag = true
        this.cpnInfoCnt = 1
        this.couponlistFlag = false
        this.coupontips = "最高可省" + realMoney + "元"
        // this.priceshow = (this.goodsPrice - couponMoney > 0 ? this.goodsPrice - couponMoney : 0).toFixed(2)
      },
    //不使用优惠券
    usecoupon:function(){
      this.curronindex = -1
      this.curronindex2 = -1
      if(this.curronindex === -1 || this.curronindex2 === -1){
        this.nocouponflag = true
      }
      this.couponlistFlag = false
      // this.couponflag = true
      this.cpnInfoCnt = 1
      this.coupontips = ''
      this.discountRatio = ''
      this.couponName  = '不使用优惠券'
      this.$store.commit('getCouponId','')
      if(this.discountrate != null){
        this.productPrice = this.discountPrice
      }else { 
        this.productPrice = this.goodsPrice
        }
      this.reservOrderVo.cpnType = '',
      this.reservOrderVo.cpnId = ''
    },

    closeCoupon:function(){
      this.couponlistFlag = false
    },
  }
};
</script>


