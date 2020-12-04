import React, { Component } from 'react';
import { Layout, Menu, Icon, message } from 'antd';
import { withRouter } from 'react-router';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as actions from '../../store/login/action';
import cookie from 'react-cookies';

const { Sider } = Layout;

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
class Sidebar extends Component {
    constructor(props) {
        super(props);
        this.state = {
            userMenus: []
        };
    }
    componentDidMount() {
        this.props.actions.getUserMenus().then((data) => {
            const { login } = this.props;
            if (login.userMenus.code == 100) {
                this.setState({
                    userMenus: login.userMenus.result
                })
            } else {
                this.props.router.push("login");
            }
        }).catch((error) => {
            this.props.router.push("login");
        })

        const userButtons = cookie.load("userButtons");
        if (!userButtons) {
            this.props.actions.getUserButtons().then((data) => {
                const { login } = this.props;
                if (login.userButtons.code == 100) {
                    const button = login.userButtons.result;
                    let opt = {
                        maxAge: 300
                    }
                    cookie.save("userButtons", true, opt);
                    button.map((item) => {
                        cookie.save(item.functioncode, true);
                    })
                } else {
                    // this.props.router.push("login");
                }
            }).catch((error) => {
                this.props.router.push("login");
            })
        }
    }
    componentWillReceiveProps(nextProps) {
        const { operation, login } = nextProps;
        switch (operation.type) {
            // case actions.GET_USERMENUS_SUCCESS:
            //     const menu = login.userMenus;
            //     if (menu.code == 100) {
            //         this.setState({
            //             userMenus: menu.result
            //         })
            //     }
            //     break;
            default:
                break;
        }
    }

    onClick = params => {
        this.props.router.push(params)
    }
    render() {
        const { userMenus } = this.state;
        return (
            <Sider width={200} style={{ background: 'dark' }} collapsible={true} collapsedWidth={0}>
                <Menu theme="dark" mode="inline" defaultSelectedKeys={['1']}>
                    {
                        userMenus.map((item, id) => {
                            return <Menu.SubMenu title={item.functionname} key={id + item.functionname}>
                                {
                                    item.sonMenus.map((param, index) => {
                                        return (<Menu.Item key={param.functionname + index} onClick={() => this.onClick(param.functioncode)}>
                                            <Icon type="user" key={"icon" + index} />
                                            <span>{param.functionname}</span>
                                        </Menu.Item>);
                                    })
                                }
                            </Menu.SubMenu>
                        })
                    }
                    {/* <Menu.Item key="refund" onClick={() => this.onClick('/KLF_PG_REFUND_LIST')}>
                    <Icon type="file-sync" />
                    <span>线下退款</span>
                    </Menu.Item> */}
                    {/* <Menu.SubMenu title="资源管理">*/}
                    {/* <Menu.Item key="1" onClick={() => this.onClick('/hotel')}> */}
                    {/* <Icon type="user" /> */}
                    {/* <span>酒店列表</span> */}
                    {/* </Menu.Item> */}
                    {/*<Menu.Item key="2" onClick={() =>this.onClick('/merchant')}>*/}
                    {/*<Icon type="video-camera" />*/}
                    {/*<span>商户列表</span>*/}
                    {/*</Menu.Item>*/}
                    {/*<Menu.Item key="5" onClick={() =>this.onClick('/product')}>*/}
                    {/*<Icon type="upload" />*/}
                    {/*<span>产品列表</span>*/}
                    {/*</Menu.Item>*/}
                    {/*</Menu.SubMenu>*/}
                    {/*<Menu.SubMenu title="商品管理">*/}
                    {/*<Menu.Item key="4" onClick={() =>this.onClick('/goods')}>*/}
                    {/*<Icon type="upload" />*/}
                    {/*<span>商品列表</span>*/}
                    {/*</Menu.Item>*/}
                    {/**/}
                    {/*<Menu.Item key="6" onClick={() =>this.onClick('/productGroup')}>*/}
                    {/*<Icon type="upload" />*/}
                    {/*<span>产品组列表</span>*/}
                    {/*</Menu.Item>*/}
                    {/*<Menu.Item key="7" onClick={() =>this.onClick('/channel')}>*/}
                    {/*<Icon type="upload" />*/}
                    {/*<span>渠道列表</span>*/}
                    {/*</Menu.Item>*/}
                    {/*</Menu.SubMenu> */}
                </Menu>
            </Sider>
        );
    }
}

export default Sidebar;