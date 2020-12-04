package com.colourfulchina.mars.api.vo.req;

import com.colourfulchina.mars.api.entity.GiftCode;
import lombok.Data;

import java.util.List;

/**
 * @Author: luck.luo
 * @Description:
 * @Date: 2019/7/26 10:19
 */
@Data
public class GiftCodeReq extends GiftCode {
    private List<Integer> giftCodeIds;
    private String prolongType;
    private String prolongDate;
}
