<template>
    <div class="productlistshowBox">
        <Topheader :headtitle="headtitle" v-if="headerBarflage"></Topheader>
        <div class="proMsgshowBox" style="margin-bottom:0">
            <div class="bannerBox">
            <mt-swipe @change="handleChange">
                <mt-swipe-item class="slide1" v-for="(item,index) in shopPicsList" :key="index">
                    <img :src="item.pgCdnHttpsFullUrl" alt="" width="100%">
                </mt-swipe-item>
            </mt-swipe>
            </div>
        </div>

        <div class="searchBar" id="searchBar" >
            <ul :class="searchBarFixed ? 'isFixed' :''"  v-if="list.shopResDto.shopItemResDtoList.length >='2'">
                <li v-for="(item,index) in list.shopResDto.shopItemResDtoList" v-on:click="selsectproduct(index)" :key="item.shopItem.name" v-bind:class="{'active':index == productcurronindex}">{{item.shopItem.name}}</li>
            </ul>
        </div>
            
      <div class="proMsgHome">
        <div class="proMsgWord">
            <div class="product_roomBox" style="padding:0;">
                <div class="product_roomMsg" style="white-space: nowrap;">
                    <!-- <div class="product_roomMsgPic"><span :style="{backgroundImage:'url(' + (productRoomMsgPic ? productRoomMsgPic : baseImg) + ')',backgroundSize:'cover', backgroundPosition:'center',backgroundRepeat:'no-repeat'}"></span></div> -->
                    <div class="product_roomMsgName">
                        <p v-if='list.shopResDto.shopItemResDtoList[productcurronindex].shopItem.name'> {{list.shopResDto.shopItemResDtoList[productcurronindex].shopItem.name}}</p>
                        <div  class="pricerMoney" v-if="surplusFreeTimes && surplusFreeTimes != 0" style="padding-top:.1rem; color:#f60;">本次预约免费</div>
                        <div v-else>
                            <p class="pricerMoney" v-if="productPrice != ' '">
                                <em v-if="list.productGroup.discountRate" style="font-size:.42rem; color: rgb(255, 102, 0); font-weight: bold;"><i style="font-size:.3rem; margin-right:.05rem;">¥</i>{{discountRatePrice}} <i style="font-size:.26rem; color:#999; text-decoration: line-through;">¥ {{productPrice}}</i></em>
                                <em v-else style="font-size:.42rem; color: rgb(255, 102, 0); font-weight: bold;"><i style="font-size:.3rem; margin-right:.05rem;">¥</i>{{productPrice}}</em>  
                            </p> 
                        </div>
                        
                    </div>

                    <!-- <div class="product_roomMsgName">
                        <p v-if='list.shopResDto.shopItemResDtoList[productcurronindex].shopItem.name'> {{list.shopResDto.shopItemResDtoList[productcurronindex].shopItem.name}}</p>
                        <p class="pricerMoney" v-if="productPrice != ' '"><em style="font-size:.42rem; color: rgb(255, 102, 0); font-weight: bold;"><i style="font-size:.3rem; margin-right:.05rem;">¥</i>{{productPrice}}</em> </p> 
                    </div> -->




                </div>
                <div class="product_roomMsgPrice">
                    <p class="priceTips" v-if="sysServicetype.indexOf('_cpn') === -1 && list.shopResDto.shopItemResDtoList[productcurronindex].productGroupProduct.gift"><span>{{list.shopResDto.shopItemResDtoList[productcurronindex].productGroupProduct.gift |getGiftTxt}}</span></p>
                    <p class="priceRad" v-if="list.productGroup.discountRate"><span>{{list.productGroup.discountRate}}折</span></p>
                    <!-- <p class="pricerMoney" v-if="list.shopResDto.shopItemResDtoList[productcurronindex].discountRate"><span>?{{list.shopResDto.shopItemResDtoList[productcurronindex].shopPriceRule}}</span><em>?{{list.shopResDto.shopItemResDtoList[productcurronindex].discountRate}}</em></p> -->
                </div>
            </div>
           
        </div>
        


        <div class="proMsgWord" v-if="shopItem != null && shopItem.menuText && shopItem.menuText != '' && shopItem.menuText != '<p></p>'">
            <div class="proMsgType">商品介绍</div>
            <div class="productbaseMsg" v-html="shopItem.menuText">
            </div>
        </div>

        <div class="proMsgWord" v-if="productdetail.goodsClauseList && productdetail.goodsClauseList != null" style="margin-bottom:.2rem;">
            <div class="proMsgType">使用细则</div>
            <div class="productbaseMsg" v-for="item in productdetail.goodsClauseList" :key="item.clause">
                <div v-if="item.clauseType == sysServicetype && item.clause" v-html="item.clause"></div>
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
import{WXbodyBottomshow,getChannge,cleanStorage,getNewdate} from '@/common/js/common.js'
Vue.component(Swipe.name, Swipe)
Vue.component(SwipeItem.name, SwipeItem)
export default {
    name:'couproductshow',
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
            sysServicetype:this.$store.state.sysService?this.$store.state.sysService:'',
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
            discountRatePrice:'',// 折扣价
            surplusFreeTimes:'', //免费次数
            roomNum:1,
            buybtnflag:true,
            shopItem:{},
            productlist:[],
            giftType: null,
            headtitle:'产品介绍',
            headerBarflage:true,
            bottomTabflag:true,
            unitId: this.$store.state.unitId?this.$store.state.unitId:getChannge("unitId"),
            productimg:'',//产品图片
            goodsIdBag:'',
            domainname: location.host.split(".")[1]+"."+location.host.split(".")[2], //一级域名
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
        //饶过首页、列表页处理
        if(getChannge("unitId") && getChannge("groupId") !=null && getChannge('sysService') && getChannge('channel')){
            //清除缓存
            this.$store.commit("getChannel", getChannge("channel").toUpperCase())
            this.$store.commit("getSysService", getChannge('sysService'))
            this.$store.commit("getUnitId", getChannge("unitId"))
            this.$store.commit("getGroupId", getChannge("groupId"))
            this.$store.commit("getProductGroupProductId", getChannge("productGroupProductId"))

            var date = new Date();
            var tmpM = "00"+(date.getMonth()+1);
            var tmpD = "00"+date.getDate();
            var dStr = date.getFullYear()+","+tmpM.slice(tmpM.length-2,tmpM.length)+","+tmpD.slice(tmpD.length-2,tmpD.length);
            this.$store.commit('getStartDate',dStr)
            this.$store.commit('getEndDate',dStr)
            this.sysServicetype = getChannge('sysService')
            this.getGiftDetail(getChannge("unitId"),getChannge("groupId"))
            this.getProductList()
            this.getLoginInfo()
        }else{
            this.list = this.$store.state.giftShopProdList
            this.selsectproduct(this.productcurronindex)
            this.getGiftTxt()
            this.nameCh = this.list.hotel!=null ? this.list.hotel.nameCh:''
            this.address = this.list.hotel!=null ? this.list.hotel.addressCh:''
        }
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

        //获取登录信息
        getLoginInfo: function() {
            this.axios({
                method: "post",
                url: "/yangjian/mem/heartBeat",
                headers: {"X-REQUESTED-SO-TOKEN": $cookies.get(localStorage.getItem('CHANNEL') + "_loginToken")}
            })
                .then(res => {
                if (res.data.code == "100") {
                    this.$store.commit("getUserInfo", res.data.result);
                } else {
                    window.location.href =
                    process.env.PASSPORT_ROOT + "/login?channel=" + localStorage.getItem('CHANNEL') +  "&backurl=" +  location.href;
                }
                })
                .catch(error => {
                console.log(error);
                });
            },

        //获取权益基本信息并缓存 (饶过首页、列表页处理) 开始
        getGiftDetail: function(unitId, groupId) {
            // 缓存基本信息
            this.$store.commit('getUnitId',unitId)
            this.$store.commit('getGroupId',groupId)
            // 调用接口查询
            this.axios({
                method: 'post',
                url: '/mars/goods/giftdetail',
                data:{
                    "giftcodeId": unitId,
                    "groupId": groupId
                }
            })
            .then((res)=>{
                if(res.data.code == 200){
                    Toast({
                      message: res.data.msg
                    });
                } else {
                    if(res.data.result!=null){
                        var groupInfo = res.data.result.giftList[0]
                        if(groupInfo.surplusTimes > 0 || groupInfo.surplusTimes == null) {
                            //缓存
                            this.surplusFreeTimes = res.data.result.giftList[0].surplusFreeTimes
                            this.$store.commit('getSurplusTimes',groupInfo.surplusTimes)
                            this.$store.commit('getSurplusFreeTimes',groupInfo.surplusFreeTimes)
                            this.$store.commit('getMinBookDays',groupInfo.minBookDays)
                            this.$store.commit('getMaxBookDays',groupInfo.maxBookDays)
                            this.$store.commit('getSysService',groupInfo.sysService)
                            this.$store.commit('getSalesChannelId',res.data.result.salesChannelId)
                        }
                    }
                }
            })
            .catch((error)=>{
                console.log(error)
            })
        },


    // 获取产品列表
        getProductList:function() {
            var startDate = this.$store.state.startDate?this.$store.state.startDate:getNewdate()
            if(startDate!=null){
                startDate = startDate.replace(",","-").replace(",","-");
            }
            var endDate = this.$store.state.endDate?this.$store.state.endDate:getNewdate(1)
            if(endDate!=null){
                endDate = endDate.replace(",","-").replace(",","-");
            }
            var datas = {
                "groupId": this.$store.state.groupId,
                "service": this.sysServicetype,
                "giftcodeId": this.unitId,
                "beginDate": startDate
            };
            this.axios({method: 'post', url: '/mars/product/goodsListNew', data: datas})
            .then(res => {
                if(res.data.code == 200){
                    Toast({
                      message: res.data.msg
                    });
                } else {
                    this.productlist = res.data.result;
                    this.giftType = "CPN"
                    this.$store.commit('getGiftShopList',this.productlist)
                    for(var i = 0;i<this.productlist.length;i++){
                        var pGPId = this.productlist[i].shopResDto.shopItemResDtoList[0].productGroupProduct.id
                        if(this.$store.state.productGroupProductId == pGPId){
                            this.list = this.productlist[i]
                        }
                    }
                    this.$store.commit('getGiftShopProdList',this.list)
                    this.$store.commit("getDiscountRate", this.list.shopResDto.shopItemResDtoList[0].discountRate)
                    this.selsectproduct(this.productcurronindex)
                    this.getGiftTxt()
                }
            })
            .catch(error => {
                console.log(error)
            })
        },


        //(饶过首页、列表页处理) 结束

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
                    reservOrderVo.exchangeNum=1
                    reservOrderVo.productGroupId=this.$store.state.groupId
                    reservOrderVo.giftCodeId=this.$store.state.unitId
                    reservOrderVo.giftDate=this.$store.state.startDate.replace(/,/g,'-');
                    reservOrderVo.serviceType=this.sysServicetype;
                    reservOrderVo.productGroupProductId=this.$store.state.productGroupProductId
                    reservOrderVo.giftPeopleNum=1
                    reservOrderVo.superposition = this.superposition
                    reservOrderVo.singleThread=this.singleThread
                    this.axios.post("/mars/reservOrderAttach/placeOrder", reservOrderVo).then(res => {
                        if (res.data.code == 100) {
                            this.$router.push({
                            name: "ressuc",
                            params: {
                                reservOrderId: res.data.result.id,
                                serviceType: res.data.result.serviceType,
                                productGroupId: res.data.result.productGroupId
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
            console.log(this.list)
            this.productcurronindex = index
            this.axios.post('/mars/product/detail',qs.stringify({ productGroupProductId: this.list.shopResDto.shopItemResDtoList[index].productGroupProduct.id, giftCodeId: this.$store.state.unitId}))
                .then((res)=>{
                    this.productId = this.list.shopResDto.shopItemResDtoList[index].productGroupProduct.id
                    this.$store.commit('getProductGroupProductId',this.productId)
                    this.productPriceshow(this.productId,this.$store.state.startDate.replace(/,/g,'-'),this.$store.state.endDate.replace(/,/g,'-'))
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
                    // this.shopPicsList = this.productdetail.shopPics
                    this.shopPicsList = this.productdetail.shopItemPics
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
                    //定制双肩包特权处理
                    this.goodsIdBag = this.productdetail.productGroup.goodsId
                    if(this.goodsIdBag == '10353'){//10353  10244
                        this.axios.post('/mars/reservOrder/checkResrrvOrder',{goodsId:this.productdetail.productGroup.goodsId,productGroupId:this.productdetail.productGroupProduct.productGroupId,productGroupProductId:this.productdetail.productGroupProduct.id,productId:this.productdetail.productGroupProduct.productId})
                            .then((res)=>{
                                if(res.data.code == '100'){
                                    if(res.data.result){
                                            this.buybtnflag = true
                                    }else{
                                            this.buybtnflag = false
                                    }
                               }
                            })
                            .catch((error)=>{
                                console.log(error)
                            })
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
                    // localStorage.setItem('bookDatesList',this.bookDatesList);
                    this.$store.commit('getBookDatesList',this.bookDatesList)
                    // 最大预约间数
                    this.maxNights = this.productdetail.goodsSetting.maxNight==null?9:this.productdetail.goodsSetting.maxNight
                    this.minNights = this.productdetail.goodsSetting.minNight==null?9:this.productdetail.goodsSetting.minNight
                    // localStorage.setItem("maxNight", this.maxNights)
                    this.$store.commit('getBookDatesList', this.maxNights)

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
                    // if(this.list.shopResDto.shopItemResDtoList[0].discountRate){
                    //     this.discountRatePrice = res.data.result.bookPrice * this.list.shopResDto.shopItemResDtoList[0].discountRate  //折扣价
                    // }else{
                    //     this.productPrice = res.data.result.bookPrice  // 原价
                    // }
                    this.productPrice = res.data.result.bookPrice
                    this.buyBtnTitle = '立即支付'
                }else{
                    this.productPrice = ' '
                    if(this.sysServicetype.indexOf('_cpn') != -1){
                        if(this.sysServicetype == 'object_cpn'){
                            this.buyBtnTitle = '立即预订'
                        }else{
                            this.buyBtnTitle = '立即兑换'
                        }
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


