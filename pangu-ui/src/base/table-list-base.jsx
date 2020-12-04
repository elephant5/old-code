import React, { Component } from 'react';
import _ from 'lodash';

class ListBase extends Component {
    constructor(props) {
        super(props);
        this.state = { 
            
        };
    }
    /**
     * 获取列表数据
     * action [func]  请求列表action
     * params [obj]   请求参数
     */
    onList = ({action, params}) => {
        if(_.isFunction(action)){
            // 每次请求加上loading
            this.setState({
                isTableLoading: true
            })
            action(params);
        }
    }
    /**
     * 分页配置
     * page [int]   当前页
     * total [int]  列表总数
     */
    getPagination = ({page, total, defaultSize = 10}) => {
        return {
            current: page,
            total: total,
            defaultPageSize: defaultSize,
            showSizeChanger: true,
            showQuickJumper: true,
            pageSizeOptions: [10,20,50,100,200,500,1000],
            showTotal: (total, range) => <div>本页显示 <span className="c-color-blue">{range[1]-range[0] + 1}</span> 条数据, 总共 <span className="c-color-blue">{total}</span> 条数据</div>,
        }
    }
}

export default ListBase;