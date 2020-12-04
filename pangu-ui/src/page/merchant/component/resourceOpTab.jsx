import React, { Fragment } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { Form, Checkbox, Tag, Button, Select, Row, Col, Badge, Divider, Tabs, DatePicker, message } from 'antd';
import './resourceOpTab.less';
import moment from 'moment';
import * as actions from '../../../store/resource/action';
import * as commonActions from '../../../store/common/action';
import CommonUpload from '../../../component/common-upload/index';
import RuleModal from './rule-modal';
import ComponentBase from '../../../base/component-base';
import { isEmpty } from 'lodash';
import Block from './block.jsx';
import Price from './price';
import _ from 'lodash';
import { getFileList } from '../../../util/index';
import cookie from 'react-cookies';
const { Option } = Select;
const { RangePicker } = DatePicker;
const TabPane = Tabs.TabPane;
const formItemLayout = {
  labelCol: { span: 4 },
  wrapperCol: { span: 10 },
};
const formItemLayout2 = {
  labelCol: { span: 4 },
  wrapperCol: { span: 10 },
};

@connect(
  ({ operation, resource, common }) => ({
    operation,
    resource: resource.toJS(),
    common: common.toJS()
  }),
  dispatch => ({
    actions: bindActionCreators({ ...actions, ...commonActions }, dispatch)
  })
)
@Form.create()
class ResourceOpTab extends ComponentBase {
  constructor(props) {
    super(props);
    const init = {
      discount: null,
      taxFeePer: null,
      serviceFeePer: null,
      netPricePer: null,
      customTaxFeePer: null,
      taxNetPricePer: null,
      adjust: null,
      taxServiceFeePer: null
    };
    this.state = {
      initialValue: {},
      tabid: props.tabid || 1,
      startDate: props.startDate,
      endDate: props.endDate,
      allSysDictList: [],
      ruleList: [],
      indeterminate: true,
      isChanged: false
    }
    this.fileList = [];
    this.id = null;
  }
  componentDidMount() {

    const { shopType, giftTypeList, shopParams, shopItem, isChanged } = this.props;
    this.getGiftList(shopType, giftTypeList, shopItem);
  }

  getGiftList = (shopType, giftTypeList, shopItem) => {
    const ruleListTemp = [];
    if (shopType === "accom" || shopType === "coupon" || shopType === "trip") {
      ruleListTemp.push({
        name: '',
        gift: null,
        isSelect: false,
        isDisabled: false,
        content: {
          settleExpress: '',
          customTaxExpress: ''
        },
        contentValue: this.init
      });
    } else {
      giftTypeList.map(item => {
        if (shopItem.gift) {
          shopItem.gift.split(",").map(gift => {
            if (gift === item.code) {
              let temp = {
                name: item.shortName,
                gift: item.code,
                isSelect: false,
                isDisabled: false,
                content: {
                  settleExpress: '',
                  customTaxExpress: ''
                },
                contentValue: this.init
              };
              ruleListTemp.push(temp);
            }
          });
        }
      });

    }
    this.setState({
      ruleList: ruleListTemp,
    });
  }
  componentWillReceiveProps(nextProps) {
    // const {  , shopItem} = nextProps;
    const { isChanged, operation, resource, shopType, shopItem, giftTypeList } = nextProps;
    if (isChanged) {
      this.getGiftList(shopType, giftTypeList, shopItem);
    }
    if (this.props.tabid !== nextProps.tabid || this.props.startDate !== nextProps.startDate || this.props.endDate !== nextProps.endDate) {
      this.setState({
        tabid: nextProps.tabid,
        startDate: nextProps.startDate,
        endDate: nextProps.endDate
      })
    }

  }

  getUploadFileProps = params => {
    return {
      fileName: params.fileName,
      fileType: 'shop.item.settle.contract',
      options: {
        // 上传回调
        onChange: (info, status) => {
          switch (status) {
            case 'done':
              this.fileList = this.fileList.concat(info);
              break;
            case 'removed':
              this.fileList = this.fileList.filter(item => info.code !== item.code)
              break;
            default:
              break;
          }
        }
      },
      config: {
        label: params.label,
        // rules: [{
        //     required: true, message: '请上传凭证'
        // }],
        // type: FILE_TYPE.PDF,
        // size: 1024*1024,
        // num: 20
      }
    }
  }

