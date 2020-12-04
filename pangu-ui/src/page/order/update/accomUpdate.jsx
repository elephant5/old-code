import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { Form, Input, Button, Select, LocaleProvider, Radio, Col, Checkbox, Cascader, DatePicker, Divider, Tag, Modal } from 'antd';
import moment from 'moment';
import { BASE_PANGU_URL } from '../../../util/url';
const RadioGroup = Radio.Group;
const { Option } = Select;
const { TextArea } = Input;
@Form.create()
class AccomUpdate extends Component {
  constructor(props) {
    super(props);
    this.lastFetchId = 0;
    this.state = {
      resourceList: [],
      groupProduct: {},
      rooms: [],
      deparDateList: [],
      checked: false,
      groupProductList: [],
      tempShopList: [],
      tempShop: {},
      stopReason:0
    };
  }
  componentDidMount() {
    const { reservOrderInfo, newShopDetail, GoodsGroupListRes } = this.props;
    let rooms = []
    for (let i = 1; i < 10; i++) {
      let params = {};
      params.value = i + "";
      params.name = i + "间"
      rooms.push(params);
    }

    let deparDateList = []
    for (let i = 1; i < 10; i++) {
      let params = {};
      let time = moment(reservOrderInfo.giftDate).add('days', i).format('YYYY-MM-DD');
      params.value = time;
      params.name = (i + 1) + "天" + i + "晚（" + time + "离店）";
      params.night = i;
      deparDateList.push(params);
    }
    let shopItem;
    const { setFieldsValue } = this.props.form;
    let tempgroupProductList = [];
    if (reservOrderInfo.dispensing !== 1) {
      const resourceList = GoodsGroupListRes[0].groupProductList.filter(item => item.shopId === reservOrderInfo.shopId);
      if (resourceList.length > 0) {
        const { setFieldsValue } = this.props.form;
        setFieldsValue({ "productId": resourceList[0].productId });
      }
      this.setState({ resourceList: resourceList });
      tempgroupProductList = GoodsGroupListRes[0].groupProductList;
    } else {
      shopItem = newShopDetail.shopItemList.filter(item => item.id === reservOrderInfo.shopItemId)[0];
      let resourceList = [];
      newShopDetail.shopItemList.map((item, index) => {
        if (index === 0) {

          shopItem = item;
        }
        let temp = {};
        temp.productId = item.id;
        temp.productName = item.name;
        temp.addon = item.addon;
        temp.needs = item.needs;
        resourceList.push(temp);
      });
      setFieldsValue({ "productId": reservOrderInfo.shopItemId });
      this.setState({ resourceList: resourceList, checked: reservOrderInfo.dispensing === 1 ? true : false });
      let tempShop = {};
      tempShop.id = newShopDetail.shop.id;
      tempShop.hotelName = newShopDetail.shop.hotelName;
      tempShop.shopName = newShopDetail.shop.name;
      tempShop.gift = "调剂";
      tempgroupProductList.push(tempShop);
    }
    this.setState({ deparDateList: deparDateList, rooms: rooms, groupProductList: tempgroupProductList, checked: reservOrderInfo.dispensing === 1 ? true : false });


  }
  // 保存基本信息
  handleSubmit = e => {
    e.preventDefault();
    const { onEvent, closeEditModel, newShopDetail, reservOrderInfo, GoodsGroupListRes } = this.props;
    this.props.form.validateFields((err, values) => {
      const { resourceList, rooms, deparDateList } = this.state;
      if (!err) {
        //设置
        values.oldProductId = reservOrderInfo.productId;
        values.oldHotelName = reservOrderInfo.shopDetailRes.shop.hotelName;
        values.oldShopName = reservOrderInfo.shopDetailRes.shop.name;
        values.oldShopItemName = reservOrderInfo.shopItemName;
        values.oldGiftDate = reservOrderInfo.giftDate;
        values.oldDeparDate = reservOrderInfo.detail.deparDate;
        values.oldCheckNight = reservOrderInfo.detail.checkNight;
        values.oldAccoAddon = reservOrderInfo.detail.accoAddon;
        values.oldGiftName = reservOrderInfo.giftName;
        values.oldGiftPhone = reservOrderInfo.giftPhone;
        if (this.state.checked) {
          const shopItem = newShopDetail.shopItemList.filter(item => item.id === values.productId)[0];
          values.shopId = shopItem.shopId;
          values.shopItemId = shopItem.id;

        } else {
          const tempShopItem = resourceList.filter(item => item.id === values.shopId);
          const shopItem = newShopDetail.shopItemList.filter(item => item.id === tempShopItem[0].shopItemId)[0];

          values.shopId = tempShopItem[0].shopId;
          values.productGroupProductId = tempShopItem[0].id;
          values.exchangeNum = tempShopItem[0].pointOrTimes;
          values.shopItemId = shopItem.id;
        }

        values.memberId = reservOrderInfo.memberId;
        values.id = reservOrderInfo.id;
        let deparDate = deparDateList.filter(item => values.deparDate === item.value);
        values.nightNumbers = deparDate[0].night;
        values.dispensing = this.state.checked ? 1 : 0;
        // values.giftType = tempShopItem[0].gift;
        // values.productGroupId = productGroup[0].id;

        values.giftDate = moment(values.giftDate).format('YYYY-MM-DD');

        values.serviceType = reservOrderInfo.serviceTypeCode;
        onEvent('updateReservOrder', values);


      }
    });
  }

