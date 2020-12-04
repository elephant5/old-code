import React, { Component } from 'react';
import { Table } from 'antd';
import moment from 'moment';

class Log extends Component {
    constructor(props) {
        super(props);
        this.state = {  };
    }
    getColumns = () => {
        return [
            {
                title: '操作范围',
                dataIndex: 'optRange'
            },
            {
                title: '操作类型',
                dataIndex: 'optName'
            },
            {
                title: '操作内容',
                dataIndex: 'content',
                render: (text, record) => {
                    return `由“${record.beforeValue}”，修改为 “${record.afterValue}”`
                }
            },
            {
                title: '操作人',
                dataIndex: 'createUser'
            },
            {
                title: '操作时间',
                dataIndex: 'createTime',
                render: (text, records) => {
                    return <div>{moment(text).format('YYYY-MM-DD hh:mm:ss')}</div>
                }
            }
        ]
    }
    onChange = value => {
        this.props.onList({
            current: value.current,
            size: value.pageSize,
        })
    }
    render() {
        const { data, loading } = this.props;
        const pagination = {
            page: data.current,
            total: data.total,
            defaultSize: 10
          }
        return (
            <div className="c-modal">
                <div className="c-title">操作日志</div>
                <Table
                    dataSource={data.records} 
                    columns={this.getColumns()} 
                    rowKey="id"
                    pagination={this.props.pagination(pagination)}
                    onChange={this.onChange}
                    loading={loading}
                />
            </div>
        );
    }
}

export default Log;