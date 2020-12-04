import React, { Component, Fragment } from 'react';
import { message, Collapse, Form, Badge, Divider, Select, Input, Button, Tabs, span, InputNumber, Icon, Checkbox, DatePicker, Tag, Modal, Radio } from 'antd';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as actions from '../../../store/goods/action';
const Option = Select.Option;

const { CheckableTag } = Tag;
@connect(
    ({ operation, goods }) => ({
        operation,
        goods: goods.toJS()
    }),
    dispatch => ({
        actions: bindActionCreators(actions, dispatch)
    })
)
@Form.create()
class GroupInfo extends Component {
    constructor(props) {
        super(props);
        this.state = {
            goodsId: null,
            display_moths: 'block',
            display_day: 'none',
            // block弹框
            isShowAddModal: false,
            isShowEditModal: false,
            checked: false,
            giftTypeList: [],
            serviceTypeList: [],
            groupDetail: {},
            isShow: true,
            cycleShow: false,
            shopTypeList: [],
            strType: null,
        };
    }
    componentWillReceiveProps(nextProps) {
    }
    componentDidMount() {
        const { groupDetail, shopTypeList } = this.props;
        this.setState({
            shopTypeList: shopTypeList,
            groupDetail: groupDetail
        }, () => {
            if (groupDetail.shopType) {
                this.chooseShopType(groupDetail.shopType)
                this.isShow(groupDetail.useLimitId)
                this.useType(groupDetail.useType)
            }
        })
    }

    resetInfo = (giftTypeList, serviceTypeList, groupDetail) => {
        giftTypeList.map(item => {
            item.checked = false;
            return item;
        });
        if (groupDetail && groupDetail.gift) {
            giftTypeList.map(item => {
                groupDetail.gift.map(gift => {
                    if (gift === item.code) {
                        item.checked = true;
                        return item;
                    }
                });
                return item;
            });
        }
        serviceTypeList.map(item => {

            item.checked = false;
            return item;
        });
        if (groupDetail && groupDetail.service) {
            serviceTypeList.map(item => {
                groupDetail.service.map(ser => {
                    if (ser === item.code) {
                        item.checked = true;
                        return item;
                    }
                });
                return item;
            });
        }
        this.setState({
            giftTypeList: giftTypeList,
            serviceTypeList: serviceTypeList,
            groupDetail: groupDetail
        });
    }

    // 保存基本信息
    handleSubmit = e => {
        e.preventDefault();
        const { onEvent, data } = this.props;
        const { giftTypeList, serviceTypeList, groupDetail } = this.state;
        let gift = [];
        let service = [];
        this.props.form.validateFields((err, values) => {
            giftTypeList.map(item => {
                if (item.checked === true) {
                    gift.push(item.code);
                }
                return item;
            });
            serviceTypeList.map(item => {
                if (item.checked === true) {
                    service.push(item.code);
                }
                return item;
            });
            values.gift = gift;
            values.service = service;
            // if(gift.length === 0){
            //    message.error("请选择资源类型！"); 
            //    return;
            // }
            // if(service.length === 0){
            //     message.error("请选择权益类型！"); 
            //     return ;
            //  }
            if (values.useLimitId !== 'none') {
                if (values.useNum === null) {
                    message.error("请填写次数或者点数！");
                    return;
                }
            }
            if (!err) {
                if (groupDetail && groupDetail.id) {
                    onEvent('productGroupUpdate', values);
                } else {
                    onEvent('productGroupSave', values);
                }
                this.props.form.resetFields();
                //this.props.actions.productGroupSave(values);
            }
        });
    }

