import React, { Component } from 'react';
import { Form, Input, Button, Select, Row, Col, Icon, DatePicker, Tag, InputNumber, Divider, Radio } from 'antd';
import moment from 'moment';
import AddModal from './add-modal';
import EditModal from './edit-modal';
import { unique } from '../../../util/index';
import cookie from 'react-cookies';
const RadioGroup = Radio.Group;
const { RangePicker } = DatePicker;
const { Option } = Select;
const { TextArea, Password } = Input;

const formItemLayout = {
    labelCol: { span: 8 },
    wrapperCol: { span: 12 },
};
const formItemLayout1 = {
    labelCol: { span: 6 },
    wrapperCol: { span: 10 },
};
const formItemLayout2 = {
    labelCol: { span: 2 },
    wrapperCol: { span: 8 },
};
const formItemLayout3 = {
    labelCol: { span: 2 },
    wrapperCol: { span: 18 },
};

@Form.create({})
class Protocol extends Component {
    constructor(props) {
        super(props);
        this.state = {
            // block弹框
            isShowAddModal: false,
            isShowEditModal: false,
            // block规则
            blockRuleList: this.props.data.shopProtocol.blockRuleList || [],
            shopProtocol: {},
            // 编辑框数据
            editParams: {}
        };
    }
    componentDidMount() {
        const { shopProtocol, } = this.props.data;
        this.setState({
            shopProtocol: this.props.data.shopProtocol
        });

    }
    handleSubmit = e => {
        e.preventDefault();
        const { onEvent, data } = this.props;
        const { blockRuleList } = this.state;

        this.props.form.validateFields((err, values) => {
            if (!err) {
                const { time, ...otherProps } = values;
                const contractStart = time[0] != null ? moment(time[0]).format('YYYY-MM-DD') : undefined;
                const contractExpiry = time[1] != null ? moment(time[1]).format('YYYY-MM-DD') : undefined;
                onEvent('protocol', {
                    ...data.shopProtocol,
                    // 覆盖
                    contractStart,
                    contractExpiry,
                    ...otherProps,
                    blockRuleList
                })
            }
        })
    }

