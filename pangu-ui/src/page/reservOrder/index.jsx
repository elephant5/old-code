import React, { Fragment, Component } from 'react';
import { connect } from 'react-redux';
import { withRouter, Link } from 'react-router';
import moment from 'moment'
import { bindActionCreators } from 'redux';
import * as actions from '../../store/reservOrder/action';
import * as resourceActions from '../../store/resource/action';
import * as commonActions from '../../store/common/action';
import * as goodsActions from '../../store/goods/action';
import Accom from './compoent/accom'
import Buffet from './compoent/buffet'
import Tea from './compoent/tea'
import './index.less';
import { Form, message, Select, Input, Button, Tabs, Tag, Card, LocaleProvider, Spin } from 'antd';
import zh_CN from 'antd/lib/locale-provider/zh_CN';
const comCss = {
    borderRadius: 5,
    color: 'rgb(81, 82, 83)',
    textAlign: 'left',
    cursor: "pointer",
}
const close = {
    // width: '25%',
    ...comCss,
    backgroundColor: 'rgb(214, 212, 212)',//:'#1890ff'
};
const withe = {
    ...comCss,
    backgroundColor: '#fff',//:'#1890ff'
};
const blue = {
    ...comCss,
    backgroundColor: 'rgb(160, 241, 174)',//:'#1890ff'
};
const shopItemCss = {
    border: "1px solid #d6d4d4", width: "auto", float: "left", borderRadius: 5, fontSize: 12, marginRight: 5, cursor: "pointer"
}
const shopItemCssChecked = {
    ...shopItemCss, backgroundColor: 'rgb(160, 241, 174)',
}
const shopItemCssClose = {
    ...shopItemCss, backgroundColor: 'rgb(214, 212, 212)',
}
const status = ["已生成", '已出库', '已激活', '已用完', '已过期', '已退货', '已作废']
@connect(
    ({ operation, reserv, resource, common, goods }) => ({
        operation,
        reserv: reserv.toJS(),
        resource: resource.toJS(),
        common: common.toJS(),
        goods: goods.toJS()
    }),
    dispatch => ({
        actions: bindActionCreators({ ...actions, ...resourceActions, ...commonActions, ...goodsActions }, dispatch)
    })
)
@withRouter
@Form.create()
class CallOrder extends Component {
    constructor(props) {
        super(props);
        this.state = {
            EquityList: [],
            carBackgroundColor: withe,
            one: false,
            styleCss: false,
            goodsGroupList: [],
            goodsInfo: {},
            two: false,
            three: false,
            serviceType: {},
            showShopItem: {},
            four: false,
            shopDetail: {},
            EquityListObj: {},
            blockReasonList: {},
            priceRule: [],
            reasonList: [],
            isOnclick: true,
            productGroupProduct: {},
            canBookShopIds: [],
        };
    }
    componentDidMount() {

    }
    componentWillReceiveProps(nextProps) {
        const { operation, reserv, resource, goods } = nextProps;
        switch (operation.type) {
            case actions.GET_SELECTEQUITYLIST_SUCCESS:
                this.selectEqList(reserv.EquityList);
                break;
            case resourceActions.GET_SHOPDETAIL_SUCCESS:
                this.setState({
                    shopDetail: resource.shopDetail.result
                });

                break;
            case resourceActions.GET_QUERYBOOKBLOCK_SUCCESS:
                this.setState({
                    blockReasonList: resource.queryBookBlock.result
                });

                break;
            case resourceActions.GET_QUERYOUTCODEBOOKBLOCK_SUCCESS:
                this.setState({
                    blockReasonList: resource.queryOutCodeBookBlock.result
                });

                break;
            case resourceActions.GET_QUERYACTCALLBOOK_SUCCESS:
                this.setState({
                    canBookShopIds: resource.queryActCallBookRes.result
                })
                break;
            case resourceActions.GET_QUERYOUTCALLBOOK_SUCCESS:
                this.setState({
                    canBookShopIds: resource.queryOutCallBookRes.result
                })
                break;
            case resourceActions.GET_GETBLOCKREASON_SUCCESS:
                this.setState({
                    reasonList: resource.blockReasonList.result
                });

                break;
            case resourceActions.GET_PRICERULE_SUCCESS:
                this.setState({
                    priceRule: resource.priceRule.result
                });

                break;
            case actions.GET_INSERTRESERVORDER_SUCCESS:
                if (reserv.insertReservOrder.code === 100) {
                    message.success("预约单添加成功！");
                    this.setState({ isOnclick: false });
                    var path = {
                        pathname: '/OrderInfo',
                        query: { id: reserv.insertReservOrder.result.id },
                    }
                    this.props.router.push(path);
                } else {
                    this.setState({ isOnclick: false });
                    message.error(reserv.insertReservOrder.msg);
                }

                break;
            case resourceActions.GET_FINDPRODUCTGROUPPRODUCTBYID_SUCCESS:
                this.setState({
                    productGroupProduct: resource.findProductGroupProductByIdRes.result
                })
            default:
                break;
        }
    }
    onEvent = (type, params) => {
        switch (type) {
            case 'getShopDetail':
                this.props.actions.getShopDetail(params);
                break;
            case 'queryBookBlock':
                this.props.actions.queryBookBlock(params);
                break;
            case 'queryOutCodeBookBlock':
                this.props.actions.queryOutCodeBookBlock(params);
                break;
            case 'getBlockReason':
                this.props.actions.getBlockReason({ id: params });
                break;
            case 'insertReservOrder':
                this.setState({ isOnclick: true });
                this.props.actions.insertReservOrder(params);
                break;
            case 'getPriceRule':
                this.props.actions.getPriceRule(params);
                break;
            case 'findProductGroupProductById':
                this.props.actions.findProductGroupProductById(params);
                break;
            case 'queryActCallBook':
                this.props.actions.queryActCallBook(params);
                break;
            case 'queryOutCallBook':
                this.props.actions.queryOutCallBook(params);
                break;
            default:
                break;
        }
    }

