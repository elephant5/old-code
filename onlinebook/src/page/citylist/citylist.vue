<template>
    <div class="citylist">
        <div class="fix-top">
           <!--  <div class="tit">
            请选择城市
            </div>
            <div class="search">
                <input type="text" v-model="inputcity"  class="search-input" placeholder="输入城市">
            </div>
            <div class="tip">
                覆盖全国<span style="font-size:0.3rem;color:#ff8c00">2000</span>多个目的地
            </div>   -->
            <div class="vistCity">
              <div><span class="vistcityName">上海</span><span class="vistcityTip">当前定位城市</span></div>
              <div class="allcity"><span @click="chooseCity('全国')">全国</span></div>
            </div>
            <div class="selectCitytitle">热门城市</div>
            <div class="hotCityContent">
                <span @click="chooseCity('上海')">上海</span><span @click="chooseCity('北京')">北京</span><span @click="chooseCity('广州')">广州</span><span @click="chooseCity('深圳')">深圳</span>
            </div>
            <div class="hotCityContent">
                <span @click="chooseCity('三亚')">三亚</span><span @click="chooseCity('杭州')">杭州</span><span @click="chooseCity('厦门')">厦门</span><span @click="chooseCity('青岛')">青岛</span>
            </div>
        </div>
        
        <div class="list">
          <div class="selectCitytitle">搜索城市</div>
            <div v-for="o in letterlist" v-bind:key="o" v-bind:id="o">
                <div class="list-letter">
                    {{ o }}
                </div>
                <div class="list-city">
                    <ul>
                        <li @click="chooseCity(cityname.name)" v-for="(cityname,index) in lettercity[o] " v-bind:key="index">
                            {{cityname.name}}
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="search-list" v-if="showsearchlist" >
            <div @click="chooseCity(s.name)" v-for="(s,i) in searchresult" v-bind:key="i" v-html="showsearchReasult(s.name)">
            </div>
        </div>
        <div class="letter-nav" v-if="!showsearchlist" >
            <div v-for="o in letterlist" v-bind:key="o+'-nav'" @click="letterNav(o)" >
                {{ o }}
            </div>
        </div>
    </div>
</template>
<script>
//import city from "@/common/js/city.json";
import BScroll from 'better-scroll'
export default {
  name: "CityList",
  data() {
    return {
      citylist: [],
      letterlist: [],
      lettercity: {},
      inputcity: "",
      showsearchlist: false,
      searchresult: []
    };
  },
  created() {
    this.initCitylist();
  },
  watch: {
    inputcity: function() {
      //监听inputcity的值
      if (this.inputcity == "") {
        this.showsearchlist = false;
      } else {
        this.showsearchlist = true;
        this.searchresult = this.cityFilter(this.inputcity);
      }
    }
  },
  methods: {
    // buildLetter() {
    //   // 构建字母项
    //   for (let i = 0; i < 26; i++) {
    //     let o = {};
    //     let letter = String.fromCharCode(65 + i);
    //     o[letter] = [];
    //     this.letterlist.push(letter);
    //     this.lettercity[letter] = [];
    //   }
    // },
    buildCity(cityNamesFilter) {
      // 按拼音构建构建城市
      for (let i = 0; i < this.citylist.length; i++) {
        let firstLetter = this.citylist[i].pinyin.substr(0, 1);
        this.lettercity[firstLetter].push(this.citylist[i]);
      }
    },
    letterNav(letter) {
      document.getElementById(letter).scrollIntoView();
    },
    cityFilter(city) {
      // 城市搜索筛选
      let citynamelist = [];
      for (let i = 0; i < this.citylist.length; i++) {
        if (this.citylist[i].name.indexOf(city) != -1) {
          citynamelist.push(this.citylist[i]);
        }
      }
      return citynamelist;
    },
    showsearchReasult(n) {
      let reg = new RegExp(this.inputcity, "g");
      let pretxt = n;
      let newtxt = pretxt.replace(
        reg,
        '<span style="color: #ff6026">' + this.inputcity + "</span>"
      );
      return newtxt;
    },
    chooseCity(c) {
      this.inputcity = c;
      this.$store.commit("cityChoosed", c);
      this.$router.back();
    },
    initCitylist() {
      this.axios({
          method: 'post',
          url: '/mars/syscity/getCityList',
      })
      .then(res => {
          this.citylist = res.data.result
          this.newBuildLetter();
          this.buildCity();
      })
      .catch(error => {
          console.log(error)
      })
    },
    newBuildLetter(){
      var map = '';
      // 按拼音构建构建城市
      for (let i = 0; i < this.citylist.length; i++) {
        let firstLetter = this.citylist[i].pinyin.substr(0, 1);
        if(map.indexOf(firstLetter)==-1){
          map += firstLetter+','
        }
      }
      // 构建字母项
      for (let i = 0; i < 26; i++) {
        let letter = String.fromCharCode(65 + i);
        if(map.indexOf(letter)>-1){
          this.letterlist.push(letter);
          this.lettercity[letter] = [];
        }
      }
    }
  }
};
</script>