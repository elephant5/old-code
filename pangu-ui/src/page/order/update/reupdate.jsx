import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { Form, Input, Button, Select ,Table,Radio, Col,InputNumber,Cascader,DatePicker,Divider,Modal} from 'antd';
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
class ReUpdate extends Component {
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
        },
      
      ];
      this.state = { 
        resourceList:[],
        groupProduct:{},
        rooms:[],
        deparDateList:[],
        dataSource :[],
        orderSettleAmount:0,
        checked: true,
        shopAmount	:0,//应付金额
        // cancelDamageAmount:0,
        orderDamageAmount	:0,//商户赔付金额,
        channel:{}
      };
  }
  componentDidMount() {
    const {reservOrderInfo,newShopDetail,GoodsGroupListRes,channels} = this.props;
    
    const resourceList  =  GoodsGroupListRes[0].groupProductList.filter(item =>  item.shopId  === reservOrderInfo.shopId);
    this.setState({resourceList:resourceList,dataSource :reservOrderInfo.shopSettleMsgRes});
    const {setFieldsValue} = this.props.form;
    if(resourceList.length > 0 ){
      setFieldsValue({
        "productId":resourceList.length,
        // orderSettleAmount:reservOrderInfo.orderSettleAmount,//结算价
        orderDamageAmount:reservOrderInfo.orderDamageAmount //商户赔付
    });
    }
    let channelstemp  = channels.filter(item =>  item.id  === reservOrderInfo.shopChannelId);
  this.setState({
    shopAmount:reservOrderInfo.orderSettleAmount+reservOrderInfo.orderDamageAmount,
    orderDamageAmount:reservOrderInfo.orderDamageAmount,
    orderSettleAmount:reservOrderInfo.orderSettleAmount
    ,channel:channelstemp[0] ,
  });
  }
  // 保存基本信息
  handleSubmit = e => { 
    e.preventDefault();
    const {onEvent ,closeEditModel,newShopDetail,reservOrderInfo,GoodsGroupListRes} = this.props;
    this.props.form.validateFields((err, values) => {
      const { resourceList ,rooms,deparDateList,dataSource} = this.state;
        if (!err) {
          console.log(values)
          // values.giftType = tempShopItem[0].gift;
          // values.productGroupId = productGroup[0].id;
          let deparDate = deparDateList.filter(item => values.deparDate === item.value);
          // values.nightNumbers = deparDate[0].night;
          values.messageFlag  = values.messageFlag === true  ?'1':"0";
          values.shopSettleMsgRes= dataSource;
          values.id  = reservOrderInfo.id;
            onEvent('reservUpdateInfo',values);
        }
    });
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
    console.log('checked = ', e.target.checked);
    this.setState({
      checked: e.target.checked,
    });
  };
  handleSave = row => {
    console.log(row)
    const {orderDamageAmount} = this.state;
    const newData = [...this.state.dataSource];
    const index = newData.findIndex(item => row.priceId === item.priceId);
    const item = newData[index];
    newData.splice(index, 1, {
      ...item,
      ...row,
    });

    let tatolPrice  = 0;
    newData.map(item => {
      let num3= Number(item.realPrice) * (item.nightNumbers ? item.nightNumbers: 1); 
      tatolPrice = tatolPrice + num3;
    });
    this.setState({ dataSource: newData 
      ,orderSettleAmount:tatolPrice,
      shopAmount:tatolPrice+orderDamageAmount  });
  };
  addPrice = values  =>{
    const {orderSettleAmount} = this.state;
    if(values){
      let cancelDamageAmount  = values + orderSettleAmount;
      this.setState({shopAmount:cancelDamageAmount,orderDamageAmount:values});
    }else{
      this.setState({shopAmount:orderSettleAmount,orderDamageAmount:0});
    }
  }
  getChannel = values =>{
    const {channels} = this.props;
    let temp  = channels.filter(item =>  item.id  === values);
    this.setState({channel:temp[0]});
  }
  render() {
    const {onEvent ,closeReserveModel,groupProductList,newShopDetail,reservOrderInfo,GoodsGroupListRes,channels,reUpdateLoading} = this.props;
    const { resourceList ,rooms,cancelDamageAmount,dataSource,tatolPrice,shopAmount,orderSettleAmount,channel} = this.state;
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
        title="预定订单预订信息修正"
        visible={true}
        onCancel={closeReserveModel}
        onOk={this.handleSubmit}
        confirmLoading={reUpdateLoading}
        cancelText="取消"
        okText="确定"
    >
       <Form onSubmit={this.handleSubmit}>
           
            <Form.Item  verticalGap={1}  label="预订渠道" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal"  indicatorGap >
                {getFieldDecorator('shopChannelId',{ rules: [{ required: true, message: '请选择预订渠道!' }],initialValue:reservOrderInfo.shopChannelId})(
                    <Select   style={{width : "50%"}} onChange={this.getChannel}>
                        {channels.map((temp,index) => {
                            return (<Option value={temp.id} key={'channels'+temp.id}  >{temp.name}</Option>);
                        })}
                    
                    </Select>
                )} &nbsp;&nbsp; 
                {reservOrderInfo.serviceType === '住宿'  && channel.internal === 0 && getFieldDecorator('channelNumber',{ initialValue:reservOrderInfo.channelNumber})(
                    <Input  style={{width : "45%"}} placeholder="渠道号(第三方资源)" ></Input>
                )} 
            </Form.Item>
            <Form.Item label="商户结算：" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal"  indicatorGa>
                <Table   components={components} size="middle"
          rowClassName={() => 'editable-row'} dataSource={dataSource }  columns={columns} loading={false} bordered={true} pagination={false}/>
            </Form.Item>
            <Form.Item label="结算总额" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal"  indicatorGa>
               {orderSettleAmount}
            </Form.Item>
            <Form.Item  label="赔付金额" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal"  indicatorGap>
            {getFieldDecorator('orderDamageAmount',  { initialValue:reservOrderInfo.orderDamageAmount})(
                 <InputNumber onChange={this.addPrice}></InputNumber>
            )} 
            </Form.Item>
            <Form.Item  label="应付总额" labelCol={{ span: 5 }} wrapperCol={{ span: 18 }} direction="horizontal"  indicatorGap>
              {shopAmount}
            </Form.Item>
          </Form>
          </Modal>
      );
  }
}
export default ReUpdate;