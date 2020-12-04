import React, { Component } from 'react';
import { Spin } from 'antd';
import './index.less';

class AppLoading extends Component {
    render() {
        return (
            <div className="c-page-loading">
                <div className="load">
                    <Spin size="large" tip="页面载入中..." />
                </div>
            </div>
        );
    }
}

// class Loading extends Component {
//     constructor(props) {
//         super(props);
//         this.state = {  };
//     }
//     render() {
//         return (
//             <div>s</div>
//         );
//     }
// }


export default AppLoading;