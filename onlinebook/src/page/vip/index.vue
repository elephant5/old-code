<template>
    <div class="bussinessBox">
         <Topheader :headtitle="headtitle" v-if="headerBarflage"></Topheader>
         <div class="bannerBox">
             <!-- <div class="searchBox">
                 <div class="searchcityName">上海 <span></span></div>
                 <div class="searchInput"><input type="text" placeholder="请输入关键字"></div>
             </div> -->
            <mt-swipe :auto="4000" class="bannerPic" v-for="item in bannerImg" :key="item.imgUrl">
                <mt-swipe-item><img :src="item.imgUrl" alt="" width="100%"></mt-swipe-item>
            </mt-swipe>
         </div>
         <div class="mainBox">
             <div class="typeBox">
                <div class="type_Icon" v-for="(item,index) in giftList" :key="item.groupId"
                :class="[{
                    'buffet': item.sysService == 'buffet',
                    'tea': item.sysService == 'tea',
                    'setmenu': item.sysService == 'setmenu',
                    'drink': item.sysService == 'drink',
                    'accom': item.sysService == 'accom',
                    'spa': item.sysService == 'spa',
                    'gym': item.sysService == 'gym',
                    'gift': item.sysService == null?'':item.sysService.indexOf('_cpn') !=-1,
                    'curron':index == productIndex
                    },
                    ]"
                    @click="vipProShow(index,item.sysService,item.groupId,unitId)"
                ><span>&nbsp;</span><em>{{item.shortName}}</em></div>
            </div>

            <div class="hotProduct" v-if="hotcmallProduct.length >= 3">
                <div class="titleDiv">
                    <div class="title_Name">超值优惠</div>
                    <!-- <div @click="cmallHotProductList">更多优惠</div> -->
                    <div @click="tolist">更多优惠</div>
                </div>
                <div class="hotList" >
                    <div class="leftproBox" @click="cmallHotProductshow(hotcmallProduct[0].projectId)">
                        <div class="productPrice">低至{{hotcmallProduct[0].discountPrice}}元起</div>
                        <div  class="productTitle">{{hotcmallProduct[0].prjGroupList[0].title}}<span> </span></div>
                        <img :src="hotcmallProduct[0].projectUrl?hotcmallProduct[0].projectUrl:require('../../common/images/pic1.jpg')" alt="" style="height:100%">
                    </div>
                    <div class="rightproBox">
                        <div class="" style="position: relative; height:2rem;"  @click="cmallHotProductshow(hotcmallProduct[1].projectId)">
                            <div class="productPrice">低至{{hotcmallProduct[1].discountPrice}}元起</div>
                            <div class="productTitle">{{hotcmallProduct[1].prjGroupList[0].title}}<span> </span></div>
                            <img :src="hotcmallProduct[1].projectUrl?hotcmallProduct[1].projectUrl:require('../../common/images/pic1.jpg')" alt="" style="height:100%">
                        </div>
                        <div style="position: relative; margin-top:.15rem; height:2rem;"  @click="cmallHotProductshow(hotcmallProduct[2].projectId)">
                            <div class="productPrice">低至{{hotcmallProduct[2].discountPrice}}元起</div>
                            <div class="productTitle">{{hotcmallProduct[2].prjGroupList[0].title}}<span> </span></div>
                            <img :src="hotcmallProduct[2].projectUrl?hotcmallProduct[2].projectUrl:require('../../common/images/pic1.jpg')" alt="" style="height:100%">
                        </div>
                    </div>
                </div>
            </div>

            <div class="Product" style="padding-top:0;">
                <div class="titleDiv"  v-if="hotcmallProduct.length < 3">
                    <div class="title_Name">超值优惠</div>
                    <div @click="tolist">更多优惠</div>
                </div>
                <div class="productList" style="padding-top:0;">
                    <div class="listShowbox" style="margin-top:0"  v-for="(item,index) in giftShopList" :key="item.shopPic" @click="productMsgShow(index,item.orderFlag,item.shopResDto.shopItemResDtoList[0].discountRate)">
                        <div  class="productPicBox"><img :src="item.shopPic" alt=""></div>
                        <div class="productName">
                            <div>{{item.shopResDto.shopItemResDtoList[0].shopItem.name + item.shopResDto.shopItemResDtoList[0].shopItem.serviceName }}</div>
                            <span>{{item.hotel.nameCh +' | '+ item.shopResDto.shop.name}}</span>
                        </div>
                        <div class="productMoney" v-if="item.shopResDto.shopItemResDtoList[0].productPrice">￥{{item.shopResDto.shopItemResDtoList[0].productPrice}} <em style="font-size:.2rem;">起</em>  <!--span>￥399</span --></div>
                    </div>
                </div>
            </div>


            <div class="hotelList">
                <div class="titleDiv">
                    <div class="title_Name">优质商户</div>
                </div>
                <!-- <div class="cityList"><span>北京</span><span>上海</span><span>广州</span><span>深圳</span></div> -->
                <div class="hotelMsgBox" v-for="(item,index) in giftShopList" :key="item.shopPic" @click="goshop(index)">

                    <div class="hotelMsgshow">
                        <span>{{item.shopResDto.shopItemResDtoList[0].shopItem.serviceName +'-'+ item.shopResDto.shopItemResDtoList[0].shopItem.name }}</span>
                        <div class="shopPicBox"><img :src="item.shopPic" alt=""></div>
                        <div class="hotelName">{{item.hotel.nameCh +' | '+ item.shopResDto.shop.name}}</div>
                        <div class="hotelAddress"><em class="icon_cityVist">&nbsp;</em>{{item.hotel.addressCh}}</div>
                    </div>

                </div>
            </div>
            <div style="height:1rem;"></div>


         </div>

         <bottombar v-if="bottomTabflag"></bottombar>

        <transition name="fade">
            <router-view></router-view>
        </transition>
    </div>
