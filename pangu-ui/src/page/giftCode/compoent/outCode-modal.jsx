import React, { Component } from 'react';
import { Modal, Form, DatePicker, Input, Select, Checkbox, Button, message, Radio } from 'antd';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as actions from '../../../store/giftCode/action';
import * as goodsActions from '../../../store/goods/action';
import CheckboxGroup from 'antd/lib/checkbox/Group';
import moment from 'moment';
import { getCodeList } from '../../../util/util';
import '../giftCode.less';

const { Option } = Select;
const { TextArea } = Input;

@connect(
    ({ operation, code, goods }) => ({
        operation,
        code: code.toJS(),
        goods: goods.toJS()
    }),
    dispatch => ({
        actions: bindActionCreators({ ...actions, ...goodsActions }, dispatch)
    })
)
@Form.create()
class OutActCodeModal extends Component {
    constructor(props) {
        super(props);
        this.state = {
            checkCodesRes: null,
            goodsChannelList: null,
        };
    }

    componentDidMount() {
    }

    componentWillReceiveProps(nextProps) {
        const { operation, code, goods } = nextProps;
        switch (operation.type) {
            case goodsActions.GET_SELECTGOODSCHANNEL_SUCCESS:
                this.setState({
                    goodsChannelList: goods.goodsChannelList.result,
                })
                break;
            default:
        }
    }

    //出库激活码提交
    handleSubmit = e => {
        e.preventDefault();
        const { outCodeOk } = this.props;
        this.props.form.validateFields((err, values) => {
            if (!err) {
                outCodeOk('out', {
                    ...values,
                    codes: this.state.checkCodesRes.generateCodes.map(item => { return item.actCode })
                });
            }
        })
    }

    //立即下载
    immUpload = e => {
        e.preventDefault();
        const { outImmUploadFun } = this.props;
        outImmUploadFun(this.state.checkCodesRes.generateCodes.map(item => { return item.actCode }));
        this.setState({
            checkCodesRes: null,
            goodsChannelList: null,
        })
    }

    //取消
    outCodeCancel = e => {
        const { outCodeCancel } = this.props;
        outCodeCancel();
        if (this.props.outImmUpload) {
            this.setState({
                checkCodesRes: null,
                goodsChannelList: null,
            })
        }
    }
    onCodeChange = e => {
        setTimeout(() => {
            const codeStr = this.props.form.getFieldValue("codeStr");
            // const codeStr=this.props.form.getFieldsValue();
            // console.info(codeStr)
            const result = getCodeList(codeStr);
            this.props.form.setFieldsValue({ "codeStr": result })
        },
            200)
    }
    //检测激活码按钮事件
    checkActCodes = e => {
        this.props.form.validateFields((err, values) => {
            if (values.codeStr == null || values.codeStr == '') {
                message.info("激活码不能为空")
            } else {
                this.setState({
                    checkCodesRes: null,
                })
                if (values.outType == "code") {
                    this.props.actions.checkActCodes(values.codeStr).then((data) => {
                        const { code } = this.props;
                        if (code.checkCodesRes.code == 100) {
                            message.success("检测成功");
                            this.setState({
                                checkCodesRes: code.checkCodesRes.result,
                            })
                            if (code.checkCodesRes.result.goodsId) {
                                this.props.actions.selectGoodsChannel(code.checkCodesRes.result.goodsId);
                            }
                        }
                    }).catch((error) => {
                        message.error("系统错误");
                    });
                } else if (values.outType == "batch") {
                    this.props.actions.checkActCodesByBatch(values.codeStr).then((data) => {
                        const { code } = this.props;
                        if (code.checkCodesByBatchRes.code == 100) {
                            message.success("检测成功");
                            this.setState({
                                checkCodesRes: code.checkCodesByBatchRes.result,
                            })
                            if (code.checkCodesByBatchRes.result.goodsId) {
                                this.props.actions.selectGoodsChannel(code.checkCodesByBatchRes.result.goodsId);
                            }
                        }
                    }).catch((error) => {
                        message.error("系统错误");
                    });
                }
            }
            // if (!err) {
            // }
        })
    }

