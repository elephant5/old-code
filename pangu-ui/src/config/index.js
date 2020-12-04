export const IMGPATH = 'https://cdn2.colourfulchina.com/upload';

export const CDNPATH = 'http://cdn.colourfulchina.com'; //192.168.90.1

export const week = [
    {
        code: '1',
        name: '周一'
    },
    {
        code: '2',
        name: '周二'
    },
    {
        code: '3',
        name: '周三'
    },
    {
        code: '4',
        name: '周四'
    },
    {
        code: '5',
        name: '周五'
    },
    {
        code: '6',
        name: '周六'
    },
    {
        code: '7',
        name: '周日'
    },
]

// 默认文件支持列表
export const FILE_TYPE = {
    IMAGE: ['jpeg', 'jpg', 'gif', 'png', 'bmp'],
    WORD: ['doc', 'rtf', 'docx'],
    EXCEL: ['xls','xlsx'],
    PDF: ['pdf'],
    FILE: ['txt', 'tiff', 'tif', 'rar', 'zip', '7z', 'cab', 'iso', 'svg'],
    LOGO: ['png', 'svg'],
    // All: []
};

// 文件上传类型
export const FILE_TYPE_LIST = [
    {
      "type": "hotel.file",
      "name": "酒店探索章节图片",
      "path": "/hotel/file/",
      "table": "hotel",
      "tableId": "id",
      "oldTableId": "old_hotel_id",
      "erpPrefix": "/erpfiles/",
      "pgPrefix": "/ftp_data/",
      "createTime": "2019-01-17T09:59:27.000+0000",
      "createUser": null,
      "updateTime": "2019-01-23T07:02:47.000+0000",
      "updateUser": null,
      "delFlag": 0
    },
    {
      "type": "hotel.pic",
      "name": "酒店图片",
      "path": "/hotel/pic/",
      "table": "hotel",
      "tableId": "id",
      "oldTableId": "old_hotel_id",
      "erpPrefix": "/erpfiles/",
      "pgPrefix": "/ftp_data/",
      "createTime": "2019-01-17T10:00:01.000+0000",
      "createUser": null,
      "updateTime": "2019-01-23T07:02:58.000+0000",
      "updateUser": null,
      "delFlag": 0
    },
    {
      "type": "project.file",
      "name": "项目文件",
      "path": "/project/file/",
      "table": "project",
      "tableId": "id",
      "oldTableId": null,
      "erpPrefix": "/erpfiles/",
      "pgPrefix": "/ftp_data/",
      "createTime": "2019-01-17T10:00:25.000+0000",
      "createUser": null,
      "updateTime": "2019-01-23T07:03:19.000+0000",
      "updateUser": null,
      "delFlag": 1
    },
    {
      "type": "shop.buffet",
      "name": "商户自助餐图片",
      "path": "/shop/buffet/",
      "table": "shop_item",
      "tableId": "id",
      "oldTableId": "old_item_id",
      "erpPrefix": "/erpfiles/",
      "pgPrefix": "/ftp_data/",
      "createTime": "2019-01-17T10:04:35.000+0000",
      "createUser": null,
      "updateTime": "2019-01-23T07:03:40.000+0000",
      "updateUser": null,
      "delFlag": 0
    },
    {
      "type": "shop.contract",
      "name": "商户合同",
      "path": "/shop/contract/",
      "table": "shop",
      "tableId": "id",
      "oldTableId": "old_shop_id",
      "erpPrefix": "/erpfiles/",
      "pgPrefix": "/ftp_data/",
      "createTime": "2019-01-17T10:05:48.000+0000",
      "createUser": null,
      "updateTime": "2019-01-23T07:03:51.000+0000",
      "updateUser": null,
      "delFlag": 0
    },
    {
      "type": "shop.item.contract",
      "name": "商户规格合同",
      "path": "/shop/item/contract/",
      "table": "shop_item",
      "tableId": "id",
      "oldTableId": "old_item_id",
      "erpPrefix": "/erpfiles/",
      "pgPrefix": "/ftp_data/",
      "createTime": "2019-01-17T10:07:15.000+0000",
      "createUser": null,
      "updateTime": "2019-01-23T07:03:58.000+0000",
      "updateUser": null,
      "delFlag": 0
    },
    {
      "type": "shop.menu",
      "name": "定制套餐图片",
      "path": "/shop/setmenu/",
      "table": "shop_item",
      "tableId": "id",
      "oldTableId": "old_item_id",
      "erpPrefix": "/erpfiles/",
      "pgPrefix": "/ftp_data/",
      "createTime": "2019-01-17T10:01:00.000+0000",
      "createUser": null,
      "updateTime": "2019-01-23T07:04:00.000+0000",
      "updateUser": null,
      "delFlag": 0
    },
    {
      "type": "shop.pic",
      "name": "商户图片",
      "path": "/shop/pic/",
      "table": "shop",
      "tableId": "id",
      "oldTableId": "old_shop_id",
      "erpPrefix": "/erpfiles",
      "pgPrefix": "/ftp_data/",
      "createTime": "2019-01-17T10:01:31.000+0000",
      "createUser": null,
      "updateTime": "2019-01-23T07:04:08.000+0000",
      "updateUser": null,
      "delFlag": 0
    },
    {
      "type": "ticket.file",
      "name": "工单文件",
      "path": "/ticket/file/",
      "table": "tk_ticket",
      "tableId": null,
      "oldTableId": null,
      "erpPrefix": "/erpfiles/",
      "pgPrefix": "/ftp_data/",
      "createTime": "2019-01-17T10:02:01.000+0000",
      "createUser": null,
      "updateTime": "2019-01-23T07:04:33.000+0000",
      "updateUser": null,
      "delFlag": 1
    },
    {
      "type": "ticket.reply.file",
      "name": "工单回访文件",
      "path": "/ticket/reply/file/",
      "table": "tk_ticket",
      "tableId": null,
      "oldTableId": null,
      "erpPrefix": "/erpfiles/",
      "pgPrefix": "/ftp_data/",
      "createTime": "2019-01-17T10:03:09.000+0000",
      "createUser": null,
      "updateTime": "2019-01-23T07:04:36.000+0000",
      "updateUser": null,
      "delFlag": 1
    }
  ]


