import React, { Component } from 'react';
import { Modal, Form, Radio,Input,DatePicker,Button,Select} from 'antd';
import locale from 'antd/lib/date-picker/locale/zh_CN';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as actions from '../../../store/giftCode/action';
import * as goodsActions from '../../../store/goods/action';

const { Option } = Select;
const { TextArea } = Input;
const { MonthPicker, RangePicker, WeekPicker } = DatePicker;

@connect(
    ({ operation, code, goods }) => ({
        operation,
        code: code.toJS(),
        goods: goods.toJS()
    }),
    dispatch => ({
        actions: bindActionCreators({ ...actions, ...goodsActions }, dispatch)
    })
)
@Form.create()
class ProlongActCodeModal extends Component {
    constructor(props) {
        super(props);
        this.state = {
            prolongType:'D',
            monthSelect:[]
        };
    }

    componentDidMount() {
        let monthSelect=[];
        for(let i=1;i<13;i++){
            let month={};
            month.value = i;
            month.title=i+"个月";
            monthSelect.push(month);
        }
        this.setState({
            monthSelect
        });
    }

    componentWillReceiveProps(nextProps) {
        
    }

    //延长激活码有效期提交
    handleSubmit = e => {
        e.preventDefault();
        const {codeIds,prolongGiftCodeOK} = this.props;
        this.props.form.validateFields((err, values) => {
            if (!err) {
                let prolongDateStr="";
                if(values.prolongType=='D'){
                    let prolongDate =values.prolongDate._d;
                    prolongDateStr = prolongDate.getFullYear()+"-"+(prolongDate.getMonth()+1)+"-"+prolongDate.getDate();
                }else{
                    prolongDateStr = values.prolongDateMonth;
                }
                prolongGiftCodeOK({
                    giftCodeIds:codeIds,
                    prolongType: values.prolongType,
                    prolongDate: prolongDateStr,
                    remarks: values.remarks,
                })
            }
        })
    }

    updateProlongType =prolongType=>{
        this.setState({ prolongType });
    }
    

    render() {
        const { prolongCodeVisible, prolongCodeCancel, prolongCodeDistroy,prolongCodeOkLoading } = this.props;
        let {prolongType,monthSelect} =this.state;
        const { getFieldDecorator } = this.props.form;
        return (
            <Modal
                title="延长有效期"
                visible={prolongCodeVisible}
                // onOk={this.handleSubmit}
                onCancel={prolongCodeCancel}
                footer={null}
                destroyOnClose={prolongCodeDistroy}
            // cancelText="取消"
                // okText="确定"
            >
                <Form onSubmit={this.handleSubmit} >
                    <Form.Item label="延长模式:">
                        {getFieldDecorator('prolongType', {initialValue: prolongType,rules: [{ required: true, message: '请选择延长模式!' }],} )(
                            <Radio.Group defaultValue="D" buttonStyle="solid" >
                                <Radio.Button value="D" onClick={()=>{this.updateProlongType('D')}}>特定日期</Radio.Button>
                                <Radio.Button value="XM" onClick={()=>{this.updateProlongType('XM')}}>顺延数月</Radio.Button>
                            </Radio.Group>
                        )}
                    </Form.Item>
                    <Form.Item >
                        {prolongType==='D'? getFieldDecorator('prolongDate', {rules: [{ required: true, message: '请选择时间!' }]})(
                                <DatePicker  locale={locale} placeholder="重置有效期"/>):getFieldDecorator('prolongDateMonth', {rules: [{ required: true, message: '请选择时长!' }],})(
                                    <Select  style={{ width: 120 }} placeholder="选择时长">
                                        {
                                            monthSelect.map(item=>{
                                                return (
                                                    <Option value={item.value} key={item.value}>{item.title}</Option>
                                                );
                                            })
                                           
                                        }
                                    </Select>
                                    )
                        }
                    </Form.Item>
                    <Form.Item label="操作备注:">
                        {getFieldDecorator('remarks', {rules: [{ required: true, message: '请填写备注!' }]})(
                            <TextArea autosize={{ minRows: 4, maxRows: 8 }} placeholder="备注说明" />
                        )}
                    </Form.Item>
                    <Form.Item>
                        <Button onClick={prolongCodeCancel}>
                                关闭
                        </Button>
                        <Button
                                style={{ marginLeft: 10 }}
                                type="primary"
                                onClick={this.handleSubmit}
                                loading={prolongCodeOkLoading}
                            >
                                确定
                        </Button>
                    </Form.Item>
                </Form>
            </Modal>
        );
    }
}

export default ProlongActCodeModal;