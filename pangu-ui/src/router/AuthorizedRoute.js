import React from 'react';
import {Route,Redirect,withRouter} from 'react-router-dom';
import PropTypes from 'prop-types';
import cookie from 'react-cookies';
//私有路由，只有登录的用户才能访问
class AuthorizedRoute extends React.Component{
    componentWillMount(){
        let  isAuthenticated = cookie.load("user") ? true :false;
        this.setState({isAuthenticated:isAuthenticated})
        // if(!isAuthenticated){
        //   const {history} = this.props;
        //   setTimeout(() => {
        //     history.replace("/login");
        //   }, 1000)
        // }
    }
    render(){
        let { component: Component,path="/",exact=false,strict=false} = this.props;
        return this.state.isAuthenticated ?  (
            <Route  path={path} exact={exact}  strict={strict}  render={(props)=>( <Component {...props} /> )} />
        ) : (<Redirect to='/login'/>);
    }
}
AuthorizedRoute.propTypes  ={
        path:PropTypes.string.isRequired,
        exact:PropTypes.bool,
        strict:PropTypes.bool,
        component:PropTypes.func.isRequired
}
// export default withRouter(AuthorizedRoute);
export default AuthorizedRoute;
