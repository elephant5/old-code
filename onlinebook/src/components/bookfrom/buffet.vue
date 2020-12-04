<template>
    <div class="shoppingFrombox_buffet">
        <div class="shoppingTop_buffet">
            <div class="shoppingMsgBox">
                <div class="shoppingMsg_Name">{{reservOrderVo.hotelName}}</div>
                <div class="shopoingroomType">
                    <i v-if='!!reservOrderVo.shopName'>{{reservOrderVo.shopName}} | </i>
                    <i v-if='!!reservOrderVo.shopItemName'>{{reservOrderVo.shopItemName}}</i>
                </div>
                <div class="shoppingMsg_tips" v-if="!!tipText"><span class="shoppingTip"></span>{{tipText}}</div>
            </div>
        </div>
        <div class="shoppingBox_buffet">
            <div class="inputlist">
                <div>套餐类型</div>
                <!-- <div class="moreDivTip" @click="selectroomNum"><span class="roomNum">{{newroomNum}} 人</span><span v-if="!!feedNum">该权益可减免{{feedNum}}人，超出需付费</span></div> -->
                <div class="" ><span class="roomNum">{{newroomNum}}</span><span v-if="!!feedNum" style="color:#ccc;">人餐  超出套餐人数需到店付</span></div>
            </div>
            <div class="inputlist">
                <div>用餐人数</div>
                <div class="moreDivTip" @click="selectroomNum"><span class="roomNum">{{newroomNum2}}</span><span style="color:#ccc;"> 人</span></div>
            </div>

            <div class="inputlist">
                <div>用餐日期</div>
                <div  class="moreDivTip"  @click="openByDrop($event)" ><span class="roomNum" v-if="personDate">{{personDate}}</span><span v-else>请选择用餐日期</span></div>
            </div>
            <div class="inputlist">
                <div>用餐时间</div>
                <div class="moreDivTip" @click="selectTime"><span class="roomNum" v-if="newtimeshow">{{newtimeshow}}</span><span v-else>请选择用餐时间</span></div>
            </div>
            <div class="inputlist">
                <div>用餐人</div>
                <div v-if="channel!='BOSC'||(channel==='BOSC'&&!linkedNamelist[0])">
                <input
                    type="text"
                    name="giftName"
                    class="text_input giftName"
                    placeholder="请输入用餐人的姓名"
                    v-model="memberInfo.mbName"
                    ref="giftName"
                    style="font-size:.31rem;"
                    v-bind:disabled="onlySelf==='1'"
                >
                </div>
                <div v-else class="moreDivTip" @click="selectMem">
                    <span class="zjzType">{{bookingName==null?linkedNamelist[0]:bookingName}}</span>
                </div>
                
            </div>
            <div class="inputlist">
                <div>手机号</div>
                <div><input type="text" name="giftPhone"  v-model="memberInfo.mobile"    class="text_input giftPhone" placeholder="填写正确的手机号" 
                v-bind:disabled="onlySelf==='1'"
                ></div>
            </div>
        </div>

        <div class="personNumbox" v-if="selectMemflag"  @click="closeselectMem">
        <div class="personNumboxmsgshow">
            <div class="closeDailogBox">选择用餐人<span @click="closeselectMem"></span></div>
            <div class="page-picker-wrapper">
                <mt-picker :slots="memSlot" @change="onMemChange" :visible-item-count="3"></mt-picker>
                <div class="selectbtn" @click="sureselectMem">确定</div>
            </div>
            </div>
        </div>

        <div class="personNumbox" v-if="selectroomNumflag" @click="coloseselectroomNum">>
            <div class="personNumboxmsgshow">
                <div class="closeDailogBox">选择用餐人数<span @click="coloseselectroomNum"></span></div>
                <div class="page-picker-wrapper">
                    <mt-picker :slots="numberSlot" @change="onroomNumChange" :visible-item-count="3"></mt-picker>
                    <div class="selectbtn" @click="sureselectroomNum">确定</div>
                </div>
            </div>
        </div>

        <div class="personZjzbox" v-if="selecttimeflag" @click="closeselectZjz">
            <div class="page-picker-wrapper">
                <div class="selecttimeboxmsgshow">
                    <div class="closeDailogBox">选择用餐时间<span @click="closeselectZjz"></span></div>
                    <mt-picker :slots="numberSlot3" @change="onTimeChange" :visible-item-count="5"></mt-picker>
                    <div class="selectbtn" @click="sureselectZjz">确定</div>
                </div>
            </div>
        </div>

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
            personDate:this.$parent.formatStrDate(this.$store.state.startDate.split(',')),
            selecttimeflag:false,
            selectroomNumflag:false,
            selectMemflag:false,
            roomNum:'1',
            newroomNum:'1',
            newroomNum2:'1',
            timeshow:'',
            newtimeshow:'',
            feedNum:'',
            fixNum:'',
            bookingName: this.linkedNamelist[0],
            numberSlot: [{
                flex: 1,
                defaultIndex: 0,
                values: [1, 2, 3, 4, 5, 6,7,8,9,10],
                className: 'slot1'
            }],
            value:[],
            numberSlot3: [
               {
                flex: 1,
                values: this.mealSection,
                className: 'slot1',
                textAlign: 'center'
                }
            ],
            memSlot:[{
              flex: 1,
              defaultIndex: 0,
              values: this.linkedNamelist,
              className: 'slot2'
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

    props:['memberInfo','reservOrderVo','tipText','valueDate','mealSection','linkedMemeList','linkedNamelist','onlinePayFlag','onlySelf','channel'],
    created(){
        let giftType = this.reservOrderVo.giftType;
        if(giftType=='D5'){
            giftType='2F1';
        }
        if(giftType == 'F2'){
            this.feedNum = '2'
            this.newroomNum ='2';
            this.newroomNum2 = '2'
            this.fixNum=2;
        }else if(giftType == '3F1'){
            this.feedNum = '1'
            this.newroomNum ='3';
            this.newroomNum2 = '3'
            this.fixNum=3;
        }else if(giftType == 'F1'){
            this.feedNum = '1'
            this.newroomNum ='1';
            this.newroomNum2 = '1'
            this.fixNum=1;
        }else if(giftType == '2F1'){
            this.feedNum = '1'
            this.newroomNum ='2';
            this.newroomNum2 = '2'
            this.fixNum=2;
        }
        this.$parent.setNum(Number(this.fixNum),Number(this.feedNum),this.onlinePayFlag);

        // console.log(this.onlySelf)
    },
    methods: {
        gotopercenter:function(){
            this.$router.push({name:'percenter'})
        },
        selectroomNum:function(){
            this.selectroomNumflag = true
        },
        sureselectroomNum:function(){
            this.selectroomNumflag = false
            var number  = Number(this.roomNum);
            if(number<this.fixNum){
                number = this.fixNum;
            }
            this.$parent.setNum(number,Number(this.feedNum),this.onlinePayFlag);
        },
        coloseselectroomNum:function(){
             this.selectroomNumflag = false
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
        closeselectZjz:function(){
            this.selecttimeflag = false
        },
        sendMsgToParent:function(){
            this.reservOrderVo.giftName = this.memberInfo.mbName;
            this.reservOrderVo.giftPhone = this.memberInfo.mobile;
            this.reservOrderVo.giftDate  = this.$store.state.startDate.replace(',','-').replace(',','-');
            this.reservOrderVo.giftTime=this.timeshow;
            this.reservOrderVo.giftPeopleNum=this.newroomNum;
            this.$emit('gopay',this.reservOrderVo);
            return true;
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
        getday(){
            // console.log('getday');
            let day = parseInt(new Date().getDate() + 1);
            let a = this.getCurrentMonthLast().getDate();
            // console.log('day',day,'a',a);
            if(day > a)
            {
                return new Date().getMonth()+2 + '月' + '1号';
            }
            else
            {
                return new Date().getMonth()+1 + '月' + parseInt(new Date().getDate() + 1) + '号';
            }
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
        selectMem:function(){
            this.selectMemflag =( this.onlySelf!='1');
        },
        closeselectMem: function(){
          this.selectMemflag = false
        },
        onMemChange:function(picker, values){
          this.bookingName = values[0]
          for(var i = 0;this.linkedMemeList.length>i;i++){
            if(this.linkedMemeList[i].split("-")[0] == this.bookingName){
              this.memberInfo.mobile = this.linkedMemeList[i].split("-")[1]
            }
          }
          this.memberInfo.mbName = this.bookingName
        },
        sureselectMem:function(){

        }

    },
}
</script>
        
        
