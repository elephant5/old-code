import React, { Component } from 'react';
import { Form, Select, Input, Button, Row, Col, Radio, Divider, message, Tag, Alert, Modal, InputNumber } from 'antd';
import { TimePicker } from 'antd';
import moment from 'moment';
import cookie from 'react-cookies';
const format = 'HH:mm';

const { Option } = Select;
const RadioGroup = Radio.Group;
const { TextArea, Search } = Input;

const formItemLayout = {
    labelCol: { span: 2 },
    wrapperCol: { span: 8 },
};
export const BMAP_NORMAL_MAP = window.BMAP_NORMAL_MAP;
export const BMAP_HYBRID_MAP = window.BMAP_HYBRID_MAP;
@Form.create()
class BaseInfo extends Component {

    state = {
        citys: [],// this.props.data.countryCity.filter(item => item.countryId === this.props.data.shop.countryId)[0].cityDetail,
        // 营业时间自定义，24小时,默认24小时
        isTime: [],//this.props.data.shop.closeTime === '24:00'? 1: 0,
        visible: false
    }
    componentDidMount() {
        const { shop } = this.props.data;
        if ( shop.shopType !== 'trip') {
            var BMap = window.BMap//取出window中的BMap对象
            let map = new BMap.Map("allmap");
            map.centerAndZoom(new BMap.Point(121.487899486, 31.24916171), 11);  // 初始化地图,设置中心点坐标和地图级别
            //添加地图类型控件
            map.addControl(new BMap.MapTypeControl({
                mapTypes: [
                    BMAP_HYBRID_MAP,//混合地图
                    BMAP_NORMAL_MAP//地图
                ]
            }));
            map.setCurrentCity("上海");          // 设置地图显示的城市 此项是必须设置的
            map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
            setTimeout(function () {
                map.enableDragging();   //两秒后开启拖拽
                map.enableInertialDragging();   //两秒后开启惯性拖拽
            }, 1000);

            setTimeout(function () {
                map.setZoom(14);
            }, 1000);
            if (shop.address) {
                this.getAddressMap(shop.address)
            }
        }
        this.setState({
            citys: this.props.data.shop.countryId ? this.props.data.countryCity.filter(item => item.countryId === this.props.data.shop.countryId)[0].cityDetail : [],
            // 营业时间自定义，24小时,默认24小时
            isTime: this.props.data.shop.closeTime ? this.props.data.shop.closeTime === '24:00' ? 1 : 0 : null,
        });
    }
    // 国家选择
    changeCountry = value => {
        const country = this.props.data.countryCity.filter(item => item.countryId === value);
        this.setState({
            citys: country[0].cityDetail
        })
    }

