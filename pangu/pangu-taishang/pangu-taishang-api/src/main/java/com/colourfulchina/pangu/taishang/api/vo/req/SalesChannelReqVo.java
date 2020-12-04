package com.colourfulchina.pangu.taishang.api.vo.req;

import com.colourfulchina.pangu.taishang.api.entity.SalesChannel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 新增和修改渠道的req vo
 *
 * @Author: luck.luo
 * @Description:
 * @Date: 2019/2/28 10:07
 */
@Data
public class SalesChannelReqVo extends SalesChannel {
    @ApiModelProperty("银行名称")
    private String bankName;
    @ApiModelProperty("销售渠道")
    private String salesChannelName;
    @ApiModelProperty("销售方式")
    private String salesWayName;
    @ApiModelProperty("银行ids")
    private List<String> bankIds;
}
