import React, { Component, Fragment } from 'react';
import { Modal, Form, DatePicker, Checkbox, Row, Col, Radio, Switch, Select, Input, InputNumber, message } from 'antd';
import { week } from '../../../config/index';
import moment from 'moment';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as actions from '../../../store/goods/action';
const { Option } = Select;
const { RangePicker } = DatePicker;
const RadioGroup = Radio.Group;
const formItemLayout = {
    labelCol: { span: 4 },
    wrapperCol: { span: 12 },
};
@connect(
    ({ operation, goods }) => ({
        operation,
        goods: goods.toJS()
    }),
    dispatch => ({
        actions: bindActionCreators(actions, dispatch)
    })
)
@Form.create()
class AddPaymentModal extends Component {

    state = {
    }

    componentWillReceiveProps(nextProps) {
        const { operation, resource } = nextProps;
        if (this.props.operation !== nextProps.operation) {
            switch (operation.type) {
                default:
                    break;
            }
        }
    }

    handleSubmit = e => {
        e.preventDefault();
        this.props.form.validateFields((err, values) => {
            if (!err) {
                const { productGroupProductId, onOk } = this.props;
                let param = {
                    productGroupProductId: productGroupProductId,
                    weeks: values.weeks,
                    bookPrice: values.bookPrice,
                    startDate: moment(values.dateRange[0]).format(),
                    endDate: moment(values.dateRange[1]).format(),
                }
                this.props.actions.convertPayment(param).then((data) => {
                    const { goods } = this.props;
                    if (goods.convertPaymentRes.code == 100) {
                        onOk(goods.convertPaymentRes.result);
                    }
                }).catch((error) => {
                    message.error("系统错误")
                });
            }
        })
    }
    render() {
        const { onCancel } = this.props;
        const { getFieldDecorator } = this.props.form;

        return (
            <Modal
                title="预约支付金额"
                visible={true}
                onOk={this.handleSubmit}
                onCancel={onCancel}
                cancelText="取消"
                okText="确定"
            >
                <Form onSubmit={this.handleSubmit}>
                    <Form.Item
                        label="起始日期："
                        {...formItemLayout}
                    >
                        {getFieldDecorator('dateRange', { rules: [{ required: true, message: '请选择日期范围' }] })(
                            <RangePicker />
                        )}
                    </Form.Item>
                    {
                        <Fragment>
                            <Form.Item
                                label="其中含："
                                {...formItemLayout}
                            >
                                {getFieldDecorator('weeks', { rules: [{ required: true, message: '请选择星期范围' }] })(
                                    <Checkbox.Group>
                                        <Row>
                                            {
                                                week.map(item => (
                                                    <Col span={8} key={item.code}>
                                                        <Checkbox
                                                            value={item.code}
                                                        >
                                                            {item.name}
                                                        </Checkbox>
                                                    </Col>
                                                ))
                                            }
                                        </Row>
                                    </Checkbox.Group>
                                )}
                            </Form.Item>
                        </Fragment>
                    }
                    <Form.Item
                        {...formItemLayout}
                        label="预约支付"
                    >
                        {getFieldDecorator('bookPrice', {
                            rules: [{ required: true, message: '请输入预约支付金额' }]
                        })(
                            <InputNumber addonBefore="￥" />
                        )}
                    </Form.Item>
                </Form>
            </Modal>
        );
    }
}

export default AddPaymentModal;