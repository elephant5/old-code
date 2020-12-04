import React, { Fragment } from 'react';
import BuffetFilter from './compoent/buffetFilter';
import AccomFilter from './compoent/accomFilter';
import Calculation from './compoent/calculation';
import { Tabs } from 'antd';
import BuffetList from './compoent/buffetList';
import AccomList from './compoent/accomList';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { bindActionCreators } from 'redux';
import * as actions from '../../store/product/action';
import * as resourceActions from '../../store/resource/action';
import TableListBase from '../../base/table-list-base';
import { message } from 'antd';
import './index.less'
const TabPane = Tabs.TabPane;

@connect(
  ({ operation, product, resource }) => ({
    operation,
    product: product.toJS(),
    resource: resource.toJS()
  }),
  dispatch => ({
    actions: bindActionCreators({ ...actions, ...resourceActions }, dispatch),

  })
)
@withRouter
class Product extends TableListBase {
  constructor(props) {
    super(props);
    this.state = {
      resourceType: [],
      giftTypeList: [],
      accomTableData: {},
      buffetTableData: {},
      accomFilterParmas: { "service": ['住宿'] },
      buffetFilterParmas: { "service": ['自助餐'] },
      cityList: [],
      accomPageSize: 10,
      buffetPageSize: 10,
      TabDisabled: true,
      selectedAccomRowKeys: [],
      selectedBuffetRowKeys: [],
    };
  }
  componentDidMount() {
    this.props.actions.getGiftTypeList();
    this.props.actions.getResourceType();
    this.props.actions.getAllCity();
    this.onList({
      action: this.props.actions.selectPage,
      params: {
        "condition": {
          "service": ["自助餐"]
        },
        "current": 1,
        "size": 10
      }
    });
    this.onList({
      action: this.props.actions.selectPage,
      params: {
        "condition": {
          "service": ["住宿"]
        },
        "current": 1,
        "size": 10
      }
    });
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
      case actions.GET_SELECTPAGE_SUCCESS:
        if (product.productPageList.result.records && product.productPageList.result.records.length > 0) {
          if (product.productPageList.result.records[0].service.indexOf('住宿') > -1) {
            this.setState({
              accomTableData: product.productPageList.result,
              isTableLoading: false
            })
          } else {
            this.setState({
              buffetTableData: product.productPageList.result,
              isTableLoading: false
            })
          }
        } else {
          this.setState({
            buffetTableData: product.productPageList.result,
            isTableLoading: false
          })
        }
        break;
      case resourceActions.GET_GETALLCITY_SUCCESS:
        this.setState({
          cityList: resource.cityList.result
        });
        break;
      default:
        break;
    }
  }
  onEvent = (type, params) => {
    const { accomPageSize, buffetPageSize } = this.state;

    switch (type) {
      // 住宿查询
      case 'accomSearch':
        this.setState({
          accomFilterParmas: params
        }, () => {
          this.onList({
            action: this.props.actions.selectPage,
            params: {
              "condition": params,
              "current": 1,
              "size": accomPageSize
            }
          })
        })
        break;
      // 非住宿查询
      case 'buffetSearch':
        this.setState({
          buffetFilterParmas: params
        }, () => {
          this.onList({
            action: this.props.actions.selectPage,
            params: {
              "condition": params,
              "current": 1,
              "size": buffetPageSize
            }
          })
        })
        break;
      //住宿导出
      case 'accomExport':
        this.setState({
          accomFilterParmas: params
        }, () => {
          params.productItemIds = this.state.selectedAccomRowKeys;
          let param = {
            "condition": params,
            "current": 1,
            "size": accomPageSize
          }
          this.props.actions.selectExport(param).then((data) => {
            const { product } = this.props;
            if (product.selectExportRes.code == 100) {
              message.info("邮件已发送至您的个人邮箱，请注意查收");
            } else {
              message.error(product.selectExportRes.msg);
            }
          }).catch((error) => {
            message.error("系统出错")
          });
        })
        break;
      //非住宿导出
      case 'buffetExport':
        this.setState({
          buffetFilterParmas: params
        }, () => {
          params.productItemIds = this.state.selectedBuffetRowKeys;
          let param = {
            "condition": params,
            "current": 1,
            "size": buffetPageSize
          }
          this.props.actions.selectExport(param).then((data) => {
            const { product } = this.props;
            if (product.selectExportRes.code == 100) {
              message.info("邮件已发送至您的个人邮箱，请注意查收");
            } else {
              message.error(product.selectExportRes.msg);
            }
          }).catch((error) => {
            message.error("系统出错")
          });
        })
        break;
      //住宿复选
      case 'selectedAccomRowKeys':
        let selectedAccomRowKeys = [];
        params.forEach((value, key, map) => {
          selectedAccomRowKeys.push(key);
        });
        this.setState({ selectedAccomRowKeys: selectedAccomRowKeys });
        break;
      //非住宿复选
      case 'selectedBuffetRowKeys':
          let selectedBuffetRowKeys = [];
          params.forEach((value, key, map) => {
            selectedBuffetRowKeys.push(key);
          });
          this.setState({ selectedBuffetRowKeys: selectedBuffetRowKeys });
        break;
      default:
    }
  }
  // 住宿切换分页
  getAccomList = params => {
    this.setState({
      accomPageSize: params.size,
    })
    this.onList({
      action: this.props.actions.selectPage,
      params: {
        "condition": this.state.accomFilterParmas,
        ...params
      }
    })
  }
  // 非住宿切换分页
  getBuffetList = params => {
    this.setState({
      buffetPageSize: params.size,
    })
    this.onList({
      action: this.props.actions.selectPage,
      params: {
        "condition": this.state.buffetFilterParmas,
        ...params
      }
    })
  }
  render() {
    const { resourceType, giftTypeList, accomTableData, buffetTableData, isTableLoading, cityList } = this.state;
    return (
      <Fragment>
        <Tabs defaultActiveKey="1">
          <TabPane tab="住宿产品" key="1">
            <AccomFilter cityList={cityList}
              resourceType={resourceType}
              giftTypeList={giftTypeList}
              onEvent={this.onEvent}
            />
            {/* <Calculation cityList={cityList}
            resourceType={resourceType}
            giftTypeList={giftTypeList}
            onEvent={this.onEvent} /> */}
            <AccomList
              data={accomTableData}
              loading={isTableLoading}
              onEvent={this.onEvent}
              pagination={this.getPagination}
              onList={this.getAccomList}
            />
          </TabPane>

          <TabPane tab="非住宿产品" key="2">
            <BuffetFilter cityList={cityList}
              resourceType={resourceType}
              giftTypeList={giftTypeList}
              onEvent={this.onEvent}
            />
            {/* <Calculation cityList={cityList}
            resourceType={resourceType}
            giftTypeList={giftTypeList}
            onEvent={this.onEvent} /> */}
            <BuffetList
              data={buffetTableData}
              loading={isTableLoading}
              onEvent={this.onEvent}
              pagination={this.getPagination}
              onList={this.getBuffetList}
            />
          </TabPane>
        </Tabs>
      </Fragment>
    );
  }
}

export default Product;