import React, { Fragment, Component } from 'react';
import { Table, Divider, Input, Tag, LocaleProvider, message } from 'antd';
import { resourColor, serviceColor, giftColor } from '../../../util/dictType.js'
import { hashHistory, Link } from 'react-router';
import { ReservOrderStatus } from '../../../util/dictType.js'

import moment from 'moment';
import zh_CN from 'antd/lib/locale-provider/zh_CN';
import cookie from 'react-cookies';
class List extends Component {
  constructor(props) {
    super(props);
  }
  getColumns = () => {
    const { reservChannelList } = this.props;
    const doc = ["magenta", "red", "volcano", "orange", "gold", "lime", "green", "cyan", "blue", "geekblue", "purple"];
    return [{
      title: '编号',
      dataIndex: 'id',
      render: text => {
        return <Link to={{ pathname: "/OrderInfo", query: { id: text } }} target="_blank" className="fontBlue fontWeight" activeStyle={{ color: 'red' }}>
          {text}</Link>;
      }
    }, {
      title: '订单日期',
      dataIndex: 'createTime',
      render: (text, row, index) => {
        return (moment(text).format('YYYY-MM-DD'))
      },
    }, {
      title: '酒店|商户',
      render: (text, row, index) => {
        if(row.hotelName){
          return (<span>{row.hotelName} | {row.shopName}</span>)
        }else{
          return (<span>{row.shopName}</span>)
        }
        
      },
    },
    {
      title: '客户',
      dataIndex: 'giftName',
    },
    {
      title: '预约日期',
      dataIndex: 'giftDate',

    },
    {
      title: '接单侠',
      dataIndex: 'orderCreator',
    },
    {
      title: '处理人',
      dataIndex: 'operator',
    },
    {
      title: '商品内部简称',
      dataIndex: 'goodsName',
    },
    {
      title: '预定渠道',
      dataIndex: 'shopChannelId',
      render: (text, row, index) => {
        return reservChannelList.map(item => {
          if (item.id === text) {
            return item.name;
          }
        });

      }
    },
    {
      title: '预约状态',
      dataIndex: 'proseStatus',
      render: (text, row, index) => {
        if (text) {
          return ReservOrderStatus.map(item => {
            if (item.type === text) {
              return <Tag color={item.color} key={item.name} >
                {row.serviceType.indexOf('_cpn') != -1 && item.type == 1 ? '兑换成功':item.name}
              </Tag>;
            }
          })
        } else {
          return '-';
        }

      },
    },
    {
      title: '标签',
      dataIndex: 'varTags',
      render: (text, row, index) => {
        if (text) {
          return text.split(",").map(item => {
            if(item && item !== '' ) {
              return <Tag key={item} >{item}</Tag>;
            }
            // if(item === "test" ||item === "VIP" || item === "自付"){
            // }
          })
        } else {
          return '-';
        }

      },
    },
    {
      title: '备注',
      dataIndex: 'reservRemark',
    }]
  }

  handleChange = value => {
    this.props.onList({
      current: value.current,
      size: value.pageSize,
    })
  }
  addLink = (record, index) => {
    // return (<Link to= {{ pathname: "/goodsDetails", query: { id: record.id } }} target="_blank" className="fontBlue fontWeight" activeStyle={{ color: 'red' }}>
    // </Link>);
    var path = {
      pathname: '/OrderInfo',
      query: { id: record.id },
    }
    this.props.router.push(path);
  }
  render() {
    const { loading, data, onEvent, channelList } = this.props;
    const view = cookie.load("KLF_PG_OL_VIEW");
    const pagination = {
      page: data.current,
      total: data.total,
      defaultSize: 10
    }
    // onRow={(record, index) => ({
    //   index,
    //   onClick: view ? () => onEvent('view', record.id) : null,
    // })}
    return (<LocaleProvider locale={zh_CN} >
      <Table 
        columns={this.getColumns()}
        dataSource={data.records}
        pagination={this.props.pagination(pagination)}
        rowKey="id"
        loading={loading}
        onChange={this.handleChange}
      /></LocaleProvider>
    );
  }
}

export default List;

