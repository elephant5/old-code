<template>
    <div class="addCardBox">
        <div class="addCardTips">请绑定持卡人本人的银行卡及银行预留的手机号</div>
        <div class="personType">
            <div class="TypeboxName">姓名</div>
            <div class="TypeboxValue"><input type="text" placeholder="你的中文姓名" maxlength="10" v-model="username"  @blur="changeusername(username)"></div>
        </div>
        <div class="personType">
            <div class="TypeboxName">身份证</div>
            <div class="TypeboxValue"><input type="text" placeholder="你的身份证号码"  maxlength="18"  v-model="idcardNo" @blur="changeidcardNo(idcardNo)"></div>
        </div>
        <div class="personType">
            <div class="TypeboxName">银行卡号</div>
            <div class="TypeboxValue"><input type="tel" placeholder="光大银行卡号" maxlength="19" v-model="bankCardNo" @blur="changeCount(bankCardNo)"></div>
        </div>
        <div class="personType">
            <div class="TypeboxName">CVC码</div>
            <div class="TypeboxValue"><input type="tel" placeholder="信用卡背面后三位" maxlength="3" v-model="bankCardCVC" @blur="cheangebankCardCVC(bankCardCVC)"></div>
        </div>
        <div class="personType">
            <div class="TypeboxName">有效期至</div>
            <div class="TypeboxValue"><input type="tel"  placeholder="YY-MM" maxlength="4" v-model="bankCardDate" @blur="cheangebankCardDate(bankCardDate)"></div>
        </div>
        <div class="personType">
            <div class="TypeboxName">手机号</div>
            <div class="TypeboxValue"><input type="tel" maxlength="11" placeholder="银行预留手机号" v-model="mobile" @blur="cheangemobile(mobile)"></div>
        </div>
        <div class="personType">
            <div class="TypeboxName">验证码</div>
            <div class="TypeboxValue"><input type="text" maxlength="6" placeholder="6位数字验证码" style="width:50%" v-model="smsCode" @blur="cheangesms(smsCode)">
                <em v-if="!sendAuthCode" @click="getsmsCode" :class="{default:!smsCodeflage}">获取验证码</em>
                <em v-else  class="default" :class="{default:smsCodeflage}">重新获取{{auth_time}}秒</em>
            </div>
        </div>
        <div class="addcardbtn" @click="gotoaddcard" :class="{defaultaddCard:addCardBtn}">
            确定
        </div>
        <div class="agreetips">绑定即表示您已同意<span @click="showdailogMsgBox">《免密支付协议》</span></div>


        <div class="ruluedailog" v-if="rulueDialogFlag">
            <div class="dailogMsgBox">
                <div class="closeDailog"><span @click="closedailogMsgBox"></span></div>
                <div class="rulurTitle">免密支付协议</div>
                <div class="dailogMsgshow">
                    本确认书由您向开联通支付科技有限公司(下称“开联通”)出具，具有授权之法律效力。请您务必审慎阅读、充分理解本确认书各条款内容，特别是免除或者限制责任的条款，前述条款可能以<strong>加粗字体</strong>显示，您应重点阅读。除非您已阅读并接受本确认书所有条款，否则您无权使用自动续费、自动缴费、自动扣款等服务。<strong>您同意本确认书即视为您已向开联通提供自动扣款授权，并自愿承担由此导致的一切法律后果。</strong>
                        <br />
                        <br />
                        您确认并授权<strong>上海静元信息技术有限公司</strong>（“客乐芙”）(以称“商户”)向开联通发出扣款指令，开联通即可在不验证您的账户密码、短信动态码等信息的情况下直接从您的银行结算账户 (根据商户接入业务渠道选择)中扣划商户指定的款项至商户指定账户。
                        <br />
                        <br />
                        <strong>开联通会在首次绑卡时验证您的短信验证码，你输入短信验证码即视为您授权开联通可依据商户的交易指令从您的账户相绑定的银行账户中扣划商户指定的款项至商户指定账户，且开联通后续再扣款时，将无需再验证您的支付密码、短信动态码等信息。</strong>
                        <br />
                        <br />
                        在任何情况下，只要商户向开联通发出支付指令，开联通就可按照该指令进行资金扣划，开联通对商户的支付指令在实质上的正确性、合法性、完整性、真实性不承担任何法律责任，相关法律责任由您和商户自行承担。
                        <br />
                        <br />
                        您在扣款账户内必须预留有足够的被扣划的余额，否则因账户余额不足导致无法及时扣款或扣款错误、失败，责任由您自行承担。因不可归责于开联通的事由，导致的不能及时划付款项、划账错误等责任与商户、开联通无关。
                </div>
            </div>
        </div>

    </div>
