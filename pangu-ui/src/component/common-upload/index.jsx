import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { Upload, Icon, Modal, message, Form, Button } from 'antd';
import PropTypes from 'prop-types';
import _ from 'lodash';
import { UPLOAD_URL } from '../../util/url';
import { getHttpPro } from '../../util/util';
import  './index.less';
import { getFileList } from '../../util/index';
import * as commonActions from '../../store/common/action';

const uploadMessage = {
    done: '上传成功',
    error: '上传失败',
    removed: '删除成功',
    noResponse: '无效的图片地址',
};
const defaultFormItemLayout = {
    labelCol: { span: 2 },
    wrapperCol: { span: 20 },
};
const FormItem = Form.Item;
@connect(
    ({operation, common}) => ({
        operation,
        common: common.toJS()
    }),
    dispatch => ({ 
        actions: bindActionCreators(commonActions, dispatch) 
    })
)
class CommonUpload extends Component {
    state = {
        // 是否显示预览图层
        previewVisible: false,
        // 预览图片url
        previewImage: '',
    };

    handleCancel = () => this.setState({ previewVisible: false })

    handlePreview = file => {
        this.setState({
            previewImage: file.url || file.thumbUrl,
            previewVisible: true,
        });
    }
    // 取得上传文件的后缀名
    getFileSuffix = fileName => {
        const fileArr = fileName.split('.');
        return fileArr[fileArr.length - 1].toLocaleLowerCase();
    }
    /**
     * 检查文件类型
     * file obj  上传的文件信息
     * fileType [string]  上传文件类型
     */
    checkFileType = (file, fileType) => {
        const suffix = this.getFileSuffix(file.name);
        return fileType.includes(suffix);
    }
    /**
     * file obj  上传的文件信息
     * fileSize number  上传文件大小
     */
    checkFileSize = (file, fileSize) => {
        return file.size <= fileSize;
    }
    /**
     * number  上传文件数量
     * limitNum  限制数量
     */
    checkFileNum = (number, limitNum) => {
        return number <= limitNum
    }

    // 设置fileList文件属性
    setUploadFile = file => {
        const params = this.getUploadParams();
        const pgCdnNoHttpFullUrl = file.response.result.pgCdnNoHttpFullUrl;
        const pgCdnHttpUrl = file.response.result.pgCdnHttpUrl;
        const guid = file.response.result.guid;
        const ext = file.response.result.ext;
        const response = file.response.result ? file.response.result : {};
        // const url = `${pgCdnHttpUrl}/${guid}.${ext}`;
        const url = `${getHttpPro()}${pgCdnNoHttpFullUrl}`;
        if(!url){
            this.clearUploadFile(file);
            message.warning(uploadMessage.noResponse);
            return;
        }
        const listData = params.value.fileList.map(item => {
            if(item.uid === file.uid){
                item.thumbUrl = url;
                item.url = url;
                item.response = response;
            }
            return item;
        });
        params.form.setFieldsValue({
            [params.fileName]: {fileList: listData}
        });
    };

