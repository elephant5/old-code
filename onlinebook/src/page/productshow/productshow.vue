<template>
    <div class="productlistshowBox">
        <Topheader :headtitle="nameCh" v-if="headerBarflage"></Topheader>
        <div class="proMsgshowBox">
            <div class="bannerBox">
            <mt-swipe @change="handleChange">
                <mt-swipe-item class="slide1" v-for="item in shopPicsList" :key="item.guid">
                    <img :src="item.pgCdnHttpUrl+'/'+item.guid+'.'+item.ext" alt="" width="100%">
                </mt-swipe-item>
            </mt-swipe>
            </div>
            
            <div class="proMsgshow">
                <div class="proMsgName">
                   <div>
                        <p class="hotelName">{{nameCh}}</p>
                        <p class="hotelNameTip" ><i  v-if="sysServicetype!='accom'">{{list.shopResDto.shop.name}}</i><i v-else>&nbsp;</i></p>
                   </div>
                   <div class="hoteCity"></div>
                </div>
                <div class="hotel_add" v-if="address"><span></span>{{address}}</div>
            </div>
        </div>

        <detailroomSearch v-if="sysServicetype=='accom'" v-on:parndtMsg="showchildMsg"></detailroomSearch>
        <div class="searchBar" id="searchBar" >
            <ul :class="searchBarFixed ? 'isFixed' :''"  v-if="list.shopResDto.shopItemResDtoList.length >='2'">
                <li v-for="(item,index) in list.shopResDto.shopItemResDtoList" v-on:click="selsectproduct(index)" v-bind:class="{'active':index == productcurronindex}" :key="item.shopItem.name">{{item.shopItem.name}}</li>
            </ul>
        </div>
            
      <div class="proMsgHome">
        <div class="proMsgWord">
            <div class="product_roomBox" >
                <div class="product_roomMsg">
                    <!-- <div class="product_roomMsgPic"><span :style="{backgroundImage:'url(' + (productRoomMsgPic ? productRoomMsgPic : baseImg) + ')',backgroundSize:'cover', backgroundPosition:'center',backgroundRepeat:'no-repeat'}"></span></div> -->
                    <div class="product_roomMsgName">
                        <p v-if='list.shopResDto.shopItemResDtoList[productcurronindex].shopItem.name'> {{list.shopResDto.shopItemResDtoList[productcurronindex].shopItem.name}}</p> 
                        <p>
                            <i v-if='list.shopResDto.shopItemResDtoList[productcurronindex].shopItem.addon'> {{list.shopResDto.shopItemResDtoList[productcurronindex].shopItem.addon}}</i> 
                            <i v-if='list.shopResDto.shopItemResDtoList[productcurronindex].shopItem.needs'>| {{list.shopResDto.shopItemResDtoList[productcurronindex].shopItem.needs}}</i>
                        </p>
                        <p><i v-if="cancelPolicy == 1">不可取消</i></p>
                        <p><i v-if="cancelPolicy == 2 && sysServicetype=='accom'">入住24小时前可限时取消</i></p>
                        <p><i v-if="cancelPolicy == 2 && (sysServicetype=='buffet' || sysServicetype=='tea')">用餐日24小时前可限时取消</i></p>
                        <p><i v-if="cancelPolicy == 2 && (sysServicetype=='gym' || sysServicetype=='spa')">24小时前可限时取消</i></p>
                        <p><i v-if="cancelPolicy == 3 && sysServicetype=='accom'">入住48小时前可限时取消</i></p>
                        <p><i v-if="cancelPolicy == 3 && (sysServicetype=='buffet' || sysServicetype=='tea')">用餐日48小时前可限时取消</i></p>
                        <p><i v-if="cancelPolicy == 3 && (sysServicetype=='gym' || sysServicetype=='spa')">48小时前可限时取消</i></p>
                        <p><i v-if="cancelPolicy == 4">随时取消</i></p>
                    </div>
                </div>
                <div class="product_roomMsgPrice">
                        <p class="priceTips" v-if="list.shopResDto.shopItemResDtoList[productcurronindex].productGroupProduct.gift"><span>{{list.shopResDto.shopItemResDtoList[productcurronindex].productGroupProduct.gift |getGiftTxt}}</span></p>
                     <p class="priceRad" v-if="list.productGroup.discountRate"><span>{{list.productGroup.discountRate}}折</span></p>
                    <!-- <p class="pricerMoney" v-if="list.shopResDto.shopItemResDtoList[productcurronindex].discountRate"><span>¥{{list.shopResDto.shopItemResDtoList[productcurronindex].shopPriceRule}}</span><em>¥{{list.shopResDto.shopItemResDtoList[productcurronindex].discountRate}}</em></p> -->
                     <p class="pricerMoney" style="position: absolute;right: 50px; bottom:-.2rem; margin-left:-1.55rem;" v-if="productPrice != ' '"><em><i style="font-size:.23rem;">¥</i> {{(productPrice * this.discountRate).toFixed(2)}}<i style="font-size:.25rem;"> 起</i></em> </p>
                     <p style="position: absolute;right: 0; bottom:-.2rem; margin-left:.15rem;text-decoration: line-through; color: #ccc;" v-if="productPrice != ' '"><em><i style="font-size:.22rem;">¥</i> {{productPrice}}</em> </p>
                </div>
            </div>
        </div>
        <div class="proMsgWord" v-if="sysServicetype == 'buffet' && !menuPicsList && !list.shopResDto.shopItemResDtoList[productcurronindex].shopItem.menuText">
            <div class="proMsgType">菜单</div>
            <div class="menuDiv" v-if="menuPicsList.length >= '1'">
                <span v-for="item in menuPicsList" :key="item.guid" :style="{backgroundImage:'url(' + item.pgCdnHttpUrl+'/'+item.guid+'.'+item.ext + ')',backgroundSize:'cover', backgroundPosition:'bottom',backgroundRepeat:'no-repeat'}">
                </span>
            </div>
            <div class="productbaseMsg" v-html="list.shopResDto.shopItemResDtoList[productcurronindex].shopItem.menuText">
                <br>
            </div>
        </div>


        <div class="proMsgWord" v-if="blockRule || children || parking || notice || openTime">
            <div class="proMsgType">温馨提示</div>
            <div class="productbaseMsg">
                <div class="productsTipsBox" v-if="blockRule">
                    <div>不可用日期</div>
                    <div>{{blockRule}}</div>
                </div>
                <div class="productsTipsBox" v-if="children">
                    <div>儿童政策</div>
                    <div>{{children}}</div>
                </div>
                <div class="productsTipsBox" v-if="parking">
                    <div>泊车信息</div>
                    <div>{{parking}}</div>
                </div>
                <div class="productsTipsBox" v-if="notice">
                    <div>重要通知</div>
                    <div>{{notice}}</div>
                </div>
                <div class="productsTipsBox" v-if="openTime" :class="openTime ? 'lastline' : ' '">
                    <div><i v-if="sysServicetype == 'accom'">入离时间</i><i v-else-if="sysServicetype == 'buffet' || sysServicetype == 'tea' || sysServicetype == 'setmenu' ">开餐时间</i><i v-else-if="sysServicetype == 'spa'||sysServicetype == 'gym'">开放时间</i></div>
                    <div>{{openTime}} - {{closeTime}}</div>
                </div>
            </div>
        </div>

        <div class="proMsgWord" v-if="superposition == 1 || singleThread == 1 || (enableMaxNight != null && enableMaxNight == 1)|| (enableMinNight != null && enableMinNight == 1) || allYear == 1 || disableCall == 1">
            <div class="proMsgType">预订限制</div>
            <div class="productbaseMsg">
                <p v-if="superposition == '1'">同一时段权益不叠加</p>
                <p v-if="singleThread == '1'">行权完毕次日才可以进行下一次预约</p>
                <p v-if="maxNights !=null && enableMaxNight == '1'">最大可预订{{maxNights}}间夜</p>
                <p v-if="minNights !=null && enableMinNight == '1'">最小可预订{{minNights}}间夜</p>
                <!-- <p v-if="enableMaxNight == '1' && maxNight !=null">最大可预订{{productdetail.goodsSetting.maxNight}}间夜</p> -->
                <p v-if="allYear == '1'">全年无限制）</p>
                <p v-if="disableCall == '1'">禁止来电预约</p>
            </div>
        </div>

        <div class="proMsgWord" v-if="shopItem != null && sysServicetype.indexOf('_cpn') != -1 && shopItem.menuText && shopItem.menuText != '' && shopItem.menuText != '<p></p>'">
            <div class="proMsgType">商品介绍</div>
            <div class="productbaseMsg" v-html="shopItem.menuText">
            </div>
        </div>

        <div class="proMsgWord" v-if="productdetail.goodsClauseList" style="margin-bottom:.2rem;">
            <div class="proMsgType">使用细则</div>
            <div class="productbaseMsg" v-for="item in productdetail.goodsClauseList" :key="item.clauseType">
                <div v-if="item.clauseType == sysServicetype && item.clause"  v-html="item.clause"></div>
            </div>
        </div>
      </div>
         <br>
         <copyright :copyright="copyright"></copyright>
         <div style="height:1.5rem;"></div>
        <buttonBuy :goshopping="goshopping" :buyBtnTitle="buyBtnTitle" :productPrice="productPrice"  :buybtnflag="buybtnflag" ></buttonBuy>
        <backthird v-if='backthirdFlag'></backthird>
    </div>