    selectEqList = (param) => {
        if (param.code === 100) {
            const { EquityList, EquityListObj } = this.state;
            if (EquityList.length > 0) {
                let tempEquity = param.result[0];
                tempEquity.styleCss = true;
                EquityList.map(item => {
                    if (tempEquity.giftCodeId === item.giftCodeId) {
                        item = tempEquity;
                    }
                    return item;
                });
                this.setState({
                    // EquityList: EquityList,
                    EquityListObj: tempEquity,
                    goodsGroupList: tempEquity.goodsGroupListRes
                });
            } else {
                this.setState({
                    EquityList: param.result,
                });
            }

        } else {
            message.error(param.msg);
        }
    }
    clearVerything = () => {
        this.setState({
            EquityList: [],
            carBackgroundColor: withe,
            one: false,
            styleCss: false,
            goodsGroupList: [],
            goodsInfo: {},
            two: false,
            three: false,
            serviceType: {},
            showShopItem: {},
            four: false,
            shopDetail: {},
            EquityListObj: {},
            blockReasonList: {},
            priceRule: [],
            reasonList: [],
            isOnclick: false,
            isShowShopList: true,
        });
    }
    // 保存基本信息
    handleSubmit = e => {
        e.preventDefault();
        this.clearVerything();
        this.props.form.validateFields((err, values) => {
            if (!err) {

                this.props.actions.selectEquityList(values);

            }
        });
    }
    checkShopItem = (item) => {
        this.setState({
            two: false,
            three: false,
            serviceType: {},
            showShopItem: {},
            four: false,
            shopDetail: {},
            EquityListObj: {},
            blockReasonList: {},
            priceRule: [],
            goodsGroupList: []
        });
        // if (item.goodsBaseVo.disableCall === '1') {
        //     message.error("当前项目禁止来电预约！");
        //     return;
        // } else if (item.tags && item.tags.indexOf('自付') >= 1) {
        //     message.error("当前激活码为自付项目，禁止来电预约！");
        //     return;
        // } else {
        // }
        const { EquityList } = this.state;
        const EquityListTemp = EquityList.map(group => {
            if (group.giftCodeId === item.giftCodeId) {
                group.styleCss = true;
            } else {
                group.styleCss = false;
            }
            return group;
        });

        let params = {};
        params.code = item.code;
        this.props.actions.selectEquityList(params).then((data) => {
            const { reserv } = this.props;
            this.setState({
                one: true,
                styleCss: true,
                goodsGroupList: item.goodsGroupListRes,
                goodsInfo: item.goodsBaseVo,
                EquityList: EquityListTemp,
                EquityListObj: item,
            })
            this.selectEqList(reserv.EquityList);
        }).catch((error) => {
            message.error('系统错误');
        });
    }
    //选择权益产品组的事件
    showShopItem = (item) => {
        this.setState({
            two: false,
            three: false,
            serviceType: {},
            showShopItem: {},
            four: false,
            shopDetail: {},
            // EquityListObj: {},
            blockReasonList: {},
            priceRule: [],
            canBookShopIds: [],
        });
        const { goodsGroupList, goodsInfo, EquityListObj } = this.state;
        if (goodsInfo.disableCall === '1') {
            message.error("当前项目禁止来电预约！");
            return;
        } else if (EquityListObj.tags && EquityListObj.tags.indexOf('自付') >= 1) {
            message.error("当前激活码为自付项目，禁止来电预约！");
            return;
        } else {
            //检测是否有预约支付
            this.props.actions.checkGroupNeedPay(item.id).then((data) => {
                const { goods } = this.props;
                if (goods.checkGroupNeedPayRes.code == 100 && goods.checkGroupNeedPayRes.result != null) {
                    if (goods.checkGroupNeedPayRes.result == true) {
                        message.error("存在预约支付，不支持来电录单");
                        return;
                    } else {
                        //可以来电录单
                        const goodsGroupListTemp = goodsGroupList.map(group => {
                            if (group.id === item.id) {
                                group.shopItemStyle = true;

                            } else {
                                group.shopItemStyle = false;
                            }
                            // if(item.useLimitId === 'cycle_repeat'){
                            //     console.log("总次数",item.cycleNum,"使用次数",item.useCount)
                            //     if(item.cycleNum === item.useCount){
                            //         group.shopItemStyle = true;
                            //     }
                            // }
                            // console.log(group)
                            return group;
                        });
                        //过滤停售
                        let tempList = [];
                        tempList = item.groupProductList.filter(item => item.status != 1);
                        item.groupProductList = tempList;
                        this.setState({
                            two: true,
                            three: true,
                            showShopItem: item,
                            goodsGroupList: goodsGroupListTemp,
                            isShowShopList: true
                        });
                        if (item.serviceList.length > 1) {

                        } else if (item.serviceList.length === 1) {
                            const serviceType = { type: item.serviceList[0], serviceCss: true };
                            this.setState({
                                four: true,
                                serviceType: serviceType
                            });
                        }
                    }
                } else {
                    message.error("查询失败");
                    return;
                }
            }).catch((error) => {
                message.error('系统错误');
                return;
            });
        }



    }
    closeGoodsGroup = item => {
        this.setState({
            two: false,
            three: false,
            serviceType: {},
            showShopItem: {},
            four: false,
            shopDetail: {},
            // EquityListObj: {},
            goodsGroupList: [],
            blockReasonList: {},
            priceRule: []
        });


    }
    showServer = item => {
        const serviceType = { type: item, serviceCss: true };
        this.setState({
            three: true,
            four: true,
            serviceType: serviceType
        });
    }

