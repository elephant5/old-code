<template>
    <div>
        <Topheader :headtitle="headtitle" v-if="headerBarflage"></Topheader>
        <div class="medicalmsgBox">
            <img src="../../common/images/medical/index_01.jpg" alt="">
            <img src="../../common/images/medical/index_02.jpg" alt="">
            <img src="../../common/images/medical/index_03.jpg" alt="">
            <img src="../../common/images/medical/index_04.png" alt="">
            <div style="height:1.2rem;"></div>
        </div>
        <div class="buttonBox">
            <div class="button_buy" @click="gohospital">立即预约</div>
        </div>
        <!-- <div class="maskshow">
            <div class="addperson">
                <div class="logoBox"><img src="../../common/images/logo.png" alt=""></div>
                <div class="addpersonMsgBox" style="margin-top:.4rem;">
                    <span><img src="../../common/images/medical/act_name.png" alt=""></span>
                    <input placeholder="请输入真实姓名">
                </div>
                <div class="addpersonMsgBox">
                    <span><img src="../../common/images/medical/act_mobile.png" alt=""></span>
                    <input placeholder="请输入手机号码">
                </div>
                <div class="addpersonMsgBox">
                    <span><img src="../../common/images/medical/act_number.png" alt=""></span>
                    <input placeholder="请输入验证码">
                </div>
                <div class="addpersonTipBox">
                    <div class="medicalBtn">快人一步备案权益人信息</div>
                    
                   权益人备案30天后才能预订<span style="color:#f60;">《就医服务》</span>！<br>备案即表示您已同意     
                </div>
            </div>
        </div> -->
    </div>
</template>
<script>
import { Toast } from "mint-ui";
import Topheader  from '@/components/head/head';
import backthird from '@/components/bottombar/backthird';
import {getChannge, WXbodyBottomshow} from "@/common/js/common.js";
export default {
    name:'medicalmsg',
    data() {
        return {
            headerBarflage: true,
            headtitle: "医疗简介",
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
    methods:{
        gohospital:function(){
            this.$router.push({name: "shopping"});
        },
    }
}
</script>
<style scoped>
    .medicalmsgBox{padding: 0;}
    .medicalmsgBox img{width: 100%; display: block; margin: 0; padding: 0;}
    .maskshow{background: rgba(0, 0, 0, .5);position: fixed; width: 100%; height: 100%; left: 0; top: 0; z-index: 1;}
    .maskshow .addperson{position: fixed; width: 80%; left: 10%; top: 50%; transform: translateY(-50%); background: #fff; border-radius: .1rem; padding: .3rem; box-sizing:border-box;}
    .maskshow .addperson .logoBox{position: fixed; width: 1.6rem; height: 1.6rem;  background: #fff; border-radius: .8rem; left: 50%; top: -.8rem; margin-left: -.8rem; overflow: hidden; text-align: center; box-sizing: border-box;}
    .maskshow .addperson .logoBox img{width: 60%; display: block;margin: 0 auto; margin-top: .2rem;}
    .maskshow .addperson .addpersonMsgBox{padding:.3rem .1rem; border-bottom: solid .01rem #e8e8e8; display: flex; justify-content:flex-start; align-content: center; align-items: center;}
    .maskshow .addperson .addpersonMsgBox span{width:.4rem; height: .4rem; padding-right: .1rem;}
    .maskshow .addperson .addpersonMsgBox span img{width: 100%;}
    .maskshow .addperson .addpersonMsgBox:last-child{margin-bottom: .5rem;}
    
    .maskshow .addperson .addpersonTipBox{text-align: center; padding-top: .3rem; color: #999; line-height: .4rem;}
    .maskshow .addperson .addpersonTipBox .medicalBtn{width:90%; margin: .2rem auto; background: #f60; color: #fff; height: .8rem; line-height: .8rem; border-radius: .1rem;}
</style>