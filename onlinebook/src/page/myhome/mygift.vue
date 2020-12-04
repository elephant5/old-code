<template>
  <div class="home" style="position: absolute; left: 0; top: 0; width:100%; height:100%; z-index:999; background:#fff;">
    <Topheader :headtitle="headtitle" v-if="headerBarflage"></Topheader>
    <div
      class="BusinessHome"
      v-for="(item,index) in giftList"
      :key="item.unitId"
      :class="{
            'used': item.status == 8 ,
            'expired':item.status == 1,
            'refunded':item.status == 5, 
            'invalid':item.status == 6,        
            }"
    >
      <div class="businessImgBox">
        <div class="business_logo">
          <!--<img :src="item.bankLogo" alt="">-->
          <img :src="'https://'+ item.bankLogo" alt />
        </div>

        <div class="bannerImgBox">
          <div class="pictip">图片仅供参考</div>
          <img :src="item.imgUrl" alt width="100%" />
        </div>
      </div>
      <div class="businessMsgBox">
        <div class="business_title">{{item.projectName}}</div>
        <div class="business_rulue">
          <p>{{item.expiryDate==null?"永久有效":item.expiryDate+"到期"}}</p>
          <p class="showrulueDailog" v-if="item.status == 9" @click="showrulueDailog(index)">
            使用细则
            <span></span>
          </p>
        </div>
        <div class="businessProBox" v-if="item.status == 9">
          <div
            class="businessProlist"
            @click="productlistShow(items.groupId,items.sysService,items.surplusTimes,items.surplusFreeTimes,items.minBookDays,items.maxBookDays,item.unitId,item.salesChannelId,item.goodsId,items.cycleCount - items.useCount,items.type)"
            v-for="(items,index) in item.giftList"
            :key="items.title"
          >
            <div class="businesspro_Name">
              <div>
                <span
                  class="businesspro_Icon"
                  :class="{
                        'businesspro_Icon_accom': items.sysService == 'accom',
                        'businesspro_Icon_buffet': items.sysService == 'buffet',
                        'businesspro_Icon_spa': items.sysService == 'spa',
                        'businesspro_Icon_gym': items.sysService == 'gym',
                        'businesspro_Icon_tea': items.sysService == 'tea',
                        'businesspro_Icon_drink': items.sysService == 'drink',
                        'businesspro_Icon_airportshuttle': items.sysService == 'car',
                        'businesspro_Icon_viphall': items.sysService == 'lounge',
                        'businesspro_Icon_medical': items.sysService == 'medical',
                        'businesspro_Icon_gift': items.sysService == null?'':items.sysService.indexOf('_cpn') !=-1,
                        }"
                ></span>
              </div>
              <div class="businesspro_Title">
                {{items.title}}
                <span
                  v-if="!!items.totalTimes && items.type != 'cycle_type'"
                >总计 {{items.totalTimes}} 次, 剩余 {{items.surplusTimes}} 次</span>
                <span v-else-if="items.type == 'cycle_type'">
                  总计 {{items.totalTimes}} 次, 剩余 {{items.surplusTimes}} 次
                  <br />
                  本期剩余可用 {{items.cycleCount - items.useCount}} 次 （有效期 {{items.endTime}} 截止）
                </span>
                <span v-else>无限制</span>
              </div>
            </div>
            <div>
              <span
                :class="items.surplusTimes == '0' || (items.cycleCount - items.useCount == 0 && items.type == 'cycle_type') ? 'btn_yuding_y' :'btn_yuding'"
              >
                <i v-if="items.surplusTimes != '0' && items.type != 'cycle_type'">
                  <i v-if="items.sysService == null? '':items.sysService.indexOf('_cpn') ===-1">预订</i>
                  <i v-else>兑换</i>
                </i>

                <i
                  v-else-if="(items.cycleCount - items.useCount != 0) && items.type == 'cycle_type'"
                >
                  <i v-if="items.sysService == null? '':items.sysService.indexOf('_cpn') ===-1">预订</i>
                  <i v-else>兑换</i>
                </i>
                <i v-else>已用完</i>
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- <recommend></recommend> -->

    <div class="ruluedailog" v-if="rulueDialogFlag">
      <div class="dailogMsgBox">
        <div class="closeDailog">
          <span @click="closedailogMsgBox"></span>
        </div>
        <div class="rulurTitle">使用细则</div>
        <div class="dailogMsgshow">
          <div
            v-if="giftList[rulurindex].goodsclauseList[0].clauseType == 'total'"
            v-html="giftList[rulurindex].goodsclauseList[0].clause"
          ></div>
        </div>
      </div>
    </div>


    <copyright :copyright="copyright"></copyright>
    <div style="height:1.2rem;"></div>
    <!-- <bottombar v-if="bottomTabflag"></bottombar> -->
    <!-- <backthird v-if="!bottomTabflag"></backthird> -->
  </div>
