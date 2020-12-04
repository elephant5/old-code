<template>
    <div class="aboutVipBox">
        <Topheader :headtitle="headtitle" v-if="headerBarflage"></Topheader>
        <div class="aboutvipmsg">
            <div class="titleDiv">《客乐芙用户服务协议》</div>
            <h2>一、会籍使用须知</h2>
            <p>1.	持客乐芙超级会员卡可享会员价购买商城商品；</p>
            <p>2.	客乐芙超级会员卡仅供注册本人使用，不得转让、出借、或以任何方式（如图片、截屏、视频等方式）分享或允许非注册人在对应实体店购物使用；</p>
            <p>3.	客乐芙超级会员卡如果用户需要更换会员手机号，请拨打热线400联系客服人员处理，原则上我司将在24小时之内完成会员手机号更换；</p>
            <p>4.	一旦出现客乐芙超级会员账号违规被多人使用绑定的情况，客乐芙平台有权冻结会员卡的使用资格；</p>

            <h2>二、会籍分类使用规则及费用说明</h2>
            <p>首次领取会员：根据渠道首次用户激活权益，即可获得为其365天的会员有效期；</p>
            <p>再次购买会员：再次购买分为季卡、年卡两类，两种卡类型价格有可能随营销或者上下浮动；多次购买VIP权益将在上一次权益时间上自动往后延期对应的时间；客乐芙会员卡一经购买概不退货；</p>

            <h2>三、会籍红包使用说</h2>
            <p>1.	超级会员卡红包按照商品订单金额自动计算最优红包规则，保证用户在最大力度上用完红包金额，红包的使用过程不找零，如若发生退货行为，红包将不予退回；</p>

        </div>
    </div>
</template>

<script>
import Vue from 'vue'
import Topheader  from '@/components/head/head';
import{WXbodyBottomshow,getChannge} from '@/common/js/common.js'
import { Toast} from "mint-ui";
export default {
    name:'aboutvip',
    data() {
        return {
            headtitle:'会员章程',
            headerBarflage:true,
            buffetname:'',
            inputname:''
        }
    },
    created(){
        WXbodyBottomshow()
        if(WXbodyBottomshow()){
            this.headerBarflage =false
        }else{
            this.headerBarflage = true
        }
    },
    components: {
        Topheader,
    },
    methods: {

    },
    
}
</script>
<style scoped>
    .aboutVipBox{ background: #fff;}
    .aboutvipmsg{padding: .2rem .3rem;}
    .aboutvipmsg .titleDiv{text-align: center; padding: .2rem 0; font-size: .3rem; font-weight: bold; color: #333;}
    .aboutvipmsg h2{font-size: .28rem; padding: .1rem 0; color: #666;}
    .aboutvipmsg p{padding: .1rem 0; color: #999; line-height: .4rem;}
</style>