import React, { Fragment } from 'react';
import Filter from './compoent/filter';
import List from './compoent/list';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { bindActionCreators } from 'redux';
import * as actions from '../../store/productGroup/action';
import * as resourceActions from '../../store/resource/action';
import TableListBase from '../../base/table-list-base';
import {  message } from 'antd';
@connect(
  ({ operation, productGroup, resource }) => ({
    operation,
    productGroup: productGroup.toJS(),
    resource: resource.toJS()
  }),
  dispatch => ({
    actions: bindActionCreators({ ...actions, ...resourceActions }, dispatch),

  })
)
@withRouter
class ProductGroup extends TableListBase {
  constructor(props) {
    super(props);
    this.state = {
      resourceType: [],
      giftTypeList: [],
      tableData: {},
      filterParmas: {},
      channelList :[],
      pageSize: 10,
    };
  }
  componentDidMount() {
    this.props.actions.getGiftTypeList();
    this.props.actions.getResourceType();
    this.props.actions.channelSelectAll();
    this.onList({
      action: this.props.actions.getSelectGroupPageList,
      params: {
        "condition": {
        },
        "current": 1,
        "size": 10
      }
    })
  }
  componentWillReceiveProps(nextProps) {
    const { operation, productGroup, resource } = nextProps;
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
      case actions.GET_SELECTGROUPPAGELIST_SUCCESS:
      if(productGroup.productGroupList.code === 100){
        this.setState({
          tableData: productGroup.productGroupList.result,
          isTableLoading: false
        })
      }else{
        message.error(productGroup.productGroupList.msg,10);
        this.setState({
          isTableLoading: false
        })
      }
        
        break;
        case actions.GET_CHANNELSELECTALL_SUCCESS:  
        this.setState({
            channelList: productGroup.channelList.result
            });
        break; 
      default:
    }
  }
  onEvent = (type, params) => {
    const { pageSize } = this.state;

    switch (type) {
      case 'selectById':
          this.props.router.push(`/productGroupDetail/${params}`);
          break;
      // 查询
      case 'search':
        this.setState({
          filterParmas: params
        }, () => {
          this.onList({
            action: this.props.actions.getSelectGroupPageList,
            params: {
              "condition": params,
              "current": 1,
              "size": pageSize
            }
          })
        })
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
      action: this.props.actions.getSelectGroupPageList,
      params: {
        "condition": this.state.filterParmas,
        ...params
      }
    })
  }
  render() {
    const { resourceType, giftTypeList, tableData, isTableLoading,channelList  } = this.state;
    return (
      <Fragment>
        <Filter
          resourceType={resourceType}
          giftTypeList={giftTypeList}
          channelList ={channelList}
          onEvent={this.onEvent}
        />
        <List
          data={tableData}
          loading={isTableLoading}
          onEvent={this.onEvent}
          pagination={this.getPagination}
          onList={this.getList}
        />
      </Fragment>
    );
  }
}

export default ProductGroup;