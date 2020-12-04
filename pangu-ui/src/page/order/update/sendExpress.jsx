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
class SendExpress extends Component {
  constructor(props) {
    super(props);

    this.state = {
      checked: true,
    };
  }
  componentDidMount() {

  }
  // 保存基本信息
  handleSubmit = e => {
    e.preventDefault();
    const { onEvent, reservOrderInfo } = this.props;
    this.props.form.validateFields((err, values) => {
      // const { checked } = this.state;
      // if (!err) {
      //   console.log(values)
      //   values.messageFlag = checked === true ? '1' : "0";
      //   values.reservOrderId = reservOrderInfo.logisticsInfo.reservOrderId;
      //   onEvent('saveObjEdit', values);
      // }
    });
  }

  onChange = e => {
    this.setState({
      checked: e.target.checked,
    });
  };

  /**
   * 保存编辑
   */
  saveEdit = e => {
    e.preventDefault();
    const { onEvent,reservOrderInfo } = this.props;
    this.props.form.validateFields((err,values) => {
      if(!err){
        values.reservOrderId = reservOrderInfo.logisticsInfo.reservOrderId;
        onEvent('saveObjEdit',values)
      }
    });
  }

  /**
   * 保存编辑并发货
   */
  saveSendEdit = e => {
    e.preventDefault();
    const { onEvent,reservOrderInfo } = this.props;
    this.props.form.validateFields((err,values) => {
      const { checked } = this.state;
      if(!err){
        values.messageFlag = checked === true ? '1' : "0";
        values.reservOrderId = reservOrderInfo.logisticsInfo.reservOrderId;
        onEvent('saveObjEditAndSend',values)
      }
    });
  }

  render() {
    const { onEvent, closeReserveModel, reservOrderInfo, expressTypeList } = this.props;
    // const { resourceList ,rooms,deparDateList,dataSource,tatolPrice} = this.state;
    const { getFieldDecorator } = this.props.form;

    return (<Modal
      title="快递发货"
      visible={true}
      onCancel={closeReserveModel}
      footer={null}
    >
      <Form onSubmit={this.handleSubmit}>
        <Form.Item verticalGap={1} label="收件人" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal" indicatorGap >
          {getFieldDecorator('consignee', { rules: [{ required: true, message: '请输入收件人!' }], initialValue: reservOrderInfo.logisticsInfo.consignee })(
            <Input></Input>
          )}
        </Form.Item>
        <Form.Item verticalGap={1} label="收件电话" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal" indicatorGap >
          {getFieldDecorator('phone', { rules: [{ required: true, message: '请输入收件人电话!' }], initialValue: reservOrderInfo.logisticsInfo.phone })(
            <Input></Input>
          )}
        </Form.Item>
        <Form.Item verticalGap={1} label="收件地址" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal" indicatorGap >
          {getFieldDecorator('address', { rules: [{ required: true, message: '请输入收件人地址!' }], initialValue: reservOrderInfo.logisticsInfo.address })(
            <Input></Input>
          )}
        </Form.Item>
        <Form.Item verticalGap={1} label="快递公司" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal" indicatorGap >
          {getFieldDecorator('expressNameId', { rules: [{ required: true, message: '请输入快递公司!' }], initialValue: reservOrderInfo.logisticsInfo.expressNameId })(
            <Select showSearch placeholder="请选择..."
              filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
              {
                expressTypeList.map(item => (
                  <Option key={item.value} value={item.value}>{item.label}</Option>
                ))
              }
            </Select>
          )}
        </Form.Item>
        <Form.Item verticalGap={1} label="快递单号" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal" indicatorGap >
          {getFieldDecorator('expressNumber', { rules: [{ required: true, message: '请输入快递单号!' }], initialValue: reservOrderInfo.logisticsInfo.expressNumber })(
            <Input></Input>
          )}
        </Form.Item>
        {/* <Form.Item verticalGap={1} label="短信通知" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal" indicatorGap >
          {getFieldDecorator('messageFlag', {})
            (
              <Checkbox checked={this.state.checked} onChange={this.onChange}></Checkbox>
            )}&nbsp;&nbsp;短信通知客户
        </Form.Item> */}
        <Form style={{ textAlign: "center" }}>
          <Button type="primary" onClick={this.saveEdit}>保存编辑</Button>
          <Button type="primary" style={{ marginLeft: 10 }} onClick={this.saveSendEdit}>保存信息并发货</Button>
        </Form>
      </Form>
    </Modal>
    );
  }
}
export default SendExpress;