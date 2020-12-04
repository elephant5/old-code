<template>
  <div class="shoppingFrombox_accom">
    <div class="shoppingTop_accom">
      <div class="shoppingMsgBox">
        <div class="shoppingMsg_Name">{{reservOrderVo.hotelName}}</div>
        <div class="shoppingdateBox">
          入住：
          <span class="dateshow">{{showStartDate}}</span>
          <span class="dataNumshow">{{reservOrderVo.nightNumbers}}晚</span>离店：
          <span class="dateshow">{{showEndDate}}</span>
        </div>
        <div class="shopoingroomType">
          <i v-if='!!reservOrderVo.shopItemName'> {{new_shopItemName}}</i>
          <i v-if='reservOrderVo.accoAddon'> | {{new_accoAddon}}</i>
          <i v-if="reservOrderVo.accoNedds != 'null'"> | {{new_accoNedds}}</i> 
        </div>
        <div class="shoppingMsg_tips" v-if="!!tipText">
          <span class="shoppingTip"></span>{{tipText}}
        </div>
      </div>
    </div>
    <div class="shoppingBox_accom">
      <div class="inputlist">
        <div>房间数</div>
        <div class="moreDivTip" @click="selectroomNum">
          <span class="roomNum">{{newroomNum}} 间</span>
          <span>每间最多住2人</span>
        </div>
      </div>
      <div class="inputlist" style="padding:.25rem 0;">
        <div>入住人<span class="moreRoomtip">房间1</span></div>
        <div v-if="!linkedNamelist[0]">
          <input
            type="text"
            name="bookName"
            class="text_input giftPhone"
            placeholder="填写实际入住人姓名"
            v-model="memberInfo.mbName"
            ref="bookName"
            style="font-size:.31rem;"
            v-bind:readonly="surebaseFlag&&!!this.myInfo.giftName"
          >
        </div>
        <div v-else class="moreDivTip" @click="selectMem">
          <span class="zjzType">{{bookingName==null?linkedNamelist[0]:bookingName}}</span>
        </div>
      </div>
      <div class="inputlist" v-if="channel != 'BOSC'"><!--v-if="!linkedNamelist[0]"-->
        <div>是否本人</div>
        <div class="surebaseMsg" >
          <span :class="surebaseFlag?'curron': ''" @click="selectSelfBtn(true)">是</span><span :class="surebaseFlag?'': 'curron'" @click="selectSelfBtn(false)">否</span>
        </div>
      </div>
      <div class="inputlist" v-else>
        <div>是否本人</div>
        <div class="surebaseMsg">
          <span :class="surebaseFlag?'curron': ''" @click="selectSelfBtn(true)">是</span><span :class="surebaseFlag?'': 'curron'"  @click="selectSelfBtn(false)">否</span>
        </div>
      </div>
      <div class="inputlist">
        <div>证件类型</div>
        <div class="moreDivTip" @click="selectZjz">
          <span class="zjzType" v-if="!!zjzType"><span style="font-size:.31rem;">{{zjzType}}</span></span>
          <span class="zjzType" v-else-if="!!memberInfo.idNumber||!memberInfo.passportNum"  style="font-size:.31rem;">身份证</span>
          <span class="zjzType" v-else-if="!!memberCertificate['HK_Macao_Pass']"  style="font-size:.31rem;">港澳通行证</span>
          <span class="zjzType" v-else>护照</span>
        </div>
      </div>
      <div class="inputlist">
        <div>证件号码</div>
        <div>
          <input type="text" name="bookIdNum"  class="text_input bookIdNum" placeholder="填写正确的证件号码" 
          v-model="memberInfo.passportNum"
          v-if="zjzType=='护照'"
          v-bind:readonly="surebaseFlag&&!!this.myInfo.passportNum">

          <input type="text" name="bookIdNum"  class="text_input bookIdNum" placeholder="填写正确的证件号码" 
          v-model="memberCertificate['HK_Macao_Pass']"
          v-else-if="zjzType=='港澳通行证'"
          v-bind:readonly="surebaseFlag&&!!this.certificateNumber">

          <input type="text" name="bookIdNum"  class="text_input bookIdNum" placeholder="填写正确的证件号码" 
          v-model="memberInfo.idNumber"
          v-else
          v-bind:readonly="surebaseFlag&&!!this.myInfo.idNumber"  @change="disFunction">

        </div>
      </div>
      <div class="inputlist"  v-if="reservOrderVo.countryId != 'CN'">
        <div>拼音</div>
        <div>
          <input
            type="text"
            name="bookNameEn"
            class="text_input giftPhone"
            placeholder="与护照/通行证拼音一致"
            v-model="memberInfo.passportName"
          v-bind:readonly="surebaseFlag&&!!this.myInfo.passportName"
          ref="bookNameEn"
          >
        </div>
      </div>
      <div class="inputlist">
        <div>手机号</div>
        <div>
          <input type="text" name="giftPhone" class="text_input" maxlength="11" placeholder="填写正确的手机号"
          v-model="memberInfo.mobile"
          ref="bookPhone"
           v-bind:readonly="surebaseFlag&&!!this.myInfo.mobile"
          >
        </div>
      </div>
      <div class="inputlist">
        <div>床型</div>
        <div>
          <p class="nodetypeshow"><span v-for="(item,index) in accom_best" @click="selectradiovalue(item.systemshow,index)" :class="curronbest == index ? 'curron' : '' ">{{item.name}}</span></p>
        </div>
      </div>
      <div v-if="newroomNum>='2'">
          <div  v-for="(item,index) in newroomNum-1">
            <div class="inputlist" style="padding:.25rem 0;">
            <div>
                入住人
                <span class="moreRoomtip">房间{{index+2}}</span>
            </div>
            <div>
                <input
                type="text"
                name="giftPhone"
                class="text_input giftPhone"
                placeholder="填写实际入住人姓名"
                v-model="bookName[index]"
                >
            </div>
            </div>
            <div class="inputlist"  v-if="reservOrderVo.countryId!= 'CN'">
            <div>拼音</div>
            <div>
            <input
                type="text"
                name="bookNameEn"
                class="text_input giftPhone"
                placeholder="与护照/通行证拼音一致"
                v-model="bookNameEn[index]"
            >
            </div>
        </div>
          </div>
      </div>
    </div>

    <div class="personNumbox" v-if="selectMemflag"  @click="closeselectMem">
      <div class="personNumboxmsgshow">
        <div class="closeDailogBox">选择入住人<span @click="closeselectMem"></span></div>
          <div class="page-picker-wrapper">
            <mt-picker :slots="memSlot" @change="onMemChange" :visible-item-count="3"></mt-picker>
            <div class="selectbtn" @click="sureselectMem">确定</div>
          </div>
        </div>
    </div>

    <div class=" personNumbox" v-if="selectroomNumflag"  @click="closeselectroomNum">
      <div class="personNumboxmsgshow">
        <div class="closeDailogBox">选择房间数<span @click="closeselectroomNum"></span></div>
          <div class="page-picker-wrapper">
            <mt-picker :slots="numberSlot" @change="onroomNumChange" :visible-item-count="3"></mt-picker>
            <div class="selectbtn" @click="sureselectroomNum">确定</div>
          </div>
        </div>

    </div>
    <div class="personZjzbox" v-if="selectZjzflag" @click="sureselectZjz">
      <div class="Zjzboxmsgshow">
        <div class="closeDailogBox">选择证件类型<span @click="sureselectZjz"></span></div>
        <div class="page-picker-wrapper">
          <mt-picker :slots="numberSlot2" @change="onZjzChange" :visible-item-count="3"></mt-picker>
          <div class="selectbtn" @click="sureselectZjz">确定</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {mapState} from 'vuex'
