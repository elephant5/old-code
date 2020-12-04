/*
 * @Author: rocky 
 * @since: 2019-03-12 14:51:56 
 */

import _ from 'lodash';
import { Component } from 'react';

class componentBase extends Component {
    /**
     * 是否显示viewType
     */
    isShowView = viewType => {
        return this.state.viewType === viewType && this.state.viewData != null;
    }
    /**
     * 显示弹框
     * viewType：[string] view名字
     * viewData：[obj]  view数据
     */
    showView = (viewType, viewData) => {
        this.setState({
            viewType,
            viewData
        })
    }
    // 重置弹框
    reset = callback => {
        this.setState({
            viewType: 'list',
            viewData: null
        }, () => {
            if (_.isFunction(callback)) {
                callback();
            }
        });
    }
}

export default componentBase;