  componentWillReceiveProps(nextProps) {
    const { reservOrderInfo, newShopDetail, GoodsGroupListRes } = nextProps;
    if (this.state.checked) {
      let resourceList = [];
      newShopDetail.shopItemList.map((item, index) => {
        if (item.serviceName === reservOrderInfo.serviceType) {
          let temp = {};
          temp.productId = item.id;
          temp.productName = item.name;
          temp.addon = item.addon;
          temp.needs = item.needs;
          resourceList.push(temp);
        }
        if(item.enable ==1){
          this.setState({stopReason:1})
        }
      });
      this.setState({ resourceList: resourceList });
    }
  }

  selectShop = values => {
    const { newShopDetail, onEvent, GoodsGroupListRes, reservOrderInfo } = this.props;
    if (this.state.checked) {
      onEvent('getShopDetail', values);
      this.setState({ isshopDetail: true });
    } else {
      const groupProduct = GoodsGroupListRes[0].groupProductList.filter(item => item.id === values);//商品中产品资源的产品组和产品关联数据
      onEvent('getShopDetail', groupProduct[0].shopId);
      this.setState({ resourceList: groupProduct, groupProduct: groupProduct, });
      const { getFieldValue, setFieldsValue } = this.props.form;
      setFieldsValue({ "productId": groupProduct[0].productId });
      this.setState({stopReason:0})
      if (groupProduct[0].status == 1) {
          this.setState({stopReason:1})
      }
    }
  }
  onSearch = value => {
    const { reservOrderInfo } = this.props;
    console.log(value)
    var pattern = new RegExp("[\u4E00-\u9FA5]+");
    // var str = "中文字符"
    // if(pattern.test(str)){
    //   console.log('该字符串是中文');
    // }
    if (this.state.checked && pattern.test(value)) {
      this.lastFetchId += 1;
      const fetchId = this.lastFetchId;
      this.setState({ hoteNameData: [], fetching: true, groupProductList: [] });
      let params = {};
      params.shopName = value;
      params.shopType = reservOrderInfo.serviceType;
      params.gift = reservOrderInfo.giftType;
      fetch(BASE_PANGU_URL + '/shop/selectShopListByName',
        {
          method: 'post',
          body: JSON.stringify(params),
          //请求头
          headers: {
            'content-type': 'application/json'
          }
        }

      )
        .then(response => response.json())
        .then((body) => {
          if (fetchId !== this.lastFetchId) { // for fetch callback order
            return;
          }
          if (body.result && body.result.length > 0) {

            this.setState({ groupProductList: body.result, fetching: false });
          } else {

            // this.setState({ groupProductList: GoodsGroupListRes[0].groupProductList, fetching: false });
          }

        });
    }
  }
  onStartChange = (values, dateString) => {
    const { getFieldValue, setFieldsValue } = this.props.form;
    let rooms = [];

    for (let i = 1; i < 10; i++) {
      let params = {};
      let time = moment(values).add('days', i).format('YYYY-MM-DD');
      params.value = time;
      params.name = (i + 1) + "天" + i + "晚（" + time + "离店）";
      params.night = i;
      rooms.push(params);
    }

    this.setState({ deparDateList: rooms });
    setFieldsValue({ "deparDate": rooms[0].value });
  }
  onChange = e => {
    if (!e.target.checked) {
      const { GoodsGroupListRes, resourceList } = this.props;

      this.setState({ groupProductList: GoodsGroupListRes[0].groupProductList });
    } else {
      this.setState({ groupProductList: [] });
    }
    this.setState({
      checked: e.target.checked,
    });
  };
  render() {
    const { onEvent, closeEditModel, newShopDetail, reservOrderInfo, GoodsGroupListRes, updateOrderLoading } = this.props;
    const { resourceList, rooms, groupProductList, deparDateList,stopReason } = this.state;
    const { getFieldDecorator } = this.props.form;
    return (<Modal width={'50%'}
      title="变更订单预订信息"
      visible={true}
      onCancel={closeEditModel}
      onOk={this.handleSubmit}
      confirmLoading={updateOrderLoading}
      cancelText="取消"
      okText="确定"
    >
      <Form onSubmit={this.handleSubmit}>
        <Form.Item verticalGap={1} label="商户选择" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal" indicatorGap >
          {getFieldDecorator('shopId', { rules: [{ required: true, message: '请选择商户!' }], initialValue: reservOrderInfo.dispensing !== 1 ? reservOrderInfo.productGroupProductId : reservOrderInfo.shopId })
            (<Select showSearch onChange={this.selectShop} onSearch={this.onSearch}
              filterOption={(input, option) => option.props.children.indexOf(input) >= 0} style={{ width: '65%' }}>
              {GoodsGroupListRes && GoodsGroupListRes[0].groupProductList &&
                groupProductList.map(item => (
                  <Option key={item.id} value={item.id}>
                    {item.hotelName + (item.shopName ? "|" + item.shopName : "") + ("      (") + item.gift + (")")}
                  </Option>
                ))
              }

            </Select>
            )} &nbsp;<label style={{ fontSize: 12, color: "blue" }}>{newShopDetail && newShopDetail.shopProtocol && newShopDetail.shopProtocol.internal === 1 ? "公司资源" : null}
            {newShopDetail && newShopDetail.shopProtocol && newShopDetail.shopProtocol.internal === 0 ? "第三方资源" : null}</label>
          &nbsp;&nbsp;{getFieldDecorator('dispensing', {})(
            <Checkbox checked={this.state.checked} onChange={this.onChange}></Checkbox>
          )}&nbsp;使用调剂酒店
                    </Form.Item>
        <Form.Item verticalGap={1} label="产品选择" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal" indicatorGap >
          {getFieldDecorator('productId', { rules: [{ required: true, message: '请选择产品!' }] })(
            <RadioGroup  >
              {resourceList && resourceList.map((temp, index) => {
                console.log("newShopDetail",newShopDetail)
                console.log("stopReason",stopReason)
                return (<Radio value={temp.productId} checked={true}>{temp.productName}{temp.addon && "|" + temp.addon}{temp.needs && "|" + temp.needs}</Radio>);
              })}

            </RadioGroup>
          )}
        </Form.Item>

        {stopReason == 1 && newShopDetail && newShopDetail.shopItemList && <Form.Item verticalGap={1} label="停售原因" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal" indicatorGap >
                            {
                                newShopDetail.shopItemList.map((item, index) => {
                                    if(item.enable==1){
                                        return (
                                            <div>
                                                <Tag color="red">
                                                    {item.name}  :  {item.stopReason}
                                                </Tag><br></br>
                                            </div>)
                                    }
                                })
                            }
                        </Form.Item>}

        <Form.Item verticalGap={1} label="预订日期" labelCol={{ span: 5 }} wrapperCol={{ span: 10 }} direction="horizontal" indicatorGap >
          {getFieldDecorator('giftDate', { rules: [{ required: true, message: '请选择预订日期!' }], initialValue: reservOrderInfo.giftDate ? moment(reservOrderInfo.giftDate, 'YYYY-MM-DD') : null })
            (
              <DatePicker style={{ width: '100%' }}
                format="YYYY-MM-DD"
                placeholder="请选择预订日期"
                onChange={this.onStartChange}
              />
            )}
        </Form.Item>
        <Form.Item verticalGap={1} label="离店日期" labelCol={{ span: 5 }} wrapperCol={{ span: 10 }} direction="horizontal" indicatorGap >
          {getFieldDecorator('deparDate', { initialValue: reservOrderInfo.detail.deparDate })
            (
              <Select
                filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0} style={{ width: '78%' }}>
                {deparDateList && deparDateList.length > 0 &&
                  deparDateList.map(item => (
                    <Option key={item.value} value={item.value}>{item.name}
                    </Option>
                  ))
                }

              </Select>

            )}&nbsp;&nbsp;
                           {getFieldDecorator('checkNight', { initialValue: reservOrderInfo.detail.checkNight + "" })
            (
              <Select style={{ width: '20%' }} showSearch={true} tabIndex={0}
                filterOption={(input, option) =>
                  option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                } >
                {rooms && rooms.map((item, index) => {
                  if (index === 0) {
                    return (<Radio value={item.value} selected={"selected"}>{item.name}</Radio>);
                  } else {
                    return (<Radio value={item.value} >{item.name}</Radio>);
                  }
                })

                }
              </Select>

            )}

        </Form.Item>
        <Form.Item verticalGap={1} label="房型" labelCol={{ span: 5 }} wrapperCol={{ span: 10 }} direction="horizontal" indicatorGap >
          {getFieldDecorator('accoNedds', { initialValue: reservOrderInfo.detail.accoNedds })(
            <Select style={{ width: '100%' }}  >
              <Option key={'NULL'} value="NULL">无要求</Option>
              <Option value="bigbed">大床</Option>
              <Option selected value="doublebed">双床</Option>
              <Option value="trybigbed">尽量大床</Option>
              <Option value="trydoublebed">尽量双床</Option>
            </Select>)}
        </Form.Item>
        <Form.Item label="预约姓名" labelCol={{ span: 5 }} wrapperCol={{ span: 10 }} direction="horizontal" indicatorGap>
          {getFieldDecorator('giftName', { initialValue: reservOrderInfo.giftName })(
            <Input></Input>
          )}
        </Form.Item>
        {newShopDetail && newShopDetail.shop && newShopDetail.shop.countryId !== 'CN' &&
          <Form.Item label="使用人拼音" labelCol={{ span: 5 }} wrapperCol={{ span: 10 }} direction="horizontal" indicatorGap >
            {getFieldDecorator('detail.bookNameEn', { rules: [{ message: '请填写使用人拼音!' }], initialValue: reservOrderInfo.detail.bookNameEn }, {})(
              <Input allowClear={true} placeholder="请填写使用人拼音" />
            )}

          </Form.Item>
        }
        <Form.Item label="预约电话：" labelCol={{ span: 5 }} wrapperCol={{ span: 10 }} direction="horizontal" indicatorGap>
          {getFieldDecorator('giftPhone', { initialValue: reservOrderInfo.giftPhone })(
            <Input></Input>
          )}
        </Form.Item>
        <Form.Item label="备注" labelCol={{ span: 5 }} wrapperCol={{ span: 10 }} direction="horizontal" indicatorGap>
          {getFieldDecorator('reservRemark', { initialValue: reservOrderInfo.reservRemark })(<TextArea style={{ width: '100%', height: 50 }} />)}
        </Form.Item>
      </Form>
    </Modal>
    );
  }
}
export default AccomUpdate;