    handleChange = info => {
        const that = this;
        const status = info.file.status;
        // 获取父组件配置信息
        const params = this.props.uploadFileProps;
        // 父组件的onChange回调
        const uploadChange = params.options.onChange;
        // status = undefined,错误文件防止被批量合并setState，异步
        if(!status){
            setTimeout(() => {
                that.clearUploadFile(info.file);
            }, 0)
        }
        switch(status){
            // 上传中
            case "uploading":
                break;
            case "done": 
                // 上传成功
                if (info.file.response.code === 100) {
                    this.setUploadFile(info.file)
                    message.success(uploadMessage[status]);
                    // 把返回结果处理后加到fileList
                    let result = info.fileList.concat(getFileList([info.file.response.result]));
                    result = result.filter(item => item.uid !== info.file.uid);
                    uploadChange(result, status);
                    // uploadChange(info.file.response.result, status);
                // 上传失败
                } else {
                    setTimeout(() => {
                        this.clearUploadFile(info.file);
                    }, 0);
                    message.error('上传失败')
                    if (uploadChange) {
                        uploadChange(info.fileList, 'error');
                    }
                }
                break;
            case "error":
            case "removed":
                this.clearUploadFile(info.file)
                message.error(uploadMessage[status])
                // 配合后端删除接口
                if(info.file.id){
                    this.props.actions.deleteFile(info.file.id)
                }
                if(uploadChange){
                    uploadChange(info.fileList, status)
                }
                break;
            default:
                break;
        }
    }
    // 清除fileList文件
    clearUploadFile = file => {
        const params = this.getUploadParams();
        let info = params.value;
        const listData = !_.isEmpty(info) ? info.fileList.filter(item => item.uid !== file.uid): [];
        info.fileList = listData
        this.props.form.setFieldsValue({
            [params.fileName]: info
        })
    }
    // 获取上传表单的配置数据
    getUploadParams = () => {
        const { uploadFileProps, form, uploadedFile } = this.props;
        const  value = form.getFieldValue(uploadFileProps.fileName);
        const valueNum = !_.isEmpty(value)? (value.fileList? value.fileList.length: value.length): 0;
        // 默认展示test
        const defalut = {
            file: {},
            fileList: uploadedFile
        }
        return {
            ...uploadFileProps,
            // 当前表单值
            value: {...defalut , ...value},
            // value,
            // 当前文件数量
            valueNum,
            form: this.props.form
        }
    }
    // 取得上传组件的配置参数
    getUploadProps = () => {
        const params = this.getUploadParams();
        const that = this;
        const info = params.value|| {};
        return {
            action: UPLOAD_URL,
            data: { fileType: params.fileType},
            listType: "picture-card",
            fileList: !_.isEmpty(info) ? info.fileList: [],
            onPreview: this.handlePreview,
            onChange: this.handleChange,
            multiple: true,
            withCredentials: true,
            beforeUpload: (file, fileList) => {
                let result = true;
                // 校验上传文件类型
                if(!_.isEmpty(params.config.type)){
                    const checkType = that.checkFileType(file, params.config.type)
                    if(!checkType){
                        message.error('请不要上传不支持类型的文件');
                        result = false;
                    }
                }
                // 校验文件大小
                if(params.config.size){
                    const checkSize = that.checkFileSize(file, params.config.size);
                    if(!checkSize){
                        message.error('请不要上传超过规定大小的文件');
                        result = false;
                    }
                }
                // 校验文件数量
                if(params.config.num){
                    const checkNum = that.checkFileNum(fileList.length + params.valueNum, params.config.num);
                    if(!checkNum){
                        message.error('请不要上传超过规定数量的文件');
                        result = false;
                    }
                }
                return result;
            }
        }
    }
    // 获取上传组件dom
    getUploadComponent = () => {
        const { uploadText = '上传图片', isPicture = true } = this.props;
        // 取得上传组件的配置参数
        const props = this.getUploadProps();
        const uploadButton = 
                isPicture ?
                <div>
                    <Icon type="plus" />
                    <div className="ant-upload-text">{uploadText}</div>
                </div>:
                <Button>
                    <Icon type="upload" /> {uploadText}
                </Button>
        return (
            <Upload {...props}>
                {uploadButton}
            </Upload>
        )
    }
    render() {
        const { previewVisible, previewImage, isPicture = true } = this.state;
        const { form, formItemLayout = defaultFormItemLayout} = this.props;
        // 取得上传表单的配置
        const params = this.getUploadParams();
        // 创建上传表单
        const formProps = form.getFieldDecorator(
            params.fileName, {
                rules: !_.isEmpty(params.config.rules)? params.config.rules: [{required: false}]
            }
        );
        // 创建上传组件
        const uploadComponent = this.getUploadComponent();
        return (
            <Fragment>
                <FormItem
                    label={params.config.label}
                    {...formItemLayout}
                >
                    {
                        formProps(uploadComponent)
                    }
                </FormItem>
                {
                    isPicture &&
                    <Modal visible={previewVisible} footer={null} onCancel={this.handleCancel}>
                        <img alt="example" style={{ width: '100%' }} src={previewImage} />
                    </Modal>
                }
            </Fragment>
        );
    }
}

export default CommonUpload;

CommonUpload.propTypes = {
    // 上传组件表单
    form: PropTypes.object.isRequired,
    /** 
     *  上传组件表单配置项
     *   {
     *       fileName: '表单名',
     *       fileType: '文件类型',
     *       options: {
     *           onChange                    上传回调事件
     *       },
     *       config: {
     *           label: '',                  formItem的label名称
     *           type: ['svg'],              支持上传的文件类型
     *           size: 1024*500,             支持上传的文件大小
     *           num: 2,                     支持上传的文件数量
     *           rules： {}                  表单验证
     *       }
     *   }
    */
    uploadFileProps: PropTypes.object.isRequired,
    // 上传按钮文案
    uploadText: PropTypes.string,
    // 布局
    formItemLayout: PropTypes.object,
    // 是否是图片列表模式
    isPicture: PropTypes.bool,
    // 默认展示图片
    uploadedFile: PropTypes.array
}