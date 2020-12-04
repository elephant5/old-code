import React from 'react';
import { Router, Route, IndexRedirect } from 'react-router';
import cookie from 'react-cookies';
// import { Router,Route, Redirect,Switch } from 'react-router-dom'
// import AuthorizedRoute from './AuthorizedRoute'

import App from '../app';
import Dashboard from '../layout/index';

//登录页
import Login from '../page/login/index';

import Hotel from '../page/hotel/index';
import HotelDetail from '../page/hotel/detail';

// 资源管理
import Merchant from '../page/merchant/index';
import AddOne from '../page/merchant/addOne';
import Addtwo from '../page/merchant/addTwo';

//商品管理
import Goods from '../page/goods/index';
import GoodsDetails from '../page/goods/goodsDetails';
import GoodsInfo from '../page/goods/goodsDetails';
import Product from '../page/product/index';
import ProductInfo from '../page/productInfo/index';
import ProductGroup from '../page/productGroup/index';
import ProductGroupCopy from '../page/productGroupCopy/index';
import ProductGroupDetail from '../page/productGroup/detail';

//订单管理-来电录单
import ReservOrder from '../page/reservOrder/index';
//订单管理列表
import Order from '../page/order/index';
//订单管理详情
import OrderInfo from '../page/order/orderDetail';

//激活码管理
import GiftCode from '../page/giftCode/index';

//渠道
import Channel from '../page/channel/index';
import AddChanel from '../page/channel/addSalesChannel';
//Logo
import Logo from '../page/logo/index';
//Logo
import Sms from '../page/sms/index';

//线下退款
import Refund from '../page/refund/index'
//销售单管理---支付宝销售单
import AlipaySalesOrder from '../page/alipaySalesOrder/index'



import WorkPlace from '../page/workplace/index';

const authRequired = (nextState, replace) => {
    const user = cookie.load("user");
    if (!user) {
        replace('/login');
    }
};

const loginEnter = (nextState, replace) => {
    const user = cookie.load("user");
    if (user) {
        replace('/workPlace')
    }
}

export default history => {
    return (
        <Router history={history}>
            <Route path='/' component={App}>
                <IndexRedirect to="workPlace" />
                <Route path='login' component={Login} />
                <Route component={Dashboard} breadcrumbName="盘古系统" >
                    <Route path='workPlace' component={WorkPlace} breadcrumbName="Dashboard / 工作台" onEnter={authRequired}></Route>

                    {/* 资源管理-酒店 */}
                    <Route path='KLF_PG_RM_HOTEL_LIST' component={Hotel} breadcrumbName="酒店列表" onEnter={authRequired}></Route>
                    <Route path='hotelDetail/:id' component={HotelDetail} breadcrumbName="酒店详情" onEnter={authRequired}></Route>

                    {/* 资源管理-商户 */}
                    <Route path='KLF_PG_RM_SHOP_LIST' component={Merchant} breadcrumbName="商户" onEnter={authRequired}></Route>
                    <Route path='addone' component={AddOne} breadcrumbName="新增商户" onEnter={authRequired}></Route>
                    <Route path='addtwo/:id' component={Addtwo} breadcrumbName="修改商户信息" onEnter={authRequired}></Route>

                    {/* 商品管理-商品 */}
                    <Route path='KLF_PG_GM_GOODS_LIST' component={Goods} breadcrumbName="商品列表" onEnter={authRequired}></Route>
                    <Route path='goodsDetails' component={GoodsDetails} breadcrumbName="新增商品" onEnter={authRequired}></Route>
                    <Route path='goodsInfo' component={GoodsInfo} breadcrumbName="修改商品" onEnter={authRequired}></Route>
                    {/* 商品管理-产品 */}
                    <Route path='KLF_PG_RM_PROD_LIST' component={Product} breadcrumbName="产品列表" onEnter={authRequired}></Route>
                    {/* 产品操作 */}
                    <Route path='productInfo' component={ProductInfo} breadcrumbName="产品操作列表" onEnter={authRequired}></Route>
                    {/* 商品管理-产品组 */}
                    <Route path='KLF_PG_GM_PROD_GROUP_LIST' component={ProductGroup} breadcrumbName="产品组列表" onEnter={authRequired}>
                    </Route>
                    <Route path='productGroupCopy' component={ProductGroupCopy} breadcrumbName="产品组列表复制" onEnter={authRequired}>
                    </Route>

                    <Route path="productGroupDetail/:id" component={ProductGroupDetail} breadcrumbName="产品组详情" onEnter={authRequired}></Route>

                    {/* 订单管理-录单 */}
                    <Route path='KLF_PG_ORDER_CALL' component={ReservOrder} breadcrumbName="来电录单" onEnter={authRequired} />
                    <Route path='KLF_PG_ORDER_LIST' component={Order} breadcrumbName="订单管理" onEnter={authRequired} />
                    <Route path='OrderInfo' component={OrderInfo} breadcrumbName="订单详情" onEnter={authRequired}></Route>
                    {/* 激活码管理 */}
                    <Route path='KLF_PG_CODE_LIST' component={GiftCode} breadcrumbName='激活码管理' onEnter={authRequired} />

                    {/* 系统管理-渠道 */}
                    <Route path='KLF_PG_GM_CHANNEL_LIST' component={Channel} breadcrumbName="渠道列表" onEnter={authRequired}></Route>
                    <Route path='addchanel' component={AddChanel} breadcrumbName="新增渠道" onEnter={authRequired}></Route>
                    {/* 系统管理-LOGO */}
                    <Route path='KLF_PG_SM_LOGO_LIST' component={Logo} breadcrumbName="Logo列表" onEnter={authRequired}></Route>
                    {/* 系统管理-短信操作 KLF_PG_SM_SMS_SEND*/}
                    <Route path='KLF_PG_SM_SMS_LIST' component={Sms} breadcrumbName="短信列表" onEnter={authRequired}></Route>
                    {/* {线下退款更新} */}
                    <Route path='KLF_PG_REFUND_LIST' component={Refund} breadcrumbName="线下退款" onEnter={authRequired}></Route>
                    {/* {销售单管理---支付宝销售单} */}
                    <Route path='KLF_PG_SALES_ALIPAY' component={AlipaySalesOrder} breadcrumbName="支付宝销售单" onEnter={authRequired}></Route>
                </Route>
            </Route>
        </Router>
    );
}; 