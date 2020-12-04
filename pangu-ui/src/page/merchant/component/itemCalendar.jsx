/* eslint-disable no-unused-vars */
import React, { Component, Fragment } from 'react';
import { Form, Icon, Input, Button, Select, Row, Col, Badge, Tag, Calendar, message } from 'antd';
import './resourceCalendar.less';
import moment from 'moment';
import * as actions from '../../../store/resource/action';
import * as commonActions from '../../../store/common/action';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { bindActionCreators } from 'redux';
import './resource.less';
const { Option } = Select;
const formItemLayout = {
  labelCol: { span: 6 },
  wrapperCol: { span: 12 },
};
const statusMap = ['processing', 'error', 'success', 'warning', "default"];
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
class ItemCalendar extends Component {
  constructor(props) {
    super(props)
    this.state = {
      // currentDay: this.getFirstDay(),
      temp: 0,
      giftArr: [],
      colors: ['Success', 'Error', 'Default', 'Processing', 'Warning'],
      shopItemCalendarList: [],
      isChangedPriceLocal: false,
      currentMonth: this.getCurrentMonth(),
      currentYear: this.getCurrentYear(),
      serviceType: {}
    }
  }

  //   // 获取开始日期当月的1号
  //   currentYear = () => {
  //   const { startDate } = this.props;
  //   const [year, month, ] = moment(startDate).format('YYYY-MM-DD').split('-');
  //   return new Date(`${year}-${month}-01`); 
  // }

  getCurrentYear = () => {
    //获取当前时间
    let date = new Date();
    let currentYear = date.getFullYear();
    return currentYear;
  }
  // 获取开始日期当月的1号
  getCurrentMonth = () => {
    //获取当前时间
    let date = new Date();
    //获取当前月的第一天     
    let monthStart = date.setDate(1);
    //获取当前月 
    let currentMonth = date.getMonth() + 1;
    this.setState({ currentYear: date.getFullYear() });
    return currentMonth;
  }
  componentDidMount() {
    const { shop, shopItem, shopType, startDate, endDate, currentDay, giftTypeList, isChangedPrice, serviceType } = this.props;
    const { currentMonth, currentYear } = this.state;
    let params = {
      shopId: shop.id,
      shopItemId: shopItem.id,
      year: currentYear,
      month: currentMonth
    }
    this.setState({ serviceType: serviceType });
    this.props.actions.getShopItemSettlePrice(params);
    this.getgiftArr(giftTypeList, shopItem, shop);
  }
  getgiftArr = (giftTypeList, shopItem, shop) => {
    let giftArr = [];
    const doc = ["magenta", "red", "volcano", "orange", "gold", "lime", "green", "cyan", "blue", "geekblue", "purple"];
    const giftArray = ["car", "lounge", "accom", "trip_cpn", "movie_cpn", "sweet_cpn", "video_cpn", "charge_cpn", "coffee_cpn","snacks_cpn","others_cpn","object_cpn"];
    if (shopItem.serviceType === 'accom' || shopItem.serviceType === 'bakery_cpn' || shopItem.serviceType === 'car' || shopItem.serviceType === 'lounge'
      || shopItem.serviceType === 'trip_cpn' || shopItem.serviceType === 'movie_cpn' || shopItem.serviceType === 'sweet_cpn'
      || shopItem.serviceType === 'video_cpn' || shopItem.serviceType === 'charge_cpn' || shopItem.serviceType === 'coffee_cpn' || shopItem.serviceType === 'music_cpn'
      || shopItem.serviceType === 'suppermarket_cpn' || shopItem.serviceType === 'takeout_cpn' || shopItem.serviceType === 'tooth_cpn' || shopItem.serviceType === 'snacks_cpn'
      || shopItem.serviceType === 'others_cpn' || shopItem.serviceType === 'object_cpn' || shopItem.serviceType === 'medical'
    ) {
      let temp = {};
      temp.color = doc[0];
      temp.code = shopItem.serviceType;
      temp.shortName = shopItem.serviceName;
      giftArr.push(temp);
    } else {
      giftTypeList.map(item => {
        if (shopItem.gift) {
          shopItem.gift.split(",").map(gift => {
            if (gift === item.code) {
              let tems = Math.floor(Math.random() * 11);
              let temp = {};
              temp.color = doc[tems];
              temp.code = item.code;
              temp.shortName = item.shortName;
              giftArr.push(temp);
            }
          });
        }
      })
    }

    this.setState({
      giftArr: giftArr
    });


  }
  componentWillReceiveProps(nextProps) {
    const { isChanged, operation, resource, shopType, shopItem, giftTypeList, shop, isChangedPrice } = nextProps;
    if (isChanged) {
      this.getgiftArr(giftTypeList, shopItem, shop);
    }
    switch (operation.type) {
      case actions.GET_SHOPITEMSETTLEPRICE_SUCCESS:
        if (resource.shopItemCalendar.code === 200) {
          message.error(resource.shopItemCalendar.msg, 10);
        } else {
          this.setState({
            shopItemCalendarList: resource.shopItemCalendar.result,
            isChangedPriceLocal: true
          });
        }
        break;
      case actions.GET_BLOCKLIST_SUCCESS:
        if (this.state.isChangedPriceLocal) {
          this.getPriceList(shopItem, shop);
        }
        break;
      case actions.GET_PRICERULE_SUCCESS:
        if (this.state.isChangedPriceLocal) {
          this.getPriceList(shopItem, shop);
        }
        break;
      case actions.GET_SETTLERULE_SUCCESS:
        if (this.state.isChangedPriceLocal) {
          this.getPriceList(shopItem, shop);
        }
        break;



      default:
        break;
    }
  }
  getPriceList = (shopItem, shop) => {
    const { currentMonth, currentYear } = this.state;
    let params = {
      shopId: shop.id,
      shopItemId: shopItem.id,
      year: currentYear,
      month: currentMonth
    }
    this.props.actions.getShopItemSettlePrice(params);
  }
  getListData = (value, giftArr, shopItemCalendarList) => {
    let listData = [];
    shopItemCalendarList.map((shopitem) => {
      let dateNow = new Date(shopitem.calendarDate);
      if ((value.month() + 1) === (dateNow.getMonth() + 1) && value.date() === dateNow.getDate()) {

        giftArr.map((item, index) => {
          shopitem.prices.map((gift, index2) => {
            if (gift.gift === item.code) {
              item.price = gift.settlePrice;
              return item;
            }

          });
          listData.push(item);
        })

      }
    })
    return listData || [];
  }
  dateCellRender = (value) => {
    const { giftArr, shopItemCalendarList } = this.state;
    if (shopItemCalendarList.length === 0) {
      return null;
    }
    const listData = this.getListData(value, giftArr, shopItemCalendarList);
    let temp = shopItemCalendarList[0];
    let dateNow = new Date(temp.calendarDate);
    if ((value.month() + 1) !== (dateNow.getMonth() + 1)) {
      return null;
    }
    return (
      <div style={{ width: '100%', height: '100%', borderTop: '1px solid #e9e9e9' }}>
        <div style={{ textAlign: 'right', marginRight: '15px' }}><b>{value.date() === 1 ? (value.month() + 1) + "月" : ""} {value.date()}</b></div>
        <div style={{ width: '100%', height: '80px', textAlign: "right", overflowY: 'auto', }}>
          <ul className="events" style={{ marginRight: '20px' }}>

            {listData.map((item, index) => {
              let tems = Math.floor(Math.random());
              return (<label key={tems}><Badge status={statusMap[index]} key={item.shortName + index + item.code + tems} text={item.price ? item.price : item.price === 0 ? item.price : '-'} /><br></br></label>);
            })
            }
          </ul>
        </div>
        {shopItemCalendarList &&
          shopItemCalendarList.map((shopitem, index) => {
            let dateNow = new Date(shopitem.calendarDate);
            let tems = Math.floor(Math.random());
            if ((value.month() + 1) === (dateNow.getMonth() + 1) && value.date() === dateNow.getDate()) {
              if (shopitem.isBlock) {
                return <div key={tems} style={{ width: '100%', height: '20px', backgroundColor: 'rgba(245,34,45,.2)', textAlign: 'center', fontSize: '12px', verticalAlign: "middle", color: "#f5222d" }}>关闭</div>;
              } else {
                return <div key={tems} style={{ width: '100%', height: '20px', backgroundColor: 'rgba(82,196,26,.2)', textAlign: 'center', fontSize: '12px', verticalAlign: "middle", color: "#52c41a" }}>开放</div>;
              }
            }
          })
        }
      </div>
    );
  }