    render() {
        const { EquityListObj, isShowShopList,
            EquityList, one, goodsGroupList, priceRule, goodsInfo, two, showShopItem, three, serviceType, four, shopDetail,
            blockReasonList, canBookShopIds, reasonList, productGroupProduct } = this.state;
        const { getFieldDecorator } = this.props.form;
        return (
            <Fragment><LocaleProvider locale={zh_CN} >
                <div className="c-modal">
                    {/* <div className="c-title">基本信息</div> */}
                    <Form onSubmit={this.handleSubmit}>
                        <Form.Item verticalGap={1} label="客户识别" labelCol={{ span: 2 }} wrapperCol={{ span: 8 }} direction="horizontal" indicatorGap >
                            {getFieldDecorator('code', { rules: [{ required: true, message: '请输入客户激活码或手机号!' }], initialValue: null })(
                                <Input maxLength={15} style={{ width: "40%" }} placeholder="请输入客户激活码或手机号" />

                            )} <Button type="primary" icon="search" htmlType="submit">查询</Button>
                        </Form.Item>
                    </Form>
                    <Form.Item verticalGap={1} label="匹配权益" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >
                        {/* {EquityList && EquityList.length === 0 && <label>没有匹配权益</label>} */}
                        {EquityList && EquityList.length < 0 ? <label>没有匹配权益</label> :
                            <Card style={{ border: 0, }}>
                                {EquityList.map((item, index) => {
                                    if (item.codeStatus === 1 || item.codeStatus === '2' || item.codeStatus === '1') {
                                        return (<Card.Grid style={item.styleCss ? blue : withe} onClick={() => this.checkShopItem(item)}>
                                            <div style={{ fontSize: "12px", width: "100%" }}>

                                                <b>#{item.goodsId}.
                                    <Link to={{ pathname: "/goodsDetails", query: { id: item.goodsId } }} target="_blank" className="fontBlue fontWeight" activeStyle={{ color: 'red' }}>
                                                        {item.goodsBaseVo.shortName}</Link>
                                                </b>
                                                <label style={{ float: 'right', color: 'red', marginLeft: '4px' }}>{item.codeStatus === "1" ? <Tag color="#f50">生肉</Tag> : status[item.codeStatus]}</label> <label style={{ float: 'right', color: '#1890ff' }}>{item.tags}</label>
                                            </div>
                                            <div style={{ fontSize: "14px", width: "100%", marginTop: '5px' }}>
                                                {item.memberName}  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{item.memberPhone}
                                            </div>
                                            <div style={{ fontSize: "14px", width: "100%", marginTop: '5px' }}>
                                                {/* 售出当天12个月内激活并使用 */}
                                                {item.goodsBaseVo.expiryValue === 'NULL' && "不限"}
                                                {item.goodsBaseVo.expiryValue === 'D' && "固定日期:" + item.goodsBaseVo.date}
                                                {item.goodsBaseVo.expiryValue === 'XM' && "售出后" + item.goodsBaseVo.usedDay + "个月内使用"}
                                                {item.goodsBaseVo.expiryValue === 'XD' && "售出后" + item.goodsBaseVo.days + "天内使用"}
                                                {item.goodsBaseVo.expiryValue === 'XM-YU' && "售出后" + item.goodsBaseVo.activeDate + "个月内激活，激活后" + item.goodsBaseVo.usedDay + "个月内使用"}
                                                {item.goodsBaseVo.expiryValue === 'XM-YD' && "售出后" + item.goodsBaseVo.activeDate + "个月内激活，激活后" + item.goodsBaseVo.daysUse + "天内使用"}
                                                {item.goodsBaseVo.expiryValue === 'XD-YU' && "售出后" + item.goodsBaseVo.activeDay + "天内激活，激活后" + item.goodsBaseVo.usedDay + "个月内使用"}
                                                {item.goodsBaseVo.expiryValue === 'XD-YD' && "售出后" + item.goodsBaseVo.days + "天内激活，激活后" + item.goodsBaseVo.daysUse + "天内使用"}
                                            </div>
                                            <div style={{ fontSize: "14px", width: "100%", marginTop: '5px' }}>
                                                售出日期:  <label style={{ color: '#1890ff' }}>{item.actOutDate}</label>
                                                激活日期: <label style={{ color: '#1890ff' }}>{item.actCodeTime ? moment(item.actCodeTime).format("YYYY-MM-DD") : "-"} </label>
                                                截止日期:  <label style={{ color: '#1890ff' }}>{item.actRule === 'NULL' ? "不限" : item.expiryDate}</label>
                                            </div>
                                            <div style={{ fontSize: "14px", width: "100%", color: "green", marginTop: '5px' }}>
                                                {item.goodsBaseVo.bankName}/{item.goodsBaseVo.salesChannelName}/{item.goodsBaseVo.salesWayName}
                                            </div>

                                        </Card.Grid>);

                                    } else {
                                        return (<Card.Grid style={close} onClick={() => this.checkShopItem(item)} >
                                            <div style={{ fontSize: "12px", width: "100%" }}>

                                                <b>#{item.goodsId}.
                            <Link to={{ pathname: "/goodsDetails", query: { id: item.goodsId } }} target="_blank" className="fontBlue fontWeight" activeStyle={{ color: 'red' }}>
                                                        {item.goodsBaseVo.shortName}</Link>

                                                </b>
                                                <label style={{ float: 'right', color: 'red', marginLeft: '4px' }}>{item.codeStatus === "1" ? <Tag color="#f50">生肉</Tag> : status[item.codeStatus]}</label> <label style={{ float: 'right', color: '#1890ff' }}>{item.tags}</label>
                                            </div>
                                            <div style={{ fontSize: "14px", width: "100%", marginTop: '5px' }}>
                                                {item.memberName}  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{item.memberPhone}
                                            </div>
                                            <div style={{ fontSize: "14px", width: "100%", marginTop: '5px' }}>
                                                {/* 售出当天12个月内激活并使用 */}
                                                {item.goodsBaseVo.expiryValue === 'NULL' && "不限"}
                                                {item.goodsBaseVo.expiryValue === 'D' && "固定日期:" + item.goodsBaseVo.date}
                                                {item.goodsBaseVo.expiryValue === 'XM' && "售出后" + item.goodsBaseVo.usedDay + "个月内使用"}
                                                {item.goodsBaseVo.expiryValue === 'XD' && "售出后" + item.goodsBaseVo.days + "天内使用"}
                                                {item.goodsBaseVo.expiryValue === 'XM-YU' && "售出后" + item.goodsBaseVo.activeDate + "个月内激活，激活后" + item.goodsBaseVo.usedDay + "个月内使用"}
                                                {item.goodsBaseVo.expiryValue === 'XM-YD' && "售出后" + item.goodsBaseVo.activeDate + "个月内激活，激活后" + item.goodsBaseVo.daysUse + "天内使用"}
                                                {item.goodsBaseVo.expiryValue === 'XD-YU' && "售出后" + item.goodsBaseVo.activeDay + "天内激活，激活后" + item.goodsBaseVo.usedDay + "个月内使用"}
                                                {item.goodsBaseVo.expiryValue === 'XD-YD' && "售出后" + item.goodsBaseVo.days + "天内激活，激活后" + item.goodsBaseVo.daysUse + "天内使用"}
                                            </div>
                                            <div style={{ fontSize: "14px", width: "100%", marginTop: '5px' }}>
                                                售出日期:  <label style={{ color: '#1890ff' }}>{item.actOutDate}</label>
                                                激活日期: <label style={{ color: '#1890ff' }}>{item.actCodeTime ? moment(item.actCodeTime).format("YYYY-MM-DD") : "-"} </label>
                                                截止日期:  <label style={{ color: '#1890ff' }}>{item.actRule === 'NULL' ? "不限" : item.expiryDate}</label>
                                            </div>
                                            <div style={{ fontSize: "14px", width: "100%", color: "green", marginTop: '5px' }}>
                                                {item.goodsBaseVo.bankName}/{item.goodsBaseVo.salesChannelName}/{item.goodsBaseVo.salesWayName}
                                            </div>

                                        </Card.Grid>);
                                    }



                                })}

                            </Card>}
                    </Form.Item>
                    {one &&
                        <Form.Item verticalGap={1} label="权益产品组" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >
                            {goodsGroupList && goodsGroupList.map((item, index) => {
                                let temp = item.totalCount - item.useCount;
                                if ((temp > 0 || item.useLimitId === 'none') && (EquityListObj.codeStatus === 1 || EquityListObj.codeStatus === '2' || EquityListObj.codeStatus === '1') && (item.cycleNum != item.useCount)) {
                                    return (<div onClick={() => this.showShopItem(item)} style={item.shopItemStyle ? shopItemCssChecked : shopItemCss}>
                                        <div><b style={{ marginLeft: 5, marginRight: 8 }}>{item.name} &nbsp;&nbsp;
                                        {item.useLimitId === 'none' ? "无限制" : null}
                                            {item.useLimitId === 'fixed_times' ? item.useNum + "次" : null}
                                            {item.useLimitId === 'fixed_point' ? item.useNum + "点" : null}
                                            {item.useLimitId === 'cycle_repeat' && item.useType === '1' ? item.useNum + "点" : item.useNum + "次"}

                                        </b></div>
                                        <div style={{ marginTop: -15, marginRight: 8 }}>
                                            {item.useLimitId === 'fixed_point' && <label style={{ marginLeft: 5, marginTop: 0 }}>总计{item.totalCount}点 剩余{item.totalCount - item.useCount}点</label>}
                                            {item.useLimitId === 'fixed_times' && <label style={{ marginLeft: 5, marginTop: 0 }}>总计{item.totalCount}次 剩余{item.totalCount - item.useCount}次</label>}
                                            {item.useLimitId === 'cycle_repeat' && item.useType === 0 && <label style={{ marginLeft: 5, marginTop: 0 }}>总计{item.totalCount}次 周期内剩余{item.cycleNum - item.useCount}次 {item.startTime} 至 {item.endTime}</label>}
                                            {item.useLimitId === 'cycle_repeat' && item.useType === 1 && <label style={{ marginLeft: 5, marginTop: 0 }}>总计{item.totalCount}点 周期内剩余{item.cycleNum - item.useCount}点 {item.startTime} 至 {item.endTime}</label>}
                                            {item.useLimitId === 'none' && <label style={{ marginLeft: 5, marginTop: 0 }}>无限制&nbsp;已使用次数：{item.useCount}</label>}
                                        </div>
                                    </div>)
                                } else {
                                    return (<div style={shopItemCssClose}>
                                        <div><b style={{ marginLeft: 5, marginRight: 8 }}>{item.name}&nbsp;&nbsp;
                                        {item.useLimitId === 'none' ? "无限制" : null}
                                            {item.useLimitId === 'fixed_times' ? item.useNum + "次" : null}
                                            {item.useLimitId === 'fixed_point' ? item.useNum + "点" : null}
                                            {item.useLimitId === 'cycle_repeat' && item.useType === '1' ? item.useNum + "点" : item.useNum + "次"}

                                        </b></div>
                                        <div style={{ marginTop: -15, marginRight: 8 }}>
                                            {item.useLimitId === 'fixed_point' && <label style={{ marginLeft: 5, marginTop: 0 }}>总计{item.totalCount}点 剩余{item.totalCount - item.useCount}点</label>}
                                            {item.useLimitId === 'fixed_times' && <label style={{ marginLeft: 5, marginTop: 0 }}>总计{item.totalCount}次 剩余{item.totalCount - item.useCount}次</label>}
                                            {item.useLimitId === 'cycle_repeat' && item.useType === 0 && <label style={{ marginLeft: 5, marginTop: 0 }}>总计{item.totalCount}次 周期内剩余{item.cycleNum - item.useCount}次 {item.startTime} 至 {item.endTime}</label>}
                                            {item.useLimitId === 'cycle_repeat' && item.useType === 1 && <label style={{ marginLeft: 5, marginTop: 0 }}>总计{item.totalCount}点 周期内剩余{item.cycleNum - item.useCount}点 {item.startTime} 至 {item.endTime}</label>}
                                            {item.useLimitId === 'none' && <label style={{ marginLeft: 5, marginTop: 0 }}>无限制&nbsp;已使用次数：{item.useCount}</label>}
                                        </div>
                                    </div>)
                                }

                            })}<br></br>
                        </Form.Item>
                    }
                    {two && (goodsInfo.superposition === "1" || goodsInfo.singleThread === "1"
                        || goodsInfo.enableMaxNight === "1"
                        || goodsInfo.enableMinNight === "1"
                        || goodsInfo.allYear === "1"
                        || goodsInfo.disableCall === "1"
                        || goodsInfo.disableWechat === "1"
                        || goodsInfo.onlySelf === "1") && <div>

                            <Form.Item style={{ marginBottom: 0 }} verticalGap={1} label="项目限制" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >
                                <div style={{ color: 'red', fontSize: 12 }}>
                                    {goodsInfo.superposition === "1" ? <label>● 同一时段权益不叠加<br /></label> : null}
                                    {goodsInfo.singleThread === "1" ? <label>● 行权完毕次日才可以进行下一次预约<br /></label> : null}
                                    {goodsInfo.enableMaxNight === "1" ? <label>● 限制单次可预订最大间夜数:{goodsInfo.maxNight}间夜<br /></label> : null}
                                    {goodsInfo.enableMinNight === "1" ? <label>● 限制单次可预订最小间夜数:{goodsInfo.minNight}间夜<br /></label> : null}
                                    {goodsInfo.allYear === "1" ? <label>● OK365<br /></label> : null}
                                    {goodsInfo.disableCall === "1" ? <label>● 禁止来电预约<br /></label> : null}
                                    {goodsInfo.disableWechat === "1" ? <label>● 禁止微信统一行权<br /></label> : null}
                                    {goodsInfo.onlySelf === "1" ? <label>● 仅限本人使用<br /></label> : null}
                                </div>
                            </Form.Item>
                            {goodsInfo.callReminder && <Form.Item verticalGap={1} label="来电提醒" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >
                                <div style={{ color: 'red', fontSize: 12 }}>
                                    {goodsInfo.callReminder}
                                </div>
                            </Form.Item>}
                            {/* {goodsInfo.remark&& <Form.Item  verticalGap={1}  label="商品备注" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal"  indicatorGap >
                        <div style={{color:'red',fontSize:12}}>
                            {goodsInfo.remark}
                            </div>
                    </Form.Item>} */}
                            {/* {goodsInfo.hotline && <Form.Item  verticalGap={1}  label="热线号码" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal"  indicatorGap >
                        <div style={{color:'red',fontSize:12}}>
                            {goodsInfo.hotline}
                            </div>
                    </Form.Item>} */}
                            {showShopItem.minBookDays && <Form.Item verticalGap={1} label="预约限制" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >
                                <div style={{ color: 'red', fontSize: 12 }}>
                                    最少提前{showShopItem.minBookDays} 天，最多提前{showShopItem.maxBookDays ? showShopItem.maxBookDays : 0} 天
                            </div>
                            </Form.Item>}
                            <br></br>
                        </div>
                    }
                    {three && showShopItem.serviceList.length > 1 &&
                        <Form.Item verticalGap={1} label="产品类型" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >
                            {showShopItem.serviceList.map((item, index) => {
                                return (<div onClick={() => this.showServer(item)} style={serviceType.type === item ? shopItemCssChecked : shopItemCss}>
                                    <b style={{ marginLeft: 5, marginRight: 8 }}>{item} </b>
                                </div>)
                            })}<br></br>
                        </Form.Item>
                    }


                    {four && serviceType.type === '自助餐' &&
                        <Buffet shopDetail={shopDetail} EquityListObj={EquityListObj} goodsInfo={goodsInfo} blockReasonList={blockReasonList} canBookShopIds={canBookShopIds} reasonList={reasonList}
                            onEvent={this.onEvent} isOnclick={this.state.isOnclick} four={four} showShopItem={showShopItem} serviceType={serviceType} goodsGroupList={goodsGroupList}
                            priceRule={priceRule}>

                        </Buffet>}
                    {four && serviceType.type === '住宿' && <Accom
                        shopDetail={shopDetail} EquityListObj={EquityListObj} isShowShopList={isShowShopList} goodsInfo={goodsInfo} blockReasonList={blockReasonList} canBookShopIds={canBookShopIds} reasonList={reasonList} productGroupProduct={productGroupProduct}
                        onEvent={this.onEvent} isOnclick={this.state.isOnclick} four={four} showShopItem={showShopItem} serviceType={serviceType} goodsGroupList={goodsGroupList} priceRule={priceRule}>
                        ></Accom>}
                    {four && (serviceType.type === '下午茶' || serviceType.type === '健身' || serviceType.type === '水疗' || serviceType.type === '定制套餐') && <Tea
                        shopDetail={shopDetail} EquityListObj={EquityListObj} goodsInfo={goodsInfo} blockReasonList={blockReasonList} canBookShopIds={canBookShopIds} reasonList={reasonList}
                        onEvent={this.onEvent} isOnclick={this.state.isOnclick} four={four} showShopItem={showShopItem} serviceType={serviceType} goodsGroupList={goodsGroupList} priceRule={priceRule}>
                        ></Tea>}
                </div>
            </LocaleProvider></Fragment>

        );
    }
}

export default CallOrder;