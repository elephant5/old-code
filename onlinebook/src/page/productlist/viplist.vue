<template>
    <div class="productlistBox">
        <Topheader :headtitle="headtitle" v-if="headerBarflage"></Topheader>
        <div v-if="sysServicetype=='accom' || vipaccomtimeflag" class="roomsearchContent">
            <div class="roomsearch">
                <div class="searchMsg">
                    <div class="search_city"><router-link to="/citylist">{{ thecity }} </router-link> <span class="pulldown"></span></div>
                    <div class="search_date"  @click="openByDialog()">
                        <div><p><i>住</i>{{personDate}}</p><p><i>离</i>{{personDate2}}</p></div>
                        <div><span class="pulldown"></span></div>
                    </div>
                    <div class="search_hotel">
                        <span class="searchicon"></span>
                        <input type="text" name="" placeholder="搜索酒店名称" v-model="inputname" ref="inputname" @blur="getProductList" class="searchinput"></div>
                </div>

                    <div class="calendarBox" v-if="calendarflag"  @click="closeByDialog">
                        <div class="calendarmsgshow">
                            <div class="closeDailogBox">选择入住离店日期<span @click="closeByDialog"></span></div>
                                <transition name="fade" style="bottom:0">
                                    <div class="calendar-dialog" v-if="calendar4.show">
                                    <div class="calendar-dialog-mask" @click="closeByDialog"></div>
                                    
                                    <div class="calendar-dialog-body">
                                        <calendar ref="calendar4" :range="calendar4.range" :zero="calendar4.zero" :lunar="calendar4.lunar" :value="calendar4.value"  @select="calendar4.select" :disabled="calendar4.disabled" @prev="calendar4.prev" @next="calendar4.next"></calendar>
                                    </div>
                                    
                                    </div>
                                </transition>
                            </div>
                    </div>
            </div>
        </div>
        <div v-else-if="sysServicetype=='drink'"  class="basesearchContent">
             <div class="basesearch">
                <div class="searchMsg">
                    <div class="search_city"><router-link to="/citylist">{{ thecity }} </router-link><span class="pulldown"></span></div>
                    <div class="search_hotel"><span class="searchicon"></span><input type="text" name="" v-model="buffetname" ref="buffetname" @blur="getProductList"  placeholder="请输入关键字" id="" class="searchinput"></div>
                </div>
            </div>
        </div>

        <div v-else-if="sysServicetype.indexOf('_cpn') !=-1"  class="basesearchContent">
             <div class="basesearch">
                <div class="searchMsg">
                    <div class="search_hotel"><span class="searchicon"></span><input type="text" name="" v-model="buffetname" ref="buffetname" @blur="getProductList"  placeholder="请输入关键字" id="" class="searchinput"></div>
                </div>
            </div>
        </div>


        
        <div v-else  class="basesearchContent">
             <div class="basesearch">
                <div class="searchTypelist" v-if="disabledtype">
                    <div v-for="(item,index) in selectType"  @click="selectTypeItemcode(item.code,index)" :class="{'curron':(item.code == sysServicetype)}" :key="item.name">{{item.name}}</div>
                </div>
                <div class="searchMsg">
                    <div class="search_city"><router-link to="/citylist">{{ thecity }} </router-link><span class="pulldown"></span></div>
                    <div class="search_date"  @click="openByDrop($event)">
                        <div><p>{{personDate}}</p></div>
                        <div><span class="pulldown"></span></div>
                    </div>
                    <div class="search_hotel"><span class="searchicon"></span><input type="text" name="" v-model="buffetname" ref="buffetname" @blur="getProductList"  placeholder="请输入关键字" id="" class="searchinput"></div>
                </div>

                <div class="calendarBox" v-if="basedateflag"  @click="closeByDialog">
                    <div class="calendarmsgshow">
                    <div class="closeDailogBox">选择预约日期<span @click="closeByDialog"></span></div>
                            <transition name="fade">
                                <div class="calendar-dropdown" v-if="calendar3.show">
                                    <!-- :style="{'left':calendar3.left+'px','top':calendar3.top+'px'}"  -->
                                    <calendar ref="calendar3" :zero="calendar3.zero" :lunar="calendar3.lunar" :value="calendar3.value" :begin="calendar3.begin" :end="calendar3.end" :disabled="calendar3.disabled" @select="calendar3.select" @prev="calendar3.prev" @next="calendar3.next"></calendar>
                                </div>
                            </transition>
                    </div>
                </div>
            </div>
        </div>

        <div class="vipsearchTypeBox">
            <div class="descOrdBox">
                <div class="descTypeCon">
                    <span  v-for="(item,index) in vipsearhtypeList" :class="vipcurronindex == index ? 'curron':''"  @click="selectVipsearchType(index)" :key="item.groupId" >{{item.shortName}}</span>
                </div>
            </div>
        </div>

        <div class="productlist">
            <div class="productlistBox">
                <div class="productlistMsg" v-for="(item,index) in currentPageData" :key="item.shopPic"  @click="productMsgShow(index,item.orderFlag,item.shopResDto.shopItemResDtoList[0].discountRate)" :class="item.orderFlag ?'' : 'overStyle'">
                    <div class="recommendProImg">
                        <span class="baseproImgbg">
                            <span :style="{backgroundImage:'url(' + (item.shopPic ? item.shopPic : baseImg) + ')',backgroundSize:'cover', backgroundPosition:'center',backgroundRepeat:'no-repeat'}"></span>
                        </span>
                    </div>
                    <div class="recommendProName">
                        <div class="hotel_name" v-if="item.hotel != null">{{item.hotel.nameCh}}</div>
                        <div class="hotel_istance"  v-if="sysServicetype.indexOf('_cpn') === -1"><i  v-if="sysServicetype!='accom'">{{item.shopResDto.shop.name}}</i><i v-else>&nbsp;</i></div>
                        <div class="hotel_tips" v-if="sysServicetype.indexOf('_cpn') === -1 && item.hotel != null"><span>{{item.shopResDto.shopItemResDtoList[0].productGroupProduct.gift |getGiftTxt}}</span></div>
                        <div class="hotel_add" v-if="sysServicetype .indexOf('_cpn') ===-1"><span>&nbsp;</span>{{item.shopResDto.shop.address}}</div>
                        <div class="hotel_type" v-for="items in item.shopResDto.shopItemResDtoList" :key="items.shopItem.addon">
                        <span class="businesspro_Icon" :class="{
                            	'businesspro_Icon_accom': sysServicetype == 'accom',
                                'businesspro_Icon_buffet': sysServicetype == 'buffet',
                                'businesspro_Icon_spa': sysServicetype == 'spa',
                                'businesspro_Icon_gym': sysServicetype == 'gym',
                                'businesspro_Icon_tea': sysServicetype == 'tea',
                                'businesspro_Icon_drink': sysServicetype == 'drink',
                                'businesspro_Icon_setmenu': sysServicetype == 'setmenu',
                                'businesspro_Icon_gift': sysServicetype .indexOf('_cpn') !=-1,
                        }"></span>
                        <i v-if='items.shopItem.name'>{{items.shopItem.name}} </i>
                        <i v-if='items.shopItem.needs'>| {{items.shopItem.needs}} </i>
                        <i v-if='items.shopItem.addon'>| {{items.shopItem.addon}} </i>
                        <span style="float:right">
                        <i v-if="items.productPrice != null && items.discountRate != 1 && items.discountRate != 0" style="margin-left:.15rem; color:#f60; font-size:.3rem; float:"><span style="font-size:.22rem;">¥</span>{{(items.productPrice * items.discountRate).toFixed(2)}}<span style="font-size:.22rem; margin-left: .02rem;">起</span></i>
                        <i v-if="items.productPrice != null && items.discountRate == 1" style="margin-left:.15rem; color:#f60; font-size:.3rem;"><span style="font-size:.22rem;">¥</span>{{(items.productPrice * items.discountRate).toFixed(2)}}<span style="font-size:.22rem; margin-left: .02rem;">起</span></i>
                        <i v-if="items.productPrice != null && items.discountRate != 1 && items.discountRate != 0" style="margin-left:.15rem; text-decoration: line-through; color: #ccc;"><span style="font-size:.22rem;">¥</span>{{items.productPrice}}</i>
                        </span>
                        </div>
                        <div class="hotel_tips" style="padding-top:.1rem;" v-if="sysServicetype.indexOf('_cpn') !=-1"><span>{{item.shopResDto.shopItemResDtoList[0].shopItem.serviceName}}</span></div>
                    </div>
                </div>
                <div v-if="this.currentPageData.length < this.articles_array.length ">
                    <button @click="nextPage()" style="background: rgb(248, 248, 248);padding-top: 20px;padding-bottom: 20px;color: rgb(195, 195, 195);text-align: center;width: 100%;font-size: 10px;"  >
                        点击加载更多.....
                    </button>
                </div>
                <nodata v-if="nodataFlag"></nodata>
            </div>
        </div>
    <br>
    <copyright :copyright="copyright"></copyright>
    <backthird v-if='backthirdFlag' @mousedown="move()"></backthird>
    </div>
