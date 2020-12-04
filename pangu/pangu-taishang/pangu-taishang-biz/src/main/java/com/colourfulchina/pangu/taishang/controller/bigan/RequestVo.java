package com.colourfulchina.pangu.taishang.controller.bigan;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
public class RequestVo implements Serializable {

    @NotBlank(message = "名字不能为空!")
    private String name;

    @Range(min = 0,max = 100,message = "请输入正确的年龄")
    private Integer age;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;

    @Email(message = "邮箱格式不对")
    @NotBlank(message = "邮箱不能为空")
    private String email;
}