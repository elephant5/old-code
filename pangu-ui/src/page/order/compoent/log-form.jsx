import React, { Component } from 'react';
import {  Form, Input,Button,Table} from 'antd';
import locale from 'antd/lib/date-picker/locale/zh_CN';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { List } from 'immutable';
import moment from 'moment';
const { TextArea } = Input;
@Form.create()
class OrderLogForm extends Component {
    constructor(props) {
        super(props);
        this.state = {
            btnLoading:false,
        };
    }
    // 保存
    handleSubmit = e => {
        e.preventDefault();
        const { onEvent} = this.props;
        this.props.form.validateFields((err, values) => {
            if (!err) {
                let {reservOrderInfo} =this.props;
                
                onEvent('insertLog', {
                    orderId: reservOrderInfo.id,
                    oldProseStatus: reservOrderInfo.proseStatus,
                    nowProseStatus: reservOrderInfo.proseStatus,
                    content: values.remarks,
                })
                
            }
        });
    }
    resetFields =()=>{
        this.props.form.resetFields();
    }
    componentDidMount() {
        
    }
    getColumns = () => {
        return [
        {
            title: '操作人',
            dataIndex: 'createUser',
        },
        {
            title: '操作时间',
            dataIndex: 'createTime',
            render: (text,row,index) => {
                return text != null && text !== '' && <div>{moment(text).format('YYYY-MM-DD  HH:mm:ss')}</div>
            }
        },
        {
          title: '操作内容',
          dataIndex: 'content',
          render: (text,row,index) => {
            return row != null && row !== '' && <div>{(row.operType=='sys_auto'?'【系统自动】':'【客服备注】')+row.content}</div>
        }
        }]
      }
    render() {
        const { getFieldDecorator } = this.props.form;
        let { orderLogList } = this.props
        return (<div>
             <Form onSubmit={this.handleSubmit}>
                    <Form.Item>
                        {getFieldDecorator('remarks', {rules: [{ required: true, message: '请填写留言内容!'}]})(
                            <TextArea autosize={{ minRows: 4, maxRows: 8 }}  />
                        )}
                    </Form.Item>
                    <Form.Item>
                        <Button
                                style={{ marginLeft: 10 }}
                                type="primary"
                                // loading={btnLoading}
                                htmlType="submit"
                            >
                                提交留言
                        </Button>
                    </Form.Item>
             </Form>
             <Table 
                columns={this.getColumns()} 
                dataSource={orderLogList} 
            />
    
        </div>)
               
    }
}

export default OrderLogForm;