import React, { Fragment, Component } from 'react';
import { Table, Divider, Input, Tag, Checkbox, Tooltip, Button } from 'antd';
import { resourColor, serviceColor, giftColor } from '../../../util/dictType.js'
let selectedRowKeysTemp = new Map();

class BuffetList extends Component {
  constructor(props) {
    super(props);
  }
  getColumns = () => {
    return [
      {
        title: '酒店|餐厅',
        dataIndex: 'hotelName',
        align: 'center',
        width: '180px',
        render: (text, row, index) => {
          return (<span>{row.hotelName} <br></br> {row.shopName}</span>)
        },
      },
      {
        title: '权益类型',
        dataIndex: 'service',
        render: (text, row, record) => {
          let sTemp;
          let gTemp;
          let sColor;
          let gColor;
          let sName;
          let gName;
          if (row.service) {
            sTemp = serviceColor.map(item => {
              if (item.name === row.service) {
                sColor = item.color;
                sName = row.service;
                return <Tag color={item.color} key={row.service} >{row.service}</Tag>;
              }
            });
          }
          if (row.gift) {
            gTemp = giftColor.map(item => {
              if (item.name === row.gift) {
                gColor = item.color;
                gName = row.gift;
                return <Tag color={item.color} key={row.gift} >{row.gift}</Tag>;
              }
            });
          }
          if (sName) {
            if (gName) {
              return <span><Tag color={sColor} key={sName} >{sName}</Tag> <br></br> <Tag color={gColor} key={gName} >{gName}</Tag></span>;
            } else {
              return <span><Tag color={sColor} key={sName} >{sName}</Tag> <br></br> -</span>;
            }
          } else {
            if (gName) {
              return <span>- <br></br> <Tag color={gColor} key={gName} >{gName}</Tag></span>;
            } else {
              return <span>- <br></br> -</span>;
            }
          }
        },
      },
      {
        title: '餐型',
        dataIndex: 'productName',
        width: '70px',
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
        title: '成本',
        dataIndex: 'cost',
        width: '70px',
        render: (text) => {
          if (text || text == '0') {
            return "￥" + text
          }
        },
      },
      {
        title: '净价',
        dataIndex: 'netPrice',
        width: '70px',
        render: (text) => {
          if (text || text == '0') {
            return "￥" + text
          }
        }
      },
      {
        title: '服务费',
        dataIndex: 'serviceRate',
        align: 'center',
        width: '70px',
        render: (text) => {
          if (text) {
            if (text == '0%') {
              return '-'
            } else {
              return text;
            }
          } else {
            return '-';
          }
        }
      },
      {
        title: '税费',
        dataIndex: 'taxRate',
        align: 'center',
        width: '70px',
        render: (text) => {
          if (text) {
            if (text == '0%') {
              return '-'
            } else {
              return text;
            }
          } else {
            return '-';
          }
        }
      },
      {
        title: '单人总价',
        dataIndex: 'singlePrice',
        width: '70px',
        render: (text) => {
          if (text || text == '0') {
            return "￥" + text
          }
        }
      },
      {
        title: '双人总价',
        dataIndex: 'doublePrice',
        width: '70px',
        render: (text) => {
          if (text || text == '0') {
            return "￥" + text
          }
        }
      },
      {
        title: '折扣率',
        dataIndex: 'itemRate',
        width: '70px'
      },
      {
        title: '点评/微商城售价',
        width: '70px',
        dataIndex: 'noData',
      },
      {
        title: '开餐时间',
        dataIndex: 'openTime',
        render: (text, row, index) => {
          let openTime = row.openTime;
          let closeTime = row.closeTime;
          if (!openTime) {
            openTime = "-";
          }
          if (!closeTime) {
            closeTime = "-";
          }
          return openTime + "~" + closeTime;
        }
      },
      {
        title: '不可用日期',
        dataIndex: 'blockRule',
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
      {
        title: '泊车信息',
        dataIndex: 'parking',
        width: '140px',
        render: (text, row, index) => {
          if (text && text.length > 7) {
            return (<div>
              {text.substr(0, 3)}...&nbsp;&nbsp;
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
        title: '儿童政策',
        width: '140px',
        dataIndex: 'childrenStr',
        render: (text, row, index) => {
          if (text && text.length > 7) {
            return (<div>
              {text.substr(0, 3)}...&nbsp;&nbsp;
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
    onEvent('selectedBuffetRowKeys', selectedRowKeysTemp)
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
    onEvent('selectedBuffetRowKeys', selectedRowKeysTemp)
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
        scroll={{ x: 'max-content' }}
        rowSelection={rowSelection}
        onChange={this.handleChange}
      />}
    </Fragment>);
  }
}

export default BuffetList;

