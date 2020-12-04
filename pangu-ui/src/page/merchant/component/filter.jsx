import React, { Component } from 'react';
import { Form, Input, Button, Select, Row, Col } from 'antd';
import cookie from 'react-cookies';

const { Option } = Select;
const formItemLayout = {
    labelCol: { span: 6 },
    wrapperCol: { span: 12 },
};

@Form.create({})
class Filter extends Component {
    constructor(props) {
        super(props);
        this.state = {};
    }
    handleSubmit = e => {
        e.preventDefault();
        const { onEvent } = this.props;
        this.props.form.validateFields((err, values) => {
            if (!err) {
                onEvent('search', {
                    "cityName": values.city,
                    "serviceName": values.resource,
                    "shopChannelId": values.channel,
                    "shopNameOrHotelName": values.hotelResource,
                })
            }
        })
    }

    getForm = () => {
        const { getFieldDecorator } = this.props.form;
        let formProps = {};
        formProps.hotelResource = getFieldDecorator('hotelResource', {
        });
        formProps.resource = getFieldDecorator('resource', {
            initialValue: ''
        });
        formProps.channel = getFieldDecorator('channel', {
            initialValue: ''
        });
        formProps.city = getFieldDecorator('city', {
        });
        return formProps;
    }
    render() {
        const formProps = this.getForm();
        const { data, onEvent, cityList } = this.props;
        const add = cookie.load("KLF_PG_RM_SL_ADD");
        return (
            <div className="c-filter">
                <Form onSubmit={this.handleSubmit} layout="inline">
                    <Row gutter={{ md: 8, lg: 24, xl: 48 }}>
                        <Col md={8} sm={24}>
                            <Form.Item>
                                {formProps.hotelResource(
                                    <Input
                                        placeholder="搜索酒店/商户"
                                        style={{ width: '100%' }}
                                    />
                                )}
                            </Form.Item>
                        </Col>
                    </Row>
                    <Row gutter={{ md: 8, lg: 24, xl: 48 }}>
                        <Col md={8} sm={24}>
                            <Form.Item
                                label="资源类型："
                                {...formItemLayout}
                            >
                                {formProps.resource(
                                    <Select
                                        style={{ width: 210 }} showSearch
                                        filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}
                                    >
                                        <Option  value="">全部</Option>
                                        {
                                            data.map(item => {
                                                return <Option key={item.name}>{item.name}</Option>
                                            })
                                        }
                                    </Select>
                                )}
                            </Form.Item>
                        </Col>
                        <Col md={8} sm={24}>
                            <Form.Item
                                label="渠道类型："
                                {...formItemLayout}
                            >
                                {formProps.channel(
                                    <Select
                                        style={{ width: 210 }}
                                    >
                                        <Option  value="">全部</Option>
                                        <Option value="1">公司资源</Option>
                                        <Option value="0">渠道资源</Option>
                                    </Select>
                                )}
                            </Form.Item>
                        </Col>
                        <Col md={8} sm={24}>
                            <Form.Item
                                label="城市："
                                {...formItemLayout}
                            >
                                {formProps.city(
                                    <Select style={{ width: 210 }} mode='multiple' showSearch
                                        filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                                        <Option  value="">全部</Option>
                                        {
                                            cityList.map(item => {
                                                return <Option key={'city2' + item.nameCh} value={item.nameCh}>{item.nameCh}</Option>;
                                            })
                                        }
                                    </Select>
                                )}
                            </Form.Item>
                        </Col>

                    </Row>
                    <Row gutter={{ md: 8, lg: 24, xl: 48 }}>
                        <Col md={8} sm={24}>
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

                        </Col>
                        {add && <Col md={8} sm={24}>
                            <Form.Item>
                                <Button type="primary" onClick={() => onEvent('addMerchant')}>
                                    + 新增商户
                                </Button>
                            </Form.Item>
                        </Col>}

                    </Row>
                </Form>
            </div>
        );
    }
}

export default Filter;