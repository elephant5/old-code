<template>
    <div class="addmedicalBox">
        <Topheader :headtitle="headtitle" v-if="headerBarflage"></Topheader>
        <div class="addCardTips">互联网就医实行实名制。为保证顺序完成预订，请填写真实信息，婴幼儿身份证信息请查看户口本</div>
        <div class="personType">
            <div class="TypeboxName"><span>*</span>姓名</div>
            <div class="TypeboxValue"><input type="text" placeholder="你的中文姓名" maxlength="10" v-model="username"  @blur="changeusername(username)"></div>
        </div>
        <div class="personType">
            <div class="TypeboxName"><span>*</span>证件类型</div>
            <div class="TypeboxValue">身份证</div>
        </div>
        <div class="personType">
            <div class="TypeboxName"><span>*</span>证件号</div>
            <div class="TypeboxValue"><input type="text" placeholder="你的证件号码"  maxlength="18"  v-model="idcardNo" @blur="changeidcardNo(idcardNo)"></div>
        </div>
        <div class="personType">
            <div class="TypeboxName"><span>*</span>医保类型</div>
            <div class="TypeboxValue medicaltype"><span :class="medicalInsuranceType == 1 ? 'curron':'' " @click="medicalInsuranceTypeFun(1)"><i></i>医保</span><span :class="medicalInsuranceType == 2 ? 'curron':'' "  @click="medicalInsuranceTypeFun(2)"><i></i>自费</span></div>
        </div>
        <div class="personType" style="border-bottom:none;">
            <div class="TypeboxName"><span>*</span>预约电话</div>
            <div class="TypeboxValue"><input type="tel" maxlength="11" placeholder="预约电话" v-model="mobile" @blur="cheangemobile(mobile)"></div>
        </div>
        <div class="medicalPerdefualt">
            <div class="TypeboxName">设为默认</div>
            <div class="TypeboxValue" @click="faDefaultshow"><span class="defualtspan" :class="faDefault == 1 ?'curron':''"><span></span></span></div>
        </div>
        <div class="medicalseverbook">
           提交即表示您已同意《用户信息保密协议》
        </div>
        <div style="text-align:center; color:#f60; padding-top:.5rem;">就诊人提交30天后，才能预约就医服务</div>
        <div class="addcardbtn" @click="gotoaddcard" style="border-radius: .6rem; margin-top:.4rem;">
            确定
        </div>
        

    </div>
</template>

<script>
import { Toast } from "mint-ui";
import Topheader  from '@/components/head/head';
import backthird from '@/components/bottombar/backthird';
import {getChannge, WXbodyBottomshow} from "@/common/js/common.js";
export default {
    name:'medicalperson',
    data() {
        return {
            headerBarflage: true,
            headtitle: "添加就诊人",
            username:'',
            usernameflage:false,
            idcardNo:'',
            idcardNoflage:false,
            mobile:'',
            mobileflage:false,
            faDefault:0,   //是否默认0否1是
            medicalInsuranceType:1, //医保类型 1-医保 2-自费  
            addCardBtn:false
        }
    },
    created(){
        WXbodyBottomshow();
        if (WXbodyBottomshow()) {
        this.headerBarflage = false;
        } else {
        this.headerBarflage = true;
        }
    },
    components: {
        Topheader,
        backthird,
    },
    methods: {
        gotoaddcard:function(){
            let datas = {
                mbid: this.$store.state.userMsg.mbid,
                mobile:this.mobile,
                name:this.username,
                certificateType:'idCard', //idCard:身份证  passport:护照  HK_Macao_Pass:港澳通行证
                certificateNumber:this.idcardNo,
                medicalInsuranceType:this.medicalInsuranceType,
                faDefault:this.faDefault 
            }
            if(this.addCardBtn){
                this.axios({method: 'post', url: '/member/family/add ',data:datas})
                .then(res => {
                    if(res.data.code == 200 || res.data.code == 400){
                        Toast({
                        message: res.data.msg
                        });
                    } else {
                        this.$router.push({name:'shopping'})
                    }
                })
                .catch(error => {
                    console.log(error)
                })
            }
           
        },

        changeaddCardBtn:function(){
            if(this.usernameflage && this.idcardNoflage && this.mobileflage){
                this.addCardBtn = true
            }
        },
        //中文姓名
        changeusername:function(val){
            //正则验证
            const namereg =  /^([\u4e00-\u9fa5\·]{1,10})$/;
            if(val && val.replace(/ /g, "")){
               if(!namereg.test(val.replace(/ /g, ""))){
                    Toast({message:'请输入正确格式的姓名'})
                    this.usernameflage = false
                } else{
                    this.usernameflage = true
                }
            }else{
                this.usernameflage = false
            }
            this.changeaddCardBtn()
            console.log(this.usernameflage)
        },

        //身份证
        changeidcardNo:function(val){
            if(val && val.replace(/ /g, "")){
                if(!this.regIdCheckFun(val.replace(/ /g, ""))){
                    Toast({message:'请输入正确格式的身份证号'})
                    this.idcardNoflage = false
                } else{
                    this.idcardNoflage = true
                }
            }else{
               this.idcardNoflage = false
            }
            this.changeaddCardBtn()
             console.log(this.idcardNoflage)
        },
        regIdCheckFun:function (val) {
            const p = /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/;
            const factor = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ];
            const parity = [ 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 ];
            const code = val.substring(17);
            if(p.test(val)) {
                let sum = 0;
                for(let i=0;i<17;i++) {
                    sum += val[i]*factor[i];
                }
                if(parity[sum % 11] == code.toUpperCase()) {
                    return true;
                }
            }
            return false;
        },

      //手机号
      cheangemobile:function(val){
          const mobile = /^[0-9]{11}$/;
           if(val && val.replace(/ /g, "")){
            if(!mobile.test(val.replace(/ /g, ""))){
                Toast({message:'请正确输入您预约银行的手机号码'})
                    this.mobileflage = false
                    this.smsCodeflage = false
            }else{
                    this.smsCodeflage = true //可发验证码
                    this.mobileflage = true
            }
           }else{
               this.mobileflage = false
               this.smsCodeflage = false
           }
           this.changeaddCardBtn()
           console.log(this.mobileflage)
      },
      faDefaultshow:function(){
          if(this.faDefault == 0){
              this.faDefault = 1
          }else{
              this.faDefault = 0
          }
      },
      medicalInsuranceTypeFun:function(id){
          this.medicalInsuranceType = id
      },

    },
}
</script>