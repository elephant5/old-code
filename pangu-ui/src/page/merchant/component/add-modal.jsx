import React, { Component, Fragment } from 'react';
import { Modal, Form, DatePicker, Checkbox, Row, Col, Radio, Switch,Select } from 'antd';
import { week } from '../../../config/index';
import moment from 'moment';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as actions from '../../../store/resource/action';
const { Option } = Select;
const { RangePicker } = DatePicker;
const RadioGroup = Radio.Group;
const formItemLayout = {
    labelCol: { span: 4 },
    wrapperCol: { span: 12},
};
@connect(
    ({operation, resource}) => ({
        operation,
        resource: resource.toJS()
    }),
    dispatch => ({ 
        actions: bindActionCreators(actions, dispatch) 
    })
)
@Form.create()
class AddModal extends Component {

    state = {
        blockRule: [],
        // 选择农历不能选周和每年重复
        isShowWeek: 0,
        festival: []
    }
    getForm = () => {
        const { getFieldDecorator } = this.props.form;
        let formProps = {};
        formProps.date = getFieldDecorator('blockTime', {
        });
        formProps.festival = getFieldDecorator('festival', {
            // initialValue: ['C1/1-C1/8']
        });
        formProps.week = getFieldDecorator('containWeek', {
        });
        formProps.calendar = getFieldDecorator('calendar', {
            initialValue: 0,
            rules: [{
                required: true,
            }],
        });
        formProps.repeat = getFieldDecorator('repeat', {
            initialValue: false,
        });
        return formProps;
    }

    componentWillReceiveProps(nextProps){
        const { operation, resource } = nextProps;
        if (this.props.operation !== nextProps.operation) {
            switch(operation.type){
                case actions.GET_BLOCKRULE_SUCCESS:
                const {getFieldValue} = nextProps.form;
                const reason =getFieldValue("reason");
                const result  =resource.blockRule.result;
                if(reason){
                    result.map(item =>{
                        item.reason = reason;
                        return item;
                     });
                }
                    this.setState({
                        blockRule: result
                    }, () => {
                        this.props.onOk({
                            festival: this.state.festival || [], 
                            blockRule: this.state.blockRule || []
                        })
                    })
                    break;
                default:
                    break;
            }
        }
    }

    handleSubmit = e => {
        e.preventDefault();
        this.props.form.validateFields((err, values) => {
            if (!err) {
                let { festival, blockTime, repeat, ...otherProps } = values;
                blockTime = blockTime || [];
                blockTime = blockTime.map(item => moment(item).format());
                repeat = repeat ? 1: 0;
                const params = {repeat, blockTime, ...otherProps};
                if(values.reason&& festival && festival.length>0){
                    festival.map(item =>{
                       item.reason = values.reason;
                       return item;
                    });
                  }
                // 获取block规则
                this.props.actions.getBlockRule(params);
                this.setState({
                    festival
                })
            }
        })
    }
    // 日历选择
    selectCalendar = e => {
        this.setState({
            isShowWeek: e.target.value
        })
    }
    render() {
        const formProps = this.getForm();
        const { onCancel, data,allSysDictList } = this.props;
        const { getFieldDecorator } = this.props.form;
        const children = [];
        if(allSysDictList){
          allSysDictList.map(item =>{
            children.push(<Option key={item.value} value ={item.value }>{item.label }</Option>);
          })
        }
        return (
            <Modal
                title="新增block"
                visible={true}
                onOk={this.handleSubmit}
                onCancel={onCancel}
                cancelText="取消"
                okText="确定"
            >
                <Form onSubmit={this.handleSubmit}>
                    <Form.Item
                        label="日历类型："
                        {...formItemLayout}
                    >
                        {formProps.calendar(
                            <RadioGroup onChange={this.selectCalendar}>
                                <Radio value={0}>公历</Radio>
                                <Radio value={1}>农历</Radio>
                            </RadioGroup>
                        )}
                    </Form.Item>
                    <Form.Item
                        label="起始日期："
                        {...formItemLayout}
                    >
                        {formProps.date(
                            <RangePicker />
                        )}
                    </Form.Item>
                    {
                        !this.state.isShowWeek &&
                        <Fragment>
                        <Form.Item
                            label="其中含："
                            {...formItemLayout}
                        >
                            {
                                formProps.week(
                                    <Checkbox.Group>
                                        <Row>
                                            {
                                                week.map(item => (
                                                    <Col span={8} key={item.code}>
                                                        <Checkbox 
                                                            // onChange={this.selectFestival}
                                                            value={item.code}
                                                        >
                                                            {item.name}
                                                        </Checkbox>
                                                    </Col>
                                                ))
                                            }
                                        </Row>
                                    </Checkbox.Group>
                                )
                            }
                        </Form.Item>
                        <Form.Item label="每年重复：" {...formItemLayout}>
                            {
                                formProps.repeat(
                                    <Switch checkedChildren="开" unCheckedChildren="关" />
                                )
                            }
                        </Form.Item>
                        </Fragment>
                    }
                    <Form.Item
                        label="特殊日期："
                        {...formItemLayout}
                    >
                        {/* <Checkbox>全部</Checkbox><br/> */}
                        {
                            formProps.festival(
                                <Checkbox.Group>
                                    <Row>
                                    {
                                        data.festivalList.map(item => (
                                            <Col span={8} key={item.rule}>
                                                <Checkbox 
                                                    onChange={this.selectFestival}
                                                    value={item}
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
                    {allSysDictList&&
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
                    }
                </Form>
            </Modal>
        );
    }
}

export default AddModal;