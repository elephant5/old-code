import React, {Fragment} from 'react';
import Filter from './compoent/filter';
import List from './compoent/list';
import {connect} from 'react-redux';
import {withRouter} from 'react-router';
import {bindActionCreators} from 'redux';
import * as actions from '../../store/channel/action';
import * as comms from '../../store/common/action';
import TableListBase from '../../base/table-list-base';
import * as dictTypes from "../../util/dictType"

@connect(
    ({operation, channel,common}) => ({
        operation,
        channel: channel.toJS(),
        common:common.toJS()
    }),
    dispatch => ({
        actions: bindActionCreators(actions, dispatch),
        comms:bindActionCreators(comms, dispatch),
    })
)
@withRouter
class Hotel extends TableListBase {
    constructor(props) {
        super(props);
        this.state = {
            tableData: {},
            filterParmas: {},
            bankTypeList: [],
            salesChannelType: [],
            salesWayType: [],
            settlementMethodType: [],
            invoiceNodeType: [],
            invoiceObjType: [],
            pageSize: 10,
        };
    }

    componentDidMount() {
        //初始化字典数据
        this.props.comms.getDictList(dictTypes.BANK_TYPE);
        this.props.comms.getDictList(dictTypes.SALES_CHANNEL_TYPE);
        this.props.comms.getDictList(dictTypes.SALES_WAY_TYPE);
        this.props.comms.getDictList(dictTypes.SETTLEMENT_METHOD_TYPE);
        this.props.comms.getDictList(dictTypes.INVOICE_NODE_TYPE);
        this.props.comms.getDictList(dictTypes.INVOICE_OBJ_TYPE);
        this.onList({
            action: this.props.actions.getSalesChannelList,
            params: {
                "condition": {},
                "current": 1,
                "size": 10
            }
        })
    }

    componentWillReceiveProps(nextProps) {
        const {operation, channel,common} = nextProps;
        switch (operation.type) {
            case actions.GET_SALESCHANNELLIST_SUCCESS:
                this.setState({
                    tableData: channel.salesChannelList.result,
                    isTableLoading: false
                })
                break;
            case comms.GET_DICTLIST_SUCCESS:
                const type = operation.result.config[0];
                if (type == dictTypes.BANK_TYPE) {
                    this.setState({
                        bankTypeList: common.dictList.data,
                    })
                } else if (type == dictTypes.SALES_CHANNEL_TYPE) {
                    this.setState({
                        salesChannelType: common.dictList.data,
                    })
                } else if (type == dictTypes.SALES_WAY_TYPE) {
                    this.setState({
                        salesWayType: common.dictList.data,
                    })
                } else if (type == dictTypes.SETTLEMENT_METHOD_TYPE) {
                    this.setState({
                        settlementMethodType: common.dictList.data,
                    })
                } else if (type == dictTypes.INVOICE_NODE_TYPE) {
                    this.setState({
                        invoiceNodeType: common.dictList.data,
                    })
                } else if (type == dictTypes.INVOICE_OBJ_TYPE) {
                    this.setState({
                        invoiceObjType: common.dictList.data,
                    })
                }
                break;
            case actions.UPDATE_SALESCHANNELSTATUS_SUCCESS:
                const param = JSON.parse(operation.result.config.data);
                const { tableData} = this.state;
                tableData.records.map(item=>{
                    if(item.id==param.id){
                        item.status = param.status;
                    }
                });
                this.setState({
                    tableData: tableData
                });
                alert("状态更新成功");
                break;
            default:
        }
    }

    onEvent = (type, params) => {
        const {pageSize} = this.state;
        
        switch (type) {
            case 'add':
                this.props.router.push('/addchanel');
                break;
            case 'update':
                var path = {
                    pathname:'/addchanel',
                    query:{id:params},
                }
                this.props.router.push(path);
                break;
            // 查询
            case 'search':
                this.setState({
                    filterParmas: params
                }, () => {
                    this.onList({
                        action: this.props.actions.getSalesChannelList,
                        params: {
                            "condition": params,
                            "current": 1,
                            "size": pageSize
                        }
                    })
                })
                break;
            default:
        }
    }
    // 切换分页
    getList = params => {
        this.setState({
            pageSize: params.size,
          })
        this.onList({
            action: this.props.actions.getSalesChannelList,
            params: {
                "condition": this.state.filterParmas,
                ...params
            }
        })
    }

    update = params=>{
        this.props.actions.updateSalesChannelStatus(params)
    }

    render() {
        const {
            tableData,
            isTableLoading,
            bankTypeList,
            salesChannelType,
            salesWayType,
            settlementMethodType,
            invoiceNodeType,
            invoiceObjType,
        } = this.state;
        return (
            <Fragment>
                <Filter
                    onEvent={this.onEvent}
                    salesChannelType={salesChannelType}
                    salesWayType={salesWayType}
                    settlementMethodType={settlementMethodType}
                    bankTypeList={bankTypeList}
                    invoiceNodeType={invoiceNodeType}
                    invoiceObjType={invoiceObjType}
                />
                <List
                    data={tableData}
                    loading={isTableLoading}
                    onEvent={this.onEvent}
                    pagination={this.getPagination}
                    onList={this.getList}
                    onUpdate ={ this.update}
                    bankTypeList={bankTypeList}
                    salesChannelType={salesChannelType}
                    salesWayType={salesWayType}
                    settlementMethodType={settlementMethodType}
                    invoiceNodeType={invoiceNodeType}
                    invoiceObjType={invoiceObjType}
                />
            </Fragment>
        );
    }
}

export default Hotel;