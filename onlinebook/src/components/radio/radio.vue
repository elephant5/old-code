<template>
    <div>
        <div class="page-picker-wrapper">
            <mt-picker :slots="numberSlot" @change="onNumberChange" :visible-item-count="3"></mt-picker>
        </div>
    </div>
</template>

<script>
import { Picker } from 'mint-ui'
Vue.component(Picker.name, Picker)
export default {
    name:'radio',
    data() {
        return {
           
        }
    },
    components: {
    },
    created() {
    },
    props:['routersysService'],
    methods: {
       
    },
}
</script>

<style lang="scss">

</style>


