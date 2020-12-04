<template>
<div>
    <form action="https://netpay.cmbchina.com/netpayment/BaseHttp.dll?MB_APPPay" method="post" hidden >
      <input type="hidden" name="jsonRequestData" id= "jsonRequestData" v-model="cmbpayValue" />
      <input type="hidden" name="charset" value='UTF-8' />
          <button style="width:.01rem; height:.01rem;"></button>
    </form>
</div>
</template>
<script>
export default {
  name: "orderform",
  data() {
    return {
    
    };
  },
  props:['cmbpayValue'],
  created() {
    console.log(this.cmbpayValue)
  },
  methods: {
    cmbPaypram(){
      if(this.cmbpayValue){
        document.forms[0].submit()
      }
    }
  },
  mounted() {
   
  },
  beforeDestroy() {
     
  }
};
</script>