  // 结算规则保存
  handleClose = e => {
    e.preventDefault();
    const { onEvent, shopId, shopItemId } = this.props;
    this.props.form.validateFields((err, values) => {
      if (!err) {
        // 未添加的不传给后端，先filter
        const settleExpressVoList = this.state.ruleList.filter(item => item.isSelect === true).map(item => {
          let obj = item.contentValue;
          obj.gift = item.gift;
          return obj;
        })
        let arr = this.getInitWeek();
        onEvent('saveClose', {
          id: this.id,
          shopId,
          shopItemId,
          startDate: moment(values.dateRange[0]).format(),
          endDate: moment(values.dateRange[1]).format(),
          weeks: arr,
          fileList: this.fileList,
          settleExpressVoList
        })
        this.fileList = [];
        this.props.form.resetFields();
      }
    })
  }

  onOk = params => {
    const ruleList = this.state.ruleList.map(item => {
      if (item.gift === params.gift) {
        item.isSelect = true
      }
      return item;
    })
    this.setState({ ruleList })
    this.props.actions.getSettleExpress(params).then((data) => {
      const { resource } = this.props;
      const list = this.state.ruleList.map(item => {
        if (item.gift === resource.settleExpress.result.gift) {
          item.content = item.contentValue = resource.settleExpress.result.expressVo;
        }
        return item;
      })
      this.setState({
        ruleList: list
      })
    }).catch((error) => {
      message.error("结算公式编辑错误")
    })
    this.reset();
  }

  // 弹框
  editRule = params => {
    this.showView('showRuleModal', { ...params })
  }

  closeSettle = params => {
    this.props.onEvent('deleteSettle', params.id)
  }

