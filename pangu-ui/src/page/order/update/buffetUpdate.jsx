import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { Form, Input, Button, Select, LocaleProvider, Radio, Col, Checkbox, Cascader, DatePicker, Divider,Tag, Modal } from 'antd';
import moment from 'moment';
import { BASE_PANGU_URL } from '../../../util/url';
import cookie from 'react-cookies';
const RadioGroup = Radio.Group;
const { Option } = Select;
const { TextArea } = Input;
@Form.create()
class BuffetUpdate extends Component {
  constructor(props) {
    super(props);
    this.lastFetchId = 0;
    this.state = {
      resourceList: [],
      groupProduct: {},
      rooms: [],
      deparDateList: [],
      timeShow: [],
      isshopDetail: false,
      checked: false,
      groupProductList: [],
      tempShopList: [],
      tempShop: {},
      stopReason:0
    };
  }
  componentDidMount() {
    const { reservOrderInfo, newShopDetail, GoodsGroupListRes } = this.props;
    let shopItem;
    const { setFieldsValue } = this.props.form;
    let tempgroupProductList = [];
    if (reservOrderInfo.dispensing !== 1) {
      const resourceList = GoodsGroupListRes[0].groupProductList.filter(item => item.id === reservOrderInfo.productGroupProductId);
      this.setState({ resourceList: resourceList });

      setFieldsValue({ "productId": reservOrderInfo.productId });
      let temp = resourceList.filter(item => item.productId === reservOrderInfo.productId);
      shopItem = newShopDetail.shopItemList.filter(item => item.id === temp[0].shopItemId)[0];
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
        resourceList.push(temp);
      });
      let tempShop = {};
      tempShop.id = newShopDetail.shop.id;
      tempShop.hotelName = newShopDetail.shop.hotelName;
      tempShop.shopName = newShopDetail.shop.name;
      tempShop.gift = "调剂";
      tempgroupProductList.push(tempShop);
      setFieldsValue({ "productId": reservOrderInfo.shopItemId });
      this.setState({ resourceList: resourceList, checked: reservOrderInfo.dispensing === 1 ? true : false });
    }


    let timeShow = [];
    let start = shopItem.openTime ? shopItem.openTime.split(":")[0] : 0;
    let end = shopItem.closeTime ? shopItem.closeTime.split(":")[0] : 24;
    let startTail = shopItem.openTime ? shopItem.openTime.split(":")[1].trim() : 0;
    let endTail = shopItem.closeTime ? shopItem.closeTime.split(":")[1].trim() : 0;
    for (let i = start; i <= end; i++) {
      let tempValue = "00";
      if (i == start) {
        if (Number(startTail) <= 30 && Number(startTail) != 0) {
          tempValue = "30";
        } else if (Number(startTail) > 30) {
          i++;
        }
      }
      let temp = {};
      temp.value = i + ":" + tempValue;
      temp.name = shopItem.name + "-" + i + ":" + tempValue;
      timeShow.push(temp);
      if (tempValue == "00") {
        if ((i == end && Number(endTail) >= 30) || (i < end)) {
          tempValue = "30";
          let temp2 = {};
          temp2.value = i + ":" + tempValue;
          temp2.name = shopItem.name + "-" + i + ":" + tempValue;
          timeShow.push(temp2);
        }
      }
    }

    this.setState({ timeShow: timeShow, shopItem: shopItem, groupProductList: tempgroupProductList });
  }
  componentWillReceiveProps(nextProps) {

    const { newShopDetail, onEvent } = nextProps;

    if (newShopDetail.shop && this.state.isshopDetail) {
      let shopItem = newShopDetail.shopItemList[0];
      let timeShow = [];
      let start = shopItem.openTime ? shopItem.openTime.split(":")[0] : 0;
      let end = shopItem.closeTime ? shopItem.closeTime.split(":")[0] : 24;

      if (this.state.resourceList && this.state.resourceList.length > 1) {
        if (this.state.checked) {
          let resourceList = [];
          newShopDetail.shopItemList.map((item, index) => {
            if (index === 0) {

              shopItem = item;
            }
            let temp = {};
            temp.productId = item.id;
            temp.productName = item.name;
            resourceList.push(temp);
          });
          this.setState({ resourceList: resourceList });
          const { getFieldValue, setFieldsValue } = this.props.form;
          setFieldsValue({ "productId": resourceList[0].id });
          setFieldsValue({ "giftTime": null });

        } else {
          this.state.resourceList.map(item => {

            if (item.productName === shopItem.name) {
              const { getFieldValue, setFieldsValue } = this.props.form;
              setFieldsValue({ "productId": item.productId });
              setFieldsValue({ "giftTime": null });
            }
          })
        }


      } else {
        if (this.state.checked) {
          let resourceList = [];
          newShopDetail.shopItemList.map((item, index) => {
            if (index === 0) {

              shopItem = item;
            }
            let temp = {};
            temp.productId = item.id;
            temp.productName = item.name;
            resourceList.push(temp);
          });
          this.setState({ resourceList: resourceList });
          const { getFieldValue, setFieldsValue } = this.props.form;
          setFieldsValue({ "productId": resourceList[0].id });
          setFieldsValue({ "giftTime": null });
        } else {
          newShopDetail.shopItemList.map(item => {
            if (this.state.resourceList[0].productName === item.name) {
              const { getFieldValue, setFieldsValue } = this.props.form;
              setFieldsValue({ "productId": this.state.resourceList[0].productId });
              setFieldsValue({ "giftTime": null });
              shopItem = item;
            }
          });
        }

      }
      start = shopItem.openTime ? shopItem.openTime.split(":")[0] : 0;
      end = shopItem.closeTime ? shopItem.closeTime.split(":")[0] : 24;
      let startTail = shopItem.openTime ? shopItem.openTime.split(":")[1].trim() : 0;
      let endTail = shopItem.closeTime ? shopItem.closeTime.split(":")[1].trim() : 0;
      for (let i = start; i <= end; i++) {
        let tempValue = "00";
        if (i == start) {
          if (Number(startTail) <= 30 && Number(startTail) != 0) {
            tempValue = "30";
          } else if (Number(startTail) > 30) {
            i++;
          }
        }
        let temp = {};
        temp.value = i + ":" + tempValue;
        temp.name = shopItem.name + "-" + i + ":" + tempValue;
        timeShow.push(temp);
        if (tempValue == "00") {
          if ((i == end && Number(endTail) >= 30) || (i < end)) {
            tempValue = "30";
            let temp2 = {};
            temp2.value = i + ":" + tempValue;
            temp2.name = shopItem.name + "-" + i + ":" + tempValue;
            timeShow.push(temp2);
          }
        }
      }

      // this.props.actions.getPriceRule(rulesParams);
      this.setState({ timeShow: timeShow, isshopDetail: false, shopItem: shopItem });
    }
  }
  // 保存基本信息
  handleSubmit = e => {
    e.preventDefault();
    const { onEvent, closeEditModel, newShopDetail, reservOrderInfo, GoodsGroupListRes } = this.props;
    this.props.form.validateFields((err, values) => {
      const { resourceList, rooms, deparDateList } = this.state;
      if (!err) {
        values.oldProductId = reservOrderInfo.productId;
        values.oldHotelName = reservOrderInfo.shopDetailRes.shop.hotelName;
        values.oldShopName = reservOrderInfo.shopDetailRes.shop.name;
        values.oldShopItemName = reservOrderInfo.shopItemName;
        values.oldGiftDate = reservOrderInfo.giftDate;
        values.oldGiftName = reservOrderInfo.giftName;
        values.oldGiftPhone = reservOrderInfo.giftPhone;
        values.oldGiftTime = reservOrderInfo.giftTime;
        if (this.state.checked) {
          values.oldShopId = reservOrderInfo.shopId;
          const shopItem = newShopDetail.shopItemList.filter(item => item.id === values.productId)[0];
          values.shopId = shopItem.shopId;
          values.shopItemId = shopItem.id;

        } else {
          const tempShopItem = resourceList.filter(item => item.id === values.shopId);
          const shopItem = newShopDetail.shopItemList.filter(item => item.id === tempShopItem[0].shopItemId)[0];
          values.shopId = tempShopItem[0].shopId;
          values.productGroupProductId = tempShopItem[0].id;
          values.shopItemId = shopItem.id;
          values.exchangeNum = tempShopItem[0].pointOrTimes;
        }
        values.id = reservOrderInfo.id;
        values.memberId = reservOrderInfo.memberId;
        // values.giftType = tempShopItem[0].gift;
        // values.productGroupId = productGroup[0].id;
        values.dispensing = this.state.checked ? 1 : 0;
        values.giftDate = moment(values.giftDate).format('YYYY-MM-DD');


        onEvent('updateReservOrder', values);


      }
    });
  }
  selectShop = values => {
    const { onEvent, GoodsGroupListRes, reservOrderInfo } = this.props;
    if (this.state.checked) {
      onEvent('getShopDetail', values);
      this.setState({ isshopDetail: true });
    } else {
      const groupProduct = GoodsGroupListRes[0].groupProductList.filter(item => item.id === values);//商品中产品资源的产品组和产品关联数据
      onEvent('getShopDetail', groupProduct[0].shopId);
      this.setState({ resourceList: groupProduct, groupProduct: groupProduct, isshopDetail: true });
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

  }
  checkProduct = e => {
    const { getFieldValue, setFieldsValue } = this.props.form;
    setFieldsValue({ "giftTime": null });
    const { onEvent, GoodsGroupListRes, newShopDetail, reservOrderInfo } = this.props;
    let tempShopItem;
    let shopItem;
    if (this.state.checked) {
      let shopId = e.target.value;
      shopItem = newShopDetail.shopItemList.filter(item => item.id === shopId)[0];
      // shopItem = this.state.shopItem;
    } else {
      tempShopItem = GoodsGroupListRes[0].groupProductList.filter(item => item.productId === e.target.value);
      shopItem = newShopDetail.shopItemList.filter(item => item.id === tempShopItem[0].shopItemId)[0];
    }


    let timeShow = [];
    let start = shopItem.openTime ? shopItem.openTime.split(":")[0] : 0;
    let end = shopItem.closeTime ? shopItem.closeTime.split(":")[0] : 24;
    let startTail = shopItem.openTime ? shopItem.openTime.split(":")[1].trim() : 0;
    let endTail = shopItem.closeTime ? shopItem.closeTime.split(":")[1].trim() : 0;
    for (let i = start; i <= end; i++) {
      let tempValue = "00";
      if (i == start) {
        if (Number(startTail) <= 30 && Number(startTail) != 0) {
          tempValue = "30";
        } else if (Number(startTail) > 30) {
          i++;
        }
      }
      let temp = {};
      temp.value = i + ":" + tempValue;
      temp.name = shopItem.name + "-" + i + ":" + tempValue;
      timeShow.push(temp);
      if (tempValue == "00") {
        if ((i == end && Number(endTail) >= 30) || (i < end)) {
          tempValue = "30";
          let temp2 = {};
          temp2.value = i + ":" + tempValue;
          temp2.name = shopItem.name + "-" + i + ":" + tempValue;
          timeShow.push(temp2);
        }
      }
    }


    this.setState({stopReason:0})
    newShopDetail.shopItemList.map((item, index) => {
      if(item.enable==1){
        this.setState({stopReason:1})
      }
  })
    this.setState({ timeShow: timeShow, shopItem: shopItem });
  }
  onChange = e => {
    if (!e.target.checked) {
      const { GoodsGroupListRes } = this.props;

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
    const { resourceList, rooms, deparDateList, timeShow, groupProductList,stopReason } = this.state;
    const { getFieldDecorator } = this.props.form;
    const adjust = cookie.load("KLF_PG_OL_ADJUST");
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
          &nbsp;&nbsp;
                        {getFieldDecorator('dispensing', {})
            (
              <Checkbox checked={this.state.checked} onChange={this.onChange}></Checkbox>
            )}&nbsp;使用调剂酒店
                    </Form.Item>
        <Form.Item verticalGap={1} label="产品选择" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal" indicatorGap >
          {getFieldDecorator('productId', { rules: [{ required: true, message: '请选择产品!' }] })(
            <RadioGroup onChange={this.checkProduct}>
              {resourceList.map((temp, index) => {
                return (<Radio value={temp.productId} checked={true}>{temp.productName}</Radio>);
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
        <Form.Item verticalGap={1} label="预订时间" labelCol={{ span: 5 }} wrapperCol={{ span: 10 }} direction="horizontal" indicatorGap >
          {getFieldDecorator('giftTime', { rules: [{ required: true, message: '请选择预订时间!' }], initialValue: reservOrderInfo.giftTime })
            (
              <Select
                filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0} style={{ width: '78%' }}>
                {timeShow &&
                  timeShow.map(item => (
                    <Option key={item.value} value={item.value}>{item.name}
                    </Option>
                  ))
                }

              </Select>

            )}&nbsp;&nbsp;
                            {getFieldDecorator('giftPeopleNum', { initialValue: reservOrderInfo.giftPeopleNum })
            (
              <Select style={{ width: '20%' }} showSearch={true} tabIndex={0}
                filterOption={(input, option) =>
                  option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                } >
                <Option key={'1ren'} value="1">1人</Option>
                <Option key={'2ren'} value="2">2人</Option>
                <Option key={'3ren'} value="3">3人</Option>
                <Option key={'4ren'} value="4">4人</Option>
                <Option key={'5ren'} value="5">5人</Option>
                <Option key={'6ren'} value="6">6人</Option>
                <Option key={'7ren'} value="7">7人</Option>
                <Option key={'8ren'} value="8">8人</Option>
                <Option key={'9ren'} value="9">9人</Option>
                <Option key={'10ren'} value="10">10人</Option>
              </Select>

            )}
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
export default BuffetUpdate;