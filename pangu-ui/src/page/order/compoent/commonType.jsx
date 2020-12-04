import React, { Component, Fragment } from 'react';

import { Form, Input, Button, Select ,LocaleProvider,Row, Col,Checkbox,Cascader,DatePicker,Divider} from 'antd';

class CommonType extends Component {
    constructor(props) {
      super(props);
      this.state = { 
         
      };
  }
 
  render() {
      return (<div>
          <Row   type="flex" >
          <Col span={6} >
            <Form.Item  label="预约时段" >
            防守打法第三
            </Form.Item>
          </Col>
          <Col span={6} >
            <Form.Item label="用餐人数">
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
export default CommonType;