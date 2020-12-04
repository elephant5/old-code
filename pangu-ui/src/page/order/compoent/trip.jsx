import React, { Component, Fragment } from 'react';

import { Form, Input, Button, Select ,LocaleProvider,Row, Col,Checkbox,Cascader,DatePicker,Divider} from 'antd';


class Trip extends Component {
    constructor(props) {
      super(props);
      this.state = { 
         
      };
  }
 
  render() {
      const {  } = this.state;
      return (<div>
          <Row   type="flex" >
          <Col span={6} >
            <Form.Item  label="服务内容" >
            防守打法第三
            </Form.Item>
          </Col>
          <Col span={6} >
            <Form.Item label="乘车人数">
            防守打法第三
            </Form.Item>
          </Col>
          <Col span={6} >
          <Form.Item label="预约时段">
          防守打法第三
          </Form.Item>
          </Col>
          </Row>
          <Row   type="flex" >
          <Col span={6} >
          <Form.Item label="航班号">
                防守打法第三
            </Form.Item>
          </Col>
          <Col span={6} >
          <Form.Item label="航段">
          防守打法第三
          </Form.Item>
          </Col>
          <Col span={6} >
          <Form.Item label="销售渠道">
          防守打法第三
          </Form.Item>
          </Col>
          </Row>
          <Row   type="flex" >
          <Col span={6} >
          <Form.Item label="上车地址">
                
            </Form.Item>
          </Col>
          <Col span={6} >
          <Form.Item label="目的地址">
          防守打法第三
          </Form.Item>
          </Col>
          <Col span={6} >

          </Col>
          </Row>
          
          </div>
      );
  }
}
export default Trip;