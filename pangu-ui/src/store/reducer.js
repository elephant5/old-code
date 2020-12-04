import { combineReducers }  from 'redux';
import { routerReducer } from 'react-router-redux';
import { operationReducer } from './opreation-reducer';
import productGroup from './productGroup/reduce';
import product from './product/reduce';
import resource from './resource/reduce';
import common from './common/reduce';
import channel from './channel/reduce';
import goods from './goods/reduce';
import login from './login/reduce';
import reserv from './reservOrder/reduce';
import code from './giftCode/reduce';
import logo from './logo/reduce';
import  nuwa from './nuwa/reduce';
import alipaySales from './alipaySales/reduce';
const rootReducer = combineReducers({
    // 将路由加入reducer
    routing: routerReducer,
    operation: operationReducer,
    // 资源
    resource,
    //产品
    product,
    //产品组
    productGroup,
    //销售渠道
    channel,
    // 公共数据
    common,
    //商品
    goods,
    //登录
    login,
    //订单
    reserv,
    //激活码
    code,
    //Logo
    logo,
    //女娲 发送短信
    nuwa,
    //支付宝销售单
    alipaySales,
});

export default rootReducer;