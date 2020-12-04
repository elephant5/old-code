import React, { Component } from 'react';
import { Form, Input, Button, Select, Checkbox } from 'antd';
import './newfilter.less'

const { Option } = Select;
@Form.create()
class Calculation extends Component {
    // 表单绑定
    getForm = () => {
        const { getFieldDecorator } = this.props.form;
        let formProps = {};
        formProps.hotelOrShopName = getFieldDecorator('hotelOrShopName', {

        });
        formProps.service = getFieldDecorator('service', {

        });
        formProps.gift = getFieldDecorator('gift', {

        });
        formProps.city = getFieldDecorator('city', {

        });
        formProps.startCost = getFieldDecorator('startCost', {

        });
        formProps.endCost = getFieldDecorator('endCost', {

        });
        formProps.startMoney = getFieldDecorator('startMoney', {

        });
        formProps.endMoney = getFieldDecorator('endMoney', {

        });
        formProps.dateshowstart = getFieldDecorator('dateshowstart', {

        });
        formProps.dateshowend = getFieldDecorator('dateshowend', {

        });
        formProps.startDiscount = getFieldDecorator('startDiscount', {

        });
        formProps.endDiscount = getFieldDecorator('endDiscount', {

        });
        
        formProps.statusType = getFieldDecorator('statusType', {

        });
        formProps.weekType = getFieldDecorator('weekType', {

        });
        formProps.channelType = getFieldDecorator('channelType', {

        });
        return formProps;
    }



    render() {
        const formProps = this.getForm();
        const { resourceType, giftTypeList, cityList } = this.props;
        return (
            <div className="c-filter">
                <div className="calcuBox">
                    <div className="titleDiv">成本测算器 <span>当前产品成本最高价：￥132.0</span><span>当前产品成本最低价：￥0</span><span>当前产品成本均价：￥44.0</span></div>
                    <div style={{marginTop:'22px'}}>
                    <Form layout="inline" onSubmit={this.handleSubmit}>
                        <Form.Item label="使用率">
                            {formProps.startDiscount(
                                <Input style={{ width: 97 }}></Input>
                            )}
                        </Form.Item>
                        <Form.Item label="资源类型">
                            {formProps.service(
                                <Select style={{ width: 160 }} mode='multiple' showSearch
                                    filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                                    {/* <Option value="">全部</Option> */}
                                    {
                                        resourceType.map(item => {
                                            return <Option key={item.name}>{item.name}</Option>
                                        })
                                    }
                                </Select>
                            )}
                    </Form.Item>
                    <Form.Item label="权益类型">
                        {formProps.gift(
                            <Select style={{ width: 160 }} mode='multiple' showSearch
                                filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                                {/* <Option value="">全部</Option> */}
                                {
                                    giftTypeList.map(item => {
                                        return <Option key={item.shortName}>{item.shortName}</Option>
                                    })
                                }
                            </Select>
                        )}
                    </Form.Item>
                    <Form.Item label="权益次数">
                             {formProps.endDiscount(
                                <Input style={{ width: 97 }}></Input>
                            )}
                        </Form.Item>
                        <Form.Item>
                            <Button
                                type="primary"
                                htmlType="submit"
                            >
                                查询
                            </Button>
                        </Form.Item>
                </Form>
                        
                    </div>
                </div>
                <div className="calcuBox">
                    <div className="titleDiv">测算结果</div>
                    <p>预计极值成本：￥44.0</p>
                    <p>预计均值成本：￥44.0</p>
                    <p>预计加权均值成本：￥44.0</p>
                </div>
            </div>
        );
    }
}

export default Calculation;