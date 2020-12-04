<template>
    <div class="accom_homeBox">
      <div class="bannerBox_room">
          <img src="../../common/theme/default/images/type/accommodation.png" alt="" srcset="">
          <div class="searchTypeN"  v-if="searchType">
                <span v-for="(item, index) in accomSearchType" :key="item.name" @click="searchPrice(index)" :class="curronindex == index ? 'curron':''">{{item.name}}</span>
          </div>
          <div class="searchBox" :class="searchType?'noTopborder':''">
              <div class="searchcity">
                  <div class="vistcity">
                      <router-link to="/citylist">{{ thecity }} </router-link>  </div>
                  <!-- <div class="mycity">我的位置</div> -->
              </div>
              <div class="searchcity searchDate">
                  <div class="datebox" @click="openByDialog()">
                     <div> 
                         <p class="searchtype">入住</p>
                         <span class="personSearchMsg">{{personDate}}</span>
                     </div>
                     <div><span class="dateNum">{{personday}} 晚</span></div>
                     <div>
                         <p class="searchtype">离店</p>
                         <span class="personSearchMsg">{{personDate2}}</span>
                     </div>
                  </div>
              </div>
              <div class="searchcity searchhotelName">
                  <div>
                      <input type="text" name="" ref="inputname" v-model="inputname" placeholder="搜索酒店名称" class="inputhotelName">
                  </div>
              </div>
              <button class="searchBtn" @click="gotolist">搜索酒店</button>

      

          </div>
      </div>

    <div class="calendarBox" v-if="calendarflag"  @click="closeByDialog">
        <div class="calendarmsgshow">
            <div class="closeDailogBox">选择入住离店日期<span @click="closeByDialog"></span></div>
            <transition name="fade" style="bottom:0">
                <div class="calendar-dialog" v-if="calendar4.show">
                <div class="calendar-dialog-mask" @click="closeByDialog"></div>
                
                <div class="calendar-dialog-body">
                    <calendar ref="calendar4" :range="calendar4.range" :zero="calendar4.zero" :lunar="calendar4.lunar" :value="calendar4.value" :disabled="calendar4.disabled" @select="calendar4.select"  @prev="calendar4.prev" @next="calendar4.next"></calendar>
                </div>
                
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
    name:'accom',
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
            inputname:'',
            startDate: [],
            endDate: [],
            groupId:'',
            number: 1,
            personday: this.$store.state.personday==null?1:this.$store.state.personDay,
            vistcity: this.thecity,
            //personNum:'1成人',
            personDate: 0,
            personDate2: 0,
            minBookDays: this.$store.state.minBookDays,
            maxBookDays: this.$store.state.maxBookDays,
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
            selectNumflag:false,
            calendarflag:false,
            accomSearchType:[
                {index:0, name:'超值类',Minprice:'200',Maxprice:'399'},
                {index:1,name:'优质类',Minprice:'400',Maxprice:'699'},
                {index:2,name:'尊享类',Minprice:'700',Maxprice:'12800'}],
            channel:'',
            searchType:false,
            curronindex:0,
        }
    },
    beforeCreate(){

    },
    created() {
         // 日历默认起始日期
         this.minday = new Date();
            var addMinays = 5;
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
            if(getChannge('channel') && getChannge('unitId')){
                this.$store.commit('getStartDate',getNewdate())
                this.$store.commit('getEndDate',getNewdate(1))
            }else{
                this.$store.commit('getStartDate',this.startDate.toString())
                this.$store.commit('getEndDate',this.endDate.toString())
            }
            this.$store.commit('getBookingDay',this.personday)
        

        if(this.$store.state.channel == 'BOSC'){
            this.searchType = true
        }else{
            this.searchType = false
        }
    },
    computed(){
        
    },
    props:['routersysService','getProductList'],
    mounted(){

    },
    methods: {
        gotolist:function(){
            if(this.$store.state.backUrl && this.$store.state.backUrl != null){
                // window.location.href='/list/'+ this.routersysService + '?bottomflag=disablebottom&backurl=' + this.$store.state.backUrl
                // window.location.href='/list/'+ this.routersysService + '?unitId='+ getChannge("unitId") + '&productId=' + this.$route.params.groupId + '&bottomflag=disablebottom&backurl=' + this.$store.state.backUrl
                this.$router.push({name:'list',params:{routersysService:this.routersysService,begin:this.personDate,end:this.personDate2,groupId:this.groupId}})
            }else{
                this.$router.push({name:'list',params:{routersysService:this.routersysService,begin:this.personDate,end:this.personDate2,groupId:this.groupId}})
            }
            this.$store.commit('getAccomKeyWord',this.$refs.inputname.value)
        },
        openByDialog(){
            stopsrc()
            this.calendarflag = true
            this.calendar4.show=true
            //获取完整的日期
            this.startDate = this.calendar4.value[0]
            var date = new Date(this.startDate[0]+'/'+this.startDate[1]+'/'+this.startDate[2]);
            this.current_year = date.getFullYear();
            this.current_month = date.getMonth()+1;
            this.getInitBlock(this.current_year, this.current_month);
        },
        closeByDialog(){
            movesrc()
            this.calendar4.show=false
            this.calendarflag=false
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
            //console.log('getday');
            let day = parseInt(new Date().getDate() + 1);
            let a = this.getCurrentMonthLast().getDate();
            //console.log('day',day,'a',a);
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
                // 如果日期小于 当前日期 就block'
                // console.log(this.minday.getTime()==date.getTime())
                if(this.minday.getTime() > date.getTime() || this.maxday.getTime() <= date.getTime() ){
                    var blockson = []
                    blockson.push(year)
                    blockson.push(month)
                    blockson.push(j)
                    // 默认禁用所有日期
                    this.blockDates.push(blockson)
                }
            }
            this.calendar4.disabled = this.blockDates;
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

        searchPrice(id){
            this.curronindex = id
            console.log(id)
            console.log(this.accomSearchType[id].Minprice  + '~' +  this.accomSearchType[id].Maxprice)
            
        }


    },

}
</script>


<style scoped>
    .searchTypeN{display: flex; position: relative; margin: 0 .28rem 0 .28rem; line-height: .8rem;}
    .searchTypeN span{flex: 1; color: #fff; font-size: .3rem; text-align: center;background: rgba(0, 0, 0, .5); }
    .searchTypeN span.curron{background: #fff; color: #333; font-weight: bold;}
    .searchTypeN span:first-child{border-top-left-radius: .1rem;}
    .searchTypeN span:last-child{border-top-right-radius: .1rem;}
    .noTopborder{border-top-left-radius: 0 !important; border-top-right-radius: 0 !important;}
</style>