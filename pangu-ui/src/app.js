import React, { Component } from 'react';
import { connect } from 'react-redux';
// import AppLoading from './component/page-loading';
import 'antd/dist/antd.less';
import './assets/global.less'

@connect(
    ({operation}) => ({operation})
)

class App extends Component {
    constructor(props) {
        super(props);
        this.state = {  };
    }
    render() {
        //无权限时加载loading
        // if(this.props.operation.loading){
        //     return <AppLoading/>
        // }
        return (
            <div className="app">
                {this.props.children}
            </div>
        );
    }
}

export default App;
