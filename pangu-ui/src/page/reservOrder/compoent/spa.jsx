import React, { Fragment, Component } from 'react';
import moment from 'moment'
import { Form, TimePicker, Select, Input, Button, Divider, message, Tabs, span, Radio, Alert, Checkbox, DatePicker, Tag, Card, Tooltip } from 'antd';
const Option = Select.Option;

const { TextArea } = Input;

const RadioGroup = Radio.Group;

@Form.create()
class Spa extends Component {
    constructor(props) {
        super(props);
        this.state = {
            groupProductList: [],
            resourceList: [],
            timeShow: [],
            groupProduct: {},//当前页面选的商户？或者产品
            deparDateList: [],
            isShow: true
        };
    }
    componentDidMount() {
        const { showShopItem, serviceType } = this.props;
        let groupProductList = [];
        const res = new Map();
        groupProductList = showShopItem.groupProductList.filter((a) => !res.has(a.shopId) && res.set(a.shopId, a))
        groupProductList = groupProductList.filter(item => item.service === serviceType.type);
        if (groupProductList.length > 0) {
            this.setState({ groupProductList: groupProductList });
        } else {
            message.error("当前资源类型无可用商户！");
            this.setState({ isShow: false });
        }

    }
    componentWillReceiveProps(nextProps) {

    }