    // 保存基本信息
    handleSubmit = e => {
        e.preventDefault();
        const { onEvent, data } = this.props;
        this.props.form.validateFields((err, values) => {
            if (!err) {
                const { openTime, closeTime,checkInTime,checkOutTime, ...otherProps } = values;
                onEvent('baseInfo', {
                    ...otherProps,
                    id: data.shop.id,
                    openTime: openTime ? moment(openTime).format('HH:mm') : '00:00',
                    closeTime: closeTime ? moment(closeTime).format('HH:mm') : '24:00',
                    checkInTime: checkInTime ? moment(checkInTime).format('HH:mm') : checkInTime,
                    checkOutTime: checkOutTime ? moment(checkOutTime).format('HH:mm') : checkOutTime,
                })
            }
        });
    }
    // 选择营业时间
    selectTime = e => {
        this.setState({
            isTime: e.target.value
        })
    }
    // 表单绑定
    getForm = () => {
        const { getFieldDecorator } = this.props.form;
        const { shop } = this.props.data;
        let formProps = {};
        formProps.shopNameCh = getFieldDecorator('shopNameCh', {
            rules: [{
                required: true,
            }],
            initialValue: shop.name
        });
        formProps.shopNameEn = getFieldDecorator('shopNameEn', {
            initialValue: shop.nameEn
        });
        formProps.phone = getFieldDecorator('phone', {
            initialValue: shop.phone
        });
        formProps.level = getFieldDecorator('level', {
            initialValue: shop.level
        });
        formProps.countryId = getFieldDecorator('countryId', {
            // rules: [{
            //     required: true,
            // }],
            initialValue: shop.countryId
        });
        formProps.cityId = getFieldDecorator('cityId', {
            initialValue: shop.cityId || '',
            // rules: [{
            //     required: true,
            // }],
        });
        formProps.addressCh = getFieldDecorator('addressCh', {
            // rules: [{
            //     required: true,
            // }],
            initialValue: shop.address
        });
        formProps.addressEn = getFieldDecorator('addressEn', {
            initialValue: shop.addressEn
        });
        formProps.coordinate = getFieldDecorator('coordinate', {
        });
        formProps.summary = getFieldDecorator('summary', {
            initialValue: shop.summary
        });
        formProps.tips = getFieldDecorator('tips', {
            initialValue: shop.tips
        });
        formProps.notes = getFieldDecorator('notes', {
            initialValue: shop.notes
        });
        // 营业时间
        formProps.openTime = getFieldDecorator('openTime', {
            initialValue: shop.openTime ? moment(shop.openTime, format) : null
        });
        formProps.closeTime = getFieldDecorator('closeTime', {
            initialValue: shop.closeTime ? moment(shop.closeTime, format) : null
        });
        formProps.checkInTime = getFieldDecorator('checkInTime', {
            initialValue: shop.checkInTime ? moment(shop.checkInTime, format) : null
        });
        formProps.checkOutTime = getFieldDecorator('checkOutTime', {
            initialValue: shop.checkOutTime ? moment(shop.checkOutTime, format) : null
        });
        return formProps;
    }

    getAddressMap = value => {
        const { getFieldValue, setFieldsValue } = this.props.form;

        var BMap = window.BMap//取出window中的BMap对象
        let map = new BMap.Map("allmap");
        if (value) {
            // 创建地址解析器实例     
            var myGeo = new BMap.Geocoder();
            // 将地址解析结果显示在地图上，并调整地图视野    
            myGeo.getPoint(value, function (point) {
                if (point) {
                    setFieldsValue({ "coordinate": point.lng + "," + point.lat });
                    map.centerAndZoom(point, 16);
                    map.addOverlay(new BMap.Marker(point));
                } else {
                    message.error("输入的地址有误，请重新输入！");
                    setFieldsValue({ "coordinate": null, "addressCh": null });
                }
            },
                "上海市");
        } else {
            message.error("输入的地址有误，请重新输入！");
            setFieldsValue({ "coordinate": null, "addressCh": null });
        }
    }
    checkNumber = value => {

        const { getFieldValue, setFieldsValue } = this.props.form;
        const max = getFieldValue("maxBookDays");
        const min = getFieldValue("minBookDays");
        if (max > min) {
            this.setState({
                visible: true,
            });
            // setFieldsValue({"minBookDays":max});
        }

    }
    initNumber = value => {
        const { getFieldValue, setFieldsValue } = this.props.form;
        const max = getFieldValue("maxBookDays");
        setFieldsValue({ "minBookDays": max });
    }
    handleOk = (e) => {
        const { getFieldValue, setFieldsValue } = this.props.form;
        const max = getFieldValue("maxBookDays");
        setFieldsValue({ "minBookDays": max });
        this.setState({
            visible: false,
        });
    }

