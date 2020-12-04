package com.colourfulchina.bigan.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * User: Ryan
 * Date: 2018/8/3
 * 第三方列表
 */
@Data
@TableName("Clf_Third_List")
public class ClfThirdList extends Model<ClfThirdList> {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;//primary key

    /**
     * 第三方简称
     */
    @TableField(value = "third_list_name")
    private String thirdListName;
    /**
     * 公司名称
     */
    @TableField(value = "company_name")
    private String companyName;

    /**
     * 电话
     */
    @TableField(value = "phone")
    private String phone;
    /**
     * 传真
     */
    @TableField(value = "fax")
    private String fax;

    /**
     * 地址
     */
    @TableField(value = "address")
    private String address;
    /**
     * 第三方开拓负责人
     */
    @TableField(value = "third_open_user")
    private String thirdOpenUser;
    /**
     * 签约公司
     */
    @TableField(value = "contract_company")
    private String contractCompany;
    /**
     * 确认方式
     */
    @TableField(value = "confirm_type")
    private String confirmType;
    /**
     * 确认网站
     */
    @TableField(value = "comfairm_web")
    private String comfairmWeb;

    /**
     * 签约日期
     */
    @TableField(value = "sign_contract_date")
    private String signContractDate;

    /**
     * 合作截止
     */
    @TableField(value = "contract_end")
    private String contractEnd;
    /**
     * 垫付方式
     */
    @TableField(value = "prep_method")
    private String prepMethod;
    /**
     * 挂账额
     */
    @TableField(value = "guazhang")
    private String guazhang;

    /**
     * 押金
     */
    @TableField(value = "cash")
    private String cash;
    /**
     * 第三方联系人
     */
    @TableField(value = "third_content")
    private String thirdContent;
    /**
     * 第三方电话
     */
    @TableField(value = "third_telephone")
    private String thirdTelephone;
    /**
     * 第三方传真
     */
    @TableField(value = "third_fax")
    private String thirdFax;
    /**
     * 第三方邮箱
     */
    @TableField(value = "third_email")
    private String thirdEmail;
    /**
     * 结算方式
     */
    @TableField(value = "pay_method")
    private String payMethod;
    /**
     * 结算周期
     */
    @TableField(value = "pay_time")
    private String payTime;
    /**
     * 开户名称
     */
    @TableField(value = "account_mame")
    private String accountName;
    /**
     * 开户银行
     */
    @TableField(value = "bank_name")
    private String bankName;
    /**
     * 对方账号
     */
    @TableField(value = "other_account")
    private String otherAccount;
    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;
    /**
     * 创建人
     */
    @TableField(value = "create_user")
    private String createUser;
    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;
    /**
     * 更新人
     */
    @TableField(value = "update_user")
    private String updateUser;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
