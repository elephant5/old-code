import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { Form, Input, Button, Select, InputNumber, Radio, Col, Checkbox, Cascader, DatePicker, Divider, Modal } from 'antd';
import moment from 'moment';
import './reserve'
const RadioGroup = Radio.Group;
const { Option } = Select;
const { TextArea } = Input;

@Form.create()
class Cancel extends Component {
  constructor(props) {
    super(props);

    this.state = {
      resourceList: [],
      groupProduct: {},
      rooms: [],
      deparDateList: [],
      dataSource: [],
      tatolPrice: 0,
      checked: true,
      checked2: true,
      checked3: true,
      checked4: true,
      checked5: true,
    };
  }
  componentDidMount() {

  }
  // 保存基本信息
  handleSubmit = e => {
    e.preventDefault();
    const { onEvent, reservOrderInfo } = this.props;
    this.props.form.validateFields((err, values) => {
      const { checked, checked2, checked3, checked4, checked5 } = this.state;
      if (!err) {
        console.log(values)
        values.messageFlag = checked === true ? '1' : "0";
        values.refundInter = checked3 === true ? '1' : "0";
        values.cancelIsPay = checked2 === true ? '1' : "0";
        if (reservOrderInfo.tags.indexOf('自付') > -1) {
          values.detail.backAmountStatus = checked4 === true ? '1' : "0";
        }
        if (reservOrderInfo.serviceTypeCode == 'object_cpn') {
          values.expressFlag = checked5 === true ? 1 : 0;
        }
        values.id = reservOrderInfo.id;
        values.cancelOperator="1";
        onEvent('reservCancel', values);


      }
    });
  }
  onChange = e => {
    this.setState({
      checked: e.target.checked,
    });
  };
  onChange2 = e => {
    this.setState({
      checked2: e.target.checked,
    });
  };
  onChange3 = e => {
    this.setState({
      checked3: e.target.checked,
    });
  };
  onChange4 = e => {
    this.setState({
      checked4: e.target.checked,
    });
  };
  onChange5 = e => {
    this.setState({
      checked5: e.target.checked,
    });
  };
  render() {
    const { onEvent, closeReserveModel, reservOrderInfo, reservCancelLoading } = this.props;
    // const { resourceList ,rooms,deparDateList,dataSource,tatolPrice} = this.state;
    const { getFieldDecorator } = this.props.form;

    return (<Modal width={'25%'}
      title="预定订单预订取消"
      visible={true}
      onCancel={closeReserveModel}
      onOk={this.handleSubmit}
      confirmLoading={reservCancelLoading}
      cancelText="取消"
      okText="确定"
    >
      <Form onSubmit={this.handleSubmit}>

        <Form.Item verticalGap={1} label="取消原因" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal" indicatorGap >
          {getFieldDecorator('cancelReason', { rules: [{ required: true, message: '请输入取消原因!' }], initialValue: null })(
            <Input></Input>
          )}
        </Form.Item>
        <Form.Item verticalGap={1} label="权益处理" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal" indicatorGap >
          {getFieldDecorator('refundInter', {})
            (
              <Checkbox checked={this.state.checked3} onChange={this.onChange3}></Checkbox>
            )}&nbsp;&nbsp;退回权益

             {reservOrderInfo.tags.indexOf('自付') > -1 && <label><br />
            {getFieldDecorator('backAmountStatus', {})
              (
                <Checkbox checked={this.state.checked4} onChange={this.onChange4}></Checkbox>
              )}&nbsp;&nbsp;全额退款  &nbsp;&nbsp;
             {getFieldDecorator('detail.backAmount', { initialValue: reservOrderInfo.detail.payAmoney })
              (
                <InputNumber></InputNumber>
              )}
          </label>}
        </Form.Item>
        <Form.Item verticalGap={1} label="商户赔付" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal" indicatorGap >
          {getFieldDecorator('cancelIsPay', {})
            (
              <Checkbox checked={this.state.checked2} onChange={this.onChange2}></Checkbox>
            )}&nbsp;&nbsp;需要支付商户赔偿金 &nbsp;&nbsp;
            {this.state.checked2 && getFieldDecorator('orderDamageAmount', { initialValue: reservOrderInfo.orderDamageAmount })
            (
              <InputNumber></InputNumber>
            )}
          <br />
          {getFieldDecorator('messageFlag', {})
            (
              <Checkbox checked={this.state.checked} onChange={this.onChange}></Checkbox>
            )}&nbsp;&nbsp;短信通知客户
            <br />
        </Form.Item>
        {reservOrderInfo.serviceTypeCode == 'object_cpn' && <Form.Item verticalGap={1} label="物流信息" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal" indicatorGap>
          {getFieldDecorator('expressFlag', {})(
            <Checkbox checked={this.state.checked5} onChange={this.onChange5}></Checkbox>
          )}&nbsp;&nbsp;退货物流
            </Form.Item>}

      </Form>
    </Modal>
    );
  }
}
export default Cancel;