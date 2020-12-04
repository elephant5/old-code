import React, { Component } from 'react';
import { Form, Input, Button, Select,Cascader } from 'antd';

const { Option } = Select;
@Form.create()
class Filter extends Component {
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
    // 重置
    reset = () => {
        this.props.form.resetFields();
    }
    // 表单绑定
    getForm = () => {
        const { getFieldDecorator } = this.props.form;
        let formProps = {};
        formProps.idOrName = getFieldDecorator('idOrName', {

        });
        formProps.salesChannel = getFieldDecorator('salesChannel', {

        });
        formProps.service = getFieldDecorator('service', {

        });
        formProps.gift = getFieldDecorator('gift', {

        });
        return formProps;
    }
    render() {
        const formProps = this.getForm();
        const { resourceType, giftTypeList,channelList } = this.props;
        return (
            <div className="c-filter">
                <Form layout="inline" onSubmit={this.handleSubmit}>
                    <Form.Item>
                        {formProps.idOrName(
                            <Input placeholder="搜索产品组ID/名称/内部简称" />
                        )}
                    </Form.Item>
                    <Form.Item label="销售渠道:">
                        {formProps.salesChannel(
                            <Cascader  style={{ width: '100%' }} options={channelList}  placeholder="请选择..." />
                        )}
                    </Form.Item>
                    <Form.Item label="资源类型:">
                        {formProps.service(
                            <Select style={{ width: 200 }} showSearch
                            filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                                <Option  value="">全部</Option>
                                {
                                    resourceType.map(item => {
                                        return <Option key={item.name}>{item.name}</Option>
                                    })
                                }
                            </Select>
                        )}
                    </Form.Item>
                    <Form.Item label="权益类型:">
                        {formProps.gift(
                            <Select style={{ width: 200 }} showSearch
                            filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                                <Option  value="">全部</Option>
                                {
                                    giftTypeList.map(item => {
                                        return <Option key={item.shortName}>{item.shortName}</Option>
                                    })
                                }
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
                </Form>
            </div>
        );
    }
}

export default Filter;