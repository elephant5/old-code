import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { bindActionCreators } from 'redux';
import * as actions from '../../store/resource/action';
import One from './component/one';
import _ from 'lodash';
import { LocaleProvider, message } from 'antd';
import zh_CN from 'antd/lib/locale-provider/zh_CN';
import 'moment/locale/zh-cn';
@connect(
    ({operation, resource}) => ({
        operation,
        resource: resource.toJS()
    }),
    dispatch => ({ 
        actions: bindActionCreators(actions, dispatch) 
    })
)
@withRouter
class AddOne extends Component {
    constructor(props) {
        super(props);
        this.state = { 
            // 商户类型
            shopTypes: [],
            // 国家城市列表
            countryCity: [],
            // 获取商户渠道
            channels: [],
            // 结算方式列表
            settleMethods: [],
            // 货币类型列表
            currencys: [],
            // 新增商户第一步
            shopOne: {},
            hotelSelList:{},
        };
    }
    componentDidMount() {
        // 商户类型
        this.props.actions.getShopType({});
        // 国家城市列表
        this.props.actions.getCountryCity();
        // 获取商户渠道
        this.props.actions.getChannel();
        // 结算方式列表
        this.props.actions.getSettleMethod();
        // 货币类型列表
        this.props.actions.getCurrency();
    }

    componentWillReceiveProps(nextProps){
        const { operation, resource } = nextProps;
        switch(operation.type){
            case actions.GET_SHOPTYPE_SUCCESS:
                this.setState({
                    shopTypes: resource.shopType.result,
                    // shopTypeId: resource.shopType.result[0].code
                })
                break;
            // 国家城市列表
            case actions.GET_COUNTRYCITY_SUCCESS: 
                this.setState({
                    countryCity: resource.countryCity.result
                })
                break;
            case actions.GET_CHANNEL_SUCCESS: 
                this.setState({
                    channels: resource.channelList.result
                })
                break;
            case actions.GET_SETTLEMETHOD_SUCCESS: 
                this.setState({
                    settleMethods: resource.settleMethod.result
                })
                break;
            case actions.GET_CURRENCY_SUCCESS: 
                this.setState({
                    currencys: resource.currency.result
                })
                break;
            case actions.ADD_SHOPONE_SUCCESS: 
                if(resource.shopOne.code  === 100){
                    message.success("商户信息保存成功！");
                }else{
                    message.error("商户信息保存失败："+resource.shopOne.msg,10);
                    return;
                }
                this.setState({
                    shopOne: resource.shopOne.result
                }, () => {
                    this.props.router.push(`/addtwo/${this.state.shopOne.shop.id}`);
                })
                break;
            case actions.GET_SELECTBYHOTELNAMELIST_SUCCESS: 
                this.setState({
                    hotelSelList: resource.hotelSelList.result
                })
                break;    
                
          default:
            break;
        }
    }

    onEvent = (type, params) => {
        switch(type){
            case 'nextStep':
                this.props.actions.addShopOne(params);
                break;
            case 'selectByHotelNameList':
            this.props.actions.selectByHotelNameList(params);
            break;    
            default: 
                break;
        }
    }
    render() {
        const { shopTypes, countryCity, channels, settleMethods, currencys,hotelSelList } = this.state;
        const loading = this.props.operation.loading[actions.ADD_SHOPONE]
        console.log(hotelSelList)
        return (
            <LocaleProvider locale={zh_CN} >
            <Fragment>
                {
                    !_.isEmpty(shopTypes) && !_.isEmpty(countryCity) && !_.isEmpty(channels) && !_.isEmpty(settleMethods) && !_.isEmpty(currencys) &&
                    <One
                        hotelSelList={hotelSelList}
                        data={{...this.state}}
                        onEvent={this.onEvent}
                        loading={loading}
                    />
                }
            </Fragment>
            </LocaleProvider>
        );
    }
}

export default AddOne;