import React, { Component } from 'react';
import { Table ,Tag,Icon} from 'antd';
import iconFont from '../../../util/iconfont.js'
import { resourColor , serviceColor} from '../../../util/dictType.js'
import cookie from 'react-cookies';
import { withRouter, Link } from 'react-router';

const IconFont = Icon.createFromIconfontCN({
    scriptUrl: iconFont
    });
class List extends Component {
    constructor(props) {
        super(props);
        this.state = {  };
    }
    getColumns = () => {
        const view = cookie.load("KLF_PG_RM_SL_VIEW");
        return [
            {
                title: '商户ID',
                dataIndex: 'id',
            },
            {
                title: '城市',
                dataIndex: 'cityName',
                render:text =>{
                    return text ?text:"-";
                }
            },
            {
                title: '酒店|商户',
                dataIndex: 'hotelNameAndShopName',
            },
            {
                title: '商户类型',
                dataIndex: 'shopServiceName',
                render:(text,record) =>{
                    if(text){
                        return resourColor.map(item => {
                            if(item.type === record.shopType){
                                return <Tag color={item.color} key={text} >{text}</Tag>;
                            }
                        });
                    }else{
                        return "-";
                    }
                }
            },
            {
                title: '资源类型',
                dataIndex: 'serviceName',
                render:text =>{
                    // return  <Tag color="" key={text} >{text}</Tag> ;
                    if(text){
                        return text.split(" ").map(ser =>{
                      return   serviceColor.map(item => {
                            if(item.name === ser){
                                return (<Tag color={item.color} key={ser} >{ser}</Tag>);
                            }
                        });
                            
                            
                        });
                    }else{
                        return "-";
                    }
                   
                }
            },
            {
                title: '渠道类型',
                dataIndex: 'shopChannelName',
            },
            {
                title: '商户资源',
                dataIndex: 'channelName',
            },
            {
                title: '图片',
                dataIndex: 'shopTagId',
                render:text =>{
                    // return <IconFont type='icontupian1' />;
                    if(text === 1 ){
                        return ( <IconFont type='icontupian'  />);
                      }else{
                        return ( <IconFont type='icontupian1'  />);
                      }
                }
                
            },
            {
                title: '操作',
                render: (text, record) => {
                    return view ? <Link to={{ pathname: "/addtwo/"+text.id,}} target="_blank" className="fontBlue fontWeight" activeStyle={{ color: 'red' }}>
                    编辑</Link> : null
                }
            },
        ]
    }

    // 切换分页
    handleChange = value => {
        this.props.onChange({
            current: value.current,
            size: value.pageSize,
        })
      }
    render() {
        const { data, loading } = this.props;
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