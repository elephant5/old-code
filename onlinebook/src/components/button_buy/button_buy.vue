<template>
    <div class="buttonBox">
        <div  @click="goshopping" :class="buybtnflag? 'button_buy': 'disable_buy'">{{buyBtnTitle}}</div>
    </div>
</template>

<script>

export default {
    name:'button_buy',
    data() {
        return {
            
        }
    },
    props:['goshopping','buyBtnTitle','buybtnflag']
}
</script>


