import React, { Component } from 'react';
import { Table } from 'antd';
import moment from 'moment';

class Log extends Component {
    constructor(props) {
        super(props);
        this.state = {  };
    }

    getColumns = () => {
        return [{
            title: 'ID',
            dataIndex: 'id'
          },
            {
            title: '操作类型',
            dataIndex: 'optName'
          },
            {
            title: '操作内容',
            dataIndex: 'serviceName',
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
          },
        ]
    }

    render() {
        const { data } = this.props;
        return (
            <div className="c-modal">
                <div className="c-title">操作日志</div>
                <Table 
                    columns={this.getColumns()} 
                    dataSource={data} 
                    pagination={false}
                    rowKey="id"
                    loading={false}
                />
            </div>
        );
    }
}

export default Log;