package com.colourfulchina.mars.api.vo.res;

import com.colourfulchina.mars.api.entity.GiftCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SendGiftCodeResVo implements Serializable{


    private static final long serialVersionUID = -9207501825290120153L;

    @ApiModelProperty(value = "行权短链")
    private String giftUrl;

    @ApiModelProperty(value = "激活码到期时间:项目配成发送激活码就开始算过期时间的,同一项目的同一批激活过期时间一样")
    private String actExpireTime;

    private List<GiftCode> codeList;
}
