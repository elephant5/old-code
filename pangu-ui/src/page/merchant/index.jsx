import React, { Fragment } from 'react';
import TableListBase from '../../base/table-list-base';
import Filter from './component/filter';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { bindActionCreators } from 'redux';
import * as actions from '../../store/resource/action';
import List from './component/list';

@connect(
    ({operation, resource}) => ({
        operation,
        resource: resource.toJS()
    }),
    dispatch => ({ 
        actions: bindActionCreators(actions, dispatch) 
    })
)
@withRouter
class Merchant extends TableListBase {
  constructor(props) {
    super(props);
    this.state = { 
      resourceTypes: [],
      tableData: {},
      filterParams: {},
      cityList:[],
      pageSize: 10,
        };
  }

  componentDidMount() {
    // 获取资源类型
    this.props.actions.getResourceType();
    this.props.actions.getAllCity();
    // 商户列表
    this.onList({
      action: this.props.actions.getSelectShopPageList,
      params: {
        "condition": {},
        "current": 1,
        "size": 10
      }
    })
  }

  componentWillReceiveProps(nextProps){
    const { operation, resource } = nextProps;
    switch(operation.type){
      case actions.GET_RESOURCETYPE_SUCCESS:
          this.setState({
            resourceTypes: resource.resourceType.result
          })
        break;
      case actions.GET_SELECTSHOPPAGELIST_SUCCESS:
          this.setState({
            tableData: resource.merchantList.result,
            isTableLoading: false
          })
          break;
          case actions.GET_GETALLCITY_SUCCESS:
          this.setState({
            cityList: resource.cityList.result
         });  
         break;
      default:
        break;
    }
  }

  onEvent = (type, params) => {
    const { pageSize } = this.state;
    switch(type){
        case 'addMerchant':
            this.props.router.push('/addone');
          break;
        case 'edit':
            this.props.router.push(`/addtwo/${params}`);
          break;
          // 查询
        case 'search':
            this.setState({
              filterParams: params
            }, () => {
              this.onList({
                action: this.props.actions.getSelectShopPageList,
                params: {
                  "condition": params,
                  "current": 1,
                  "size": pageSize
                }
              })
            })
          break;
        default:
          break;
    }
  }

  // 切换分页
  handleChange = params => {
    this.setState({
      pageSize: params.size,
    })
    this.onList({
      action: this.props.actions.getSelectShopPageList,
      params: {
        "condition": this.state.filterParams,
        ...params
      }
    })
  }
  render() {
    const { resourceTypes, tableData, isTableLoading,cityList } = this.state;
    return (
        <Fragment>
            <Filter 
              data={resourceTypes}
              cityList={cityList}
              onEvent={this.onEvent}
            />
            <List
              loading={isTableLoading}
              data={tableData}
              pagination={this.getPagination}
              onEvent={this.onEvent}
              onChange={this.handleChange}
            />
        </Fragment>
    );
  }
}

export default Merchant;