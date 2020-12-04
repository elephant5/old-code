import React, { Fragment } from 'react';
import Filter from './compoent/filter';
import List from './compoent/list';
import GenerateActCodeModal from './compoent/generateCode-modal';
import OutActCodeModal from './compoent/outCode-modal';
import ReturnActCodeModal from './compoent/returnCode-modal';
import ObsoleteActCodeModal from './compoent/obsoleteCode-modal';
import ProlongActCodeModal from './compoent/prolongCode-modal';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { bindActionCreators } from 'redux';
import * as actions from '../../store/giftCode/action';
import * as goodsActions from '../../store/goods/action';
import TableListBase from '../../base/table-list-base';
import { message, Input } from 'antd';
import { ACT_CODE_TAG } from '../../util/dictType'
@connect(
  ({ operation, code, goods }) => ({
    operation,
    code: code.toJS(),
    goods: goods.toJS()
  }),
  dispatch => ({
    actions: bindActionCreators({ ...actions, ...goodsActions }, dispatch),

  })
)
@withRouter
class GiftCode extends TableListBase {
  constructor(props) {
    super(props);
    this.state = {
      tableData: {},
      filterParmas: {},
      allGoodsList: [],
      salesChannelList: [],
      generateCodeVisible: false,
      isOut: false,
      genImmUpload: false,
      outImmUpload: false,
      outCodeVisible: false,
      outCodeDistroy: false,
      returnCodeVisible: false,
      returnCodeDistroy: false,
      obsoleteCodeVisible: false,
      obsoleteCodeDistroy: false,
      codeBatchNo: null,
      codeGoodsId: null,
      actCodeTags: null,
      pageSize: 10,
      generateCodeOkLoading: false,
      generateOutCodeLoading: false,
      outCodeOkLoading: false,
      outImmUploadFunLoading: false,
      returnCodeOkLoading: false,
      obsoleteCodeOkLoading: false,
      exportListLoading: false,
      prolongCodeVisible: false,
      prolongCodeDistroy: false,
      prolongCodeOkLoading: false,
      selectedRowKeys: []
    };
  }
  componentDidMount() {
    this.props.actions.selectGoodsList();
    this.props.actions.channelSelectAll();
    this.props.actions.getAllSysDict({ type: ACT_CODE_TAG });
    this.onList({
      action: this.props.actions.selectPageList,
      params: {
        "condition": {
        },
        "current": 1,
        "size": 10
      }
    })
  }
  componentWillReceiveProps(nextProps) {
    const { operation, code, goods } = nextProps;
    switch (operation.type) {
      case actions.GET_SELECTPAGELIST_SUCCESS:
        this.setState({
          tableData: code.giftCodePageList.result,
          isTableLoading: false
        })
        break;
      case goodsActions.GET_SELECTGOODSLIST_SUCCESS:
        this.setState({
          allGoodsList: goods.allGoodsList.result
        });
        break;
      case goodsActions.GET_CHANNELSELECTALL_SUCCESS:
        this.setState({
          salesChannelList: goods.channelList.result
        });
        break;
      case goodsActions.GET_GETALLSYSDICT_SUCCESS:
        this.setState({
          actCodeTags: goods.sysDict.result
        });
        break;
      default:
    }
  }
  onEvent = (type, params) => {
    const { pageSize } = this.state;
    switch (type) {
      // 查询
      case 'search':
        this.setState({
          filterParmas: params
        }, () => {
          this.onList({
            action: this.props.actions.selectPageList,
            params: {
              "condition": params,
              "current": 1,
              "size": pageSize
            }
          })
        })
        break;
      case 'export':
        this.setState({
          exportListLoading: true,
        })
        let values = {
          "condition": params,
          "current": 1,
        }
        this.props.actions.exportList(values).then((data) => {
          const { code } = this.props;
          if (code.exportListRes.code == 100 && code.exportListRes.result != null && code.exportListRes.result != "") {
            window.open(code.exportListRes.result)
          }
          this.setState({
            exportListLoading: false,
          })
        }).catch((error) => {
          message.error("系统出错")
          this.setState({
            exportListLoading: false,
          })
        });
        break;

      //打开生成激活码的模态框
      case 'generateCodeModal':
        this.setState({
          generateCodeVisible: true,
        });
        break;
      //打开出库激活码的模态框
      case 'outCodeModal':
        this.setState({
          outCodeVisible: true,
          outCodeDistroy: false
        });
        break;
      //打开退货激活码的模态框
      case 'returnCodeModal':
        this.setState({
          returnCodeVisible: true,
          returnCodeDistroy: false
        });
        break;
      //打开作废激活码的模态框
      case 'obsoleteCodeModal':
        this.setState({
          obsoleteCodeVisible: true,
          obsoleteCodeDistroy: false
        });
        break;
      //打开延长有效期的模态框
      case 'prolongCodeModal':
        if (this.state.selectedRowKeys.length > 0) {
          this.setState({
            prolongCodeVisible: true,
            prolongCodeDistroy: false
          });
        } else {
          message.error('尚未选择操作对象');
        }
        break;
      default:
    }
  }
  // 切换分页
  getList = params => {
    this.setState({
      pageSize: params.size,
      selectedRowKeys: []
    })
    this.onList({
      action: this.props.actions.selectPageList,
      params: {
        "condition": this.state.filterParmas,
        ...params
      }
    })
  }

