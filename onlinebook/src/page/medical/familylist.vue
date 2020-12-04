<template>
    <div class="hospitalCon" style="position: relative;">
        <Topheader :headtitle="headtitle" v-if="headerBarflage"></Topheader>
        <div style="background:#f4f4f4; padding:.3rem 0; text-align:center; color:#999;">已添加{{number1}}人，还可以添加{{3-number1}}人</div>
        <div v-for="(item, index) in familyallList" class="familListBox" style="border-top:solid .08rem #f4f4f4;border-bottom:solid .08rem #f4f4f4;" @click="showfamilyMsg(item.id)">
            <div :class="item.faDefault == 1 ?'curron':''">{{item.name}} <em v-if="item.faDefault == 1" style="display:inline-block; background:#f60; padding:.05rem; border-radius: .1rem; color: #fff; margin-left:.15rem; font-size:.2rem;">默认</em><span v-if="item.medicalInsuranceType == 2" style="font-size:.2rem;">自费</span><span v-else  style="font-size:.2rem;">医保</span><span style="display:block;  padding:.1rem 0; margin:0;">{{item.mobile}}</span></div>
        </div>
        <div style="background:#f4f4f4; padding:.4rem; text-align:center;" v-if="familyallList < 3">
            <div style="background:#fe4b4c; padding:.2rem 0; color:#fff;border-radius: .6rem;" @click="addpersonmsg">添加就诊人</div>
        </div>
    </div>
</template>
<script>
import Vue from 'vue'
import Topheader  from '@/components/head/head';
import backthird from '@/components/bottombar/backthird';
import {getChannge, WXbodyBottomshow} from "@/common/js/common.js";
import { Toast, Indicator,InfiniteScroll } from 'mint-ui';
import { MessageBox } from 'mint-ui';
Vue.use(InfiniteScroll)
export default {
    name:'medicalpersonlist',
    data() {
        return {
            headerBarflage: true,
            headtitle: "就诊人管理",
            familycurron:'',
            familyallList:[],
            number1:0,
        }
    },
    created(){
        WXbodyBottomshow();
        if (WXbodyBottomshow()) {
        this.headerBarflage = false;
        } else {
        this.headerBarflage = true;
        }
        this.familyList()
    },
    components: {
        Topheader,
        backthird,
    },
    methods:{
        familyList:function(id){
            this.axios({method: 'post', url: '/member/family/list',data:{"id":this.$store.state.userMsg.mbid}})
            .then(res => {
                this.familyallList = res.data.result
                this.number1 = this.familyallList.length
            })
            .catch(error => {
                console.log(error)
            })
            
        },
        showfamilyMsg:function(showid){
            this.$router.push({name:'editperson',query:{personid:showid}})
            
        },
        addpersonmsg:function(){
            this.$router.push({name:'medicalperson'})
        },
    }
}
</script>
<style scoped>
  
</style>