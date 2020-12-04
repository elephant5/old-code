import React, { Fragment } from 'react';
import Filter from './compoent/filter';
import List from './compoent/list';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { bindActionCreators } from 'redux';
import * as actions from '../../store/reservOrder/action';
import * as productActions from '../../store/product/action';
import * as resourceActions from '../../store/resource/action';
import * as commonActions from '../../store/common/action';
import TableListBase from '../../base/table-list-base';
import * as goodsActions from '../../store/goods/action';
import * as comms from '../../store/common/action';
import { message } from 'antd';
import * as dictTypes from "../../util/dictType";
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
    actions: bindActionCreators({ ...actions, ...resourceActions, ...productActions, ...commonActions, ...goodsActions,...comms }, dispatch),
  })
)
@withRouter
class Order extends TableListBase {
  constructor(props) {
    super(props);
    this.state = {
      resourceType: [],
      giftTypeList: [],
      tableData: {},
      filterParmas: {},
      cityList: [],
      isTableLoading: true,
      serviceTypeList: [],
      channelList: [],
      shopList: [],
      reservChannelList: [],
      allGoodsList: [],
      channels: [], channel: {},
      actCodeTags: [],
      reservOrderTags: [],
      pageSize: 10,
      downLoadLoading: false,
      bankTypeList:[],
      expressTypeList:[]
    };
  }
  componentDidMount() {
    this.props.actions.getGiftTypeList();
    this.props.actions.getResourceType();
    // this.props.actions.selectShopList();
    this.props.actions.getChannel();
    // this.props.actions.selectGoodsList();
    this.props.actions.channelSelectAll();
    this.props.actions.getAllSysDict({ type: "act_code_tag" }).then((data) => {
      const { goods } = this.props;
      if (goods.sysDict.code === 100) {
        this.setState({
          actCodeTags: goods.sysDict.result
        })
      }
    });
    this.props.actions.getAllSysDict({ type: "bank_type" }).then((data) => {
      const { goods } = this.props;
      if (goods.sysDict.code === 100) {
        this.setState({
          bankTypeList: goods.sysDict.result
        })
      }
    });
    this.props.actions.getAllSysDict({ type: "reserv_order_tag" }).then((data) => {
      const { goods } = this.props;
      if (goods.sysDict.code === 100) {
        this.setState({
          reservOrderTags: goods.sysDict.result
        })
      }
    });

    this.onList({
      action: this.props.actions.selectReservOrderPageList,
      params: {
        "condition": {
        },
        "current": 1,
        "size": 10
      }
    })

    this.props.actions.getDictList(dictTypes.EXPRESS_TYPE);
  }
  componentWillReceiveProps(nextProps) {
    const { operation, reserv, product, resource, common, goods } = nextProps;
    switch (operation.type) {
      case resourceActions.GET_GIFTTYPELIST_SUCCESS:
        this.setState({
          giftTypeList: resource.giftTypeList.result
        })
        break;
      case resourceActions.GET_RESOURCETYPE_SUCCESS:
        this.setState({
          resourceType: resource.resourceType.result
        })
        break;
      case resourceActions.GET_SELECTSHOPLIST_SUCCESS:
        this.setState({
          shopList: resource.shopList.result
        });
        break;
      case resourceActions.GET_CHANNEL_SUCCESS:
        this.setState({
          reservChannelList: resource.channelList.result
        });
        break;
      case actions.GET_SELECTRESERVORDERPAGELIST_SUCCESS:
        this.setState({
          tableData: reserv.reservOrderList.result,
          isTableLoading: false
        })
        break;
      case goodsActions.GET_CHANNELSELECTALL_SUCCESS:
        this.setState({
          channelList: goods.channelList.result
        });
        break;
      case goodsActions.GET_SELECTGOODSLIST_SUCCESS:
        this.setState({
          allGoodsList: goods.allGoodsList.result
        });
        break;
      case comms.GET_DICTLIST_SUCCESS:
        const type = operation.result.config[0];
        if (type == dictTypes.EXPRESS_TYPE) {
            this.setState({
              expressTypeList: common.dictList.data,
            })
        }  
        break;
      default:
        break;
    }
  }
  onEvent = (type, params) => {
    const { pageSize } = this.state;

    switch (type) {
      // 查询
      case 'search':
        this.setState({
          filterParmas: params
        }, () => {
          let strs = params.expressNumbers ;
          params.expressNumbers=null;
          if(!!strs){
            let expressNumbers = strs.split(",");
            params.expressNumbers=expressNumbers;
          }
          this.onList({
            action: this.props.actions.selectReservOrderPageList,
            params: {
              "condition": params,
              "current": 1,
              "size": pageSize
            }
          })
        })
        break;
      case 'view':
        var path = {
          pathname: '/OrderInfo',
          query: { id: params },
        }
        this.props.router.push(path);
        break;
      case 'exportOrder':
        this.setState({
          downLoadLoading: true,
        })
        let values = {
          "condition": params,
          "current": 1,
        }
        this.props.actions.exportOrder(values).then((data) => {
          const { reserv } = this.props;
          // if (reserv.exportOrderRes.code == 100 && reserv.exportOrderRes.result != null && reserv.exportOrderRes.result != "") {
          //   window.open(reserv.exportOrderRes.result)
          if (reserv.exportOrderRes.code == 100 ) {
            message.info("邮件已发送至您的个人邮箱，请注意查收");
          } else {
            message.error("系统错误");
          }
          this.setState({
            downLoadLoading: false,
          })
        }).catch((error) => {
          message.error("系统出错")
          this.setState({
            downLoadLoading: false,
          })
        });
        break;
      default:
    }
  }
  // 切换分页
  getList = params => {
    this.setState({
      pageSize: params.size,
    })
    this.onList({
      action: this.props.actions.selectReservOrderPageList,
      params: {
        "condition": this.state.filterParmas,
        ...params
      }
    })
  }
  render() {
    const { resourceType, giftTypeList, tableData, isTableLoading, serviceTypeList, channelList, shopList, reservChannelList
      , allGoodsList, actCodeTags, reservOrderTags,bankTypeList,expressTypeList } = this.state;
    return (
      <Fragment>

        <Filter channelList={channelList} shopList={shopList} reservChannelList={reservChannelList} allGoodsList={allGoodsList}
          resourceType={resourceType}
          giftTypeList={giftTypeList}
          onEvent={this.onEvent} actCodeTags={actCodeTags}
          reservOrderTags={reservOrderTags}
          serviceTypeList={serviceTypeList}
          downLoadLoading={this.state.downLoadLoading}
          bankTypeList={bankTypeList} 
          expressTypeList={expressTypeList}  
        />
        <List
          data={tableData} reservChannelList={reservChannelList}
          loading={isTableLoading}
          onEvent={this.onEvent}
          pagination={this.getPagination}
          onList={this.getList}
        />

      </Fragment>
    );
  }
}

export default Order;