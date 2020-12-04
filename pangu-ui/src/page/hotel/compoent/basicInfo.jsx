import React, { Component } from 'react';
import { Form, Select, Input, Button, Row, Col,Divider,message,Icon } from 'antd';
import cookie from 'react-cookies';
// import BMap from 'BMap';
const { Option } = Select;
const { TextArea,Search } = Input;
export const BMAP_NORMAL_MAP =window.BMAP_NORMAL_MAP;
export const BMAP_HYBRID_MAP = window.BMAP_HYBRID_MAP;
const formItemLayout = {
    labelCol: { span: 2 },
    wrapperCol: { span: 8 },
  };

@Form.create()
class BasicInfo extends Component {
    constructor(props) {
        super(props);
        this.state = { 
            citys: props.data.countryCity.filter(item => item.countryId === props.data.hotel.countryId)[0].cityDetail
        };
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
        const { onEvent, data } = this.props;
        if( data.hotel.addressCh){
            this.getAddressMap( data.hotel.addressCh)
        }
        
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
    // 保存基本信息
    handleSubmit = e => {
        e.preventDefault();
        const { onEvent, data } = this.props;
        this.props.form.validateFields((err, values) => {
            if (!err) {
              onEvent('saveBaseInfo', {
                "addressCh": values.address,
                "addressEn": values.enAddress,
                "cityId": values.city,
                "hotelNameCh": values.hotelNameCh,
                "hotelNameEn": values.hotelNameEn,
                "id": data.hotel.id,
                "username":values.username,
                "accountType":1,
                "password":values.password,
                "star": values.starLevel = '请选择'? null: values.starLevel
              })
            }
        });
    }
    // 表单绑定
    getForm = () => {
        const { getFieldDecorator } = this.props.form;
        const { data } = this.props;
        let formProps = {};
        formProps.hotelName = getFieldDecorator('hotelName', {
            initialValue: data.hotel.nameCh
        });
        formProps.hotelEnName = getFieldDecorator('hotelEnName', {
            initialValue: data.hotel.nameEn
        });
        formProps.country = getFieldDecorator('country', {
            initialValue: data.hotel.countryId
        });
        formProps.city = getFieldDecorator('city', {
            initialValue: data.hotel.cityId
        });
        formProps.starLevel = getFieldDecorator('starLevel', {
            initialValue: '请选择'
        });
        formProps.address = getFieldDecorator('address', {
            initialValue: data.hotel.addressCh
        });
        formProps.enAddress = getFieldDecorator('enAddress', {
            initialValue: data.hotel.addressEn
        });
        formProps.coordinate = getFieldDecorator('coordinate', {
            initialValue: null
        });
        return formProps;
    }
    // 选择国家
    selectCountry = value => {
        const country = this.props.data.countryCity.filter(item => item.countryId === value);
        this.setState({
            citys: country[0].cityDetail
        })
    }

    
    render() {
        const formProps = this.getForm();
        const { getFieldDecorator } = this.props.form;
        const { data } = this.props;
        const { citys } = this.state;
        const edit = cookie.load("KLF_PG_RM_HL_EDIT");
        return (
            <div className="c-modal">
                <div className="c-title">基本信息</div>
                <Form onSubmit={this.handleSubmit}>
                <div><br></br></div>
                    <Form.Item
                        label="酒店名称："
                        labelCol={{span: 2}}
                        wrapperCol={{span: 16}}
                    >
                       
                                    {formProps.hotelName(
                                        <Input placeholder="请输入酒店名称"  style={{ width: '50%' }}/>
                                    )}&nbsp;&nbsp;&nbsp;
                                    {formProps.hotelEnName(
                                        <Input placeholder="请输入酒店名称（英文）"  style={{ width: '48%' }}/>
                                    )}
                    </Form.Item>
                    <Form.Item
                        label="商户星级："
                        {...formItemLayout} 
                    >
                        {formProps.starLevel(
                            <Select
                              style={{ width: 200 }}
                              placeholder='请选择'
                            >
                              <Option value="1" >挂星</Option>
                              <Option value="0">其余</Option>
                            </Select>,
                        )}
                    </Form.Item>
                    <Divider dashed={true}/>
                    <Form.Item
                            label="酒店账号"
                            labelCol={{ span: 2 }}
                            wrapperCol={{ span: 16 }}
                        >

                            {getFieldDecorator('username', {
                                initialValue: data.hotel.username
                            })(
                                <Input style={{ width: '29%' }} prefix={<Icon type="user" style={{ fontSize: 13 }} />} />
                            )}<span style={{ color: 'grey', fontSize: '12px', marginLeft: '10px' }}>可以使用字母、数字和符号，不能跟已有的用户名重复</span>
                        </Form.Item>
                        <Form.Item
                            label="酒店密码"
                            labelCol={{ span: 2 }}
                            wrapperCol={{ span: 16 }}
                        >
                            {getFieldDecorator('password', {
                                initialValue: data.hotel.password
                            })(
                                <Input prefix={<Icon type="lock" style={{ fontSize: 13 }} />} style={{ width: '29%' }} />
                            )}<span style={{ color: 'grey', fontSize: '12px', marginLeft: '10px' }}>可以使用字母、数字和符号</span>
                        </Form.Item>
                        <Divider dashed={true}/>
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
                            
                            {getFieldDecorator('city', { initialValue: data.hotel.cityId})
                                (<Select
                                    style={{ width: "33%" ,marginLeft:"13%"}}
                                        placeholder='请选择'
                                        showSearch
                                        filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}
                                    >
                                        {
                                            citys.map(item =>(
                                                <Option key={item.cityId} value={item.cityId}>{item.cityName}</Option>
                                            ))
                                        }
                                    </Select>
                                )}  
                        </Form.Item>
                        </Col>
                        <Col span={12} order={2}>
                                <div  id={"allmap"} style={{width:'100%',height:280,border:'white solid 1px',float:"left",position:'absolute',paddingRight:'10px'}}></div>
                        </Col>
                        </Row>
                    <Form.Item
                        label="地址："
                        {...formItemLayout} 
                    >
                        {formProps.address(
                            <Search
                            placeholder="请输入商户地址（中文）" style={{ width: "80%"}}
  onSearch={ this.getAddressMap} 
  enterButton
/>
                        )}
                         
                    </Form.Item>
                    <Form.Item
                        label="英文地址："
                        {...formItemLayout} 
                    >
                        {formProps.enAddress(
                            <Input placeholder="请输入酒店地址（英文）"/>
                        )}
                    </Form.Item>
                    <Form.Item
                        label="坐标："
                        {...formItemLayout} 
                    >
                        {formProps.coordinate(
                            <Input disabled={true}/>
                        )}
                    </Form.Item>
                    <Form.Item 
                        wrapperCol={{
                            span: 8, offset: 4
                        }}
                    >
                        { edit && <Button
                            type="primary"
                            htmlType="submit"
                            loading={this.props.loading}
                        >
                            保存
                        </Button>}
                    </Form.Item>
                </Form>
            </div>
        );
    }
}

export default BasicInfo;