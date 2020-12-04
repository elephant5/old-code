import 'braft-editor/dist/index.css'
import React from 'react'
import BraftEditor from 'braft-editor'
import { Form, Input, Button, message } from 'antd'
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as actions from '../../../store/goods/action';
import { UPLOAD_URL } from '../../../util/url';
import { getHttpPro } from '../../../util/util';
import cookie from 'react-cookies';
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
    // console.log("response")
    // console.log(response)
    // console.log(response.srcElement)
    // console.log(response.target)
    // console.log(response.srcElement.response)
    // console.log(response.target.response)
    // console.log(response.target.response.code)
    // console.log(response.target.response.result)
    if (response && response.target && response.target.response) {
      const res = JSON.parse(response.target.response);
      // console.log(res);
      if (res.code == 100 && res.result) {
        const result = res.result;
        // console.log(111111)
        // const fileUrl=result.pgCdnHttpUrl+'/'+result.guid+'.'+result.ext;
        const fileUrl = getHttpPro() + result.pgCdnNoHttpFullUrl;
        // console.log(fileUrl)
        param.success({
          url: fileUrl,
          // meta: {
          //     // id: 'xxx',
          //     // title: 'xxx',
          //     // alt: 'xxx',
          //     // loop: true, // 指定音视频是否循环播放
          //     autoPlay: true, // 指定音视频是否自动播放
          //     controls: true, // 指定音视频是否显示控制栏
          //     poster: fileUrl, // 指定视频播放器的封面
          // }
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
  ({ operation, goods }) => ({
    operation,
    goods: goods.toJS()
  }),
  dispatch => ({
    actions: bindActionCreators(actions, dispatch)
  })
)
@Form.create()
class GoodsClause extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      goodsClauseList: [],
      goodsClase: {},
    };
  }
  componentWillMount() {
    const { goodsId } = this.props;
    const { goodsClase } = this.state;
    goodsClase.goodsId = goodsId;
    // console.log(this.props);
    // console.log(goodsId);
    this.props.actions.selectGoodsClauseListByGoodsId(goodsClase);
  }
  componentDidMount() {
    setTimeout(() => {
      // console.log(this.state.goodsClauseList);
      let clauseList = [];
      this.state.goodsClauseList.map((clause, index) => {
        clauseList[index] = BraftEditor.createEditorState(clause.clause);
      })
      // console.log(clauseList)
      this.props.form.setFieldsValue({
        clauses: clauseList,
      })
    }, 500)
  }
  componentWillReceiveProps(nextProps) {
    const { operation, goods } = nextProps;
    switch (operation.type) {
      case actions.GET_GOODS_CLAUSE_LIST_SELECTBYGOODSID_SUCCESS:
        // console.log(operation)
        if (goods.goodsClauseList.code == 100) {
          if (goods.goodsClauseList.result) {
            this.setState({
              goodsClauseList: goods.goodsClauseList.result
            });
          }
        } else {
          const msg = goods.goodsClauseList.msg ? goods.goodsClauseList.msg : '查询商品使用限制出错';
          message.success(msg, 10);
        }
        break;
      case actions.SAVE_GOODS_CLAUSE_LIST_SUCCESS:
        if (goods.goodsClauseList.code == 100) {
          if (goods.goodsClauseList.result) {
            this.setState({
              goodsClauseList: goods.goodsClauseList.result
            });
          }
          message.success('保存成功', 5);
        } else {
          const msg = goods.goodsClauseList.msg ? goods.goodsClauseList.msg : '更新商品详情出错';
          message.success(msg, 10);
        }
        break;
      default:
        break;
    }
  }
  // 添加保存
  handleSubmit = e => {
    e.preventDefault();
    const { data, actions } = this.props;
    const { goodsClauseList } = this.state;
    this.props.form.validateFields((err, values) => {
      if (!err) {
        // console.log("data");
        // console.log(data);
        // console.log("values");
        // console.log(values);
        // console.log(values.clauses[0].toHTML());
        goodsClauseList.map((clause, index) => {
          clause.clause = values.clauses[index].toHTML();
        })
        // console.log(goodsClauseList);
        actions.saveGoodsClauseListByGoodsId(goodsClauseList);
      }
    });
  }

  render() {
    const { getFieldDecorator } = this.props.form;
    const { goodsClauseList } = this.state;
    const edit = cookie.load("KLF_PG_GM_GL_EDIT");
    // const controls = ['bold', 'italic', 'underline', 'text-color']
    const formItemLayout = {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 4 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 20 },
      },
    };
    // console.log('render')
    // console.log(goodsClauseList)
    const formItems = goodsClauseList.map((k, index) => (
      <Form.Item
        {...(formItemLayout)}
        label={k.clauseTypeName}
        required={false}
        key={k.clauseType}
      >
        {getFieldDecorator(`clauses[${index}]`, {
        })(
          <BraftEditor
            style={{ border: 'solid 1px #ccc' }}
            placeholder="请输入"
            onChange={this.handleChange}
            media={{ uploadFn: myUploadFn, accepts: { video: false, audio: false }, externals: { video: false, audio: false, embed: false, image: false } }}
          />
        )}
      </Form.Item>
    ));
    return (
      <div className="demo-container">
        <Form onSubmit={this.handleSubmit}>
          {formItems}
          {/* <FormItem {...formItemLayout} label="使用总则">
                        {getFieldDecorator('clause1', {
                        })(
                        <BraftEditor
                            style={{border: 'solid 1px #ccc'}}
                            className="my-editor"
                            placeholder="请输入"
                        />
                        )}
                    </FormItem> */}

          <FormItem {...formItemLayout}>
            {edit && <Button size="large" type="primary" htmlType="submit">保存</Button>}
          </FormItem>
        </Form>
      </div>
    );
  }
}

export default GoodsClause;