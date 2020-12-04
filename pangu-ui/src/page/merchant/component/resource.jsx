import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { bindActionCreators } from 'redux';
import * as actions from '../../../store/resource/action';
import * as commonActions from '../../../store/common/action';
import React, { Component, Fragment } from 'react';
import { Form, Icon, message, Button, Select, Badge, Divider, Popconfirm, Tag } from 'antd';
import './resource.less';
import ResourceBasic from './resourceBasic';
import ItemCalendar from './itemCalendar';
import ResourceOpTab from './resourceOpTab';
import _ from 'lodash';
import { formatDate } from '../../../util/util';
import moment from 'moment';
import { getFileList } from '../../../util/index';
import BraftEditor from 'braft-editor'
import { resourColor, serviceColor } from '../../../util/dictType.js'
import cookie from 'react-cookies';

const { Option } = Select;
const formItemLayout = {
  labelCol: { span: 6 },
  wrapperCol: { span: 12 },
};
@connect(
  ({ operation, resource, common }) => ({
    operation,
    resource: resource.toJS(),
    common: common.toJS()
  }),
  dispatch => ({
    actions: bindActionCreators({ ...actions, ...commonActions }, dispatch)
  })
)
@withRouter
@Form.create()
class Resource extends Component {
  constructor(props) {
    super(props);

    const currentDay = new Date();
    const monthFirstDay = new Date(currentDay.getFullYear(), currentDay.getMonth(), 1);
    const monthFirstWeekDay = monthFirstDay.getDay();
    const weekDay = currentDay.getDay();
    let startDate = new Date(monthFirstDay.getTime() - 86400 * 1000 * monthFirstWeekDay);
    const lastMonth = new Date(currentDay.getMonth() + 1 >= 12 ? currentDay.getFullYear() + 1 : currentDay.getFullYear(), currentDay.getMonth() + 1 >= 12 ? 0 : currentDay.getMonth() + 1, 1);
    let endDate = new Date(lastMonth.getTime() - 86400 * 1000);
    endDate = new Date(endDate.getTime() + 86400 * 1000 * (6 - endDate.getDay()));
    this.state = {
      editId: -1, // -1,无编辑状态，0新增，>0编辑已有
      startDate: new Date(),
      endDate: endDate,
      shopDetail: {
        // 基本信息
        shop: {},
        shopItemList: [],
        // 商户协议
        shopProtocol: {}
      },
      serviceList: [],
      festivalList: [],
      // 产品价格展示
      priceRule: [],
      // block规则展示
      blockRule: [],
      // 结算规则
      settleRule: [],
      // 结算表达式
      settleExpress: {},
      // block规则回显
      blockParams: {},
      // 默认图片展示
      uploadedFile: [],
      // 产品价格默认图片展示
      uploadedPrice: [],
      // 结算规则默认图片展示
      uploadedSettle: [],
      shopTypeService: [],
      serviceGiftList: [],
      isChanged: false,
      isChangedPrice: false,
    };
    // 编辑
    this.editBlock = {}
  }

  componentDidMount() {
    const { shopDetail } = this.state;
    this.props.actions.getShopDetail(this.props.params.id);
    this.props.actions.getFestivalList();
  }

