import React, { Fragment, Component } from 'react';
import { Table, Divider, Input } from 'antd';
import {  Link } from 'react-router';

import cookie from 'react-cookies';

class List extends Component {
  constructor(props){
    super(props);
    this.state = {
      // 可编辑行对应的key
      editingKey: '4',
      nameCh: '',
      nameEn: ''
    }
  }
  // 修改酒店名
  changeHotelName = params => {
    this.setState({
      editingKey: params.id,
      nameCh: params.nameCh,
      nameEn: params.nameEn
    })
  }
  // 中文名修改
  changeNameCh = e => {
    this.setState({
      nameCh: e.target.value
    })
  }
  // 英文名修改
  changeNameEn = e => {
    this.setState({
      nameEn: e.target.value
    })
  }
  // 保存
  onSave = id => {
    const { nameCh, nameEn } = this.state;
    this.props.onEvent('save', { 
      hotelNameCh: nameCh, 
      hotelNameEn: nameEn,
      id 
    });
    this.setState({
      editingKey: ''
    })
  }
  getColumns = () => {
      const { onEvent } = this.props;
      const view = cookie.load("KLF_PG_RM_HL_VIEW");
      const modify = cookie.load("KLF_PG_RM_HL_MODIFY");
      return [{
        title: '酒店ID',
        dataIndex: 'id',
        // filterMultiple: false,
        
        // filters: [{
        //   text: '1',
        //   value: '1',
        // }, {
        //   text: '2',
        //   value: '2',
        // }],
        // onFilter: (value, record) => record.id.indexOf(value) === 0,
      }, {
        title: '城市',
        dataIndex: 'cityNameCh',
      }, {
        title: '酒店名称',
        dataIndex: 'nameCh',
        render: (text, record) => {
          return record.id === this.state.editingKey ? <Input onChange={this.changeNameCh} value={this.state.nameCh}/> 
                                                     : <div>{text}</div>
        }
      },
       {
        title: '酒店英文名称',
        dataIndex: 'nameEn',
        render: (text, record) => {
          return record.id === this.state.editingKey ? <Input onChange={this.changeNameEn} value={this.state.nameEn}/> 
                                                     : <div>{text}</div>
        }
      },
      {
        title: '操作',
        key: 'action',
        render: (text, record) => {
          return (
            <Fragment>
              {
                this.state.editingKey === record.id ? 
                  <span>
                    <span className="c-color-blue" onClick={() => this.onSave(record.id)}>保存</span>
                    <Divider type="vertical" />
                    <span className="c-color-blue" onClick={() => this.setState({editingKey: ''})}>取消</span>
                  </span>
                        :
                <span>
                  {view &&<Link to={{ pathname: "/hotelDetail/"+record.id,}} target="_blank" className="fontBlue fontWeight" activeStyle={{ color: 'red' }}>
                    编辑</Link>
                  // <span className="c-color-blue" onClick={() => onEvent('editor', record.id)}>编辑</span>
                  }
                  <Divider type="vertical" />
                  {modify && <span className="c-color-blue" onClick={() => this.changeHotelName(record)}>修改酒店名</span>}
                </span>
              }
            </Fragment>
          )
        },
      }]
  }

  handleChange = value => {
    this.props.onList({
        current: value.current,
        size: value.pageSize,
    })
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

