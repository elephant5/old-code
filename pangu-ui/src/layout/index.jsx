import React, { Component } from 'react';
import { Layout, Breadcrumb } from 'antd';
import Header from '../component/header/index';
import Sidebar from '../component/sidebar/index';
import Footer from '../component/footer/index';
import './index.less';

const { Content } = Layout;

class Dashboard extends Component {
    constructor(props) {
        super(props);
        this.state = {  };
    }
    render() {
        return (
            <Layout className="layout-box">
                <Header/>
                <Layout>
                    <Sidebar/>
                    <Layout>
                        <Breadcrumb style={{ margin: '16px 0' }} routes={this.props.routes}>
                        </Breadcrumb>
                        <Content style={{
                                background: '#F0F2F5', padding: 24, margin: 0, minHeight: 280,
                                overflowY: 'auto'
                            }}
                        >
                            {this.props.children}
                        </Content>
                        <Footer/>
                    </Layout>
                </Layout>
            </Layout>
        );
    }
}

export default Dashboard;
