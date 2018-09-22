package com.dimeng.p2p.account.user.service.entity;

import com.dimeng.p2p.S61.enums.T6120_F05;

/**
 *认证信息 
 *
 */
public class RzxxInfo
{
    /**
     * 认证项ID
     */
    public int id;
    
    /**
     * 认证状态,WYZ:未验证;TG:通过;BTG:不通过;DSH:待审核
     */
    public T6120_F05 type;
    
    /**
     * 认证类型名称
     */
    public String name;
    
    /**
     * 认证积分
     */
    public int score;
    
    /**
     * 附件编码
     */
    public String[] fileCodes;
}
