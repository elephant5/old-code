package com.colourfulchina.mars.service.impl;

import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.service.MemberInterfaceService;
import com.colourfulchina.member.api.entity.MemMemberAccount;
import com.colourfulchina.member.api.entity.MemMemberInfo;
import com.colourfulchina.member.api.feign.RemoteLoginService;
import com.colourfulchina.member.api.feign.RemoteMemberAccountService;
import com.colourfulchina.member.api.feign.RemoteMemberInfoServcie;
import com.colourfulchina.member.api.req.MemAccMobileReqDTO;
import com.colourfulchina.member.api.req.MemLoginReqDTO;
import com.colourfulchina.member.api.res.MemLoginResDTO;
import com.colourfulchina.member.api.res.MemMemberAccountDTO;
import com.colourfulchina.member.api.res.MemSimpleRes;
import com.colourfulchina.member.api.res.MemberAccountInfoVo;
import com.colourfulchina.tianyan.admin.api.entity.SysDict;
import com.colourfulchina.tianyan.admin.api.feign.RemoteDictService;
import com.colourfulchina.tianyan.common.core.util.R;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 会员信息调用类
 */
@Service
@Slf4j
@AllArgsConstructor
public class MemberInterfaceServiceImpl implements MemberInterfaceService {

    RemoteMemberAccountService remoteMemberAccountService;

    RemoteMemberInfoServcie remoteMemberInfoServcie;

    RemoteLoginService loginService;

    RemoteDictService remoteDictService;

    RemoteLoginService remoteLoginService;

    /**
     * 会员ID集合获取会员信息
     * @param acidList
     * @return
     */
    public Map<Long, MemMemberAccount> getAccountList(List<Long> acidList){
        if(acidList.isEmpty()){
            return null;
        }
//        List<Long> ids = Arrays.asList( acidList.toArray(new Long[acidList.size()]));
        List<MemMemberAccount> memberAccounts = remoteMemberAccountService.getAccountList(acidList).getResult();
        Map<Long, MemMemberAccount> memberAccountMap = memberAccounts.stream().collect(Collectors.toMap(MemMemberAccount::getAcid, bank -> bank));
        return memberAccountMap;
    }

    /**
     * mobile
     * acChannel
     * smsCode = 'colour'
     * @param req
     * @return
     */
    @Override
    public MemLoginResDTO memberLogin(MemLoginReqDTO req){
        CommonResultVo<MemLoginResDTO> result = null;
        while (true){
            try {
                result =  loginService.memberLogin(req);
                if(result == null){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }else{
                    break;
                }
            } catch (Exception e) {
                log.error(e.getMessage(),e);
            }
        }
            return result.getResult() == null?null: result.getResult();
    }

    /**
     * 字典表的查询
     * @param type
     * @return
     */
    public    List<SysDict> selectSysDict(String type)  {
        R<List<SysDict>> resultVO = remoteDictService.findDictByType(type);

        return resultVO.getData();
    }


    /**
     * 字典表的查询
     * @param sysDict
     * @return
     */
    @Override
    public   SysDict selectByType( SysDict sysDict)  {
        R<SysDict> resultVO = remoteDictService.selectByType(sysDict);

        return resultVO.getData();
    }

    @Override
    public MemLoginResDTO getMemberFullInfo(Long memberId) {
        CommonResultVo<MemLoginResDTO> result = remoteMemberAccountService.getMemberFullInfo(memberId);
        return result.getResult();
    }

    @Override
    public List<MemSimpleRes> selectMemByAcids(List<Long> acids) {
        CommonResultVo<List<MemSimpleRes>> result = remoteMemberAccountService.selectMemByAcids(acids);
        return result.getResult();
    }

    /**
     * 根据手机号查询会员信息
     *
     * @param memMemberInfo
     * @return
     */
    @Override
    public MemberAccountInfoVo getMember(MemMemberInfo memMemberInfo) {
        CommonResultVo<MemberAccountInfoVo> result =  remoteMemberInfoServcie.getMember(memMemberInfo);
//        Assert.isTrue(result != null && result.getCode() == 100, "根据id获取酒店信息失败");
        return result == null? null : result.getResult();
    }

    /**
     * 获取用户账户信息
     * @param dto
     * @return
     */
    @Override
    public MemMemberAccountDTO getMemAccount(MemAccMobileReqDTO dto) {
        return remoteMemberAccountService.getMemAccount(dto).getResult();
    }

    /**
     * 获取用户会员信息
     * @param memMemberInfo
     * @return
     */
    @Override
    public MemMemberInfo getMemberInfo(MemMemberInfo memMemberInfo) {
        return remoteMemberInfoServcie.getMemberInfo(memMemberInfo).getResult();
    }

    /**
     * 注册会员信息
     *激活的时候如果当前激活人不存在账户系统中个，则需要注册
     * @param memMemberAccount
     * @return
     */
    @Override
    public MemMemberAccount getMemberAddAccount(MemMemberAccount memMemberAccount) {
         remoteMemberAccountService.addMemAccount(memMemberAccount);
         return memMemberAccount;
    }

    /**
     * 注册会员信息。存在则返回，不存在则注册
     * @param reqDTO
     * @return
     */
    @Override
    public MemMemberAccount register(MemLoginReqDTO reqDTO) {
        return remoteLoginService.register(reqDTO).getResult();
    }
}
