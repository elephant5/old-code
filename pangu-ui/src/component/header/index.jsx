import React, { Component } from 'react';
import { Layout, Menu, Icon, Button, message } from 'antd';
import './index.less'
import SubMenu from 'antd/lib/menu/SubMenu';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import cookie from 'react-cookies';
import * as actions from '../../store/login/action';
import { loginOut } from '../../api/login';
import { withRouter } from 'react-router';

const { Header } = Layout;
@connect(
    ({ operation, login }) => ({
        operation,
        login: login.toJS()
    }),
    dispatch => ({
        actions: bindActionCreators(actions, dispatch),
    })
)
@withRouter
class Head extends Component {
    constructor(props) {
        super(props);
        this.state = {};
    }
    reBackWorkPlace = () => {

    }

    //退出登录
    loginOut = e => {
        this.props.actions.loginOut().then((data)=>{
            const {login} = this.props;
            if(login.loginOut.code == 100){
                //清除所有cookie
                let allCookie = document.cookie.match(/[^ =;]+(?=\=)/g);
                if(allCookie){
                    for(var i = 0;i<allCookie.length;i++){
                        cookie.remove(allCookie[i])
                    }
                }
                // cookie.remove("user");
                // cookie.remove("userButtons")
                this.props.router.push("login")
            }
        }).catch((error)=>{
            message.error("系统错误")
        })
    }

    render() {
        const user = cookie.load("user");
        return (
            <Header className="header">
                <div className="logo" key="logo">
                    <img src="./imgs/colourful-logo.png" alt="logo" />
                    <h1 className="textLogo" onClick={this.reBackWorkPlace}>盘古系统</h1>
                </div>
                <div style={{ display: "inline-block", float: "right" }}>
                    <Menu mode="horizontal" onClick={this.handleClickMenu} style={{ background: '#001529', color: '#FFF', lineHeight: '62px' }}>
                        <SubMenu
                            style={{ float: 'right' }}
                            title={
                                <span>
                                    <Icon type="user" />
                                    你好，{user.loginname}
                                </span>
                            }
                        >
                            <Menu.Item key="logout" onClick={this.loginOut}>
                                退出登录
                            </Menu.Item>
                        </SubMenu>
                    </Menu>
                </div>
                {/* <Menu
                    theme="dark"
                    mode="horizontal"
                    defaultSelectedKeys={['2']}
                    style={{ lineHeight: '64px' }}
                >
                    <Menu.Item key="1">
                        <Icon type="clock-circle" />
                        <span>盘古系统</span>
                    </Menu.Item>
                </Menu> */}
            </Header>
        );
    }
}

export default Head;