    // 保存基本信息
    handleSubmit = e => {
        e.preventDefault();
        const { onEvent, four, showShopItem, shopDetail, EquityListObj, blockReasonList, goodsInfo, serviceType, goodsGroupList } = this.props;
        this.props.form.validateFields((err, values) => {
            const productGroup = goodsGroupList.filter(item => item.shopItemStyle === true);
            if (!err) {
                const tempShopItem = showShopItem.groupProductList.filter(item => item.id === values.shopId);
                const shopItem = shopDetail.shopItemList.filter(item => item.name === tempShopItem[0].productName)[0];
                values.serviceType = shopDetail.shop.shopType;
                values.superposition = goodsInfo.superposition;
                // values.productId = tempShopItem[0].productId;
                values.shopId = tempShopItem[0].shopId;
                values.productGroupProductId = tempShopItem[0].id;
                values.memberId = EquityListObj.memberId;
                values.salesChannleId = goodsInfo.salesChannelId;
                values.giftCodeId = EquityListObj.giftCodeId;
                values.giftType = tempShopItem[0].gift;
                values.goodsId = goodsInfo.id;
                values.productGroupId = productGroup[0].id;
                values.giftDate = moment(values.giftDate).format('YYYY-MM-DD');
                values.shopChannelId = shopDetail.shopProtocol.shopChannelId;
                values.exchangeNum = tempShopItem[0].pointOrTimes;
                values.shopItemId = shopItem.id;
                onEvent('insertReservOrder', values);


            }
        });
    }
    selectShop = values => {

        const { onEvent, showShopItem, shopDetail, EquityListObj, goodsInfo } = this.props;



        let rooms = []
        if (goodsInfo && goodsInfo.enableMaxNight && goodsInfo.enableMaxNight !== '0') {
            for (let i = 1; i <= goodsInfo.enableMaxNight; i++) {
                let params = {};
                params.value = i + "";
                params.name = i + "间"
                rooms.push(params);
            }
        } else {
            for (let i = 1; i < 10; i++) {
                let params = {};
                params.value = i + "";
                params.name = i + "间"
                rooms.push(params);
            }
        }
        const groupProduct = showShopItem.groupProductList.filter(item => item.id === values);//商品中产品资源的产品组和产品关联数据
        let pointOrTimes = groupProduct[0].pointOrTimes;
        if (showShopItem.useLimitId !== 'none') {
            let temp = showShopItem.totalCount - showShopItem.useCount;
            if (pointOrTimes > temp) {
                message.error("您当前的点数或者次数不够，请重新选择！");
                return false;
            }
        }
        onEvent('getShopDetail', groupProduct[0].shopId);
        onEvent('getBlockReason', groupProduct[0].shopItemId);
        const resourceList = showShopItem.groupProductList.filter(item => item.shopId === groupProduct[0].shopId);
        console.log(resourceList);
        this.setState({ resourceList: resourceList, groupProduct: groupProduct, rooms: rooms });
        const { getFieldValue, setFieldsValue } = this.props.form;
        setFieldsValue({ "productId": resourceList[0].productId });
        const params = {};
        params.goodsId = goodsInfo.id;
        params.productGroupId = showShopItem.id;
        params.shopId = groupProduct[0].shopId;
        params.productGroupProductId = groupProduct[0].id;
        let date = new Date();
        let activationDate = moment(date).format('YYYY-MM-DD')
        if (EquityListObj.codeStatus !== '1') {
            activationDate = EquityListObj.actCodeTime;
        }
        params.outDate = EquityListObj.actOutDate;
        params.actExpireTime = EquityListObj.expiryDate;
        params.activationDate = moment(activationDate).format('YYYY-MM-DD');
        // this.props.actions.queryBookBlock(params);
        if (EquityListObj.codeStatus != '1' || (EquityListObj.expiryDate != null && EquityListObj.expiryDate != '')) {
            onEvent('queryBookBlock', params);
        } else {
            onEvent('queryOutCodeBookBlock', params);
        }
        setFieldsValue({ "checkNight": 1 + "" });

    }
    checkProduct = e => {
        const { getFieldValue, setFieldsValue } = this.props.form;
        setFieldsValue({ "giftTime": null });
        const { showShopItem, shopDetail } = this.props;
        const tempShopItem = showShopItem.groupProductList.filter(item => item.productId === e.target.value);
        const shopItem = shopDetail.shopItemList.filter(item => item.name === tempShopItem[0].productName)[0];
        let timeShow = [];


        this.setState({ timeShow: timeShow });
    }
    disabledDate = (current) => {
        // Can not select days before today and today
        const blockReasonList = this.props.blockReasonList;
        // if(blockReasonList && blockReasonList.bookDates&&blockReasonList.bookDates.length > 0){
        //     // return blockReasonList.bookDates.map(item => {
        //     //     return (current && current < moment().endOf('day')) || current > moment(item).endOf("day");
        //     // });
        //     return  current.valueOf()  < moment(blockReasonList.bookDates[0]).endOf('day') || current > moment(blockReasonList.bookDates[blockReasonList.bookDates.length-1]).endOf("day")

        // }else{
        //       return current && current < moment().endOf('day') ;
        // }
        if (blockReasonList && blockReasonList.bookDates && blockReasonList.bookDates.length > 0) {
            let isDisable = true;
            let currentStr = current.format("YYYY-MM-DD");
            blockReasonList.bookDates.map(item => {
                let dayStr = moment(item).format("YYYY-MM-DD");
                if (dayStr === currentStr) {
                    isDisable = false;
                }
            });
            return isDisable;
        } else {
            return true;
        }
    }
    onStartChange = (values, dateString) => {
        const { getFieldValue, setFieldsValue } = this.props.form;
        const { onEvent, showShopItem, shopDetail, EquityListObj, goodsInfo } = this.props;
        let rooms = [];
        if (goodsInfo && goodsInfo.enableMaxNight && goodsInfo.enableMaxNight !== '0') {
            for (let i = 1; i <= goodsInfo.enableMaxNight; i++) {
                let params = {};
                let time = moment(values).add('days', i).format('YYYY-MM-DD');
                params.value = time;
                params.name = (i + 1) + "天" + i + "晚（" + time + "离店）"
                rooms.push(params);
            }
        } else {
            for (let i = 1; i < 10; i++) {
                let params = {};
                let time = moment(values).add('days', i).format('YYYY-MM-DD');
                params.value = time;
                params.name = (i + 1) + "天" + i + "晚（" + time + "离店）"
                rooms.push(params);
            }
        }
        this.setState({ deparDateList: rooms });
        setFieldsValue({ "deparDate": rooms[0].value });
    }

