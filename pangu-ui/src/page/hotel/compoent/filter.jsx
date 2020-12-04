import React, { Component } from 'react';
import { Form, Input, Button,Select} from 'antd';
const { Option } = Select;
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
    // 重置
    reset = () => {
        this.props.form.resetFields();
    }
    // 表单绑定
    getForm = () => {
        const { getFieldDecorator } = this.props.form;
        let formProps = {};
        formProps.hotelName = getFieldDecorator('hotelName', {
            // initialValue: '酒店'
        });
        formProps.city = getFieldDecorator('cityName', {
            // initialValue: '上海'
        });
        return formProps;
    }
    render() {
        const formProps = this.getForm();
        const {  onEvent, cityList} = this.props;
        return (
            <div className="c-filter">
                <Form layout="inline" onSubmit={this.handleSubmit}>
                    <Form.Item
                        label="酒店名称："
                    >
                        {formProps.hotelName(
                            <Input placeholder="请输入酒店名称" />
                        )}
                    </Form.Item>
                    <Form.Item
                        label="城市："
                    >
                        {formProps.city(
                              <Select style={{ width: 210 }} mode='multiple' showSearch
                              filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                              <Option  value="">全部</Option>
                              {
                                  cityList.map(item => {
                                     return  <Option key={'city2'+item.nameCh} value={item.nameCh}>{item.nameCh}</Option>;
                                  })
                              }
                              </Select>
                        )}
                    </Form.Item>
                    <Form.Item>
                        <Button
                            type="primary"
                            htmlType="submit"
                        >
                            查询
                        </Button>
                        <Button onClick={this.reset} style={{marginLeft: 10}}
                        >
                            重置
                        </Button>
                    </Form.Item>
                </Form>
            </div>
        );
    }
}

export default Filter;