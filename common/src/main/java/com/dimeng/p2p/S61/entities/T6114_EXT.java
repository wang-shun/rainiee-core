package com.dimeng.p2p.S61.entities;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S65.enums.T6501_F03;

import java.sql.Timestamp;

/** 
 * 用户申请更换很行卡记录
 */
public class T6114_EXT extends AbstractEntity
{
    
    private static final long serialVersionUID = 1L;
    
    /** 
     * 自增ID
     */
    public int F01;
    
    /** 
     * 用户银行卡ID-T6114
     */
    public int F02;
    
    /** 
     * 用户ID,参考T6110.F01
     */
    public int F03;
    
    
    /** 
     * 状态,DTJ:待提交;YTJ:已提交;DQR:待确认;CG:成功;SB:失败;
     */
    public T6501_F03 F04;
    
    /** 
     * 申请时间
     */
    public Timestamp F05;
    
    /** 
     * 提交时间
     */
    public Timestamp F06;
    
    /** 
     * 完成时间
     */
    public Timestamp F07;
    
    /** 
     * 流水号
     */
    public String F08;
    
    /** 
     * 备注
     */
    public String F09;
}
