import React, { Component } from 'react';
import { Form, Input, Button, Select, Checkbox, DatePicker } from 'antd';
import './newfilter.less'
import moment from 'moment';
import 'moment/locale/zh-cn';
moment.locale('zh-cn');
const { MonthPicker, RangePicker, WeekPicker } = DatePicker;
const { Option } = Select;
@Form.create()
class AccomFilter extends Component {
    // 查询
    handleSubmit = e => {
        e.preventDefault();
        const { onEvent } = this.props;
        this.props.form.validateFields((err, values) => {
            if (!err) {
                onEvent('accomSearch', {
                    ...values,
                    startDate: values.canOrderTime == null ? null : moment(values.canOrderTime[0]).format(),
                    endDate: values.canOrderTime == null ? null : moment(values.canOrderTime[1]).format(),
                    startCost: values.startCost ? values.startCost : null,
                    endCost: values.endCost ? values.endCost : null,
                })
            }
        });
    }
    // 重置
    reset = () => {
        this.props.form.resetFields();
    }
    /**
 * 导出产品组产品
 */
    export = e => {
        e.preventDefault();
        const { onEvent } = this.props;
        this.props.form.validateFields((err, values) => {
            if (!err) {
                onEvent('accomExport', {
                    ...values
                })
            }
        });
    }
    // 表单绑定
    getForm = () => {
        const { getFieldDecorator } = this.props.form;
        let formProps = {};
        formProps.hotelOrShopName = getFieldDecorator('hotelOrShopName', {

        });
        formProps.service = getFieldDecorator('service', {
            initialValue: ['住宿']
        });

        formProps.city = getFieldDecorator('city', {

        });
        formProps.gift = getFieldDecorator('gift', {

        });
        formProps.channelTypes = getFieldDecorator('channelTypes', {
            // initialValue: ['1']
        });
        formProps.canOrderTime = getFieldDecorator('canOrderTime', {

        })
        formProps.startCost = getFieldDecorator('startCost', {

        });
        formProps.endCost = getFieldDecorator('endCost', {

        });
        formProps.startNetPrice = getFieldDecorator('startNetPrice', {

        });
        formProps.endNetPrice = getFieldDecorator('endNetPrice', {

        });
        formProps.startItemRate = getFieldDecorator('startItemRate', {

        });
        formProps.endItemRate = getFieldDecorator('endItemRate', {

        });
        formProps.applyWeek = getFieldDecorator('applyWeek', {
            // initialValue: ['1','2','3','4','5','6','7']
        });
        formProps.isExpiry = getFieldDecorator('isExpiry', {

        });
        formProps.statusTypes = getFieldDecorator('statusTypes', {

        });
        return formProps;
    }
    render() {
        const formProps = this.getForm();
        const { resourceType, giftTypeList, cityList } = this.props;
        return (
            <div className="c-filter">
                <Form layout="inline" onSubmit={this.handleSubmit}>
                    <Form.Item label="资源类型"  style={{ width: '340px' }}>
                        {formProps.service(
                            <Select style={{ width: 220 }} disabled={true} mode='multiple' showSearch
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
                    <Form.Item label="酒店|商户:" style={{ width: '340px' }}>
                        {formProps.hotelOrShopName(
                            <Input style={{ width: 180 }} placeholder="搜索酒店/商户" />
                        )}
                    </Form.Item>
                    <Form.Item label="酒店城市:" style={{ width: '340px' }}>
                        {formProps.city(
                            <Select style={{ width: 180 }} mode='multiple' showSearch placeholder="选择城市"
                                filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                                <Option value="">全部</Option>
                                {
                                    cityList.map(item => {
                                        return <Option key={'city2' + item.id} value={item.nameCh}>{item.nameCh}</Option>;
                                    })
                                }
                            </Select>
                        )}
                    </Form.Item>

                    <Form.Item label="适用日期:" style={{ width: '340px' }}>
                        {formProps.canOrderTime(
                            <RangePicker style={{ width: 180 }} />
                        )}
                    </Form.Item>

                    <Form.Item label="成本价格:" style={{ width: '340px' }}>
                        {formProps.startCost(
                            <Input style={{ width: 97 }} addonBefore='￥'></Input>
                        )}&nbsp;&nbsp;-&nbsp;&nbsp; {formProps.endCost(
                            <Input style={{ width: 97 }} addonBefore='￥' ></Input>
                        )}
                    </Form.Item>

                    {/* {noAccom && <Form.Item label="净价">
                        {formProps.startNetPrice(
                            <Input style={{ width: 97 }} addonBefore='￥' disabled={true}></Input>
                        )}&nbsp;&nbsp;-&nbsp;&nbsp; {formProps.endNetPrice(
                            <Input style={{ width: 97 }} addonBefore='￥' disabled={true}></Input>
                        )}
                    </Form.Item>}
                    {noAccom && <Form.Item label="折扣率">
                        {formProps.startItemRate(
                            <Input style={{ width: 97 }} disabled={true}></Input>
                        )}&nbsp;&nbsp;-&nbsp;&nbsp; {formProps.endItemRate(
                            <Input style={{ width: 97 }} disabled={true}></Input>
                        )}
                    </Form.Item>} */}
                    <Form.Item label="适用星期" style={{ width: '340px' }}>
                        {formProps.applyWeek(
                            <Checkbox.Group style={{ width: '220' }}  >
                                <Checkbox value="1">周一</Checkbox>
                                <Checkbox value="2" >周二</Checkbox>
                                <Checkbox value="3" >周三</Checkbox>
                                <Checkbox value="4" >周四</Checkbox><br></br>
                                <Checkbox value="5" >周五</Checkbox>
                                <Checkbox value="6" >周六</Checkbox>
                                <Checkbox value="7" >周日</Checkbox>
                            </Checkbox.Group>
                        )}
                    </Form.Item>
                    <Form.Item label="预订渠道" style={{ width: '340px' }}>
                        {formProps.channelTypes(
                            <Checkbox.Group style={{ width: '220' }} >
                                <Checkbox value="1" checked>公司资源</Checkbox>
                                <Checkbox value="0">渠道资源</Checkbox>
                            </Checkbox.Group>
                        )}
                    </Form.Item>
                    <Form.Item label="在售状态">
                        {formProps.statusTypes(
                            <Checkbox.Group style={{ width: '220' }} >
                                <Checkbox value="0">在售</Checkbox>
                                <Checkbox value="1">停售</Checkbox>
                            </Checkbox.Group>
                        )}
                    </Form.Item>
                    <div style={{ display: 'block', textAlign: 'right' }}>
                        <Form.Item>
                            {formProps.isExpiry(
                                <Checkbox value="1">查询全部产品</Checkbox>
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
                            <Button onClick={this.export} style={{ marginLeft: 10 }}>导出</Button>
                        </Form.Item>
                    </div>
                </Form>
            </div >
        );
    }
}

export default AccomFilter;