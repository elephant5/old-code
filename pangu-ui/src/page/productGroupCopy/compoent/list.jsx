import React, { Fragment, Component } from 'react';
import { Table, Divider, Input ,Tag} from 'antd';
import { resourColor , serviceColor,giftColor} from '../../../util/dictType.js'
class ProductGroupInfoList extends Component {
  constructor(props){
    super(props);
  }
    getColumns = () => {
      const { onEvent } = this.props;
      return [{
        title: '产品组ID',
        dataIndex: 'id',
      }, {
        title: '内部简称',
        dataIndex: 'shortName',
        render: (text,record) => <a onClick={()=>onEvent("selectById",record.id)}>{text}</a>,
      },{
        title: '商品ID',
        dataIndex: 'goodsId',
      }, {
        title: '商品简称',
        dataIndex: 'goodsShortName',
      }, {
        title: '销售渠道',
        dataIndex: 'bankId',
        render: (text, record) => {
          const temp = record.salesChannelId ? record.salesChannelId: '-';
          return text+"/" +temp+"/"+record.salesWayId;
        },
      },
       {
        title: '资源类型',
        dataIndex: 'service',
        render:(text,record) =>{
          // let num=Math.floor(Math.random()*11);
          // return (<Tag color={doc[num]} key={text} >{text}</Tag>);
          if(text){
            
              return  text.split(" ").map(ser =>{
                return serviceColor.map(item => {
                    if(item.name === ser){
  
                      return <Tag color={item.color} key={ser} >{ser}</Tag>;
                  }
              });
                
            });
        }else{
            return "-";
        }
      }
      },
      {
        title: '权益类型',
        dataIndex: 'gift',
        render:text =>{
          if(text){
            
             return  text.split(" ").map(gift =>{
              return giftColor.map(item => {
                if(item.name === gift){
                  return <Tag color={item.color} key={gift} >{gift}</Tag>;
               }
              });
                
            });
        }else{
            return "-";
        }
      }
      },
      {
        title: '成本最高价',
        dataIndex: 'maxCost',
      },
      {
        title: '成本最低价',
        dataIndex: 'minCost',
      }]
  }

  handleChange = value => {
    this.props.onList({
        current: value.current,
        size: value.pageSize,
    })
  }
  onSelectChange = (selectedRowKeys) => {
    const { onEvent } = this.props;
    onEvent('selectedRowKeys', selectedRowKeys)
    this.setState({ selectedRowKeys });
  }
  render() {
    const { loading, data } = this.props;
    const pagination = {
      page: data.current,
      total: data.total,
      defaultSize: 10
    }
    const rowSelection = {
      // selectedRowKeys,
      onChange: this.onSelectChange,
    };
    return (
        <Table rowSelection={rowSelection}  
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

export default ProductGroupInfoList;

