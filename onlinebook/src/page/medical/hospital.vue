<template>
    <div class="hospitalCon" style="position: relative;">
        <Topheader :headtitle="headtitle" v-if="headerBarflage"></Topheader>
        <div class="hospitalSeachBox">
            <div class="searchMsg">
                <div class="search_city" @click="showcityFun(false)">{{thecity}}<span class="pulldown"></span></div>
                <div class="search_date" @click="searchhospitaltype">
                    <div><p>{{hospitaltype}}</p></div>
                    <div><span class="pulldown"></span></div>
                </div>
                <div class="search_hotel"><span class="searchicon"></span><input type="text" name="" v-model="buffetname" ref="buffetname" @blur="getProductList" @keypress="getProductList"  placeholder="请输入关键字" id="" class="searchinput"></div>
            </div>
        </div>
        <div class="hospitalMsgBox" @click="goshopping(index)"  v-for="(item,index) in articles_array">
            <div class="hospitalName">{{item.name}}</div>
            <div class="hospitalType">{{item.grade}} {{item.hospitalType}}</div>
            <div class="hospitalAddress">{{item.province}} {{item.city}}</div>
        </div>
        <div v-if="articles_array.length == 0" style="color:#999; padding:1rem 0; text-align:center;">
            暂无数据
        </div>
        
        <div class="maskshow" v-if="typeflag" @click="surehospitaltype(currontype)">
            <div class="hotspitaltypeBox">
                <div v-for="(item,index) in hospitaltypeList" :class="[currontype == index ? 'curron' : '']" @click="surehospitaltype(index)">{{item}}</div>
            </div>
        </div>

    <!-- <p class="page-picker-desc">地址: {{ addressProvince }} {{ addressCity }}</p> -->
      <div class="cistymask" v-if="searchcityshow" @click="showcityFun(true)">
          <div class="msgBox">
              <div class="msgBox_title">选择城市</div>
              <mt-picker :slots="addressSlots" @change="onAddressChange" :visible-item-count="5"></mt-picker>
              <div class="masksureBtnBox" @click="showcityFun(true)">确定</div>
          </div>
      </div>
        <div class="maskshow" style="top:0" v-if="hospitalPerson">
            <div class="addpersonshow">
                就诊人提交30天后，才能预订就医服务!
                <div class="addpersonBtn" @click="addpersonmsg">填写就诊人信息</div>
            </div>
        </div>
    </div>
