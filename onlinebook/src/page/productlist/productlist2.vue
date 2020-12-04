<template>
    <div class="productlistBox">
        <Topheader :headtitle="headtitle" v-if="headerBarflage"></Topheader>
        <div  class="basesearchContent">
             <div class="basesearch">
                <div class="searchMsg">
                    <div class="search_hotel"><span class="searchicon"></span><input type="text" name="" v-model="buffetname" ref="buffetname" @blur="getProductList"  @keypress="getProductList"   placeholder="请输入关键字" id="" class="searchinput"></div>
                </div>
            </div>
        </div>

        <div class="productlist">
            <div class="productlistBox">
                <div class="productlistMsg" v-for="(item,index) in articles_array" :key="index"  @click="productMsgShow(index,item.orderFlag)" :class="item.orderFlag ?'' : 'overStyle'">
                    <div class="recommendProImg">
                        <span class="baseproImgbg">
                            <span :style="{backgroundImage:'url(' + (item.shopPic ? item.shopPic : baseImg) + ')',backgroundSize:'cover', backgroundPosition:'center',backgroundRepeat:'no-repeat'}"></span>
                        </span>
                    </div>
                    <div class="recommendProName">
                        <div class="hotel_type" v-for="items in item.shopResDto.shopItemResDtoList" :key="items.shopItem.name" style="background:#fff;">
                        <i v-if='items.shopItem.name' style="font-weight: bold; font-size:.29rem">{{items.shopItem.name}} </i>
                        <i v-if='items.shopItem.needs'>| {{items.shopItem.needs}} </i>
                        <i v-if='items.shopItem.addon'>| {{items.shopItem.addon}} </i></div>
                        <div class="hotel_tips" style="padding-top:.1rem;"><span>{{item.shopResDto.shopItemResDtoList[0].shopItem.serviceName}}</span></div>
                        <div v-if='item.shopResDto.shopItemResDtoList[0].productPrice' style="padding-top:.4rem; font-size:.32rem; color:#f60;font-weight: bold;"><span style="font-size:.28rem; margin-right:.05rem;">¥</span>{{item.shopResDto.shopItemResDtoList[0].productPrice}}</div>
                        
                        
                    </div>
                    
                </div>
                <nodata v-if="nodataFlag"></nodata>
            </div>
        </div>
        <br>
        <copyright :copyright="copyright"></copyright>
        <backthird v-if='backthirdFlag'></backthird>
    </div>
</template>
<script>

// import listbuffetsearch  from '@/components/searchlist/listbuffetsearch';
// import listbasesearch  from '@/components/searchlist/listbasesearch';

