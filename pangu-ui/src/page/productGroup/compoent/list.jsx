import React, { Fragment, Component } from 'react';
import { Table, Divider, Input, Tag } from 'antd';
import { Link } from 'react-router';
import { resourColor, serviceColor, giftColor } from '../../../util/dictType.js'
import cookie from 'react-cookies';
class List extends Component {
  constructor(props) {
    super(props);
  }
  getColumns = () => {
    const { onEvent } = this.props;
    const view = cookie.load("KLF_PG_GM_PGL_VIEW");
    const doc = ["magenta", "red", "volcano", "orange", "gold", "lime", "green", "cyan", "blue", "geekblue", "purple"];
    return [{
      title: '产品组ID',
      dataIndex: 'id',
    }, {
      title: '产品组内部简称',
      dataIndex: 'shortName',
      render: view ? (text, record) => <a onClick={() => onEvent("selectById", record.id)}>{text}</a> : null,
    }, {
      title: '销售渠道',
      dataIndex: 'bankId',
      render: (text, record) => {
        const temp = record.salesChannelId ? record.salesChannelId : '-';
        return text + "/" + temp + "/" + record.salesWayId;
      },
    },
    {
      title: '资源类型',
      dataIndex: 'service',
      render: text => {

        if (text) {

          return text.split(" ").map(ser => {
            return serviceColor.map(item => {
              if (item.name === ser) {

                return <Tag color={item.color} key={ser} >{ser}</Tag>;
              }
            });

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

          return text.split(" ").map(gift => {
            return giftColor.map(item => {
              if (item.name === gift) {
                return <Tag color={item.color} key={gift} >{gift}</Tag>;
              }
            });

          });
        } else {
          return "-";
        }
      }
    },
    {
      title: '成本最高价',
      dataIndex: 'maxCost',
    },
    {
      title: '成本最低价',
      dataIndex: 'minCost',
    },
    {
      title: '商品ID',
      dataIndex: 'goodsId',
    },
    {
      title: '商品内部简称',
      dataIndex: 'goodsShortName',
      render: view ? (text, record) => <Link to={{ pathname: "/goodsDetails", query: { id: record.goodsId } }} target="_blank" className="fontBlue fontWeight" activeStyle={{ color: 'red' }}>

        {text}</Link> : null,
    }
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
      />
    );
  }
}

export default List;

