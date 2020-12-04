import React, { Component } from 'react';
import { Modal, Form, DatePicker, Input, Select, Checkbox, Button, message } from 'antd';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as actions from '../../../store/giftCode/action';
import * as goodsActions from '../../../store/goods/action';
import CheckboxGroup from 'antd/lib/checkbox/Group';
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
class ObsoleteActCodeModal extends Component {
    constructor(props) {
        super(props);
        this.state = {
            checkCodesRes: null,
        };
    }

    componentDidMount() {
    }

    componentWillReceiveProps(nextProps) {
        const { operation, code, goods } = nextProps;
        switch (operation.type) {
            default:
        }
    }

    //作废激活码提交
    handleSubmit = e => {
        e.preventDefault();
        const { obsoleteCodeOk } = this.props;
        this.props.form.validateFields((err, values) => {
            if (!err) {
                obsoleteCodeOk('obsolete', {
                    ...values,
                    codes: [...this.props.code.checkCodesRes.result.generateCodes.map(item => { return item.actCode }), ...this.props.code.checkCodesRes.result.outCodes.map(item => { return item.actCode }), ...this.props.code.checkCodesRes.result.activeCodes.map(item => { return item.actCode }), ...this.props.code.checkCodesRes.result.runOutCodes.map(item => { return item.actCode }), ...this.props.code.checkCodesRes.result.pastCodes.map(item => { return item.actCode })]
                });
                this.setState({
                    checkCodesRes: null,
                })
            }
        })
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
                this.props.actions.checkActCodes(values.codeStr).then((data) => {
                    const { code } = this.props;
                    if (code.checkCodesRes.code == 100) {
                        message.success("检测成功");
                        this.setState({
                            checkCodesRes: code.checkCodesRes.result,
                        })
                    }
                }).catch((error) => {
                    message.error("系统错误");
                });
            }
            // if (!err) {
            // }
        })
    }

    render() {
        const { obsoleteCodeVisible, obsoleteCodeCancel, obsoleteCodeDistroy, obsoleteCodeOkLoading } = this.props;
        const { checkCodesRes } = this.state;
        const { getFieldDecorator } = this.props.form;
        return (
            <Modal
                title="作废激活码"
                visible={obsoleteCodeVisible}
                // onOk={this.handleSubmit}
                onCancel={obsoleteCodeCancel}
                footer={null}
                destroyOnClose={obsoleteCodeDistroy}
                width={800}
            // cancelText="取消"
            // okText="确定"
            >
                <Form onSubmit={this.handleSubmit}>
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
                                <p style={{ fontSize: 12, margin: 0 }}>共提交{checkCodesRes.allCodes.length}个激活码，其中 <span style={{ color: "red" }}>{checkCodesRes.obsoleteCodes.length + checkCodesRes.notExistCodes.length + checkCodesRes.returnCodes.length}</span> 个状态有误， <span style={{ color: "blue" }}>{checkCodesRes.allCodes.length - checkCodesRes.obsoleteCodes.length - checkCodesRes.notExistCodes.length - checkCodesRes.returnCodes.length}</span> 个可作废</p>
                                {checkCodesRes.commonGoods == false && <p style={{ fontSize: 12, margin: 0, color: "red" }}>激活码隶属于多个商品，一次只可以作废同一商品的激活码</p>}
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
                                    {checkCodesRes.obsoleteCodes && checkCodesRes.obsoleteCodes.map(item => (
                                        <tr style={{ color: "red" }}>
                                            <td>{item.actCode}</td>
                                            <td>作废</td>
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
                                    {checkCodesRes.generateCodes && checkCodesRes.generateCodes.map(item => (
                                        <tr>
                                            <td>{item.actCode}</td>
                                            <td>生成</td>
                                            <td>{item.goodsId}</td>
                                            <td>{item.goodsShortName}</td>
                                        </tr>
                                    ))}
                                    {checkCodesRes.outCodes && checkCodesRes.outCodes.map(item => (
                                        <tr>
                                            <td>{item.actCode}</td>
                                            <td>出库</td>
                                            <td>{item.goodsId}</td>
                                            <td>{item.goodsShortName}</td>
                                        </tr>
                                    ))}
                                    {checkCodesRes.activeCodes && checkCodesRes.activeCodes.map(item => (
                                        <tr>
                                            <td>{item.actCode}</td>
                                            <td>激活</td>
                                            <td>{item.goodsId}</td>
                                            <td>{item.goodsShortName}</td>
                                        </tr>
                                    ))}
                                    {checkCodesRes.runOutCodes && checkCodesRes.runOutCodes.map(item => (
                                        <tr>
                                            <td>{item.actCode}</td>
                                            <td>用完</td>
                                            <td>{item.goodsId}</td>
                                            <td>{item.goodsShortName}</td>
                                        </tr>
                                    ))}
                                    {checkCodesRes.pastCodes && checkCodesRes.pastCodes.map(item => (
                                        <tr>
                                            <td>{item.actCode}</td>
                                            <td>过期</td>
                                            <td>{item.goodsId}</td>
                                            <td>{item.goodsShortName}</td>
                                        </tr>
                                    ))}
                                </table>
                                {/* {checkCodesRes.notExistCodes && checkCodesRes.notExistCodes.map(item => (<p>{item}  不存在</p>))}
                                {checkCodesRes.obsoleteCodes && checkCodesRes.obsoleteCodes.map(item => (<p>{item}  已作废</p>))} */}
                            </Form.Item>
                            <Form.Item label="备注说明">
                                {getFieldDecorator('obsoleteRemarks', {})(
                                    <TextArea autosize={{ minRows: 4, maxRows: 8 }} placeholder="备注说明" />
                                )}
                            </Form.Item>
                            {checkCodesRes.commonGoods == true && checkCodesRes.allCodes.length - checkCodesRes.obsoleteCodes.length - checkCodesRes.notExistCodes.length - checkCodesRes.returnCodes.length > 0 && <Form.Item style={{ textAlign: "center" }}>
                                <Button type="primary" onClick={this.handleSubmit} loading={obsoleteCodeOkLoading}>确认作废</Button>
                            </Form.Item>}
                        </div>
                    }
                </Form>
            </Modal>
        );
    }
}

export default ObsoleteActCodeModal;