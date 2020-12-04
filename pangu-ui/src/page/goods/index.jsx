import React, { Fragment } from 'react';
import GoodsFilter from './component/filter';
import GoodsList from './component/list';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { bindActionCreators } from 'redux';
import * as actions from '../../store/goods/action';
import TableListBase from '../../base/table-list-base';
import locale from 'antd/lib/locale-provider/zh_CN';
import GoodsEditModal from './component/edit-modal'
import { message } from 'antd';

@connect(
    ({operation, goods}) => ({
        operation,
        goods: goods.toJS()
    }),
    dispatch => ({ 
        actions: bindActionCreators(actions, dispatch) 
    })
)
@withRouter
class Goods extends TableListBase {
    constructor(props) {
        super(props); 
        this.state = { 
            tableData: {},
            filterParmas: {},
            authTypeList: [],
            channelList: [],
            isShowEditModal:false,
            pageSize: 10,
        };
    }
    componentDidMount() {
        this.props.actions.authTypeList();
        this.props.actions.channelSelectAll();
        this.onList({
            action: this.props.actions.selectGoodsPageList,
            params: {
               
                "current": 1,
                "size": 10
            }
        })
        
   
    }
    componentWillReceiveProps(nextProps) {
        const { operation, goods } = nextProps;
        const { tableData} = this.state;
        switch(operation.type){
            case actions.GET_SELECTGOODSPAGELIST_SUCCESS:
                this.setState({
                    tableData: goods.goodsList.result,
                    isTableLoading: false
                })
                break;
            case actions.GET_AUTHTYPELIST_SUCCESS:  
                this.setState({
                    authTypeList: goods.authTypeList.result
                    });
                break;    
            case actions.GET_CHANNELSELECTALL_SUCCESS:  
                this.setState({
                    channelList: goods.channelList.result
                    });
                break;   
            case actions.GET_GOODSUPDATESTATUS_SUCCESS:
                const param = JSON.parse(operation.result.config.data);
                // const { tableData} = this.state;
                tableData.records.map(item => {
                    if(item.id === param.id){
                        item.status = param.status;
                    }
                });
                this.setState({
                    tableData: tableData
                });
                break;  
            case actions.GET_COPYGOODSBYID_SUCCESS:
            
                this.onList({
                    action: this.props.actions.selectGoodsPageList,
                    params: {
                        "condition": this.state.params,
                        "current": 1,
                        "size": 10
                    }
                })  
                break; 
            case actions.GET_UPDATEGOODSSTATUS_SUCCESS:
               
                const params = goods.updateGoodsStatus.result;
                
                tableData.records.map(item=>{
                    if(item.id === params.id){
                        item.status = params.status;
                        item.upType = params.upType;
                        item.upstartTime=params.upstartTime;
                        item.downEndTime=params.downEndTime;
                    } 
                    return item;
                });
                this.setState({
                    tableData: tableData,
                    isShowEditModal: false,
                });
                message.success("修改商品状态成功",10);
            break;

            default:
        }
    }
    onEvent = (type, params) => {
        const {pageSize} = this.state;
        switch(type){
            case 'editor':
            var path = {
                pathname:'/goodsDetails',
                query:{id:params},
            }
                this.props.router.push(path);
                break;
            //新增商品
            case 'addGoods':
                this.props.router.push('/goodsDetails');
            break;        
            // 查询
            case 'search':
                this.setState({
                    filterParmas: params
                }, () => {
                    this.onList({
                        action: this.props.actions.selectGoodsPageList,
                        params: {
                            "condition": params,
                            "current": 1,
                            "size": pageSize
                        }
                    })
                })
                break;
            case 'updateGoodsStatus':
            this.setState({
                filteisShowEditModal:false
            }, () => {
                this.props.actions.updateGoodsStatus(params)
            })
            break;
            default:
        }
    }
    // 切换分页
    getList = params => {
        this.setState({
            pageSize: params.size,
        })
        this.onList({
            action: this.props.actions.selectGoodsPageList,
            params: {
                "condition": this.state.filterParmas,
                ...params
            }
        })
    }
    changeGoodsStates = params=>{
        // alert("updateStatus");
        this.props.actions.goodsUpdateStatus(params)
    }
    copyGoods= params =>{
        this.props.actions.copyGoodsById(params);
        // this.props.router.push(`/selectById/${params.goodsId}`);
    }
    openGoodsEditModel = item => {
        this.setState({
            isShowEditModal: true,
            tempRecord:item
        })
    }
    closeGoodsEditModel = item => {
        this.setState({
            isShowEditModal: false,
            tempRecord:{}
        })
    }
    render() {
        const { tableData, isTableLoading,authTypeList,channelList ,isShowEditModal,tempRecord} = this.state;
        return (
            <Fragment>
                <GoodsFilter
                    authTypeList={authTypeList}
                    channelList ={channelList}
                    onEvent={this.onEvent}
                />
                <GoodsList
                    data={tableData}
                    loading={isTableLoading}
                    onEvent={this.onEvent}
                    onChangeGoodsStates ={this.changeGoodsStates}
                    onCopyGoods = {this.copyGoods}
                    pagination={this.getPagination}
                    openGoodsEditModel={this.openGoodsEditModel}
                    closeGoodsEditModel={this.closeGoodsEditModel}
                    onList={this.getList}

                    locale ={locale}
                />
                 {isShowEditModal && <GoodsEditModal  onEvent={this.onEvent} tempRecord ={tempRecord} closeGoodsEditModel={this.closeGoodsEditModel} ></GoodsEditModal>}
            </Fragment>
           
        );
    }
}

export default Goods;