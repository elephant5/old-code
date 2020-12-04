<template>
    <div class="roomdetailsearchBox">
        <div class="searchBox">
            <div class="searchcity searchDate">
                <div class="datebox"  @click="openByDialog()">
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
        </div>

        <div class="calendarBox" v-if="calendarflag" @click="closeByDialog">
            <div class="calendarmsgshow">
                <div class="closeDailogBox">选择入住离店日期<span @click="closeByDialog"></span></div>
                <transition name="fade" style="bottom:0">
                    <div class="calendar-dialog" v-if="calendar4.show">
                    <div class="calendar-dialog-mask" @click="closeByDialog"></div>
                    
                    <div class="calendar-dialog-body">
                        <calendar ref="calendar4" :range="calendar4.range" :zero="calendar4.zero" :lunar="calendar4.lunar" :value="calendar4.value" :disabled="calendar4.disabled"  @select="calendar4.select"  @prev="calendar4.prev" @next="calendar4.next"></calendar>
                    </div>
                    
                    </div>
                </transition>
            </div>
        </div>

    </div>
    
</template>

<script>
import Vue from 'vue'
import { Toast } from 'mint-ui';
import qs from 'qs'
import calendar from '@/components/calendar/calendar.vue'
import { constants } from 'fs';
export default {
    name:'detailsearch',
    components: {
        calendar
    },
    data() {
        return {
           list:[],
           giftnum:'',
           current_year: 0,
           current_month: 0,
           blockDates : [],
           bookDates:[],
           number: 1,
           personday: this.$store.state.personDay,
           vistcity:'上海',
           //personDate:new Date().getMonth()+1+'月'+ new Date().getDate()+'日',
           //personDate2:this.getday(),
           personDate: this.formatStrDate(this.$store.state.startDate),
           personDate2: this.formatStrDate(this.$store.state.endDate),
           giftType: null,
           calendar4:{
                //disabled:[[2019,6,21]],
                display:"2019/06/05 ~ 2020/06/06",
                show:false,
                range:true,
                zero:true,
                value:[this.$store.state.startDate.split(','),this.$store.state.endDate.split(',')], //默认日期
                lunar:true, //显示农历
                select:(begin,end)=>{
                    // 如果是开放住宿，可自由选择日期
                    if(this.giftType!='NX'){
                        // 如果选择同一天，结束日期自动加1
                        var newEnd = new Date(begin[0].replace(/\b(0+)/gi,"")+"/"+begin[1].replace(/\b(0+)/gi,"")+"/"+begin[2].replace(/\b(0+)/gi,""))
                        //var addMinays = 1;
                        newEnd = newEnd.setDate(newEnd.getDate() + this.giftnum);
                        newEnd = new Date(newEnd)
                        var year = newEnd.getFullYear();
                        var month = (newEnd.getMonth()+1)<10?"0"+(newEnd.getMonth()+1):(newEnd.getMonth()+1)
                        var date = newEnd.getDate()<10?"0"+newEnd.getDate():newEnd.getDate()
                        end = [""+year+"", ""+month+"" ,""+date+""]
                    } else {
                        // 如果选的是同一天，截止日期+1天
                        if(end.toString()==begin.toString()) {
                            // 如果选择同一天，结束日期自动加1
                            var newEnd = new Date(begin[0].replace(/\b(0+)/gi,"")+"/"+begin[1].replace(/\b(0+)/gi,"")+"/"+begin[2].replace(/\b(0+)/gi,""))
                            newEnd = newEnd.setDate(newEnd.getDate() + 1);
                            newEnd = new Date(newEnd)
                            var year = newEnd.getFullYear();
                            var month = (newEnd.getMonth()+1)<10?"0"+(newEnd.getMonth()+1):(newEnd.getMonth()+1)
                            var date = newEnd.getDate()<10?"0"+newEnd.getDate():newEnd.getDate()
                            end = [""+year+"", ""+month+"" ,""+date+""]
                        }
                    }
                    // 获取选中的日期
                    if (this.getSelectedDays(begin, end)) {
                        // 如果是开放住宿，所选日期范围不得超过
                        this.personday = this.diffDays(end, begin);
                        // 如果是开放住宿并且 选择间夜 大于可选
                        if(this.giftType=='NX' && this.personday>this.giftnum){
                            Toast({
                                message: '当前权益单次最多可预订'+this.giftnum+'间夜'
                            });
                            // 选择日期范围自动 匹配 可预约的最大范围
                            var newEnd = new Date(begin[0].replace(/\b(0+)/gi,"")+"/"+begin[1].replace(/\b(0+)/gi,"")+"/"+begin[2].replace(/\b(0+)/gi,""))
                            newEnd = newEnd.setDate(newEnd.getDate() + this.giftnum);
                            newEnd = new Date(newEnd)
                            var year = newEnd.getFullYear();
                            var month = (newEnd.getMonth()+1)<10?"0"+(newEnd.getMonth()+1):(newEnd.getMonth()+1)
                            var date = newEnd.getDate()<10?"0"+newEnd.getDate():newEnd.getDate()
                            end = [""+year+"", ""+month+"" ,""+date+""]
                            // 在计算一次 间夜数
                            this.personday = this.giftnum
                        } else {
                            // 关闭日历
                            this.calendarflag = false
                            this.calendar4.show=false
                        }
                        // var diffDays = this.diffDays(end, begin);
                        this.calendar4.display = begin.join("/")+" ~ "+end.join("/");
                        this.personDate = this.formatDate(begin);
                        this.personDate2 = this.formatDate(end);
                        this.calendar4.value=[begin, end];
                        this.$store.commit('getStartDate',begin.toString())
                        this.$store.commit('getEndDate',end.toString())
                        this.childmsg()
                    } else {
                        Toast({
                            message: '选择日期不可预订'
                        });
                        this.calendar4.value = [this.$store.state.startDate.split(','),this.$store.state.endDate.split(',')]
                    }
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
                    this.getBlockCalendar(this.current_year, this.current_month);
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
                    this.getBlockCalendar(this.current_year, this.current_month);
                    // 重新渲染日历
                    this.$refs.calendar4.render(this.$refs.calendar4.year, this.$refs.calendar4.month);
                }
            },
            calendarflag:false,

        }
    },
    created() {
        // 可预约时间
        this.bookDates = this.$store.state.bookDatesList
        this.list = this.$store.state.giftShopProdList
        this.getGiftTxt()
        this.giftType = this.$router.currentRoute.params.type
    },
    props:['routersysService'],
    methods: {
        childmsg:function(){
            this.$emit('parndtMsg',this.personday)
        },
        gotolist:function(){
            this.$router.push({name:'list',params:{routersysService:this.routersysService,begin:this.personDate,end:this.personDate2}})
        },
        openByDialog(){
            //console.log(this.bookDates)
            this.calendarflag = true
            this.calendar4.show=true
            //获取完整的日期
            var startDate = this.calendar4.value[0]
            var date = new Date(startDate[0]+'/'+startDate[1]+'/'+startDate[2]);
            this.current_year = date.getFullYear();
            this.current_month = date.getMonth()+1;
            this.getBlockCalendar(this.current_year, this.current_month);
        },
        closeByDialog(){
            this.calendar4.show=false
            this.calendarflag=false
        },
        formatDate(date) {
            //console.log(date[2]);
            let mymonth = date[1];
            let myweekday = date[2];
            return (mymonth.replace(/\b(0+)/gi,"") + '月' + myweekday.replace(/\b(0+)/gi,"") + '日');
        },
        formatStrDate(date) {
            let mymonth = date.split(',')[1];
            let myweekday = date.split(',')[2];
            return (mymonth.replace(/\b(0+)/gi,"") + '月' + myweekday.replace(/\b(0+)/gi,"") + '日');
        },
        getBlockCalendar: function(year, month){ 
            // 可选日期转数组
            this.bookDates = this.$store.state.bookDatesList
            var arr = this.bookDates.split(',')
            var noBlock = []
            for(var i=0;arr.length>i;i++){
                //console.log(arr[i].substr(0, 10).split('-'))
                var arryList = arr[i].substr(0, 10).split('-').map(
                    function(item,index,array){
                        return item-0;
                    }
                );
                noBlock.push(arryList)
            }
            // block日期
            var day = new Date(year, month, 0);
            //获取当月所有的日期
            var days = day.getDate();
            // console.log("days", days)
            // 循环添加 
            for(var j=1;days>=j;j++){
                var datest = []
                datest.push(year)
                datest.push(month)
                datest.push(j)
                // 如果可以不含有该日期，就添加Block
                if(!this.contains(noBlock, datest)){
                    // 默认禁用所有日期
                    this.blockDates.push(datest)
                }
            }
            //console.log("我的block====:",this.blockDates);
            this.calendar4.disabled = this.blockDates;
        },
        // 比对数组
        contains: function(a, obj) {
	        var i = a.length;
	        while (i--) {
		        if (a[i].toString() == obj.toString()) {
		            return true;
		        }
	        }
	        return false;
        },
        diffDays: function(endDay, staDay) {
            var end = new Date(endDay[0].replace(/\b(0+)/gi,"")+"/"+endDay[1].replace(/\b(0+)/gi,"")+"/"+endDay[2].replace(/\b(0+)/gi,""))
            var sta = new Date(staDay[0].replace(/\b(0+)/gi,"")+"/"+staDay[1].replace(/\b(0+)/gi,"")+"/"+staDay[2].replace(/\b(0+)/gi,""))
            return parseInt((end.getTime() - sta.getTime()) / 1000 / 60 / 60 /24) //把相差的毫秒数转换为天数   
        },
        getGiftTxt: function() {
            var giftTxt;
            var sum = this.list.shopResDto.shopItemResDtoList[0].productGroupProduct.gift;
            //console.log('sum',sum)
            switch (sum) {
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
        },
        // 获取选中的日期
        getSelectedDays: function(begin, end) {
            // 获取选中了几天
            var days = this.diffDays(end, begin)+1;
            // 如果选中的日期大于0
            if (days>0) {
                var blockcnt = 0;
                for(var i=0;days>i;i++){
                    var newbegin = new Date(begin[0].replace(/\b(0+)/gi,"")+"/"+begin[1].replace(/\b(0+)/gi,"")+"/"+begin[2].replace(/\b(0+)/gi,""))
                    newbegin = newbegin.setDate(newbegin.getDate() + i);
                    newbegin = new Date(newbegin)
                    var selectedDay = [];
                    selectedDay.push(newbegin.getFullYear());
                    selectedDay.push(newbegin.getMonth()+1);
                    selectedDay.push(newbegin.getDate())
                    // 如果含有block日期
                    if(this.contains(this.blockDates, selectedDay)){
                        // 默认禁用所有日期
                        blockcnt++;
                    }
                }
                if(blockcnt<=1){
                    return true;
                }
            }
            return false;
        },
    } 
}
</script>


