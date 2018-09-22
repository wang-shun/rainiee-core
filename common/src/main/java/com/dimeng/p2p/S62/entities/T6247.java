/*
 * 文 件 名:  T6247.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  guomianyun
 * 修改时间:  2015年3月9日
 */
package com.dimeng.p2p.S62.entities;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6247_F05;

import java.sql.Timestamp;

/**
 * 审核记录
 * <功能详细描述>
 * 
 * @author  guomianyun
 * @version  [版本号, 2015年3月9日]
 */
public class T6247 extends AbstractEntity
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 自增ID
     */
    public int F01 ;
    /**
     * 标的ID,参考T6242.F01
     */
    public int F02 ; 
    
    /**
     * 审核人,参考T7110.F01
     */
    public int F03 ; 
    /**
     * 反馈时间
     */
    public Timestamp F04; 
    
    /**
     * 状态,YCL:已处理;WCL:未处理;
     */
    public T6247_F05 F05 ;
    
    /**
     * 审核意见
     */
    public String F06 ;
    
    /**
     * 审核人名称
     */
    public String sysName;
}
