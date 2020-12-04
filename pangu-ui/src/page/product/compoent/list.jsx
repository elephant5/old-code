import React, { Fragment, Component } from 'react';
import { Table, Divider, Input, Tag } from 'antd';
import { resourColor, serviceColor, giftColor } from '../../../util/dictType.js'
class List extends Component {
  constructor(props) {
    super(props);
  }
  getColumns = () => {
    const doc = ["magenta", "red", "volcano", "orange", "gold", "lime", "green", "cyan", "blue", "geekblue", "purple"];
    return [{
      title: '产品ID',
      dataIndex: 'id',
    }, {
      title: '城市',
      dataIndex: 'cityName',
    }, {
      title: '酒店|商户',
      render: (text, row, index) => {
        return (<span>{row.hotelName} | {row.shopName}</span>)
      },
    },
    {
      title: '资源类型',
      dataIndex: 'service',
      render: (text, record) => {
        if (text) {
          return serviceColor.map(item => {
            if (item.type === record.serviceCode) {
              return <Tag color={item.color} key={text} >{text}</Tag>;
            }
          });
        } else {
          return "-";
        }
      }
    },
    {
      title: '权益类型',
      dataIndex: 'gift',
      render: text => {
        if (text) {
          return giftColor.map(item => {
            if (item.name === text) {
              return <Tag color={item.color} key={text} >{text}</Tag>;
            }
          });
        } else {
          return "-";
        }
      }
    },
    {
      title: '资源名称',
      dataIndex: 'productName',
      render: (text, row, index) => {
        let productName = row.productName;
        if (row.serviceCode === 'accom') {
          if (row.needs) {
            productName = productName + ' | ' + row.needs
          }
          if (row.addon) {
            productName = productName + ' | ' + row.addon
          }
        }
        return productName;
      }
    },
    {
      title: '最低成本',
      dataIndex: 'minCost',
    },
    {
      title: '最高成本',
      dataIndex: 'maxCost',
    },
    {
      title: '状态',
      dataIndex: 'status',
      render: (text, row, index) => {
        var status = row.status;
        if (status == 0) {
          status = '在售';
        }
        if (status == 1) {
          status = '停售';
        }
        return status;
      },
    },
    {
      title: '渠道类型',
      dataIndex: 'channelType',
    }]
  }

  handleChange = value => {
    this.props.onList({
      current: value.current,
      size: value.pageSize,
    })
  }

  expandedRowRender = (record, index, indent, expanded) => {
    const columns = [
      {
        title: '序号',
        dataIndex: 'id',
        width: '3.85%',
        align: 'center'
      },
      {
        title: '成本',
        dataIndex: 'cost',
        width: '6.1%',
        key: 'cost',
        align: 'center',
        render: (text, row, index) => {
          return (<span style={{ color: 'red' }}><b>{text}</b></span>)
        }
      },
      {
        title: '市场参考价',
        dataIndex: 'marketCost',
        width: '6.1%',
        key: 'marketCost',
        align: 'center',
      },
      { title: '适用范围', dataIndex: 'applyTime', key: 'applyTime' }
    ];

    return <Table size="middle"
      style={{ backGround: '#e6f7ff' }}
      rowKey="id" columns={columns} bordered={true} showHeader={true}
      dataSource={record.productItems}

      pagination={false} />;
  };

  render() {
    const { loading, data } = this.props;
    let expandedRowKeys = data.records ? data.records.map(item => item.id) : null;
    const pagination = {
      page: data.current,
      total: data.total,
      defaultSize: 10
    }
    return (<Fragment>{data && data.records &&
      <Table size="middle"
        columns={this.getColumns()}
        dataSource={data.records}
        pagination={this.props.pagination(pagination)}
        rowKey="id"
        loading={loading}
        bordered={true}
        onChange={this.handleChange}
        expandRowByClick={true}
        expandedRowRender={this.expandedRowRender}
        expandedRowKeys={expandedRowKeys}
      />}
    </Fragment>);
  }
}

export default List;

