import React, { Component } from 'react';
import { Form, Input, Button, Select, Row, Col } from 'antd';

const { Option } = Select;
@Form.create()
class GoodsGroupFilter extends Component {
    constructor(props) {
        super(props);
        this.state = { 
           goodsId:null,
           itemProduct:{} ,
        };
    }
    componentDidMount() {
        const { itemProduct } = this.props;
        this.setState({
            itemProduct:itemProduct,
        });
    }
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
    
    render() {
        const { getFieldDecorator } = this.props.form;
        const { key,cityList,goodsId,onEvent} = this.props;
        const {itemProduct} = this.state;
        return (
            <div className="c-filter" key ={key}>
                <Form layout="inline" onSubmit={this.handleSubmit}>
                <Row gutter={24}>
                <Col span={10} style={{}}>
                <Form.Item>
                        
                        {getFieldDecorator('shopIdOrShopName', {})(
                            <Input placeholder="搜索产品组ID/名称/内部简称"  style={{width:'100%'}}/>
                        )}
                           {getFieldDecorator('goodsId', {initialValue: goodsId})(
                            <Input  style={{ display:'none' }} /> 
                                
                        )}
                           {getFieldDecorator('groupId', {initialValue: itemProduct.id})(
                            <Input  style={{ display:'none' }} /> 
                                
                        )}
                    </Form.Item>
                </Col>
                <Col span={10} style={{  }}>
                    <Form.Item label="城市"> 
                    
                    {getFieldDecorator('city', {initialValue:""})(
                        <Select style={{ width: 200 }} showSearch
                        filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                        <Option  value="" Selected>全部</Option>
                        {
                            cityList.map(item => (
                                    <Option key={'city'+item.id} value={item.id}>{item.nameCh}</Option>
                                ))
                           }
                        </Select>
                    )}
                    </Form.Item>
                    
                </Col>
                <Col span={4} style={{  }}>
                            <Button type="primary" htmlType="submit"     > 查询  </Button>
                            <Button onClick={this.reset} style={{ marginLeft: 10 }}     > 重置     </Button>
                </Col>
                </Row>
                <Row gutter={24}>
                <Col span={10} style={{}}>
                        <Button type="primary" onClick={() => onEvent('addProduct',itemProduct)} > + 添加产品  </Button>
                        <Button  style={{ marginLeft: 10 }} onClick={() => onEvent('delProduct',itemProduct)}    > 移除产品    </Button>
                        <Button  style={{ marginLeft: 10 }}  onClick={() => onEvent('editProduct',itemProduct)}   > 编辑产品    </Button>
                </Col>
                <Col span={10} style={{  }}>
                    
                    
                </Col>
                <Col span={4} style={{  }}>
                       
                </Col>
                </Row>
                </Form>
            </div>
        );
    }
}

export default GoodsGroupFilter;