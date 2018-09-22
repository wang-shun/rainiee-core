package com.dimeng.p2p.modules.account.console.service.entity;

import java.sql.Timestamp;

import com.dimeng.p2p.S61.enums.T6120_F05;

/**
 * 必要认证信息
 * @author gongliang
 *
 */
public class Attestation
{
    /**
     * 认证Id
     */
    public int id;
    
    /**
     * 项目名称
     */
    public String attestationName;
    
    /**
     * 状态(WYZ:未验证;TG:通过;BTG:不通过)
     */
    public T6120_F05 attestationState;
    
    /**
     * 认证时间
     */
    public Timestamp attestationTime;
    
    /**
     * 信用分数
     */
    public int creditGrades;
    
    /**
     * 认证附件
     */
    public String[] attachments;
    
    /**
     * 附件地址
     */
    public String[] fileUrls;
}
