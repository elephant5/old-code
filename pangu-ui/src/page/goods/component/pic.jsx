import React, { Component } from 'react';
import '../index.less';
import CommonUpload from '../../../component/common-upload/index';
import { Form, Button } from 'antd';
import { FILE_TYPE } from '../../../config/index';
import cookie from 'react-cookies';

@Form.create()
class Pic extends Component {

    constructor(props) {
        super(props);
        this.fileList = [];
    }

    handleSubmit = e => {
        e.preventDefault();
        const { form, onEvent } = this.props;
        form.validateFields((err, values) => {
            if (!err) {
                onEvent('uploadFile', this.fileList);
            }
        })
    }
    getUploadFileProps = params => {
        return {
            fileName: params.fileName,
            fileType: 'goods.pic',
            options: {
                // 上传回调
                onChange: (info, status) => {
                    switch (status) {
                        case 'done':
                            const result = info.map(item => {
                                item.objId = this.props.goodsId;
                                return item;
                            })
                            this.fileList = result;
                            // const uploadedFile = this.props.uploadedFile.map(item => {
                            //     item.objId = this.props.goodsId;
                            //     return item;
                            // })
                            // const params = {...info, objId: this.props.props.goodsId}
                            // this.fileList = [...uploadedFile, ...this.fileList.concat(params)];
                            break;
                        case 'removed':
                            const result2 = info.map(item => {
                                item.objId = this.props.goodsId;
                                return item;
                            })
                            this.fileList = result2
                            // this.fileList = this.fileList.filter(item => info.code !== item.code)
                            break;
                        default:
                            break;
                    }
                }
            },
            config: {
                label: '',
                type: FILE_TYPE.IMAGE,
                // size: 1024*1024,
                // num: 20
            }
        }
    }
    render() {
        const form = this.props.form;
        const edit = cookie.load("KLF_PG_GM_GL_EDIT");
        return (
            <div className="modal">
                <div className="title">
                    <span>上传图片</span>
                    <span className="remark">建议宽度不小于750px，不超过1M。支持 JPG、PNG、JPEG，支持批量上传。</span>
                </div>
                <div className="content">
                    <Form onSubmit={this.handleSubmit}>
                        <CommonUpload
                            uploadedFile={this.props.uploadedFile}
                            form={form}
                            uploadFileProps={this.getUploadFileProps({ fileName: 'pic' })}
                        />
                        {edit && <Button loading={this.props.loading} type="primary" htmlType="submit">提交</Button>}
                    </Form>
                </div>
            </div>
        );
    }
}

export default Pic;


