package com.dimeng.p2p.S61.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6165_F02;
import java.sql.Timestamp;

/** 
 * 企业账号审核状态
 */
public class T6165 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 用户id
     */
    public int F01;

    /** 
     * 审核状态标记，I：初始，T：提交，P：审核中，R：审核拒绝，F：开户失败，K：开户中，Y：开户成功
     */
    public T6165_F02 F02;

    /** 
     * 审核描述
     */
    public String F03;

    /** 
     * 最后更新时间
     */
    public Timestamp F04;

}
