import React, { Fragment } from 'react';
import UploadLogo from './component/UploadLogo';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { bindActionCreators } from 'redux';
import * as actions from '../../store/logo/action';
import TableListBase from '../../base/table-list-base';
import locale from 'antd/lib/locale-provider/zh_CN';
import { message, List, Card, Row, Col, Button } from 'antd';
import { getHttpPro } from '../../util/util';
import cookie from 'react-cookies';
@connect(
    ({ operation, logo }) => ({
        operation,
        logo: logo.toJS()
    }),
    dispatch => ({
        actions: bindActionCreators(actions, dispatch)
    })
)
@withRouter
class Logo extends TableListBase {
    constructor(props) {
        super(props);
        this.state = {
            // bankLogoList:{},
            isShowUploadModal: false,
        };
    }
    componentDidMount() {
        this.props.actions.selectBankLogoList();
        // console.info(getHttpPro())
    }
    componentWillReceiveProps(nextProps) {
        // console.log(nextProps)
        const { operation, logo } = nextProps;
        let { bankLogoList } = this.state;
        // console.log(logo.bankLogoList)

        switch (operation.type) {
            case actions.SELECT_BANK_LOGO_LIST_SUCCESS:
                this.setState({
                    bankLogoList: logo.bankLogoList.result,
                })
                break;
            case actions.SELECT_BANK_LOGO_LIST_FAILURE:
                message.error('查询失败')
                break;
            case actions.INSERT_BANK_LOGO_SUCCESS:
                const bankLogo = logo.bankLogo.result
                if (bankLogo) {
                    if (!bankLogoList) {
                        bankLogoList = []

                    }
                    bankLogoList.push(bankLogo)

                }
                this.setState({
                    isShowUploadModal: false,
                    bankLogoList: bankLogoList,
                })
                message.success('上传成功')
                break;
            case actions.INSERT_BANK_LOGO_FAILURE:
                this.setState({
                    isShowUploadModal: false
                })
                message.error('上传失败')
                break;
            default:
        }
    }
    onEvent = (type, params) => {
        const { pageSize } = this.state;
        switch (type) {
            case 'uploadLogo':
                this.props.actions.insertBankLogo(params);
                break;
            default:
        }
    }
    // 弹出上传logo框
    uploadLogo = item => {
        console.info('uploadLogo')
        this.setState({
            isShowUploadModal: true
        })
    }
    //上传Logo
    onOk = params => {
        this.setState({
            isShowUploadModal: false
        })
    }
    // 关闭上传Logo框
    onCancel = () => {
        this.setState({
            isShowUploadModal: false
        })
    }
    render() {
        const { bankLogoList, isShowUploadModal } = this.state;
        const httpPro = getHttpPro();
        const upload = cookie.load("KLF_PG_SM_LOGO_UPLOAD");
        return (
            <Fragment>
                <div style={{ 'margin-bottom': '10px' }}>
                    <Row>
                        <Col>
                            {upload && <Button type="primary" htmlType="button" onClick={this.uploadLogo}>上传Logo</Button>}
                            {
                                isShowUploadModal &&
                                <UploadLogo
                                    onCancel={this.onCancel}
                                    onOk={this.onOk}
                                    onEvent={this.onEvent}
                                />
                            }
                        </Col>
                    </Row>
                </div>
                {
                    bankLogoList &&
                    <List
                        grid={{
                            gutter: 5,
                            xs: 1,
                            sm: 2,
                            md: 3,
                            lg: 4,
                            xl: 5,
                            xxl: 6,
                        }}
                        dataSource={bankLogoList}
                        renderItem={item => (
                            <List.Item >
                                <Card
                                    style={{ "background-color": "transparent" }}
                                    title={item.name}
                                    cover={<img src={httpPro + item.sysFileDto.pgCdnNoHttpFullUrl} style={{ "height": "80px", "background-color": "transparent" }} />}
                                >
                                </Card>
                            </List.Item>
                        )}
                    />
                }
            </Fragment>

        );
    }
}

export default Logo;