  editSettle = params => {
    // 编辑需要id
    this.id = params.id;
    const ruleList = this.state.ruleList.map(item => {
      var tempItem = {
        adjust: null,
        customTaxExpress: null,
        customTaxFeePer: null,
        discount: null,
        gift: null,
        netPricePer: null,
        serviceFeePer: null,
        settleExpress: null,
        taxFeePer: null,
        taxNetPricePer: null,
        taxServiceFeePer: null,
      }
      if (item.gift === params.gift) {
        if (params.settleExpress) {
          tempItem = params.settleExpress;
          tempItem.gift = params.gift;
          tempItem.customTaxExpress = params.customTaxExpress;
          tempItem.settleExpress = params.express;
        } else {
          tempItem.adjust = null;
          tempItem.customTaxExpress = "";
          tempItem.customTaxFeePer = null;
          tempItem.discount = null;
          tempItem.gift = params.gift;
          tempItem.netPricePer = null;
          tempItem.serviceFeePer = null;
          tempItem.settleExpress = params.express;
          tempItem.taxFeePer = null;
          tempItem.taxNetPricePer = null;
          tempItem.taxServiceFeePer = null;
        }
        item.isSelect = true;
        item.isDisabled = false;
      } else {
        tempItem.settleExpress = null;
        tempItem.customTaxExpress = null;
        item.isDisabled = true;
        item.isSelect = false;
      }
      item.content = item.contentValue = tempItem;
      return item;
    })
    this.setState({
      initialValue: params,
      ruleList
    })
  }
  getInitWeek = () => {
    if (!_.isEmpty(this.state.initialValue)) {
      let arr = [];
      if (this.state.initialValue.monday === 1) {
        arr.push('1')
      }
      if (this.state.initialValue.tuesday === 1) {
        arr.push('2')
      }
      if (this.state.initialValue.wednesday === 1) {
        arr.push('3')
      }
      if (this.state.initialValue.thursday === 1) {
        arr.push('4')
      }
      if (this.state.initialValue.friday === 1) {
        arr.push('5')
      }
      if (this.state.initialValue.saturday === 1) {
        arr.push('6')
      }
      if (this.state.initialValue.sunday === 1) {
        arr.push('7')
      }
      return arr;
    }
    return [];
  }
  resetForm = () => {
    this.props.form.resetFields();
  }
  onCheckAllChange = (e) => {
    const { initialValue } = this.state;
    let params = initialValue ? initialValue : {};
    let checkedValue = 0;
    if (e.target.checked) {
      checkedValue = 1;
      params.friday = checkedValue;
      params.monday = checkedValue;
      params.saturday = checkedValue;
      params.sunday = checkedValue;
      params.thursday = checkedValue;
      params.tuesday = checkedValue;
      params.wednesday = checkedValue;

    } else {
      params.friday = checkedValue;
      params.monday = checkedValue;
      params.saturday = checkedValue;
      params.sunday = checkedValue;
      params.thursday = checkedValue;
      params.tuesday = checkedValue;
      params.wednesday = checkedValue;
    }
    this.setState({
      initialValue: params,
      indeterminate: e.target.checked ? false : true
    })
  }
  checkedValue = e => {
    const { initialValue } = this.state;
    let params = initialValue ? initialValue : {};
    let checkedValue = e.target.value;// e.target.checked ? 1 :0;
    let checked = e.target.checked;
    if (checkedValue === "1") {
      params.monday = checked ? 1 : 0;
    }
    if (checkedValue === "2") {
      params.tuesday = checked ? 1 : 0;
    }
    if (checkedValue === "3") {
      params.wednesday = checked ? 1 : 0;
    }
    if (checkedValue === "4") {
      params.thursday = checked ? 1 : 0;
    }
    if (checkedValue === "5") {
      params.friday = checked ? 1 : 0;
    }
    if (checkedValue === "6") {
      params.saturday = checked ? 1 : 0;
    }
    if (checkedValue === "7") {
      params.sunday = checked ? 1 : 0;
    }
    this.setState({
      initialValue: params,
      indeterminate: e.target.checked ? false : true
    })
  }
  renderClose = () => {
    const { settleRule } = this.props;
    const { getFieldDecorator } = this.props.form;
    const { viewData, initialValue, indeterminate, ruleList } = this.state;
    // 弹框展示
    const isShowRuleModal = this.isShowView('showRuleModal');
    const edit = cookie.load("KLF_PG_RM_SL_SERVICE_EDIT");
    return (<Fragment>
      {
        isShowRuleModal &&
        <RuleModal
          onOk={this.onOk}
          onCancel={this.reset}
          data={viewData}
        />
      }
      <div className="c-modal">

        <div style={{ marginBottom: 5, marginTop: 5, marginLeft: 10 }}>
          {
            !_.isEmpty(settleRule) &&
            settleRule.map((item, index) => {
              return (<div>
                <Tag key={`rule-${index + 1}`}
                  closable={edit ? true : false} color="blue" style={{ marginBottom: 2 }}
                  onClose={e => e.stopPropagation()}
                  afterClose={() => this.closeSettle(item)}
                  onClick={() => edit ? this.editSettle(item) : null}
                >
                  {item.timeStr}:{item.settleRuleStr}
                </Tag><br /></div>
              )
            })
          }
        </div>
        {!_.isEmpty(settleRule) && <Divider></Divider>}
        <Form className="batch-form" onSubmit={this.handleClose}>
          <Form.Item
            {...formItemLayout}
            label="起止日期"
          >
            {getFieldDecorator('dateRange', {
              initialValue: [
                initialValue.startDate ? moment(initialValue.startDate, 'YYYY-MM-DD') : null,
                initialValue.endDate ? moment(initialValue.endDate, 'YYYY-MM-DD') : null
              ],
              rules: [{ required: true, message: '请选择日期范围' }]
            })(
              <RangePicker />
            )}
          </Form.Item>
          <Form.Item
            {...formItemLayout}
            label="其中含"
          >
            {getFieldDecorator('weekday', {
              initialValue: this.getInitWeek(),
              rules: [{ required: true }]
            })(
              <Row>
                <Col span={6}><Checkbox onChange={this.onCheckAllChange} indeterminate={indeterminate} >全部</Checkbox></Col>
                <Col span={6}><Checkbox onChange={this.checkedValue} value="1" checked={initialValue ? initialValue.monday === 1 ? true : false : false}>周一</Checkbox></Col>
                <Col span={6}><Checkbox onChange={this.checkedValue} value="2" checked={initialValue ? initialValue.tuesday === 1 ? true : false : false}>周二</Checkbox></Col>
                <Col span={6}><Checkbox onChange={this.checkedValue} value="3" checked={initialValue ? initialValue.wednesday === 1 ? true : false : false}>周三</Checkbox></Col>
                <Col span={6}><Checkbox onChange={this.checkedValue} value="4" checked={initialValue ? initialValue.thursday === 1 ? true : false : false}>周四</Checkbox></Col>
                <Col span={6}><Checkbox onChange={this.checkedValue} value="5" checked={initialValue ? initialValue.friday === 1 ? true : false : false}>周五</Checkbox></Col>
                <Col span={6}><Checkbox onChange={this.checkedValue} value="6" checked={initialValue ? initialValue.saturday === 1 ? true : false : false}>周六</Checkbox></Col>
                <Col span={6}><Checkbox onChange={this.checkedValue} value="7" checked={initialValue ? initialValue.sunday === 1 ? true : false : false}>周日</Checkbox></Col>
              </Row>

            )}
          </Form.Item>
          <Form.Item
            {...formItemLayout2}
            label="结算规则："
          >
            {
              ruleList.map(item => (
                <div className="rule-box" key={item.gift} onClick={!item.isDisabled ? () => this.editRule(item) : null}>
                  <div className="before">{item.name}</div>
                  <div className="content ellipsis">{item.content.settleExpress}{item.content.customTaxExpress}</div>
                </div>
              ))
            }
          </Form.Item>
          <CommonUpload
            form={this.props.form}
            uploadText="上传凭证"
            uploadFileProps={this.getUploadFileProps({ fileName: 'closePz', label: '结算凭证：' })}
            formItemLayout={formItemLayout}
            uploadedFile={initialValue ? initialValue.files ? getFileList(initialValue.files) : null : null}
          />

          <Row type="flex" justify="start">
            <Col offset={2} span={8}>
              {edit && <Button
                type="primary"
                htmlType="submit"
                style={{ marginRight: 20 }}
                loading={this.props.settleLoading}
              >保存</Button>}
              <Button type="default" onClick={this.resetForm}>重置</Button>
            </Col>
          </Row>
        </Form>
      </div>
    </Fragment>)
  }

