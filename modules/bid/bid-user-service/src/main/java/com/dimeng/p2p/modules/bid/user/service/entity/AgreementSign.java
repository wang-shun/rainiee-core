package com.dimeng.p2p.modules.bid.user.service.entity;

import java.sql.Timestamp;

import com.dimeng.p2p.S62.enums.T6272_F06;

/**
 * 
 * 协议签署实体
 * <功能详细描述>
 * 
 * @author  pengwei
 * @version  [版本号, 2016年6月17日]
 */
public class AgreementSign {
    /**
     * 协议版本号
     */
    public int agreementVersionNum;
    
    /**
     * 协议更新时间
     */
    public Timestamp agreementUpdateTime;
    
    /**
     * 协议签署时间
     */
    public Timestamp agreementSignTime;
    
    /**
     * 协议保全ID
     */
    public String agreementSaveID;
    
    /**
     * 协议保全状态
     */
    public T6272_F06 agreementSaveState;
    
    /**
     * 协议ID T6272.F01
     */
    public int agreementId;
}