  componentWillReceiveProps(nextProps) {
    const { operation, resource, common } = nextProps;
    switch (operation.type) {
      case actions.GET_SHOPDETAIL_SUCCESS:
        this.setState({
          shopDetail: resource.shopDetail.result,
        }, () => {
          this.props.actions.getShopTypeService(resource.shopDetail.result.shop.shopType);

        })
        break;
      case commonActions.GET_FESTIVALLIST_SUCCESS:
        this.setState({
          festivalList: common.festivalList
        })
        break;
      // case actions.GET_SHOPITEMDETAIL:
      //   this.setState({
      //     shopItemDetail: resource.shopItemDetail.result
      //   })
      //   break;
      // 新增，删除规格
      case actions.ADD_SIZE_SUCCESS:
        message.success('保存商户资源成功！');
        this.changeEditId.bind(resource.sizeInfo.result.id);
        if (this.state.fileList && this.state.fileList.length > 0) {
          this.props.actions.updateFile(this.state.fileList);
        }

        // 重新刷新頁面
        this.props.actions.getShopDetail(this.props.params.id);
        this.props.actions.getPriceRule({
          shopId: resource.sizeInfo.result.shopId,
          shopItemId: resource.sizeInfo.result.id
        })
        this.props.actions.getSettleRule({
          shopId: resource.sizeInfo.result.shopId,
          shopItemId: resource.sizeInfo.result.id
        })
        this.setState({
          isChanged: true,
          isChangedPrice: true,
          editId: resource.sizeInfo.result.id,
        });
        break;
      case actions.UPDATE_SIZE_SUCCESS:
        message.success('修改商户资源成功！');

        if (this.state.fileList && this.state.fileList.length > 0) {
          let temp = this.state.fileList.map(item => {
            item.type = 'shop.item.pic';
            return item;
          })
          this.props.actions.updateFile(temp);
        }
        // 重新刷新頁面
        this.props.actions.getShopDetail(this.props.params.id);
        this.props.actions.getPriceRule({
          shopId: resource.updateSize.result.shopId,
          shopItemId: resource.updateSize.result.id
        })
        this.props.actions.getSettleRule({
          shopId: resource.updateSize.result.shopId,
          shopItemId: resource.updateSize.result.id
        })
        this.setState({
          isChanged: true,
          isChangedPrice: true,

        });

        break;
      // merge之后，重新掉默认展示接口
      case actions.UPDATE_FILE_SUCCESS:
        if (resource.file.code === 100) {
          const objId = resource.file.result[0].objId;
          const type = resource.file.result[0].type;
          this.props.actions.getUploadedFile({ objId, type });
        }


        break;
      case actions.DELETE_SIZE_SUCCESS:
        this.props.actions.getShopDetail(this.props.params.id);
        break;
      case actions.UPDATE_PRICE_SUCCESS:
        message.success('保存成功');
        const arr = this.state.fileList.map(item => {
          item.objId = resource.updatePrice.result.id
          return item;
        });
        this.setState({
          fileList: arr
        }, () => {
          this.props.actions.updateFile(this.state.fileList);
        })
        this.props.actions.getPriceRule({
          shopId: this.state.shopDetail.shop.id,
          shopItemId: this.state.editId
        })
        break;
      case actions.UPDATE_SETTLE_SUCCESS:
        message.success('保存成功');
        resource.updateSettle.result.forEach(item => {
          const arr1 = this.state.fileList.map(curr => {
            curr.objId = item.id;
            return curr;
          })
          this.props.actions.updateFile(arr1)
        })
        this.props.actions.getSettleRule({
          shopId: this.state.shopDetail.shop.id,
          shopItemId: this.state.editId
        })
        break;
      case actions.GET_PRICERULE_SUCCESS:
        this.setState({
          priceRule: resource.priceRule.result,
          isChangedPrice: true
        })
        break;
      case actions.GET_BLOCKLIST_SUCCESS:
        this.setState({
          blockRule: resource.blockList.result || [],
          isChangedPrice: true
        })
        break;
      case actions.GET_SETTLERULE_SUCCESS:
        this.setState({
          settleRule: resource.settleRule.result,
          isChangedPrice: true
        })
        break;
      case actions.UPDATE_BLOCK_SUCCESS:
        message.success('保存Block信息成功');
        this.props.actions.getBlockList(resource.updateBlock.result);
        this.setState({
          isChangedPrice: true
        });
        break;
      case actions.GET_SETTLEEXPRESS_SUCCESS:
        this.setState({
          settleExpress: resource.settleExpress.result
        })
        break;
      case commonActions.GET_SERVICELIST_SUCCESS:
        this.setState({
          serviceList: common.serviceList
        })
        break;
      case actions.DELETE_BLOCK_SUCCESS:
        message.success('删除Block信息成功');
        if (!_.isEmpty(this.editBlock)) {
          this.props.actions.updateBlock(this.editBlock);
        } else {
          this.props.actions.getBlockList(this.state.editId)
        }
        this.setState({
          isChangedPrice: true
        });
        break;
      case actions.DELETE_PRICE_SUCCESS:
        message.success('删除产品价格成功');
        this.props.actions.getPriceRule({
          shopId: this.state.shopDetail.shop.id,
          shopItemId: this.state.editId
        })
        this.setState({
          isChangedPrice: true
        });
        break;
      case actions.DELETE_SETTLE_SUCCESS:
        message.success('删除结算规则成功');
        this.props.actions.getSettleRule({
          shopId: this.state.shopDetail.shop.id,
          shopItemId: this.state.editId
        });
        this.setState({
          isChangedPrice: true
        });
        break;
      case actions.GET_BLOCKPARAMS_SUCCESS:
        this.setState({
          blockParams: resource.blockParams.result,
        })
        break;
      case commonActions.GET_UPLOADEDFILE_SUCCESS:
        this.setState({
          uploadedFile: common.uploadedFile,
        })
        break;
      case commonActions.GET_GETSHOPTYPESERVICE_SUCCESS:
        this.setState({
          shopTypeService: common.shopTypeService,
        })
        break;
      case actions.GET_SERVICEGIFTGET_SUCCESS:

        this.setState({
          serviceGiftList: resource.serviceGiftList.result
        })
        break;


      default:
        break;
    }
  }

