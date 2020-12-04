<template>
  <div class="ordershowbox" >
    <div style="position: fixed; top: 0; left: 0; bottom:0; overflow-y: scroll; width: 100%; height: auto; -webkit-overflow-scrolling: touch;">
      <Topheader :headtitle="headtitle" v-if="headerBarflage"></Topheader>
    <div class="orderBox">
      <div class="orderType">
      <div>{{statusTxt}}</div></div>
      <div class="paystatus_n" v-if="reservOrderVo.proseStatus==0 &&reservOrderVo.payStatus==1&&!!moneyNum">
        <p class="moneyNum">待支付：<span>¥{{moneyNum}}</span></p>
        <p class="moneyNumTip">请在2小时内付款，超时订单将自动关闭</p>
        <p class="moneyPya" @click="gotopay(reservOrderVo.id)">继续支付</p>
      </div>
      <div class="paystatus_n" v-else-if="reservOrderVo.proseStatus==2||reservOrderVo.proseStatus==3">
        <p class="moneyNum" v-if="reservOrderVo.payStatus==0">退款中：<span>¥{{moneyNum}}</span></p>
        <p class="moneyNum" v-if="reservOrderVo.payStatus==3">退款成功：<span>¥{{moneyNum}}</span></p>
      </div>
      <div class="orderTip">{{statusTip}}</div>
    </div>

    
    <div class="orderBox" v-if="(reservOrderVo.serviceType != 'car') && (reservOrderVo.serviceType != 'medical')">
      <div class="orderName" v-if="(String(reservOrderVo.serviceType)).indexOf('_cpn') === -1 ">{{reservOrderVo.goodsName}}</div>
      <div class="orderMsgBox">
        <div class="orderMsgshow">
          <div class="ordermsgshowPic">
            <span class="defaultBg"><span :style="{backgroundImage:'url('+ reservOrderVo.goodsImg + ')', backgroundSize:'cover', backgroundPosition:'center',backgroundRepeat:'no-repeat'}"></span></span>
          </div>
          <div class="ordermsgshowMore">
            {{reservOrderVo.hotelName}}
            <div class="ordermsgshowMore_type" v-if="reservOrderVo.serviceType=='accom'"> {{reservOrderVo.productName}}  <span >| {{reservOrderVo.addon}} </span>|{{reservOrderVo.needs}}</div>
            <div class="ordermsgshowMore_type" v-else><span v-if="(String(reservOrderVo.serviceType)).indexOf('_cpn') === -1 "> {{reservOrderVo.shopName}}|</span>{{reservOrderVo.productName}}</div>
            <div class="gitftstype" v-if="(String(reservOrderVo.serviceType)).indexOf('_cpn') != -1"><span>{{reservOrderVo.serviceName}}</span></div>
            <div class="gitftstype" v-else><span v-if="reservOrderVo.giftType">{{reservOrderVo.giftType | getGiftTxt}}</span></div>
          </div>
        </div>
        <!-- <div class="orderPrice">
          <p>
            <i>¥</i>
            {{reservOrderVo.totalAmount}}
          </p>
          <p>x{{reservOrderVo.exchangeNum}}</p>
        </div> -->
      </div>
    </div>
   
    <!-- 根据状态判断 -->
    <div class="orderBox" v-if="reservOrderVo.proseStatus==1&&!!reservOrderVo.varCode&&reservOrderVo.serviceType!='accom'">
      <div class="couponexchangeNum">核销码</div>
      <div class="couponexchangeBox">{{reservOrderVo.varCode}}</div>
      <div class="couponexchangeBox" v-if="!!imgSrc"><img :src="imgSrc" style="width:2.2rem; display:block; margin:.2rem auto;" /></div>
      <div style="padding:.1rem 0; text-align:center; color:#2577e3;" v-clipboard:copy="reservOrderVo.varCode" v-clipboard:success="onCopy" v-clipboard:error="onError">复制</div>
      <div class="orderNumTip">到店请出示此确认号，享用尊贵权益</div>
    </div>
    <div class="orderBox" v-if="reservOrderVo.proseStatus==1&&!!reservOrderVo.reservNumber&&reservOrderVo.serviceType=='accom'">
      <div class="couponexchangeNum">确认号</div>
      <div class="couponexchangeBox">{{reservOrderVo.reservNumber}}</div>
      <div style="padding:.1rem 0; text-align:center; color:#2577e3;" v-clipboard:copy="reservOrderVo.reservNumber" v-clipboard:success="onCopy" v-clipboard:error="onError">复制</div>
      <div class="orderNumTip">到店请出示此确认号，享用尊贵权益</div>
    </div>

    <!--  券码信息 -->
    <div v-if="(String(reservOrderVo.serviceType)).indexOf('_cpn') != -1 && !couponStatusflag">
      <div class="orderBox" v-if="reservOrderVo.couponsType != 'SHORTURL'">
        <div class="couponexchangeNum">券码信息</div>
        <div class="couponexchangeBox">{{reservOrderVo.thirdCodePassword}}</div>
        <div style="padding:.1rem 0; text-align:center; color:#2577e3;" v-clipboard:copy="reservOrderVo.thirdCodePassword" v-clipboard:success="onCopy" v-clipboard:error="onError">复制</div>
        <p style="color:#999; text-align:center; padding-top:.1rem;">有效期至 {{reservOrderVo.experTime}}</p>
      </div>
      <div v-else-if="reservOrderVo.thirdCodePassword && reservOrderVo.shortUrl">
        <div  class="orderBox">
          <div class="couponexchangeNum">券码信息</div>
          <div class="couponexchangeBox">{{reservOrderVo.thirdCodePassword}}</div>
          <div style="padding:.1rem 0; text-align:center; color:#2577e3;" v-clipboard:copy="reservOrderVo.thirdCodePassword" v-clipboard:success="onCopy" v-clipboard:error="onError">复制</div>
          <p style="color:#999; text-align:center; padding-top:.1rem;">有效期至 {{reservOrderVo.experTime}}</p>
        </div>
          <div class="thirdCodePassword">
            <div @click="gotousercoupon(reservOrderVo.shortUrl)" class="gotocoupon">立即使用</div>
          </div>
      </div>
      <div  class="thirdCodePassword" v-else>
        <div @click="gotousercoupon(reservOrderVo.shortUrl)" class="gotocoupon">立即使用</div>
      </div>
    </div>

    <div class="orderBox" v-if="reservOrderVo.serviceType=='accom'">
      <div class="orderperMsg">
        <div>入住时间</div>
        <div>{{reservOrderVo.details[0].checkDate}} <span style="border:solid .02rem #ddd; display:inline-block; padding:.02rem .05rme; border-radius: .3rem;  padding: .02rem .2rem; color: #666; margin:0 .1rem;">{{reservOrderVo.details[0].nightNumbers}}晚</span> {{reservOrderVo.details[0].deparDate}}</div>
      </div>
      <div class="orderperMsg">
        <div>房间数</div>
        <div>{{reservOrderVo.details[0].checkNight}} 间</div>
      </div>
      <div v-for="(detail) in  reservOrderVo.details" :key="detail.bookNameEn">
        <div class="orderperMsg" >
          <div>入住人</div>
          <div>{{reservOrderVo.giftName}}</div>
        </div>
        <div class="orderperMsg" v-if="!!detail.bookNameEn">
          <div>拼音</div>
          <div>{{detail.bookNameEn}}</div>
        </div>
      </div>
      <div class="orderperMsg">
        <div>手机号</div>
        <div>{{reservOrderVo.giftPhone}}</div>
      </div>
      <div class="orderperMsg">
        <div>床型</div>
        <div>{{reservOrderVo.details[0].accoNedds}}</div>
      </div>
    </div>

    <div class="orderBox" v-else-if="(String(reservOrderVo.serviceType)).indexOf('_cpn') != -1">
      <div  v-if="reservOrderVo.serviceType !== 'object_cpn'">
        <div class="orderperMsg">
          <div>使用人</div>
          <div>{{reservOrderVo.giftName}}</div>
        </div>
        <div class="orderperMsg">
          <div>手机号</div>
          <div>{{reservOrderVo.giftPhone}}</div>
        </div>
      </div>
      <div v-else>
        <div class="orderperMsg">
          <div>收件人姓名</div>
          <div>{{reservOrderVo.consignee}}</div>
        </div>
        <div class="orderperMsg">
          <div>收件人电话</div>
          <div>{{reservOrderVo.expressPhone}}</div>
        </div>
        <div class="orderperMsg">
          <div>配送方式</div>
          <div>{{reservOrderVo.expressModeStr}}</div>
        </div>
        <div class="orderperMsg">
          <div style="width:1.5rem;">收货地址</div>
          <div>{{reservOrderVo.expressAddress}}</div>
        </div>
        <div class="orderperMsg" v-if="reservOrderVo.companyName">
          <div>快递公司</div>
          <div>{{reservOrderVo.companyName}}</div>
        </div>
        <div class="orderperMsg">
          <div>快递状态</div>
          <div v-if="reservOrderVo.expressStatus">{{reservOrderVo.expressNumber}}</div>
          <div v-else>暂未发货</div>
        </div>
      </div>
      
    </div>

    <div class="orderBox" v-else-if="reservOrderVo.serviceType == 'car'">
      <div style="text-align:center; padding:.2rem 0; font-weight: bold;">
          {{reservOrderVo.shopName}}
        <div style="position: relative;  height: .15rem; border-bottom: solid .01rem #d7d7d7; margin: .3rem 0;">
            <span style="background: #fff; padding: 0 .3rem; position: relative; margin-top: -0.2rem; display: inline-block; color:#999;"><em v-if="reservOrderVo.travelType == 1">接机</em><em v-else>送机</em>服务</span>
        </div>
      </div>
      <div class="orderperMsg" v-if="reservOrderVo.travelType == 1"><!--接机-->
        <div>接机航班号</div>
        <div>{{reservOrderVo.flightNumber}}</div>
      </div>
      <div class="orderperMsg"  v-if="reservOrderVo.travelType == 1">
        <div>目的地</div>
        <div>{{reservOrderVo.endPoint}}</div>
      </div>
      <div class="orderperMsg" v-if="reservOrderVo.travelType == 2"><!--送机-->
        <div>出发地</div>
        <div>{{reservOrderVo.startPoint}}</div>
      </div>
      <div class="orderperMsg">
        <div>接送机时间</div>
        <div>{{reservOrderVo.giftDate}} {{reservOrderVo.giftTime}}</div>
      </div>
      <div class="orderperMsg">
        <div>接送机机场</div>
        <div>{{reservOrderVo.airport}}</div>
      </div>
      <div class="orderperMsg">
        <div>乘车人数</div>
        <div>{{reservOrderVo.giftPeopleNum}} 人</div>
      </div>
      <div class="orderperMsg">
        <div>乘车人</div>
        <div>{{reservOrderVo.giftName}}</div>
      </div>
      <div class="orderperMsg">
        <div>手机号</div>
        <div>{{reservOrderVo.giftPhone}}</div>
      </div>
      <div class="orderperMsg" v-if="reservOrderVo.remark">
        <div>备注信息</div>
        <div>{{reservOrderVo.remark}}</div>
      </div>
    </div>

    <div class="orderBox orderBox2" v-else-if="reservOrderVo.serviceType == 'medical'">
      <div style="border-bottom:solid .2rem #f4f4f4; padding:0 .3rem .3rem .3rem;">
        <div style="font-size:.3rem; line-height:.6rem;">{{reservOrderVo.hospitalInfos[0].name}}</div>
        <div class="medicalName" style="font-size:.26rem; padding:.1rem 0;"><span>{{reservOrderVo.hospitalInfos[0].grade}}</span><span>{{reservOrderVo.hospitalInfos[0].hospitalType}}</span></div>
        <div style="color:#999; font-size:.24rem;">{{reservOrderVo.hospitalInfos[0].province}} {{reservOrderVo.hospitalInfos[0].city}}</div>
      </div>
      <div class="orderperMsg" style="padding-left:.3rem; padding-right:.3rem; padding-top:.3rem;">
        <div>预约科室</div>
        <div>{{reservOrderVo.hospitalInfos[0].department}}</div>
      </div>
      <div class="orderperMsg paddlr3">
        <div>就诊类型</div>
        <div>{{reservOrderVo.hospitalInfos[0].visit}}</div>
      </div>
      <div class="orderperMsg paddlr3" v-if="reservOrderVo.hospitalInfos[0].visit == '专家门诊'">
        <div>特殊需求</div>
        <div>{{reservOrderVo.hospitalInfos[0].special}}</div>
      </div>
      <div class="orderperMsg paddlr3">
        <div>就诊时间</div>
        <div>{{reservOrderVo.giftDate}} {{reservOrderVo.giftTime}}</div>
      </div>
      <div class="orderperMsg paddlr3" style="padding-bottom:.3rem;">
        <div>病情描述</div>
        <div>{{reservOrderVo.remark}}</div>
      </div>
      <div style="border-top:solid .2rem #f4f4f4; padding-top:.3rem;">
        <div class="orderperMsg paddlr3">
          <div>医保类型</div>
          <div v-if="reservOrderVo.hospitalInfos[0].memberFamily.medicalInsuranceType == 1">医保</div>
          <div v-else>自费</div>
        </div>
        <div class="orderperMsg paddlr3">
          <div>就诊人</div>
          <div>{{reservOrderVo.hospitalInfos[0].memberFamily.name}}</div>
        </div>
        <div class="orderperMsg paddlr3">
          <div>联系电话</div>
          <div>{{reservOrderVo.hospitalInfos[0].memberFamily.mobile}}</div>
        </div>
        <div class="orderperMsg paddlr3">
          <div>证件号</div>
          <div>{{reservOrderVo.hospitalInfos[0].memberFamily.certificateNumber}}</div>
        </div>
      </div>
    </div>

    <div class="orderBox" v-else>
      <div class="orderperMsg">
        <div>人数/时间</div>
        <div>{{reservOrderVo.giftPeopleNum}}人， {{reservOrderVo.giftDate}} {{reservOrderVo.giftTime}}</div>
      </div>
      <div class="orderperMsg">
        <div>用餐人</div>
        <div>{{reservOrderVo.giftName}}</div>
      </div>
      <div class="orderperMsg">
        <div>手机号</div>
        <div>{{reservOrderVo.giftPhone}}</div>
      </div>
      <div class="orderperMsg" v-if="reservOrderVo.reservRemark">
        <div>特殊需求</div>
        <div>{{reservOrderVo.reservRemark}}</div>
      </div>
    </div>
   

    <div class="orderBox" v-if="Object.keys(storePrice).length!=0 && reservOrderVo.serviceType != 'car'">
      <div class="orderperMsg">
        <div>到店预计付</div>
        <div>
        {{(storePrice.payPrice * reservOrderVo.giftPeopleNum -storePrice.netPrice*(reservOrderVo.giftPeopleNum - num)).toFixed(2)}}
        <div class="tipsshow">实际支付金额以门店价格为准</div>
        </div>
      </div>
      <div class="orderperMsg">
      </div>
      <div class="orderperMsg">
        <div>价格</div>
        <div>{{storePrice.netPrice}} x {{num}}</div>
      </div>
      <div class="orderperMsg" v-if="!!storePrice.serviceRate">
        <div>服务费</div>
        <div>{{storePrice.serviceRate*100}}% x {{reservOrderVo.giftPeopleNum}}</div>
      </div>
      <div class="orderperMsg" v-if="!!storePrice.taxRate">
        <div>增值税</div>
        <div>{{storePrice.taxRate*100}}% x {{reservOrderVo.giftPeopleNum}}</div>
      </div>
    </div>
    <div class="orderBox" v-if="!!moneyNum">
      <div class="orderperMsg">
        <div>价格</div>
        <div>¥ {{!!this.reservOrderVo.details[0].totalAmoney?totalAmoney:moneyNum}}</div>
      </div>
      <div class="orderperMsg" v-if="!!this.reservOrderVo.details[0].discountAmoney">
        <div>折扣金额</div>
        <div>- ¥ {{!!this.reservOrderVo.details[0].discountAmoney?this.reservOrderVo.details[0].discountAmoney:0}}</div>
      </div>
      <div class="orderperMsg">
        <div>实付</div>
        <div class="colour">¥ {{moneyNum}}</div>
      </div>
    </div>
    <div class="orderBox">
      <div class="orderperMsg">
        <div>订单编号</div>
        <div>{{reservOrderVo.id}}</div>
      </div>
      <div class="orderperMsg">
        <div>下单时间</div>
        <div>{{reservOrderVo.createTimeStr}}</div>
      </div>
    </div>
    <div v-if="false">
      <div >
        <div style=" text-align:center;"><span class="cancelBtn" @click="cancelOrder">取消预约</span></div>
      </div>
    </div>
    <div v-if="(String(reservOrderVo.serviceType)).indexOf('_cpn') != -1">
      <p style="height:3rem;">&nbsp;</p>
    </div>
    </div>

    <backthird v-if='backthirdflag'></backthird>

    <div class="couponStatueDiv" @click="sendcoupon" v-if="couponStatusflag && (String(reservOrderVo.serviceType)).indexOf('_cpn') != -1 && reservOrderVo.payStatus == 2">
        <span v-if="(String(reservOrderVo.serviceType)).indexOf('_cpn') != -1">查看券码</span>
        <span v-else-if="(String(reservOrderVo.serviceType)).indexOf('_cpn') === -1">立即查看</span>
    </div>

    <div class="alipaydailogBox" v-if="alipayFalge">
      <div class="alipaydailogMsgBox">
        <div class="alipaycloseBtn" @click="closealipay">关闭</div>
        <div class="alipaytitle">支付宝预约行权地址</div>
        <div class="alipayurl">{{reservOrderVo.shortUrl}}</div>
        <div style="padding:.1rem 0; text-align:center; color:#2577e3;" v-clipboard:copy="reservOrderVo.shortUrl" v-clipboard:success="onCopy" v-clipboard:error="onError">复制</div>
        <div class="alipayurlTips">请复制上面行权地址至手机浏览器中打开，<br>供商家核销</div>
      </div>
    </div>


    <cmbPay v-if="cmbpayshow" :cmbpayValue="cmbpayValue" ref="cmbPaychild"></cmbPay>
    <icbcpay v-if="icbcpayshow" :icbcpayValue="icbcpayValue" ref="icbcPaychild"></icbcpay>
  </div>
 
