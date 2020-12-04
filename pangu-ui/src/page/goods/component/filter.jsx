import React, { Component } from 'react';
import { Form, Input, Button, Select, Divider, Cascader, Row, Col } from 'antd';
import cookie from 'react-cookies';
const Option = Select.Option;
const Search = Input.Search;

@Form.create()
class GoodsFilter extends Component {
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
        formProps.shortName = getFieldDecorator('shortName', {
            initialValue: null
        });

        return formProps;
    }
    render() {
        const formProps = this.getForm();
        const { getFieldDecorator } = this.props.form;
        const { authTypeList, channelList, data, onEvent } = this.props;
        const add = cookie.load("KLF_PG_GM_GL_ADD");
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
        return (
            <div className="c-filter">
                <Form layout="inline" onSubmit={this.handleSubmit}>
                    <Row gutter={24} type="flex">
                        <Col xl={4} md={12} sm={24}>
                            <Form.Item  {...formItemLayout}>
                                {formProps.shortName(
                                    <Input style={{ width: '250px' }} placeholder="搜索商品ID/商品名称" />
                                )}
                            </Form.Item>
                        </Col>
                        <Col xl={6} md={12} sm={24}>
                            <Form.Item label="销售渠道：">

                                {getFieldDecorator('salesChannelIds', {})(
                                    <Cascader options={channelList} placeholder="请选择..." />
                                )}
                            </Form.Item>
                        </Col>
                        <Col xl={6} md={12} sm={24}>
                            <Form.Item label="验证方式：" >

                                {getFieldDecorator('authType', {})(
                                    <Select style={{ width: '250px' }}  >
                                        <Option value='' >请选择...</Option>
                                        {
                                            authTypeList.map(item => (
                                                <Option key={item.value} value={item.value}>{item.label}</Option>
                                            ))}
                                    </Select>)}
                            </Form.Item>
                        </Col>
                        <Col xl={6} md={12} sm={24}>
                            <Form.Item>
                                <Button type="primary" htmlType="submit">查询</Button>
                                <Button onClick={this.reset} style={{ marginLeft: 10 }} >重置</Button>
                            </Form.Item>
                        </Col>
                        <Col xl={6} md={12} sm={24}>
                            <Form.Item>
                                {add && <Button type="primary" htmlType="button" onClick={() => onEvent('addGoods')} >+  新增商品</Button>}
                            </Form.Item>
                        </Col>
                    </Row>



                </Form>
            </div>
        );
    }
}

export default GoodsFilter;