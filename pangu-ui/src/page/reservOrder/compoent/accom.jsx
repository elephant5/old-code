import React, { Fragment, Component } from 'react';
import { Link } from 'react-router';
import moment from 'moment'
import { Form, TimePicker, Select, Input, Button, Divider, message, Row, Col, Radio, Alert, Checkbox, DatePicker, Tag, Card, Tooltip } from 'antd';
const Option = Select.Option;

const { TextArea } = Input;

const RadioGroup = Radio.Group;

@Form.create()
class Accom extends Component {
    constructor(props) {
        super(props);
        this.state = {
            groupProductList: [],
            resourceList: [],
            timeShow: [],
            groupProduct: {},//当前页面选的商户？或者产品
            deparDateList: [],
            isShow: true,
            rooms: [],
            checkNight: "1",
            isShowShopList: false,
            showShopItem: [],
            shopItemIdTemp: null,
            stopReason: 0,
            bookFlag: false,
        };
    }
    componentDidMount() {
        const { showShopItem, serviceType } = this.props;
        if (showShopItem) {
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
            let rooms = []
            for (let i = 1; i <= 10; i++) {
                let params = {};
                params.value = i + "";
                params.name = i + "间"
                rooms.push(params);
            }
            this.setState({ groupProductList: groupProductList, showShopItem: showShopItem, rooms: rooms });
        }

    }

    getShopMsg(){
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
        const { shopDetail, showShopItem, serviceType, isShowShopList, canBookShopIds } = nextProps;
        const { bookFlag } = this.state;
        let tempFlag = bookFlag;
        // nextProps.form.resetFields();
        // if (showShopItem.id !== this.state.showShopItem.id) {
        // }
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
        this.setState({ groupProductList: groupProductList, showShopItem: showShopItem });

        // var b = (JSON.stringify(shopDetail) == "{}");
        //     console.log("resourceList",this.state.resourceList);
        //     if(!b){
        //             const { getFieldValue, setFieldsValue } = this.props.form;
        //             let stopItem = shopDetail.shopItemList.filter(item =>  item.enable != 1);
        //             if(stopItem){
        //                 let tempResource =   this.state.resourceList.filter(item =>item.shopItemId  === stopItem[0].id );
        //                 var a = (JSON.stringify(tempResource) == "[]");
        //                 if(!a){
        //                     setFieldsValue({ "productId":  tempResource[0].productId});
        //                 }else{
        //                     // let tempResources =   this.state.resourceList.filter(item =>item.shopItemId  !== stopItem[0].id );
        //                     // // message.error("当前产品已被停售，请重新选择！");
        //                     // setFieldsValue({ "productId":  tempResources[0].productId});
        //                 }
        //           } 
        //     }
    }

