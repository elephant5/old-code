import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { Form, Input, Button, Select ,Table,Radio, InputNumber,Checkbox,DatePicker,Divider,Modal} from 'antd';
import moment from 'moment';
import './reserve'
const RadioGroup = Radio.Group;
const { Option } = Select;
const { TextArea } = Input;

const EditableContext = React.createContext();
const EditableRow = ({ form, index, ...props }) => (
  <EditableContext.Provider value={form}>
    <tr {...props} />
  </EditableContext.Provider>
);
const EditableFormRow = Form.create()(EditableRow);
class EditableCell extends React.Component {
  state = {
    editing: false,
  };

  toggleEdit = () => {
    const editing = !this.state.editing;
    this.setState({ editing }, () => {
      if (editing) {
        this.input.focus();
      }
    });
  };

  save = e => {
    const { record, handleSave } = this.props;
    this.form.validateFields((error, values) => {
      if (error && error[e.currentTarget.id]) {
        return;
      }
      this.toggleEdit();
      handleSave({ ...record, ...values });
    });
  };

  renderCell = form => {
    this.form = form;
    const { children, dataIndex, record, title } = this.props;
    const { editing } = this.state;
    return editing ? (
      <Form.Item style={{ margin: 0 }}>
        {form.getFieldDecorator(dataIndex, {
          
          initialValue: record[dataIndex],
        })(<InputNumber ref={node => (this.input = node)} width={'10%'} onPressEnter={this.save} onBlur={this.save} />)}
      </Form.Item>
    ) : (
      <div
        className="editable-cell-value-wrap"
        style={{ paddingRight: 24 }}
        onClick={this.toggleEdit}
      >
        {children}
      </div>
    );
  };

