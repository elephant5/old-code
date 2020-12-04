import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { bindActionCreators } from 'redux';
import { message, Tabs, Button } from 'antd';
import GoodsDetail from './component/detail'
import Pic from './component/pic'
import Portal from './component/portal'
import GoodsClause from './component/clause'
import GoodsPrice from './component/price'
import GoodsGroup from '../goodsGroup/index'
import * as actions from '../../store/goods/action';
import * as resourceActions from '../../store/resource/action';
import * as logoActions from '../../store/logo/action';
import BasicInfo from './component/basicInfo';
import { USE_LIMIT_TYPE } from '../../util/dictType'
import { getFileList } from '../../util/index';
import _ from 'lodash';
const TabPane = Tabs.TabPane;
@connect(
    ({ operation, goods, logo, resource }) => ({
        operation,
        goods: goods.toJS(),
        logo: logo.toJS(),
        resource: resource.toJS()
    }),
    dispatch => ({
        actions: bindActionCreators({ ...actions, ...logoActions, ...resourceActions }, dispatch)
    })
)
@withRouter
class GoodsDeatils extends Component {
    constructor(props) {
        super(props);
        this.state = {
            // 渠道
            channelList: [],
            // 验证方式
            authTypeList: [],
            goodsInfo: {},
            festivalList: [],
            TabDisabled: true,
            giftTypeList: [],
            serviceTypeList: [],
            shopTypeList: [],
            sysDict: [],
            isEdit: false,
            // 已上传的文件列表
            uploadedFile: [],
            goodsPortalSetting: {},
            bankLogoList: {},
        };
    }
    componentDidMount() {
        this.props.actions.getGiftTypeAll();
        this.props.actions.getServiceTypeAll();
        this.props.actions.getShopType({});
        this.props.actions.authTypeList();
        this.props.actions.channelSelectAll();
        var data = this.props.location.query;
        var { id } = data;
        if (!!id) {
            //根据id查找渠道信息
            this.props.actions.selectById(data);
            this.props.actions.selectGoodsPortalSetting(data);
        }
        this.props.actions.getFestivalList();
        this.props.actions.getAllSysDict({ type: USE_LIMIT_TYPE });
        this.props.actions.selectBankLogoList();
    }

    componentWillReceiveProps(nextProps) {
        const { operation, goods, logo, resource } = nextProps;

        switch (operation.type) {
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
            case actions.GET_SELECTBYID_SUCCESS:
                this.setState({
                    goodsInfo: goods.goodsInfo.result,
                    TabDisabled: false,
                    isEdit: true,
                    uploadedFile: goods.goodsInfo.result.fileDtoList,
                }, () => {
                    // 商户图片默认展示
                    // this.getUploadedFile(goods.goodsInfo.result.shop.id, 'shop.pic')
                });

                break;
            case actions.GET_GOODSINSERT_SUCCESS:
                message.success('商品信息保存成功！', 5);
                this.setState({
                    goodsInfo: goods.goodsInfo.result,
                    TabDisabled: false,
                    isEdit: true
                });
                break;
            case actions.GET_GOODSUPDATE_SUCCESS:

                if (goods.goodsInfoUpdate.code === 100) {
                    message.success('商品信息修改成功！', 5);
                    this.setState({
                        goodsInfo: goods.goodsInfoUpdate.result,
                        TabDisabled: false
                    });
                } else {
                    message.success('商品信息修改失败！：' + goods.goodsInfoUpdate.msg, 5);
                }

                break;
            case actions.GET_GOODSUPDATESTATUS_FAILURE:
                message.success('商品信息修改失败！：' + goods.goodsInfoUpdate.msg, 5);
                break;
            case actions.GET_FESTIVALLIST_SUCCESS:
                this.setState({
                    festivalList: goods.festivalList.result
                })
                break;
            case actions.GET_SERVICETYPE_SUCCESS:
                this.setState({
                    serviceTypeList: goods.serviceTypeAll.result
                });
                break;
            case actions.GET_GIFTTYPE_SUCCESS:
                this.setState({
                    giftTypeList: goods.giftTypeAll.result
                });
                break;
            case resourceActions.GET_SHOPTYPE_SUCCESS:
                this.setState({
                    shopTypeList: resource.shopType.result
                });
                break;
            case actions.GET_GETALLSYSDICT_SUCCESS:
                this.setState({
                    sysDict: goods.sysDict.result
                });
                break;
            case actions.GET_PRODUCTGROUP_SUCCESS:
                message.success('产品组新增成功！', 5);
                this.setState({
                    addDiv: true
                });
                break;
            case actions.GET_GETSALESCHANNEL_SUCCESS:
                const { goodsInfo } = this.state;
                if (goods.salesChannel.result) {
                    let temp = goods.salesChannel.result;
                    goodsInfo.commission = temp.commision ? temp.commision * 100 : null;
                    this.setState({
                        goodsInfo: goodsInfo
                    });
                }
                break;
            case actions.UPDATE_FILE_SUCCESS:
                message.success('图片保存成功！', 5);
                break;
            case actions.SELECT_GOODS_PORTAL_SETTING_SUCCESS:
                if (goods.goodsPortalSetting && goods.goodsPortalSetting.code == 100) {
                    this.setState({
                        goodsPortalSetting: goods.goodsPortalSetting.result,
                    })
                }
                break;
            case actions.INSERT_GOODS_PORTAL_SETTING_SUCCESS:
                if (goods.goodsPortalSetting && goods.goodsPortalSetting.code == 100) {
                    this.setState({
                        goodsPortalSetting: goods.goodsPortalSetting.result,
                    })
                } else {
                    message.error('保存失败')
                }
                break;
            case actions.INSERT_GOODS_PORTAL_SETTING_FAILURE:
                message.error('保存失败')
                break;
            case actions.UPDATE_GOODS_PORTAL_SETTING_SUCCESS:
                if (goods.goodsPortalSetting && goods.goodsPortalSetting.code == 100) {
                    this.setState({
                        goodsPortalSetting: goods.goodsPortalSetting.result,
                    })
                } else {
                    message.error('保存失败')
                }
                break;
            case actions.UPDATE_GOODS_PORTAL_SETTING_FAILURE:
                message.error('保存失败')
                break;
            case logoActions.SELECT_BANK_LOGO_LIST_SUCCESS:
                this.setState({
                    bankLogoList: logo.bankLogoList.result,
                })
                break;
            default:
                break;
        }
    }

