<template>
    <div class="shoppingFrombox_gym">
       <div class="shoppingTop_trip">
           <div class="shoppingMsgBox">
               <div class="shoppingMsg_Name">服务内容</div>
               <div class="shoppingMsgBox">
                   <div style="margin-left:0" :class="travelType == 1 ? 'curron':''" @click="tripcurronFun(1)">接机</div>
                   <div :class="travelType == 2 ? 'curron':''"  @click="tripcurronFun(2)">送机</div>
               </div>
           </div>
       </div>
        <div class="shoppingBox_gym">
            <div class="inputlist" v-if="travelType == 1">
                <div>航班号</div>
                <div><input type="text"  class="text_input giftPhone"  placeholder="请输入接机航班号" v-model="flightNumber"   style="font-size:.31rem;"></div>
            </div>
            <div class="inputlist"  @click="openByDrop($event)" v-if="travelType == 1">
                <div >接机日期</div>
                <div  class="moreDivTip" v-if="personDate" style="font-size:.31rem; color:#333;">{{personDate}}</div>
                <div  class="moreDivTip" v-else style="color:#999;">请选择接机日期</div>
            </div>
            <div class="inputlist" @click="openPickerTime" v-if="travelType == 1">
                <div >接机时间</div>
                <div  class="moreDivTip" v-if="tripTime" style="font-size:.31rem; color:#333;">{{tripTime}}</div>
                <div  class="moreDivTip" v-else style="color:#999;">请选择接机时间</div>
            </div>
            <div class="inputlist" v-if="travelType == 1">
                <div>接机机场</div>
                <div><input type="text"  class="text_input giftPhone"  placeholder="请输入机场及航站楼" v-model="airport"   style="font-size:.31rem;"></div>
            </div>
            <div class="inputlist" v-if="travelType == 1">
                <div>目的地</div>
                <div><input type="text"  class="text_input giftPhone"  placeholder="请输入目的地" v-model="endPoint"  style="font-size:.31rem;"></div>
            </div>
            
            <div class="inputlist" v-if="travelType == 2">
                <div>出发地</div>
                <div><input type="text"  class="text_input giftPhone"  placeholder="请输入出发地"  v-model="startPoint"  style="font-size:.31rem;"></div>
            </div>
            <div class="inputlist"  @click="openByDrop($event)" v-if="travelType == 2">
                <div >送机日期</div>
                <div  class="moreDivTip" v-if="personDate" style="font-size:.31rem; color:#333;">{{personDate}}</div>
                <div  class="moreDivTip" v-else style="color:#999;">请选择送机日期</div>
            </div>
            <div class="inputlist" @click="openPickerTime"  v-if="travelType == 2">
                <div >接机时间</div>
                <div  class="moreDivTip" v-if="tripTime" style="font-size:.31rem; color:#333;">{{tripTime}}</div>
                <div  class="moreDivTip" v-else style="color:#999;">请选择送机时间</div>
            </div>
            <div class="inputlist"  v-if="travelType == 2">
                <div>送机机场</div>
                <div><input type="text"  class="text_input giftPhone"  placeholder="请输入机场及航站楼" v-model="airport"   style="font-size:.31rem;"></div>
            </div>

            <div class="inputlist">
                <div>乘车人数</div>
                <div class="moreDivTip" @click="selectroomNum"><span class="roomNum">{{newroomNum}} <span style="font-size:.28rem; color:#999;">人</span></span><!--<span v-if="!!feedNum">该权益可减免{{feedNum}}人，超出需付费</span>--></div>
            </div>
            <div class="inputlist">
                <div>联系人</div>
                <div><input type="text" name="giftName"   v-model="memberInfo.mbName" class="text_input giftPhone" placeholder="填写实际使用人姓名"></div>
            </div>
            
            <div class="inputlist">
                <div>联系电话</div>
                <div><input type="text" name="giftPhone" v-model="memberInfo.mobile"  class="text_input" placeholder="填写正确的手机号"></div>
            </div>
        </div>
        <div class="personNumbox" v-if="selectroomNumflag" @click="coloseelectroomNum">
             <div class="personNumboxmsgshow">
                <div class="closeDailogBox">选择乘车人数<span @click="coloseelectroomNum"></span></div>
                <div class="page-picker-wrapper">
                    <mt-picker :slots="numberSlot" @change="onroomNumChange" :visible-item-count="5"></mt-picker>
                    <div class="selectbtn" @click="sureselectroomNum">确定</div>
                </div>
            </div>
        </div>
        

        <!-- <mt-datetime-picker ref="tripDate"  type="date" :startDate="new Date(tripEndDate(1))" :endDate="new Date(tripEndDate(30))" year-format="{value} 年"  month-format="{value} 月"  date-format="{value} 日"  v-model="tripDate" @confirm="handleChangeDate"></mt-datetime-picker> -->
        <div class="calendarBox" v-if="calendarflag">
            <div class="calendarmsgshow">
                <div class="closeDailogBox">选择日期<span @click="sureselectroomNum"></span></div>
                <transition name="fade">
                <div class="calendar-dropdown" v-if="calendar3.show">
                    <!-- :style="{'left':calendar3.left+'px','top':calendar3.top+'px'}"  -->
                    <calendar ref="calendar3" :disabled="calendar3.disabled" :zero="calendar3.zero" :lunar="calendar3.lunar" :value="calendar3.value" :begin="calendar3.begin" :end="calendar3.end" @select="calendar3.select" @prev="calendar3.prev" @next="calendar3.next"></calendar>
                </div>
                </transition>
            </div>
        </div>
        <mt-datetime-picker ref="tripTime"  type="time"  v-model="tripTime" @confirm="handleChangeTime" hour-format="{value} 时" minute-format="{value} 分"></mt-datetime-picker>
    </div>
