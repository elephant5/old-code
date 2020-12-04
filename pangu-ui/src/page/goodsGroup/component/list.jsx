import React, { Fragment, Component } from 'react';
import { Table, Divider, Input } from 'antd';
import { resourColor , serviceColor,giftColor} from '../../../util/dictType.js'
class GoodsGroupList extends Component {
  constructor(props){
    super(props);
    this.state ={
        selectedRowKeys: [],
        loading: false,
    }
  }
  
  

  getColumns = () => {
    return [{
      title: '产品ID',
      dataIndex: 'productId',
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
    },
    {
      title: '权益类型',
      dataIndex: 'gift',
    },
    {
      title: '资源',
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
        title: '最高价',
        dataIndex: 'cost',
      },
      {
        title: '最低价',
        dataIndex: 'status',
      },  
      {
        title: 'Block',
        dataIndex: 'blockNatural',
      },
    {
      title: '点数',
      dataIndex: 'pointOrTimes',
    },
  
    {
      title: '排序',
      dataIndex: 'sort',
    }]
}
onSelectChange = (selectedRowKeys) => {
    this.setState({ selectedRowKeys });
  }

render() {
  const { itemProduct,key,cityList,product,goodsId,loading} = this.props;
  const {selectedRowKeys }  = this.state;
  
  const hasSelected = selectedRowKeys.length > 0;
  return (
    <Table 
    key ={"table"+key}
        columns={this.getColumns()} 
        dataSource={itemProduct.groupProductList} 
        // pagination={this.props.pagination(pagination)}
        rowKey="id"
        loading={loading}
        onChange={this.handleChange}
    />
    );
}
}



export default GoodsGroupList;

