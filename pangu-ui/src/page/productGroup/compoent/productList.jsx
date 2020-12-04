import React, { Fragment, Component } from 'react';
import { Table, Divider, Tag } from 'antd';
import { resourColor, serviceColor, giftColor } from '../../../util/dictType.js'
class ProductList extends Component {
  constructor(props) {
    super(props);
  }
  getColumns = () => {
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
      title: '资源',
      dataIndex: 'productName',
      render: (text, row, index) => {
        // console.log("productName")
        // console.log(text)
        // console.log(row.productName)
        // console.log(row.serviceCode)
        // console.log(row.needs)
        // console.log(row.addon)
        let productName = row.productName;
        if (row.service === '住宿') {
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
      title: '最高价',
      dataIndex: 'maxCost',
    },
    {
      title: '最低价',
      dataIndex: 'minCost',
    },
    {
      title: 'Block',
      dataIndex: 'blockNatural',
    },
    {
      title: '点数',
      dataIndex: 'pointOrTimes',
    },
    {
      title: '状态',
      dataIndex: 'status',
    },
    {
      title: '排序',
      dataIndex: 'sort',
    }]
  }

  render() {
    const { loading, data } = this.props;
    return (
      <Table
        columns={this.getColumns()}
        pagination={false}
        dataSource={data}
        rowKey="id"
        loading={loading}
      />
    );
  }
}

export default ProductList;

