import React, { Component } from 'react';
import { Form, Input, Button, Select, DatePicker, Row, Col, Modal } from 'antd';
import cookie from 'react-cookies';
import { LocaleProvider } from 'antd';
import zh_CN from 'antd/lib/locale-provider/zh_CN';
const { MonthPicker, RangePicker, WeekPicker } = DatePicker;
const { TextArea } = Input;
const Option = Select.Option;
const Search = Input.Search;
const widthTemp = { width: 270 };
@Form.create()
class Filter extends Component {
    constructor(props) {
        super(props);
        this.state = {
            
        };
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

    // 重置
    reset = () => {
        this.props.form.resetFields();

    }

    closeGoodsEditModel = item => {
       this.props.closeGoodsEditModel();
    }
    openGoodsEditModel = () => {
        this.props.openGoodsEditModel();
    }
    render() {
        const add = cookie.load("KLF_PG_SM_SMS_SEND");
        const { getFieldDecorator } = this.props.form;
        return (
            <div className="c-filter">
                <Form layout="inline" onSubmit={this.handleSubmit}>
                    <Row gutter={24} type="flex">
                        <Col xl={5} md={12} sm={24}>
                            <Form.Item label="手机号：" >
                                {getFieldDecorator('phone', {})(
                                    <Input placeholder="搜索手机号" />
                                )}
                            </Form.Item>
                        </Col>
                        <Col xl={5} md={12} sm={24}>
                            <Form.Item label="关键词">
                                {getFieldDecorator('content', {})(
                                    <Input placeholder="输入关键词" />
                                )}

                            </Form.Item>
                        </Col>
                        <Col xl={5} md={12} sm={24}>
                            <Form.Item label="发送状态" >
                                {getFieldDecorator('state')(
                                    <Select style={{ width: 150 }} showSearch placeholder="请选择..."
                                        filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                                        <Option key='0' value='0' >未发送</Option>
                                        <Option key='1' value='1' >发送成功</Option>
                                        <Option key='2' value='2' >发送失败</Option>
                                    </Select>
                                )}
                            </Form.Item>
                        </Col>

                        <Col xl={4} md={12} sm={24}>
                            <Form.Item>
                                <Button type="primary" htmlType="submit">查询</Button>
                                <Button onClick={this.reset} style={{ marginLeft: 10 }} >重置</Button>
                            </Form.Item>
                        </Col>
                    </Row>
                    <Row gutter={24} type="flex">

                        <Col xl={8} md={12} sm={24}> <LocaleProvider locale={zh_CN}>
                            <Form.Item label="发送时间" >

                                {getFieldDecorator('createTimes', {})(
                                    <RangePicker />
                                )}
                            </Form.Item>
                        </LocaleProvider>
                        </Col>
                        <Col xl={7} md={12} sm={24}>

                        </Col>
                        <Col xl={4} md={12} sm={24}>
                            <Form.Item>
                                {add && <Button type="primary" htmlType="button" onClick={this.openGoodsEditModel} >+  短信单发</Button>}
                            </Form.Item>
                        </Col>
                    </Row>
                </Form>

               
            </div>
        );
    }
}

export default Filter;