import React, { Fragment } from 'react';
import ProductGroupCopyFilter from './compoent/filter';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { bindActionCreators } from 'redux';
import * as actions from '../../store/productGroup/action';
import * as resourceActions from '../../store/resource/action';
import * as productGroupActions from '../../store/productGroup/action';
import TableListBase from '../../base/table-list-base';
import { Table, Divider, Tag, message,Alert } from 'antd';
import { resourColor , serviceColor,giftColor} from '../../util/dictType.js'
import ProductGroupInfoList from './compoent/list';
@connect(
  ({ operation, productGroup, resource }) => ({
    operation,
    productGroup: productGroup.toJS(),
    resource: resource.toJS()
  }),
  dispatch => ({
    actions: bindActionCreators({ ...actions, ...resourceActions,...productGroupActions }, dispatch),

  })
)
@withRouter
class ProductGroupCopy extends TableListBase {
  constructor(props) {
    super(props);
    this.state = {
      resourceType: [],
      giftTypeList: [],
      tableData: {},
      filterParmas: {},
      selectedRowKeys:[],
      returnParams:{},
      pageSize: 10,
    };
  }
  componentDidMount() {
    this.props.actions.getGiftTypeList();
    this.props.actions.getResourceType();
    this.props.actions.channelSelectAll();
    this.setState({
      returnParams: this.props.location.query
    })
    this.onList({
      action: this.props.actions.getSelectGroupPageList,
      params: {
        "condition": this.props.location.query,
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
        case actions.GET_COPYGROUP_SUCCESS:
        message.success("复制产品组成功！",10)
        window.history.go(-1);
        break;
        
      default:
    }
  }
  getPaginations = ({page, total, defaultSize = 10}) => {
    return {
        current: page,
        total: total,
        defaultPageSize: defaultSize,
        showSizeChanger: true,
        showQuickJumper: true,
        pageSizeOptions: [10,20,50,100,200,500,1000],
        showTotal: (total, range) => <div>本页显示 <span className="c-color-blue">{range[1]-range[0] + 1}</span> 条数据</div>,
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
        case 'selectedRowKeys':
          this.setState({selectedRowKeys:params});
        break;
        case 'submitChooseData':
          const paramsMap = {};
          paramsMap.goodsId = this.state.returnParams.goodsId;
          paramsMap.groupIds = this.state.selectedRowKeys;
          this.props.actions.copyGroup(paramsMap);
        break;
      case 'reback':
          window.history.go(-1);
        break;
      default:
    }
  }
  // 切换分页
  getList = params => {
    this.setState({
      pageSize: params.size
    })
    this.onList({
      action: this.props.actions.getSelectGroupPageList,
      params: {
        "condition": this.state.filterParmas,
        ...params
      }
    })
  }

  
 
handleChange = value => {
  this.props.onList({
      current: value.current,
      size: value.pageSize,
  })
}
// onSelectChange = (selectedRowKeys) => {
//   this.setState({ selectedRowKeys });
// }
  render() {
    const { resourceType, giftTypeList, tableData, selectedRowKeys,loading,channelList } = this.state;
    const pagination = {
      page: tableData.current,
      total: tableData.total,
      defaultSize: 10
    }
    
    return (
      <Fragment>
        <ProductGroupCopyFilter
          resourceType={resourceType}
          giftTypeList={giftTypeList} 
          channelList ={channelList}
          onEvent={this.onEvent}
        />
        {/* <List
          data={tableData}
          loading={isTableLoading}
          onEvent={this.onEvent}
          pagination={this.getPagination}
          onList={this.getList}
        /> */}
          <div >
          <Alert  message={   <Fragment> 已选择 <a style={{ fontWeight: 600 }}>{selectedRowKeys.length}</a> 项！&nbsp;&nbsp;
              </Fragment> }  type="info"  showIcon  />
        </div>
        {/* <Table  rowSelection={rowSelection}  
            columns={this.getColumns()} 
            dataSource={tableData.records} 
            pagination={this.getPaginations(pagination)}
            rowKey="id"
            loading={loading}
            onChange={this.handleChange} 
            onList={this.getList} 
        /> */}
        <ProductGroupInfoList
          data={tableData}
          loading={loading}
          onEvent={this.onEvent}
          onChange={this.handleChange}
          pagination={this.getPagination}
          onList={this.getList}
        />
        
      </Fragment>
    );
  }
}

export default ProductGroupCopy;