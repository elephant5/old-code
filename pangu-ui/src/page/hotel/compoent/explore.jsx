import React, { Component, Fragment } from 'react';
import { List, Avatar, Icon, Button, Form, Input, Popconfirm } from 'antd';
import { FILE_TYPE } from '../../../config/index';
import '../index.less';
import CommonUpload from '../../../component/common-upload/index';
import _ from 'lodash';
import { getHttpPro } from '../../../util/util';
import cookie from 'react-cookies';

const { TextArea } = Input;

const formItemLayout = {
    labelCol: { span: 4 },
    wrapperCol: { span: 16 },
};
@Form.create()
class Explore extends Component {
    constructor(props) {
        super(props);
        this.state = {
            // 添加章节按钮展示
            isAdd: true,
            // 编辑某条对应id
            editId: null,
        };
        // 添加,编辑
        this.fileList = [];
    }
    // 添加章节
    add = () => {
        this.setState({
            isAdd: false
        })
    }
    // 编辑
    edit = params => {
        this.setState({
            editId: params.hotelPortal.id
        })
    }
    // 编辑保存
    editSubmit = e => {
        e.preventDefault();
        const { data, onEvent } = this.props;
        const hotelId = data[0].hotelPortal.hotelId;
        this.props.form.validateFields((err, values) => {
            if (!err) {
                const { title, content } = values;
                this.setState({
                    editId: null
                })
                //编辑传hotelId，id
                onEvent('edit', {
                    title,
                    content,
                    hotelId,
                    id: this.state.editId,
                    fileList: this.fileList
                })
                //   编辑成功，清空
                this.fileList = [];
            }
        });
    }
    // 添加保存
    addSubmit = e => {
        e.preventDefault();
        const { data, onEvent } = this.props;
        console.log(data);
        const hotelId = data[0].hotelPortal.hotelId || 1;
        this.props.form.validateFields((err, values) => {
            if (!err) {
                this.setState({
                    isAdd: true
                })
                const { title, content } = values;
                onEvent('edit', {
                    title,
                    content,
                    hotelId,
                    fileList: this.fileList
                })
                //   保存成功，清空
                this.fileList = [];
            }
        });
    }

    getUploadFileProps = params => {
        return {
            fileName: params.fileName,
            fileType: 'hotel.file',
            options: {
                // 上传回调
                onChange: (info, status) => {
                    switch (status) {
                        case 'done':
                            // this.fileList = this.fileList.concat(info);
                            // const result = info.map(item => {
                            //     item.objId = this.props.data.shop.id;
                            //     return item;
                            // })
                            this.fileList = info;
                            break;
                        case 'removed':
                            this.fileList = info;
                            // this.fileList = this.fileList.filter(item => item.code !== info.code);
                            break;
                        default:
                            break;
                    }
                }
            },
            config: {
                label: '图片',
                type: FILE_TYPE.IMAGE,
                // size: 1024*1024,
                // num: 20
            }
        }
    }
    /**
     * 编辑，添加章节dom
     * params ? 编辑 ：添加
     */
    getEditDom = params => {
        const { getFieldDecorator } = this.props.form;
        const { onEvent, form } = this.props;
        const edit = cookie.load("KLF_PG_RM_HL_EDIT");
        return (
            <Fragment>
                <div className="c-title" style={{ borderBottom: 'none' }}>
                    {params ? '编辑' : '添加章节'}
                </div>
                <Form onSubmit={params ? this.editSubmit : this.addSubmit}>
                    <Form.Item>
                        <CommonUpload
                            uploadedFile={this.props.uploadedFile}
                            form={form}
                            formItemLayout={formItemLayout}
                            uploadFileProps={this.getUploadFileProps({ fileName: 'pic' })}
                        />
                    </Form.Item>
                    <Form.Item
                        label="章节标题："
                        {...formItemLayout}
                    >
                        {getFieldDecorator('title', {
                            initialValue: params ? params.hotelPortal.title : ''
                        })(
                            <Input placeholder="请输入章节标题" />
                        )}
                    </Form.Item>
                    <Form.Item
                        label="章节描述："
                        {...formItemLayout}
                    >
                        {getFieldDecorator('content', {
                            initialValue: params ? params.hotelPortal.content : ''
                        })(
                            <TextArea placeholder="请输入章节描述" />
                        )}
                    </Form.Item>
                    <Form.Item
                        wrapperCol={{
                            span: 8, offset: 4
                        }}
                    >
                        {edit && <Button
                            type="primary"
                            htmlType="submit"
                        >
                            保存
                        </Button>}
                        {edit && <Button
                            style={{ marginLeft: 10 }}
                            onClick={params ? () => onEvent('delete', {
                                id: params.hotelPortal.id,
                                hotelId: params.hotelPortal.hotelId
                            }) :
                                () => {
                                    this.setState({
                                        isAdd: true
                                    })
                                }
                            }
                        >删除</Button>}
                    </Form.Item>
                </Form>
            </Fragment>
        )
    }

    render() {
        const { data, onEvent, loading } = this.props;
        const { isAdd, editId } = this.state;
        const edit = cookie.load("KLF_PG_RM_HL_EDIT");
        return (
            <div className="c-modal">
                <div className="c-title">酒店探索</div>
                <List
                    itemLayout="horizontal"
                    dataSource={data}
                    loading={loading}
                    renderItem={item => {
                        if (editId === item.hotelPortal.id) {
                            return this.getEditDom(item)
                        }
                        return (
                            <List.Item actions={ edit &&
                                [
                                    <span className="c-color-blue" onClick={() => this.edit(item)}>编辑</span>,
                                    <Popconfirm
                                        placement="left" title='确定要删除吗？'
                                        onConfirm={() => onEvent('delete', {
                                            id: item.hotelPortal.id,
                                            hotelId: item.hotelPortal.hotelId
                                        })
                                        }
                                        okText="Yes"
                                        cancelText="No"
                                    >
                                        <span className="c-color-blue">删除</span>
                                    </Popconfirm>
                                ]
                            }>
                                <List.Item.Meta
                                    avatar={<Avatar src={!_.isEmpty(item.sysFileDtoList) ? `${getHttpPro()}${item.sysFileDtoList[0].pgCdnNoHttpFullUrl}` : ''} />}
                                    title={item.hotelPortal.title}
                                    description={<div className="ellipsis2">{item.hotelPortal.content}</div>}
                                />
                            </List.Item>
                        )
                    }}
                />
                {edit &&
                    (isAdd ?
                        <Button type="dashed" style={{ margin: '24px 0' }} block onClick={this.add}>
                            <Icon type="plus" /> 添加章节
                        </Button>
                        :
                        this.getEditDom())
                }
            </div>
        );
    }
}

export default Explore;

