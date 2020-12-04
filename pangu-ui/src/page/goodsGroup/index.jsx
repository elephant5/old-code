import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { bindActionCreators } from 'redux';
import { message, Collapse, Form, Tabs, Button, Icon } from 'antd';

// import * as actions from '../../store/resource/action';
import * as actions from '../../store/goods/action';
import GroupList from './component/groupList';
import GroupInfo from './component/groupInfo'
import AddGroupInfo from './component/addGroupInfo'

import './index.less'
import _ from 'lodash';
import cookie from 'react-cookies';
import GroupInfoTable from './component/groupInfoTable';
@connect(
  ({ operation, goods }) => ({
    operation,
    goods: goods.toJS()
  }),
  dispatch => ({
    actions: bindActionCreators(actions, dispatch)
  })
)
@withRouter
@Form.create()
class GoodsGroup extends Component {
  constructor(props) {
    super(props);
    this.state = {
      addDiv: false,
      editDiv: false,
      productGroupList: [],
      groupDetail: {},
      productGroupUpdate: {},
      cityList: [],
    };
  }
  componentDidMount() {
    const { goodsId } = this.props;
    this.props.actions.selectGoodsGroup(goodsId);
    this.props.actions.getAllCity();

  }

  componentWillReceiveProps(nextProps) {
    const { operation, goods } = nextProps;
    const { productGroupList } = this.state;
    switch (operation.type) {
      case actions.GET_PRODUCTGROUP_SUCCESS:
        message.success('产品组新增成功！', 5);
        const temp = goods.productGroup.result;
        const tempproductGroupList = [...productGroupList, temp];
        this.setState({
          addDiv: false,
          productGroupList: tempproductGroupList,
        });

        break;
      case actions.GET_PRODUCTGROUPUPDATE_SUCCESS:
        message.success('产品组修改成功！', 5);
        const temp2 = goods.productGroupUpdate.result;
        // const tempList = this.state.productGroupList.filter(item => {return item.id !== temp2.id});
        const tempList = productGroupList.map(item => {
          goods.productGroupUpdate.result.isShow = false;
          if (item.id === temp2.id) {
            temp2.groupProductList = item.groupProductList;
            temp2.groupProductListIds = item.groupProductListIds;
            temp2.blockRule = item.blockRule;
            temp2.blockRuleList = item.blockRuleList;
            return temp2;
          } else {
            return item;
          }
        });
        this.setState({
          productGroupList: tempList,
          // productGroupList:tempList,
          editDiv: false,
          // addDiv: false,
        }, () => {
        });
        break;
      case actions.GET_PRODUCTGROUPSELECTPAGE_SUCCESS:

        goods.productGroupList.result.map(item => {
          item.isShow = false;
          return item;
        });
        this.setState({
          productGroupList: goods.productGroupList.result
        });

        break;
      case actions.GET_GETALLCITY_SUCCESS:
        this.setState({
          cityList: goods.cityList.result
        });

        break;

      case actions.GET_PRODUCTGROUPGROUPDETAIL_SUCCESS:
        this.setState({
          groupDetail: goods.groupDetail.result,
          addDiv: false,
          editDiv: true,
        });
        break;

      case actions.GET_PRODUCTGROUPDELETE_SUCCESS:
        if (goods.groupDelete.code === 100) {
          message.success('产品组删除成功！', 5);
          this.setState({
            productGroupList: this.state.productGroupList.filter(item => item.id !== goods.groupDelete.result),
            editDiv: false,
            addDiv: false,
          });
        } else {
          message.error(goods.groupDelete.msg, 5);
        }
        break;

      default:
        break;
    }
  }