import Vue from 'vue'
import { Picker,Toast } from 'mint-ui'
Vue.component(Picker.name, Picker)
export default {
    name:'shoppingroom',
    data() {
        return {
            changeFlag:'',
            selectZjzflag:false,
            selectroomNumflag:false,
            selectMemflag:false,
            roomNum:'1',
            newroomNum:'1',
            bookingName: this.linkedNamelist[0],
            numberSlot: [{
                flex: 1,
                defaultIndex: 0,
                values: [1, 2, 3, 4, 5, 6,7,8,9,10],
                className: 'slot1'
            }],
            numberSlot2: [{
                flex: 1,
                defaultIndex: 0,
                values: ['身份证','护照','港澳通行证'],
                className: 'slot2'
            }],
            memSlot:[{
              flex: 1,
              defaultIndex: 0,
              values: this.linkedNamelist,
              className: 'slot2'
            }],
            books:[],
            bookName:[],
            bookNameEn:[],
            zjzType:'',
            showStartDate:'',
            showEndDate:'',
            surebaseFlag:true,
            equitySubNum:1 ,
            curronbest:-1,
            accom_best:[{name:'尽量大床', systemshow:'trybigbed', type:0}, {name:'尽量双床', systemshow:'trydoublebed', type:1}, {name:'无需求', systemshow:'NULL', type:2}],
            new_shopItemName:this.reservOrderVo.shopItemName,
            new_accoAddon:this.reservOrderVo.accoAddon,
            new_accoNedds:this.reservOrderVo.accoNedds
        }
    },
    props:['reservOrderVo','memberInfo','tipText','myInfo','setting','bookPrice','memberCertificate','certificateNumber','linkedMemeList','linkedNamelist','channel'],
    computed:{
      ...mapState([
        'startDate',
        'endDate',
        'personDay',
      ]
      )
    },
    created() {
      this.showStartDate=this.$parent.formatStrDate( this.startDate.split(','))
      this.showEndDate= this.$parent.formatStrDate( this.endDate.split(','))
      console.log("showEndDate :"+this.showEndDate);
      this.reservOrderVo.accoNedds = ''
      this.reservOrderVo.nightNumbers = this.personDay
    },
    methods: {
        disFunction:function(){
          // this.cardNum = this.memberInfo.idNumber;
        },
        gotopercenter:function(){
            this.$router.push({name:'percenter'})
        },
        selectroomNum:function(){
            this.selectroomNumflag = true
        },
        sureselectroomNum:function(){
            //限制间夜数
            //NX 夜数*间数 <最大可预订间夜数   < 权益可用次数
            // N3  夜数 <3   夜数*间数<最大可预订间夜数  间数<权益可用次数
            var giftType = this.reservOrderVo.giftType;
            var nightBetween = parseInt( this.reservOrderVo.nightNumbers) * parseInt( this.roomNum);//间夜
            if(/^N/.test(giftType)){
              //this.reservOrderVo.nightNumbers  夜数
              //this.roomNum 间数
              var goodsSetting = this.setting.goodsSetting;
                //权益可用次数
                var equityNum = this.setting.equityCodeDetail.totalCount - this.setting.equityCodeDetail.useCount;
                //开放住宿
                if(giftType=='NX'){
                  //是否限制单次最大可预约数  默认值为9
                  if(!this.juageNums(nightBetween)){
                    return false;
                  }
                  if(nightBetween>equityNum){
                    Toast({
                      message: '预定间夜数不能超过权益剩余次数'
                    });
                    return false;
                  }
                }else{
                  var num = parseInt(giftType.substr(1));
                  if(this.reservOrderVo.checkNight>num){
                    Toast({
                      message: '预定夜数不能超过权益可用次数'
                    });
                    return false;
                  }
                  if(this.roomNum>equityNum){
                    Toast({
                      message: '预定间数不能超过权益剩余次数'
                    });
                    return false;
                  }
                }
              
            }else{
              Toast({
                 message: '权益类型有误！'
              });
              return false;
            }
            this.newroomNum = this.roomNum
            this.selectroomNumflag = false
            //开放住宿权益使用次数按间夜数来
            //非开放住宿权益使用次数 按房间数来
            if(giftType =='NX'){
              this.equitySubNum=nightBetween;
            }else{
              this.equitySubNum=this.roomNum;
            }
            this.$parent.setNum(this.roomNum,0,true);
        },
        closeselectroomNum:function(){
           this.selectroomNumflag = false
        },
        onroomNumChange(picker, values) {
            this.roomNum = values[0];
        }, 
        selectZjz:function(){
          //被禁用时 不能改变证件类型
          this.selectZjzflag = true
        },
        onZjzChange(picker, values) {
            this.zjzType=values[0];            
        }, 
        sureselectZjz:function(){
            this.selectZjzflag = false
        },
        sendMsgToParent:function(){
            //入住间
            this.reservOrderVo.checkNight= this.newroomNum;
            if(this.zjzType=='护照'){
              this.reservOrderVo.bookIdType = 'passport';
              this.reservOrderVo.bookIdNum = this.memberInfo.passportNum;
            }else if(this.zjzType == '港澳通行证'){
              this.reservOrderVo.bookIdType = 'HK_Macao_Pass'
              this.reservOrderVo.bookIdNum = this.memberCertificate['HK_Macao_Pass']
            }else{
              this.reservOrderVo.bookIdType = 'idCard';
              this.reservOrderVo.bookIdNum = this.memberInfo.idNumber;
            }
            
            //渲染books
            // this.books = new Array(this.newroomNum-1);
            // var book;
            //初始化为 会员名和护照名
            var bookNameStr = this.memberInfo.mbName;
            var bookNameEnStr=this.memberInfo.passportName;
            for(var i= 0; i<( this.newroomNum-1); i++){
                // book={};
                if(!this.bookName[i]){
                  Toast({
                          message: '入住人名称必填'
                  });
                  return false;
                }
               bookNameStr+=","+this.bookName[i];
                if(this.reservOrderVo.countryId!='CN'){
                  if(!this.bookNameEn[i]){
                    Toast({
                            message: '入住人拼音必填'
                    });
                    return false;
                  }
                  bookNameEnStr+=","+this.bookNameEn[i];
                }
                
            }
            //住宿预订人
            
            this.reservOrderVo.bookPhone =  this.memberInfo.mobile;
            this.reservOrderVo.giftName = bookNameStr;
            this.reservOrderVo.giftPhone = this.memberInfo.mobile;
            this.reservOrderVo.deparDate=this.$store.state.endDate.replace(/,/g,'-');
            this.reservOrderVo.checkDate=this.$store.state.startDate.replace(/,/g,'-');
            //组合房间预约人名称
            this.reservOrderVo.bookName = bookNameStr;
            if(this.reservOrderVo.countryId!='CN'){
              this.reservOrderVo.bookNameEn =bookNameEnStr;
            }
            //是否本人
            this.reservOrderVo.self=this.surebaseFlag;
            if(this.reservOrderVo.giftType =='NX'){
              let nums = this.reservOrderVo.checkNight*this.reservOrderVo.nightNumbers;
              this.equitySubNum=nums;
              if(!this.juageNums(nums)){
                return false;
              }
            }else{
              this.equitySubNum=this.reservOrderVo.checkNight;
            }
            //扣减的权益次数
            this.reservOrderVo.exchangeNum=this.equitySubNum;

            this.reservOrderVo.equitySubNum = this.equitySubNum;
            this.$emit('gopay',this.reservOrderVo);
            return true;
           
        },
        juageNums:function(nums){
              //最大最小预定间夜数判断
              let goodsSetting =  this.setting.goodsSetting;
              let maxNight = 9;
                if(goodsSetting.enableMaxNight==1&&!!goodsSetting.maxNight){
                  maxNight=goodsSetting.maxNight;
                }
                let minNight = 1;
                if(goodsSetting.enableMinNight==1&&!!goodsSetting.minNight){
                  minNight=goodsSetting.minNight;
                }
                  if(nums >maxNight){
                      Toast({
                        message: '预定间夜数不能超过单次最大可预约间夜数'
                      });
                      return false;
                  }
                  if(nums < minNight){
                      Toast({
                        message: '预定间夜数不能小于最小可预约间夜数'
                      });
                      return false;
                  }
                  return true;
        },
        //选择是否是本人
        selectSelfBtn:function(flag){
          if(flag){
            this.memberInfo.mbName=this.myInfo.giftName;
            this.memberInfo.idNumber=this.myInfo.idNumber;
            this.memberInfo.passportNum=this.myInfo.passportNum;
            this.memberInfo.passportName=this.myInfo.passportName;
            this.memberInfo.mobile=this.myInfo.mobile;
            this.memberCertificate['HK_Macao_Pass'] = this.certificateNumber
          }else{
            this.memberInfo.idNumber='';
            this.memberInfo.passportNum='';
            this.memberInfo.passportName='';
            // this.memberInfo.mobile='';
            // this.memberInfo.mbName='';
            this.memberCertificate['HK_Macao_Pass']='';
          }
          this.surebaseFlag =flag;
        },
        selectradiovalue:function(valueword,index){
          this.curronbest = index
          this.reservOrderVo.accoNedds = valueword
          // console.log(this.reservOrderVo.accoNedds)
        },
        selectMem:function(){
          this.selectMemflag = true
        },
        closeselectMem: function(){
          this.selectMemflag = false
        },
        onMemChange:function(picker, values){
          // 选择的人
          this.bookingName = values[0]
          // 选择的人的手机号
          for(var i = 0;this.linkedMemeList.length>i;i++){
            if(this.linkedMemeList[i].split("-")[0] == this.bookingName){
              this.memberInfo.mobile = this.linkedMemeList[i].split("-")[1]
            }
          }
          // 是否本人
          this.surebaseFlag = this.memberInfo.ctName == this.bookingName
          this.selectSelfBtn(this.memberInfo.ctName == this.bookingName)
          // 赋值
          this.memberInfo.mbName = this.bookingName
        },
        sureselectMem:function(){

        }

    },
}
</script>