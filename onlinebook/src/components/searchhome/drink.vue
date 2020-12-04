<template>
    <div class="drink_homeBox">
        <div class="drinksearchContent">
            <div class="searchMsg">
                <div class="search_city">全国 <span class="pulldown"></span></div>
                <div class="search_hotel"><span class="searchicon"></span><input type="text" name="" placeholder="搜索酒店名称" id="" class="searchinput"></div>
            </div>
        </div>
        <div class="productlist">
            <div class="productlistBox">
                <div class="productlistMsg" v-for="(item,index) in articles_array"  @click="productMsgShow(index,item.orderFlag)" :class="item.orderFlag ?'' : 'overStyle'">
                    <div class="recommendProImg">
                        <span class="baseproImgbg">
                            <span :style="{backgroundImage:'url(' + (item.shopPic ? item.shopPic : baseImg) + ')',backgroundSize:'cover', backgroundPosition:'center',backgroundRepeat:'no-repeat'}"></span>
                        </span>
                    </div>
                    <div class="recommendProName">
                        <div class="hotel_name">{{item.hotel.nameCh}}</div>
                        <div class="hotel_istance"><i  v-if="sysServicetype!='accom'">{{item.shopResDto.shop.name}}</i><i v-else>&nbsp;</i></div>
                        <div class="hotel_tips"><span>{{item.shopResDto.shopItemResDtoList[0].productGroupProduct.gift |getGiftTxt}}</span></div>
                        <div class="hotel_add"><span>&nbsp;</span>{{item.shopResDto.shop.address}}</div>
                        <div class="hotel_type" v-for="items in item.shopResDto.shopItemResDtoList"><span class="businesspro_Icon" :class="{
                            	'businesspro_Icon_accom': sysServicetype == 'accom',
                                'businesspro_Icon_buffet': sysServicetype == 'buffet',
                                'businesspro_Icon_spa': sysServicetype == 'spa',
                                'businesspro_Icon_gym': sysServicetype == 'gym',
                                'businesspro_Icon_tea': sysServicetype == 'tea',
                                'businesspro_Icon_airportshuttle': sysServicetype == 'car',
                                'businesspro_Icon_viphall': sysServicetype == 'lounge',
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
export default {
    name:'drink',
    data() {
        return {
           searchString:'',
           baseImg:require('../../common/theme/default/images/pic.jpg'),
        }
    },
    props:['items','routersysService'],

    created() {
       
    },
    methods: {
        searchinput:function(){
            this.$emit('searchinputstring',this.searchString)
        },
        productMsgShow(index,orderFlag){
            if(orderFlag){
                this.$store.commit('getGiftShopProdList',this.articles_array[index])
                this.$router.push({name:'productshow',params:{}})
            }
        },
    },

    computed: {
        
    },
}
</script>


