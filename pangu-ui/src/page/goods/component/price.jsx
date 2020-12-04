import 'braft-editor/dist/index.css'
import React from 'react'
import { Form, Input, InputNumber, Button, message, Table, Popconfirm, Divider, Icon } from 'antd'
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as actions from '../../../store/goods/action';
import cookie from 'react-cookies';
const FormItem = Form.Item;
const EditableContext = React.createContext();
const EditableRow = ({ form, index, ...props }) => (
  <EditableContext.Provider value={form}>
    <tr {...props} />
  </EditableContext.Provider>
);

const EditableFormRow = Form.create()(EditableRow);
class EditableCell extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    const {
      editing,
      dataIndex,
      title,
      inputType,
      record,
      index,
      onChange,
      ...restProps
    } = this.props;
    return (
      <EditableContext.Consumer>
        {(form) => {
          const { getFieldDecorator } = form;
          return (
            <td {...restProps}>
              {editing ? (
                <FormItem style={{ margin: 0 }}>
                  {getFieldDecorator(dataIndex, {
                    rules: [{
                      required: false,
                      message: `请输入${title}!`,
                    }],
                    initialValue: record[dataIndex],
                  })(
                    this.props.inputType === 'number' ? <InputNumber onChange={() => onChange(form, dataIndex)} /> : <Input />
                    // this.getInput(onChange(form,record.key,dataIndex))
                  )}
                </FormItem>
              ) : restProps.children}
            </td>
          );
        }}
      </EditableContext.Consumer>
    );
  }
}

@connect(
  ({ operation, goods }) => ({
    operation,
    goods: goods.toJS()
  }),
  dispatch => ({
    actions: bindActionCreators(actions, dispatch)
  })
)
@Form.create()
class GoodsPrice extends React.Component {
  constructor(props) {
    super(props);
    this.columns = [
      // {title: 'ID',dataIndex:'id',key:'id',editable:false},
      { title: '折扣名', dataIndex: 'level', key: 'level', editable: true },
      { title: '零售价', dataIndex: 'price', key: 'price', editable: false },
      { title: '折扣价', dataIndex: 'discount', key: 'discount', editable: true },
      { title: '折扣比例', dataIndex: 'disRate', key: 'disRate', editable: true },
      {
        title: '操作', dataIndex: 'operation', key: 'operation', render: (text, record) => {
          const editable = this.isEditing(record);
          return (
            <div>
              {editable ? (
                <span>
                  <EditableContext.Consumer>
                    {form => (
                      <a
                        href="javascript:;"
                        onClick={() => this.handleSave(form, record.key)}
                        style={{ marginRight: 8 }}
                      >
                        保存
                        </a>
                    )}
                  </EditableContext.Consumer>
                  <Popconfirm
                    title="你确定要取消么?"
                    onConfirm={() => this.handleCancel(record.key)}
                  >
                    <a>取消</a>
                  </Popconfirm>
                </span>
              ) : (
                  <a onClick={() => this.handleEdit(record.key)}>编辑</a>
                )}
              <Divider type="vertical" />
              <Popconfirm title="你确定要删除么?" onConfirm={() => this.handleDelete(record.key)}>
                <a href="javascript:;">删除</a>
              </Popconfirm>
            </div>
          )
        }
      },
    ];
    this.state = {
      goodsPriceList: [],
      goodsPrice: {},
      count: 1,
      editingKey: '',
      editingDataIndex: '',
    };
  }

  componentWillMount() {
    const { goodsId } = this.props;
    const { goodsPrice } = this.state;
    goodsPrice.goodsId = goodsId;
    // console.log(this.props);
    // console.log(goodsId);
    this.props.actions.selectGoodsPriceListByGoodsId(goodsPrice);
  }
  componentDidMount() {

  }
  componentWillReceiveProps(nextProps) {
    const { operation, goods } = nextProps;
    switch (operation.type) {
      case actions.GET_GOODS_PRICE_LIST_SELECTBYGOODSID_SUCCESS:
        // console.log(operation)
        if (goods.goodsPriceList.code == 100) {
          const goodsPriceListRes = goods.goodsPriceList.result;
          if (goodsPriceListRes) {
            let count = 1;
            const goodsPriceList = goodsPriceListRes.goodsPriceResList;
            goodsPriceList.map((price, index) => {
              if (!price.key) {
                price.key = count;
                count++;
              }
            });
            console.log(goodsPriceList)
            this.setState({
              goodsPriceList: goodsPriceList,
              count: count,
              price: goodsPriceListRes.price,
              goodsId: goodsPriceListRes.goodsId,
            });
          }
        } else {
          const msg = goods.goodsPriceList.msg ? goods.goodsPriceList.msg : '查询商品使用限制出错';
          message.success(msg, 10);
        }
        break;
      case actions.SAVE_GOODS_PRICE_SUCCESS:
        if (goods.goodsPrice.code == 100) {
          const goodsPrice = goods.goodsPrice.result;
          if (goodsPrice) {
            const { goodsPriceList } = this.state;
            let newgoodsPriceList = goodsPriceList.filter(item => !(item.id == goodsPrice.id && goodsPrice.delFlag == 1));

            this.setState({
              goodsPriceList: newgoodsPriceList,
              // editingKey:'',
            });
          }
        } else {
          const msg = goods.goodsPrice.msg ? goods.goodsPrice.msg : '更新商品详情出错';
          message.success(msg, 10);
        }
        break;
      default:
        break;
    }
  }

