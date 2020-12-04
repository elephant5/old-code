import React, { Component, Fragment } from 'react';
import { Form, Select, Input, Button, Row, Col, Radio ,Divider,Icon, message,Spin } from 'antd';
import _ from 'lodash';
import debounce from 'lodash/debounce';
import { BASE_PANGU_URL } from '../../../util/url';
// import BMap from 'BMap';
// import {BMAP_HYBRID_MAP, BMAP_NORMAL_MAP} from "../../../common/BMAP_DATA";
const { Option } = Select;
const RadioGroup = Radio.Group;
const { TextArea,Search } = Input;

const formItemLayout = {
    labelCol: { span: 2 },
    wrapperCol: { span: 8 }
};
const formItemLayout2 = {
    labelCol: { span: 4 },
    wrapperCol: { span: 20 }
};
export const BMAP_NORMAL_MAP =window.BMAP_NORMAL_MAP;
export const BMAP_HYBRID_MAP = window.BMAP_HYBRID_MAP;
@Form.create()
class AddMerchant extends Component {
    constructor(props) {
        super(props);
        this.lastFetchId = 0;
        this.selectByHotelNameList = debounce(this.selectByHotelNameList, 800);
      }
    state = {
        // 城市
        citys: this.props.data.countryCity[0].cityDetail,
        // 商户类型
        shopTypeId: this.props.data.shopTypes[0].code,
        // 商户类型
        shopNature: 0,
        // 结算方式，结算货币是否展示
        isShowSettle: this.props.data.channels[0].internal,
        addressMap:{},
        hoteNameData: [],
        value: [],
        fetching: false,
    }
    componentDidMount(){
        var BMap = window.BMap//取出window中的BMap对象
        let map =new BMap.Map("allmap");
        map.centerAndZoom(new BMap.Point(121.487899486,31.24916171 ), 11);  // 初始化地图,设置中心点坐标和地图级别
        //添加地图类型控件
        map.addControl(new BMap.MapTypeControl({
            mapTypes:[
                BMAP_HYBRID_MAP,//混合地图
                BMAP_NORMAL_MAP//地图
            ]}));
        map.setCurrentCity("上海");          // 设置地图显示的城市 此项是必须设置的
        map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
        setTimeout(function(){
            map.enableDragging();   //两秒后开启拖拽
            map.enableInertialDragging();   //两秒后开启惯性拖拽
         }, 1000);
         setTimeout(function(){
            map.setZoom(14);   
        }, 1000);
        
        

    }
    componentWillReceiveProps(nextProps){
        // const { data, loading } = this.props;
        // console.log(data)
        // if(data.hotelSelList){
        //     this.setState({ hoteNameData:data.hotelSelList, fetching: false });
        // }else{
        //     this.setState({ hoteNameData:[], fetching: false });
        // }
    }

