import React, { Fragment, Component } from 'react';
import { Table, Divider, Input ,Tag} from 'antd';
import { resourColor , serviceColor,giftColor} from '../../../util/dictType.js'
class ProductInfoList extends Component {
  constructor(props){
    super(props);
  }
  getColumns = () => {
      return [{
        title: '产品ID',
        dataIndex: 'id',
      }, {
        title: '城市',
        dataIndex: 'cityName',
      }, {
        title: '酒店|商户',
        render: (text, row, index) => {
          return (<span>{row.hotelName} | {row.shopName}</span>)
        },
      },
       {
        title: '资源类型',
        dataIndex: 'service',
        render:(text,record) =>{
          // let num=Math.floor(Math.random()*11);
          // return (<Tag color={doc[num]} key={text} >{text}</Tag>);
          if(text){
            return serviceColor.map(item => {
                if(item.name === text){
                    return <Tag color={item.color} key={text} >{text}</Tag>;
                }
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
            return giftColor.map(item => {
                if(item.name === text){
                    return <Tag color={item.color} key={text} >{text}</Tag>;
                }
            });
        }else{
            return "-";
        }
      }
      },
      {
        title: '资源名称',
        dataIndex: 'productName',
        render: (text,row,index) =>{
          // console.log("productName")
          // console.log(text)
          // console.log(row.productName)
          // console.log(row.serviceCode)
          // console.log(row.needs)
          // console.log(row.addon)
          let productName = row.productName;
          if(row.serviceCode === 'accom'){
            if(row.needs){
              productName = productName + ' | ' + row.needs
            }
            if(row.addon){
              productName = productName + ' | ' + row.addon
            }
          }
          return productName;
        }
      },
      {
        title: '最低成本',
        dataIndex: 'minCost',
      },
      {
        title: '最高成本',
        dataIndex: 'maxCost',
      },
      {
        title: '状态',
        dataIndex: 'status',
        render: (text,row,index) => {
          var status = row.status;
          if(status == 0){
            status = '在售';
          }
          if(status == 1){
            status = '停售';
          }
          return status;
        },
      },
      {
        title: '渠道类型',
        dataIndex: 'channelType',
      }]
  }

  handleChange = value => {
    this.props.onList({
      "condition": this.props.paramsLocal,
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
      defaultSize: 10,
    }
    const rowSelection = {
      
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

export default ProductInfoList;

