import React, { Component } from 'react';
import { Form, Checkbox, Tag, Input, Button, Row, Col, InputNumber, DatePicker, Divider, Popconfirm } from 'antd';
import './block.less';
import moment from 'moment';
import CommonUpload from '../../../component/common-upload/index';
import { getFileList } from '../../../util/index';
import _ from 'lodash';
import cookie from 'react-cookies';

const { RangePicker } = DatePicker;
const formItemLayout = {
  labelCol: { span: 4 },
  wrapperCol: { span: 18 },
};

@Form.create()
class Block extends Component {

  constructor(props) {
    super(props);
    this.fileList = [];
    this.state = {
      // 默认数据, 编辑使用
      initialValue: {},
      indeterminate: true,
    };
    // id用于编辑
    this.id = null;
  }
  // 产品价格保存
  handlePrice = e => {
    e.preventDefault();
    const { onEvent, shopId, shopItemId } = this.props;
    this.props.form.validateFields((err, values) => {
      if (!err) {
        let arr = this.getInitWeek();
        onEvent('savePrice', {
          id: this.id,
          shopId,
          shopItemId,
          startDate: moment(values.dateRange[0]).format(),
          endDate: moment(values.dateRange[1]).format(),
          netPrice: values.price,
          serviceRate: values.service * 0.01,
          taxRate: values.tax * 0.01,
          weeks: arr,
          fileList: this.fileList
        });
        this.id = null;
        this.fileList = [];
        this.props.form.resetFields();
      }
    })
  }
  getUploadFileProps = params => {
    return {
      fileName: params.fileName,
      fileType: 'shop.item.price.contract',
      options: {
        // 上传回调
        onChange: (info, status) => {
          switch (status) {
            case 'done':
              this.fileList = this.fileList.concat(info);
              break;
            case 'removed':
              this.fileList = this.fileList.filter(item => info.code !== item.code)
              break;
            default:
              break;
          }
        }
      },
      config: {
        label: params.label,
        // rules: [{
        //     required: true, message: '请上传凭证'
        // }],
        // type: FILE_TYPE.PDF,
        // size: 1024*1024,
        // num: 20
      }
    }
  }
  closePrice = params => {
    this.props.onEvent('deletePrice', params.id);
  }
  editPrice = params => {
    this.id = params.id;
    this.setState({
      initialValue: params
    })
  }
  getInitWeek = () => {
    if (!_.isEmpty(this.state.initialValue)) {
      let arr = [];
      if (this.state.initialValue.monday === 1) {
        arr.push('1')
      }
      if (this.state.initialValue.tuesday === 1) {
        arr.push('2')
      }
      if (this.state.initialValue.wednesday === 1) {
        arr.push('3')
      }
      if (this.state.initialValue.thursday === 1) {
        arr.push('4')
      }
      if (this.state.initialValue.friday === 1) {
        arr.push('5')
      }
      if (this.state.initialValue.saturday === 1) {
        arr.push('6')
      }
      if (this.state.initialValue.sunday === 1) {
        arr.push('7')
      }
      return arr
    }
    return []
  }