    handleSubmit = e => {
        e.preventDefault();
        const { onEvent } = this.props;
        this.props.form.validateFields((err, values) => {
            if (!err) {
                onEvent('nextStep', values)
            }
        });
    }
    getForm = () => {
        const { getFieldDecorator ,getFieldValue} = this.props.form;
        const { citys, shopTypeId, isShowSettle, shopNature } = this.state;
        const { shopTypes, countryCity, channels ,shop } = this.props.data;
        // console.log(shopTypeId)
        let formProps = {};
        formProps.shopTypeId = getFieldDecorator('shopTypeId', {
            initialValue: shopTypes[0].code,
            rules: [{
                required: true,
            }]
        });
        formProps.shopNature = getFieldDecorator('shopNature', {
            initialValue: 0,
            rules: [{
                required: true,
            }]
        });
        if(!shopNature  ){
            if(shopTypeId !== 'trip'){//shopTypeId !== 'coupon' &&  
                formProps.hotelNameCh = getFieldDecorator('hotelNameCh', {
                    rules: [{
                        required: true,
                        message: '请输入酒店名称'
                    }]
                });
            }
            
        }
        if(shopTypeId !== 'accom'){
            formProps.shopName = getFieldDecorator('shopName', {
                rules: [{
                    required: true,
                    message: '请输入商户名称'
                }]
            });
        }
        formProps.shopChannelId = getFieldDecorator('shopChannelId', {
            initialValue: channels[0].id,
            rules: [{
                required: true,
            }]
        });
        if(isShowSettle  ) {
            // if(shopTypeId !== 'coupon' &&  shopTypeId !== 'trip'){
                formProps.settleMethod = getFieldDecorator('settleMethod', {
                    rules: [{
                        required: true,
                    }]
                });
                formProps.settleCurrency = getFieldDecorator('settleCurrency', {
                    rules: [{
                        required: true,
                    }]
                });
            // }
        }
        if(  shopTypeId !== 'trip'){//shopTypeId !== 'coupon' &&
            formProps.country = getFieldDecorator('countryId', {
                initialValue: countryCity[0].countryId,
                rules: [{
                    required:  shopTypeId === 'coupon'?false:true,
                    // initialValue:"中国"
                }]
            });
            formProps.city = getFieldDecorator('cityId', {
                initialValue: !_.isEmpty(citys)? citys[0].cityId: '',
                rules: [{
                    required:  shopTypeId === 'coupon'?false:true,
                }]
            });
            formProps.address = getFieldDecorator('address', {
                rules: [{
                    required:  shopTypeId === 'coupon'?false:true,
                    message: '请输入酒店地址'
                }]
            });
            formProps.coordinate = getFieldDecorator('coordinate', {
                initialValue: null,
                rules: [{
                    required: shopTypeId === 'coupon'?false:true,
                    message: '请获取酒店地址坐标'
                }]
            });
        }
        
        formProps.notes = getFieldDecorator('notes', {
        });
        return formProps;
    }
    

    // 国家选择
    changeCountry = value => {
        this.setState({
            citys: this.props.data.countryCity.filter(item => item.countryId === value)[0].cityDetail
        })
    }
    // 类型选择
    changeType = e => {
        // console.log(e.target.value)
        this.setState({
            shopTypeId: e.target.value
        });
        if(e.target.value === 'coupon' ||  e.target.value === 'trip' ){
            const {getFieldValue,setFieldsValue} = this.props.form;
            setFieldsValue({"shopName":" "});
            setFieldsValue({"hotelNameCh":" "});
            setFieldsValue({"cityId":" "});
            setFieldsValue({"address":" "});
            setFieldsValue({"coordinate":" "});
        }
        
    }
    // 商户性质
    changeNature = e => {
        this.setState({
            shopNature: e.target.value
        })
    }
    // 商户渠道切换
    changeChannel = id => {
        this.setState({
            isShowSettle: this.props.data.channels.filter(item => item.id === id)[0].internal
        })
        
    }
    getAddressMap = value =>{
        const {getFieldValue,setFieldsValue} = this.props.form;
        var BMap = window.BMap//取出window中的BMap对象
        let map =new BMap.Map("allmap");
        if(value){
             // 创建地址解析器实例     
        var myGeo = new BMap.Geocoder();      
        // 将地址解析结果显示在地图上，并调整地图视野    
        myGeo.getPoint(value, function(point){      
            if (point) { 
                setFieldsValue({"coordinate":point.lng+","+point.lat}); 
                map.centerAndZoom(point, 16);      
                map.addOverlay(new BMap.Marker(point));      
            }else{
                message.error("输入的地址有误，请重新输入！",);
                setFieldsValue({"coordinate":null,"address":null}); 
            }   
        }, 
        "上海市");
        }else{
            message.error("输入的地址有误，请重新输入！",);
            setFieldsValue({"coordinate":null,"address":null}); 
        }
    }

