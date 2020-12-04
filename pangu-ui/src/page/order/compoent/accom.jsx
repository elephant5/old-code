import React, { Component, Fragment } from 'react';

import { Form, Input, Button, Select ,LocaleProvider,Row, Col,Checkbox,Cascader,DatePicker,Divider} from 'antd';

class Accom extends Component {
    constructor(props) {
      super(props);
      this.state = { 
         
      };
  }
 
  render() {
    const {reservOrderInfo,shopDetail} = this.props;
    // const shopItem  = shopDetail.shopItemList.filter(item =>  item.id  === reservOrderInfo.shopItemId);
      return (<div>
          <Row   type="flex" >
          <Col span={6} >
            <Form.Item  label="入住日期" >
              {reservOrderInfo.giftDate}
            </Form.Item>
          </Col>
          <Col span={6} >
            <Form.Item label="离店日期">
            {reservOrderInfo.detail && reservOrderInfo.detail.deparDate}
            </Form.Item>
          </Col>
          <Col span={6} >
         {reservOrderInfo.detail && <Form.Item label="间夜总数">
          {reservOrderInfo.detail.checkNight}间  X {reservOrderInfo.detail.nightNumbers ? reservOrderInfo.detail.nightNumbers:'1'  }夜
          </Form.Item>} 
          </Col>
          <Col span={6} >
         {reservOrderInfo.detail && reservOrderInfo.detail.accoNedds && <Form.Item label="床型要求">
            {reservOrderInfo.detail.accoNedds ==="NULL"?"无要求":""}
            {reservOrderInfo.detail.accoNedds ==="bigbed"?"大床":""}
            {reservOrderInfo.detail.accoNedds ==="doublebed"?"双床":""}
            {reservOrderInfo.detail.accoNedds ==="trybigbed"?"尽量大床":""}
            {reservOrderInfo.detail.accoNedds ==="trydoublebed"?"尽量双床":""}
          </Form.Item>}
          </Col>
                               
          </Row>
          
          
          </div>
      );
  }
}
export default Accom;