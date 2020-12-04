package com.colourfulchina.mars.service;

import com.colourfulchina.member.api.entity.MemMemberAccount;
import com.colourfulchina.member.api.entity.MemMemberInfo;
import com.colourfulchina.member.api.req.MemAccMobileReqDTO;
import com.colourfulchina.member.api.req.MemLoginReqDTO;
import com.colourfulchina.member.api.res.MemLoginResDTO;
import com.colourfulchina.member.api.res.MemMemberAccountDTO;
import com.colourfulchina.member.api.res.MemSimpleRes;
import com.colourfulchina.member.api.res.MemberAccountInfoVo;
import com.colourfulchina.tianyan.admin.api.entity.SysDict;

import java.util.List;
import java.util.Map;

public interface MemberInterfaceService {

    /**
     * 会员ID集合获取会员信息
     * @param acidList
     * @return
     */
    public Map<Long, MemMemberAccount> getAccountList(List<Long> acidList);

    /**
     * 会员登录注册
     * @param req
     * @return
     */
    public MemLoginResDTO memberLogin(MemLoginReqDTO req);

    /**
     * 字典表的查询
     * @param type
     * @return
     */
    public    List<SysDict> selectSysDict(String type);


    /**
     * 根据手机号查询会员信息
     * @param memMemberInfo
     * @return
     */
   MemberAccountInfoVo getMember( MemMemberInfo memMemberInfo);

    /**
     * 获取用户账号信息
     * @param dto
     * @return
     */
   MemMemberAccountDTO getMemAccount(MemAccMobileReqDTO dto);

    /**
     * 获取用户会员信息
     * @param memMemberInfo
     * @return
     */
   MemMemberInfo getMemberInfo(MemMemberInfo memMemberInfo);

    /**
     * 注册会员信息
     *激活的时候如果当前激活人不存在账户系统中个，则需要注册
     * @param memMemberAccount
     * @return
     */
    public MemMemberAccount getMemberAddAccount(MemMemberAccount memMemberAccount);

    /**
     * 注册会员信息。存在则返回，不存在则注册
     * @param reqDTO
     * @return
     */
    MemMemberAccount register(MemLoginReqDTO reqDTO);

    public   SysDict selectByType( SysDict sysDict);

    /**
     * 根据memberId查询 会员信息和会员账户
     * @param memberId
     * @return
     */
    MemLoginResDTO getMemberFullInfo(Long memberId);

    /**
     * acids查询手机  姓名
     * @param acids
     * @return
     */
    List<MemSimpleRes> selectMemByAcids(List<Long> acids);
}
