import React, { Fragment } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as actions from '../../store/productGroup/action';
import ProductList from './compoent/productList';
import './detail.less';
import _ from 'lodash';
import { Divider, Tag } from 'antd';
import TableListBase from '../../base/table-list-base';
import { resourColor, serviceColor, giftColor } from '../../util/dictType.js'
@connect(
    ({ operation, productGroup }) => ({
        operation,
        productGroup: productGroup.toJS()
    }),
    dispatch => ({
        actions: bindActionCreators(actions, dispatch)
    })
)
class ProductGroupDetail extends TableListBase {
    constructor(props) {
        super(props);
        this.state = {
            productGroupDetail: {},
            blockRules: [],
            productList: []
        };
    }

    componentDidMount() {
        const id = this.props.routeParams.id;
        this.props.actions.getProductGroupDetail(id);
        // this.onList({
        //     action: this.props.actions.getProductGroupDetail,
        //     params: id
        // })

    }


    componentWillReceiveProps(nextProps) {
        const { operation, productGroup } = nextProps;
        const id = this.props.routeParams.id;
        switch (operation.type) {
            case actions.GET_PRODUCTGROUPDETAIL_SUCCESS:
                this.setState({
                    productGroupDetail: productGroup.productGroupDetail.result,
                    blockRules: productGroup.productGroupDetail.result.blockRules,
                    productList: productGroup.productGroupDetail.result.productVoList,
                    isTableLoading: false
                })
                break;
            default:
        }
    }

    //事件回传
    onEvent = (type, params) => {
        // const { deleteHotelPortal, updateHotelPortal, updateHotelBaseMsg } = this.props.actions;
        // switch(type) {
        //     // 酒店探索章节增加/修改
        //     case 'edit':
        //         const {fileList, ...other} = params;
        //         // 图片上传list
        //         this.setState({
        //             fileList: fileList
        //         }, () => {
        //             updateHotelPortal(other);
        //         })
        //         break;
        //     // 酒店探索章节删除
        //     case 'delete':
        //         deleteHotelPortal(params);
        //         break;
        //     // 酒店基本信息修改接口
        //     case 'saveBaseInfo':
        //         updateHotelBaseMsg(params)
        //         break;
        //     default:
        //         break;
        // }
    }

    render() {
        const { productGroupDetail, blockRules, productList, isTableLoading } = this.state;
        return (
            <Fragment>
                {/* <div className="c-modal">
                    <div className="c-title">产品组详情</div>
                    <div className={'group-detail-wrapper'}>
                        <div className={'group-detail-msg-left'}>
                            <span>{productGroupDetail.shortName}</span>&nbsp;&nbsp;&nbsp;<span>{productGroupDetail.gift}</span><br></br>
                            <span>{productGroupDetail.useLimit}：{productGroupDetail.useNum}</span><br></br>
                            <span>折扣比例：{productGroupDetail.discountRate * 100}%</span>
                        </div>
                        <div className={'group-detail-msg-center'}>
                            <span>{productGroupDetail.service}</span>
                        </div>
                        <div className={'group-detail-msg-right'}>
                            <span>成本最高价：{productGroupDetail.maxCost}</span><br/>
                            <span>成本最低价：{productGroupDetail.minCost}</span>
                        </div>
                        <Divider dashed className={'group-detail-divider'}/>
                        <div className={'group-detail-msg-bottom'}>
                            <span>产品组Block：</span>
                            {
                                blockRules.map(item => {
                                    return <Tag>{item.natural}</Tag>
                                })
                            }
                        </div>
                    </div>
                </div>*/}
                <div>

                    <div className="c-modal" style={{ marginBottom: '0px' }} >
                        <div className="shop-item-wrapper" key={1} style={{ margin: '24px 24px 0' }} >

                            <div className="shop-basic-wrapper" style={{ width: '40%' }}>
                                <div className="shop-img" style={{ margin: '24px 14px 0 0' }}>
                                    <img width="140px" height="80px" src='../imgs/default-picture@2x.jpg' />
                                </div>
                                <div className="shop-basic">
                                    <div className="shop-title">
                                        {productGroupDetail.shortName}  &nbsp;&nbsp;
                            {productGroupDetail.service && productGroupDetail.service.split(" ").map(s => {

                                            // return (<Tag color={doc2[tems]} key={s}>{s}</Tag>)
                                            return serviceColor.map(item => {
                                                if (item.name === s) {
                                                    return <Tag color={item.color} key={s} >{s}</Tag>;
                                                }
                                            });
                                        })}

                                    </div>
                                    <div className="shop-addon">{productGroupDetail.useLimit}：{productGroupDetail.useNum}</div>
                                    <div className="shop-addon">折扣比例：{productGroupDetail.discountRate ? productGroupDetail.discountRate * 100 : 0}%</div>
                                </div>
                            </div>
                            <div className="shop-basic-wrapper" style={{ width: '30%' }}>
                                {productGroupDetail.gift && productGroupDetail.gift.split(" ").map(list => {
                                    return giftColor.map(item => {
                                        if (item.name === list) {
                                            return <Tag color={item.color} key={list} >{list}</Tag>;
                                        }
                                    });

                                })}

                            </div>
                            <div style={{ width: '30%' }}  >
                                <label>成本最高价：{productGroupDetail.maxCost ? productGroupDetail.maxCost : '-'}</label>
                                <br />
                                <label>成本最低价：{productGroupDetail.minCost ? productGroupDetail.minCost : '-'}</label>
                            </div>

                        </div>
                        <Divider style={{ margin: '0px 5' }} />
                        <div style={{ margin: '5px 10px 20px 25px' }}>

                            <b>产品组Blocks：</b>&nbsp;&nbsp;&nbsp;&nbsp;
                        {blockRules && blockRules.map(item => (

                                <Tag closable
                                    key={item.rule}
                                    onClose={e => e.stopPropagation()}
                                    afterClose={() => this.onClose(item)}
                                // onClick={() => this.editBlock(item)}
                                >{item.natural}</Tag>))
                            }


                            {/* <Tag closable>Tag 2</Tag><Tag closable>Tag 2</Tag><Tag closable>Tag 2</Tag><Tag closable>Tag 2</Tag><Tag closable>Tag 2</Tag><Tag closable>Tag 2</Tag> */}
                        </div>
                        {/* <Divider className='divider-css' style={{margin: '0px 0'}}/> */}
                        <br></br>
                    </div>
                </div>

                <div className={'group-detail-list'}>
                    <div className="c-modal">
                        <div className="c-title"><span>产品列表</span><span>(包含{productList.length}个产品)</span></div>
                        <ProductList
                            data={productList}
                            loading={isTableLoading}
                            onEvent={this.onEvent}
                        />
                    </div>
                </div>
            </Fragment>
        );
    }
}

export default ProductGroupDetail;