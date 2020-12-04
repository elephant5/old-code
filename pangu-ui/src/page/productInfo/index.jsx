import React, { Fragment } from 'react';
import ProductInfoFilter from './compoent/filter';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { bindActionCreators } from 'redux';
import * as actions from '../../store/product/action';
import * as productGroupActions from '../../store/productGroup/action';
import * as resourceActions from '../../store/resource/action';
import TableListBase from '../../base/table-list-base';
import { Table, Tag, Input, message, Alert } from 'antd';
import { LocaleProvider } from 'antd';
import zh_CN from 'antd/lib/locale-provider/zh_CN';
import ProductInfoList from './compoent/list';
import Calculation from './compoent/calculation';
@connect(
  ({ operation, product, resource, productGroup }) => ({
    operation,
    product: product.toJS(),
    resource: resource.toJS(),
    productGroup: productGroup.toJS(),
  }),
  dispatch => ({
    actions: bindActionCreators({ ...actions, ...resourceActions, ...productGroupActions }, dispatch),

  })
)
@withRouter
class ProductInfo extends TableListBase {
  constructor(props) {
    super(props);
    this.state = {
      resourceType: [],
      giftTypeList: [],
      tableData: {},
      filterParmas: {},
      selectedRowKeys: [], // Check here to configure the default column
      loading: true,
      itemProduct: {},
      cityList: [],
      disabled: false,
      paramsLocal: {},
      pageSize: 10,
      selectedRowKeysTemp:[]
    };
  }
  componentDidMount() {
    this.props.actions.getGiftTypeList();
    this.props.actions.getResourceType();
    this.props.actions.getAllCity();
    this.setState({
      itemProduct: this.props.location.query
    })
    const item = this.props.location.query;
    let params = {};
    if (item.serviceList) {
      if (typeof (item.serviceList) === 'object') {
        params.serviceList = item.serviceList;
      } else {
        let temp = [];
        temp.push(item.serviceList)
        params.serviceList = temp;
      }
    }

    if (item.giftList) {
      if (typeof (item.giftList) === 'object') {
        params.giftList = item.giftList;
      } else {
        let temp = [];
        temp.push(item.giftList)
        params.giftList = temp;
      }
    }


    if (item.groupProductListIds) {
      params.ids = item.groupProductListIds;
    }
    params.statusType = 0;
    this.setState({
      paramsLocal: params
    });
    this.onList({
      action: this.props.actions.selectPage,
      params: {
        "condition": params,
        "current": 1,
        "size": 10
      }
    })
  }
  componentWillReceiveProps(nextProps) {
    const { operation, product, resource } = nextProps;

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
      case resourceActions.GET_GETALLCITY_SUCCESS:
        this.setState({
          cityList: resource.cityList.result
        });
        break;
      case actions.GET_SELECTPAGE_SUCCESS:
        if( product.productPageList.code == 100){
          // let tempList  = product.productPageList.result.records.map(item => {
          //   item.children = item.productItems;
          //   return item;
          // });

          this.setState({
            tableData: product.productPageList.result,
            loading: false
          })
        }else{
          this.setState({
            loading: false
          })
        }
        
        break;
      case productGroupActions.GET_GROUPADDPRODUCT_SUCCESS:
        message.success("产品添加成功！", 10);
        this.setState({ disabled: false });
        window.history.go(-1);
        // var path = {
        //   pathname:'/goodsDetails',
        //   query:{id:params},
        //   }
        //   this.props.router.push(path);
        break;
      default:
    }
  }
  onEvent = (type, params) => {
    const { paramsLocal, pageSize,selectedRowKeysTemp } = this.state;
    switch (type) {
      // 查询
      case 'search':
        // params.serviceList = paramsLocal.serviceList;
        // params.giftList = paramsLocal.giftList;
        // params.statusType = 0;
        this.setState({
          filterParmas: params
        }, () => {
          this.onList({
            action: this.props.actions.selectPage,
            params: {
              "condition": { ...params, ...paramsLocal },
              "current": params.current?params.current:1,
              "size": params.pageSize?params.pageSize:10
            }
          })
        })
        break;
        case 'fenye':
          // params.serviceList = paramsLocal.serviceList;
          // params.giftList = paramsLocal.giftList;
          // params.statusType = 0;
          
            this.onList({
              action: this.props.actions.selectPage,
              params: {
                "condition": { ...params, ...paramsLocal },
                "current": params.current?params.current:1,
                "size": params.pageSize?params.pageSize:10
              }
          })
          break;
      case 'selectedRowKeys':
          let selectedRowKeys = [];
          params.forEach((value, key, map) =>{
           selectedRowKeys.push(key);
          });
        this.setState({ selectedRowKeys: selectedRowKeys,selectedRowKeysTemp:params });
        break;
      case 'submitChooseData':
        const groupparams = {};
        groupparams.productGroupId = this.state.itemProduct.id;
        let pruducts =[];
        let tempMap = new Map();
        selectedRowKeysTemp.forEach((value, key, map) =>{
          let productItemIds =[];

          let productId = value.id;
          if(tempMap.has(productId)){
            productItemIds =tempMap.get(productId); 
          }
          productItemIds.push(value.productItemId);
          tempMap.set(productId ,productItemIds );
         });
        
         tempMap.forEach((value, key, map) =>{
          let packProducts ={};
          packProducts.productId = key;
          packProducts.productItemIds = value;
          pruducts.push(packProducts);
         });
         groupparams.packProducts = pruducts;
        this.props.actions.groupAddProduct(groupparams);
         console.log(groupparams)
        this.setState({ disabled: true });
        break;
      case 'reback':
        window.history.go(-1);
        break;

      default:
    }
  }
  // 切换分页
  getList = params => {
    const { paramsLocal,  } = this.state;
    this.setState({
      pageSize: params.size,
    })
    this.onList({
      action: this.props.actions.selectPage,
      params: {
        "condition": { ...this.state.filterParmas, ...params.condition ,...paramsLocal},
        current: params.current,
        size: params.size,
      }
    })
  }
  onSelectChange = (selectedRowKeys) => {

  }
 
  handleChange = value => {
    this.props.onList({
      current: value.current,
      size: value.pageSize,
    })
  }
  render() {
    const { resourceType, loading, giftTypeList, tableData, selectedRowKeys, itemProduct, cityList, paramsLocal, disabled } = this.state;
    const pagination = {
      page: tableData.current,
      total: tableData.total,
      defaultSize: 10
    }

    // const hasSelected = selectedRowKeys ? selectedRowKeys.length > 0 :false;
    return (
      <Fragment><LocaleProvider locale={zh_CN}>
        <ProductInfoFilter cityList={cityList}
          resourceType={resourceType}
          itemProduct={itemProduct}
          giftTypeList={giftTypeList}
          onEvent={this.onEvent}
          disabled={disabled}
        /></LocaleProvider>
        {/* <Calculation cityList={cityList}
            resourceType={resourceType}
            giftTypeList={giftTypeList}
            onEvent={this.onEvent} /> */}
            
        <div>

          <div >
            <Alert message={
              <Fragment> 已选择 <a style={{ fontWeight: 600 }}>{selectedRowKeys.length}</a> 项&nbsp;&nbsp;，（仅处理本页数据，您可以修改页面展示数据来增加批量处理量，当页最大支持处理1000条数据！）
              </Fragment>} type="info" showIcon />
          </div>
        <LocaleProvider locale={zh_CN}>
          <ProductInfoList
            data={tableData}
            loading={loading}
            onEvent={this.onEvent}
            pagination={this.getPagination}
            onList={this.getList}
            paramsLocal={paramsLocal} 
            itemProduct={itemProduct}
          /></LocaleProvider>
        </div>
      </Fragment>
    );
  }
}

export default ProductInfo;