    render() {
        const { groupProductList, resourceList, timeShow, rooms, deparDateList, isShow } = this.state;
        const { getFieldDecorator } = this.props.form;
        const { onEvent, four, showShopItem, shopDetail, EquityListObj, blockReasonList, goodsInfo, priceRule, reasonList, serviceType } = this.props;

        return (isShow &&
            <Fragment>
                <div className="c-modal" style={{ marginTop: 0 }}>
                    {/* <div className="c-title">基本信息</div> */}
                    <Form onSubmit={this.handleSubmit}>
                        {EquityListObj && EquityListObj.codeStatus === '1' && <div>
                            <Divider style={{ margin: 0 }}>激活信息填写</Divider>
                            <Form.Item verticalGap={1} label="手机号" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >

                                {getFieldDecorator('activePhone', { rules: [{ required: true, message: '请输入客户激活的手机号!' }], initialValue: null })(
                                    <Input maxLength={11} allowClear={true} style={{ width: "20%" }} placeholder="请输入客户激活的手机号" />

                                )}
                            </Form.Item>
                            <Form.Item style={{ marginBottom: 10 }} verticalGap={1} label="姓氏称呼" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >

                                {getFieldDecorator('activeName', { rules: [{ required: true, message: '请输入客户激活的姓氏称呼!' }], initialValue: null })(
                                    <Input maxLength={11} allowClear={true} style={{ width: "20%" }} placeholder="请输入客户激活的姓氏称呼" />

                                )}   &nbsp;&nbsp;
                         {getFieldDecorator('activeSex')(
                                    <RadioGroup >
                                        <Radio value={1} checked={true} >先生</Radio>
                                        <Radio value={2}>女士</Radio>
                                    </RadioGroup>
                                )}


                            </Form.Item>
                            <Divider style={{ margin: 0 }} />
                        </div>
                        }

                        {four &&
                            <Form.Item verticalGap={1} label="商户选择" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >
                                {getFieldDecorator('shopId', { rules: [{ required: true, message: '请选择商户!' }] })
                                    (<Select showSearch onChange={this.selectShop}
                                        filterOption={(input, option) => option.props.children.indexOf(input) >= 0} style={{ width: '30%' }}>
                                        {groupProductList &&
                                            groupProductList.map(item => (
                                                <Option key={item.id} value={item.id}>{item.hotelName}{item.shopName && "|" + item.shopName}
                                                    <span style={{ float: "right", fontSize: 12, color: "#d6d4d4" }}>&nbsp;{item.gift}</span>
                                                </Option>
                                            ))
                                        }

                                    </Select>
                                    )} &nbsp;<label style={{ fontSize: 12, color: "blue" }}>{shopDetail && shopDetail.shopProtocol && shopDetail.shopProtocol.internal === 1 ? "公司资源" : null}
                                    {shopDetail && shopDetail.shopProtocol && shopDetail.shopProtocol.internal === 0 ? "第三方资源" : null}</label>
                            </Form.Item>}
                        <Form.Item verticalGap={1} label="产品选择" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >
                            {getFieldDecorator('productId', { rules: [{ required: true, message: '请选择产品!' }] })(
                                <RadioGroup  >
                                    {resourceList.map((temp, index) => {
                                        return (<Radio value={temp.productId} checked={true}>{temp.productName}</Radio>);
                                    })}

                                </RadioGroup>
                            )}
                        </Form.Item>



                        {stopReason == 1 && shopDetail && shopDetail.shopItemList && <Form.Item verticalGap={1} label="停售原因" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >
                            {
                                shopDetail.shopItemList.map((item, index) => {
                                    if(item.enable==1){
                                        return (
                                            <div>
                                                <Tag color="red">
                                                    {item.name}  :  {item.stopReason}
                                                </Tag><br></br>
                                            </div>)
                                    }
                                })
                            }
                        </Form.Item>}

                        
                        {priceRule && priceRule.length > 0 && <Form.Item verticalGap={1} label="参考市价" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >
                            {
                                priceRule.map((item, index) => {
                                    return (
                                        <div>
                                            <Tag key={`rule-${index + Math.floor(Math.random() * 1000)}`} color="blue">
                                                {item.timeString}:{item.priceString}
                                            </Tag><br></br>
                                        </div>)
                                })
                            }
                        </Form.Item>}
                        {blockReasonList && blockReasonList.blockRule && <Form.Item verticalGap={1} label="不可预订" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >
                            <label style={{ color: 'red' }}>{blockReasonList.blockRule}</label>
                        </Form.Item>}
                        {reasonList && reasonList.length > 0 && <Form.Item verticalGap={1} label="关房关餐" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >
                            {reasonList.map(item => {
                                return (<label style={{ color: 'red' }}>{item.block + ":" + item.reason} <br /></label>)
                            })}
                        </Form.Item>}
                        <Form.Item verticalGap={1} label="预订日期" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >
                            {getFieldDecorator('giftDate', {})
                                (
                                    <DatePicker style={{ width: '20%' }} disabledDate={this.disabledDate}
                                        format="YYYY-MM-DD"
                                        placeholder="请选择预订日期"
                                        onChange={this.onStartChange}
                                        onOpenChange={this.handleStartOpenChange} />
                                )}
                        </Form.Item>
                        <Form.Item verticalGap={1} label="离店日期" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >
                            {getFieldDecorator('deparDate', {})
                                (
                                    <Select
                                        filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0} style={{ width: '20%' }}>
                                        {deparDateList && deparDateList.length > 0 &&
                                            deparDateList.map(item => (
                                                <Option key={item.value} value={item.value}>{item.name}
                                                </Option>
                                            ))
                                        }

                                    </Select>

                                )}&nbsp;&nbsp;
                           {getFieldDecorator('checkNight', {})
                                (
                                    <Select style={{ width: '5%' }} showSearch={true} tabIndex={0}
                                        filterOption={(input, option) =>
                                            option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                                        } >
                                        {goodsInfo && goodsInfo.enableMaxNight && rooms && rooms.map((item, index) => {
                                            if (index === 0) {
                                                return (<Radio value={item.value} selected={"selected"}>{item.name}</Radio>);
                                            } else {
                                                return (<Radio value={item.value} >{item.name}</Radio>);
                                            }

                                        })

                                        }
                                    </Select>

                                )}

                        </Form.Item>
                        <Form.Item verticalGap={1} label="房型" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >
                            {getFieldDecorator('accoNedds', { rules: [{ message: '请选择床型!' }], initialValue: null })(
                                <Select style={{ width: '20%' }}  >
                                    <Option key={'NULL'} value="NULL">无要求</Option>
                                    <Option value="bigbed">大床</Option>
                                    <Option selected value="doublebed">双床</Option>
                                    <Option value="trybigbed">尽量大床</Option>
                                    <Option value="trydoublebed">尽量双床</Option>
                                </Select>)}
                        </Form.Item>

                        <Form.Item verticalGap={1} label="入住人姓名" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >
                            {getFieldDecorator('giftName', { rules: [{ required: true, message: '请填写入住人姓名!' }], initialValue: EquityListObj.memberName })(
                                <Input maxLength={15} allowClear={true} disabled={goodsInfo.onlySelf === '1' ? true : false} style={{ width: "20%" }} placeholder="请填写使用人姓名" />
                            )}
                        </Form.Item>
                        <Form.Item verticalGap={1} label="联系方式" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >
                            {getFieldDecorator('giftPhone', { rules: [{ required: true, message: '请填写联系方式!' }], initialValue: EquityListObj.memberPhone })(
                                <Input maxLength={15} allowClear={true} disabled={goodsInfo.onlySelf === '1' ? true : false} style={{ width: "20%" }} placeholder="请填写联系方式" />
                            )}

                        </Form.Item>

                        <Form.Item verticalGap={1} label="备注" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >
                            {getFieldDecorator('reservRemark')(<TextArea style={{ width: '40%', height: 80 }} />)}
                            <div style={{ padding: '1% 0 1%' }}>
                                <Button type="primary" htmlType="submit" >提交预约单</Button> &nbsp;&nbsp;&nbsp;&nbsp;
                     <Button onClick={() => this.onEvent('cancel')} >取消</Button>
                            </div>
                        </Form.Item>

                    </Form>

                </div>
            </Fragment>

        );
    }
}

export default Spa;