  render() {
    const {
      editable,
      dataIndex,
      title,
      record,
      index,
      handleSave,
      children,
      ...restProps
    } = this.props;
    return (
      <td {...restProps}>
        {editable ? (
          <EditableContext.Consumer>{this.renderCell}</EditableContext.Consumer>
        ) : (
          children
        )}
      </td>
    );
  }
}
@Form.create()
class Reserve extends Component {
    constructor(props) {
      super(props);
      this.columns = [
        // {dataIndex:'orderId'},
        {title: '编号',dataIndex:'priceId'},
        {
          title: '预定时间',
          dataIndex: 'bookDate',
          key: 'bookDate',
          render: (text, row, index) => {
            return   (moment(text).format('YYYY-MM-DD'));
          }
        },
        
        {
          title: '协议价',
          dataIndex: 'protocolPrice',
          key: 'protocolPrice',
        },
        {
          title: '数量',
          dataIndex: 'nightNumbers',
          key: 'nightNumbers',
        },
        {
          title: '实报价',
          dataIndex: 'realPrice',
          key: 'realPrice',
          editable: true,
          // render: (text, row, index) => {
          //   let  realPrice  = text;
          //  if(this.props.reservOrderInfo.proseStatus === '0' || this.props.reservOrderInfo.proseStatus === '4'){
          //   realPrice = row.protocolPrice;
          //  }
          //  return realPrice;
          // }
        },
      
      ];
      this.state = { 
        resourceList:[],
        groupProduct:{},
        rooms:[],
        deparDateList:[],
        dataSource :[],
        tatolPrice:0,
        checked: true,
        channel:{},
        hospital:{}
      };
  }
  componentDidMount() {
    const {reservOrderInfo,newShopDetail,GoodsGroupListRes,channels} = this.props;
    let hospital = reservOrderInfo.hospital;
    this.setState({hospital,hospital}); 
    const resourceList  =  GoodsGroupListRes[0].groupProductList.filter(item =>  item.shopId  === reservOrderInfo.shopId);
    let temp =[];
    if(reservOrderInfo.proseStatus === '0' || this.props.reservOrderInfo.proseStatus === '4'){
       reservOrderInfo.shopSettleMsgRes.map(item => {
          item.realPrice = item.protocolPrice ?  item.protocolPrice :0;
          temp.push(item);
      });
    }else{
      temp = reservOrderInfo.shopSettleMsgRes;
    }
    let channelstemp  = channels.filter(item =>  item.id  === reservOrderInfo.shopChannelId);
    this.setState({resourceList:resourceList,dataSource :temp,tatolPrice:reservOrderInfo.orderSettleAmount,channel:channelstemp[0] });
    const {setFieldsValue} = this.props.form;
    setFieldsValue({"productId":resourceList.length > 0 ? resourceList[0].productId:null });
  }
  // 保存基本信息
  handleSubmit = e => { 
    e.preventDefault();
    const {onEvent ,closeEditModel,newShopDetail,reservOrderInfo,GoodsGroupListRes} = this.props;
    this.props.form.validateFields((err, values) => {
      const { resourceList ,rooms,deparDateList,dataSource,hospital} = this.state;
        if (!err) {
          values.messageFlag  = values.messageFlag === true  ?'1':"0";
          values.shopSettleMsgRes= dataSource;
          values.id  = reservOrderInfo.id;
          if(reservOrderInfo.serviceType === '绿通就医' ){
            //修改医院的参数  没有修改医院
            if(values.hospitalId!=reservOrderInfo.hospital.name){
              values.hospitalName=hospital.name;
              values.grade = hospital.grade;
              values.hospitalType=hospital.hospitalType;
              values.province=hospital.province;
              values.city=hospital.city;
              values.reservOrderHospitalId=hospital.id;
            }
          }
            onEvent('reservSuccess',values);
        }
    });
  }
  selectHospital=(value,option)=>{
    let hospital = this.props.sysHospitalList[parseInt(option.key)];
    console.info(hospital);
    this.setState({hospital:hospital});
  }
  selectShop = values =>{
    const { onEvent,GoodsGroupListRes,reservOrderInfo} = this.props;
        
    const groupProduct =  GoodsGroupListRes[0].groupProductList.filter(item =>  item.id  === values);//商品中产品资源的产品组和产品关联数据
    onEvent('getShopDetail',groupProduct[0].shopId);
    this.setState({resourceList:groupProduct,groupProduct:groupProduct,});
    const {getFieldValue,setFieldsValue} = this.props.form;
    setFieldsValue({"productId":groupProduct[0].productId});
  }
  onStartChange =(values,dateString) =>{
    const {getFieldValue,setFieldsValue} = this.props.form;
    
  }
  onChange = e => {
    this.setState({
      checked: e.target.checked,
    });
  };
  handleSave = row => {
    console.log(row)
    const newData = [...this.state.dataSource];
    const index = newData.findIndex(item => row.priceId === item.priceId);
    const item = newData[index];
    newData.splice(index, 1, {
      ...item,
      ...row,
    });

    let tatolPrice  = 0;
    newData.map(item => {
      let num3= Number(item.realPrice); 
      let nightNumbers = item.nightNumbers ? Number(item.nightNumbers) : 0; 
      tatolPrice = tatolPrice + (num3*nightNumbers);
    });
    this.setState({ dataSource: newData ,tatolPrice:tatolPrice});


  };
  getChannel = values =>{
    const {channels} = this.props;
    let temp  = channels.filter(item =>  item.id  === values);
    this.setState({channel:temp[0]});
  }
  render() {
    const {onEvent ,closeReserveModel,groupProductList,newShopDetail,reservOrderInfo,GoodsGroupListRes,channels,reservSuccessLoading,sysHospitalList} = this.props;
    const { resourceList ,rooms,deparDateList,dataSource,tatolPrice,channel,hospital} = this.state;
    const { getFieldDecorator } = this.props.form;
    const components = {
      body: {
        row: EditableFormRow,
        cell: EditableCell,
      },
    };
    const columns =this.columns.map(col => {
      if (!col.editable) {
        return col;
      }
      return {
        ...col,
        onCell: record => ({
          record,
          editable: col.editable,
          dataIndex: col.dataIndex,
          title: col.title,
          handleSave: this.handleSave,
        }),
      };
    });
      return (<Modal width={'35%'}
        title="预定订单预订信息"
        visible={true}
        onCancel={closeReserveModel}
        onOk={this.handleSubmit}
        confirmLoading={reservSuccessLoading}
        cancelText="取消"
        okText="确定"
    >
       {reservOrderInfo.serviceType != '机场出行'&& reservOrderInfo.serviceType != '绿通就医' &&<Form onSubmit={this.handleSubmit}>
            <Form.Item  verticalGap={1}  label="预订信息" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal"  indicatorGap >
            { reservOrderInfo.shopDetailRes.shop.hotelName+"|"+ reservOrderInfo.shopDetailRes.shop.name} <br></br>
            {reservOrderInfo.serviceType === '住宿' && <div>{reservOrderInfo.giftDate} 入住 &nbsp;&nbsp; {reservOrderInfo.detail.checkNight}间夜  &nbsp;&nbsp; {reservOrderInfo.detail.deparDate}离店 <br/></div> 
           } 
            {(reservOrderInfo.serviceType === '自助餐'||reservOrderInfo.serviceType === '下午茶'||
            reservOrderInfo.serviceType === '定制套餐'||reservOrderInfo.serviceType === '水疗' || reservOrderInfo.serviceType === '健身') && <div>用餐日期：{reservOrderInfo.giftDate}  &nbsp;&nbsp; 时间：{reservOrderInfo.giftTime}  &nbsp;&nbsp;用餐人数： {reservOrderInfo.giftPeopleNum}人 <br/></div> 
           } 
           {reservOrderInfo.giftName}<br/>
            {reservOrderInfo.giftPhone}<br/>
            
            </Form.Item>
            <Form.Item  verticalGap={1}  label="预订渠道" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal"  indicatorGap >
                {getFieldDecorator('shopChannelId',{ rules: [{ required: true, message: '请选择预订渠道!' }],initialValue:reservOrderInfo.shopChannelId})(
                    <Select  style={{width : "50%"}} onChange={this.getChannel}>
                        {channels.map((temp,index) => {
                            return (<Option value={temp.id} key={'channels'+temp.id}  >{temp.name}</Option>);
                        })}
                    </Select>
                )}&nbsp;&nbsp; 
                {reservOrderInfo.serviceType === '住宿'  && channel.internal === 0 && getFieldDecorator('channelNumber',{ initialValue:reservOrderInfo.channelNumber})(
                    <Input  style={{width : "45%"}} placeholder="渠道号(第三方资源)" ></Input>
                )} 
            </Form.Item>
            <Form.Item  verticalGap={1}  label="预约号" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal"  indicatorGap >
            {getFieldDecorator('reservNumber', {})
                (
            <Input></Input>
            )}
            <br/>
            {getFieldDecorator('messageFlag', {})
                (
            <Checkbox checked={this.state.checked} onChange={this.onChange}></Checkbox> 
            )}&nbsp;发送成功通知短信
            <br/>
            </Form.Item>
           
           
            {/* <Form.Item  label="预约姓名" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal"  indicatorGap>
            {getFieldDecorator('giftName',  { initialValue:reservOrderInfo.giftName})(
                 <Input></Input>
            )} 
            </Form.Item>
            <Form.Item  label="预约电话：" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal"  indicatorGap>
            {getFieldDecorator('giftPhone',  { initialValue:reservOrderInfo.giftPhone})(
                 <Input></Input>
            )} 
            </Form.Item> */}
            <Form.Item label="商户结算：" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal"  indicatorGa>
                <Table   components={components}
                  rowClassName={() => 'editable-row'} dataSource={dataSource }  columns={columns} loading={false} bordered={true} pagination={false}/>
            </Form.Item>
            <Form.Item label="结算总额" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal"  indicatorGa>
               {tatolPrice}
            </Form.Item>
            
            <Form.Item  label="备注" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal"  indicatorGap>
            {getFieldDecorator('reservRemark', { initialValue:reservOrderInfo.reservRemark} )( <TextArea style={{ width: '100%', height: 50 }} />)}
            </Form.Item>
          </Form>}

          {/* =================绿通就医============================== */}
          {reservOrderInfo.serviceType === '绿通就医'&&<Form onSubmit={this.handleSubmit}>
            <Form.Item  verticalGap={1}  label="医院信息" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal"  indicatorGap >
                    {getFieldDecorator('hospitalId',{initialValue: reservOrderInfo.hospital.name})(
                                    <Select  showSearch placeholder="模糊搜索"
                                        filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0} onChange={this.selectHospital}>
                                        {
                                            sysHospitalList.map((item,i) => (
                                                <Option key={i} value={item.id}>{ item.name}</Option>
                                            ))
                                        }
                                    </Select>
                      )}
            </Form.Item>
            <Form.Item  verticalGap={1}  label="医院性质" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal"  indicatorGap >
                  {!!hospital.hospitalType?hospital.hospitalType:""} {hospital.grade}
            </Form.Item>
            <Form.Item  verticalGap={1}  label="就医人信息" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal"  indicatorGap >
              {reservOrderInfo.hospitalName}  {reservOrderInfo.giftPhone}
            </Form.Item>
            <Form.Item  verticalGap={1}  label="就诊类型" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal"  indicatorGap >
                {getFieldDecorator('visit',{initialValue:reservOrderInfo.hospital.visit})(
                    <RadioGroup>
                      <Radio value="专家陪诊">专家陪诊</Radio>
                      <Radio value="住院服务">住院服务</Radio>
                    </RadioGroup>)}
            </Form.Item>
            <Form.Item  verticalGap={1}  label="服务类型" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal"  indicatorGap >
                  {getFieldDecorator('special',{initialValue:reservOrderInfo.hospital.special})(
                    <RadioGroup>
                      <Radio value="陪诊">陪诊</Radio>
                      <Radio value="不陪诊">不陪诊</Radio>
                    </RadioGroup>)}
            </Form.Item>
            <Form.Item  verticalGap={1}  label="预约科室" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal"  indicatorGap >
              {reservOrderInfo.hospital.department}
            </Form.Item>
            <Form.Item  verticalGap={1}  label="就医日期" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal"  indicatorGap >
              {getFieldDecorator('giftDate',{initialValue:moment(reservOrderInfo.giftDate, "YYYY-MM-DD")})(<DatePicker  format="YYYY-MM-DD" />)}
            </Form.Item>
            <Form.Item  verticalGap={1}  label="备注" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal"  indicatorGap >
              {getFieldDecorator('remark',{initialValue:reservOrderInfo.remark})(<TextArea rows={4} />)}
            </Form.Item>
            <Form.Item  verticalGap={1}  label="就医确认号" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal"  indicatorGap >
            {getFieldDecorator('reservNumber', {})
                (
            <Input></Input>
            )}
            <br/>
            {getFieldDecorator('messageFlag', {})
                (
            <Checkbox checked={this.state.checked} onChange={this.onChange}></Checkbox> 
            )}&nbsp;短信通知用户
            <br/>
            </Form.Item>
          </Form>}
          {/* =================机场出行============================== */}
          {reservOrderInfo.serviceType === '机场出行'&&<Form onSubmit={this.handleSubmit}>
            <Form.Item  verticalGap={1}  label="预订信息" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal"  indicatorGap >
              {reservOrderInfo.giftName} {reservOrderInfo.giftPhone}
            </Form.Item>
            <Form.Item  verticalGap={1}  label="时间人数" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal"  indicatorGap >
              {reservOrderInfo.giftDate+" "+reservOrderInfo.giftTime}  {reservOrderInfo.giftPeopleNum}
            </Form.Item>
            <Form.Item  verticalGap={1}  label="接送车型" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal"  indicatorGap >
            {reservOrderInfo.shopItemName}
            </Form.Item>
            <Form.Item  verticalGap={1}  label="预订渠道" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal"  indicatorGap >
              {reservOrderInfo.shopDetailRes && channels.map(item => {
                    if (item.id === reservOrderInfo.shopDetailRes.shopProtocol.shopChannelId) {
                      return item.name;
                    }
                 }
                )}
            </Form.Item>
            <Form.Item  verticalGap={1}  label="预约单号" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal"  indicatorGap >
            {getFieldDecorator('reservNumber', {})
                (
            <Input></Input>
            )}
            <br/>
            {getFieldDecorator('messageFlag', {})
                (
            <Checkbox checked={this.state.checked} onChange={this.onChange}></Checkbox> 
            )}&nbsp;短信通知用户
            <br/>
            </Form.Item>
          </Form>}
          </Modal>
      );
  }
}
export default Reserve;