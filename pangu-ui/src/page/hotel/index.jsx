import React, { Fragment } from 'react';
import Filter from './compoent/filter';
import List from './compoent/list';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { bindActionCreators } from 'redux';
import * as actions from '../../store/resource/action';
import TableListBase from '../../base/table-list-base';

@connect(
    ({operation, resource}) => ({
        operation,
        resource: resource.toJS()
    }),
    dispatch => ({ 
        actions: bindActionCreators(actions, dispatch) 
    })
)
@withRouter
class Hotel extends TableListBase {
    constructor(props) {
        super(props);
        this.state = { 
            tableData: {},
            cityList:[],
            filterParmas: {},
            pageSize: 10,
        };
    }
    componentDidMount() {
        this.props.actions.getAllCity();
        this.onList({
            action: this.props.actions.getSelectPageList,
            params: {
                // "condition": {
                //     "cityName": "上海",
                //     "hotelName": "酒店"
                // },
                "current": 1,
                "size": 10
            }
        })
    }
    componentWillReceiveProps(nextProps) {
        const { operation, resource } = nextProps;
        switch(operation.type){
            case actions.GET_SELECTPAGELIST_SUCCESS:
                this.setState({
                    tableData: resource.hotelList.result,
                    isTableLoading: false
                })
                break;
            case actions.UPDATE_HOTELNAME_SUCCESS:
                this.onList({
                    action: this.props.actions.getSelectPageList,
                    params: {
                        "condition": this.state.params,
                        "current": 1,
                        "size": 10
                    }
                })  
                break;
            case actions.GET_GETALLCITY_SUCCESS:
            this.setState({
                cityList: resource.cityList.result
            });  
            break;    
            default:
        }
    }
    onEvent = (type, params) => {
        const { pageSize } = this.state;

        switch(type){
            case 'editor':
                this.props.router.push(`/hotelDetail/${params}`);
                break;
            // 修改酒店名
            case 'save':
                this.props.actions.updHotelName(params);
                break;
            // 查询
            case 'search':
                this.setState({
                    filterParmas: params
                }, () => {
                    this.onList({
                        action: this.props.actions.getSelectPageList,
                        params: {
                            "condition": params,
                            "current": 1,
                            "size": pageSize
                        }
                    })
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
            action: this.props.actions.getSelectPageList,
            params: {
                "condition": this.state.filterParmas,
                ...params
            }
        })
    }
    render() {
        const { tableData, isTableLoading ,cityList} = this.state;
        return (
            <Fragment>
                <Filter
                    onEvent={this.onEvent} 
                    cityList={cityList}
                />
                <List
                    data={tableData}
                    loading={isTableLoading}
                    onEvent={this.onEvent}
                    pagination={this.getPagination}
                    onList={this.getList}
                />
            </Fragment>
        );
    }
}

export default Hotel;