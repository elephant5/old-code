import React, { Component } from 'react';
import { Modal, Form, Radio, InputNumber ,Divider} from 'antd';
const RadioGroup = Radio.Group;

const InputNumberWith = 80;
@Form.create()
class ruleModal extends Component {
    state = {
        value: 1,
    }
    onChange = e => {
        this.setState({
            value: e.target.value
        })
    }
    handleSubmit = e => {
        e.preventDefault();
        const { onOk, data } = this.props;
        this.props.form.validateFields((err, values) => {
            if (!err) {
                const expressVo = {
                    discount: null,
                    taxFeePer: null,
                    serviceFeePer: null,
                    netPricePer: null,
                    customTaxFeePer: null,
                    taxNetPricePer: null,
                    adjust: null,
                    taxServiceFeePer: null
                }
                switch (this.state.value){
                    case 1:
                        onOk({
                            expressVo: expressVo,
                            gift: data.gift
                        })
                        break;
                    case 2:
                        onOk({
                            expressVo: { ...expressVo,
                                discount: values.discount1,
                                taxFeePer: values.taxFeePer1 * 0.01,
                                serviceFeePer: values.serviceFeePer1 * 0.01,
                                netPricePer: values.netPricePer1 * 0.01,
                            },
                            gift: data.gift
                        })
                        break;
                    case 3:
                        onOk({
                            expressVo: { ...expressVo,
                                netPricePer: values.netPricePer * 0.01,
                                serviceFeePer: values.serviceFeePer *0.01,
                                customTaxFeePer: values.customTaxFeePer * 0.01,
                                discount: values.discount,
                                taxNetPricePer: values.taxNetPricePer * 0.01,
                                taxServiceFeePer: values.taxServiceFeePer * 0.01,
                                adjust: values.adjust,
                            },
                            gift: data.gift
                        })
                        break;
                    default:
                        break;
                } 
            }
        })
    }
    getForm = () => {
        const { getFieldDecorator } = this.props.form;
        let formProps = {};
        formProps.netPricePer1 = getFieldDecorator('netPricePer1', {
        });
        formProps.serviceFeePer1 = getFieldDecorator('serviceFeePer1', {
        });
        formProps.taxFeePer1 = getFieldDecorator('taxFeePer1', {
            initialValue: ''
        });
        formProps.discount1 = getFieldDecorator('discount1', {
        });
        formProps.netPricePer = getFieldDecorator('netPricePer', {
        });
        formProps.serviceFeePer = getFieldDecorator('serviceFeePer', {
        });
        formProps.taxFeePer = getFieldDecorator('taxFeePer', {
            initialValue: ''
        });
        formProps.discount = getFieldDecorator('discount', {
        });
        formProps.taxNetPricePer = getFieldDecorator('taxNetPricePer', {
        });
        formProps.taxServiceFeePer = getFieldDecorator('taxServiceFeePer', {
        });
        formProps.adjust = getFieldDecorator('adjust', {
        });
        formProps.customTaxFeePer = getFieldDecorator('customTaxFeePer', {
        });
        return formProps;
    }
    render() {
        const formProps = this.getForm();
        const { onCancel, data } = this.props;
        return (
            <Modal
                title="结算规则"
                visible={true}
                onOk={this.handleSubmit}
                onCancel={onCancel}
                width={'60%'}
                cancelText='取消'
                okText='确定'
            >
                <Form onSubmit={this.handleSubmit}>
                    <RadioGroup onChange={this.onChange} value={this.state.value}>
                    <Radio value={1} style={{display:'block'}}>无</Radio>
                    <Divider dashed={true}  />
                    <Radio value={2}>标准算法</Radio>
                        <div style={{display: 'flex'}}>
                            <Form.Item
                                label="产品净价 * "
                                style={{display:'flex'}}
                            >
                            {
                                formProps.netPricePer1(
                                    <InputNumber style={{width: InputNumberWith}} min={0}></InputNumber>
                                )
                            }&nbsp;%<label style={{marginLeft:8,marginRight:8,}}>  + </label>
                            </Form.Item>
                            <Form.Item
                                label="产品服务费 *"
                                style={{display:'flex'}}
                            >
                            {
                                formProps.serviceFeePer1(
                                    <InputNumber style={{width: InputNumberWith}}  min={0}></InputNumber>
                                )
                            }&nbsp;%<label style={{marginLeft:8,marginRight:8,}}>  + </label>
                            </Form.Item>
                            <Form.Item
                                label="增值税 * "
                                style={{display:'flex'}}
                            >
                            {
                                formProps.taxFeePer1(
                                    <InputNumber style={{width: InputNumberWith}}  min={0}></InputNumber>
                                )
                            }&nbsp;%<label style={{marginLeft:8,marginRight:8,}}>  + </label>
                            </Form.Item>
                            <Form.Item
                                label="固定贴现"
                                style={{display:'flex'}}
                            >
                            {
                                formProps.discount1(
                                    <InputNumber style={{width: InputNumberWith}}  min={0} ></InputNumber>
                                )
                            }
                            </Form.Item>
                            <Form.Item
                                style={{display:'flex'}}
                            >
                                <div>&nbsp;&nbsp;=&nbsp;&nbsp;<span className="c-color-red">0</span></div>
                            </Form.Item>
                        </div>
                        <Divider dashed={true}  />
                    <Radio value={3} style={{display:'block'}}>特殊算法
                    </Radio>
                    <div>
                        <div style={{display: 'flex'}}>
                            <Form.Item
                                label="产品净价 * "
                                style={{display:'flex'}}
                            >
                            {
                                formProps.netPricePer(
                                    <InputNumber style={{width: InputNumberWith}} style={{width:'100px'}} min={0}></InputNumber>
                                )
                            }&nbsp;%<label style={{marginLeft:8,marginRight:8,}}>  + </label>
                            </Form.Item>
                            <Form.Item
                                label="产品服务费 X"
                                style={{display:'flex'}}
                            >
                            {
                                formProps.serviceFeePer(
                                    <InputNumber style={{width: InputNumberWith}}  min={0} ></InputNumber>
                                )
                            }&nbsp;%<label style={{marginLeft:8,marginRight:8,}}>  + </label>
                            </Form.Item>
                            <Form.Item
                                label="自定义增值税 * "
                                style={{display:'flex'}}
                            >
                            {
                                formProps.customTaxFeePer(
                                    <InputNumber style={{width: InputNumberWith}}  min={0}></InputNumber>
                                )
                            }%<label style={{marginLeft:8,marginRight:8,}}>  + </label>
                            </Form.Item>
                            <Form.Item
                                label="固定贴现"
                                style={{display:'flex'}}
                            >
                            {
                                formProps.discount(
                                    <InputNumber style={{width: InputNumberWith}}  min={0}></InputNumber>
                                )
                            }
                            </Form.Item>
                            <Form.Item
                                style={{display:'flex'}}
                            >
                                <div>&nbsp;&nbsp;=&nbsp;&nbsp;<span className="c-color-red">0</span></div>
                            </Form.Item>
                        </div>
                        <div style={{display: 'flex'}}>
                            <Form.Item
                                label="自定义增值税  =  产品净价 *  "
                                style={{display:'flex'}}
                            >
                            {
                                formProps.taxNetPricePer(
                                    <InputNumber style={{width: InputNumberWith}}  min={0} ></InputNumber>
                                )
                            }&nbsp;%<label style={{marginLeft:8,marginRight:8,}}>  + </label>
                            </Form.Item>
                            <Form.Item
                                label="产品服务费 * "
                                style={{display:'flex'}}
                            >
                            {
                                formProps.taxServiceFeePer(
                                    <InputNumber style={{width: InputNumberWith}}  min={0} ></InputNumber>
                                )
                            }&nbsp;% <label style={{marginLeft:8,marginRight:8,}}>  + </label>
                            </Form.Item>
                            <Form.Item
                                label="调整金额"
                                style={{display:'flex'}}
                            >
                            {
                                formProps.adjust(
                                    <InputNumber style={{width: InputNumberWith}}  min={0}></InputNumber>
                                )
                            }
                            </Form.Item>
                        </div>
                    </div>
                </RadioGroup>
                </Form>
            </Modal>
        );
    }
}

export default ruleModal;