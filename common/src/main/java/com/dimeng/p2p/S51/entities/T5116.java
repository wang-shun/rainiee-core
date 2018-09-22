package com.dimeng.p2p.S51.entities
;

import com.dimeng.framework.service.AbstractEntity;
import java.sql.Timestamp;

/** 
 * 焦点图广告
 */
public class T5116 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 广告管理自增ID
     */
    public int F01;

    /** 
     * 排序值
     */
    public int F02;

    /** 
     * 广告图片标题
     */
    public String F03;

    /** 
     * 图片链接
     */
    public String F04;

    /** 
     * 图片编码
     */
    public String F05;

    /** 
     * 创建者,参考T7011.F01
     */
    public int F06;

    /** 
     * 上架时间
     */
    public Timestamp F07;

    /** 
     * 下架时间
     */
    public Timestamp F08;

    /** 
     * 创建时间
     */
    public Timestamp F09;

    /** 
     * 最后修改时间
     */
    public Timestamp F10;

}
