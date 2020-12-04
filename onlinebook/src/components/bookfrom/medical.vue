<template>
    <div class="shoppingFrombox_gym">
       <div class="shoppingTop_medical">
            <div class="shoppingMsgBox">
                <div class="shoppingMsg_Name">{{shopMsgshow.name}}</div>
                <div class="shopoingroomType">
                    <i>{{shopMsgshow.serviceName}}</i>
                </div>
                <div class="shoppingMsg_tips" v-if="!!tipText"><span class="shoppingTip"></span>{{tipText}}</div>
            </div>
        </div>
        <div class="shoppingBox_gym">
            <div class="inputlist">
                <div>选择医院</div>
                <div  class="moreDivTip" v-if="hosplitalname" style="font-size:.31rem; color:#333;" @click="selsecthospitalName">{{hosplitalname}}</div>
                <div  class="moreDivTip" v-else style="color:#999;"  @click="selsecthospitalName">请选择医院</div>
            </div>
            <div class="inputlist">
                <div>预约科室</div>
                <div><input type="text"  class="text_input giftPhone"  placeholder="请输入预约科室" v-model="department"   style="font-size:.31rem;"></div>
            </div>
            
            <div class="inputlist" style="padding:0;">
                <div>就诊类型</div>
                <div class="medicalType"><span :class="visit == '专家门诊' ?'curron':''" @click="visitFun('专家门诊')">专家门诊</span> <span  :class="visit == '住院安排' ?'curron':''" @click="visitFun('住院安排')">住院安排</span></div>
            </div>
            <div class="inputlist" v-if="visit == '专家门诊'">
                <div>特殊需求</div>
                <div class="medicalneeds"><span :class="special == '陪诊' ?'curron':''" @click="specialFun('陪诊')"><em></em>陪诊</span> <span :class="special == '无需陪诊' ?'curron':''" @click="specialFun('无需陪诊')"><em></em>无需陪诊</span></div>
            </div>
            <div class="inputlist"  @click="openByDrop($event)">
                <div >预约日期</div>
                <div  class="moreDivTip" v-if="personDate" style="font-size:.31rem; color:#333;">{{personDate}}</div>
                <div  class="moreDivTip" v-else style="color:#999;">请选择就诊日期</div>
            </div>
            <div class="inputlist" @click="openPickerTime">
                <div >预约时间</div>
                <div  class="moreDivTip" v-if="tripTime" style="font-size:.31rem; color:#333;">{{tripTime}}</div>
                <div  class="moreDivTip" v-else style="color:#999;">请选择就诊时间</div>
            </div>
            <div class="inputlist" @click="selectroomNum">
                <div>就诊人</div>
                <div class="moreDivTip"><input type="text" name="giftName"   v-model="familyList.name" class="text_input giftPhone" placeholder="填写实际使用人姓名"></div>
            </div>
        </div>
        <div class="personNumbox" v-if="selectroomNumflag" @click="coloseelectroomNum">
             <div class="personNumboxmsgshow" style="height:auto; padding-bottom:.3rem; height:4.5rem;">
                <div class="closeDailogBox"><span style=" color: rgb(53, 119, 227); white-space: nowrap; left: .2rem; top:.2rem; background: none;color:#3577e3" @click="addperson" >管理就诊人</span>选择就诊人<span @click="coloseelectroomNum"></span></div>
                
                <div style="padding-top:.3rem;"><div v-for="(item, index) in familyallList" class="familListBox">
                    <div :class="familycurron == index ?'curron':''" @click="selectFmailyList(index)">{{item.name}}<span>{{item.mobile}}</span></div>
                </div>
                </div>
            </div>
        </div>
        <!-- <mt-datetime-picker ref="tripDate"  type="date" :startDate="new Date(tripEndDate(1))" :endDate="new Date(tripEndDate(30))" year-format="{value} 年"  month-format="{value} 月"  date-format="{value} 日"  v-model="tripDate" @confirm="handleChangeDate"></mt-datetime-picker> -->
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
        <mt-datetime-picker ref="tripTime"  type="time"  v-model="tripTime" @confirm="handleChangeTime" hour-format="{value} 时" minute-format="{value} 分"></mt-datetime-picker>

        <div class="maskshow" style="top:0" v-if="hospitalPerson">
            <div class="addpersonshow">
                就诊人提交30天后，才能预订就医服务!
                <div class="addpersonBtn" @click="addpersonmsg">填写就诊人信息</div>
            </div>
        </div>

    </div>
</template>

