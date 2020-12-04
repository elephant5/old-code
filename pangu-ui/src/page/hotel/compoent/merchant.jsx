import React, { Component } from 'react';
import { Table,Icon ,Tag} from 'antd';
import iconFont from '../../../util/iconfont.js'
import { resourColor , serviceColor} from '../../../util/dictType.js'
const IconFont = Icon.createFromIconfontCN({
  scriptUrl: iconFont
  });
class Merchant extends Component {
    constructor(props) {
        super(props);
        this.state = {  };
    }

    getColumns = () => {
      const { onEvent } = this.props;
        return [{
            title: '商户ID',
            dataIndex: 'id',
            render: text => <a href="javascript:;" onClick={() => onEvent('selectShopById', text)} >{text}</a>,
          },
            {
            title: '商户名称',
            dataIndex: 'shopNameCh',
            render: (text,record) => <a href="javascript:;" onClick={() => onEvent('selectShopById', record.id)} >{text}</a>,
          },
            {
            title: '资源类型',
            dataIndex: 'serviceName',
            render:text =>{
              // return  <Tag color="" key={text} >{text}</Tag> ;
              if(text){
                return   serviceColor.map(item => {
                      if(item.name === text){
                          return (<Tag color={item.color} key={text} >{text}</Tag>);
                      }
                      
                  });
              }else{
                  return "-";
              }
             
          }
          },
            {
            title: '渠道资源',
            dataIndex: 'shopChannelType'
          },
            {
            title: '图片',
            dataIndex: 'delFlag',
            render:text =>{
              // return <IconFont type='icontupian1' />;
              if(text === 1 ){
                  return ( <IconFont type='icontupian'  />);
                }else{
                  return ( <IconFont type='icontupian1'  />);
                }
          }
          },
        ]
    }

    render() {
        const { data } = this.props;
        return (
            <div className="c-modal">
                <div className="c-title">关联商户</div>
                <Table 
                    columns={this.getColumns()} 
                    dataSource={data} 
                    pagination={false}
                    rowKey="id"
                    loading={false}
                />
            </div>
        );
    }
}

export default Merchant;