    handleChange = (params) => {
        const { giftTypeList, groupDetail } = this.state;
        const { productGroupList } = this.props;
        let isExist = false;
        productGroupList.map(item => {
            if (item.id === groupDetail.id && item.groupProductList) {
                item.groupProductList.map(pro => {
                    if (pro.gift === params.shortName) {
                        isExist = true;
                        return;
                    }
                });
            }
        });
        if (isExist) {
            message.error("当前产品组的权益类型包含产品，请先移除产品后，再移除当前权益！");
        } else {
            giftTypeList.map(item => {
                if (item.code === params.code) {
                    item.checked = params.checked === false ? true : false;
                }
                return item;
            });
            this.setState({
                giftTypeList: giftTypeList,
            });
        }
    }

    chooseShopType = (params) => {
        let detail = this.state.groupDetail;
        this.props.actions.selectListByShopType(params).then((data) => {
            const { goods } = this.props;
            if (goods.selectListByShopTypeRes.code == 100) {
                let serviceList = goods.selectListByShopTypeRes.result;
                serviceList.map(item => {
                    item.checked = false;
                    return item;
                });
                if (detail && detail.service) {
                    serviceList.map(item => {
                        detail.service.map(ser => {
                            if (ser === item.code) {
                                item.checked = true;
                                return item;
                            }
                        });
                        return item;
                    });
                }
                this.setState({
                    serviceTypeList: serviceList
                })
            } else {
                message.error("系统错误")
            }
        }).catch((error) => {
            message.error("系统错误")
        });

        this.props.actions.selectGiftByShopType(params).then((data) => {
            const { goods } = this.props;
            if (goods.selectGiftByShopTypeRes.code == 100) {
                let giftList = goods.selectGiftByShopTypeRes.result;
                giftList.map(item => {
                    item.checked = false;
                    return item;
                });
                if (detail && detail.gift) {
                    giftList.map(item => {
                        detail.gift.map(gift => {
                            if (gift === item.code) {
                                item.checked = true;
                                return item;
                            }
                        });
                        return item;
                    });
                }
                this.setState({
                    giftTypeList: giftList
                })
            } else {
                message.error("系统错误")
            }
        }).catch((error) => {
            message.error("系统错误")
        });
    }