  changeEditId(editId) {
    // const currentDay = new Date();
    // const monthFirstDay = new Date(currentDay.getFullYear(), currentDay.getMonth(), 1);
    // const monthFirstWeekDay = monthFirstDay.getDay();
    // const weekDay = currentDay.getDay();
    // let startDate = new Date(monthFirstDay.getTime() - 86400 * 1000 * monthFirstWeekDay);
    // const lastMonth = new Date(currentDay.getMonth() + 1 >= 12 ? currentDay.getFullYear() + 1 : currentDay.getFullYear(), currentDay.getMonth() + 1 >= 12 ? 0 : currentDay.getMonth() + 1, 1);
    // let endDate = new Date(lastMonth.getTime() - 86400 * 1000);
    // endDate = new Date(endDate.getTime() + 86400 * 1000 * (6 - endDate.getDay()));

    // const { shopDetail, startDate, endDate } = this.state;
    // this.props.actions.getShopItemSettlePrice(JSON.stringify({
    //   shopId: shopDetail.shop.id,
    //   shopItemId: editId,
    //   start: moment(startDate).format('YYYY-MM-DD'),
    //   end: moment(endDate).format('YYYY-MM-DD'),
    //   // start: formatDate(startDate, "yyyy-MM-dd"),
    //   // end: formatDate(endDate, "yyyy-MM-dd"),
    //   // gift: 'test'
    // }));
    // this.props.actions.getShopItemDetail({
    //   shopId: shopDetail.shop.id,
    //   shopItemId: editId,
    // });
    const { shopDetail } = this.state;
    const shopId = shopDetail.shop.id;
    const shopItemId = editId;
    // 产品价格
    this.props.actions.getPriceRule({
      shopId,
      shopItemId
    })
    // block规则
    this.props.actions.getBlockList(shopItemId)
    // 结算规则
    this.props.actions.getSettleRule({
      shopId,
      shopItemId
    })
    // 图片默认展示
    this.props.actions.getUploadedFile({ objId: shopItemId, type: 'shop.item.pic' });
    // 产品价格默认展示
    // this.props.actions.getUploadedPrice({objId: shopItemId, type: 'shop.item.price.contract'});
    // 结算规则默认展示
    // this.props.actions.getUploadedSettle({objId: shopItemId, type: 'shop.item.settle.contract'});
    this.setState({
      editId: editId,
      // startDate: startDate,
      // endDate: endDate,
    });

  }

  getForm = () => {
    const { getFieldDecorator } = this.props.form;
    let formProps = {};
    formProps.hotelResource = getFieldDecorator('hotelResource', {
    });
    formProps.resource = getFieldDecorator('resource', {
      initialValue: ''
    });
    formProps.channel = getFieldDecorator('channel', {
      initialValue: ''
    });
    formProps.city = getFieldDecorator('city', {
    });
    return formProps;
  }
  // 添加
  add = params => {
    this.props.actions.serviceGiftGet(params.code);
    this.setState({
      editId: 0,
      serviceType: params
    })
  }

