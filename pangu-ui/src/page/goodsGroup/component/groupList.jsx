import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { bindActionCreators } from 'redux';
import * as commonActions from '../../../store/common/action';
import { message, Collapse, Form, Badge, Divider, Input, Button, CheckableTag, Mention, Tabs, span, Popconfirm, Icon, Checkbox, DatePicker, Tag } from 'antd';
import AddModal from '../../merchant/component/add-modal';
import GoodsEditModal from '../../merchant/component/edit-modal';
import CommonUpload from '../../../component/common-upload/index';
import { FILE_TYPE } from '../../../config/index';
import * as actions from '../../../store/goods/action';
import { unique } from '../../../util/index';
import { resourColor, serviceColor, giftColor } from '../../../util/dictType.js'
import _ from 'lodash';
import cookie from 'react-cookies';
import { getFileList } from '../../../util/index';
import Pic from './pic';
const Panel = Collapse.Panel;
const TabPane = Tabs.TabPane;
@connect(
    ({ operation, goods,common }) => ({
        operation,
        goods: goods.toJS(),
        common:common.toJS()
    }),
    dispatch => ({
        actions: bindActionCreators({...actions,...commonActions }, dispatch)
    })
)
@withRouter
@Form.create()
class GroupList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isShowAddModal: false,
            isShowEditModal: false,
            product: {},
            // block规则
            blockRuleList: [],
            // 已上传的文件列表
            uploadedFile: [],
        };
    }
    componentDidMount() {
        const { goodsId, product } = this.props;
        this.setState({
            product: product,
            blockRuleList: product.blockRuleList
        });
        // 商户图片默认展示
        this.getUploadedFile(product.id, 'group.pic')
    }

    componentWillReceiveProps(nextProps) {
        const { operation, goods,common } = nextProps;
        switch(operation.type){
            case commonActions.GET_UPLOADEDFILE_SUCCESS:
                this.setState({
                    uploadedFile: common.uploadedFile
                })
            // console.log("common.upload",common.uploadedFile)
                break;
                // merge成功后更新默认显示
                case actions.UPDATE_FILE_SUCCESS:
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
        const { product, blockRuleList } = this.state;
        switch (type) {
            case 'deleteGroupBlock':
                let temp = {};
                temp.productGroupId = product.id;
                temp.blockRuleList = blockRuleList;
                this.props.actions.editGroupBlock(temp);
                break;
            case 'uploadFile':
                this.props.actions.updateFile(params);
                break;
            default:
        }
    }
    // 关闭block框
    onCancel = () => {
        this.setState({
            isShowAddModal: false
        })
    }
    onEditCancel = () => {
        this.setState({
            isShowEditModal: false
        })
    }
    onOk = params => {
        const { product, blockRuleList } = this.state;
        let rules = [];
        if (blockRuleList) {
            rules = [...blockRuleList, ...params.festival, ...params.blockRule];
            //  rules.push(blockRuleList);

        } else {
            rules = [...params.festival, ...params.blockRule];
        }

        const norepeatRule = unique(rules);
        product.blockRuleList = norepeatRule

        this.setState({
            product: product,
            blockRuleList:norepeatRule,
            isShowAddModal: false
        }, () => {
            let params = {};
            params.productGroupId = product.id;
            params.specialBlocks = norepeatRule;
            this.props.actions.addGroupBlock(params);
        })
    }
    // 编辑block保存
    handleEdit = params => {
        // 先删除当前条，在覆盖
        const { product, blockRuleList } = this.state;
        this.setState({
            blockRuleList: this.state.blockRuleList.filter(item => item.rule !== params.deleteData.rule)
        }, () => {
            const rules = [...this.state.blockRuleList, ...params.festival, ...params.blockRule];
            const norepeatRule = unique(rules);
            product.blockRuleList = norepeatRule;
            let temp = {};
            temp.productGroupId = product.id;
            temp.blockRuleList = norepeatRule;
            this.props.actions.editGroupBlock(temp);
            this.setState({
                product: product,
                isShowEditModal: false
            }, () => {


            })
        })
    }
    // 弹出新增block框
    addBlock = item => {
        // const {  blockRuleList } = this.state;
        // if(blockRuleList.length === 0 ){
        //     this.setState({
        //         isShowAddModal: true
        //     })
        // }else{
        //     this.setState({
        //         isShowEditModal: true,
        //         editParams: blockRuleList
        //     })
        // }
        this.setState({
            isShowAddModal: true
        })
    }
    // 删除block
    onClose = params => {
        this.setState({
            blockRuleList: this.state.blockRuleList.filter(item => item.rule !== params.rule)
        }, () => {


            this.onEvent("deleteGroupBlock")
        })

    }
    // 打开编辑block
    editBlock = item => {
        this.setState({
            isShowEditModal: true,
            editParams: item
        })
    }
    render() {
        const { onEvent, festivalList, key, sysDict, product,operation } = this.props;
        const { isShowAddModal, isShowEditModal, editParams,uploadedFile } = this.state;
        var b = (JSON.stringify(uploadedFile) == "[]");
        if(!b && uploadedFile[0].objId == product.id){
            product.url = uploadedFile[0].pgCdnHttpsFullUrl;
           product.list = uploadedFile;
        }
        const picLoading = operation.loading[actions.UPDATE_FILE];
        const edit = cookie.load("KLF_PG_GM_GL_EDIT");
        return (<Fragment>  {
            <div className="c-modal" style={{ marginBottom: '0px' }} key={key}>
                <div className="shop-item-wrapper" key={1} style={{ margin: '24px 24px 0' }} >
                    <div className="shop-basic-wrapper" style={{ width: '40%' }}>
                        <div className="shop-img"  style={{ margin: '24px 14px 0 0' }}>
                               {!product.url && <Pic  
                                groupId={product.id}
                                onEvent={this.onEvent}
                                loading={picLoading}
                                // uploadedFile={getFileList(product.list)}
                            />}
                        </div>
                        <div className="shop-img"  style={{ margin: '24px 14px 0 0' }}>
                               {product.url && <Pic  
                                groupId={product.id}
                                onEvent={this.onEvent}
                                loading={picLoading}
                                uploadedFile={getFileList(product.list)}
                            />}
                        </div>
                        {/* <div className="shop-img" >
                                {product.url && <img style={{ width: '200px',height: '140px' }} src={product.url} />}
                        </div> */}
                        <div className="shop-basic">
                            <div className="shop-title">
                                {product.name}  &nbsp;&nbsp;
                            {product.service && product.serviceList.map(s => {

                                    // return (<Tag color={doc2[tems]} key={s}>{s}</Tag>)
                                    return serviceColor.map(item => {
                                        if (item.name === s) {
                                            return <Tag color={item.color} key={s} >{s}</Tag>;
                                        }
                                    });
                                })}

                            </div>
                            <div className="shop-addon">{sysDict.map(item => {
                                if (item.value === product.useLimitId && product.useLimitId === 'fixed_times') {
                                    return '使用限制: ' + item.label + ": " + product.useNum + ' 次';
                                }
                                if (item.value === product.useLimitId && product.useLimitId === 'fixed_point') {
                                    return '使用限制: ' + item.label + ": " + product.useNum + ' 点';
                                }
                                if (item.value === product.useLimitId && product.useLimitId === 'none') {
                                    return '使用限制: ' + item.label;
                                }
                                if (item.value === product.useLimitId && product.useLimitId === 'cycle_repeat') {
                                    let typeWord = '';
                                    if(product.cycleType === 0){
                                        typeWord = '天';
                                    }else if(product.cycleType === 1){
                                        typeWord = '周';
                                    }
                                    else if(product.cycleType === 2){
                                        typeWord = '月';
                                    }
                                    else if(product.cycleType === 3){
                                        typeWord = '年';
                                    }
                                    if (product.useType === 0){
                                       return '使用限制: ' + item.label + ": " + product.useNum + '次,' + "每" + product.cycleTime + typeWord + product.cycleNum + '次';
                                    }else {
                                       return '使用限制: ' + item.label + ": " + product.useNum + '点,' + "每" + product.cycleTime + typeWord + product.cycleNum + '点';
                                   }
                                }
                            })}</div>
                            <div className="shop-addon">折扣比例：{product.discountRate ? product.discountRate * 100 : 0}%</div>
                        </div>
                    </div>
                    <div className="shop-basic-wrapper" style={{ width: '30%' }}>
                        {product.gift && product.giftList.map(list => {
                            return giftColor.map(item => {
                                if (item.name === list) {
                                    return <Tag color={item.color} key={list} >{list}</Tag>;
                                }
                            });

                        })}

                    </div>
                    {edit && <div className="shop-ops"  >
                        <Icon className="shop-op" type="edit" onClick={() => onEvent('groupEdit', { id: product.id })} />
                        <Popconfirm placement="top" title="确定删除当前产品组吗？" onConfirm={() => onEvent('groupDelete', { id: product.id })} okText="确定" cancelText="取消">
                            <Icon className="shop-op" type="delete" /></Popconfirm>
                    </div>}

                </div>
                <Divider style={{ margin: '0px 0' }} />
                <div style={{ margin: '5px 10px 20px 25px' }}>

                    <b>产品组Block：</b>{edit && <Button type="primary" ghost onClick={this.addBlock}>+ 添加Block</Button>} &nbsp;&nbsp;&nbsp;&nbsp;
                        {product.blockRuleList && product.blockRuleList.map(item => (

                        <Tag closable
                            key={item.rule}
                            onClose={e => e.stopPropagation()}
                            afterClose={() => this.onClose(item)}
                        // onClick={() => this.editBlock(item)}
                        >{item.natural}</Tag>))
                    }
                    {
                        // 新增block弹框
                        isShowAddModal &&
                        <AddModal
                            onCancel={this.onCancel}
                            onOk={this.onOk}
                            data={{ festivalList: festivalList }}
                        />
                    }
               </div>
                {/* <Divider className='divider-css' style={{margin: '0px 0'}}/> */}
            </div>
        }
        </Fragment>
        );
    }
}
export default GroupList;