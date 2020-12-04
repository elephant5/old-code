<template>
    <div class="vipListBox">
        <div class="MainBox"></div>
        <Topheader :headtitle="headtitle" v-if="headerBarflage"></Topheader>


         <div class="basesearch">
            <div class="searchMsg">
                <div class="search_city"><router-link to="/citylist">选择城市</router-link><span class="pulldown"></span></div>
                <div class="search_date">
                        <div><p>04-01</p></div>
                        <div><span class="pulldown"></span></div>
                    </div>
                <div class="search_hotel"><span class="searchicon"></span><input type="text" name="" v-model="buffetname" ref="buffetname"  placeholder="请输入关键字" id="" class="searchinput"></div>
            </div>
        </div>


        <div class="roomsearch">
            <div class="searchMsg">
                <div class="search_city"><router-link to="/citylist">选择城市 </router-link> <span class="pulldown"></span></div>
                <div class="search_date">
                    <div><p><i>住</i>04-01</p><p><i>离</i>04-21</p></div>
                    <div><span class="pulldown"></span></div>
                </div>
                <div class="search_hotel">
                    <span class="searchicon"></span>
                    <input type="text" name="" placeholder="搜索酒店名称" v-model="inputname" ref="inputname" class="searchinput"></div>
            </div>
        </div>



        <div class="descOrdBox">
            <div class="descTypeCon">
                <span class="curron">自助餐</span><span>下午茶</span><span>定制套餐</span><span>单杯茶饮</span>
            </div>
            <div class="descAllListBox">
                <div class="descSearchname">
                    <span class="curron">推荐</span><span>价格</span><span>优惠</span><span>销量</span>
                </div>
                <div class="descSearchshow">排序</div>
            </div>
        </div>
        <div class="vipProductBox">
            <div class="productlistMsg">
                <div class="recommendProImg">
                    <span class="baseproImgbg">
                        <span></span>
                    </span>
                </div>
                <div class="recommendProName">
                    <div class="hotel_name">北京瑞吉酒店</div>
                    <div class="hotel_istance"><i>景苑咖啡厅</i></div>
                    <div class="hotel_tips"><span>两人同行享五折优惠</span></div>
                    <div class="hotel_add"><span>&nbsp;</span>北京市朝阳区建国门外大街21号</div>
                    <div class="hotel_type">
                        <span class="businesspro_Icon businesspro_Icon_buffet"></span> <i>午餐 </i>
                        <span style="float: right;"><i class="moneyTip"><span style="font-size: 0.22rem;">¥</span>1.00<span class="moneyTip2">起</span></i></span>
                    </div>
                    <div class="hotel_type"><span class="businesspro_Icon businesspro_Icon_buffet"></span> <i>晚餐 </i> <span style="float: right;"></span></div>
                </div>
            </div>

            <div class="productlistMsg">
                <div class="recommendProImg">
                    <span class="baseproImgbg">
                        <span></span>
                    </span>
                </div>
                <div class="recommendProName">
                    <div class="hotel_name">爱奇艺VIP会员月卡</div>
                    <div class="hotel_tips"><span class="couponTips">视频VIP券</span></div>
                    <div class="couponMoneyBox"><span style="font-size: 0.22rem;">¥</span>1.00</div>
                </div>
            </div>

            <div class="productlistMsg">
                <div class="recommendProImg">
                    <span class="baseproImgbg">
                        <span></span>
                    </span>
                </div>
                <div class="recommendProName">
                    <div class="hotel_name">北京瑞吉酒店</div>
                    <div class="hotel_istance"><i>景苑咖啡厅</i></div>
                    <div class="hotel_tips"><span>两人同行享五折优惠</span></div>
                    <div class="hotel_add"><span>&nbsp;</span>北京市朝阳区建国门外大街21号</div>
                    <div class="hotel_type">
                        <span class="businesspro_Icon businesspro_Icon_buffet"></span> <i>午餐 </i>
                        <span style="float: right;"><i class="moneyTip"><span style="font-size: 0.22rem;">¥</span>1.00<span class="moneyTip2">起</span></i></span>
                    </div>
                    <div class="hotel_type"><span class="businesspro_Icon businesspro_Icon_buffet"></span> <i>晚餐 </i> <span style="float: right;"></span></div>
                </div>
            </div>

            <div class="productlistMsg">
                <div class="recommendProImg">
                    <span class="baseproImgbg">
                        <span></span>
                    </span>
                </div>
                <div class="recommendProName">
                    <div class="hotel_name">爱奇艺VIP会员月卡</div>
                    <div class="hotel_tips"><span class="couponTips">视频VIP券</span></div>
                    <div class="couponMoneyBox"><span style="font-size: 0.22rem;">¥</span>1.00</div>
                </div>
            </div>

            <div class="productlistMsg">
                <div class="recommendProImg">
                    <span class="baseproImgbg">
                        <span></span>
                    </span>
                </div>
                <div class="recommendProName">
                    <div class="hotel_name">北京瑞吉酒店</div>
                    <div class="hotel_istance"><i>景苑咖啡厅</i></div>
                    <div class="hotel_tips"><span>两人同行享五折优惠</span></div>
                    <div class="hotel_add"><span>&nbsp;</span>北京市朝阳区建国门外大街21号</div>
                    <div class="hotel_type">
                        <span class="businesspro_Icon businesspro_Icon_buffet"></span> <i>午餐 </i>
                        <span style="float: right;"><i class="moneyTip"><span style="font-size: 0.22rem;">¥</span>1.00<span class="moneyTip2">起</span></i></span>
                    </div>
                    <div class="hotel_type"><span class="businesspro_Icon businesspro_Icon_buffet"></span> <i>晚餐 </i> <span style="float: right;"></span></div>
                </div>
            </div>

            <div class="productlistMsg">
                <div class="recommendProImg">
                    <span class="baseproImgbg">
                        <span></span>
                    </span>
                </div>
                <div class="recommendProName">
                    <div class="hotel_name">爱奇艺VIP会员月卡</div>
                    <div class="hotel_tips"><span class="couponTips">视频VIP券</span></div>
                    <div class="couponMoneyBox"><span style="font-size: 0.22rem;">¥</span>1.00</div>
                </div>
            </div>



        </div>
    </div>
