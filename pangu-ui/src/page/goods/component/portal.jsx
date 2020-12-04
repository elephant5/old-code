import React, { Component } from 'react';
import { Form, message, Select, Input, Button, Tabs } from 'antd';
import cookie from 'react-cookies';

const Option = Select.Option;
const formItemLayout = {
    labelCol: { span: 4 },
    wrapperCol: { span: 8 },
};
const dateFormat = 'YYYY-MM-DD';
@Form.create()
class Portal extends Component {
    constructor(props) {
        super(props);
        this.state = {
            goodsId: null,
            code: null,
        };
    }
    componentDidMount() {
        const { goodsId, code } = this.props;
        this.setState({
            goodsId: goodsId,
            code: code,
        })
    }
    componentWillReceiveProps(nextProps) {
        const { goodsId } = nextProps;


    }
    handleChange = (e) => {
        //   console.info( this.props.form)
        //   console.info( this.props.form.getFieldsValue())
        //   console.info( this.props.form.getFieldValue('code'))
        setTimeout(() => {
            const value = this.props.form.getFieldValue('code');
            if (value) {
                this.setState({
                    code: value,
                })
            }
        });
    }
    // 保存基本信息
    handleSubmit = e => {
        e.preventDefault();
        const { onEvent, data } = this.props;

        this.props.form.validateFields((err, values) => {

            if (!err) {
                if (values.id === null || values.id === undefined) {
                    onEvent('insertGoodsPortalSetting', values);
                } else {
                    onEvent('updateGoodsPortalSetting', values);
                }

            }
        });
    }

    chooseChannel = (values) => {
        if (!values) return;
        const { onEvent } = this.props;
        onEvent('getBankLogoList', { bankLogoList: values });
    }

    render() {
        const { getFieldDecorator } = this.props.form;
        const { goodsPortalSetting, onEvent, bankLogoList } = this.props;
        const { goodsId, code } = this.state;
        const edit = cookie.load("KLF_PG_GM_GL_EDIT");
        // console.info("goodsPortalSetting")
        // console.info(goodsPortalSetting)
        return (

            <div className="c-modal">
                <div className="c-title">基本信息</div>
                <Form onSubmit={this.handleSubmit}>
                    <Form.Item style={{ marginTop: '20px' }} label="站点地址：" labelCol={{ span: 2 }} wrapperCol={{ span: 5 }}>
                        {getFieldDecorator('code', { rules: [{ required: true, message: '请输入站点地址' }], initialValue: goodsPortalSetting.code })(
                            <Input onChange={this.handleChange} maxLength={20} allowClear />
                        )}
                        {getFieldDecorator('id', { initialValue: goodsPortalSetting.id })(
                            <Input style={{ display: 'none' }} />

                        )}
                        {getFieldDecorator('goodsId', { initialValue: goodsPortalSetting.goodsId ? goodsPortalSetting.goodsId : goodsId })(
                            <Input style={{ display: 'none' }} />

                        )}
                        <br />
                        <span style={{ fontSize: '12px', marginTop: '-10px' }} >{goodsPortalSetting.giftUrl}<span>{code}</span></span>
                        <br />
                        <span style={{ fontSize: '12px', marginTop: '-10px' }} >{goodsPortalSetting.shortUrl}</span>
                    </Form.Item>
                    <Form.Item style={{ marginTop: '20px' }} label="网站标题：" labelCol={{ span: 2 }} wrapperCol={{ span: 5 }}>
                        {getFieldDecorator('title', {
                            rules: [{ required: true, message: '请输入网站标题' }], initialValue: goodsPortalSetting.title
                        })(<Input allowClear />
                        )}
                    </Form.Item>
                    <Form.Item style={{ marginTop: '20px' }} label="Logo：" labelCol={{ span: 2 }} wrapperCol={{ span: 8 }}>
                        {getFieldDecorator('bankLogoId', { rules: [{ required: false, message: '请选择Logo' }], initialValue: goodsPortalSetting.bankLogoId })
                            (<Select showSearch
                                filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0} style={{ width: '50%' }}>
                                {
                                    bankLogoList && bankLogoList.map(item => (
                                        <Option key={item.id} value={item.id}>{item.name}</Option>
                                    ))
                                }
                            </Select>
                            )}
                    </Form.Item>
                    <div className="c-title">基础样式</div>

                    <Form.Item label="网页背景颜色" labelCol={{ span: 2 }} wrapperCol={{ span: 8 }} >
                        {getFieldDecorator('styleBodyFill', { initialValue: goodsPortalSetting.styleBodyFill })(
                            <Input />
                        )}
                    </Form.Item>
                    <Form.Item label="网页文字颜色" labelCol={{ span: 2 }} wrapperCol={{ span: 8 }} >
                        {getFieldDecorator('styleBodyText', { initialValue: goodsPortalSetting.styleBodyText })(
                            <Input />
                        )}
                    </Form.Item>
                    <Form.Item label="按钮背景颜色" labelCol={{ span: 2 }} wrapperCol={{ span: 8 }} >
                        {getFieldDecorator('styleBtnFill', { initialValue: goodsPortalSetting.styleBtnFill })(
                            <Input />
                        )}
                    </Form.Item>
                    <Form.Item label="按钮文字颜色" labelCol={{ span: 2 }} wrapperCol={{ span: 8 }} >
                        {getFieldDecorator('styleBtnText', { initialValue: goodsPortalSetting.styleBtnText })(
                            <Input />
                        )}
                    </Form.Item>
                    <Form.Item label="超链接颜色" labelCol={{ span: 2 }} wrapperCol={{ span: 8 }} >
                        {getFieldDecorator('styleLinkText', { initialValue: goodsPortalSetting.styleLinkText })(
                            <Input />
                        )}
                    </Form.Item>
                    <br></br>
                    <Form.Item>
                        <div style={{ padding: '1% 0 1%' }}>
                            {edit && <Button type="primary" htmlType="submit" >保存</Button>} &nbsp;&nbsp;&nbsp;&nbsp;
                     <Button onClick={() => onEvent('cancel')} >取消</Button>
                        </div>

                    </Form.Item>


                </Form>
            </div>

        );
    }
}

export default Portal;