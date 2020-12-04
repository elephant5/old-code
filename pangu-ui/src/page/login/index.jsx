import React, { Fragment, Component } from 'react';
import {
  Form, Icon, Input, Button, Checkbox, message, Card, Layout
} from 'antd';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { bindActionCreators } from 'redux';
import './index.less';
import * as actions from '../../store/login/action';
import cookie from 'react-cookies';
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
@Form.create()
class Login extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isChecked: true,
      user: {}
    };
  }
  componentDidMount() {
  }
  componentWillReceiveProps(nextProps) {
    const { operation, login } = nextProps;
    switch (operation.type) {
      default:
    }
  }
  onEvent = (type, params) => {
    switch (type) {
      default:
    }
  }
  handleSubmit = (e) => {
    e.preventDefault();
    this.props.form.validateFields((err, values) => {
      if (!err) {
        this.setState({ isChecked: true });
        this.props.actions.checkLogin(values).then((data) => {
          const { login } = this.props;
          const res = login.user;

          //登录前前清除cookie
          let allCookie = document.cookie.match(/[^ =;]+(?=\=)/g);
          if (allCookie) {
            for (var i = 0; i < allCookie.length; i++) {
              cookie.remove(allCookie[i])
            }
          }

          if (res.code != 100 && this.state.isChecked) {
            message.error(res.msg);
            this.setState({ isChecked: false });
          } else {
            if (this.state.isChecked) {
              cookie.save("user", login.user.result);
              var path = {
                pathname: '/workPlace',
              }
              this.props.router.push(path);
            }
          }
        }).catch((error) => {
          message.error("系统出错")
        })
      }
    });
  }

  render() {
    const { getFieldDecorator } = this.props.form;
    return (
      <Fragment>
        <div className={"loginbody"}>
          <div className="main">
            <div className="left">
              <img alt="" src='./imgs/pg_bg.jpg' />
            </div>
            <div className="right">
              {/* <h1><img alt="" src='./imgs/logo.png' width="60" /> 盘古系统</h1> */}
              <h2>用户登录</h2>
              <Card key="account" tab="账户密码登录" style={{ width: '100%'}} bordered={false}>
                <Form onSubmit={this.handleSubmit} className={"login-form"}>
                  <Form.Item>
                    {getFieldDecorator('userName', {
                      rules: [{ required: true, message: '请输入用户名!' }],
                    })(
                      <Input prefix={<Icon type="user" style={{ color: 'rgba(0,0,0,.25)' }} />} placeholder="请输入用户名" usage="true" size="large" />
                    )}
                  </Form.Item>
                  <Form.Item>
                    {getFieldDecorator('password', {
                      rules: [{ required: true, message: '请输入密码!' }],
                    })(
                      <Input prefix={<Icon type="lock" style={{ color: 'rgba(0,0,0,.25)' }} />} type="password" placeholder="请输入密码" usage="true" size="large" />
                    )}
                  </Form.Item>
                  <Form.Item>
                    {/*{getFieldDecorator('remember', {*/}
                    {/*valuePropName: 'checked',*/}
                    {/*initialValue: true,*/}
                    {/*})(*/}
                    {/*<Checkbox>Remember me</Checkbox>*/}
                    {/*)}*/}
                    {/*<a className="login-form-forgot" href="">Forgot password</a>*/}
                    <Button type="primary" htmlType="submit" className="login-form-button" size="large" >
                      登录
                            </Button>
                    {/*Or <a href="">register now!</a>*/}
                  </Form.Item>
                </Form>
                <div className={"other"}>
                  注册请联系管理员
                </div>
              </Card>
            </div>
            <div className={"footer-center"}>
            <p>建议使用<a href={'http://www.firefox.com.cn'}>FireFox</a>，<a href={'https://www.google.cn/chrome/'}>Chrome</a>浏览器访问</p>
            <p>Copyright<Icon type="copyright" />2019 The Colourful China All Rights Reserved    沪ICP备13046660号-2</p>
          </div>
          </div>
          
        </div>
      </Fragment>
    );
  }
}

export default Login;