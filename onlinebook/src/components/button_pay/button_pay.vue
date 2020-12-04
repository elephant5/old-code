<template>
    <div v-if="productPrice" :class="paybtnflag && btnDisable ? 'shoppingPayBox':'disablePayBox'">
            <div class="priceNumbox" v-if="productPrice">
                <div class="payTips">{{Object.keys(onlinePrice).length!=0? '在线付':'预计到店付'}}</div>
                    <div class="payMoney">
                        <!-- <p><i>¥</i>{{discountAmount*num}}</p> -->
                        <p><i>¥</i>{{productPrice.toFixed(2)}}</p>
                        <span v-if="!!offerPrice">已减{{offerPrice.toFixed(2)}}元</span>
                    </div>
            </div>
            <div>
                <div class="button_pay" >
                    <div class="paymoneylist" @click="showPrice" :class="!showFlag ? '' : 'paymoneylist_up' ">明细</div>
                    <div class="buttonWord" @click="gopay">{{payMoneyBtnTitle}}</div>
                    <div class="mask" v-show="showFlag">
                        <div class="pricelistBox" v-if="Object.keys(storePrice).length!=0" >
                            <p>费用明细</p>
                            <div class="pricelistMsg">
                                <span>价格</span>
                                <span>¥{{storePrice.netPrice}}x {{totalNum-num}}</span>
                            </div>
                            <div class="pricelistMsg" v-if="!!storePrice.serviceRate">
                                <span>服务费</span>
                                <span>{{storePrice.serviceRate*100}}% x {{totalNum}}</span>
                            </div>
                            <div class="pricelistMsg" v-if="!!storePrice.taxRate">
                                <span>增值税</span>
                                <span>{{storePrice.taxRate*100}}% x {{totalNum}}</span>
                            </div>
                            <div class="pricelistMsg">
                                <span>到店预计需付</span>
                                <span>¥{{productPrice.toFixed(2)}}</span>
                            </div>
                            <div class="pricelistTip">预计支付金额仅供参考，请以门店实际价格为准</div>
                        </div>
                        <div class="pricelistBox" v-if="Object.keys(onlinePrice).length!=0" >
                            <p>费用明细</p>
                            <div  v-if="sysServicetype==='accom'">
                                <div class="pricelistMsg" v-for="item in  onlinePrice ">
                                    <span>{{item.bookDate | dateFormat}}</span>
                                    <!-- <span>¥{{item.bookPrice}} x {{num}} 间</span> -->
                                    <span v-if="sysService.indexOf('_cpn') != -1 ">¥{{item.bookPrice}} </span>
                                    <span v-else>¥{{item.bookPrice}} x {{totalNum}} 间</span>
                                </div>
                                <!-- <div class="pricelistMsg" v-if="oldPrice-productPrice>0">
                                    <span>优惠</span>
                                    <span>-¥{{(oldPrice-productPrice).toFixed(2)}}</span>
                                </div>
                                <div class="pricelistMsg" v-if="!!storePrice.taxRate">
                                    <span>实付</span>
                                    <span>¥{{productPrice.toFixed(2)}}</span>
                                </div> -->
                            </div>
                            <div v-else>
                                <div class="pricelistMsg">
                                    <span>预付金额</span>
                                    <span>¥{{oldPrice}}</span>
                                </div>
                                <div class="pricelistMsg" v-if="discountRate != 1 || discountRate != 0"> 
                                    <span>折扣<i style="background:#f60; color:#fff; padding:.02rem .06rem; border-radius: .05rem; margin-left:.1rem;">折</i></span>
                                    <span>{{(discountRate * 10).toFixed(1)}}折</span>
                                </div>
                                <div class="pricelistMsg" v-if="oldPrice-productPrice>0">
                                    <span>优惠券<i style="background:#f00; color:#fff; padding:.02rem .06rem; border-radius: .05rem; margin-left:.1rem;">券</i></span>
                                    <span>- ¥{{((oldPrice * discountRate)-productPrice).toFixed(2)}}</span>
                                </div>
                                <div class="pricelistMsg" v-if="!!productPrice">
                                    <span>实付金额</span>
                                    <span>¥{{productPrice.toFixed(2)}}</span>
                                </div>
                            </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
    <div  :class="paybtnflag && btnDisable ? 'shoppingPaybuttonBox3':'disableBtn'" v-else  @click="gopay">
        <div class="exchange">{{buyBtnTitle}}</div>
    </div>
</template>

<script>
import{getStoreNum} from '@/common/js/util.js'
export default {
    name:'button_pay',
    data() {
        return {
            sysService:this.$store.state.sysService,
            allPrice:0,
            showFlag:false,
            offerPrice:0,
            discountRate:this.$store.state.discountRate,
        }
    },
    watch:{
        oldPrice(val,oldVal){
            this.setOfferPrice()
        },
        productPrice(val,oldVal){
            this.setOfferPrice()
        },
    },
    
    props:['gopay','totalAmount','discountAmount','num','productPrice','days','buyBtnTitle','payMoneyBtnTitle','paybtnflag','onlinePrice','storePrice','giftType','bookPrice','btnDisable','totalNum','sysServicetype','oldPrice'],
    methods:{
        pricelist:function(){

        },
        showPrice:function(){
            this.showFlag=!this.showFlag;
        },
        setOfferPrice:function(){
            let oldPrice = this.oldPrice;
            let productPrice = this.productPrice;
            if(!!productPrice&&!!oldPrice){
                this.offerPrice = oldPrice-productPrice;
            }
        }
    },
}
</script>
        
        