</template>

<script>
import { Toast } from "mint-ui";
import Topheader  from '@/components/head/head';
import backthird from '@/components/bottombar/backthird';
import{WXbodyBottomshow,getChannge} from '@/common/js/common.js'
export default {
    name:'cardbind',
    data() {
        return {
            username:'',
            usernameflage:false,
            idcardNo:'',
            idcardNoflage:false,
            bankCardNo:'',
            bankCardNoflage:false,
            bankCardCVC:'',
            bankCardCVCflage:false,
            bankCardDate:'',
            bankCardDateflage:false,
            mobile:'',
            mobileflage:false,
            smsCode:'',
            smsCodeflage:false,
            addbankflage:false,
            yzmflage:false,
            auth_time:0,
            sendAuthCode:false,
            addCardBtn:true,
            smsoriginalRequestId:'',
            mchtId:'',//商户号
            rulueDialogFlag:false,
            cardlist: this.$route.query.cardlist?this.$route.query.cardlist:[],
            ordermsg:this.$route.query.ordermsg?this.$route.query.ordermsg:'',
        }
    },
    created(){
        
    },
    components: {
        Topheader,
        backthird,
    },
    methods: {
        gotoaddcard:function(){
            var smsCode= this.smsCode
            var originalRequestId = this.smsoriginalRequestId
            if(!this.addCardBtn){
                 this.axios({
                    method: 'post',
                    url: '/mercury/bankCard/bindCard',
                    headers: { 'Content-type': 'application/json' },
                    data: {
                        originalRequestId,
                        smsCode,
                        payerTelephone: this.mobile,
                        payerAcctNo: this.bankCardNo,
                        payerIdNo: this.idcardNo,
                        payerName: this.username,
                        cvv2:this.bankCardCVC,
                        acctValidDate:this.bankCardDate,
                        payerIdType: "01",
                        }
                })
                .then(res => {
                    if (res.data.code == 100) {
                        this.gotoPay(this.bankCardNo,res.data.result.agreementId)
                    //    this.$router.push({name: "card",query:{cardlist:res.data.result,ordermsg:this.ordermsg}});
                    }
                })
                 .catch(error => {
                  console.log(error);
                });
            }
        },
         gotoPay:function(bankCardNo,agreementId) {
            this.axios({
                      method: "get",
                      url: "/mars/pay/getPayParams/"+ this.ordermsg.id
                    }).then(res => {
                        if(res.data.code == 100){
                            var jsondata = JSON.parse(res.data.result.params);
                          this.axios({
                            method: "post",
                            url: "/mars/pay/getAggregatePayParams/",
                                headers: {'Content-type': 'application/json'},
                                data: {
                                    amount: jsondata.amount,
                                    mbId: jsondata.mbId,
                                    orderId: jsondata.orderId,
                                    curcode: '156',//jsondata.curcode'',
                                    source: jsondata.source,
                                    body: jsondata.body,
                                    paymentType: jsondata.paymentType,
                                    acId: jsondata.acId,
                                    merchantId: jsondata.merchantId,
                                    payMethod: jsondata.payMethod,
                                    goodsName: jsondata.goodsName,
                                    agreementId: agreementId,
                                    }
                        }).then(res => {
                            if(res.data.code == 100){
                                // var data = JSON.parse(res.data.result.params); 
                                // data.agreementId = agreementId;
                                // var data2 = JSON.stringify(data);
                                if(this.$store.state.backUrl && this.$store.state.backUrl != null){
                                    window.location.href = process.env.PAY_ROOT+'/home?channel='+Base64.encode(this.$store.state.channel)+'&params='+ Base64.encode(encodeURIComponent(res.data.result.params))+'&backurl='+this.$store.state.backUrl
                                }else{
                                    window.location.href = process.env.PAY_ROOT+'/home?channel='+Base64.encode(this.$store.state.channel)+'&params='+ Base64.encode(encodeURIComponent(res.data.result.params))
                                }
                            }else{
                                Toast({
                                message: res.data.msg
                                });
                            }
                        }).catch(error => {
                            console.log(error);
                        });
                        }
                    })
        },
        changeaddCardBtn:function(){
            if(this.usernameflage && this.idcardNoflage && this.bankCardNoflage && this.bankCardCVCflage && this.bankCardDateflage && this.yzmflage){
                this.addCardBtn = false
            }
        },
        showdailogMsgBox:function(){
            this.rulueDialogFlag = true
        },
        closedailogMsgBox:function(){
            this.rulueDialogFlag = false
        },
        getsmsCode:function() {
            if(this.smsCodeflage){
                this.sendAuthCode = true
                this.getAuthCode()
                this.axios({
                    method: 'post',
                    url: '/mercury/bankCard/getAgreementSms',
                    headers: {'Content-type': 'application/json'},
                    data: {
                        payerTelephone: this.mobile,
                        payerAcctNo: this.bankCardNo,
                        payerIdNo: this.idcardNo,
                        payerIdType: "01",
                        payerName: this.username
                        }
                })
                .then(res => {
                    if (res.data.code == 100) {
                       this.smsoriginalRequestId = res.data.result.requestId
                    }else{
                        Toast({message:res.data.msg})
                    }
                })
                 .catch(error => {
                  console.log(error);
                }); 
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

        //检查银行卡号
        changeCount:function(val){
            const bankCardreg = /^[0-9]{16,19}$/;
            if(val && val.replace(/ /g, "")){
                if(!bankCardreg.test(val.replace(/ /g, ""))) {
                    var bankno = val.replace(/^(.{8}).+(.{4})$/g, "$1******$2");
                    if(bankno.length < 16 || bankno.length > 19) {
                        Toast({message:'银行卡号长度必须在16到19之间'})
                        this.bankCardNoflage = false
                    }else{
                        Toast({message:'请正确输入光大信用卡号'});
                        this.bankCardNoflage = false
                    }   
                }else{
                    const bankCardNum = (val.replace(/ /g, "")).substring(0,9);
                    if(bankCardNum == '622655369' || bankCardNum == '622658121' || bankCardNum == '524336015' || bankCardNum == '553431005'){
                        this.bankCardNoflage = true
                    }else{
                        Toast({message:'请正确输入信用卡号'});
                        this.bankCardNoflage = false
                    }
                    // this.bankCardNoflage = true
                }
            }else{
               this.bankCardNoflage = false
            }
            this.changeaddCardBtn()
            console.log(this.bankCardNoflage)
      },

      //银行卡CVC
      cheangebankCardCVC:function(val){
          const bankCardCVC = /^[0-9]{3}$/;
           if(val && val.replace(/ /g, "")){
            if(!bankCardCVC.test(val.replace(/ /g, ""))){
                Toast({message:'请正确输入信用卡背面后三位'})
                    this.bankCardCVCflage = false;
                } else{
                    this.bankCardCVCflage = true;
                }
           }else{
              this.bankCardCVCflage = false;
           }
           this.changeaddCardBtn()
           console.log(this.bankCardCVCflage)
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

      //有效期
      cheangebankCardDate:function(val){
          const mobile = /^[0-9]{4}$/;
           if(val && val.replace(/ /g, "")){
            if(!mobile.test(val.replace(/ /g, ""))){
                Toast({message:'请正确输入银行卡有效期'})
                    this.bankCardDateflage = false;
            }else{
                this.bankCardDateflage = true
            }
           }else{
               this.bankCardDateflage = false;
           }
           this.changeaddCardBtn()
           console.log(this.bankCardDateflage)
      },
      //验证码
      cheangesms:function(val){
          const smsCode = /^[0-9]{4,6}$/;
           if(val && val.replace(/ /g, "")){
            if(!smsCode.test(val.replace(/ /g, ""))){
                Toast({message:'请输入的验证码'})
                    this.yzmflage =  false
            }else{
                this.yzmflage =  true
            }
           }else{
               this.yzmflage =  false;
           }
           this.changeaddCardBtn()
           console.log(this.yzmflage)
      },
        //倒计时 
        getAuthCode: function () {
            if(this.sendAuthCode){
                this.auth_time = 30
                var auth_timetimer = setInterval(() => {
                this.auth_time--
                if (this.auth_time <= 0) {
                this.sendAuthCode = false;
                clearInterval(auth_timetimer);
                }
            }, 1000)
            this.sendAuthCode = true
            }
        },

    },
}
</script>