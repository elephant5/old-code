import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { bindActionCreators } from 'redux';
import * as actions from '../../../store/resource/action';
import * as commonActions from '../../../store/common/action';
import { Form, Icon, Input, Button, Select, Row, Col, Modal, InputNumber, TimePicker, TreeSelect } from 'antd';
import './resource.less';
import CommonUpload from '../../../component/common-upload/index';
import { FILE_TYPE } from '../../../config/index';
import moment from 'moment';
import BraftEditor from 'braft-editor'
import { UPLOAD_URL } from '../../../util/url';
import { getHttpPro } from '../../../util/util';
import './resource.less';
import cookie from 'react-cookies';


const format = 'HH:mm';
const SHOW_PARENT = TreeSelect.SHOW_PARENT;
const { Option } = Select;
const { TextArea } = Input;
const formItemLayout = {
  labelCol: { span: 4 },
  wrapperCol: { span: 12 },
};
const formItemLayout3 = {
  labelCol: { span: 6 },
  wrapperCol: { span: 12 },
};
const formItemLayout2 = {
  labelCol: { span: 2 },
  wrapperCol: { span: 18 },
};

const myUploadFn = (param) => {

  const serverURL = UPLOAD_URL
  const xhr = new XMLHttpRequest
  const fd = new FormData()

  const successFn = (response) => {
    // 假设服务端直接返回文件上传后的地址
    // 上传成功后调用param.success并传入上传后的文件地址
    // console.log("response")
    // console.log(response)
    // console.log(response.srcElement)
    // console.log(response.target)
    // console.log(response.srcElement.response)
    // console.log(response.target.response)
    // console.log(response.target.response.code)
    // console.log(response.target.response.result)
    if (response && response.target && response.target.response) {
      const res = JSON.parse(response.target.response);
      // console.log(res);
      if (res.code === 100 && res.result) {
        const result = res.result;
        // console.log(111111)
        // const fileUrl=result.pgCdnHttpUrl+'/'+result.guid+'.'+result.ext;
        const fileUrl = getHttpPro() + result.pgCdnNoHttpFullUrl;
        // console.log(fileUrl)
        param.success({
          url: fileUrl,
          // meta: {
          //     // id: 'xxx',
          //     // title: 'xxx',
          //     // alt: 'xxx',
          //     // loop: true, // 指定音视频是否循环播放
          //     autoPlay: true, // 指定音视频是否自动播放
          //     controls: true, // 指定音视频是否显示控制栏
          //     poster: fileUrl, // 指定视频播放器的封面
          // }
        })
      }
    }

  }

  const progressFn = (event) => {
    // 上传进度发生变化时调用param.progress
    param.progress(event.loaded / event.total * 100)
  }

  const errorFn = (response) => {
    // 上传发生错误时调用param.error
    param.error({
      msg: 'unable to upload.'
    })
  }

  xhr.upload.addEventListener("progress", progressFn, false)
  xhr.addEventListener("load", successFn, false)
  xhr.addEventListener("error", errorFn, false)
  xhr.addEventListener("abort", errorFn, false)
  // xhr.setRequestHeader("Content-Type","application/json");
  fd.append('file', param.file)
  fd.append('fileType', 'hotel.pic')
  xhr.open('POST', serverURL, true)
  xhr.send(fd)

}
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
@withRouter
@Form.create()
class ResourceBasic extends Component {
  constructor(props) {
    super(props);
    this.fileList = [];
    this.state = {
      value: ["105"],
      treeData: [],
      visible: false,
      resourceLength: 0,
      resourceValue: [],
      temp: "",
      serviceGiftLists: [],

      Resourcestatus: [
        { id: "0", name: "在售" },
        { id: "1", name: "停售" }
      ],

      style: { display: "none", color: "red" },
      stopReason: 0
    }
  }

  showhide = (e) => {
    if (e === "0") {
      this.setState({ stopReason: 0 });
      this.setState({ style: { display: "none", color: "red" } })
    }
    else {
      this.setState({ style: { display: "block", color: "#000" } });
      this.setState({ stopReason: 1 });
    }
  }


