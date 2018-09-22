package com.dimeng.p2p.S51.entities
;

import com.dimeng.framework.service.AbstractEntity;
import java.sql.Timestamp;

/** 
 * 合作伙伴
 */
public class T5113 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 合作伙伴自增ID
     */
    public int F01;

    /** 
     * 排序值
     */
    public int F02;

    /** 
     * 浏览次数
     */
    public int F03;

    /** 
     * 名称
     */
    public String F04;

    /** 
     * 链接地址
     */
    public String F05;

    /** 
     * 图片编码
     */
    public String F06;

    /** 
     * 创建者ID,参考T7011.F01
     */
    public int F07;

    /** 
     * 创建时间
     */
    public Timestamp F08;

    /** 
     * 最后更新时间
     */
    public Timestamp F09;

}
