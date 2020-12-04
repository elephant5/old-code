<template>
    <div class="carlist">
        <div class="cardlistBox">
            <div class="cardlistMsg">
                <div class="card_bankName">
                    <p>光大银行</p>
                    <p>饿了么联名信用卡</p>
                    <p>6228 **** **** 4371</p>
                </div>
                <div class="default">
                    <!-- <span>默认卡</span> -->
                </div>
            </div>
            <div>
                <div>
                    设为支付默认卡
                    优先使用此卡支付
                </div>
            </div>
            <div class="removebtn" >
                解除绑定
            </div>
        </div>
    </div>
</template>

<script>
import Topheader  from '@/components/head/head';
import backthird from '@/components/bottombar/backthird';
import{WXbodyBottomshow,getChannge} from '@/common/js/common.js'
export default {
    name:'mycard',
    data() {
        return {
            
        }
    },
    created(){
        
    },
    components: {
        Topheader,
        backthird,
    },
    methods: {
        gotoAddCard:function(){
             this.$router.push({name:'bind'})
        }
    },
}
</script>