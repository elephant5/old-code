import React, { Fragment, Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { bindActionCreators } from 'redux';
import * as actions from '../../../store/productGroup/action';
import * as resourceActions from '../../../store/resource/action';
import * as productActions from '../../../store/product/action';
import * as productGroupActions from '../../../store/productGroup/action';
import { Form, Input, Button, Select, Row, Col, Table, message, InputNumber, Popconfirm, Tag, Alert } from 'antd';
// import { DragDropContext, DragSource, DropTarget } from 'react-dnd';
// import HTML5Backend from 'react-dnd-html5-backend';
// import update from 'immutability-helper';
import '../index'
import AddModal from '../../merchant/component/add-modal';
import EditModal from '../../merchant/component/edit-modal';
import TableListBase from '../../../base/table-list-base';
import { unique } from '../../../util/index';
const { Option } = Select;
const FormItem = Form.Item;
const EditableContext = React.createContext();
let dragingIndex = -1;


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
    const { editing, dataIndex, title, inputType, record, index, addBlock, onClose, editBlock, handleEdit, } = this.props;
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
    // this.onList({
    //   action: this.props.actions.queryGroupProduct,
    //   params: {
    //     "condition": {
    //     },
    //     "current": 1,
    //     "size": 10
    //   }
    // })
  }
  componentWillReceiveProps(nextProps) {
    const { operation, productGroup, resource } = nextProps;
    const { selectedRowKeys, loading, itemProduct, groupProductList } = this.state;
    const { onEvent } = this.props;
    switch (operation.type) {
      case actions.GET_GROUPDELPRODUCT_SUCCESS:
        message.success("移除产品组的产品成功", 10);
        const tempList = groupProductList;//itemProduct.groupProductList;
        let addList = [];
        if (tempList && selectedRowKeys.length > 0) {
          tempList.map(item => {
            selectedRowKeys.map(rows => {
              if (rows != item.id) {
                addList.push(item);
              }
            })
          });
        }
        itemProduct.groupProductList = addList;
        // groupProductList = addList;
        this.setState({
          itemProduct: itemProduct,
          groupProductList: addList,
          selectedRowKeys: [],
          loading: false
        }, () => {
          // onEvent('delProduct',{selectedRowKeys:selectedRowKeys,id:itemProduct.id});
        })

        break;
      case actions.GET_QUERYGROUPPRODUCT_SUCCESS:
        this.setState({
          // itemProduct:itemProduct,
          groupProductList: productGroup.queryGroupProduct.result
        });
        break;
      case actions.GET_GROUPEDITPRODUCT_SUCCESS:
        break;
      default:
    }
  }
  onEvent = (type, params) => {
    switch (type) {
      case 'addProduct':
        var path = {
          pathname: '/productInfo',
          query: this.state.itemProduct,

        }
        this.props.router.push(path);
        break;
      case 'delProduct':
        const params = {};
        params.productGroupId = this.state.itemProduct.id;
        params.productGroupProductIds = this.state.selectedRowKeys;
        this.props.actions.groupDelProduct(params);
        break;
      case 'editProduct':

        break;
      // 查询
      // case 'search':
      //   this.setState({
      //     filterParmas: params
      //   }, () => {
      //     this.onList({
      //       action: this.props.actions.queryGroupProduct,
      //       params: params
      //     })
      //   })
      //   break;
      // 保存
      case 'addProductBlock':

        break;
      default:
    }
  }
  // 切换分页
  getList = params => {
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
      fixed: 'right',
    }, {
      title: '城市',
      dataIndex: 'cityName',
    }, {
      title: '酒店|商户',
      render: (text, row, index) => {
        return (<span>{row.hotelName} | {row.shopName}</span>)
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
    },
    {
      title: '最高价',
      dataIndex: 'cost',
    },
    {
      title: '最低价',
      dataIndex: 'status',
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

  edit(key) {
    this.setState({ editingKey: key });
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
    const rules = [...this.state.blockRuleList, ...params.festival, ...params.blockRule];
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
  render() {
    // const { product } = this.state;
    const { goodsId, cityList, key, onEvent, festivalList } = this.props;
    const { getFieldDecorator } = this.props.form;
    const { selectedRowKeys, loading, itemProduct, groupProductList, isShowEditModal, isShowAddModal, editParams } = this.state;
    const rowSelection = {
      selectedRowKeys,
      onChange: this.onSelectChange,
    };
    // const hasSelected = selectedRowKeys.length > 0;
    // const {itemProduct} = this.state;
    const components = {
      body: {
        // row: DragableBodyRow,
        cell: EditableCell,
      },
    };
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
      fixed: 'right',
      render: (text, record) => {
        const { editingKey } = this.state;
        const editable = this.isEditing(record);
        return (
          <div>
            {editable ? (
              <span>
                <EditableContext.Consumer>
                  {form => (
                    <a href="javascript:;" onClick={() => this.save(form, record.id, record)} style={{ marginRight: 8 }} > 保存  </a>
                  )}
                </EditableContext.Consumer>
                <Popconfirm title="确定取消吗?" onConfirm={() => this.cancel(record.id)} >  <a>取消</a> </Popconfirm>
              </span>
            ) : (
                <a disabled={editingKey !== ''} onClick={() => this.edit(record.id)}>编辑</a>
              )}
          </div>
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
          editing: this.isEditing(record),
          addBlock: () => this.addBlock(record),
          // onClick:()=>this.editBlock,
          onClose: this.onClose,
          editBlock: this.editBlock,
          handleEdit: this.handleEdit,

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
        <div className="c-filter" key={key} style={{ borderRadius: '0' }}>
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
                      <Option  value="" Selected>全部</Option>
                      {
                        cityList.map(item => (
                          <Option key={'city' + item.id} value={item.nameCh}>{item.nameCh}</Option>
                        ))
                      }
                    </Select>
                  )}
                </Form.Item>

              </Col>
              <Col span={4} style={{}}>
                <Button type="primary" htmlType="submit"     > 查询  </Button>
                <Button onClick={this.reset} style={{ marginLeft: 10 }}     > 重置     </Button>
              </Col>
            </Row>
            <Row gutter={24}>
              <Col span={10} style={{}}>
                <Button type="primary" onClick={() => this.onEvent('addProduct', itemProduct)} > + 添加产品  </Button>
                <Button style={{ marginLeft: 10 }} onClick={() => this.onEvent('delProduct', itemProduct)}    > 移除产品    </Button>

              </Col>
              <Col span={10} style={{}}>


              </Col>
              <Col span={4} style={{}}>

              </Col>
            </Row>
          </Form>
        </div>
        <div >
          <Alert message={<Fragment> 已选择 <a style={{ fontWeight: 600 }}>{selectedRowKeys.length}</a> 项！&nbsp;&nbsp;
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
        <EditableContext.Provider value={this.props.form}>
          <Table rowSelection={rowSelection}
            components={components}
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
        </EditableContext.Provider>

      </Fragment>
    );
  }
}

export default GroupInfoTable;