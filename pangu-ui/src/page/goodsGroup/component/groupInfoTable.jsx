import React, { Fragment, Component } from 'react';
import { connect } from 'react-redux';
import { withRouter, Link } from 'react-router';
import { bindActionCreators } from 'redux';
import * as actions from '../../../store/productGroup/action';
import * as resourceActions from '../../../store/resource/action';
import * as productActions from '../../../store/product/action';
import * as productGroupActions from '../../../store/productGroup/action';
import { Form, Input, Button, Select, Row, Col, Table, message, InputNumber, Popconfirm, Tag, Alert, Badge, Modal, Icon } from 'antd';
import { DragDropContext, DragSource, DropTarget, DragDropContextProvider } from 'react-dnd';
import HTML5Backend from 'react-dnd-html5-backend';
import update from 'immutability-helper';
import AddModal from '../../merchant/component/add-modal';
import EditModal from '../../merchant/component/edit-modal';
import EditProductModal from '../component/editProduct-modal';
import TableListBase from '../../../base/table-list-base';
import { unique } from '../../../util/index';
import iconFont from '../../../util/iconfont.js'
import cookie from 'react-cookies';
const IconFont = Icon.createFromIconfontCN({
  scriptUrl: iconFont
});
const { Option } = Select;
const FormItem = Form.Item;
const EditableContext = React.createContext();
let dragingIndex = -1;
const status = ['在售', '停售', '申请停售'];
const statusMap = ['success', 'error', 'processing'];
class BodyRow extends Component {
  render() {
    const {
      isOver,
      connectDragSource,
      connectDropTarget,
      moveRow,
      ...restProps
    } = this.props;
    const style = { ...restProps.style, cursor: 'move' };

    // let className = restProps.className;
    if (isOver) {
      if (restProps.index > dragingIndex) {
        // className += ' drop-over-downward';
        style.borderBottom = ' 2px dashed #1890ff'
      }
      if (restProps.index < dragingIndex) {
        // className += ' drop-over-upward';
        style.borderTop = ' 2px dashed #1890ff'
      }
    }

    return connectDragSource(
      connectDropTarget(
        <tr
          {...restProps}
          // className={className}
          style={style}
        />
      )
    );
  }
}
const rowSource = {
  beginDrag(props) {
    dragingIndex = props.index;
    return {
      index: props.index,
    };
  },
};
const rowTarget = {
  drop(props, monitor) {
    const dragIndex = monitor.getItem().index;
    const hoverIndex = props.index;

    // Don't replace items with themselves
    if (dragIndex === hoverIndex) {
      return;
    }

    // Time to actually perform the action
    props.moveRow(dragIndex, hoverIndex);

    // Note: we're mutating the monitor item here!
    // Generally it's better to avoid mutations,
    // but it's good here for the sake of performance
    // to avoid expensive index searches.
    monitor.getItem().index = hoverIndex;
  },
};
const DragableBodyRow = DropTarget(
  'row',
  rowTarget,
  (connect, monitor) => ({
    connectDropTarget: connect.dropTarget(),
    isOver: monitor.isOver(),
  }),
)(
  DragSource(
    'row',
    rowSource,
    (connect) => ({
      connectDragSource: connect.dragSource(),
    }),
  )(BodyRow),
);

// const EditableFormRow = Form.create()(EditableRow);
class EditableCell extends Component {
  constructor(props) {
    super(props);
    this.state = {
      tempRecord: { id: '123123' }
    }
  }
  getInput = (params, form) => {
    const { getFieldDecorator } = form;
    const { editing, dataIndex, title, inputType, record, index, addBlock, onClose, } = this.props;
    if (this.props.inputType === 'number') {

      return <FormItem style={{ margin: 0 }}>
        {getFieldDecorator(dataIndex, {
          rules: [{
            required: true,
            message: `请输入 ${title}!`,
          }],
          initialValue: record[dataIndex],
        })(<InputNumber />)
        }
      </FormItem>;
    }
    if (params.blockRuleList) {

      return (<div> <Button type="primary" ghost onClick={() => addBlock(record)}>+ 添加Block</Button> &nbsp;&nbsp;&nbsp;
      {
          params.blockRuleList.map(item => (<Tag closable
            key={item.rule}
            onClose={e => e.stopPropagation()}
            afterClose={() => onClose(item, params)}
          // onClick={() => editBlock(item,params)}
          >{item.natural}</Tag>
          ))}
      </div>);
    } else {
      return (<Button type="primary" ghost onClick={() => addBlock(record)}>+ 添加Block</Button>);
    }

  };

