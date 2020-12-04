<template>
    <div class="backthirdcon">
        <span class="defaultBtnbg" v-if="backBtnflag" @click='backthird'>返回</span>
        <span class="ccbBtnbg" v-else @click='backthird'></span>
    </div>
</template>

<script>

export default {
    name:'bottombar',
    data() {
        return {
            backBtnflag:true,
        }
    },
    created(){
        if(this.$store.state.channel == 'CCB'){
            this.backBtnflag = false
        }
    },
    props:[],
    methods: {
        backthird:function(){
            // 清除缓存
            if(this.$store.state.channel == 'ALIPAY'){
                my.navigateTo({
                    url: '/pages/myOrdersCenter/myOrdersCenter'
                })
            }else{
                window.location.href = decodeURIComponent(this.$store.state.backUrl)
            }
            
        },
    },
}
</script>
        