    getForm = () => {
        const { getFieldDecorator } = this.props.form;
        const { shopProtocol } = this.state;
        let formProps = {};
        formProps.shopChannelId = getFieldDecorator('shopChannelId', {
            rules: [{
                required: true,
            }],
            initialValue: shopProtocol.shopChannelId
        });
        formProps.principal = getFieldDecorator('principal', {
            initialValue: shopProtocol.principal
        });
        formProps.imprest = getFieldDecorator('imprest', {
            initialValue: shopProtocol.imprest
        });
        formProps.deposit = getFieldDecorator('deposit', {
            initialValue: shopProtocol.deposit
        });
        // 合同有效期，传 contractExpiry，contractStart字段
        formProps.time = getFieldDecorator('time', {
            rules: [{
                required: shopProtocol.internal == 1 ? true : false,
            }],
            initialValue: [
                shopProtocol.contractStart ? moment(shopProtocol.contractStart) : null,
                shopProtocol.contractExpiry ? moment(shopProtocol.contractExpiry) : null,
            ]
        });
        formProps.settleMethod = getFieldDecorator('settleMethod', {
            rules: [{
                required: shopProtocol.internal == 1 ? true : false,
                message: "请输入结算方式"
            }],
            initialValue: shopProtocol.settleMethod || undefined
        });
        formProps.currency = getFieldDecorator('currency', {
            rules: [{
                required: shopProtocol.internal == 1 ? true : false,
                message: "请输入结算币种"
            }],
            initialValue: shopProtocol.currency || undefined
        });
        formProps.decimal = getFieldDecorator('decimal', {
            initialValue: shopProtocol.decimal
        });
        formProps.roundup = getFieldDecorator('roundup', {
            initialValue: shopProtocol.roundup || undefined
        });
        formProps.accountName = getFieldDecorator('accountName', {
            initialValue: shopProtocol.accountName
        });
        formProps.openingBank = getFieldDecorator('openingBank', {
            initialValue: shopProtocol.openingBank
        });
        formProps.bankAccount = getFieldDecorator('bankAccount', {
            initialValue: shopProtocol.bankAccount
        });
        formProps.changePrice = getFieldDecorator('changePrice', {
            initialValue: shopProtocol.changePrice ? shopProtocol.changePrice : 0
        });
        formProps.parking = getFieldDecorator('parking', {
            initialValue: shopProtocol.parking
        });
        formProps.children = getFieldDecorator('children', {
            initialValue: shopProtocol.children
        });
        formProps.notice = getFieldDecorator('notice', {
            initialValue: shopProtocol.notice
        });
        return formProps;
    }
    // 关闭block框
    onCancel = () => {
        this.setState({
            isShowAddModal: false
        })
    }
    onEditCancel = () => {
        this.setState({
            isShowEditModal: false
        })
    }
    onOk = params => {
        const rules = [...this.state.blockRuleList, ...params.festival, ...params.blockRule];
        const norepeatRule = unique(rules);
        this.setState({
            blockRuleList: norepeatRule,
            isShowAddModal: false
        })
    }
    // 编辑block保存
    handleEdit = params => {
        // 先删除当前条，在覆盖
        this.setState({
            blockRuleList: this.state.blockRuleList.filter(item => item.rule !== params.deleteData.rule)
        }, () => {
            const rules = [...this.state.blockRuleList, ...params.festival, ...params.blockRule];
            const norepeatRule = unique(rules);
            this.setState({
                blockRuleList: norepeatRule,
                isShowEditModal: false
            })
        })
    }
    // 弹出新增block框
    addBlock = () => {
        this.setState({
            isShowAddModal: true
        })
    }
    // 删除block
    onClose = params => {
        this.setState({
            blockRuleList: this.state.blockRuleList.filter(item => item.rule !== params.rule)
        })
    }
    // 打开编辑block
    editBlock = item => {
        this.setState({
            isShowEditModal: true,
            editParams: item
        })
    }
    changeChannels = value => {
        const { data } = this.props;
        const { shopProtocol } = this.state;

        data.channels.map(item => {
            if (item.id === value) {
                shopProtocol.internal = item.internal;
            }
        });
        this.setState({
            shopProtocol: shopProtocol
        })
    }