  render() {
    const { editing, dataIndex, title, inputType, record, index, addBlock, ...restProps } = this.props;
    return (
      <EditableContext.Consumer>
        {(form) => {
          return (
            <td {...restProps}>
              {editing ? (
                this.getInput(record, form)
              ) : restProps.children

              }

            </td>
          );
        }}
      </EditableContext.Consumer>
    );
  }
}

@Form.create()
@connect(
  ({ operation, productGroup, resource, product }) => ({
    operation,
    productGroup: productGroup.toJS(),
    resource: resource.toJS(),
    product: product.toJS(),
  }),
  dispatch => ({
    actions: bindActionCreators({ ...actions, ...resourceActions, ...productActions, ...productGroupActions }, dispatch),

  })
)
@withRouter
class GroupInfoTable extends TableListBase {
  constructor(props) {
    super(props);
    this.state = {
      resourceType: [],
      giftTypeList: [],
      tableData: {},
      filterParmas: {},
      itemProduct: {},
      loading: false,
      selectedRowKeys: [],
      editingKey: '',
      groupProductList: [],
      isShowEditModal: false,
      isShowAddModal: false,
      tempRecord: {},
      blockRuleList: [],
      oldBlockRuleList: [],
      visible: false,
      disabled: false,
      record: {},
      pageSize: 10,
      editProductVisible: false,
      editProductDistroy: false,
      productDetail: {},
    };
  }

