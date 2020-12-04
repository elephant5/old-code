import React, { Component } from 'react';
import { Modal, Form, DatePicker, Input, Select, Checkbox, InputNumber, Button, message } from 'antd';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import GeneratesOut from './generatesOut';
import * as actions from '../../../store/giftCode/action';
import * as goodsActions from '../../../store/goods/action';
import CheckboxGroup from 'antd/lib/checkbox/Group';

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
class GenerateActCodeModal extends Component {
    constructor(props) {
        super(props);
        this.state = {
            goodsChannelList: null,
            generateCodeUploadLoading: false,
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

    //生成激活码提交
    handleSubmit = e => {
        e.preventDefault();
        const { generateCodeOk } = this.props;
        this.props.form.validateFields((err, values) => {
            if (!err) {
                generateCodeOk('generate', {
                    ...values
                })
            }
        })
    }

    //立即出库
    immOutCode = e => {
        e.preventDefault();
        const { codeGoodsId, immOutCode } = this.props;
        this.props.actions.selectGoodsChannel(codeGoodsId);
        immOutCode();
    }

    //立即下载
    immUpload = e => {
        e.preventDefault();
        const { codeBatchNo } = this.props;
        this.setState({
            generateCodeUploadLoading: true,
        })
        this.props.actions.exportExcel(codeBatchNo).then((data) => {
            const { code } = this.props;
            if (code.exportExcelRes.code == 100 && code.exportExcelRes.result != null) {
                window.open(code.exportExcelRes.result)
            } else {
                message.error("系统错误");
            }
            this.setState({
                generateCodeUploadLoading: false,
            })
        }).catch((error) => {
            message.error("系统出错")
            this.setState({
                generateCodeUploadLoading: false,
            })
        });
    }

    //确认出库
    okOutCode = params => {
        const { generateOutCode } = this.props;
        generateOutCode({ ...params })
    }

    render() {
        const { generateCodeVisible, generateCodeCancel, allGoodsList, codeBatchNo, actCodeTags, isOut, genImmUpload, generateCodeOkLoading,generateOutCodeLoading } = this.props;
        const { goodsChannelList } = this.state;
        const { getFieldDecorator } = this.props.form;
        return (
            <Modal
                title="生成激活码"
                visible={generateCodeVisible}
                // onOk={this.handleSubmit}
                onCancel={generateCodeCancel}
                footer={null}
            // cancelText="取消"
            // okText="确定"
            >
                <Form onSubmit={this.handleSubmit}>
                    {isOut === false && <div>
                        <Form.Item label="商品:">
                            {getFieldDecorator('goodsId', { rules: [{ required: true, message: '请选择商品!' }] })(
                                <Select showSearch placeholder="请选择..."
                                    filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                                    {
                                        allGoodsList.map(item => (
                                            <Option key={item.id} value={item.id}>{'#' + item.id + ' ' + item.shortName}</Option>
                                        ))
                                    }
                                </Select>
                            )}
                        </Form.Item>
                        <Form.Item label="激活码数量:">
                            {getFieldDecorator('actCodeNum', { rules: [{ required: true, message: '请输入激活码数量!' }] })(
                                <InputNumber style={{ width: '100%' }} min="1" max="10000" placeholder="激活码数量(单次生成不超过一万个)" />
                            )}
                        </Form.Item>
                        <Form.Item label="备注说明:">
                            {getFieldDecorator('remarks', {})(
                                <TextArea autosize={{ minRows: 4, maxRows: 8 }} placeholder="备注说明" />
                            )}
                        </Form.Item>
                        {!codeBatchNo && <Form style={{ textAlign: "center" }}>
                            <Button onClick={generateCodeCancel}
                            >
                                关闭
                        </Button>
                            <Button
                                style={{ marginLeft: 10 }}
                                type="primary"
                                onClick={this.handleSubmit}
                                loading={generateCodeOkLoading}
                            >
                                确定
                        </Button>
                        </Form>}
                        {codeBatchNo && <Form style={{ textAlign: "center" }}>
                            <Button type="primary" onClick={this.immOutCode}>
                                立即出库
                        </Button>
                            <Button type="primary" style={{ marginLeft: 10 }} onClick={this.immUpload} loading={this.state.generateCodeUploadLoading}>
                                立即下载
                        </Button>
                        </Form>}
                    </div>}

                    {isOut === true &&
                        <GeneratesOut
                            actCodeTags={actCodeTags}
                            goodsChannelList={goodsChannelList}
                            okOutCode={this.okOutCode}
                            codeBatchNo={codeBatchNo}
                            genImmUpload={genImmUpload}
                            generateOutCodeLoading={generateOutCodeLoading}
                        />}
                </Form>
            </Modal>
        );
    }
}

export default GenerateActCodeModal;