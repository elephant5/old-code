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
const { TextArea } = Input;
const FormItem = Form.Item;
const formItemLayout = {
  labelCol: { span: 4 },
  wrapperCol: { span: 16 },
};
// 定义rem基准值
const sizeBase = 23.4375

// 定义输入转换函数
const unitImportFn = (unit, type, source) => {

  // type为单位类型，例如font-size等
  // source为输入来源，可能值为create或paste

  // 此函数的返回结果，需要过滤掉单位，只返回数值
  if (unit.indexOf('rem')) {
    return parseFloat(unit, 10) * sizeBase
  } else {
    return parseFloat(unit, 10)
  }

}

// 定义输出转换函数
const unitExportFn = (unit, type, target) => {

  if (type === 'line-height') {
    // 输出行高时不添加单位
    return unit
  }

  // target的值可能是html或者editor，对应输出到html和在编辑器中显示这两个场景
  if (target === 'html') {
    // 只在将内容输出为html时才进行转换
    return unit / sizeBase + 'rem'
  } else {
    // 在编辑器中显示时，按px单位展示
    return unit + 'px'
  }

}

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
class GoodsDetail extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      goodsDetail: {}
    };
  }
  componentWillMount() {
    const { goodsId } = this.props;
    const { goodsDetail } = this.state;
    goodsDetail.goodsId = goodsId;
    this.setState({
      goodsDetail: goodsDetail
    })
    // console.log(this.state.goodsDetail)
    this.props.actions.selectGoodsDetailByGoodsId(goodsId);
  }
  componentDidMount() {
    setTimeout(() => {
      this.props.form.setFieldsValue({
        detail: BraftEditor.createEditorState(this.state.goodsDetail.detail)
      })
    }, 500)
  }
  componentWillReceiveProps(nextProps) {
    // console.log(nextProps);
    const { operation, goods } = nextProps;
    switch (operation.type) {
      case actions.GET_GOODS_DETAIL_SELECTBYID_SUCCESS:
        // console.log(operation)
        if (goods.goodsDetail.code == 100) {
          if (goods.goodsDetail.result) {
            this.setState({
              goodsDetail: goods.goodsDetail.result
            });
          }
        } else {
          const msg = goods.goodsDetail.msg ? goods.goodsDetail.msg : '查询商品详情出错';
          message.success(msg, 10);
        }
        break;
      case actions.SAVE_GOODS_DETAIL_SUCCESS:
        if (goods.goodsDetail.code == 100) {
          if (goods.goodsDetail.result) {
            this.setState({
              goodsDetail: goods.goodsDetail.result
            });
          }
          message.success('保存成功', 10);
        } else {
          const msg = goods.goodsDetail.msg ? goods.goodsDetail.msg : '更新商品详情出错';
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
    const { goodsDetail } = this.state;
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
        goodsDetail.detail = this.state.editorState.toHTML();
        // console.log(goodsDetail)
        actions.saveGoodsDetail(goodsDetail);
      }
    });
  }

  handleChange = (editorState) => {
    this.setState({
      editorState: editorState,
    })
  }

  render() {
    const { getFieldDecorator } = this.props.form
    const { goodsDetail } = this.state
    const edit = cookie.load("KLF_PG_GM_GL_EDIT");
    // console.log('goodsDetail')
    // console.log(goodsDetail)
    // const dddd=BraftEditor.createEditorState(goodsDetail.detail);
    // console.log(goodsDetail.detail)
    return (
      <div className="demo-container">
        <Form onSubmit={this.handleSubmit}>
          <FormItem {...formItemLayout} label="商品详情">
            {getFieldDecorator('detail', {
            })(
              <BraftEditor
                style={{ border: 'solid 1px #ccc' }}
                placeholder="请输入"
                // converts={{ unitImportFn, unitExportFn }}
                onChange={this.handleChange}
                media={{ uploadFn: myUploadFn, accepts: { video: false, audio: false }, externals: { video: false, audio: false, embed: false, image: false } }}
              />
            )}
          </FormItem>
          <FormItem {...formItemLayout}>
            {edit && <Button size="large" type="primary" htmlType="submit">保存</Button>}
          </FormItem>
        </Form>
      </div>
    );
  }
}

export default GoodsDetail;