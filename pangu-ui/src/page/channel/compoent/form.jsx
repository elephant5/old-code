import React, { Component } from 'react';
import { Form, Input, Select, Button, InputNumber, Radio } from 'antd';
import cookie from 'react-cookies';

const Option = Select.Option;

@Form.create()
class BaseForm extends Component {
    constructor(props) {
        super(props);
        this.state = {
            bankName: [],
            salesChannelName: [],
            salesWayName: [],
        };
    }

    // 表单绑定
    getForm = () => {
        let formProps = {};
        return formProps;
    }
    // 保存
    handleSubmit = e => {
        e.preventDefault();
        const { onEvent, salesChannel } = this.props;
        const { salesWayName } = this.state;
        this.props.form.validateFields((err, values) => {
            if (!err) {
                onEvent('saveOrUpdate', {
                    id: salesChannel.id,
                    settleMethodId: values.settleMethodId,
                    invoiceNodeId: values.invoiceNodeId,
                    invoiceObjId: values.invoiceObjId,
                    commision: values.commision != 0 ? (values.commision / 100) : 0,
                    status: values.status,
                    bankName: values.bankName,
                    salesChannelName: values.salesChannelName,
                    salesWayName: values.salesWayName,
                    orders: values.orders
                })
            }
        });
    }

    componentDidMount() {
        const {
            bankType,
            salesChannelType,
            salesWayType,
            salesChannel,
            dictTypes,
        } = this.props;
        //编辑时初始化 银行名称、销售渠道、销售方式
        const bankName = dictTypes.getLabelByType(salesChannel.bankId, bankType);
        const salesChannelName = dictTypes.getLabelByType(salesChannel.salesChannelId, salesChannelType);
        const salesWayName = dictTypes.getLabelByType(salesChannel.salesWayId, salesWayType);
        this.setState({
            bankName: bankName,
            salesChannelName: salesChannelName,
            salesWayName: salesWayName
        });
    }

