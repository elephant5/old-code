<template>
    <div class="header">
       <span class="icon_bank" @click="gobank">&nbsp;</span>{{headtitle}}
    </div>
</template>

<script>
export default {
    name:'topheader',
    data() {
        return {

        }
    },
    props:['headtitle'],
    components:{
        
    },
    created() {
        
    },
    methods: {
        gobank:function(){
            window.history.go(-1)
        }
    },
}
</script>


