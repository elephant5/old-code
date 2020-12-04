import React from 'react';
import { Tabs } from 'antd';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { bindActionCreators } from 'redux';
import * as actions from '../../store/resource/action';
import * as commonActions from '../../store/common/action';
import BaseInfo from './component/baseInfo';
import Resource from './component/resource';
import Protocol from './component/protocol';
import Pic from './component/pic';
import Log from './component/log';
import TableListBase from '../../base/table-list-base';
import { UPLOAD_URL } from '../../util/url';
import { getHttpPro } from '../../util/util';
import _ from 'lodash';
import { message } from 'antd';
import { getFileList } from '../../util/index';
import { Form, Input, Button, Row, Col } from 'antd'
import BraftEditor from 'braft-editor'
import { ACCOM_BLOCK, BUFFET_BLOCK, SPA_GYM_BLOCK } from '../../util/dictType'
import { LocaleProvider } from 'antd';
import zh_CN from 'antd/lib/locale-provider/zh_CN';
import cookie from 'react-cookies';
const TabPane = Tabs.TabPane;
const FormItem = Form.Item;
const formItemLayout = {
    labelCol: { span: 4 },
    wrapperCol: { span: 16 },
};

const myUploadFn = (param) => {

    const serverURL = UPLOAD_URL
    const xhr = new XMLHttpRequest
    const fd = new FormData()

    const successFn = (response) => {
        // 假设服务端直接返回文件上传后的地址
        // 上传成功后调用param.success并传入上传后的文件地址

        if (response && response.target && response.target.response) {
            const res = JSON.parse(response.target.response);
            // console.log(res);
            if (res.code === 100 && res.result) {
                const result = res.result;
                // const fileUrl=result.pgCdnHttpUrl+'/'+result.guid+'.'+result.ext;
                const fileUrl = getHttpPro() + result.pgCdnNoHttpFullUrl;
                param.success({
                    url: fileUrl,

                })
            }
        }

    }

    const progressFn = (event) => {
        // 上传进度发生变化时调用param.progress
        param.progress(event.loaded / event.total * 100)
    }

    const errorFn = (response) => {
        // 上传发生错误时调用param.error
        param.error({
            msg: 'unable to upload.'
        })
    }

    xhr.upload.addEventListener("progress", progressFn, false)
    xhr.addEventListener("load", successFn, false)
    xhr.addEventListener("error", errorFn, false)
    xhr.addEventListener("abort", errorFn, false)
    // xhr.setRequestHeader("Content-Type","application/json");
    fd.append('file', param.file)
    fd.append('fileType', 'hotel.pic')
    xhr.open('POST', serverURL, true)
    xhr.send(fd)

}
@connect(
    ({ operation, resource, common }) => ({
        operation,
        resource: resource.toJS(),
        common: common.toJS()
    }),
    dispatch => ({
        actions: bindActionCreators({ ...actions, ...commonActions }, dispatch)
    })
)
@withRouter
@Form.create()
class AddTwo extends TableListBase {
    constructor(props) {
        super(props);
        this.state = {
            shopDetail: {
                // 基本信息
                shop: {},
                shopItemList: [],
                // 商户协议
                shopProtocol: {}
            },
            // 国家城市列表
            countryCity: [],
            // 商户渠道
            channels: [],
            // 结算方式列表
            settleMethods: [],
            // 货币类型列表
            currencys: [],
            // block节假日
            festivalList: [],
            // 操作日志列表
            shopLogList: {},
            // 已上传的文件列表
            uploadedFile: [],
            giftTypeList: [],
            allSysDictList: [],
            pageSize: 10,
        };
    }
    componentDidMount() {
        this.props.actions.getShopDetail(this.props.routeParams.id);
        this.props.actions.getCountryCity();
        this.props.actions.getChannel();
        this.props.actions.getSettleMethod();
        this.props.actions.getCurrency();
        this.props.actions.getFestivalList();
        this.props.actions.getGiftTypeList();

    }