</template>

<script>
import Vue from 'vue'
import Topheader  from '@/components/head/head';
import{WXbodyBottomshow,getChannge} from '@/common/js/common.js'
import { Toast} from "mint-ui";
export default {
    name:'viplist',
    data() {
        return {
            headtitle:'超级会员',
            headerBarflage:true,
            buffetname:'',
            inputname:''
        }
    },
    created(){
        WXbodyBottomshow()
        if(WXbodyBottomshow()){
            this.headerBarflage =false
        }else{
            this.headerBarflage = true
        }
    },
    components: {
        Topheader,
    },
    methods: {

    },
    
}
</script>
<style scoped>
.basesearch {background: #fff; padding: .1rem .2rem;}
.basesearch .searchTypelist {display: flex; justify-content: flex-start; align-items: center; align-content: center; position: relative; margin-bottom: .05rem; }
.basesearch .searchTypelist div {flex: 1;  text-align: center; padding: .2rem 0; color: #666; position: relative; }
.basesearch .searchTypelist div.curron {color: #333; font-weight: bold; }
.basesearch .searchTypelist div.curron:after {content: ""; position: absolute;  width: .5rem; height: .04rem; background: #f60;  left: 50%;  margin-left: -.25rem; bottom: 0; }
.basesearch .searchMsg {display: flex; justify-content: flex-start; align-items: center;  align-content: center; background: #f8f8f8;  padding: .15rem .15rem; border-radius: .05rem;}
.basesearch .searchMsg .search_city {padding-right: .3rem; max-width: 30%; }
.basesearch .searchMsg .search_city a {color: #666; }
.basesearch .searchMsg .search_date {display: flex; justify-content: flex-start; align-items: center;  align-content: center; border-left: solid 0.03rem #eee; border-right: solid 0.03rem #eee; padding: 0 .3rem; margin-right: .3rem; font-size: .24rem; }
.basesearch .searchMsg .search_date i {color: #999; margin-right: .1rem; }
.basesearch .searchMsg .search_hotel { flex: 1; }
.basesearch .searchMsg .search_hotel .searchinput {background: none; height: .6rem; width: 80%; }
.basesearch .searchMsg .search_hotel .searchicon {display: inline-block; width: .22rem; height: .22rem; background: url(../../common/images/search.png) center no-repeat; background-size: 100%; margin-right: .1rem; }
.basesearch .searchMsg .pulldown { display: inline-block; width: .18rem; height: .18rem;  background: url(../../common/images/pulldown.png) center no-repeat;  background-size: 90%;  margin-left: .1rem; }

.roomsearch{background: #fff; padding: .1rem  .2rem;}
.roomsearch .searchMsg{display: flex; justify-content: flex-start; align-items: center; align-content: center; background: #f8f8f8;; padding: .15rem .15rem; border-radius: .05rem;}
.roomsearch .search_city{padding-right: .3rem; max-width: 30%;}
.roomsearch .search_city a{color:#666}
.roomsearch .search_date{display: flex; justify-content: flex-start; align-items: center; align-content: center; border-left: solid .03rem #eee; border-right: solid .03rem #eee; padding:0 .3rem; margin-right: .3rem; font-size: .24rem;}
.roomsearch .search_date i{color:#999; margin-right: .1rem;}
.search_hotel{flex: 1;}
.searchinput{background: none; height: .6rem; width: 80%; font-size: .32rem;}
.searchicon{display: inline-block;  width: .22rem; height: .22rem; background: url(../../common/images/search.png) center no-repeat; background-size: 100%; margin-right: .1rem;}
.pulldown{display: inline-block; width: .18rem; height: .18rem; background: url(../../common/images/pulldown.png) center no-repeat; background-size: 90%; margin-left: .1rem;}
  




    .descOrdBox{box-shadow: 0 0 0.05rem #f3f3f3; border-bottom: solid .02rem #f3f3f3;}
    .descTypeCon{width: 100%; display: flex; justify-content: space-between; align-items: center; align-content: center; padding: 0 .2rem; box-sizing: border-box;}
    .descTypeCon span{text-align: center; width: 25%; line-height: .52rem; border-bottom: solid .02rem #f3f3f3; color: #999; font-size: .2rem; position: relative; padding-top: .1rem;}
    .descTypeCon span.curron{color: #333; font-weight: bold;}
    .descTypeCon span.curron:before{content: ''; width: .3rem; height: .05rem; background: #f60; position: absolute; left: 50%; bottom:0;  transform: translate(-50%,0)}
    .descAllListBox{width: 100%; line-height: .66rem; display: flex; justify-content: space-between; align-items: center; align-content: center; padding: 0 .2rem; box-sizing: border-box; color: #999;}
    .descSearchname{flex: 1;display: flex; justify-content:flex-start; align-items: center; align-content: center; }
    .descSearchname span{margin: 0 .1rem; width: 11%;}
    .descSearchname span.curron{color: #FF6B00;}
    .descSearchshow{width: 1rem; text-align: center;}
    .vipListBox{background:#fff; position:absolute; width: 100%;  top: 0; min-height: 100%; z-index: 100;}
    .productlistMsg{display: flex;  justify-content: flex-start;  border-bottom: solid 0.02rem #f8f8f8;  padding: .3rem .3rem; }
    .recommendProImg { border-radius: .1rem; overflow: hidden; width: 1.86rem; height: 1.86rem; padding-right: .2rem;  position: relative; }
    .baseproImgbg{display: inline-block; border-radius: .1rem; overflow: hidden; width: 1.86rem; height: 1.86rem;  position: relative; background: url(../../common/images/pic1.jpg) center no-repeat; background-size: cover;}
    .baseproImgbg span {display: inline-block;  border-radius: .1rem;   overflow: hidden;  width: 1.86rem; height: 1.86rem; position: relative; }
    .recommendProName{font-size: .26rem; -webkit-box-flex: 1;  -ms-flex: 1; flex: 1; overflow: hidden;}
    .hotel_name{font-size: .28rem; font-weight: bold; color: #333;}
    .hotel_istance { color: #666;  padding: .1rem 0; text-align: right; display: flex;justify-content: space-between; }
    .hotel_tips span { border: solid 0.02rem #f60;  color: #f60; padding: .02rem .08rem; font-size: .22rem; border-radius: .05rem; }
    .hotel_tips span.couponTips{margin-top: .3rem; display: inline-block;}
    .hotel_add { color: #666; padding: .1rem 0;  line-height: .42rem; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
    .hotel_add span {display: inline-block; width: .3rem; height: .3rem; margin-right: .1rem; vertical-align: middle; background: url(../../common/images/vist.png) center no-repeat; background-size: 90%; }
    
    .hotel_type {background: #f8f8f8;  padding: .05rem .1rem; color: #666; }
    .hotel_type .moneyTip{margin-left: 0.15rem; color: rgb(255, 102, 0); font-size: 0.3rem;}
    .hotel_type .moneyTip2{font-size: 0.22rem; margin-left: 0.02rem;}
    .hotel_type .businesspro_Icon {display: inline-block;  width: .4rem; height: .4rem;  margin-right: .1rem; vertical-align: middle; }
    .hotel_type .businesspro_Icon_buffet {background: url(../../common/images/bussiness/gift/buffet.png) center no-repeat; background-size: 90%; }
    .hotel_type .businesspro_Icon_accom {background: url(../../common/images/bussiness/gift/accom.png) center no-repeat; background-size: 90%; }
    .hotel_type .businesspro_Icon_gym {background: url(../../common/images/bussiness/gift/gym.png) center no-repeat; background-size: 90%; }
    .hotel_type .businesspro_Icon_spa {background: url(../../common/images/bussiness/gift/spa.png) center no-repeat; background-size: 90%; }
    .hotel_type .businesspro_Icon_tea {background: url(../../common/images/bussiness/gift/tea.png) center no-repeat; background-size: 90%; }
    .hotel_type .businesspro_Icon_drink {background: url(../../common/images/bussiness/gift/single-cup.png) center no-repeat; background-size: 90%; }
    .hotel_type .businesspro_Icon_setmenu {background: url(../../common/images/bussiness/gift/set-mea.png) center no-repeat; background-size: 90%; }
    .hotel_type .businesspro_Icon_gift {background: url(../../common/images/bussiness/gift/coupon.png) center no-repeat; background-size: 90%; }


    .couponMoneyBox{color: #f60; padding: .25rem 0 0 0; font-size: .38rem;}
</style>