import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import { withRouter, Link } from 'react-router';
import { bindActionCreators } from 'redux';
import { Form, Input, Button, Menu, Dropdown, Icon, Select, LocaleProvider, Row, Col, message, Table, Tag, Divider } from 'antd';
import * as actions from '../../store/reservOrder/action';
import * as productActions from '../../store/product/action';
import * as resourceActions from '../../store/resource/action';
import * as commonActions from '../../store/common/action';
import * as goodsActions from '../../store/goods/action';
import { USE_LIMIT_TYPE } from '../../util/dictType'
import zh_CN from 'antd/lib/locale-provider/zh_CN';
import Accom from './compoent/accom'
import Trip from './compoent/trip'
import CommonType from './compoent/commonType'
import moment from 'moment';
import { ReservOrderStatus, ReservCodeStatus, serviceColor, payOrderStatus } from '../../util/dictType.js'
import AccomUpdate from './update/accomUpdate';
import BuffetUpdate from './update/buffetUpdate';
import TeaUpdate from './update/teaUpdate';
import Reserve from './update/reserve';
import Fail from './update/fail';
import Cancel from './update/cancel';
import SendExpress from './update/sendExpress'
import ReUpdate from './update/reupdate';
import cookie from 'react-cookies';
import OrderLogForm from './compoent/log-form.jsx';
import _ from 'lodash';
import * as dictTypes from "../../util/dictType";

const columns = [
  {
    title: '预定时间',
    dataIndex: 'bookDate',
    key: 'bookDate',
    render: (text, row, index) => {
      return (moment(text).format('YYYY-MM-DD'));
    }
  },
  {
    title: '净价',
    dataIndex: 'netPrice',
    key: 'netPrice',
  },
  {
    title: '服务费',
    dataIndex: 'serviceRate',
    key: 'serviceRate',
  },
  {
    title: '增值税',
    dataIndex: 'taxRate',
    key: 'taxRate',
  },
  {
    title: '结算规则',
    dataIndex: 'settleRule',
    key: 'settleRule',
  },
  {
    title: '协议价',
    dataIndex: 'protocolPrice',
    key: 'protocolPrice',
  },
  {
    title: '数量',
    dataIndex: 'nightNumbers',
    key: 'nightNumbers',
  },
  {
    title: '实报价',
    dataIndex: 'realPrice',
    key: 'realPrice',

  },
  {
    title: '币种',
    dataIndex: 'currency',
    key: 'currency',
  },
];
@connect(
  ({ operation, reserv, product, resource, common, goods }) => ({
    operation,
    product: product.toJS(),
    reserv: reserv.toJS(),
    resource: resource.toJS(),
    common: common.toJS(),
    goods: goods.toJS()
  }),
  dispatch => ({
    actions: bindActionCreators({ ...actions, ...resourceActions, ...productActions, ...commonActions, ...goodsActions }, dispatch),
  })
)
@withRouter
@Form.create()
class OrderDetails extends Component {
  constructor(props) {
    super(props);
    this.state = {
      // 渠道
      channelList: [],
      // 验证方式
      authTypeList: [],
      goodsInfo: {},
      festivalList: [],
      TabDisabled: true,
      giftTypeList: [],
      serviceTypeList: [],
      sysDict: [],
      isEdit: false,
      reservOrderInfo: {},
      // 国家城市列表
      countryCity: [],
      // 获取商户渠道
      channels: [],
      channel: {},
      // 结算方式列表
      settleMethods: [],
      // 货币类型列表
      currencys: [],
      shopSettleMsg: [],
      isLoading: true,
      isUpdate: false,
      GoodsGroupListRes: [],
      newShopDetail: {},
      isReserve: false,
      isFail: false,
      isCancel: false,
      isReUpdate: false,
      isSendExpress: false,
      city: {},
      selectChannelListByServerType: [],
      startHandleLoading: false,
      updateOrderLoading: false,
      reservSuccessLoading: false,
      reservFailLoading: false,
      reservCancelLoading: false,
      reUpdateLoading: false,
      messageLoading: false,
      orderLogList: [],
      logLog: {},
      orderId: {},
      thirdCpn: {},
      shortUrl: '',
      thirdCodePassword: '',
      receiveState: '未领取',
      type: '',
      expressTypeList: [],
      sysHospitalList:[]
    };
  }
  componentDidMount() {
    // this.props.actions.getGiftTypeAll();
    // this.props.actions.getServiceTypeAll();
    // this.props.actions.authTypeList();
    // this.props.actions.channelSelectAll();
    // 获取商户渠道
    // this.props.actions.selectChannelListByServerType();
    // 结算方式列表
    this.props.actions.getSettleMethod();
    // 货币类型列表
    this.props.actions.getCurrency();
    this.props.actions.getChannel();
    this.props.actions.getSysHospitalList({});
    var data = this.props.location.query;
    var { id } = data;
    if (!!id) {
      //根据id查找渠道信息
      this.props.actions.selectReservOrderById(data);
      this.setState({
        orderId: id
      });
      this.onEvent('queryLogs', { "orderId": id });
    }
    // this.props.actions.getAllSysDict({type:USE_LIMIT_TYPE});
    this.props.actions.getDictList(dictTypes.EXPRESS_TYPE);
  }

