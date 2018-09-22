package com.dimeng.p2p.S61.entities;

import java.sql.Date;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6141_F03;
import com.dimeng.p2p.S61.enums.T6141_F04;
import com.dimeng.p2p.S61.enums.T6141_F09;

/** 
 * 个人基础信息
 */
public class T6141 extends AbstractEntity
{
    
    private static final long serialVersionUID = 1L;
    
    /** 
     * 登录账号ID
     */
    public int F01;
    
    /** 
     * 姓名
     */
    public String F02;
    
    /** 
     * 兴趣类型,LC:理财;JK:借款
     */
    public T6141_F03 F03;
    
    /** 
     * 实名认证,TG:通过;BTG:不通过;
     */
    public T6141_F04 F04;
    
    /** 
     * 用户头像文件编码
     */
    public String F05;
    
    /** 
     * 身份证号,3-18位星号替换
     */
    public String F06;
    
    /** 
     * 身份证号,加密存储,唯一
     */
    public String F07;
    
    /** 
     * 出身日期
     */
    public Date F08;
    
    /** 
     * 性别
     */
    public T6141_F09 F09;
    
}