  isEditing = record => record.key === this.state.editingKey;

  //取消
  handleCancel = () => {
    this.setState({ editingKey: '' });
  }
  //编辑
  handleEdit = (key) => {
    this.setState({ editingKey: key });
  }
  //change
  handleChange = (form, dataIndex) => {
    console.log('handleChange')
    // console.log(form)
    // console.log(key)
    // console.log(dataIndex)
    const { price } = this.state;
    // const values=form.getFieldsValue();
    // console.log(values)

    setTimeout(() => {
      const value = form.getFieldValue(dataIndex);
      console.log(value)
      if (value) {
        if (price) {
          if (dataIndex === 'discount') {
            form.setFieldsValue({
              disRate: value / price,
            })
          }

          if (dataIndex === 'disRate') {
            form.setFieldsValue({
              discount: price * value,
            })
          }
        }
      }
    })
  }
  //保存
  handleSave = (form, key) => {
    console.log(key);
    console.log(form);
    const { price, goodsId, goodsPriceList } = this.state;
    form.validateFields((err, values) => {
      if (!err) {
        if (!values.level) {
          message.success('请输入折扣名', 5);
          return;
        }
        if (!values.discount && !values.disRate) {
          message.success('请输入折扣价或折扣比例', 5);
          return;
        }
        let modify = {}
        goodsPriceList.map((item, index) => {
          if (item.key === key) {
            modify = item;
          }
        })
        console.log(modify)
        modify.level = values.level;
        if (values.disRate) {
          modify.disRate = values.disRate;
          modify.discount = price * values.disRate;
          form.setFieldsValue({
            discount: price * values.disRate,
          })
        } else if (values.discount) {
          modify.disRate = values.discount / price
          modify.discount = values.discount;
          form.setFieldsValue({
            disRate: values.discount / price,
          })
        }
        console.log(modify)
        actions.saveGoodsPriceByGoodsId(modify);
        this.setState({
          editingKey: '',
        })
      }
    })
  }
  //删除
  handleDelete = (key) => {
    const { actions } = this.props;
    const goodsPriceList = [...this.state.goodsPriceList];
    goodsPriceList.map((price, index) => {
      if (price.key == key) {
        if (price.id) {
          price.delFlag = 1;
          actions.saveGoodsPriceByGoodsId(price);
        } else {
          this.setState({ goodsPriceList: goodsPriceList.filter(item => item.key !== key) });
        }
      }
    })
  }
  handleAdd = () => {
    const { count, goodsPriceList, price, goodsId } = this.state;
    const newData = {
      key: count,
      level: '',
      price: price,
      discount: 0,
      disRate: 0,
      goodsId: goodsId,
    };
    this.setState({
      goodsPriceList: [...goodsPriceList, newData],
      count: count + 1,
      editingKey: count,
    });
  }

  render() {
    const { goodsPriceList } = this.state;
    const edit = cookie.load("KLF_PG_GM_GL_EDIT");
    const components = {
      body: {
        row: EditableFormRow,
        cell: EditableCell,
      },
    };
    const columns = this.columns.map((col) => {
      if (!col.editable) {
        return col;
      }
      return {
        ...col,
        onCell: record => ({
          record,
          inputType: col.dataIndex != 'level' ? 'number' : 'text',
          dataIndex: col.dataIndex,
          title: col.title,
          editing: this.isEditing(record),
          onChange: this.handleChange,
        }),
      };
    });



    return (
      <div>
        <Table
          components={components}
          columns={columns}
          dataSource={goodsPriceList}
          pagination={false}
        />
        {edit && <Button type="dashed" style={{ margin: '24px 0' }} block onClick={this.handleAdd}>
          <Icon type="plus" /> 添加价格
            </Button>}
      </div>
    );
  }
}

export default GoodsPrice;