  //生成激活码模态框确定按钮
  generateCodeOk = (type, params) => {
    switch (type) {
      case 'generate':
        this.setState({
          generateCodeOkLoading: true,
        })
        this.props.actions.generateActCode(params).then((data) => {
          const { code } = this.props;
          if (code.generateActCodeRes.code == 100) {
            message.success('生成激活码成功');
            this.setState({
              codeBatchNo: code.generateActCodeRes.result.codeBatchNo,
              codeGoodsId: code.generateActCodeRes.result.goodsId,
            });
          }
          this.setState({
            generateCodeOkLoading: false,
          })
        }).catch((error) => {
          message.error('系统错误');
          this.setState({
            generateCodeOkLoading: false,
          })
        });
        break;
      default:
    }
  }

  //生成激活码立即出库按钮
  immOutCode = e => {
    this.setState({
      isOut: true
    })
  }

  //生成激活码确认出库按钮
  generateOutCode = params => {
    this.setState({
      generateOutCodeLoading: true,
    })
    this.props.actions.outActCodeByBatch(params).then((data) => {
      const { code } = this.props;
      if (code.outActCodeByBatchRes.code == 100) {
        message.success('激活码出库成功');
        this.setState({
          // generateCodeVisible: false,
          genImmUpload: true,
          // codeBatchNo: null,
          // isOut: false
        });
      }
      this.setState({
        generateOutCodeLoading: false,
      })
    }).catch((error) => {
      message.error('系统错误');
      this.setState({
        generateOutCodeLoading: false,
      })
    });
  }

  //生成激活码模态框取消按钮
  generateCodeCancel = e => {
    this.setState({
      generateCodeVisible: false,
      codeBatchNo: null,
      isOut: false,
      genImmUpload: false,
    })
  }

  //出库激活码模态框确定按钮
  outCodeOk = (type, params) => {
    switch (type) {
      case 'out':
        this.setState({
          outCodeOkLoading: true,
        })
        this.props.actions.outActCodeByCodes(params).then((data) => {
          const { code } = this.props;
          if (code.outActCodeByCodesRes.code == 100) {
            message.success('激活码出库成功');
            this.setState({
              outImmUpload: true,
              // outCodeVisible: false,
              // outCodeDistroy: true,
            });
          }
          this.setState({
            outCodeOkLoading: false,
          })
        }).catch((error) => {
          message.error('系统错误');
          this.setState({
            outCodeOkLoading: false,
          })
        });
        break;
      default:
    }
  }

  //出库激活码下载按钮
  outImmUploadFun = params => {
    this.setState({
      outImmUploadFunLoading: true,
    })
    this.props.actions.exportOutCodeExcel(params).then((data) => {
      const { code } = this.props;
      if (code.exportOutCodeExcelRes.code == 100 && code.exportOutCodeExcelRes.result != null) {
        window.open(code.exportOutCodeExcelRes.result)
        this.setState({
          outImmUpload: false,
          outCodeVisible: false,
          outCodeDistroy: true,
        })
      } else {
        message.error("系统错误");
      }
      this.setState({
        outImmUploadFunLoading: false,
      })
    }).catch((error) => {
      message.error("系统出错")
      this.setState({
        outImmUploadFunLoading: false,
      })
    });
  }

  //出库激活码模态框取消按钮
  outCodeCancel = e => {
    if (this.state.outImmUpload) {
      this.setState({
        outCodeVisible: false,
        outImmUpload: false,
        outCodeDistroy: true,
      })
    } else {
      this.setState({
        outCodeVisible: false,
        // outImmUpload: false,
      })
    }
  }

  //退货激活码模态框确定按钮
  returnCodeOk = (type, params) => {
    switch (type) {
      case 'return':
        this.setState({
          returnCodeOkLoading: true,
        })
        this.props.actions.returnActCodeByCodes(params).then((data) => {
          const { code } = this.props;
          if (code.returnActCodeByCodesRes.code == 100) {
            message.success('激活码退货成功');
            this.setState({
              returnCodeVisible: false,
              returnCodeDistroy: true,
            });
          }
          this.setState({
            returnCodeOkLoading: false,
          })
        }).catch((error) => {
          message.error('系统错误');
          this.setState({
            returnCodeOkLoading: false,
          })
        });
        break;
      default:
    }
  }

  //退货激活码模态框取消按钮
  returnCodeCancel = e => {
    this.setState({
      returnCodeVisible: false,
    })
  }

  //作废激活码模态框确定按钮
  obsoleteCodeOk = (type, params) => {
    switch (type) {
      case 'obsolete':
        this.setState({
          obsoleteCodeOkLoading: true,
        })
        this.props.actions.obsoleteActCodeByCodes(params).then((data) => {
          const { code } = this.props;
          if (code.obsoleteActCodeByCodesRes.code == 100) {
            message.success('激活码作废成功');
            this.setState({
              obsoleteCodeVisible: false,
              obsoleteCodeDistroy: true,
            });
          }
          this.setState({
            obsoleteCodeOkLoading: false,
          })
        }).catch((error) => {
          message.error('系统错误');
          this.setState({
            obsoleteCodeOkLoading: false,
          })
        });
        break;
      default:
    }
  }

