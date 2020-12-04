import React, { Fragment, Component } from 'react';
import { Table, Divider, Input, Tag, Checkbox, Tooltip, Button } from 'antd';
import { resourColor, serviceColor, giftColor } from '../../../util/dictType.js'

let selectedRowKeysTemp = new Map();
class AccomList extends Component {
  constructor(props) {
    super(props);
  }
  getColumns = () => {
    return [
      {
        title: '国家|城市',
        dataIndex: 'countryName',
        align: 'center',
        width: '80px',
        render: (text, row, index) => {
          return (<span>{row.countryName} <br></br> {row.cityName}</span>)
        },
      },
      {
        title: '酒店中文名称',
        dataIndex: 'hotelName',
        align: 'center',
        width: '150px',
      },
      {
        title: '房型',
        dataIndex: 'productName',
        width: '150px',
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
        title: '适用日期',
        dataIndex: 'applyDate',
        width: '120px',
        render: (text) => {
          if (text) {
            return <span>{text.split('~')[0]}~<br></br>{text.split('~')[1]}</span>
          }
        }
      },
      {
        title: '适用星期',
        dataIndex: 'applyWeek',
        width: '120px',
        render: (text) => {
          if (text) {
            return text.replace(new RegExp(/(星期)/g), '周');
          }
        }
      },
      {
        title: '结算价',
        dataIndex: 'cost',
        width: '60px',
        render: (text) => {
          if (text || text == '0') {
            return "￥" + text
          }
        }
      },
      {
        title: 'OTA参考价',
        dataIndex: 'noData',
        width: '60px'
      },
      {
        title: '地址',
        dataIndex: 'address',
        width: '160px',
        render: (text, row, index) => {
          if (text && text.length > 5) {
            return (<div>
              {text.substr(0, 4)}...&nbsp;&nbsp;
              <Tooltip placement="topLeft" title={text} style={{ backgroundColor: 'transparent' }}>
                <Button style={{ boxShadow: 'none', backgroundColor: 'transparent', borderColor: "transparent", color: '#1890ff', fontSize: 12, backGround: "#ffff" }} >
                  更多↑</Button>
              </Tooltip></div>);
          } else {
            return <div>{text}</div>;
          }
        }
      },
      {
        title: '不可用日期',
        dataIndex: 'blockRule',
        width: '160px',
        render: (text, row, index) => {
          if (text && text.length > 7) {
            return (<div>
              {text.substr(0, 9)}...&nbsp;&nbsp;
        <Tooltip placement="topLeft" title={text} style={{ backgroundColor: 'transparent' }}>
                <Button style={{ boxShadow: 'none', backgroundColor: 'transparent', borderColor: "transparent", color: '#1890ff', fontSize: 12, backGround: "#ffff" }} >
                  更多↑</Button>
              </Tooltip></div>);
          } else {
            return <div>{text}</div>;
          }
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

  onSelectChange = (record, selected, selectedRows, nativeEvent) => {
    const { onEvent } = this.props;
    if (selected) {
      selectedRowKeysTemp.set(record.productItemId, record);
    } else {
      selectedRowKeysTemp.delete(record.productItemId);
    }
    let selectedRowKeys = [];
    selectedRowKeysTemp.forEach((value, key, map) => {
      selectedRowKeys.push(key);
    });
    onEvent('selectedAccomRowKeys', selectedRowKeysTemp)
  }

  onSelectAll = (selected, selectedRows, changeRows) => {
    const { onEvent } = this.props;
    if (selected) {
      changeRows.map(item => {
        selectedRowKeysTemp.set(item.productItemId, item);
      })

    } else {
      changeRows.map(item => {
        selectedRowKeysTemp.delete(item.productItemId, item);
      })
    }
    let selectedRowKeys = [];
    selectedRowKeysTemp.forEach((value, key, map) => {
      selectedRowKeys.push(key);
    });
    onEvent('selectedAccomRowKeys', selectedRowKeysTemp)
  }

  render() {
    const { loading, data } = this.props;
    const pagination = {
      page: data.current,
      total: data.total,
      defaultSize: 10
    }
    const rowSelection = {
      onSelect: this.onSelectChange,
      onSelectAll: this.onSelectAll,
    }
    return (<Fragment>{data && data.records &&
      <Table size="middle"
        columns={this.getColumns()}
        dataSource={data.records}
        pagination={this.props.pagination(pagination)}
        rowKey="productItemId"
        loading={loading}
        bordered={true}
        rowSelection={rowSelection}
        onChange={this.handleChange}
      />}
    </Fragment>);
  }
}

export default AccomList;