</template>

<script>
import Vue from 'vue'
import qs from 'qs'
import { Toast } from 'mint-ui';
import { hotelTip } from 'util';
import buttonBuy from '@/components/button_buy/button_buy'
import detailroomSearch from '@/components/searchdetail/roomdetailsearch'
import Topheader  from '@/components/head/head';
import backthird from '@/components/bottombar/backthird';
import copyright  from '@/components/copyright/copyright';
import { Swipe, SwipeItem } from 'mint-ui'
import{WXbodyBottomshow,getChannge} from '@/common/js/common.js'
Vue.component(Swipe.name, Swipe)
Vue.component(SwipeItem.name, SwipeItem)
export default {
    name:'productshow',
    data() {
        return {
            giftnum:'',
            cancelPolicy:'',
            blockRule:'',
            children:'',
            parking:'',
            notice:'',
            openTime:'',
            closeTime:'',
            productId: '',
            productdetail: [],
            blockRules: [],
            bookDatesList: [],
            routerParams:'',
            name:'',
            nameCh:'',
            addon:'',
            needs:'',
            gift:'',
            summary:'',
            address:'',
            shopItemPics:'',
            sysServicetype:this.$store.state.sysService,
            baseImg:require('../../common/theme/default/images/pic.jpg'),
            buyBtnTitle:'-',
            searchBarFixed:false,
            productcurronindex:0,
            list:[],
            shopPicsList:[],
            superposition: '',
            singleThread:'',
            enableMaxNight:'',
            enableMinNight:'',
            // maxNight:'',
            allYear:'',
            disableCall:'',
            menuPicsList:[],
            productRoomMsgPic:'',
            personday:this.$store.state.personDay,
            maxNights: 9,
            minNights: 1,
            sum: null,
            productPrice:'',
            roomNum:1,
            buybtnflag:true,
            shopItem:{},
            headtitle:'商户介绍',
            headerBarflage:true,
            bottomTabflag:true,
            domainname: location.host.split(".")[1]+"."+location.host.split(".")[2], //一级域名
            discountRate: this.$store.state.discountRate?this.$store.state.discountRate:'',
            copyright:'',
            backthirdFlag:false,//返回第三方按钮
        }
    },
    created() {
        WXbodyBottomshow()
        if(WXbodyBottomshow()){
            this.headerBarflage =false
        }else{
            this.headerBarflage = true
        }
        if(getChannge('channel')){
            localStorage.setItem('CHANNEL',getChannge('channel').toUpperCase())
            this.$store.commit('getChannel',getChannge('channel').toUpperCase())
        }
        if(getChannge('backurl')){
            this.$store.commit('getBackUrl',window.location.href.split("&backurl=")[1])
            $cookies.set('THIRDBACKURL',window.location.href.split('backurl=')[1],'','/', this.domainname)
        }else{
            $cookies.set('THIRDBACKURL','','','/', this.domainname)
        }
        if(localStorage.getItem('disablebottom') && localStorage.getItem('disablebottom') == 'show' ){
            this.bottomTabflag = false
        }else{
            this.bottomTabflag = true
        }
        if(this.$store.state.backUrl){
            this.backthirdFlag = true
        }else{
            this.backthirdFlag = false
        }

        //饶过前面，直接卖单品处理
        this.list = this.$store.state.giftShopProdList;
        this.selsectproduct(this.productcurronindex)
        this.getGiftTxt()
        this.nameCh = this.list.hotel!=null ? this.list.hotel.nameCh:''
        this.address = this.list.hotel!=null ? this.list.hotel.addressCh:''
    },
    components: {
        detailroomSearch,
        buttonBuy,
        Topheader,
        backthird,
        copyright,
    },
    mounted(){
        window.addEventListener('scroll', this.handleScroll)
    },
    methods: {
        showchildMsg:function(data){
            this.personday = data
            this.$store.commit('getBookingDay',data)
        },
        handleScroll:function(){
            var scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop;
            var offsetTop = document.querySelector('#searchBar').offsetTop;
             if (scrollTop > offsetTop) {
                    this.searchBarFixed = true
                } else {
                    this.searchBarFixed = false
               }
        },
        goshopping:function(){
            if(this.buybtnflag){
                // 如果是开放住宿
                if(this.sum == 'NX'){
                    if(this.personday-this.giftnum>0){
                        Toast({
                            message: '当前权益单次最多可预订'+this.giftnum+'间夜'
                        });
                        return;
                    }
                } else {
                    // 如果预约天数不为空 并且 天数 不等于权益剩余次数
                    if (this.giftnum > -1) {
                        if(this.personday && this.personday != this.giftnum){
                            Toast({
                                message: '当前权益只能预订'+this.giftnum+'间夜，请更改入住离店日期'
                            });
                            return;
                        }
                    }
                }
                var reservOrderVo = {
                    shopChannelId:this.productdetail.shopProtocol.channelId,
                    giftType:this.productdetail.productGroupProduct.gift,
                    shopItemName:this.list.shopResDto.shopItemResDtoList[this.productcurronindex].shopItem.name,
                    accoNedds:this.list.shopResDto.shopItemResDtoList[this.productcurronindex].shopItem.needs,
                    accoAddon:this.list.shopResDto.shopItemResDtoList[this.productcurronindex].shopItem.addon,
                    hotelName:this.nameCh,
                    discountAmount:this.list.shopResDto.shopItemResDtoList[this.productcurronindex].discountRate,
                    totalAmount:this.list.shopResDto.shopItemResDtoList[this.productcurronindex].shopPriceRule,
                    shopId:this.list.shopResDto.shop.id,
                    shopItemId:this.productdetail.shopItem.id,
                    goodsId:this.productdetail.goodsSetting.goodsId,
                    productId:this.productdetail.productGroupProduct.productId,
                    countryId:this.productdetail.shop.countryId,
                    shopName:this.productdetail.shop.name
                };
                if(this.sysServicetype=='drink'){
                    reservOrderVo.exchangeNum=1;
                    reservOrderVo.giftCodeId=this.$store.state.unitId;
                    reservOrderVo.giftDate=this.$store.state.startDate.replace(/,/g,'-');
                    reservOrderVo.serviceType=this.sysServicetype;
                    reservOrderVo.productGroupProductId=this.$store.state.productGroupProductId;
                    reservOrderVo.giftPeopleNum=1;
                    reservOrderVo.superposition = this.superposition;
                    reservOrderVo.productGroupId = this.$store.state.groupId;
                    reservOrderVo.singleThread=this.singleThread;
                    reservOrderVo.useFreeCount = 0;
                    
                    this.axios.post("/mars/reservOrderAttach/placeOrder", reservOrderVo).then(res => {
                        if (res.data.code == 100) {
                            this.$router.push({
                            name: "ressuc",
                            params: {
                                reservOrderId: res.data.result.id,
                                serviceType: res.data.result.serviceType,
                                productGroupId: res.data.result.prod3uctGroupId
                            }
                            });
                        } else {
                            Toast({
                            message: res.data.msg
                            });
                        }
                    }).catch(error => {
                        this.btnDisable = false;
                        console.log(error);
                    });
                }else{
                
                // if(this.$store.state.backUrl && this.$store.state.backUrl != null){
                //     window.location.href='/shopping?bottomflag=disablebottom&backurl=' + this.$store.state.backUrl
                // }else{
                //     this.$router.push({name:'shopping',params:{productGroupProductId:this.productId}})
                // }
                this.$store.commit('getReservOrderVo',reservOrderVo)
                this.$store.commit('getCancelPolicy',this.cancelPolicy)
                this.$store.commit('getOpenTime',this.productdetail.shopItem.openTime)
                this.$store.commit('getCloseTime',this.productdetail.shopItem.closeTime)
                this.$router.push({name:'shopping',params:{productGroupProductId:this.productId}})
                }
            }
            
            
        },
        handleChange:function(index) {
            
        },
        selsectproduct: function (index,$event) {
            this.productcurronindex = index
            this.axios.post('/mars/product/detail',qs.stringify({ productGroupProductId: this.list.shopResDto.shopItemResDtoList[index].productGroupProduct.id, giftCodeId: this.$store.state.unitId}))
                .then((res)=>{
                    this.productId = this.list.shopResDto.shopItemResDtoList[index].productGroupProduct.id
                    //判断单个产品的状态
                    if((!res.data.result.blockRules || !res.data.result.blockRules.bookDates) || res.data.result.blockRules.bookDates.length == 0){//停售
                        this.buybtnflag = false
                    }else{
                        this.buybtnflag = true
                    }
                    console.log(this.buybtnflag)
                    this.$store.commit('getProductGroupProductId',this.productId)
                    this.productPriceshow(this.productId,this.$store.state.startDate.replace(/,/g,'-'),this.$store.state.endDate.replace(/,/g,'-'))
                    // localStorage.setItem('productGroupProductId','5799');//假数据，
                    // 产品明细
                    this.productdetail = res.data.result.productDetail
                    // this.cancelPolicy = this.productdetail.shopItem.cancelPolicy
                    if(this.sysServicetype.indexOf('_cpn') != -1){
                        this.cancelPolicy = 1
                    }else{
                        this.cancelPolicy = this.productdetail.shopItem.cancelPolicy
                    }
                    this.children = this.productdetail.shopProtocol.children
                    this.parking = this.productdetail.shopProtocol.parking
                    this.notice = this.productdetail.shopProtocol.notice
                    this.openTime = this.productdetail.shopItem.openTime
                    this.closeTime = this.productdetail.shopItem.closeTime
                    this.shopPicsList = this.productdetail.shopPics
                    this.superposition = this.productdetail.goodsSetting.superposition
                    this.enableMaxNight = this.productdetail.goodsSetting.enableMaxNight
                    this.enableMinNight = this.productdetail.goodsSetting.enableMinNight
                    // this.maxNight = productdetail.goodsSetting.maxNight
                    this.singleThread = this.productdetail.goodsSetting.singleThread
                    this.allYear = this.productdetail.goodsSetting.allYear
                    this.disableCall = this.productdetail.goodsSetting.disableCall
                    this.menuPicsList = this.productdetail.shopItemPics
                    if(this.productdetail.productGroupProduct.status != "0"){
                        this.buybtnflag = false
                    }

                    if(this.menuPicsList.length>0){
                        this.productRoomMsgPic = this.menuPicsList[0].pgCdnHttpUrl+'/'+this.menuPicsList[0].guid+'.'+this.menuPicsList[0].ext
                    }
                    // Block 规则
                    this.blockRules = res.data.result.blockRules
                    //不可预订
                    // this.blockRule = this.blockRules.blockRule
                    this.blockRule = this.blockRules != null ? this.blockRules.blockRule :''
                    //localStorage.setItem('blockRule',this.blockRule);
                    //可预约日期
                    // this.bookDatesList = this.blockRules.bookDates
                    this.bookDatesList = this.blockRules != null ? this.blockRules.bookDates : ''
                    // console.log(this.bookDatesList)
                    this.$store.commit('getBookDatesList',this.bookDatesList.toString())
                    // 最大预约间数
                    this.maxNights = this.productdetail.goodsSetting.maxNight==null?9:this.productdetail.goodsSetting.maxNight
                    this.minNights = this.productdetail.goodsSetting.minNight==null?9:this.productdetail.goodsSetting.minNight
                    this.$store.commit('getMaxNight',this.maxNights)

                    this.shopItem = this.productdetail.shopItem
                })
                .catch((error)=>{
                    console.log(error)
                })
        },
        productPriceshow:function(productGroupProductId,startDate,endDate){
            this.axios.post('/mars/boscbank/getMinPrice',{productGroupProductId:productGroupProductId,startDate:startDate,endDate:endDate})
            .then((res)=>{
                if(res.data.result){
                    this.productPrice = res.data.result.bookPrice
                    this.buyBtnTitle = '立即支付'
                }else{
                    this.productPrice = ' '
                    if(this.sysServicetype.indexOf('_cpn') != -1 && this.sysServicetype == 'object_cpn'){
                        this.buyBtnTitle = '立即预订'
                    }else{
                         this.buyBtnTitle = '立即预约'
                    }
                }
            })
            .catch((error)=>{
                console.log(error)
            })
        },
        getGiftTxt: function() {
            var giftTxt = -1;  // 表示无限制
            this.sum = this.list.shopResDto.shopItemResDtoList[this.productcurronindex].productGroupProduct.gift;
            switch (this.sum) {
                case "N1":
                    giftTxt = 1;
                    break;
                case "N2":
                    giftTxt = 2;
                    break;
                case "N3":
                    giftTxt = 3;
                    break;
                case "N4":
                    giftTxt = 4;
                    break;
                case "NX":
                    // 如果是开放住宿，可预约天数不得超过 最大预定数 以及 权益剩余次数
                    var surplusTimes = this.$store.state.surplusTimes
                    var maxNights = this.$store.state.maxNight
                    // 两者取最小
                    giftTxt = surplusTimes-maxNights>0?parseInt(maxNights):parseInt(surplusTimes)
                    break;
            }
            this.giftnum = giftTxt;
            this.$store.commit('getGiftNum',this.giftnum)
            //console.log('giftTxt',giftTxt,'this.giftnum',this.giftnum,'this.personday',this.personday)
        }

    },
    destroyed () {
       window.removeEventListener('scroll', this.handleScroll)
    }
}
</script>