  render() {
    const { blockRule, festivalList, shopId, shopItemId, onEvent, priceRule, data, blockParams, shopParams, allSysDictList } = this.props;
    const isShow = shopParams.shopProtocol.internal === 1 ? false : false;
    return (
      <Fragment>
        <div className="optab-top-wrapper" style={{ border: 0 }}>
          <Tabs defaultActiveKey="1" type="card">
            <TabPane tab="Block规则" key="1">
              <Block
                blockRule={blockRule}
                blockParams={blockParams}
                festivalList={festivalList}
                shopId={shopId}
                shopItemId={shopItemId}
                onEvent={onEvent}
                blockLoading={this.props.blockLoading}
                allSysDictList={allSysDictList}
              />
            </TabPane>
            <TabPane tab="产品价格" key="2" disabled={isShow}>
              <Price
                blockRule={blockRule}
                festivalList={festivalList}
                shopId={shopId}
                shopItemId={shopItemId}
                onEvent={onEvent}
                priceRule={priceRule}
                data={data}
                priceLoading={this.props.priceLoading}
              />
            </TabPane>
            <TabPane tab="结算规则" key="3" disabled={isShow}>
              {
                this.renderClose()
              }
            </TabPane>
          </Tabs>
        </div>
      </Fragment>
    );
  }
}

export default ResourceOpTab;















