import React, { Component } from 'react';
import { Form, message ,Select, Input, Button, Cascader, Modal,Tabs ,span,InputNumber,Alert,Checkbox ,DatePicker ,Tag} from 'antd';
import moment from 'moment'
import locale from 'antd/lib/date-picker/locale/zh_CN';
import goods from '../../../store/goods/reduce';
import { isNull } from 'util';
import AddModal from '../../merchant/component/add-modal';
import GoodsEditModal from '../../merchant/component/edit-modal';
import { unique } from '../../../util/index';
import { LocaleProvider } from 'antd';
import zh_CN from 'antd/lib/locale-provider/zh_CN';
import * as actions from '../../../store/goods/action';
import 'moment/locale/zh-cn';
import cookie from 'react-cookies';
const Option = Select.Option;
const TabPane = Tabs.TabPane;
const { TextArea } = Input;
const formItemLayout = {
    labelCol: { span: 4 },
    wrapperCol: { span: 8 },
  };
  const dateFormat = 'YYYY-MM-DD';
@Form.create()
class BasicInfo extends Component {
    constructor(props) {
        super(props);
        this.state = { 
           goodsId:null,
           display_info: true,
           display_moths: true,
           display_mothsTemp: true,
           display_date: false,
           display_day: false,
           display_temp:false,
           display_dayuse:false,
           isEdit_two:true,
           visible:false,
            // block弹框
            isShowAddModal: false,
            isShowEditModal: false,

            // block规则
            blockRuleList:  [],
            commissionType:"0",
        };
    }
    componentDidMount() {
        const {goodsInfo,isEdit } = this.props;
        if(isEdit && goodsInfo){
            this.getForm(goodsInfo);
        }
        this.setState({commissionType:goodsInfo?goodsInfo.commissionType:'0'})
    }
    componentWillReceiveProps(nextProps){
        const { isEdit, goodsInfo, } = nextProps;
        const {isEdit_two } = this.state;
        if(goodsInfo.blockRuleList){
            this.setState({
                blockRuleList: goodsInfo.blockRuleList
             });
        }
        if(isEdit && isEdit_two ){
            this.getForm(goodsInfo);
            this.setState({commissionType:goodsInfo.commissionType?goodsInfo.commissionType:'0'})
         }
      }
    // 保存基本信息
    handleSubmit = e => { 
        e.preventDefault();
        const { onEvent, data } = this.props;
       
        this.props.form.validateFields((err, values) => {
           
            if (!err) {
                values.blockRuleList = this.state.blockRuleList;
              if(values.expiryValue === 'NULL'){
                values.expiryValue = 'NULL';
            }else if(values.expiryValue === 'XD'){
                values.expiryValue = values.expiryValue+':'+values.days;
            }else if(values.expiryValue === 'D'){
                values.expiryValue =  values.expiryValue+':'+values.date.format('YYYY-MM-DD');
            }
            else if(values.expiryValue === 'XM'){
                values.expiryValue =values.expiryValue+':'+values.usedDay;;
            }
            else if(values.expiryValue === 'XM-YU'  ){
                values.expiryValue = values.expiryValue+':'+values.salesDate+values.activeDate+'/'+values.activeDay+values.usedDay;
            }else if(values.expiryValue === 'XD-YU' ){
                values.expiryValue = values.expiryValue+':'+values.days+'/'+values.activeDay+values.usedDay;
            }  
            else if(values.expiryValue === 'XM-YD'  ){
                values.expiryValue = values.expiryValue+':'+values.salesDate+values.activeDate+'/'+values.days;
            }else if(values.expiryValue === 'XD-YD' ){
                values.expiryValue = values.expiryValue+':'+values.days+'/'+values.daysUse;
            }   
            if(values.id === null || values.id === undefined){
                onEvent('insertGoods',values);
            }else{
                onEvent('goodsUpdate',values);
            }
            
            }
        });
    }
    // 表单绑定
    getForm = (goodsInfo) => {
        
        if( goodsInfo && goodsInfo.expiryValue ){
            const value = goodsInfo.expiryValue;
            this.expiryChange(value);
            this.setState({ 
                isEdit_two:false
             });
        }
        
        
    }
    chooseChannel=(values) =>{
        if(!values)return;
        const { onEvent } = this.props;
        onEvent('getSalesChannel',{salesChannelIds:values});
        // this.props.actions.getSalesChannel({salesChannelIds:values});
    }
    commissionType =  (value)=> {
        this.setState({ 
            commissionType:value
         });
    }
    expiryChange = (value ) =>{
        const {getFieldValue,setFieldsValue} = this.props.form;
        if(value === 'NULL'){
            this.setState({ 
                display_info: false,
                display_moths: false,
                display_date: false,
                display_day: false,
                display_temp:false,
                display_dayuse:false,
                display_mothsTemp:false,
             },()=>{
                });

        }else if(value === 'XD'){
            this.setState({ 
                display_info: false,
                display_moths: false,
                display_date: false,
                display_day: true,
                display_temp:false,
                display_dayuse:false,
                display_mothsTemp:false,
             });
        }else if(value === 'D'){
            this.setState({ 
                display_info: false,
                display_moths:false,
                display_date: true,
                display_day: false,
                display_temp:false,
                display_dayuse:false,
                display_mothsTemp:false,
             });
        }
        else if(value === 'XM'){
            this.setState({ 
                display_info: false,
                display_moths:true,
                display_date: false,
                display_day: false,
                display_temp:false,
                display_dayuse:false,
                display_mothsTemp:false,
             });
        }
        else if(value === 'XM-YU' ){
            this.setState({ 
                display_info: true,
                display_moths: true,
                display_date: false,
                display_day:false,
                display_temp:true,
                display_dayuse:false,
                display_mothsTemp:true,
             });
        }else if(value === 'XD-YU'){
            this.setState({ 
                display_info: false,
                display_moths: true,
                display_date: false,
                display_day:true,
                display_temp:false,
                display_dayuse:false,
                display_mothsTemp:true,
             });
        }else if(value === 'XM-YD'){
            this.setState({ 
                display_info: true,
                display_moths: false,
                display_date: false,
                display_day:true,
                display_temp:true,
                display_dayuse:false,
                display_mothsTemp:false,
             });
        }else if(value === 'XD-YD'){
            this.setState({ 
                display_info: false,
                display_moths: false,
                display_date: false,
                display_day:true,
                display_temp:false,
                display_dayuse:true,
                display_mothsTemp:false,
             });
        }
        this.setState({ 
            isEdit_two:false
         });
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
        const rules = [...this.state.blockRuleList, ...params.festival, ...params.blockRule];
        const norepeatRule = unique(rules);
        this.setState({
            blockRuleList: norepeatRule,
            isShowAddModal: false
        })
    }
    // 编辑block保存
    handleEdit = params => {
        // 先删除当前条，在覆盖
        
        this.setState({
            blockRuleList: this.state.blockRuleList.filter(item => item.rule !== params.deleteData.rule)
        }, () => {
            const rules = [...this.state.blockRuleList, ...params.festival, ...params.blockRule];
            const norepeatRule = unique(rules);
            this.setState({
                blockRuleList: norepeatRule,
                isShowEditModal: false
            })
        })
    }
    // 弹出新增block框
    addBlock = item => {
      
        this.setState({
            isShowAddModal: true
        })
    }
    // 删除block
    onClose = params => {
        this.setState({
            blockRuleList: this.state.blockRuleList.filter(item => item.rule !== params.rule)
        })
    }
    // 打开编辑block
    editBlock = item => {
        this.setState({
            isShowEditModal: true,
            editParams: item
        })
    }
    checkNumber = value =>{
        
        const {getFieldValue,setFieldsValue} = this.props.form;
        const max =getFieldValue("maxBookDays");
        const min = getFieldValue("minBookDays");
        if(max > min){
            this.setState({
                visible: true,
            });
            // setFieldsValue({"minBookDays":max});
        }
       
    }
    initNumber = value =>{
        const {getFieldValue,setFieldsValue} = this.props.form;
        const max =getFieldValue("maxBookDays");
        setFieldsValue({"minBookDays":max});
    }
    showModal = () => {
        this.setState({
          visible: true,
        });
      }
    