    // 保存基本信息
    handleSubmit = e => {
        e.preventDefault();
        const { onEvent, four, showShopItem, shopDetail, EquityListObj, blockReasonList, goodsInfo, serviceType, goodsGroupList, productGroupProduct } = this.props;
        const { deparDateList } = this.state;
        this.props.form.validateFields((err, values) => {
            const productGroup = goodsGroupList.filter(item => item.shopItemStyle === true);
            let deparDate = deparDateList.filter(item => values.deparDate === item.value);
            let maxNigh = deparDate[0].night * values.checkNight;
            let flag = this.juageNight(values.checkNight, values.deparDate);
            if (!flag) {
                return false;
            }
            const tempShopItem = showShopItem.groupProductList.filter(item => item.productId === values.productId);
            let tempTimes = tempShopItem[0].pointOrTimes ? tempShopItem[0].pointOrTimes : 1;
            let times = tempTimes * values.checkNight;
            let last = showShopItem.totalCount - showShopItem.useCount;
            if (tempTimes) {

                if (times > last) {
                    message.error("您当前次数或者点数不足，请重新选择！");
                    return false;
                }
            }
            const num = productGroupProduct.gift.substring(1);
            if (num === 'X') {
                times = tempTimes * maxNigh;
                if (times > last) {
                    message.error("您当前次数或者点数不足，请重新选择！");
                    return false;
                }
            }
            if (tempShopItem[0].status == 1) {
                message.error("当前产品已被停售，请重新选择！");
                return false;

            }
            //点数不足
            if (!err) {
                const shopItem = shopDetail.shopItemList.filter(item => item.id === tempShopItem[0].shopItemId)[0];
                if (!shopItem) {
                    message.error("未找到当前房型");
                    return false;
                }
                values.serviceType = serviceType.type;
                values.superposition = goodsInfo.superposition;
                values.singleThread = goodsInfo.singleThread;
                values.nightNumbers = deparDate[0].night;
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
                values.exchangeNum = times;//tempShopItem[0].pointOrTimes;
                values.shopItemId = shopItem.id;
                onEvent('insertReservOrder', values);


            }
        });
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
        const { onEvent, showShopItem, shopDetail, EquityListObj, goodsInfo, productGroupProduct } = this.props;

        let rooms = []
        if (goodsInfo && goodsInfo.enableMaxNight && goodsInfo.enableMaxNight !== '0') {
            for (let i = 1; i <= goodsInfo.maxNight; i++) {
                let params = {};
                params.value = i + "";
                params.name = i + "间"
                rooms.push(params);
            }
        } else {
            for (let i = 1; i <= 10; i++) {
                let params = {};
                params.value = i + "";
                params.name = i + "间"
                rooms.push(params);
            }
        }
        const groupProduct = showShopItem.groupProductList.filter(item => item.id === values);//商品中产品资源的产品组和产品关联数据
        onEvent('findProductGroupProductById', groupProduct[0].id);
        let pointOrTimes = groupProduct[0].pointOrTimes;
        if (showShopItem.useLimitId !== 'none') {
            let temp = showShopItem.totalCount - showShopItem.useCount;
            if (pointOrTimes > temp) {
                message.error("您当前的点数或者次数不够，请重新选择！");
                return false;
            }
        }
        this.setState({ stopReason: 0 })
        if (groupProduct[0].status == 1) {
            this.setState({ stopReason: 1 })
            onEvent('getShopDetail', groupProduct[0].shopId);
            message.error("当前产品已被停售，请重新选择！");
            return false;

        }

        let rulesParams = {};
        rulesParams.shopItemId = groupProduct[0].shopItemId;
        rulesParams.shopId = groupProduct[0].shopId;
        onEvent('getPriceRule', rulesParams);
        onEvent('getShopDetail', groupProduct[0].shopId);
        onEvent('getBlockReason', groupProduct[0].shopItemId);
        const resourceList = showShopItem.groupProductList.filter(item => item.shopId === groupProduct[0].shopId);
        this.setState({ resourceList: resourceList, groupProduct: groupProduct, rooms: rooms, deparDateList: [] });
        const { getFieldValue, setFieldsValue } = this.props.form;
        setFieldsValue({ "productId": resourceList[0].productId });
        setFieldsValue({ "deparDate": null });
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
        //区分已激活的码和已出库的码调用方法
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
        const { onEvent, showShopItem, shopDetail, goodsInfo, EquityListObj } = this.props;
        const tempShopItem = showShopItem.groupProductList.filter(item => item.productId === e.target.value);
        const shopItem = shopDetail.shopItemList.filter(item => item.id === tempShopItem[0].shopItemId)[0];
        if (!shopItem) {
            message.error("未找到当前房型");
            return false;
        }
        let timeShow = [];
        this.setState({ stopReason: 0 })
        if (tempShopItem[0].status == 1) {
            this.setState({ stopReason: 1 });
            this.setState({ timeShow: [] });
            message.error("当前产品已被停售，请重新选择！");
            return false;

        }
        onEvent('findProductGroupProductById', tempShopItem[0].id);
        onEvent('getBlockReason', shopItem.id);
        let rulesParams = {};
        rulesParams.shopItemId = shopItem.id;
        rulesParams.shopId = shopItem.shopId;
        onEvent('getPriceRule', rulesParams);

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
        //区分已激活的码和已出库的码调用方法
        if (EquityListObj.codeStatus != '1' || (EquityListObj.expiryDate != null && EquityListObj.expiryDate != '')) {
            onEvent('queryBookBlock', params);
        } else {
            onEvent('queryOutCodeBookBlock', params);
        }

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
        const { goodsInfo } = this.props;
        if (goodsInfo.allYear === '1') {
            return current < moment().endOf('day');
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
                //   return current && current < moment().endOf('day') ;
                return true;
            }
        }

    }
    onStartChange = (values, dateString) => {
        const { getFieldValue, setFieldsValue } = this.props.form;
        const { onEvent, showShopItem, shopDetail, EquityListObj, goodsInfo, blockReasonList, productGroupProduct } = this.props;
        let rooms = [];
        let jian = [];
        const num = productGroupProduct.gift.substring(1)
        if (goodsInfo && goodsInfo.enableMaxNight && goodsInfo.enableMaxNight !== '0') {
            for (let i = 1; i <= goodsInfo.maxNight; i++) {
                let params = {};
                let time = moment(values).add('days', i).format('YYYY-MM-DD');
                params.value = time;
                params.name = (i + 1) + "天" + i + "晚（" + time + "离店）";
                params.night = i;
                rooms.push(params);
            }
        } else {
            if (num !== 'X') {
                let params = {};
                let time = moment(values).add('days', num).format('YYYY-MM-DD');
                if (blockReasonList && blockReasonList.bookDates && blockReasonList.bookDates.length > 0) {
                    blockReasonList.bookDates.map(item => {
                        let itemTime = moment(item).add('days', num).format('YYYY-MM-DD');
                    });
                }
                params.value = time;
                params.name = (Number(num) + 1) + "天" + num + "晚（" + time + "离店）";
                params.night = num;

                rooms.push(params);



            } else {
                for (let i = 1; i < 10; i++) {
                    let params = {};
                    let time = moment(values).add('days', i).format('YYYY-MM-DD');
                    if (blockReasonList && blockReasonList.bookDates && blockReasonList.bookDates.length > 0) {
                        blockReasonList.bookDates.map(item => {
                            let itemTime = moment(item).add('days', i).format('YYYY-MM-DD');
                        });
                    }
                    params.value = time;
                    params.name = (i + 1) + "天" + i + "晚（" + time + "离店）";
                    params.night = i;

                    rooms.push(params);
                }
            }
        }
        this.setState({ deparDateList: rooms }, () => {
            this.juageNight(getFieldValue("checkNight"), rooms[0].value);
        });
        setFieldsValue({ "deparDate": rooms[0].value });

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
    onLeftChange = values => {
        const { getFieldValue } = this.props.form;
        const { deparDateList, blockReasonList } = this.state;
        const checkNight = getFieldValue("checkNight");
        this.juageNight(checkNight, values);
        if (blockReasonList && blockReasonList.bookDates && blockReasonList.bookDates.length > 0) {
            let deparDate = deparDateList.filter(item => deparDate === item.value);
            if (deparDate.night) {

            }
        }
    }
    onCheckNight = values => {
        const { getFieldValue } = this.props.form;
        const deparDate = getFieldValue("deparDate");
        this.juageNight(values, deparDate);

    }
    //最大最小间夜数的判断
    juageNight = (checkNight, deparDate) => {
        const { goodsInfo, productGroupProduct } = this.props;
        const { deparDateList } = this.state;
        const num = productGroupProduct.gift.substring(1);
        let chooseDate = deparDateList.filter(item => item.value === deparDate);
        let night = 1;
        if (!!chooseDate && chooseDate.length != 0) {
            night = chooseDate[0].night;
        }
        let totalCount = checkNight * night;
        if (num === 'X' && goodsInfo && goodsInfo.enableMaxNight && goodsInfo.enableMaxNight !== '0') {
            if (totalCount > goodsInfo.maxNight) {
                message.error("当前只能选择最大间夜数为：" + goodsInfo.maxNight + "间夜");
                return false;
            }
        }
        if (num === 'X' && goodsInfo && goodsInfo.enableMinNight && goodsInfo.enableMinNight !== '0') {
            if (totalCount < goodsInfo.minNight) {
                message.error("当前只能选择最小间夜数为：" + goodsInfo.minNight + "间夜");
                return false;
            }
        }
        return true;
    }
    render() {
        const { groupProductList, resourceList, timeShow, rooms, deparDateList, isShow, stopReason } = this.state;
        const { getFieldDecorator } = this.props.form;
        const { onEvent, four, showShopItem, shopDetail, EquityListObj, blockReasonList, goodsInfo, priceRule, reasonList, serviceType, isOnclick, productGroupProduct } = this.props;
        const formItemLayout = {
            labelCol: {
                xs: { span: 24 },
                sm: { span: 6 },
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 16 },
            },
        };
        return (isShow &&
            <Fragment>
                <div className="c-modal" style={{ marginTop: 0 }}>
                    {/* <div className="c-title">基本信息</div> */}
                    <Form onSubmit={this.handleSubmit} layout="horizontal">
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
                                    <Input maxLength={11} onChange={this.addPhone} allowClear={true} style={{ width: "20%" }} placeholder="请输入客户激活的手机号" />

                                )}
                            </Form.Item>
                            <Form.Item style={{ marginBottom: 10 }} verticalGap={1} label="姓氏称呼" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >

                                {getFieldDecorator('activeName', { rules: [{ required: true, message: '请输入客户激活的姓氏称呼!' }], initialValue: null })(
                                    <Input onChange={this.addName} allowClear={true} style={{ width: "20%" }} placeholder="请输入客户激活的姓氏称呼" />

                                )}   &nbsp;&nbsp;
                         {getFieldDecorator('activeSex')(
                                    <RadioGroup >
                                        <Radio value={1} checked={true} >先生</Radio>
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
                                    {shopDetail && shopDetail.shopProtocol && shopDetail.shopProtocol.internal === 0 ? "第三方资源" : null} &nbsp;&nbsp;

                                    {shopDetail && shopDetail.shop && shopDetail.shop.id && <Link to={{ pathname: "/addtwo/" + this.props.shopDetail.shop.id, query: {} }} target="_blank" className="fontBlue fontWeight" activeStyle={{ color: 'red' }}>
                                        进入当前商户</Link>}

                                </label>
                            </Form.Item>}

                        <Form.Item verticalGap={1} label="产品选择" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >
                            {getFieldDecorator('productId', { rules: [{ required: true, message: '请选择产品!' }] })(
                                <RadioGroup onChange={this.checkProduct} >
                                    {resourceList.map((temp, index) => {
                                        return (<Radio value={temp.productId} checked={true}>{temp.productName}{temp.addon && "|" + temp.addon}{temp.needs && "|" + temp.needs}</Radio>);
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
                                            </Tag><br />
                                        </div>)
                                })
                            }
                        </Form.Item>}
                        {blockReasonList && blockReasonList.blockRule && <Form.Item verticalGap={1} label="不可预订" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >
                            <label style={{ color: 'red' }}>{blockReasonList.blockRule}</label>
                        </Form.Item>}
                        {reasonList && reasonList.length > 0 && <Form.Item verticalGap={1} label="关房关餐" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >
                            {reasonList.map(item => {
                                return (<label style={{ color: 'red' }}>item.block +":" + item.reason <br /></label>)
                            })}
                        </Form.Item>}
                        <Form.Item verticalGap={1} label="预订日期" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >
                            {getFieldDecorator('giftDate', {})
                                (
                                    <DatePicker style={{ width: '30%' }} disabledDate={this.disabledDate}
                                        format="YYYY-MM-DD"
                                        placeholder="请选择预订日期"
                                        onChange={this.onStartChange}
                                        onOpenChange={this.handleStartOpenChange} />
                                )}
                        </Form.Item>
                        <Row gutter={18}>
                            <Col xl={8} md={12} sm={24}>
                                <Form.Item label="离店日期：" {...formItemLayout}   >
                                    {getFieldDecorator('deparDate', {})
                                        (
                                            <Select onChange={this.onLeftChange}
                                                filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0} style={{ width: '85%' }}>
                                                {deparDateList && deparDateList.length > 0 &&
                                                    deparDateList.map(item => (
                                                        <Option key={item.value} value={item.value}>{item.name}
                                                        </Option>
                                                    ))
                                                }

                                            </Select>

                                        )}
                                </Form.Item>
                            </Col>
                            <Col xl={4} md={12} sm={24}>
                                <Form.Item label="" {...formItemLayout}   >
                                    {getFieldDecorator('checkNight', { initialValue: this.state.checkNight })
                                        (
                                            <Select onChange={this.onCheckNight} showSearch={true} tabIndex={0}
                                                filterOption={(input, option) =>
                                                    option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                                                } >
                                                {rooms && rooms.map((item, index) => {
                                                    return (<Option value={item.value} >{item.name}</Option>);
                                                })}
                                            </Select>)}
                                </Form.Item>
                            </Col>
                        </Row>

                        <Form.Item verticalGap={1} label="床型" labelCol={{ span: 2 }} wrapperCol={{ span: 22 }} direction="horizontal" indicatorGap >
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
                                <Button type="primary" htmlType="submit" disabled={isOnclick} >提交预约单</Button> &nbsp;&nbsp;&nbsp;&nbsp;
                     <Button onClick={() => this.onEvent('cancel')} >取消</Button>
                            </div>
                        </Form.Item>

                    </Form>

                </div>
            </Fragment>

        );
    }
}

export default Accom;