package com.colourfulchina.mars;

import com.alibaba.fastjson.JSONObject;
import com.colourfulchina.mars.api.vo.BoscCheckCardVo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestMain {

    public static void main(String args[]) {

        BoscCheckCardVo boscCheckCardVo = new BoscCheckCardVo();
        StringBuffer bf  = new StringBuffer("{");
        bf.append("\"MsgType\"").append(":\"").append(boscCheckCardVo.getMsgType()).append("\",");
        bf.append("\"AcctNo\"").append(":\"").append(boscCheckCardVo.getAcctNo()).append("\",");
        bf.append("\"ProCode\"").append(":\"").append(boscCheckCardVo.getProCode()).append("\",");
        bf.append("\"TranTime\"").append(":\"").append(boscCheckCardVo.getTranTime()).append("\",");
        bf.append("\"SysNo\"").append(":\"").append(boscCheckCardVo.getSysNo()).append("\",");
        bf.append("\"LocalTime\"").append(":\"").append(boscCheckCardVo.getLocalTime()).append("\",");
        bf.append("\"LocalDate\"").append(":\"").append(boscCheckCardVo.getLocalDate()).append("\",");
        bf.append("\"EcifNo\"").append(":\"").append(boscCheckCardVo.getEcifNo()).append("\",");
        bf.append("\"CardNo\"").append(":\"").append(boscCheckCardVo.getCardNo()).append("\",");
        bf.append("\"SerNo\"").append(":\"").append(boscCheckCardVo.getSerNo()).append("\"}");
        log.info("检查卡状态返回结果：{}", bf.toString());
    }
}