export const SERVICE_LIST =  [{
  "code": "accom",
  "name": "住宿",
  "createTime": "2019-01-22T06:15:14.000+0000",
  "createUser": "init",
  "updateTime": "2019-01-22T06:15:14.000+0000",
  "updateUser": "init",
  "delFlag": 0
}, {
  "code": "buffet",
  "name": "自助餐",
  "createTime": "2019-01-22T06:15:14.000+0000",
  "createUser": "init",
  "updateTime": "2019-01-22T06:15:14.000+0000",
  "updateUser": "init",
  "delFlag": 0
}, {
  "code": "car",
  "name": "机场出行",
  "createTime": "2019-01-22T06:15:14.000+0000",
  "createUser": "init",
  "updateTime": "2019-01-22T06:15:14.000+0000",
  "updateUser": "init",
  "delFlag": 0
}, {
  "code": "drink",
  "name": "单杯茶饮",
  "createTime": "2019-01-22T06:15:14.000+0000",
  "createUser": "init",
  "updateTime": "2019-01-22T06:15:14.000+0000",
  "updateUser": "init",
  "delFlag": 0
}, {
  "code": "exchange",
  "name": "礼品兑换",
  "createTime": "2019-01-22T06:15:14.000+0000",
  "createUser": "init",
  "updateTime": "2019-01-22T06:15:14.000+0000",
  "updateUser": "init",
  "delFlag": 0
}, {
  "code": "gym",
  "name": "健身",
  "createTime": "2019-01-22T06:15:14.000+0000",
  "createUser": "init",
  "updateTime": "2019-01-22T06:15:14.000+0000",
  "updateUser": "init",
  "delFlag": 0
}, {
  "code": "lounge",
  "name": "贵宾厅",
  "createTime": "2019-01-22T06:15:14.000+0000",
  "createUser": "init",
  "updateTime": "2019-01-22T06:15:14.000+0000",
  "updateUser": "init",
  "delFlag": 0
}, {
  "code": "setmenu",
  "name": "定制套餐",
  "createTime": "2019-01-22T06:15:14.000+0000",
  "createUser": "init",
  "updateTime": "2019-01-22T06:15:14.000+0000",
  "updateUser": "init",
  "delFlag": 0
}, {
  "code": "spa",
  "name": "水疗",
  "createTime": "2019-01-22T06:15:14.000+0000",
  "createUser": "init",
  "updateTime": "2019-01-22T06:15:14.000+0000",
  "updateUser": "init",
  "delFlag": 0
}, {
  "code": "tea",
  "name": "下午茶",
  "createTime": "2019-01-22T06:15:14.000+0000",
  "createUser": "init",
  "updateTime": "2019-01-22T06:15:14.000+0000",
  "updateUser": "init",
  "delFlag": 0
}];