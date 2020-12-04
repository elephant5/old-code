<template>
    <div class="shoppingFrombox_gym">
        <div class="shoppingTop_gym">
            <div class="shoppingMsgBox">
                <div class="shoppingMsg_Name">{{reservOrderVo.hotelName}}</div>
                <div class="shopoingroomType">
                    <i v-if='!!reservOrderVo.shopName'>{{reservOrderVo.shopName}}</i>
                    <i v-if='!!reservOrderVo.shopItemName&&reservOrderVo.serviceType=="spa"'>| {{reservOrderVo.shopItemName}}</i>
                </div>
                <div class="shoppingMsg_tips" v-if="!!tipText"><span class="shoppingTip"></span>{{tipText}}</div>
            </div>
        </div>
        <div class="shoppingBox_gym">
            <div class="inputlist">
                <div>人数</div>
                <div class="moreDivTip" @click="selectroomNum"><span class="roomNum">{{newroomNum}} 人</span><!--<span v-if="!!feedNum">该权益可减免{{feedNum}}人，超出需付费</span>--></div>
            </div>
            <div class="inputlist">
                <div>预约日期</div>
                <div class="moreDivTip" @click="openByDrop($event)" ><span class="roomNum" v-if="personDate">{{personDate}} </span><span v-else>请选择预约日期</span></div>
            </div>
            <div class="inputlist">
                <div>预约时间</div>
                <div class="moreDivTip" @click="selectTime"><span class="roomNum" v-if="newtimeshow">{{newtimeshow}} </span><span v-else>请选择预约时间</span></div>
            </div>
            <div class="inputlist">
                <div>使用人</div>
                <div><input type="text" name="giftName"   v-model="memberInfo.mbName" class="text_input giftPhone" placeholder="填写实际使用人姓名"  v-bind:disabled="onlySelf==='1'"></div>
            </div>
            
            <div class="inputlist">
                <div>手机号</div>
                <div><input type="text" name="giftPhone" v-model="memberInfo.mobile"  class="text_input" placeholder="填写正确的手机号"  v-bind:disabled="onlySelf==='1'"></div>
            </div>
            
        </div>


        <div class="personNumbox" v-if="selectroomNumflag" @click="coloseelectroomNum">
             <div class="personNumboxmsgshow">
                <div class="closeDailogBox">选择预约人数<span @click="coloseelectroomNum"></span></div>
                <div class="page-picker-wrapper">
                    <mt-picker :slots="numberSlot" @change="onroomNumChange" :visible-item-count="5"></mt-picker>
                    <div class="selectbtn" @click="sureselectroomNum">确定</div>
                </div>
            </div>
        </div>
        <div class="personZjzbox" v-if="selecttimeflag" @click="coloseselectZjz">
            <div class="page-picker-wrapper">
                <div class="selecttimeboxmsgshow">
                    <div class="closeDailogBox">选择预约时间<span @click="coloseselectZjz"></span></div>
                <mt-picker :slots="numberSlot3" @change="onTimeChange" :visible-item-count="5"></mt-picker>
                <div class="selectbtn" @click="sureselectZjz">确定</div>
             </div>
            </div>
        </div>
        
     <div class="calendarBox" v-if="calendarflag"  @click="sureselectroomNum">
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
    </div>
</template>

<script>
import Vue from 'vue'
import qs from 'qs'
import tabs from '@/components/tab/tab'
import calendar from '@/components/calendar/calendar.vue'
import { Picker } from 'mint-ui'
Vue.component(Picker.name, Picker)
export default {
    name:'shoppingroom',
    components: {
        calendar
    },
    data() {
        return {
            current_year: 0,
            current_month: 0,
            blockDates: [],
            personDate:this.$parent.formatStrDate( this.$store.state.startDate.split(',')),
            personday:this.$store.state.personDay,
            selectroomNumflag:false,
            selecttimeflag:false,
            roomNum:'1',
            newroomNum:'1',
            feedNum:'',
            timeshow:'',
            newtimeshow:'',
            zjzType:'身份证',
            numberSlot: [{
                flex: 1,
                defaultIndex: 0,
                values: [1, 2, 3, 4, 5, 6,7,8,9,10],
                className: 'slot1'
            }],
            numberSlot2: [{
                flex: 1,
                defaultIndex: 0,
                values: ['身份证','护照'],
                className: 'slot2'
            }],
            numberSlot3: [
               {
                flex: 1,
                values: this.mealSection,
                className: 'slot1',
                textAlign: 'center'
                }
            ],
            calendarflag:false,
            calendar3:{
                display:new Date().getMonth()+1+'月'+ new Date().getDate()+'日',
                show:false,
                zero:true,
                value: this.$store.state.startDate.split(','), //默认日期
                lunar:true, //显示农历
                select:(value)=>{
                    this.calendar3.show=false;
                    this.calendar3.value=value;
                    this.calendar3.display=value.join("/");
                    this.calendarflag = false;
                    this.personDate = this.formatDate(value);
                    this.$store.commit('getStartDate',value.toString())
                    this.$parent.getSetting()
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
            // console.log(this.onlySelf)
    },
    props:['memberInfo','reservOrderVo','tipText','valueDate','mealSection','onlinePayFlag','onlySelf'],
    methods: {
        sendMsgToParent:function(){
            this.reservOrderVo.giftName = this.memberInfo.mbName;
            this.reservOrderVo.giftPhone = this.memberInfo.mobile;
            this.reservOrderVo.giftDate  = this.$store.state.startDate.replace(',','-').replace(',','-');
            this.reservOrderVo.giftTime=this.timeshow;
            this.reservOrderVo.giftPeopleNum=this.roomNum;
            this.$emit('gopay',this.reservOrderVo);
            return true;
        },
        gotopercenter:function(){
            this.$router.push({name:'percenter'})
        },
        selectroomNum:function(){
            // this.selectroomNumflag = true
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
        selectTime:function(){
            this.selecttimeflag = true
        },
        onTimeChange(picker, values) {
            this.timeshow = values[0];
        }, 
        sureselectZjz:function(){
            this.newtimeshow = this.timeshow
            this.selecttimeflag = false
        },
        coloseselectZjz:function(){
            this.selecttimeflag = false
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
            // console.log(e)
            //获取完整的日期
            var startDate = this.calendar3.value
            var date = new Date(startDate[0]+'/'+startDate[1]+'/'+startDate[2]);
            this.current_year = date.getFullYear();
            this.current_month = date.getMonth()+1;
            this.getBlockCalendar(this.current_year, this.current_month);
        },
        formatDate(date) {
            //console.log(date[2]);
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
            // console.log('getday');
            let day = parseInt(new Date().getDate() + 1);
            let a = this.getCurrentMonthLast().getDate();
            // console.log('day',day,'a',a);
            if(day > a) {
                return new Date().getMonth()+2 + '月' + '1号';
            } else {
                return new Date().getMonth()+1 + '月' + parseInt(new Date().getDate() + 1) + '号';
            }
        },
        // 
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
        
        