    // 重置
    reset = () => {
        this.props.form.resetFields();
        this.props.form.resetFields(`name`, []);
    }
    serviceHandleChange = (params) => {
        const { serviceTypeList, groupDetail } = this.state;
        const { productGroupList } = this.props;
        let isExist = false;
        productGroupList.map(item => {
            if (item.id === groupDetail.id && item.groupProductList) {
                item.groupProductList.map(pro => {
                    if (pro.service === params.name) {
                        isExist = true;
                        return;
                    }
                });
            }
        });
        if (isExist) {
            message.error("当前产品组的资源类型包含产品，请先移除产品后，再移除当前资源！");
        } else {
            serviceTypeList.map(item => {
                if (item.code === params.code) {
                    item.checked = params.checked === false ? true : false;
                }

                return item;
            });
            this.setState({
                serviceTypeList: serviceTypeList,
            });
        }


    }
    isShow = value => {
        const { groupDetail } = this.state;
        groupDetail.useLimitId = value;
        if (value === 'none') {
            this.setState({
                isShow: false,
                cycleShow: false,
                groupDetail: groupDetail,
            });
        } else if (value === 'cycle_repeat') {
            this.setState({
                isShow: false,
                cycleShow: true,
                groupDetail: groupDetail,
            })
        } else {
            this.setState({
                isShow: true,
                cycleShow: false,
                groupDetail: groupDetail,
            });
        }
    }
    useType = value => {
        if (value == '0') {
            this.setState({
                strType: '次'
            })
        } else if (value == '1') {
            this.setState({
                strType: '点'
            })
        }
    }
    render() {
        const { onEvent, goodsId, sysDict = [], } = this.props;
        const { getFieldDecorator } = this.props.form;
        const { giftTypeList, serviceTypeList, groupDetail, isShow, shopTypeList, cycleShow, strType } = this.state;
        return (
            <Modal
                title="修改产品组"
                visible={true} onCancel={() => onEvent('displayDiv')}
                footer={null} width={'40%'}
            >
                <Form onSubmit={this.handleSubmit}>
                    <Form.Item label="产品组名称：" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }}>
                        {getFieldDecorator('name', { rules: [{ required: true, message: '请输入增产品组名称!' }], initialValue: groupDetail.name })(
                            <Input maxLength={20} />
                        )}
                        {getFieldDecorator('goodsId', { initialValue: goodsId })(
                            <Input style={{ display: 'none' }} />

                        )}
                        {getFieldDecorator('id', { initialValue: groupDetail.id })(
                            <Input style={{ display: 'none' }} />

                        )}
                        <span style={{ fontSize: '12px', marginTop: '-10px' }} >注：产品组名称为前端显示的名称，请控制在20个字符以内</span>
                    </Form.Item>
                    <Form.Item style={{ marginTop: '20px' }} label="内部简称：" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }}>
                        {getFieldDecorator('shortName', {
                            rules: [{ required: true, message: '请输入内部简称' }], initialValue: groupDetail.shortName
                        })(<Input />
                        )}
                        <span style={{ fontSize: '12px', marginTop: '-10px' }} >注：内部简称为方便内部人员理解的名称，无字数限制</span>
                    </Form.Item>
                    <Form.Item style={{ marginTop: '20px' }} label="商户类型" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }}>
                        {getFieldDecorator('shopType', { initialValue: groupDetail.shopType })(
                            <Radio.Group buttonStyle="solid">
                                {shopTypeList && shopTypeList.map(item => (
                                    <Radio.Button value={item.code} onClick={() => this.chooseShopType(item.code)}>{item.name}</Radio.Button>
                                ))}
                            </Radio.Group>
                        )}
                    </Form.Item>
                    <Form.Item style={{ marginTop: '20px' }} label="资源类型" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }}>
                        {getFieldDecorator('service', {})
                            (<div>
                                {serviceTypeList.map(item => (

                                    <CheckableTag {...this.props} style={{ border: '1px solid #d9d9d9' }} checked={item.checked} onChange={() => this.serviceHandleChange(item)} >{item.name}</CheckableTag>
                                ))}


                            </div>
                            )}
                    </Form.Item>
                    <Form.Item style={{ marginTop: '20px' }} label="权益类型" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }}>

                        {getFieldDecorator('gift', {})
                            (<div>
                                {giftTypeList.map(item => (

                                    <CheckableTag {...this.props} style={{ border: '1px solid #d9d9d9' }} checked={item.checked} onChange={() => this.handleChange(item)} >{item.shortName}</CheckableTag>
                                ))}


                            </div>
                            )}
                    </Form.Item>


                    <Form.Item verticalGap={1} style={{ marginTop: '20px' }} label="使用限制" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal" indicatorGap >
                        {getFieldDecorator('useLimitId', { rules: [{ required: true, message: '请选择使用限制!' }], initialValue: groupDetail.useLimitId ? groupDetail.useLimitId : null })
                            (<Select style={{ width: '30%' }} onChange={this.isShow} showSearch
                                filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                                {
                                    sysDict.map(item => (
                                        <Option key={item.value} value={item.value}>{item.label}</Option>
                                    ))
                                }
                            </Select>
                            )} &nbsp;&nbsp; &nbsp;&nbsp;
                       {isShow && <span className="ant-form-text" style={{ marginLeft: '100px;' }}>{sysDict.map(item => {
                                if (item.value === groupDetail.useLimitId) {
                                    return item.label + ":";
                                }
                            })} </span>}
                        {isShow && getFieldDecorator('useNum', { initialValue: groupDetail.useNum })(<InputNumber step={1} min={0}
                            formatter={value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')}
                            parser={value => value.replace(/\s?|(,*)/g, '')} />
                        )}

                        {cycleShow &&
                            <span>
                                <span>总共 </span>
                                {getFieldDecorator('useNum', { initialValue: groupDetail.useNum })(
                                    <InputNumber style={{ width: '30%' }} step={1} min={0}
                                        formatter={value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')}
                                        parser={value => value.replace(/\s?|(,*)/g, '')} />)}
                                {getFieldDecorator('useType', { initialValue: groupDetail.useType ? groupDetail.useType + '' : '0' })(
                                    <Select style={{ width: '25%' }} onChange={this.useType}>
                                        <Option value='0'>次</Option>
                                        <Option value='1'>点</Option>
                                    </Select>)}
                                <span> 每 </span>
                                {getFieldDecorator('cycleTime', { initialValue: groupDetail.cycleTime })(
                                    <InputNumber style={{ width: '30%' }} step={1} min={0}
                                        formatter={value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')}
                                        parser={value => value.replace(/\s?|(,*)/g, '')} />)}
                                {getFieldDecorator('cycleType', { initialValue: groupDetail.cycleType ? groupDetail.cycleType + '' : '2' })(
                                    <Select style={{ width: '25%' }}>
                                        <Option value='0'>天</Option>
                                        <Option value='1'>周</Option>
                                        <Option value='2'>个月</Option>
                                        <Option value='3'>年</Option>
                                    </Select>)}
                                {getFieldDecorator('cycleNum', { initialValue: groupDetail.cycleNum })(
                                    <InputNumber style={{ width: '30%' }} step={1} min={0}
                                        formatter={value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')}
                                        parser={value => value.replace(/\s?|(,*)/g, '')} />
                                )}
                                <span> {strType}</span>
                            </span>
                        }

                    </Form.Item>
                    <Form.Item style={{ marginTop: '20px' }} label="免费次数:" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }}>
                        {getFieldDecorator('freeCount', { initialValue: groupDetail.freeCount })(
                            <InputNumber min={0}></InputNumber>
                        )}
                    </Form.Item>
                    <Form.Item style={{ marginTop: '20px' }} label="产品使用率:" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }}>
                        {getFieldDecorator('useRate', { initialValue: groupDetail.useRate })(
                            <InputNumber min={0}></InputNumber>
                        )}
                    </Form.Item>
                    <Form.Item style={{ marginTop: '20px' }} label="折扣比例：" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} >
                        {getFieldDecorator('discountRate', { initialValue: groupDetail.discountRate ? groupDetail.discountRate * 100 : 0 })(<InputNumber step={1} min={0}
                        // formatter={value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')}
                        // parser={value => value.replace(/\s?|(,*)/g, '')}
                        />
                        )}
                        <span className="ant-form-text"> %</span>

                    </Form.Item>

                    <Form.Item label="预约限制" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} >
                        <span >最少提前</span>&nbsp;&nbsp;
                        {getFieldDecorator('minBookDays', { initialValue: groupDetail.minBookDays })(<InputNumber step={1} min={0}
                            formatter={value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')}
                            parser={value => value.replace(/\s?|(,*)/g, '')} />)} &nbsp;&nbsp;天
                        <span style={{ marginLeft: '20px' }}>最多提前</span>&nbsp;&nbsp;
                        {getFieldDecorator('maxBookDays', { initialValue: groupDetail.maxBookDays })(<InputNumber step={1} min={0}
                                formatter={value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')}
                                parser={value => value.replace(/\s?|(,*)/g, '')} />)} &nbsp;&nbsp;天
                        <span style={{ color: 'grey', marginLeft: '40px', fontSize: '12px', }}>不填表示没有限制</span>

                        <br></br>
                        <div style={{ padding: '3% 0 4%' }}>
                            <Button type="primary" htmlType="submit" >保存</Button> &nbsp;&nbsp;&nbsp;&nbsp;
                     {/* <Button type="primary" onClick={this.reset  } >重置</Button>&nbsp;&nbsp;&nbsp;&nbsp; */}
                            <Button onClick={() => onEvent('displayDiv')}   >取消</Button>

                        </div>
                    </Form.Item>
                </Form>
            </Modal>

        );
    }
}
export default GroupInfo;