  componentWillReceiveProps(nextProps) {
    const { operation, reserv, product, resource, common, goods } = nextProps;
    const { orderId } = this.state;
    switch (operation.type) {
      case goodsActions.GET_AUTHTYPELIST_SUCCESS:
        this.setState({
          authTypeList: goods.authTypeList.result
        });
        break;
      case actions.GET_SELECTRESERVORDERBYID_SUCCESS:
        this.setState({
          reservOrderInfo: reserv.reservOrderInfo.result,
        }, () => {
          // const obj  = reserv.reservOrderInfo.result;
          // let params  ={};
          // params.shopId =obj.shopId;
          // params.shopItemId=obj.shopItemId;
          // params.gift=obj.giftType;
          // params.shopChannelId=obj.shopChannelId;
          // params.bookDate=obj.giftDate;
          this.onEvent('getShopDetail', reserv.reservOrderInfo.result.shopId);
          this.props.actions.selectGoodsGroupById(reserv.reservOrderInfo.result.productGroupId);
          this.props.actions.getCity(reserv.reservOrderInfo.result.shopDetailRes.shop.cityId);
          this.props.actions.selectChannelListByServerType({ service: reserv.reservOrderInfo.result.serviceType });
          if (!_.isEmpty(this.state.reservOrderInfo)) {
            this.setState({
              thirdCpn: this.state.reservOrderInfo.cpnThirdCode,
              receiveState: !this.state.reservOrderInfo.thirdCpnNo ? '未领取' : '已领取',
              type: this.state.reservOrderInfo.serviceTypeCode
            }, () => {
              this.setState({
                shortUrl: !_.isEmpty(this.state.thirdCpn) ? this.state.thirdCpn.shortUrl : '',
                thirdCodePassword: !_.isEmpty(this.state.thirdCpn) ? this.state.thirdCpn.thirdCodePassword : ''
              })
            })
          }
        });
        break;

      case resourceActions.GET_SELECTCHANNELLISTBYSERVERTYPE_SUCCESS:

        this.setState({
          selectChannelListByServerType: resource.selectChannelListByServerType.result
        })
        break;
      case resourceActions.GET_CHANNEL_SUCCESS:

        this.setState({
          channels: resource.channelList.result
        })
        break;
      case resourceActions.GET_SETTLEMETHOD_SUCCESS:
        this.setState({
          settleMethods: resource.settleMethod.result
        })
        break;
      case resourceActions.GET_SHOPSETTLEMSG_SUCCESS:
        let arry = [];
        arry.push(resource.ShopSettleMsgRes.result);
        this.setState({
          shopSettleMsg: arry,
          isLoading: false
        })
        break;
      case goodsActions.GET_SELECTGOODSGROUPBYID_SUCCESS:

        this.setState({
          GoodsGroupListRes: goods.selectGoodsGroupById.result
        })
        break;
      case resource.GET_CURRENCY_SUCCESS:
        this.setState({
          currencys: resource.currency.result
        })
        break;
      case actions.GET_STARTHANDLE_SUCCESS:
        message.success("开始处理预订单！");
        this.setState({
          reservOrderInfo: reserv.startHandle.result,
          startHandleLoading: false,
        });
        this.onEvent('queryLogs', { "orderId": orderId });
        break;
      case resourceActions.GET_SHOPDETAIL_SUCCESS:
        this.setState({
          newShopDetail: resource.shopDetail.result
        });

        break;
      case resourceActions.GET_GETCITY_SUCCESS:
        this.setState({
          city: resource.city
        });

        break;
      case actions.GET_UPDATERESERVORDER_SUCCESS:
        if (reserv.updateReservOrder.code === 100) {
          message.success("预订单修改成功！");
          this.setState({
            reservOrderInfo: reserv.updateReservOrder.result,
          });
          this.closeReserveModel();
          this.onEvent('queryLogs', { "orderId": orderId });
        } else {
          message.error("预订单修改失败！" + reserv.updateReservOrder.msg);
        }
        this.setState({
          updateOrderLoading: false,
        });
        break;

      case actions.GET_RESERVCANCEL_SUCCESS:
        if (reserv.reservCancel.code === 100) {
          message.success("预订单取消成功！");
          this.setState({
            reservOrderInfo: reserv.reservCancel.result,
          });
          this.closeReserveModel();
          this.onEvent('queryLogs', { "orderId": orderId });
        } else {
          message.error("预订单取消失败！" + reserv.reservCancel.msg);


        }
        this.setState({
          reservCancelLoading: false,
        });

        break;
      case actions.GET_RESERVFAIL_SUCCESS:
        if (reserv.reservFail.code === 100) {
          message.success("预订单预定失败操作成功！");
          this.setState({
            reservOrderInfo: reserv.reservFail.result,
          });
          this.closeReserveModel();
          this.onEvent('queryLogs', { "orderId": orderId });
        } else {
          message.error("预订单预定失败操作失败！" + reserv.reservFail.msg);

        }
        this.setState({
          reservFailLoading: false,
        });

        break;
      case actions.GET_RESERVSUCCESS_SUCCESS:
        if (reserv.reservSuccess.code === 100) {
          message.success("预订单预定成功！");
          this.setState({
            reservOrderInfo: reserv.reservSuccess.result
          });
          this.onEvent('queryLogs', { "orderId": orderId });
        } else {
          message.error("预订单预定失败！" + reserv.reservSuccess.msg);

        }
        this.setState({
          reservSuccessLoading: false,
        })

        this.closeReserveModel();
        break;
      case actions.GET_RESERVUPDATEINFO_SUCCESS:
        if (reserv.reservUpdateInfo.code === 100) {
          message.success("预订单信息修改成功！");
          this.setState({
            reservOrderInfo: reserv.reservUpdateInfo.result,

          });
        } else {
          message.error("预订单信息修改失败！" + reserv.reservUpdateInfo.msg);

        }
        this.setState({
          reUpdateLoading: false,
        })
        this.closeReserveModel();
        break;
      case actions.GET_MESSAGE_SUCCESS:
        if (reserv.messageInfo.code === 100) {
          message.success("短信发送成功！");
        } else {
          message.error("短信发送失败！" + reserv.messageInfo.result.msg);
        }
        this.setState({
          messageLoading: false,
        })
        this.onEvent('queryLogs', { "orderId": orderId });
        break;
      case actions.GET_REUSE_SUCCESS:

        if (reserv.reuse.code === 100) {
          message.success("核销码激活成功！");
          this.setState({
            reservOrderInfo: reserv.reuse.result,
          });
        } else {
          message.error("核销码激活失败！" + reserv.reuse.result.msg);
        }

        break;
      case actions.GET_ORDERLIST_SUCCESS:
        if (reserv.orderLogListRes.code === 100) {

          let list = reserv.orderLogListRes.result;
          this.setState({
            orderLogList: list,
          });
        } else {
          message.error("留言列表查询失败" + reserv.orderLogListRes.msg);
        }

        break;
      case actions.ADD_ORDERLOG_SUCCESS:
        if (reserv.logLogRes.code === 100) {
          message.success("留言插入成功！");
          let { orderLogList } = this.state;
          orderLogList.push(reserv.logLogRes.result);
          this.refs.orderLog.resetFields();
          this.onEvent('queryLogs', { "orderId": orderId });
        } else {
          message.error("留言插入失败！");
        }

        break;
      case commonActions.GET_DICTLIST_SUCCESS:
        const type = operation.result.config[0];
        if (type == dictTypes.EXPRESS_TYPE) {
          this.setState({
            expressTypeList: common.dictList.data,
          })
        }
        break;
      case actions.GET_SYSHOSPITALLIST_SUCCESS:
        this.setState({
          sysHospitalList: reserv.getSysHospitalListRes.result,
        })
        break;
      default:
        break;
    }
    // this.onEvent('queryLogs',{"orderId":this.state.reservOrderInfo.id});
  }