</template>

<script>
import Vue from 'vue'
import moment from 'moment'
import qs from 'qs'
import tabs from '@/components/tab/tab'
import calendar from '@/components/calendar/calendar.vue'
import { Picker,DatetimePicker } from 'mint-ui'
Vue.component(Picker.name, Picker)
Vue.component(DatetimePicker.name, DatetimePicker)
export default {
    name:'shoppingroom',
    components: {
        calendar
    },
    data() {
        return {
             minday: null,
            maxday:null,
            current_year: 0,
            current_month: 0,
            blockDates : [],
            selectroomNumflag:false,
            selecttimeflag:false,
            roomNum:'1',
            newroomNum:'1',
            useFreeCount:'0',
            feedNum:'',
            timeshow:'',
            newtimeshow:'',
            zjzType:'身份证',
            tripTime:'',//接送机时间
            personDate:this.$parent.formatStrDate(this.$store.state.startDate.split(',')),
            travelType:1,//出行类型 1-接机 2-送机
            flightNumber:'', //航班号
            airport:'', //机场
            endPoint:'', //目的地
            startPoint:'', //出发地
            
            numberSlot: [{
                flex: 1,
                defaultIndex: 0,
                values: [1, 2, 3],
                className: 'slot1'
            }],
            calendarflag:false,
            calendar3:{
                display:new Date().getMonth()+1+'月'+ new Date().getDate()+'日',
                show:false,
                zero:true,
                value:this.valueDate, //默认日期
                lunar:true, //显示农历
                select:(value)=>{
                    this.calendar3.show=false;
                    this.calendar3.value=value;
                    this.calendar3.display=value.join("/");
                    this.calendarflag = false;
                    this.personDate = this.formatDate(value);
                    this.$store.commit('getStartDate',value.toString())
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
                    this.getBlockCalendar(this.current_year, this.current_month);
                    // 重新渲染日历
                    this.$refs.calendar3.render(this.$refs.calendar3.year, this.$refs.calendar3.month);
                }
            },
        }
    },
    created(){
            this.feedNum = '1';
            this.$parent.setNum(1,1,this.onlinePayFlag);
    },
    props:['memberInfo','reservOrderVo','tipText','valueDate','mealSection','onlinePayFlag','onlySelf'],
    methods: {
        tripcurronFun:function(id){
            this.travelType = id
        },
        tripEndDate:function(day){
            var dd = new Date();
                dd.setDate(dd.getDate() + day);//获取AddDayCount天后的日期
            var y = dd.getFullYear();
            var m = (dd.getMonth()+1)<10?"0"+(dd.getMonth()+1):(dd.getMonth()+1);//获取当前月份的日期，不足10补0
            var d = dd.getDate()<10?"0"+dd.getDate():dd.getDate();//获取当前几号，不足10补0
            return y+"-"+m+"-"+d
        },
        sendMsgToParent:function(){
            this.reservOrderVo.travelType = this.travelType; //出行类型
            this.reservOrderVo.flightNumber = this.flightNumber; //航班号
            this.reservOrderVo.airport = this.airport; //机场
            this.reservOrderVo.endPoint = this.endPoint; //目的地
            this.reservOrderVo.startPoint = this.startPoint; //出发地
            this.reservOrderVo.giftName = this.memberInfo.mbName;
            this.reservOrderVo.giftPhone = this.memberInfo.mobile;
            this.reservOrderVo.giftDate  = this.$store.state.startDate.replace(',','-').replace(',','-');
            this.reservOrderVo.giftTime = this.tripTime;
            this.reservOrderVo.giftPeopleNum = this.newroomNum;
            this.$emit('gopay',this.reservOrderVo);
            return true;
        },
        gotopercenter:function(){
            this.$router.push({name:'percenter'})
        },
        selectroomNum:function(){
            this.selectroomNumflag = true
        },
        sureselectroomNum:function(){
            this.newroomNum = this.roomNum
            this.selectroomNumflag = false;
            this.$parent.setNum(this.roomNum,Number(this.feedNum),this.onlinePayFlag);
        },
        coloseelectroomNum:function(){
            this.newroomNum = this.roomNum
            this.selectroomNumflag = false;
        },
        onroomNumChange(picker, values) {
            this.roomNum = values[0];
        },  
        openPickerTime() {
            this.$refs.tripTime.open()
        },
        handleChangeTime (value) {
            
        },
        openByDrop(e){
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
            //console.log(e)
            //获取完整的日期
            var startDate = this.calendar3.value
            var date = new Date(startDate[0]+'/'+startDate[1]+'/'+startDate[2]);
            this.current_year = date.getFullYear();
            this.current_month = date.getMonth()+1;
            this.getBlockCalendar(this.current_year, this.current_month);
            console.log(this.personDate)

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
        formatDate(date) {
            //console.log(date[2]);
            let mymonth = date[1];
            let myweekday = date[2];
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
            this.calendar3.disabled = this.blockDates;
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
    },
}
</script>