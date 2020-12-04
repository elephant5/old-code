import React, { Component } from 'react';
import '../index.less';
import CommonUploadLogo from '../../../component/common-upload-logo/index';
import { Form, Button, Modal,Input } from 'antd';
import { FILE_TYPE } from '../../../config/index';

@Form.create()
class UploadLogo extends Component {

    constructor(props){
        super(props);
        this.fileList = [];
        this.state ={
            bankLogo: {},
        }
    }

    handleSubmit = e => {
        e.preventDefault();
        const { form, onEvent } = this.props;
        form.validateFields((err, values) => {
            if (!err) {
                // console.info(values)
                // console.info(this.fileList)
                values.sysFileDto=this.fileList[0];
                // console.info(values)
                onEvent('uploadLogo', values);
            }
        })
    }
    getUploadFileProps = params => {
        return {
            fileName: params.fileName,
            fileType: 'bank.logo',
            options: {
                // 上传回调
                onChange: (info, status) => {
                    switch(status){
                        case 'done':
                            const result = info.map(item => {
                                // item.objId = this.props.goodsId;
                                return item;
                            })
                            this.fileList = result;
                            break;
                        case 'removed':
                                const result2 = info.map(item => {
                                    return item;
                                })
                                this.fileList = result2
                            break;
                        default:
                            break;
                    }
                }
            },
            config: {
                label: '',
                type: FILE_TYPE.LOGO,
                num: 1,
                multiple: false,
                rules:[{required:true,message:'请上传Logo'}]
            }
        }
    }
    render() {
        const form = this.props.form;
        const { getFieldDecorator } = this.props.form;
        const { onCancel } = this.props;
        const { bankLogo } = this.state;
        return (
            <Modal
                title="上传Logo"
                visible={true}
                cancelText="取消"
                okText="确定"
                onCancel={onCancel}
                onOk={this.handleSubmit}
            >
                <div className="modal">
                    <div className="title">
                        {/* <span>上传Logo</span> */}
                        <span className="remark">建议宽度不小于750px，不超过1M。支持 PNG、SVG，支持批量上传。</span>
                    </div>
                    <div className="content">
                        <Form onSubmit={this.handleSubmit}>
                            <Form.Item   style = {{marginTop : '20px' }} label="Logo名称：" labelCol={{ span: 5 }} wrapperCol={{ span: 10 }}>
                                {getFieldDecorator('name', { rules: [{ required: true, message: '请输入Logo名称' }],initialValue: bankLogo.name })(
                                    <Input  maxLength={50} allowClear/>
                                )}
                                {getFieldDecorator('id', {initialValue: bankLogo.id})(
                                    <Input  style={{ display:'none' }} /> 
                                        
                                )}
                            </Form.Item>
                            <CommonUploadLogo
                                uploadedFile={this.props.uploadedFile}
                                form={form}
                                uploadFileProps={this.getUploadFileProps({fileName: 'logo'})}
                            />
                        </Form>
                    </div>
                </div>
            </Modal>
        );
    }
}

export default UploadLogo;