    onEvent = (type, params) => {
        switch (type) {
            case 'addchanel':
                this.props.router.push('/addchanel');
                break;

            case 'cancel':
                this.props.router.push('/KLF_PG_GM_GOODS_LIST');
                break;
            case 'insertGoods':
                this.props.actions.goodsInsert(params);
                break;
            case 'goodsUpdate':
                this.props.actions.goodsUpdate(params);
                break;
            case 'getSalesChannel':
                this.props.actions.getSalesChannel(params);
                break;
            case 'uploadFile':
                this.props.actions.updateFile(params)
                break;
            case 'insertGoodsPortalSetting':
                this.props.actions.insertGoodsPortalSetting(params)
                break;
            case 'updateGoodsPortalSetting':
                this.props.actions.updateGoodsPortalSetting(params)
                break;
            case 'getBankLogoList':
                this.props.actions.getBankLogoList(params);
                break;
            default:
        }
    }
    render() {
        const { channelList, authTypeList, goodsInfo, festivalList, serviceTypeList, giftTypeList, sysDict, isEdit, uploadedFile, goodsPortalSetting, bankLogoList, shopTypeList } = this.state;
        const { operation } = this.props;
        const picLoading = operation.loading[actions.UPDATE_FILE];
        //   console.info("goodsPortalSetting")
        //     console.info(goodsPortalSetting)
        return (

            <Fragment>
                {

                    <Tabs defaultActiveKey="1">

                        <TabPane tab="基本信息" key="1">
                            <BasicInfo
                                isEdit={isEdit}
                                goodsInfo={goodsInfo}
                                channelList={channelList}
                                authTypeList={authTypeList}
                                festivalList={festivalList}
                                data={{ ...this.state }}
                                onEvent={this.onEvent}
                            />
                        </TabPane>
                        <TabPane tab="产品组" key="2" disabled={this.state.TabDisabled}>
                            <GoodsGroup data={{ ...this.state }} festivalList={festivalList} onEvent={this.onEvent} sysDict={sysDict} giftTypeList={giftTypeList} serviceTypeList={serviceTypeList} shopTypeList={shopTypeList} goodsId={goodsInfo.id}></GoodsGroup>
                        </TabPane>
                        <TabPane tab="销售价格" key="3" disabled={this.state.TabDisabled}>
                            <GoodsPrice
                                goodsId={goodsInfo.id}
                            />
                        </TabPane>
                        <TabPane tab="使用条款" disabled={this.state.TabDisabled} key="4">
                            <GoodsClause
                                goodsId={goodsInfo.id}
                            />
                        </TabPane>
                        <TabPane tab="商品详情" disabled={this.state.TabDisabled} key="5">
                            <GoodsDetail
                                goodsId={goodsInfo.id}
                            />
                        </TabPane>
                        <TabPane tab="商品图片" disabled={this.state.TabDisabled} key="6">
                            <Pic
                                goodsId={goodsInfo.id}
                                onEvent={this.onEvent}
                                loading={picLoading}
                                uploadedFile={getFileList(uploadedFile)}
                            />
                        </TabPane>
                        <TabPane tab="网站设置" disabled={this.state.TabDisabled} key="7">
                            <Portal
                                goodsId={goodsInfo.id}
                                code={goodsPortalSetting ? goodsPortalSetting.code : null}
                                goodsPortalSetting={goodsPortalSetting}
                                bankLogoList={bankLogoList}
                                onEvent={this.onEvent}
                            />
                        </TabPane>
                    </Tabs>
                }
            </Fragment>
        );
    }
}
export default GoodsDeatils;