<template>
    <div class="shopshowBox">
        <Topheader :headtitle="nameCh" v-if="headerBarflage"></Topheader>
        <div class="proMsgshowBox">
            <div class="bannershowBox">
            <mt-swipe>
                <mt-swipe-item class="slide1" v-for="item in shopPicsList" :key="item.guid">
                    <img :src="item.pgCdnHttpUrl+'/'+item.guid+'.'+item.ext" alt="" width="100%">
                </mt-swipe-item>
            </mt-swipe>
            </div>
            
            <div class="proMsgshow">
                <div class="proMsgName">
                   <div>
                        <p class="hotelName">{{nameCh}}</p>
                        <p class="hotelNameTip" ><i >{{shopname}}</i><i>&nbsp;</i></p>
                   </div>
                   <div class="hoteCity"></div>
                </div>
                <div class="hotel_add"><span></span>{{address}}</div>
            </div>
            <div class="shopMsgshow" v-html="summary">
                
            </div>
        </div>
         <br>
         <copyright :copyright="copyright"></copyright>
    </div>
</template>

<script>
import Vue from 'vue'
import qs from 'qs'
import { Toast } from 'mint-ui';
import Topheader  from '@/components/head/head';
import copyright  from '@/components/copyright/copyright';
import { Swipe, SwipeItem } from 'mint-ui'
import{WXbodyBottomshow,getChannge} from '@/common/js/common.js'
Vue.component(Swipe.name, Swipe)
Vue.component(SwipeItem.name, SwipeItem)
export default {
    name:'shop',
    data() {
        return {
           headerBarflage:true,
           nameCh:'',
           address:'',
           summary:'',
           shopname:'',
           productdetail: [],
           shopPicsList:[],
           list:[],
           index:0,
           copyright:''
        }
    },
    created() {
        WXbodyBottomshow()
        if(WXbodyBottomshow()){
            this.headerBarflage =false
        }else{
            this.headerBarflage = true
        }
        this.list = this.$store.state.giftShopProdList
        this.shopshow()
    },
    components: {
        Topheader,
        copyright,
    },
    mounted(){
       
    },
    methods: {
        shopshow(){
            this.axios.post('/mars/product/detail',qs.stringify({ productGroupProductId: this.$route.params.productGroupProductId, giftCodeId:this.$route.params.giftCodeId}))
                .then((res)=>{
                    console.log()
                    this.productdetail = res.data.result.productDetail
                    this.shopPicsList = this.productdetail.shopPics
                    this.summary = this.productdetail.shop.summary
                    this.nameCh = this.productdetail.shop.hotelName
                    this.shopname = this.productdetail.shop.name
                    this.address = this.productdetail.shop.address
                })
                .catch((error)=>{
                    console.log(error)
                })
        }
    },
}
</script>

<style scoped>
    .shopshowBox{background:#fff; position:absolute; width: 100%;  top: 0; min-height: 100%; z-index: 100;}
    .bannershowBox{width: 100%; position: relative;  height: 4.2rem;background: url(../../common/images/pic1.jpg) center no-repeat; background-size: cover;}
    .bannershowBox .mint-swipe {height: 4.2rem;  color: #fff;  text-align: center; }
    .bannershowBox .mint-swipe-item {line-height: 4.2rem; }
    .bannershowBox .slide1 img {position: absolute; left: 0; top: 50%; transform: translateY(-50%); }
    .proMsgshowBox .proMsgshow { padding: .2rem .3rem; }
    .proMsgshowBox .proMsgshow .proMsgName {display: flex;  justify-content: space-between;  align-content: center; align-items: center; }
    .proMsgshowBox .proMsgshow .proMsgName .hotelName {font-size: .32rem;  color: #333; font-weight: bold; }
    .proMsgshowBox .proMsgshow .proMsgName .hotelNameTip {font-size: .3rem;  color: #333;  padding: .1rem 0; }
    .proMsgshowBox .proMsgshow .proMsgName .hoteCity {color: #f60; }
    .proMsgshowBox .proMsgshow .hotel_add {padding-top: .05rem;  color: #999; font-size: .28rem; }
    .proMsgshowBox .proMsgshow .hotel_add span {display: inline-block; width: .3rem; height: .3rem;  margin-right: .1rem;  vertical-align: middle; background: url(../../common/images/bussiness/Iconvin@2x.png) center no-repeat; background-size: 90%; }
    .proMsgshowBox .shopMsgshow{padding: 0 .3rem .2rem .3rem; color: #999; line-height: .42rem;}
</style>
