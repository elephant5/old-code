import React, {Component, Fragment} from 'react';
import {connect} from 'react-redux';
import {withRouter} from 'react-router';
import {bindActionCreators} from 'redux';
import * as actions from '../../store/channel/action';
import * as comms from '../../store/common/action';
import BaseForm from "./compoent/form";
import * as dictTypes from "../../util/dictType";
import _ from 'lodash';

@connect(
    ({operation, channel, common}) => ({
        operation,
        channel: channel.toJS(),
        common: common.toJS()
    }),
    dispatch => ({
        actions: bindActionCreators(actions, dispatch),
        comms: bindActionCreators(comms, dispatch)
    })
)
@withRouter
class AddSalesChannel extends Component {
    constructor(props) {
        super(props);
        this.state = {
            bankType: [],
            salesChannelType: [],
            salesWayType: [],
            settlementMethodType: [],
            invoiceNodeType: [],
            invoiceObjType: [],
            salesChannel: {}
        };
    }

    componentDidMount() {
        var data = this.props.location.query;
        var {id} = data;
        if (!!id) {
            //根据id查找渠道信息
            this.props.actions.getSalesChannel(id);
        }
        //初始化字典数据
        this.props.comms.getDictList(dictTypes.BANK_TYPE);
        this.props.comms.getDictList(dictTypes.SALES_CHANNEL_TYPE);
        this.props.comms.getDictList(dictTypes.SALES_WAY_TYPE);
        this.props.comms.getDictList(dictTypes.SETTLEMENT_METHOD_TYPE);
        this.props.comms.getDictList(dictTypes.INVOICE_NODE_TYPE);
        this.props.comms.getDictList(dictTypes.INVOICE_OBJ_TYPE);
    }

    componentWillReceiveProps(nextProps) {
        const {operation, channel, common} = nextProps;

        switch (operation.type) {
            case comms.GET_DICTLIST_SUCCESS:
                const type = operation.result.config[0];
                const value = common.dictList.data;
                if (type == dictTypes.SETTLEMENT_METHOD_TYPE) {
                    this.setState({
                        settlementMethodType: value,
                    })
                } else if (type == dictTypes.INVOICE_NODE_TYPE) {
                    this.setState({
                        invoiceNodeType: value,
                    })
                } else if (type == dictTypes.INVOICE_OBJ_TYPE) {
                    this.setState({
                        invoiceObjType: value,
                    })
                } else if (type == dictTypes.BANK_TYPE) {
                    this.setState({
                        bankType: value,
                    })
                } else if (type == dictTypes.SALES_CHANNEL_TYPE) {
                    this.setState({
                        salesChannelType: value,
                    })
                } else if (type == dictTypes.SALES_WAY_TYPE) {
                    this.setState({
                        salesWayType: value,
                    })
                }
                break;
            case actions.GET_SALESCHANNEL_SUCCESS:
                const {salesChannel} = channel;
                if (salesChannel.code == 100) {

                    this.setState({
                        salesChannel: salesChannel.result
                    });
                } else {
                    alert("查询失败");
                }
                break;
            case actions.ADD_SALESCHANNEL_SUCCESS:
                const {addResult} = channel;
                if (addResult.code == 100) {
                    alert("插入成功");
                    window.history.go(-1);
                    // this.props.router.push('/channel');
                } else {
                    alert(addResult.msg);
                }
                break;
            case actions.UPDATE_SALESCHANNEL_SUCCESS:
                const {updateResult} = channel;
                if (updateResult.code == 100) {
                    alert("更新成功");
                    this.props.router.push('/KLF_PG_GM_CHANNEL_LIST');
                } else {
                    alert(updateResult.msg);
                }
                break;
        }

    }

    onEvent = (type, params) => {
        const {salesChannel} = this.state;
        switch (type) {
            case 'saveOrUpdate':
                if (!!params.id) {
                    this.props.actions.updateSalesChannel(params);
                } else {
                    this.props.actions.addSalesChannel(params);
                }
                break;
            case 'back':
                window.history.go(-1);
                break;
            case 'changeInputValue':
                switch (params.type) {
                    case  dictTypes.BANK_TYPE:
                        salesChannel.bankId = params.value;
                        break;
                    case dictTypes.SALES_CHANNEL_TYPE:
                        salesChannel.salesChannelId = params.value;
                        break;
                    case dictTypes.SALES_WAY_TYPE:
                        salesChannel.salesWayId = params.value;
                        break;
                    default:
                        break;
                }
                this.setState({
                    salesChannel: salesChannel
                });
                break;
            default:
        }
    }

    render() {
        var data = this.props.location.query;
        var {id} = data;
        const {
            settlementMethodType,
            invoiceNodeType,
            invoiceObjType,
            bankType,
            salesChannelType,
            salesWayType,
            salesChannel
        } = this.state;
        if (_.isEmpty(id)) {
            return (
                <Fragment>
                    {
                        <BaseForm
                            onEvent={this.onEvent}
                            settlementMethodType={settlementMethodType}
                            invoiceNodeType={invoiceNodeType}
                            invoiceObjType={invoiceObjType}
                            salesChannel={salesChannel}
                            bankType={bankType}
                            salesChannelType={salesChannelType}
                            salesWayType={salesWayType}
                            dictTypes={dictTypes}
                        />
                    }
                </Fragment>
            )
        } else {
            return (
                <Fragment>
                    {
                        !_.isEmpty(salesChannel) &&!_.isEmpty(bankType)&&!_.isEmpty(salesChannelType)&&!_.isEmpty(salesWayType)&&
                        <BaseForm
                            onEvent={this.onEvent}
                            settlementMethodType={settlementMethodType}
                            invoiceNodeType={invoiceNodeType}
                            invoiceObjType={invoiceObjType}
                            salesChannel={salesChannel}
                            bankType={bankType}
                            salesChannelType={salesChannelType}
                            salesWayType={salesWayType}
                            dictTypes={dictTypes}
                        />
                    }
                </Fragment>
            )
        }
    };
}

export default AddSalesChannel;