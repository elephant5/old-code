package com.colourfulchina.mars.api.vo.req;

import com.colourfulchina.mars.api.entity.BoscBankTxtEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class GetBoscBanksReqVo {

    private List<BoscBankTxtEntity> list;

    @ApiModelProperty(value = "条件类型:1.获取数据库中不存在的数据;2.获取数据库中存在,但是上海银行当天有效数据中不存在的数据；3：以前被冻结账户再次出现在上海银行当天有效数据中的数据")
    private Integer type;
}
