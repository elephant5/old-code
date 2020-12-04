import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  // linkActiveClass:'',
  // linkExactActiveClass:'',
  // scrollBehavior (to, from, savedPosition) {
  //   if(savedPosition){
  //     return savedPosition
  //   }else{
  //     return {x:0,y:0}
  //   }
  // },
  // parseQuery(query){

  // },
  // stringifyQuery(obj){

  // },
  fallback:true,
  routes: [
    {
      path:'/',
      redirect:'/home'
    },
    {
      path: '/home',
      name: 'home',
      meta:{
        title: '客乐芙中国',
        requireAuth: true, 
      },
      component: () => import('../page/home/home.vue'),
    },
    {
      path: '/product/:groupId/:sysService',
      // props:true,
      name: 'product',
      meta:{
        title: '酒店搜索',
        requireAuth: true, 
      },
      component: () => import('../page/product/product.vue'),
    },
    {
      path: '/list/:sysService',
      props:true,
      name: 'list',
      meta:{
        title: '酒店列表',
        requireAuth: true, 
      },
      component: () => import('../page/productlist/productlist.vue'),
    },
    {
      path: '/viplist/:sysService/:unitId/:groupId/',
      props:true,
      name: 'viplist',
      meta:{
        title: '酒店列表',
        requireAuth: true, 
      },
      component: () => import('../page/productlist/viplist.vue'),
    },
    {
      path: '/coulist/:sysService',
      props:true,
      name: 'coulist',
      meta:{
        title: '产品列表',
        requireAuth: true, 
      },
      component: () => import('../page/productlist/productlist2.vue'),
    },
    {
      path: '/citylist',
      // props:true,
      name: 'citylist',
      meta:{
        title: '热门城市',
        requireAuth: true, 
      },
      component: () => import('../page/citylist/citylist.vue'),
    },
    {
      path: '/productshow/:type',
      name: 'productshow',
      meta:{title: '商户介绍',
      requireAuth: true, 
      },
      component: () => import('../page/productshow/productshow.vue'),
    },
    {
      path: '/couproductshow/:type',
      name: 'couproductshow',
      meta:{title: '产品介绍',
      requireAuth: true, 
      },
      component: () => import('../page/productshow/productshow2.vue'),
    },
    {
      path: '/shopping',
      name: 'shopping',
      meta:{title: '订单填写',
      requireAuth: true, 
      },
      component: () => import('../page/shopping/shopping.vue'),
    },
    {
      // path: '/ressuc/:reservOrderId/:productGroupId/:serviceType',
      path: '/ressuc&reservOrderId=:reservOrderId',
      props:true,
      name: 'ressuc',
      meta:{title: '提交成功',
      requireAuth: true, 
      },
      component: () => import('../page/ressuc/ressuc.vue'),
    },
    {
      path: '/order',
      name: 'order',
      meta:{
        title: '订单详情',
        requireAuth: true, 
      },
      component: () => import('../page/order/order.vue'),
    },
    {
      path: '/percenter',
      name: 'percenter',
      meta:{
        title: '个人中心',
        requireAuth: true, 
      },
      component: () => import('../page/myhome/myhome.vue'),
      children:[
        {
          path: 'myorder/:id',
          name: 'myorder',
          meta:{
            title: '我的订单',
            requireAuth: true, 
          },
          component: () => import('../page/myhome/myorder.vue'),
        },
        {
          path: 'personmsg',
          name: 'personmsg',
          meta:{
            title: '个人信息',
            requireAuth: true, 
          },
          component: () => import('../page/myhome/mybasemsg.vue'),
        },
        {
          path: 'mycoupon',
          meta:{
            title: '我的优惠券',
            requireAuth: true, 
          },
          name: 'mycoupon',
          component: () => import('../page/myhome/mycoupon.vue'),
        },
        {
          path: 'mymember',
          meta:{
            title: '权益激活',
            requireAuth: true, 
          },
          name: 'mymember',
          component: () => import('../page/myhome/active.vue'),
        },
        {
          path: 'mygift',
          meta:{
            title: '我的权益',
            requireAuth: true, 
          },
          name: 'mygift',
          component: () => import('../page/myhome/mygift.vue'),
        }
      ]
    },
    {
      path: '/orderform/:reservOrderId',
      props:true,
      name: 'orderform',
      meta:{title: '订单',
          requireAuth: true, 
      },
      component: () => import('../page/shopping/orderform.vue'),
    },
    {
      path: '/safarishow',
      props:true,
      name: 'safarishow',
      meta:{title: '跳转中',
          // requireAuth: true, 
      },
      component: () => import('../page/shopping/safarishow.vue'),
    },
    {
      path: '/card/:cardlist/:ordermsg',
      name: 'card',
      meta:{title: '银行卡管理',
      requireAuth: true, 
      },
      component: () => import('../page/card/list.vue'),
      children:[
        {
          path: '/bind/:cardlist/:ordermsg',
          name: 'bind',
          meta:{title: '添加银行卡',
          requireAuth: true, 
          },
          component: () => import('../page/card/bind.vue'),
        },
        {
          path: '/mycard',
          name: 'mycard',
          meta:{title: '银行卡管理',
          requireAuth: true, 
          },
          component: () => import('../page/card/mycard.vue'),
        },
      ]
    },
    {
      path: '/vip',
      name: 'vip',
      meta:{title: '超级会员',
      requireAuth: true, 
      },
      component: () => import('../page/vip/index.vue'),
      children:[
        {
          path: 'viplist',
          name: 'viplist',
          meta:{title: '列表',
          requireAuth: true, 
          },
          component: () => import('../page/vip/list.vue'),
        },
        
      ]
    },
    {
      path: '/shop/:productGroupProductId/:giftCodeId',
      name: 'shop',
      meta:{title: '商户介绍',
      requireAuth: true, 
      },
      component: () => import('../page/vip/shop.vue'),
    },
    {
      path: '/aboutvip',
      name: 'aboutvip',
      meta:{title: '会员章程',
      requireAuth: true, 
      },
      component: () => import('../page/vip/aboutvip.vue'),
    },
    {
      path: '/medicalperson',
      name: 'medicalperson',
      meta:{title: '添加就诊人',
      requireAuth: true, 
      },
      component: () => import('../page/medical/addmedical.vue'),
    },
    {
      path: '/medicalpersonlist',
      name: 'medicalpersonlist',
      meta:{title: '添加就诊人',
      requireAuth: true, 
      },
      component: () => import('../page/medical/familylist.vue'),
    },
    {
      path: '/editperson',
      name: 'editperson',
      meta:{title: '编辑信息',requireAuth: true,},
      component: () => import('../page/medical/editperson.vue'),
    },
    {
      path: '/hospital',
      name: 'hospital',
      meta:{title: '医院列表',
      requireAuth: true, 
      },
      component: () => import('../page/medical/hospital.vue'),
    },
    {
      path: '/medicalmsg',
      name: 'medicalmsg',
      meta:{title: '医疗简介',
      requireAuth: true, 
      },
      component: () => import('../page/medical/medicalmsg.vue'),
    },
  ]
})
