package com.colourfulchina.pangu.taishang.service;


import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.pangu.taishang.api.entity.OutlineRefundInfo;
import com.colourfulchina.pangu.taishang.api.vo.req.OutlineRefundInfoReq;
import com.colourfulchina.pangu.taishang.api.vo.res.OutlineRefundInfoRes;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface OutlineRefundInfoService extends IService<OutlineRefundInfo> {

    Page<OutlineRefundInfoRes> getOutlineRefundInfoByImport(List<OutlineRefundInfoReq> list);

    Boolean exportOutlineRefundInfo(List<OutlineRefundInfoReq> outlineRefundInfos, HttpServletResponse response);

    Boolean updateRefundInfo(List<OutlineRefundInfoReq> outlineRefundInfoVos);

    Page<OutlineRefundInfoRes> queryRefundInfoByConditioin(OutlineRefundInfoReq outlineRefundInfo);
}