    render() {
        const formProps = this.getForm();
        const { data, shop, allSysDictList } = this.props;
        const { isShowAddModal, blockRuleList, isShowEditModal, editParams, shopProtocol } = this.state;
        const { getFieldDecorator } = this.props.form;
        const edit = cookie.load("KLF_PG_RM_SL_PROTOCOL_EDIT");
        return (
            <div className="c-modal">
                {
                    // 新增block弹框
                    isShowAddModal &&
                    <AddModal
                        onCancel={this.onCancel}
                        onOk={this.onOk}
                        allSysDictList={allSysDictList}
                        data={{ festivalList: data.festivalList, allSysDictList }}
                    />
                }
                {
                    // 编辑block框
                    isShowEditModal &&
                    <EditModal
                        onCancel={this.onEditCancel}
                        onOk={this.handleEdit}
                        data={{ festivalList: data.festivalList, editParams, allSysDictList }}
                    />
                }
                <h2 className="mid-title">协议信息</h2>
                <Divider />
                <Form onSubmit={this.handleSubmit}>
                    {shopProtocol.internal === 1 && <Form.Item
                        label="商户渠道："
                        labelCol={{ span: 2 }}
                        wrapperCol={{ span: 16 }}
                    >
                        {formProps.shopChannelId(
                            <Select showSearch onSelect={this.changeChannels}
                                filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}
                                style={{ width: 140 }}>
                                {data.channels.map(item => <Option value={item.id} key={item.id}>{item.name}</Option>)
                                }
                            </Select>
                        )}
                        <label style={{ marginLeft: "12px" }}>负责人：</label>
                        {formProps.principal(
                            <Input
                                placeholder="请输入"
                                style={{ width: 140 }}
                            />
                        )}    <label style={{ marginLeft: "10px" }}>预付款：</label>
                        {formProps.imprest(
                            <InputNumber
                                placeholder="请输入"
                                style={{ width: 140 }}
                                prefix={<Icon type="money-collect" />}
                            />
                        )}     <label style={{ marginLeft: "10px" }}>押金：</label>
                        {formProps.deposit(
                            <InputNumber
                                placeholder="请输入"
                                style={{ width: 140 }}
                                prefix={<Icon type="money-collect" />}
                            />
                        )}
                    </Form.Item>}
                    { shopProtocol.internal === 0 && <Form.Item
                        label="商户渠道："
                        labelCol={{ span: 2 }}
                        wrapperCol={{ span: 16 }}
                    >
                        {formProps.shopChannelId(
                            <Select showSearch onSelect={this.changeChannels}
                                filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}
                                style={{ width: 140 }}>
                                {data.channels.map(item => <Option value={item.id} key={item.id}>{item.name}</Option>)
                                }
                            </Select>
                        )}
                    </Form.Item>}
                    {shopProtocol.internal === 1 && <div>
                        <Form.Item
                            label="合同有效期："
                            labelCol={{ span: 2 }}
                            wrapperCol={{ span: 16 }}
                        >
                            {formProps.time(
                                <RangePicker style={{ width: '29%' }} />
                            )}
                        </Form.Item>
                        {shop.shopType !== "accom" && shop.shopType !== "trip" &&  <Form.Item
                            label="商户账号"
                            labelCol={{ span: 2 }}
                            wrapperCol={{ span: 16 }}
                        >

                            {getFieldDecorator('shopAccount', {
                                rules: [{ required: shop.shopType !== "coupon" ? true:false, message: '请输入商品账号!' }],
                                initialValue: shopProtocol.shopAccount
                            })(
                                <Input style={{ width: '29%' }} prefix={<Icon type="user" style={{ fontSize: 13 }} />} />
                            )}<span style={{ color: 'grey', fontSize: '12px', marginLeft: '10px' }}>可以使用字母、数字和符号，不能跟已有的用户名重复</span>
                        </Form.Item>}
                        {shop.shopType !== "accom" && shop.shopType !== "trip" &&  <Form.Item
                            label="商户密码"
                            labelCol={{ span: 2 }}
                            wrapperCol={{ span: 16 }}
                        >
                            {getFieldDecorator('shopPassword', {
                                rules: [{ required:  shop.shopType !== "coupon" ? true:false, message: '请输入商品账号密码!' }],
                                initialValue: shopProtocol.shopPassword
                            })(
                                <Input prefix={<Icon type="lock" style={{ fontSize: 13 }} />} style={{ width: '29%' }} />
                            )}<span style={{ color: 'grey', fontSize: '12px', marginLeft: '10px' }}>可以使用字母、数字和符号</span>
                        </Form.Item>}
                        <Divider />
                        <h2 className="mid-title">结算信息</h2>
                        <Row type='flex' justify="start" >
                            <Col span={6}>
                                <Form.Item
                                    label="结算方式："
                                    labelCol={{ span: 8 }}
                                    wrapperCol={{ span: 8 }}
                                >
                                    {formProps.settleMethod(
                                        <Select showSearch
                                            filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}
                                            style={{ width: 150 }}
                                            placeholder="请选择"
                                        >
                                            {
                                                data.settleMethods.map(item => <Option value={item.settleMethod} key={item.id}>{item.settleMethod}</Option>)
                                            }
                                        </Select>
                                    )}
                                </Form.Item>
                            </Col>
                            <Col span={4}>
                                <Form.Item
                                    label="结算货币："
                                    labelCol={{ span: 8 }}
                                    wrapperCol={{ span: 8 }}
                                >
                                    {formProps.currency(
                                        <Select
                                            style={{ width: 150 }}
                                            placeholder="请选择" showSearch
                                            filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}
                                        >
                                            {
                                                data.currencys.map(item => <Option value={item.code} key={item.code}>{item.name}</Option>)
                                            }
                                        </Select>
                                    )}
                                </Form.Item>
                            </Col>
                            <Col span={8}>
                                <Form.Item
                                    label="金额精度："
                                    {...formItemLayout}

                                >
                                    {formProps.decimal(
                                        <Select
                                            style={{ width: 150 }}
                                            placeholder="请选择"
                                        >
                                            <Option value={0} >整数</Option>
                                            <Option value={1} >一位小数</Option>
                                            <Option value={2} >两位小数</Option>
                                        </Select>
                                    )}
                                </Form.Item>
                            </Col>
                            <Col>
                                <Form.Item
                                    label="进位规则："
                                    {...formItemLayout}

                                >
                                    {formProps.roundup(
                                        <Select
                                            style={{ width: 150 }}
                                            placeholder="请选择"
                                        >
                                            <Option value="0" >四舍五入</Option>
                                            <Option value="1" >向上取整</Option>
                                            <Option value="2" >向下取整</Option>
                                        </Select>
                                    )}
                                </Form.Item>
                            </Col>
                        </Row>
                        <Form.Item
                            label="结算价格的变动对已经生成的预约单的影响："
                            labelCol={{ span: 6 }}
                            wrapperCol={{ span: 8 }}
                        >
                            {formProps.changePrice(
                                <RadioGroup >
                                    <Radio value={0}>保持不变</Radio>
                                    <Radio value={1}>随之改变</Radio>

                                </RadioGroup>
                            )}
                        </Form.Item>
                        <Divider />
                        <h2 className="mid-title">付款信息</h2>
                        <Form.Item
                            label="开户名："
                            {...formItemLayout2}
                        >
                            {formProps.accountName(
                                <Input placeholder="请输入开户名" />
                            )}
                        </Form.Item>
                        <Form.Item
                            label="开户银行："
                            {...formItemLayout2}
                        >
                            {formProps.openingBank(
                                <Input placeholder="请输入开户行" />
                            )}
                        </Form.Item>
                        <Form.Item
                            label="银行账号："
                            {...formItemLayout2}
                        >
                            {formProps.bankAccount(
                                <Input placeholder="请输入银行账号" />
                            )}
                        </Form.Item>
                    </div>}
                    <Divider />
                    <h2 className="mid-title">使用规则</h2>
                    <Form.Item
                        label="商户Block："
                        {...formItemLayout2}
                    >
                        <Button type="primary" ghost onClick={this.addBlock}>+ 添加Block</Button>
                    </Form.Item>
                    <Form.Item style={{ marginLeft: '8%' }} {...formItemLayout2}>
                        {
                            blockRuleList.map((item, idx) => (
                                <Tag key={item.rule}
                                    closable
                                    onClose={e => e.stopPropagation()}
                                    afterClose={() => this.onClose(item)}
                                // onClick={() => this.editBlock(item)}
                                >
                                    {item.natural}
                                </Tag>
                            )
                            )
                        }
                    </Form.Item>
                    {shopProtocol.internal === 1 && shop.shopType !== "trip" && shop.shopType !== "coupon" && <div>
                        <Divider />
                        <Form.Item
                            label="泊车信息："
                            {...formItemLayout3}
                        >
                            {formProps.parking(
                                <TextArea placeholder="请输入停车信息" rows={4} />
                            )}
                        </Form.Item>
                        <Form.Item
                            label="儿童政策："
                            {...formItemLayout3}
                        >
                            {formProps.children(
                                <TextArea placeholder="请输入儿童政策内容" rows={4} />
                            )}
                        </Form.Item>
                        <Form.Item
                            label="重要通知："
                            {...formItemLayout3}
                        >
                            {formProps.notice(
                                <TextArea placeholder="请输入" rows={4} />
                            )}
                        </Form.Item>
                    </div>}

                    <Form.Item
                        wrapperCol={{
                            span: 8, offset: 4
                        }}
                    >
                        {edit && <Button style={{ marginBottom: 10 }}
                            type="primary"
                            htmlType="submit"
                            loading={this.props.loading}
                        >
                            保存
                        </Button>}
                        <Button style={{ marginLeft: 20, marginBottom: 10 }}>
                            取消
                        </Button>
                    </Form.Item>
                </Form>
            </div>
        );
    }
}

export default Protocol;

