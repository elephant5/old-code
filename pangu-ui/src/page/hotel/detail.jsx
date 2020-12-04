import React, { Fragment } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as actions from '../../store/resource/action';
import * as commonActions from '../../store/common/action';
import BasicInfo from './compoent/basicInfo';
import Explore from './compoent/explore';
import Merchant from './compoent/merchant';
import Log from './compoent/log';
import _ from 'lodash';
import TableListBase from '../../base/table-list-base';
import { getFileList } from '../../util/index';
import { message } from 'antd';

@connect(
    ({operation, resource, common}) => ({
        operation,
        resource: resource.toJS(),
        common: common.toJS()
    }),
    dispatch => ({ 
        actions: bindActionCreators({...actions, ...commonActions}, dispatch) 
    })
)
class Detail extends TableListBase {
    constructor(props) {
        super(props);
        this.state = { 
            hotelDetail: {
                hotel: {},
                hotelPortalVoList: []
            },
            //酒店关联商户
            hotelsShop: [],
            // 操作日志
            operationLog: {
                records: []
            },
            // 国家城市
            countryCity: [],
            // 酒店探索
            hoterPortal: {},
            // 图片默认展示
            uploadedFile: []
        };
    }

    componentDidMount() {
        const id = this.props.routeParams.id;
        this.onList({
            action: this.props.actions.getHotelDetail,
            params: id
        })
        this.props.actions.getHotelsShop(id);
        // 操作日志
        this.props.actions.getOperationLog(id);
        // 国家城市列表
        this.props.actions.getCountryCity();
        //默认图片展示
        this.props.actions.getUploadedFile({objId: id, type: 'hotel.file'});
    }

    
    componentWillReceiveProps(nextProps) {
        const { operation, resource, common } = nextProps;
        const id = this.props.routeParams.id;
        switch(operation.type){
            case actions.GET_HOTELDETAIL_SUCCESS: 
                this.setState({
                    hotelDetail: resource.hotelDetail.result,
                    isTableLoading: false
                })
                break;
            case actions.GET_HOTELSSHOP_SUCCESS: 
                this.setState({
                    hotelsShop: resource.hotelsShop.result
                })
                break;
            case actions.GET_OPERATIONLOG_SUCCESS: 
                this.setState({
                    operationLog: resource.operationLog.result
                })
                break;
            // 国家城市列表
            case actions.GET_COUNTRYCITY_SUCCESS: 
                this.setState({
                    countryCity: resource.countryCity.result
                })
                break;
            // 酒店探索章节删除
            case actions.DELETE_HOTELPORTAL_SUCCESS:
                this.onList({
                    action: this.props.actions.getHotelDetail,
                    params: id
                })
                break; 
                // 酒店探索添加
            case actions.UPDATE_HOTELPORTAL_SUCCESS:
                if(resource.updateHotelPortal.code == 100){
                    this.setState({
                        hoterPortal: resource.updateHotelPortal.result
                    }, () => {
                        const params = this.state.fileList.map(item => {
                            item.objId = this.state.hoterPortal.id;
                            return item;
                        })
                        this.props.actions.updateFile(params);
                    })
                }else{
                    message.error(resource.updateHotelPortal.msg);
                }
                
                break;
                // 基本信息修改
            case actions.UPDATE_HOTELBASEMSG_SUCCESS:
                if(resource.hotelBaseMsg.code == 100){
                    message.success("酒店信息修改成功！");
                    this.onList({
                        action: this.props.actions.getHotelDetail,
                        params: id
                    });
                }else{
                    message.error(resource.hotelBaseMsg.msg);
                }
                
             
            break;
            case actions.UPDATE_FILE_SUCCESS:
                this.onList({
                    action: this.props.actions.getHotelDetail,
                    params: id
                })
                this.props.actions.getUploadedFile({objId: id, type: 'hotel.file'});
                break;
            case commonActions.GET_UPLOADEDFILE_SUCCESS:
                this.setState({
                    uploadedFile: common.uploadedFile
                })
                break;
            default:
        }
    }

    //事件回传
    onEvent = (type, params) => {
        const { deleteHotelPortal, updateHotelPortal, updateHotelBaseMsg } = this.props.actions;
        switch(type) {
            // 酒店探索章节增加/修改
            case 'edit':
                const {fileList, ...other} = params;
                // 图片上传list
                this.setState({
                    fileList: fileList
                }, () => {
                    updateHotelPortal(other);
                })
                break;
            // 酒店探索章节删除
            case 'delete':
                deleteHotelPortal(params);
                break;
            // 酒店基本信息修改接口
            case 'saveBaseInfo':
                updateHotelBaseMsg(params)
                break;
            case 'selectShopById':
            this.props.router.push(`/addtwo/${params}`);
            break;    
            default:
                break;
        }
    }

    render() {
        const { hotelDetail, hotelsShop, operationLog, uploadedFile,
            countryCity, isTableLoading } = this.state;
        const baseLoading = this.props.operation.loading[actions.UPDATE_HOTELBASEMSG];
        return (
            <Fragment>
                {
                    !_.isEmpty(hotelDetail.hotel) && !_.isEmpty(countryCity) && 
                    <BasicInfo
                        data={{hotel: hotelDetail.hotel, countryCity}}
                        onEvent={this.onEvent}
                        loading={baseLoading}
                    />
                }
                <Merchant
                    data={hotelsShop}
                    onEvent={this.onEvent}
                />
                <Explore
                    data={hotelDetail.hotelPortalVoList}
                    id={this.props.routeParams.id}
                    onEvent={this.onEvent}
                    loading={isTableLoading}
                    uploadedFile={getFileList(uploadedFile)}
                />
                <Log
                    data={operationLog.records}
                />
            </Fragment>
        );
    }
}

export default Detail;