import Vue from 'vue'
import nodata  from '@/components/nodata/nodata';
import qs from 'qs'
import calendar from '@/components/calendar/calendar.vue'
import { hotelTip } from 'util';
import Topheader  from '@/components/head/head';
import backthird from '@/components/bottombar/backthird';
import copyright  from '@/components/copyright/copyright';
import{WXbodyBottomshow,stopsrc,movesrc,getChannge,cleanStorage,getNewdate} from '@/common/js/common.js'
import { Toast, Indicator,InfiniteScroll } from 'mint-ui';
Vue.use(InfiniteScroll)
export default {
    name:'recommend',
    data() {
        return {
            thecity: this.$store.state.mycity,
            minday: null,
            maxday:null,
            current_year: 0,
            current_month: 0,
            disabledtype:false,
            selectType:[],
            blockDates : [],
            inputname:'',
            buffetname:'',
            productlist:[],
            // newprolist:[],
            // overprlist:[],
            hotelname:'',
            hoteleat:'',
            type:'',
            adress:'',
            shopname:'',
            needs:'',
            addon:'',
            searchTypeindex:1,
            articles_array:[],
            sysServicetype: this.$store.state.sysService?this.$store.state.sysService:this.$route.params.sysService,
            unitId: this.$store.state.unitId?this.$store.state.unitId:getChannge("unitId"),
            baseImg:require('../../common/theme/default/images/pic.jpg'),
            personDate: this.$store.state.startDate?this.formatStrDate(this.$store.state.startDate):this.formatStrDate(getNewdate()),
            personDate2: this.$store.state.endDate?this.formatStrDate(this.$store.state.endDate):this.formatStrDate(getNewdate(1)),
            calendarflag:false,
            basedateflag:false,
            nodataFlag:false,
            giftType: null,
            pageInfo:{
                page:1,
                size:5
            },
            isLoading: false, // 加载中转
            isMoreLoading: true, // 加载更多中
            noMore: false, // 是否还有更多
            headtitle:'产品列表',
            headerBarflage:true,
            bottomTabflag:true,
            domainname: location.host.split(".")[1]+"."+location.host.split(".")[2], //一级域名
            copyright:'',
            backthirdFlag:false,//返回第三方按钮

        } 
    },
    components:{
        // listbuffetsearch,
        // listbasesearch,
        calendar,
        nodata,
        Topheader,
        backthird,
        copyright,
    },
    
    created() {
        Indicator.open({
            text: '加载中...',
            spinnerType: 'fading-circle'
        });
        WXbodyBottomshow()
        if(WXbodyBottomshow()){
            this.headerBarflage =false
        }else{
            this.headerBarflage = true
        }
        if(getChannge('channel')){
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
        this.inputname = this.$store.state.accomKeyWord
        this.buffetname = this.$store.state.buffetKeyWord
        if(getChannge('type')){
            this.$store.commit('getOrderForm',getChannge('type').toUpperCase())
        }
        // 处理专区绕过首页进入搜索页
        if(getChannge("unitId") && getChannge("productId") !=null){
            //清除缓存
            localStorage.setItem('CHANNEL',getChannge('channel').toUpperCase())
            this.$store.commit('getSysService',this.$route.params.sysService)
            if(this.$route.params.sysService.indexOf("_cpn") != -1){
                this.$store.commit('getStartDate',getNewdate())
                this.$store.commit('getEndDate',getNewdate(1))
            }else{
                this.$store.commit('getStartDate',getNewdate(2))
                this.$store.commit('getEndDate',getNewdate(3))
            }
            this.selectTypeCode()
            this.getGiftDetail(getChannge("unitId"),getChannge("productId"))
           
        }else{
            this.selectTypeCode()
        }
        
        if(!this.$store.state.startDate || !this.$store.state.endDate){
            var date = new Date();d
            var tmpM = "00"+(date.getMonth()+1);
            var tmpD = "00"+date.getDate();
            var dStr = date.getFullYear()+","+tmpM.slice(tmpM.length-2,tmpM.length)+","+tmpD.slice(tmpD.length-2,tmpD.length);
            this.$store.commit('getStartDate',dStr)
            this.$store.commit('getEndDate',dStr)
        }
        this.getProductList()

    },
    mounted () {
        // this.getProductList()
    },
    methods: {
        
        selectTypeCode(){
            this.axios.post('/mars/goods/getGroupInfo',qs.stringify({ groupId: this.$store.state.groupId}))
            .then((res)=>{
                this.selectType = res.data.result;
                //console.log(this.selectType)
                if(this.selectType.length > 1){
                    this.disabledtype = true;
                }
            })
            .catch((error)=>{
                console.log(error)
            })
        },
        selectTypeItemcode(type,index){
            this.sysServicetype = type;
            this.getProductList()
            this.$store.commit('getSysService',this.sysServicetype)
        },
        productMsgShow(index,orderFlag){
            if(orderFlag){
                this.$store.commit('getGiftShopProdList',this.articles_array[index])
                this.$store.commit("getProductGroupProductId", this.articles_array[index].shopResDto.shopItemResDtoList[0].productGroupProduct.id)
                this.$store.commit("getDiscountRate", this.articles_array[index].shopResDto.shopItemResDtoList[0].discountRate)
                // if(this.$store.state.backUrl && this.$store.state.backUrl != null){
                //     // window.location.href='/couproductshow/'+ this.giftType + '?backurl=' + this.$store.state.backUrl
                //     this.$router.push({name:'couproductshow',params:{type:this.giftType}})
                // }else{
                //     this.$router.push({name:'couproductshow',params:{type:this.giftType}})
                // }
                this.$router.push({name:'couproductshow',params:{type:this.giftType}})
            }
        },
        diffDays: function(endDay, staDay) {
            var end = new Date(endDay[0].replace(/\b(0+)/gi,"")+"/"+endDay[1].replace(/\b(0+)/gi,"")+"/"+endDay[2].replace(/\b(0+)/gi,""))
            var sta = new Date(staDay[0].replace(/\b(0+)/gi,"")+"/"+staDay[1].replace(/\b(0+)/gi,"")+"/"+staDay[2].replace(/\b(0+)/gi,""))
            return parseInt((end.getTime() - sta.getTime()) / 1000 / 60 / 60 /24) //把相差的毫秒数转换为天数   
        },
        formatStrDate(date) {
            let mymonth = date.split(',')[1];
            let myweekday = date.split(',')[2];
            return (mymonth.replace(/\b(0+)/gi,"") + '月' + myweekday.replace(/\b(0+)/gi,"") + '日');
        },
        //获取当前时间，格式YYYY-MM-DD
        getFormatDate: function(date) {
            var year = date.getFullYear();
            var month = date.getMonth() + 1;
            var strDate = date.getDate();
            if (month >= 1 && month <= 9) {
                month = "0" + month;
            }
            if (strDate >= 0 && strDate <= 9) {
                strDate = "0" + strDate;
            }
            var currentdate = year + "/" + month + "/" + strDate;
            return new Date(currentdate);
        },
       // 获取产品列表
        getProductList:function() {
            var startDate = this.$store.state.startDate
            if(startDate!=null){
                startDate = startDate.replace(",","-").replace(",","-");
            }
            var endDate = this.$store.state.endDate
            if(endDate!=null){
                endDate = endDate.replace(",","-").replace(",","-");
            }
            var datas = {};
            if(this.$store.state.sysService == 'accom'){
                datas = {
                    "groupId": this.$store.state.groupId,
                    "service": this.$route.params.sysService,
                    "giftcodeId": this.unitId?this.unitId:getChannge("unitId"),
                    "beginDate": startDate,
                    "endDate":  endDate
                }
            } else if(this.$store.state.sysService == 'medical' || this.$store.state.sysService == 'lounge'  || this.$store.state.sysService == 'car') {
                datas = {
                    "groupId": this.$store.state.groupId,
                    "service": this.$route.params.sysService,
                    "giftcodeId": this.unitId?this.unitId:getChannge("unitId"),
                    "beginDate": endDate
                }
            }else{
                datas = {
                    "groupId": this.$store.state.groupId,
                    "service": this.$route.params.sysService,
                    "giftcodeId": this.unitId?this.unitId:getChannge("unitId"),
                    "beginDate": startDate
                }
            }
            this.axios({
                method: 'post',
                url: '/mars/product/goodsListNew',
                data: datas
            })
            .then(res => {
                // 关闭遮罩
                Indicator.close();
                if(res.data.code == 200){
                    Toast({
                      message: res.data.msg
                    });
                } else {
                    this.productlist = res.data.result;
                    // 按照orderFlag排序
                    this.productlist.sort(
                        function(a,b){
                            return b.orderFlag-a.orderFlag
                        }
                    );
                    //console.log(this.productlist.length)
                    if(this.sysServicetype.indexOf('_cpn') === -1){
                        this.giftType = this.productlist[0].shopResDto.shopItemResDtoList[0].productGroupProduct.gift
                    }else{
                        this.giftType = "CPN"
                    }
                    this.$store.commit('getGiftShopList',this.productlist)
                    this.items()
                }
            })
            .catch(error => {
                console.log(error)
            })
        },
        items: function () {
            var articles_array1 = this.productlist;
            var text = '';
            if(this.sysServicetype == 'accom'){
                text = this.inputname;
            } else {
                text = this.buffetname;
            }
            // 文本框筛选
            if (!text){
                //console.log(articles_array1)
                return this.articles_array = articles_array1
            } else {
                var searchString = text;
                searchString = searchString.trim().toLowerCase();
                articles_array1 = articles_array1.filter(function(item){
                    console.log(item)
                    if(item.shopResDto.shopItemResDtoList[0].shopItem.name.toLowerCase().indexOf(searchString) != -1){
                        return  item;
                    }
                })
            }
            // // 返回过来后的数组
            // console.log(this.articles_array.length)
            return this.articles_array = articles_array1;

        },

        //获取权益基本信息并缓存
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

    },
    computed: {
        
    },
    destroyed(){
    }
}
</script>