  onEvent = (type, params) => {
    let {reservOrderInfo} = this.state;
    switch (type) {
      case 'getShopDetail':
        this.props.actions.getShopDetail(params);
        break;
      case 'updateReservOrder':
        this.setState({
          isUpdate: false,
          updateOrderLoading: true,
        }, () => {
          this.props.actions.updateReservOrder(params);
        })

        break;
      case 'reservSuccess':
        debugger;
        this.setState({
          reservSuccessLoading: true,
        })
        if(reservOrderInfo.serviceType === '绿通就医'&&params.hospitalId===reservOrderInfo.hospital.name){
          params.hospitalId=reservOrderInfo.hospital.hospitalId;
        }
        this.props.actions.reservSuccess(params);
        break;
      case 'reservFail':
        this.setState({
          reservFailLoading: true,
        })
        this.props.actions.reservFail(params);
        break;
      case 'reservCancel':
        this.setState({
          reservCancelLoading: true,
        })
        this.props.actions.reservCancel(params);
        break;
      case 'reservUpdateInfo':
        this.setState({
          reUpdateLoading: true,
        })
        this.props.actions.reservUpdateInfo(params);
        break;
      case 'insertLog':
        // this.setState({
        //   reUpdateLoading: true,
        // })
        this.props.actions.insertLog(params);
        break;
      case 'queryLogs':
        // this.setState({
        //   reUpdateLoading: true,
        // })
        this.props.actions.listLogs(params);
        break;
      case 'saveObjEdit':
        this.props.actions.saveObjEdit(params).then((data) => {
          const { reserv } = this.props;
          const { orderId } = this.state;
          if (reserv.saveObjEditRes && reserv.saveObjEditRes.code == 100) {
            message.info("保存成功");
            this.setState({
              reservOrderInfo: reserv.saveObjEditRes.result
            });
            this.onEvent('queryLogs', { "orderId": orderId });
            this.closeReserveModel();
          } else {
            message.error(reserv.saveObjEdit.msg)
          }
        }).catch((error) => {
          message.error("系统错误")
        });
        break;
      case 'saveObjEditAndSend':
        this.props.actions.saveObjEditAndSend(params).then((data) => {
          const { reserv } = this.props;
          const { orderId } = this.state;
          if (reserv.saveObjEditAndSendRes && reserv.saveObjEditAndSendRes.code == 100) {
            message.info("发货成功");
            this.setState({
              reservOrderInfo: reserv.saveObjEditAndSendRes.result
            });
            this.onEvent('queryLogs', { "orderId": orderId });
            this.closeReserveModel();
          } else {
            message.error(reserv.saveObjEditAndSendRes.msg)
          }
        }).catch((error) => {
          message.error("系统错误")
        });
        break;
      default:
        break;
    }
  }

  handleMenuClick = (e) => {
    console.log('click', e);
  }
  startHandle = () => {
    this.setState({
      startHandleLoading: true,
    })
    const { reservOrderInfo } = this.state;
    this.props.actions.startHandle({ id: reservOrderInfo.id });
  }

  update = () => {
    this.setState({ isUpdate: true });
  }

