<template>
	<div class="bookingTabCon">
		<div class="tab">
			<ul class="tab-header">
				<li v-for="tab in tabList" :class="[tabIndex == tab.index ? 'active' : '']"  @click="changeTab(tab)">{{tab.name}} <i>{{couponsize[tab.index]}}</i></li>
			</ul>
			<div class="tab-body">
				<slot name="tabcontent"></slot>
			</div>
		</div>
	</div>
</template>

<script type="text/javascript">
	export default {
		props: {
			tabList: Array,
			tabIndex: {
				type: Number,
				default: 0
			},
			couponsize:Array
		},
		data() {
			return {
				
			}
		},
		methods: {
			changeTab(tab) {
				this.$emit('changeTab', tab)
			}
		}
	}
</script>