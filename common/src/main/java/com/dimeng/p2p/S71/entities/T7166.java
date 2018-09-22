package com.dimeng.p2p.S71.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S71.enums.T7166_F03;
import com.dimeng.p2p.S71.enums.T7166_F04;
import com.dimeng.p2p.S71.enums.T7166_F07;
import java.sql.Date;
import java.sql.Timestamp;

/** 
 * 客户关怀
 */
public class T7166 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 关怀ID，自增
     */
    public int F01;

    /** 
     * 预留字段
     */
    public int F02;

    /** 
     * 关怀类型,SR:生日;JR:节日
     */
    public T7166_F03 F03;

    /** 
     * 关怀形式:DX:短信;YJ:邮件;ZNX:站内信;
     */
    public T7166_F04 F04;

    /** 
     * 标题
     */
    public String F05;

    /** 
     * 内容
     */
    public String F06;

    /** 
     * 状态:QY:启用;TY:停用;
     */
    public T7166_F07 F07;

    /** 
     * 创建人
     */
    public int F08;

    /** 
     * 发送日期
     */
    public Date F09;

    /** 
     * 创建时间
     */
    public Timestamp F10;

}
