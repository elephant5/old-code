package com.colourfulchina.pangu.taishang.api.dto;


import lombok.Data;

@Data
public class TSysUser implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7262035784289996726L;

	private Long id; // 主键;

	private Long posiid; // 职位id;

	private Long departid; // 部门id;

	private Long teamid; // 团队id;

	private Integer isremind; // 是否提醒;是否提醒，0:不提醒，1：提醒

	private String lstupdatedtm; // lstupdatedtm;

	private String deptname;// 部门名称

	private String positionname; // 职位名称

	private String password; // 密码;

	private String jobtime; // 入职时间;

	private String leavetime; // 离职时间;

	private String loginname; // 用户登陆名;

	private String mobilephone; // 移动电话;

	private String area; // 地区;

	private String lastlogintime; // 上次登陆时间;

	private String createdtm; // createdtm;

	private String officephone; // 办公电话;

	private String lastloginip; // 上次登陆ip;

	private String qq; // qq;

	private String rtxcode; // 内部imo编码;

	private Integer status; // 用户状态;状态：1正常；0离职

	private String allowip; // 允许登陆ip;

	private String createusr; // createuser;

	private String lastupdateusr; // lastupdateuser;

	private Integer attr; // 员工属性;0:全职，1：兼职

	private String csid; // 客服编号;客服编号 remark:2014/6/10 本应为 Long型,为了老接口能用暂时保留

	private String pswdexpiredtime; // 密码过期时间;

	private String jobno; // 员工工号;

	private String address; // 用户地址;

	private String email; // email;

	private Integer failloginnum; // 登陆失败次数;

	private Integer loginchannel; // 登录通道;

	private String realname; // 真实姓名;

	private Integer accountlocked;// 账户锁定

	private int isdeleted = 0; // 状态;

	private String remark; // 备注;

	private Long traderid; // 加盟商id;
	private String cname; // 公司名称

	private String posiindept; // 职位所属部门

	private Integer ishead;// 1是 2 否

	private Long ehrId;// ehr系统ID

	private String ehrMD5;// ehr数据MD5值

	private String ehrUpdateTime;// ehr数据同步时间

	private String certno;// 身份证号

	private String contractcompany;// 签约公司

	private Long deptprovince;// 所属城市

	private Long deptcity;// 所属区县

	private String usedpassword;

	private Long headid;// 上级主管ID，对应原表PID

	private Integer issys;// 是否管理员

	// 上级主管名子
	private String headName;

	private String agentId;// 客服工号

	private String jobnonew; // 新员工工号;

	private Integer isPermissionAdmin; // 是否权限管理员

	private Long positionId;// 职务id

}