    render() {
        const {
            bankType,
            salesChannelType,
            salesWayType,
            settlementMethodType,
            invoiceNodeType,
            invoiceObjType,
            salesChannel,
            onEvent
        } = this.props;
        const RadioGroup = Radio.Group;
        const { getFieldDecorator } = this.props.form;
        const {
            bankName,
            salesChannelName,
            salesWayName,
        } = this.state;
        const edit = cookie.load("KLF_PG_GM_CL_EDIT");
        return (

            <div className="c-filter">
                <Form onSubmit={this.handleSubmit}>
                    <Form.Item
                        style={{ marginTop: '20px' }} label="银行名称：" labelCol={{ span: 2 }} wrapperCol={{ span: 6 }}
                    >
                        {
                            getFieldDecorator('bankName', {
                                rules: [{ required: true, message: '请输入银行名称!' }], initialValue: bankName
                            })(
                                <Select
                                    mode="combobox"
                                    disabled={!!salesChannel.id}
                                    placeholder="请输入银行名称" showSearch
                                    filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                                    {
                                        bankType.map(item => <Option
                                            key={item.label}>{item.label}</Option>)
                                    }
                                </Select>
                            )}
                    </Form.Item>
                    <Form.Item
                        style={{ marginTop: '20px' }} label="销售渠道：" labelCol={{ span: 2 }} wrapperCol={{ span: 6 }}
                    >
                        {getFieldDecorator('salesChannelName', { initialValue: salesChannelName })(
                            <Select
                                mode="combobox"
                                disabled={!!salesChannel.id}
                                placeholder={!salesChannel.id ? '请输入销售渠道' : ''} showSearch
                                filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                                {
                                    salesChannelType.map(item => <Option
                                        key={item.label}>{item.label}</Option>)
                                }
                            </Select>
                        )}
                    </Form.Item>
                    <Form.Item
                        style={{ marginTop: '20px' }} label="销售方式：" labelCol={{ span: 2 }} wrapperCol={{ span: 6 }}
                    >
                        {getFieldDecorator('salesWayName', {
                            rules: [{ required: true, message: '请输入销售方式!' }],
                            initialValue: salesWayName
                        })(
                            <Select
                                mode="combobox"
                                disabled={!!salesChannel.id}
                                placeholder="请选择或输入销售方式" showSearch
                                filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}
                            >
                                {
                                    salesWayType.map(item => <Option
                                        key={item.label}>{item.label}</Option>)
                                }
                            </Select>
                        )}
                    </Form.Item>
                    <Form.Item
                        style={{ marginTop: '20px' }} label="结算方式：" labelCol={{ span: 2 }} wrapperCol={{ span: 6 }}
                    >
                        {getFieldDecorator('settleMethodId', {
                            rules: [{ required: true, message: '请选择结算方式!' }],
                            initialValue: salesChannel.settleMethodId
                        })(
                            <Select placeholder="请选择" style={{ width: '50%' }} showSearch
                                filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                                {
                                    settlementMethodType.map(item => <Option
                                        key={item.value}>{item.label}</Option>)
                                }
                            </Select>
                        )}<br />
                        <span
                            style={{ fontSize: '12px', marginTop: '-10px', color: '#ccc' }}>注:结算方式为跟银行的结算方式，如无需结算请选择无</span>
                    </Form.Item>
                    <Form.Item
                        style={{ marginTop: '20px' }} label="开票节点：" labelCol={{ span: 2 }} wrapperCol={{ span: 6 }}
                    >
                        {getFieldDecorator('invoiceNodeId', { initialValue: salesChannel.invoiceNodeId })(
                            <Select placeholder="请选择" style={{ width: '50%' }} showSearch
                                filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                                {
                                    invoiceNodeType.map(item => <Option
                                        key={item.value}>{item.label}</Option>)
                                }
                            </Select>
                        )}
                        <br />
                        <span style={{ fontSize: '12px', marginTop: '-10px', color: '#ccc' }}>注:开票节点为给用户可以申请开票的时间节点</span>
                    </Form.Item>
                    <Form.Item
                        style={{ marginTop: '20px' }} label="开票对象：" labelCol={{ span: 2 }} wrapperCol={{ span: 6 }}
                    >
                        {getFieldDecorator('invoiceObjId', { initialValue: salesChannel.invoiceObjId })(
                            <Select placeholder="请选择" style={{ width: '50%' }} showSearch
                                filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                                {
                                    invoiceObjType.map(item => <Option
                                        key={item.value}>{item.label}</Option>)
                                }
                            </Select>
                        )}
                        <br />
                        <span style={{ fontSize: '12px', marginTop: '-10px', color: '#ccc' }}>注：开票对象为该渠道下的销售单的收票方主体</span>
                    </Form.Item>
                    <Form.Item
                        style={{ marginTop: '20px' }} label="佣金比例：" labelCol={{ span: 2 }} wrapperCol={{ span: 10 }}
                    >
                        {getFieldDecorator('commision', {
                            initialValue: !!salesChannel && !!salesChannel.commision ? (salesChannel.commision * 100) : null
                        })(
                            <InputNumber
                                defaultValue={100}
                                min={0}
                                max={100}
                                formatter={value => `${value}%`}
                                parser={value => value.replace('%', '')}
                            />
                        )}
                        <br />
                        <span style={{ fontSize: '12px', marginTop: '-10px', color: '#ccc' }}>注：该渠道的抽佣比例</span>
                    </Form.Item>
                    <Form.Item style={{ marginTop: '20px' }} label="排序：" labelCol={{ span: 2 }} wrapperCol={{ span: 10 }} >
                        {getFieldDecorator('orders', {  initialValue: salesChannel? salesChannel.orders :0
                        })( <InputNumber   defaultValue={0} /> )}
                        <span style={{ fontSize: '12px', marginTop: '-10px', color: '#ccc' }}>&nbsp;注：数值越小越靠前显示</span>
                    </Form.Item>
                    <Form.Item
                        style={{ marginTop: '20px' }} label="状态：" labelCol={{ span: 2 }} wrapperCol={{ span: 10 }}
                    >
                        {getFieldDecorator('status', {
                            rules: [{ required: true, message: '请选择状态!' }],
                            initialValue: salesChannel.status
                        })(
                            <RadioGroup>
                                <Radio value={0}>启用</Radio>
                                <Radio value={1}>停用</Radio>
                            </RadioGroup>
                        )}
                    </Form.Item>
                    <Form.Item style={{ marginTop: '20px', marginLeft: '50px' }} >
                        {edit && <Button style={{ marginLeft: 10 }}
                            type="primary"
                            htmlType="submit"
                        >
                            保存
                        </Button>}
                        <Button style={{ marginLeft: 10 }} onClick={() => {

                            onEvent('back')
                        }}
                        >
                            取消
                        </Button>
                    </Form.Item>
                </Form>
            </div>
        );
    }
}

export default BaseForm;