</template>

<script>
import {mapState} from 'vuex'
import{WXbodyBottomshow} from '@/common/js/common.js'
import{transformatStatus,getStoreNum} from '@/common/js/util.js'
import { MessageBox } from 'mint-ui';
import { Toast } from "mint-ui";
import{getChannge} from '@/common/js/common.js'
import backthird from '@/components/bottombar/backthird'
import Topheader  from '@/components/head/head'
import cmbPay from "@/components/button_pay/cmbpay"
import icbcpay from "@/components/button_pay/icbcpay"
import VueClipboard from 'vue-clipboard2'
import Vue from 'vue'
Vue.use(VueClipboard)
let Base64 = require('js-base64').Base64;

export default {
  name: "home",
  data() {
    return {
      reservOrderVo: {},
      id: "",
      statusTxt: "",
      statusTip: "",
      showFlag:true,
      moneyNum:0,
      storePrice:{},
      num:1,
      totalAmoney:'',
      backthirdflag:false,
      couponStatusflag:false,
      reservOrderId:'',//预约单编号
      source:'',//第三方券来源，
      productNo:'',//第三方券商品编号
      imgSrc:"",
      headtitle:'订单详情',
      headerBarflage:true,
      cmbpayshow:false,
      cmbpayValue:'',
      icbcpayshow:false,
      icbcpayValue:'',
      mycouponstatus:true,
      alipayFalge:false, //支付宝行权地址显示
    };
  },
  created() {
    WXbodyBottomshow()
    if(WXbodyBottomshow()){
          this.headerBarflage =false
      }else{
          this.headerBarflage = true
      }
      if(this.$store.state.channel == 'CMB'){
          this.cmbpayshow = true
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

    if(this.$store.state.backUrl){
        this.backthirdflag = true
    }else{
        this.backthirdflag = false
    }
    
    this.id = this.$route.query.reservOrderId;
    this.init();

  },
  components:{
        backthird,
        Topheader,
        cmbPay,
        icbcpay
    },
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
  methods: {
    init: function() {
      this.axios.get("/mars/reservOrder/get/" + this.id)
        .then(res => {
          if (res.data.code == 100) {
            if(res.data.result.serviceType.indexOf('_cpn') != -1){
              this.source = res.data.result.thirdCpnSource
              this.productNo = res.data.result.thirdCpnNum
              if(res.data.result.thirdCodePassword || res.data.result.couponsType){
                this.couponStatusflag = false
              }else{
                this.couponStatusflag = true
              }
            }
            this.reservOrderVo = res.data.result;
            this.showFlag = this.isShowCancel(this.reservOrderVo.serviceType,this.reservOrderVo.proseStatus,this.reservOrderVo.useStatus,this.reservOrderVo.giftDate);
            var tips = transformatStatus(this.reservOrderVo.proseStatus,this.reservOrderVo.useStatus,this.reservOrderVo.payStatus,this.reservOrderVo.serviceType,this.reservOrderVo.expressStatus);
            if(!!tips){
              this.statusTxt = tips.statusTxt;
              this.statusTip = tips.statusTip;
            }
            this.getPrice(res.data.result.giftDate,res.data.result.productGroupProductId,res.data.result.giftType,res.data.result.giftPeopleNum);
            this.moneyNum = this.reservOrderVo.details[0].payAmoney;
            this.totalAmoney = this.reservOrderVo.details[0].payAmoney+ this.reservOrderVo.details[0].discountAmoney
            if(this.reservOrderVo.proseStatus==1&&!!this.reservOrderVo.varCode&&this.reservOrderVo.serviceType!='accom'){
              this.axios.get("/mars/reservCode/getEncodeUrl/" +this.reservOrderVo.varCode).then(res => {
                if (res.data.code == 100) {
                  this.imgSrc=res.data.result;
                }
              });
              
            }
          } else {
            Toast({ message:res.data.msg});
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    checkInit: function() {
      this.axios.get("/mars/reservOrder/get/" + this.id)
        .then(res => {
          if (res.data.code == 100) {
            if(res.data.result.serviceType.indexOf('_cpn') != -1){
              this.source = res.data.result.thirdCpnSource
              this.productNo = res.data.result.thirdCpnNum
              if(res.data.result.thirdCodePassword || res.data.result.couponsType){
                this.couponStatusflag = false
              }else{
                this.couponStatusflag = true
              }
            }
            this.reservOrderVo = res.data.result;
            this.showFlag = this.isShowCancel(this.reservOrderVo.serviceType,this.reservOrderVo.proseStatus,this.reservOrderVo.useStatus,this.reservOrderVo.giftDate);
            var tips = transformatStatus(this.reservOrderVo.proseStatus,this.reservOrderVo.useStatus,this.reservOrderVo.payStatus,this.reservOrderVo.serviceType,this.reservOrderVo.expressStatus);
            if(!!tips){
              this.statusTxt = tips.statusTxt;
              this.statusTip = tips.statusTip;
            }
            this.getPrice(res.data.result.giftDate,res.data.result.productGroupProductId,res.data.result.giftType,res.data.result.giftPeopleNum);
            this.moneyNum = this.reservOrderVo.details[0].payAmoney;
            this.totalAmoney = this.reservOrderVo.details[0].payAmoney+ this.reservOrderVo.details[0].discountAmoney
            if(this.reservOrderVo.proseStatus==1&&!!this.reservOrderVo.varCode&&this.reservOrderVo.serviceType!='accom'){
              this.axios.get("/mars/reservCode/getEncodeUrl/" +this.reservOrderVo.varCode).then(res => {
                if (res.data.code == 100) {
                  this.imgSrc=res.data.result;
                }
              });
              
            }
          } else {
            Toast({ message:res.data.msg});
          }
          //1、在线预约券码类的“立即查看”按钮名称改成“查看券码”
          //2、券码链接类的，点击查看券码后，直接领取并跳转 
          // 单纯只有链接的 直接跳转 
          if(this.reservOrderVo && this.reservOrderVo.shortUrl && !this.reservOrderVo.thirdCodePassword){
              this.gotousercoupon(this.reservOrderVo.shortUrl);
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    getPrice:function(startDate,productGroupProductId,giftType,num){
      this.num = getStoreNum(giftType,num);
      if(this.num>0){
        this.axios.post("/mars/boscbank/getStorePrice" ,{startDate:startDate,productGroupProductId:productGroupProductId})
          .then(res => {
            if (res.data.code == 100) {
              this.storePrice = res.data.result;
            } else {
              Toast({ message:res.data.msg});
            }
        }).catch(error => {
          console.log(error);
        });
      }
    },
    isShowCancel:function(serviceType,proseStatus,useStatus,giftDate){
      if(serviceType!=null&&serviceType.indexOf('_cpn')!=-1){
          return false;
      }else if(serviceType=='drink'){
          if(useStatus!='3' && useStatus!='4' ){
             var day = (new Date() - new Date(giftDate +" 00:00:00"))/(24*60*60*1000);
             return day>=3;
          }
      }else if(proseStatus=='0'){
          return true;
      }
      return false;
    },
    cancelOrder:function(){

      MessageBox.confirm('确定要取消预约么？','友情提示').then(action => {
          this.axios.post("/mars/reservOrder/reservCancel" , {id:this.id,refundInter:1,tags:"用户自行取消,"})
        .then(res => {
          if (res.data.code == 100) {
            // this.reservOrderVo = res.data.result;
            this.reservOrderVo.proseStatus = res.data.result.proseStatus;
            var tips = transformatStatus(this.reservOrderVo.proseStatus);
            if(!!tips){
              this.statusTxt = tips.statusTxt;
              this.statusTip = tips.statusTip;
              Toast({message:'取消成功'})
              // this.$router.push({name:'percenter',params:{}})
              this.showFlag = false
            }
          } else {
            Toast({ message:res.data.msg});
          }
        })
        .catch(error => {
          console.log(error);
        });
      })
      
    },
    gotopay:function(orderid){
          //聚合支付
            this.axios({
          method: "get",
          url: "/mars/pay/getPayParams/"+ orderid
          }).then(res => {
              if(res.data.code == 100){
                  var jsondata = JSON.parse(res.data.result.params);
                  let params = {
                      "acId": jsondata.acId,
                      "amount": jsondata.amount,
                      "body": this.reservOrderVo.hotelName?this.reservOrderVo.hotelName:'',
                      "curcode": jsondata.curcode,
                      "goodsName": this.reservOrderVo.productName?this.reservOrderVo.productName:'',
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
                      this.axios.post('/aggregatePay/pay/order',{"params": res.data.result.params,"payChannelId": 13})
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
        gotousercoupon:function(url){
          window.location.href = url
        },
        sendcoupon:function(){
          if(this.mycouponstatus){
            this.mycouponstatus = false
            this.axios.post("/mars/coupons/receiveCoupons" , {source:this.source,num :1,productNo:this.productNo,reserveOrderId:this.id})
            .then(res => {
              if (res.data.code == 100) {
                this.checkInit()
                this.mycouponstatus = false
              } else {
                // Toast({ message:res.data.msg});
                this.mycouponstatus = true
                Toast({ message:'产品缺货中，详情请致电客服4006368008'});
              }
            })
            .catch(error => {
              console.log(error);
              this.mycouponstatus = true
            });
        }
          },
         onCopy (e) {
          Toast({ message:'内容已复制到剪切板！'});
          },
          // 复制失败时的回调函数
          onError (e) {
          Toast({ message:'抱歉，复制失败！'});
          }
    }
};
</script>
<style scoped>
  .colour {color: rgba(255, 73, 38, 1);  }
   .alipaydailogBox{position: fixed; width: 100%; height: 100%; top: 0; left: 0; background: rgba(0,0,0,.6); z-index: 1000;}
  .alipaydailogMsgBox{position: fixed; width: 80%; height: 3.5rem; left: 10%; top: 50%; margin-top: -1.5rem; background: #fff; border-radius: .3rem;}
  .alipaydailogMsgBox .alipaytitle{padding: .3rem 0; text-align: center; font-weight: bold; color: #666;}
  .alipaydailogMsgBox .alipayurlTips{text-align: center; color: #999; font-size:.28rem; padding-bottom:.3rem; padding-top: .1rem; font-size: .26rem;}
  .alipaydailogMsgBox .alipayurl{padding: .2rem 0; text-align:center; color: #2577e3; font-size: .3rem;}
  .alipaydailogMsgBox .alipaycloseBtn{display: inline-block; font-weight: normal; width: .52rem; height: .32rem; color: #ddd; position: absolute; right: .3rem; top: .2rem; white-space: nowrap; font-size: .26rem;}

</style>