  componentDidMount() {

    const { shop = {}, countryCity, serviceType } = this.props;
    // const { treeData } = this.state;


    if (shop.enable == "1") {
      this.showhide("1");
    }



    this.props.actions.serviceGiftGet(serviceType.code);
    const treeDataTemp = [];
    countryCity.map((item, index) => {
      if (item.cityDetail.length === 0) {
        return;
      }
      let params = {};
      params.title = item.countryName;
      params.value = item.countryName;
      params.key = "0-" + index;
      params.disabled = true;
      params.disableCheckbox = true;
      let children = [];
      item.cityDetail.map((citys, index) => {
        let temp = {};
        temp.title = citys.cityName;
        temp.value = citys.cityId;
        temp.key = params.key + "-" + index;
        children.push(temp);
      });
      params.children = children;
      treeDataTemp.push(params);
    });

    this.setState({
      treeData: treeDataTemp,
      resourceLength: shop.gift ? shop.gift.split(",").length : 0,
      resourceValue: shop.gift ? shop.gift.split(",") : []
      // startDate: startDate,
      // endDate: endDate,
    });

    // 异步设置编辑器内容
    // setTimeout(() => {
    //   this.props.form.setFieldsValue({
    //     //   msgContent: BraftEditor.createEditorState(listTextInfo);
    //     menuText: shop.menuText ? BraftEditor.createEditorState(shop.menuText) : ''
    //   });
    //   // this.onChange(value);
    // }, 1000);
  }

  componentWillReceiveProps(nextProps) {
    const { operation, resource, common } = nextProps;
    switch (operation.type) {

      case actions.GET_SERVICEGIFTGET_SUCCESS:

        this.setState({
          serviceGiftLists: resource.serviceGiftList.result
        })
        break;

      default:
        break;
    }
  }
  onChange = (value) => {
    this.setState({ value });
  }

  getUploadFileProps = params => {
    return {
      fileName: params.fileName,
      fileType: 'shop.item.pic',
      options: {
        // 上传回调
        onChange: (info, status) => {
          switch (status) {
            case 'done':
              // const params = {...info, objId: this.props.id}
              // this.fileList = this.fileList.concat(params);
              const result = info.map(item => {
                item.objId = this.props.id;
                return item;
              })
              this.fileList = result;
              break;
            case 'removed':
              const result2 = info.map(item => {
                item.objId = this.props.id;
                return item;
              })
              this.fileList = result2
              // this.fileList = this.fileList.filter(item => info.code !== item.code)
              break;
            default:
              break;
          }
        }
      },
      config: {
        label: this.props.serviceType.code === 'accom' ? '房型图片' : this.props.serviceType.name + '图片',
        type: FILE_TYPE.IMAGE,
        // size: 1024*1024,
        // num: 20
      }
    }
  }
  handleSubmit = e => {
    e.preventDefault();
    const { form, onEvent, editId } = this.props;
    const { editorState } = this.state;
    form.validateFields((err, values) => {
      if (!err) {
        var sprson = values.stopReason;
        if (values.enable === "0") {
          sprson = ""
        } else {
          sprson = values.stopReason
        }
        const params = {
          "addon": values.addon,
          "cancelPolicy": values.cancelRule,
          "closeTime": values.closeTime ? moment(values.closeTime).format(format) : '',
          "enable": values.enable,
          "menuText": editorState ? editorState.toHTML() : null,
          "cityList": values.citys,
          "giftList": values.giftList,
          "name": values.name,
          "needs": values.needs,
          "openTime": values.openTime ? moment(values.openTime).format(format) : '',
          "retainRoom": values.reserveRoom,
          "serviceType": this.props.serviceType.code,
          "shopId": this.props.id,
          "id": this.props.id,
          "xcAvgPrice": values.ctripAvgPrice,
          "xcMaxPrice": values.ctripHighPrice,
          "xcMinPrice": values.ctripLowPrice,
          "thirdCpnNum": values.thirdCpnNum,
          "thirdCpnName": values.thirdCpnName,
          "expressMode": values.expressMode,
          "stopReason": sprson,
          fileList: this.fileList
        }
        editId > 0 ? onEvent('editSize', params) : onEvent('addSize', params);
        console.log("sprson" + sprson)
        // 清空图片
        this.fileList = [];
      }
    })
  }
  handleChange = (editorState) => {
    this.setState({
      editorState: editorState,
    })
  }
  onChangeValue = (value) => {
    const { resourceValue } = this.state;
    this.setState({
      visible: true,
      temp: value
    });
  }
  addResourceValue = (value) => {
    const { resourceValue } = this.state;
    resourceValue.push(value);
    this.setState({
      resourceValue: resourceValue,
    });

  }
  showModal = () => {
    this.setState({
      visible: true,
    });
  }

  handleOk = (e) => {
    const { resourceValue, temp } = this.state;
    this.setState({
      visible: false,
      resourceValue: this.state.resourceValue.filter(item => item !== temp)
    });
  }

