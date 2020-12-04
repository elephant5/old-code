<template>
    <div class="safarishowTipdailog">
        <div style="color:#fff;"> ccb</div>
    </div>
</template>
<script>
import{getChannge} from '@/common/js/common.js'
export default {
  name: "safarishow",
  data() {
    return {
    };
  },
  created() {
    
  },
  mounted(){
    let ua = window.navigator.userAgent.toLowerCase()
    if (ua.match(/MicroMessenger/i) == 'micromessenger') {
        // 微信内
        // console.log(this.$store.state.thirdbackurl)
        return 
    }else{
        // 2.2微信外
        this.axios({
          method: "get",
          url: "/mars/pay/"+ getChannge('reservOrderId')
        }).then(res => {
          window.location.href= res.data.result.ccbPayRes.url
        }).catch(error => {
            console.log(error);
        });
        // console.log(this.$store.state.thirdbackurl)
        // window.location.href = this.$store.state.thirdbackurl
    }
  },
  methods: {
  }
};
</script>