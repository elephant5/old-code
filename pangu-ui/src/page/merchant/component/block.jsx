import React, { Component } from 'react';
import { Form, Checkbox, Switch, Tag, Button, Select, Row, Col, DatePicker, Divider } from 'antd';
import './block.less';
import moment from 'moment';
import _ from 'lodash';
import cookie from 'react-cookies';

const { Option } = Select;
const { RangePicker } = DatePicker;
const formItemLayout = {
  labelCol: { span: 4 },
  wrapperCol: { span: 12 },
};

@Form.create()
class Block extends Component {
  state = {
    // 特殊日期
    initialValue: '',
    isEdit: false
  }
  handleBlock = e => {
    e.preventDefault();
    const { onEvent, shopItemId, blockRule } = this.props;
    this.props.form.validateFields((err, values) => {
      if (!err) {
        let arr = []

        values.festival.forEach(ele => {
          const a = this.props.festivalList.filter(item => {
            return item.rule === ele
          })

          arr.push(...a)
        });
        if (values.reason && arr.length > 0) {
          arr.map(item => {
            item.reason = values.reason;
            return item;
          });
        }
        let blockTime = values.blockTime ? values.blockTime.map(item => item ? moment(item).format() : null) : null;
        let resultblockTime = blockTime.filter(item => item != null)
        if (!this.state.isEdit) {
          onEvent('saveBlock', {
            shopItemId,
            "blockTime": resultblockTime,
            "calendar": values.calendar,
            "containWeek": values.containWeek,
            "repeat": values.repeat ? 1 : 0,
            "reason": values.reason,
            specialBlocks: arr
          })
        } else {

          onEvent('editBlock', {
            shopItemId,
            "blockTime": resultblockTime,
            "calendar": values.calendar,
            "containWeek": values.containWeek,
            "repeat": values.repeat ? 1 : 0,
            specialBlocks: arr,
            blockRuleList: blockRule.filter(item => item.rule !== this.state.editParams.rule),
          })
        }
      }
      this.props.form.resetFields();
    })
  }
  // 删除block
  closeBlock = params => {
    const { shopItemId, blockRule, onEvent } = this.props;
    onEvent('deleteBlock', {
      blockRuleList: blockRule.filter(item => item.rule !== params.rule),
      shopItemId
    })
  }
  editBlock = params => {
    this.setState({
      isEdit: true
    })
    if (params.type !== 2) {
      this.props.onEvent('getBlock', params);
      this.setState({
        editParams: params
      })
    } else {
      this.setState({
        initialValue: params.rule
      })
    }
  }
  handleReset = () => {
    this.props.form.resetFields();
  }
  render() {
    const { getFieldDecorator } = this.props.form;
    const { blockParams, allSysDictList } = this.props;
    const children = [];
    const edit = cookie.load("KLF_PG_RM_SL_SERVICE_EDIT");
    if (allSysDictList) {
      allSysDictList.map(item => {
        children.push(<Option key={item.value} value={item.value}>{item.label}</Option>);
      })
    }

    return (

      <div className="c-modal">

        {/* <div className="c-title">block规则</div> */}
        <div style={{ marginBottom: 5, marginTop: 5, marginLeft: 10 }}>
          {this.props.blockRule.map((rule, index) => {
            return (
              <Tag key={`rule-${index + 1}`} color="blue"
                closable={edit ? true : false}
                onClose={e => e.stopPropagation()}
                afterClose={() => this.closeBlock(rule)}
              >
                {rule.natural}
              </Tag>
            )
          })
          }
        </div>
        {!_.isEmpty(this.props.blockRule) && <Divider></Divider>}
        <Form className="batch-form" onSubmit={this.handleBlock}>
          <Form.Item
            {...formItemLayout}
            label="日历类型"
          >
            {getFieldDecorator('calendar', {
              initialValue: blockParams.calendar || 0,
              rules: [{ required: true, message: '请选择日历类型' }]
            })(
              <Select>
                <Option value={0}>公历</Option>
                <Option value={1}>农历</Option>
              </Select>
            )}
          </Form.Item>
          <Form.Item
            {...formItemLayout}
            label="起止日期"
          >
            {getFieldDecorator('blockTime', {
              initialValue: [
                !_.isEmpty(blockParams.blockTime) ? moment(blockParams.blockTime[0], 'YYYY-MM-DD') : null,
                !_.isEmpty(blockParams.blockTime) ? moment(blockParams.blockTime[1], 'YYYY-MM-DD') : null,
              ],
              // rules: [{ required: true, message: '请选择日期范围'}]
            })(
              <RangePicker />
            )}
          </Form.Item>
          <Form.Item
            {...formItemLayout}
            label="其中含"
          >
            {getFieldDecorator('containWeek', {
              initialValue: blockParams.containWeek || [],
              rules: [{ required: false }]
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
            {...formItemLayout}
            label="每年重复"
          >
            {getFieldDecorator('repeat', {
              initialValue: blockParams.repeat || 0
            })(
              <Switch checkedChildren="开" unCheckedChildren="关" />
            )}
          </Form.Item>
          <Form.Item
            label="特殊日期："
            {...formItemLayout}
          >
            {
              getFieldDecorator('festival', {
                initialValue: [this.state.initialValue],
              })(
                <Checkbox.Group>
                  <Row>
                    {
                      this.props.festivalList.map(item => (
                        <Col span={8} key={item.rule}>
                          <Checkbox
                            value={item.rule}
                          >
                            {item.natural}
                          </Checkbox>
                        </Col>
                      ))
                    }
                  </Row>
                </Checkbox.Group>
              )
            }
          </Form.Item>
          <Form.Item
            label="Block原因"
            {...formItemLayout}
          >
            {
              getFieldDecorator('reason', {
              })(
                <Select
                  // mode="multiple"
                  placeholder="请选择Block原因"
                  // value={selectedItems}
                  style={{ width: '100%' }}
                  allowClear={true}
                >
                  {children}
                </Select>
              )
            }
          </Form.Item>
          <Row type="flex" justify="start">
            <Col offset={2} span={8}>
              {edit && <Button
                type="primary"
                htmlType="submit"
                style={{ marginRight: 8 }}
                loading={this.props.blockLoading}
              >保存</Button>}
              <Button type="default" onClick={this.handleReset}>取消</Button>
            </Col>
          </Row>
        </Form>
      </div>
    );
  }
}

export default Block;

