import React, { Fragment } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { bindActionCreators } from 'redux';
import * as actions from '../../store/product/action';
import * as productGroupActions from '../../store/productGroup/action';
import * as resourceActions from '../../store/resource/action';
import TableListBase from '../../base/table-list-base';
import { Table, Tag, Input, message,Alert,Avatar } from 'antd';
import cookie from 'react-cookies';
import './Workplace.less'
@connect(
  ({ operation, product, resource ,productGroup}) => ({
    operation,
    product: product.toJS(),
    resource: resource.toJS(),
    productGroup:productGroup.toJS(),
  }),
  dispatch => ({
    actions: bindActionCreators({ ...actions, ...resourceActions,...productGroupActions }, dispatch),

  })
)
@withRouter
class WorkPlace extends TableListBase {
  constructor(props) {
    super(props);
    this.state = {
      currentUser:{}
    };
  }
  componentDidMount() {
    let values = cookie.load("user");
    if(values){
      this.setState({
        currentUser:values
      });
    }
    // else{
    //   var path = {
    //       pathname:'/login',
    //     }
    //   this.props.router.push(path);
    // }
  }
  componentWillReceiveProps(nextProps) {
    const { operation, product, resource } = nextProps;
    switch (operation.type) {
      case resourceActions.GET_GIFTTYPELIST_SUCCESS:
        
      break;
      default:
      break;
    }
  }
  onEvent = (type, params) => {
    switch (type) {
      // 查询
      case 'search':
        
        break;
       
      default:
      break;
    }
  }
 
  render() {
    const {currentUser} = this.state;
    return (
      <Fragment>
         <div className="pageHeaderContent">
          <div className="avatar">
            <Avatar size="large"  src='./imgs/default-picture@2x.jpg'  />
          </div>
          {currentUser&& <div className="content">
            <div className="contentTitle">
              早安，
              {currentUser.loginname}
              ，祝你开心每一天！
            </div>
            <div>
               {currentUser.realname} | {currentUser.email} | {currentUser.deptname}
            </div>
          </div>}
        </div>
      </Fragment>
    );
  }
}

export default WorkPlace;