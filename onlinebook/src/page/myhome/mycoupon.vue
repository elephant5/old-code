<template>
    <div class="myhomeBox_mycoupon">
        <tabs :tabList="tabList" :tabIndex="tabIndex" :couponsize="couponsize" @changeTab="changeTab">
            <div class="myorderlist" slot="tabcontent">
                <div class="cpn" v-if="couponsize[0] == 0 && tabIndex == 0">
                    您暂时还没有折扣券
                </div>
                <div class="cpn" v-if="couponsize[1] == 0 && tabIndex == 1">
                    您暂时还没有抵用券
                </div>
                <!-- 折扣券 -->
                <ul class="couponlistDiv" v-if="tabIndex == 0">
                    <li v-for="item in cpnDiscountsInfoList" :key="item">
                       <div class="couponlistMsg">
                            <div >
                            <div class="couponlistShow">
                                <p class="couponlist_Name">{{item.name}}</p>
                                <p class="couponlist_Date">有效期: {{item.validStartTime.split(' ')[0]}} - {{item.experTime.split(' ')[0]}}</p>
                            </div>
                            </div>
                            <div class="couponNum"><p><span>{{item.discountRatio}}折</span></p></div>
                        </div>
                        <div class="couponlistType">
                            <div><p>{{item.upperAmount==0?"无限制":"最高可减"+item.upperAmount+"元"}}</p></div>
                            <!-- <div class="prolink" >查看可用商品&gt;</div> -->
                            <div class="prolink" >{{cpnStatus[item.status]}}</div>
                        </div>
                    </li>
                </ul>

                <!-- 抵用券 -->
                <ul class="couponlistDiv"  v-if="tabIndex == 1">
                    <li v-for="item in cpnVoucherInfoList" :key="item">
                        <div class="couponlistMsg">
                            <div >
                            <div class="couponlistShow">
                                <p class="couponlist_Name">{{item.name}}</p>
                                <p class="couponlist_Date">有效期: {{item.validStartTime.split(' ')[0]}} - {{item.experTime.split(' ')[0]}}</p>
                            </div>
                            </div>
                            <div class="couponNum"><p>￥<span>{{item.worth}}</span></p></div>
                        </div>
                        <div class="couponlistType">
                            <div><p>{{item.floorAmount==0?"无门槛":"满"+item.floorAmount+"元可用"}}</p></div>
                            <!-- <div class="prolink" >查看可用商品&gt;</div> -->
                            <div class="prolink" >{{cpnStatus[item.status]}}</div>
                        </div>
                    </li>
                </ul>
            </div>
            

            </tabs>
    </div>
</template>

<script>
import tabs from '@/components/tab/tab'
import {formateDate } from "@/common/js/common.js";
export default {
    name:'mybasemsg',
    data() {
        return {
            tabIndex: 0,
            tabList:[
                {
                    index : 0,
                    name: "折扣券",
                    type: 2
                },
                {
                    index : 1,
                    name: "抵用券",
                    type: 1
                },
            ],
            cpnDiscountsInfoList:[], //折扣券
            cpnVoucherInfoList:[], //抵用券
            couponsize:[0,0],
            memberInfo:{},
            cpnStatus : [
                '可使用',
                '已使用',
                '已过期',
                '已作废',
                '',
                '已使用'
                ]
        }
    },
    components:{
        tabs
    },
    created(){
        this.myorderlist(1,10,7),
        this.getLoginInfo()
    },
    props:[],
    methods:{
        changeTab:function(tab) {
            this.tabIndex = tab.index
            this.count = 1
                    if(tab.type == "0"){
                        this.myorderlist(1,10,7)
                    }
                    if(tab.type == "1"){
                        this.myorderlist(1,10,2)
                    }
                    if(tab.type == "2"){
                        this.myorderlist(1,10,7)
                    }
        },
        myorderlist:function(){

            this.orderlist = []
        },
        
        //获取用户券列表
        getMemCpnsList:function(){
            this.axios.post('/coupons/common/getMemCpnListByCondition',{
                "acId":this.memberInfo.acid,
                "statusList":[0,1,2,3,5]
            })
            .then(res => {
                if(res.data.code == 100 && res.data.msg == 'ok'){
                    var cvList = []  //筛选出抵用券列表
                    var cdList = []  //筛选出折扣券列表
                    if(res.data.result.cpnVoucherInfoList.length > 0){
                        cvList = res.data.result.cpnVoucherInfoList.filter(function(item){
                            if(Date.parse(formateDate(new Date(item.validStartDate))) <= Date.parse(formateDate(new Date())) && Date.parse(formateDate(new Date())) <= Date.parse(formateDate(new Date(item.experDate))) 
                                || Date.parse(formateDate(new Date())) > Date.parse(formateDate(new Date(item.experDate)))){
                                return item;
                            }
                        });
                    }
                    if(res.data.result.cpnDiscountsInfoList.length > 0){
                        cdList = res.data.result.cpnDiscountsInfoList.filter(function(item){
                            if((Date.parse(item.validStartDate) <= Date.parse(new Date()) && Date.parse(new Date()) <= Date.parse(item.experDate))
                                || Date.parse(new Date()) > Date.parse(item.experDate)){
                                return item;
                            }
                        });
                    }

                    // this.cpnVoucherInfoList = res.data.result.cpnVoucherInfoList//抵用券
                    // this.cpnDiscountsInfoList = res.data.result.cpnDiscountsInfoList //折扣券
                    // this.couponsize = [this.cpnDiscountsInfoList.length,this.cpnVoucherInfoList.length]
                    this.cpnVoucherInfoList = cvList//抵用券
                    this.cpnDiscountsInfoList = cdList //折扣券
                    this.couponsize = [cdList.length,cvList.length]
                }
            })
            .catch(error => {
                console.log(error)
            })
        },
        //获取用户信息
        getLoginInfo: function() {
        this.axios({
            method: "post",
            url: "/yangjian/mem/heartBeat-helife"
        })
            .then(res => {
            this.memberInfo = res.data.result;
            this.getMemCpnsList()
            })
            .catch(error => {
            console.log(error);
            });
        },

        formateDate:function(date, format){
            if (!date) return;
			    if (!format) format = "yyyy/MM/dd HH:mm:ss";
			    switch (typeof date) {
			        case "string":
			            date = new Date(date.replace(/-/g, "/"));
			            break;
			        case "number":
			            date = new Date(date);
			            break;
			    }
			    if (!date instanceof Date) return;
			    var dict = {
			        "yyyy": date.getFullYear(),
			        "M": date.getMonth() + 1,
			        "d": date.getDate(),
			        "H": date.getHours(),
			        "m": date.getMinutes(),
			        "s": date.getSeconds(),
			        "MM": ("" + (date.getMonth() + 101)).substr(1),
			        "dd": ("" + (date.getDate() + 100)).substr(1),
			        "HH": ("" + (date.getHours() + 100)).substr(1),
			        "mm": ("" + (date.getMinutes() + 100)).substr(1),
			        "ss": ("" + (date.getSeconds() + 100)).substr(1)
			    };
			    return format.replace(/(yyyy|MM?|dd?|HH?|ss?|mm?)/g, function () {
			        return dict[arguments[0]];
			    });

      }

    }
}
</script>

<style scoped>
    .cpn{
        text-align: center; background: #fff; padding: 2rem 0; margin-top: .3rem; color: #999;
    }
</style>
        