  handleCancel = (e) => {
    const { resourceValue, value } = this.state;
    const { getFieldValue, setFieldsValue } = this.props.form;
    setFieldsValue({ "giftList": resourceValue });
    this.setState({
      visible: false,
    });
  }
  render() {
    // this.showhide() 
    const { shop = {}, id = 0, shopType, serviceType, shopParams, countryCity, giftTypeList, serviceGiftList } = this.props;
    const { getFieldDecorator } = this.props.form;
    const { treeData, serviceGiftLists, stopReason } = this.state;
    const children = [];
    const edit = cookie.load("KLF_PG_RM_SL_SERVICE_EDIT");
    giftTypeList.map(item => {
      if (serviceGiftLists.length > 0) {
        serviceGiftLists.map(gift => {
          if (item.code === gift.giftId) {
            children.push(<Option key={item.code} value={item.code}>{item.shortName}</Option>);
          }
        });
      } else {
        serviceGiftList.map(gift => {
          if (item.code === gift.giftId) {
            children.push(<Option key={item.code} value={item.code}>{item.shortName}</Option>);
          }
        });
      }

    })
    const formItemLayout5 = {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 6 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
    };
    return (
      <Fragment>
        <div className="shop-add-content">
          <Form onSubmit={this.handleSubmit}>
            <Form.Item style={{ display: 'none' }} label="ID"   >
              {getFieldDecorator('id', {
                initialValue: id,
              })(
                <Input />
              )}
            </Form.Item>

            <Row gutter={24}>
              <Col xl={8} md={12} sm={24}>
                <Form.Item  {...formItemLayout5} label={`${serviceType.code === 'accom' ? '房型' : serviceType.name}名称`} >
                  {getFieldDecorator('name', {
                    initialValue: shop.name,
                    rules: [{ required: true, message: `请输入${serviceType.code === 'accom' ? '房型' : serviceType.name}名称` }]
                  })(
                    <Input style={{ width: "100%" }} />
                  )}
                </Form.Item>
              </Col>
              {serviceType.code !== 'accom' && shopParams.shopType !== 'coupon' && shopParams.shopType !== 'trip' && <Col xl={5} md={12} sm={24}>
                <Form.Item
                  label={serviceType.code === 'buffet' ? '开餐时间起' : "开始时间"}
                  {...formItemLayout5}
                >
                  {getFieldDecorator('openTime', {
                    rules: [{
                      required: true, message: serviceType.code === 'buffet' ? '请选择开餐时间' : `请选择开始时间`,
                    }],
                    initialValue: shop.openTime ? moment(shop.openTime, format) : moment('00:00', format),
                  })(
                    <TimePicker placeholder="开始时间" format={format} />
                  )}
                </Form.Item>
              </Col>}
              {serviceType.code !== 'accom' && shopParams.shopType !== 'coupon' && shopParams.shopType !== 'trip' && <Col xl={5} md={12} sm={24}>
                <Form.Item
                  label="结束时间"
                  {...formItemLayout5}
                >
                  {getFieldDecorator('closeTime', {
                    rules: [{
                      required: true, message: `请选择结束时间`,
                    }],
                    initialValue: shop.closeTime ? moment(shop.closeTime, format) : moment('00:00', format),
                  })(
                    <TimePicker placeholder="结束时间" format={format} />
                  )}
                </Form.Item>
              </Col>}
              <Col xl={5} md={12} sm={24}>
                <Form.Item label="资源状态：" {...formItemLayout5}>
                  {getFieldDecorator('enable', {
                    initialValue: shop.enable ? shop.enable + "" : "0",
                    rules: [{ required: true, message: '请选择资源状态' }]
                  })(
                    <Select onChange={this.showhide}>
                      {
                        this.state.Resourcestatus.map((item, i) => {
                          return (
                            <option key={i} value={item.id}>
                              {item.name}
                            </option>
                          )
                        })}
                    </Select>
                  )}
                </Form.Item>
                <Form.Item dispaly style={this.state.style} label="停售原因" {...formItemLayout5}>
                  {getFieldDecorator('stopReason', {
                    initialValue: shop.stopReason,
                    rules: [{ required: stopReason == 1 ? true : false, message: '请选择停售原因' }]
                  })(
                    <Select>
                      <Option value="合同关餐">合同关餐</Option>
                      <Option value="酒店特殊节假日活动">酒店特殊节假日活动</Option>
                      <Option value="酒店特殊活动调价">酒店特殊活动调价</Option>
                      <Option value="酒店跑量">酒店跑量</Option>
                      <Option value="酒店合同到期未续签">酒店合同到期未续签</Option>
                      <Option value="散客满餐">散客满餐</Option>
                      <Option value="团队关餐">团队关餐</Option>
                      <Option value="酒店宴会">酒店宴会</Option>
                      <Option value="酒店不再续约">酒店不再续约</Option>
                      <Option value="酒店装修">酒店装修</Option>
                      <Option value="已下线">已下线</Option>
                      <Option value="产品调整">产品调整</Option>
                    </Select>)}
                </Form.Item>
              </Col>
            </Row>

            <Row gutter={24}>
              {<Col xl={8} md={12} sm={24}>
                <Form.Item label="第三方产品编号" {...formItemLayout5}>
                  {getFieldDecorator("thirdCpnNum", { initialValue: shop.thirdCpnNum })(
                    <Input />
                  )}
                </Form.Item>
              </Col>}
              {<Col xl={8} md={12} sm={24}>
                <Form.Item label="第三方产品名称" {...formItemLayout5}>
                  {getFieldDecorator("thirdCpnName", { initialValue: shop.thirdCpnName })(
                    <Input />
                  )}
                </Form.Item>
              </Col>}
            </Row>

            {serviceType.code === 'object_cpn' && <Row gutter={24}>
              <Col xl={8} md={12} sm={24}>
                <Form.Item label="配送方式" {...formItemLayout5}>
                  {getFieldDecorator("expressMode", { initialValue: shop.expressMode+"" })(
                    <Select style={{ width: 140 }}>
                      <Option value="0">无需配送</Option>
                      <Option value="1">快递发货</Option>
                      <Option value="2">及时送达</Option>
                      <Option value="3">到店自取</Option>
                    </Select>
                  )}
                </Form.Item>
              </Col>
            </Row>}

            {serviceType.code === 'accom' && (

              <Form.Item
                labelCol={{ span: 2 }}
                wrapperCol={{ span: 22 }}
                label="床型"
              >
                {getFieldDecorator('needs', {
                  rules: [{ required: true, message: '请输入床型' }],
                  initialValue: shop.needs || '大床',
                })(
                  <Select style={{ width: 140 }}>
                    <Option value="大/双">大/双</Option>
                    <Option value="大床">大床</Option>
                    <Option value="双床">双床</Option>
                    <Option value="三小床">三小床</Option>
                    <Option value="二中床">二中床</Option>
                    <Option value="二大床">二大床</Option>
                  </Select>
                )}<label style={{ marginLeft: "10px" }}>餐型：</label>
                {getFieldDecorator('addon', {
                  initialValue: shop.addon || '无早',
                  rules: [{ required: true, message: '请输入餐型' }]
                })(
                  <Select style={{ width: 140 }}>
                    <Option value="无早">无早</Option>
                    <Option value="单早">单早</Option>
                    <Option value="双早">双早</Option>
                    <Option value="三早">三早</Option>
                    <Option value="四早">四早</Option>
                  </Select>
                )}<label style={{ marginLeft: "10px" }}>取消政策：</label>
                {getFieldDecorator('cancelRule', {
                  initialValue: shop.cancelPolicy || 0,
                  rules: [{ required: false }]
                })(
                  <Select style={{ width: 144 }}>
                    <Option value="">请选择</Option>
                    <Option value="1">不可变更</Option>
                    <Option value="2">提前24小时</Option>
                    <Option value="3">提前48小时</Option>
                    <Option value="4">可变更</Option>
                  </Select>
                )}<label style={{ marginLeft: "10px" }} >保留房间：</label>
                {getFieldDecorator('reserveRoom', {
                  rules: [
                    { required: false },
                    { pattern: /^\d*$/, message: '大于等于0的整数' }
                  ],
                  initialValue: shop.retainRoom || 0,
                })(
                  <InputNumber style={{ width: 140 }} />
                )}
                <span className="ant-form-text">间</span>
              </Form.Item>
            )}
            {(shopParams.shopType === 'buffet' || shopParams.shopType === 'gym' || shopParams.shopType === 'spa') && <Row gutter={24}>
              <Col xl={8} md={12} sm={24}>
                <Form.Item
                  label="权益类型"
                  {...formItemLayout5}
                >

                  {getFieldDecorator('giftList', {
                    initialValue: shop.gift ? shop.gift.split(",") : []
                  })(
                    <Select
                      mode="multiple"
                      placeholder="请选择权益类型"
                      // value={selectedItems}
                      onDeselect={this.onChangeValue}
                      onSelect={this.addResourceValue}
                      style={{ width: '100%' }}
                      showArrow={true}
                    >
                      {children}
                    </Select>
                  )}<br></br><label style={{ fontSize: 12, color: "red" }}>注：取消选中后，该权益对应的产品即将停售，请谨慎取消，保存后无法撤回！</label>
                  <Modal
                    title="删除权益类型,确定删除吗？"
                    visible={this.state.visible}
                    onOk={this.handleOk}
                    onCancel={this.handleCancel}
                    cancelText="取消"
                    okText="确定"
                  >
                    <p><label style={{ fontSize: 12, color: "red" }}>注：取消选中后，该权益对应的产品即将停售，请谨慎取消，保存后无法撤回！</label></p>

                  </Modal>
                </Form.Item>
              </Col>
              {<Col xl={5} md={12} sm={24}> <Form.Item label="取消政策"  {...formItemLayout5}>
                {getFieldDecorator('cancelRule', {
                  initialValue: shop.cancelPolicy ? shop.cancelPolicy + "" : "1",
                  rules: [{ required: true, message: '请选择取消政策' }]
                })(
                  <Select style={{}}>
                    <Option value="1">不可变更</Option>
                    <Option value="2">提前24小时</Option>
                    <Option value="3">提前48小时</Option>
                    <Option value="4">可变更</Option>
                  </Select>
                )}
              </Form.Item></Col>}
            </Row>}
            {shopParams.shopType === 'trip' && <Form.Item
              label="适用城市"
              {...formItemLayout2}
            >

              {getFieldDecorator('citys', {
                initialValue: null
              })(
                <TreeSelect
                  showSearch
                  allowClear={true}
                  style={{ width: "50%" }}
                  value={this.state.value}
                  dropdownStyle={{ maxHeight: 400, overflow: 'auto' }}
                  treeData={treeData}
                  placeholder="请选择适用城市，可多选"
                  treeCheckable={true}
                  treeDefaultExpandAll={true}
                  // treeCheckStrictly={true}
                  treeNodeFilterProp="title"
                  onChange={this.onChange}
                />
              )}
              <label style={{ fontSize: 12, marginLeft: 10 }}>注：城市可多选，可搜索选择！</label>
            </Form.Item>}
            <CommonUpload
              uploadedFile={this.props.uploadedFile}
              form={this.props.form}
              uploadFileProps={this.getUploadFileProps({ fileName: 'pic' })}
            />
            <br></br>
            {serviceType.code === 'accom' && (
              <Form.Item labelCol={{ span: 2 }}
                wrapperCol={{ span: 22 }}
                label="携程价格："
              >
                <label  >最低价：</label>
                {getFieldDecorator('ctripLowPrice', {
                  rules: [
                    { required: false },
                    { pattern: /^\d*$/, message: '大于等于0的整数' }
                  ],
                  initialValue: shop.xcMinPrice || 0,
                })(
                  <InputNumber />
                )}<label style={{ marginLeft: "10px" }} >最高价：</label>
                {getFieldDecorator('ctripHighPrice', {
                  rules: [
                    { required: false },
                    { pattern: /^\d*$/, message: '大于等于0的整数' }
                  ],
                  initialValue: shop.xcMaxPrice || 0,
                })(
                  <InputNumber />
                )}<label style={{ marginLeft: "10px" }} >平均价：</label>
                {getFieldDecorator('ctripAvgPrice', {
                  rules: [
                    { required: false },
                    { pattern: /^\d*$/, message: '大于等于0的整数' }
                  ],
                  initialValue: shop.xcAvgPrice || 0,
                })(
                  <InputNumber />
                )}
              </Form.Item>
            )}

            {serviceType.code !== 'accom' && <Form.Item
              label="资源说明"
              {...formItemLayout2}
            >

              {getFieldDecorator('menuText', {
                initialValue: BraftEditor.createEditorState(shop.menuText) || '',
              })(
                <BraftEditor
                  style={{ width: '100%', border: 'solid 1px #ccc', marginTop: 10 }}
                  placeholder="请输入"
                  // converts={{ unitImportFn, unitExportFn }}
                  onChange={this.handleChange}
                  media={{ uploadFn: myUploadFn, accepts: { video: false, audio: false }, externals: { video: false, audio: false, embed: false, image: false } }}
                />
              )}
            </Form.Item>}
            <Row type="flex" justify="start">
              <Col offset={2} span={8}>
                {edit && <Button
                  type="primary"
                  htmlType="submit"
                  style={{ marginRight: 8 }}
                  loading={this.props.addLoading}
                >
                  保存
                </Button>}
                {/* <Button style={{marginRight: 8}} type="default" onClick={() => {this.props.onEvent('deleteSize', this.props.id)}}>删除</Button> */}
                <Button type="default" onClick={() => { this.props.onEvent('displayDiv', this.props.id) }}>关闭</Button>
              </Col>
            </Row>
          </Form>
        </div>
      </Fragment>
    );
  }
}

export default ResourceBasic;