    componentWillReceiveProps(nextProps) {
        const { operation, resource, common } = nextProps;
        switch (operation.type) {
            case actions.GET_SHOPDETAIL_SUCCESS:
                this.setState({
                    shopDetail: resource.shopDetail.result
                }, () => {
                    setTimeout(() => {
                        this.props.form.setFieldsValue({
                            detail: BraftEditor.createEditorState(this.state.shopDetail.shop.detail)
                        })
                    }, 500)
                    let shopTypeTemp = resource.shopDetail.result.shop.shopType;
                    if (shopTypeTemp === 'accom') {
                        this.props.actions.getAllSysDict({ type: ACCOM_BLOCK });
                    }
                    if (shopTypeTemp === 'buffet') {
                        this.props.actions.getAllSysDict({ type: BUFFET_BLOCK });
                    }
                    if (shopTypeTemp === 'gym' || shopTypeTemp === 'spa') {
                        this.props.actions.getAllSysDict({ type: SPA_GYM_BLOCK });
                    }
                    // 商户图片默认展示
                    this.getUploadedFile(resource.shopDetail.result.shop.id, 'shop.pic')
                    // 操作日志
                    this.onList({
                        action: this.props.actions.getShopLogList,
                        params: {
                            "condition": {
                                shopId: this.state.shopDetail.shop.id
                            },
                            "current": 1,
                            "size": 10
                        }
                    })
                })
                break;
            case actions.GET_COUNTRYCITY_SUCCESS:
                this.setState({
                    countryCity: resource.countryCity.result
                })
                break;
            case actions.GET_CHANNEL_SUCCESS:
                this.setState({
                    channels: resource.channelList.result
                })
                break;
            case actions.GET_SETTLEMETHOD_SUCCESS:
                this.setState({
                    settleMethods: resource.settleMethod.result
                })
                break;
            case actions.GET_CURRENCY_SUCCESS:
                this.setState({
                    currencys: resource.currency.result
                })
                break;
            case commonActions.GET_FESTIVALLIST_SUCCESS:
                this.setState({
                    festivalList: common.festivalList
                })
                break;
            // 协议，基本信息修改
            case actions.UPDATE_SHOPBASEMSG_SUCCESS:
                message.success('基本信息保存成功！');
                break;
            case actions.UPDATE_SHOPPROTOCOL_SUCCESS:
                if(resource.shopProtocol.code == 100 ){
                    message.success('协议信息保存成功！');
                    this.props.actions.getShopDetail(this.props.routeParams.id);
                }else{
                    message.error('协议信息保存失败：' + resource.shopProtocol.msg);
                }
               
                break;
            case actions.GET_SHOPITEMLIST:
                this.setState({
                    shopItemList: resource.shopItemList.result
                })
                break;
            case actions.GET_SHOPITEMDETAIL:
                this.setState({
                    shopItemDetail: resource.shopItemDetail.result
                })
                break;
            case actions.GET_SHOPLOGLIST_SUCCESS:
                this.setState({
                    shopLogList: resource.shopLogList.result,
                    isTableLoading: false
                })
                break;
            case commonActions.GET_UPLOADEDFILE_SUCCESS:
                this.setState({
                    uploadedFile: common.uploadedFile
                })
                break;
            // merge成功后更新默认显示
            case actions.UPDATE_FILE_SUCCESS:
                if (resource.file.code === 100) {
                    const objId = resource.file.result[0].objId;
                    const type = resource.file.result[0].type;
                    this.getUploadedFile(objId, type);
                }

                break;
            // merge成功后更新默认显示
            case actions.GET_ADDSHOPDETAIL_SUCCESS:
                message.success('商品详情保存成功！');
                break;
            case actions.GET_GIFTTYPELIST_SUCCESS:
                this.setState({
                    giftTypeList: resource.giftTypeList.result
                })
                break;
            case commonActions.GET_GETALLSYSDICT_SUCCESS:
                this.setState({
                    allSysDictList: common.allSysDictList,
                })
                break;
            default:
                break;
        }
    }

    // 上传图片默认展示接口
    getUploadedFile = (objId, type) => {
        this.props.actions.getUploadedFile({ objId, type });
    }
    onEvent = (type, params) => {
        const { pageSize } = this.state;
        switch (type) {
            // 基本信息修改
            case 'baseInfo':
                this.props.actions.updateShopBaseMsg(params);
                break;
            // 协议信息修改
            case 'protocol':
                this.props.actions.updateShopProtocol(params);
                break;
            case 'uploadFile':
                this.props.actions.updateFile(params)
                break;
            default:
                break;
        }
    }
    // 添加保存
    handleSubmit = e => {
        e.preventDefault();
        const { data, actions } = this.props;
        const { shopDetail, editorState } = this.state;
        this.props.form.validateFields((err, values) => {
            // console.log(err)
            // console.log(!err)
            if (!err) {
                // console.log("data");
                // console.log(data);
                // console.log("values");
                // console.log(values);
                // console.log(this.state.editorState.toHTML());
                // console.log(goodsDetail)
                shopDetail.shop.detail = editorState.toHTML();
                // console.log(goodsDetail)
                this.props.actions.addShopDetail(shopDetail.shop);
            }
        });
    }
    handleChange = (editorState) => {
        this.setState({
            editorState: editorState,
        })
    }
    getList = params => {
        this.setState({
            pageSize: params.size,
        })
        this.onList({
            action: this.props.actions.getShopLogList,
            params: {
                "condition": {
                    shopId: this.state.shopDetail.shop.id
                },
                ...params
            }
        })
    }