</template>
<script>

// import listbuffetsearch  from '@/components/searchlist/listbuffetsearch';
// import listbasesearch  from '@/components/searchlist/listbasesearch';
import {mapState} from 'vuex'
import Vue from 'vue'
import nodata  from '@/components/nodata/nodata';
import qs from 'qs'
import calendar from '@/components/calendar/calendar.vue'
import { hotelTip } from 'util';
import Topheader  from '@/components/head/head';
import backthird from '@/components/bottombar/backthird';
import copyright  from '@/components/copyright/copyright';
import{WXbodyBottomshow,stopsrc,movesrc,getChannge,cleanStorage} from '@/common/js/common.js'
import { Toast, Indicator,InfiniteScroll } from 'mint-ui';
Vue.use(InfiniteScroll)
export default {
    name:'viplist',
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
            // groupId: localStorage.getItem('groupId'),
            sysServicetype: this.$store.state.sysService?this.$store.state.sysService:this.$route.params.sysService,
            unitId: this.$store.state.unitId?this.$store.state.unitId:getChannge("unitId"),
            baseImg:require('../../common/theme/default/images/pic.jpg'),
            personDate: this.$store.state.startDate?this.formatStrDate(this.$store.state.startDate):'',
            personDate2: this.$store.state.endDate?this.formatStrDate(this.$store.state.endDate):'',
            calendarflag:false,
            basedateflag:false,
            nodataFlag:false,
            giftType: null,
            pageInfo:{
                page:1,
                size:5
            },
            totalPage: 1, // 统共页数，默认为1
            currentPage: 1, //当前页数 ，默认为1
            pageSize: 10, // 每页显示数量
            currentPageData: [], //当前页显示内容
            calendar4:{
                display:"2019/06/05 ~ 2020/06/06",
                show:false,
                range:true,
                zero:true,
                value:[this.$store.state.startDate?this.$store.state.startDate.split(','):'',this.$store.state.endDate?this.$store.state.endDate.split(','):''], //默认日期
                lunar:true, //显示农历
                select:(begin,end)=>{
                    // 如果选择同一天，结束日期自动加1
                    if(end==begin){
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
                    this.$store.commit('getStartDate',begin.toString())
                    this.$store.commit('getEndDate',end.toString())
                    this.$store.commit('getBookingDay',this.personday)
                    this.getProductList();
                },
                // 上月
                prev:()=>{
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
            calendar3:{
                display:new Date().getMonth()+1+'月'+ new Date().getDate()+'日',
                show:false,
                zero:true,
                value:this.$store.state.startDate?this.$store.state.startDate.split(','):'', //默认日期
                lunar:true, //显示农历
                select:(value)=>{
                    this.calendar3.show=false;
                    this.calendar3.value=value;
                    this.calendar3.display=value.join("/");
                    this.basedateflag = false
                    this.personDate = this.formatDate(value);
                    this.$store.commit('getStartDate',value.toString())
                    this.getProductList();
                    movesrc()
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
                    this.$refs.calendar3.render(this.$refs.calendar3.year, this.$refs.calendar3.month);
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
                    this.$refs.calendar3.render(this.$refs.calendar3.year, this.$refs.calendar3.month);
                }
            },
            isLoading: false, // 加载中转
            isMoreLoading: true, // 加载更多中
            noMore: false, // 是否还有更多
            headtitle:'酒店列表',
            headerBarflage:true,
            bottomTabflag:true,
            domainname: location.host.split(".")[1]+"."+location.host.split(".")[2], //一级域名
            copyright:'',
            vipsearchFlag:false,  //vip服务类型搜索
            vipsearhtypeList:this.$store.state.vipGiftList,
            vipcurronindex:this.$store.state.viptypeCurronIndex,
            vipaccomtimeflag:false,
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
        // this.getProductList()
        // this.selectTypeCode()
        if(WXbodyBottomshow()){
            this.headerBarflage =false
        }else{
            this.headerBarflage = true
        }
        if(getChannge('viptypeshow') && getChannge('viptypeshow') == 'show'){
            this.vipsearchFlag = true
        }else{
            this.vipsearchFlag = false
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


        // 处理专区绕过首页进入搜索页列表页
        this.$store.commit('getSysService',this.$route.params.sysService)
        this.selectTypeCode()
        this.getGiftDetail(this.$route.params.unitId,this.$route.params.groupId)

    
    },

    computed:{
      ... mapState([
          "channel",
          "groupId",
          "startDate",
          "endDate",
      ]),
    },
    mounted () {
        

        if(this.$store.state.startDate == null || this.$store.state.endDate == null){
            var date = new Date();
            var tmpM = "00"+(date.getMonth()+1);
            var tmpD = "00"+date.getDate();
            var dStr = date.getFullYear()+","+tmpM.slice(tmpM.length-2,tmpM.length)+","+tmpD.slice(tmpD.length-2,tmpD.length);
            this.$store.commit('getStartDate',dStr)
            this.$store.commit('getEndDate',dStr)
        }

        this.inputname = this.$store.state.accomKeyWord
        this.buffetname = this.$store.state.buffetKeyWord
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
        var addMaxDays = 31;
        if(this.$store.state.maxBookDays != null && this.$store.state.maxBookDays != 'null'){
            addMaxDays = parseInt(this.$store.state.maxBookDays)
        }
        this.maxday = this.maxday.setDate(this.maxday.getDate() + addMaxDays);
        this.maxday = new Date(this.maxday)


        this.getProductList()
    },
    methods: {
        move(e){
        let odiv = e.target;    //获取目标元素
        
        //算出鼠标相对元素的位置
        let disX = e.clientX - odiv.offsetLeft;
        let disY = e.clientY - odiv.offsetTop;
        document.onmousemove = (e)=>{    //鼠标按下并移动的事件
            //用鼠标的位置减去鼠标相对元素的位置，得到元素的位置
            let left = e.clientX - disX;  
            let top = e.clientY - disY;
            
            //绑定元素位置到positionX和positionY上面
            this.positionX = top;
            this.positionY = left;
            
            //移动当前元素
            odiv.style.left = left + 'px';
            odiv.style.top = top + 'px';
        };
        document.onmouseup = (e) => {
            document.onmousemove = null;
            document.onmouseup = null;
        };
        },
        openByDialog(){
            stopsrc()
            this.calendarflag = true
            this.calendar4.show=true
            //获取完整的日期
            //console.log(this.calendar4.value[0])
            // 获取当前选择的起始日期
            var startDate = this.calendar4.value[0]
            var date = new Date(startDate[0]+'/'+startDate[1]+'/'+startDate[2]);
            this.current_year = date.getFullYear();
            this.current_month = date.getMonth()+1;
            this.getInitBlock(this.current_year, this.current_month);
        },
        closeByDialog(){
            movesrc()
            this.calendar4.show=false
            this.calendarflag=false
        },
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
        openByDrop(e){
            this.basedateflag = true
            this.calendar3.show=true
            e.stopPropagation()
            window.setTimeout(()=>{
                document.addEventListener("click",(e)=>{
                    this.calendar3.show=false;
                    this.basedateflag = false
                    document.removeEventListener("click",()=>{},false);
                },false);
            },1000)
            //console.log(e)
            //获取完整的日期
            var startDate = this.calendar3.value
            var date = new Date(startDate[0]+'/'+startDate[1]+'/'+startDate[2]);
            this.current_year = date.getFullYear();
            this.current_month = date.getMonth()+1;
            this.getInitBlock(this.current_year, this.current_month);
        },

        formatDate(date) {
            let mymonth = date[1];
            let myweekday = date[2];
            return (mymonth.replace(/\b(0+)/gi,"") + '月' + myweekday.replace(/\b(0+)/gi,"") + '日');
        },
        formatStrDate(date) {
            let mymonth = date.split(',')[1];
            let myweekday = date.split(',')[2];
            return (mymonth.replace(/\b(0+)/gi,"") + '月' + myweekday.replace(/\b(0+)/gi,"") + '日');
        },
        selectTypeCode(){
            this.axios.post('/mars/goods/getGroupInfo',qs.stringify({ groupId:this.$store.state.groupId?this.$store.state.groupId:getChannge("productId")}))
            .then((res)=>{
                this.selectType = res.data.result;
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
            this.$store.commit('getSysService',this.sysServicetype)
            this.getProductList();
        },
        productMsgShow(index,orderFlag,discountrate){
            if(orderFlag){
                this.$store.commit('getGiftShopProdList',this.articles_array[index])
                this.$store.commit('getDiscountRate',discountrate)
                if(this.$store.state.backUrl && this.$store.state.backUrl != null){
                    window.location.href='/productshow/'+ this.giftType + '?backurl=' + this.$store.state.backUrl
                }else{
                    this.$router.push({name:'productshow',params:{type:this.giftType}})
                }
                
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
                if(this.minday.getTime() > date.getTime() || this.maxday.getTime() <= date.getTime()){
                    var blockson = []
                    blockson.push(year)
                    blockson.push(month)
                    blockson.push(j)
                    // 默认禁用所有日期
                    this.blockDates.push(blockson)
                }
            }
            this.calendar3.disabled = this.blockDates;
            this.calendar4.disabled = this.blockDates;
        },
        diffDays: function(endDay, staDay) {
            var end = new Date(endDay[0].replace(/\b(0+)/gi,"")+"/"+endDay[1].replace(/\b(0+)/gi,"")+"/"+endDay[2].replace(/\b(0+)/gi,""))
            var sta = new Date(staDay[0].replace(/\b(0+)/gi,"")+"/"+staDay[1].replace(/\b(0+)/gi,"")+"/"+staDay[2].replace(/\b(0+)/gi,""))
            return parseInt((end.getTime() - sta.getTime()) / 1000 / 60 / 60 /24) //把相差的毫秒数转换为天数   
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
        selectVipsearchType:function(index){
            this.vipcurronindex = index
            this.$store.commit('getViptypeCurronIndex',index)
            this.$store.commit('getSysService',this.$store.state.vipGiftList[index].sysService)
            this.$store.commit('getGroupId',this.$store.state.vipGiftList[index].groupId)
            if(this.$store.state.vipGiftList[index].sysService == 'accom'){
                this.vipaccomtimeflag = true
            }else{
                this.vipaccomtimeflag = false
            }
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
                        "groupId": this.$store.state.vipGiftList[index].groupId,
                        "service": this.$store.state.vipGiftList[index].sysService,
                        "giftcodeId": this.$store.state.unitId,
                        "beginDate": startDate,
                        "endDate":  endDate
                    }
                } else {
                    datas = {
                        "groupId": this.$store.state.vipGiftList[index].groupId,
                        "service": this.$store.state.vipGiftList[index].sysService,
                        "giftcodeId": this.$store.state.unitId,
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
                    if(this.sysServicetype.indexOf('_cpn') == -1){
                        this.giftType = this.productlist[0].shopResDto.shopItemResDtoList[0].productGroupProduct.gift
                    }else{
                        this.giftType = "CPN"
                    }
                    this.$store.commit('getGiftShopList',this.productlist)
                    this.items()
                    this.getGiftDetail(this.$route.params.unitId,this.$store.state.vipGiftList[index].groupId)
                }
            })
            .catch(error => {
                console.log(error)
            })
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
            if(this.$store.state.sysService=='accom'){
                datas = {
                    "groupId": this.$store.state.groupId,
                    "service": this.sysServicetype,
                    "giftcodeId": this.unitId?this.unitId:getChannge("unitId"),
                    "beginDate": startDate,
                    "endDate":  endDate
                }
            } else {
                datas = {
                    "groupId": this.$store.state.groupId,
                    "service": this.sysServicetype,
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
                    if(this.sysServicetype.indexOf('_cpn') == -1){
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
            // 城市筛选
            if(this.thecity && this.thecity != '选择城市' && this.thecity!='全国') {
                var citysearch = this.thecity;
                citysearch = citysearch.trim().toLowerCase();
                articles_array1 = articles_array1.filter(function(item){
                    if(item.hotel.addressCh.toLowerCase().indexOf(citysearch) != -1){
                        return item;
                    }
                })
            } 
            // 文本框筛选
            if (!text){
                //console.log(articles_array1)
                this.articles_array = articles_array1;
                this.getCurrentPageData();
                return this.articles_array;
            } else {
                var searchString = text;
                searchString = searchString.trim().toLowerCase();
                articles_array1 = articles_array1.filter(function(item){
                    if(item.hotel.nameCh.toLowerCase().indexOf(searchString) != -1){
                        return  item;
                    }
                })
            }
            // // 返回过来后的数组
            this.articles_array = articles_array1;
            this.getCurrentPageData();
            return this.articles_array;

        },
        // 设置当前页面数据，对数组操作的截取规则为[0~9],[10~20]...,
        // 当currentPage为1时，我们显示(0*pageSize+1)-1*pageSize，当currentPage为2时，我们显示(1*pageSize+1)-2*pageSize...
        getCurrentPageData() {
            // 计算一共有几页
            this.totalPage = Math.ceil(this.articles_array.length / this.pageSize);
            // 计算得0时设置为1
            this.totalPage = this.totalPage == 0 ? 1 : this.totalPage;
            let begin = 0;
            let end = this.currentPage * this.pageSize;
            this.currentPageData = this.articles_array.slice(
                begin,
                end
            );
        },
        // 下一页
        nextPage() {
            if (this.currentPage == this.totalPage) {
                return false;
            } else {
                this.currentPage++;
                this.getCurrentPageData();
            }
        }    
    },
    computed: {
        
    },
    destroyed(){
    }
}
</script>
<style scoped>
    .vipsearchTypeBox .descOrdBox{box-shadow: 0 0 0.05rem #f3f3f3; border-bottom: solid .02rem #f3f3f3; background: #fff;}
    .vipsearchTypeBox .descTypeCon{width: 100%; display: flex; justify-content: space-between; align-items: center; align-content: center; padding: 0 .2rem; box-sizing: border-box;}
    .vipsearchTypeBox .descTypeCon span{text-align: center; width: 25%; line-height: .52rem; border-bottom: solid .02rem #f3f3f3; color: #999; font-size: .2rem; position: relative; padding-top: .1rem; white-space: nowrap;}
    .vipsearchTypeBox .descTypeCon span.curron{color: #333; font-weight: bold;}
    .vipsearchTypeBox .descTypeCon span.curron:before{content: ''; width: .3rem; height: .05rem; background: #f60; position: absolute; left: 50%; bottom:0;  transform: translate(-50%,0)}
</style>