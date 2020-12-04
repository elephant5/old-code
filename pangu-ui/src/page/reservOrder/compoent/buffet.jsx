import React, { Fragment, Component } from 'react';
import { Link } from 'react-router';
import moment from 'moment'
import { Form, Select, Input, Button, Divider, span, Radio, DatePicker, Tag, message, } from 'antd';
const Option = Select.Option;
const { TextArea } = Input;
const RadioGroup = Radio.Group;

@Form.create()
class Buffet extends Component {
    constructor(props) {
        super(props);
        this.state = {
            groupProductList: [],
            resourceList: [],
            timeShow: [],
            isshopDetail: false,
            shopItem: {},
            isShow: true,
            showShopItem: [],
            shopItemIdTemp: null,
            shopDetailTemp: null,
            stopReason: 0,
            bookFlag: false,
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
        this.setState({ groupProductList: groupProductList, showShopItem: showShopItem });
    }

    getShopMsg() {
        const { showShopItem, serviceType, canBookShopIds } = this.props;
        const { bookFlag } = this.state;
        let groupProductList = [];
        const res = new Map();
        groupProductList = showShopItem.groupProductList.filter((a) => !res.has(a.shopId) && res.set(a.shopId, a))
        groupProductList = groupProductList.filter(item => item.service === serviceType.type);
        if (bookFlag) {
            if (groupProductList.length > 0) {
                if (canBookShopIds && canBookShopIds.length > 0) {
                    //过滤可预订商户
                    groupProductList = groupProductList.filter((g) => {
                        var temp = false;
                        canBookShopIds.map((c) => {
                            if (g.shopId == c) {
                                temp = true;
                            }
                        })
                        return temp;
                    });
                } else {
                    //移除所有商户
                    groupProductList = [];
                }
            }
        }
        if (groupProductList.length > 0) {
            this.setState({ groupProductList: groupProductList });
        } else {
            // message.error("当前资源类型无可用商户！");
            // this.setState({ isShow: false });
        }
        this.setState({ groupProductList: groupProductList, });
    }

    componentWillReceiveProps(nextProps) {

        const { shopDetail, onEvent, showShopItem, serviceType, canBookShopIds } = nextProps;
        const { shopDetailTemp, bookFlag } = this.state;
        let tempFlag = bookFlag;
        let groupProductList = [];
        const res = new Map();
        groupProductList = showShopItem.groupProductList.filter((a) => !res.has(a.shopId) && res.set(a.shopId, a))
        groupProductList = groupProductList.filter(item => item.service === serviceType.type);
        //更换了点击的产品组时预查询可预约商户bookFlag设置成false
        if (showShopItem.id !== this.state.showShopItem.id) {
            this.setState({
                bookFlag: false,
            })
            tempFlag = false;
        }
        if (tempFlag) {
            if (groupProductList.length > 0) {
                if (canBookShopIds && canBookShopIds.length > 0) {
                    //过滤可预订商户
                    groupProductList = groupProductList.filter((g) => {
                        var temp = false;
                        canBookShopIds.map((c) => {
                            if (g.shopId == c) {
                                temp = true;
                            }
                        })
                        return temp;
                    });
                } else {
                    //移除所有商户
                    groupProductList = [];
                }
            }
        }
        if (groupProductList.length > 0) {
            this.setState({ groupProductList: groupProductList });
        } else {
            // message.error("当前资源类型无可用商户！");
            // this.setState({ isShow: false });
        }
        this.setState({ groupProductList: groupProductList, });

        if (shopDetail.shop && this.state.isshopDetail && this.state.resourceList.length > 0) {
            let shopItem = shopDetail.shopItemList.filter(item => item.id == this.state.shopItemIdTemp)[0];

            if (!shopItem) {
                return;
            }
            let timeShow = [];
            let start = shopItem.openTime ? shopItem.openTime.split(":")[0] : 0;
            let end = shopItem.openTime ? shopItem.closeTime.split(":")[0] : 24;

            if (this.state.resourceList && this.state.resourceList.length > 1) {
                this.state.resourceList.map(item => {

                    if (item.shopItemId === shopItem.id) {
                        const { getFieldValue, setFieldsValue } = this.props.form;
                        setFieldsValue({ "productId": item.productId });
                        setFieldsValue({ "giftTime": null });
                    }
                })
            } else {
                shopDetail.shopItemList.map(item => {
                    if (this.state.resourceList[0].shopItemId === item.id) {
                        const { getFieldValue, setFieldsValue } = this.props.form;
                        setFieldsValue({ "productId": this.state.resourceList[0].productId });
                        setFieldsValue({ "giftTime": null });
                        shopItem = item;
                    }
                });
            }
            start = shopItem.openTime ? shopItem.openTime.split(":")[0] : 0;
            end = shopItem.openTime ? shopItem.closeTime.split(":")[0] : 24;
            let startTail = shopItem.openTime ? shopItem.openTime.split(":")[1].trim() : 0;
            let endTail = shopItem.closeTime ? shopItem.closeTime.split(":")[1].trim() : 0;
            for (let i = start; i <= end; i++) {
                let tempValue = "00";
                if (i == start) {
                    if (Number(startTail) <= 30 && Number(startTail) != 0) {
                        tempValue = "30";
                    } else if (Number(startTail) > 30) {
                        i++;
                    }
                }
                let temp = {};
                temp.value = i + ":" + tempValue;
                temp.name = shopItem.name + "-" + i + ":" + tempValue;
                timeShow.push(temp);
                if (tempValue == "00") {
                    if ((i == end && Number(endTail) >= 30) || (i < end)) {
                        tempValue = "30";
                        let temp2 = {};
                        temp2.value = i + ":" + tempValue;
                        temp2.name = shopItem.name + "-" + i + ":" + tempValue;
                        timeShow.push(temp2);
                    }
                }
            }
            let rulesParams = {};
            rulesParams.shopItemId = shopItem.id;
            rulesParams.shopId = shopItem.shopId;
            onEvent('getPriceRule', rulesParams);
            // this.props.actions.getPriceRule(rulesParams);
            this.setState({ timeShow: timeShow, isshopDetail: false, shopItem: shopItem });

            var b = (JSON.stringify(shopDetail) == "{}");
            if (!b) {
                const { getFieldValue, setFieldsValue } = this.props.form;
                // let stopItem = shopDetail.shopItemList.filter(item =>  item.enable != 1);
                let stopItem = shopDetail.shopItemList;
                var c = (JSON.stringify(stopItem) == "[]");
                if (!c) {
                    let tempResource = this.state.resourceList.filter(item => item.shopItemId === this.state.shopItemIdTemp);
                    var a = (JSON.stringify(tempResource) == "[]");
                    if (!a) {
                        setFieldsValue({ "productId": tempResource[0].productId });
                    } else {
                        message.error("当前产品已被停售，请重新选择！");
                        return false;
                    }
                }
            }

        }
    }

    // 保存基本信息
    handleSubmit = e => {
        e.preventDefault();
        const { onEvent, four, showShopItem, shopDetail, EquityListObj, blockReasonList, goodsInfo, serviceType, goodsGroupList } = this.props;
        this.props.form.validateFields((err, values) => {
            const productGroup = goodsGroupList.filter(item => item.shopItemStyle === true);
            if (!err) {

                const tempShopItem = showShopItem.groupProductList.filter(item => item.productId === values.productId);
                if (tempShopItem[0].status == 1) {

                    message.error("当前产品已被停售，请重新选择！");
                    return false;

                }
                const shopItem = shopDetail.shopItemList.filter(item => item.id === tempShopItem[0].shopItemId)[0];
                if (!shopItem) {
                    message.error("未找到当前餐型");
                    return false;
                }
                values.serviceType = serviceType.type;
                values.superposition = goodsInfo.superposition;
                values.singleThread = goodsInfo.singleThread;
                // values.productId = tempShopItem[0].productId;
                values.shopId = tempShopItem[0].shopId;
                values.exchangeNum = tempShopItem[0].pointOrTimes;
                values.productGroupProductId = tempShopItem[0].id;
                values.memberId = EquityListObj.memberId;
                values.salesChannleId = goodsInfo.salesChannelId;
                values.giftCodeId = EquityListObj.giftCodeId;
                values.giftType = tempShopItem[0].gift;
                values.goodsId = goodsInfo.id;
                values.productGroupId = productGroup[0].id;
                values.giftDate = moment(values.giftDate).format('YYYY-MM-DD');
                values.shopChannelId = shopDetail.shopProtocol.shopChannelId;
                values.shopItemId = shopItem.id;
                onEvent('insertReservOrder', values);
            }
        });
    }

    handleChange = (value) => {
        console.log(`selected ${value}`);
    }

    /**预查询可用商户 */
    queryBookShop = e => {
        e.preventDefault();
        const { onEvent, showShopItem, EquityListObj, goodsInfo } = this.props;
        let bookDateMoment = this.props.form.getFieldValue("bookDate");
        if (bookDateMoment) {
            this.setState({ bookFlag: true }, () => {
                let bookDate = moment(bookDateMoment).format('YYYY-MM-DD')
                const params = {};
                params.bookDate = bookDate;
                params.goodsId = goodsInfo.id;
                params.productGroupId = showShopItem.id;
                let date = new Date();
                let activationDate = moment(date).format('YYYY-MM-DD')
                if (EquityListObj.codeStatus !== '1') {
                    activationDate = EquityListObj.actCodeTime;
                }
                params.outDate = EquityListObj.actOutDate;
                params.actExpireTime = EquityListObj.expiryDate;
                params.activationDate = moment(activationDate).format('YYYY-MM-DD');
                if (EquityListObj.codeStatus != '1' || (EquityListObj.expiryDate != null && EquityListObj.expiryDate != '')) {
                    onEvent('queryActCallBook', params);
                } else {
                    onEvent('queryOutCallBook', params);
                }
            })
        } else {
            this.setState({
                bookFlag: false,
            }, () => {
                this.getShopMsg();
            })
        }
    }

    selectShop = values => {

        this.setState({ resourceList: [], timeShow: [] });
        const { onEvent, showShopItem, EquityListObj, goodsInfo, shopDetail, reasonList } = this.props;
        const tempShopItem = showShopItem.groupProductList.filter(item => item.id === values);
        let pointOrTimes = tempShopItem[0].pointOrTimes;

        if (showShopItem.useLimitId !== 'none') {
            let temp = showShopItem.totalCount - showShopItem.useCount;
            if (pointOrTimes > temp) {
                message.error("您当前的点数或者次数不够，请重新选择！");
                return false;
            }
        }
        this.setState({ stopReason: 0 })

        onEvent('getShopDetail', tempShopItem[0].shopId);
        onEvent('getBlockReason', tempShopItem[0].shopItemId);
        const resourceList = showShopItem.groupProductList.filter(item => item.shopId === tempShopItem[0].shopId);
        if (resourceList.length === 1) {

        }
        this.setState({ resourceList: resourceList, shopItemIdTemp: tempShopItem[0].shopItemId, shopDetailTemp: tempShopItem[0].shopId });

        const params = {};
        params.goodsId = goodsInfo.id;
        params.productGroupId = showShopItem.id;
        params.shopId = tempShopItem[0].shopId;
        params.productGroupProductId = tempShopItem[0].id;
        let date = new Date();
        let activationDate = moment(date).format('YYYY-MM-DD')
        if (EquityListObj.codeStatus !== '1') {
            activationDate = EquityListObj.actCodeTime;
        }
        this.setState({ isshopDetail: true });
        params.outDate = EquityListObj.actOutDate;
        params.actExpireTime = EquityListObj.expiryDate;
        params.activationDate = moment(activationDate).format('YYYY-MM-DD');
        // this.props.actions.queryBookBlock(params);
        if (EquityListObj.codeStatus != '1' || (EquityListObj.expiryDate != null && EquityListObj.expiryDate != '')) {
            onEvent('queryBookBlock', params);
        } else {
            onEvent('queryOutCodeBookBlock', params);
        }


    }
    checkProduct = e => {
        const { getFieldValue, setFieldsValue } = this.props.form;
        setFieldsValue({ "giftTime": null });
        const { onEvent, showShopItem, shopDetail, goodsInfo, EquityListObj } = this.props;
        const tempShopItem = showShopItem.groupProductList.filter(item => item.productId === e.target.value);
        const shopItem = shopDetail.shopItemList.filter(item => item.id === tempShopItem[0].shopItemId)[0];
        if (!shopItem) {
            message.error("未找到当前餐型");
            return false;
        }
        this.setState({ stopReason: 0 })
        if (tempShopItem[0].status == 1) {
            this.setState({ stopReason: 1 });
            this.setState({ timeShow: [] });
            message.error("当前产品已被停售，请重新选择！");
            return false;

        }

        onEvent('getBlockReason', shopItem.id);

        let timeShow = [];
        let start = shopItem.openTime ? shopItem.openTime.split(":")[0] : 0;
        let end = shopItem.openTime ? shopItem.closeTime.split(":")[0] : 24;
        let startTail = shopItem.openTime ? shopItem.openTime.split(":")[1].trim() : 0;
        let endTail = shopItem.closeTime ? shopItem.closeTime.split(":")[1].trim() : 0;
        for (let i = start; i <= end; i++) {
            let tempValue = "00";
            if (i == start) {
                if (Number(startTail) <= 30 && Number(startTail) != 0) {
                    tempValue = "30";
                } else if (Number(startTail) > 30) {
                    i++;
                }
            }
            let temp = {};
            temp.value = i + ":" + tempValue;
            temp.name = shopItem.name + "-" + i + ":" + tempValue;
            timeShow.push(temp);
            if (tempValue == "00") {
                if ((i == end && Number(endTail) >= 30) || (i < end)) {
                    tempValue = "30";
                    let temp2 = {};
                    temp2.value = i + ":" + tempValue;
                    temp2.name = shopItem.name + "-" + i + ":" + tempValue;
                    timeShow.push(temp2);
                }
            }
        }
        let rulesParams = {};
        rulesParams.shopItemId = shopItem.id;
        rulesParams.shopId = shopItem.shopId;
        onEvent('getPriceRule', rulesParams);
        this.setState({ timeShow: timeShow, shopItem: shopItem });

        const params = {};
        params.goodsId = goodsInfo.id;
        params.productGroupId = showShopItem.id;
        params.shopId = tempShopItem[0].shopId;
        params.productGroupProductId = tempShopItem[0].id;
        let date = new Date();
        let activationDate = moment(date).format('YYYY-MM-DD')
        if (EquityListObj.codeStatus !== '1') {
            activationDate = EquityListObj.actCodeTime;
        }
        params.outDate = EquityListObj.actOutDate;
        params.actExpireTime = EquityListObj.expiryDate;
        params.activationDate = moment(activationDate).format('YYYY-MM-DD');
        if (EquityListObj.codeStatus != '1' || (EquityListObj.expiryDate != null && EquityListObj.expiryDate != '')) {
            onEvent('queryBookBlock', params);
        } else {
            onEvent('queryOutCodeBookBlock', params);
        }
    }
    disabledDate = (current) => {
        // Can not select days before today and today
        const blockReasonList = this.props.blockReasonList;
        const { goodsInfo } = this.props;
        if (goodsInfo.allYear === '1') {
            return true;
        } else {
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

    }
    addPhone = (values) => {
        const { getFieldValue, setFieldsValue } = this.props.form;
        const { goodsInfo } = this.props;
        if (goodsInfo.onlySelf === "1") {
            setFieldsValue({ "giftPhone": values.target.value });
        }

    }
    addName = (values) => {
        const { getFieldValue, setFieldsValue } = this.props.form;
        const { goodsInfo } = this.props;
        if (goodsInfo.onlySelf === "1") {
            setFieldsValue({ "giftName": values.target.value });
        }

    }

    render() {
        const { groupProductList, resourceList, timeShow, isShow, stopReason } = this.state;
        const { getFieldDecorator } = this.props.form;
        const { onEvent, four, showShopItem, shopDetail, EquityListObj, blockReasonList, goodsInfo, serviceType, priceRule, reasonList, isOnclick } = this.props;

        return (isShow &&
            <Fragment>
                <div className="c-modal" style={{ marginTop: 0 }}>
                    {/* <div className="c-title">基本信息</div> */}
                    <Form onSubmit={this.handleSubmit}>
                        {EquityListObj && EquityListObj.codeStatus === '1' && <div>
                            <Divider style={{ margin: 0 }}>激活信息填写</Divider>
                            <Form.Item verticalGap={1} label="手机号" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >

                                {getFieldDecorator('activePhone', {
                                    rules: [{
                                        required: true,
                                        pattern: new RegExp(/^[0-9]\d*$/, "g"),
                                        message: '请输入客户激活的手机号'
                                    }, {
                                        min: 11,
                                        message: '手机号不能少于11个字符',
                                    }], initialValue: null
                                })(
                                    <Input maxLength={11} onChange={this.addPhone} style={{ width: "20%" }} placeholder="请输入客户激活的手机号" />

                                )}
                            </Form.Item>
                            <Form.Item style={{ marginBottom: 10 }} verticalGap={1} label="姓氏称呼" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >

                                {getFieldDecorator('activeName', { rules: [{ required: true, message: '请输入客户激活的姓氏称呼!' }], initialValue: null })(
                                    <Input onChange={this.addName} allowClear={true} style={{ width: "20%" }} placeholder="请输入客户激活的姓氏称呼" />

                                )}  &nbsp;&nbsp;
                         {getFieldDecorator('activeSex')(
                                    <RadioGroup >
                                        <Radio value={1} checked={true}>先生</Radio>
                                        <Radio value={0}>女士</Radio>
                                    </RadioGroup>
                                )}


                            </Form.Item>
                            <Divider style={{ margin: 0 }} />
                        </div>
                        }
                        <Form.Item verticalGap={1} label="查询可预约日期" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >
                            {getFieldDecorator('bookDate', {})
                                (
                                    <DatePicker style={{ width: '30%' }}
                                        format="YYYY-MM-DD"
                                        placeholder="预选择预订日期,初步筛选可用商户,可不选"
                                    />
                                )}
                            <Button type="primary" icon="search" onClick={this.queryBookShop}>查询</Button>
                            <br></br>
                            <p style={{color:"red",fontSize:"12px"}}>Tips：仅用于预筛选指定日期可用商户，方便客服选择可预约的商户，缩小选择范围，非必填，不填则不使用此功能。</p>
                        </Form.Item>

                        {four &&
                            <Form.Item verticalGap={1} label="商户选择" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >
                                {getFieldDecorator('shopId', { rules: [{ required: true, message: '请选择商户!' }] })
                                    (<Select showSearch onChange={this.selectShop}
                                        filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0} style={{ width: '30%' }}>
                                        {groupProductList &&
                                            groupProductList.map(item => (
                                                <Option key={item.id} value={item.id}>
                                                    {item.hotelName + (item.shopName ? "|" + item.shopName : "") + ("      (") + item.gift + (")")}

                                                </Option>
                                            ))
                                        }

                                    </Select>
                                    )} &nbsp;<label style={{ fontSize: 12, color: "blue" }}>{shopDetail && shopDetail.shopProtocol && shopDetail.shopProtocol.internal === 1 ? "公司资源" : null}
                                    {shopDetail && shopDetail.shopProtocol && shopDetail.shopProtocol.internal === 0 ? "第三方资源" : null}&nbsp;&nbsp;
                        {shopDetail && shopDetail.shop && shopDetail.shop.id && <Link to={{ pathname: "/addtwo/" + this.props.shopDetail.shop.id, query: {} }} target="_blank" className="fontBlue fontWeight" activeStyle={{ color: 'red' }}>
                                        进入当前商户</Link>}
                                </label>
                            </Form.Item>}
                        <Form.Item verticalGap={1} label="产品选择" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >
                            {getFieldDecorator('productId', { rules: [{ required: true, message: '请选择产品!' }] })(
                                <RadioGroup onChange={this.checkProduct} >
                                    {resourceList.map((temp, index) => {
                                        return (<Radio value={temp.productId}  >{temp.productName}</Radio>);
                                    })}

                                </RadioGroup>
                            )}
                        </Form.Item>


                        {stopReason == 1 && shopDetail && shopDetail.shopItemList && <Form.Item verticalGap={1} label="停售原因" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >
                            {
                                shopDetail.shopItemList.map((item, index) => {
                                    if (item.enable == 1) {
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
                            {getFieldDecorator('giftDate', { rules: [{ required: true, message: '请选择预订日期!' }] })
                                (
                                    <DatePicker style={{ width: '30%' }} disabledDate={this.disabledDate}
                                        // showTime={{ defaultValue: moment('00:00:00', 'HH:mm:ss') }}
                                        format="YYYY-MM-DD"
                                        placeholder="请选择预订日期"
                                    // onOpenChange={this.handleStartOpenChange}
                                    />
                                )}
                        </Form.Item>
                        <Form.Item verticalGap={1} label="预订时间" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >
                            {getFieldDecorator('giftTime', { rules: [{ required: true, message: '请选择预订时间!' }] })
                                (
                                    <Select
                                        filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0} style={{ width: '30%' }}>
                                        {timeShow &&
                                            timeShow.map(item => (
                                                <Option key={item.value} value={item.value}>{item.name}
                                                </Option>
                                            ))
                                        }

                                    </Select>

                                )}&nbsp;&nbsp;
                            {getFieldDecorator('giftPeopleNum', { initialValue: "2" })
                                (
                                    <Select style={{ width: '15%' }} showSearch={true} tabIndex={0}
                                        filterOption={(input, option) =>
                                            option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                                        } >
                                        <Option key={'1ren'} value="1">1人</Option>
                                        <Option key={'2ren'} value="2">2人</Option>
                                        <Option key={'3ren'} value="3">3人</Option>
                                        <Option key={'4ren'} value="4">4人</Option>
                                        <Option key={'5ren'} value="5">5人</Option>
                                        <Option key={'6ren'} value="6">6人</Option>
                                        <Option key={'7ren'} value="7">7人</Option>
                                        <Option key={'8ren'} value="8">8人</Option>
                                        <Option key={'9ren'} value="9">9人</Option>
                                        <Option key={'10ren'} value="10">10人</Option>
                                    </Select>

                                )}
                        </Form.Item>

                        <Form.Item verticalGap={1} label="使用人姓名" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >
                            {getFieldDecorator('giftName', { rules: [{ required: true, message: '请填写使用人姓名!' }], initialValue: EquityListObj.memberName })(
                                <Input allowClear={true} disabled={goodsInfo.onlySelf === '1' && EquityListObj.memberName ? true : false} style={{ width: "20%" }} placeholder="请填写使用人姓名" />
                            )}
                        </Form.Item>
                        {shopDetail && shopDetail.shop && shopDetail.shop.countryId !== 'CN' &&
                            <Form.Item verticalGap={1} label="使用人拼音" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >
                                {getFieldDecorator('detail.bookNameEn', { rules: [{ required: true, message: '请填写使用人拼音!' }] })(
                                    <Input allowClear={true} disabled={goodsInfo.onlySelf === '1' ? true : false} style={{ width: "20%" }} placeholder="请填写使用人拼音" />
                                )}

                            </Form.Item>
                        }
                        <Form.Item verticalGap={1} label="联系方式" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >
                            {getFieldDecorator('giftPhone', { rules: [{ required: true, message: '请填写联系方式!' }], initialValue: EquityListObj.memberPhone })(
                                <Input allowClear={true} disabled={goodsInfo.onlySelf === '1' ? true : false} style={{ width: "20%" }} placeholder="请填写联系方式" />
                            )}

                        </Form.Item>

                        <Form.Item verticalGap={1} label="备注" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >
                            {getFieldDecorator('reservRemark')(<TextArea style={{ width: '40%', height: 80 }} />)}
                            <div style={{ padding: '1% 0 1%' }}>
                                <Button type="primary" htmlType="submit" disabled={isOnclick}>提交预约单</Button> &nbsp;&nbsp;&nbsp;&nbsp;
                     <Button onClick={() => this.onEvent('cancel')} >取消</Button>
                            </div>
                        </Form.Item>

                    </Form>

                </div>
            </Fragment>

        );
    }
}

export default Buffet;