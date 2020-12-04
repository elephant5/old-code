import React, { Fragment } from 'react';
import Filter from './compoent/filter';
import List from './compoent/list';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { bindActionCreators } from 'redux';
import * as actions from '../../store/nuwa/action';
import * as comms from '../../store/common/action';
import TableListBase from '../../base/table-list-base';
import { Form, Input, Button, Select,  message, Row, Col, Modal } from 'antd';
import { LocaleProvider } from 'antd';
import zh_CN from 'antd/lib/locale-provider/zh_CN';
const { TextArea } = Input;
@connect(
    ({ operation, nuwa, common }) => ({
        operation,
        nuwa: nuwa.toJS(),
        common: common.toJS()
    }),
    dispatch => ({
        actions: bindActionCreators(actions, dispatch),
        comms: bindActionCreators(comms, dispatch),
    })
)

@withRouter
@Form.create()
class SMS extends TableListBase {
    constructor(props) {
        super(props);
        this.state = {
            isTableLoading: true,
            tableData: [],
            isShowEditModal: false
        };
    }

    componentDidMount() {
        this.onList({
            action: this.props.actions.selectQueueByPage,
            params: {
                "current": 1,
                "size": 10
            }
        })

    }

    componentWillReceiveProps(nextProps) {
        const { operation, nuwa } = nextProps;
        switch (operation.type) {
            case actions.GET_SELECTQUEUEBYPAGE_SUCCESS:
                if (nuwa.queueList.code == 100) {
                    this.setState({
                        tableData: nuwa.queueList.result,
                        isTableLoading: false
                    })
                } else {
                    this.setState({
                        isTableLoading: false
                    })
                }

                break;
            case actions.GET_SENDMSG_SUCCESS:
                if (nuwa.sendMsg.code == 100) {
                    message.success('短信发送成功！')
                    this.closeGoodsEditModel();
                   
                    this.onList({
                        action: this.props.actions.selectQueueByPage,
                        params: {
                            "current": 1,
                            "size": 10
                        }
                    })
                } else {
                    message.error('短信发送失败！'+ nuwa.sendMsg.msg)
                    this.setState({
                        isTableLoading: false
                    })
                }

                break;

            default:
                break;
        }
    }
    onEvent = (type, params) => {
        const { pageSize } = this.state;

        switch (type) {
            case 'addSendMsg':

                break;
            // 查询
            case 'search':
                this.setState({
                    filterParmas: params
                }, () => {
                    this.onList({
                        action: this.props.actions.selectQueueByPage,
                        params: {
                            "condition": params,
                            "current": 1,
                            "size": pageSize
                        }
                    })
                })
                break;
            default:
        }
    }
    // 切换分页
    getList = params => {
        this.setState({
            pageSize: params.size,
        })
        this.onList({
            action: this.props.actions.selectQueueByPage,
            params: {
                "condition": this.state.filterParmas,
                ...params
            }
        })
    }

    closeGoodsEditModel = item => {
        this.setState({
            isShowEditModal: false,
            tempRecord: {}
        })
    }
    openGoodsEditModel = () => {
        this.setState({
            isShowEditModal: true,
            tempRecord: {}
        })
    }

    // 查询
    addSendMsg = e => {
        e.preventDefault();
        this.props.form.validateFields((err, values) => {
            if (!err) {
                this.setState({
                    filteisShowEditModal: false
                }, () => {
                    this.props.actions.sendMsg(values);
                })
            }
        });
    }
    // 查询
    againSendMsg =values => {
        this.props.actions.sendMsg(values);
    }
    render() {
        const { tableData, isTableLoading, } = this.state;
        const { getFieldDecorator } = this.props.form;
        return (
            <Fragment><LocaleProvider locale={zh_CN}>
                <Filter
                    onEvent={this.onEvent}
                    closeGoodsEditModel={this.closeGoodsEditModel}
                    openGoodsEditModel={this.openGoodsEditModel}
                /></LocaleProvider>
                <LocaleProvider locale={zh_CN}><List
                    data={tableData}
                    loading={isTableLoading}
                    onEvent={this.onEvent}
                    pagination={this.getPagination}
                    onList={this.getList}
                    againSendMsg={this.againSendMsg}
                /></LocaleProvider>
                <Modal
                    title="短信单发"
                    visible={this.state.isShowEditModal}
                    onOk={this.addSendMsg}
                    onCancel={this.closeGoodsEditModel}
                    cancelText="取消"
                    okText="发送短信"
                >
                    <Form onSubmit={this.addSendMsg}>
                        <Form.Item label="手机号" style={{ width: "100%" }} labelCol={{ span: 5 }} wrapperCol={{ span: 13 }}>
                            {getFieldDecorator('phone', {
                                rules: [{ required: true, message: '请输入手机号!' }]
                            })(
                                <Input placeholder="请输入手机号" min={11} max={11} />
                            )}
                        </Form.Item>
                        <Form.Item label="短信内容" style={{ width: "100%" }} labelCol={{ span: 5 }} wrapperCol={{ span: 13 }}>
                            {getFieldDecorator('content', {
                                rules: [{ required: true, message: '请输入短信内容!' }]
                            })(
                                <TextArea autosize={{ minRows: 8, maxRows: 20 }} placeholder="短信内容" />
                            )}
                        </Form.Item>
                    </Form>
                </Modal>
            </Fragment>
        );
    }
}

export default SMS;