  onEvent = (type, params) => {
    const { productGroupList } = this.state;
    switch (type) {
      case 'displayDiv':
        this.setState({
          addDiv: false,
          editDiv: false,
        });
        break;
      case 'productGroupSave':
        this.props.actions.productGroupSave(params);
        break;
      case 'groupEdit':
        this.props.actions.groupfindOneById(params.id);
        break;
      case 'groupDelete':
        this.props.actions.productGroupDelete(params.id);
        break;
      case 'productGroupUpdate':
        this.props.actions.productGroupUpdate(params);
        break;
      case 'uploadFile':
        this.props.actions.updateFile(params)
        break;
      case 'copyProductGroup':
        var path = {
          pathname: '/productGroupCopy',
          query: params,

        }
        this.props.router.push(path);
        break;
      case 'delProduct':
        productGroupList.map(item => {
          if (item.id === params.id) {
            params.selectedRowKeys.map(ids => {
              item.groupProductList.filter(pro => { return pro.id !== ids });
            })

          }
          return item;
        });
        this.setState({
          productGroupList: productGroupList,
        });
        break;
      default:
    }
  }
  displayDiv = () => {
    this.setState({
      addDiv: true,
      editDiv: false,
    });
    this.props.form.resetFields();
  }
  divChange = (params) => {
    const { productGroupList } = this.state;
    productGroupList.map(item => {
      if (item.id === params.id) {
        if (params.isShow === true) {
          item.isShow = false;
        } else {
          item.isShow = true;
        }
      } else {
        item.isShow = false;
      }
      return item;
    });
    this.setState({
      productGroupList: productGroupList,
    });
  }


  render() {
    const { addDiv, productGroupList, groupDetail, cityList, editDiv } = this.state;
    const { serviceTypeList, giftTypeList, goodsId, sysDict, festivalList, shopTypeList } = this.props;
    const isShow = productGroupList.length > 0 ? true : false;
    const edit = cookie.load("KLF_PG_GM_GL_EDIT");
    let params = {};
    params.goodsId = goodsId;
    if (isShow) {
      let ids = [];
      productGroupList.map(item => {
        ids.push(item.id);
      });
      params.ids = ids;
      params.groupIds = ids;

    }
    return (
      <Fragment>


        {isShow && productGroupList.map(item => {
          return (
            <div key={'groupkey' + item.id}>
              <Collapse bordered={false} accordion defaultActiveKey={['a1']} key={'Collapsekey' + item.id}>
                <div key={'Collapsekeychild' + item.id}>
                  <GroupList data={{ ...this.state }} sysDict={sysDict} key={item.id}
                    product={item}
                    giftTypeList={giftTypeList}
                    serviceTypeList={serviceTypeList}
                    goodsId={goodsId}
                    onEvent={this.onEvent} festivalList={festivalList} />
                </div>
                <div className="ant-collapse-header" style={{ textAlign: 'center', backgroundColor: '#d9d9d9' }} onClick={() => this.divChange(item)}>
                  {item.isShow ? <Icon type="caret-down" /> : <Icon type="caret-up" />} <b>产品列表</b>&nbsp;&nbsp;（包含产品{item.groupProductList ? item.groupProductList.length : "0"}个）
                                </div>
                {item.isShow && <div >

                  <GroupInfoTable data={{ ...this.state }} cityList={cityList} onEvent={this.onEvent} festivalList={festivalList} goodsId={goodsId} itemProduct={item} ></GroupInfoTable>
                </div>}

              </Collapse>
            </div>

          )
        })}
        <div className="c-modal" style={{ marginBottom: '0px' }}>
          {editDiv && <GroupInfo data={{ ...this.state }} onCancel={this.displayDiv} onEvent={this.onEvent} shopTypeList={shopTypeList} productGroupList={productGroupList} groupDetail={groupDetail} sysDict={sysDict} goodsId={goodsId}
          />}
          {addDiv && <AddGroupInfo data={{ ...this.state }} onCancel={this.displayDiv} onEvent={this.onEvent} sysDict={sysDict} shopTypeList={shopTypeList} goodsId={goodsId}
          />}
        </div>
        <div className="c-modal" style={{ marginBottom: '0px' }}>
          {edit && <Button type="dashed" block size='large' style={{ fontSize: '24px', height: '80px', borderWidth: '2px' }} onClick={this.displayDiv}>+ 新增产品组</Button>}
        </div>

        <div className="c-modal" style={{ marginBottom: '0px' }}>
          {edit && <Button type="dashed" block htmlType={'button'} size='large' style={{ fontSize: '24px', height: '80px', borderWidth: '2px' }} onClick={() => this.onEvent('copyProductGroup', params)} >+ 复制产品组</Button>}
        </div>
      </Fragment>
    );
  }
}
export default GoodsGroup;