  //作废激活码模态框取消按钮
  obsoleteCodeCancel = e => {
    this.setState({
      obsoleteCodeVisible: false,
    })
  }
  //延长有效期模态框取消按钮
  prolongCodeCancel = e => {
    this.setState({
      prolongCodeVisible: false,
      prolongCodeDistroy: true,
    })
  }

  onSelectedRowKeysChange = (selectedRowKeys) => {
    this.setState({ selectedRowKeys });
  }

  //延长激活码有效期
  prolongGiftCodeOK = params => {
    this.setState({
      prolongCodeOkLoading: true,
    })
    this.props.actions.prolongGiftCode(params).then((data) => {
      const { code } = this.props;
      if (code.prolongGiftCodeRes.code == 100) {
        message.success('延长有效期成功');
        this.setState({
          prolongCodeVisible: false,
          prolongCodeDistroy: true,
        });
        this.clearCheck();
        this.onList({
          action: this.props.actions.selectPageList,
          params: {
            "condition": this.state.filterParmas,
            ...params
          }
        })
      } else {
        message.error(code.prolongGiftCodeRes.msg);
      }
      this.setState({
        prolongCodeOkLoading: false,
      })
    }).catch((error) => {
      message.error('系统错误');
      this.setState({
        prolongCodeOkLoading: false,
      })
    });
  }

  clearCheck = () => { // 处理勾选数据后清空勾选
    this.setState({
      selectedRowKeys: []
    })
  }
  render() {
    const { tableData, isTableLoading, allGoodsList, salesChannelList, generateCodeVisible, isOut, genImmUpload, outImmUpload, outCodeVisible, codeBatchNo, codeGoodsId, actCodeTags, outCodeDistroy, returnCodeVisible, returnCodeDistroy, obsoleteCodeVisible, obsoleteCodeDistroy, prolongCodeVisible, prolongCodeDistroy, selectedRowKeys, prolongCodeOkLoading } = this.state;
    const rowSelection = {
      selectedRowKeys,
      onChange: this.onSelectedRowKeysChange,
    };
    return (
      <Fragment>
        <Filter
          allGoodsList={allGoodsList}
          salesChannelList={salesChannelList}
          onEvent={this.onEvent}
          exportListLoading={this.state.exportListLoading}
        />
        <List
          data={tableData}
          loading={isTableLoading}
          onEvent={this.onEvent}
          pagination={this.getPagination}
          onList={this.getList}
          rowSelection={rowSelection}
        />

        <GenerateActCodeModal
          generateCodeVisible={generateCodeVisible}
          isOut={isOut}
          genImmUpload={genImmUpload}
          codeBatchNo={codeBatchNo}
          codeGoodsId={codeGoodsId}
          generateCodeOk={this.generateCodeOk}
          generateCodeCancel={this.generateCodeCancel}
          allGoodsList={allGoodsList}
          actCodeTags={actCodeTags}
          generateOutCode={this.generateOutCode}
          immOutCode={this.immOutCode}
          generateCodeOkLoading={this.state.generateCodeOkLoading}
          generateOutCodeLoading={this.state.generateOutCodeLoading}
        />

        {<OutActCodeModal
          outCodeVisible={outCodeVisible}
          outCodeOk={this.outCodeOk}
          outCodeCancel={this.outCodeCancel}
          actCodeTags={actCodeTags}
          outImmUpload={outImmUpload}
          outImmUploadFun={this.outImmUploadFun}
          outCodeDistroy={outCodeDistroy}
          outCodeOkLoading={this.state.outCodeOkLoading}
          outImmUploadFunLoading={this.state.outImmUploadFunLoading}
        />}

        <ReturnActCodeModal
          returnCodeVisible={returnCodeVisible}
          returnCodeOk={this.returnCodeOk}
          returnCodeCancel={this.returnCodeCancel}
          returnCodeDistroy={returnCodeDistroy}
          returnCodeOkLoading={this.state.returnCodeOkLoading}
        />

        <ObsoleteActCodeModal
          obsoleteCodeVisible={obsoleteCodeVisible}
          obsoleteCodeOk={this.obsoleteCodeOk}
          obsoleteCodeCancel={this.obsoleteCodeCancel}
          obsoleteCodeDistroy={obsoleteCodeDistroy}
          obsoleteCodeOkLoading={this.state.obsoleteCodeOkLoading}
        />
        <ProlongActCodeModal
          prolongCodeVisible={prolongCodeVisible}
          prolongCodeCancel={this.prolongCodeCancel}
          prolongCodeDistroy={prolongCodeDistroy}
          codeIds={selectedRowKeys}
          prolongGiftCodeOK={this.prolongGiftCodeOK}
          prolongCodeOkLoading={prolongCodeOkLoading}
        />
      </Fragment>
    );
  }
}

export default GiftCode;