import React, { Fragment, Component } from 'react';
import { Table, Divider, Input,Tag } from 'antd';
import { resourColor , serviceColor,giftColor} from '../../../util/dictType.js'
import moment from 'moment';

class List extends Component {
  constructor(props){
    super(props);
  }
  state={
    selectedRowKeys:[]
  }
  getColumns = () => {
      const { onEvent } = this.props;
      const doc = ["magenta", "red", "volcano", "orange","gold","lime","green","cyan","blue","geekblue","purple"];
      return [{
        title: '编号',
        dataIndex: 'id',
      }, {
        title: '批次号',
        dataIndex: 'codeBatchNo',
      }, {
        title: '码状态',
        dataIndex: 'codeStatusName',
      },
       {
        title: '激活码',
        dataIndex: 'actCode',
      },
      // {
      //   title: '最后状态变更时间',
      //   dataIndex: 'updateTime',
      //   render: (text,row,index) =>{
      //     return <div>{moment(text).format('YYYY-MM-DD hh:mm:ss')}</div>
      //   }
      // },
      {
        title: '商品编号',
        dataIndex: 'goodsId',
      },
      {
        title: '关联商品',
        dataIndex: 'goodsShortName',
      },
      {
        title: '销售渠道',
        dataIndex: 'salesChannelName',
      },
      {
        title: '手机号码',
        dataIndex: 'phone',
      },
      {
        title: '权益人姓名',
        dataIndex: 'peopleName',
      },
      {
        title: '到期时间',
        dataIndex: 'actExpireTime',
        render: (text,row,index) => {
          return text != null && text !== '' && <div>{moment(text).format('YYYY-MM-DD')}</div>
        }
      },
      {
        title: '出库日期',
        dataIndex: 'actOutDate',
        render: (text,row,index) => {
          return text != null && text !== '' && <div>{moment(text).format('YYYY-MM-DD')}</div>
        }
      },
      {
        title: '激活日期',
        dataIndex: 'actCodeTime',
        render: (text,row,index) => {
          return text != null && text !== '' && <div>{moment(text).format('YYYY-MM-DD')}</div>
        }
      },
      {
        title: '退货日期',
        dataIndex: 'actReturnDate',
        render: (text,row,index) => {
          return text != null && text !== '' && <div>{moment(text).format('YYYY-MM-DD')}</div>
        }
      },
      {
        title: '作废日期',
        dataIndex: 'actObsoleteDate',
        render: (text,row,index) => {
          return text != null && text !== '' && <div>{moment(text).format('YYYY-MM-DD')}</div>
        }
      },
      ]
  }

  handleChange = value => {
    this.props.onList({
        current: value.current,
        size: value.pageSize,
    })
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
            rowSelection={this.props.rowSelection}
        />
      );
  }
}

export default List;

