import React, { Component } from 'react';
import { Form, Select, Input, Button } from 'antd';
import cookie from 'react-cookies';

const Option = Select.Option;
const Search = Input.Search;

@Form.create()
class Filter extends Component {
    constructor(props) {
        super(props);
        this.state = {
            bankName: "",
            inputBankName: "",
            bankIds: []
        };
        this.onChangeBankInput = this.onChangeBankInput.bind(this);
        this.reset = this.reset.bind(this);
    }

    // 查询
    handleSubmit = e => {
        e.preventDefault();
        const { onEvent } = this.props;
        this.props.form.validateFields((err, values) => {
            if (!err) {
                onEvent('search', {
                    ...values
                })
            }
        });
    }
    //输入银行名称
    inputBankName = e => {
        this.setState({
            bankName: e.target.value
        });
    }
    onChangeBankInput = e => {
        const value = e.target.value;
        const { inputBankName } = this.state;
        if (value != inputBankName) {
            var bankIds = [];
            if (!!value) {
                const { bankTypeList } = this.props;
                bankTypeList.map((item) => {
                    if (item.label.indexOf(value) != -1) {
                        bankIds.push(item.value);
                    }
                })
                //防止没有 后台不查询现象
                bankIds.push("-100");
            }
            this.setState({
                bankIds: bankIds,
                inputBankName: value
            });
        }
    }
    // 重置
    reset = () => {
        this.props.form.resetFields();
        this.setState({
            bankName: "",
            inputBankName: "",
            bankIds: []
        });
    }
    // 表单绑定
    getForm = () => {
        const { getFieldDecorator } = this.props.form;
        let formProps = {};
        //销售渠道
        formProps.salesChannelId = getFieldDecorator('salesChannelId', {
            initialValue: ""
        });
        //销售方式
        formProps.salesWayId = getFieldDecorator('salesWayId', {
            initialValue: ""
        });
        formProps.settleMethodId = getFieldDecorator('settleMethodId', {
            initialValue: ""
        });
        formProps.bankIds = getFieldDecorator('bankIds', {
            initialValue: this.state.bankIds
        });
        formProps.invoiceObjId = getFieldDecorator('invoiceObjId', {
            initialValue: ""
        });
        formProps.invoiceNodeId = getFieldDecorator('invoiceNodeId', {
            initialValue: ""
        });
        formProps.status = getFieldDecorator('status', {
            initialValue: ""
        });
        return formProps;
    }

    render() {
        const formProps = this.getForm();
        const add = cookie.load("KLF_PG_GM_CL_ADD");
        const {
            salesChannelType,
            salesWayType,
            settlementMethodType,
            invoiceNodeType,
            invoiceObjType,
            onEvent
        } = this.props;
        return (
            <div className="c-filter">
                <Form layout="inline" onSubmit={this.handleSubmit}>
                    <Form.Item>
                        <Input
                            placeholder="搜索银行"
                            style={{ width: 200 }}
                            onBlur={this.onChangeBankInput}
                            onChange={this.inputBankName}
                            value={this.state.bankName}
                        />
                    </Form.Item>
                    <Form.Item
                        label="销售渠道："
                    >
                        {formProps.salesChannelId(
                            <Select defaultValue="" style={{ width: 120 }} showSearch
                                filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                                <Option  value="">全部</Option>
                                {
                                    salesChannelType.map(item => <Option value={item.value}
                                        key={item.id}>{item.label}</Option>)
                                }
                            </Select>
                        )}
                    </Form.Item>
                    <Form.Item
                        label="销售方式："
                    >
                        {formProps.salesWayId(
                            <Select defaultValue="" style={{ width: 120 }} showSearch
                                filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                                <Option  value="">全部</Option>
                                {
                                    salesWayType.map(item => <Option value={item.value}
                                        key={item.id}>{item.label}</Option>)
                                }
                            </Select>
                        )}
                    </Form.Item>
                    <Form.Item
                        label="结算方式："
                    >
                        {formProps.settleMethodId(
                            <Select defaultValue="" style={{ width: 120 }} showSearch
                                filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                                <Option  value="">全部</Option>
                                {
                                    settlementMethodType.map(item => <Option key={item.value}>{item.label}</Option>)
                                }
                            </Select>
                        )}
                    </Form.Item>
                    <Form.Item
                        label="开票节点："
                    >
                        {formProps.invoiceNodeId(
                            <Select defaultValue="" style={{ width: 120 }} showSearch
                                filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                                <Option  value="">全部</Option>
                                {
                                    invoiceNodeType.map(item => <Option key={item.value}>{item.label}</Option>)
                                }
                            </Select>
                        )}
                    </Form.Item>
                    <Form.Item
                        label="开票对象："
                    >
                        {formProps.invoiceObjId(
                            <Select defaultValue="" style={{ width: 120 }} showSearch
                                filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                                <Option  value="">全部</Option>
                                {
                                    invoiceObjType.map(item => <Option key={item.value}>{item.label}</Option>)
                                }
                            </Select>
                        )}
                    </Form.Item>
                    <Form.Item
                        label="状态："
                    >
                        {formProps.status(
                            <Select defaultValue="" style={{ width: 120 }}>
                                <Option  value="">全部</Option>
                                <Option key={0}>启用</Option>
                                <Option key={1}>停用</Option>
                            </Select>
                        )}
                    </Form.Item>
                    <Form.Item>
                        <Button
                            type="primary"
                            htmlType="submit"
                        >
                            查询
                        </Button>
                        <Button onClick={this.reset} style={{ marginLeft: 10 }}
                        >
                            重置
                        </Button>
                    </Form.Item>
                    <Form.Item>
                        {add && <Button type="primary" onClick={() => onEvent('add')}>
                            + 新增销售渠道
                        </Button>}
                    </Form.Item>
                </Form>
            </div>
        );
    }
}

export default Filter;