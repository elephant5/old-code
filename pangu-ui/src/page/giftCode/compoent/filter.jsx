import React, { Component } from 'react';
import { Form, Input, Button, Select, Cascader, Checkbox, DatePicker } from 'antd';
import cookie from 'react-cookies';

const widthTemp = { width: 270 };
const { Option } = Select;
const { MonthPicker, RangePicker, WeekPicker } = DatePicker;
@Form.create()
class Filter extends Component {
    // 查询
    handleSubmit = e => {
        e.preventDefault();
        const { onEvent } = this.props;
        this.props.form.validateFields((err, values) => {
            if (!err) {
                onEvent('search', {
                    ...values
                })
            }
        });
    }

    //导出
    export = e => {
        e.preventDefault();
        const { onEvent } = this.props;
        this.props.form.validateFields((err, values) => {
            if (!err) {
                onEvent('export', {
                    ...values
                })
            }
        })
    }

    // 重置
    reset = () => {
        this.props.form.resetFields();
    }
    //生成激活码modal
    generateCodeModal = () => {
        const { onEvent } = this.props;
        onEvent("generateCodeModal", {

        })
    }

    //出库激活码modal
    outCodeModal = () => {
        const { onEvent } = this.props;
        onEvent("outCodeModal", {

        })
    }

    //退货激活码modal
    returnCodeModal = () => {
        const { onEvent } = this.props;
        onEvent("returnCodeModal", {

        })
    }

    //作废激活码modal
    obsoleteCodeModal = () => {
        const { onEvent } = this.props;
        onEvent("obsoleteCodeModal", {

        })
    }

    //延长有效期modal
    prolongCodeModal = () => {
        const { onEvent } = this.props;
        onEvent("prolongCodeModal", {

        })
    }
    // 表单绑定
    getForm = () => {
        const { getFieldDecorator } = this.props.form;
        let formProps = {};
        formProps.codeBatchNo = getFieldDecorator('codeBatchNo', {

        });
        formProps.giftCodes = getFieldDecorator('giftCodes', {

        });
        formProps.goodsIds = getFieldDecorator('goodsIds', {

        });
        formProps.salesChannel = getFieldDecorator('salesChannel', {

        });
        formProps.codeStatus = getFieldDecorator('codeStatus', {

        });
        formProps.expireTimes = getFieldDecorator('expireTimes', {

        });
        return formProps;
    }
    render() {
        const { getFieldDecorator } = this.props.form;
        const formProps = this.getForm();
        const { allGoodsList, salesChannelList, exportListLoading } = this.props;
        const gen = cookie.load("KLF_PG_GM_AL_GEN");
        const out = cookie.load("KLF_PG_GM_AL_OUT");
        const ren = cookie.load("KLF_PG_GM_AL_REN");
        const obs = cookie.load("KLF_PG_GM_AL_OBS");
        const delay = cookie.load("KLF_PG_GM_AL_DELAY");
        return (
            <div className="c-filter">
                <Form layout="inline" onSubmit={this.handleSubmit}>
                    <Form.Item label="批次号:">
                        {formProps.codeBatchNo(
                            <Input placeholder="批次号（模糊）" />
                        )}
                    </Form.Item>
                    <Form.Item label="激活码:">
                        {formProps.giftCodes(
                            <Input placeholder="激活码，逗号隔开（精确）" />
                        )}
                    </Form.Item>
                    <Form.Item label="隶属商品:" >
                        {formProps.goodsIds(
                            <Select style={widthTemp} mode='multiple' showSearch placeholder="请选择..."
                                filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                                {
                                    allGoodsList.map(item => (
                                        <Option key={item.id} value={item.id}>{'#' + item.id + ' ' + item.shortName}</Option>
                                    ))
                                }
                            </Select>

                        )}
                    </Form.Item>
                    <Form.Item label="手机号码:">
                        {getFieldDecorator("phone", {})(
                            <Input placeholder="客户手机号"></Input>
                        )}
                    </Form.Item>
                    <br></br>
                    <Form.Item label="销售渠道:" >
                        {formProps.salesChannel(
                            <Cascader style={widthTemp} options={salesChannelList} placeholder="请选择..." />
                        )}
                    </Form.Item>
                    <br></br>
                    <Form.Item label="激活码状态" >
                        {formProps.codeStatus(
                            <Checkbox.Group style={{ width: '100%' }} >
                                <Checkbox value="0">生成</Checkbox>
                                <Checkbox value="1">出库</Checkbox>
                                <Checkbox value="2">激活</Checkbox>
                                <Checkbox value="3">用完</Checkbox>
                                <Checkbox value="4">过期</Checkbox>
                                <Checkbox value="5">退货</Checkbox>
                                <Checkbox value="6">作废</Checkbox>
                            </Checkbox.Group>
                        )}
                    </Form.Item>
                    <br></br>
                    <Form.Item label="激活码到期时间" >
                        {formProps.expireTimes(
                            <RangePicker style={widthTemp} format="YYYY-MM-DD" />
                        )}
                    </Form.Item>
                    <Form.Item>
                        <Button
                            type="primary"
                            htmlType="submit"
                        >
                            查询
                        </Button>
                        <Button onClick={this.reset} style={{ marginLeft: 10 }}
                        >
                            重置
                        </Button>
                        <Button onClick={this.export} style={{ marginLeft: 10 }} loading={exportListLoading}>
                            导出
                        </Button>
                    </Form.Item>
                </Form>
                {gen && <Button type="primary" onClick={this.generateCodeModal}>生成激活码</Button>}
                {out && <Button type="primary" onClick={this.outCodeModal} style={{ marginLeft: 10 }}>出库激活码</Button>}
                {ren && <Button type="primary" onClick={this.returnCodeModal} style={{ marginLeft: 10 }}>退货激活码</Button>}
                {obs && <Button type="primary" onClick={this.obsoleteCodeModal} style={{ marginLeft: 10 }}>作废激活码</Button>}
                {delay && <Button type="primary" onClick={this.prolongCodeModal} style={{ marginLeft: 10 }}>延长有效期</Button>}
            </div>
        );
    }
}

export default Filter;