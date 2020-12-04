import React, { Fragment, Component } from 'react';
import { connect } from 'react-redux';
import { withRouter, Link } from 'react-router';
import moment from 'moment'
import { bindActionCreators } from 'redux';
import * as actions from '../../store/alipaySales/action';
import './index.less';
import _ from 'lodash';
import { Form, message, Select, Input, Button, Tabs, Tag, Card, LocaleProvider, Spin, Divider, Popconfirm } from 'antd';
import zh_CN from 'antd/lib/locale-provider/zh_CN';

@connect(
    ({ operation, alipaySales }) => ({
        operation,
        alipaySales: alipaySales.toJS()
    }),
    dispatch => ({
        actions: bindActionCreators({ ...actions }, dispatch)
    })
)
@withRouter
@Form.create()
class AlipaySalesOrder extends Component {
    constructor(props) {
        super(props);
        this.state = {
            checkSalesOrderRefundRes: {},
            querySalesOrderRes: {},
        };
    }
    componentDidMount() {

    }
    componentWillReceiveProps(nextProps) {
        const { operation, alipaySales } = nextProps;
        switch (operation.type) {
            // case actions.GET_QUERYSALESORDER_SUCCESS:
            //     break;
            default:
                break;
        }
    }
    // onEvent = (type, params) => {
    //     switch (type) {
    //         case 'getShopDetail':
    //             this.props.actions.getShopDetail(params);
    //             break;
    //         default:
    //             break;
    //     }
    // }

    // 表单提交
    handleSubmit = e => {
        e.preventDefault();
        this.queryOrder();
    }

    /**
     * 查询订单信息
     */
    queryOrder() {
        this.props.form.validateFields((err, values) => {
            if (!err) {
                this.props.actions.querySalesOrder(values).then((data) => {
                    const { alipaySales } = this.props;
                    if (alipaySales.querySalesOrderRes.code == 100 && alipaySales.querySalesOrderRes.result != null) {
                        this.setState({
                            querySalesOrderRes: alipaySales.querySalesOrderRes.result,
                        })
                        let param = {
                            salesOrderId: alipaySales.querySalesOrderRes.result.id,
                        }
                        this.props.actions.checkSalesOrderRefund(param).then((data) => {
                            const { alipaySales } = this.props;
                            if (alipaySales.checkSalesOrderRefundRes.code == 100 && alipaySales.checkSalesOrderRefundRes.result != null) {
                                this.setState({
                                    checkSalesOrderRefundRes: alipaySales.checkSalesOrderRefundRes.result,
                                })
                            } else {
                                message.error(alipaySales.checkSalesOrderRefundRes.msg);
                                this.reEmpty();
                            }
                        }).catch((error) => {
                            message.error("系统错误");
                            this.reEmpty();
                        })
                    } else {
                        message.error(alipaySales.querySalesOrderRes.msg);
                        this.reEmpty();
                    }
                }).catch((error) => {
                    message.error("系统错误");
                    this.reEmpty();
                });
            } else {
                this.reEmpty();
            }
        });
    }

    /**
     * 退款
     */
    refundOrder = (salesOrderId) => {
        let param = {
            id: salesOrderId,
        }
        this.props.actions.refundSaleOrder(param).then((data) => {
            const { alipaySales } = this.props;
            if (alipaySales.refundSaleOrderRes.code == 100 && alipaySales.refundSaleOrderRes.result != null) {
                message.info(alipaySales.refundSaleOrderRes.result)
                this.queryOrder();
            } else {
                message.error(alipaySales.refundSaleOrderRes.msg);
            }
        }).catch((error) => {
            message.error("系统错误")
        })
    }

    /**
     * 不操作
     */
    cancel(e) {
    }

    /**
     * 数据置空
     */
    reEmpty() {
        this.setState({
            checkSalesOrderRefundRes: null,
            querySalesOrderRes: null,
        })
    }

    render() {
        const { checkSalesOrderRefundRes, querySalesOrderRes } = this.state;
        const { getFieldDecorator } = this.props.form;
        return (
            <Fragment><LocaleProvider locale={zh_CN} >
                <div className="c-modal">
                    <Form onSubmit={this.handleSubmit}>
                        <Form.Item verticalGap={1} label="订单信息查询" labelCol={{ span: 3 }} wrapperCol={{ span: 10 }} direction="horizontal" indicatorGap >
                            {getFieldDecorator('codeOrOrder', { rules: [{ required: true, message: '请输入激活码或者订单号!' }], initialValue: null })(
                                <Input style={{ width: "80%" }} placeholder="输入激活码或者订单号" />
                            )}
                            <Button type="primary" icon="search" htmlType="submit">查询</Button>
                        </Form.Item>
                    </Form>
                    {!_.isEmpty(querySalesOrderRes) && <div className="c-base-root">
                        <Divider>订单信息</Divider>
                        <div className="c-content">
                            <p>订单号：{querySalesOrderRes.id}</p>
                            <p>商品名称：{querySalesOrderRes.projectId + " # " + querySalesOrderRes.subject}</p>
                            <p>激活码：{querySalesOrderRes.activateCode}</p>
                            <p>激活码状态：{querySalesOrderRes.actCodeStatusStr}</p>
                            <p>激活码到期日：{moment(querySalesOrderRes.expiryDate).format('YYYY-MM-DD')}</p>
                            <p>订单支付金额：{querySalesOrderRes.realAmount}</p>
                            <p>订单日期：{moment(querySalesOrderRes.createTime).format('YYYY-MM-DD hh:mm:ss')}</p>
                            <p>订单状态：{querySalesOrderRes.statusStr}</p>
                        </div>
                        <Divider>查询结果</Divider>
                        <div className="c-result">
                            {checkSalesOrderRefundRes && checkSalesOrderRefundRes.canRefund && checkSalesOrderRefundRes.canOnline &&
                                <dev>
                                    <p>符合线上退款条件，点击“退款”按钮，系统自动退款，请勿重复操作或线下退款~</p>
                                    <Popconfirm title={"谨慎操作，确认退款吗？ 订单号：" + querySalesOrderRes.id} onConfirm={() => this.refundOrder(querySalesOrderRes.id)} onCancel={this.cancel} okText="是" cancelText="否">
                                        <Button type="primary">退款</Button>
                                    </Popconfirm>
                                    <p className="p-red">谨慎操作！</p>
                                </dev>
                            }
                            {checkSalesOrderRefundRes && !checkSalesOrderRefundRes.canRefund &&
                                <p>{checkSalesOrderRefundRes.notRefundReason}</p>
                            }
                            {checkSalesOrderRefundRes && checkSalesOrderRefundRes.canRefund && !checkSalesOrderRefundRes.canOnline &&
                                <p>{checkSalesOrderRefundRes.notOnlineReason}</p>
                            }
                        </div>
                    </div>}
                </div>
            </LocaleProvider></Fragment>

        );
    }
}

export default AlipaySalesOrder;