  onPanelChange = (value, mode) => {
    if (mode === 'month') {
      const { shop, shopItem } = this.props;
      //获取当前时间
      let date = value.toDate();
      //获取当前月 
      let currentMonth = date.getMonth() + 1;

      //获取当前时间
      let dateNow = new Date();
      //获取当前月 
      let currentMonthNow = dateNow.getMonth() + 1;
      let params = {
        shopId: shop.id,
        shopItemId: shopItem.id,
        year: date.getFullYear(),
        month: currentMonth
      }
      this.setState({
        currentMonth: currentMonth,
        currentYear: date.getFullYear(),
      });
      this.props.actions.getShopItemSettlePrice(params);
    }

  }
  onSelect = (value) => {
    this.getFirstAndlastDay(value);
  }
  getFirstAndlastDay = (value) => {
    const { shop, shopItem } = this.props;
    //获取当前时间
    let date = value.toDate();
    //获取当前月 
    let currentMonth = date.getMonth() + 1;

    //获取当前时间
    let dateNow = new Date();
    //获取当前月 
    let currentMonthNow = dateNow.getMonth() + 1;
    if (currentMonthNow !== currentMonth) {
      let params = {
        shopId: shop.id,
        shopItemId: shopItem.id,
        year: date.getFullYear(),
        month: currentMonth
      }
      this.setState({
        currentMonth: currentMonth,
        currentYear: date.getFullYear(),
      });
      this.props.actions.getShopItemSettlePrice(params);
    }
  }
  render() {
    const { shopProtocol, currencys } = this.props;
    const { giftArr } = this.state;
    const colors = ['pink', 'red', 'yellow', 'orange', 'cyan', 'green', 'blue', 'purple', 'geekblue', 'magenta', 'volcano', 'gold', 'lime'];

    return (
      <Fragment>
        <div style={{ width: '50%', }}>
          <Calendar dateFullCellRender={this.dateCellRender} onPanelChange={this.onPanelChange} onSelect={this.onSelect} />
          <br></br>
          <div style={{ marginLeft: '50%', marginBottom: '20px' }}>
            &nbsp;&nbsp;&nbsp;&nbsp;<span>币种:{currencys != null ? currencys.map(item => { if (item.code == shopProtocol.currency) return item.name }) : shopProtocol.currency}</span>
            {
              giftArr.map((item, index) => {
                let tems = Math.floor(Math.random());
                return <Badge status={statusMap[index]} key={item.shortName + index + item.code + tems} text={item.shortName} style={{ marginLeft: 10 }} />;
              })
            }
          </div>
        </div>
      </Fragment>
    );
  }
}

export default ItemCalendar;








