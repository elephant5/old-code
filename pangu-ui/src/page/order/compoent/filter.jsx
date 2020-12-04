import React, { Component } from 'react';
import { Form, Input, Button, Radio, Select, LocaleProvider, Row, Col, Checkbox, Cascader, DatePicker } from 'antd';
import zh_CN from 'antd/lib/locale-provider/zh_CN';
import cookie from 'react-cookies';
import { BASE_PANGU_URL } from '../../../util/url';
const { Option } = Select;
const widthTemp = { width: 270 };
const { MonthPicker, RangePicker, WeekPicker } = DatePicker;

@Form.create()
class Filter extends Component {
    constructor(props) {
        super(props);
        this.lastFetchId = 0;
        this.state = {
            shopListTemp: [],
            goodsList:[],
        };
    }
    componentDidMount() {

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
    componentWillReceiveProps(nextProps) {
        // const { shopList ,allGoodsList} = nextProps;
        // if (shopList ) {
            
        //     this.setState({ shopListTemp: shopList});
        // }
        // if (allGoodsList ) {
            
        //     this.setState({ goodsList: allGoodsList});
        // }

    }
    // 重置
    reset = () => {
        this.props.form.resetFields();
    }


    downLoad = e => {
        e.preventDefault();
        const { onEvent } = this.props;
        this.props.form.validateFields((err, values) => {
            if (!err) {
                onEvent('exportOrder', {
                    ...values
                })
            }
        });
    }

    onSeachShop = value => {
        var pattern = new RegExp("[\u4E00-\u9FA5]+");
        value   =   value.replace(/\s+/g,"");   
        if (value.length === 0 ) {
            return ;
        }
        this.setState({ shopListTemp:[]});
        let params = {};
        params.shopName = value;
        this.lastFetchId += 1;
        const fetchId = this.lastFetchId;
        fetch(BASE_PANGU_URL + '/shop/seachShopListByName',
        {
          method: 'post',
          body: JSON.stringify(params),
          //请求头
          headers: {
            'content-type': 'application/json'
          }
        }

      )
        .then(response => response.json())
        .then((body) => {
        //   if (fetchId !== this.lastFetchId) { // for fetch callback order
        //     return;
        //   }
          if (body.result && body.result.length > 0) {

            
            setTimeout(()=>{
                this.setState({ shopListTemp: body.result, fetching: false });
                },
            300)
          } else {

            // this.setState({ groupProductList: GoodsGroupListRes[0].groupProductList, fetching: false });
          }

        });
    }

    onSeachGoods = value => {
        var pattern = new RegExp("[\u4E00-\u9FA5]+");
        value   =   value.replace(/\s+/g,"");   
        if (value.length === 0 ) {
            return ;
        }
        let params = {};
        params.name = value;
        this.lastFetchId += 1;
       
        fetch(BASE_PANGU_URL + '/goods/seachGoodsListByName',
        {
          method: 'post',
          body: JSON.stringify(params),
          //请求头
          headers: {
            'content-type': 'application/json'
          }
        })
        .then(response => response.json())
        .then((body) => {
        //   if (fetchId !== this.lastFetchId) { // for fetch callback order
        //     return;
        //   }
          if (body.result && body.result.length > 0) {
            
            this.setState({ goodsList: body.result, fetching: false });
          } else {

            // this.setState({ groupProductList: GoodsGroupListRes[0].groupProductList, fetching: false });
          }

        });
    }
    // andList =e =>{
    //     e.preventDefault();
    //     const { onEvent } = this.props;
    //     this.props.form.validateFields((err, values) => {
    //         if (!err) {
    //             values.typeList='and'
    //             onEvent('search', {
    //                 ...values
    //             })
    //         }
    //     });
    // }
    filterShop = e => {
        console.log(e);
        const { shopList, } = this.props;
        if (e) {
            let temp = shopList.map(item => {
                console.log(item.hotelName + "-" + item.id + item.shopName)
                if (item.hotelName && item.hotelName.indexOf(e) >= 0) {
                    return item;
                } else {
                    if (item.shopName.indexOf(e) >= 0) {
                        return item;
                    }
                }
            });
            this.setState({ shopListTemp: temp });
        }

    }
    render() {
        const { getFieldDecorator } = this.props.form;
        const { shopListTemp,goodsList } = this.state;
        const { resourceType, giftTypeList, channelList, shopList, reservChannelList, bankTypeList, actCodeTags, reservOrderTags,downLoadLoading ,expressTypeList} = this.props;
        const download = cookie.load("KLF_PG_OL_DOWNLOAD");
        return (<LocaleProvider locale={zh_CN} >
            <div className="c-filter">
                <Form layout="inline" onSubmit={this.handleSubmit}>

                    <Row type="flex">
                        <Col span={6} order={1}>
                            <Form.Item label="订单类型" style={{ width: "100%" }}>
                                {getFieldDecorator('serviceTypes')(
                                    <Select style={widthTemp} mode='multiple' showSearch placeholder="请选择..."
                                        filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                                        {
                                            resourceType.map(item => (
                                                <Option key={item.id} value={item.code}>{item.name}</Option>
                                            ))
                                        }
                                    </Select>

                                )}
                            </Form.Item>
                        </Col>
                        <Col span={6} order={2}>
                            <Form.Item label="权益类型" >
                                {getFieldDecorator('resourceTypes')(
                                    <Select style={widthTemp} mode='multiple' showSearch placeholder="请选择..."
                                        filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                                        {
                                            giftTypeList.map(item => (
                                                <Option key={item.id} value={item.code}>{item.shortName}</Option>
                                            ))
                                        }

                                    </Select>

                                )}
                            </Form.Item>
                        </Col>
                        <Col span={6} order={3}>
                            <Form.Item label="销售渠道" >
                                {getFieldDecorator('channel')(
                                    <Cascader style={widthTemp} options={channelList} placeholder="请选择..." />

                                )}
                            </Form.Item>
                        </Col>
                        <Col span={6} order={3}>
                            <Form.Item label="预定渠道" >
                                {getFieldDecorator('reservChannel')(
                                    <Select style={widthTemp} mode='multiple' showSearch placeholder="请选择..."
                                        filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                                        {
                                            reservChannelList.map(item => (
                                                <Option key={item.id} value={item.id}>{item.name}</Option>
                                            ))
                                        }

                                    </Select>

                                )}
                            </Form.Item>
                        </Col>
                    </Row>
                    <Row type="flex">
                        <Col span={6} order={1}>
                            <Form.Item label="隶属商品" >
                                {getFieldDecorator('goods')(
                                    <Select style={widthTemp} mode='multiple' showSearch placeholder="搜索商品或者ID搜索"  onSearch={this.onSeachGoods}
                                        filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                                        {goodsList && goodsList.length> 0 && 
                                            goodsList.map(item => (
                                                <Option key={item.id} value={item.id}>{'#' + item.id + '  ' + item.shortName}</Option>
                                            ))
                                        }
                                    </Select>
                                )}
                            </Form.Item>
                        </Col>
                        <Col span={6} order={2}>
                            <Form.Item label="选择商户" >
                                {getFieldDecorator('shopIds')(
                                    <Select style={widthTemp} showSearch placeholder="输入商户名或者ID搜索" onSearch={this.onSeachShop}
                                    notFoundContent={null} filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}
                                        >
                                        {shopListTemp && shopListTemp.length > 0 &&
                                            shopListTemp.map(item => (
                                                <Option key={item.id} value={item.id}>{item.hotelNameAndShopName}</Option>
                                            )) }
                                    </Select>
                                )}
                            </Form.Item>
                        </Col>
                        <Col span={6} order={3}>
                            <Form.Item label="关联人识别" >
                                {getFieldDecorator('params')(
                                    <Input style={widthTemp} placeholder="请输入预约人|手机号|使用人|使用人手机号"></Input>

                                )}
                            </Form.Item>
                        </Col>
                        <Col span={6} order={4}>
                            <Form.Item label="订单号" >
                                {getFieldDecorator('reservOrderId')(
                                    <Input style={widthTemp} placeholder="请输入预约单号"></Input>

                                )}
                            </Form.Item>
                        </Col>

                    </Row>
                    <Row type="flex">
                        <Col span={6} order={1}>
                            <Form.Item label="核销码" >
                                {getFieldDecorator('varCode')(
                                    <Input style={widthTemp} placeholder="请输入核销码"></Input>

                                )}
                            </Form.Item>
                        </Col>
                        <Col span={6} order={2}>
                        <Form.Item label="激活码" >
                                {getFieldDecorator('actCode')(
                                    <Input style={widthTemp} placeholder="请输入激活码"></Input>

                                )}
                            </Form.Item>
                        </Col>
                        <Col span={6} order={2}>
                        <Form.Item label="酒店确认号" >
                                {getFieldDecorator('reservNumber')(
                                    <Input style={widthTemp} placeholder="请输入酒店确认号"></Input>

                                )}
                            </Form.Item>
                        </Col>
                        <Col span={6} order={2}>
                            <Form.Item label="大客户" >
                                {getFieldDecorator('bankTypeId')(
                                    <Select style={widthTemp}  showSearch placeholder="请选择..."
                                        filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                                        {
                                            bankTypeList.map(item => (
                                                <Option key={item.value} value={item.value}>{item.label}</Option>
                                            ))
                                        }
                                    </Select>
                                )}
                            </Form.Item>
                        </Col>
                    </Row>
                    <Row type="flex">
                        <Col span={6} order={1}>
                            <Form.Item label="快递公司" >
                                {getFieldDecorator('expressNameIds')(
                                    <Select style={widthTemp}   mode='multiple' showSearch placeholder="请选择..."
                                    filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                                    {
                                        expressTypeList.map(item => (
                                            <Option key={item.value} value={item.value}>{item.label}</Option>
                                        ))
                                    }
                                </Select>
                                )}
                            </Form.Item>
                        </Col>
                        <Col span={6} order={2}>
                            <Form.Item label="快递单号" >
                                {getFieldDecorator('expressNumbers')(
                                    <Input style={widthTemp} placeholder="多个快递单用，隔开"></Input>
                                )}
                            </Form.Item>
                        </Col>
                        <Col span={6} order={3}>
                            <Form.Item label="快递状态" >
                                {getFieldDecorator('expressStatus',{initialValue: ""})(
                                    <Select  style={ widthTemp }>
                                        <Option  value="">全部</Option>
                                        <Option key={0}>未发货</Option>
                                        <Option key={1}>已发货</Option>
                                        <Option key={2}>已收货</Option>
                                        <Option key={3}>已退货</Option>
                                    </Select>
                                )}
                                
                            </Form.Item>
                        </Col>
                    </Row>
                    <Row type="flex">
                        <Col span={24} order={1}>
                            <Form.Item label="标签筛选" >
                                {getFieldDecorator('tagsList')(
                                    <Checkbox.Group style={{ width: '100%' }} >
                                        {actCodeTags && actCodeTags.map(item => {
                                            return (<Checkbox value={item.value}>{item.value}</Checkbox>);
                                        })}
                                        {reservOrderTags && reservOrderTags.map(item => {
                                            return (<Checkbox value={item.value}>{item.value}</Checkbox>);
                                        })}
                                    </Checkbox.Group>
                                )}
                                {getFieldDecorator('tagType', { initialValue: 'and' })(
                                    <Radio.Group>
                                        <Radio value={'and'}>交集查询</Radio>
                                        <Radio value={'or'}>合集查询</Radio>
                                    </Radio.Group>
                                )}
                                {/* <Button  onClick={this.andList} size={"small"}
                            type="default"
                            htmlType="buttom"
                        >
                            交集查询
                        </Button>
                        <Button style={{ marginLeft: 10 }} onClick={this.orList} 
                            type="default"size={"small"}
                            htmlType="buttom"
                        >
                            合集查询
                        </Button> */}
                            </Form.Item>
                        </Col>
                    </Row>
                    <Row type="flex">
                        <Col span={24} order={1}>
                            <Form.Item label="预定状态" >
                                {getFieldDecorator('reservStatus')(
                                    <Checkbox.Group style={{ width: '100%' }} >
                                        <Checkbox value="0">尚未预定</Checkbox>
                                        <Checkbox value="4">预定中</Checkbox>
                                        <Checkbox value="1">预定成功/兑换成功</Checkbox>
                                        <Checkbox value="2">预定取消</Checkbox>
                                        <Checkbox value="3">预定失败</Checkbox>
                                    </Checkbox.Group>
                                )}
                            </Form.Item>
                        </Col>
                    </Row>
                    <Row type="flex">
                        <Col span={24} order={1}>
                            <Form.Item label="使用状态" >
                                {getFieldDecorator('useStatus')(
                                    <Checkbox.Group style={{ width: '100%' }} >
                                        <Checkbox value="0">未使用</Checkbox>
                                        <Checkbox value="2">已使用</Checkbox>
                                        <Checkbox value="1">已过期</Checkbox>
                                        <Checkbox value="3">已作废</Checkbox>
                                    </Checkbox.Group>
                                )}
                            </Form.Item>
                        </Col>
                    </Row>

                    <Row type="flex">
                        <Col span={6} order={1}>
                            <Form.Item label="订单日期范围" >
                                {getFieldDecorator('orderDate')(
                                    <RangePicker style={widthTemp} />

                                )}
                            </Form.Item>
                        </Col>
                        <Col span={6} order={2}>
                            <Form.Item label="预约日期范围" >
                                {getFieldDecorator('reservDates')(

                                    <RangePicker style={widthTemp} />
                                )}
                            </Form.Item>
                        </Col>
                        <Col span={6} order={2}>
                            <Form.Item label="快递日期范围" >
                                {getFieldDecorator('expressDates')(

                                    <RangePicker style={widthTemp} />
                                )}
                            </Form.Item>
                        </Col>
                    </Row>
                    <Row type="flex">
                        <Col span={6} order={1}>
                            <Form.Item label="预约成功日期" >
                                {getFieldDecorator('successDates')(
                                    <RangePicker style={widthTemp} />

                                )}
                            </Form.Item>
                        </Col>
                        <Col span={6} order={2}>
                            <Form.Item label="预约取消日期" >
                                {getFieldDecorator('cancelDates')(
                                    <RangePicker style={widthTemp} />

                                )}
                            </Form.Item>
                        </Col>
                    </Row>
                    <Row type="flex">
                        <Col span={24} order={1} style={{ textAlign: 'center' }}>
                            <Form.Item >
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
                                {download && <Button onClick={this.downLoad} loading={downLoadLoading} style={{ marginLeft: 10 }}
                                >
                                    下载查询结果
                        </Button>}
                            </Form.Item>
                        </Col>


                    </Row>

                </Form>
            </div></LocaleProvider>
        );
    }
}

export default Filter;