  onEvent = (type, params) => {
    const { fileList, ...other } = params;
    switch (type) {
      // 添加商户资源规格
      case 'addSize':
        this.setState({
          fileList
        })
        this.props.actions.addSize(other);
        return;
      case 'editSize':
        this.setState({
          fileList
        })
        this.props.actions.updateSize(other);
        return;
      case 'deleteSize':
        this.props.actions.deleteSize(params).then((data) => {
          const { resource } = this.props;
          if (resource.deleteSizeRes.code == 100) {
            message.success("删除成功");
          }else{
            message.info(resource.deleteSizeRes.msg);
          }
        }).catch((error) => {
          message.error("系统错误");
        });
        return;
      // 产品价格保存
      case 'savePrice':
        this.setState({
          fileList
        });
        this.props.actions.updatePrice(other);
        return;
      // 结算规则保存
      case 'saveClose':
        this.setState({
          fileList
        });
        this.props.actions.updateSettle(other);
        return;
      case 'saveBlock':
        this.props.actions.updateBlock(params);
        return;
      case 'settleExpress':
        this.props.actions.getSettleExpress(params);
        return;
      // 删除block
      case 'deleteBlock':
        this.props.actions.deleteBlock(params);
        return;
      // 删除price
      case 'deletePrice':
        this.props.actions.deletePrice(params);
        return;
      // 删除结算规则
      case 'deleteSettle':
        this.props.actions.deleteSettle(params);
        return;
      // block前端回显
      case 'getBlock':
        this.props.actions.getBlockParams(params);
        return;
      // 编辑block
      case 'editBlock':
        const data = params;
        const { shopItemId, blockRuleList, ...otherParams } = data;
        // 删除
        this.props.actions.deleteBlock({
          blockRuleList,
          shopItemId,
        });
        // 新增
        this.editBlock = { shopItemId, ...otherParams };
        return;
      case 'displayDiv':
        this.setState({
          editId: 1
        })
        return;

      default:
        return;
    }
  }
  render() {
    const { editId, startDate, endDate, shopDetail, serviceType, serviceList, uploadedFile,
      festivalList, priceRule, blockRule, settleRule, settleExpress, blockParams, shopTypeService, serviceGiftList, isChanged, isChangedPrice } = this.state;
    const { shopType } = shopDetail.shop;
    const { countryCity } = this.props.data;
    const { giftTypeList, allSysDictList } = this.props;
    // const shopType = 'accom';
    const addLoading = this.props.operation.loading[actions.ADD_SIZE];
    const blockLoading = this.props.operation.loading[actions.UPDATE_BLOCK];
    const settleLoading = this.props.operation.loading[actions.UPDATE_SETTLE];
    const priceLoading = this.props.operation.loading[actions.UPDATE_PRICE];
    const serviceAdd = cookie.load("KLF_PG_RM_SL_SERVICE_ADD");
    const serviceView = cookie.load("KLF_PG_RM_SL_SERVICE_VIEW");
    const serviceEdit = cookie.load("KLF_PG_RM_SL_SERVICE_EDIT");
    return (
      <Fragment>
        <div>
          {serviceAdd &&
            (shopType === 'accom' ?
              <Button onClick={() => this.add({ 'code': "accom", 'name': "房型" })} key={'accom'} type="primary" style={{ margin: '10px 10px' }}>+&nbsp;添加房型</Button>
              :
              shopTypeService.map(item => (
                <Button onClick={() => this.add({ 'code': item.code, 'name': item.name })} key={item.code} type="primary" style={{ margin: '10px 10px' }}>+&nbsp;添加{item.name}</Button>
              )))
          }
          {editId === 0 && (
            <div className="c-modal">

              <ResourceBasic
                shopType={shopType}
                serviceType={serviceType}
                id={shopDetail.shop.id}
                shopParams={shopDetail.shop}
                editId={editId}
                addLoading={addLoading}
                countryCity={countryCity}
                onEvent={this.onEvent}
                giftTypeList={giftTypeList}
                allSysDictList={allSysDictList}
                serviceGiftList={serviceGiftList}
              />
              <Divider type="horizontal" dashed={true} style={{ margin: '0 0 24px' }} />
              {/* <div className="resource-flex-wrapper">
                <ResourceCalendar
                  shopType={shopType}
                  topStyle={{ width: '60%', margin: '0 32px 24px' }}
                  shopItemId={editId}
                  shopId={shopDetail.shop.id}
                  onEvent={this.props.onEvent}
                  startDate={startDate}
                  endDate={endDate}
                  currentDay={new Date()}
                />
                <ResourceOpTab tabid={1} startDate={startDate} endDate={endDate} shopItemDetail={shopItemDetail} />
              </div> */}
              <div style={{ width: '100%', height: 1 }}></div>
            </div>
          )}
          {
            shopDetail.shopItemList.map((shopItem, index) => {
              let res = '';
              const params = { 'code': shopItem.serviceType, 'name': shopItem.serviceName }
              if (editId === shopItem.id) {
                res = (<div className="c-modal" key={`idx${index}`} >
                  <ResourceBasic
                    uploadedFile={getFileList(uploadedFile)}
                    shopType={shopType}
                    serviceType={params}
                    id={shopItem.id}
                    editId={editId}
                    onEvent={this.onEvent}
                    countryCity={countryCity}
                    shop={shopItem}
                    shopParams={shopDetail.shop}
                    giftTypeList={giftTypeList}
                    allSysDictList={allSysDictList}
                    serviceGiftList={serviceGiftList}
                  />
                  <Divider type="horizontal" dashed={true} style={{ margin: '0 0 24px' }} />
                  <div className="resource-flex-wrapper">
                    <ItemCalendar
                      shopType={shopType}
                      topStyle={{ width: '50%', margin: '0 32px 24px' }}
                      shopItemId={editId}
                      serviceType={params}
                      shopItem={shopItem}
                      shopId={shopDetail.shop.id}
                      onEvent={this.props.onEvent}
                      startDate={startDate}
                      shop={shopDetail.shop}
                      endDate={endDate}
                      currentDay={new Date()}
                      giftTypeList={giftTypeList}
                      serviceGiftList={serviceGiftList}
                      isChanged={isChanged}
                      isChangedPrice={isChangedPrice}
                      shopProtocol={this.props.shopProtocol}
                      currencys={this.props.currencys}
                    />
                    <ResourceOpTab
                      tabid={1}
                      startDate={startDate}
                      endDate={endDate}
                      blockParams={blockParams}
                      blockLoading={blockLoading}
                      settleLoading={settleLoading}
                      priceLoading={priceLoading}
                      settleExpress={settleExpress}
                      blockRule={blockRule}
                      priceRule={priceRule}
                      settleRule={settleRule}
                      shopId={shopItem.shopId}
                      shopItemId={shopItem.id}
                      data={shopDetail}
                      festivalList={festivalList}
                      onEvent={this.onEvent}
                      shopType={shopType}
                      shopItem={shopItem}
                      shopParams={shopDetail}
                      allSysDictList={allSysDictList}
                      giftTypeList={giftTypeList}
                      serviceGiftList={serviceGiftList}
                      isChanged={isChanged}
                    />
                  </div>
                  <div style={{ width: '100%', height: 1 }}></div>
                </div>)
              } else {
                res = (
                  <div className="shop-item-wrapper" style={{}} key={`shop-${index + 1}`}>
                    <div className="shop-basic-wrapper" onClick={this.changeEditId.bind(this, shopItem.id)} >
                      <div className="shop-img">
                        <img width="100px" height="60px" src='./imgs/default-picture@2x.jpg' />
                      </div>
                      {shopType !== 'accom' && (
                        <div className="shop-basic">
                          <div className="shop-title" onClick={this.changeEditId.bind(this, shopItem.id)}>{shopItem.name}</div>
                        </div>
                      )}
                      {shopType === 'accom' && (
                        <div className="shop-basic">
                          <div className="shop-title">{shopItem.name}</div>
                          <div className="shop-addon">{shopItem.needs ? `${shopItem.needs}  ` : ''}{shopItem.addon}</div>
                        </div>
                      )}
                    </div>
                    <div>
                      {
                        serviceColor.map(item => {
                          if (item.name === shopItem.serviceName) {
                            return (<Tag color={item.color} key={item.type + "1231"} >{item.name}</Tag>);
                          }
                        })
                      }
                    </div>
                    <div>
                      {shopItem.enable === 0 ?
                        <Badge className="shop-status" status="success" text="在售" />
                        :
                        <Badge className="shop-status" status="default" text="停售" />
                      }
                    </div>
                    <div className="shop-ops">
                      {serviceView && <Icon className="shop-op" type="edit" onClick={this.changeEditId.bind(this, shopItem.id)} />}
                      {serviceEdit && <Popconfirm placement="top" title="确定删除当前资源吗？" onConfirm={() => this.onEvent('deleteSize', shopItem.id)} okText="确定" cancelText="取消">
                        <Icon className="shop-op" type="delete" />
                      </Popconfirm>}

                    </div>
                  </div>
                );
              }
              return res;
            })
          }
        </div>
      </Fragment>
    );
  }
}

export default Resource;




