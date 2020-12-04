import React, { Component } from 'react';
import { Modal, Form, DatePicker, Input, Select, Checkbox, InputNumber, Button, message } from 'antd';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import CheckboxGroup from 'antd/lib/checkbox/Group';
import * as actions from '../../../store/giftCode/action';

const { Option } = Select;
const { TextArea } = Input;

@connect(
    ({ operation, code }) => ({
        operation,
        code: code.toJS()
    }),
    dispatch => ({
        actions: bindActionCreators(actions, dispatch)
    })
)
@Form.create()
class GeneratesOut extends Component {
    constructor(props) {
        super(props);
        this.state = {
            generateOutCodeUpLoadLoading: false,
        };
    }

    componentDidMount() {
    }

    //确认出库
    handleSubmit = e => {
        e.preventDefault();
        const { okOutCode, codeBatchNo } = this.props;
        this.props.form.validateFields((err, values) => {
            if (!err) {
                okOutCode({ ...values, codeBatchNo: codeBatchNo })
            }
        })
    }

    //立即下载
    immUpload = e => {
        e.preventDefault();
        const { codeBatchNo } = this.props;
        this.setState({
            generateOutCodeUpLoadLoading: true,
        })
        this.props.actions.exportExcel(codeBatchNo).then((data) => {
            const { code } = this.props;
            if (code.exportExcelRes.code == 100 && code.exportExcelRes.result != null) {
                window.open(code.exportExcelRes.result)
            } else {
                message.error("系统错误");
            }
            this.setState({
                generateOutCodeUpLoadLoading: false,
            })
        }).catch((error) => {
            message.error("系统出错")
            this.setState({
                generateOutCodeUpLoadLoading: false,
            })
        });
    }

    render() {
        const { actCodeTags, goodsChannelList, genImmUpload, generateOutCodeLoading } = this.props;
        const { getFieldDecorator } = this.props.form;
        return (
            <Form onSubmit={this.handleSubmit}>
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
                <Form.Item style={{ textAlign: "center" }}>
                    {!genImmUpload && <Button type="primary" onClick={this.handleSubmit} loading={generateOutCodeLoading}>确认出库</Button>}
                    {genImmUpload && <Button type="primary" onClick={this.immUpload} loading={this.state.generateOutCodeUpLoadLoading}>立即下载</Button>}
                </Form.Item>
            </Form>
        );
    }
}

export default GeneratesOut;