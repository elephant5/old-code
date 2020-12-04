import React, { Component } from 'react';
import { Form, Input, Button, Select } from 'antd';

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
    /**
 * 导出产品组产品
 */
    export = e => {
        e.preventDefault();
        const { onEvent } = this.props;
        this.props.form.validateFields((err, values) => {
            if (!err) {
                onEvent('export', {
                    ...values
                })
            }
        });
    }
    // 表单绑定
    getForm = () => {
        const { getFieldDecorator } = this.props.form;
        let formProps = {};
        formProps.hotelOrShopName = getFieldDecorator('hotelOrShopName', {

        });
        formProps.service = getFieldDecorator('service', {

        });
        formProps.gift = getFieldDecorator('gift', {

        });
        formProps.city = getFieldDecorator('city', {

        });
        formProps.startCost = getFieldDecorator('startCost', {

        });
        formProps.endCost = getFieldDecorator('endCost', {

        });
        formProps.statusType = getFieldDecorator('statusType', {

        });
        formProps.channelType = getFieldDecorator('channelType', {

        });
        return formProps;
    }
    render() {
        const formProps = this.getForm();
        const { resourceType, giftTypeList, cityList } = this.props;
        return (
            <div className="c-filter">
                <Form layout="inline" onSubmit={this.handleSubmit}>
                    <Form.Item>
                        {formProps.hotelOrShopName(
                            <Input placeholder="搜索酒店/商户" />
                        )}
                    </Form.Item>
                    <Form.Item label="资源类型:">
                        {formProps.service(
                            <Select style={{ width: 200 }} mode='multiple' showSearch
                                filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                                {/* <Option value="">全部</Option> */}
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
                            <Select style={{ width: 200 }} mode='multiple' showSearch
                                filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                                {/* <Option value="">全部</Option> */}
                                {
                                    giftTypeList.map(item => {
                                        return <Option key={item.shortName}>{item.shortName}</Option>
                                    })
                                }
                            </Select>
                        )}
                    </Form.Item>
                    <Form.Item label="城市:">
                        {formProps.city(
                            <Select style={{ width: 200 }} mode='multiple' showSearch
                                filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                                <Option value="">全部</Option>
                                {
                                    cityList.map(item => {
                                        return <Option key={'city2' + item.id} value={item.nameCh}>{item.nameCh}</Option>;
                                    })
                                }
                            </Select>
                        )}
                    </Form.Item>
                    <Form.Item label="成本价:">
                        {formProps.startCost(
                            <Input style={{ width: '30%' }} addonBefore='￥'></Input>
                        )}&nbsp;&nbsp;-&nbsp;&nbsp; {formProps.endCost(
                            <Input style={{ width: '30%' }} addonBefore='￥' ></Input>
                        )}
                    </Form.Item>
                    <Form.Item label="状态:">
                        {formProps.statusType(
                            <Select
                                style={{ width: 210 }}
                            >
                                <Option value={null}>全部</Option>
                                <Option value={0}>在售</Option>
                                <Option value={1}>停售</Option>
                            </Select>
                        )}
                    </Form.Item>
                    <Form.Item label="渠道:">
                        {formProps.channelType(
                            <Select
                                style={{ width: 210 }}
                            >
                                <Option value="">全部</Option>
                                <Option value="1">公司资源</Option>
                                <Option value="0">渠道资源</Option>
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
                        <Button onClick={this.export} style={{ marginLeft: 10 }}>导出</Button>
                    </Form.Item>
                </Form>
            </div>
        );
    }
}

export default Filter;