    handleCancel = (e) => {
        const { getFieldValue, setFieldsValue } = this.props.form;
        setFieldsValue({ "minBookDays": null });
        this.setState({
            visible: false,
        });
    }
    render() {
        const formProps = this.getForm();
        const { countryCity, shop } = this.props.data;
        const { getFieldDecorator } = this.props.form;
        const infoEdit = cookie.load("KLF_PG_RM_SL_INFO_EDIT");
        return (
            <div className="c-modal">
                <div className="c-title">基本信息</div>
                <Form onSubmit={this.handleSubmit}>
                    <br></br>
                    {shop.shopNature === 0 && shop.shopType !== 'trip' &&  <Form.Item
                        label="隶属酒店："
                        labelCol={{ span: 2 }}
                        wrapperCol={{ span: 18 }}
                    >
                        <label style={{ color: '#108ee9', fontSize: '24px' }}>{shop.hotelName}</label>
                    </Form.Item>}
                    {shop.shopNature === 0 && shop.shopType !== 'trip' &&  <Divider dashed={true} />}
                    <Form.Item
                        label="商户类型："
                        labelCol={{ span: 2 }}
                        wrapperCol={{ span: 18 }}
                    >
                        <Tag color={"#108ee9"} key={shop.shopServiceName} >{shop.shopServiceName}</Tag>
                    </Form.Item>
                    <Form.Item
                        label="商户名称1："
                        labelCol={{ span: 2 }}
                        wrapperCol={{ span: 18 }}
                    >
                        {formProps.shopNameCh(
                            <Input placeholder="请输入商户名称（中文）" style={{ width: '49%' }} />
                        )}&nbsp;&nbsp;&nbsp;&nbsp;
                                    {formProps.shopNameEn(
                            <Input placeholder="请输入商户名称（英文）" style={{ width: '49%' }} />
                        )}
                    </Form.Item>
                    {shop.shopType !== 'trip' &&  <div >
                        <Form.Item
                            label="电话："
                            {...formItemLayout}
                        >
                            {formProps.phone(
                                <Input placeholder="请输入电话" style={{ width: '49%' }} />
                            )}
                        </Form.Item>
                        <Form.Item
                            label="商户级别："
                            {...formItemLayout}
                        >
                            {formProps.level(
                                <Select
                                    style={{ width: '49%' }}
                                    placeholder='请选择'
                                >
                                    <Option value="A" >A类</Option>
                                    <Option value="B">B类</Option>
                                </Select>
                            )}
                        </Form.Item>

                        <Divider dashed={true} />
                        <Row >
                            <Col span={12} order={2}>
                                <Form.Item
                                    label="城市："
                                    labelCol={{ span: 4 }}
                                    wrapperCol={{ span: 16 }}
                                >
                                    {formProps.countryId(
                                        <Select
                                            onChange={this.changeCountry}
                                            style={{ width: '49%' }}
                                            placeholder="请选择国家" showSearch
                                            filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}
                                        >
                                            {
                                                countryCity.map(item => (
                                                    <Option key={item.countryId} value={item.countryId}>{item.countryName}</Option>
                                                ))
                                            }
                                        </Select>
                                    )}&nbsp;&nbsp;&nbsp;
                                {formProps.cityId(
                                        <Select style={{ width: '49%' }} placeholder="请选择城市" showSearch
                                            filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                                            {
                                                this.state.citys.map(item => (
                                                    <Option key={item.cityId} value={item.cityId}>{item.cityName}</Option>
                                                ))
                                            }
                                        </Select>
                                    )}
                                </Form.Item>
                            </Col>
                            <Col span={12} order={2}>
                                <div id={"allmap"} style={{ width: '100%', height: 280, border: 'white solid 1px', float: "left", position: 'absolute', paddingRight: '10px' }}></div>
                            </Col>
                        </Row>
                        <Form.Item
                            label="地址："
                            {...formItemLayout}
                        >
                            {formProps.addressCh(
                                <Search placeholder="请输入地址" onSearch={this.getAddressMap} enterButton />
                            )}
                        </Form.Item>
                        <Form.Item
                            label="英文地址："
                            {...formItemLayout}
                        >
                            {formProps.addressEn(
                                <Input placeholder="请输入酒店地址（英文）" />
                            )}
                        </Form.Item>
                        <Form.Item
                            label="坐标："
                            {...formItemLayout}
                        >
                            {formProps.coordinate(
                                <Input disabled={true} />
                            )}
                        </Form.Item>
                        <br />
                    </div>}
                    <Divider dashed={true} />
                    {shop.shopType !== 'trip'  && shop.shopType !== 'coupon'  &&<div >
                        <Form.Item label="营业时间：" {...formItemLayout}>
                            <RadioGroup defaultValue={this.props.data.shop.closeTime === '24:00' ? 1 : 0} onChange={this.selectTime}>
                                <Radio value={1}>24小时</Radio>
                                <Radio value={0}>自定义</Radio>
                            </RadioGroup>
                            {
                                !this.state.isTime && <div>
                                    {formProps.openTime(
                                        <TimePicker format={format} />
                                    )}&nbsp;&nbsp;&nbsp;&nbsp;
                                    ~&nbsp;&nbsp;&nbsp;&nbsp;
                                    {formProps.closeTime(
                                        <TimePicker format={format} />
                                    )}
                                </div>

                            }  
                        </Form.Item>
                        {shop.shopType === 'accom' && <Form.Item label="入住时间：" {...formItemLayout}>
                            {
                                <div>
                                    {formProps.checkInTime(
                                        <TimePicker format={format} />
                                    )}&nbsp;&nbsp;&nbsp;&nbsp;
                                    离店时间：&nbsp;&nbsp;&nbsp;&nbsp;
                                    {formProps.checkOutTime(
                                        <TimePicker format={format} />
                                    )}
                                </div>
                            }
                        </Form.Item>}
                        <Form.Item label="预约限制" labelCol={{ span: 2 }} wrapperCol={{ span: 8 }} >
                            <span >最少提前</span>&nbsp;&nbsp;
                        {getFieldDecorator('minBookDays', { initialValue: shop.minBookDays })(<InputNumber min={0} max={100} onBlur={this.initNumber} />)} &nbsp;&nbsp;天
                        <span style={{ marginLeft: '20px' }}>最多提前</span>&nbsp;&nbsp;
                        {getFieldDecorator('maxBookDays', { initialValue: shop.maxBookDays })(<InputNumber min={0} max={100} onBlur={this.checkNumber} />)} &nbsp;&nbsp;天
                        <span style={{ color: 'grey', marginLeft: '20px', fontSize: '12px', }}>不填表示没有限制</span>
                            <Modal
                                title="预约限制提示"
                                visible={this.state.visible}
                                onOk={this.handleOk}
                                onCancel={this.handleCancel}
                                cancelText="取消"
                                okText="确定"
                            >
                                <Alert message="预约限制提示：您当前填写的最多提前的天数不可比最少提前的天数小，请重新修改！(默认大于等于最多提前天数或者不填)" type="error" />
                            </Modal>
                        </Form.Item>
                        </div>}
                        <Form.Item
                            label="商户介绍："
                            labelCol={{ span: 2 }}
                            wrapperCol={{ span: 16 }}
                        >
                            {formProps.summary(
                                <TextArea placeholder="请输入商户介绍内容" rows={6} />
                            )}
                        </Form.Item>
                        <Form.Item
                            label="录单提示："
                            labelCol={{ span: 2 }}
                            wrapperCol={{ span: 16 }}
                        >
                            {formProps.tips(
                                <TextArea placeholder="请输入录单提示内容" rows={6} />
                            )}
                        </Form.Item>
                    
                    <Form.Item
                        label="备注："
                        labelCol={{ span: 2 }}
                        wrapperCol={{ span: 16 }}
                    >
                        {formProps.notes(
                            <TextArea placeholder="请输入备注内容" rows={6} />
                        )}
                    </Form.Item>

                    <Form.Item
                        wrapperCol={{
                            span: 4, offset: 4
                        }}
                    >
                        {infoEdit && <Button style={{ marginBottom: 20 }}
                            type="primary"
                            htmlType="submit"
                            loading={this.props.loading}
                        >
                            保存
                        </Button>}
                        <Button style={{ marginLeft: 20, marginBottom: 20 }}>
                            重置
                        </Button>
                    </Form.Item>
                </Form>
            </div>
        );
    }
}

export default BaseInfo;