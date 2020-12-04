import React, { Fragment, Component } from 'react';
import { Table, Divider, Badge, Modal } from 'antd';
import { getLabelByType } from "../../../util/dictType"
import cookie from 'react-cookies';
import moment from 'moment';
const states = ['未发送','发送成功', '发送失败'];

class List extends Component {


    constructor(props) {
        super(props);
        this.state = {
          
          }
    }
    
    getColumns = () => {
        const add = cookie.load("KLF_PG_SM_SMS_SEND");
        return [{
            title: 'ID',
            dataIndex: 'id',
            align:'center'
        }, {
            title: '手机号',
            dataIndex: 'phone',
           
        }, {
            title: '短信内容',
            dataIndex: 'content',
            
        }, {
            title: '发送时间',
            dataIndex: 'createTime',
            
            width:180,
            align:'center',
           render(text) {
               if(text){
                return moment(text).format('YYYY-MM-DD HH:mm:ss');
               }
           
          },
        }, {
            
            title: '状态',
            align:'center',
            dataIndex: 'state',
            width:90,
            style:{ wordBreak: 'keep-all',whiteSpace:'nowrap'},
            render(text) {
                if(text){
                    return states[text];
                }
              },
        }, {
            
            title: '操作',
            align:'center',
            dataIndex: 'action',
            width:80,
            render: (text, record) => {
            return <Fragment>{add && <span className='c-color-blue' style={{'cursor':'pointer'}} onClick={ ()=> this.sendMsg(record)}>重发</span>}</Fragment> ;
              },
        },
        ]
    }

    handleChange = value => {
        this.props.onList({
            current: value.current,
            size: value.pageSize,
        })
    }
    sendMsg = params => {
        const { onEvent } = this.props;
        this.props.againSendMsg(params);
    }
    render() {
        const { loading, data } = this.props;
        const pagination = {
            page: data.current,
            total: data.total,
            defaultSize: 10
        }

        return (
            <Table
                columns={this.getColumns()}
                dataSource={data.records}
                pagination={this.props.pagination(pagination)}
                rowKey="id"
                loading={loading}
                onChange={this.handleChange}
            />
        );
    }
}

export default List;

