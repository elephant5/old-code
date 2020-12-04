import React, { Component, Fragment } from 'react';

import { Form, Input, Button, Select ,LocaleProvider,Row, Col,Checkbox,Cascader,DatePicker,Divider,Modal} from 'antd';
const { Option } = Select;
@Form.create()
class TripUpdate extends Component {
    constructor(props) {
      super(props);
      this.state = { 
         
      };
  }
 
  render() {
    const { closeGoodsEditModel,shopList } = this.props;
    const {  } = this.state;
    const { getFieldDecorator } = this.props.form;
      return (
       <div>
          <Form.Item  label="服务内容" >
            {getFieldDecorator('shopIds',)(
                
            )} 
            </Form.Item>
            <Form.Item  label="乘车人数" >
            {getFieldDecorator('city',)(
                
            )} 
            </Form.Item>
            <Form.Item  label="航班号" >
            {getFieldDecorator('city',)(
                
            )} 
            </Form.Item>
            <Form.Item  label="航段" >
            {getFieldDecorator('city',)(
                
            )} 
            </Form.Item>
            <Form.Item  label="上车地点" >
            {getFieldDecorator('reservDate',)(
               
            )} 
            目的地地址：
          {getFieldDecorator('reservDate',)(
               
            )} 
            </Form.Item>
            <Form.Item  label="预约时段" >
            {getFieldDecorator('city',)(
               
            )} 
            </Form.Item>
            <Form.Item  label="备注" >
            {getFieldDecorator('city',)(
                
            )} 
            </Form.Item></div>
      );
  }
}
export default TripUpdate;