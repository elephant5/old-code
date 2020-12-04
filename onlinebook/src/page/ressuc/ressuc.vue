<template>
    <div class="shoppingStatuc">
        <!-- <Topheader :headtitle="butTitle" v-if="headerBarflage"></Topheader> -->
        <div class="resBox" v-if="payStatus == 'fail'">
            <span class="failspan">&nbsp;</span>
            <p class="suctip">支付失败</p>
            <div class="oterlinkBox">
                <div @click='gotopay' >继续支付</div>
                <div @click='gogoorderlist' v-if="memberOrdershowFlag">查看详情</div>
            </div>
        </div>
        <div class="resBox" v-else>
            <span class="sucspan">&nbsp;</span>
            <p class="suctip">{{butTitle}}</p>
            <div class="oterlinkBox">
                <div @click='gotoback' >继续行权</div>
                <div @click='gogoorderlist' v-if="memberOrdershowFlag">查看详情</div>
            </div>
        </div>

        <!-- <div class="othercoupon">
            <div class="recommendTitle">
                <span>&nbsp;</span>恭喜获得<span>&nbsp;</span>
            </div>
            <ul class="couponlistDiv">
                <li>
                <div class="couponlistMsg">
                    <div >
                    <div class="couponlistShow">
                        <p class="couponlist_Name">5折咖啡券</p>
                        <p class="couponlist_Date">有效期: 2019/03/12-2019/03/18</p>
                    </div>
                    </div>
                    <div class="couponType"><div><i>5</i>折</div><span>最高减15元</span></div>
                </div>
                <div class="couponlistType">
                    <div>仅限咖啡券类商品</div>
                    <div class="prolink">查看可用商品</div>
                </div>
                </li>
            </ul>
        </div> -->

        <!-- <div class="recommendBox">
            <div class="recommendTitle">
                <span>&nbsp;</span>猜你喜欢<span>&nbsp;</span>
            </div>
            <div class="recommendProBox">
                <div class="recommendProMsg">
                    <div class="recommendProImg">
                        <img src="../../common/images/pic.jpg" alt="">
                    </div>
                    <div class="recommendProName">
                    下午茶超值套餐—下午茶双人免费1次+两人…
                    </div>
                    <div class="recommendProPrice">
                         <span><i>¥</i>599</span><em>¥788</em>
                    </div>
                </div>
                <div class="recommendProMsg">
                    <div class="recommendProImg">
                        <img src="../../common/images/pic.jpg" alt="">
                    </div>
                     <div class="recommendProName">
                    下午茶超值套餐—下午茶双人免费1次+两人…
                    </div>
                    <div class="recommendProPrice">
                        <span><i>¥</i>599</span><em>¥788</em>
                    </div>
                </div>
                <div class="recommendProMsg">
                    <div class="recommendProImg">
                        <img src="../../common/images/pic.jpg" alt="">
                    </div>
                     <div class="recommendProName">
                    下午茶超值套餐—下午茶双人免费1次+两人…
                    </div>
                    <div class="recommendProPrice">
                        <span><i>¥</i>599</span><em>¥788</em>
                    </div>
                </div>
                <div class="recommendProMsg">
                    <div class="recommendProImg">
                        <img src="../../common/images/pic.jpg" alt="">
                    </div>
                     <div class="recommendProName">
                    下午茶超值套餐—下午茶双人免费1次+两人…
                    </div>
                    <div class="recommendProPrice">
                        <span><i>¥</i>599</span><em>¥788</em>
                    </div>
                </div> 

            </div>
        </div> -->

    <backthird v-if='backthirdFlag'></backthird>
    </div>
</template>

<script>
import Topheader  from '@/components/head/head';
import backthird from '@/components/bottombar/backthird';
import{WXbodyBottomshow,getChannge} from '@/common/js/common.js'
export default {
    name:'ressuc',
    data() {
        return {
            reservOrderId:getChannge('reservOrderId'),
            payStatus:'success',
            productGroupId:this.$store.state.groupId?this.$store.state.groupId:'',
            serviceType:this.$store.state.sysService?this.$store.state.sysService:'',
            butTitle:'提交成功',
            headerBarflage:true,
            bottomTabflag:true,
            memberOrdershowFlag:true,
            backthirdFlag:false,//返回第三方按钮
        }
    },
    created(){
        if(getChannge('payStatus')){
            this.payStatus = getChannge('payStatus')
        }
        WXbodyBottomshow()
        if(getChannge('channel')){
            this.$store.commit('getChannel',getChannge('channel').toUpperCase())
        }
        if(localStorage.getItem('CHANNEL') == 'JD'){
            this.memberOrdershowFlag = false
        }
        if(WXbodyBottomshow()){
            this.headerBarflage =false
        }else{
            this.headerBarflage = true
        }
        if(this.serviceType.indexOf('_cpn') != -1){
            this.butTitle = '兑换成功'
        }
        if(this.$store.state.backUrl){
            this.backthirdFlag = true
        }else{
            this.backthirdFlag = false
        }

        // this.getOrdereMsg()
    },
    components: {
        Topheader,
        backthird,
    },
    methods: {
        gotopay:function(){
            window.location.href=decodeURIComponent(getChannge('paybackurl'))
        },
        gotoback:function(){
            if(this.$store.state.backUrl){
                 window.location.href = decodeURIComponent(this.$store.state.backUrl)
            }else{
                //1 PRJCODE和ACTCODE都为空
                if(!this.$store.state.prjCode && !this.$store.state.actCode){
                    window.location.href = '/home?channel='+localStorage.getItem('CHANNEL')
                } 
                //2 PRJCODE 不为空 ACTCODE为空
                else if(this.$store.state.prjCode && !this.$store.state.actCode) {
                    window.location.href = '/home?channel='+localStorage.getItem('CHANNEL')+'&prjCode='+this.$store.state.prjCode
                }
                //3 PRJCODE 为空 ACTCODE不为空
                else if(!this.$store.state.prjCode && this.$store.state.actCode) {
                    window.location.href = '/home?channel='+localStorage.getItem('CHANNEL')+'&actCode='+this.$store.state.actCode
                }
                //4 PRJCODE 不为空 ACTCODE 不为空
                else {
                    window.location.href = '/home?channel='+localStorage.getItem('CHANNEL')+'&prjCode='+this.$store.state.prjCode+'&actCode='+this.$store.state.actCode
                }
                
            }
        },
        gogoorderlist:function(){
            if(this.$store.state.backUrl){
                window.location.href = '/order?reservOrderId='+getChannge('reservOrderId') + '&bottomflag=disablebottom&backurl=' + this.$store.state.backUrl
            } else {
                window.location.href = '/order?reservOrderId='+getChannge('reservOrderId')
            }
        },
       getOrdereMsg:function(){
           this.axios({
            method: 'get',
            url: '/mars/reservOrder/getOrder/' + getChannge('reservOrderId'),
        })
        .then(res => {
            
        })
        .catch(error => {
          console.log(error)
        })
       },
       encodeSearchKey(key) {
            const encodeArr = [{
            code: '%',
            encode: '%25'
            }, {
            code: '?',
            encode: '%3F'
            }, {
            code: '#',
            encode: '%23'
            }, {
            code: '&',
            encode: '%26'
            }, {
            code: '=',
            encode: '%3D'
            }];
            return key.replace(/[%?#&=]/g, ($, index, str) => {
            for (const k of encodeArr) {
                if (k.code === $) {
                return k.encode;
                }
            }
            });
        },

    },
}
</script>