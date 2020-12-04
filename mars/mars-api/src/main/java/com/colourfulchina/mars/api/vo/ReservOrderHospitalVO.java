package com.colourfulchina.mars.api.vo;

import com.colourfulchina.mars.api.entity.ReservOrderHospital;
import lombok.Data;

import java.util.List;

/**
 * @Author: 罗幸
 * @Description:
 * @Date: 2020/6/10 10:53
 */
@Data
public class ReservOrderHospitalVO extends ReservOrderHospital {
    //就诊人信息
    private Object memberFamily;
}
