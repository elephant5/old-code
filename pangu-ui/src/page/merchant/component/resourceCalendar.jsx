import React, { Component, Fragment } from 'react';
import { Form, Icon, Input, Button, Select, Row, Col, Badge, InputNumber } from 'antd';
import './resourceCalendar.less';
import moment from 'moment';

const { Option } = Select;
const formItemLayout = {
  labelCol: { span: 6 },
  wrapperCol: { span: 12},
};

@Form.create()
class ResourceCalendar extends Component {
  constructor(props){
    super(props)
    this.state = {
      currentDay: this.getFirstDay()
    }
  }

  // 获取开始日期当月的1号
  getFirstDay = () => {
    const { startDate } = this.props;
    const [year, month, ] = moment(startDate).format('YYYY-MM-DD').split('-');
    return new Date(`${year}-${month}-01`); 
  }

  componentWillReceiveProps(nextProps) {
  }
  // 获取下个月
  handleNext = () => {
      const { currentDay } = this.state;
      let year = currentDay.getFullYear();
      let month = ('00'+(currentDay.getMonth() + 1)).slice(-2);
      let day = ('00'+currentDay.getDate()).slice(-2);
      let result = '';
      if(month === '12'){
        month = '01';
        result = `${year + 1}/${month}/${day}`;
        result = new Date(result);
        this.setState({
          currentDay: result
        })
      }else{
        this.setState({
          currentDay: new Date(`${year}/${month - 0 + 1}/${day}`)
        })
      }
  }
  // 获取上个月
  handlePrev = () => {
      const { currentDay } = this.state;
      let year = currentDay.getFullYear();
      let month = ('00'+(currentDay.getMonth() + 1)).slice(-2);
      let day = ('00'+currentDay.getDate()).slice(-2);
      let result = '';
      if(month === '01'){
        month = '12';
        result = `${year - 1}/${month}/${day}`;
        result = new Date(result);
        this.setState({
          currentDay: result
        })
      }else{
        this.setState({
          currentDay: new Date(`${year}/${month - 1}/${day}`)
        })
      }
  }

  getItem = () => {
    let { startDate, endDate, currentDay = new Date() } = this.props;
    const today = new Date();
    let dateItems = [];
    const weekItems = [];
    while (startDate.getTime() <= endDate.getTime()) {
      dateItems.push({
        // dateStr: startDate.getDate() === 1 ? formatDate(startDate, "MM月 d") : formatDate(startDate, "d"),
        beforeToday: today.getTime() >= startDate.getTime(),
        // state: 0开放，1资源关闭，2资源以上级别的关闭（商户、全局）
        // TODO
        state: startDate.getDay() < 3 ? 0 : (startDate.getDay() === 6 ? 2 : 1),
      });
      if (startDate.getDay() === 6) {
        weekItems.push(dateItems);
        dateItems = [];
      }
      startDate = new Date(startDate.getTime() + 86400 * 1000);
    }
    return {dateItems, weekItems}
  } 

  render() {
    const weekDays = ['日', '一', '二', '三', '四', '五', '六'];
    const { currentDay } = this.state;
    let { startDate, endDate, dateDatas = [], shopType = 'accom', topStyle = { 'width' : '60%' } } = this.props;
    // 从当月1号开始
    startDate = currentDay;
    const today = new Date();
    let dateItems = [];
    const weekItems = [];
    for(let i = 0, len = startDate.getDay(); i < len; i++ ){
      dateItems.push({
        dateStr: '',
        beforeToday: today.getTime() >= startDate.getTime(),
        state: startDate.getDay() < 3 ? 0 : (startDate.getDay() === 6 ? 2 : 1)
      })
    }
    while (startDate.getTime() <= endDate.getTime()) {
      dateItems.push({
        dateStr: startDate.getDate() === 1 ? moment(startDate).format('MM月D') : startDate.getDate(),
        beforeToday: today.getTime() >= startDate.getTime(),
        // state: 0开放，1资源关闭，2资源以上级别的关闭（商户、全局）
        // TODO
        state: startDate.getDay() < 3 ? 0 : (startDate.getDay() === 6 ? 2 : 1),
      });
      if (startDate.getDay() === 6) {
        weekItems.push(dateItems);
        dateItems = [];
      }
      startDate = new Date(startDate.getTime() + 86400 * 1000);
    }
    return (
      <Fragment>
        <div className="calendar-top-wrapper" style={{...topStyle}}>
          <div className="calendar-month">
            <Icon type="left" className="calendar-month-op" onClick={this.handlePrev}/>
            <h3 className="calendar-month-title">{moment(currentDay).format('YYYY年MM月')}</h3>
            <Icon type="right" className="calendar-month-op"  onClick={this.handleNext}/>
          </div>
          <div className="calendar-flex calendar-weekdays">
            {weekDays.map((weekDay, index) => {
              return <div key={`weekDay-${index + 1}`} className="calendar-weekday">{weekDays[index]}</div>
            })}
          </div>
          <div className="calendar-week">
            {weekItems.map((dateItems, index) => {
              let initState = NaN;
              return (<div className="calendar-flex calendar-days" key={`weekIndex + ${index+ 1}`}>
                {dateItems.map((dateItem, jndex) => {
                  let showTxt = false;
                  let text = '';
                  let stateOtherClass = 'state-open';
                  switch(dateItem.state) {
                    case 0:
                      text = "开放";
                      stateOtherClass = 'state-open';
                      break;
                    case 1:
                      text = '关闭';
                      stateOtherClass = 'state-close';
                      break;
                    default:
                      text = '';
                      stateOtherClass = 'state-ancestor-close';
                      break;
                  }
                  if (initState !== dateItem.state) {
                    showTxt = true;
                    initState = dateItem.state;
                  }
                  return (<div key={`weekDay-${index * 7 + jndex + 1}`} className={`calendar-date ${dateItem.state === 2 ? "date-ancestor-close" : ''}`}>
                    <div className={`calendar-date-str ${dateItem.beforeToday ? 'before-today' : ''}`}>{dateItem.dateStr}</div>
                    <div className={`calendar-date-state ${stateOtherClass}`}>{showTxt ? text : ''}</div>
                  </div>)
                })}

              </div>)
            })}
          </div>
          <div className="calendar-extra">
          {
            shopType === 'accom' ?<Button type="default">查看历史结算价</Button>
            : (<div>
              <Badge status="error" text="双免" style={{marginRight:20}}/>
              <Badge status="processing" text="二免一" />
            </div>)
          }
          </div>
        </div>
      </Fragment>
    );
  }
}

export default ResourceCalendar;








