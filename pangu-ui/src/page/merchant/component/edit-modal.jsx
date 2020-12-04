import React, { Component, Fragment } from 'react';
import { Modal, Form, DatePicker, Checkbox, Row, Col, Radio, Switch } from 'antd';
import { week } from '../../../config/index';
import moment from 'moment';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as actions from '../../../store/resource/action';

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
class EditModal extends Component {

    state = {
        blockRule: [],
        // 选择农历不能选周和每年重复
        isShowWeek: 0,
        festival: [],
        // 前端默认展示
        blockParams: {}
    }
    componentDidMount() {
        const type = this.props.data.editParams.type;
        if(type !== 2){
            this.props.actions.getBlockParams(this.props.data.editParams);
        }
    }
    componentWillReceiveProps(nextProps) {
        const { operation, resource } = nextProps;
        switch(operation.type){
            case actions.GET_BLOCKPARAMS_SUCCESS:
                this.setState({
                    blockParams: resource.blockParams.result
                })
                break;
            case actions.GET_BLOCKRULE_SUCCESS:
                this.setState({
                    blockRule: resource.blockRule.result
                }, () => {
                    this.props.onOk({
                        festival: this.state.festival || [],
                        blockRule: this.state.blockRule, 
                        deleteData: this.props.data.editParams
                    })
                })
                break;
            default:
                break;
        }
    }
    getForm = () => {
        const { blockParams } = this.state;
        const { editParams } = this.props.data;
        const { getFieldDecorator } = this.props.form;
        let formProps = {};
        formProps.date = getFieldDecorator('blockTime', {
            initialValue: blockParams.blockTime
        });
        formProps.festival = getFieldDecorator('festival', {
            initialValue: editParams.type === 2 ? [editParams.rule]: []
        });
        formProps.week = getFieldDecorator('containWeek', {
            initialValue: blockParams.containWeek
        });
        formProps.calendar = getFieldDecorator('calendar', {
            initialValue: blockParams.calendar || 0,
            rules: [{
                required: true,
            }],
        });
        formProps.repeat = getFieldDecorator('repeat', {
            initialValue: blockParams.repeat,
        });
        return formProps;
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
                // 获取block规则
                this.props.actions.getBlockRule(params);
                festival = this.props.data.festival.filter(item => item.rule === festival);
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
        const { onCancel, data } = this.props;
        return (
            <Modal
                title="编辑block"
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
                                    <Switch checkedChildren="开" unCheckedChildren="关"  />
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
                </Form>
            </Modal>
        );
    }
}

export default EditModal;