  componentDidMount() {
    // this.props.actions.getGiftTypeList();
    // this.props.actions.getResourceType();
    const { itemProduct } = this.props;
    this.setState({
      itemProduct: itemProduct,
      groupProductList: itemProduct.groupProductList
    });

  }
  componentWillReceiveProps(nextProps) {
    const { operation, productGroup, resource } = nextProps;
    const { selectedRowKeys, loading, itemProduct, groupProductList } = this.state;
    const { onEvent } = this.props;
    switch (operation.type) {
      case actions.GET_GROUPDELPRODUCT_SUCCESS:
        message.success("移除产品组的产品成功", 10);
        // const tempList = groupProductList;//itemProduct.groupProductList;
        // let addList = [];
        // if(tempList && selectedRowKeys.length > 0){
        //   tempList.map(item =>{
        //     selectedRowKeys.map(rows =>{
        //         if(rows !=  item.id){
        //           addList.push(item);
        //         }
        //     })
        //   });
        // }
        const tempList = groupProductList.filter(item => selectedRowKeys.indexOf(item.id) === -1)
        itemProduct.groupProductList = tempList;
        // groupProductList = addList;
        this.setState({
          itemProduct: itemProduct,
          groupProductList: tempList,
          selectedRowKeys: [],
          loading: false
        }, () => {
          // onEvent('delProduct',{selectedRowKeys:selectedRowKeys,id:itemProduct.id});
        })
        this.setState({ disabled: false });
        break;
      case actions.GET_QUERYGROUPPRODUCT_SUCCESS:
        this.setState({
          // itemProduct:itemProduct,
          groupProductList: productGroup.queryGroupProduct.result
        });
        break;
      case actions.GET_UPDATESTATUS_SUCCESS:
        const updateStatus = productGroup.updateStatus.result;
        if (groupProductList) {
          groupProductList.map(item => {
            if (item.id === updateStatus.id) {
              item.status = 1
            }
            return item;
          });
        }
        this.setState({
          // itemProduct:itemProduct,
          groupProductList: groupProductList
        });
        break;
      case actions.GET_UPDATERECOMMEND_SUCCESS:
        const updateRecommend = productGroup.updateRecommend.result;
        if (groupProductList) {
          groupProductList.map(item => {
            if (item.id === updateRecommend.id) {
              item.recommend = updateRecommend.recommend
            }
            return item;
          });
        }
        this.setState({
          groupProductList: groupProductList
        });
        break;
      default:
        break;
    }
  }
  onEvent = (type, params) => {
    const { pageSize } = this.state;
    switch (type) {
      case 'addProduct':
        this.props.actions.findByGroupId(this.state.itemProduct.id).then((date)=>{
          const {productGroup} = this.props;
          if(productGroup.findByGroupIdRes.code == 100){
            let queryParam = this.state.itemProduct;
            queryParam.groupProductListIds = productGroup.findByGroupIdRes.result.groupProductListIds;
            var path = {
              pathname: '/productInfo',
              query: queryParam,
    
            }
            this.props.router.push(path);
          }else{
            message.error("系统错误")
          }
        }).catch((error)=>{
          message.error("系统错误")
        })
        break;
      case 'delProduct':
        const params = {};
        params.productGroupId = this.state.itemProduct.id;
        params.productGroupProductIds = this.state.selectedRowKeys;
        this.props.actions.groupDelProduct(params);
        this.setState({ disabled: true });
        break;
      case 'editProduct':

        break;
      // 查询
      case 'saveSort':


        break;
      // 保存
      case 'addProductBlock':

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
      action: this.props.actions.queryGroupProduct,
      params: {
        "condition": this.state.filterParmas,
        ...params
      }
    })
  }


  getColumns = () => {
    return [{
      title: '产品ID',
      dataIndex: 'productId',
    }, {
      title: '推荐产品',
      dataIndex: 'recommend',
      render: (text, record) => {
        if (text === 0) {
          return <a onClick={() => this.updateRecommendSet(record)}><IconFont type='iconaixin2' /></a>;
        } else if (text === 1) {
          return <a onClick={() => this.updateRecommendSet(record)}><IconFont type='iconaixin1-copy' /></a>;
        } else {
          return <a onClick={() => this.updateRecommendSet(record)}><IconFont type='iconaixin2' /></a>;
        }

      },
    }, {
      title: '城市',
      dataIndex: 'cityName',
    }, {
      title: '酒店|商户',
      render: (text, row, index) => {
        return (<Link to={{ pathname: "/addtwo/" + row.shopId, query: {} }} target="_blank" className="fontBlue fontWeight" activeStyle={{ color: 'red' }}>

          {row.hotelName} | {row.shopName} </Link>);
      },
    },
    {
      title: '资源类型',
      dataIndex: 'service',
    },
    {
      title: '权益类型',
      dataIndex: 'gift',
    },
    {
      title: '资源',
      dataIndex: 'productName',
      render: (text, row, index) => {
        // console.log("productName")
        // console.log(text)
        // console.log(row.productName)
        // console.log(row.serviceCode)
        // console.log(row.needs)
        // console.log(row.addon)
        let productName = row.productName;
        if (row.service === '住宿') {
          if (row.needs) {
            productName = productName + ' | ' + row.needs
          }
          if (row.addon) {
            productName = productName + ' | ' + row.addon
          }
        }
        return productName;
      }
    },
    {
      title: '状态',
      dataIndex: 'status',
      render: (text, record) => {
        if (text === '2') {
          return (<a onClick={() => this.showModal(record)}><Badge status={statusMap[text]} text={status[text]} /></a>);
        } else {
          return (<Badge status={statusMap[text]} text={status[text]} />);
        }

      }
    },
    {
      title: '最高价',
      dataIndex: 'maxCost',
    },

    {
      title: '最低价',
      dataIndex: 'minCost',

    },
    {
      title: '预约支付金额',
      dataIndex: 'bookPayment',
    },
    {
      title: 'Block',
      dataIndex: 'blockNatural',
      key: 'blockNatural',
      editable: true,
      render: (text, record) => {
        let temp = '';
        if (record.blockRuleList) {
          record.blockRuleList.map(item => {
            temp = temp + item.natural + ' ';

          })
        }
        return temp;
      }
    },

    {
      title: '排序',
      dataIndex: 'sort',
      key: 'sort',
      editable: true,

    },

    ]
  }

  showModal = (record) => {
    this.setState({
      visible: true,
      record: record,
    });
  }
  updateRecommendSet = (row) => {
    row.recommend = row.recommend === 1 ? 0 : 1;
    // console.log(row)
    this.props.actions.updateRecommend(row);
  }
  handleOk = (e) => {
    this.props.actions.updateStatus(this.state.record);
    this.setState({
      visible: false,
    });
  }

  handleCancel = (e) => {
    this.setState({
      visible: false,
      record: {},
    });
  }
  isEditing = record => record.id === this.state.editingKey;
  cancel = (value) => {
    const { groupProductList, oldBlockRuleList } = this.state;
    if (groupProductList) {
      groupProductList.map(item => {
        if (item.id === value) {
          item.blockRuleList = oldBlockRuleList;
        }
        return item;
      });
    }
    this.setState({ editingKey: '', groupProductList: groupProductList });

  };
  save(form, key, values) {
    const { groupProductList, oldBlockRuleList } = this.state;
    let blockRuleList = [];
    if (groupProductList) {
      groupProductList.map(item => {
        if (item.id === values.id) {
          blockRuleList = item.blockRuleList;
        }
      });
    }

    form.validateFields((error, row) => {
      if (error) {
        message.error("请输入完整信息后保存！", 10)
        return;
      }
      const newData = [...this.state.groupProductList];
      const index = newData.findIndex(item => key === item.id);
      if (index > -1) {
        const item = newData[index];
        newData.splice(index, 1, {
          ...item,
          ...row,
        });

        this.setState({ groupProductList: newData, editingKey: '' });
        row.productGroupProductId = values.id;
        row.blockRuleList = blockRuleList;

        this.props.actions.groupEditProduct(row);
      } else {
        newData.push(row);
        this.setState({ groupProductList: newData, editingKey: '' });
      }
    });
  }

  /**编辑 */
  edit(key) {
    this.setState({ editProductVisible: true, productDetail: key });
  }
  onSelectChange = (selectedRowKeys) => {
    this.setState({ selectedRowKeys });
  }


  // 关闭block框
  onCancel = () => {
    this.setState({
      isShowAddModal: false
    })
  }
  onEditCancel = () => {
    this.setState({
      isShowEditModal: false
    })
  }

  onOk = params => {
    const { blockRuleList, tempRecord, groupProductList } = this.state;

    let rules = [];
    if (blockRuleList) {
      rules = [...this.state.blockRuleList, ...params.festival, ...params.blockRule];
    } else {
      rules = [...params.festival, ...params.blockRule];
    }
    const norepeatRule = unique(rules);
    let tempList = [];
    if (groupProductList) {
      groupProductList.map(item => {
        if (item.id === tempRecord.id) {
          tempList = item.blockRuleList;
          item.blockRuleList = norepeatRule;

        }
        return item;
      })
    }
    this.setState({
      groupProductList: groupProductList,
      isShowAddModal: false,
      blockRuleList: [],
      oldBlockRuleList: tempList,
      tempRecord: {}
    })
  }
  // 编辑block保存
  handleEdit = params => {
    const { blockRuleList, tempRecord, groupProductList } = this.state;
    // 先删除当前条，在覆盖
    const tempList = blockRuleList.filter(item => {
      return item.rule !== params.deleteData.rule
    });
    const rules = [...tempList, ...params.festival, ...params.blockRule];

    if (groupProductList) {
      groupProductList.map(item => {
        if (item.id === tempRecord.id) {
          item.blockRuleList = rules;
        }
        return item;
      })
    }
    this.setState({
      groupProductList: groupProductList,
      isShowEditModal: false,
      blockRuleList: [],
      tempRecord: {}
    })
    // this.setState({
    //     blockRuleList: this.state.blockRuleList.filter(item => item.rule !== params.deleteData.rule)
    // }, () => {
    //     const rules = [...this.state.blockRuleList, ...params.festival, ...params.blockRule];
    //     const norepeatRule = unique(rules);
    //     this.setState({
    //         blockRuleList: norepeatRule,
    //         isShowEditModal: false
    //     })
    // })
  }
  // 弹出新增block框
  addBlock = (record) => {
    this.setState({
      blockRuleList: record.blockRuleList,
      tempRecord: record,
      isShowAddModal: true
    });
  }
  // 删除block
  onClose = (params, record) => {
    if (record.blockRuleList) {
      record.blockRuleList.filter(item => item.rule !== params.rule)
    }
    const { groupProductList } = this.state;
    const temp = groupProductList.map(item => {
      if (item.blockRuleList && item.id === record.id) {
        const blockList = item.blockRuleList.filter(item => item.rule !== params.rule);
        item.blockRuleList = blockList;
      }
      return item;
    });
    this.setState({
      groupProductList: temp,
      itemProduct: record,
      blockRuleList: []
    });
  }
  // 打开编辑block
  editBlock = (item, record) => {
    this.setState({
      blockRuleList: record.blockRuleList,
      tempRecord: record,
      isShowEditModal: true,
      editParams: item
    });
  }
  // 查询
  handleSubmit = e => {
    e.preventDefault();
    this.props.form.validateFields((err, values) => {
      if (!err) {
        this.setState({
          filterParmas: values
        }, () => {
          this.onList({
            action: this.props.actions.queryGroupProduct,
            params: values
          })
        })

        // this.onList({
        //   action: this.props.actions.getSelectProductPageList,
        //   params: {
        //     "condition": params,
        //     "current": 1,
        //     "size": 10
        //   }
        // })
      }
    });
  }
  // 重置
  reset = () => {
    this.props.form.resetFields();
  }
  /**
   * 导出产品组产品
   */
  export = e => {
    e.preventDefault();
    this.props.form.validateFields((err, values) => {
      if (!err) {
        this.setState({
          filterParmas: values
        })
        this.props.actions.groupProductExport(values).then((data) => {
          const { productGroup } = this.props;
          if (productGroup.groupProductExportRes.code == 100) {
            message.info("邮件已发送至您的个人邮箱，请注意查收");
          } else {
            message.error("系统错误");
          }
        }).catch((error) => {
          message.error("系统出错")
        });
      }
    });
  }
  moveRow = (dragIndex, hoverIndex) => {
    const { groupProductList } = this.state;
    const dragRow = groupProductList[dragIndex];
    const newstate = update(this.state, {
      groupProductList: {
        $splice: [[dragIndex, 1], [hoverIndex, 0, dragRow]],
      },
    });
    newstate.groupProductList.map((item, index) => {
      item.sort = index + 1;
      return item;
    });
    // console.log(newstate.groupProductList)
    this.setState(
      update(this.state, {
        groupProductList: {
          $splice: [[dragIndex, 1], [hoverIndex, 0, dragRow]],
        },
      })
    );
    // this.onEvent("saveSort",newstate.groupProductList);
    this.props.actions.saveSort({ groupProductList: newstate.groupProductList });
  }

  /**编辑产品详情模态框确认按钮 */
  editProductOk = params => {
    this.props.actions.groupEditProduct(params).then((data) => {
      const { productGroup } = this.props;
      if (productGroup.groupEditProduct.code == 100) {
        const { groupProductList } = this.state;
        const temp = groupProductList.map(item => {
          if (item.id === params.productGroupProductId) {
            const point = productGroup.groupEditProduct.result.point;
            const pointOrTimes = productGroup.groupEditProduct.result.pointOrTimes;
            const sort = productGroup.groupEditProduct.result.sort;
            const blockList = productGroup.groupEditProduct.result.blockRuleList;
            item.point = point;
            item.pointOrTimes = pointOrTimes;
            item.sort = sort;
            item.blockRuleList = blockList;
          }
          return item;
        });
        this.setState({
          editProductVisible: false,
          groupProductList: temp,
        })
      }
    }).catch((error) => {
      message.error("系统错误")
    });
  }

  /**编辑产品详情模态框取消按钮 */
  editProductCancel = e => {
    this.setState({
      editProductVisible: false
    })
  }

  render() {
    // const { product } = this.state;
    const { goodsId, cityList, key, onEvent, festivalList } = this.props;
    const { getFieldDecorator } = this.props.form;
    const { selectedRowKeys, loading, itemProduct, groupProductList, isShowEditModal, isShowAddModal, editParams, editProductVisible, editProductDistroy, productDetail } = this.state;
    const edit = cookie.load("KLF_PG_GM_GL_EDIT");
    const rowSelection = {
      selectedRowKeys,
      onChange: this.onSelectChange,
    };
    // const hasSelected = selectedRowKeys.length > 0;
    // const {itemProduct} = this.state;

    const tempColumns = this.getColumns();
    if (itemProduct.useLimitId === 'fixed_point') {
      const tempindex = {
        title: '点数',
        dataIndex: 'pointOrTimes',
        key: 'pointOrTimes',
        editable: true,
      }
      tempColumns.push(tempindex);
    }
    const oprationCol = {
      title: '操作',
      dataIndex: 'actions',
      key: 'actions',
      render: (text, record) => {
        const { editingKey } = this.state;
        const editable = this.isEditing(record);
        return (edit ?
          <a disabled={editingKey !== ''} onClick={() => this.edit(record)}>编辑</a> : null
          // <div>
          //   {editable ? (
          //     <span>
          //       <EditableContext.Consumer>
          //         {form => (
          //           <a href="javascript:;" onClick={() => this.save(form, record.id, record)} style={{ marginRight: 8 }} > 保存  </a>
          //         )}
          //       </EditableContext.Consumer>
          //       <Popconfirm title="确定取消吗?" onConfirm={() => this.cancel(record.id)} >  <a>取消</a> </Popconfirm>
          //     </span>
          //   ) : (
          //       <a disabled={editingKey !== ''} onClick={() => this.edit(record.id)}>编辑</a>
          //     )}
          // </div>
        );
      },

    };
    tempColumns.push(oprationCol);

    const columns = tempColumns.map((col) => {
      if (!col.editable) {
        return col;
      }
      return {
        ...col,
        onCell: record => ({
          record,
          inputType: col.dataIndex === 'blockNatural' ? 'text' : 'number',
          dataIndex: col.dataIndex,
          title: col.title,
        }),
      };
    });
    return (
      <Fragment>
        {/* <GoodsGroupFilter key={'filter'+itemProduct.id}
            onEvent={this.onEvent} cityList={cityList} itemProduct={itemProduct} festivalList={festivalList} goodsId={goodsId} 
            />
            <GoodsGroupList key={'list'+itemProduct.id}  itemProduct ={itemProduct} festivalList={festivalList} goodsId={goodsId} 
            data={itemProduct.productVoList} 
            // loading={isTableLoading}
            onEvent={this.onEvent}
            // pagination={this.getPagination}
            onList={this.getList}
            /> */}
        <div className="c-filter" key={key} style={{ borderRadius: '0', marginBottom: '0' }}>
          <Form layout="inline" onSubmit={this.handleSubmit}>
            <Row gutter={24}>
              <Col span={10} style={{}}>
                <Form.Item>

                  {getFieldDecorator('shopIdOrShopName', {})(
                    <Input placeholder="搜索产品组ID/名称/内部简称" style={{ width: '100%' }} />
                  )}
                  {getFieldDecorator('goodsId', { initialValue: goodsId })(
                    <Input style={{ display: 'none' }} />

                  )}
                  {getFieldDecorator('groupId', { initialValue: itemProduct.id })(
                    <Input style={{ display: 'none' }} />

                  )}
                </Form.Item>
              </Col>
              <Col span={10} style={{}}>
                <Form.Item label="城市">

                  {getFieldDecorator('city', { initialValue: "" })(
                    <Select style={{ width: 200 }} showSearch
                      filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                      <Option value="" Selected>全部</Option>
                      {
                        cityList.map(item => (
                          <Option key={'city' + item.id} value={item.nameCh}>{item.nameCh}</Option>
                        ))
                      }
                    </Select>
                  )}
                </Form.Item>

              </Col>
              <Col span={10} style={{}}>
                <Form.Item label="状态">

                  {getFieldDecorator('status', { initialValue: null })(
                    <Select style={{ width: 200 }}>
                      <Option value={null} Selected>全部</Option>
                      <Option value={0}>在售</Option>
                      <Option value={1}>停售</Option>
                    </Select>
                  )}
                </Form.Item>

              </Col>
              <Col span={4} style={{}}>
                <Button type="primary" htmlType="submit"     > 查询  </Button>
                <Button onClick={this.reset} style={{ marginLeft: 10 }}>重置</Button>
                <Button onClick={this.export} style={{ marginLeft: 10 }}>导出</Button>
              </Col>
            </Row>
            <Row gutter={24}>
              {edit && <Col span={10} style={{}}>
                <Button type="primary" onClick={() => this.onEvent('addProduct', itemProduct)} > + 添加产品  </Button>
                <Button style={{ marginLeft: 10 }} onClick={() => this.onEvent('delProduct', itemProduct)} disabled={this.state.disabled}  > 移除产品    </Button>

              </Col>}
              <Col span={10} style={{}}>


              </Col>
              <Col span={4} style={{}}>

              </Col>
            </Row>
          </Form>
        </div>

        <div >
          <Alert message={<Fragment> 已选择 <a style={{ fontWeight: 600 }}>{selectedRowKeys.length}</a> 项 ！&nbsp;&nbsp;
              </Fragment>} type="info" showIcon />
        </div>
        {
          // 新增block弹框
          isShowAddModal &&
          <AddModal
            onCancel={this.onCancel}
            onOk={this.onOk}
            data={{ festivalList: festivalList }}
          />
        }
        {
          // 编辑block框
          isShowEditModal &&
          <EditModal
            onCancel={this.onEditCancel}
            onOk={this.handleEdit}
            data={{ festivalList: festivalList, editParams }}
          />
        }
        <Modal
          title="申请停售"
          visible={this.state.visible}
          onOk={this.handleOk}
          onCancel={this.handleCancel}
          cancelText="取消"
          okText="确定"
        >
          <p style={{ color: 'red' }}>确定申请停售此产品吗？</p>
        </Modal>
        <Table rowSelection={rowSelection}
          key={"table" + key}
          columns={columns}
          dataSource={groupProductList}
          pagination={false}
          rowKey="id"
          loading={loading}
          // onChange={this.handleChange}
          onRow={(record, index) => ({
            index,
            moveRow: this.moveRow,
          })}
        />
        {editProductVisible && <EditProductModal
          editProductVisible={editProductVisible}
          editProductOk={this.editProductOk}
          editProductCancel={this.editProductCancel}
          editProductDistroy={editProductDistroy}
          productDetail={productDetail}
          festivalList={festivalList}
        />}
      </Fragment>
    );
  }
}

export default GroupInfoTable;
