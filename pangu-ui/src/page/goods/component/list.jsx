import React, { Fragment, Component } from 'react';
import { Table, Divider, Badge } from 'antd';
import { withRouter, Link } from 'react-router';
import locale from 'antd/lib/date-picker/locale/zh_CN';
import { LocaleProvider } from 'antd';
import zh_CN from 'antd/lib/locale-provider/zh_CN';
import 'moment/locale/zh-cn';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import cookie from 'react-cookies';

const statusMap = ['processing', 'success', 'error'];
const status = ['未上架', '已上架', '已下架'];

class GoodsList extends Component {
  constructor(props) {
    super(props);
    this.state = {
      // 可编辑行对应的key
      editingKey: '4',
      nameCh: '',
      nameEn: '',
      data: {}
    }
  }
  componentDidMount() {

  }
  componentWillReceiveProps(nextProps) {

  }
  // 修改酒店名
  changeGoods = params => {
    this.setState({
      editingKey: params.id,
    })
  }
  // 复制商品
  copyGoods = params => {
    this.setState({
      editingKey: params.id
    })
  }
  // 英文名修改
  changeGoodsStates = params => {
    this.setState({
      editingKey: params.id
    })
  }
  // 英文名修改
  open = params => {
    this.setState({
      editingKey: params.id
    })
  }
  getColumns = () => {
    const { onEvent } = this.props;
    const copy = cookie.load("KLF_PG_GM_GL_COPY");
    const onoff = cookie.load("KLF_PG_GM_GL_ONOFF");
    const view = cookie.load("KLF_PG_GM_GL_VIEW");
    return [
      {
        title: '商品ID',
        dataIndex: 'id',
        sorter: (a, b) => a.id - b.id,
      }, {
        title: '内部简称',
        dataIndex: 'shortName',
      }, {
        title: '销售渠道',
        dataIndex: 'bankName',
        render: (text, record) => {
          const temp = record.salesChannelName ? record.salesChannelName : '-';



          return text + "/" + temp + "/" + record.salesWayName;
        },
      },
      {
        title: '验证方式',
        dataIndex: 'authType',
        render: (text, record) => {
          const temp = text;

          if (temp === '0') {
            return '手机';
          }
          if (temp === '1') {
            return '激活码';
          }

          return "temp";
        },
      },
      {
        title: '零售价',
        dataIndex: 'salesPrice',
        filterMultiple: false,
        sorter: (a, b) => a.salesPrice - b.salesPrice,
      },
      {
        title: '状态',
        dataIndex: 'status',
        onFilter: (value, record) => record.status.indexOf(value) === 0,
        sortDirections: ['descend'],
        filters: [{
          text: '未上架',
          value: '0',
        }, {
          text: '已上架',
          value: '1',
        }, {
          text: '已下架',
          value: '2',
        }],
        render(val) {
          return <Badge status={statusMap[val]} text={status[val]} />;
        },
      },
      {
        title: '操作',
        key: 'action',
        render: (text, record) => {
          const temp = record.status;
          if (temp === '0') {
            return (
              <Fragment>
                {
                  <span>
                    {view && <Link to={{ pathname: "/goodsDetails", query: { id: record.id } }} target="_blank" className="fontBlue fontWeight" activeStyle={{ color: 'red' }}>

                      编辑</Link>}
                    <Divider type="vertical" />
                    {copy && <span className="c-color-blue" style={{'cursor':'pointer'}} onClick={() => this.copyGoods(record.id)}>复制商品</span>}
                    <Divider type="vertical" />
                    {onoff && <span className='c-color-blue' style={{'cursor':'pointer'}} onClick={() => this.open(record)}>上架</span>}
                  </span>
                }
              </Fragment>
            )
          }
          if (temp === '1') {
            return (
              <Fragment>
                {

                  <span>
                    {view && <Link to={{ pathname: "/goodsDetails", query: { id: record.id } }} target="_blank" className="fontBlue fontWeight" activeStyle={{ color: 'red' }}>

                      编辑</Link>}
                    <Divider type="vertical" />
                    {copy && <span className="c-color-blue" style={{'cursor':'pointer'}} onClick={() => this.copyGoods(record.id)}>复制商品</span>}
                    <Divider type="vertical" />
                    {onoff && <span className='c-color-blue'style={{'cursor':'pointer'}}  onClick={() => this.open(record)}>下架</span>}

                  </span>
                }
              </Fragment>
            )
          }
          if (temp === '2') {
            return (
              <Fragment>
                {

                  <span>
                    {view && <Link to={{ pathname: "/goodsDetails", query: { id: record.id } }} target="_blank" className="fontBlue fontWeight" activeStyle={{ color: 'red' }}>

                      编辑</Link>}
                    <Divider type="vertical" />
                    {copy && <span className="c-color-blue" style={{'cursor':'pointer'}} onClick={() => this.copyGoods(record.id)}>复制商品</span>}
                    <Divider type="vertical" />
                    {onoff && <span className='c-color-blue' style={{'cursor':'pointer'}} onClick={() => this.open(record)}>上架</span>}

                  </span>
                }
              </Fragment>
            )
          }

        },
      }]
  }

  handleChange = (value, filters, sorter) => {

    let params = { "status": filters.status, "field": sorter.field, "order": sorter.order }
    this.props.onList({
      // "condition": params,
      current: value.current,
      size: value.pageSize,
    })
  }

  changeGoodsStates = (id, status) => {
    this.props.onChangeGoodsStates({
      id: id,
      status: status
    });
  }
  open = (recode) => {
    this.props.openGoodsEditModel(recode);
  }
  copyGoods = (id) => {
    this.props.onCopyGoods({
      goodsId: id
    });
  }

  render() {
    const { loading, data } = this.props;
    // const { data } = this.state;
    const pagination = {
      page: data.current,
      total: data.total,
      defaultSize: 10
    }
    return (
      <LocaleProvider locale={zh_CN} >
        <Table 
          columns={this.getColumns()}
          dataSource={data.records}
          pagination={this.props.pagination(pagination)}
          rowKey="id"
          loading={loading}
          onChange={this.handleChange}
        />
      </LocaleProvider>
    );
  }
}

export default GoodsList;