</template>

<script>
import Vue from 'vue'
import { Toast, Swipe, SwipeItem  } from "mint-ui";
import Topheader  from '@/components/head/head';
import{WXbodyBottomshow,getChannge,getNewdate} from '@/common/js/common.js'
import bottombar  from '@/components/bottombar/bottombar';
import calendar from '@/components/calendar/calendar.vue'
Vue.component(Swipe.name, Swipe);
Vue.component(SwipeItem.name, SwipeItem);
export default {
    name:'vip',
    data() {
        return {
            headtitle:'超级会员',
            hotcmallProduct:[],
            headerBarflage:true,
            unitId:'',
            productGroupProductId:'',
            giftList:[],
            bannerImg:[],
            productIndex:0,
            shopIndex:0,
            giftProductList:[],
            giftShopList:[],
            productlist:[],
            sysService:'',
            bottomTabflag:true,
            minday: null,
            maxday:null,
            giftType:'',
            current_year: 0,
            current_month: 0,
            startDate: [],
            endDate: [],
            personDate: 0,
            personDate2: 0,
            calendar4:{
                //disabled:[[2019,6,21]],  // 设置不可选的天数
                display:"2019/06/05 ~ 2020/06/06",
                show:false,
                range:true,
                zero:true,
                //value:[this.startDate, this.endDate], //默认日期
                lunar:true, //显示农历
                select:(begin,end)=>{
                    // 如果选择同一天，结束日期自动加1
                    if(end.toString()==begin.toString()){
                        var newEnd = new Date(begin[0].replace(/\b(0+)/gi,"")+"/"+begin[1].replace(/\b(0+)/gi,"")+"/"+begin[2].replace(/\b(0+)/gi,""))
                        var addMinays = 1;
                        newEnd = newEnd.setDate(newEnd.getDate() + addMinays);
                        newEnd = new Date(newEnd)
                        end = [""+newEnd.getFullYear()+"", ""+(newEnd.getMonth()+1)+"" ,""+newEnd.getDate()+""]
                        // console.log(end)
                    }
                    this.calendar4.show=false;
                    this.calendar4.value=[begin,end];
                    this.calendar4.display=begin.join("/")+" ~ "+end.join("/");
                    this.personDate = this.formatDate(begin);
                    this.personDate2 = this.formatDate(end);
                    this.personday = this.diffDays(end, begin);
                    if(end){
                        this.calendarflag = false
                        movesrc()
                    }
                    this.startDate = begin;
                    this.endDate = end;
                    this.$store.commit('getStartDate',begin.toString())
                    this.$store.commit('getEndDate',end.toString())
                    this.$store.commit('getBookingDay',this.personday)
                    this.getProductList()
                },
                // 上月
                prev:()=>{
                    //console.log("====我的prev====",this.current_year, this.current_month) 
                    if (this.current_month == 1) {
                        this.current_month = 12
                        this.current_year = parseInt(this.current_year) - 1
                    } else {
                        this.current_month = parseInt(this.current_month) - 1
                    }
                    // 获取block日期
                    this.getInitBlock(this.current_year, this.current_month);
                    // 重新渲染日历
                    this.$refs.calendar4.render(this.$refs.calendar4.year, this.$refs.calendar4.month);
                },
                // 下月
                next:()=>{
                    //console.log("====我的next====",this.current_year, this.current_month) 
                    if (this.current_month == 12) {
                        this.current_month = 1
                        this.current_year = parseInt(this.current_year) + 1
                    } else {
                        this.current_month = parseInt(this.current_month) + 1
                    }
                    // 获取block日期
                    this.getInitBlock(this.current_year, this.current_month);
                    // 重新渲染日历
                    this.$refs.calendar4.render(this.$refs.calendar4.year, this.$refs.calendar4.month);
                }
            },
        }
    },
    created(){
        WXbodyBottomshow()
        if(WXbodyBottomshow()){
            this.headerBarflage =false
        }else{
            this.headerBarflage = true
        }
        if(getChannge('channel')){
            this.$store.commit('getChannel',getChannge('channel').toUpperCase())
        }
        this.$store.commit('getStartDate',getNewdate())
        this.$store.commit('getEndDate',getNewdate(1))

        // 日历默认起始日期
        this.minday = new Date();
        var addMinays = 1;
        if(this.$store.state.minBookDays != null && this.$store.state.minBookDays != 'null'){
            addMinays = parseInt(this.$store.state.minBookDays)
        }
        this.minday = this.minday.setDate(this.minday.getDate() + addMinays);
        // 格式化日期
        this.minday = this.getFormatDate(new Date(this.minday))
        // 获取可选结束日期
        this.maxday = new Date();
        var addMaxDays = 65;
        if(this.$store.state.maxBookDays != null && this.$store.state.maxBookDays != 'null'){
            addMaxDays = parseInt(this.$store.state.maxBookDays)
        }
        this.maxday = this.maxday.setDate(this.maxday.getDate() + addMaxDays);
        this.maxday = new Date(this.maxday)
        // 缓存选择日期
        if(this.$store.state.startDate == null || this.$store.state.endDate == null){
            // 默认选中的日期
            var start = [this.minday.getFullYear(), (this.minday.getMonth()+1)<10?"0"+(this.minday.getMonth()+1):(this.minday.getMonth()+1), this.minday.getDate()<10?"0"+this.minday.getDate():this.minday.getDate()];
            var enddate = new Date(this.minday.getFullYear()+"/"+(this.minday.getMonth()+1)+"/"+this.minday.getDate());
            enddate = enddate.setDate(enddate.getDate() + 1);
            enddate = new Date(enddate);
            var endd = [enddate.getFullYear(), (enddate.getMonth()+1)<10?"0"+(enddate.getMonth()+1):(enddate.getMonth()+1), enddate.getDate()<10?"0"+enddate.getDate():enddate.getDate()]
            this.calendar4.value = [start, endd]

            this.personDate = (this.minday.getMonth()+1)+ '月' +(this.minday.getDate())+ '日';
            this.personDate2 = (enddate.getMonth()+1)+ '月' +(enddate.getDate())+ '日';
        } else {
            this.personDate = this.formatStrDate(this.$store.state.startDate)
            this.personDate2 = this.formatStrDate(this.$store.state.endDate)
            this.calendar4.value = [this.$store.state.startDate.split(','),this.$store.state.endDate.split(',')]
        }
        //获取完整的日期
        this.startDate = this.calendar4.value[0]
        this.endDate = this.calendar4.value[1]

        this.$store.commit('getStartDate',this.startDate.toString())
        this.$store.commit('getEndDate',this.endDate.toString())
        this.$store.commit('getBookingDay',this.personday)
        this.$store.commit("getActCode", getChannge("actCode"))
        this.getLoginInfo()
    },
    components: {
        Topheader,
        calendar,
        bottombar,
    },
    methods: {
        goshop(index){
            this.shopIndex = index
            this.productGroupProductId = this.giftShopList[index].shopResDto.shopItemResDtoList[0].productGroupProduct.id
            this.$router.push({name:'shop',params:{productGroupProductId:this.productGroupProductId, giftCodeId: this.unitId}})
        },
        tolist(){
            
             if(this.giftList[this.productIndex].sysService.indexOf('_cpn') != -1){
                this.$router.push({name:'coulist',params:{sysService:this.giftList[this.productIndex].sysService}})
            }else{
                // window.location.href='/list/'+this.giftList[this.productIndex].sysService + '?unitId='+ this.unitId + '&productId=' + this.giftList[this.productIndex].groupId + '&viptypeshow=show'
                // this.$router.push({name:'list',params:{sysService:this.giftList[this.productIndex].sysService}})
                this.$router.push({name: "viplist", params: { sysService: this.giftList[this.productIndex].sysService, unitId: this.unitId, groupId:this.giftList[this.productIndex].groupId}});
            }

        },
        productMsgShow(index,orderFlag,discountrate){
            if(orderFlag){
                this.$store.commit('getGiftShopProdList',this.giftShopList[index])
                this.$store.commit('getDiscountRate',discountrate)
                if(this.giftList[this.productIndex].sysService.indexOf('_cpn') != -1){
                    this.giftType = "CPN"
                }else{
                        this.giftType = this.giftShopList[index].shopResDto.shopItemResDtoList[0].productGroupProduct.gift
                }
                this.$router.push({name:'productshow',params:{type:this.giftType}})   
            }else{
                Toast({
                        message: '因疫情暂未对外开放，请到更多优惠内查询'
                    });
            }
            
        },
        //获取用户信息
        getLoginInfo:function(){
            this.axios({
            method: 'post',
            url: '/yangjian/mem/heartBeat',
            headers: {'X-REQUESTED-SO-TOKEN': $cookies.get(localStorage.getItem('CHANNEL')+'_loginToken')}
        })
        .then(res => {
            if(res.data.code == "100"){
                if(getChannge('actCode')){
                    this.vipProductList(res.data.result.acid)
                }else{
                    Toast({
                            message: '亲，你还未加入超级会员呢~~~'
                        });
                }
            }else{
                window.location.href = process.env.PASSPORT_ROOT+"/login?channel="+ localStorage.getItem('CHANNEL') + "&backurl="+ location.href  
            }
        })
        .catch(error => {
          console.log(error)
        })
      },
        //会员产品组数据区
      vipProductList:function(memberid){
            this.axios({
            method: 'post',
            url: '/mars/goods/index',
            data:{"memberId": memberid, "prjCode":'', "actCode": getChannge('actCode')}
        })
        .then(res => {
            this.bannerImg = res.data.result
            for(var i=0; i < res.data.result.length; i++){
                this.giftList = res.data.result[i].giftList
                this.unitId = res.data.result[i].unitId
                this.$store.commit('getGoodsId',res.data.result[i].goodsId)
            }
            this.$store.commit('getVipGiftList',this.giftList)
            this.vipProShow(0,this.giftList[this.productIndex].sysService,this.giftList[this.productIndex].groupId,this.unitId)
            this.cmallHotProduct()
        })
        .catch(error => {
          console.log(error)
        })
      },

    //热门活动
      cmallHotProduct:function(){
            this.axios({
            method: 'post',
            headers:{'Xe-Auth':'95RD5ehvv9FLOZ9w38w3JpktZ5HiB'},
            url: '/cmall/shop/v1/front/project/getCCBHomePage',
            data:{acChannel:'JD',sticket:''},
        })
        .then(res => {
            if(res.data.ok == 0){
                this.hotcmallProduct = res.data.obj.list
                console.log(this.hotcmallProduct)
            }else{

            }
            

        })
        .catch(error => {
          console.log(error)
        })
      },
    cmallHotProductshow:function(id){
        window.location.href = 'http://cmall.icolourful.cn/b/'+ this.$store.state.channel.toLowerCase() +'/detail2/'+ id
    },
    cmallHotProductList:function(){
        window.location.href = 'http://cmall.icolourful.cn/b/'+ this.$store.state.channel.toLowerCase() +'/list'
    },
      //会员资源
      vipProShow:function(index,sysService,groupId){
          this.productIndex = index
          this.$store.commit('getSysService',sysService)
          this.$store.commit('getViptypeCurronIndex',index)
          

          var startDate = this.$store.state.startDate;
            if(startDate!=null){
                startDate = startDate.replace(",","-").replace(",","-");
            }
            var endDate = this.$store.state.endDate;
            if(endDate!=null){
                endDate = endDate.replace(",","-").replace(",","-");
            }
            var datas = {};
            if(sysService =='accom'){
                datas = {
                    "groupId": groupId,
                    "service": sysService,
                    "giftcodeId": this.unitId,
                    "beginDate": startDate,
                    "endDate":  endDate
                }
            } else {
                datas = {
                    "groupId": groupId,
                    "service": sysService,
                    "giftcodeId": this.unitId,
                    "beginDate": startDate
                }
            }
            this.axios({
            method: 'post',
            url: '/mars/product/goodsListNew',
            data:datas
            })
            .then(res => {
                this.productlist = res.data.result;
                    // 按照orderFlag排序
                    this.productlist.sort(
                        function(a,b){
                            return b.orderFlag-a.orderFlag
                        }
                    );
                this.giftShopList = this.productlist.slice(0,4)
                this.$store.commit('getVipGiftProductList',this.giftShopList)                
                this.productGroupProductId = res.data.result[index].shopResDto.shopItemResDtoList[this.shopIndex].productGroupProduct.id
                
            })
            .catch(error => {
                console.log(error)
            })

            this.getGiftDetail(this.unitId,groupId)

      },

        hotBussiness:function(){
            // 缓存基本信息
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
                        this.$store.commit('getSalesChannelId',res.data.result.salesChannelId)
                    }
                }
            })
            .catch((error)=>{
                console.log(error)
            })
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
                        // if(groupInfo.surplusTimes > 0 || groupInfo.surplusTimes == null) {
                        //     //缓存
                            
                        // }
                        this.$store.commit('getSurplusTimes',groupInfo.surplusTimes)
                        this.$store.commit('getSurplusFreeTimes',groupInfo.surplusFreeTimes)
                        this.$store.commit('getMinBookDays',groupInfo.minBookDays)
                        this.$store.commit('getMaxBookDays',groupInfo.maxBookDays)
                        this.$store.commit('getSalesChannelId',res.data.result.salesChannelId)
                    }
                }
            })
            .catch((error)=>{
                console.log(error)
            })
        },

      formatDate(date) {
            let mymonth = date[1];
            let myweekday = date[2];
            return (mymonth.replace(/\b(0+)/gi,"") + '月' + myweekday.replace(/\b(0+)/gi,"") + '日');
        },
        getCurrentMonthLast(){
            var date=new Date();
            var currentMonth=date.getMonth();
            var nextMonth=++currentMonth;
            var nextMonthFirstDay=new Date(date.getFullYear(),nextMonth,1);
            var oneDay=1000*60*60*24;
            var lastTime = new Date(nextMonthFirstDay-oneDay);
            var month = parseInt(lastTime.getMonth()+1);
            var day = lastTime.getDate();
            if (month < 10) {
                month = '0' + month
            }
            if (day < 10) {
                day = '0' + day
            }
            return new Date(date.getFullYear() + '-' + month + '-' + day );
        },
        getday(){
            let day = parseInt(new Date().getDate() + 1);
            let a = this.getCurrentMonthLast().getDate();
            if(day > a) {
                return new Date().getMonth()+2 + '月' + '1号';
            } else {
                return new Date().getMonth()+1 + '月' + parseInt(new Date().getDate() + 1) + '号';
            }
        },
        getInitBlock: function(year, month) {
            // block日期
            var day = new Date(year, month, 0);
            //获取当月所有的日期
            var days = day.getDate();
            // console.log("days", days)
            // 循环添加 
            for(var j=1;days>=j;j++){
                var date = new Date(year,month-1,j);
                // 如果日期小于 当前日期 就block
                if(this.minday.getTime() > date.getTime() || this.maxday.getTime() <= date.getTime() ){
                    var blockson = []
                    blockson.push(year)
                    blockson.push(month)
                    blockson.push(j)
                    // 默认禁用所有日期
                    this.blockDates.push(blockson)
                }
            }
            this.calendar3.disabled = this.blockDates;
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
    },
    beforeDestroy(){
        
    }
}
</script>
<style scoped>
    .bannerBox{width: 100%; position: relative;  height: 5.4rem;}
    .bannerBox .searchBox{background:linear-gradient(0, rgba(0,0,0,.0) 20%, rgba(0,0,0,.6) 80%); position: absolute; left:0; top: 0; width: 100%; z-index: 99; box-sizing: border-box; padding: .2rem .3rem; display: flex; justify-content: flex-start; align-content: center; align-items: center;}
    .bannerBox .searchcityName{color: #fff; margin: 0 .2rem; vertical-align: middle;}
    .bannerBox .searchcityName span{display: inline-block; width: .32rem; height: .32rem; background: url(../../common/images/bussiness/icon-down@2x.png) center bottom no-repeat; background-size: 80%;}
    .bannerBox .searchBox .searchInput{background: #fff; height: .52rem; line-height: .52rem; border-radius: .26rem; flex: 1; margin: 0 .2rem;}
    .bannerBox .searchBox .searchInput input{height: .52rem; line-height: .52rem; border-radius: .26rem; width: 100%; padding:0 .3rem 0 .6rem; background: url(../../common/images/bussiness/icon-search@2x.png) .2rem center no-repeat; background-size: .28rem; box-sizing: border-box;}
    .bannerPic{background: url(../../common/images/pic1.jpg) left center no-repeat; background-size: 100%;}
    .bannerPic img{width: 100%;}
    .mainBox{padding: .4rem .3rem;background: #fff; border-top-left-radius: .2rem; border-top-right-radius: .2rem; overflow: hidden; position: relative; margin-top: -.6rem; }
    .typeBox{display: flex; justify-content:space-between; align-content: center; align-items: center; padding-bottom: .2rem;overflow-x: scroll;}
    .type_Icon{margin: 0 .2rem; text-align: center; font-size: .24rem; white-space: nowrap; position: relative;}
    .type_Icon span{display:block; width: 1rem; height: 1rem; margin: 0 auto .15rem auto; }
    .type_Icon.buffet  span{ background: url(../../common/images/bussiness/icon_buffet.png) center no-repeat; background-size:.92rem auto; }
    .type_Icon.tea  span{ background: url(../../common/images/bussiness/icon_tea.png) center no-repeat; background-size:.92rem auto; }
    .type_Icon.setmenu  span{ background: url(../../common/images/bussiness/icon_setmenu.png) center no-repeat; background-size:.92rem auto; }
    .type_Icon.drink  span{ background: url(../../common/images/bussiness/icon_drink.png) center no-repeat; background-size:.92rem auto; }
    .type_Icon.accom  span{ background: url(../../common/images/bussiness/icon_accom.png) center no-repeat; background-size:.92rem auto; }
    .type_Icon.curron em{color: #f60; display: block;}
    .type_Icon.curron em:before{content: ''; position: absolute; display: inline-block; left:50%; bottom: -.15rem; width: .4rem; margin-left: -.2rem; height: .05rem; background: #f60;}
    .hotProduct{padding-top: .4rem;}
    .titleDiv{display:flex; justify-content: space-between; padding: .1rem 0;}
    .title_Name{font-weight: bold; font-size: .3rem;}
    .hotList{padding:.3rem 0; display: flex; justify-content: space-between;}
    .leftproBox{width: 48%; position: relative; height: 4.16rem;}
    .leftproBox img{width: 100%;}
    .rightproBox{flex: 1; margin-left: .2rem; position: relative;}
    .rightproBox img{width: 100%;}
    .productPrice {position: absolute; left:0; top: 0; background: rgba(255, 107, 0, 1); z-index: 3; color: #fff; padding: .05rem .1rem; border-bottom-right-radius: .1rem; font-size: .24rem;}
    .productTitle{position: absolute; left: 0; bottom: 0; width: 100%; color: #fff; z-index: 3; padding:.2rem .15rem; font-size: .32rem; box-sizing:border-box; background:linear-gradient(0, rgba(0,0,0,.8) 20%, rgba(0,0,0,.2) 80%)}
    .productTitle span{display: block; font-size: .22rem;  color: #f3f3f3}  
    .Product, .hotelList{padding-top: .2rem;}
    .productList{display: flex; justify-content: space-between; flex-wrap: wrap; padding-top: .2rem;}
    .listShowbox{width:48%; margin: .2rem .1rem; position: relative; overflow: hidden;}
    .listShowbox:nth-child(odd){margin-left: 0;}
    .listShowbox:nth-child(even){margin-right: 0;}
    .listShowbox img{width: 100%;}
    .productName{height: 1.4rem;}
    .productName span{display: block;  font-size: .22rme; color: #999; padding-top: .1rem;}
    .productPicBox{position: relative; width: 100%; height: 2.2rem; border-radius: .1rem; overflow: hidden; background: url(../../common/images/pic1.jpg) left top no-repeat; background-size:cover; margin-bottom: .1rem;}
    .productPicBox img{width: 100%; position: absolute; left: 50%; top: 50%; transform: translate(-50%,-50%)}
    .productMoney{color: #f60; font-size: .3rem;}
    .productMoney span{display: inline-block; margin-left: .2rem; text-decoration: line-through; color: #ccc; font-size: .26rem;}
    .cityList{display: flex; justify-content: flex-start; padding-top: .2rem;}
    .cityList span{border:solid .02rem #f3f3f3; border-radius: .1rem; padding: .1rem .2rem; margin: 0 .15rem;}
    .cityList span:first-child{margin-left:0;}
    .hotelMsgBox{padding-top: .05rem;}
    .hotelMsgshow{border: solid .01rem #f3f3f3; overflow: hidden; border-radius: .1rem; margin:  .3rem 0; position: relative; overflow: hidden;}
    .hotelMsgshow .shopPicBox{position: relative; width: 100%; height: 3rem; overflow: hidden; background: url(../../common/images/pic1.jpg) left top no-repeat; background-size:cover;}
    .hotelMsgshow .shopPicBox img{width: 100%; position: absolute; left: 50%; top: 50%; transform: translate(-50%,-50%)}
    .hotelMsgshow span{position: absolute; left: 0; top: 0; background: rgba(0,0,0,.7); padding: .08rem .2rem; color: #fff; font-size: .24rem; border-bottom-right-radius: .1rem; z-index: 5;}
    .hotelName{padding: .1rem .2rem; font-size: .3rem;}
    .hotelAddress{padding:0 .2rem .2rem .2rem; font-size: .26rem; color: #999;}
    .hotelAddress .icon_cityVist{display:inline-block; width: .3rem; height: .3rem; background: url(../../common/images/bussiness/Iconvin@2x.png) center no-repeat; background-size: 100%;}
</style>