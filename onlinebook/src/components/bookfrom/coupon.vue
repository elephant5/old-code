<template>
        <div class="shoppingFrombox_coupon">
            <div class="shoppingTop_coupon">
            <div class="shoppingMsgBox">
                <div class="shoppingMsg_Name">{{reservOrderVo.shopItemName}}</div>
                <div class="shoppingMsg_tips" v-if="!!tipText"><span class="shoppingTip"></span>{{tipText}}</div>
            </div>
            </div>
            <div class="shoppingBox_coupon" v-if="sysServicetype !== 'object_cpn'">
                <div class="inputlist" v-if="memberInfo.mbName">
                    <div>使用人</div>
                    <div><span>{{memberInfo.mbName}}</span></div>
                </div>
                <div class="inputlist">
                    <div>手机号</div>
                    <div><span>{{memberInfo.mobile}}</span></div>
                </div>
            </div>

            <div class="shoppingBox_coupon" v-else>
                <div class="inputlist">
                    <div style="width:1.5rem;">收件人</div>
                    <div><input
                        type="text"
                        name="addressEn"
                        placeholder="请填写收件人姓名"
                        v-model="consignee"
                        @focus="consigneeMsg"
                         class="text_input"
                        style="font-size:.31rem;"
                    ></div>
                </div>
                <div class="inputlist">
                    <div style="width:1.5rem;">收件人电话</div>
                    <div><input
                        type="text"
                        name="addressEn"
                        placeholder="请填写收件人电话"
                        v-model="expressPhone"
                        @focus="expressMsg"
                         class="text_input"
                         style="font-size:.31rem;"
                    ></div>
                </div>
                <div class="inputlist">
                    <div style="width:1.5rem;">配送方式</div>
                    <div><span style="font-size:.245rem;">快递发货</span></div>
                </div>
                <div class="inputlist">
                    <div style="width:1.5rem;">收货地址</div>
                    <div><input
                        type="text"
                        name="addressEn"
                        placeholder="请填写收货地址"
                        v-model="expressAddress"
                        class="text_input"
                        style="font-size:.31rem;"
                    ></div>
                </div>
            </div>

            <div class="shoppingBox_coupon" style="display:none;">
                <div class="inputlist">
                    <div>兑换数量</div>
                    <div style="text-align:right;"><span class="cut">&nbsp;</span><input type="tel" name="" value="1" id="" disabled="disabled" class="num_input"><span class="add" >&nbsp;</span></div>
                </div>
            </div>
        </div>
</template>

<script>

export default {
    name:'shoppingaccom',
    data() {
        return {
            expressAddress:'',
            consignee:'',//收件人姓名
            expressPhone:'',//收件人电话
        }
    },
    created(){
        // console.log(this.reservOrderVo)
        // console.log(this.sysServicetype)
    },
    watch:{
        
    },
    mounted(){
      
    },
    props:['memberInfo','reservOrderVo','tipText','valueDate','mealSection','linkedMemeList','linkedNamelist','sysServicetype'],
    methods: {
        consigneeMsg(){
            this.consignee = this.memberInfo.mbName;
        },
        expressMsg(){
            this.expressPhone = this.memberInfo.mobile;
        },
        gotopercenter:function(){
            this.$router.push({name:'percenter'})
        },
        sendMsgToParent:function(){
            this.reservOrderVo.giftName = this.memberInfo.mbName;
            this.reservOrderVo.giftPhone = this.memberInfo.mobile;
            this.reservOrderVo.giftDate  = this.$store.state.startDate.replace(',','-').replace(',','-');
            this.reservOrderVo.giftTime=this.timeshow;
            this.reservOrderVo.consignee = this.consignee;
            this.reservOrderVo.expressPhone = this.expressPhone;
            this.reservOrderVo.expressMode = 1, //配送方式： 0：无需配送 1：快递发货 2：及时送达 3：到店自取
            this.reservOrderVo.expressAddress = this.expressAddress,//配送地址
            this.reservOrderVo.giftPeopleNum=this.newroomNum;
            this.$emit('gopay',this.reservOrderVo);
            return true;
        },
    },
}
</script>
        
