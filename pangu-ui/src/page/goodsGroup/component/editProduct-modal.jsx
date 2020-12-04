import React, { Component } from 'react';
import { Modal, Form, DatePicker, Input, Select, Checkbox, Button, message, Row, Col, Tag, Divider, InputNumber, Table, Popconfirm } from 'antd';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as actions from '../../../store/giftCode/action';
import * as resourceAction from '../../../store/resource/action';
import * as goodsActions from '../../../store/goods/action';
import CheckboxGroup from 'antd/lib/checkbox/Group';
import { resourColor, serviceColor, giftColor } from '../../../util/dictType.js'
import AddModal from '../../merchant/component/add-modal';
import AddPaymentModal from '../component/addPayment-modal';
import { unique } from '../../../util/index';

const { Option } = Select;
const { TextArea } = Input;

@connect(
  ({ operation, code, goods, resource }) => ({
    operation,
    code: code.toJS(),
    goods: goods.toJS(),
    resource: resource.toJS(),
  }),
  dispatch => ({
    actions: bindActionCreators({ ...actions, ...goodsActions, ...resourceAction }, dispatch)
  })
)
@Form.create()
class EditProductModal extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isShowAddBlockModal: false,
      isShowAddPaymentModal: false,
      blockRuleList: [],
      bookBasePaymentList: [],
      count: null,
      settleRuleList: [],
    };
    this.columns = [
      {
        title: '起止时间',
        dataIndex: 'timeStr',
      },
      {
        title: '预约支付金额',
        dataIndex: 'bookPrice',
      },
      {
        title: '操作',
        //   dataIndex: 'operation',
        render: (text, record) =>
          this.state.bookBasePaymentList.length >= 1 ? (
            <Popconfirm title="确定删除吗?" onConfirm={() => this.handleDelete(record.id)}>
              <a href="javascript:;">删除</a>
            </Popconfirm>
          ) : null,
      },
    ];
  }

  componentDidMount() {
    const { productDetail } = this.props;
    if (productDetail) {
      this.props.actions.selectBookBasePaymentList(productDetail.id).then((data) => {
        const { goods } = this.props;
        if (goods.selectBookBasePaymentListRes.code == 100) {
          this.setState({
            bookBasePaymentList: goods.selectBookBasePaymentListRes.result,
            count: -1,
          });
        }
      }).catch((error) => {
        message.error('系统错误');
      });

      this.props.actions.getSettleRule({ shopId: productDetail.shopId, shopItemId: productDetail.shopItemId }).then((date) => {
        const { resource } = this.props;
        if (resource.settleRule.code == 100) {
          this.setState({
            settleRuleList: resource.settleRule.result,
          })
        }
      }).catch((error) => {
        message.error('系统错误');
      })

      this.setState({
        blockRuleList: productDetail.blockRuleList || []
      })
    }
  }

  componentWillReceiveProps(nextProps) {
    const { operation, code, goods } = nextProps;
    switch (operation.type) {
      default:
    }
  }

  //提交
  handleSubmit = e => {
    e.preventDefault();
    const { editProductOk, productDetail } = this.props;
    this.props.form.validateFields((err, values) => {
      if (!err) {
        let params = {
          productGroupProductId: productDetail.id,
          sort: values.sort,
          blockRuleList: this.state.blockRuleList,
          bookPaymentVoList: this.state.bookBasePaymentList,
        }
        editProductOk(params)
      }
    })
  }

  //新增block
  addBlock = e => {
    this.setState({
      isShowAddBlockModal: true,
    })
  }

  // 关闭block框
  onCancelAddBlock = () => {
    this.setState({
      isShowAddBlockModal: false
    })
  }

  //确定block
  onOkAddBlock = params => {
    const rules = [...this.state.blockRuleList, ...params.festival, ...params.blockRule];
    const norepeatRule = unique(rules);
    this.setState({
      blockRuleList: norepeatRule,
      isShowAddBlockModal: false
    })
  }

  // 删除block
  onClose = params => {
    this.setState({
      blockRuleList: this.state.blockRuleList.filter(item => item.rule !== params.rule)
    })
  }

  // 关闭预约支付金额框
  onCancelAddPayment = () => {
    this.setState({
      isShowAddPaymentModal: false
    })
  }

  //确定预约支付金额
  onOkAddPayment = params => {
    const { count, bookBasePaymentList } = this.state;
    params.id = count;
    this.setState({
      bookBasePaymentList: [...bookBasePaymentList, params],
      count: count - 1,
      isShowAddPaymentModal: false
    })
  }

  //删除一行
  handleDelete = id => {
    const bookBasePaymentList = [...this.state.bookBasePaymentList];
    this.setState({ bookBasePaymentList: bookBasePaymentList.filter(item => item.id !== id) });
  };

  //新增一行
  handleAdd = () => {
    this.setState({
      isShowAddPaymentModal: true,
    })
  };

  render() {
    const { editProductVisible, editProductCancel, editProductDistroy, productDetail, festivalList } = this.props;
    const { getFieldDecorator } = this.props.form;
    const { isShowAddBlockModal, blockRuleList, isShowAddPaymentModal, bookBasePaymentList, settleRuleList } = this.state;
    const columns = this.columns;
    return (
      <Modal
        title="编辑"
        visible={editProductVisible}
        onOk={this.handleSubmit}
        onCancel={editProductCancel}
        // footer={null}
        destroyOnClose={editProductDistroy}
        width={'80%'}
        cancelText="取消"
        okText="确定"
      >
        <Form onSubmit={this.handleSubmit} layout="horizontal">
          <div>
            <Row>
              <Col span={8}>
                <span style={{ fontWeight: "bold" }}>{productDetail.hotelName + '|' + productDetail.shopName}</span>
              </Col>
              <Col span={6}>
                {productDetail.productName}
              </Col>
              <Col span={10}>
                {productDetail.service &&
                  serviceColor.map(item => {
                    if (item.name === productDetail.service) {
                      return <Tag color={item.color} key={productDetail.service} >{productDetail.service}</Tag>;
                    }
                  })
                }
                {productDetail.gift &&
                  giftColor.map(item => {
                    if (item.name === productDetail.gift) {
                      return <Tag color={item.color} key={productDetail.gift} >{productDetail.gift}</Tag>;
                    }
                  })
                }
              </Col>
            </Row>
            <Row style={{ marginTop: 10 }}>
              <Col>
                <span>结算成本：</span>
              </Col>
            </Row>
            <Row>
              {settleRuleList && settleRuleList.map(item => {
                return <div><Tag>{item.timeStr + '-' + item.settleRuleStr}</Tag><br /></div>
              })}
            </Row>
          </div>
          <Divider></Divider>
          <Form.Item label="排序:" style={{ marginTop: '20px' }} labelCol={{ span: 2 }} wrapperCol={{ span: 12 }}>
            {getFieldDecorator('sort', { initialValue: productDetail.sort })(
              <InputNumber></InputNumber>
            )}
          </Form.Item>
          <Form.Item style={{ marginTop: '20px' }} label="产品Block：" labelCol={{ span: 2 }} wrapperCol={{ span: 12 }}>
            <Button type="primary" ghost onClick={this.addBlock}>+ 添加Block</Button>&nbsp;&nbsp;&nbsp;&nbsp;
                                {
              // 新增block弹框
              isShowAddBlockModal &&
              <AddModal
                onCancel={this.onCancelAddBlock}
                onOk={this.onOkAddBlock}
                data={{ festivalList: festivalList }}
              />
            }
            {blockRuleList &&
              blockRuleList.map((item, idx) => (
                <Tag key={item.rule}
                  closable
                  onClose={e => e.stopPropagation()}
                  afterClose={() => this.onClose(item)}
                >
                  {item.natural}
                </Tag>
              )
              )
            }
          </Form.Item>
          <Form.Item label="预约支付金额:" style={{ marginTop: '20px' }} labelCol={{ span: 2 }} wrapperCol={{ span: 12 }}>
            <div>
              <Button onClick={this.handleAdd} type="primary" style={{ marginBottom: 16 }}>
                新增一行
                            </Button>
              {
                isShowAddPaymentModal &&
                <AddPaymentModal
                  onCancel={this.onCancelAddPayment}
                  onOk={this.onOkAddPayment}
                  productGroupProductId={productDetail.id}
                />
              }
              <Table
                rowClassName={() => 'editable-row'}
                bordered
                dataSource={bookBasePaymentList}
                columns={columns}
                pagination={false}
              />
            </div>
          </Form.Item>
        </Form>
      </Modal>
    );
  }
}

export default EditProductModal;