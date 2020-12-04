import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { Form, Input, Button, Select, Table, Radio, Col, Checkbox, InputNumber, DatePicker, Divider, Modal } from 'antd';
import moment from 'moment';
import './reserve'
const { Option } = Select;

@Form.create()
class Fail extends Component {
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
    };
  }
  componentDidMount() {

  }
  // 保存基本信息
  handleSubmit = e => {
    e.preventDefault();
    const { onEvent, reservOrderInfo } = this.props;
    this.props.form.validateFields((err, values) => {
      const { checked, checked2 ,checked3, checked4} = this.state;
      if (!err) {
        // values.giftType = tempShopItem[0].gift;
        // values.productGroupId = productGroup[0].id;
        values.messageFlag = checked === true ? '1' : "0";
        values.failInter = checked2 === true ? '1' : "0";
        if(reservOrderInfo.tags.indexOf('自付') > -1){
          values.detail.backAmountStatus = checked3 === true  ?'1':"0";
        }
        if(reservOrderInfo.serviceTypeCode == 'object_cpn'){
          values.expressFlag = checked4 === true ? '1' : "0";
        }
        values.id = reservOrderInfo.id;
        onEvent('reservFail', values);


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
  render() {
    const { onEvent, closeReserveModel,  reservOrderInfo,reservFailLoading } = this.props;
    const {  } = this.state;
    const { getFieldDecorator } = this.props.form;
    return (<Modal width={'25%'}
      title="预定订单预订失败"
      visible={true}
      onCancel={closeReserveModel}
      onOk={this.handleSubmit}
      confirmLoading={reservFailLoading}
      cancelText="取消"
      okText="确定"
    >
      <Form onSubmit={this.handleSubmit}>

        <Form.Item verticalGap={1} label="失败原因" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal" indicatorGap >
          {getFieldDecorator('failReason', { rules: [{ required: true, message: '请选择失败原因!' }], initialValue: null })(
            <Select>
              {reservOrderInfo.serviceType == '住宿' &&
                <Option label="满房" value="满房">满房</Option>
              }
              {reservOrderInfo.serviceType == '住宿' &&
                <Option label="价格过高" value="价格过高">价格过高</Option>
              }
              {reservOrderInfo.serviceType == '住宿' &&
                <Option label="保留房已满" value="保留房已满">保留房已满</Option>
              }
              {reservOrderInfo.serviceType == '住宿' &&

                <Option label="报价未出" value="报价未出">报价未出</Option>

              }
              {reservOrderInfo.serviceType == '住宿' &&

                <Option label="权益不适用日期" value="权益不适用日期">权益不适用日期</Option>

              }
              {reservOrderInfo.serviceType == '住宿' &&

                <Option label="未满足权益预定时限" value="未满足权益预定时限">未满足权益预定时限</Option>

              }
              {reservOrderInfo.serviceType == '住宿' &&

                <Option label="客户要求不能满足" value="客户要求不能满足">客户要求不能满足</Option>

              }
              {reservOrderInfo.serviceType == '住宿' &&
                <Option label="其他" value="其他">其他</Option>
              }
              {reservOrderInfo.serviceType != '住宿' &&
                <Option label="满座" value="满座">满座</Option>

              }
              {reservOrderInfo.serviceType != '住宿' &&

                <Option label="合同或账款原因" value="合同或账款原因">合同或账款原因</Option>

              }
              {reservOrderInfo.serviceType != '住宿' &&

                <Option label="商户原因（装修、不开放等）" value="商户原因（装修、不开放等）">商户原因（装修、不开放等）</Option>

              }
              {reservOrderInfo.serviceType != '住宿' &&

                <Option label="权益不适用日期" value="权益不适用日期">权益不适用日期</Option>

              }
              {reservOrderInfo.serviceType != '住宿' &&

                <Option label="未满足权益预定时限" value="未满足权益预定时限">未满足权益预定时限</Option>

              }
              {reservOrderInfo.serviceType != '住宿' &&

                <Option label="客户要求不能满足" value="客户要求不能满足">客户要求不能满足</Option>

              }
              {reservOrderInfo.serviceType != '住宿' &&

                <Option label="其他" value="其他">其他</Option>
              }
            </Select>
          )}
        </Form.Item>
        <Form.Item verticalGap={1} label="权益处理" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal" indicatorGap >
          {getFieldDecorator('failInter', {})
            (
              <Checkbox checked={this.state.checked2} onChange={this.onChange2}></Checkbox>
            )}&nbsp;&nbsp;退回权益
            <br/>
            {reservOrderInfo.tags.indexOf('自付') > -1  && <label>
            {getFieldDecorator('backAmountStatus', {})
                (
            <Checkbox checked={this.state.checked3} onChange={this.onChange3}></Checkbox> 
            )}&nbsp;&nbsp;全额退款 &nbsp;&nbsp;
            {getFieldDecorator('detail.backAmount', {initialValue:reservOrderInfo.detail.payAmoney})
               (
                 <InputNumber></InputNumber>
           )}
            <br/></label>}
          {getFieldDecorator('messageFlag', {})
            (
              <Checkbox checked={this.state.checked} onChange={this.onChange}></Checkbox>
            )}&nbsp;&nbsp;短信通知客户
        </Form.Item>
        {reservOrderInfo.serviceTypeCode == 'object_cpn' && <Form.Item verticalGap={1} label="物流信息" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal" indicatorGap>
          {getFieldDecorator('expressFlag',{})(
            <Checkbox checked={this.state.checked4} onChange={this.onChange4}></Checkbox>
          )}&nbsp;&nbsp;退货物流
        </Form.Item>}

      </Form>
    </Modal>
    );
  }
}
export default Fail;