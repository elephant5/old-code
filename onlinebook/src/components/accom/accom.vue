<template>
    <div>
        <div class="searchBox">
            <input type="text" name="" id="" v-model="searchString"  class="searchinput" @keyup="searchinput" placeholder="搜索兑换券名称">
        </div>
        <!-- <div class="proCouponlist"  @click="productMsgShow(item.shop.id)" v-for="item in productlist"> -->
        <div class="proCouponlist"  v-for="item in items"  @click="productMsgShow(item.res.productGroupProduct.id,item.discountRate,item.shopPriceRule,(item.res.productGroup.discountRate)*100)">
            <div class="proCouponImg"><span><img :src="item.res.shopItemPics" alt=""></span></div>
            <div class="proCouponName">
                <p class="proNameTitle">{{item.res.shopItem.name}}</p>
                <p class="proNameTip">{{item.res.shop.name}}</p>
                <div class="recommendProPrice">
                    <div><span><i>¥</i>{{item.discountRate}}</span><em>¥{{item.shopPriceRule}}</em></div>
                    <div><span>{{(item.res.productGroup.discountRate)*100}}折</span></div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import qs from 'qs'
export default {
    name:'accom',
    data() {
        return {
           searchString:'',
        }
    },
    props:['items','routersysService'],

    created() {
       
    },
    methods: {
        searchinput:function(){
            this.$emit('searchinputstring',this.searchString)
        },
        productMsgShow(productgroupId,discountRate,shopPriceRule,rule){
            this.$router.push({name:'productshow',params:{productgroupId:productgroupId,discountRate:discountRate,shopPriceRule:shopPriceRule,rule:rule}})
            // console.log(productgroupId)
        },
    },

    computed: {
        
    },
}
</script>

<style lang="scss">
@import '../../common/theme/default/css/index.scss';
.searchBox{padding:.1rem .3rem;
    .searchinput{
        border:none; background: #f8f8f8 url(../../common/images/search.png); height: .64rem; line-height: .64rem; width: 93%; border-radius: .06rem; background-position: .15rem .15rem; background-repeat:no-repeat; background-size: .3rem; padding-left: .55rem;
    }
}
.proCouponlist{padding: .2rem 0; display: flex; justify-content: flex-start; border-bottom: solid .02rem #f3f3f3; margin: 0 .3rem;
    .proCouponImg{
        span{display: block; width: 2.2rem; height: 1.8rem; margin-right: .2rem; overflow: hidden; border-radius: .06rem;
            img{width: 100%
            }
        }
    }
    .proCouponName{flex-grow:1;
        .proNameTitle{font-size: $pro_title_size; font-weight: bold;}
        .proNameTip{color: $color_666}
    }
    .recommendProPrice{display: flex; justify-content: space-between; padding-top: .6rem;
        div:first-child{
            span{color: $color_f60; font-size: .32rem; margin-right: .2rem;
                    i{
                        font-size: .26rem;
                    }
                }
                em{text-decoration: line-through; color: $color_666}
        }
        div:last-child{
            span{display: inline-block; background: $color_f60; color:$color_fff; padding: .02rem .1rem; border-radius: .05rem;}
        }
    }
}
</style>


