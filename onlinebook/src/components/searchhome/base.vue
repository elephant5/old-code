<template>
    <div class="base_homeBox">
      <div class="bannerBox_base">
          <img :src="searchbannerImg" alt="">
          <div class="searchBox">
              <div class="searchcity">
                 <div class="vistcity">
                      <router-link to="/citylist">{{ thecity }} </router-link>  </div>
                  <!-- <div class="mycity">我的位置</div> -->
              </div>
              <div class="searchcity searchDate">
                  <div class="datebox" @click="openByDrop($event)">
                      <p class="searchtype">预约日期</p>
                      <span class="personSearchMsg">{{personDate}}</span>
                  </div>
                 
              </div>
              <div class="searchcity searchhotelName">
                  <div>
                      <input type="text" name="" ref="buffetname" v-model="buffetname"  id="" placeholder="搜索酒店名称" class="inputhotelName">
                  </div>
              </div>
              <button class="searchBtn" @click="gotolist">搜索酒店</button>
          </div>
      </div>

       <div class="calendarBox" v-if="calendarflag" @click="closeByDialog">
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
</template>

<script>
import Vue from 'vue'
import qs from 'qs'
import calendar from '@/components/calendar/calendar.vue'
import{stopsrc,movesrc,getChannge,cleanStorage,getNewdate} from '@/common/js/common.js'
export default {
    name:'basesearch',
    components: {
        calendar
    },
    data() {
        return {
            thecity: this.$store.state.mycity,
            minday: null,
            maxday:null,
            current_year: 0,
            current_month: 0,
            blockDates : [],
            vistcity: this.thecity,
            buffetname:'',
            startDate: [],
            endDate: [new Date().getFullYear(),(new Date().getMonth()+1)<10?"0"+(new Date().getMonth()+1):(new Date().getMonth()+1),(new Date().getDate()+1)<10?"0"+(new Date().getDate()+1):(new Date().getDate()+1)],
            personDate: 0,
            calendar3:{
                disabled:[[2019,6,20],[2019,6,21],[2019,6,22]],  // 设置不可选的天数
                display:new Date().getMonth()+1+'月'+ new Date().getDate()+'日',
                show:false,
                zero:true,
                lunar:true, //显示农历
                multi:false,
                select:(value)=>{
                    this.calendar3.show=false;
                    this.calendar3.value=value;
                    this.calendar3.display=value.join("/");
                    this.calendarflag = false;
                    this.personDate = this.formatDate(value);
                    this.startDate = value;
                    this.endDate = value;
                    this.$store.commit('getStartDate',this.startDate.toString())
                    this.$store.commit('getEndDate',this.endDate.toString())
                    movesrc()
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
            selectNumflag:false,
            calendarflag:false,
            searchbannerImg:null,
        }
    },
    created() {
       
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
        var addMaxDays = 11;
        if(this.$store.state.maxBookDays != null && this.$store.state.maxBookDays!='null'){
            addMaxDays = parseInt(this.$store.state.maxBookDays)
        }
        this.maxday = this.maxday.setDate(this.maxday.getDate() + addMaxDays);
        this.maxday = new Date(this.maxday);

        // 缓存选择日期
        if(this.$store.state.startDate == null){
            // 默认选中的日期
            var start = [this.minday.getFullYear(), (this.minday.getMonth()+1)<10?"0"+(this.minday.getMonth()+1):(this.minday.getMonth()+1), this.minday.getDate()<10?"0"+this.minday.getDate():this.minday.getDate()];
            this.personDate = (this.minday.getMonth()+1)+ '月' +this.minday.getDate()+ '日';
            this.calendar3.value = start
        } else {
            this.personDate = this.formatStrDate(this.$store.state.startDate)
            this.calendar3.value = this.$store.state.startDate.split(',')
        }
        this.startDate = this.calendar3.value
        if(getChannge('channel') && getChannge('unitId')){
            this.$store.commit('getStartDate',getNewdate())
            this.$store.commit('getEndDate',getNewdate(1))
        }else{
            this.$store.commit('getStartDate',this.startDate.toString())
            this.$store.commit('getEndDate',this.endDate.toString())
        }
    },
    props:['routersysService','getProductList'],
    mounted() {
        this.showSearchBanner()
    },
    methods: {
        showSearchBanner:function(){
            if(this.routersysService == 'buffet'){
                this.searchbannerImg = require('../../common/theme/default/images/type/restaurant.png')
            } else if(this.routersysService == 'spa'){
                 this.searchbannerImg = require('../../common/theme/default/images/type/spa.png')
            } else if(this.routersysService == 'gym'){
                 this.searchbannerImg = require('../../common/theme/default/images/type/bodybuilding.png')
            } else if(this.routersysService == 'tea'){
                 this.searchbannerImg = require('../../common/theme/default/images/type/tea.png')
            } else if(this.routersysService == 'setmenu'){
                 this.searchbannerImg = require('../../common/theme/default/images/type/restaurant.png')
            }
        },
        openByDrop(e){
            stopsrc()
            this.calendarflag = true
            this.calendar3.show=true;
            e.stopPropagation();
            window.setTimeout(()=>{
                document.addEventListener("click",(e)=>{
                    this.calendar3.show=false;
                    this.calendarflag = false
                    document.removeEventListener("click",()=>{},false);
                },false);
            },1000)
            // console.log(e)
            //获取完整的日期
            var startDate = this.calendar3.value
            var date = new Date(startDate[0]+'/'+startDate[1]+'/'+startDate[2]);
            this.current_year = date.getFullYear();
            this.current_month = date.getMonth()+1;
            this.getInitBlock(this.current_year, this.current_month);
        },
        closeByDialog(){
            movesrc()
            this.calendar3.show=false
            this.calendarflag=false
        },
        gotolist:function(){
            if(this.$store.state.backUrl && this.$store.state.backUrl != null){
                // window.location.href='/list/'+ this.routersysService + '?bottomflag=disablebottom&backurl=' + this.$store.state.backUrl
                // window.location.href='/list/'+ this.routersysService + '?unitId='+ getChannge("unitId") + '&productId=' + this.$route.params.groupId + '&bottomflag=disablebottom&backurl=' + this.$store.state.backUrl
                this.$router.push({name:'list',params:{routersysService:this.routersysService}})
            }else{
               this.$router.push({name:'list',params:{routersysService:this.routersysService}})
            }
            this.$store.commit('getBuffetKeyWord',this.$refs.buffetname.value)
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
}
</script>



