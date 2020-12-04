<template>
    <div class="body">
        <Topheader :headtitle="headtitle" v-if="headerBarflage"></Topheader>
        <!-- <coupon v-if="routersysService.indexOf('_cpn') != -1" :routerParams="routerParams" :items="items" :searchinputstring="searchinputstring"  :routersysService="routersysService" :getProductList="getProductList"></coupon> -->
        <!-- <drink v-if="routersysService == 'drink' || routersysService == 'car' || routersysService == 'lounge'" :routersysService="routersysService"></drink> -->
        <accom v-if="routersysService == 'accom'" :routerParams="routerParams" :items="items" :searchinputstring="searchinputstring"  :routersysService="routersysService" :getProductList="getProductList" ></accom>
        <basesearch v-if="routersysService == 'buffet'||routersysService =='spa'||routersysService =='gym'||routersysService =='tea' ||routersysService =='setmenu'" :routersysService="routersysService" :getProductList="getProductList"></basesearch>
        <hotrecommend :getProductList="getProductList" :hotproductlist="hotproductlist"></hotrecommend>
        <copyright :copyright="copyright"></copyright>
        <backthird v-if='backthirdFlag'></backthird>
    </div>
</template>

<script>
import {mapState} from 'vuex'
import accom  from '@/components/searchhome/accom';
import basesearch  from '@/components/searchhome/base';
// import coupon  from '@/components/searchhome/coupon';
// import drink  from '@/components/searchhome/drink';
import Topheader  from '@/components/head/head';
import{WXbodyBottomshow,stopsrc,movesrc,getChannge,getNewdate} from '@/common/js/common.js'
import hotrecommend  from '@/components/hotrecommend/hotrecommend';
import backthird from '@/components/bottombar/backthird';
import copyright  from '@/components/copyright/copyright';
import { Toast } from 'mint-ui';
import qs from 'qs'
export default {
    name:'product',
    data() {
        return {
            searchinputstring:'',
            productlist:[],
            routerParams:'',
            routersysService:'',
            bottomTabflag:true,
            hotproductlist:[],
            headtitle:'酒店搜索',
            headerBarflage:true,
            searchType:true,
            copyright:'',
            backthirdFlag:false,//返回第三方按钮

        }
    },
    components:{
        accom,
        basesearch,
        // coupon,
        // drink,
        hotrecommend,
        backthird,
        Topheader,
        copyright,
    },
    
    created() {
        WXbodyBottomshow()
        if(WXbodyBottomshow()){
            this.headerBarflage =false
        }else{
            this.headerBarflage = true
        }
        this.routerParams = this.$route.params.groupId;
        this.routersysService = this.$route.params.sysService
        if(getChannge('channel') && getChannge('unitId')){
            localStorage.setItem('CHANNEL',getChannge('channel').toUpperCase())
            this.$store.commit('getStartDate',getNewdate())
            this.$store.commit('getEndDate',getNewdate(1))
            this.$store.commit('getChannel',getChannge('channel').toUpperCase())
            this.$store.commit('getSysService',this.$route.params.sysService)
            this.getGiftDetail(getChannge('unitId'),this.$route.params.groupId)
        }
        if(getChannge('bottomflag')){
            localStorage.setItem('disablebottom','show')
        }else{
             localStorage.removeItem('disablebottom')
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
    },
    computed:{
      ... mapState([
          "channel",
          "groupId",
          "sysService",
          "unitId",
          "startDate",
          "endDate",
      ]),

      // 计算数学，匹配搜索
        items: function () {
            var articles_array = this.productlist,
                searchString = this.searchinputstring;
            if(!searchString){
                return articles_array;
            }
            searchString = searchString.trim().toLowerCase();
            articles_array = articles_array.filter(function(item){
                if(item.res.shopItem.name.toLowerCase().indexOf(searchString) !== -1){
                    return item;
                }
            })
 
            // 返回过来后的数组
            return articles_array;
        }
    },
    mounted() {

        
    },
    
    methods: {
        //获取权益基本信息并缓存( 饶过首页处理 )
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
                    debugger
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

        getProductList:function() {
            var startDate = this.startDate;
            if(startDate!=null){
                startDate = startDate.replace(",","-").replace(",","-");
            }
            var endDate = this.endDate;
            if(endDate!=null){
                endDate = endDate.replace(",","-").replace(",","-");
            }
            var datas = {};
            if(this.routersysService=='accom'){
                datas = {
                    "groupId": this.groupId?this.groupId:this.$route.params.groupId,
                    "service": this.sysService?this.sysService:this.$route.params.sysService,
                    "giftcodeId": this.unitId,
                    "beginDate": startDate,
                    "endDate":  endDate,
                    "recommend": 1, //推荐
                }
            } else {
                datas = {
                   "groupId": this.groupId?this.groupId:this.$route.params.groupId,
                    "service": this.sysService?this.sysService:this.$route.params.sysService,
                    "giftcodeId": this.unitId,
                    "beginDate": startDate,
                    "recommend": 1, //推荐
                }
            }
            this.axios({
                method: 'post',
                url: '/mars/product/goodsList',
                data: datas
            })
            .then(res => {
                if(res.data.code == 200){
                    Toast({message: res.data.msg});
                } else {
                    this.$store.commit('getHotproductList',res.data.result)
                    this.hotproductlist = res.data.result;
                    // 按照orderFlag排序
                    if(this.hotproductlist.length > 0){
                        this.hotproductlist.sort(
                            function(a,b){
                                return b.orderFlag-a.orderFlag
                            }
                        );
                    }
                    if(!!this.hotproductlist &&this.hotproductlist.length>0&&this.hotproductlist[0].shopResDto){
                        this.giftType = this.hotproductlist[0].shopResDto.shopItemResDtoList[0].productGroupProduct.gift
                    }
                    
                }
            })
            .catch(error => {
                console.log(error)
            })
        },
    },
}
</script>


