import React, { Component } from 'react'
import { REFUND_URL } from '../../util/url'
import { Spin } from 'antd';
import './compoent/index.less'

class Refund extends Component {
    constructor(props) {
        super(props);
        this.state = {
            refundUrl:'',
            keyCode:'',
            loading:true
        };
    }

    componentDidMount(){
        this.getmd5key();
    }
    
    getmd5key = () =>{
        const finnalKey = 'pangu';
        const keyCode = Math.random().toString(36).substr(2);
        const key = this.$md5(finnalKey + keyCode);
        this.state.refundUrl = REFUND_URL + '?_key=' + key + '&_key_Code=' + keyCode;
    }
    isShow = () =>{
        this.setState({loading:false})
    }
    render() {
        const {refundUrl} = this.state;
        return (
                <div style={{ width: '100%',height: '100%'}}>
                    <Spin spinning={this.state.loading} size="large" style={{width:'100%', height:'100%'}}>
                        <iframe
                        onLoad={this.isShow}
                        style={{ width: '100%',height: '100%',border: 'none'}}
                        src={refundUrl}
                        >
                        </iframe>
                    </Spin>
                </div>
        )
    }
}

export default Refund
