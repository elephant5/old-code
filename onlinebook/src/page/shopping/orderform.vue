<template>
<div class="onloadpapy">
    <form :action="payInfo.url"  id="payForm" method="post" hidden>
        <input name="merchantID"  :value="this.payInfo.merchantID"/>
        <input name="merOrderNum" :value="this.payInfo.merOrderNum"/>
        <input name="merOrderAmt"  :value="this.payInfo.merOrderAmt"/>
        <input name="curType"  :value="this.payInfo.curType"/>
        <input name="orderDate" :value="this.payInfo.orderDate"/>
        <input name="orderTime" :value="this.payInfo.orderTime"/>
        <input name="merNotifySign"  :value="this.payInfo.merNotifySign"/>
        <input name="merNotifyUrl"  :value="this.payInfo.merNotifyUrl"/>
        <input name="merGetGoodsSign"  :value="this.payInfo.merGetGoodsSign"/>
        <input name="merGetGoodsUrl"  :value="this.payInfo.merGetGoodsUrl"/>
        <input name="signDataStr"  :value="this.payInfo.signDataStr"/>
        <input name="signData"   :value="this.payInfo.signData" />
        <input name="KoalB64Cert"  :value="this.payInfo.koalB64Cert"/>
        <input name="flag"  :value="this.payInfo.flag"/>
        <input name="shortcutMerCode"  :value="this.payInfo.shortcutMerCode"/>
        <input name="productName"  :value="this.payInfo.productName"/>
        <input name="productProperty"  :value="this.payInfo.productProperty"/>
        <input name="consigneeName"  :value="this.payInfo.consigneeName"/>
        <input name="consigneeAddress"  :value="this.payInfo.consigneeAddress"/>
        <input name="stage"  :value="this.payInfo.stage"/>
        <input name="mainPage"  :value="this.payInfo.mainPage"/>
        <input name="shopName"  :value="this.payInfo.shopName"/>
        <input name="payeeMerchantName"   :value="this.payInfo.payeeMerchantName"/>
        <input name="isGlobal"  :value="this.payInfo.isGlobal"/>
        <input name="national" :value="this.payInfo.national"/>
        <input name="merchantWebInfo" :value="this.payInfo.merchantWebInfo"/>
        <input name="terminalType"  :value="this.payInfo.terminalType"/>
        <button id="btn">提交</button>
    </form>
  <div class="onloadMask">
    <div>
      <span></span>
      <p>去支付<em id='loadStatus'>{{payload[this.curronlist]}}</em></p>
    </div>
  </div>
</div>
</template>
<script>
import { Toast } from "mint-ui";
export default {
  name: "orderform",
  data() {
    return {
      payInfo:{},
      payload:[' ','.','..','...'],
      loadinghtml:'',
      curronlist:0,
      timer: '',
    };
  },
  created() {
    this.getPayInfo();
  },
  methods: {
    get:function(){
        this.curronlist ++
        if(this.curronlist > 3){
            this.curronlist = 0;
        }
        this.loadinghtml = this.payload[this.curronlist]
    },
    //支付订单信息
    getPayInfo: function() {
        var reservOrderId = this.$route.params.reservOrderId;
        this.axios({
            method: "get",
            url: "/mars/pay/"+reservOrderId
        }).then(res => {
            this.payInfo = res.data.result;
            this.$nextTick(function () {
              document.getElementById('btn').click()
            })
        }).catch(error => {
            console.log(error);
        });
    },
  },
  mounted() {
    this.timer = setInterval(this.get, 500)
    // console.log( this.timer)
  },
  beforeDestroy() {
      clearInterval(this.timer);
  }
};
</script>