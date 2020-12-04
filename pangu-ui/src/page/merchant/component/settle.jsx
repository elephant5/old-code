import React, { Component, Fragment } from 'react';
import { Form, Checkbox, Switch, Tag, Input, Button, Select, Row, Col, Badge, InputNumber, Tabs, DatePicker } from 'antd';
import './block.less';
import moment from 'moment';
import CommonUpload from '../../../component/common-upload/index';
import RuleModal from './rule-modal';
import ComponentBase from '../../../base/component-base';

const { Option } = Select;
const { RangePicker } = DatePicker;
const formItemLayout = {
  labelCol: { span: 6 },
  wrapperCol: { span: 12},
};
const formItemLayout2 = {
    labelCol: { span: 6 },
    wrapperCol: { span: 18},
  };

@Form.create()
class Block extends ComponentBase {
    handleBlock = e => {
        e.preventDefault();
        const { onEvent, shopItemId } = this.props;
        this.props.form.validateFields((err, values) => {
            if (!err) {
              onEvent('saveBlock', {
                shopItemId,
                "blockTime": values.blockTime.map(item => moment(item).format()),
                "calendar": values.calendar,
                "containWeek": values.containWeek,
                "repeat": values.repeat? 1: 0,
                specialBlocks: values.festival
              })
            }
        })
      }
    getUploadFileProps = params => {
        return {
            fileName: params.fileName,
            fileType: 'shop.item.contract',
            options: {
                // 上传回调
                onChange: (info, status) => {
                    switch(status){
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

    // 弹框
  editRule = params => {
    this.showView('showRuleModal', {...params})
  }
  onOk = params => {
    this.props.onEvent('settleExpress', params);
    this.reset();
  }
    render() {
        const { getFieldDecorator } = this.props.form;
    const { viewData} = this.state;
    // 弹框展示
    const isShowRuleModal = this.isShowView('showRuleModal');
    return (<Fragment>
      {
        isShowRuleModal &&
        <RuleModal
          onOk={this.onOk}
          onCancel={this.reset}
          data={viewData}
        />
      }
      <div className="batch-show-wrapper">
        <div className="current-show">
          <p className="current-show-title">结算规则</p>
          <div className="current-rules">
            {
              this.props.settleRule.map(item => {
                return (
                  <div className="current-rule" key={item.id}>{item.timeStr}: {item.settleRuleStr}</div>
                )
              })
            }
          </div>
        </div>
        <Form className="batch-form" onSubmit={this.handleClose}>
          <Form.Item
            {...formItemLayout}
            label="起止日期"
          >
            {getFieldDecorator('dateRange', {
            //   initialValue: [moment(formatDate(startDate, "yyyy-MM-dd"), 'YYYY-MM-DD'), moment(formatDate(endDate, "yyyy-MM-dd"), 'YYYY-MM-DD')],
              rules: [{ required: true, message: '请选择日期范围'}]
            })(
              <RangePicker/>
            )}
          </Form.Item>
          <Form.Item
            {...formItemLayout}
            label="其中含"
          >
            {getFieldDecorator('weekday', {
              rules: [{ required: false}]
            })(
              <Checkbox.Group style={{ width: "100%" }}>
                <Row>
                  <Col span={6}><Checkbox value="1">周一</Checkbox></Col>
                  <Col span={6}><Checkbox value="2">周二</Checkbox></Col>
                  <Col span={6}><Checkbox value="3">周三</Checkbox></Col>
                  <Col span={6}><Checkbox value="4">周四</Checkbox></Col>
                  <Col span={6}><Checkbox value="5">周五</Checkbox></Col>
                  <Col span={6}><Checkbox value="6">周六</Checkbox></Col>
                  <Col span={6}><Checkbox value="7">周日</Checkbox></Col>
                </Row>
              </Checkbox.Group>
            )}
          </Form.Item>
          <Form.Item
            {...formItemLayout2}
            label="结算规则："
          >
          {
            this.state.ruleList.map(item => (
              <div className="rule-box" key={item.gift} onClick={() => this.editRule(item)}>
                <div className="before">{item.name}</div>
                <div className="content ellipsis">{item.content.settleExpress}</div>
              </div>
            ))
          }
          </Form.Item>
          <CommonUpload
            form={this.props.form}
            uploadText="上传凭证"
            uploadFileProps={this.getUploadFileProps({fileName: 'closePz', label: '结算凭证：'})}
            formItemLayout={formItemLayout}
          />
          <Row type="flex" justify="start">
            <Col>
              <Button 
                type="primary" 
                htmlType="submit" 
                style={{marginRight: 20}}
                loading={this.props.settleLoading}
              >保存</Button>
              <Button type="default">重置</Button>
            </Col>
          </Row>
        </Form>
        </div>
    </Fragment>)
    }
}

export default Block;