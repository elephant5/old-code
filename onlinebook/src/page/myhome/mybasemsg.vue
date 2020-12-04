<template>
    <div class="myhomeBox_mybasemsg">
        <div class="bodyBox">
            <div class="mybasemsg">
                <div class="typename">头像</div>
                <div class="myphoto">
                        <span><img :src="headPhoto"></span>
                    </div>
            </div>
            <div class="mybasemsg">
                <div class="typename">真实姓名</div>
                <div class="readyname"><input type="text" name="" id="" placeholder="请填写真实姓名" v-model="username"></div>
            </div>
            <div class="mybasemsg">
                <div class="typename">性别</div>
                <div class="sex" @click="selectSex">
                    <span>{{sex}}</span>
                </div>
            </div>
            <div class="mybasemsg">
                <div class="typename">证件类型</div>
                <div class="credentials" @click="selectZjz">
                     <span>{{zjz}}</span>
                </div>
            </div>
            <div class="mybasemsg">
                <div class="typename">证件号</div>
                <div class="credentialsNum">
                    <input type="text" name="" id="" placeholder="选填" v-model="idCard">
                    </div>
            </div>
            <div class="mybasemsg">
                <div class="typename">手机号</div>
                <div class="mobile"><input type="text" name="" id="" placeholder="请输入正确的手机号" v-model="tel"></div>
            </div>
          </div>

        <div class="logoutBtn" @click="submitMsg">确定</div>

        <div class="personSexbox" v-if="selectsexflag"  @click="sureselectSex">
            <div class="Zjzboxmsgshow">
                <div class="closeDailogBox">选择证件类型<span @click="sureselectZjz"></span></div>
                <div class="page-picker-wrapper">
                    <mt-picker :slots="numberSlot" @change="onSexChange" :visible-item-count="3"></mt-picker>
                    <div class="selectbtn" @click="sureselectSex">确定</div>
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
import Vue from 'vue'
import qs from 'qs'
import { Picker ,Toast } from 'mint-ui'
import { futimesSync } from 'fs';
Vue.component(Picker.name, Picker)
export default {
    name:'mybasemsg',
    data() {
        return {
            selectsexflag:false,
            selectZjzflag:false,
            numberSlot: [{
                flex: 1,
                defaultIndex: 0,
                values: ['男','女','保密'],
                className: 'slot1'
            }],
            numberSlot2: [{
                flex: 1,
                defaultIndex: 0,
                values: ['身份证','护照'],
                className: 'slot2'
            }],
            sex:null,
            zjz:'身份证',
            headPhoto: null,
            username: null,
            tel: null,
            idCard: null,
            userinfo: null,
            userfull: null,
        }
    },
    components:{
        
    },
    props:[],
    methods:{
        selectSex:function(){
            this.selectsexflag = true
        },
        selectZjz:function(){
            this.selectZjzflag = true
        },
        onSexChange(picker, values) {
            this.sex = values[0];
        }, 
        sureselectSex:function(){
            this.selectsexflag = false
        },
        onZjzChange(picker, values) {
            this.zjz = values[0];
        }, 
        sureselectZjz:function(){
            this.selectZjzflag = false
            if(this.zjz=='身份证'){
                this.idCard = this.userinfo.idNumber
            } else if(this.zjz=='护照'){
                this.idCard = this.userinfo.passportNum
            } 
        },
        //获取用户信息
        getLoginInfo:function(){
            this.axios({
                method: 'post',
                url: '/yangjian/mem/heartBeat',
            })
            .then(res => {
                if(res.data.code == "100"){
                    this.userinfo = res.data.result;
                    this.headPhoto = this.userinfo.headImgUrl
                    //手机号
                    this.tel = this.userinfo.mobile;
                    //昵称
                    this.username = this.userinfo.mbName;
                    //性别
                    if(this.userinfo.sex==1){
                        this.sex = '男' ;
                    } else if(this.userinfo.sex==2){
                        this.sex = '女' ;
                    } else{
                        this.sex = '保密';
                    }
                    //证件号码
                    this.idCard = this.userinfo.idNumber

                }else{
                    window.location.href = process.env.PASSPORT_ROOT+"/login?channel="+ localStorage.getItem('CHANNEL') + "&backurl="+ location.href  
                }
            })
            .catch(error => {
                console.log(error)
            })
        }, 
        submitMsg: function(){
            var passportNum = null;
            var idNumber = null;
            var upsex = null;
            // 姓名
            if(!this.username){
                Toast({
                    message: '请填写姓名'
                });
                return;
            }
            // 性别
            if(!this.sex){
                Toast({
                    message: '请填写性别'
                });
                return;
            }
            // 证件号码
            if(!this.idCard) {
                Toast({
                    message: '请填写证件号码'
                });
                return;
            }
            // 身份证号码
            if(this.zjz=='身份证'){
                idNumber = this.idCard
            } else if(this.zjz=='护照'){
                passportNum = this.idCard
            } 
            // 性别
            if(this.sex == '男'){
                 upsex = 1
            } else if(this.sex=='女'){
                 upsex = 2
            } 
            this.axios({
                method: 'post',
                url: '/yangjian/mem/updateMemberInfo',
                data: {
                    "mbName": this.username,
                    "idNumber": idNumber,
                    "sex": upsex,
                    "passportNum": passportNum
                }
            })
            .then(res => {
                if(res.data.code == "100"){
                    this.$router.push({name:'percenter'})
                } else {
                    Toast({
                        message: res.data.msg
                    }); 
                }
            })
            .catch(error => {
                console.log(error)
            })
        }
    },
    created(){
        this.getLoginInfo();
    }
}
</script>
        
        