    render() {
        const { shopDetail, countryCity, festivalList, uploadedFile,
            channels, settleMethods, currencys, shopLogList, isTableLoading, giftTypeList, allSysDictList
        } = this.state;
        const { getFieldDecorator } = this.props.form
        const { operation } = this.props;
        const baseLoading = operation.loading[actions.UPDATE_SHOPBASEMSG];
        const picLoading = operation.loading[actions.UPDATE_FILE];
        const protocolLoading = operation.loading[actions.UPDATE_SHOPPROTOCOL];
        const edit = cookie.load("KLF_PG_RM_SL_DETAIL_EDIT");
        const infoView = cookie.load("KLF_PG_RM_SL_INFO_VIEW");
        const serviceView = cookie.load("KLF_PG_RM_SL_SERVICE_VIEW");
        const protocolView = cookie.load("KLF_PG_RM_SL_PROTOCOL_VIEW");
        const picView = cookie.load("KLF_PG_RM_SL_PIC_VIEW");
        const detailView = cookie.load("KLF_PG_RM_SL_DETAIL_VIEW");
        const logView = cookie.load("KLF_PG_RM_SL_LOG_VIEW");
        return (
            <Tabs defaultActiveKey='1'>
                {infoView && <TabPane tab="基本信息" key={1}>
                    {
                        !_.isEmpty(countryCity) && !_.isEmpty(shopDetail.shop) &&
                        <BaseInfo
                            data={{ ...shopDetail, countryCity }}
                            onEvent={this.onEvent}
                            loading={baseLoading}
                        />
                    }
                </TabPane>}
                {serviceView && <TabPane tab="商户资源" key={2}>
                    <LocaleProvider locale={zh_CN} >
                        <Resource
                            data={{ ...shopDetail, countryCity }}
                            onEvent={this.onEvent}
                            actions={this.props.actions}
                            giftTypeList={giftTypeList}
                            allSysDictList={allSysDictList}
                            shopProtocol={shopDetail.shopProtocol}
                            currencys={currencys}
                        //   shopItemDetail={shopItemDetail}
                        />
                    </LocaleProvider>
                </TabPane>}

                {protocolView && <TabPane tab="协议信息" key={3}>
                    {
                        !_.isEmpty(shopDetail.shopProtocol) &&
                        <Protocol
                            data={{ ...shopDetail, channels, settleMethods, currencys, festivalList }}
                            onEvent={this.onEvent}
                            shop={shopDetail.shop}
                            loading={protocolLoading}
                            allSysDictList={allSysDictList}
                        />
                    }
                </TabPane>}
                {picView && <TabPane tab="商户图片" key={4}>
                    <Pic
                        onEvent={this.onEvent}
                        data={shopDetail}
                        loading={picLoading}
                        uploadedFile={getFileList(uploadedFile)}
                    />
                </TabPane>}
                {detailView && <TabPane tab="商户详情" key={6}>
                    <div className="demo-container">
                        <Form onSubmit={this.handleSubmit}>
                            <FormItem {...formItemLayout} label="商户详情">
                                {getFieldDecorator('detail', {
                                })(
                                    <BraftEditor
                                        style={{ border: 'solid 1px #ccc', marginTop: 10 }}
                                        placeholder="请输入"
                                        // converts={{ unitImportFn, unitExportFn }}
                                        onChange={this.handleChange}
                                        media={{ uploadFn: myUploadFn, accepts: { video: false, audio: false }, externals: { video: false, audio: false, embed: false, image: false } }}
                                    />
                                )}
                            </FormItem>

                            <Form.Item>
                                <Row gutter={10}>
                                    <Col span={2} offset={4}>
                                        {edit && <Button size="large" type="primary" style={{ marginBottom: 8 }} htmlType="submit">保存</Button>}
                                    </Col>
                                    <Col span={2}>

                                    </Col>
                                </Row>
                            </Form.Item>
                        </Form>
                    </div>
                </TabPane>}
                {logView && <TabPane tab="操作日志" key={5}>
                    <Log
                        data={shopLogList}
                        pagination={this.getPagination}
                        onList={this.getList}
                        loading={isTableLoading}
                    />
                </TabPane>}
            </Tabs>
        );
    }
}

export default AddTwo;