  reset = () => {
    this.props.form.resetFields();
    const { getFieldValue, setFieldsValue } = this.props.form;
  }
  onCheckAllChange = (e) => {
    const { initialValue } = this.state;
    let params = initialValue ? initialValue : {};
    let checkedValue = 0;
    if (e.target.checked) {
      checkedValue = 1;
      params.friday = checkedValue;
      params.monday = checkedValue;
      params.saturday = checkedValue;
      params.sunday = checkedValue;
      params.thursday = checkedValue;
      params.tuesday = checkedValue;
      params.wednesday = checkedValue;

    } else {
      params.friday = checkedValue;
      params.monday = checkedValue;
      params.saturday = checkedValue;
      params.sunday = checkedValue;
      params.thursday = checkedValue;
      params.tuesday = checkedValue;
      params.wednesday = checkedValue;
    }
    this.setState({
      initialValue: params,
      indeterminate: e.target.checked ? false : true
    })
  }
  checkedValue = e => {
    const { initialValue } = this.state;
    let params = initialValue ? initialValue : {};
    let checkedValue = e.target.value;// e.target.checked ? 1 :0;
    let checked = e.target.checked;
    if (checkedValue === "1") {
      params.monday = checked ? 1 : 0;
    }
    if (checkedValue === "2") {
      params.tuesday = checked ? 1 : 0;
    }
    if (checkedValue === "3") {
      params.wednesday = checked ? 1 : 0;
    }
    if (checkedValue === "4") {
      params.thursday = checked ? 1 : 0;
    }
    if (checkedValue === "5") {
      params.friday = checked ? 1 : 0;
    }
    if (checkedValue === "6") {
      params.saturday = checked ? 1 : 0;
    }
    if (checkedValue === "7") {
      params.sunday = checked ? 1 : 0;
    }
    this.setState({
      initialValue: params,
      indeterminate: true
    })
  }
  render() {
    const { getFieldDecorator } = this.props.form;
    const { priceRule } = this.props;
    const edit = cookie.load("KLF_PG_RM_SL_SERVICE_EDIT");
    return (

      <div className="c-modal">

        {/* <div className="c-title">产品价格</div> */}
        <div style={{ marginBottom: 5, marginTop: 5, marginLeft: 10 }}>
          {
            priceRule.map((item, index) => {
              return (
                //<Popconfirm placement="top" title="确定删除当前资源吗？" onConfirm={() =>this.onEvent('deleteSize', shopItem.id)} okText="确定" cancelText="取消">
                <div>
                  <Tag key={`rule-${index + Math.floor(Math.random() * 1000)}`} color="blue"
                    closable={edit ? true : false} style={{ marginBottom: 2 }}
                    onClose={e => e.stopPropagation()}
                    afterClose={() => this.closePrice(item)}
                    onClick={() => edit ? this.editPrice(item) : null}
                  >
                    {item.timeString}:{item.priceString}
                  </Tag><br></br>
                </div>)
            })
          }
        </div>
        {!_.isEmpty(priceRule) && <Divider></Divider>}
        <Form className="batch-form" onSubmit={this.handlePrice}>
          <Form.Item
            {...formItemLayout}
            label="起止日期"
          >
            {getFieldDecorator('dateRange', {
              initialValue: [
                this.state.initialValue.startDate ? moment(this.state.initialValue.startDate, 'YYYY-MM-DD') : null,
                this.state.initialValue.endDate ? moment(this.state.initialValue.endDate, 'YYYY-MM-DD') : null
              ],
              rules: [{ required: true, message: '请选择日期范围' }]
            })(
              <RangePicker />
            )}
          </Form.Item>
          <Form.Item
            {...formItemLayout}
            label="其中含"
          >
            {getFieldDecorator('weekdays', {
              initialValue: this.getInitWeek(),
              rules: [{ required: true }]
            })(
              // <Checkbox.Group style={{ width: "100%" }}>
              <Row>
                <Col span={6}><Checkbox onChange={this.onCheckAllChange} indeterminate={this.state.indeterminate} >全部</Checkbox></Col>
                <Col span={6}><Checkbox onChange={this.checkedValue} value="1" checked={this.state.initialValue ? this.state.initialValue.monday === 1 ? true : false : false}>周一</Checkbox></Col>
                <Col span={6}><Checkbox onChange={this.checkedValue} value="2" checked={this.state.initialValue ? this.state.initialValue.tuesday === 1 ? true : false : false}>周二</Checkbox></Col>
                <Col span={6}><Checkbox onChange={this.checkedValue} value="3" checked={this.state.initialValue ? this.state.initialValue.wednesday === 1 ? true : false : false}>周三</Checkbox></Col>
                <Col span={6}><Checkbox onChange={this.checkedValue} value="4" checked={this.state.initialValue ? this.state.initialValue.thursday === 1 ? true : false : false}>周四</Checkbox></Col>
                <Col span={6}><Checkbox onChange={this.checkedValue} value="5" checked={this.state.initialValue ? this.state.initialValue.friday === 1 ? true : false : false}>周五</Checkbox></Col>
                <Col span={6}><Checkbox onChange={this.checkedValue} value="6" checked={this.state.initialValue ? this.state.initialValue.saturday === 1 ? true : false : false}>周六</Checkbox></Col>
                <Col span={6}><Checkbox onChange={this.checkedValue} value="7" checked={this.state.initialValue ? this.state.initialValue.sunday === 1 ? true : false : false}>周日</Checkbox></Col>
              </Row>
              // </Checkbox.Group>
            )}
          </Form.Item>
          <Form.Item
            {...formItemLayout}
            label="结算货币："
          >
            <div>{this.props.data.shopProtocol.currency}</div>
          </Form.Item>
          <Form.Item
            {...formItemLayout}
            label="净价："
          >
            {getFieldDecorator('price', {
              initialValue: this.state.initialValue.netPrice ? this.state.initialValue.netPrice : null,
              rules: [{ required: true, message: '请输入净价' }]
            })(
              <Input addonBefore="￥" />
            )}
          </Form.Item>
          <Form.Item
            {...formItemLayout}
            label="服务费："
          >
            {getFieldDecorator('service', {
              initialValue: this.state.initialValue.serviceRate ? this.state.initialValue.serviceRate * 100 : null,
              rules: [{ required: false }]
            })(
              <InputNumber
                min={0}
                max={100}
                formatter={value => `${value}%`}
                parser={value => value.replace('%', '')}
              />
            )}
          </Form.Item>
          <Form.Item
            {...formItemLayout}
            label="增值税："
          >
            {getFieldDecorator('tax', {
              initialValue: this.state.initialValue.taxRate ? this.state.initialValue.taxRate * 100 : null,
              rules: [{ required: false }]
            })(
              <InputNumber
                min={0}
                max={100}
                formatter={value => `${value}%`}
                parser={value => value.replace('%', '')}
              />
            )}
          </Form.Item>
          <CommonUpload
            isPicture={false}
            form={this.props.form}
            uploadText="上传凭证"
            uploadFileProps={this.getUploadFileProps({ fileName: 'pingz', label: '价格凭证：' })}
            formItemLayout={formItemLayout}
            uploadedFile={this.state.initialValue ? this.state.initialValue.files ? getFileList(this.state.initialValue.files) : null : null}
          />
          <Row type="flex" justify="start">
            <Col offset={2} span={8}>
              {edit && <Button
                type="primary"
                htmlType="submit"
                style={{ marginRight: 8 }}
                loading={this.props.priceLoading}
              >保存</Button>}
              <Button type="default" onClick={this.reset}>重置</Button>
            </Col>
          </Row>
        </Form>
      </div>
    );
  }
}

export default Block;