    render() {
        const { outCodeVisible, outCodeCancel, actCodeTags, outCodeDistroy, outImmUpload, outCodeOkLoading, outImmUploadFunLoading } = this.props;
        const { checkCodesRes, goodsChannelList } = this.state;
        const { getFieldDecorator } = this.props.form;
        return (
            <Modal
                title="出库激活码"
                visible={outCodeVisible}
                // onOk={this.handleSubmit}
                onCancel={this.outCodeCancel}
                footer={null}
                destroyOnClose={outCodeDistroy}
                width={800}
            // cancelText="取消"
            // okText="确定"
            >
                <Form onSubmit={this.handleSubmit}>
                    <Form.Item label="出库方式：">
                        {getFieldDecorator('outType', { initialValue: 'code' })(
                            <Radio.Group>
                                <Radio value={'code'}>激活码</Radio>
                                <Radio value={'batch'}>批次号</Radio>
                            </Radio.Group>
                        )}
                    </Form.Item>
                    <Form.Item label="激活码列表:">
                        {getFieldDecorator('codeStr', {})(
                            <TextArea onChange={this.onCodeChange} autosize={{ minRows: 4 }} placeholder="激活码列表（用,号隔开）" />
                        )}
                    </Form.Item>
                    <Form.Item style={{ textAlign: "center" }}>
                        <Button type="primary" onClick={this.checkActCodes}>检测合法性</Button>
                    </Form.Item>
                    {checkCodesRes &&
                        <div>
                            <Form.Item label="检测报告:">
                                <p style={{ fontSize: 12, margin: 0 }}>共提交{checkCodesRes.allCodes.length}个激活码，其中 <span style={{ color: "red" }}>{checkCodesRes.allCodes.length - checkCodesRes.generateCodes.length}</span> 个状态有误， <span style={{ color: "blue" }}>{checkCodesRes.generateCodes.length}</span> 个可出库</p>
                                {checkCodesRes.commonGoods == false && <p style={{ fontSize: 12, margin: 0, color: "red" }}>激活码隶属于多个商品，一次只可以出库同一商品的激活码</p>}
                                <table className={'check-table'}>
                                    <tr>
                                        <th>激活码</th>
                                        <th>状态</th>
                                        <th>商品编号</th>
                                        <th>内部简称</th>
                                    </tr>
                                    {checkCodesRes.notExistCodes && checkCodesRes.notExistCodes.map(item => (
                                        <tr style={{ color: "red" }}>
                                            <td>{item.actCode}</td>
                                            <td>不存在</td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                    ))}
                                    {checkCodesRes.outCodes && checkCodesRes.outCodes.map(item => (
                                        <tr style={{ color: "red" }}>
                                            <td>{item.actCode}</td>
                                            <td>出库</td>
                                            <td>{item.goodsId}</td>
                                            <td>{item.goodsShortName}</td>
                                        </tr>
                                    ))}
                                    {checkCodesRes.activeCodes && checkCodesRes.activeCodes.map(item => (
                                        <tr style={{ color: "red" }}>
                                            <td>{item.actCode}</td>
                                            <td>激活</td>
                                            <td>{item.goodsId}</td>
                                            <td>{item.goodsShortName}</td>
                                        </tr>
                                    ))}
                                    {checkCodesRes.runOutCodes && checkCodesRes.runOutCodes.map(item => (
                                        <tr style={{ color: "red" }}>
                                            <td>{item.actCode}</td>
                                            <td>用完</td>
                                            <td>{item.goodsId}</td>
                                            <td>{item.goodsShortName}</td>
                                        </tr>
                                    ))}
                                    {checkCodesRes.pastCodes && checkCodesRes.pastCodes.map(item => (
                                        <tr style={{ color: "red" }}>
                                            <td>{item.actCode}</td>
                                            <td>过期</td>
                                            <td>{item.goodsId}</td>
                                            <td>{item.goodsShortName}</td>
                                        </tr>
                                    ))}
                                    {checkCodesRes.returnCodes && checkCodesRes.returnCodes.map(item => (
                                        <tr style={{ color: "red" }}>
                                            <td>{item.actCode}</td>
                                            <td>退货</td>
                                            <td>{item.goodsId}</td>
                                            <td>{item.goodsShortName}</td>
                                        </tr>
                                    ))}
                                    {checkCodesRes.obsoleteCodes && checkCodesRes.obsoleteCodes.map(item => (
                                        <tr style={{ color: "red" }}>
                                            <td>{item.actCode}</td>
                                            <td>作废</td>
                                            <td>{item.goodsId}</td>
                                            <td>{item.goodsShortName}</td>
                                        </tr>
                                    ))}
                                    {checkCodesRes.generateCodes && checkCodesRes.generateCodes.map(item => (
                                        <tr>
                                            <td>{item.actCode}</td>
                                            <td>生成</td>
                                            <td>{item.goodsId}</td>
                                            <td>{item.goodsShortName}</td>
                                        </tr>
                                    ))}
                                </table>
                                {/* {checkCodesRes.notExistCodes && checkCodesRes.notExistCodes.map(item => (<p>{item}  不存在</p>))}
                                {checkCodesRes.outCodes && checkCodesRes.outCodes.map(item => (<p>{item}  已出库</p>))}
                                {checkCodesRes.activeCodes && checkCodesRes.activeCodes.map(item => (<p>{item}  已激活</p>))}
                                {checkCodesRes.runOutCodes && checkCodesRes.runOutCodes.map(item => (<p>{item}  已用完</p>))}
                                {checkCodesRes.pastCodes && checkCodesRes.pastCodes.map(item => (<p>{item}  已过期</p>))}
                                {checkCodesRes.returnCodes && checkCodesRes.returnCodes.map(item => (<p>{item}  已退货</p>))}
                                {checkCodesRes.obsoleteCodes && checkCodesRes.obsoleteCodes.map(item => (<p>{item}  已作废</p>))} */}
                            </Form.Item>
                            <Form.Item label="添加标签:">
                                {getFieldDecorator('tagList', {})(
                                    <CheckboxGroup>
                                        {actCodeTags.map(item => (<Checkbox value={item.value}>{item.label}</Checkbox>))}
                                    </CheckboxGroup>
                                )}
                            </Form.Item>
                            <Form.Item label="有效期限:">
                                {getFieldDecorator('actExpireTime', {})(
                                    <DatePicker />
                                )}
                            </Form.Item>
                            <Form.Item label="销售渠道">
                                {getFieldDecorator('salesChannelId', { rules: [{ required: true, message: '请选择销售渠道!' }] })(
                                    <Select showSearch placeholder="请选择..."
                                        filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                                        {goodsChannelList &&
                                            goodsChannelList.map(item => (
                                                <Option key={item.id} value={item.id}>{item.bankName}/{item.salesChannelName}/{item.salesWayName}</Option>
                                            ))
                                        }
                                    </Select>
                                )}
                            </Form.Item>
                            <Form.Item label="备注说明">
                                {getFieldDecorator('outRemarks', {})(
                                    <TextArea autosize={{ minRows: 4, maxRows: 8 }} placeholder="备注说明" />
                                )}
                            </Form.Item>
                            {checkCodesRes.commonGoods == true && checkCodesRes.generateCodes.length > 0 && <Form.Item style={{ textAlign: "center" }}>
                                {!outImmUpload && <Button type="primary" onClick={this.handleSubmit} loading={outCodeOkLoading}>确认出库</Button>}
                                {outImmUpload && <Button type="primary" onClick={this.immUpload} loading={outImmUploadFunLoading}>立即下载</Button>}
                            </Form.Item>}
                        </div>
                    }
                </Form>
            </Modal>
        );
    }
}

export default OutActCodeModal;