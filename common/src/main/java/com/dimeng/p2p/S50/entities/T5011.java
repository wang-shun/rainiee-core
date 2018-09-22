package com.dimeng.p2p.S50.entities
;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S50.enums.T5011_F05;

/** 
 * 文章
 */
public class T5011 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 文章自增ID
     */
    public int F01;

    /** 
     * 文章类别id,T5010.F01
     */
    public int F02;

    /** 
     * 浏览次数
     */
    public int F03;

    /** 
     * 排序值
     */
    public int F04;

    /** 
     * 发布状态,WFB:未发布;YFB:已发布;
     */
    public T5011_F05 F05;

    /** 
     * 文章标题
     */
    public String F06;

    /** 
     * 文章来源
     */
    public String F07;

    /** 
     * 文章摘要
     */
    public String F08;

    /** 
     * 封面图片文件编码
     */
    public String F09;

    /** 
     * 创建者ID,参考T7011.F01
     */
    public int F10;

    /** 
     * 创建时间
     */
    public Timestamp F11;

    /** 
     * 发布时间
     */
    public Timestamp F12;

    /** 
     * 最后更新时间
     */
    public Timestamp F13;

}