</template>
<script>
import Vue from 'vue'
import Topheader  from '@/components/head/head';
import backthird from '@/components/bottombar/backthird';
import {getChannge, WXbodyBottomshow} from "@/common/js/common.js";
import { Toast, Indicator,InfiniteScroll } from 'mint-ui';
import { Picker ,Popup } from 'mint-ui'
import { MessageBox } from 'mint-ui';
Vue.use(InfiniteScroll)
Vue.component(Picker.name, Picker)
Vue.component(Popup.name, Popup);
const address = {
    '北京': ['北京'],
    '广东': ['广州', '深圳', '珠海', '汕头', '韶关', '佛山', '江门', '湛江', '茂名', '肇庆', '惠州', '梅州', '汕尾', '河源', '阳江', '清远', '东莞', '中山', '潮州', '揭阳', '云浮'],
    '上海': ['上海'],
    '天津': ['天津'],
    '重庆': ['重庆'],
    '辽宁': ['沈阳', '大连', '鞍山', '抚顺', '本溪', '丹东', '锦州', '营口', '阜新', '辽阳', '盘锦', '铁岭', '朝阳', '葫芦岛'],
    '江苏': ['南京', '苏州', '无锡', '常州', '镇江', '南通', '泰州', '扬州', '盐城', '连云港', '徐州', '淮安', '宿迁'],
    '湖北': ['武汉', '黄石', '十堰', '荆州', '宜昌', '襄樊', '鄂州', '荆门', '孝感', '黄冈', '咸宁', '随州', '恩施土家族苗族自治州', '仙桃', '天门', '潜江', '神农架林区'],
    '四川': ['成都', '自贡', '攀枝花', '泸州', '德阳', '绵阳', '广元', '遂宁', '内江', '乐山', '南充', '眉山', '宜宾', '广安', '达州', '雅安', '巴中', '资阳', '阿坝藏族羌族自治州', '甘孜藏族自治州', '凉山彝族自治州'],
    '陕西': ['西安', '铜川', '宝鸡', '咸阳', '渭南', '延安', '汉中', '榆林', '安康', '商洛'],
    '河北': ['石家庄', '唐山', '秦皇岛', '邯郸', '邢台', '保定', '张家口', '承德', '沧州', '廊坊', '衡水'],
    '山西': ['太原', '大同', '阳泉', '长治', '晋城', '朔州', '晋中', '运城', '忻州', '临汾', '吕梁'],
    '河南': ['郑州', '开封', '洛阳', '平顶山', '安阳', '鹤壁', '新乡', '焦作', '濮阳', '许昌', '漯河', '三门峡', '南阳', '商丘', '信阳', '周口', '驻马店'],
    '吉林': ['长春', '吉林', '四平', '辽源', '通化', '白山', '松原', '白城', '延边朝鲜族自治州'],
    '黑龙江': ['哈尔滨', '齐齐哈尔', '鹤岗', '双鸭山', '鸡西', '大庆', '伊春', '牡丹江', '佳木斯', '七台河', '黑河', '绥化', '大兴安岭地区'],
    '内蒙古': ['呼和浩特', '包头', '乌海', '赤峰', '通辽', '鄂尔多斯', '呼伦贝尔', '巴彦淖尔', '乌兰察布', '锡林郭勒盟', '兴安盟', '阿拉善盟'],
    '山东': ['济南', '青岛', '淄博', '枣庄', '东营', '烟台', '潍坊', '济宁', '泰安', '威海', '日照', '莱芜', '临沂', '德州', '聊城', '滨州', '菏泽'],
    '安徽': ['合肥', '芜湖', '蚌埠', '淮南', '马鞍山', '淮北', '铜陵', '安庆', '黄山', '滁州', '阜阳', '宿州', '巢湖', '六安', '亳州', '池州', '宣城'],
    '浙江': ['杭州', '宁波', '温州', '嘉兴', '湖州', '绍兴', '金华', '衢州', '舟山', '台州', '丽水'],
    '福建': ['福州', '厦门', '莆田', '三明', '泉州', '漳州', '南平', '龙岩', '宁德'],
    '湖南': ['长沙', '株洲', '湘潭', '衡阳', '邵阳', '岳阳', '常德', '张家界', '益阳', '郴州', '永州', '怀化', '娄底', '湘西土家族苗族自治州'],
    '广西': ['南宁', '柳州', '桂林', '梧州', '北海', '防城港', '钦州', '贵港', '玉林', '百色', '贺州', '河池', '来宾', '崇左'],
    '江西': ['南昌', '景德镇', '萍乡', '九江', '新余', '鹰潭', '赣州', '吉安', '宜春', '抚州', '上饶'],
    '贵州': ['贵阳', '六盘水', '遵义', '安顺', '铜仁地区', '毕节地区', '黔西南布依族苗族自治州', '黔东南苗族侗族自治州', '黔南布依族苗族自治州'],
    '云南': ['昆明', '曲靖', '玉溪', '保山', '昭通', '丽江', '普洱', '临沧', '德宏傣族景颇族自治州', '怒江傈僳族自治州', '迪庆藏族自治州', '大理白族自治州', '楚雄彝族自治州', '红河哈尼族彝族自治州', '文山壮族苗族自治州', '西双版纳傣族自治州'],
    '西藏': ['拉萨', '那曲地区', '昌都地区', '林芝地区', '山南地区', '日喀则地区', '阿里地区'],
    '海南': ['海口', '三亚', '五指山', '琼海', '儋州', '文昌', '万宁', '东方', '澄迈县', '定安县', '屯昌县', '临高县', '白沙黎族自治县', '昌江黎族自治县', '乐东黎族自治县', '陵水黎族自治县', '保亭黎族苗族自治县', '琼中黎族苗族自治县'],
    '甘肃': ['兰州', '嘉峪关', '金昌', '白银', '天水', '武威', '酒泉', '张掖', '庆阳', '平凉', '定西', '陇南', '临夏回族自治州', '甘南藏族自治州'],
    '宁夏': ['银川', '石嘴山', '吴忠', '固原', '中卫'],
    '青海': ['西宁', '海东地区', '海北藏族自治州', '海南藏族自治州', '黄南藏族自治州', '果洛藏族自治州', '玉树藏族自治州', '海西蒙古族藏族自治州'],
    '新疆': ['乌鲁木齐', '克拉玛依', '吐鲁番地区', '哈密地区', '和田地区', '阿克苏地区', '喀什地区', '克孜勒苏柯尔克孜自治州', '巴音郭楞蒙古自治州', '昌吉回族自治州', '博尔塔拉蒙古自治州', '石河子', '阿拉尔', '图木舒克', '五家渠', '伊犁哈萨克自治州'],
    '香港': ['香港'],
    '澳门': ['澳门'],
    '台湾': ['台北市', '高雄市', '台北县', '桃园县', '新竹县', '苗栗县', '台中县', '彰化县', '南投县', '云林县', '嘉义县', '台南县', '高雄县', '屏东县', '宜兰县', '花莲县', '台东县', '澎湖县', '基隆市', '新竹市', '台中市', '嘉义市', '台南市']
  };