<script>
import {mapState} from 'vuex'
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
            selectroomNumflag:false,
            selecttimeflag:false,
            blockDates : [],
            shopMsgshow:this.$store.state.giftShopProdList.shopResDto.shopItemResDtoList[0].shopItem,//商户信息
            roomNum:'1',
            newroomNum:'1',
            useFreeCount:'0',
            feedNum:'',
            hosplitalname:'',
            familycurron:'',
            timeshow:'',
            newtimeshow:'',
            zjzType:'身份证',
            department:'',//科室
            personDate:this.$parent.formatStrDate(this.$store.state.startDate.split(',')),//预约日期,
            tripTime:'',//预约时间
            visit:'专家门诊', //就诊类型  专家门诊   住院安排
            special:'陪诊', //特殊需求  陪诊  无需陪诊
            familyList:{}, //就诊人信息
            familyallList:[],
            basedateflag:false,
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
            hospitalPerson:false,  //就诊人信息
        }
    },
    created(){
            this.feedNum = '1';
            this.$parent.setNum(1,1,this.onlinePayFlag);
            
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
            this.familyListfun();
            this.hosplitalname = this.$store.state.hospitalMsg?this.$store.state.hospitalMsg.name:''
    },
    props:['memberInfo','reservOrderVo','tipText','mealSection','onlinePayFlag','onlySelf'],
    computed:{
      ... mapState([
          "hospitalMsg",
      ])
  },
    methods: {
        visitFun:function(id){
            this.visit = id
        },
        specialFun:function(id){
            if(this.visit == '专家门诊'){
                this.special = id
            }else{
                this.special = ''
            }
            
        },
        selsecthospitalName:function(){
            this.$router.push({name: "hospital"});
        },
        familyListfun:function(){
            this.axios({method: 'post', url: '/member/family/list',data:{"id":this.$store.state.userMsg.mbid}})
            .then(res => {
                this.familyallList = res.data.result
                if(this.familyallList.length != 0){
                    for(var i = 0; i< this.familyallList.length; i++){
                        if(this.familyallList[i].faDefault == '1'){
                            this.familyList = this.familyallList[i]
                            this.familycurron = i
                        }
                    }
               }else{
                   this.hospitalPerson = true
               }

            })
            .catch(error => {
                console.log(error)
            })
        },
        selectFmailyList:function(id){
            this.familyList = this.familyallList[id]
            this.familycurron = id
        },
        // tripEndDate:function(day){
        //     var dd = new Date();
        //         dd.setDate(dd.getDate() + day);//获取AddDayCount天后的日期
        //     var y = dd.getFullYear();
        //     var m = (dd.getMonth()+1)<10?"0"+(dd.getMonth()+1):(dd.getMonth()+1);//获取当前月份的日期，不足10补0
        //     var d = dd.getDate()<10?"0"+dd.getDate():dd.getDate();//获取当前几号，不足10补0
        //     return y+"-"+m+"-"+d
        // },
        sendMsgToParent:function(){
            this.reservOrderVo.hospitalId = this.$store.state.hospitalMsg.id; //医院ID
            this.reservOrderVo.department = this.department; //科室
            this.reservOrderVo.visit = this.visit; //就诊类型
            this.reservOrderVo.special = this.special; //特殊需求
            this.reservOrderVo.memFamilyId = this.familyList.id; //预约人家属ID
            this.reservOrderVo.giftDate  = this.$store.state.startDate.replace(',','-').replace(',','-');
            this.reservOrderVo.giftTime = this.tripTime;
            this.reservOrderVo.giftPhone = this.memberInfo.mobile;

            this.$emit('gopay',this.reservOrderVo);
            return true;
        },
        addpersonmsg:function(){
            this.$router.push({name:'medicalperson'})
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
        formatDate(date) {
            //console.log(date[2]);
            let mymonth = date[1];
            let myweekday = date[2];
            return (mymonth.replace(/\b(0+)/gi,"") + '月' + myweekday.replace(/\b(0+)/gi,"") + '日')
        },
        addperson(){
            this.$router.push({name:'medicalpersonlist'})
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
        //设备block日期
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
        closeByDialog(){
            this.basedateflag=false
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
<style>
.maskshow{position:fixed; width: 100%; height: 100%; top:1.4rem; left: 0; background: rgba(0, 0, 0, .5); z-index: 5}
.maskshow .addpersonshow{background:#fff; padding: .4rem .3rem .5rem .3rem; position: fixed;  left: 10%; width: 80%; top: 40%; transform: rotateY(-30%); box-sizing: border-box; border-radius: .1rem; color: #c0392b; text-align: center;}
.maskshow .addpersonshow .addpersonBtn{background: #fe4b4c; color: #fff; margin: .4rem .5rem 0 .5rem; padding: .2rem 0; border-radius: 1rem;}
</style>