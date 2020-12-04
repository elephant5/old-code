import React, { Fragment, Component } from 'react';
import { Table, Divider, Badge, Tag, Tooltip, Button } from 'antd';
import { resourColor, serviceColor, giftColor } from '../../../util/dictType.js'
import './newfilter.less'
import moment from 'moment';


let selectedRowKeysTemp = new Map();
class ProductInfoList extends Component {
  constructor(props) {
    super(props);

    this.state = {
      expandedRowKeys: []
    }
  }
  getColumns = () => {
    const { itemProduct } = this.props;
    let noAccom = true;
    if (itemProduct.service) {
      let service = itemProduct.service.split(' ');
      if ((service.indexOf('住宿') > -1 && service.length == 1) || (service.indexOf('accom') > -1 && service.length == 1)) {
        noAccom = false;
      }
    }
    if (noAccom) {
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
    } else {
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
  }

  handleChange = value => {
    // this.props.onList({
    //   "condition": this.props.paramsLocal,
    //   current: value.current,
    //   size: value.pageSize,
    // })
    const { onEvent } = this.props;
    onEvent("search", value);
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
    // this.setState({ selectedRowKeys, selectedRowKeysTemp: selectedRowKeys });
    onEvent('selectedRowKeys', selectedRowKeysTemp)
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
    // this.setState({ selectedRowKeys, selectedRowKeysTemp: selectedRowKeys });
    onEvent('selectedRowKeys', selectedRowKeysTemp)
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
      { title: '适用范围', dataIndex: 'applyTime', key: 'applyTime' }
    ];


    const rowSelection = {
      onSelectAll: this.onSelectAll,
      onSelect: this.onSelectChange,
    };
    return <Table rowSelection={rowSelection} size="middle"
      style={{ backGround: '#e6f7ff' }}
      rowKey="id" columns={columns} bordered={true} showHeader={true}
      dataSource={record.productItems}

      pagination={false} />;
  };

  removeByValue = (arr, val) => {
    for (var i = 0; i < arr.length; i++) {
      if (arr[i] == val) {
        arr.splice(i, 1);
        break;
      }
    }
  }
  componentWillMount() {
    selectedRowKeysTemp.clear();
  }
  componentWillReceiveProps(nextProps) {
    const { loading, data, operation } = nextProps;
    if (data && data.records) {
      let expandedRowKeys = data.records.map(item => item.id);
      console.log(expandedRowKeys)
      this.setState({ expandedRowKeys: expandedRowKeys });
    }
  }
  componentWillUpdate(nextProps, nextState) {
    console.log('Component WILL UPDATE!');

  }
  onExpandedRowsChange = (row) => {
    this.setState({ expandedRowKeys: row });
  }
  handleChange = value => {
    this.props.onList({
      // "condition": this.props.selectPage,
      current: value.current,
      size: value.pageSize,
    })
    // const { onEvent } = this.props;
    // onEvent("fenye",value);
  }
  rowClassName = (record, index) => {
    if (record) {
      if (moment() >= moment(record.endDate)) {
        return 'tr_expiry_color';
      } else {
        if (Number((moment(record.endDate) - moment()) / (24 * 3600 * 1000)).toFixed(0) <= 60) {
          return 'tr_expiry_color';
        }
      }
    }
  }
  render() {
    const { loading, data, paramsLocal } = this.props;
    const { expandedRowKeys } = this.state;
    const pagination = {
      page: data.current,
      total: data.total,
      defaultSize: 10,
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
        onChange={this.handleChange}
        scroll={{ x: 'max-content' }}
        rowSelection={rowSelection}
        rowClassName={this.rowClassName}
      // expandRowByClick={true}
      // expandedRowRender={this.expandedRowRender}
      // expandedRowKeys={expandedRowKeys}
      // onExpandedRowsChange={this.onExpandedRowsChange}
      />}
    </Fragment>);
  }
}

export default ProductInfoList;
