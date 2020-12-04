<template>
    <div class="carlist">
        <div class="cardlistBox">
            <div v-if="cardlist!=''">
            <div class="cardlistMsg"  v-for="(item) in cardlist" @click="gotoPay(item.bankCardNo,item.agreementId)">
                <div class="card_bankName">
                    <p>{{item.bankName}}</p>
                    <p>{{item.bankCardType}}</p>
                    <p>{{item.bankCardNo}}</p>
                </div>
                <!-- <div class="default">
                    <span>默认卡</span>
                </div> -->
            </div>
            </div>
            <div class="addcardbtn" @click="gotoAddCard">
                + 添加银行卡
            </div>
        </div>
        <transition name="fade">
            <router-view></router-view>
       </transition>
    </div>
</template>

<script>
import { Toast } from "mint-ui";
import Topheader  from '@/components/head/head';
import backthird from '@/components/bottombar/backthird';
import{WXbodyBottomshow,getChannge} from '@/common/js/common.js'
export default {
    name:'cardlist',
    data() {
        return {
            cardlist: this.$route.query.cardlist?this.$route.query.cardlist:[],
            ordermsg:this.$route.query.ordermsg?this.$route.query.ordermsg:'',
        }
    },
    created(){
        for(var i = 0; i < this.cardlist.length; i++){
            var num = this.fascinatingCardNumber(this.cardlist[i].bankCardNo);
            this.cardlist[i].bankCardNo = num;
        }
    },
    components: {
        Topheader,
        backthird,
    },
    methods: {
        gotoAddCard:function(){
             this.$router.push({name:'bind',query:{cardlist:this.cardlist,ordermsg:this.ordermsg}})
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
                                    curcode: '156',//jsondata.curcode'',//人民币
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
        fascinatingCardNumber(str) {
            return str.replace(/(\d{4})(\d+)(\d{4})/,() => {
                const matches = [RegExp.$1,RegExp.$2,RegExp.$3];
                const [startNum, middleNum, endNum] = matches;
                const group = [];
                const GAP = 4
                for (var i =0; i < middleNum.length; i += GAP){
                    group.push(middleNum.substring(i,i + GAP));
                }
                return startNum + ' ' + group.join(' ').replace(/\d/g,'*') + ' ' + endNum;
            });
        }
    },
}
</script>