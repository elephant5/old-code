import React, { Fragment, Component } from 'react';
import { Table, Divider, Badge, Modal } from 'antd';
import { getLabelByType } from "../../../util/dictType"
import cookie from 'react-cookies';
//开启状态
const OPEN_STATE = 0;
const OPEN_STATE_STR = "启用";
//关闭状态
const CLOSE_STATE = 1;
const CLOSE_STATE_STR = "停用";
const statusMap = ['success', 'error'];
const confirm = Modal.confirm;
class List extends Component {


    constructor(props) {
        super(props);
        this.state = {
        }
    }

    getColumns = () => {
        const { onEvent } = this.props;
        const { bankTypeList, salesChannelType, salesWayType, settlementMethodType, invoiceNodeType, invoiceObjType } = this.props;
        const view = cookie.load("KLF_PG_GM_CL_VIEW");
        const onoff = cookie.load("KLF_PG_GM_CL_ONOFF")
        return [{
            title: '渠道ID',
            dataIndex: 'id',
        }, {
            title: '银行名称',
            dataIndex: 'bankId',
            render: (text) => {
                return getLabelByType(text, bankTypeList)
            }
        }, {
            title: '销售渠道',
            dataIndex: 'salesChannelId',
            render: (text) => {
                return getLabelByType(text, salesChannelType)
            }
        }, {
            title: '销售方式',
            dataIndex: 'salesWayId',
            render: (text) => {
                return getLabelByType(text, salesWayType)
            }
        }, {
            title: '结算方式',
            dataIndex: 'settleMethodId',
            render: (text) => {
                return getLabelByType(text, settlementMethodType)
            }
        }, {
            title: '开票节点',
            dataIndex: 'invoiceNodeId',
            render: (text) => {
                return getLabelByType(text, invoiceNodeType)
            }
        }, {
            title: '开票对象',
            dataIndex: 'invoiceObjId',
            render: (text) => {
                return getLabelByType(text, invoiceObjType)
            }
        }, {
            title: '佣金',
            dataIndex: 'commision',
            render: (text) => {
                if (!text && text != 0) {
                    return "";
                }
                return (text * 100) + "%"
            }
        }, {
            title: '状态',
            dataIndex: 'status',
            // render: (text) => {
            //     const temp = text ;

            //     return temp == OPEN_STATE?OPEN_STATE_STR:CLOSE_STATE_STR
            // }
            render(text) {
                return <Badge status={statusMap[text]} text={text == OPEN_STATE ? OPEN_STATE_STR : CLOSE_STATE_STR} />;
            },
        }, {
            title: '排序',
            dataIndex: 'orders',
        },
        {
            title: '操作',
            key: 'action',
            render: (text, record) => {
                return (
                    <Fragment>
                        {

                            <span>
                                {view && <span className="c-color-blue" onClick={() => onEvent('update', record.id)}>编辑</span>}
                                <Divider type="vertical" />
                                {onoff && <span className="c-color-blue" onClick={() => this.updateStatus(record.id, record.status)}>{record.status == OPEN_STATE ? CLOSE_STATE_STR : OPEN_STATE_STR}</span>}
                            </span>

                        }
                    </Fragment>
                )
            },
        }]
    }

    handleChange = value => {
        this.props.onList({
            current: value.current,
            size: value.pageSize,
        })
    }
    updateStatus = (id, status) => {
        var updateStatus;
        var title;
        if (status == OPEN_STATE) {
            updateStatus = CLOSE_STATE;
            title = CLOSE_STATE_STR;
        } else {
            updateStatus = OPEN_STATE;
            title = OPEN_STATE_STR;
        }
        const { onUpdate } = this.props;
        confirm({
            title: '确定' + title + "吗?",
            okText: '确定',
            cancelText: '取消',
            onOk() {
                onUpdate({
                    id: id,
                    status: updateStatus
                });

            },
            onCancel() { },
        });

    }
    render() {
        const { loading, data } = this.props;
        const pagination = {
            page: data.current,
            total: data.total,
            defaultSize: 10
        }

        return (
            <Table
                columns={this.getColumns()}
                dataSource={data.records}
                pagination={this.props.pagination(pagination)}
                rowKey="id"
                loading={loading}
                onChange={this.handleChange}
            />
        );
    }
}

export default List;

