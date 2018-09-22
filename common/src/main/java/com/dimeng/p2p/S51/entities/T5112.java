package com.dimeng.p2p.S51.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S51.enums.T5112_F03;
import java.sql.Timestamp;

/** 
 * 在线客服
 */
public class T5112 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 在线客服自增ID
     */
    public int F01;

    /** 
     * 浏览次数
     */
    public int F02;

    /** 
     * 客服类型,QQ:QQ;DH:电话;YX:邮箱
     */
    public T5112_F03 F03;

    /** 
     * 排序值
     */
    public int F04;

    /** 
     * 客服名称
     */
    public String F05;

    /** 
     * 客服号码
     */
    public String F06;

    /** 
     * 图片编码
     */
    public String F07;

    /** 
     * 创建者,参考T7110.F01
     */
    public int F08;

    /** 
     * 创建时间
     */
    public Timestamp F09;

    /** 
     * 最后更新时间
     */
    public Timestamp F10;

}