    handleChange = (value) => {
        this.setState({
          value,
          hoteNameData: [],
          fetching: false,
        });
      }
      selectByHotelNameList = (value) => {
        this.lastFetchId += 1;
        const fetchId = this.lastFetchId;
        this.setState({ hoteNameData: [], fetching: true });
       
        fetch(BASE_PANGU_URL+'/hotel/selectByHotelNameList',{method:'post',body:value})
          .then(response => response.json())
          .then((body) => {
            if (fetchId !== this.lastFetchId) { // for fetch callback order
              return;
            }
            if(body.result && body.result.length > 0  ){
                const data = body.result.map(user => ({
                    text: user.nameCh ,
                    value: user.nameCh,
                  }));
                  this.setState({ hoteNameData:data, fetching: false });
            }else{
                const data =[{
                    text: value,
                    value: value,
                  }];
                this.setState({ hoteNameData:data, fetching: false });
            }
           
          });
        // const { onEvent } = this.props;
        // onEvent('selectByHotelNameList', value)
      }
    
    render() {
        const { data, loading } = this.props;
        const formProps = this.getForm();
        const { citys, shopTypeId, shopNature, isShowSettle,fetching, hoteNameData, value,hotelSelList  } = this.state;
        return (
            <div className="c-modal">
                <div className="c-title">基本信息</div>
            <Form onSubmit={this.handleSubmit}>
                <Form.Item label="商户类型" {...formItemLayout} style={{marginTop:'20px'}}>
                    {formProps.shopTypeId(
                        <RadioGroup onChange={this.changeType}>
                            {
                                data.shopTypes.map(item => <Radio value={item.code}  key={item.code}>{item.name}</Radio>) 
                            }
                        </RadioGroup>
                    )}
                </Form.Item>
                <Divider />
                {
                    shopTypeId !== 'accom' && shopTypeId !== 'coupon' && shopTypeId !== 'trip' && 
                    <Form.Item label="商户性质" {...formItemLayout}>
                        {formProps.shopNature(
                            <RadioGroup onChange={this.changeNature}>
                                <Radio value={0}>附属商户</Radio>
                                <Radio value={1}>独立商户</Radio>
                            </RadioGroup>
                        )}
                    </Form.Item>
                    
                }
                {//shopTypeId !== 'coupon' &&
                    !shopNature &&  shopTypeId !== 'trip' && 
                    <Form.Item label="关联酒店：" {...formItemLayout}> 
                        {formProps.hotelNameCh(
                            <Select placeholder="请输入酒店名称或搜索关联酒店名称" 
                            modal="multiple"
                            showSearch 
                            // labelInValue
                            // value={value}
                            notFoundContent={fetching ? <Spin size="small" /> : null}
                            filterOption={false}
                            onSearch={this.selectByHotelNameList}
                            onChange={this.handleChange}
                            >
                            {hoteNameData.map(d => <Option key={d.value}>{d.text}</Option>)}
                        </Select>
                        )}
                    </Form.Item>
                }
                {
                    shopTypeId !== 'accom' &&
                    <Form.Item label="商户名称：" {...formItemLayout}>
                        {formProps.shopName(
                            <Input placeholder="请输入商户名称"/>
                        )}
                    </Form.Item>
                }
                
                        <Form.Item label="商户渠道：" labelCol={{span: 2}}
                        wrapperCol={{span: 18}}>
                            {formProps.shopChannelId(
                                <Select
                                    style={{ width: "18%"}}
                                    placeholder='请选择'
                                    onChange={this.changeChannel} 
                                    showSearch
                                    filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}
                                >
                                    {
                                        data.channels.map(item => (
                                            <Option key={item.id} value={item.id}>{item.name}</Option>
                                        ))
                                    }
                                </Select>
                            )}&nbsp;&nbsp;&nbsp;&nbsp;{isShowSettle === 1 && (<label style={{marginLeft:'2%'}}>结算方式：</label>)}
                             {isShowSettle === 1 && 
                                formProps.settleMethod(
                                    <Select
                                        style={{ width: "18%"}}
                                        placeholder='请选择'
                                        showSearch
                                filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}
                                    >
                                        {
                                            data.settleMethods.map(item => (
                                                <Option key={item.settleMethod} >{item.settleMethod}</Option>
                                            ))
                                        }
                                    </Select>
                                )}&nbsp;&nbsp;&nbsp;&nbsp; {isShowSettle === 1 && (<label style={{marginLeft:'5%'}}>结算货币：</label>) }
                                {isShowSettle === 1 &&  formProps.settleCurrency(
                                    <Select
                                        style={{ width: "18%" }}
                                        placeholder='请选择' showSearch
                                        filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}
                                    >
                                        {
                                            data.currencys.map(item => (
                                                <Option key={item.code} value={item.code}>{item.name}</Option>
                                            ))
                                        }
                                    </Select>
                                )}
                        </Form.Item>
                        {
                     shopTypeId !== 'trip' && <div>
                        <Divider dashed style={{marginTop:0}}></Divider>
                        <Row >
                        <Col span={12}>
                        <Form.Item label="城市："  labelCol={{span: 4}}
                        wrapperCol={{span: 20}} >
                            {formProps.country(
                                <Select
                                style={{ width: "33%" }}
                                    placeholder='请选择'
                                    onChange={this.changeCountry}
                                    showSearch
                                    filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}
                                >
                                    {
                                        data.countryCity.map(item => <Option key={item.countryId}>{item.countryName}</Option>)
                                    }
                                </Select>
                            )}&nbsp;&nbsp;
                            {formProps.city(
                                <Select
                                style={{ width: "33%" ,marginLeft:"13%"}}
                                    placeholder='请选择'
                                    showSearch
                                    filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}
                                >
                                    {
                                        citys.map(item =>(
                                            <Option key={item.cityId}>{item.cityName}</Option>
                                        ))
                                    }
                                </Select>
                            )}   
                        </Form.Item>
                        </Col>
                        <Col span={12} order={2}>
                                <div  id={"allmap"} style={{width:'100%',height:180,border:'white solid 1px',float:"left",position:'absolute',paddingRight:'10px'}}></div>
                        </Col>
                        </Row>
                        
                       
                        <Row>
                        <Col span={12}>
                        <Form.Item label="地址：" {...formItemLayout2}>
                            {formProps.address(
                                <Search
                                placeholder="请输入商户地址（中文）并搜索地址坐标" style={{ width: "80%"}}
      onSearch={ this.getAddressMap}
      enterButton
    />
                            )}
                        </Form.Item>
                        </Col>
                        <Col span={0}></Col>
                        </Row>
                        <Row>
                        <Col span={12}>
                        <Form.Item
                                label="坐标："
                                {...formItemLayout2} 
                            >
                                {formProps.coordinate(
                                    <Input style={{ width: "40%"}} enterButton allowClear={true} disabled={true}/>
                                )}
                            </Form.Item>
                        </Col>
                        </Row>
                        </div>}
                        <Divider dashed style={{marginTop:0}}></Divider>
                        <Row>
                        <Col span={12}>
                        <Form.Item
                            label="备注："
                            {...formItemLayout2} 
                        >
                            {formProps.notes(
                                <TextArea placeholder="请输入"  rows={4}/>
                            )}
                        </Form.Item>
                        </Col>
                        </Row>
                
                <Form.Item>
                    <Row gutter={10}>
                        <Col span={2} offset={4}>
                            <Button
                                type="primary"
                                htmlType="submit"
                                loading={loading}
                            >
                                下一步
                            </Button>
                        </Col>
                        <Col span={2}>
                            <Button>
                                取消
                            </Button>
                        </Col>
                    </Row>
                </Form.Item>
                <div><br></br></div>
            </Form>
            </div>
        );
    }
}

export default AddMerchant;