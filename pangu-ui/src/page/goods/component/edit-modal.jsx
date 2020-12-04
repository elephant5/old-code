import React, { Component } from 'react';
import { Modal, Form, DatePicker,  Input, Select,Checkbox } from 'antd';
import { week } from '../../../config/index';
import moment from 'moment';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
// import * as actions from '../../../store/resource/action';
import * as actions from '../../../store/goods/action';
import { LocaleProvider } from 'antd';
import zh_CN from 'antd/lib/locale-provider/zh_CN';
import 'moment/locale/zh-cn';

const dateFormat = 'YYYY-MM-DD HH:mm:ss';
@connect(
    ({operation, goods}) => ({
        operation,
        goods: goods.toJS()  
    }),
    dispatch => ({ 
        goods: bindActionCreators(actions, dispatch) 
    })
)
@Form.create()
class GoodsEditModal extends Component {

    state = {
        blockRule: [],
        // 选择农历不能选周和每年重复
        isShowWeek: 0,
        festival: [],
        // 前端默认展示
        tempRecord:{},
        blockParams: {},
        statusName:'未上架',
        isShow:true,
        startValue:null , 
        endValue:null,
        endOpen: false,
        isShowDown:false,
    }
    componentDidMount() {
        const { tempRecord } = this.props;
        let statusName  ='未上架';
        let isShow = true;
        let isShowDown = true;
        if(tempRecord.status === '1'){
            statusName ='已上架';
            isShow = false;
            isShowDown=true;
        }else if(tempRecord.status === '2') {
            statusName ='已下架';
            isShow = true;
            isShowDown=true;
        }
        
        this.setState({
            tempRecord:tempRecord,
            statusName:statusName,
            isShow:isShow,
            isShowDown:isShowDown,
            startValue:tempRecord.upstartTime?moment(tempRecord.upstartTime,dateFormat ):null,
            endValue:tempRecord.downEndTime?moment(tempRecord.downEndTime,dateFormat ):null,
        });
    }
    handleSubmit = e => {
        e.preventDefault();
        const { onEvent } = this.props;
        this.props.form.validateFields((err, values) => {
            if (err) return;
           
            if(values.upType){
                values.upType =  '1';
                if(values.status === '1'){
                    values.status = '2';
                }else{
                    values.status = '1';
                }
            }else{
                values.upType =  '0';
            }
            values.upstartTime=values.upstartTime?moment(values.upstartTime).utcOffset(0) :null;
            values.downEndTime = values.downEndTime?moment(values.downEndTime).utcOffset(0):null;
            onEvent('updateGoodsStatus',values);
        })
    }
    // 日历选择
    selectCalendar = e => {
        this.setState({
            isShowWeek: e.target.value
        })
    }
    onChangeType = (value ) =>{
        if(value === '0'){
            this.setState({
                isShow: true
            })
        }else{
            this.setState({
                isShow: false
            })
        }
        
    }
    disabledStartDate = (startValue) => {
        const endValue = this.state.endValue;
        if (!startValue || !endValue) {
          return  startValue < moment().endOf('day');//moment().endOf('day')今天以后的时间 ，，，moment().subtract(1, "days")包含今天的时间
        }
        return startValue.valueOf() > endValue.valueOf()  ||  startValue < moment().endOf('day');
      }
    
      disabledEndDate = (endValue) => {
        const startValue = this.state.startValue;
        if (!endValue || !startValue) {
          return endValue < moment().endOf('day');
        }
        return endValue.valueOf() <= startValue.valueOf() ||  endValue < moment().endOf('day');
      }
    
      onChange = (field, value) => {
        this.setState({
          [field]: value,
        });
      }
    
      onStartChange = (value) => {
        this.onChange('startValue', value);
      }
    
      onEndChange = (value) => {
        this.onChange('endValue', value);
      }
    
      handleStartOpenChange = (open) => {
        if (!open) {
        //   this.setState({ endOpen: true });
        }
      }
    
      handleEndOpenChange = (open) => {
        this.setState({ endOpen: open });
      }
        // 不可选择的时间段
   onChecked = (e) => {
    this.setState({
        isShow: e.target.checked ?false:true,
        isShowDown: e.target.checked ?false:true
    })
    
    
  }
    render() {
        const { closeGoodsEditModel, } = this.props;
        const { isShow ,tempRecord,statusName,endOpen,endValue,startValue,isShowDown} = this.state;
        const { getFieldDecorator } = this.props.form;
        return (
            <Modal
                title="修改商品上下架时间"
                visible={true}
                onOk={this.handleSubmit}
                onCancel={closeGoodsEditModel}
                cancelText="取消"
                okText="确定"
            >
                <Form onSubmit={this.handleSubmit}>
                    <Form.Item    label="当前状态"labelCol={{ span:5 }} wrapperCol={{ span: 10 }}>
                        {getFieldDecorator('statusName',{})
                        (<span>
                            {statusName}
                        </span>
                        )}
                        {getFieldDecorator('status',{initialValue:tempRecord.status})(<Input  style={{ display:'none' }} />)  }
                        {getFieldDecorator('id',{initialValue:tempRecord.id})(<Input  style={{ display:'none' }} />)  }
                    </Form.Item>
                    <Form.Item    label="上下架"labelCol={{ span:5 }} wrapperCol={{ span: 12 }}>
                        {(tempRecord.status === '0'||tempRecord.status === '2') && getFieldDecorator('upType', {})
                        ( <Checkbox onChange={this.onChecked}>即时上架</Checkbox>
                        
                        )}
                        {tempRecord.status === '1' && getFieldDecorator('upType', {})
                        ( <Checkbox onChange={this.onChecked}>及时下架</Checkbox>
                        
                        )}
                    </Form.Item>
                    {isShow &&tempRecord.status != '1' &&<LocaleProvider locale={zh_CN}>
                    <Form.Item  label="上架时间"labelCol={{ span: 5 }} wrapperCol={{ span: 12 }}>
                        {getFieldDecorator('upstartTime', { initialValue:tempRecord.upstartTime?moment(moment(tempRecord.upstartTime).utcOffset(0),dateFormat ).utcOffset(480):null})
                        (
                            
                            <DatePicker  style={{  width: '100%' }} disabledDate={this.disabledStartDate} 
                            showTime={{ defaultValue: moment('00:00:00', 'HH:mm:ss') }}
                            format="YYYY-MM-DD HH:mm:ss"
                            value={startValue}
                            placeholder="请选择上架时间"
                            onChange={this.onStartChange}
                            onOpenChange={this.handleStartOpenChange}/>
                            )}
                    </Form.Item></LocaleProvider>}
                    {isShowDown   && <LocaleProvider locale={zh_CN}>
                    <Form.Item  label="下架时间"labelCol={{ span: 5 }} wrapperCol={{ span: 12 }}>
                        {getFieldDecorator('downEndTime', { initialValue:tempRecord.downEndTime?moment(moment(tempRecord.downEndTime).utcOffset(0),dateFormat ).utcOffset(480):null})
                        (
                            <DatePicker disabledDate={this.disabledEndDate}
                            showTime={{ defaultValue: moment('00:00:00', 'HH:mm:ss') }}
                            format="YYYY-MM-DD HH:mm:ss"
                            value={endValue}
                            placeholder="请选择下架时间"
                            onChange={this.onEndChange}
                            open={endOpen}
                            onOpenChange={this.handleEndOpenChange}  style={{  width: '100%' }}  />
                        )}
                    </Form.Item></LocaleProvider>}
                </Form>
            </Modal>
        );
    }
}

export default GoodsEditModal;