export default {
    name:'hospital',
    data() {
        return {
            headerBarflage: true,
            headtitle: "医院列表",
            thecity: '选择城市',
            buffetname:'',
            articles_array:[],
            hospitaltype:'全部医院',
            hospitaltypeList:['全部医院','传染医院','儿童医院','妇产医院','骨科医院','精神医院','口腔医院','中医医院','肿瘤医院','综合医院','其他医院'],
            currontype:0,
            typeflag:false,
            hospitalPerson:false,
            addressSlots: [
          {
            flex: 1,
            values: Object.keys(address),
            className: 'slot1',
            textAlign: 'center'
          }, {
            divider: true,
            content: '-',
            className: 'slot2'
          }, {
            flex: 1,
            values: ['北京'],
            className: 'slot3',
            textAlign: 'center'
          }
        ],
        addressProvince: '全部',
        addressCity: '全国',
        searchcityshow: false,
        }
    },
    created(){
        WXbodyBottomshow();
        if (WXbodyBottomshow()) {
        this.headerBarflage = false;
        } else {
        this.headerBarflage = true;
        }
        this.buffetname = this.$store.state.buffetKeyWord
        this.getProductList()
        this.surehospitaltype(this.currontype)
    },
    components: {
        Topheader,
        backthird,
    },
    methods:{
        showcityFun:function(flage){
            if(flage){
                this.searchcityshow = false
                this.items()
            }else{
                this.searchcityshow = true
            }
            this.thecity = this.addressCity
        },
        onAddressChange(picker, values) {
            picker.setSlotValues(1, address[values[0]]);
            this.addressProvince = values[0];
            this.addressCity = values[1];
           
        },
        goshopping:function(id){
            // this.axios({method: 'post', url: '/member/family/list',data:{"id":this.$store.state.userMsg.mbid}})
            // .then(res => {
            //     if(res.data.code == 200 || res.data.code == 400){
            //         Toast({
            //           message: res.data.msg
            //         });
            //     } else {
            //         if(res.data.result.length != 0){
            //             this.$store.commit('getHospitalMsg',this.articles_array[id])
            //             this.$router.push({name:'shopping'})
            //         }else{
            //            this.hospitalPerson = true
            //         }
            //     }
            // })
            // .catch(error => {
            //     console.log(error)
            // })
            this.$store.commit('getHospitalMsg',this.articles_array[id])
            this.$router.push({name:'shopping'})
            
        },
        addpersonmsg:function(){
            this.$router.push({name:'medicalperson'})
        },
        searchhospitaltype:function(){
            this.typeflag = true
            
            // document.body.addEventListener('touchmove', function(e){
            //     e.preventDefault();
            // }, { passive: false });
        },
        surehospitaltype:function(id){
             this.typeflag = false
             this.currontype = id
             this.hospitaltype = this.hospitaltypeList[id]
             this.items()
            // document.body.addEventListener('touchmove', function (e) {
            //     e.returnValue = true;
            // }, {passive: false})
        },
        // 获取医院列表
        getProductList:function() {
            this.axios({method: 'post', url: '/mars/sysHospital/getSysHospitalList', data: {}})
            .then(res => {
                // 关闭遮罩
                Indicator.close();
                if(res.data.code == 200){
                    Toast({
                      message: res.data.msg
                    });
                } else {
                    this.productlist = res.data.result;
                    this.$store.commit('getGiftShopList',this.productlist)
                    this.items()
                }
            })
            .catch(error => {
                console.log(error)
            })
        },
        items: function () {
            var articles_array1 = this.productlist;
            var text = '';
                text = this.buffetname;
            // 城市筛选
            if(this.thecity && this.thecity != '选择城市' && this.thecity!='全国') {
                var citysearch = this.thecity;
                citysearch = citysearch.trim().toLowerCase();
                articles_array1 = articles_array1.filter(function(item){
                    if(item.city.toLowerCase().indexOf(citysearch) != -1){
                        return item;
                    }
                })
            }
            //医院类型
            if(this.hospitaltype && this.hospitaltype == '全部医院') {
                this.articles_array = articles_array1;
                return this.articles_array;
            }else{
                var searchString = this.hospitaltype;
                searchString = searchString.trim().toLowerCase();
                articles_array1 = articles_array1.filter(function(item){
                    if(item.hospitalType.toLowerCase().indexOf(searchString) != -1){
                        return  item;
                    }
                })
            }
            // 文本框筛选
            if (!text){
                //console.log(articles_array1)
                this.articles_array = articles_array1;
                return this.articles_array;
            } else {
                var searchString = text;
                searchString = searchString.trim().toLowerCase();
                articles_array1 = articles_array1.filter(function(item){
                    if(item.name.toLowerCase().indexOf(searchString) != -1){
                        return  item;
                    }
                })
            }
            // // 返回过来后的数组
            this.articles_array = articles_array1;
            return this.articles_array;

        },
    }
}
</script>
<style scoped>
.cistymask{position:fixed; width: 100%; height: 100%; top:0; left: 0; background: rgba(0, 0, 0, .5); z-index: 5}
.cistymask .msgBox{background:#fff; padding:none; position: absolute; left: 0; bottom:0; width: 100%; padding-bottom:.3rem;}
.cistymask .msgBox_title{text-align: center; border-bottom:solid .02rem #f3f3f3; padding:.2rem; margin-bottom: .1rem;}
.cistymask .masksureBtnBox{background: #fe4b4c; color: #fff; padding: .2rem 0; border-radius: 1rem; width: 85%; margin: 0 auto; font-size: .32rem; text-align: center; margin-top: .3rem;}
   .maskshow{position:fixed; width: 100%; height: 100%; top:1.4rem; left: 0; background: rgba(0, 0, 0, .5); z-index: 5}
   .maskshow .hotspitaltypeBox{background:#fff; padding: .2rem .3rem .3rem .3rem; display: flex; justify-content: flex-start; align-items: center; align-content: center; flex-wrap: wrap;}
   .maskshow .hotspitaltypeBox div{width:20.5%; text-align: center; margin: .1rem; padding: .1rem 0; border:solid .01rem #d7d7d7; border-radius: .05rem; box-sizing:content-box;}
   .maskshow .hotspitaltypeBox div.curron{border-color:#f60; color: #f60;}
   .maskshow .addpersonshow{background:#fff; padding: .4rem .3rem .5rem .3rem; position: fixed;  left: 10%; width: 80%; top: 40%; transform: rotateY(-30%); box-sizing: border-box; border-radius: .1rem; color: #c0392b; text-align: center;}
   .maskshow .addpersonshow .addpersonBtn{background: #fe4b4c; color: #fff; margin: .4rem .5rem 0 .5rem; padding: .2rem 0; border-radius: 1rem;}
</style>