      handleOk = (e) => {
        const {getFieldValue,setFieldsValue} = this.props.form;
        const max =getFieldValue("maxBookDays");
        setFieldsValue({"minBookDays":max});
        this.setState({
          visible: false,
        });
      }
    
      handleCancel = (e) => {
        const {getFieldValue,setFieldsValue} = this.props.form;
        const max =getFieldValue("maxBookDays");
        setFieldsValue({"minBookDays":null});
        this.setState({
          visible: false,
        });
      }
    render() {
        // const formProps = this.getForm();
        const { getFieldDecorator } = this.props.form;
        const { channelList,authTypeList,goodsInfo, onEvent,festivalList,isEdit } = this.props;
        const { isShowAddModal, blockRuleList, isShowEditModal, editParams,display_info,display_moths, display_date,display_day,display_temp,display_mothsTemp,display_dayuse} = this.state;
        const edit = cookie.load("KLF_PG_GM_GL_EDIT");
        
        return (
           
                <div className="c-modal">
                    <div className="c-title">基本信息</div>
                    <Form onSubmit={this.handleSubmit}>
                    <Form.Item   style = {{marginTop : '20px' }} label="商品名称：" labelCol={{ span: 2 }} wrapperCol={{ span: 5 }}>
                        {getFieldDecorator('name', { rules: [{ required: true, message: '请输入商品名称!' }],initialValue: goodsInfo.name })(
                            <Input  maxLength={50} allowClear/>
                        )}
                        {getFieldDecorator('id', {initialValue: goodsInfo.id})(
                            <Input  style={{ display:'none' }} /> 
                                
                        )}
                        <span style ={{fontSize :'12px',marginTop:'-10px'}} >注：商品名称为前端显示的名称，请控制在50个字符以内</span>
                    </Form.Item>
                    <Form.Item   style = {{marginTop : '20px' }} label="内部简称："labelCol={{ span: 2 }} wrapperCol={{ span: 5 }}>
                        {getFieldDecorator('shortName', {
                            rules: [{ required: true, message: '请输入内部简称' }],initialValue: goodsInfo.shortName
                        })(<Input  allowClear/>
                        )}
                        <span style ={{fontSize :'12px',marginTop:'-10px'}} >注：内部简称为方便内部人员理解的名称，无字数限制</span>
                    </Form.Item>
                    <LocaleProvider locale={zh_CN} ><Form.Item   style = {{marginTop : '20px' }} label="销售渠道："labelCol={{ span: 2 }}  wrapperCol={{ span: 7 }} direction="horizontal">
                        {goodsInfo.status !='1' && getFieldDecorator('salesChannelIds', {
                            rules: [{ required: true, message: '请选择销售渠道!' }],
                            initialValue: goodsInfo.salesChannelIds
                        })
                        (<Cascader style={{ width: '300px' }}  options={channelList} onChange={this.chooseChannel} />)
                        }
                        {goodsInfo.status ==='1' && getFieldDecorator('salesChannelIds', {
                            rules: [{ required: true, message: '请选择销售渠道!' }],
                            initialValue: goodsInfo.salesChannelIds
                        })
                        (<Cascader style={{ width: '300px' }} disabled options={channelList} onChange={this.chooseChannel} />)
                        }
                        <br></br>
                         <span style ={{fontSize :'12px',marginTop:'-10px'}}>注：销售渠道决定该商品在哪个渠道售卖，请谨慎选择，若没有合适的渠道可以新增</span>
                    </Form.Item></LocaleProvider>
                    <Form.Item   style = {{marginTop : '20px' }} label="佣金类型："labelCol={{ span: 2 }} wrapperCol={{ span: 8 }} >
                    {getFieldDecorator('commissionType',  { rules: [{ required: true, message: '请输入零售价：!' }],initialValue: goodsInfo.commissionType? goodsInfo.commissionType:'0'})
                        (<Select  style={{ width: '15%'  }}  onChange={this.commissionType}> 
                        <Option key= {'NULL0'}  value="0">比例</Option>
                        <Option value="1">佣金</Option>
                        
                    </Select> 
                        )}&nbsp;&nbsp;<span className="ant-form-text" style={{marginLeft: '100px;'}}> {this.state.commissionType == '0'?'比例':'金额'}：</span>
                        {getFieldDecorator('commission', {initialValue: goodsInfo.commission})(<InputNumber  step={0.1}  min={0} max={100} /> 
                        )}
                        <span className="ant-form-text">{this.state.commissionType == '0'?'%':'元'} </span>
                           
                    </Form.Item>
                    <Form.Item verticalGap={1}  style = {{marginTop : '20px' }} label="零售价："labelCol={{ span: 2 }} wrapperCol={{ span: 8 }} direction="horizontal"  indicatorGap >
                        {getFieldDecorator('salesPrice',  { rules: [{ required: true, message: '请输入零售价：!' }],initialValue: goodsInfo.salesPrice})
                        (<InputNumber   step={1} min={0}/>
                        )}
                        <span className="ant-form-text" style={{marginLeft: '100px;'}}> 市场价：</span>
                        {getFieldDecorator('marketPrice', {initialValue: goodsInfo.marketPrice})(<InputNumber   step={1} min={0}/>
                            )}
                            
                    </Form.Item>
                     
                    
                    <div className="c-title">使用限制</div>
                    <Form.Item   style = {{marginTop : '20px' }} label="验证方式："labelCol={{ span: 2 }} wrapperCol={{ span: 8 }}>
                        {getFieldDecorator('authType', { rules: [{ required: true, message: '请选择验证方式!' }],initialValue: goodsInfo.authType})
                        (<Select    showSearch
                            filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0} style = {{width: '50%' }}>
                            {
                            authTypeList.map(item => (
                                    <Option key={item.id}  value={item.value}>{item.label}</Option>
                                ))
                           }
                         </Select>
                        )}
                    </Form.Item>
                    <LocaleProvider><Form.Item   style = {{marginTop : '20px' }} label="权益有效期："labelCol={{ span: 2 }} wrapperCol={{ span: 12 }}>
                        {getFieldDecorator('expiryValue', { rules: [{ required: true, message: '请选择权益有效期!' }],initialValue: goodsInfo.expiryValue})(
                            <Select  style={{ width: '33%'  }}  onChange={this.expiryChange}> 
                                <Option key= {'NULL'}  value="NULL">不限</Option>
                                <Option value="D">固定日期</Option>
                                <Option selected value="XM">售出后X个月内使用</Option>
                                <Option value="XD">售出后X天内使用</Option>
                                <Option value="XM-YU">售出后X个月内激活，激活后Y个月内使用</Option>
                                <Option value="XM-YD">售出后X个月内激活，激活后Y天内使用</Option>
                                <Option value="XD-YU">售出后X天内激活，激活后Y个月内使用</Option>
                                <Option value="XD-YD">售出后X天内激活，激活后Y天内使用</Option>
                            </Select>
                        )} &nbsp;&nbsp;
                       
                        {display_info&&getFieldDecorator('salesDate', {initialValue: goodsInfo.salesDate})(
                            <Select  style={{ width: '15%'  }} > 
                                <Option value="+M">售出当月起</Option>
                                <Option selected value="=M">售出当天起</Option>
                                <Option value="-M">售出次月起</Option>
                            </Select>
                        )} &nbsp;&nbsp;
                         
                        {display_temp &&getFieldDecorator('activeDate', {initialValue: goodsInfo.activeDate})(
                            <Select  style={{ width: '15%' }} > 
                                <Option value="1">1个月内激活</Option>
                                <Option value="2">2个月内激活</Option>
                                <Option value="3">3个月内激活</Option>
                                <Option value="6">6个月内激活</Option>
                                <Option value="9">9个月内激活</Option>
                                <Option selected value="12">12个月内激活</Option>
                                <Option value="18">18个月内激活</Option>
                                <Option value="24">24个月内激活</Option>
                            </Select>
                        )} &nbsp;&nbsp;
                        {display_day && getFieldDecorator('days', {initialValue: goodsInfo.days,rules:[{ required:  true , message: '请填写天数' }]})( <InputNumber   step={1} min={1} placeholder="请填写天数" 
                                style={{ width: '15%'}}
                                formatter={value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')} 
                                parser={value => value.replace(/\s?|(,*)/g, '')} /> 
                        )} &nbsp;&nbsp;
                        {display_mothsTemp&&getFieldDecorator('activeDay', {initialValue: goodsInfo.activeDay})(
                            <Select  style={{ width: '15%'}} > 
                                <Option value="+M">激活当月起</Option>
                                <Option selected value="=M">激活当天起</Option>
                                <Option value="-M">激活次月起</Option>
                            </Select>
                        )} &nbsp;&nbsp;
                        {display_moths&&getFieldDecorator('usedDay', {initialValue: goodsInfo.usedDay})(
                            <Select  style={{ width: '15%'  }} > 
                                <Option value="1">1个月内使用</Option>
                                <Option value="2">2个月内使用</Option>
                                <Option value="3">3个月内使用</Option>
                                <Option value="6">6个月内使用</Option>
                                <Option value="9">9个月内使用</Option>
                                <Option selected value="12">12个月内使用</Option>
                                <Option value="18">18个月内使用</Option>
                                <Option value="24">24个月内使用</Option>
                            </Select>
                        )} &nbsp;&nbsp;
                        {display_dayuse && getFieldDecorator('daysUse', {initialValue: goodsInfo.daysUse})( <InputNumber   step={1} min={1} placeholder="请填写天数" 
                                style={{ width: '15%'}}
                                formatter={value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')} 
                                parser={value => value.replace(/\s?|(,*)/g, '')} /> 
                        )} &nbsp;&nbsp;
                       {display_date&&getFieldDecorator('date', {initialValue: goodsInfo.date?moment(goodsInfo.date,dateFormat ):null})(
                             <DatePicker locale={locale} style={{  width: '25%' }} />
                        )} 
                    </Form.Item></LocaleProvider>
                    <Form.Item   style = {{marginTop : '20px' }} label="商品Block："labelCol={{ span: 2 }} wrapperCol={{ span: 12 }}>
                        <Button type="primary"  ghost onClick={this.addBlock}>+ 添加Block</Button>&nbsp;&nbsp;&nbsp;&nbsp; 
                                {
                                    // 新增block弹框
                                    isShowAddModal &&
                                    <AddModal
                                        onCancel={this.onCancel}
                                        onOk={this.onOk}
                                        data={{festivalList: festivalList }}
                                    />
                                }
                                {
                                    // 编辑block框
                                    isShowEditModal &&
                                    <GoodsEditModal
                                        onCancel={this.onEditCancel}
                                        onOk={this.handleEdit}
                                        data={{festivalList: festivalList, editParams}}
                                    />
                                }
                                 {
                                    blockRuleList.map((item, idx) => (
                                        <Tag key={item.rule} 
                                            closable 
                                            onClose={e => e.stopPropagation()}
                                            afterClose={() => this.onClose(item)}
                                            // onClick={() => this.editBlock(item)}
                                        >
                                            {item.natural}
                                        </Tag>
                                        )
                                    )
                                }
                    </Form.Item>
                    <Form.Item   style = {{marginTop : '20px' }} label="使用限制："labelCol={{ span: 2 }} wrapperCol={{ span: 8 }}  direction="vertical" indicatorGap>
                        {getFieldDecorator('superposition',  {valuePropName:'checked',
                                                                initialValue: goodsInfo.superposition === "1"?true: false })( 
                            <Checkbox >同一时段权益不叠加</Checkbox> 
                        )}<span style={{color:'grey',fontSize :'12px',}}>不可预订同一时间下该商品的多个权益</span>
                        
                        <br></br>
                        {getFieldDecorator('singleThread',  {valuePropName:'checked',
                                                                initialValue: goodsInfo.singleThread === "1" ? true: false })( 
                            <Checkbox >行权完毕次日才可以进行下一次预约</Checkbox>
                        )}<span style={{color:'grey',fontSize :'12px',}}>不资源之间互不影响，只对相同资源类型做限制</span>
                        <br></br>
                        {getFieldDecorator('enableMaxNight', {valuePropName:'checked',
                                                                initialValue: goodsInfo.enableMaxNight === "1"?true: false })( 
                            <Checkbox >限制单次可预订最大间夜数</Checkbox>
                        )}
                        {getFieldDecorator('maxNight', { initialValue: goodsInfo.maxNight})( 
                            <InputNumber  min={1} max={10}  />
                        )}&nbsp;&nbsp;间夜
                        <br></br>
                        {getFieldDecorator('enableMinNight', {valuePropName:'checked',
                                                                initialValue: goodsInfo.enableMinNight === "1"?true: false })( 
                            <Checkbox >限制单次可预订最小间夜数</Checkbox>
                        )}
                        {getFieldDecorator('minNight', { initialValue: goodsInfo.minNight})( 
                            <InputNumber  min={1} max={10}  />
                        )}&nbsp;&nbsp;间夜
                        <br></br>
                        {getFieldDecorator('allYear', {valuePropName:'checked',
                                                                initialValue: goodsInfo.allYear === "1"?true: false })( 
                            <Checkbox >OK365</Checkbox>
                        )}<span style={{color:'grey',fontSize :'12px',}}>没有任何节假日/关餐/关房的限制</span>
                        <br></br>
                        {getFieldDecorator('disableCall', {valuePropName:'checked',
                                                                initialValue: goodsInfo.disableCall === "1"?true: false })( 
                            <Checkbox  >禁止来电预约</Checkbox>
                        )}
                        <br></br>
                        {getFieldDecorator('disableWechat', {valuePropName:'checked',
                                                                initialValue: goodsInfo.disableWechat === "1"?true: false })( 
                            <Checkbox  >禁止微信统一行权</Checkbox>
                        )}
                         <br></br>
                        {getFieldDecorator('onlySelf', {valuePropName:'checked',
                                                                initialValue: goodsInfo.onlySelf === "1"?true: false })( 
                            <Checkbox  >仅限人本使用</Checkbox>
                        )}
                    </Form.Item>
                    <Form.Item    label="预约限制" labelCol={{ span: 2 }} wrapperCol={{ span: 8 }} >
                        <span >最少提前</span>&nbsp;&nbsp;
                        {getFieldDecorator('minBookDays',  {initialValue: goodsInfo.minBookDays})(<InputNumber min={0} max={100}/>)} &nbsp;&nbsp;天 
                        <span style = {{marginLeft : '20px' }}>最多提前</span>&nbsp;&nbsp;
                        {getFieldDecorator('maxBookDays',  {initialValue: goodsInfo.maxBookDays})(<InputNumber  min={0} max={100}/>)} &nbsp;&nbsp;天 
                        <span style={{color:'grey',marginLeft : '20px',fontSize :'12px',}}>不填表示没有限制</span>
                        <Modal
                            title="预约限制提示"
                            visible={this.state.visible}
                            onOk={this.handleOk}
                            onCancel={this.handleCancel}
                            cancelText="取消"
                            okText="确定"
                            >
                            <Alert message="预约限制提示：您当前填写的最多提前的天数不可比最少提前的天数小，请重新修改！(默认大于等于最多提前天数或者不填)" type="error" />
                        </Modal>
                    </Form.Item>
                    <Form.Item    label="热线号码" labelCol={{ span: 2 }} wrapperCol={{ span: 6 }} >
                    {getFieldDecorator('hotline', { rules: [{ required: true, message: '请输入热线号码!' }], initialValue: goodsInfo.hotline})( 
                            <Input style={{ width: '50%' }}></Input>
                        )}
                        <span style={{color:'grey',marginLeft : '20px',fontSize :'12px',}}>显示在通知短信中的热线号码</span>
                    </Form.Item>
                    <div className="c-title">营运须知</div>
                    <Form.Item  style = {{marginTop : '1%' }}  label="商品备注" labelCol={{ span: 2 }} wrapperCol={{ span: 8 }} >
                     {getFieldDecorator('remark', {initialValue: goodsInfo.remark})( <TextArea style={{ width: '100%', height: 100 }} />)}
                    </Form.Item>
                    <Form.Item  label="来电提醒" labelCol={{ span: 2 }} wrapperCol={{ span: 8 }} >
                     {getFieldDecorator('callReminder', {initialValue: goodsInfo.callReminder})( <TextArea style={{ width: '100%', height: 100 }} />)}
                    </Form.Item>

                    <Form.Item    label="客户来源" labelCol={{ span: 2 }} wrapperCol={{ span: 8 }} >
                     {getFieldDecorator('customSource', {initialValue: goodsInfo.customSource})( <Input style={{ width: '50%' }}></Input>)}
                    </Form.Item>
                    <Form.Item    label="核销提示" labelCol={{ span: 2 }} wrapperCol={{ span: 8 }} >
                     {getFieldDecorator('verifyReminder', {initialValue: goodsInfo.verifyReminder})( <Input style={{ width: '50%' }}></Input>)}

                     <br></br>
                     <div style={{  padding: '1% 0 1%' }}>
                     {edit&&<Button type="primary"  htmlType="submit" >保存</Button>} &nbsp;&nbsp;&nbsp;&nbsp;
                     <Button  onClick={() => onEvent('cancel')} >取消</Button>
                    </div>
                    
                    </Form.Item>
                   
                    
                    </Form>
                </div> 
            
        );
    }
}

export default BasicInfo;