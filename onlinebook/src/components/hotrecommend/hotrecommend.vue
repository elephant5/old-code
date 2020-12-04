<template>
    <div class="hotrecommendContent">
<div class="hotrecommendBox">
            <div class="recommendTitle">
                <span>&nbsp;</span>推荐酒店<span>&nbsp;</span>
            </div>
            <div class="recommendProBox">
                <div class="recommendProMsg" v-for="(item,index) in hotproductlist" @click="productMsgShow(index,item.orderFlag,item.shopResDto.shopItemResDtoList[0].discountRate)" :class="item.orderFlag ?'' : 'overStyle'" :key="index">
                    <div class="recommendProImg">
                        <span class="baseproImgbg">
                            <span :style="{backgroundImage:'url(' + (item.shopPic ? item.shopPic : baseImg) + ')',backgroundSize:'cover', backgroundPosition:'center',backgroundRepeat:'no-repeat'}"></span>
                        </span>
                    </div>
                    <div class="recommendProName">
                        <div class="hotel_name">{{item.hotel.nameCh}}</div>
                        <div class="hotel_istance"><i  v-if="sysServicetype!='accom'">{{item.shopResDto.shop.name}}</i><i v-else>&nbsp;</i></div>
                        <div class="hotel_tips"><span>{{item.shopResDto.shopItemResDtoList[0].productGroupProduct.gift |getGiftTxt}}</span></div>
                        <!-- <div class="hotel_tips" v-if="item.shopResDto.shopItemResDtoList[0].productGroupProduct.gift=='3F1'"><span>三免一</span></div>
                        <div class="hotel_tips" v-if="item.shopResDto.shopItemResDtoList[0].productGroupProduct.gift=='B1F1'"><span>买一赠一</span></div>
                        <div class="hotel_tips" v-if="item.shopResDto.shopItemResDtoList[0].productGroupProduct.gift=='D5'"><span>五折</span></div>
                        <div class="hotel_tips" v-if="item.shopResDto.shopItemResDtoList[0].productGroupProduct.gift=='F1'"><span>单免</span></div>
                        <div class="hotel_tips" v-if="item.shopResDto.shopItemResDtoList[0].productGroupProduct.gift=='F2'"><span>双免</span></div>
                        <div class="hotel_tips" v-if="item.shopResDto.shopItemResDtoList[0].productGroupProduct.gift=='N1'"><span>两天一晚</span></div>
                        <div class="hotel_tips" v-if="item.shopResDto.shopItemResDtoList[0].productGroupProduct.gift=='N2'"><span>三天两晚</span></div>
                        <div class="hotel_tips" v-if="item.shopResDto.shopItemResDtoList[0].productGroupProduct.gift=='N3'"><span>四天三晚</span></div>
                        <div class="hotel_tips" v-if="item.shopResDto.shopItemResDtoList[0].productGroupProduct.gift=='N4'"><span>五天四晚</span></div>
                        <div class="hotel_tips" v-if="item.shopResDto.shopItemResDtoList[0].productGroupProduct.gift=='NX'"><span>开放住宿</span></div> -->
                        <div class="hotel_add"><span>&nbsp;</span>{{item.shopResDto.shop.address}}</div>
                        <div class="hotel_type" v-for="items in item.shopResDto.shopItemResDtoList"><span class="businesspro_Icon" :class="{
                            	'businesspro_Icon_accom': sysServicetype == 'accom',
                                'businesspro_Icon_buffet': sysServicetype == 'buffet',
                                'businesspro_Icon_spa': sysServicetype == 'spa',
                                'businesspro_Icon_gym': sysServicetype == 'gym',
                                'businesspro_Icon_tea': sysServicetype == 'tea',
                                'businesspro_Icon_drink': sysServicetype == 'drink',
                        }"></span>
                        <i v-if='items.shopItem.name'>{{items.shopItem.name}} </i>
                        <i v-if='items.shopItem.needs'>| {{items.shopItem.needs}} </i>
                        <i v-if='items.shopItem.addon'>| {{items.shopItem.addon}} </i></div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</template>
<script>
import qs from 'qs'
import { hotelTip } from 'util';
export default {
    name:'recommend',
    data() {
        return {
           baseImg:require('../../common/theme/default/images/pic.jpg'),
           sysServicetype: this.$store.state.sysService,
           giftType:'',
        }
    },
    props:['getProductList','hotproductlist'],
    created() {
       this.getProductList()
    },
    methods: {
        productMsgShow(index,orderFlag,discountrate){
            if(orderFlag){
                this.$store.commit('getGiftShopProdList',this.hotproductlist[index])
                this.$store.commit('getDiscountRate',discountrate)
                this.giftType = this.hotproductlist[0].shopResDto.shopItemResDtoList[0].productGroupProduct.gift
                this.$router.push({name:'productshow',params:{type:this.giftType}})
            }
        },
    },
}
</script>