  Reserve = () => {
    this.setState({ isReserve: true });
  }
  closeEditModel = item => {
    this.setState({
      isUpdate: false,
      isReserve: false,
      isFail: false,
      isCancel: false,
      isReUpdate: false,
      isSendExpress: false,
    })
  }
  closeReserveModel = item => {
    this.setState({
      isReserve: false,
      isUpdate: false,
      isFail: false,
      isCancel: false,
      isReUpdate: false,
      isSendExpress: false,
    });
  }
  isFail = item => {
    this.setState({ isFail: true });
  }
  isCancel = item => {
    this.setState({ isCancel: true });
  }
  isReUpdate = item => {
    this.setState({ isReUpdate: true });
  }
  isSendExpress = item => {
    this.setState({ isSendExpress: true })
  }
  message = item => {
    const { reservOrderInfo } = this.state;
    this.setState({
      messageLoading: true,
    })
    this.props.actions.message(reservOrderInfo);
  }
  reuse = () => {
    const { reservOrderInfo } = this.state;
    this.props.actions.reuse(reservOrderInfo.reservCode.id);
  }
  render() {
    const { reservOrderInfo, channels, countryCity, shopSettleMsg, isUpdate, GoodsGroupListRes, newShopDetail,
      isReserve, isCancel, isFail, isReUpdate, isSendExpress, city, selectChannelListByServerType, orderLogList, expressTypeList,sysHospitalList } = this.state;
    const process = cookie.load("KLF_PG_OL_PROCESS");
    const modify = cookie.load("KLF_PG_OL_MODIFY");
    const book = cookie.load("KLF_PG_OL_BOOK");
    const fail = cookie.load("KLF_PG_OL_FAIL");
    const cancel = cookie.load("KLF_PG_OL_CANCEL");
    const fix = cookie.load("KLF_PG_OL_FIX");
    return (<LocaleProvider locale={zh_CN} >
      <div className="c-filter">

        {reservOrderInfo.proseStatus === '0' && process && (<div style={{ float: 'left' }}><Button type="primary" onClick={this.startHandle} loading={this.state.startHandleLoading}>开始处理</Button> <Divider type="vertical" /></div>)}
        {(reservOrderInfo.proseStatus === '4' || reservOrderInfo.proseStatus === '1' || reservOrderInfo.proseStatus === '0') && modify && <div style={{ float: 'left' }}>
          <Button type="primary" onClick={this.update} disabled={reservOrderInfo.tags && reservOrderInfo.tags.indexOf('自付') >= 1 ? true : false}>变更预订信息</Button><Divider type="vertical" />
        </div>}
        {(reservOrderInfo.proseStatus === '4' || reservOrderInfo.proseStatus === '0' || reservOrderInfo.proseStatus === '1') && book && (<div style={{ float: 'left' }}><Button type="primary" onClick={this.Reserve}>预定</Button><Divider type="vertical" /></div>)}
        {(reservOrderInfo.proseStatus === '4' || reservOrderInfo.proseStatus === '0' || reservOrderInfo.proseStatus === '1') && fail && (<div style={{ float: 'left' }}><Button type="primary" onClick={this.isFail}>失败</Button><Divider type="vertical" /></div>)}
        {(reservOrderInfo.proseStatus === '4' || reservOrderInfo.proseStatus === '1' || reservOrderInfo.proseStatus === '0') && cancel && (<div style={{ float: 'left' }}><Button type="primary" onClick={this.isCancel} >取消</Button><Divider type="vertical" /></div>)}
        {reservOrderInfo.serviceTypeCode == 'object_cpn' && (<div style={{ float: 'left' }}><Button type="primary" onClick={this.isSendExpress} disabled={(reservOrderInfo.proseStatus === '1' && reservOrderInfo.logisticsInfo.status == 0) ? false : true}>快递发货</Button><Divider type="vertical" /></div>)}

        {reservOrderInfo.proseStatus === '1' && book && <Button type="primary" onClick={this.message} loading={this.state.messageLoading}>预定成功通知</Button>}
        {reservOrderInfo.proseStatus === '2' && book && <Button type="primary" onClick={this.message} loading={this.state.messageLoading}>预定取消通知</Button>}
        {reservOrderInfo.proseStatus === '3' && book && <Button type="primary" onClick={this.message} loading={this.state.messageLoading}>预定失败通知</Button>}
        <Divider type="vertical" />
        {fix && <Button type="primary" onClick={this.isReUpdate} disabled={reservOrderInfo.proseStatus === '1' ? false : true}>修正</Button>}
        <Divider orientation="left">基础信息</Divider>
        {isUpdate && reservOrderInfo.serviceType === '住宿' && <AccomUpdate onEvent={this.onEvent} closeEditModel={this.closeEditModel} newShopDetail={newShopDetail} reservOrderInfo={reservOrderInfo} GoodsGroupListRes={GoodsGroupListRes} updateOrderLoading={this.state.updateOrderLoading}></AccomUpdate>}
        {isUpdate && reservOrderInfo.serviceType === '自助餐' && <BuffetUpdate onEvent={this.onEvent} closeEditModel={this.closeEditModel} newShopDetail={newShopDetail} reservOrderInfo={reservOrderInfo} GoodsGroupListRes={GoodsGroupListRes} updateOrderLoading={this.state.updateOrderLoading}></BuffetUpdate>}
        {isUpdate && (reservOrderInfo.serviceType === '下午茶' || reservOrderInfo.serviceType === '定制套餐' || reservOrderInfo.serviceType === '健身' || reservOrderInfo.serviceType === '水疗')
          && <TeaUpdate onEvent={this.onEvent} closeEditModel={this.closeEditModel} newShopDetail={newShopDetail} reservOrderInfo={reservOrderInfo} GoodsGroupListRes={GoodsGroupListRes} updateOrderLoading={this.state.updateOrderLoading}></TeaUpdate>}

        {isReserve && <Reserve onEvent={this.onEvent}  sysHospitalList={sysHospitalList} channels={selectChannelListByServerType} closeReserveModel={this.closeReserveModel} reservOrderInfo={reservOrderInfo} GoodsGroupListRes={GoodsGroupListRes} reservSuccessLoading={this.state.reservSuccessLoading}></Reserve>}
        {isCancel && <Cancel onEvent={this.onEvent} closeReserveModel={this.closeReserveModel} reservOrderInfo={reservOrderInfo} reservCancelLoading={this.state.reservCancelLoading}></Cancel>}
        {isFail && <Fail onEvent={this.onEvent} closeReserveModel={this.closeReserveModel} reservOrderInfo={reservOrderInfo} reservFailLoading={this.state.reservFailLoading}></Fail>}
        {isSendExpress && <SendExpress onEvent={this.onEvent} closeReserveModel={this.closeReserveModel} reservOrderInfo={reservOrderInfo} expressTypeList={expressTypeList}></SendExpress>}
        {isReUpdate && <ReUpdate onEvent={this.onEvent} channels={selectChannelListByServerType} closeReserveModel={this.closeReserveModel} reservOrderInfo={reservOrderInfo} GoodsGroupListRes={GoodsGroupListRes} reUpdateLoading={this.state.reUpdateLoading}></ReUpdate>}

        <Form layout="inline" onSubmit={this.handleSearch} type="flex">
          <Row type="flex" >
            <Col span={6} >
              <Form.Item label="系统编号"  >
                {reservOrderInfo.id}
              </Form.Item>
            </Col>
            <Col span={6} >
              <Form.Item label="产品类型">
                {

                  serviceColor.map(item => {
                    if (item.name === reservOrderInfo.serviceType) {
                      return <Tag color={item.color} key={reservOrderInfo.serviceType} >{reservOrderInfo.serviceType}</Tag>;
                    }
                  })
                }
              </Form.Item>
            </Col>
            <Col span={6} >
              <Form.Item label="隶属商品">
                <Link to={{ pathname: "/goodsDetails", query: { id: reservOrderInfo.goodsId } }} target="_blank" className="fontBlue fontWeight" activeStyle={{ color: 'red' }}>
                  {reservOrderInfo.goodsBaseVo && reservOrderInfo.goodsBaseVo.shortName}</Link>
              </Form.Item>
            </Col>
            <Col span={6} >
              <Form.Item label="激活码">
                {reservOrderInfo.code}
              </Form.Item>
            </Col>
          </Row>
          <Row type="flex" >
            <Col span={6} >
              <Form.Item label="订单日期">
                {moment(reservOrderInfo.createTime).format('YYYY-MM-DD HH:mm')}
              </Form.Item>
            </Col>
            <Col span={6} >
              <Form.Item label="客户名称">
                {reservOrderInfo.activeName}
              </Form.Item>
            </Col>
            <Col span={6} >
              <Form.Item label="客户电话">
                {reservOrderInfo.giftPhone}
              </Form.Item>
            </Col>
            <Col span={6} >
              <Form.Item label="销售渠道">
                {reservOrderInfo.goodsBaseVo && reservOrderInfo.goodsBaseVo.bankName + "/"}
                {reservOrderInfo.goodsBaseVo && reservOrderInfo.goodsBaseVo.salesChannelName ? reservOrderInfo.goodsBaseVo.salesChannelName : '-'}
                {reservOrderInfo.goodsBaseVo && "/" + reservOrderInfo.goodsBaseVo.salesWayName}
              </Form.Item>
            </Col>
          </Row>

          {
            this.state.type.indexOf('_cpn') != -1 &&
            <Row type='flex'>
              <Col span={6} >
                <Form.Item label="兑换券码">
                  {!_.isEmpty(this.state.thirdCpn) && 'SHORTURL' == this.state.thirdCpn.couponsType
                    ? this.state.shortUrl : this.state.thirdCodePassword}

                </Form.Item>
              </Col>
              <Col span={6} >
                <Form.Item label="领取状态">
                  {this.state.receiveState}
                </Form.Item>
              </Col>
              <Col span={6} >
                <Form.Item label="领取时间">
                  {
                    !_.isEmpty(this.state.thirdCpn) ? moment(this.state.thirdCpn.receiveTime).format('YYYY-MM-DD HH:mm')
                      : ''
                  }

                </Form.Item>
              </Col>
            </Row>
          }

          <Row type="flex" >
            <Col span={6} >
              <Form.Item label="处理状态">
                {

                  ReservOrderStatus.map(item => {
                    if (item.type === reservOrderInfo.proseStatus) {
                      //处理状态下增加一种状态【兑换成功】
                      return this.state.type.indexOf('_cpn') != -1 && reservOrderInfo.proseStatus == 1 ?
                        <Tag color={item.color} key={item.name} >兑换成功</Tag> :
                        <Tag color={item.color} key={item.name} >{item.name}</Tag>

                      // return <Tag color={item.color} key={item.name} >{item.name}</Tag>;
                    }
                  })}

              </Form.Item>
            </Col>
            <Col span={6} >
              <Form.Item label="标签">
                {reservOrderInfo.tags && reservOrderInfo.tags.split(",").map(item => {
                  if (item) {
                    return <Tag>{item}</Tag>
                  }

                })}
              </Form.Item>
            </Col>
            <Col span={6} >
              {reservOrderInfo.orderCreator && <Form.Item label="接单侠">
                {reservOrderInfo.orderCreator}
              </Form.Item>}
            </Col>
            <Col span={6} >
              {reservOrderInfo.operator && <Form.Item label="处理人">
                {reservOrderInfo.operator}
              </Form.Item>}
            </Col>

          </Row>


          {reservOrderInfo.tags && reservOrderInfo.tags.indexOf('自付') >= 0 &&
            <Row type="flex" >

              <Col span={6} >
                <Form.Item label="自付金额">
                  {reservOrderInfo.detail.payAmoney}
                </Form.Item>
              </Col>
              <Col span={6} >
                <Form.Item label="退款金额">
                  {reservOrderInfo.detail.backAmount}
                </Form.Item>
              </Col>
              <Col span={6} >
                {reservOrderInfo.detail.backAmountStatus && <Form.Item label="退款状态">
                  {

                    payOrderStatus.map(item => {
                      if (item.type === reservOrderInfo.detail.backAmountStatus) {
                        return <Tag color={item.color} key={item.name} >{item.name}</Tag>;
                      }
                    })}
                </Form.Item>}
              </Col>
              <Col span={6} >
                {reservOrderInfo.detail.backFailResean && <Form.Item label="退款失败">
                  {reservOrderInfo.detail.backFailResean}
                </Form.Item>}
              </Col>
            </Row>
          }
          <Divider orientation="left" >预约信息</Divider>
          {reservOrderInfo.serviceType != '机场出行'&& reservOrderInfo.serviceType != '绿通就医' &&<Fragment><Row type="flex" >
            <Col span={6} >
              <Form.Item label="商户名称" >

                {reservOrderInfo.shopDetailRes && <Link to={{ pathname: "/addtwo/" + reservOrderInfo.shopDetailRes.shop.id, query: {} }} target="_blank" className="fontBlue fontWeight" activeStyle={{ color: 'red' }}>
                  {reservOrderInfo.shopDetailRes.shop.hotelName ? reservOrderInfo.shopDetailRes.shop.hotelName + "|" + reservOrderInfo.shopDetailRes.shop.name : reservOrderInfo.shopDetailRes.shop.name}</Link>}
              </Form.Item>
            </Col>
            <Col span={6} >
              <Form.Item label="商户城市">
                {reservOrderInfo.shopDetailRes && city && city.nameCh}

              </Form.Item>

            </Col>
            <Col span={12} >
              <Form.Item label="商户地址">
                {reservOrderInfo.shopDetailRes && reservOrderInfo.shopDetailRes.shop.address}
              </Form.Item>
            </Col>

          </Row>
          <Row type="flex" >
            <Col span={6} >
              <Form.Item label="资源名称">
                {reservOrderInfo.shopItemName}
              </Form.Item>
            </Col>
            <Col span={6} >
              <Form.Item label="资源渠道" >
                {reservOrderInfo.shopDetailRes && this.state.channels.map(item => {
                  if (item.id === reservOrderInfo.shopDetailRes.shopProtocol.shopChannelId) {
                    return item.name;
                  }
                })}
              </Form.Item>
            </Col>
            <Col span={6} >
              <Form.Item label="权益类型">
                {reservOrderInfo.giftTypeName}
              </Form.Item>
            </Col>
          </Row>
          <Row type="flex" >
            <Col span={6} >
              <Form.Item label="预约姓名" >
                {reservOrderInfo.giftName}
              </Form.Item>
            </Col>
            <Col span={6} >
              <Form.Item label="预约电话">
                {reservOrderInfo.giftPhone}
              </Form.Item>
            </Col>
            <Col span={6} >
              {reservOrderInfo.detail && reservOrderInfo.detail.bookNameEn && <Form.Item label="预订人姓名拼音">
                {reservOrderInfo.detail && reservOrderInfo.detail.bookNameEn}
              </Form.Item>}
            </Col>
          </Row>
          <Row type="flex">
            {reservOrderInfo.thirdCpnNum && <Col span={6}>
              <Form.Item label="第三方产品编号">
                {reservOrderInfo.thirdCpnNum}
              </Form.Item>
            </Col>}
            {reservOrderInfo.thirdCpnName && <Col span={6}>
              <Form.Item label="第三方产品名称">
                {reservOrderInfo.thirdCpnName}
              </Form.Item>
            </Col>}
          </Row>
          {//-------------------------------------------------
          }
          {(reservOrderInfo.serviceType === '下午茶' || reservOrderInfo.serviceType === '定制套餐' || reservOrderInfo.serviceType === '自助餐') &&
            <Row type="flex" >
              <Col span={6} >
                <Form.Item label="预约日期">
                  {reservOrderInfo.giftDate}
                </Form.Item>
              </Col>
              <Col span={6} >
                <Form.Item label="预约时段" >
                  {reservOrderInfo.giftTime}
                </Form.Item>
              </Col>
              <Col span={6} >
                <Form.Item label="用餐人数">
                  {reservOrderInfo.giftPeopleNum}
                </Form.Item>
              </Col>

            </Row>
          }
          {(reservOrderInfo.serviceType === 'SPA' || reservOrderInfo.serviceType === '健身' || reservOrderInfo.serviceType === '单杯茶饮') &&
            <Row type="flex" >
              <Col span={6} >
                <Form.Item label="预约日期">
                  {reservOrderInfo.giftDate}
                </Form.Item>
              </Col>
              <Col span={6} >
                <Form.Item label="预约时段" >
                  {reservOrderInfo.giftTime}
                </Form.Item>
              </Col>
              <Col span={6} >

              </Col>

            </Row>
          }
          {reservOrderInfo.serviceType === '住宿' && <Accom reservOrderInfo={reservOrderInfo} shopDetail={newShopDetail}></Accom>}
          {reservOrderInfo.serviceType === '出行' && <Trip reservOrderInfo={reservOrderInfo}></Trip>}

          <Row type="flex" >
              {reservOrderInfo.detail && reservOrderInfo.detail.notice && <Col span={6} >
                <Form.Item label="重要通知" >
                  {reservOrderInfo.detail.notice}
                </Form.Item>
              </Col>}
            <Col span={12} >
              <Form.Item label="备注" >
                {reservOrderInfo.reservRemark}
              </Form.Item>
            </Col>
          </Row>

          {reservOrderInfo.serviceType === '实物券' && reservOrderInfo.logisticsInfo &&
            (<div><Divider orientation="left" >物流信息</Divider>
              <Row type="flex" >
                <Col span={6} >
                  <Form.Item label="收件人">
                    {reservOrderInfo.logisticsInfo.consignee}
                  </Form.Item>
                </Col>
                <Col span={6} >
                  <Form.Item label="收件人手机" >
                    {reservOrderInfo.logisticsInfo.phone}
                  </Form.Item>
                </Col>
              </Row>

              <Row type="flex" >
                <Col span={6} >
                  <Form.Item label="收货地址">
                    {reservOrderInfo.logisticsInfo.address}
                  </Form.Item>
                </Col>
                <Col span={6} >
                  <Form.Item label="快递公司" >
                    {!!reservOrderInfo.logisticsInfo.expressNameId &&
                      dictTypes.getLabelByType(reservOrderInfo.logisticsInfo.expressNameId, expressTypeList)
                    }
                  </Form.Item>
                </Col>
                <Col span={6} >
                  <Form.Item label="快递单号" >
                    {reservOrderInfo.logisticsInfo.expressNumber}
                  </Form.Item>
                </Col>
                <Col span={6} >
                  <Form.Item label="快递寄送时间" >
                    {reservOrderInfo.logisticsInfo.expressDate ? moment(reservOrderInfo.logisticsInfo.expressDate).format('YYYY-MM-DD HH:mm') : null}
                  </Form.Item>
                </Col>
              </Row>
            </div>)
          }</Fragment>}
          {reservOrderInfo.serviceType === '机场出行' &&<Fragment>
            <Row type="flex" >
              <Col span={6} >
                <Form.Item label="商户名称" >

                  {reservOrderInfo.shopDetailRes && <Link to={{ pathname: "/addtwo/" + reservOrderInfo.shopDetailRes.shop.id, query: {} }} target="_blank" className="fontBlue fontWeight" activeStyle={{ color: 'red' }}>
                    {reservOrderInfo.shopDetailRes.shop.hotelName ? reservOrderInfo.shopDetailRes.shop.hotelName + "|" + reservOrderInfo.shopDetailRes.shop.name : reservOrderInfo.shopDetailRes.shop.name}</Link>}
                </Form.Item>
              </Col>
              <Col span={6} >
                <Form.Item label="资源渠道" >
                  {reservOrderInfo.shopDetailRes && this.state.channels.map(item => {
                    if (item.id === reservOrderInfo.shopDetailRes.shopProtocol.shopChannelId) {
                      return item.name;
                    }
                  })}
                </Form.Item>
              </Col>
              <Col span={6} >
                <Form.Item label="权益类型">
                  {reservOrderInfo.giftTypeName}
                </Form.Item>
              </Col>
            </Row>
            <Row type="flex" >
              <Col span={6} >
                <Form.Item label="预约姓名" >
                  {reservOrderInfo.giftName}
                </Form.Item>
              </Col>
              <Col span={6} >
                <Form.Item label="预约电话">
                  {reservOrderInfo.giftPhone}
                </Form.Item>
              </Col>
              <Col span={6} >
                <Form.Item label="资源名称">
                  {reservOrderInfo.shopItemName}
                </Form.Item>
              </Col>
            </Row>
            <Row type="flex" >
              <Col span={6} >
                <Form.Item label="服务内容">
                  {reservOrderInfo.travelType===1?"接机服务":"送机服务"}
                </Form.Item>
              </Col>
              <Col span={6} >
                <Form.Item label="预约时间" >
                  {reservOrderInfo.giftDate+" "+reservOrderInfo.giftTime}
                </Form.Item>
              </Col>
              <Col span={6} >
                <Form.Item label="乘车人数">
                  {reservOrderInfo.giftPeopleNum}
                </Form.Item>
              </Col>
            </Row>
            <Row type="flex" >
              <Col span={6} >
                <Form.Item label="航班">
                  {reservOrderInfo.flightNumber}
                </Form.Item>
              </Col>
              <Col span={6} >
                <Form.Item label="出发地" >
                  {reservOrderInfo.travelType===1?reservOrderInfo.airport:reservOrderInfo.startPoint}
                </Form.Item>
              </Col>
              <Col span={6} >
                <Form.Item label="目的地">
                  {reservOrderInfo.travelType===1?reservOrderInfo.endPoint:reservOrderInfo.airport}
                </Form.Item>
              </Col>
            </Row>
            <Row type="flex" >
              <Col span={12} >
                <Form.Item label="备注" >
                  {reservOrderInfo.remark}
                </Form.Item>
              </Col>
            </Row>
          </Fragment>}


          {reservOrderInfo.serviceType === '绿通就医' &&<Fragment>
            <Row type="flex" >
              <Col span={6} >
                <Form.Item label="医院名称" >
                  {reservOrderInfo.hospital.name}
                </Form.Item>
              </Col>
              <Col span={6} >
                <Form.Item label="医院资质" >
                  {reservOrderInfo.hospital.hospitalType}
                </Form.Item>
              </Col>
              <Col span={6} >
                <Form.Item label="资源渠道">
                  {reservOrderInfo.shopDetailRes && this.state.channels.map(item => {
                    if (item.id === reservOrderInfo.shopDetailRes.shopProtocol.shopChannelId) {
                      return item.name;
                    }
                  })}
                </Form.Item>
              </Col>
            </Row>
            <Row type="flex" >
              <Col span={6} >
                <Form.Item label="就诊类型" >
                  {reservOrderInfo.hospital.visit}
                </Form.Item>
              </Col>
              <Col span={6} >
                <Form.Item label="服务类型" >
                  {reservOrderInfo.hospital.special}
                </Form.Item>
              </Col>
              <Col span={6} >
                <Form.Item label="医保类型" >
                  {reservOrderInfo.medicalInsuranceType===1?'医保':'自费'}
                </Form.Item>
              </Col>
            </Row>
            <Row type="flex" >
              <Col span={6} >
                <Form.Item label="预约科室" >
                  {reservOrderInfo.hospital.department}
                </Form.Item>
              </Col>
              <Col span={6} >
                <Form.Item label="就医人姓名" >
                  {reservOrderInfo.hospitalName}
                </Form.Item>
              </Col>
              <Col span={6} >
                <Form.Item label="就医人电话">
                  {reservOrderInfo.giftPhone}
                </Form.Item>
              </Col>
            </Row>
            <Row type="flex" >
              <Col span={6} >
                <Form.Item label="预约时间" >
                  {reservOrderInfo.giftDate+" "+reservOrderInfo.giftTime}
                </Form.Item>
              </Col>
              <Col span={6} >
                <Form.Item label="医院确认号">
                  {reservOrderInfo.reservNumber}
                </Form.Item>
              </Col>
            </Row>
            <Row type="flex" >
              <Col span={12} >
                <Form.Item label="病情描述" >
                  {reservOrderInfo.remark}
                </Form.Item>
              </Col>
            </Row>
          </Fragment>}

          <Divider orientation="left" >结算信息</Divider>
          <Row type="flex" >
            <Col span={6} >
              <Form.Item label="预订渠道" >
                {this.state.channels.map(item => {
                  if (item.id === reservOrderInfo.shopChannelId) {
                    return item.name;
                  }
                })}

              </Form.Item>
            </Col>
            <Col span={6} >
              <Form.Item label="结算方式">
                {reservOrderInfo.shopSettleMsgRes && reservOrderInfo.shopSettleMsgRes.map(item => {
                  return item.settleMethod;
                })}
                {/* {this.state.channels.map(item => {
                  if (item.id === reservOrderInfo.shopChannelId) {
                    if (item.internal === '1' || item.internal === 1) {//公司资源
                      return reservOrderInfo.shopDetailRes.shopProtocol.settleMethod;
                    } else {
                      return item.settleMethod;
                    }
                  }
                })} */}
              </Form.Item>
            </Col>
            <Col span={6} >
              <Form.Item label="结算币种">
                {reservOrderInfo.shopSettleMsgRes && reservOrderInfo.shopSettleMsgRes.map(item => {
                  return item.currency;
                })}
                {/* {this.state.channels.map(item => {
                  if (item.id === reservOrderInfo.shopChannelId) {
                    if (item.internal === '1' || item.internal === 1) {//公司资源
                      return reservOrderInfo.shopDetailRes.shopProtocol.currency;
                    } else {
                      return item.currency;
                    }

                  }
                })} */}
              </Form.Item>
            </Col>
            {reservOrderInfo.serviceType === '住宿' && reservOrderInfo.channelNumber && <Col span={6} >
              <Form.Item label="渠道号">
                {reservOrderInfo.channelNumber}
              </Form.Item>
            </Col>}
          </Row>
          <Row type="flex" >
            <Col span={6} >
              {reservOrderInfo.serviceType === '住宿' && reservOrderInfo.reservNumber &&
                <Form.Item label="确认号">
                  {reservOrderInfo.reservNumber}
                </Form.Item>}
              {reservOrderInfo.reservCode && <Form.Item label="核销码">
                {reservOrderInfo.reservCode && reservOrderInfo.reservCode.varCode}
              </Form.Item>}
            </Col>
            <Col span={6} >
              {reservOrderInfo.reservCode && <Form.Item label="使用状态">
                {reservOrderInfo.reservCode && ReservCodeStatus.map(item => {
                  if (item.type === reservOrderInfo.reservCode.varStatus) {
                    return <Tag color={item.color} key={item.name} >{item.name}</Tag>;
                  }
                })}
                {reservOrderInfo.reservCode && reservOrderInfo.reservCode.varStatus === '1' ? <a onClick={this.reuse}>重新激活</a> : null}

              </Form.Item>}
            </Col>
            <Col span={6} >
              {reservOrderInfo.reservCode && <Form.Item label="使用时间">
                {reservOrderInfo.reservCode && reservOrderInfo.reservCode.varUseTime && moment(reservOrderInfo.reservCode.varUseTime).format('YYYY-MM-DD HH:mm')}
              </Form.Item>}
            </Col>

          </Row>
          <Row type="flex" >
            <Col span={24} >
              <Form.Item label="商户结算">
                <Table dataSource={reservOrderInfo.shopSettleMsgRes} width={'70%'} columns={columns} loading={false} bordered={true} pagination={false} />
              </Form.Item>
            </Col>

          </Row>
          <Row type="flex" >
            <Col span={6} >
              {<Form.Item label="结算总额">
                {reservOrderInfo.orderSettleAmount ? reservOrderInfo.orderSettleAmount : 0}
              </Form.Item>}
            </Col>

            <Col span={6} >
              {<Form.Item label="商户赔付金额">
                {reservOrderInfo.orderDamageAmount ? reservOrderInfo.orderDamageAmount : 0}
              </Form.Item>}
            </Col>
            {<Col span={6} >
              <Form.Item label="应付总金额">
                {reservOrderInfo.shopAmount ? reservOrderInfo.shopAmount : 0}
              </Form.Item>
            </Col>}
            <Col span={6} >

            </Col>
          </Row>

        </Form>
        <Divider orientation="left" >操作日志</Divider>
        {
          orderLogList != null &&
          <OrderLogForm orderLogList={orderLogList} reservOrderInfo={reservOrderInfo} onEvent={this.onEvent} ref="orderLog"></OrderLogForm>
        }
      </div>

    </LocaleProvider>
    );
  }
}
export default OrderDetails;