</template>

<script>
import {mapState} from 'vuex'
import qs from "qs";
import BScroll from "better-scroll";
import recommend from "@/components/recommend/recommend";
import { Toast, Indicator } from "mint-ui";
import { formatDate } from "@/common/js/formatDate.js";
import bottombar from "@/components/bottombar/bottombar";
import copyright from "@/components/copyright/copyright";
import {getChannge, WXbodyBottomshow, stopsrc, movesrc, getNewdate} from "@/common/js/common.js";
import backthird from "@/components/bottombar/backthird";
import Topheader from "@/components/head/head";
import { MessageBox } from "mint-ui";

export default {
  name: "mygift",
  data() {
    return {
      selectType: [],
      gifttable: [],
      giftList: [],
      grouptable: [],
      serviceList: "",
      bankLogo: "", //require('../../common/images/bussiness_logo.png')'',
      rulueDialogFlag: false,
      selectTypeDialogFlag: false,
      rulurindex: null,
      bankLogoList: [],
      bottomTabflag: true,
      headtitle: "客乐芙中国",
      headerBarflage: true,
      domainname:location.host.split(".")[1] + "." + location.host.split(".")[2], //一级域名
      copyright: ""
    };
  },
  filters: {
    formatDate(time) {
      var date = new Date(time);
      return formatDate(date, "yyyy-MM-dd");
    }
  },
  components: {
    recommend,
    bottombar,
    backthird,
    Topheader,
    copyright
  },
  created() {
    WXbodyBottomshow();
    if (WXbodyBottomshow()) {
      this.headerBarflage = false;
    } else {
      this.headerBarflage = true;
    }
    //获取渠道
    if(getChannge("channel")){
      this.$store.commit("getChannel", getChannge("channel").toUpperCase());
    }
    // 获取商品ID
    if(getChannge("prjCode")){
      this.$store.commit("getPrjCode", getChannge("prjCode"));
    }
    // 获取激活码
    if(getChannge("actCode")){
      this.$store.commit("getActCode", getChannge("actCode"));
    }
    Indicator.open({
      text: "加载中...",
      spinnerType: "fading-circle"
    });
    this.getLoginInfo();

    if (getChannge("bottomflag")) {
      localStorage.setItem("disablebottom", "show");
    } else {
      localStorage.removeItem("disablebottom");
    }
    if (getChannge("backurl")) {
      this.$store.commit('getBackUrl',window.location.href.split("&backurl=")[1])
      $cookies.set( "THIRDBACKURL", window.location.href.split("&backurl=")[1], "",  "/",  this.domainname );
    } else {
      $cookies.set("THIRDBACKURL", "", "", "/", this.domainname);
    }

    if (localStorage.getItem("disablebottom") && localStorage.getItem("disablebottom") == "show") {
      this.bottomTabflag = false;
    } else {
      this.bottomTabflag = true;
    }
    this.$store.commit('getStartDate',getNewdate())
    this.$store.commit('getEndDate',getNewdate(1))
  },
  mounted() {},
  computed: {
    
  },
  methods: {
    //获取用户信息
    getLoginInfo: function() {
      this.axios({
        method: "post",
        url: "/yangjian/mem/heartBeat",
        headers: {"X-REQUESTED-SO-TOKEN": $cookies.get(localStorage.getItem('CHANNEL') + "_loginToken")}
      })
        .then(res => {
          if (res.data.code == "100") {
            this.$store.commit("getUserInfo", res.data.result);
            this.getIndexInfo(res.data.result.acid);
          } else {
            window.location.href =
              process.env.PASSPORT_ROOT + "/login?channel=" + localStorage.getItem('CHANNEL') +  "&backurl=" +  location.href;
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    // 获取首页接口
    getIndexInfo: function(memberid) {
      this.axios({
        method: "post",
        url: "/mars/goods/index",
        data: {
          memberId: memberid,
        }
      })
        .then(res => {
          // 关闭遮罩
          Indicator.close();
          if (res.data.code == 200) {
            Toast({
              message: res.data.msg
            });
          } else {
                this.giftList = res.data.result;
                  for (var i = 0; this.giftList.length > i; i++) {
                    if (this.giftList[i].status == 4) {
                      this.giftList[i].status = 1;
                    } else if (this.giftList[i].status != 5 && this.giftList[i].status != 6) {
                      var sumcnt = 0;
                      var gitItem = this.giftList[i].giftList;
                      for (var j = 0; gitItem.length > j; j++) {
                        let surplusTimes = gitItem[j].surplusTimes;
                        if (surplusTimes == null) {
                          sumcnt = 1;
                          break;
                        }
                        sumcnt += surplusTimes;
                      }

                      if (sumcnt > 0) {
                        // 未用完为3
                        this.giftList[i].status = 9;
                        //status为3时判断过期时间
                        if ( this.giftList[i].expiryDate !== null && this.giftList[i].expiryDate !== "null") {
                          var a = this.getexpirydate(this.giftList[i].expiryDate);
                          var b = this.getFormatDate();
                          if (a - b < 0) {
                            this.giftList[i].status = 1; //已过期为1
                          }
                        }
                      } else {
                        // 已用完为2
                        this.giftList[i].status = 8;
                      }
                    }
                  }
                  this.giftList.sort(function(a, b) {
                    return b.status - a.status;
                  });
          }
          this.$store.commit("getGiftList", this.giftList)
          // console.log(this.$store.getters.getGiftList)
          if (res.data.result.length == "0") {
            this.$router.push({ name: "mymember", params: {} });
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    productlistShow: function(groupId, sysService, surplusTimes,surplusFreeTimes, minBookDays, maxBookDays, unitId, salesChannelId, goodsId, num, type) {
        this.$store.commit('getUnitId',unitId)
        this.$store.commit('getGoodsId',goodsId)
        this.$store.commit('getGroupId',groupId)
        this.$store.commit('getSysService',sysService)
        this.$store.commit('getSurplusTimes',surplusTimes)
        this.$store.commit('getSurplusFreeTimes',surplusFreeTimes)
        this.$store.commit('getMinBookDays',minBookDays)
        this.$store.commit('getMaxBookDays',maxBookDays)
        this.$store.commit('getSalesChannelId',salesChannelId)
        this.$store.commit('getNum',num)
        this.$store.commit('getType',type)
        if((surplusTimes > 0 || surplusTimes == null) && type != "cycle_type") {
         if (sysService == "drink") {
            this.$router.push({name: "list", params: { sysService: sysService }});
          } else if (sysService.indexOf("_cpn") != -1  || sysService == "car" || sysService == "lounge" || sysService == 'medical' ) {
            if(sysService == "car" || sysService == "lounge" || sysService == 'medical'){
              this.$store.commit('getStartDate',getNewdate(2))
            }
            this.$router.push({name: "coulist", params: { sysService: sysService }});
          } else if(sysService == 'medical'){
              this.$router.push({name: "medicalmsg", params: { sysService: sysService }});
          }else {
            this.$router.push({name: "product", params: { groupId: groupId, sysService: sysService }});
          }
        } else if (type == "cycle_type" && num > 0) {
          if (sysService == "drink") {
            this.$router.push({name: "list",  params: { sysService: sysService }});
          } else if (sysService.indexOf("_cpn") != -1  || sysService == "car" || sysService == "lounge" || sysService == 'medical') {
              if(sysService == "car" || sysService == "lounge" || sysService == 'medical'){
                this.$store.commit('getStartDate',getNewdate(2))
              }
              this.$router.push({name: "coulist", params: { sysService: sysService }});
          } else if(sysService == 'medical'){
              this.$router.push({name: "medicalmsg", params: { sysService: sysService }});
          } else {
            this.$router.push({name: "product", params: { groupId: groupId, sysService: sysService }});
          }
        }
    },

    showrulueDailog: function(index) {
      this.rulueDialogFlag = true;
      this.rulurindex = index;
      stopsrc();
      document.getElementById("body").style.position = "fixed";
      document.getElementById("body").style.height = "100%";
    },
    bodyScroll: function(event) {
      event.preventDefault();
    },
    closedailogMsgBox: function() {
      this.rulueDialogFlag = false;
      movesrc();
      document.body.addEventListener("touchmove", this.bodyScroll, false);
      document.getElementById("body").style.position = "initial";
      document.getElementById("body").style.height = "auto";
    },
    getexpirydate: function(timeStr1) {
      //日期转换成时间戳
      return new Date(timeStr1).getTime();
    },
    getFormatDate: function() {
      var date = new Date();
      var time = date.getTime();
      // console.log('date',time)
      return time;
    }
  }
};
</script>


